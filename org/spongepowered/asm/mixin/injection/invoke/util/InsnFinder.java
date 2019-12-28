package org.spongepowered.asm.mixin.injection.invoke.util;

import org.spongepowered.asm.lib.tree.analysis.Value;
import org.spongepowered.asm.lib.tree.analysis.Frame;
import org.spongepowered.asm.lib.tree.analysis.Interpreter;
import org.spongepowered.asm.lib.tree.analysis.BasicInterpreter;
import org.spongepowered.asm.lib.tree.analysis.BasicValue;
import org.spongepowered.asm.lib.tree.analysis.Analyzer;
import org.apache.logging.log4j.LogManager;
import org.spongepowered.asm.lib.tree.analysis.AnalyzerException;
import org.spongepowered.asm.lib.tree.AbstractInsnNode;
import org.spongepowered.asm.mixin.injection.struct.Target;
import org.apache.logging.log4j.Logger;

public class InsnFinder
{
    private static final Logger logger;
    
    public AbstractInsnNode findPopInsn(final Target v1, final AbstractInsnNode v2) {
        try {
            /*SL:164*/new PopAnalyzer(v2).analyze(v1.classNode.name, v1.method);
        }
        catch (AnalyzerException a1) {
            /*SL:166*/if (a1.getCause() instanceof AnalysisResultException) {
                /*SL:167*/return ((AnalysisResultException)a1.getCause()).getResult();
            }
            InsnFinder.logger.catching(/*EL:169*/(Throwable)a1);
        }
        /*SL:171*/return null;
    }
    
    static {
        logger = LogManager.getLogger("mixin");
    }
    
    static class AnalysisResultException extends RuntimeException
    {
        private static final long serialVersionUID = 1L;
        private AbstractInsnNode result;
        
        public AnalysisResultException(final AbstractInsnNode a1) {
            this.result = a1;
        }
        
        public AbstractInsnNode getResult() {
            /*SL:57*/return this.result;
        }
    }
    
    enum AnalyzerState
    {
        SEARCH, 
        ANALYSE, 
        COMPLETE;
    }
    
    static class PopAnalyzer extends Analyzer<BasicValue>
    {
        protected final AbstractInsnNode node;
        
        public PopAnalyzer(final AbstractInsnNode a1) {
            super(new BasicInterpreter());
            this.node = a1;
        }
        
        @Override
        protected Frame<BasicValue> newFrame(final int a1, final int a2) {
            /*SL:145*/return new PopFrame(a1, a2);
        }
        
        class PopFrame extends Frame<BasicValue>
        {
            private AbstractInsnNode current;
            private AnalyzerState state;
            private int depth;
            
            public PopFrame(final int a2, final int a3) {
                super(a2, a3);
                this.state = AnalyzerState.SEARCH;
                this.depth = 0;
            }
            
            @Override
            public void execute(final AbstractInsnNode a1, final Interpreter<BasicValue> a2) throws AnalyzerException {
                /*SL:109*/super.execute(this.current = a1, a2);
            }
            
            @Override
            public void push(final BasicValue a1) throws IndexOutOfBoundsException {
                /*SL:114*/if (this.current == PopAnalyzer.this.node && this.state == AnalyzerState.SEARCH) {
                    /*SL:115*/this.state = AnalyzerState.ANALYSE;
                    /*SL:116*/++this.depth;
                }
                else/*SL:117*/ if (this.state == AnalyzerState.ANALYSE) {
                    /*SL:118*/++this.depth;
                }
                /*SL:120*/super.push((Value)a1);
            }
            
            @Override
            public BasicValue pop() throws IndexOutOfBoundsException {
                /*SL:125*/if (this.state == AnalyzerState.ANALYSE && /*EL:126*/--this.depth == 0) {
                    /*SL:127*/this.state = AnalyzerState.COMPLETE;
                    /*SL:128*/throw new AnalysisResultException(this.current);
                }
                /*SL:131*/return (BasicValue)super.pop();
            }
        }
    }
}

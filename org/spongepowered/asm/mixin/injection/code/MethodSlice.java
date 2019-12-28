package org.spongepowered.asm.mixin.injection.code;

import java.util.NoSuchElementException;
import java.util.ListIterator;
import org.apache.logging.log4j.LogManager;
import org.spongepowered.asm.util.Annotations;
import org.spongepowered.asm.lib.tree.AnnotationNode;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.IInjectionPointContext;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.util.Bytecode;
import java.util.Deque;
import org.spongepowered.asm.mixin.MixinEnvironment;
import java.util.Collection;
import org.spongepowered.asm.lib.tree.InsnList;
import org.spongepowered.asm.lib.tree.AbstractInsnNode;
import java.util.LinkedList;
import org.spongepowered.asm.mixin.injection.throwables.InjectionError;
import org.spongepowered.asm.lib.tree.MethodNode;
import com.google.common.base.Strings;
import org.spongepowered.asm.mixin.injection.throwables.InvalidSliceException;
import org.spongepowered.asm.mixin.injection.InjectionPoint;
import org.apache.logging.log4j.Logger;

public final class MethodSlice
{
    private static final Logger logger;
    private final ISliceContext owner;
    private final String id;
    private final InjectionPoint from;
    private final InjectionPoint to;
    private final String name;
    
    private MethodSlice(final ISliceContext a1, final String a2, final InjectionPoint a3, final InjectionPoint a4) {
        if (a3 == null && a4 == null) {
            throw new InvalidSliceException(a1, String.format("%s is redundant. No 'from' or 'to' value specified", this));
        }
        this.owner = a1;
        this.id = Strings.nullToEmpty(a2);
        this.from = a3;
        this.to = a4;
        this.name = getSliceName(a2);
    }
    
    public String getId() {
        /*SL:353*/return this.id;
    }
    
    public ReadOnlyInsnList getSlice(final MethodNode a1) {
        final int v1 = /*EL:363*/a1.instructions.size() - 1;
        final int v2 = /*EL:364*/this.find(a1, this.from, 0, 0, this.name + "(from)");
        final int v3 = /*EL:365*/this.find(a1, this.to, v1, v2, this.name + "(to)");
        /*SL:367*/if (v2 > v3) {
            /*SL:368*/throw new InvalidSliceException(this.owner, String.format("%s is negative size. Range(%d -> %d)", this.describe(), v2, v3));
        }
        /*SL:371*/if (v2 < 0 || v3 < 0 || v2 > v1 || v3 > v1) {
            /*SL:372*/throw new InjectionError("Unexpected critical error in " + this + ": out of bounds start=" + v2 + " end=" + v3 + " lim=" + v1);
        }
        /*SL:375*/if (v2 == 0 && v3 == v1) {
            /*SL:376*/return new ReadOnlyInsnList(a1.instructions);
        }
        /*SL:379*/return new InsnListSlice(a1.instructions, v2, v3);
    }
    
    private int find(final MethodNode a1, final InjectionPoint a2, final int a3, final int a4, final String a5) {
        /*SL:396*/if (a2 == null) {
            /*SL:397*/return a3;
        }
        final Deque<AbstractInsnNode> v1 = /*EL:400*/new LinkedList<AbstractInsnNode>();
        final ReadOnlyInsnList v2 = /*EL:401*/new ReadOnlyInsnList(a1.instructions);
        final boolean v3 = /*EL:402*/a2.find(a1.desc, v2, v1);
        final InjectionPoint.Selector v4 = /*EL:403*/a2.getSelector();
        /*SL:404*/if (v1.size() != 1 && v4 == InjectionPoint.Selector.ONE) {
            /*SL:405*/throw new InvalidSliceException(this.owner, String.format("%s requires 1 result but found %d", this.describe(a5), v1.size()));
        }
        /*SL:408*/if (!v3) {
            /*SL:409*/if (this.owner.getContext().getOption(MixinEnvironment.Option.DEBUG_VERBOSE)) {
                MethodSlice.logger.warn(/*EL:410*/"{} did not match any instructions", new Object[] { this.describe(a5) });
            }
            /*SL:412*/return a4;
        }
        /*SL:415*/return a1.instructions.indexOf((v4 == InjectionPoint.Selector.FIRST) ? v1.getFirst() : v1.getLast());
    }
    
    @Override
    public String toString() {
        /*SL:423*/return this.describe();
    }
    
    private String describe() {
        /*SL:427*/return this.describe(this.name);
    }
    
    private String describe(final String a1) {
        /*SL:431*/return describeSlice(a1, this.owner);
    }
    
    private static String describeSlice(final String a1, final ISliceContext a2) {
        final String v1 = /*EL:435*/Bytecode.getSimpleName(a2.getAnnotation());
        final MethodNode v2 = /*EL:436*/a2.getMethod();
        /*SL:437*/return String.format("%s->%s(%s)::%s%s", a2.getContext(), v1, a1, v2.name, v2.desc);
    }
    
    private static String getSliceName(final String a1) {
        /*SL:441*/return String.format("@Slice[%s]", Strings.nullToEmpty(a1));
    }
    
    public static MethodSlice parse(final ISliceContext a1, final Slice a2) {
        final String v1 = /*EL:452*/a2.id();
        final At v2 = /*EL:454*/a2.from();
        final At v3 = /*EL:455*/a2.to();
        final InjectionPoint v4 = /*EL:457*/(v2 != null) ? InjectionPoint.parse(a1, v2) : null;
        final InjectionPoint v5 = /*EL:458*/(v3 != null) ? InjectionPoint.parse(a1, v3) : null;
        /*SL:460*/return new MethodSlice(a1, v1, v4, v5);
    }
    
    public static MethodSlice parse(final ISliceContext a1, final AnnotationNode a2) {
        final String v1 = /*EL:471*/Annotations.<String>getValue(a2, "id");
        final AnnotationNode v2 = /*EL:473*/Annotations.<AnnotationNode>getValue(a2, "from");
        final AnnotationNode v3 = /*EL:474*/Annotations.<AnnotationNode>getValue(a2, "to");
        final InjectionPoint v4 = /*EL:476*/(v2 != null) ? InjectionPoint.parse(a1, v2) : null;
        final InjectionPoint v5 = /*EL:477*/(v3 != null) ? InjectionPoint.parse(a1, v3) : null;
        /*SL:479*/return new MethodSlice(a1, v1, v4, v5);
    }
    
    static {
        logger = LogManager.getLogger("mixin");
    }
    
    static final class InsnListSlice extends ReadOnlyInsnList
    {
        private final int start;
        private final int end;
        
        protected InsnListSlice(final InsnList a1, final int a2, final int a3) {
            super(a1);
            this.start = a2;
            this.end = a3;
        }
        
        @Override
        public ListIterator<AbstractInsnNode> iterator() {
            /*SL:195*/return this.iterator(0);
        }
        
        @Override
        public ListIterator<AbstractInsnNode> iterator(final int a1) {
            /*SL:205*/return new SliceIterator(super.iterator(this.start + a1), this.start, this.end, this.start + a1);
        }
        
        @Override
        public AbstractInsnNode[] toArray() {
            final AbstractInsnNode[] v1 = /*EL:214*/super.toArray();
            final AbstractInsnNode[] v2 = /*EL:215*/new AbstractInsnNode[this.size()];
            /*SL:216*/System.arraycopy(v1, this.start, v2, 0, v2.length);
            /*SL:217*/return v2;
        }
        
        @Override
        public int size() {
            /*SL:226*/return this.end - this.start + 1;
        }
        
        @Override
        public AbstractInsnNode getFirst() {
            /*SL:235*/return super.get(this.start);
        }
        
        @Override
        public AbstractInsnNode getLast() {
            /*SL:244*/return super.get(this.end);
        }
        
        @Override
        public AbstractInsnNode get(final int a1) {
            /*SL:253*/return super.get(this.start + a1);
        }
        
        @Override
        public boolean contains(final AbstractInsnNode v2) {
            /*SL:262*/for (final AbstractInsnNode a1 : this.toArray()) {
                /*SL:263*/if (a1 == v2) {
                    /*SL:264*/return true;
                }
            }
            /*SL:267*/return false;
        }
        
        @Override
        public int indexOf(final AbstractInsnNode a1) {
            final int v1 = /*EL:280*/super.indexOf(a1);
            /*SL:281*/return (v1 >= this.start && v1 <= this.end) ? (v1 - this.start) : -1;
        }
        
        public int realIndexOf(final AbstractInsnNode a1) {
            /*SL:291*/return super.indexOf(a1);
        }
        
        static class SliceIterator implements ListIterator<AbstractInsnNode>
        {
            private final ListIterator<AbstractInsnNode> iter;
            private int start;
            private int end;
            private int index;
            
            public SliceIterator(final ListIterator<AbstractInsnNode> a1, final int a2, final int a3, final int a4) {
                this.iter = a1;
                this.start = a2;
                this.end = a3;
                this.index = a4;
            }
            
            @Override
            public boolean hasNext() {
                /*SL:100*/return this.index <= this.end && this.iter.hasNext();
            }
            
            @Override
            public AbstractInsnNode next() {
                /*SL:108*/if (this.index > this.end) {
                    /*SL:109*/throw new NoSuchElementException();
                }
                /*SL:111*/++this.index;
                /*SL:112*/return this.iter.next();
            }
            
            @Override
            public boolean hasPrevious() {
                /*SL:120*/return this.index > this.start;
            }
            
            @Override
            public AbstractInsnNode previous() {
                /*SL:128*/if (this.index <= this.start) {
                    /*SL:129*/throw new NoSuchElementException();
                }
                /*SL:131*/--this.index;
                /*SL:132*/return this.iter.previous();
            }
            
            @Override
            public int nextIndex() {
                /*SL:140*/return this.index - this.start;
            }
            
            @Override
            public int previousIndex() {
                /*SL:148*/return this.index - this.start - 1;
            }
            
            @Override
            public void remove() {
                /*SL:156*/throw new UnsupportedOperationException("Cannot remove insn from slice");
            }
            
            @Override
            public void set(final AbstractInsnNode a1) {
                /*SL:164*/throw new UnsupportedOperationException("Cannot set insn using slice");
            }
            
            @Override
            public void add(final AbstractInsnNode a1) {
                /*SL:172*/throw new UnsupportedOperationException("Cannot add insn using slice");
            }
        }
    }
}

package org.spongepowered.asm.mixin.injection.modify;

import org.spongepowered.asm.lib.tree.InsnList;
import java.util.ListIterator;
import org.spongepowered.asm.lib.tree.VarInsnNode;
import org.spongepowered.asm.lib.tree.AbstractInsnNode;
import java.util.Collection;
import org.spongepowered.asm.mixin.injection.struct.Target;
import org.spongepowered.asm.mixin.injection.struct.InjectionPointData;
import org.spongepowered.asm.lib.Type;
import org.spongepowered.asm.mixin.injection.InjectionPoint;

@AtCode("LOAD")
public class BeforeLoadLocal extends ModifyVariableInjector.ContextualInjectionPoint
{
    private final Type returnType;
    private final LocalVariableDiscriminator discriminator;
    private final int opcode;
    private final int ordinal;
    private boolean opcodeAfter;
    
    protected BeforeLoadLocal(final InjectionPointData a1) {
        this(a1, 21, false);
    }
    
    protected BeforeLoadLocal(final InjectionPointData a1, final int a2, final boolean a3) {
        super(a1.getContext());
        this.returnType = a1.getMethodReturnType();
        this.discriminator = a1.getLocalVariableDiscriminator();
        this.opcode = a1.getOpcode(this.returnType.getOpcode(a2));
        this.ordinal = a1.getOrdinal();
        this.opcodeAfter = a3;
    }
    
    @Override
    boolean find(final Target v-3, final Collection<AbstractInsnNode> v-2) {
        final SearchState searchState = /*EL:195*/new SearchState(this.ordinal, this.discriminator.printLVT());
        /*SL:197*/for (final AbstractInsnNode v : v-3.method.instructions) {
            /*SL:200*/if (searchState.isPendingCheck()) {
                final int a1 = /*EL:201*/this.discriminator.findLocal(this.returnType, this.discriminator.isArgsOnly(), v-3, v);
                /*SL:202*/searchState.check(v-2, v, a1);
            }
            else {
                /*SL:203*/if (!(v instanceof VarInsnNode) || v.getOpcode() != this.opcode || (this.ordinal != -1 && searchState.success())) {
                    continue;
                }
                /*SL:204*/searchState.register((VarInsnNode)v);
                /*SL:205*/if (this.opcodeAfter) {
                    /*SL:206*/searchState.setPendingCheck();
                }
                else {
                    final int a2 = /*EL:208*/this.discriminator.findLocal(this.returnType, this.discriminator.isArgsOnly(), v-3, v);
                    /*SL:209*/searchState.check(v-2, v, a2);
                }
            }
        }
        /*SL:214*/return searchState.success();
    }
    
    static class SearchState
    {
        private final boolean print;
        private final int targetOrdinal;
        private int ordinal;
        private boolean pendingCheck;
        private boolean found;
        private VarInsnNode varNode;
        
        SearchState(final int a1, final boolean a2) {
            this.ordinal = 0;
            this.pendingCheck = false;
            this.found = false;
            this.targetOrdinal = a1;
            this.print = a2;
        }
        
        boolean success() {
            /*SL:120*/return this.found;
        }
        
        boolean isPendingCheck() {
            /*SL:124*/return this.pendingCheck;
        }
        
        void setPendingCheck() {
            /*SL:128*/this.pendingCheck = true;
        }
        
        void register(final VarInsnNode a1) {
            /*SL:132*/this.varNode = a1;
        }
        
        void check(final Collection<AbstractInsnNode> a1, final AbstractInsnNode a2, final int a3) {
            /*SL:136*/this.pendingCheck = false;
            /*SL:137*/if (a3 != this.varNode.var && (a3 > -2 || !this.print)) {
                /*SL:138*/return;
            }
            /*SL:141*/if (this.targetOrdinal == -1 || this.targetOrdinal == this.ordinal) {
                /*SL:142*/a1.add(a2);
                /*SL:143*/this.found = true;
            }
            /*SL:146*/++this.ordinal;
            /*SL:147*/this.varNode = null;
        }
    }
}

package org.spongepowered.asm.mixin.struct;

import org.spongepowered.asm.mixin.transformer.throwables.MixinTransformerError;
import org.spongepowered.asm.lib.Handle;
import org.spongepowered.asm.lib.tree.FieldInsnNode;
import org.spongepowered.asm.lib.tree.MethodInsnNode;
import org.spongepowered.asm.util.Bytecode;

public abstract class MemberRef
{
    private static final int[] H_OPCODES;
    
    public abstract boolean isField();
    
    public abstract int getOpcode();
    
    public abstract void setOpcode(final int p0);
    
    public abstract String getOwner();
    
    public abstract void setOwner(final String p0);
    
    public abstract String getName();
    
    public abstract void setName(final String p0);
    
    public abstract String getDesc();
    
    public abstract void setDesc(final String p0);
    
    @Override
    public String toString() {
        final String v1 = /*EL:360*/Bytecode.getOpcodeName(this.getOpcode());
        /*SL:361*/return String.format("%s for %s.%s%s%s", v1, this.getOwner(), this.getName(), this.isField() ? ":" : "", this.getDesc());
    }
    
    @Override
    public boolean equals(final Object a1) {
        /*SL:366*/if (!(a1 instanceof MemberRef)) {
            /*SL:367*/return false;
        }
        final MemberRef v1 = /*EL:370*/(MemberRef)a1;
        /*SL:371*/return this.getOpcode() == v1.getOpcode() && this.getOwner().equals(/*EL:372*/v1.getOwner()) && this.getName().equals(/*EL:373*/v1.getName()) && this.getDesc().equals(/*EL:374*/v1.getDesc());
    }
    
    @Override
    public int hashCode() {
        /*SL:379*/return this.toString().hashCode();
    }
    
    static int opcodeFromTag(final int a1) {
        /*SL:383*/return (a1 >= 0 && a1 < MemberRef.H_OPCODES.length) ? MemberRef.H_OPCODES[a1] : 0;
    }
    
    static int tagFromOpcode(final int v1) {
        /*SL:387*/for (int a1 = 1; a1 < MemberRef.H_OPCODES.length; ++a1) {
            /*SL:388*/if (MemberRef.H_OPCODES[a1] == v1) {
                /*SL:389*/return a1;
            }
        }
        /*SL:392*/return 0;
    }
    
    static {
        H_OPCODES = new int[] { 0, 180, 178, 181, 179, 182, 184, 183, 183, 185 };
    }
    
    public static final class Method extends MemberRef
    {
        private static final int OPCODES = 191;
        public final MethodInsnNode insn;
        
        public Method(final MethodInsnNode a1) {
            this.insn = a1;
        }
        
        @Override
        public boolean isField() {
            /*SL:64*/return false;
        }
        
        @Override
        public int getOpcode() {
            /*SL:69*/return this.insn.getOpcode();
        }
        
        @Override
        public void setOpcode(final int a1) {
            /*SL:74*/if ((a1 & 0xBF) == 0x0) {
                /*SL:75*/throw new IllegalArgumentException("Invalid opcode for method instruction: 0x" + Integer.toHexString(a1));
            }
            /*SL:78*/this.insn.setOpcode(a1);
        }
        
        @Override
        public String getOwner() {
            /*SL:83*/return this.insn.owner;
        }
        
        @Override
        public void setOwner(final String a1) {
            /*SL:88*/this.insn.owner = a1;
        }
        
        @Override
        public String getName() {
            /*SL:93*/return this.insn.name;
        }
        
        @Override
        public void setName(final String a1) {
            /*SL:98*/this.insn.name = a1;
        }
        
        @Override
        public String getDesc() {
            /*SL:103*/return this.insn.desc;
        }
        
        @Override
        public void setDesc(final String a1) {
            /*SL:108*/this.insn.desc = a1;
        }
    }
    
    public static final class Field extends MemberRef
    {
        private static final int OPCODES = 183;
        public final FieldInsnNode insn;
        
        public Field(final FieldInsnNode a1) {
            this.insn = a1;
        }
        
        @Override
        public boolean isField() {
            /*SL:135*/return true;
        }
        
        @Override
        public int getOpcode() {
            /*SL:140*/return this.insn.getOpcode();
        }
        
        @Override
        public void setOpcode(final int a1) {
            /*SL:145*/if ((a1 & 0xB7) == 0x0) {
                /*SL:146*/throw new IllegalArgumentException("Invalid opcode for field instruction: 0x" + Integer.toHexString(a1));
            }
            /*SL:149*/this.insn.setOpcode(a1);
        }
        
        @Override
        public String getOwner() {
            /*SL:154*/return this.insn.owner;
        }
        
        @Override
        public void setOwner(final String a1) {
            /*SL:159*/this.insn.owner = a1;
        }
        
        @Override
        public String getName() {
            /*SL:164*/return this.insn.name;
        }
        
        @Override
        public void setName(final String a1) {
            /*SL:169*/this.insn.name = a1;
        }
        
        @Override
        public String getDesc() {
            /*SL:174*/return this.insn.desc;
        }
        
        @Override
        public void setDesc(final String a1) {
            /*SL:179*/this.insn.desc = a1;
        }
    }
    
    public static final class Handle extends MemberRef
    {
        private org.spongepowered.asm.lib.Handle handle;
        
        public Handle(final org.spongepowered.asm.lib.Handle a1) {
            this.handle = a1;
        }
        
        public org.spongepowered.asm.lib.Handle getMethodHandle() {
            /*SL:207*/return this.handle;
        }
        
        @Override
        public boolean isField() {
            /*SL:212*/switch (this.handle.getTag()) {
                case 5:
                case 6:
                case 7:
                case 8:
                case 9: {
                    /*SL:218*/return false;
                }
                case 1:
                case 2:
                case 3:
                case 4: {
                    /*SL:223*/return true;
                }
                default: {
                    /*SL:225*/throw new MixinTransformerError("Invalid tag " + this.handle.getTag() + " for method handle " + this.handle + ".");
                }
            }
        }
        
        @Override
        public int getOpcode() {
            final int v1 = /*EL:231*/MemberRef.opcodeFromTag(this.handle.getTag());
            /*SL:232*/if (v1 == 0) {
                /*SL:233*/throw new MixinTransformerError("Invalid tag " + this.handle.getTag() + " for method handle " + this.handle + ".");
            }
            /*SL:235*/return v1;
        }
        
        @Override
        public void setOpcode(final int a1) {
            final int v1 = /*EL:240*/MemberRef.tagFromOpcode(a1);
            /*SL:241*/if (v1 == 0) {
                /*SL:242*/throw new MixinTransformerError("Invalid opcode " + Bytecode.getOpcodeName(a1) + " for method handle " + this.handle + ".");
            }
            final boolean v2 = /*EL:244*/v1 == 9;
            /*SL:245*/this.handle = new org.spongepowered.asm.lib.Handle(v1, this.handle.getOwner(), this.handle.getName(), this.handle.getDesc(), v2);
        }
        
        @Override
        public String getOwner() {
            /*SL:250*/return this.handle.getOwner();
        }
        
        @Override
        public void setOwner(final String a1) {
            final boolean v1 = /*EL:255*/this.handle.getTag() == 9;
            /*SL:256*/this.handle = new org.spongepowered.asm.lib.Handle(this.handle.getTag(), a1, this.handle.getName(), this.handle.getDesc(), v1);
        }
        
        @Override
        public String getName() {
            /*SL:261*/return this.handle.getName();
        }
        
        @Override
        public void setName(final String a1) {
            final boolean v1 = /*EL:266*/this.handle.getTag() == 9;
            /*SL:267*/this.handle = new org.spongepowered.asm.lib.Handle(this.handle.getTag(), this.handle.getOwner(), a1, this.handle.getDesc(), v1);
        }
        
        @Override
        public String getDesc() {
            /*SL:272*/return this.handle.getDesc();
        }
        
        @Override
        public void setDesc(final String a1) {
            final boolean v1 = /*EL:277*/this.handle.getTag() == 9;
            /*SL:278*/this.handle = new org.spongepowered.asm.lib.Handle(this.handle.getTag(), this.handle.getOwner(), this.handle.getName(), a1, v1);
        }
    }
}

package org.spongepowered.asm.lib;

public final class Handle
{
    final int tag;
    final String owner;
    final String name;
    final String desc;
    final boolean itf;
    
    public Handle(final int a1, final String a2, final String a3, final String a4) {
        this(a1, a2, a3, a4, a1 == 9);
    }
    
    public Handle(final int a1, final String a2, final String a3, final String a4, final boolean a5) {
        this.tag = a1;
        this.owner = a2;
        this.name = a3;
        this.desc = a4;
        this.itf = a5;
    }
    
    public int getTag() {
        /*SL:144*/return this.tag;
    }
    
    public String getOwner() {
        /*SL:155*/return this.owner;
    }
    
    public String getName() {
        /*SL:164*/return this.name;
    }
    
    public String getDesc() {
        /*SL:173*/return this.desc;
    }
    
    public boolean isInterface() {
        /*SL:184*/return this.itf;
    }
    
    public boolean equals(final Object a1) {
        /*SL:189*/if (a1 == this) {
            /*SL:190*/return true;
        }
        /*SL:192*/if (!(a1 instanceof Handle)) {
            /*SL:193*/return false;
        }
        final Handle v1 = /*EL:195*/(Handle)a1;
        /*SL:196*/return this.tag == v1.tag && this.itf == v1.itf && this.owner.equals(v1.owner) && this.name.equals(v1.name) && /*EL:197*/this.desc.equals(v1.desc);
    }
    
    public int hashCode() {
        /*SL:202*/return this.tag + (this.itf ? 64 : 0) + this.owner.hashCode() * this.name.hashCode() * this.desc.hashCode();
    }
    
    public String toString() {
        /*SL:220*/return this.owner + '.' + this.name + this.desc + " (" + this.tag + (this.itf ? " itf" : "") + ')';
    }
}

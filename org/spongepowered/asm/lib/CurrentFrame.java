package org.spongepowered.asm.lib;

class CurrentFrame extends Frame
{
    void execute(final int a1, final int a2, final ClassWriter a3, final Item a4) {
        /*SL:50*/super.execute(a1, a2, a3, a4);
        final Frame v1 = /*EL:51*/new Frame();
        /*SL:52*/this.merge(a3, v1, 0);
        /*SL:53*/this.set(v1);
        /*SL:54*/this.owner.inputStackTop = 0;
    }
}

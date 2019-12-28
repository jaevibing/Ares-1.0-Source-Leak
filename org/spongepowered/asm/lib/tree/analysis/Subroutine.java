package org.spongepowered.asm.lib.tree.analysis;

import java.util.Collection;
import java.util.ArrayList;
import org.spongepowered.asm.lib.tree.JumpInsnNode;
import java.util.List;
import org.spongepowered.asm.lib.tree.LabelNode;

class Subroutine
{
    LabelNode start;
    boolean[] access;
    List<JumpInsnNode> callers;
    
    private Subroutine() {
    }
    
    Subroutine(final LabelNode a1, final int a2, final JumpInsnNode a3) {
        this.start = a1;
        this.access = new boolean[a2];
        (this.callers = new ArrayList<JumpInsnNode>()).add(a3);
    }
    
    public Subroutine copy() {
        final Subroutine v1 = /*EL:63*/new Subroutine();
        /*SL:64*/v1.start = this.start;
        /*SL:65*/v1.access = new boolean[this.access.length];
        /*SL:66*/System.arraycopy(this.access, 0, v1.access, 0, this.access.length);
        /*SL:67*/v1.callers = new ArrayList<JumpInsnNode>(this.callers);
        /*SL:68*/return v1;
    }
    
    public boolean merge(final Subroutine v-2) throws AnalyzerException {
        boolean b = /*EL:72*/false;
        /*SL:73*/for (int a1 = 0; a1 < this.access.length; ++a1) {
            /*SL:74*/if (v-2.access[a1] && !this.access[a1]) {
                /*SL:75*/this.access[a1] = true;
                /*SL:76*/b = true;
            }
        }
        /*SL:79*/if (v-2.start == this.start) {
            /*SL:80*/for (int v0 = 0; v0 < v-2.callers.size(); ++v0) {
                final JumpInsnNode v = /*EL:81*/v-2.callers.get(v0);
                /*SL:82*/if (!this.callers.contains(v)) {
                    /*SL:83*/this.callers.add(v);
                    /*SL:84*/b = true;
                }
            }
        }
        /*SL:88*/return b;
    }
}

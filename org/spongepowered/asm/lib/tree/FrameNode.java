package org.spongepowered.asm.lib.tree;

import java.util.ArrayList;
import java.util.Map;
import org.spongepowered.asm.lib.MethodVisitor;
import java.util.Arrays;
import java.util.List;

public class FrameNode extends AbstractInsnNode
{
    public int type;
    public List<Object> local;
    public List<Object> stack;
    
    private FrameNode() {
        super(-1);
    }
    
    public FrameNode(final int a1, final int a2, final Object[] a3, final int a4, final Object[] a5) {
        super(-1);
        switch (this.type = a1) {
            case -1:
            case 0: {
                this.local = asList(a2, a3);
                this.stack = asList(a4, a5);
                break;
            }
            case 1: {
                this.local = asList(a2, a3);
                break;
            }
            case 2: {
                this.local = Arrays.<Object>asList(new Object[a2]);
            }
            case 4: {
                this.stack = asList(1, a5);
                break;
            }
        }
    }
    
    public int getType() {
        /*SL:134*/return 14;
    }
    
    public void accept(final MethodVisitor a1) {
        /*SL:145*/switch (this.type) {
            case -1:
            case 0: {
                /*SL:148*/a1.visitFrame(this.type, this.local.size(), asArray(this.local), this.stack.size(), asArray(this.stack));
                /*SL:150*/break;
            }
            case 1: {
                /*SL:152*/a1.visitFrame(this.type, this.local.size(), asArray(this.local), 0, null);
                /*SL:153*/break;
            }
            case 2: {
                /*SL:155*/a1.visitFrame(this.type, this.local.size(), null, 0, null);
                /*SL:156*/break;
            }
            case 3: {
                /*SL:158*/a1.visitFrame(this.type, 0, null, 0, null);
                /*SL:159*/break;
            }
            case 4: {
                /*SL:161*/a1.visitFrame(this.type, 0, null, 1, asArray(this.stack));
                break;
            }
        }
    }
    
    public AbstractInsnNode clone(final Map<LabelNode, LabelNode> v-1) {
        final FrameNode v0 = /*EL:168*/new FrameNode();
        /*SL:169*/v0.type = this.type;
        /*SL:170*/if (this.local != null) {
            /*SL:171*/v0.local = new ArrayList<Object>();
            /*SL:172*/for (int v = 0; v < this.local.size(); ++v) {
                Object a1 = /*EL:173*/this.local.get(v);
                /*SL:174*/if (a1 instanceof LabelNode) {
                    /*SL:175*/a1 = v-1.get(a1);
                }
                /*SL:177*/v0.local.add(a1);
            }
        }
        /*SL:180*/if (this.stack != null) {
            /*SL:181*/v0.stack = new ArrayList<Object>();
            /*SL:182*/for (int v = 0; v < this.stack.size(); ++v) {
                Object v2 = /*EL:183*/this.stack.get(v);
                /*SL:184*/if (v2 instanceof LabelNode) {
                    /*SL:185*/v2 = v-1.get(v2);
                }
                /*SL:187*/v0.stack.add(v2);
            }
        }
        /*SL:190*/return v0;
    }
    
    private static List<Object> asList(final int a1, final Object[] a2) {
        /*SL:196*/return Arrays.<Object>asList(a2).subList(0, a1);
    }
    
    private static Object[] asArray(final List<Object> v-1) {
        final Object[] v0 = /*EL:200*/new Object[v-1.size()];
        /*SL:201*/for (int v = 0; v < v0.length; ++v) {
            Object a1 = /*EL:202*/v-1.get(v);
            /*SL:203*/if (a1 instanceof LabelNode) {
                /*SL:204*/a1 = ((LabelNode)a1).getLabel();
            }
            /*SL:206*/v0[v] = a1;
        }
        /*SL:208*/return v0;
    }
}

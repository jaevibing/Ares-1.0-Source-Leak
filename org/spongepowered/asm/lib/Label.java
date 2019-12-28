package org.spongepowered.asm.lib;

public class Label
{
    static final int DEBUG = 1;
    static final int RESOLVED = 2;
    static final int RESIZED = 4;
    static final int PUSHED = 8;
    static final int TARGET = 16;
    static final int STORE = 32;
    static final int REACHABLE = 64;
    static final int JSR = 128;
    static final int RET = 256;
    static final int SUBROUTINE = 512;
    static final int VISITED = 1024;
    static final int VISITED2 = 2048;
    public Object info;
    int status;
    int line;
    int position;
    private int referenceCount;
    private int[] srcAndRefPositions;
    int inputStackTop;
    int outputStackMax;
    Frame frame;
    Label successor;
    Edge successors;
    Label next;
    
    public int getOffset() {
        /*SL:278*/if ((this.status & 0x2) == 0x0) {
            /*SL:279*/throw new IllegalStateException("Label offset position has not been resolved yet");
        }
        /*SL:282*/return this.position;
    }
    
    void put(final MethodWriter a1, final ByteVector a2, final int a3, final boolean a4) {
        /*SL:306*/if ((this.status & 0x2) == 0x0) {
            /*SL:307*/if (a4) {
                /*SL:308*/this.addReference(-1 - a3, a2.length);
                /*SL:309*/a2.putInt(-1);
            }
            else {
                /*SL:311*/this.addReference(a3, a2.length);
                /*SL:312*/a2.putShort(-1);
            }
        }
        else/*SL:315*/ if (a4) {
            /*SL:316*/a2.putInt(this.position - a3);
        }
        else {
            /*SL:318*/a2.putShort(this.position - a3);
        }
    }
    
    private void addReference(final int v1, final int v2) {
        /*SL:338*/if (this.srcAndRefPositions == null) {
            /*SL:339*/this.srcAndRefPositions = new int[6];
        }
        /*SL:341*/if (this.referenceCount >= this.srcAndRefPositions.length) {
            final int[] a1 = /*EL:342*/new int[this.srcAndRefPositions.length + 6];
            /*SL:343*/System.arraycopy(this.srcAndRefPositions, 0, a1, 0, this.srcAndRefPositions.length);
            /*SL:345*/this.srcAndRefPositions = a1;
        }
        /*SL:347*/this.srcAndRefPositions[this.referenceCount++] = v1;
        /*SL:348*/this.srcAndRefPositions[this.referenceCount++] = v2;
    }
    
    boolean resolve(final MethodWriter v-5, final int v-4, final byte[] v-3) {
        boolean b = /*EL:375*/false;
        /*SL:376*/this.status |= 0x2;
        /*SL:377*/this.position = v-4;
        int i = /*EL:378*/0;
        /*SL:379*/while (i < this.referenceCount) {
            int a3 = /*EL:380*/this.srcAndRefPositions[i++];
            int v1 = /*EL:381*/this.srcAndRefPositions[i++];
            /*SL:383*/if (a3 >= 0) {
                final int a2 = /*EL:384*/v-4 - a3;
                /*SL:385*/if (a2 < -32768 || a2 > 32767) {
                    /*SL:395*/a3 = (v-3[v1 - 1] & 0xFF);
                    /*SL:396*/if (a3 <= 168) {
                        /*SL:398*/v-3[v1 - 1] = (byte)(a3 + 49);
                    }
                    else {
                        /*SL:401*/v-3[v1 - 1] = (byte)(a3 + 20);
                    }
                    /*SL:403*/b = true;
                }
                /*SL:405*/v-3[v1++] = (byte)(a2 >>> 8);
                /*SL:406*/v-3[v1] = (byte)a2;
            }
            else {
                final int v2 = /*EL:408*/v-4 + a3 + 1;
                /*SL:409*/v-3[v1++] = (byte)(v2 >>> 24);
                /*SL:410*/v-3[v1++] = (byte)(v2 >>> 16);
                /*SL:411*/v-3[v1++] = (byte)(v2 >>> 8);
                /*SL:412*/v-3[v1] = (byte)v2;
            }
        }
        /*SL:415*/return b;
    }
    
    Label getFirst() {
        /*SL:427*/return (this.frame == null) ? this : this.frame.owner;
    }
    
    boolean inSubroutine(final long a1) {
        /*SL:442*/return (this.status & 0x400) != 0x0 && /*EL:443*/(this.srcAndRefPositions[(int)(a1 >>> 32)] & (int)a1) != 0x0;
    }
    
    boolean inSameSubroutine(final Label v2) {
        /*SL:458*/if ((this.status & 0x400) == 0x0 || (v2.status & 0x400) == 0x0) {
            /*SL:459*/return false;
        }
        /*SL:461*/for (int a1 = 0; a1 < this.srcAndRefPositions.length; ++a1) {
            /*SL:462*/if ((this.srcAndRefPositions[a1] & v2.srcAndRefPositions[a1]) != 0x0) {
                /*SL:463*/return true;
            }
        }
        /*SL:466*/return false;
    }
    
    void addToSubroutine(final long a1, final int a2) {
        /*SL:478*/if ((this.status & 0x400) == 0x0) {
            /*SL:479*/this.status |= 0x400;
            /*SL:480*/this.srcAndRefPositions = new int[a2 / 32 + 1];
        }
        final int[] srcAndRefPositions = /*EL:482*/this.srcAndRefPositions;
        final int n = (int)(a1 >>> 32);
        srcAndRefPositions[n] |= (int)a1;
    }
    
    void visitSubroutine(final Label v2, final long v3, final int v5) {
        Label v6 = /*EL:503*/this;
        /*SL:504*/while (v6 != null) {
            Label a2 = /*EL:506*/v6;
            /*SL:507*/v6 = a2.next;
            /*SL:508*/a2.next = null;
            /*SL:510*/if (v2 != null) {
                /*SL:511*/if ((a2.status & 0x800) != 0x0) {
                    /*SL:512*/continue;
                }
                final Label label = /*EL:514*/a2;
                label.status |= 0x800;
                /*SL:516*/if ((a2.status & 0x100) != 0x0 && /*EL:517*/!a2.inSameSubroutine(v2)) {
                    /*SL:518*/a2 = new Edge();
                    /*SL:519*/a2.info = a2.inputStackTop;
                    /*SL:520*/a2.successor = v2.successors.successor;
                    /*SL:521*/a2.next = a2.successors;
                    /*SL:522*/a2.successors = a2;
                }
            }
            else {
                /*SL:527*/if (a2.inSubroutine(v3)) {
                    /*SL:528*/continue;
                }
                /*SL:531*/a2.addToSubroutine(v3, v5);
            }
            /*SL:535*/for (Edge a3 = a2.successors; a3 != null; /*SL:546*/a3 = a3.next) {
                if (((a2.status & 0x80) == 0x0 || a3 != a2.successors.next) && a3.successor.next == null) {
                    a3.successor.next = v6;
                    v6 = a3.successor;
                }
            }
        }
    }
    
    public String toString() {
        /*SL:562*/return "L" + System.identityHashCode(this);
    }
}

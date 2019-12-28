package javassist.bytecode;

final class LongVector
{
    static final int ASIZE = 128;
    static final int ABITS = 7;
    static final int VSIZE = 8;
    private ConstInfo[][] objects;
    private int elements;
    
    public LongVector() {
        this.objects = new ConstInfo[8][];
        this.elements = 0;
    }
    
    public LongVector(final int a1) {
        final int v1 = (a1 >> 7 & 0xFFFFFFF8) + 8;
        this.objects = new ConstInfo[v1][];
        this.elements = 0;
    }
    
    public int size() {
        /*SL:37*/return this.elements;
    }
    
    public int capacity() {
        /*SL:39*/return this.objects.length * 128;
    }
    
    public ConstInfo elementAt(final int a1) {
        /*SL:42*/if (a1 < 0 || this.elements <= a1) {
            /*SL:43*/return null;
        }
        /*SL:45*/return this.objects[a1 >> 7][a1 & 0x7F];
    }
    
    public void addElement(final ConstInfo v2) {
        final int v3 = /*EL:49*/this.elements >> 7;
        final int v4 = /*EL:50*/this.elements & 0x7F;
        final int v5 = /*EL:51*/this.objects.length;
        /*SL:52*/if (v3 >= v5) {
            final ConstInfo[][] a1 = /*EL:53*/new ConstInfo[v5 + 8][];
            /*SL:54*/System.arraycopy(this.objects, 0, a1, 0, v5);
            /*SL:55*/this.objects = a1;
        }
        /*SL:58*/if (this.objects[v3] == null) {
            /*SL:59*/this.objects[v3] = new ConstInfo[128];
        }
        /*SL:61*/this.objects[v3][v4] = v2;
        /*SL:62*/++this.elements;
    }
}

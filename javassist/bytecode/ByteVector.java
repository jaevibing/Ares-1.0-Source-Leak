package javassist.bytecode;

class ByteVector implements Cloneable
{
    private byte[] buffer;
    private int size;
    
    public ByteVector() {
        this.buffer = new byte[64];
        this.size = 0;
    }
    
    public Object clone() throws CloneNotSupportedException {
        final ByteVector v1 = /*EL:32*/(ByteVector)super.clone();
        /*SL:33*/v1.buffer = this.buffer.clone();
        /*SL:34*/return v1;
    }
    
    public final int getSize() {
        /*SL:37*/return this.size;
    }
    
    public final byte[] copy() {
        final byte[] v1 = /*EL:40*/new byte[this.size];
        /*SL:41*/System.arraycopy(this.buffer, 0, v1, 0, this.size);
        /*SL:42*/return v1;
    }
    
    public int read(final int a1) {
        /*SL:46*/if (a1 < 0 || this.size <= a1) {
            /*SL:47*/throw new ArrayIndexOutOfBoundsException(a1);
        }
        /*SL:49*/return this.buffer[a1];
    }
    
    public void write(final int a1, final int a2) {
        /*SL:53*/if (a1 < 0 || this.size <= a1) {
            /*SL:54*/throw new ArrayIndexOutOfBoundsException(a1);
        }
        /*SL:56*/this.buffer[a1] = (byte)a2;
    }
    
    public void add(final int a1) {
        /*SL:60*/this.addGap(1);
        /*SL:61*/this.buffer[this.size - 1] = (byte)a1;
    }
    
    public void add(final int a1, final int a2) {
        /*SL:65*/this.addGap(2);
        /*SL:66*/this.buffer[this.size - 2] = (byte)a1;
        /*SL:67*/this.buffer[this.size - 1] = (byte)a2;
    }
    
    public void add(final int a1, final int a2, final int a3, final int a4) {
        /*SL:71*/this.addGap(4);
        /*SL:72*/this.buffer[this.size - 4] = (byte)a1;
        /*SL:73*/this.buffer[this.size - 3] = (byte)a2;
        /*SL:74*/this.buffer[this.size - 2] = (byte)a3;
        /*SL:75*/this.buffer[this.size - 1] = (byte)a4;
    }
    
    public void addGap(final int v-1) {
        /*SL:79*/if (this.size + v-1 > this.buffer.length) {
            int a1 = /*EL:80*/this.size << 1;
            /*SL:81*/if (a1 < this.size + v-1) {
                /*SL:82*/a1 = this.size + v-1;
            }
            final byte[] v1 = /*EL:84*/new byte[a1];
            /*SL:85*/System.arraycopy(this.buffer, 0, v1, 0, this.size);
            /*SL:86*/this.buffer = v1;
        }
        /*SL:89*/this.size += v-1;
    }
}

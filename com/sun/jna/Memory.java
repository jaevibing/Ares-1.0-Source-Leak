package com.sun.jna;

import java.util.Collections;
import java.util.WeakHashMap;
import java.nio.ByteBuffer;
import java.lang.ref.WeakReference;
import java.util.Iterator;
import java.util.Collection;
import java.util.LinkedList;
import java.lang.ref.Reference;
import java.util.Map;

public class Memory extends Pointer
{
    private static final Map<Memory, Reference<Memory>> allocatedMemory;
    private static final WeakMemoryHolder buffers;
    protected long size;
    
    public static void purge() {
        Memory.buffers.clean();
    }
    
    public static void disposeAll() {
        final Collection<Memory> collection = /*EL:71*/new LinkedList<Memory>(Memory.allocatedMemory.keySet());
        /*SL:72*/for (final Memory v1 : collection) {
            /*SL:73*/v1.dispose();
        }
    }
    
    public Memory(final long a1) {
        this.size = a1;
        if (a1 <= 0L) {
            throw new IllegalArgumentException("Allocation size must be greater than zero");
        }
        this.peer = malloc(a1);
        if (this.peer == 0L) {
            throw new OutOfMemoryError(/*EL:75*/"Cannot allocate " + a1 + " bytes");
        }
        Memory.allocatedMemory.put(this, new WeakReference<Memory>(this));
    }
    
    protected Memory() {
    }
    
    @Override
    public Pointer share(final long a1) {
        /*SL:132*/return this.share(a1, this.size() - a1);
    }
    
    @Override
    public Pointer share(final long a1, final long a2) {
        /*SL:144*/this.boundsCheck(a1, a2);
        /*SL:145*/return new SharedMemory(a1, a2);
    }
    
    public Memory align(final int v-5) {
        /*SL:157*/if (v-5 <= 0) {
            /*SL:158*/throw new IllegalArgumentException("Byte boundary must be positive: " + v-5);
        }
        int i = /*EL:160*/0;
        while (i < 32) {
            /*SL:161*/if (v-5 == 1 << i) {
                final long n = /*EL:162*/~(v-5 - 1L);
                /*SL:164*/if ((this.peer & n) == this.peer) {
                    /*SL:172*/return this;
                }
                final long a1 = this.peer + v-5 - 1L & n;
                final long v1 = this.peer + this.size - a1;
                if (v1 <= 0L) {
                    throw new IllegalArgumentException("Insufficient memory to align to the requested boundary");
                }
                return (Memory)this.share(a1 - this.peer, v1);
            }
            else {
                ++i;
            }
        }
        /*SL:175*/throw new IllegalArgumentException("Byte boundary must be a power of two");
    }
    
    @Override
    protected void finalize() {
        /*SL:181*/this.dispose();
    }
    
    protected synchronized void dispose() {
        try {
            free(/*EL:187*/this.peer);
        }
        finally {
            Memory.allocatedMemory.remove(/*EL:189*/this);
            /*SL:190*/this.peer = 0L;
        }
    }
    
    public void clear() {
        /*SL:196*/this.clear(this.size);
    }
    
    public boolean valid() {
        /*SL:201*/return this.peer != 0L;
    }
    
    public long size() {
        /*SL:205*/return this.size;
    }
    
    protected void boundsCheck(final long v1, final long v3) {
        /*SL:214*/if (v1 < 0L) {
            /*SL:215*/throw new IndexOutOfBoundsException("Invalid offset: " + v1);
        }
        /*SL:217*/if (v1 + v3 > this.size) {
            final String a1 = /*EL:218*/"Bounds exceeds available space : size=" + this.size + ", offset=" + (v1 + v3);
            /*SL:220*/throw new IndexOutOfBoundsException(a1);
        }
    }
    
    @Override
    public void read(final long a1, final byte[] a2, final int a3, final int a4) {
        /*SL:238*/this.boundsCheck(a1, a4 * 1L);
        /*SL:239*/super.read(a1, a2, a3, a4);
    }
    
    @Override
    public void read(final long a1, final short[] a2, final int a3, final int a4) {
        /*SL:252*/this.boundsCheck(a1, a4 * 2L);
        /*SL:253*/super.read(a1, a2, a3, a4);
    }
    
    @Override
    public void read(final long a1, final char[] a2, final int a3, final int a4) {
        /*SL:266*/this.boundsCheck(a1, a4 * 2L);
        /*SL:267*/super.read(a1, a2, a3, a4);
    }
    
    @Override
    public void read(final long a1, final int[] a2, final int a3, final int a4) {
        /*SL:280*/this.boundsCheck(a1, a4 * 4L);
        /*SL:281*/super.read(a1, a2, a3, a4);
    }
    
    @Override
    public void read(final long a1, final long[] a2, final int a3, final int a4) {
        /*SL:294*/this.boundsCheck(a1, a4 * 8L);
        /*SL:295*/super.read(a1, a2, a3, a4);
    }
    
    @Override
    public void read(final long a1, final float[] a2, final int a3, final int a4) {
        /*SL:308*/this.boundsCheck(a1, a4 * 4L);
        /*SL:309*/super.read(a1, a2, a3, a4);
    }
    
    @Override
    public void read(final long a1, final double[] a2, final int a3, final int a4) {
        /*SL:322*/this.boundsCheck(a1, a4 * 8L);
        /*SL:323*/super.read(a1, a2, a3, a4);
    }
    
    @Override
    public void write(final long a1, final byte[] a2, final int a3, final int a4) {
        /*SL:340*/this.boundsCheck(a1, a4 * 1L);
        /*SL:341*/super.write(a1, a2, a3, a4);
    }
    
    @Override
    public void write(final long a1, final short[] a2, final int a3, final int a4) {
        /*SL:354*/this.boundsCheck(a1, a4 * 2L);
        /*SL:355*/super.write(a1, a2, a3, a4);
    }
    
    @Override
    public void write(final long a1, final char[] a2, final int a3, final int a4) {
        /*SL:368*/this.boundsCheck(a1, a4 * 2L);
        /*SL:369*/super.write(a1, a2, a3, a4);
    }
    
    @Override
    public void write(final long a1, final int[] a2, final int a3, final int a4) {
        /*SL:382*/this.boundsCheck(a1, a4 * 4L);
        /*SL:383*/super.write(a1, a2, a3, a4);
    }
    
    @Override
    public void write(final long a1, final long[] a2, final int a3, final int a4) {
        /*SL:396*/this.boundsCheck(a1, a4 * 8L);
        /*SL:397*/super.write(a1, a2, a3, a4);
    }
    
    @Override
    public void write(final long a1, final float[] a2, final int a3, final int a4) {
        /*SL:410*/this.boundsCheck(a1, a4 * 4L);
        /*SL:411*/super.write(a1, a2, a3, a4);
    }
    
    @Override
    public void write(final long a1, final double[] a2, final int a3, final int a4) {
        /*SL:424*/this.boundsCheck(a1, a4 * 8L);
        /*SL:425*/super.write(a1, a2, a3, a4);
    }
    
    @Override
    public byte getByte(final long a1) {
        /*SL:442*/this.boundsCheck(a1, 1L);
        /*SL:443*/return super.getByte(a1);
    }
    
    @Override
    public char getChar(final long a1) {
        /*SL:456*/this.boundsCheck(a1, 1L);
        /*SL:457*/return super.getChar(a1);
    }
    
    @Override
    public short getShort(final long a1) {
        /*SL:470*/this.boundsCheck(a1, 2L);
        /*SL:471*/return super.getShort(a1);
    }
    
    @Override
    public int getInt(final long a1) {
        /*SL:484*/this.boundsCheck(a1, 4L);
        /*SL:485*/return super.getInt(a1);
    }
    
    @Override
    public long getLong(final long a1) {
        /*SL:498*/this.boundsCheck(a1, 8L);
        /*SL:499*/return super.getLong(a1);
    }
    
    @Override
    public float getFloat(final long a1) {
        /*SL:512*/this.boundsCheck(a1, 4L);
        /*SL:513*/return super.getFloat(a1);
    }
    
    @Override
    public double getDouble(final long a1) {
        /*SL:526*/this.boundsCheck(a1, 8L);
        /*SL:527*/return super.getDouble(a1);
    }
    
    @Override
    public Pointer getPointer(final long a1) {
        /*SL:540*/this.boundsCheck(a1, Pointer.SIZE);
        /*SL:541*/return super.getPointer(a1);
    }
    
    @Override
    public ByteBuffer getByteBuffer(final long a1, final long a2) {
        /*SL:558*/this.boundsCheck(a1, a2);
        final ByteBuffer v1 = /*EL:559*/super.getByteBuffer(a1, a2);
        Memory.buffers.put(/*EL:562*/v1, this);
        /*SL:563*/return v1;
    }
    
    @Override
    public String getString(final long a1, final String a2) {
        /*SL:569*/this.boundsCheck(a1, 0L);
        /*SL:570*/return super.getString(a1, a2);
    }
    
    @Override
    public String getWideString(final long a1) {
        /*SL:576*/this.boundsCheck(a1, 0L);
        /*SL:577*/return super.getWideString(a1);
    }
    
    @Override
    public void setByte(final long a1, final byte a2) {
        /*SL:594*/this.boundsCheck(a1, 1L);
        /*SL:595*/super.setByte(a1, a2);
    }
    
    @Override
    public void setChar(final long a1, final char a2) {
        /*SL:608*/this.boundsCheck(a1, Native.WCHAR_SIZE);
        /*SL:609*/super.setChar(a1, a2);
    }
    
    @Override
    public void setShort(final long a1, final short a2) {
        /*SL:622*/this.boundsCheck(a1, 2L);
        /*SL:623*/super.setShort(a1, a2);
    }
    
    @Override
    public void setInt(final long a1, final int a2) {
        /*SL:636*/this.boundsCheck(a1, 4L);
        /*SL:637*/super.setInt(a1, a2);
    }
    
    @Override
    public void setLong(final long a1, final long a2) {
        /*SL:650*/this.boundsCheck(a1, 8L);
        /*SL:651*/super.setLong(a1, a2);
    }
    
    @Override
    public void setFloat(final long a1, final float a2) {
        /*SL:664*/this.boundsCheck(a1, 4L);
        /*SL:665*/super.setFloat(a1, a2);
    }
    
    @Override
    public void setDouble(final long a1, final double a2) {
        /*SL:678*/this.boundsCheck(a1, 8L);
        /*SL:679*/super.setDouble(a1, a2);
    }
    
    @Override
    public void setPointer(final long a1, final Pointer a2) {
        /*SL:692*/this.boundsCheck(a1, Pointer.SIZE);
        /*SL:693*/super.setPointer(a1, a2);
    }
    
    @Override
    public void setString(final long a1, final String a2, final String a3) {
        /*SL:698*/this.boundsCheck(a1, Native.getBytes(a2, a3).length + 1L);
        /*SL:699*/super.setString(a1, a2, a3);
    }
    
    @Override
    public void setWideString(final long a1, final String a2) {
        /*SL:704*/this.boundsCheck(a1, (a2.length() + 1L) * Native.WCHAR_SIZE);
        /*SL:705*/super.setWideString(a1, a2);
    }
    
    @Override
    public String toString() {
        /*SL:710*/return "allocated@0x" + Long.toHexString(this.peer) + " (" + this.size + " bytes)";
    }
    
    protected static void free(final long a1) {
        /*SL:715*/if (a1 != 0L) {
            /*SL:716*/Native.free(a1);
        }
    }
    
    protected static long malloc(final long a1) {
        /*SL:721*/return Native.malloc(a1);
    }
    
    public String dump() {
        /*SL:726*/return this.dump(0L, (int)this.size());
    }
    
    static {
        allocatedMemory = Collections.<Memory, Reference<Memory>>synchronizedMap(new WeakHashMap<Memory, Reference<Memory>>());
        buffers = new WeakMemoryHolder();
    }
    
    private class SharedMemory extends Memory
    {
        public SharedMemory(final long a1, final long a2) {
            this.size = a2;
            this.peer = Memory.this.peer + a1;
        }
        
        @Override
        protected void dispose() {
            /*SL:90*/this.peer = 0L;
        }
        
        @Override
        protected void boundsCheck(final long a1, final long a2) {
            /*SL:95*/Memory.this.boundsCheck(this.peer - Memory.this.peer + a1, a2);
        }
        
        @Override
        public String toString() {
            /*SL:99*/return super.toString() + " (shared from " + Memory.this.toString() + ")";
        }
    }
}

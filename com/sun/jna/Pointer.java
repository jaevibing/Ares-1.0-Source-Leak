package com.sun.jna;

import java.io.Writer;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import java.util.ArrayList;
import java.nio.ByteOrder;
import java.nio.ByteBuffer;
import java.lang.reflect.Array;
import java.nio.Buffer;

public class Pointer
{
    public static final int SIZE;
    public static final Pointer NULL;
    protected long peer;
    
    public static final Pointer createConstant(final long a1) {
        /*SL:64*/return new Opaque(a1);
    }
    
    public static final Pointer createConstant(final int a1) {
        /*SL:72*/return new Opaque(a1 & -1L);
    }
    
    Pointer() {
    }
    
    public Pointer(final long a1) {
        this.peer = a1;
    }
    
    public Pointer share(final long a1) {
        /*SL:93*/return this.share(a1, 0L);
    }
    
    public Pointer share(final long a1, final long a2) {
        /*SL:100*/if (a1 == 0L) {
            /*SL:101*/return this;
        }
        /*SL:103*/return new Pointer(this.peer + a1);
    }
    
    public void clear(final long a1) {
        /*SL:108*/this.setMemory(0L, a1, (byte)0);
    }
    
    @Override
    public boolean equals(final Object a1) {
        /*SL:113*/return a1 == this || /*EL:116*/(a1 != null && /*EL:119*/a1 instanceof Pointer && ((Pointer)a1).peer == this.peer);
    }
    
    @Override
    public int hashCode() {
        /*SL:124*/return (int)((this.peer >>> 32) + (this.peer & -1L));
    }
    
    public long indexOf(final long a1, final byte a2) {
        /*SL:136*/return Native.indexOf(this, this.peer, a1, a2);
    }
    
    public void read(final long a1, final byte[] a2, final int a3, final int a4) {
        /*SL:149*/Native.read(this, this.peer, a1, a2, a3, a4);
    }
    
    public void read(final long a1, final short[] a2, final int a3, final int a4) {
        /*SL:162*/Native.read(this, this.peer, a1, a2, a3, a4);
    }
    
    public void read(final long a1, final char[] a2, final int a3, final int a4) {
        /*SL:175*/Native.read(this, this.peer, a1, a2, a3, a4);
    }
    
    public void read(final long a1, final int[] a2, final int a3, final int a4) {
        /*SL:188*/Native.read(this, this.peer, a1, a2, a3, a4);
    }
    
    public void read(final long a1, final long[] a2, final int a3, final int a4) {
        /*SL:201*/Native.read(this, this.peer, a1, a2, a3, a4);
    }
    
    public void read(final long a1, final float[] a2, final int a3, final int a4) {
        /*SL:214*/Native.read(this, this.peer, a1, a2, a3, a4);
    }
    
    public void read(final long a1, final double[] a2, final int a3, final int a4) {
        /*SL:227*/Native.read(this, this.peer, a1, a2, a3, a4);
    }
    
    public void read(final long v1, final Pointer[] v3, final int v4, final int v5) {
        /*SL:240*/for (Pointer a3 = (Pointer)0; a3 < v5; ++a3) {
            final Pointer a2 = /*EL:241*/this.getPointer(v1 + a3 * Pointer.SIZE);
            /*SL:242*/a3 = v3[a3 + v4];
            /*SL:244*/if (a3 == null || a2 == null || a2.peer != a3.peer) {
                /*SL:245*/v3[a3 + v4] = a2;
            }
        }
    }
    
    public void write(final long a1, final byte[] a2, final int a3, final int a4) {
        /*SL:266*/Native.write(this, this.peer, a1, a2, a3, a4);
    }
    
    public void write(final long a1, final short[] a2, final int a3, final int a4) {
        /*SL:280*/Native.write(this, this.peer, a1, a2, a3, a4);
    }
    
    public void write(final long a1, final char[] a2, final int a3, final int a4) {
        /*SL:294*/Native.write(this, this.peer, a1, a2, a3, a4);
    }
    
    public void write(final long a1, final int[] a2, final int a3, final int a4) {
        /*SL:308*/Native.write(this, this.peer, a1, a2, a3, a4);
    }
    
    public void write(final long a1, final long[] a2, final int a3, final int a4) {
        /*SL:322*/Native.write(this, this.peer, a1, a2, a3, a4);
    }
    
    public void write(final long a1, final float[] a2, final int a3, final int a4) {
        /*SL:336*/Native.write(this, this.peer, a1, a2, a3, a4);
    }
    
    public void write(final long a1, final double[] a2, final int a3, final int a4) {
        /*SL:350*/Native.write(this, this.peer, a1, a2, a3, a4);
    }
    
    public void write(final long a3, final Pointer[] a4, final int v1, final int v2) {
        /*SL:361*/for (int a5 = 0; a5 < v2; ++a5) {
            /*SL:362*/this.setPointer(a3 + a5 * Pointer.SIZE, a4[v1 + a5]);
        }
    }
    
    Object getValue(final long v-4, final Class<?> v-2, final Object v-1) {
        Object v0 = /*EL:372*/null;
        /*SL:373*/if (Structure.class.isAssignableFrom(v-2)) {
            Structure a1 = /*EL:374*/(Structure)v-1;
            /*SL:375*/if (Structure.ByReference.class.isAssignableFrom(v-2)) {
                /*SL:376*/a1 = Structure.updateStructureByReference(v-2, a1, this.getPointer(v-4));
            }
            else {
                /*SL:378*/a1.useMemory(this, (int)v-4, true);
                /*SL:379*/a1.read();
            }
            /*SL:381*/v0 = a1;
        }
        else/*SL:382*/ if (v-2 == Boolean.TYPE || v-2 == Boolean.class) {
            /*SL:383*/v0 = Function.valueOf(this.getInt(v-4) != 0);
        }
        else/*SL:384*/ if (v-2 == Byte.TYPE || v-2 == Byte.class) {
            /*SL:385*/v0 = this.getByte(v-4);
        }
        else/*SL:386*/ if (v-2 == Short.TYPE || v-2 == Short.class) {
            /*SL:387*/v0 = this.getShort(v-4);
        }
        else/*SL:388*/ if (v-2 == Character.TYPE || v-2 == Character.class) {
            /*SL:389*/v0 = this.getChar(v-4);
        }
        else/*SL:390*/ if (v-2 == Integer.TYPE || v-2 == Integer.class) {
            /*SL:391*/v0 = this.getInt(v-4);
        }
        else/*SL:392*/ if (v-2 == Long.TYPE || v-2 == Long.class) {
            /*SL:393*/v0 = this.getLong(v-4);
        }
        else/*SL:394*/ if (v-2 == Float.TYPE || v-2 == Float.class) {
            /*SL:395*/v0 = this.getFloat(v-4);
        }
        else/*SL:396*/ if (v-2 == Double.TYPE || v-2 == Double.class) {
            /*SL:397*/v0 = this.getDouble(v-4);
        }
        else/*SL:398*/ if (Pointer.class.isAssignableFrom(v-2)) {
            final Pointer a2 = /*EL:399*/this.getPointer(v-4);
            /*SL:400*/if (a2 != null) {
                final Pointer a3 = /*EL:401*/(v-1 instanceof Pointer) ? ((Pointer)v-1) : null;
                /*SL:403*/if (a3 == null || a2.peer != a3.peer) {
                    /*SL:404*/v0 = a2;
                }
                else {
                    /*SL:406*/v0 = a3;
                }
            }
        }
        else/*SL:409*/ if (v-2 == String.class) {
            final Pointer v = /*EL:410*/this.getPointer(v-4);
            /*SL:411*/v0 = ((v != null) ? v.getString(0L) : null);
        }
        else/*SL:412*/ if (v-2 == WString.class) {
            final Pointer v = /*EL:413*/this.getPointer(v-4);
            /*SL:414*/v0 = ((v != null) ? new WString(v.getWideString(0L)) : null);
        }
        else/*SL:415*/ if (Callback.class.isAssignableFrom(v-2)) {
            final Pointer v = /*EL:418*/this.getPointer(v-4);
            /*SL:419*/if (v == null) {
                /*SL:420*/v0 = null;
            }
            else {
                Callback v2 = /*EL:422*/(Callback)v-1;
                final Pointer v3 = /*EL:423*/CallbackReference.getFunctionPointer(v2);
                /*SL:424*/if (!v.equals(v3)) {
                    /*SL:425*/v2 = CallbackReference.getCallback(v-2, v);
                }
                /*SL:427*/v0 = v2;
            }
        }
        else/*SL:429*/ if (Platform.HAS_BUFFERS && Buffer.class.isAssignableFrom(v-2)) {
            final Pointer v = /*EL:430*/this.getPointer(v-4);
            /*SL:431*/if (v == null) {
                /*SL:432*/v0 = null;
            }
            else {
                final Pointer v4 = /*EL:434*/(v-1 == null) ? null : /*EL:435*/Native.getDirectBufferPointer((Buffer)v-1);
                /*SL:436*/if (v4 == null || !v4.equals(v)) {
                    /*SL:437*/throw new IllegalStateException("Can't autogenerate a direct buffer on memory read");
                }
                /*SL:439*/v0 = v-1;
            }
        }
        else/*SL:441*/ if (NativeMapped.class.isAssignableFrom(v-2)) {
            final NativeMapped v5 = /*EL:442*/(NativeMapped)v-1;
            /*SL:443*/if (v5 != null) {
                final Object v6 = /*EL:444*/this.getValue(v-4, v5.nativeType(), null);
                /*SL:445*/v0 = v5.fromNative(v6, new FromNativeContext(v-2));
                /*SL:446*/if (v5.equals(v0)) {
                    /*SL:447*/v0 = v5;
                }
            }
            else {
                final NativeMappedConverter v7 = /*EL:450*/NativeMappedConverter.getInstance(v-2);
                final Object v8 = /*EL:451*/this.getValue(v-4, v7.nativeType(), null);
                /*SL:452*/v0 = v7.fromNative(v8, new FromNativeContext(v-2));
            }
        }
        else {
            /*SL:454*/if (!v-2.isArray()) {
                /*SL:461*/throw new IllegalArgumentException("Reading \"" + v-2 + "\" from memory is not supported");
            }
            v0 = v-1;
            if (v0 == null) {
                throw new IllegalStateException("Need an initialized array");
            }
            this.readArray(v-4, v0, v-2.getComponentType());
        }
        /*SL:463*/return v0;
    }
    
    private void readArray(final long v-6, final Object v-4, final Class<?> v-3) {
        int length = /*EL:468*/0;
        /*SL:469*/length = Array.getLength(v-4);
        /*SL:472*/if (v-3 == Byte.TYPE) {
            /*SL:473*/this.read(v-6, (byte[])v-4, 0, length);
        }
        else/*SL:475*/ if (v-3 == Short.TYPE) {
            /*SL:476*/this.read(v-6, (short[])v-4, 0, length);
        }
        else/*SL:478*/ if (v-3 == Character.TYPE) {
            /*SL:479*/this.read(v-6, (char[])v-4, 0, length);
        }
        else/*SL:481*/ if (v-3 == Integer.TYPE) {
            /*SL:482*/this.read(v-6, (int[])v-4, 0, length);
        }
        else/*SL:484*/ if (v-3 == Long.TYPE) {
            /*SL:485*/this.read(v-6, (long[])v-4, 0, length);
        }
        else/*SL:487*/ if (v-3 == Float.TYPE) {
            /*SL:488*/this.read(v-6, (float[])v-4, 0, length);
        }
        else/*SL:490*/ if (v-3 == Double.TYPE) {
            /*SL:491*/this.read(v-6, (double[])v-4, 0, length);
        }
        else/*SL:493*/ if (Pointer.class.isAssignableFrom(v-3)) {
            /*SL:494*/this.read(v-6, (Pointer[])v-4, 0, length);
        }
        else/*SL:496*/ if (Structure.class.isAssignableFrom(v-3)) {
            final Structure[] v0 = /*EL:497*/(Structure[])v-4;
            /*SL:498*/if (Structure.ByReference.class.isAssignableFrom(v-3)) {
                Pointer[] a2 = /*EL:499*/this.getPointerArray(v-6, v0.length);
                /*SL:500*/for (a2 = 0; a2 < v0.length; ++a2) {
                    /*SL:501*/v0[a2] = Structure.updateStructureByReference(v-3, v0[a2], a2[a2]);
                }
            }
            else {
                Structure v = /*EL:505*/v0[0];
                /*SL:506*/if (v == null) {
                    /*SL:507*/v = Structure.newInstance(v-3, this.share(v-6));
                    /*SL:508*/v.conditionalAutoRead();
                    /*SL:509*/v0[0] = v;
                }
                else {
                    /*SL:512*/v.useMemory(this, (int)v-6, true);
                    /*SL:513*/v.read();
                }
                final Structure[] v2 = /*EL:515*/v.toArray(v0.length);
                /*SL:516*/for (int a3 = 1; a3 < v0.length; ++a3) {
                    /*SL:517*/if (v0[a3] == null) {
                        /*SL:519*/v0[a3] = v2[a3];
                    }
                    else {
                        /*SL:522*/v0[a3].useMemory(this, (int)(v-6 + a3 * v0[a3].size()), true);
                        /*SL:523*/v0[a3].read();
                    }
                }
            }
        }
        else {
            /*SL:528*/if (!NativeMapped.class.isAssignableFrom(v-3)) {
                /*SL:538*/throw new IllegalArgumentException("Reading array of " + v-3 + " from memory not supported");
            }
            final NativeMapped[] v3 = (NativeMapped[])v-4;
            final NativeMappedConverter v4 = NativeMappedConverter.getInstance(v-3);
            final int v5 = Native.getNativeSize(v-4.getClass(), v-4) / v3.length;
            for (int v6 = 0; v6 < v3.length; ++v6) {
                final Object v7 = this.getValue(v-6 + v5 * v6, v4.nativeType(), v3[v6]);
                v3[v6] = (NativeMapped)v4.fromNative(v7, new FromNativeContext(v-3));
            }
        }
    }
    
    public byte getByte(final long a1) {
        /*SL:553*/return Native.getByte(this, this.peer, a1);
    }
    
    public char getChar(final long a1) {
        /*SL:565*/return Native.getChar(this, this.peer, a1);
    }
    
    public short getShort(final long a1) {
        /*SL:577*/return Native.getShort(this, this.peer, a1);
    }
    
    public int getInt(final long a1) {
        /*SL:589*/return Native.getInt(this, this.peer, a1);
    }
    
    public long getLong(final long a1) {
        /*SL:601*/return Native.getLong(this, this.peer, a1);
    }
    
    public NativeLong getNativeLong(final long a1) {
        /*SL:613*/return new NativeLong((NativeLong.SIZE == 8) ? this.getLong(a1) : this.getInt(a1));
    }
    
    public float getFloat(final long a1) {
        /*SL:625*/return Native.getFloat(this, this.peer, a1);
    }
    
    public double getDouble(final long a1) {
        /*SL:637*/return Native.getDouble(this, this.peer, a1);
    }
    
    public Pointer getPointer(final long a1) {
        /*SL:651*/return Native.getPointer(this.peer + a1);
    }
    
    public ByteBuffer getByteBuffer(final long a1, final long a2) {
        /*SL:663*/return Native.getDirectByteBuffer(this, this.peer, a1, a2).order(ByteOrder.nativeOrder());
    }
    
    @Deprecated
    public String getString(final long a1, final boolean a2) {
        /*SL:680*/return a2 ? this.getWideString(a1) : this.getString(a1);
    }
    
    public String getWideString(final long a1) {
        /*SL:685*/return Native.getWideString(this, this.peer, a1);
    }
    
    public String getString(final long a1) {
        /*SL:696*/return this.getString(a1, Native.getDefaultStringEncoding());
    }
    
    public String getString(final long a1, final String a2) {
        /*SL:707*/return Native.getString(this, a1, a2);
    }
    
    public byte[] getByteArray(final long a1, final int a2) {
        final byte[] v1 = /*EL:714*/new byte[a2];
        /*SL:715*/this.read(a1, v1, 0, a2);
        /*SL:716*/return v1;
    }
    
    public char[] getCharArray(final long a1, final int a2) {
        final char[] v1 = /*EL:723*/new char[a2];
        /*SL:724*/this.read(a1, v1, 0, a2);
        /*SL:725*/return v1;
    }
    
    public short[] getShortArray(final long a1, final int a2) {
        final short[] v1 = /*EL:732*/new short[a2];
        /*SL:733*/this.read(a1, v1, 0, a2);
        /*SL:734*/return v1;
    }
    
    public int[] getIntArray(final long a1, final int a2) {
        final int[] v1 = /*EL:741*/new int[a2];
        /*SL:742*/this.read(a1, v1, 0, a2);
        /*SL:743*/return v1;
    }
    
    public long[] getLongArray(final long a1, final int a2) {
        final long[] v1 = /*EL:750*/new long[a2];
        /*SL:751*/this.read(a1, v1, 0, a2);
        /*SL:752*/return v1;
    }
    
    public float[] getFloatArray(final long a1, final int a2) {
        final float[] v1 = /*EL:759*/new float[a2];
        /*SL:760*/this.read(a1, v1, 0, a2);
        /*SL:761*/return v1;
    }
    
    public double[] getDoubleArray(final long a1, final int a2) {
        final double[] v1 = /*EL:768*/new double[a2];
        /*SL:769*/this.read(a1, v1, 0, a2);
        /*SL:770*/return v1;
    }
    
    public Pointer[] getPointerArray(final long a1) {
        final List<Pointer> v1 = /*EL:777*/new ArrayList<Pointer>();
        int v2 = /*EL:778*/0;
        /*SL:780*/for (Pointer v3 = this.getPointer(a1); v3 != null; /*SL:783*/v3 = this.getPointer(a1 + v2)) {
            v1.add(v3);
            v2 += Pointer.SIZE;
        }
        /*SL:785*/return v1.<Pointer>toArray(new Pointer[v1.size()]);
    }
    
    public Pointer[] getPointerArray(final long a1, final int a2) {
        final Pointer[] v1 = /*EL:790*/new Pointer[a2];
        /*SL:791*/this.read(a1, v1, 0, a2);
        /*SL:792*/return v1;
    }
    
    public String[] getStringArray(final long a1) {
        /*SL:803*/return this.getStringArray(a1, -1, Native.getDefaultStringEncoding());
    }
    
    public String[] getStringArray(final long a1, final String a2) {
        /*SL:811*/return this.getStringArray(a1, -1, a2);
    }
    
    public String[] getStringArray(final long a1, final int a2) {
        /*SL:821*/return this.getStringArray(a1, a2, Native.getDefaultStringEncoding());
    }
    
    @Deprecated
    public String[] getStringArray(final long a1, final boolean a2) {
        /*SL:834*/return this.getStringArray(a1, -1, a2);
    }
    
    public String[] getWideStringArray(final long a1) {
        /*SL:838*/return this.getWideStringArray(a1, -1);
    }
    
    public String[] getWideStringArray(final long a1, final int a2) {
        /*SL:842*/return this.getStringArray(a1, a2, "--WIDE-STRING--");
    }
    
    @Deprecated
    public String[] getStringArray(final long a1, final int a2, final boolean a3) {
        /*SL:854*/return this.getStringArray(a1, a2, a3 ? "--WIDE-STRING--" : Native.getDefaultStringEncoding());
    }
    
    public String[] getStringArray(final long v-6, final int v-4, final String v-3) {
        final List<String> list = /*EL:865*/new ArrayList<String>();
        int v0 = /*EL:867*/0;
        /*SL:868*/if (v-4 != -1) {
            Pointer a3 = /*EL:869*/this.getPointer(v-6 + v0);
            int a2 = /*EL:870*/0;
            /*SL:871*/while (a2++ < v-4) {
                /*SL:875*/a3 = ((a3 == null) ? null : ("--WIDE-STRING--".equals(v-3) ? a3.getWideString(0L) : a3.getString(0L, v-3)));
                /*SL:876*/list.add(a3);
                /*SL:877*/if (a2 < v-4) {
                    /*SL:878*/v0 += Pointer.SIZE;
                    /*SL:879*/a3 = this.getPointer(v-6 + v0);
                }
            }
        }
        else {
            Pointer pointer;
            /*SL:883*/while ((pointer = this.getPointer(v-6 + v0)) != null) {
                final String v = /*EL:884*/(pointer == null) ? null : ("--WIDE-STRING--".equals(v-3) ? /*EL:886*/pointer.getWideString(0L) : /*EL:887*/pointer.getString(0L, v-3));
                /*SL:888*/list.add(v);
                /*SL:889*/v0 += Pointer.SIZE;
            }
        }
        /*SL:892*/return list.<String>toArray(new String[list.size()]);
    }
    
    void setValue(final long v-4, final Object v-2, final Class<?> v-1) {
        /*SL:902*/if (v-1 == Boolean.TYPE || v-1 == Boolean.class) {
            /*SL:903*/this.setInt(v-4, Boolean.TRUE.equals(v-2) ? -1 : 0);
        }
        else/*SL:904*/ if (v-1 == Byte.TYPE || v-1 == Byte.class) {
            /*SL:905*/this.setByte(v-4, (byte)((v-2 == null) ? 0 : ((byte)v-2)));
        }
        else/*SL:906*/ if (v-1 == Short.TYPE || v-1 == Short.class) {
            /*SL:907*/this.setShort(v-4, (short)((v-2 == null) ? 0 : ((short)v-2)));
        }
        else/*SL:908*/ if (v-1 == Character.TYPE || v-1 == Character.class) {
            /*SL:909*/this.setChar(v-4, (v-2 == null) ? '\0' : ((char)v-2));
        }
        else/*SL:910*/ if (v-1 == Integer.TYPE || v-1 == Integer.class) {
            /*SL:911*/this.setInt(v-4, (v-2 == null) ? 0 : ((int)v-2));
        }
        else/*SL:912*/ if (v-1 == Long.TYPE || v-1 == Long.class) {
            /*SL:913*/this.setLong(v-4, (v-2 == null) ? 0L : ((long)v-2));
        }
        else/*SL:914*/ if (v-1 == Float.TYPE || v-1 == Float.class) {
            /*SL:915*/this.setFloat(v-4, (v-2 == null) ? 0.0f : ((float)v-2));
        }
        else/*SL:916*/ if (v-1 == Double.TYPE || v-1 == Double.class) {
            /*SL:917*/this.setDouble(v-4, (v-2 == null) ? 0.0 : ((double)v-2));
        }
        else/*SL:918*/ if (v-1 == Pointer.class) {
            /*SL:919*/this.setPointer(v-4, (Pointer)v-2);
        }
        else/*SL:920*/ if (v-1 == String.class) {
            /*SL:921*/this.setPointer(v-4, (Pointer)v-2);
        }
        else/*SL:922*/ if (v-1 == WString.class) {
            /*SL:923*/this.setPointer(v-4, (Pointer)v-2);
        }
        else/*SL:924*/ if (Structure.class.isAssignableFrom(v-1)) {
            final Structure a1 = /*EL:925*/(Structure)v-2;
            /*SL:926*/if (Structure.ByReference.class.isAssignableFrom(v-1)) {
                /*SL:927*/this.setPointer(v-4, (a1 == null) ? null : a1.getPointer());
                /*SL:928*/if (a1 != null) {
                    /*SL:929*/a1.autoWrite();
                }
            }
            else {
                /*SL:933*/a1.useMemory(this, (int)v-4, true);
                /*SL:934*/a1.write();
            }
        }
        else/*SL:936*/ if (Callback.class.isAssignableFrom(v-1)) {
            /*SL:937*/this.setPointer(v-4, CallbackReference.getFunctionPointer((Callback)v-2));
        }
        else/*SL:938*/ if (Platform.HAS_BUFFERS && Buffer.class.isAssignableFrom(v-1)) {
            final Pointer a2 = /*EL:939*/(v-2 == null) ? null : /*EL:940*/Native.getDirectBufferPointer((Buffer)v-2);
            /*SL:941*/this.setPointer(v-4, a2);
        }
        else/*SL:942*/ if (NativeMapped.class.isAssignableFrom(v-1)) {
            final NativeMappedConverter a3 = /*EL:943*/NativeMappedConverter.getInstance(v-1);
            final Class<?> v1 = /*EL:944*/a3.nativeType();
            /*SL:945*/this.setValue(v-4, a3.toNative(v-2, new ToNativeContext()), v1);
        }
        else {
            /*SL:946*/if (!v-1.isArray()) {
                /*SL:949*/throw new IllegalArgumentException("Writing " + v-1 + " to memory is not supported");
            }
            this.writeArray(v-4, v-2, v-1.getComponentType());
        }
    }
    
    private void writeArray(final long v-3, final Object v-1, final Class<?> v0) {
        /*SL:955*/if (v0 == Byte.TYPE) {
            final byte[] a1 = /*EL:956*/(byte[])v-1;
            /*SL:957*/this.write(v-3, a1, 0, a1.length);
        }
        else/*SL:958*/ if (v0 == Short.TYPE) {
            final short[] a2 = /*EL:959*/(short[])v-1;
            /*SL:960*/this.write(v-3, a2, 0, a2.length);
        }
        else/*SL:961*/ if (v0 == Character.TYPE) {
            final char[] a3 = /*EL:962*/(char[])v-1;
            /*SL:963*/this.write(v-3, a3, 0, a3.length);
        }
        else/*SL:964*/ if (v0 == Integer.TYPE) {
            final int[] v = /*EL:965*/(int[])v-1;
            /*SL:966*/this.write(v-3, v, 0, v.length);
        }
        else/*SL:967*/ if (v0 == Long.TYPE) {
            final long[] v2 = /*EL:968*/(long[])v-1;
            /*SL:969*/this.write(v-3, v2, 0, v2.length);
        }
        else/*SL:970*/ if (v0 == Float.TYPE) {
            final float[] v3 = /*EL:971*/(float[])v-1;
            /*SL:972*/this.write(v-3, v3, 0, v3.length);
        }
        else/*SL:973*/ if (v0 == Double.TYPE) {
            final double[] v4 = /*EL:974*/(double[])v-1;
            /*SL:975*/this.write(v-3, v4, 0, v4.length);
        }
        else/*SL:976*/ if (Pointer.class.isAssignableFrom(v0)) {
            final Pointer[] v5 = /*EL:977*/(Pointer[])v-1;
            /*SL:978*/this.write(v-3, v5, 0, v5.length);
        }
        else/*SL:979*/ if (Structure.class.isAssignableFrom(v0)) {
            final Structure[] v6 = /*EL:980*/(Structure[])v-1;
            /*SL:981*/if (Structure.ByReference.class.isAssignableFrom(v0)) {
                final Pointer[] v7 = /*EL:982*/new Pointer[v6.length];
                /*SL:983*/for (int v8 = 0; v8 < v6.length; ++v8) {
                    /*SL:984*/if (v6[v8] == null) {
                        /*SL:985*/v7[v8] = null;
                    }
                    else {
                        /*SL:987*/v7[v8] = v6[v8].getPointer();
                        /*SL:988*/v6[v8].write();
                    }
                }
                /*SL:991*/this.write(v-3, v7, 0, v7.length);
            }
            else {
                Structure v9 = /*EL:993*/v6[0];
                /*SL:994*/if (v9 == null) {
                    /*SL:995*/v9 = Structure.newInstance(v0, this.share(v-3));
                    /*SL:996*/v6[0] = v9;
                }
                else {
                    /*SL:998*/v9.useMemory(this, (int)v-3, true);
                }
                /*SL:1000*/v9.write();
                final Structure[] v10 = /*EL:1001*/v9.toArray(v6.length);
                /*SL:1002*/for (int v11 = 1; v11 < v6.length; ++v11) {
                    /*SL:1003*/if (v6[v11] == null) {
                        /*SL:1004*/v6[v11] = v10[v11];
                    }
                    else {
                        /*SL:1006*/v6[v11].useMemory(this, (int)(v-3 + v11 * v6[v11].size()), true);
                    }
                    /*SL:1008*/v6[v11].write();
                }
            }
        }
        else {
            /*SL:1011*/if (!NativeMapped.class.isAssignableFrom(v0)) {
                /*SL:1021*/throw new IllegalArgumentException("Writing array of " + v0 + " to memory not supported");
            }
            final NativeMapped[] v12 = (NativeMapped[])v-1;
            final NativeMappedConverter v13 = NativeMappedConverter.getInstance(v0);
            final Class<?> v14 = v13.nativeType();
            final int v11 = Native.getNativeSize(v-1.getClass(), v-1) / v12.length;
            for (int v15 = 0; v15 < v12.length; ++v15) {
                final Object v16 = v13.toNative(v12[v15], new ToNativeContext());
                this.setValue(v-3 + v15 * v11, v16, v14);
            }
        }
    }
    
    public void setMemory(final long a1, final long a2, final byte a3) {
        /*SL:1032*/Native.setMemory(this, this.peer, a1, a2, a3);
    }
    
    public void setByte(final long a1, final byte a2) {
        /*SL:1045*/Native.setByte(this, this.peer, a1, a2);
    }
    
    public void setShort(final long a1, final short a2) {
        /*SL:1058*/Native.setShort(this, this.peer, a1, a2);
    }
    
    public void setChar(final long a1, final char a2) {
        /*SL:1071*/Native.setChar(this, this.peer, a1, a2);
    }
    
    public void setInt(final long a1, final int a2) {
        /*SL:1084*/Native.setInt(this, this.peer, a1, a2);
    }
    
    public void setLong(final long a1, final long a2) {
        /*SL:1097*/Native.setLong(this, this.peer, a1, a2);
    }
    
    public void setNativeLong(final long a1, final NativeLong a2) {
        /*SL:1110*/if (NativeLong.SIZE == 8) {
            /*SL:1111*/this.setLong(a1, a2.longValue());
        }
        else {
            /*SL:1113*/this.setInt(a1, a2.intValue());
        }
    }
    
    public void setFloat(final long a1, final float a2) {
        /*SL:1127*/Native.setFloat(this, this.peer, a1, a2);
    }
    
    public void setDouble(final long a1, final double a2) {
        /*SL:1140*/Native.setDouble(this, this.peer, a1, a2);
    }
    
    public void setPointer(final long a1, final Pointer a2) {
        /*SL:1155*/Native.setPointer(this, this.peer, a1, (a2 != null) ? a2.peer : 0L);
    }
    
    @Deprecated
    public void setString(final long a1, final String a2, final boolean a3) {
        /*SL:1173*/if (a3) {
            /*SL:1174*/this.setWideString(a1, a2);
        }
        else {
            /*SL:1177*/this.setString(a1, a2);
        }
    }
    
    public void setWideString(final long a1, final String a2) {
        /*SL:1190*/Native.setWideString(this, this.peer, a1, a2);
    }
    
    public void setString(final long a1, final WString a2) {
        /*SL:1202*/this.setWideString(a1, (a2 == null) ? null : a2.toString());
    }
    
    public void setString(final long a1, final String a2) {
        /*SL:1215*/this.setString(a1, a2, Native.getDefaultStringEncoding());
    }
    
    public void setString(final long a1, final String a2, final String a3) {
        final byte[] v1 = /*EL:1228*/Native.getBytes(a2, a3);
        /*SL:1229*/this.write(a1, v1, 0, v1.length);
        /*SL:1230*/this.setByte(a1 + v1.length, (byte)0);
    }
    
    public String dump(final long v2, final int v4) {
        final int v5 = /*EL:1235*/4;
        final String v6 = /*EL:1236*/"memory dump";
        final StringWriter v7 = /*EL:1238*/new StringWriter("memory dump".length() + 2 + v4 * 2 + v4 / 4 * 4);
        final PrintWriter v8 = /*EL:1239*/new PrintWriter(v7);
        /*SL:1240*/v8.println("memory dump");
        /*SL:1242*/for (byte a2 = 0; a2 < v4; ++a2) {
            /*SL:1244*/a2 = this.getByte(v2 + a2);
            /*SL:1245*/if (a2 % 4 == 0) {
                v8.print("[");
            }
            /*SL:1246*/if (a2 >= 0 && a2 < 16) {
                /*SL:1247*/v8.print("0");
            }
            /*SL:1248*/v8.print(Integer.toHexString(a2 & 0xFF));
            /*SL:1249*/if (a2 % 4 == 3 && a2 < v4 - 1) {
                /*SL:1250*/v8.println("]");
            }
        }
        /*SL:1252*/if (v7.getBuffer().charAt(v7.getBuffer().length() - 2) != ']') {
            /*SL:1253*/v8.println("]");
        }
        /*SL:1255*/return v7.toString();
    }
    
    @Override
    public String toString() {
        /*SL:1260*/return "native@0x" + Long.toHexString(this.peer);
    }
    
    public static long nativeValue(final Pointer a1) {
        /*SL:1265*/return (a1 == null) ? 0L : a1.peer;
    }
    
    public static void nativeValue(final Pointer a1, final long a2) {
        /*SL:1270*/a1.peer = a2;
    }
    
    static {
        if ((SIZE = Native.POINTER_SIZE) == 0) {
            throw new Error("Native library not initialized");
        }
        NULL = null;
    }
    
    private static class Opaque extends Pointer
    {
        private final String MSG;
        
        private Opaque(final long a1) {
            super(a1);
            this.MSG = "This pointer is opaque: " + this;
        }
        
        @Override
        public Pointer share(final long a1, final long a2) {
            /*SL:1279*/throw new UnsupportedOperationException(this.MSG);
        }
        
        @Override
        public void clear(final long a1) {
            /*SL:1283*/throw new UnsupportedOperationException(this.MSG);
        }
        
        @Override
        public long indexOf(final long a1, final byte a2) {
            /*SL:1287*/throw new UnsupportedOperationException(this.MSG);
        }
        
        @Override
        public void read(final long a1, final byte[] a2, final int a3, final int a4) {
            /*SL:1291*/throw new UnsupportedOperationException(this.MSG);
        }
        
        @Override
        public void read(final long a1, final char[] a2, final int a3, final int a4) {
            /*SL:1295*/throw new UnsupportedOperationException(this.MSG);
        }
        
        @Override
        public void read(final long a1, final short[] a2, final int a3, final int a4) {
            /*SL:1299*/throw new UnsupportedOperationException(this.MSG);
        }
        
        @Override
        public void read(final long a1, final int[] a2, final int a3, final int a4) {
            /*SL:1303*/throw new UnsupportedOperationException(this.MSG);
        }
        
        @Override
        public void read(final long a1, final long[] a2, final int a3, final int a4) {
            /*SL:1307*/throw new UnsupportedOperationException(this.MSG);
        }
        
        @Override
        public void read(final long a1, final float[] a2, final int a3, final int a4) {
            /*SL:1311*/throw new UnsupportedOperationException(this.MSG);
        }
        
        @Override
        public void read(final long a1, final double[] a2, final int a3, final int a4) {
            /*SL:1315*/throw new UnsupportedOperationException(this.MSG);
        }
        
        @Override
        public void read(final long a1, final Pointer[] a2, final int a3, final int a4) {
            /*SL:1319*/throw new UnsupportedOperationException(this.MSG);
        }
        
        @Override
        public void write(final long a1, final byte[] a2, final int a3, final int a4) {
            /*SL:1323*/throw new UnsupportedOperationException(this.MSG);
        }
        
        @Override
        public void write(final long a1, final char[] a2, final int a3, final int a4) {
            /*SL:1327*/throw new UnsupportedOperationException(this.MSG);
        }
        
        @Override
        public void write(final long a1, final short[] a2, final int a3, final int a4) {
            /*SL:1331*/throw new UnsupportedOperationException(this.MSG);
        }
        
        @Override
        public void write(final long a1, final int[] a2, final int a3, final int a4) {
            /*SL:1335*/throw new UnsupportedOperationException(this.MSG);
        }
        
        @Override
        public void write(final long a1, final long[] a2, final int a3, final int a4) {
            /*SL:1339*/throw new UnsupportedOperationException(this.MSG);
        }
        
        @Override
        public void write(final long a1, final float[] a2, final int a3, final int a4) {
            /*SL:1343*/throw new UnsupportedOperationException(this.MSG);
        }
        
        @Override
        public void write(final long a1, final double[] a2, final int a3, final int a4) {
            /*SL:1347*/throw new UnsupportedOperationException(this.MSG);
        }
        
        @Override
        public void write(final long a1, final Pointer[] a2, final int a3, final int a4) {
            /*SL:1351*/throw new UnsupportedOperationException(this.MSG);
        }
        
        @Override
        public ByteBuffer getByteBuffer(final long a1, final long a2) {
            /*SL:1355*/throw new UnsupportedOperationException(this.MSG);
        }
        
        @Override
        public byte getByte(final long a1) {
            /*SL:1359*/throw new UnsupportedOperationException(this.MSG);
        }
        
        @Override
        public char getChar(final long a1) {
            /*SL:1363*/throw new UnsupportedOperationException(this.MSG);
        }
        
        @Override
        public short getShort(final long a1) {
            /*SL:1367*/throw new UnsupportedOperationException(this.MSG);
        }
        
        @Override
        public int getInt(final long a1) {
            /*SL:1371*/throw new UnsupportedOperationException(this.MSG);
        }
        
        @Override
        public long getLong(final long a1) {
            /*SL:1375*/throw new UnsupportedOperationException(this.MSG);
        }
        
        @Override
        public float getFloat(final long a1) {
            /*SL:1379*/throw new UnsupportedOperationException(this.MSG);
        }
        
        @Override
        public double getDouble(final long a1) {
            /*SL:1383*/throw new UnsupportedOperationException(this.MSG);
        }
        
        @Override
        public Pointer getPointer(final long a1) {
            /*SL:1387*/throw new UnsupportedOperationException(this.MSG);
        }
        
        @Override
        public String getString(final long a1, final String a2) {
            /*SL:1391*/throw new UnsupportedOperationException(this.MSG);
        }
        
        @Override
        public String getWideString(final long a1) {
            /*SL:1395*/throw new UnsupportedOperationException(this.MSG);
        }
        
        @Override
        public void setByte(final long a1, final byte a2) {
            /*SL:1399*/throw new UnsupportedOperationException(this.MSG);
        }
        
        @Override
        public void setChar(final long a1, final char a2) {
            /*SL:1403*/throw new UnsupportedOperationException(this.MSG);
        }
        
        @Override
        public void setShort(final long a1, final short a2) {
            /*SL:1407*/throw new UnsupportedOperationException(this.MSG);
        }
        
        @Override
        public void setInt(final long a1, final int a2) {
            /*SL:1411*/throw new UnsupportedOperationException(this.MSG);
        }
        
        @Override
        public void setLong(final long a1, final long a2) {
            /*SL:1415*/throw new UnsupportedOperationException(this.MSG);
        }
        
        @Override
        public void setFloat(final long a1, final float a2) {
            /*SL:1419*/throw new UnsupportedOperationException(this.MSG);
        }
        
        @Override
        public void setDouble(final long a1, final double a2) {
            /*SL:1423*/throw new UnsupportedOperationException(this.MSG);
        }
        
        @Override
        public void setPointer(final long a1, final Pointer a2) {
            /*SL:1427*/throw new UnsupportedOperationException(this.MSG);
        }
        
        @Override
        public void setString(final long a1, final String a2, final String a3) {
            /*SL:1431*/throw new UnsupportedOperationException(this.MSG);
        }
        
        @Override
        public void setWideString(final long a1, final String a2) {
            /*SL:1435*/throw new UnsupportedOperationException(this.MSG);
        }
        
        @Override
        public void setMemory(final long a1, final long a2, final byte a3) {
            /*SL:1439*/throw new UnsupportedOperationException(this.MSG);
        }
        
        @Override
        public String dump(final long a1, final int a2) {
            /*SL:1443*/throw new UnsupportedOperationException(this.MSG);
        }
        
        @Override
        public String toString() {
            /*SL:1447*/return "const@0x" + Long.toHexString(this.peer);
        }
    }
}

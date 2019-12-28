package com.sun.jna;

public abstract class IntegerType extends Number implements NativeMapped
{
    private static final long serialVersionUID = 1L;
    private int size;
    private Number number;
    private boolean unsigned;
    private long value;
    
    public IntegerType(final int a1) {
        this(a1, 0L, false);
    }
    
    public IntegerType(final int a1, final boolean a2) {
        this(a1, 0L, a2);
    }
    
    public IntegerType(final int a1, final long a2) {
        this(a1, a2, false);
    }
    
    public IntegerType(final int a1, final long a2, final boolean a3) {
        this.size = a1;
        this.unsigned = a3;
        this.setValue(a2);
    }
    
    public void setValue(final long v2) {
        long v3 = /*EL:74*/v2;
        /*SL:75*/this.value = v2;
        /*SL:76*/switch (this.size) {
            case 1: {
                /*SL:78*/if (this.unsigned) {
                    this.value = (v2 & 0xFFL);
                }
                /*SL:79*/v3 = (byte)v2;
                /*SL:80*/this.number = (byte)v2;
                /*SL:81*/break;
            }
            case 2: {
                /*SL:83*/if (this.unsigned) {
                    this.value = (v2 & 0xFFFFL);
                }
                /*SL:84*/v3 = (short)v2;
                /*SL:85*/this.number = (short)v2;
                /*SL:86*/break;
            }
            case 4: {
                /*SL:88*/if (this.unsigned) {
                    this.value = (v2 & 0xFFFFFFFFL);
                }
                /*SL:89*/v3 = (int)v2;
                /*SL:90*/this.number = (int)v2;
                /*SL:91*/break;
            }
            case 8: {
                /*SL:93*/this.number = v2;
                /*SL:94*/break;
            }
            default: {
                /*SL:96*/throw new IllegalArgumentException("Unsupported size: " + this.size);
            }
        }
        /*SL:98*/if (this.size < 8) {
            final long a1 = /*EL:99*/~((1L << this.size * 8) - 1L);
            /*SL:100*/if ((v2 < 0L && v3 != v2) || (v2 >= 0L && (a1 & v2) != 0x0L)) {
                /*SL:102*/throw new IllegalArgumentException("Argument value 0x" + /*EL:103*/Long.toHexString(v2) + " exceeds native capacity (" + this.size + " bytes) mask=0x" + /*EL:104*/Long.toHexString(a1));
            }
        }
    }
    
    @Override
    public Object toNative() {
        /*SL:111*/return this.number;
    }
    
    @Override
    public Object fromNative(final Object v-3, final FromNativeContext v-2) {
        final long value = /*EL:117*/(v-3 == null) ? 0L : ((Number)v-3).longValue();
        try {
            final IntegerType a1 = /*EL:120*/(IntegerType)this.getClass().newInstance();
            /*SL:121*/a1.setValue(value);
            /*SL:122*/return a1;
        }
        catch (InstantiationException a2) {
            /*SL:125*/throw new IllegalArgumentException("Can't instantiate " + this.getClass());
        }
        catch (IllegalAccessException v1) {
            /*SL:129*/throw new IllegalArgumentException("Not allowed to instantiate " + this.getClass());
        }
    }
    
    @Override
    public Class<?> nativeType() {
        /*SL:136*/return this.number.getClass();
    }
    
    @Override
    public int intValue() {
        /*SL:141*/return (int)this.value;
    }
    
    @Override
    public long longValue() {
        /*SL:146*/return this.value;
    }
    
    @Override
    public float floatValue() {
        /*SL:151*/return this.number.floatValue();
    }
    
    @Override
    public double doubleValue() {
        /*SL:156*/return this.number.doubleValue();
    }
    
    @Override
    public boolean equals(final Object a1) {
        /*SL:161*/return a1 instanceof IntegerType && this.number.equals(((IntegerType)a1).number);
    }
    
    @Override
    public String toString() {
        /*SL:167*/return this.number.toString();
    }
    
    @Override
    public int hashCode() {
        /*SL:172*/return this.number.hashCode();
    }
    
    public static <T extends IntegerType> int compare(final T a1, final T a2) {
        /*SL:190*/if (a1 == a2) {
            /*SL:191*/return 0;
        }
        /*SL:192*/if (a1 == null) {
            /*SL:193*/return 1;
        }
        /*SL:194*/if (a2 == null) {
            /*SL:195*/return -1;
        }
        /*SL:197*/return compare(a1.longValue(), a2.longValue());
    }
    
    public static int compare(final IntegerType a1, final long a2) {
        /*SL:213*/if (a1 == null) {
            /*SL:214*/return 1;
        }
        /*SL:216*/return compare(a1.longValue(), a2);
    }
    
    public static final int compare(final long a1, final long a2) {
        /*SL:222*/if (a1 == a2) {
            /*SL:223*/return 0;
        }
        /*SL:224*/if (a1 < a2) {
            /*SL:225*/return -1;
        }
        /*SL:227*/return 1;
    }
}

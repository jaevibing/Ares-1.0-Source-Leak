package javassist.compiler.ast;

import javassist.compiler.CompileError;

public class IntConst extends ASTree
{
    protected long value;
    protected int type;
    
    public IntConst(final long a1, final int a2) {
        this.value = a1;
        this.type = a2;
    }
    
    public long get() {
        /*SL:31*/return this.value;
    }
    
    public void set(final long a1) {
        /*SL:33*/this.value = a1;
    }
    
    public int getType() {
        /*SL:37*/return this.type;
    }
    
    @Override
    public String toString() {
        /*SL:39*/return Long.toString(this.value);
    }
    
    @Override
    public void accept(final Visitor a1) throws CompileError {
        /*SL:42*/a1.atIntConst(this);
    }
    
    public ASTree compute(final int a1, final ASTree a2) {
        /*SL:46*/if (a2 instanceof IntConst) {
            /*SL:47*/return this.compute0(a1, (IntConst)a2);
        }
        /*SL:48*/if (a2 instanceof DoubleConst) {
            /*SL:49*/return this.compute0(a1, (DoubleConst)a2);
        }
        /*SL:51*/return null;
    }
    
    private IntConst compute0(final int v-8, final IntConst v-7) {
        final int type = /*EL:55*/this.type;
        final int type2 = /*EL:56*/v-7.type;
        int a3 = 0;
        /*SL:58*/if (type == 403 || type2 == 403) {
            final int a1 = /*EL:59*/403;
        }
        else/*SL:60*/ if (type == 401 && type2 == 401) {
            final int a2 = /*EL:62*/401;
        }
        else {
            /*SL:64*/a3 = 402;
        }
        final long value = /*EL:66*/this.value;
        final long value2 = /*EL:67*/v-7.value;
        long v1 = 0L;
        /*SL:69*/switch (v-8) {
            case 43: {
                /*SL:71*/v1 = value + value2;
                /*SL:72*/break;
            }
            case 45: {
                /*SL:74*/v1 = value - value2;
                /*SL:75*/break;
            }
            case 42: {
                /*SL:77*/v1 = value * value2;
                /*SL:78*/break;
            }
            case 47: {
                /*SL:80*/v1 = value / value2;
                /*SL:81*/break;
            }
            case 37: {
                /*SL:83*/v1 = value % value2;
                /*SL:84*/break;
            }
            case 124: {
                /*SL:86*/v1 = (value | value2);
                /*SL:87*/break;
            }
            case 94: {
                /*SL:89*/v1 = (value ^ value2);
                /*SL:90*/break;
            }
            case 38: {
                /*SL:92*/v1 = (value & value2);
                /*SL:93*/break;
            }
            case 364: {
                /*SL:95*/v1 = this.value << (int)value2;
                /*SL:96*/a3 = type;
                /*SL:97*/break;
            }
            case 366: {
                /*SL:99*/v1 = this.value >> (int)value2;
                /*SL:100*/a3 = type;
                /*SL:101*/break;
            }
            case 370: {
                /*SL:103*/v1 = this.value >>> (int)value2;
                /*SL:104*/a3 = type;
                /*SL:105*/break;
            }
            default: {
                /*SL:107*/return null;
            }
        }
        /*SL:110*/return new IntConst(v1, a3);
    }
    
    private DoubleConst compute0(final int v-5, final DoubleConst v-4) {
        final double n = /*EL:114*/this.value;
        final double value = /*EL:115*/v-4.value;
        double v1 = 0.0;
        /*SL:117*/switch (v-5) {
            case 43: {
                final double a1 = /*EL:119*/n + value;
                /*SL:120*/break;
            }
            case 45: {
                final double a2 = /*EL:122*/n - value;
                /*SL:123*/break;
            }
            case 42: {
                /*SL:125*/v1 = n * value;
                /*SL:126*/break;
            }
            case 47: {
                /*SL:128*/v1 = n / value;
                /*SL:129*/break;
            }
            case 37: {
                /*SL:131*/v1 = n % value;
                /*SL:132*/break;
            }
            default: {
                /*SL:134*/return null;
            }
        }
        /*SL:137*/return new DoubleConst(v1, v-4.type);
    }
}

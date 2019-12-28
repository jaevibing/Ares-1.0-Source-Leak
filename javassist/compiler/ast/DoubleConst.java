package javassist.compiler.ast;

import javassist.compiler.CompileError;

public class DoubleConst extends ASTree
{
    protected double value;
    protected int type;
    
    public DoubleConst(final double a1, final int a2) {
        this.value = a1;
        this.type = a2;
    }
    
    public double get() {
        /*SL:31*/return this.value;
    }
    
    public void set(final double a1) {
        /*SL:33*/this.value = a1;
    }
    
    public int getType() {
        /*SL:37*/return this.type;
    }
    
    @Override
    public String toString() {
        /*SL:39*/return Double.toString(this.value);
    }
    
    @Override
    public void accept(final Visitor a1) throws CompileError {
        /*SL:42*/a1.atDoubleConst(this);
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
    
    private DoubleConst compute0(final int v1, final DoubleConst v2) {
        final int v3;
        /*SL:56*/if (this.type == 405 || v2.type == 405) {
            final int a1 = /*EL:58*/405;
        }
        else {
            /*SL:60*/v3 = 404;
        }
        /*SL:62*/return compute(v1, this.value, v2.value, v3);
    }
    
    private DoubleConst compute0(final int a1, final IntConst a2) {
        /*SL:66*/return compute(a1, this.value, a2.value, this.type);
    }
    
    private static DoubleConst compute(final int v-5, final double v-4, final double v-2, final int v0) {
        final double v;
        /*SL:73*/switch (v-5) {
            case 43: {
                final double a1 = /*EL:75*/v-4 + v-2;
                /*SL:76*/break;
            }
            case 45: {
                final double a2 = /*EL:78*/v-4 - v-2;
                /*SL:79*/break;
            }
            case 42: {
                final double a3 = /*EL:81*/v-4 * v-2;
                /*SL:82*/break;
            }
            case 47: {
                final double a4 = /*EL:84*/v-4 / v-2;
                /*SL:85*/break;
            }
            case 37: {
                /*SL:87*/v = v-4 % v-2;
                /*SL:88*/break;
            }
            default: {
                /*SL:90*/return null;
            }
        }
        /*SL:93*/return new DoubleConst(v, v0);
    }
}

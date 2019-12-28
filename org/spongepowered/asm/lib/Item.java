package org.spongepowered.asm.lib;

final class Item
{
    int index;
    int type;
    int intVal;
    long longVal;
    String strVal1;
    String strVal2;
    String strVal3;
    int hashCode;
    Item next;
    
    Item() {
    }
    
    Item(final int a1) {
        this.index = a1;
    }
    
    Item(final int a1, final Item a2) {
        this.index = a1;
        this.type = a2.type;
        this.intVal = a2.intVal;
        this.longVal = a2.longVal;
        this.strVal1 = a2.strVal1;
        this.strVal2 = a2.strVal2;
        this.strVal3 = a2.strVal3;
        this.hashCode = a2.hashCode;
    }
    
    void set(final int a1) {
        /*SL:151*/this.type = 3;
        /*SL:152*/this.intVal = a1;
        /*SL:153*/this.hashCode = (Integer.MAX_VALUE & this.type + a1);
    }
    
    void set(final long a1) {
        /*SL:163*/this.type = 5;
        /*SL:164*/this.longVal = a1;
        /*SL:165*/this.hashCode = (Integer.MAX_VALUE & this.type + (int)a1);
    }
    
    void set(final float a1) {
        /*SL:175*/this.type = 4;
        /*SL:176*/this.intVal = Float.floatToRawIntBits(a1);
        /*SL:177*/this.hashCode = (Integer.MAX_VALUE & this.type + (int)a1);
    }
    
    void set(final double a1) {
        /*SL:187*/this.type = 6;
        /*SL:188*/this.longVal = Double.doubleToRawLongBits(a1);
        /*SL:189*/this.hashCode = (Integer.MAX_VALUE & this.type + (int)a1);
    }
    
    void set(final int a1, final String a2, final String a3, final String a4) {
        /*SL:207*/this.type = a1;
        /*SL:208*/this.strVal1 = a2;
        /*SL:209*/this.strVal2 = a3;
        /*SL:210*/this.strVal3 = a4;
        /*SL:211*/switch (a1) {
            case 7: {
                /*SL:213*/this.intVal = 0;
            }
            case 1:
            case 8:
            case 16:
            case 30: {
                /*SL:218*/this.hashCode = (Integer.MAX_VALUE & a1 + a2.hashCode());
            }
            case 12: {
                /*SL:221*/this.hashCode = (Integer.MAX_VALUE & a1 + a2.hashCode() * a3.hashCode());
            }
            default: {
                /*SL:230*/this.hashCode = (Integer.MAX_VALUE & a1 + a2.hashCode() * a3.hashCode() * /*EL:231*/a4.hashCode());
            }
        }
    }
    
    void set(final String a1, final String a2, final int a3) {
        /*SL:246*/this.type = 18;
        /*SL:247*/this.longVal = a3;
        /*SL:248*/this.strVal1 = a1;
        /*SL:249*/this.strVal2 = a2;
        /*SL:250*/this.hashCode = (Integer.MAX_VALUE & 18 + a3 * this.strVal1.hashCode() * /*EL:251*/this.strVal2.hashCode());
    }
    
    void set(final int a1, final int a2) {
        /*SL:265*/this.type = 33;
        /*SL:266*/this.intVal = a1;
        /*SL:267*/this.hashCode = a2;
    }
    
    boolean isEqualTo(final Item a1) {
        /*SL:281*/switch (this.type) {
            case 1:
            case 7:
            case 8:
            case 16:
            case 30: {
                /*SL:287*/return a1.strVal1.equals(this.strVal1);
            }
            case 5:
            case 6:
            case 32: {
                /*SL:291*/return a1.longVal == this.longVal;
            }
            case 3:
            case 4: {
                /*SL:294*/return a1.intVal == this.intVal;
            }
            case 31: {
                /*SL:296*/return a1.intVal == this.intVal && a1.strVal1.equals(this.strVal1);
            }
            case 12: {
                /*SL:298*/return a1.strVal1.equals(this.strVal1) && a1.strVal2.equals(this.strVal2);
            }
            case 18: {
                /*SL:300*/return a1.longVal == this.longVal && a1.strVal1.equals(this.strVal1) && a1.strVal2.equals(this.strVal2);
            }
            default: {
                /*SL:308*/return a1.strVal1.equals(this.strVal1) && a1.strVal2.equals(this.strVal2) && a1.strVal3.equals(this.strVal3);
            }
        }
    }
}

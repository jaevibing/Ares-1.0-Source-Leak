package javassist.convert;

import javassist.bytecode.BadBytecode;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.ConstPool;
import javassist.bytecode.CodeIterator;
import javassist.CtClass;
import javassist.CtField;

public final class TransformWriteField extends TransformReadField
{
    public TransformWriteField(final Transformer a1, final CtField a2, final String a3, final String a4) {
        super(a1, a2, a3, a4);
    }
    
    @Override
    public int transform(final CtClass v-8, int v-7, final CodeIterator v-6, final ConstPool v-5) throws BadBytecode {
        final int byte1 = /*EL:33*/v-6.byteAt(v-7);
        /*SL:34*/if (byte1 == 181 || byte1 == 179) {
            final int u16bit = /*EL:35*/v-6.u16bitAt(v-7 + 1);
            final String field = /*EL:36*/TransformReadField.isField(v-8.getClassPool(), v-5, this.fieldClass, this.fieldname, this.isPrivate, u16bit);
            /*SL:38*/if (field != null) {
                /*SL:39*/if (byte1 == 179) {
                    final CodeAttribute a1 = /*EL:40*/v-6.get();
                    /*SL:41*/v-6.move(v-7);
                    final char a2 = /*EL:42*/field.charAt(0);
                    /*SL:43*/if (a2 == 'J' || a2 == 'D') {
                        /*SL:45*/v-7 = v-6.insertGap(3);
                        /*SL:46*/v-6.writeByte(1, v-7);
                        /*SL:47*/v-6.writeByte(91, v-7 + 1);
                        /*SL:48*/v-6.writeByte(87, v-7 + 2);
                        /*SL:49*/a1.setMaxStack(a1.getMaxStack() + 2);
                    }
                    else {
                        /*SL:53*/v-7 = v-6.insertGap(2);
                        /*SL:54*/v-6.writeByte(1, v-7);
                        /*SL:55*/v-6.writeByte(95, v-7 + 1);
                        /*SL:56*/a1.setMaxStack(a1.getMaxStack() + 1);
                    }
                    /*SL:59*/v-7 = v-6.next();
                }
                final int a3 = /*EL:62*/v-5.addClassInfo(this.methodClassname);
                final String a4 = /*EL:63*/"(Ljava/lang/Object;" + field + ")V";
                final int v1 = /*EL:64*/v-5.addMethodrefInfo(a3, this.methodName, a4);
                /*SL:65*/v-6.writeByte(184, v-7);
                /*SL:66*/v-6.write16bit(v1, v-7 + 1);
            }
        }
        /*SL:70*/return v-7;
    }
}

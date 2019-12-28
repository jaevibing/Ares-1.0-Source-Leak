package javassist.convert;

import javassist.bytecode.BadBytecode;
import javassist.bytecode.CodeIterator;
import javassist.NotFoundException;
import javassist.bytecode.ConstPool;
import javassist.ClassPool;
import javassist.Modifier;
import javassist.CtField;
import javassist.CtClass;

public class TransformReadField extends Transformer
{
    protected String fieldname;
    protected CtClass fieldClass;
    protected boolean isPrivate;
    protected String methodClassname;
    protected String methodName;
    
    public TransformReadField(final Transformer a1, final CtField a2, final String a3, final String a4) {
        super(a1);
        this.fieldClass = a2.getDeclaringClass();
        this.fieldname = a2.getName();
        this.methodClassname = a3;
        this.methodName = a4;
        this.isPrivate = Modifier.isPrivate(a2.getModifiers());
    }
    
    static String isField(final ClassPool a2, final ConstPool a3, final CtClass a4, final String a5, final boolean a6, final int v1) {
        /*SL:45*/if (!a3.getFieldrefName(v1).equals(a5)) {
            /*SL:46*/return null;
        }
        try {
            final CtClass a7 = /*EL:49*/a2.get(a3.getFieldrefClassName(v1));
            /*SL:50*/if (a7 == a4 || (!a6 && isFieldInSuper(a7, a4, a5))) {
                /*SL:51*/return a3.getFieldrefType(v1);
            }
        }
        catch (NotFoundException ex) {}
        /*SL:54*/return null;
    }
    
    static boolean isFieldInSuper(final CtClass a2, final CtClass a3, final String v1) {
        /*SL:58*/if (!a2.subclassOf(a3)) {
            /*SL:59*/return false;
        }
        try {
            final CtField a4 = /*EL:62*/a2.getField(v1);
            /*SL:63*/return a4.getDeclaringClass() == a3;
        }
        catch (NotFoundException ex) {
            /*SL:66*/return false;
        }
    }
    
    @Override
    public int transform(final CtClass v-5, int v-4, final CodeIterator v-3, final ConstPool v-2) throws BadBytecode {
        final int byte1 = /*EL:72*/v-3.byteAt(v-4);
        /*SL:73*/if (byte1 == 180 || byte1 == 178) {
            int a4 = /*EL:74*/v-3.u16bitAt(v-4 + 1);
            final String v1 = isField(/*EL:75*/v-5.getClassPool(), v-2, this.fieldClass, this.fieldname, this.isPrivate, a4);
            /*SL:77*/if (v1 != null) {
                /*SL:78*/if (byte1 == 178) {
                    /*SL:79*/v-3.move(v-4);
                    /*SL:80*/v-4 = v-3.insertGap(1);
                    /*SL:81*/v-3.writeByte(1, v-4);
                    /*SL:82*/v-4 = v-3.next();
                }
                final String a2 = /*EL:85*/"(Ljava/lang/Object;)" + v1;
                final int a3 = /*EL:86*/v-2.addClassInfo(this.methodClassname);
                /*SL:87*/a4 = v-2.addMethodrefInfo(a3, this.methodName, a2);
                /*SL:88*/v-3.writeByte(184, v-4);
                /*SL:89*/v-3.write16bit(a4, v-4 + 1);
                /*SL:90*/return v-4;
            }
        }
        /*SL:94*/return v-4;
    }
}

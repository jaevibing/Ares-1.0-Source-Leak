package javassist.convert;

import javassist.CannotCompileException;
import javassist.bytecode.CodeIterator;
import javassist.CtClass;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.ConstPool;

public final class TransformNewClass extends Transformer
{
    private int nested;
    private String classname;
    private String newClassName;
    private int newClassIndex;
    private int newMethodNTIndex;
    private int newMethodIndex;
    
    public TransformNewClass(final Transformer a1, final String a2, final String a3) {
        super(a1);
        this.classname = a2;
        this.newClassName = a3;
    }
    
    @Override
    public void initialize(final ConstPool a1, final CodeAttribute a2) {
        /*SL:36*/this.nested = 0;
        final boolean newClassIndex = /*EL:37*/false;
        this.newMethodIndex = (newClassIndex ? 1 : 0);
        this.newMethodNTIndex = (newClassIndex ? 1 : 0);
        this.newClassIndex = (newClassIndex ? 1 : 0);
    }
    
    @Override
    public int transform(final CtClass v2, final int v3, final CodeIterator v4, final ConstPool v5) throws CannotCompileException {
        final int v6 = /*EL:51*/v4.byteAt(v3);
        /*SL:52*/if (v6 == 187) {
            final int a1 = /*EL:53*/v4.u16bitAt(v3 + 1);
            /*SL:54*/if (v5.getClassInfo(a1).equals(this.classname)) {
                /*SL:55*/if (v4.byteAt(v3 + 3) != 89) {
                    /*SL:56*/throw new CannotCompileException("NEW followed by no DUP was found");
                }
                /*SL:59*/if (this.newClassIndex == 0) {
                    /*SL:60*/this.newClassIndex = v5.addClassInfo(this.newClassName);
                }
                /*SL:62*/v4.write16bit(this.newClassIndex, v3 + 1);
                /*SL:63*/++this.nested;
            }
        }
        else/*SL:66*/ if (v6 == 183) {
            final int a2 = /*EL:67*/v4.u16bitAt(v3 + 1);
            final int a3 = /*EL:68*/v5.isConstructor(this.classname, a2);
            /*SL:69*/if (a3 != 0 && this.nested > 0) {
                final int a4 = /*EL:70*/v5.getMethodrefNameAndType(a2);
                /*SL:71*/if (this.newMethodNTIndex != a4) {
                    /*SL:72*/this.newMethodNTIndex = a4;
                    /*SL:73*/this.newMethodIndex = v5.addMethodrefInfo(this.newClassIndex, a4);
                }
                /*SL:76*/v4.write16bit(this.newMethodIndex, v3 + 1);
                /*SL:77*/--this.nested;
            }
        }
        /*SL:81*/return v3;
    }
}

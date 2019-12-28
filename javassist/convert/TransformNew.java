package javassist.convert;

import javassist.bytecode.Descriptor;
import javassist.bytecode.StackMap;
import javassist.bytecode.StackMapTable;
import javassist.CannotCompileException;
import javassist.bytecode.CodeIterator;
import javassist.CtClass;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.ConstPool;

public final class TransformNew extends Transformer
{
    private int nested;
    private String classname;
    private String trapClass;
    private String trapMethod;
    
    public TransformNew(final Transformer a1, final String a2, final String a3, final String a4) {
        super(a1);
        this.classname = a2;
        this.trapClass = a3;
        this.trapMethod = a4;
    }
    
    @Override
    public void initialize(final ConstPool a1, final CodeAttribute a2) {
        /*SL:36*/this.nested = 0;
    }
    
    @Override
    public int transform(final CtClass v-5, final int v-4, final CodeIterator v-3, final ConstPool v-2) throws CannotCompileException {
        final int v0 = /*EL:55*/v-3.byteAt(v-4);
        /*SL:56*/if (v0 == 187) {
            int a3 = /*EL:57*/v-3.u16bitAt(v-4 + 1);
            /*SL:58*/if (v-2.getClassInfo(a3).equals(this.classname)) {
                /*SL:59*/if (v-3.byteAt(v-4 + 3) != 89) {
                    /*SL:60*/throw new CannotCompileException("NEW followed by no DUP was found");
                }
                /*SL:63*/v-3.writeByte(0, v-4);
                /*SL:64*/v-3.writeByte(0, v-4 + 1);
                /*SL:65*/v-3.writeByte(0, v-4 + 2);
                /*SL:66*/v-3.writeByte(0, v-4 + 3);
                /*SL:67*/++this.nested;
                final StackMapTable a2 = /*EL:69*/(StackMapTable)v-3.get().getAttribute(/*EL:70*/"StackMapTable");
                /*SL:71*/if (a2 != null) {
                    /*SL:72*/a2.removeNew(v-4);
                }
                /*SL:75*/a3 = (StackMap)v-3.get().getAttribute("StackMap");
                /*SL:76*/if (a3 != null) {
                    /*SL:77*/a3.removeNew(v-4);
                }
            }
        }
        else/*SL:80*/ if (v0 == 183) {
            final int u16bit = /*EL:81*/v-3.u16bitAt(v-4 + 1);
            final int v = /*EL:82*/v-2.isConstructor(this.classname, u16bit);
            /*SL:83*/if (v != 0 && this.nested > 0) {
                final int a4 = /*EL:84*/this.computeMethodref(v, v-2);
                /*SL:85*/v-3.writeByte(184, v-4);
                /*SL:86*/v-3.write16bit(a4, v-4 + 1);
                /*SL:87*/--this.nested;
            }
        }
        /*SL:91*/return v-4;
    }
    
    private int computeMethodref(int a1, final ConstPool a2) {
        final int v1 = /*EL:95*/a2.addClassInfo(this.trapClass);
        final int v2 = /*EL:96*/a2.addUtf8Info(this.trapMethod);
        /*SL:97*/a1 = a2.addUtf8Info(/*EL:98*/Descriptor.changeReturnType(this.classname, a2.getUtf8Info(a1)));
        /*SL:100*/return a2.addMethodrefInfo(v1, a2.addNameAndTypeInfo(v2, a1));
    }
}

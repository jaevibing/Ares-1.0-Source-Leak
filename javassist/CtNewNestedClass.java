package javassist;

import javassist.bytecode.ClassFile;
import javassist.bytecode.InnerClassesAttribute;

class CtNewNestedClass extends CtNewClass
{
    CtNewNestedClass(final String a1, final ClassPool a2, final boolean a3, final CtClass a4) {
        super(a1, a2, a3, a4);
    }
    
    @Override
    public void setModifiers(int a1) {
        /*SL:36*/a1 &= 0xFFFFFFF7;
        /*SL:37*/super.setModifiers(a1);
        updateInnerEntry(/*EL:38*/a1, this.getName(), this, true);
    }
    
    private static void updateInnerEntry(final int v-6, final String v-5, final CtClass v-4, final boolean v-3) {
        final ClassFile classFile2 = /*EL:42*/v-4.getClassFile2();
        final InnerClassesAttribute innerClassesAttribute = /*EL:43*/(InnerClassesAttribute)classFile2.getAttribute("InnerClasses");
        /*SL:45*/if (innerClassesAttribute == null) {
            /*SL:46*/return;
        }
        /*SL:49*/for (int v0 = innerClassesAttribute.tableLength(), v = 0; v < v0; ++v) {
            /*SL:50*/if (v-5.equals(innerClassesAttribute.innerClass(v))) {
                int a3 = /*EL:51*/innerClassesAttribute.accessFlags(v) & 0x8;
                /*SL:52*/innerClassesAttribute.setAccessFlags(v, v-6 | a3);
                final String a2 = /*EL:53*/innerClassesAttribute.outerClass(v);
                /*SL:54*/if (a2 == null || !v-3) {
                    break;
                }
                try {
                    /*SL:56*/a3 = v-4.getClassPool().get(a2);
                    updateInnerEntry(/*EL:57*/v-6, v-5, a3, false);
                    /*SL:62*/break;
                }
                catch (NotFoundException a4) {
                    throw new RuntimeException("cannot find the declaring class: " + a2);
                }
            }
        }
    }
}

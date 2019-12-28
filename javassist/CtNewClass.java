package javassist;

import java.io.IOException;
import java.io.DataOutputStream;
import javassist.bytecode.ClassFile;

class CtNewClass extends CtClassType
{
    protected boolean hasConstructor;
    
    CtNewClass(final String a3, final ClassPool a4, final boolean v1, final CtClass v2) {
        super(a3, a4);
        this.wasChanged = true;
        final String v3;
        if (v1 || v2 == null) {
            final String a5 = null;
        }
        else {
            v3 = v2.getName();
        }
        this.classfile = new ClassFile(v1, a3, v3);
        if (v1 && v2 != null) {
            this.classfile.setInterfaces(new String[] { v2.getName() });
        }
        this.setModifiers(Modifier.setPublic(this.getModifiers()));
        this.hasConstructor = v1;
    }
    
    @Override
    protected void extendToString(final StringBuffer a1) {
        /*SL:47*/if (this.hasConstructor) {
            /*SL:48*/a1.append("hasConstructor ");
        }
        /*SL:50*/super.extendToString(a1);
    }
    
    @Override
    public void addConstructor(final CtConstructor a1) throws CannotCompileException {
        /*SL:56*/this.hasConstructor = true;
        /*SL:57*/super.addConstructor(a1);
    }
    
    @Override
    public void toBytecode(final DataOutputStream v2) throws CannotCompileException, IOException {
        /*SL:63*/if (!this.hasConstructor) {
            try {
                /*SL:65*/this.inheritAllConstructors();
                /*SL:66*/this.hasConstructor = true;
            }
            catch (NotFoundException a1) {
                /*SL:69*/throw new CannotCompileException(a1);
            }
        }
        /*SL:72*/super.toBytecode(v2);
    }
    
    public void inheritAllConstructors() throws CannotCompileException, NotFoundException {
        final CtClass superclass = /*EL:88*/this.getSuperclass();
        final CtConstructor[] declaredConstructors = /*EL:89*/superclass.getDeclaredConstructors();
        int n = /*EL:91*/0;
        /*SL:92*/for (int i = 0; i < declaredConstructors.length; ++i) {
            final CtConstructor ctConstructor = /*EL:93*/declaredConstructors[i];
            final int v0 = /*EL:94*/ctConstructor.getModifiers();
            /*SL:95*/if (this.isInheritable(v0, superclass)) {
                final CtConstructor v = /*EL:97*/CtNewConstructor.make(ctConstructor.getParameterTypes(), ctConstructor.getExceptionTypes(), /*EL:98*/this);
                /*SL:99*/v.setModifiers(v0 & 0x7);
                /*SL:100*/this.addConstructor(v);
                /*SL:101*/++n;
            }
        }
        /*SL:105*/if (n < 1) {
            /*SL:106*/throw new CannotCompileException("no inheritable constructor in " + superclass.getName());
        }
    }
    
    private boolean isInheritable(final int v2, final CtClass v3) {
        /*SL:112*/if (Modifier.isPrivate(v2)) {
            /*SL:113*/return false;
        }
        /*SL:115*/if (!Modifier.isPackage(v2)) {
            /*SL:124*/return true;
        }
        final String a1 = this.getPackageName();
        final String a2 = v3.getPackageName();
        if (a1 == null) {
            return a2 == null;
        }
        return a1.equals(a2);
    }
}

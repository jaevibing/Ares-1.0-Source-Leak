package javassist.convert;

import javassist.NotFoundException;
import javassist.ClassPool;
import javassist.bytecode.BadBytecode;
import javassist.bytecode.CodeIterator;
import javassist.CtClass;
import javassist.bytecode.CodeAttribute;
import javassist.Modifier;
import javassist.CtMethod;
import javassist.bytecode.ConstPool;

public class TransformCall extends Transformer
{
    protected String classname;
    protected String methodname;
    protected String methodDescriptor;
    protected String newClassname;
    protected String newMethodname;
    protected boolean newMethodIsPrivate;
    protected int newIndex;
    protected ConstPool constPool;
    
    public TransformCall(final Transformer a1, final CtMethod a2, final CtMethod a3) {
        this(a1, a2.getName(), a3);
        this.classname = a2.getDeclaringClass().getName();
    }
    
    public TransformCall(final Transformer a1, final String a2, final CtMethod a3) {
        super(a1);
        this.methodname = a2;
        this.methodDescriptor = a3.getMethodInfo2().getDescriptor();
        final String name = a3.getDeclaringClass().getName();
        this.newClassname = name;
        this.classname = name;
        this.newMethodname = a3.getName();
        this.constPool = null;
        this.newMethodIsPrivate = Modifier.isPrivate(a3.getModifiers());
    }
    
    @Override
    public void initialize(final ConstPool a1, final CodeAttribute a2) {
        /*SL:55*/if (this.constPool != a1) {
            /*SL:56*/this.newIndex = 0;
        }
    }
    
    @Override
    public int transform(final CtClass v1, int v2, final CodeIterator v3, final ConstPool v4) throws BadBytecode {
        final int v5 = /*EL:69*/v3.byteAt(v2);
        /*SL:70*/if (v5 == 185 || v5 == 183 || v5 == 184 || v5 == 182) {
            int a2 = /*EL:72*/v3.u16bitAt(v2 + 1);
            /*SL:73*/a2 = v4.eqMember(this.methodname, this.methodDescriptor, a2);
            /*SL:74*/if (a2 != null && this.matchClass(a2, v1.getClassPool())) {
                final int a3 = /*EL:75*/v4.getMemberNameAndType(a2);
                /*SL:76*/v2 = this.match(v5, v2, v3, v4.getNameAndTypeDescriptor(a3), /*EL:77*/v4);
            }
        }
        /*SL:81*/return v2;
    }
    
    private boolean matchClass(final String v-1, final ClassPool v0) {
        /*SL:85*/if (this.classname.equals(v-1)) {
            /*SL:86*/return true;
        }
        try {
            final CtClass v = /*EL:89*/v0.get(v-1);
            final CtClass v2 = /*EL:90*/v0.get(this.classname);
            /*SL:91*/if (v.subtypeOf(v2)) {
                try {
                    final CtMethod a1 = /*EL:93*/v.getMethod(this.methodname, this.methodDescriptor);
                    /*SL:94*/return a1.getDeclaringClass().getName().equals(this.classname);
                }
                catch (NotFoundException a2) {
                    /*SL:98*/return true;
                }
            }
        }
        catch (NotFoundException v3) {
            /*SL:102*/return false;
        }
        /*SL:105*/return false;
    }
    
    protected int match(final int a4, final int a5, final CodeIterator v1, final int v2, final ConstPool v3) throws BadBytecode {
        /*SL:111*/if (this.newIndex == 0) {
            final int a6 = /*EL:112*/v3.addNameAndTypeInfo(v3.addUtf8Info(this.newMethodname), v2);
            final int a7 = /*EL:114*/v3.addClassInfo(this.newClassname);
            /*SL:115*/if (a4 == 185) {
                /*SL:116*/this.newIndex = v3.addInterfaceMethodrefInfo(a7, a6);
            }
            else {
                /*SL:118*/if (this.newMethodIsPrivate && a4 == 182) {
                    /*SL:119*/v1.writeByte(183, a5);
                }
                /*SL:121*/this.newIndex = v3.addMethodrefInfo(a7, a6);
            }
            /*SL:124*/this.constPool = v3;
        }
        /*SL:127*/v1.write16bit(this.newIndex, a5 + 1);
        /*SL:128*/return a5;
    }
}

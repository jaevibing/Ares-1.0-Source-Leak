package javassist.convert;

import javassist.bytecode.Bytecode;
import javassist.bytecode.BadBytecode;
import javassist.bytecode.Descriptor;
import javassist.bytecode.CodeIterator;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.ConstPool;
import javassist.NotFoundException;
import javassist.CtMethod;
import javassist.CtClass;

public class TransformBefore extends TransformCall
{
    protected CtClass[] parameterTypes;
    protected int locals;
    protected int maxLocals;
    protected byte[] saveCode;
    protected byte[] loadCode;
    
    public TransformBefore(final Transformer a1, final CtMethod a2, final CtMethod a3) throws NotFoundException {
        super(a1, a2, a3);
        this.methodDescriptor = a2.getMethodInfo2().getDescriptor();
        this.parameterTypes = a2.getParameterTypes();
        this.locals = 0;
        this.maxLocals = 0;
        final byte[] array = null;
        this.loadCode = array;
        this.saveCode = array;
    }
    
    @Override
    public void initialize(final ConstPool a1, final CodeAttribute a2) {
        /*SL:46*/super.initialize(a1, a2);
        /*SL:47*/this.locals = 0;
        /*SL:48*/this.maxLocals = a2.getMaxLocals();
        final byte[] array = /*EL:49*/null;
        this.loadCode = array;
        this.saveCode = array;
    }
    
    @Override
    protected int match(final int a5, final int v1, final CodeIterator v2, final int v3, final ConstPool v4) throws BadBytecode {
        /*SL:55*/if (this.newIndex == 0) {
            String a6 = /*EL:56*/Descriptor.ofParameters(this.parameterTypes) + 'V';
            /*SL:57*/a6 = Descriptor.insertParameter(this.classname, a6);
            final int a7 = /*EL:58*/v4.addNameAndTypeInfo(this.newMethodname, a6);
            final int a8 = /*EL:59*/v4.addClassInfo(this.newClassname);
            /*SL:60*/this.newIndex = v4.addMethodrefInfo(a8, a7);
            /*SL:61*/this.constPool = v4;
        }
        /*SL:64*/if (this.saveCode == null) {
            /*SL:65*/this.makeCode(this.parameterTypes, v4);
        }
        /*SL:67*/return this.match2(v1, v2);
    }
    
    protected int match2(final int a1, final CodeIterator a2) throws BadBytecode {
        /*SL:71*/a2.move(a1);
        /*SL:72*/a2.insert(this.saveCode);
        /*SL:73*/a2.insert(this.loadCode);
        final int v1 = /*EL:74*/a2.insertGap(3);
        /*SL:75*/a2.writeByte(184, v1);
        /*SL:76*/a2.write16bit(this.newIndex, v1 + 1);
        /*SL:77*/a2.insert(this.loadCode);
        /*SL:78*/return a2.next();
    }
    
    @Override
    public int extraLocals() {
        /*SL:81*/return this.locals;
    }
    
    protected void makeCode(final CtClass[] a1, final ConstPool a2) {
        final Bytecode v1 = /*EL:84*/new Bytecode(a2, 0, 0);
        final Bytecode v2 = /*EL:85*/new Bytecode(a2, 0, 0);
        final int v3 = /*EL:87*/this.maxLocals;
        final int v4 = /*EL:88*/(a1 == null) ? 0 : a1.length;
        /*SL:89*/v2.addAload(v3);
        /*SL:90*/this.makeCode2(v1, v2, 0, v4, a1, v3 + 1);
        /*SL:91*/v1.addAstore(v3);
        /*SL:93*/this.saveCode = v1.get();
        /*SL:94*/this.loadCode = v2.get();
    }
    
    private void makeCode2(final Bytecode a3, final Bytecode a4, final int a5, final int a6, final CtClass[] v1, final int v2) {
        /*SL:100*/if (a5 < a6) {
            final int a7 = /*EL:101*/a4.addLoad(v2, v1[a5]);
            /*SL:102*/this.makeCode2(a3, a4, a5 + 1, a6, v1, v2 + a7);
            /*SL:103*/a3.addStore(v2, v1[a5]);
        }
        else {
            /*SL:106*/this.locals = v2 - this.maxLocals;
        }
    }
}

package javassist.convert;

import javassist.bytecode.BadBytecode;
import javassist.bytecode.CodeIterator;
import javassist.CannotCompileException;
import javassist.bytecode.MethodInfo;
import javassist.CtClass;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.ConstPool;
import javassist.bytecode.Opcode;

public abstract class Transformer implements Opcode
{
    private Transformer next;
    
    public Transformer(final Transformer a1) {
        this.next = a1;
    }
    
    public Transformer getNext() {
        /*SL:41*/return this.next;
    }
    
    public void initialize(final ConstPool a1, final CodeAttribute a2) {
    }
    
    public void initialize(final ConstPool a1, final CtClass a2, final MethodInfo a3) throws CannotCompileException {
        /*SL:46*/this.initialize(a1, a3.getCodeAttribute());
    }
    
    public void clean() {
    }
    
    public abstract int transform(final CtClass p0, final int p1, final CodeIterator p2, final ConstPool p3) throws CannotCompileException, BadBytecode;
    
    public int extraLocals() {
        /*SL:54*/return 0;
    }
    
    public int extraStack() {
        /*SL:56*/return 0;
    }
}

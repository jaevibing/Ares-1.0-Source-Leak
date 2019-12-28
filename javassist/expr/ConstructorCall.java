package javassist.expr;

import javassist.CtConstructor;
import javassist.NotFoundException;
import javassist.CtMethod;
import javassist.bytecode.MethodInfo;
import javassist.CtClass;
import javassist.bytecode.CodeIterator;

public class ConstructorCall extends MethodCall
{
    protected ConstructorCall(final int a1, final CodeIterator a2, final CtClass a3, final MethodInfo a4) {
        super(a1, a2, a3, a4);
    }
    
    @Override
    public String getMethodName() {
        /*SL:44*/return this.isSuper() ? "super" : "this";
    }
    
    @Override
    public CtMethod getMethod() throws NotFoundException {
        /*SL:53*/throw new NotFoundException("this is a constructor call.  Call getConstructor().");
    }
    
    public CtConstructor getConstructor() throws NotFoundException {
        /*SL:60*/return this.getCtClass().getConstructor(this.getSignature());
    }
    
    @Override
    public boolean isSuper() {
        /*SL:68*/return super.isSuper();
    }
}

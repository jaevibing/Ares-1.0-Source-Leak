package javassist.bytecode.analysis;

import javassist.NotFoundException;
import javassist.ClassPool;
import javassist.CtClass;

public class MultiArrayType extends Type
{
    private MultiType component;
    private int dims;
    
    public MultiArrayType(final MultiType a1, final int a2) {
        super(null);
        this.component = a1;
        this.dims = a2;
    }
    
    @Override
    public CtClass getCtClass() {
        final CtClass ctClass = /*EL:38*/this.component.getCtClass();
        /*SL:39*/if (ctClass == null) {
            /*SL:40*/return null;
        }
        ClassPool classPool = /*EL:42*/ctClass.getClassPool();
        /*SL:43*/if (classPool == null) {
            /*SL:44*/classPool = ClassPool.getDefault();
        }
        final String v0 = /*EL:46*/this.arrayName(ctClass.getName(), this.dims);
        try {
            /*SL:49*/return classPool.get(v0);
        }
        catch (NotFoundException v) {
            /*SL:51*/throw new RuntimeException(v);
        }
    }
    
    @Override
    boolean popChanged() {
        /*SL:56*/return this.component.popChanged();
    }
    
    @Override
    public int getDimensions() {
        /*SL:60*/return this.dims;
    }
    
    @Override
    public Type getComponent() {
        /*SL:64*/return (this.dims == 1) ? this.component : new MultiArrayType(this.component, this.dims - 1);
    }
    
    @Override
    public int getSize() {
        /*SL:68*/return 1;
    }
    
    @Override
    public boolean isArray() {
        /*SL:72*/return true;
    }
    
    @Override
    public boolean isAssignableFrom(final Type a1) {
        /*SL:76*/throw new UnsupportedOperationException("Not implemented");
    }
    
    @Override
    public boolean isReference() {
        /*SL:80*/return true;
    }
    
    public boolean isAssignableTo(final Type a1) {
        /*SL:84*/if (Type.eq(a1.getCtClass(), Type.OBJECT.getCtClass())) {
            /*SL:85*/return true;
        }
        /*SL:87*/if (Type.eq(a1.getCtClass(), Type.CLONEABLE.getCtClass())) {
            /*SL:88*/return true;
        }
        /*SL:90*/if (Type.eq(a1.getCtClass(), Type.SERIALIZABLE.getCtClass())) {
            /*SL:91*/return true;
        }
        /*SL:93*/if (!a1.isArray()) {
            /*SL:94*/return false;
        }
        final Type v1 = /*EL:96*/this.getRootComponent(a1);
        final int v2 = /*EL:97*/a1.getDimensions();
        /*SL:99*/if (v2 > this.dims) {
            /*SL:100*/return false;
        }
        /*SL:102*/if (v2 < this.dims) {
            /*SL:106*/return Type.eq(v1.getCtClass(), Type.OBJECT.getCtClass()) || Type.eq(v1.getCtClass(), Type.CLONEABLE.getCtClass()) || /*EL:109*/Type.eq(v1.getCtClass(), Type.SERIALIZABLE.getCtClass());
        }
        /*SL:115*/return this.component.isAssignableTo(v1);
    }
    
    @Override
    public boolean equals(final Object a1) {
        /*SL:119*/if (!(a1 instanceof MultiArrayType)) {
            /*SL:120*/return false;
        }
        final MultiArrayType v1 = /*EL:121*/(MultiArrayType)a1;
        /*SL:123*/return this.component.equals(v1.component) && this.dims == v1.dims;
    }
    
    @Override
    public String toString() {
        /*SL:128*/return this.arrayName(this.component.toString(), this.dims);
    }
}

package org.spongepowered.asm.lib.tree.analysis;

import java.util.List;
import org.spongepowered.asm.lib.Type;

public class SimpleVerifier extends BasicVerifier
{
    private final Type currentClass;
    private final Type currentSuperClass;
    private final List<Type> currentClassInterfaces;
    private final boolean isInterface;
    private ClassLoader loader;
    
    public SimpleVerifier() {
        this(null, null, false);
    }
    
    public SimpleVerifier(final Type a1, final Type a2, final boolean a3) {
        this(a1, a2, null, a3);
    }
    
    public SimpleVerifier(final Type a1, final Type a2, final List<Type> a3, final boolean a4) {
        this(327680, a1, a2, a3, a4);
    }
    
    protected SimpleVerifier(final int a1, final Type a2, final Type a3, final List<Type> a4, final boolean a5) {
        super(a1);
        this.loader = this.getClass().getClassLoader();
        this.currentClass = a2;
        this.currentSuperClass = a3;
        this.currentClassInterfaces = a4;
        this.isInterface = a5;
    }
    
    public void setClassLoader(final ClassLoader a1) {
        /*SL:133*/this.loader = a1;
    }
    
    public BasicValue newValue(final Type v-2) {
        /*SL:138*/if (v-2 == null) {
            /*SL:139*/return BasicValue.UNINITIALIZED_VALUE;
        }
        final boolean b = /*EL:142*/v-2.getSort() == 9;
        /*SL:143*/if (b) {
            /*SL:144*/switch (v-2.getElementType().getSort()) {
                case 1:
                case 2:
                case 3:
                case 4: {
                    /*SL:149*/return new BasicValue(v-2);
                }
            }
        }
        BasicValue v0 = /*EL:153*/super.newValue(v-2);
        /*SL:154*/if (BasicValue.REFERENCE_VALUE.equals(v0)) {
            /*SL:155*/if (b) {
                /*SL:156*/v0 = this.newValue(v-2.getElementType());
                String v = /*EL:157*/v0.getType().getDescriptor();
                /*SL:158*/for (int a1 = 0; a1 < v-2.getDimensions(); ++a1) {
                    /*SL:159*/v = '[' + v;
                }
                /*SL:161*/v0 = new BasicValue(Type.getType(v));
            }
            else {
                /*SL:163*/v0 = new BasicValue(v-2);
            }
        }
        /*SL:166*/return v0;
    }
    
    protected boolean isArrayValue(final BasicValue a1) {
        final Type v1 = /*EL:171*/a1.getType();
        /*SL:172*/return v1 != null && ("Lnull;".equals(v1.getDescriptor()) || /*EL:173*/v1.getSort() == 9);
    }
    
    protected BasicValue getElementValue(final BasicValue a1) throws AnalyzerException {
        final Type v1 = /*EL:179*/a1.getType();
        /*SL:180*/if (v1 != null) {
            /*SL:181*/if (v1.getSort() == 9) {
                /*SL:182*/return this.newValue(Type.getType(v1.getDescriptor().substring(1)));
            }
            /*SL:184*/if ("Lnull;".equals(v1.getDescriptor())) {
                /*SL:185*/return a1;
            }
        }
        /*SL:188*/throw new Error("Internal error");
    }
    
    protected boolean isSubTypeOf(final BasicValue a1, final BasicValue a2) {
        final Type v1 = /*EL:194*/a2.getType();
        final Type v2 = /*EL:195*/a1.getType();
        /*SL:196*/switch (v1.getSort()) {
            case 5:
            case 6:
            case 7:
            case 8: {
                /*SL:201*/return v2.equals(v1);
            }
            case 9:
            case 10: {
                /*SL:204*/return "Lnull;".equals(v2.getDescriptor()) || /*EL:206*/((v2.getSort() == 10 || v2.getSort() == /*EL:207*/9) && /*EL:208*/this.isAssignableFrom(v1, v2));
            }
            default: {
                /*SL:213*/throw new Error("Internal error");
            }
        }
    }
    
    public BasicValue merge(final BasicValue v2, final BasicValue v3) {
        /*SL:219*/if (v2.equals(v3)) {
            /*SL:255*/return v2;
        }
        Type a1 = v2.getType();
        final Type a2 = v3.getType();
        if (a1 == null || (a1.getSort() != 10 && a1.getSort() != 9) || a2 == null || (a2.getSort() != 10 && a2.getSort() != 9)) {
            return BasicValue.UNINITIALIZED_VALUE;
        }
        if ("Lnull;".equals(a1.getDescriptor())) {
            return v3;
        }
        if ("Lnull;".equals(a2.getDescriptor())) {
            return v2;
        }
        if (this.isAssignableFrom(a1, a2)) {
            return v2;
        }
        if (this.isAssignableFrom(a2, a1)) {
            return v3;
        }
        while (a1 != null && !this.isInterface(a1)) {
            a1 = this.getSuperClass(a1);
            if (this.isAssignableFrom(a1, a2)) {
                return this.newValue(a1);
            }
        }
        return BasicValue.REFERENCE_VALUE;
    }
    
    protected boolean isInterface(final Type a1) {
        /*SL:259*/if (this.currentClass != null && a1.equals(this.currentClass)) {
            /*SL:260*/return this.isInterface;
        }
        /*SL:262*/return this.getClass(a1).isInterface();
    }
    
    protected Type getSuperClass(final Type a1) {
        /*SL:266*/if (this.currentClass != null && a1.equals(this.currentClass)) {
            /*SL:267*/return this.currentSuperClass;
        }
        final Class<?> v1 = /*EL:269*/this.getClass(a1).getSuperclass();
        /*SL:270*/return (v1 == null) ? null : Type.getType(v1);
    }
    
    protected boolean isAssignableFrom(final Type v2, final Type v3) {
        /*SL:274*/if (v2.equals(v3)) {
            /*SL:275*/return true;
        }
        /*SL:277*/if (this.currentClass != null && v2.equals(this.currentClass)) {
            /*SL:278*/if (this.getSuperClass(v3) == null) {
                /*SL:279*/return false;
            }
            /*SL:281*/if (this.isInterface) {
                /*SL:282*/return v3.getSort() == 10 || v3.getSort() == /*EL:283*/9;
            }
            /*SL:285*/return this.isAssignableFrom(v2, this.getSuperClass(v3));
        }
        else {
            /*SL:288*/if (this.currentClass == null || !v3.equals(this.currentClass)) {
                Class<?> a2 = /*EL:302*/this.getClass(v2);
                /*SL:303*/if (a2.isInterface()) {
                    /*SL:304*/a2 = Object.class;
                }
                /*SL:306*/return a2.isAssignableFrom(this.getClass(v3));
            }
            if (this.isAssignableFrom(v2, this.currentSuperClass)) {
                return true;
            }
            if (this.currentClassInterfaces != null) {
                for (int a2 = 0; a2 < this.currentClassInterfaces.size(); ++a2) {
                    final Type a3 = this.currentClassInterfaces.get(a2);
                    if (this.isAssignableFrom(v2, a3)) {
                        return true;
                    }
                }
            }
            return false;
        }
    }
    
    protected Class<?> getClass(final Type v2) {
        try {
            /*SL:311*/if (v2.getSort() == 9) {
                /*SL:312*/return Class.forName(v2.getDescriptor().replace('/', '.'), false, this.loader);
            }
            /*SL:315*/return Class.forName(v2.getClassName(), false, this.loader);
        }
        catch (ClassNotFoundException a1) {
            /*SL:317*/throw new RuntimeException(a1.toString());
        }
    }
}

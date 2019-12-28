package javassist.bytecode.annotation;

import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import javassist.ClassPool;
import javassist.bytecode.ConstPool;

public class ArrayMemberValue extends MemberValue
{
    MemberValue type;
    MemberValue[] values;
    
    public ArrayMemberValue(final ConstPool a1) {
        super('[', a1);
        this.type = null;
        this.values = null;
    }
    
    public ArrayMemberValue(final MemberValue a1, final ConstPool a2) {
        super('[', a2);
        this.type = a1;
        this.values = null;
    }
    
    @Override
    Object getValue(final ClassLoader v1, final ClassPool v2, final Method v3) throws ClassNotFoundException {
        /*SL:57*/if (this.values == null) {
            /*SL:58*/throw new ClassNotFoundException("no array elements found: " + v3.getName());
        }
        final int v4 = /*EL:61*/this.values.length;
        final Class v5;
        /*SL:63*/if (this.type == null) {
            final Class a1 = /*EL:64*/v3.getReturnType().getComponentType();
            /*SL:65*/if (a1 == null || v4 > 0) {
                /*SL:66*/throw new ClassNotFoundException("broken array type: " + v3.getName());
            }
        }
        else {
            /*SL:70*/v5 = this.type.getType(v1);
        }
        final Object v6 = /*EL:72*/Array.newInstance(v5, v4);
        /*SL:73*/for (int a2 = 0; a2 < v4; ++a2) {
            /*SL:74*/Array.set(v6, a2, this.values[a2].getValue(v1, v2, v3));
        }
        /*SL:76*/return v6;
    }
    
    @Override
    Class getType(final ClassLoader a1) throws ClassNotFoundException {
        /*SL:80*/if (this.type == null) {
            /*SL:81*/throw new ClassNotFoundException("no array type specified");
        }
        final Object v1 = /*EL:83*/Array.newInstance(this.type.getType(a1), 0);
        /*SL:84*/return v1.getClass();
    }
    
    public MemberValue getType() {
        /*SL:93*/return this.type;
    }
    
    public MemberValue[] getValue() {
        /*SL:100*/return this.values;
    }
    
    public void setValue(final MemberValue[] a1) {
        /*SL:107*/this.values = a1;
        /*SL:108*/if (a1 != null && a1.length > 0) {
            /*SL:109*/this.type = a1[0];
        }
    }
    
    @Override
    public String toString() {
        final StringBuffer v0 = /*EL:116*/new StringBuffer("{");
        /*SL:117*/if (this.values != null) {
            /*SL:118*/for (int v = 0; v < this.values.length; ++v) {
                /*SL:119*/v0.append(this.values[v].toString());
                /*SL:120*/if (v + 1 < this.values.length) {
                    /*SL:121*/v0.append(", ");
                }
            }
        }
        /*SL:125*/v0.append("}");
        /*SL:126*/return v0.toString();
    }
    
    @Override
    public void write(final AnnotationsWriter v2) throws IOException {
        final int v3 = /*EL:133*/(this.values == null) ? 0 : this.values.length;
        /*SL:134*/v2.arrayValue(v3);
        /*SL:135*/for (int a1 = 0; a1 < v3; ++a1) {
            /*SL:136*/this.values[a1].write(v2);
        }
    }
    
    @Override
    public void accept(final MemberValueVisitor a1) {
        /*SL:143*/a1.visitArrayMemberValue(this);
    }
}

package com.sun.jna;

import java.util.Iterator;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public abstract class Union extends Structure
{
    private StructField activeField;
    
    protected Union() {
    }
    
    protected Union(final Pointer a1) {
        super(a1);
    }
    
    protected Union(final Pointer a1, final int a2) {
        super(a1, a2);
    }
    
    protected Union(final TypeMapper a1) {
        super(a1);
    }
    
    protected Union(final Pointer a1, final int a2, final TypeMapper a3) {
        super(a1, a2, a3);
    }
    
    @Override
    protected List<String> getFieldOrder() {
        final List<Field> fieldList = /*EL:69*/this.getFieldList();
        final List<String> list = /*EL:70*/new ArrayList<String>(fieldList.size());
        /*SL:71*/for (final Field v1 : fieldList) {
            /*SL:72*/list.add(v1.getName());
        }
        /*SL:74*/return list;
    }
    
    public void setType(final Class<?> v2) {
        /*SL:85*/this.ensureAllocated();
        /*SL:86*/for (final StructField a1 : this.fields().values()) {
            /*SL:87*/if (a1.type == v2) {
                /*SL:88*/this.activeField = a1;
                /*SL:89*/return;
            }
        }
        /*SL:92*/throw new IllegalArgumentException("No field of type " + v2 + " in " + this);
    }
    
    public void setType(final String a1) {
        /*SL:102*/this.ensureAllocated();
        final StructField v1 = /*EL:103*/this.fields().get(a1);
        /*SL:104*/if (v1 != null) {
            /*SL:105*/this.activeField = v1;
            /*SL:111*/return;
        }
        throw new IllegalArgumentException("No field named " + a1 + " in " + this);
    }
    
    @Override
    public Object readField(final String a1) {
        /*SL:119*/this.ensureAllocated();
        /*SL:120*/this.setType(a1);
        /*SL:121*/return super.readField(a1);
    }
    
    @Override
    public void writeField(final String a1) {
        /*SL:130*/this.ensureAllocated();
        /*SL:131*/this.setType(a1);
        /*SL:132*/super.writeField(a1);
    }
    
    @Override
    public void writeField(final String a1, final Object a2) {
        /*SL:141*/this.ensureAllocated();
        /*SL:142*/this.setType(a1);
        /*SL:143*/super.writeField(a1, a2);
    }
    
    public Object getTypedValue(final Class<?> v2) {
        /*SL:159*/this.ensureAllocated();
        /*SL:160*/for (final StructField a1 : this.fields().values()) {
            /*SL:161*/if (a1.type == v2) {
                /*SL:162*/this.activeField = a1;
                /*SL:163*/this.read();
                /*SL:164*/return this.getFieldValue(this.activeField.field);
            }
        }
        /*SL:167*/throw new IllegalArgumentException("No field of type " + v2 + " in " + this);
    }
    
    public Object setTypedValue(final Object a1) {
        final StructField v1 = /*EL:181*/this.findField(a1.getClass());
        /*SL:182*/if (v1 != null) {
            /*SL:183*/this.activeField = v1;
            /*SL:184*/this.setFieldValue(v1.field, a1);
            /*SL:185*/return this;
        }
        /*SL:187*/throw new IllegalArgumentException("No field of type " + a1.getClass() + " in " + this);
    }
    
    private StructField findField(final Class<?> v2) {
        /*SL:196*/this.ensureAllocated();
        /*SL:197*/for (final StructField a1 : this.fields().values()) {
            /*SL:198*/if (a1.type.isAssignableFrom(v2)) {
                /*SL:199*/return a1;
            }
        }
        /*SL:202*/return null;
    }
    
    @Override
    protected void writeField(final StructField a1) {
        /*SL:208*/if (a1 == this.activeField) {
            /*SL:209*/super.writeField(a1);
        }
    }
    
    @Override
    protected Object readField(final StructField a1) {
        /*SL:219*/if (a1 == this.activeField || (!Structure.class.isAssignableFrom(a1.type) && /*EL:220*/!String.class.isAssignableFrom(a1.type) && /*EL:221*/!WString.class.isAssignableFrom(a1.type))) {
            /*SL:223*/return super.readField(a1);
        }
        /*SL:229*/return null;
    }
    
    @Override
    protected int getNativeAlignment(final Class<?> a1, final Object a2, final boolean a3) {
        /*SL:235*/return super.getNativeAlignment(a1, a2, true);
    }
}

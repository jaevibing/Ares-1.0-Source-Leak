package javassist.bytecode.annotation;

import java.io.IOException;
import javassist.ClassPool;
import java.util.Set;
import java.util.Iterator;
import javassist.NotFoundException;
import javassist.CtMethod;
import javassist.CtClass;
import javassist.bytecode.Descriptor;
import java.util.LinkedHashMap;
import javassist.bytecode.ConstPool;

public class Annotation
{
    ConstPool pool;
    int typeIndex;
    LinkedHashMap members;
    
    public Annotation(final int a1, final ConstPool a2) {
        this.pool = a2;
        this.typeIndex = a1;
        this.members = null;
    }
    
    public Annotation(final String a1, final ConstPool a2) {
        this(a2.addUtf8Info(Descriptor.of(a1)), a2);
    }
    
    public Annotation(final ConstPool v2, final CtClass v3) throws NotFoundException {
        this(v2.addUtf8Info(Descriptor.of(v3.getName())), v2);
        if (!v3.isInterface()) {
            throw new RuntimeException("Only interfaces are allowed for Annotation creation.");
        }
        final CtMethod[] v4 = v3.getDeclaredMethods();
        if (v4.length > 0) {
            this.members = new LinkedHashMap();
        }
        for (CtClass a2 = (CtClass)0; a2 < v4.length; ++a2) {
            a2 = v4[a2].getReturnType();
            this.addMemberValue(v4[a2].getName(), createMemberValue(v2, a2));
        }
    }
    
    public static MemberValue createMemberValue(final ConstPool v-1, final CtClass v0) throws NotFoundException {
        /*SL:133*/if (v0 == CtClass.booleanType) {
            /*SL:134*/return new BooleanMemberValue(v-1);
        }
        /*SL:135*/if (v0 == CtClass.byteType) {
            /*SL:136*/return new ByteMemberValue(v-1);
        }
        /*SL:137*/if (v0 == CtClass.charType) {
            /*SL:138*/return new CharMemberValue(v-1);
        }
        /*SL:139*/if (v0 == CtClass.shortType) {
            /*SL:140*/return new ShortMemberValue(v-1);
        }
        /*SL:141*/if (v0 == CtClass.intType) {
            /*SL:142*/return new IntegerMemberValue(v-1);
        }
        /*SL:143*/if (v0 == CtClass.longType) {
            /*SL:144*/return new LongMemberValue(v-1);
        }
        /*SL:145*/if (v0 == CtClass.floatType) {
            /*SL:146*/return new FloatMemberValue(v-1);
        }
        /*SL:147*/if (v0 == CtClass.doubleType) {
            /*SL:148*/return new DoubleMemberValue(v-1);
        }
        /*SL:149*/if (v0.getName().equals("java.lang.Class")) {
            /*SL:150*/return new ClassMemberValue(v-1);
        }
        /*SL:151*/if (v0.getName().equals("java.lang.String")) {
            /*SL:152*/return new StringMemberValue(v-1);
        }
        /*SL:153*/if (v0.isArray()) {
            final CtClass a1 = /*EL:154*/v0.getComponentType();
            final MemberValue a2 = createMemberValue(/*EL:155*/v-1, a1);
            /*SL:156*/return new ArrayMemberValue(a2, v-1);
        }
        /*SL:158*/if (v0.isInterface()) {
            final Annotation v = /*EL:159*/new Annotation(v-1, v0);
            /*SL:160*/return new AnnotationMemberValue(v, v-1);
        }
        final EnumMemberValue v2 = /*EL:166*/new EnumMemberValue(v-1);
        /*SL:167*/v2.setType(v0.getName());
        /*SL:168*/return v2;
    }
    
    public void addMemberValue(final int a1, final MemberValue a2) {
        final Pair v1 = /*EL:182*/new Pair();
        /*SL:183*/v1.name = a1;
        /*SL:184*/v1.value = a2;
        /*SL:185*/this.addMemberValue(v1);
    }
    
    public void addMemberValue(final String a1, final MemberValue a2) {
        final Pair v1 = /*EL:195*/new Pair();
        /*SL:196*/v1.name = this.pool.addUtf8Info(a1);
        /*SL:197*/v1.value = a2;
        /*SL:198*/if (this.members == null) {
            /*SL:199*/this.members = new LinkedHashMap();
        }
        /*SL:201*/this.members.put(a1, v1);
    }
    
    private void addMemberValue(final Pair a1) {
        final String v1 = /*EL:205*/this.pool.getUtf8Info(a1.name);
        /*SL:206*/if (this.members == null) {
            /*SL:207*/this.members = new LinkedHashMap();
        }
        /*SL:209*/this.members.put(v1, a1);
    }
    
    @Override
    public String toString() {
        final StringBuffer sb = /*EL:216*/new StringBuffer("@");
        /*SL:217*/sb.append(this.getTypeName());
        /*SL:218*/if (this.members != null) {
            /*SL:219*/sb.append("(");
            final Iterator v0 = /*EL:220*/this.members.keySet().iterator();
            /*SL:221*/while (v0.hasNext()) {
                final String v = /*EL:222*/v0.next();
                /*SL:223*/sb.append(v).append("=").append(this.getMemberValue(v));
                /*SL:224*/if (v0.hasNext()) {
                    /*SL:225*/sb.append(", ");
                }
            }
            /*SL:227*/sb.append(")");
        }
        /*SL:230*/return sb.toString();
    }
    
    public String getTypeName() {
        /*SL:239*/return Descriptor.toClassName(this.pool.getUtf8Info(this.typeIndex));
    }
    
    public Set getMemberNames() {
        /*SL:248*/if (this.members == null) {
            /*SL:249*/return null;
        }
        /*SL:251*/return this.members.keySet();
    }
    
    public MemberValue getMemberValue(final String v2) {
        /*SL:270*/if (this.members == null) {
            /*SL:271*/return null;
        }
        final Pair a1 = /*EL:273*/this.members.get(v2);
        /*SL:274*/if (a1 == null) {
            /*SL:275*/return null;
        }
        /*SL:277*/return a1.value;
    }
    
    public Object toAnnotationType(final ClassLoader a1, final ClassPool a2) throws ClassNotFoundException, NoSuchClassError {
        /*SL:295*/return AnnotationImpl.make(a1, /*EL:296*/MemberValue.loadClass(a1, this.getTypeName()), a2, this);
    }
    
    public void write(final AnnotationsWriter v2) throws IOException {
        final String v3 = /*EL:307*/this.pool.getUtf8Info(this.typeIndex);
        /*SL:308*/if (this.members == null) {
            /*SL:309*/v2.annotation(v3, 0);
            /*SL:310*/return;
        }
        /*SL:313*/v2.annotation(v3, this.members.size());
        /*SL:314*/for (final Pair a1 : this.members.values()) {
            /*SL:317*/v2.memberValuePair(a1.name);
            /*SL:318*/a1.value.write(v2);
        }
    }
    
    @Override
    public boolean equals(final Object a1) {
        /*SL:327*/if (a1 == this) {
            /*SL:328*/return true;
        }
        /*SL:329*/if (a1 == null || !(a1 instanceof Annotation)) {
            /*SL:330*/return false;
        }
        final Annotation v1 = /*EL:332*/(Annotation)a1;
        /*SL:334*/if (!this.getTypeName().equals(v1.getTypeName())) {
            /*SL:335*/return false;
        }
        final LinkedHashMap v2 = /*EL:337*/v1.members;
        /*SL:338*/if (this.members == v2) {
            /*SL:339*/return true;
        }
        /*SL:340*/if (this.members == null) {
            /*SL:341*/return v2 == null;
        }
        /*SL:343*/return v2 != null && /*EL:346*/this.members.equals(v2);
    }
    
    static class Pair
    {
        int name;
        MemberValue value;
    }
}

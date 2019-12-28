package javassist.bytecode;

import javassist.bytecode.annotation.ArrayMemberValue;
import javassist.bytecode.annotation.AnnotationMemberValue;
import javassist.bytecode.annotation.ClassMemberValue;
import javassist.bytecode.annotation.EnumMemberValue;
import javassist.bytecode.annotation.StringMemberValue;
import javassist.bytecode.annotation.BooleanMemberValue;
import javassist.bytecode.annotation.ShortMemberValue;
import javassist.bytecode.annotation.LongMemberValue;
import javassist.bytecode.annotation.IntegerMemberValue;
import javassist.bytecode.annotation.FloatMemberValue;
import javassist.bytecode.annotation.DoubleMemberValue;
import javassist.bytecode.annotation.CharMemberValue;
import javassist.bytecode.annotation.ByteMemberValue;
import javassist.bytecode.annotation.MemberValue;
import java.util.HashMap;
import java.io.OutputStream;
import javassist.bytecode.annotation.AnnotationsWriter;
import java.io.ByteArrayOutputStream;
import javassist.bytecode.annotation.Annotation;
import java.util.Map;
import java.io.IOException;
import java.io.DataInputStream;

public class AnnotationsAttribute extends AttributeInfo
{
    public static final String visibleTag = "RuntimeVisibleAnnotations";
    public static final String invisibleTag = "RuntimeInvisibleAnnotations";
    
    public AnnotationsAttribute(final ConstPool a1, final String a2, final byte[] a3) {
        super(a1, a2, a3);
    }
    
    public AnnotationsAttribute(final ConstPool a1, final String a2) {
        this(a1, a2, new byte[] { 0, 0 });
    }
    
    AnnotationsAttribute(final ConstPool a1, final int a2, final DataInputStream a3) throws IOException {
        super(a1, a2, a3);
    }
    
    public int numAnnotations() {
        /*SL:157*/return ByteArray.readU16bit(this.info, 0);
    }
    
    @Override
    public AttributeInfo copy(final ConstPool v1, final Map v2) {
        final Copier v3 = /*EL:164*/new Copier(this.info, this.constPool, v1, v2);
        try {
            /*SL:166*/v3.annotationArray();
            /*SL:167*/return new AnnotationsAttribute(v1, this.getName(), v3.close());
        }
        catch (Exception a1) {
            /*SL:170*/throw new RuntimeException(a1);
        }
    }
    
    public Annotation getAnnotation(final String v2) {
        final Annotation[] v3 = /*EL:184*/this.getAnnotations();
        /*SL:185*/for (int a1 = 0; a1 < v3.length; ++a1) {
            /*SL:186*/if (v3[a1].getTypeName().equals(v2)) {
                /*SL:187*/return v3[a1];
            }
        }
        /*SL:190*/return null;
    }
    
    public void addAnnotation(final Annotation v2) {
        final String v3 = /*EL:200*/v2.getTypeName();
        final Annotation[] v4 = /*EL:201*/this.getAnnotations();
        /*SL:202*/for (int a1 = 0; a1 < v4.length; ++a1) {
            /*SL:203*/if (v4[a1].getTypeName().equals(v3)) {
                /*SL:204*/v4[a1] = v2;
                /*SL:205*/this.setAnnotations(v4);
                /*SL:206*/return;
            }
        }
        final Annotation[] v5 = /*EL:210*/new Annotation[v4.length + 1];
        /*SL:211*/System.arraycopy(v4, 0, v5, 0, v4.length);
        /*SL:212*/v5[v4.length] = v2;
        /*SL:213*/this.setAnnotations(v5);
    }
    
    public boolean removeAnnotation(final String v-1) {
        final Annotation[] v0 = /*EL:226*/this.getAnnotations();
        /*SL:227*/for (int v = 0; v < v0.length; ++v) {
            /*SL:228*/if (v0[v].getTypeName().equals(v-1)) {
                final Annotation[] a1 = /*EL:229*/new Annotation[v0.length - 1];
                /*SL:230*/System.arraycopy(v0, 0, a1, 0, v);
                /*SL:231*/if (v < v0.length - 1) {
                    /*SL:232*/System.arraycopy(v0, v + 1, a1, v, v0.length - v - 1);
                }
                /*SL:235*/this.setAnnotations(a1);
                /*SL:236*/return true;
            }
        }
        /*SL:239*/return false;
    }
    
    public Annotation[] getAnnotations() {
        try {
            /*SL:253*/return new Parser(this.info, this.constPool).parseAnnotations();
        }
        catch (Exception v1) {
            /*SL:256*/throw new RuntimeException(v1);
        }
    }
    
    public void setAnnotations(final Annotation[] v-2) {
        final ByteArrayOutputStream a2 = /*EL:268*/new ByteArrayOutputStream();
        final AnnotationsWriter v0 = /*EL:269*/new AnnotationsWriter(a2, this.constPool);
        try {
            final int v = /*EL:271*/v-2.length;
            /*SL:272*/v0.numAnnotations(v);
            /*SL:273*/for (int a1 = 0; a1 < v; ++a1) {
                /*SL:274*/v-2[a1].write(v0);
            }
            /*SL:276*/v0.close();
        }
        catch (IOException v2) {
            /*SL:279*/throw new RuntimeException(v2);
        }
        /*SL:282*/this.set(a2.toByteArray());
    }
    
    public void setAnnotation(final Annotation a1) {
        /*SL:293*/this.setAnnotations(new Annotation[] { a1 });
    }
    
    @Override
    void renameClass(final String a1, final String a2) {
        final HashMap v1 = /*EL:301*/new HashMap();
        /*SL:302*/v1.put(a1, a2);
        /*SL:303*/this.renameClass(v1);
    }
    
    @Override
    void renameClass(final Map v2) {
        final Renamer v3 = /*EL:307*/new Renamer(this.info, this.getConstPool(), v2);
        try {
            /*SL:309*/v3.annotationArray();
        }
        catch (Exception a1) {
            /*SL:311*/throw new RuntimeException(a1);
        }
    }
    
    @Override
    void getRefClasses(final Map a1) {
        /*SL:315*/this.renameClass(a1);
    }
    
    @Override
    public String toString() {
        final Annotation[] v1 = /*EL:321*/this.getAnnotations();
        final StringBuilder v2 = /*EL:322*/new StringBuilder();
        int v3 = /*EL:323*/0;
        /*SL:324*/while (v3 < v1.length) {
            /*SL:325*/v2.append(v1[v3++].toString());
            /*SL:326*/if (v3 != v1.length) {
                /*SL:327*/v2.append(", ");
            }
        }
        /*SL:330*/return v2.toString();
    }
    
    static class Walker
    {
        byte[] info;
        
        Walker(final byte[] a1) {
            this.info = a1;
        }
        
        final void parameters() throws Exception {
            final int v1 = /*EL:341*/this.info[0] & 0xFF;
            /*SL:342*/this.parameters(v1, 1);
        }
        
        void parameters(final int v1, int v2) throws Exception {
            /*SL:346*/for (int a1 = 0; a1 < v1; ++a1) {
                /*SL:347*/v2 = this.annotationArray(v2);
            }
        }
        
        final void annotationArray() throws Exception {
            /*SL:351*/this.annotationArray(0);
        }
        
        final int annotationArray(final int a1) throws Exception {
            final int v1 = /*EL:355*/ByteArray.readU16bit(this.info, a1);
            /*SL:356*/return this.annotationArray(a1 + 2, v1);
        }
        
        int annotationArray(int v1, final int v2) throws Exception {
            /*SL:360*/for (int a1 = 0; a1 < v2; ++a1) {
                /*SL:361*/v1 = this.annotation(v1);
            }
            /*SL:363*/return v1;
        }
        
        final int annotation(final int a1) throws Exception {
            final int v1 = /*EL:367*/ByteArray.readU16bit(this.info, a1);
            final int v2 = /*EL:368*/ByteArray.readU16bit(this.info, a1 + 2);
            /*SL:369*/return this.annotation(a1 + 4, v1, v2);
        }
        
        int annotation(int a3, final int v1, final int v2) throws Exception {
            /*SL:373*/for (int a4 = 0; a4 < v2; ++a4) {
                /*SL:374*/a3 = this.memberValuePair(a3);
            }
            /*SL:376*/return a3;
        }
        
        final int memberValuePair(final int a1) throws Exception {
            final int v1 = /*EL:383*/ByteArray.readU16bit(this.info, a1);
            /*SL:384*/return this.memberValuePair(a1 + 2, v1);
        }
        
        int memberValuePair(final int a1, final int a2) throws Exception {
            /*SL:391*/return this.memberValue(a1);
        }
        
        final int memberValue(final int v-2) throws Exception {
            final int a2 = /*EL:398*/this.info[v-2] & 0xFF;
            /*SL:399*/if (a2 == 101) {
                final int a1 = /*EL:400*/ByteArray.readU16bit(this.info, v-2 + 1);
                final int v1 = /*EL:401*/ByteArray.readU16bit(this.info, v-2 + 3);
                /*SL:402*/this.enumMemberValue(v-2, a1, v1);
                /*SL:403*/return v-2 + 5;
            }
            /*SL:405*/if (a2 == 99) {
                final int v2 = /*EL:406*/ByteArray.readU16bit(this.info, v-2 + 1);
                /*SL:407*/this.classMemberValue(v-2, v2);
                /*SL:408*/return v-2 + 3;
            }
            /*SL:410*/if (a2 == 64) {
                /*SL:411*/return this.annotationMemberValue(v-2 + 1);
            }
            /*SL:412*/if (a2 == 91) {
                final int v2 = /*EL:413*/ByteArray.readU16bit(this.info, v-2 + 1);
                /*SL:414*/return this.arrayMemberValue(v-2 + 3, v2);
            }
            final int v2 = /*EL:417*/ByteArray.readU16bit(this.info, v-2 + 1);
            /*SL:418*/this.constValueMember(a2, v2);
            /*SL:419*/return v-2 + 3;
        }
        
        void constValueMember(final int a1, final int a2) throws Exception {
        }
        
        void enumMemberValue(final int a1, final int a2, final int a3) throws Exception {
        }
        
        void classMemberValue(final int a1, final int a2) throws Exception {
        }
        
        int annotationMemberValue(final int a1) throws Exception {
            /*SL:444*/return this.annotation(a1);
        }
        
        int arrayMemberValue(int v1, final int v2) throws Exception {
            /*SL:451*/for (int a1 = 0; a1 < v2; ++a1) {
                /*SL:452*/v1 = this.memberValue(v1);
            }
            /*SL:455*/return v1;
        }
    }
    
    static class Renamer extends Walker
    {
        ConstPool cpool;
        Map classnames;
        
        Renamer(final byte[] a1, final ConstPool a2, final Map a3) {
            super(a1);
            this.cpool = a2;
            this.classnames = a3;
        }
        
        @Override
        int annotation(final int a1, final int a2, final int a3) throws Exception {
            /*SL:479*/this.renameType(a1 - 4, a2);
            /*SL:480*/return super.annotation(a1, a2, a3);
        }
        
        @Override
        void enumMemberValue(final int a1, final int a2, final int a3) throws Exception {
            /*SL:486*/this.renameType(a1 + 1, a2);
            /*SL:487*/super.enumMemberValue(a1, a2, a3);
        }
        
        @Override
        void classMemberValue(final int a1, final int a2) throws Exception {
            /*SL:491*/this.renameType(a1 + 1, a2);
            /*SL:492*/super.classMemberValue(a1, a2);
        }
        
        private void renameType(final int v1, final int v2) {
            final String v3 = /*EL:496*/this.cpool.getUtf8Info(v2);
            final String v4 = /*EL:497*/Descriptor.rename(v3, this.classnames);
            /*SL:498*/if (!v3.equals(v4)) {
                final int a1 = /*EL:499*/this.cpool.addUtf8Info(v4);
                /*SL:500*/ByteArray.write16bit(a1, this.info, v1);
            }
        }
    }
    
    static class Copier extends Walker
    {
        ByteArrayOutputStream output;
        AnnotationsWriter writer;
        ConstPool srcPool;
        ConstPool destPool;
        Map classnames;
        
        Copier(final byte[] a1, final ConstPool a2, final ConstPool a3, final Map a4) {
            this(a1, a2, a3, a4, true);
        }
        
        Copier(final byte[] a1, final ConstPool a2, final ConstPool a3, final Map a4, final boolean a5) {
            super(a1);
            this.output = new ByteArrayOutputStream();
            if (a5) {
                this.writer = new AnnotationsWriter(this.output, a3);
            }
            this.srcPool = a2;
            this.destPool = a3;
            this.classnames = a4;
        }
        
        byte[] close() throws IOException {
            /*SL:538*/this.writer.close();
            /*SL:539*/return this.output.toByteArray();
        }
        
        @Override
        void parameters(final int a1, final int a2) throws Exception {
            /*SL:543*/this.writer.numParameters(a1);
            /*SL:544*/super.parameters(a1, a2);
        }
        
        @Override
        int annotationArray(final int a1, final int a2) throws Exception {
            /*SL:548*/this.writer.numAnnotations(a2);
            /*SL:549*/return super.annotationArray(a1, a2);
        }
        
        @Override
        int annotation(final int a1, final int a2, final int a3) throws Exception {
            /*SL:553*/this.writer.annotation(this.copyType(a2), a3);
            /*SL:554*/return super.annotation(a1, a2, a3);
        }
        
        @Override
        int memberValuePair(final int a1, final int a2) throws Exception {
            /*SL:558*/this.writer.memberValuePair(this.copy(a2));
            /*SL:559*/return super.memberValuePair(a1, a2);
        }
        
        @Override
        void constValueMember(final int a1, final int a2) throws Exception {
            /*SL:563*/this.writer.constValueIndex(a1, this.copy(a2));
            /*SL:564*/super.constValueMember(a1, a2);
        }
        
        @Override
        void enumMemberValue(final int a1, final int a2, final int a3) throws Exception {
            /*SL:570*/this.writer.enumConstValue(this.copyType(a2), this.copy(a3));
            /*SL:571*/super.enumMemberValue(a1, a2, a3);
        }
        
        @Override
        void classMemberValue(final int a1, final int a2) throws Exception {
            /*SL:575*/this.writer.classInfoIndex(this.copyType(a2));
            /*SL:576*/super.classMemberValue(a1, a2);
        }
        
        @Override
        int annotationMemberValue(final int a1) throws Exception {
            /*SL:580*/this.writer.annotationValue();
            /*SL:581*/return super.annotationMemberValue(a1);
        }
        
        @Override
        int arrayMemberValue(final int a1, final int a2) throws Exception {
            /*SL:585*/this.writer.arrayValue(a2);
            /*SL:586*/return super.arrayMemberValue(a1, a2);
        }
        
        int copy(final int a1) {
            /*SL:599*/return this.srcPool.copy(a1, this.destPool, this.classnames);
        }
        
        int copyType(final int a1) {
            final String v1 = /*EL:613*/this.srcPool.getUtf8Info(a1);
            final String v2 = /*EL:614*/Descriptor.rename(v1, this.classnames);
            /*SL:615*/return this.destPool.addUtf8Info(v2);
        }
    }
    
    static class Parser extends Walker
    {
        ConstPool pool;
        Annotation[][] allParams;
        Annotation[] allAnno;
        Annotation currentAnno;
        MemberValue currentMember;
        
        Parser(final byte[] a1, final ConstPool a2) {
            super(a1);
            this.pool = a2;
        }
        
        Annotation[][] parseParameters() throws Exception {
            /*SL:639*/this.parameters();
            /*SL:640*/return this.allParams;
        }
        
        Annotation[] parseAnnotations() throws Exception {
            /*SL:644*/this.annotationArray();
            /*SL:645*/return this.allAnno;
        }
        
        MemberValue parseMemberValue() throws Exception {
            /*SL:649*/this.memberValue(0);
            /*SL:650*/return this.currentMember;
        }
        
        @Override
        void parameters(final int v1, int v2) throws Exception {
            final Annotation[][] v3 = /*EL:654*/new Annotation[v1][];
            /*SL:655*/for (int a1 = 0; a1 < v1; ++a1) {
                /*SL:656*/v2 = this.annotationArray(v2);
                /*SL:657*/v3[a1] = this.allAnno;
            }
            /*SL:660*/this.allParams = v3;
        }
        
        @Override
        int annotationArray(int v1, final int v2) throws Exception {
            final Annotation[] v3 = /*EL:664*/new Annotation[v2];
            /*SL:665*/for (int a1 = 0; a1 < v2; ++a1) {
                /*SL:666*/v1 = this.annotation(v1);
                /*SL:667*/v3[a1] = this.currentAnno;
            }
            /*SL:670*/this.allAnno = v3;
            /*SL:671*/return v1;
        }
        
        @Override
        int annotation(final int a1, final int a2, final int a3) throws Exception {
            /*SL:675*/this.currentAnno = new Annotation(a2, this.pool);
            /*SL:676*/return super.annotation(a1, a2, a3);
        }
        
        @Override
        int memberValuePair(int a1, final int a2) throws Exception {
            /*SL:680*/a1 = super.memberValuePair(a1, a2);
            /*SL:681*/this.currentAnno.addMemberValue(a2, this.currentMember);
            /*SL:682*/return a1;
        }
        
        @Override
        void constValueMember(final int v-1, final int v0) throws Exception {
            final ConstPool v = /*EL:687*/this.pool;
            MemberValue v2 = null;
            /*SL:688*/switch (v-1) {
                case 66: {
                    final MemberValue a1 = /*EL:690*/new ByteMemberValue(v0, v);
                    /*SL:691*/break;
                }
                case 67: {
                    final MemberValue a2 = /*EL:693*/new CharMemberValue(v0, v);
                    /*SL:694*/break;
                }
                case 68: {
                    /*SL:696*/v2 = new DoubleMemberValue(v0, v);
                    /*SL:697*/break;
                }
                case 70: {
                    /*SL:699*/v2 = new FloatMemberValue(v0, v);
                    /*SL:700*/break;
                }
                case 73: {
                    /*SL:702*/v2 = new IntegerMemberValue(v0, v);
                    /*SL:703*/break;
                }
                case 74: {
                    /*SL:705*/v2 = new LongMemberValue(v0, v);
                    /*SL:706*/break;
                }
                case 83: {
                    /*SL:708*/v2 = new ShortMemberValue(v0, v);
                    /*SL:709*/break;
                }
                case 90: {
                    /*SL:711*/v2 = new BooleanMemberValue(v0, v);
                    /*SL:712*/break;
                }
                case 115: {
                    /*SL:714*/v2 = new StringMemberValue(v0, v);
                    /*SL:715*/break;
                }
                default: {
                    /*SL:717*/throw new RuntimeException("unknown tag:" + v-1);
                }
            }
            /*SL:720*/this.currentMember = v2;
            /*SL:721*/super.constValueMember(v-1, v0);
        }
        
        @Override
        void enumMemberValue(final int a1, final int a2, final int a3) throws Exception {
            /*SL:727*/this.currentMember = new EnumMemberValue(a2, a3, this.pool);
            /*SL:729*/super.enumMemberValue(a1, a2, a3);
        }
        
        @Override
        void classMemberValue(final int a1, final int a2) throws Exception {
            /*SL:733*/this.currentMember = new ClassMemberValue(a2, this.pool);
            /*SL:734*/super.classMemberValue(a1, a2);
        }
        
        @Override
        int annotationMemberValue(int a1) throws Exception {
            final Annotation v1 = /*EL:738*/this.currentAnno;
            /*SL:739*/a1 = super.annotationMemberValue(a1);
            /*SL:740*/this.currentMember = new AnnotationMemberValue(this.currentAnno, this.pool);
            /*SL:741*/this.currentAnno = v1;
            /*SL:742*/return a1;
        }
        
        @Override
        int arrayMemberValue(int v1, final int v2) throws Exception {
            final ArrayMemberValue v3 = /*EL:746*/new ArrayMemberValue(this.pool);
            final MemberValue[] v4 = /*EL:747*/new MemberValue[v2];
            /*SL:748*/for (int a1 = 0; a1 < v2; ++a1) {
                /*SL:749*/v1 = this.memberValue(v1);
                /*SL:750*/v4[a1] = this.currentMember;
            }
            /*SL:753*/v3.setValue(v4);
            /*SL:754*/this.currentMember = v3;
            /*SL:755*/return v1;
        }
    }
}

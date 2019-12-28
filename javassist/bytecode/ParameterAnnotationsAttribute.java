package javassist.bytecode;

import java.util.HashMap;
import java.io.OutputStream;
import javassist.bytecode.annotation.AnnotationsWriter;
import java.io.ByteArrayOutputStream;
import javassist.bytecode.annotation.Annotation;
import java.util.Map;
import java.io.IOException;
import java.io.DataInputStream;

public class ParameterAnnotationsAttribute extends AttributeInfo
{
    public static final String visibleTag = "RuntimeVisibleParameterAnnotations";
    public static final String invisibleTag = "RuntimeInvisibleParameterAnnotations";
    
    public ParameterAnnotationsAttribute(final ConstPool a1, final String a2, final byte[] a3) {
        super(a1, a2, a3);
    }
    
    public ParameterAnnotationsAttribute(final ConstPool a1, final String a2) {
        this(a1, a2, new byte[] { 0 });
    }
    
    ParameterAnnotationsAttribute(final ConstPool a1, final int a2, final DataInputStream a3) throws IOException {
        super(a1, a2, a3);
    }
    
    public int numParameters() {
        /*SL:101*/return this.info[0] & 0xFF;
    }
    
    @Override
    public AttributeInfo copy(final ConstPool v1, final Map v2) {
        final AnnotationsAttribute.Copier v3 = /*EL:108*/new AnnotationsAttribute.Copier(this.info, this.constPool, v1, v2);
        try {
            /*SL:110*/v3.parameters();
            /*SL:112*/return new ParameterAnnotationsAttribute(v1, this.getName(), v3.close());
        }
        catch (Exception a1) {
            /*SL:115*/throw new RuntimeException(a1.toString());
        }
    }
    
    public Annotation[][] getAnnotations() {
        try {
            /*SL:133*/return new AnnotationsAttribute.Parser(this.info, this.constPool).parseParameters();
        }
        catch (Exception v1) {
            /*SL:136*/throw new RuntimeException(v1.toString());
        }
    }
    
    public void setAnnotations(final Annotation[][] v-4) {
        final ByteArrayOutputStream a2 = /*EL:150*/new ByteArrayOutputStream();
        final AnnotationsWriter v2 = /*EL:151*/new AnnotationsWriter(a2, this.constPool);
        try {
            final int length = /*EL:153*/v-4.length;
            /*SL:154*/v2.numParameters(length);
            /*SL:155*/for (final Annotation[] v : /*EL:156*/v-4) {
                /*SL:157*/v2.numAnnotations(v.length);
                /*SL:158*/for (int a1 = 0; a1 < v.length; ++a1) {
                    /*SL:159*/v[a1].write(v2);
                }
            }
            /*SL:162*/v2.close();
        }
        catch (IOException ex) {
            /*SL:165*/throw new RuntimeException(ex);
        }
        /*SL:168*/this.set(a2.toByteArray());
    }
    
    @Override
    void renameClass(final String a1, final String a2) {
        final HashMap v1 = /*EL:176*/new HashMap();
        /*SL:177*/v1.put(a1, a2);
        /*SL:178*/this.renameClass(v1);
    }
    
    @Override
    void renameClass(final Map v2) {
        final AnnotationsAttribute.Renamer v3 = /*EL:182*/new AnnotationsAttribute.Renamer(this.info, this.getConstPool(), v2);
        try {
            /*SL:184*/v3.parameters();
        }
        catch (Exception a1) {
            /*SL:186*/throw new RuntimeException(a1);
        }
    }
    
    @Override
    void getRefClasses(final Map a1) {
        /*SL:190*/this.renameClass(a1);
    }
    
    @Override
    public String toString() {
        final Annotation[][] annotations = /*EL:196*/this.getAnnotations();
        final StringBuilder sb = /*EL:197*/new StringBuilder();
        int v0 = /*EL:198*/0;
        /*SL:199*/while (v0 < annotations.length) {
            final Annotation[] v = /*EL:200*/annotations[v0++];
            int v2 = /*EL:201*/0;
            /*SL:202*/while (v2 < v.length) {
                /*SL:203*/sb.append(v[v2++].toString());
                /*SL:204*/if (v2 != v.length) {
                    /*SL:205*/sb.append(" ");
                }
            }
            /*SL:208*/if (v0 != annotations.length) {
                /*SL:209*/sb.append(", ");
            }
        }
        /*SL:212*/return sb.toString();
    }
}

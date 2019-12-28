package javassist.bytecode;

import java.io.OutputStream;
import javassist.bytecode.annotation.AnnotationsWriter;
import java.io.ByteArrayOutputStream;
import javassist.bytecode.annotation.MemberValue;
import java.util.Map;
import java.io.IOException;
import java.io.DataInputStream;

public class AnnotationDefaultAttribute extends AttributeInfo
{
    public static final String tag = "AnnotationDefault";
    
    public AnnotationDefaultAttribute(final ConstPool a1, final byte[] a2) {
        super(a1, "AnnotationDefault", a2);
    }
    
    public AnnotationDefaultAttribute(final ConstPool a1) {
        this(a1, new byte[] { 0, 0 });
    }
    
    AnnotationDefaultAttribute(final ConstPool a1, final int a2, final DataInputStream a3) throws IOException {
        super(a1, a2, a3);
    }
    
    @Override
    public AttributeInfo copy(final ConstPool v1, final Map v2) {
        final AnnotationsAttribute.Copier v3 = /*EL:108*/new AnnotationsAttribute.Copier(this.info, this.constPool, v1, v2);
        try {
            /*SL:111*/v3.memberValue(0);
            /*SL:112*/return new AnnotationDefaultAttribute(v1, v3.close());
        }
        catch (Exception a1) {
            /*SL:115*/throw new RuntimeException(a1.toString());
        }
    }
    
    public MemberValue getDefaultValue() {
        try {
            /*SL:126*/return new AnnotationsAttribute.Parser(this.info, this.constPool).parseMemberValue();
        }
        catch (Exception v1) {
            /*SL:129*/throw new RuntimeException(v1.toString());
        }
    }
    
    public void setDefaultValue(final MemberValue v2) {
        final ByteArrayOutputStream v3 = /*EL:140*/new ByteArrayOutputStream();
        final AnnotationsWriter v4 = /*EL:141*/new AnnotationsWriter(v3, this.constPool);
        try {
            /*SL:143*/v2.write(v4);
            /*SL:144*/v4.close();
        }
        catch (IOException a1) {
            /*SL:147*/throw new RuntimeException(a1);
        }
        /*SL:150*/this.set(v3.toByteArray());
    }
    
    @Override
    public String toString() {
        /*SL:158*/return this.getDefaultValue().toString();
    }
}

package javassist.convert;

import javassist.bytecode.CodeIterator;
import javassist.bytecode.CodeAttribute;
import javassist.Modifier;
import javassist.CtField;
import javassist.bytecode.ConstPool;
import javassist.CtClass;

public final class TransformFieldAccess extends Transformer
{
    private String newClassname;
    private String newFieldname;
    private String fieldname;
    private CtClass fieldClass;
    private boolean isPrivate;
    private int newIndex;
    private ConstPool constPool;
    
    public TransformFieldAccess(final Transformer a1, final CtField a2, final String a3, final String a4) {
        super(a1);
        this.fieldClass = a2.getDeclaringClass();
        this.fieldname = a2.getName();
        this.isPrivate = Modifier.isPrivate(a2.getModifiers());
        this.newClassname = a3;
        this.newFieldname = a4;
        this.constPool = null;
    }
    
    @Override
    public void initialize(final ConstPool a1, final CodeAttribute a2) {
        /*SL:47*/if (this.constPool != a1) {
            /*SL:48*/this.newIndex = 0;
        }
    }
    
    @Override
    public int transform(final CtClass v1, final int v2, final CodeIterator v3, final ConstPool v4) {
        final int v5 = /*EL:60*/v3.byteAt(v2);
        /*SL:61*/if (v5 == 180 || v5 == 178 || v5 == 181 || v5 == 179) {
            int a2 = /*EL:63*/v3.u16bitAt(v2 + 1);
            /*SL:65*/a2 = TransformReadField.isField(v1.getClassPool(), v4, this.fieldClass, this.fieldname, this.isPrivate, a2);
            /*SL:67*/if (a2 != null) {
                /*SL:68*/if (this.newIndex == 0) {
                    final int a3 = /*EL:69*/v4.addNameAndTypeInfo(this.newFieldname, a2);
                    /*SL:71*/this.newIndex = v4.addFieldrefInfo(v4.addClassInfo(this.newClassname), /*EL:72*/a3);
                    /*SL:73*/this.constPool = v4;
                }
                /*SL:76*/v3.write16bit(this.newIndex, v2 + 1);
            }
        }
        /*SL:80*/return v2;
    }
}

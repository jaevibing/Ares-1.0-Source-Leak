package org.spongepowered.asm.lib;

final class FieldWriter extends FieldVisitor
{
    private final ClassWriter cw;
    private final int access;
    private final int name;
    private final int desc;
    private int signature;
    private int value;
    private AnnotationWriter anns;
    private AnnotationWriter ianns;
    private AnnotationWriter tanns;
    private AnnotationWriter itanns;
    private Attribute attrs;
    
    FieldWriter(final ClassWriter a1, final int a2, final String a3, final String a4, final String a5, final Object a6) {
        super(327680);
        if (a1.firstField == null) {
            a1.firstField = this;
        }
        else {
            a1.lastField.fv = this;
        }
        a1.lastField = this;
        this.cw = a1;
        this.access = a2;
        this.name = a1.newUTF8(a3);
        this.desc = a1.newUTF8(a4);
        if (a5 != null) {
            this.signature = a1.newUTF8(a5);
        }
        if (a6 != null) {
            this.value = a1.newConstItem(a6).index;
        }
    }
    
    public AnnotationVisitor visitAnnotation(final String a1, final boolean a2) {
        final ByteVector v1 = /*EL:150*/new ByteVector();
        /*SL:152*/v1.putShort(this.cw.newUTF8(a1)).putShort(0);
        final AnnotationWriter v2 = /*EL:153*/new AnnotationWriter(this.cw, true, v1, v1, 2);
        /*SL:154*/if (a2) {
            /*SL:155*/v2.next = this.anns;
            /*SL:156*/this.anns = v2;
        }
        else {
            /*SL:158*/v2.next = this.ianns;
            /*SL:159*/this.ianns = v2;
        }
        /*SL:161*/return v2;
    }
    
    public AnnotationVisitor visitTypeAnnotation(final int a1, final TypePath a2, final String a3, final boolean a4) {
        final ByteVector v1 = /*EL:170*/new ByteVector();
        /*SL:172*/AnnotationWriter.putTarget(a1, a2, v1);
        /*SL:174*/v1.putShort(this.cw.newUTF8(a3)).putShort(0);
        final AnnotationWriter v2 = /*EL:175*/new AnnotationWriter(this.cw, true, v1, v1, v1.length - 2);
        /*SL:177*/if (a4) {
            /*SL:178*/v2.next = this.tanns;
            /*SL:179*/this.tanns = v2;
        }
        else {
            /*SL:181*/v2.next = this.itanns;
            /*SL:182*/this.itanns = v2;
        }
        /*SL:184*/return v2;
    }
    
    public void visitAttribute(final Attribute a1) {
        /*SL:189*/a1.next = this.attrs;
        /*SL:190*/this.attrs = a1;
    }
    
    public void visitEnd() {
    }
    
    int getSize() {
        int v1 = /*EL:207*/8;
        /*SL:208*/if (this.value != 0) {
            /*SL:209*/this.cw.newUTF8("ConstantValue");
            /*SL:210*/v1 += 8;
        }
        /*SL:212*/if ((this.access & 0x1000) != 0x0 && /*EL:213*/((this.cw.version & 0xFFFF) < 49 || (this.access & 0x40000) != 0x0)) {
            /*SL:215*/this.cw.newUTF8("Synthetic");
            /*SL:216*/v1 += 6;
        }
        /*SL:219*/if ((this.access & 0x20000) != 0x0) {
            /*SL:220*/this.cw.newUTF8("Deprecated");
            /*SL:221*/v1 += 6;
        }
        /*SL:223*/if (this.signature != 0) {
            /*SL:224*/this.cw.newUTF8("Signature");
            /*SL:225*/v1 += 8;
        }
        /*SL:227*/if (this.anns != null) {
            /*SL:228*/this.cw.newUTF8("RuntimeVisibleAnnotations");
            /*SL:229*/v1 += 8 + this.anns.getSize();
        }
        /*SL:231*/if (this.ianns != null) {
            /*SL:232*/this.cw.newUTF8("RuntimeInvisibleAnnotations");
            /*SL:233*/v1 += 8 + this.ianns.getSize();
        }
        /*SL:235*/if (this.tanns != null) {
            /*SL:236*/this.cw.newUTF8("RuntimeVisibleTypeAnnotations");
            /*SL:237*/v1 += 8 + this.tanns.getSize();
        }
        /*SL:239*/if (this.itanns != null) {
            /*SL:240*/this.cw.newUTF8("RuntimeInvisibleTypeAnnotations");
            /*SL:241*/v1 += 8 + this.itanns.getSize();
        }
        /*SL:243*/if (this.attrs != null) {
            /*SL:244*/v1 += this.attrs.getSize(this.cw, null, 0, -1, -1);
        }
        /*SL:246*/return v1;
    }
    
    void put(final ByteVector a1) {
        final int v1 = /*EL:256*/64;
        final int v2 = /*EL:257*/0x60000 | (this.access & 0x40000) / 64;
        /*SL:259*/a1.putShort(this.access & ~v2).putShort(this.name).putShort(this.desc);
        int v3 = /*EL:260*/0;
        /*SL:261*/if (this.value != 0) {
            /*SL:262*/++v3;
        }
        /*SL:264*/if ((this.access & 0x1000) != 0x0 && /*EL:265*/((this.cw.version & 0xFFFF) < 49 || (this.access & 0x40000) != 0x0)) {
            /*SL:267*/++v3;
        }
        /*SL:270*/if ((this.access & 0x20000) != 0x0) {
            /*SL:271*/++v3;
        }
        /*SL:273*/if (this.signature != 0) {
            /*SL:274*/++v3;
        }
        /*SL:276*/if (this.anns != null) {
            /*SL:277*/++v3;
        }
        /*SL:279*/if (this.ianns != null) {
            /*SL:280*/++v3;
        }
        /*SL:282*/if (this.tanns != null) {
            /*SL:283*/++v3;
        }
        /*SL:285*/if (this.itanns != null) {
            /*SL:286*/++v3;
        }
        /*SL:288*/if (this.attrs != null) {
            /*SL:289*/v3 += this.attrs.getCount();
        }
        /*SL:291*/a1.putShort(v3);
        /*SL:292*/if (this.value != 0) {
            /*SL:293*/a1.putShort(this.cw.newUTF8("ConstantValue"));
            /*SL:294*/a1.putInt(2).putShort(this.value);
        }
        /*SL:296*/if ((this.access & 0x1000) != 0x0 && /*EL:297*/((this.cw.version & 0xFFFF) < 49 || (this.access & 0x40000) != 0x0)) {
            /*SL:299*/a1.putShort(this.cw.newUTF8("Synthetic")).putInt(0);
        }
        /*SL:302*/if ((this.access & 0x20000) != 0x0) {
            /*SL:303*/a1.putShort(this.cw.newUTF8("Deprecated")).putInt(0);
        }
        /*SL:305*/if (this.signature != 0) {
            /*SL:306*/a1.putShort(this.cw.newUTF8("Signature"));
            /*SL:307*/a1.putInt(2).putShort(this.signature);
        }
        /*SL:309*/if (this.anns != null) {
            /*SL:310*/a1.putShort(this.cw.newUTF8("RuntimeVisibleAnnotations"));
            /*SL:311*/this.anns.put(a1);
        }
        /*SL:313*/if (this.ianns != null) {
            /*SL:314*/a1.putShort(this.cw.newUTF8("RuntimeInvisibleAnnotations"));
            /*SL:315*/this.ianns.put(a1);
        }
        /*SL:317*/if (this.tanns != null) {
            /*SL:318*/a1.putShort(this.cw.newUTF8("RuntimeVisibleTypeAnnotations"));
            /*SL:319*/this.tanns.put(a1);
        }
        /*SL:321*/if (this.itanns != null) {
            /*SL:322*/a1.putShort(this.cw.newUTF8("RuntimeInvisibleTypeAnnotations"));
            /*SL:323*/this.itanns.put(a1);
        }
        /*SL:325*/if (this.attrs != null) {
            /*SL:326*/this.attrs.put(this.cw, null, 0, -1, -1, a1);
        }
    }
}

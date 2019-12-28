package org.spongepowered.asm.lib;

import java.io.IOException;
import java.io.InputStream;

public class ClassReader
{
    static final boolean SIGNATURES = true;
    static final boolean ANNOTATIONS = true;
    static final boolean FRAMES = true;
    static final boolean WRITER = true;
    static final boolean RESIZE = true;
    public static final int SKIP_CODE = 1;
    public static final int SKIP_DEBUG = 2;
    public static final int SKIP_FRAMES = 4;
    public static final int EXPAND_FRAMES = 8;
    static final int EXPAND_ASM_INSNS = 256;
    public final byte[] b;
    private final int[] items;
    private final String[] strings;
    private final int maxStringLength;
    public final int header;
    
    public ClassReader(final byte[] a1) {
        this(a1, 0, a1.length);
    }
    
    public ClassReader(final byte[] v-6, final int v-5, final int v-4) {
        this.b = v-6;
        if (this.readShort(v-5 + 6) > 52) {
            throw new IllegalArgumentException();
        }
        this.items = new int[this.readUnsignedShort(v-5 + 8)];
        final int length = this.items.length;
        this.strings = new String[length];
        int maxStringLength = 0;
        int header = v-5 + 10;
        for (int v0 = 1; v0 < length; ++v0) {
            this.items[v0] = header + 1;
            int v = 0;
            switch (v-6[header]) {
                case 3:
                case 4:
                case 9:
                case 10:
                case 11:
                case 12:
                case 18: {
                    final int a1 = 5;
                    break;
                }
                case 5:
                case 6: {
                    final int a2 = 9;
                    ++v0;
                    break;
                }
                case 1: {
                    final int a3 = 3 + this.readUnsignedShort(header + 1);
                    if (a3 > maxStringLength) {
                        maxStringLength = a3;
                        break;
                    }
                    break;
                }
                case 15: {
                    v = 4;
                    break;
                }
                default: {
                    v = 3;
                    break;
                }
            }
            header += v;
        }
        this.maxStringLength = maxStringLength;
        this.header = header;
    }
    
    public int getAccess() {
        /*SL:244*/return this.readUnsignedShort(this.header);
    }
    
    public String getClassName() {
        /*SL:256*/return this.readClass(this.header + 2, new char[this.maxStringLength]);
    }
    
    public String getSuperName() {
        /*SL:270*/return this.readClass(this.header + 4, new char[this.maxStringLength]);
    }
    
    public String[] getInterfaces() {
        int n = /*EL:283*/this.header + 6;
        final int unsignedShort = /*EL:284*/this.readUnsignedShort(n);
        final String[] array = /*EL:285*/new String[unsignedShort];
        /*SL:286*/if (unsignedShort > 0) {
            final char[] v0 = /*EL:287*/new char[this.maxStringLength];
            /*SL:288*/for (int v = 0; v < unsignedShort; ++v) {
                /*SL:289*/n += 2;
                /*SL:290*/array[v] = this.readClass(n, v0);
            }
        }
        /*SL:293*/return array;
    }
    
    void copyPool(final ClassWriter v-8) {
        final char[] a2 = /*EL:304*/new char[this.maxStringLength];
        final int length = /*EL:305*/this.items.length;
        final Item[] array = /*EL:306*/new Item[length];
        /*SL:307*/for (int i = 1; i < length; ++i) {
            int n = /*EL:308*/this.items[i];
            final int n2 = /*EL:309*/this.b[n - 1];
            final Item item = /*EL:310*/new Item(i);
            /*SL:312*/switch (n2) {
                case 9:
                case 10:
                case 11: {
                    final int a1 = /*EL:316*/this.items[this.readUnsignedShort(n + 2)];
                    /*SL:317*/item.set(n2, this.readClass(n, a2), this.readUTF8(a1, a2), this.readUTF8(a1 + 2, a2));
                    /*SL:319*/break;
                }
                case 3: {
                    /*SL:321*/item.set(this.readInt(n));
                    /*SL:322*/break;
                }
                case 4: {
                    /*SL:324*/item.set(Float.intBitsToFloat(this.readInt(n)));
                    /*SL:325*/break;
                }
                case 12: {
                    /*SL:327*/item.set(n2, this.readUTF8(n, a2), this.readUTF8(n + 2, a2), null);
                    /*SL:329*/break;
                }
                case 5: {
                    /*SL:331*/item.set(this.readLong(n));
                    /*SL:332*/++i;
                    /*SL:333*/break;
                }
                case 6: {
                    /*SL:335*/item.set(Double.longBitsToDouble(this.readLong(n)));
                    /*SL:336*/++i;
                    /*SL:337*/break;
                }
                case 1: {
                    String v1 = /*EL:339*/this.strings[i];
                    /*SL:340*/if (v1 == null) {
                        /*SL:341*/n = this.items[i];
                        final String[] strings = /*EL:342*/this.strings;
                        final int n3 = i;
                        final String utf = this.readUTF(n + 2, this.readUnsignedShort(n), /*EL:343*/a2);
                        strings[n3] = utf;
                        v1 = utf;
                    }
                    /*SL:345*/item.set(n2, v1, null, null);
                    /*SL:346*/break;
                }
                case 15: {
                    final int v2 = /*EL:349*/this.items[this.readUnsignedShort(n + 1)];
                    final int v3 = /*EL:350*/this.items[this.readUnsignedShort(v2 + 2)];
                    /*SL:351*/item.set(20 + this.readByte(n), this.readClass(v2, a2), /*EL:352*/this.readUTF8(v3, a2), /*EL:353*/this.readUTF8(v3 + 2, a2));
                    /*SL:354*/break;
                }
                case 18: {
                    /*SL:357*/if (v-8.bootstrapMethods == null) {
                        /*SL:358*/this.copyBootstrapMethods(v-8, array, a2);
                    }
                    final int v3 = /*EL:360*/this.items[this.readUnsignedShort(n + 2)];
                    /*SL:361*/item.set(this.readUTF8(v3, a2), this.readUTF8(v3 + 2, a2), this.readUnsignedShort(n));
                    /*SL:363*/break;
                }
                default: {
                    /*SL:368*/item.set(n2, this.readUTF8(n, a2), null, null);
                    break;
                }
            }
            final int v2 = /*EL:372*/item.hashCode % array.length;
            /*SL:373*/item.next = array[v2];
            /*SL:374*/array[v2] = item;
        }
        int i = /*EL:377*/this.items[1] - 1;
        /*SL:378*/v-8.pool.putByteArray(this.b, i, this.header - i);
        /*SL:379*/v-8.items = array;
        /*SL:380*/v-8.threshold = (int)(0.75 * length);
        /*SL:381*/v-8.index = length;
    }
    
    private void copyBootstrapMethods(final ClassWriter v-7, final Item[] v-6, final char[] v-5) {
        int attributes = /*EL:394*/this.getAttributes();
        boolean b = /*EL:395*/false;
        /*SL:396*/for (String a2 = (String)this.readUnsignedShort(attributes); a2 > 0; --a2) {
            /*SL:397*/a2 = this.readUTF8(attributes + 2, v-5);
            /*SL:398*/if ("BootstrapMethods".equals(a2)) {
                /*SL:399*/b = true;
                /*SL:400*/break;
            }
            /*SL:402*/attributes += 6 + this.readInt(attributes + 4);
        }
        /*SL:404*/if (!b) {
            /*SL:405*/return;
        }
        final int unsignedShort = /*EL:408*/this.readUnsignedShort(attributes + 8);
        int i = /*EL:409*/0;
        int v0 = attributes + 10;
        while (i < unsignedShort) {
            final int v = /*EL:410*/v0 - attributes - 10;
            int v2 = /*EL:411*/this.readConst(this.readUnsignedShort(v0), v-5).hashCode();
            /*SL:412*/for (int a3 = this.readUnsignedShort(v0 + 2); a3 > 0; --a3) {
                /*SL:413*/v2 ^= this.readConst(this.readUnsignedShort(v0 + 4), v-5).hashCode();
                /*SL:414*/v0 += 2;
            }
            /*SL:416*/v0 += 4;
            final Item v3 = /*EL:417*/new Item(i);
            /*SL:418*/v3.set(v, v2 & Integer.MAX_VALUE);
            final int v4 = /*EL:419*/v3.hashCode % v-6.length;
            /*SL:420*/v3.next = v-6[v4];
            /*SL:421*/v-6[v4] = v3;
            ++i;
        }
        /*SL:423*/i = this.readInt(attributes + 4);
        final ByteVector v5 = /*EL:424*/new ByteVector(i + 62);
        /*SL:425*/v5.putByteArray(this.b, attributes + 10, i - 2);
        /*SL:426*/v-7.bootstrapMethodsCount = unsignedShort;
        /*SL:427*/v-7.bootstrapMethods = v5;
    }
    
    public ClassReader(final InputStream a1) throws IOException {
        this(readClass(a1, false));
    }
    
    public ClassReader(final String a1) throws IOException {
        this(readClass(ClassLoader.getSystemResourceAsStream(a1.replace('.', '/') + ".class"), true));
    }
    
    private static byte[] readClass(final InputStream v-5, final boolean v-4) throws IOException {
        /*SL:469*/if (v-5 == null) {
            /*SL:470*/throw new IOException("Class not found");
        }
        try {
            byte[] array = /*EL:473*/new byte[v-5.available()];
            int n = /*EL:474*/0;
            while (true) {
                final int read = /*EL:476*/v-5.read(array, n, array.length - n);
                /*SL:477*/if (read == -1) {
                    /*SL:478*/if (n < array.length) {
                        final byte[] a1 = /*EL:479*/new byte[n];
                        /*SL:480*/System.arraycopy(array, 0, a1, 0, n);
                        /*SL:481*/array = a1;
                    }
                    /*SL:483*/return array;
                }
                /*SL:485*/n += read;
                /*SL:486*/if (n != array.length) {
                    continue;
                }
                final int a2 = /*EL:487*/v-5.read();
                /*SL:488*/if (a2 < 0) {
                    /*SL:489*/return array;
                }
                final byte[] v1 = /*EL:491*/new byte[array.length + 1000];
                /*SL:492*/System.arraycopy(array, 0, v1, 0, n);
                /*SL:493*/v1[n++] = (byte)a2;
                /*SL:494*/array = v1;
            }
        }
        finally {
            /*SL:498*/if (v-4) {
                /*SL:499*/v-5.close();
            }
        }
    }
    
    public void accept(final ClassVisitor a1, final int a2) {
        /*SL:521*/this.accept(a1, new Attribute[0], a2);
    }
    
    public void accept(final ClassVisitor v-24, final Attribute[] v-23, final int v-22) {
        int v-25 = /*EL:547*/this.header;
        final char[] array = /*EL:548*/new char[this.maxStringLength];
        final Context context = /*EL:550*/new Context();
        /*SL:551*/context.attrs = v-23;
        /*SL:552*/context.flags = v-22;
        /*SL:553*/context.buffer = array;
        int unsignedShort = /*EL:556*/this.readUnsignedShort(v-25);
        final String class1 = /*EL:557*/this.readClass(v-25 + 2, array);
        final String class2 = /*EL:558*/this.readClass(v-25 + 4, array);
        final String[] a4 = /*EL:559*/new String[this.readUnsignedShort(v-25 + 6)];
        /*SL:560*/v-25 += 8;
        /*SL:561*/for (int a1 = 0; a1 < a4.length; ++a1) {
            /*SL:562*/a4[a1] = this.readClass(v-25, array);
            /*SL:563*/v-25 += 2;
        }
        String utf8 = /*EL:567*/null;
        String utf2 = /*EL:568*/null;
        String utf3 = /*EL:569*/null;
        String class3 = /*EL:570*/null;
        String utf4 = /*EL:571*/null;
        String utf5 = /*EL:572*/null;
        int a5 = /*EL:573*/0;
        int a6 = /*EL:574*/0;
        int a7 = /*EL:575*/0;
        int a8 = /*EL:576*/0;
        int a9 = /*EL:577*/0;
        Attribute attribute = /*EL:578*/null;
        /*SL:580*/v-25 = this.getAttributes();
        /*SL:581*/for (int i = this.readUnsignedShort(v-25); i > 0; --i) {
            final String utf6 = /*EL:582*/this.readUTF8(v-25 + 2, array);
            /*SL:585*/if ("SourceFile".equals(utf6)) {
                /*SL:586*/utf2 = this.readUTF8(v-25 + 8, array);
            }
            else/*SL:587*/ if ("InnerClasses".equals(utf6)) {
                /*SL:588*/a9 = v-25 + 8;
            }
            else/*SL:589*/ if ("EnclosingMethod".equals(utf6)) {
                /*SL:590*/class3 = this.readClass(v-25 + 8, array);
                final int a2 = /*EL:591*/this.readUnsignedShort(v-25 + 10);
                /*SL:592*/if (a2 != 0) {
                    /*SL:593*/utf4 = this.readUTF8(this.items[a2], array);
                    /*SL:594*/utf5 = this.readUTF8(this.items[a2] + 2, array);
                }
            }
            else/*SL:596*/ if ("Signature".equals(utf6)) {
                /*SL:597*/utf8 = this.readUTF8(v-25 + 8, array);
            }
            else/*SL:599*/ if ("RuntimeVisibleAnnotations".equals(utf6)) {
                /*SL:600*/a5 = v-25 + 8;
            }
            else/*SL:602*/ if ("RuntimeVisibleTypeAnnotations".equals(utf6)) {
                /*SL:603*/a7 = v-25 + 8;
            }
            else/*SL:604*/ if ("Deprecated".equals(utf6)) {
                /*SL:605*/unsignedShort |= 0x20000;
            }
            else/*SL:606*/ if ("Synthetic".equals(utf6)) {
                /*SL:607*/unsignedShort |= 0x41000;
            }
            else/*SL:609*/ if ("SourceDebugExtension".equals(utf6)) {
                final int a3 = /*EL:610*/this.readInt(v-25 + 4);
                /*SL:611*/utf3 = this.readUTF(v-25 + 8, a3, new char[a3]);
            }
            else/*SL:613*/ if ("RuntimeInvisibleAnnotations".equals(utf6)) {
                /*SL:614*/a6 = v-25 + 8;
            }
            else/*SL:616*/ if ("RuntimeInvisibleTypeAnnotations".equals(utf6)) {
                /*SL:617*/a8 = v-25 + 8;
            }
            else/*SL:618*/ if ("BootstrapMethods".equals(utf6)) {
                final int[] v0 = /*EL:619*/new int[this.readUnsignedShort(v-25 + 8)];
                int v = /*EL:620*/0;
                int v2 = v-25 + 10;
                while (v < v0.length) {
                    /*SL:621*/v0[v] = v2;
                    /*SL:622*/v2 += 2 + this.readUnsignedShort(v2 + 2) << 1;
                    ++v;
                }
                /*SL:624*/context.bootstrapMethods = v0;
            }
            else {
                final Attribute v3 = /*EL:626*/this.readAttribute(v-23, utf6, v-25 + 8, this.readInt(v-25 + 4), /*EL:627*/array, -1, null);
                /*SL:628*/if (v3 != null) {
                    /*SL:629*/v3.next = attribute;
                    /*SL:630*/attribute = v3;
                }
            }
            /*SL:633*/v-25 += 6 + this.readInt(v-25 + 4);
        }
        /*SL:637*/v-24.visit(this.readInt(this.items[1] - 7), unsignedShort, class1, utf8, class2, a4);
        /*SL:641*/if ((v-22 & 0x2) == 0x0 && (utf2 != null || utf3 != null)) {
            /*SL:643*/v-24.visitSource(utf2, utf3);
        }
        /*SL:647*/if (class3 != null) {
            /*SL:648*/v-24.visitOuterClass(class3, utf4, utf5);
        }
        /*SL:653*/if (a5 != 0) {
            int i = /*EL:654*/this.readUnsignedShort(a5);
            int j = a5 + 2;
            while (i > 0) {
                /*SL:655*/j = this.readAnnotationValues(j + 2, array, true, v-24.visitAnnotation(this.readUTF8(j, array), /*EL:656*/true));
                --i;
            }
        }
        /*SL:659*/if (a6 != 0) {
            int i = /*EL:660*/this.readUnsignedShort(a6);
            int j = a6 + 2;
            while (i > 0) {
                /*SL:661*/j = this.readAnnotationValues(j + 2, array, true, v-24.visitAnnotation(this.readUTF8(j, array), /*EL:662*/false));
                --i;
            }
        }
        /*SL:665*/if (a7 != 0) {
            int i = /*EL:666*/this.readUnsignedShort(a7);
            int j = a7 + 2;
            while (i > 0) {
                /*SL:667*/j = this.readAnnotationTarget(context, j);
                /*SL:668*/j = this.readAnnotationValues(j + 2, array, true, v-24.visitTypeAnnotation(context.typeRef, context.typePath, this.readUTF8(j, array), /*EL:670*/true));
                --i;
            }
        }
        /*SL:673*/if (a8 != 0) {
            int i = /*EL:674*/this.readUnsignedShort(a8);
            int j = a8 + 2;
            while (i > 0) {
                /*SL:675*/j = this.readAnnotationTarget(context, j);
                /*SL:676*/j = this.readAnnotationValues(j + 2, array, true, v-24.visitTypeAnnotation(context.typeRef, context.typePath, this.readUTF8(j, array), /*EL:678*/false));
                --i;
            }
        }
        /*SL:683*/while (attribute != null) {
            final Attribute next = /*EL:684*/attribute.next;
            /*SL:685*/attribute.next = null;
            /*SL:686*/v-24.visitAttribute(attribute);
            /*SL:687*/attribute = next;
        }
        /*SL:691*/if (a9 != 0) {
            int i = /*EL:692*/a9 + 2;
            /*SL:693*/for (int j = this.readUnsignedShort(a9); j > 0; --j) {
                /*SL:694*/v-24.visitInnerClass(this.readClass(i, array), this.readClass(i + 2, array), /*EL:695*/this.readUTF8(i + 4, array), this.readUnsignedShort(i + 6));
                /*SL:697*/i += 8;
            }
        }
        /*SL:702*/v-25 = this.header + 10 + 2 * a4.length;
        /*SL:703*/for (int i = this.readUnsignedShort(v-25 - 2); i > 0; --i) {
            /*SL:704*/v-25 = this.readField(v-24, context, v-25);
        }
        /*SL:706*/v-25 += 2;
        /*SL:707*/for (int i = this.readUnsignedShort(v-25 - 2); i > 0; --i) {
            /*SL:708*/v-25 = this.readMethod(v-24, context, v-25);
        }
        /*SL:712*/v-24.visitEnd();
    }
    
    private int readField(final ClassVisitor v-13, final Context v-12, int v-11) {
        final char[] buffer = /*EL:729*/v-12.buffer;
        int unsignedShort = /*EL:730*/this.readUnsignedShort(v-11);
        final String utf8 = /*EL:731*/this.readUTF8(v-11 + 2, buffer);
        final String utf2 = /*EL:732*/this.readUTF8(v-11 + 4, buffer);
        /*SL:733*/v-11 += 6;
        String utf3 = /*EL:736*/null;
        int a4 = /*EL:737*/0;
        int a5 = /*EL:738*/0;
        int a6 = /*EL:739*/0;
        int a7 = /*EL:740*/0;
        Object a8 = /*EL:741*/null;
        Attribute v0 = /*EL:742*/null;
        /*SL:744*/for (int v = this.readUnsignedShort(v-11); v > 0; --v) {
            String a3 = /*EL:745*/this.readUTF8(v-11 + 2, buffer);
            /*SL:748*/if ("ConstantValue".equals(a3)) {
                final int a2 = /*EL:749*/this.readUnsignedShort(v-11 + 8);
                /*SL:750*/a8 = ((a2 == 0) ? null : this.readConst(a2, buffer));
            }
            else/*SL:751*/ if ("Signature".equals(a3)) {
                /*SL:752*/utf3 = this.readUTF8(v-11 + 8, buffer);
            }
            else/*SL:753*/ if ("Deprecated".equals(a3)) {
                /*SL:754*/unsignedShort |= 0x20000;
            }
            else/*SL:755*/ if ("Synthetic".equals(a3)) {
                /*SL:756*/unsignedShort |= 0x41000;
            }
            else/*SL:759*/ if ("RuntimeVisibleAnnotations".equals(a3)) {
                /*SL:760*/a4 = v-11 + 8;
            }
            else/*SL:762*/ if ("RuntimeVisibleTypeAnnotations".equals(a3)) {
                /*SL:763*/a6 = v-11 + 8;
            }
            else/*SL:765*/ if ("RuntimeInvisibleAnnotations".equals(a3)) {
                /*SL:766*/a5 = v-11 + 8;
            }
            else/*SL:768*/ if ("RuntimeInvisibleTypeAnnotations".equals(a3)) {
                /*SL:769*/a7 = v-11 + 8;
            }
            else {
                /*SL:771*/a3 = this.readAttribute(v-12.attrs, a3, v-11 + 8, this.readInt(v-11 + 4), /*EL:772*/buffer, -1, null);
                /*SL:773*/if (a3 != null) {
                    /*SL:774*/a3.next = v0;
                    /*SL:775*/v0 = a3;
                }
            }
            /*SL:778*/v-11 += 6 + this.readInt(v-11 + 4);
        }
        /*SL:780*/v-11 += 2;
        final FieldVisitor v2 = /*EL:783*/v-13.visitField(unsignedShort, utf8, utf2, utf3, a8);
        /*SL:785*/if (v2 == null) {
            /*SL:786*/return v-11;
        }
        /*SL:790*/if (a4 != 0) {
            int v3 = /*EL:791*/this.readUnsignedShort(a4);
            int v4 = a4 + 2;
            while (v3 > 0) {
                /*SL:792*/v4 = this.readAnnotationValues(v4 + 2, buffer, true, v2.visitAnnotation(this.readUTF8(v4, buffer), /*EL:793*/true));
                --v3;
            }
        }
        /*SL:796*/if (a5 != 0) {
            int v3 = /*EL:797*/this.readUnsignedShort(a5);
            int v4 = a5 + 2;
            while (v3 > 0) {
                /*SL:798*/v4 = this.readAnnotationValues(v4 + 2, buffer, true, v2.visitAnnotation(this.readUTF8(v4, buffer), /*EL:799*/false));
                --v3;
            }
        }
        /*SL:802*/if (a6 != 0) {
            int v3 = /*EL:803*/this.readUnsignedShort(a6);
            int v4 = a6 + 2;
            while (v3 > 0) {
                /*SL:804*/v4 = this.readAnnotationTarget(v-12, v4);
                /*SL:805*/v4 = this.readAnnotationValues(v4 + 2, buffer, true, v2.visitTypeAnnotation(v-12.typeRef, v-12.typePath, this.readUTF8(v4, buffer), /*EL:807*/true));
                --v3;
            }
        }
        /*SL:810*/if (a7 != 0) {
            int v3 = /*EL:811*/this.readUnsignedShort(a7);
            int v4 = a7 + 2;
            while (v3 > 0) {
                /*SL:812*/v4 = this.readAnnotationTarget(v-12, v4);
                /*SL:813*/v4 = this.readAnnotationValues(v4 + 2, buffer, true, v2.visitTypeAnnotation(v-12.typeRef, v-12.typePath, this.readUTF8(v4, buffer), /*EL:815*/false));
                --v3;
            }
        }
        /*SL:820*/while (v0 != null) {
            final Attribute v5 = /*EL:821*/v0.next;
            /*SL:822*/v0.next = null;
            /*SL:823*/v2.visitAttribute(v0);
            /*SL:824*/v0 = v5;
        }
        /*SL:828*/v2.visitEnd();
        /*SL:830*/return v-11;
    }
    
    private int readMethod(final ClassVisitor v-17, final Context v-16, int v-15) {
        final char[] buffer = /*EL:847*/v-16.buffer;
        /*SL:848*/v-16.access = this.readUnsignedShort(v-15);
        /*SL:849*/v-16.name = this.readUTF8(v-15 + 2, buffer);
        /*SL:850*/v-16.desc = this.readUTF8(v-15 + 4, buffer);
        /*SL:851*/v-15 += 6;
        int v-18 = /*EL:854*/0;
        int n = /*EL:855*/0;
        String[] a4 = /*EL:856*/null;
        String utf8 = /*EL:857*/null;
        int n2 = /*EL:858*/0;
        int a5 = /*EL:859*/0;
        int a6 = /*EL:860*/0;
        int a7 = /*EL:861*/0;
        int a8 = /*EL:862*/0;
        int v-19 = /*EL:863*/0;
        int v10 = /*EL:864*/0;
        int v11 = /*EL:865*/0;
        final int classReaderOffset = /*EL:866*/v-15;
        Attribute v0 = /*EL:867*/null;
        /*SL:869*/for (int v = this.readUnsignedShort(v-15); v > 0; --v) {
            String a3 = /*EL:870*/this.readUTF8(v-15 + 2, buffer);
            /*SL:873*/if ("Code".equals(a3)) {
                /*SL:874*/if ((v-16.flags & 0x1) == 0x0) {
                    /*SL:875*/v-18 = v-15 + 8;
                }
            }
            else/*SL:877*/ if ("Exceptions".equals(a3)) {
                /*SL:878*/a4 = new String[this.readUnsignedShort(v-15 + 8)];
                /*SL:879*/n = v-15 + 10;
                /*SL:880*/for (int a2 = 0; a2 < a4.length; ++a2) {
                    /*SL:881*/a4[a2] = this.readClass(n, buffer);
                    /*SL:882*/n += 2;
                }
            }
            else/*SL:884*/ if ("Signature".equals(a3)) {
                /*SL:885*/utf8 = this.readUTF8(v-15 + 8, buffer);
            }
            else/*SL:886*/ if ("Deprecated".equals(a3)) {
                /*SL:887*/v-16.access |= 0x20000;
            }
            else/*SL:889*/ if ("RuntimeVisibleAnnotations".equals(a3)) {
                /*SL:890*/a5 = v-15 + 8;
            }
            else/*SL:892*/ if ("RuntimeVisibleTypeAnnotations".equals(a3)) {
                /*SL:893*/a7 = v-15 + 8;
            }
            else/*SL:894*/ if ("AnnotationDefault".equals(a3)) {
                /*SL:895*/v-19 = v-15 + 8;
            }
            else/*SL:896*/ if ("Synthetic".equals(a3)) {
                /*SL:897*/v-16.access |= 0x41000;
            }
            else/*SL:900*/ if ("RuntimeInvisibleAnnotations".equals(a3)) {
                /*SL:901*/a6 = v-15 + 8;
            }
            else/*SL:903*/ if ("RuntimeInvisibleTypeAnnotations".equals(a3)) {
                /*SL:904*/a8 = v-15 + 8;
            }
            else/*SL:906*/ if ("RuntimeVisibleParameterAnnotations".equals(a3)) {
                /*SL:907*/v10 = v-15 + 8;
            }
            else/*SL:909*/ if ("RuntimeInvisibleParameterAnnotations".equals(a3)) {
                /*SL:910*/v11 = v-15 + 8;
            }
            else/*SL:911*/ if ("MethodParameters".equals(a3)) {
                /*SL:912*/n2 = v-15 + 8;
            }
            else {
                /*SL:914*/a3 = this.readAttribute(v-16.attrs, a3, v-15 + 8, this.readInt(v-15 + 4), /*EL:915*/buffer, -1, null);
                /*SL:916*/if (a3 != null) {
                    /*SL:917*/a3.next = v0;
                    /*SL:918*/v0 = a3;
                }
            }
            /*SL:921*/v-15 += 6 + this.readInt(v-15 + 4);
        }
        /*SL:923*/v-15 += 2;
        final MethodVisitor v2 = /*EL:926*/v-17.visitMethod(v-16.access, v-16.name, v-16.desc, utf8, a4);
        /*SL:928*/if (v2 == null) {
            /*SL:929*/return v-15;
        }
        /*SL:942*/if (v2 instanceof MethodWriter) {
            final MethodWriter v3 = /*EL:943*/(MethodWriter)v2;
            /*SL:944*/if (v3.cw.cr == this && utf8 == v3.signature) {
                boolean v4 = /*EL:945*/false;
                /*SL:946*/if (a4 == null) {
                    /*SL:947*/v4 = (v3.exceptionCount == 0);
                }
                else/*SL:948*/ if (a4.length == v3.exceptionCount) {
                    /*SL:949*/v4 = true;
                    /*SL:950*/for (int v5 = a4.length - 1; v5 >= 0; --v5) {
                        /*SL:951*/n -= 2;
                        /*SL:952*/if (v3.exceptions[v5] != this.readUnsignedShort(n)) {
                            /*SL:953*/v4 = false;
                            /*SL:954*/break;
                        }
                    }
                }
                /*SL:958*/if (v4) {
                    /*SL:964*/v3.classReaderOffset = classReaderOffset;
                    /*SL:965*/v3.classReaderLength = v-15 - classReaderOffset;
                    /*SL:966*/return v-15;
                }
            }
        }
        /*SL:972*/if (n2 != 0) {
            /*SL:973*/for (int v6 = this.b[n2] & 0xFF, v7 = n2 + 1; v6 > 0; --v6, v7 += 4) {
                /*SL:974*/v2.visitParameter(this.readUTF8(v7, buffer), this.readUnsignedShort(v7 + 2));
            }
        }
        /*SL:979*/if (v-19 != 0) {
            final AnnotationVisitor v8 = /*EL:980*/v2.visitAnnotationDefault();
            /*SL:981*/this.readAnnotationValue(v-19, buffer, null, v8);
            /*SL:982*/if (v8 != null) {
                /*SL:983*/v8.visitEnd();
            }
        }
        /*SL:986*/if (a5 != 0) {
            int v6 = /*EL:987*/this.readUnsignedShort(a5);
            int v7 = a5 + 2;
            while (v6 > 0) {
                /*SL:988*/v7 = this.readAnnotationValues(v7 + 2, buffer, true, v2.visitAnnotation(this.readUTF8(v7, buffer), /*EL:989*/true));
                --v6;
            }
        }
        /*SL:992*/if (a6 != 0) {
            int v6 = /*EL:993*/this.readUnsignedShort(a6);
            int v7 = a6 + 2;
            while (v6 > 0) {
                /*SL:994*/v7 = this.readAnnotationValues(v7 + 2, buffer, true, v2.visitAnnotation(this.readUTF8(v7, buffer), /*EL:995*/false));
                --v6;
            }
        }
        /*SL:998*/if (a7 != 0) {
            int v6 = /*EL:999*/this.readUnsignedShort(a7);
            int v7 = a7 + 2;
            while (v6 > 0) {
                /*SL:1000*/v7 = this.readAnnotationTarget(v-16, v7);
                /*SL:1001*/v7 = this.readAnnotationValues(v7 + 2, buffer, true, v2.visitTypeAnnotation(v-16.typeRef, v-16.typePath, this.readUTF8(v7, buffer), /*EL:1003*/true));
                --v6;
            }
        }
        /*SL:1006*/if (a8 != 0) {
            int v6 = /*EL:1007*/this.readUnsignedShort(a8);
            int v7 = a8 + 2;
            while (v6 > 0) {
                /*SL:1008*/v7 = this.readAnnotationTarget(v-16, v7);
                /*SL:1009*/v7 = this.readAnnotationValues(v7 + 2, buffer, true, v2.visitTypeAnnotation(v-16.typeRef, v-16.typePath, this.readUTF8(v7, buffer), /*EL:1011*/false));
                --v6;
            }
        }
        /*SL:1014*/if (v10 != 0) {
            /*SL:1015*/this.readParameterAnnotations(v2, v-16, v10, true);
        }
        /*SL:1017*/if (v11 != 0) {
            /*SL:1018*/this.readParameterAnnotations(v2, v-16, v11, false);
        }
        /*SL:1022*/while (v0 != null) {
            final Attribute v9 = /*EL:1023*/v0.next;
            /*SL:1024*/v0.next = null;
            /*SL:1025*/v2.visitAttribute(v0);
            /*SL:1026*/v0 = v9;
        }
        /*SL:1030*/if (v-18 != 0) {
            /*SL:1031*/v2.visitCode();
            /*SL:1032*/this.readCode(v2, v-16, v-18);
        }
        /*SL:1036*/v2.visitEnd();
        /*SL:1038*/return v-15;
    }
    
    private void readCode(final MethodVisitor v-11, final Context v-10, int v-9) {
        final byte[] b = /*EL:1053*/this.b;
        final char[] buffer = /*EL:1054*/v-10.buffer;
        final int unsignedShort = /*EL:1055*/this.readUnsignedShort(v-9);
        final int unsignedShort2 = /*EL:1056*/this.readUnsignedShort(v-9 + 2);
        final int int1 = /*EL:1057*/this.readInt(v-9 + 4);
        /*SL:1058*/v-9 += 8;
        final int n = /*EL:1061*/v-9;
        final int n2 = /*EL:1062*/v-9 + int1;
        final Label[] labels = /*EL:1063*/new Label[int1 + 2];
        v-10.labels = labels;
        final Label[] array = labels;
        /*SL:1064*/this.readLabel(int1 + 1, array);
        /*SL:1065*/while (v-9 < n2) {
            int a3 = /*EL:1066*/v-9 - n;
            int v1 = /*EL:1067*/b[v-9] & 0xFF;
            /*SL:1068*/switch (ClassWriter.TYPE[v1]) {
                case 0:
                case 4: {
                    /*SL:1071*/++v-9;
                    /*SL:1072*/continue;
                }
                case 9: {
                    /*SL:1074*/this.readLabel(a3 + this.readShort(v-9 + 1), array);
                    /*SL:1075*/v-9 += 3;
                    /*SL:1076*/continue;
                }
                case 18: {
                    /*SL:1078*/this.readLabel(a3 + this.readUnsignedShort(v-9 + 1), array);
                    /*SL:1079*/v-9 += 3;
                    /*SL:1080*/continue;
                }
                case 10: {
                    /*SL:1082*/this.readLabel(a3 + this.readInt(v-9 + 1), array);
                    /*SL:1083*/v-9 += 5;
                    /*SL:1084*/continue;
                }
                case 17: {
                    /*SL:1086*/v1 = (b[v-9 + 1] & 0xFF);
                    /*SL:1087*/if (v1 == 132) {
                        /*SL:1088*/v-9 += 6;
                        continue;
                    }
                    /*SL:1090*/v-9 += 4;
                    /*SL:1092*/continue;
                }
                case 14: {
                    /*SL:1095*/v-9 = v-9 + 4 - (a3 & 0x3);
                    /*SL:1097*/this.readLabel(a3 + this.readInt(v-9), array);
                    /*SL:1098*/for (int a2 = this.readInt(v-9 + 8) - this.readInt(v-9 + 4) + 1; a2 > 0; --a2) {
                        /*SL:1099*/this.readLabel(a3 + this.readInt(v-9 + 12), array);
                        /*SL:1100*/v-9 += 4;
                    }
                    /*SL:1102*/v-9 += 12;
                    /*SL:1103*/continue;
                }
                case 15: {
                    /*SL:1106*/v-9 = v-9 + 4 - (a3 & 0x3);
                    /*SL:1108*/this.readLabel(a3 + this.readInt(v-9), array);
                    /*SL:1109*/for (a3 = this.readInt(v-9 + 4); a3 > 0; --a3) {
                        /*SL:1110*/this.readLabel(a3 + this.readInt(v-9 + 12), array);
                        /*SL:1111*/v-9 += 8;
                    }
                    /*SL:1113*/v-9 += 8;
                    /*SL:1114*/continue;
                }
                case 1:
                case 3:
                case 11: {
                    /*SL:1118*/v-9 += 2;
                    /*SL:1119*/continue;
                }
                case 2:
                case 5:
                case 6:
                case 12:
                case 13: {
                    /*SL:1125*/v-9 += 3;
                    /*SL:1126*/continue;
                }
                case 7:
                case 8: {
                    /*SL:1129*/v-9 += 5;
                    /*SL:1130*/continue;
                }
                default: {
                    /*SL:1133*/v-9 += 4;
                    continue;
                }
            }
        }
        /*SL:1139*/for (int v2 = this.readUnsignedShort(v-9); v2 > 0; --v2) {
            final Label v3 = /*EL:1140*/this.readLabel(this.readUnsignedShort(v-9 + 2), array);
            final Label v4 = /*EL:1141*/this.readLabel(this.readUnsignedShort(v-9 + 4), array);
            final Label v5 = /*EL:1142*/this.readLabel(this.readUnsignedShort(v-9 + 6), array);
            final String v6 = /*EL:1143*/this.readUTF8(this.items[this.readUnsignedShort(v-9 + 8)], buffer);
            /*SL:1144*/v-11.visitTryCatchBlock(v3, v4, v5, v6);
            /*SL:1145*/v-9 += 8;
        }
        /*SL:1147*/v-9 += 2;
        int[] v7 = /*EL:1150*/null;
        int[] v8 = /*EL:1151*/null;
        int v9 = /*EL:1152*/0;
        int v10 = /*EL:1153*/0;
        int v11 = /*EL:1154*/-1;
        int v12 = /*EL:1155*/-1;
        int v13 = /*EL:1156*/0;
        int v14 = /*EL:1157*/0;
        boolean v15 = /*EL:1158*/true;
        final boolean v16 = /*EL:1159*/(v-10.flags & 0x8) != 0x0;
        int v17 = /*EL:1160*/0;
        int v18 = /*EL:1161*/0;
        int v19 = /*EL:1162*/0;
        Context v20 = /*EL:1163*/null;
        Attribute v21 = /*EL:1164*/null;
        /*SL:1166*/for (int v22 = this.readUnsignedShort(v-9); v22 > 0; --v22) {
            final String v23 = /*EL:1167*/this.readUTF8(v-9 + 2, buffer);
            /*SL:1168*/if ("LocalVariableTable".equals(v23)) {
                /*SL:1169*/if ((v-10.flags & 0x2) == 0x0) {
                    /*SL:1170*/v13 = v-9 + 8;
                    int v24 = /*EL:1171*/this.readUnsignedShort(v-9 + 8);
                    int v25 = v-9;
                    while (v24 > 0) {
                        int v26 = /*EL:1172*/this.readUnsignedShort(v25 + 10);
                        /*SL:1173*/if (array[v26] == null) {
                            final Label label = /*EL:1174*/this.readLabel(v26, array);
                            label.status |= 0x1;
                        }
                        /*SL:1176*/v26 += this.readUnsignedShort(v25 + 12);
                        /*SL:1177*/if (array[v26] == null) {
                            final Label label2 = /*EL:1178*/this.readLabel(v26, array);
                            label2.status |= 0x1;
                        }
                        /*SL:1180*/v25 += 10;
                        --v24;
                    }
                }
            }
            else/*SL:1183*/ if ("LocalVariableTypeTable".equals(v23)) {
                /*SL:1184*/v14 = v-9 + 8;
            }
            else/*SL:1185*/ if ("LineNumberTable".equals(v23)) {
                /*SL:1186*/if ((v-10.flags & 0x2) == 0x0) {
                    int v24 = /*EL:1187*/this.readUnsignedShort(v-9 + 8);
                    int v25 = v-9;
                    while (v24 > 0) {
                        final int v26 = /*EL:1188*/this.readUnsignedShort(v25 + 10);
                        /*SL:1189*/if (array[v26] == null) {
                            final Label label3 = /*EL:1190*/this.readLabel(v26, array);
                            label3.status |= 0x1;
                        }
                        Label v27;
                        /*SL:1193*/for (v27 = array[v26]; v27.line > 0; /*SL:1197*/v27 = v27.next) {
                            if (v27.next == null) {
                                v27.next = new Label();
                            }
                        }
                        /*SL:1199*/v27.line = this.readUnsignedShort(v25 + 12);
                        /*SL:1200*/v25 += 4;
                        --v24;
                    }
                }
            }
            else/*SL:1204*/ if ("RuntimeVisibleTypeAnnotations".equals(v23)) {
                /*SL:1205*/v7 = this.readTypeAnnotations(v-11, v-10, v-9 + 8, true);
                /*SL:1207*/v11 = ((v7.length == 0 || this.readByte(v7[0]) < 67) ? -1 : this.readUnsignedShort(v7[0] + 1));
            }
            else/*SL:1209*/ if ("RuntimeInvisibleTypeAnnotations".equals(v23)) {
                /*SL:1210*/v8 = this.readTypeAnnotations(v-11, v-10, v-9 + 8, false);
                /*SL:1212*/v12 = ((v8.length == 0 || this.readByte(v8[0]) < 67) ? -1 : this.readUnsignedShort(v8[0] + 1));
            }
            else/*SL:1213*/ if ("StackMapTable".equals(v23)) {
                /*SL:1214*/if ((v-10.flags & 0x4) == 0x0) {
                    /*SL:1215*/v17 = v-9 + 10;
                    /*SL:1216*/v18 = this.readInt(v-9 + 4);
                    /*SL:1217*/v19 = this.readUnsignedShort(v-9 + 8);
                }
            }
            else/*SL:1237*/ if ("StackMap".equals(v23)) {
                /*SL:1238*/if ((v-10.flags & 0x4) == 0x0) {
                    /*SL:1239*/v15 = false;
                    /*SL:1240*/v17 = v-9 + 10;
                    /*SL:1241*/v18 = this.readInt(v-9 + 4);
                    /*SL:1242*/v19 = this.readUnsignedShort(v-9 + 8);
                }
            }
            else {
                /*SL:1250*/for (int v24 = 0; v24 < v-10.attrs.length; ++v24) {
                    /*SL:1251*/if (v-10.attrs[v24].type.equals(v23)) {
                        final Attribute v28 = /*EL:1252*/v-10.attrs[v24].read(this, v-9 + 8, this.readInt(v-9 + 4), /*EL:1253*/buffer, n - 8, array);
                        /*SL:1254*/if (v28 != null) {
                            /*SL:1255*/v28.next = v21;
                            /*SL:1256*/v21 = v28;
                        }
                    }
                }
            }
            /*SL:1261*/v-9 += 6 + this.readInt(v-9 + 4);
        }
        /*SL:1263*/v-9 += 2;
        /*SL:1266*/if (v17 != 0) {
            /*SL:1272*/v20 = v-10;
            /*SL:1273*/v20.offset = -1;
            /*SL:1274*/v20.mode = 0;
            /*SL:1275*/v20.localCount = 0;
            /*SL:1276*/v20.localDiff = 0;
            /*SL:1277*/v20.stackCount = 0;
            /*SL:1278*/v20.local = new Object[unsignedShort2];
            /*SL:1279*/v20.stack = new Object[unsignedShort];
            /*SL:1280*/if (v16) {
                /*SL:1281*/this.getImplicitFrame(v-10);
            }
            /*SL:1294*/for (int v22 = v17; v22 < v17 + v18 - 2; ++v22) {
                /*SL:1295*/if (b[v22] == 8) {
                    final int v29 = /*EL:1296*/this.readUnsignedShort(v22 + 1);
                    /*SL:1297*/if (v29 >= 0 && v29 < int1 && /*EL:1298*/(b[n + v29] & 0xFF) == 0xBB) {
                        /*SL:1299*/this.readLabel(v29, array);
                    }
                }
            }
        }
        /*SL:1305*/if ((v-10.flags & 0x100) != 0x0) {
            /*SL:1317*/v-11.visitFrame(-1, unsignedShort2, null, 0, null);
        }
        int v22 = /*EL:1321*/((v-10.flags & 0x100) == 0x0) ? -33 : 0;
        /*SL:1322*/v-9 = n;
        /*SL:1323*/while (v-9 < n2) {
            final int v29 = /*EL:1324*/v-9 - n;
            final Label v30 = /*EL:1327*/array[v29];
            /*SL:1328*/if (v30 != null) {
                Label v31 = /*EL:1329*/v30.next;
                /*SL:1330*/v30.next = null;
                /*SL:1331*/v-11.visitLabel(v30);
                /*SL:1332*/if ((v-10.flags & 0x2) == 0x0 && v30.line > 0) {
                    /*SL:1333*/v-11.visitLineNumber(v30.line, v30);
                    /*SL:1334*/while (v31 != null) {
                        /*SL:1335*/v-11.visitLineNumber(v31.line, v30);
                        /*SL:1336*/v31 = v31.next;
                    }
                }
            }
            /*SL:1342*/while (v20 != null && (v20.offset == v29 || v20.offset == -1)) {
                /*SL:1346*/if (v20.offset != -1) {
                    /*SL:1347*/if (!v15 || v16) {
                        /*SL:1348*/v-11.visitFrame(-1, v20.localCount, v20.local, v20.stackCount, v20.stack);
                    }
                    else {
                        /*SL:1351*/v-11.visitFrame(v20.mode, v20.localDiff, v20.local, v20.stackCount, v20.stack);
                    }
                }
                /*SL:1355*/if (v19 > 0) {
                    /*SL:1356*/v17 = this.readFrame(v17, v15, v16, v20);
                    /*SL:1357*/--v19;
                }
                else {
                    /*SL:1359*/v20 = null;
                }
            }
            int v25 = /*EL:1364*/b[v-9] & 0xFF;
            /*SL:1365*/switch (ClassWriter.TYPE[v25]) {
                case 0: {
                    /*SL:1367*/v-11.visitInsn(v25);
                    /*SL:1368*/++v-9;
                    /*SL:1369*/break;
                }
                case 4: {
                    /*SL:1371*/if (v25 > 54) {
                        /*SL:1372*/v25 -= 59;
                        /*SL:1373*/v-11.visitVarInsn(54 + (v25 >> 2), v25 & 0x3);
                    }
                    else {
                        /*SL:1376*/v25 -= 26;
                        /*SL:1377*/v-11.visitVarInsn(21 + (v25 >> 2), v25 & 0x3);
                    }
                    /*SL:1379*/++v-9;
                    /*SL:1380*/break;
                }
                case 9: {
                    /*SL:1382*/v-11.visitJumpInsn(v25, array[v29 + this.readShort(v-9 + 1)]);
                    /*SL:1383*/v-9 += 3;
                    /*SL:1384*/break;
                }
                case 10: {
                    /*SL:1386*/v-11.visitJumpInsn(v25 + v22, /*EL:1387*/array[v29 + this.readInt(v-9 + 1)]);
                    /*SL:1388*/v-9 += 5;
                    /*SL:1389*/break;
                }
                case 18: {
                    /*SL:1394*/v25 = ((v25 < 218) ? (v25 - 49) : (v25 - 20));
                    final Label v32 = /*EL:1395*/array[v29 + this.readUnsignedShort(v-9 + 1)];
                    /*SL:1401*/if (v25 == 167 || v25 == 168) {
                        /*SL:1402*/v-11.visitJumpInsn(v25 + 33, v32);
                    }
                    else {
                        /*SL:1404*/v25 = ((v25 <= 166) ? ((v25 + 1 ^ 0x1) - 1) : (v25 ^ 0x1));
                        final Label v27 = /*EL:1406*/new Label();
                        /*SL:1407*/v-11.visitJumpInsn(v25, v27);
                        /*SL:1408*/v-11.visitJumpInsn(200, v32);
                        /*SL:1409*/v-11.visitLabel(v27);
                        /*SL:1414*/if (v17 != 0 && (v20 == null || v20.offset != v29 + 3)) {
                            /*SL:1416*/v-11.visitFrame(256, 0, null, 0, null);
                        }
                    }
                    /*SL:1419*/v-9 += 3;
                    /*SL:1420*/break;
                }
                case 17: {
                    /*SL:1423*/v25 = (b[v-9 + 1] & 0xFF);
                    /*SL:1424*/if (v25 == 132) {
                        /*SL:1425*/v-11.visitIincInsn(this.readUnsignedShort(v-9 + 2), this.readShort(v-9 + 4));
                        /*SL:1426*/v-9 += 6;
                        break;
                    }
                    /*SL:1428*/v-11.visitVarInsn(v25, this.readUnsignedShort(v-9 + 2));
                    /*SL:1429*/v-9 += 4;
                    /*SL:1431*/break;
                }
                case 14: {
                    /*SL:1434*/v-9 = v-9 + 4 - (v29 & 0x3);
                    final int v26 = /*EL:1436*/v29 + this.readInt(v-9);
                    final int v33 = /*EL:1437*/this.readInt(v-9 + 4);
                    final int v34 = /*EL:1438*/this.readInt(v-9 + 8);
                    final Label[] v35 = /*EL:1439*/new Label[v34 - v33 + 1];
                    /*SL:1440*/v-9 += 12;
                    /*SL:1441*/for (int v36 = 0; v36 < v35.length; ++v36) {
                        /*SL:1442*/v35[v36] = array[v29 + this.readInt(v-9)];
                        /*SL:1443*/v-9 += 4;
                    }
                    /*SL:1445*/v-11.visitTableSwitchInsn(v33, v34, array[v26], v35);
                    /*SL:1446*/break;
                }
                case 15: {
                    /*SL:1450*/v-9 = v-9 + 4 - (v29 & 0x3);
                    final int v26 = /*EL:1452*/v29 + this.readInt(v-9);
                    final int v33 = /*EL:1453*/this.readInt(v-9 + 4);
                    final int[] v37 = /*EL:1454*/new int[v33];
                    final Label[] v35 = /*EL:1455*/new Label[v33];
                    /*SL:1456*/v-9 += 8;
                    /*SL:1457*/for (int v36 = 0; v36 < v33; ++v36) {
                        /*SL:1458*/v37[v36] = this.readInt(v-9);
                        /*SL:1459*/v35[v36] = array[v29 + this.readInt(v-9 + 4)];
                        /*SL:1460*/v-9 += 8;
                    }
                    /*SL:1462*/v-11.visitLookupSwitchInsn(array[v26], v37, v35);
                    /*SL:1463*/break;
                }
                case 3: {
                    /*SL:1466*/v-11.visitVarInsn(v25, b[v-9 + 1] & 0xFF);
                    /*SL:1467*/v-9 += 2;
                    /*SL:1468*/break;
                }
                case 1: {
                    /*SL:1470*/v-11.visitIntInsn(v25, b[v-9 + 1]);
                    /*SL:1471*/v-9 += 2;
                    /*SL:1472*/break;
                }
                case 2: {
                    /*SL:1474*/v-11.visitIntInsn(v25, this.readShort(v-9 + 1));
                    /*SL:1475*/v-9 += 3;
                    /*SL:1476*/break;
                }
                case 11: {
                    /*SL:1478*/v-11.visitLdcInsn(this.readConst(b[v-9 + 1] & 0xFF, buffer));
                    /*SL:1479*/v-9 += 2;
                    /*SL:1480*/break;
                }
                case 12: {
                    /*SL:1482*/v-11.visitLdcInsn(this.readConst(this.readUnsignedShort(v-9 + 1), buffer));
                    /*SL:1483*/v-9 += 3;
                    /*SL:1484*/break;
                }
                case 6:
                case 7: {
                    int v26 = /*EL:1487*/this.items[this.readUnsignedShort(v-9 + 1)];
                    final boolean v38 = /*EL:1488*/b[v26 - 1] == 11;
                    final String v39 = /*EL:1489*/this.readClass(v26, buffer);
                    /*SL:1490*/v26 = this.items[this.readUnsignedShort(v26 + 2)];
                    final String v40 = /*EL:1491*/this.readUTF8(v26, buffer);
                    final String v41 = /*EL:1492*/this.readUTF8(v26 + 2, buffer);
                    /*SL:1493*/if (v25 < 182) {
                        /*SL:1494*/v-11.visitFieldInsn(v25, v39, v40, v41);
                    }
                    else {
                        /*SL:1496*/v-11.visitMethodInsn(v25, v39, v40, v41, v38);
                    }
                    /*SL:1498*/if (v25 == 185) {
                        /*SL:1499*/v-9 += 5;
                        break;
                    }
                    /*SL:1501*/v-9 += 3;
                    /*SL:1503*/break;
                }
                case 8: {
                    int v26 = /*EL:1506*/this.items[this.readUnsignedShort(v-9 + 1)];
                    int v33 = /*EL:1507*/v-10.bootstrapMethods[this.readUnsignedShort(v26)];
                    final Handle v42 = /*EL:1508*/(Handle)this.readConst(this.readUnsignedShort(v33), buffer);
                    final int v43 = /*EL:1509*/this.readUnsignedShort(v33 + 2);
                    final Object[] v44 = /*EL:1510*/new Object[v43];
                    /*SL:1511*/v33 += 4;
                    /*SL:1512*/for (int v45 = 0; v45 < v43; ++v45) {
                        /*SL:1513*/v44[v45] = this.readConst(this.readUnsignedShort(v33), buffer);
                        /*SL:1514*/v33 += 2;
                    }
                    /*SL:1516*/v26 = this.items[this.readUnsignedShort(v26 + 2)];
                    final String v46 = /*EL:1517*/this.readUTF8(v26, buffer);
                    final String v47 = /*EL:1518*/this.readUTF8(v26 + 2, buffer);
                    /*SL:1519*/v-11.visitInvokeDynamicInsn(v46, v47, v42, v44);
                    /*SL:1520*/v-9 += 5;
                    /*SL:1521*/break;
                }
                case 5: {
                    /*SL:1524*/v-11.visitTypeInsn(v25, this.readClass(v-9 + 1, buffer));
                    /*SL:1525*/v-9 += 3;
                    /*SL:1526*/break;
                }
                case 13: {
                    /*SL:1528*/v-11.visitIincInsn(b[v-9 + 1] & 0xFF, b[v-9 + 2]);
                    /*SL:1529*/v-9 += 3;
                    /*SL:1530*/break;
                }
                default: {
                    /*SL:1533*/v-11.visitMultiANewArrayInsn(this.readClass(v-9 + 1, buffer), b[v-9 + 3] & 0xFF);
                    /*SL:1534*/v-9 += 4;
                    break;
                }
            }
            /*SL:1539*/while (v7 != null && v9 < v7.length && v11 <= v29) {
                /*SL:1540*/if (v11 == v29) {
                    final int v26 = /*EL:1541*/this.readAnnotationTarget(v-10, v7[v9]);
                    /*SL:1542*/this.readAnnotationValues(v26 + 2, buffer, true, v-11.visitInsnAnnotation(v-10.typeRef, v-10.typePath, this.readUTF8(v26, buffer), /*EL:1544*/true));
                }
                /*SL:1547*/v11 = ((++v9 >= v7.length || this.readByte(v7[v9]) < 67) ? -1 : this.readUnsignedShort(v7[v9] + 1));
            }
            /*SL:1549*/while (v8 != null && v10 < v8.length && v12 <= v29) {
                /*SL:1550*/if (v12 == v29) {
                    final int v26 = /*EL:1551*/this.readAnnotationTarget(v-10, v8[v10]);
                    /*SL:1552*/this.readAnnotationValues(v26 + 2, buffer, true, v-11.visitInsnAnnotation(v-10.typeRef, v-10.typePath, this.readUTF8(v26, buffer), /*EL:1554*/false));
                }
                /*SL:1558*/v12 = ((++v10 >= v8.length || this.readByte(v8[v10]) < 67) ? -1 : this.readUnsignedShort(v8[v10] + 1));
            }
        }
        /*SL:1561*/if (array[int1] != null) {
            /*SL:1562*/v-11.visitLabel(array[int1]);
        }
        /*SL:1566*/if ((v-10.flags & 0x2) == 0x0 && v13 != 0) {
            int[] v48 = /*EL:1567*/null;
            /*SL:1568*/if (v14 != 0) {
                /*SL:1569*/v-9 = v14 + 2;
                /*SL:1570*/v48 = new int[this.readUnsignedShort(v14) * 3];
                /*SL:1571*/for (int v24 = v48.length; v24 > 0; /*SL:1572*/v48[--v24] = v-9 + 6, /*SL:1573*/v48[--v24] = this.readUnsignedShort(v-9 + 8), /*SL:1574*/v48[--v24] = this.readUnsignedShort(v-9), /*SL:1575*/v-9 += 10) {}
            }
            /*SL:1578*/v-9 = v13 + 2;
            /*SL:1579*/for (int v24 = this.readUnsignedShort(v13); v24 > 0; --v24) {
                final int v25 = /*EL:1580*/this.readUnsignedShort(v-9);
                final int v26 = /*EL:1581*/this.readUnsignedShort(v-9 + 2);
                final int v33 = /*EL:1582*/this.readUnsignedShort(v-9 + 8);
                String v39 = /*EL:1583*/null;
                /*SL:1584*/if (v48 != null) {
                    /*SL:1585*/for (int v43 = 0; v43 < v48.length; v43 += 3) {
                        /*SL:1586*/if (v48[v43] == v25 && v48[v43 + 1] == v33) {
                            /*SL:1587*/v39 = this.readUTF8(v48[v43 + 2], buffer);
                            /*SL:1588*/break;
                        }
                    }
                }
                /*SL:1592*/v-11.visitLocalVariable(this.readUTF8(v-9 + 4, buffer), this.readUTF8(v-9 + 6, buffer), v39, array[v25], array[v25 + v26], v33);
                /*SL:1595*/v-9 += 10;
            }
        }
        /*SL:1600*/if (v7 != null) {
            /*SL:1601*/for (int v29 = 0; v29 < v7.length; ++v29) {
                /*SL:1602*/if (this.readByte(v7[v29]) >> 1 == 32) {
                    int v24 = /*EL:1603*/this.readAnnotationTarget(v-10, v7[v29]);
                    /*SL:1604*/v24 = this.readAnnotationValues(v24 + 2, buffer, true, v-11.visitLocalVariableAnnotation(v-10.typeRef, v-10.typePath, v-10.start, v-10.end, v-10.index, this.readUTF8(v24, buffer), /*EL:1607*/true));
                }
            }
        }
        /*SL:1612*/if (v8 != null) {
            /*SL:1613*/for (int v29 = 0; v29 < v8.length; ++v29) {
                /*SL:1614*/if (this.readByte(v8[v29]) >> 1 == 32) {
                    int v24 = /*EL:1615*/this.readAnnotationTarget(v-10, v8[v29]);
                    /*SL:1616*/v24 = this.readAnnotationValues(v24 + 2, buffer, true, v-11.visitLocalVariableAnnotation(v-10.typeRef, v-10.typePath, v-10.start, v-10.end, v-10.index, this.readUTF8(v24, buffer), /*EL:1619*/false));
                }
            }
        }
        /*SL:1626*/while (v21 != null) {
            final Attribute v49 = /*EL:1627*/v21.next;
            /*SL:1628*/v21.next = null;
            /*SL:1629*/v-11.visitAttribute(v21);
            /*SL:1630*/v21 = v49;
        }
        /*SL:1634*/v-11.visitMaxs(unsignedShort, unsignedShort2);
    }
    
    private int[] readTypeAnnotations(final MethodVisitor v-6, final Context v-5, int v-4, final boolean v-3) {
        final char[] buffer = /*EL:1655*/v-5.buffer;
        final int[] array = /*EL:1656*/new int[this.readUnsignedShort(v-4)];
        /*SL:1657*/v-4 += 2;
        /*SL:1658*/for (int v0 = 0; v0 < array.length; ++v0) {
            /*SL:1659*/array[v0] = v-4;
            final int v = /*EL:1660*/this.readInt(v-4);
            /*SL:1661*/switch (v >>> 24) {
                case 0:
                case 1:
                case 22: {
                    /*SL:1665*/v-4 += 2;
                    /*SL:1666*/break;
                }
                case 19:
                case 20:
                case 21: {
                    /*SL:1670*/++v-4;
                    /*SL:1671*/break;
                }
                case 64:
                case 65: {
                    /*SL:1674*/for (int a3 = this.readUnsignedShort(v-4 + 1); a3 > 0; --a3) {
                        final int a2 = /*EL:1675*/this.readUnsignedShort(v-4 + 3);
                        /*SL:1676*/a3 = this.readUnsignedShort(v-4 + 5);
                        /*SL:1677*/this.readLabel(a2, v-5.labels);
                        /*SL:1678*/this.readLabel(a2 + a3, v-5.labels);
                        /*SL:1679*/v-4 += 6;
                    }
                    /*SL:1681*/v-4 += 3;
                    /*SL:1682*/break;
                }
                case 71:
                case 72:
                case 73:
                case 74:
                case 75: {
                    /*SL:1688*/v-4 += 4;
                    /*SL:1689*/break;
                }
                default: {
                    /*SL:1700*/v-4 += 3;
                    break;
                }
            }
            final int v2 = /*EL:1703*/this.readByte(v-4);
            /*SL:1704*/if (v >>> 24 == 66) {
                final TypePath a4 = /*EL:1705*/(v2 == 0) ? null : new TypePath(this.b, v-4);
                /*SL:1706*/v-4 += 1 + 2 * v2;
                /*SL:1707*/v-4 = this.readAnnotationValues(v-4 + 2, buffer, true, v-6.visitTryCatchAnnotation(v, a4, this.readUTF8(v-4, buffer), /*EL:1709*/v-3));
            }
            else {
                /*SL:1711*/v-4 = this.readAnnotationValues(v-4 + 3 + 2 * v2, buffer, true, null);
            }
        }
        /*SL:1714*/return array;
    }
    
    private int readAnnotationTarget(final Context v-3, int v-2) {
        int int1 = /*EL:1732*/this.readInt(v-2);
        /*SL:1733*/switch (int1 >>> 24) {
            case 0:
            case 1:
            case 22: {
                /*SL:1737*/int1 &= 0xFFFF0000;
                /*SL:1738*/v-2 += 2;
                /*SL:1739*/break;
            }
            case 19:
            case 20:
            case 21: {
                /*SL:1743*/int1 &= 0xFF000000;
                /*SL:1744*/++v-2;
                /*SL:1745*/break;
            }
            case 64:
            case 65: {
                /*SL:1748*/int1 &= 0xFF000000;
                final int v0 = /*EL:1749*/this.readUnsignedShort(v-2 + 1);
                /*SL:1750*/v-3.start = new Label[v0];
                /*SL:1751*/v-3.end = new Label[v0];
                /*SL:1752*/v-3.index = new int[v0];
                /*SL:1753*/v-2 += 3;
                /*SL:1754*/for (int v = 0; v < v0; ++v) {
                    final int a1 = /*EL:1755*/this.readUnsignedShort(v-2);
                    final int a2 = /*EL:1756*/this.readUnsignedShort(v-2 + 2);
                    /*SL:1757*/v-3.start[v] = this.readLabel(a1, v-3.labels);
                    /*SL:1758*/v-3.end[v] = this.readLabel(a1 + a2, v-3.labels);
                    /*SL:1759*/v-3.index[v] = this.readUnsignedShort(v-2 + 4);
                    /*SL:1760*/v-2 += 6;
                }
                /*SL:1762*/break;
            }
            case 71:
            case 72:
            case 73:
            case 74:
            case 75: {
                /*SL:1769*/int1 &= 0xFF0000FF;
                /*SL:1770*/v-2 += 4;
                /*SL:1771*/break;
            }
            default: {
                /*SL:1782*/int1 &= ((int1 >>> 24 < 67) ? -256 : -16777216);
                /*SL:1783*/v-2 += 3;
                break;
            }
        }
        final int v0 = /*EL:1786*/this.readByte(v-2);
        /*SL:1787*/v-3.typeRef = int1;
        /*SL:1788*/v-3.typePath = ((v0 == 0) ? null : new TypePath(this.b, v-2));
        /*SL:1789*/return v-2 + 1 + 2 * v0;
    }
    
    private void readParameterAnnotations(final MethodVisitor v1, final Context v2, int v3, final boolean v4) {
        final int v5 = /*EL:1808*/this.b[v3++] & 0xFF;
        int v6;
        int v7;
        /*SL:1817*/for (v6 = Type.getArgumentTypes(v2.desc).length - v5, v7 = 0; v7 < v6; ++v7) {
            final AnnotationVisitor a1 = /*EL:1819*/v1.visitParameterAnnotation(v7, "Ljava/lang/Synthetic;", false);
            /*SL:1820*/if (a1 != null) {
                /*SL:1821*/a1.visitEnd();
            }
        }
        final char[] v8 = /*EL:1824*/v2.buffer;
        /*SL:1825*/while (v7 < v5 + v6) {
            int a2 = /*EL:1826*/this.readUnsignedShort(v3);
            /*SL:1827*/v3 += 2;
            /*SL:1828*/while (a2 > 0) {
                final AnnotationVisitor a3 = /*EL:1829*/v1.visitParameterAnnotation(v7, this.readUTF8(v3, v8), v4);
                /*SL:1830*/v3 = this.readAnnotationValues(v3 + 2, v8, true, a3);
                --a2;
            }
            ++v7;
        }
    }
    
    private int readAnnotationValues(int a1, final char[] a2, final boolean a3, final AnnotationVisitor a4) {
        int v1 = /*EL:1854*/this.readUnsignedShort(a1);
        /*SL:1855*/a1 += 2;
        /*SL:1856*/if (a3) {
            /*SL:1857*/while (v1 > 0) {
                /*SL:1858*/a1 = this.readAnnotationValue(a1 + 2, a2, this.readUTF8(a1, a2), a4);
                --v1;
            }
        }
        else {
            /*SL:1861*/while (v1 > 0) {
                /*SL:1862*/a1 = this.readAnnotationValue(a1, a2, null, a4);
                --v1;
            }
        }
        /*SL:1865*/if (a4 != null) {
            /*SL:1866*/a4.visitEnd();
        }
        /*SL:1868*/return a1;
    }
    
    private int readAnnotationValue(int v-5, final char[] v-4, final String v-3, final AnnotationVisitor v-2) {
        /*SL:1890*/if (v-2 != null) {
            Label_1209: {
                /*SL:1902*/switch (this.b[v-5++] & 0xFF) {
                    case 68:
                    case 70:
                    case 73:
                    case 74: {
                        /*SL:1907*/v-2.visit(v-3, this.readConst(this.readUnsignedShort(v-5), v-4));
                        /*SL:1908*/v-5 += 2;
                        /*SL:1909*/break;
                    }
                    case 66: {
                        /*SL:1911*/v-2.visit(v-3, (byte)this.readInt(this.items[this.readUnsignedShort(v-5)]));
                        /*SL:1912*/v-5 += 2;
                        /*SL:1913*/break;
                    }
                    case 90: {
                        /*SL:1915*/v-2.visit(v-3, (this.readInt(/*EL:1916*/this.items[this.readUnsignedShort(v-5)]) == 0) ? Boolean.FALSE : Boolean.TRUE);
                        /*SL:1918*/v-5 += 2;
                        /*SL:1919*/break;
                    }
                    case 83: {
                        /*SL:1921*/v-2.visit(v-3, (short)this.readInt(this.items[this.readUnsignedShort(v-5)]));
                        /*SL:1922*/v-5 += 2;
                        /*SL:1923*/break;
                    }
                    case 67: {
                        /*SL:1925*/v-2.visit(v-3, (char)this.readInt(this.items[this.readUnsignedShort(v-5)]));
                        /*SL:1926*/v-5 += 2;
                        /*SL:1927*/break;
                    }
                    case 115: {
                        /*SL:1929*/v-2.visit(v-3, this.readUTF8(v-5, v-4));
                        /*SL:1930*/v-5 += 2;
                        /*SL:1931*/break;
                    }
                    case 101: {
                        /*SL:1933*/v-2.visitEnum(v-3, this.readUTF8(v-5, v-4), this.readUTF8(v-5 + 2, v-4));
                        /*SL:1934*/v-5 += 4;
                        /*SL:1935*/break;
                    }
                    case 99: {
                        /*SL:1937*/v-2.visit(v-3, Type.getType(this.readUTF8(v-5, v-4)));
                        /*SL:1938*/v-5 += 2;
                        /*SL:1939*/break;
                    }
                    case 64: {
                        /*SL:1941*/v-5 = this.readAnnotationValues(v-5 + 2, v-4, true, v-2.visitAnnotation(v-3, this.readUTF8(v-5, v-4)));
                        /*SL:1943*/break;
                    }
                    case 91: {
                        final int unsignedShort = /*EL:1945*/this.readUnsignedShort(v-5);
                        /*SL:1946*/v-5 += 2;
                        /*SL:1947*/if (unsignedShort == 0) {
                            /*SL:1948*/return this.readAnnotationValues(v-5 - 2, v-4, false, v-2.visitArray(v-3));
                        }
                        /*SL:1951*/switch (this.b[v-5++] & 0xFF) {
                            case 66: {
                                byte[] a2 = /*EL:1953*/new byte[unsignedShort];
                                /*SL:1954*/for (a2 = 0; a2 < unsignedShort; ++a2) {
                                    /*SL:1955*/a2[a2] = (byte)this.readInt(this.items[this.readUnsignedShort(v-5)]);
                                    /*SL:1956*/v-5 += 3;
                                }
                                /*SL:1958*/v-2.visit(v-3, a2);
                                /*SL:1959*/--v-5;
                                break Label_1209;
                            }
                            case 90: {
                                final boolean[] a3 = /*EL:1962*/new boolean[unsignedShort];
                                /*SL:1963*/for (int a4 = 0; a4 < unsignedShort; ++a4) {
                                    /*SL:1964*/a3[a4] = (this.readInt(this.items[this.readUnsignedShort(v-5)]) != 0);
                                    /*SL:1965*/v-5 += 3;
                                }
                                /*SL:1967*/v-2.visit(v-3, a3);
                                /*SL:1968*/--v-5;
                                break Label_1209;
                            }
                            case 83: {
                                final short[] v3 = /*EL:1971*/new short[unsignedShort];
                                /*SL:1972*/for (int v2 = 0; v2 < unsignedShort; ++v2) {
                                    /*SL:1973*/v3[v2] = (short)this.readInt(this.items[this.readUnsignedShort(v-5)]);
                                    /*SL:1974*/v-5 += 3;
                                }
                                /*SL:1976*/v-2.visit(v-3, v3);
                                /*SL:1977*/--v-5;
                                break Label_1209;
                            }
                            case 67: {
                                final short[] v3 = /*EL:1980*/(short[])new char[unsignedShort];
                                /*SL:1981*/for (int v2 = 0; v2 < unsignedShort; ++v2) {
                                    /*SL:1982*/v3[v2] = (char)this.readInt(this.items[this.readUnsignedShort(v-5)]);
                                    /*SL:1983*/v-5 += 3;
                                }
                                /*SL:1985*/v-2.visit(v-3, v3);
                                /*SL:1986*/--v-5;
                                break Label_1209;
                            }
                            case 73: {
                                final int[] v4 = /*EL:1989*/new int[unsignedShort];
                                /*SL:1990*/for (int v2 = 0; v2 < unsignedShort; ++v2) {
                                    /*SL:1991*/v4[v2] = this.readInt(this.items[this.readUnsignedShort(v-5)]);
                                    /*SL:1992*/v-5 += 3;
                                }
                                /*SL:1994*/v-2.visit(v-3, v4);
                                /*SL:1995*/--v-5;
                                break Label_1209;
                            }
                            case 74: {
                                final long[] v5 = /*EL:1998*/new long[unsignedShort];
                                /*SL:1999*/for (int v2 = 0; v2 < unsignedShort; ++v2) {
                                    /*SL:2000*/v5[v2] = this.readLong(this.items[this.readUnsignedShort(v-5)]);
                                    /*SL:2001*/v-5 += 3;
                                }
                                /*SL:2003*/v-2.visit(v-3, v5);
                                /*SL:2004*/--v-5;
                                break Label_1209;
                            }
                            case 70: {
                                final float[] v6 = /*EL:2007*/new float[unsignedShort];
                                /*SL:2008*/for (int v2 = 0; v2 < unsignedShort; ++v2) {
                                    /*SL:2010*/v6[v2] = Float.intBitsToFloat(this.readInt(this.items[this.readUnsignedShort(v-5)]));
                                    /*SL:2011*/v-5 += 3;
                                }
                                /*SL:2013*/v-2.visit(v-3, v6);
                                /*SL:2014*/--v-5;
                                break Label_1209;
                            }
                            case 68: {
                                final double[] v7 = /*EL:2017*/new double[unsignedShort];
                                /*SL:2018*/for (int v2 = 0; v2 < unsignedShort; ++v2) {
                                    /*SL:2020*/v7[v2] = Double.longBitsToDouble(this.readLong(this.items[this.readUnsignedShort(v-5)]));
                                    /*SL:2021*/v-5 += 3;
                                }
                                /*SL:2023*/v-2.visit(v-3, v7);
                                /*SL:2024*/--v-5;
                                break Label_1209;
                            }
                            default: {
                                /*SL:2027*/v-5 = this.readAnnotationValues(v-5 - 3, v-4, false, v-2.visitArray(v-3));
                                break Label_1209;
                            }
                        }
                        break;
                    }
                }
            }
            /*SL:2030*/return v-5;
        }
        switch (this.b[v-5] & 0xFF) {
            case 101: {
                return v-5 + 5;
            }
            case 64: {
                return this.readAnnotationValues(v-5 + 3, v-4, true, null);
            }
            case 91: {
                return this.readAnnotationValues(v-5 + 1, v-4, false, null);
            }
            default: {
                return v-5 + 3;
            }
        }
    }
    
    private void getImplicitFrame(final Context v2) {
        final String v3 = /*EL:2041*/v2.desc;
        final Object[] v4 = /*EL:2042*/v2.local;
        int v5 = /*EL:2043*/0;
        /*SL:2044*/if ((v2.access & 0x8) == 0x0) {
            /*SL:2045*/if ("<init>".equals(v2.name)) {
                /*SL:2046*/v4[v5++] = Opcodes.UNINITIALIZED_THIS;
            }
            else {
                /*SL:2048*/v4[v5++] = this.readClass(this.header + 2, v2.buffer);
            }
        }
        int v6 = /*EL:2051*/1;
        while (true) {
            final int a1 = /*EL:2053*/v6;
            /*SL:2054*/switch (v3.charAt(v6++)) {
                case 'B':
                case 'C':
                case 'I':
                case 'S':
                case 'Z': {
                    /*SL:2060*/v4[v5++] = Opcodes.INTEGER;
                    /*SL:2061*/continue;
                }
                case 'F': {
                    /*SL:2063*/v4[v5++] = Opcodes.FLOAT;
                    /*SL:2064*/continue;
                }
                case 'J': {
                    /*SL:2066*/v4[v5++] = Opcodes.LONG;
                    /*SL:2067*/continue;
                }
                case 'D': {
                    /*SL:2069*/v4[v5++] = Opcodes.DOUBLE;
                    /*SL:2070*/continue;
                }
                case '[': {
                    /*SL:2072*/while (v3.charAt(v6) == '[') {
                        /*SL:2073*/++v6;
                    }
                    /*SL:2075*/if (v3.charAt(v6) == 'L') {
                        /*SL:2076*/++v6;
                        /*SL:2077*/while (v3.charAt(v6) != ';') {
                            /*SL:2078*/++v6;
                        }
                    }
                    /*SL:2081*/v4[v5++] = v3.substring(a1, ++v6);
                    /*SL:2082*/continue;
                }
                case 'L': {
                    /*SL:2084*/while (v3.charAt(v6) != ';') {
                        /*SL:2085*/++v6;
                    }
                    /*SL:2087*/v4[v5++] = v3.substring(a1 + 1, v6++);
                    /*SL:2088*/continue;
                }
                default: {
                    /*SL:2093*/v2.localCount = v5;
                }
            }
        }
    }
    
    private int readFrame(int v-7, final boolean v-6, final boolean v-5, final Context v-4) {
        final char[] buffer = /*EL:2112*/v-4.buffer;
        final Label[] labels = /*EL:2113*/v-4.labels;
        final int n;
        /*SL:2116*/if (v-6) {
            final int a1 = /*EL:2117*/this.b[v-7++] & 0xFF;
        }
        else {
            /*SL:2119*/n = 255;
            /*SL:2120*/v-4.offset = -1;
        }
        /*SL:2122*/v-4.localDiff = 0;
        final int v0;
        /*SL:2123*/if (n < 64) {
            final int a2 = /*EL:2124*/n;
            /*SL:2125*/v-4.mode = 3;
            /*SL:2126*/v-4.stackCount = 0;
        }
        else/*SL:2127*/ if (n < 128) {
            final int a3 = /*EL:2128*/n - 64;
            /*SL:2129*/v-7 = this.readFrameType(v-4.stack, 0, v-7, buffer, labels);
            /*SL:2130*/v-4.mode = 4;
            /*SL:2131*/v-4.stackCount = 1;
        }
        else {
            /*SL:2133*/v0 = this.readUnsignedShort(v-7);
            /*SL:2134*/v-7 += 2;
            /*SL:2135*/if (n == 247) {
                /*SL:2136*/v-7 = this.readFrameType(v-4.stack, 0, v-7, buffer, labels);
                /*SL:2137*/v-4.mode = 4;
                /*SL:2138*/v-4.stackCount = 1;
            }
            else/*SL:2139*/ if (n >= 248 && n < 251) {
                /*SL:2141*/v-4.mode = 2;
                /*SL:2142*/v-4.localDiff = 251 - n;
                /*SL:2143*/v-4.localCount -= v-4.localDiff;
                /*SL:2144*/v-4.stackCount = 0;
            }
            else/*SL:2145*/ if (n == 251) {
                /*SL:2146*/v-4.mode = 3;
                /*SL:2147*/v-4.stackCount = 0;
            }
            else/*SL:2148*/ if (n < 255) {
                int v = /*EL:2149*/v-5 ? v-4.localCount : 0;
                /*SL:2150*/for (int a4 = n - 251; a4 > 0; --a4) {
                    /*SL:2151*/v-7 = this.readFrameType(v-4.local, v++, v-7, buffer, labels);
                }
                /*SL:2154*/v-4.mode = 1;
                /*SL:2155*/v-4.localDiff = n - 251;
                /*SL:2156*/v-4.localCount += v-4.localDiff;
                /*SL:2157*/v-4.stackCount = 0;
            }
            else {
                /*SL:2159*/v-4.mode = 0;
                int v = /*EL:2160*/this.readUnsignedShort(v-7);
                /*SL:2161*/v-7 += 2;
                /*SL:2162*/v-4.localDiff = v;
                /*SL:2163*/v-4.localCount = v;
                int v2 = /*EL:2164*/0;
                while (v > 0) {
                    /*SL:2165*/v-7 = this.readFrameType(v-4.local, v2++, v-7, buffer, labels);
                    --v;
                }
                /*SL:2168*/v = this.readUnsignedShort(v-7);
                /*SL:2169*/v-7 += 2;
                /*SL:2170*/v-4.stackCount = v;
                /*SL:2171*/v2 = 0;
                while (v > 0) {
                    /*SL:2172*/v-7 = this.readFrameType(v-4.stack, v2++, v-7, buffer, labels);
                    --v;
                }
            }
        }
        /*SL:2178*/this.readLabel(v-4.offset += v0 + 1, labels);
        /*SL:2179*/return v-7;
    }
    
    private int readFrameType(final Object[] a1, final int a2, int a3, final char[] a4, final Label[] a5) {
        final int v1 = /*EL:2203*/this.b[a3++] & 0xFF;
        /*SL:2204*/switch (v1) {
            case 0: {
                /*SL:2206*/a1[a2] = Opcodes.TOP;
                /*SL:2207*/break;
            }
            case 1: {
                /*SL:2209*/a1[a2] = Opcodes.INTEGER;
                /*SL:2210*/break;
            }
            case 2: {
                /*SL:2212*/a1[a2] = Opcodes.FLOAT;
                /*SL:2213*/break;
            }
            case 3: {
                /*SL:2215*/a1[a2] = Opcodes.DOUBLE;
                /*SL:2216*/break;
            }
            case 4: {
                /*SL:2218*/a1[a2] = Opcodes.LONG;
                /*SL:2219*/break;
            }
            case 5: {
                /*SL:2221*/a1[a2] = Opcodes.NULL;
                /*SL:2222*/break;
            }
            case 6: {
                /*SL:2224*/a1[a2] = Opcodes.UNINITIALIZED_THIS;
                /*SL:2225*/break;
            }
            case 7: {
                /*SL:2227*/a1[a2] = this.readClass(a3, a4);
                /*SL:2228*/a3 += 2;
                /*SL:2229*/break;
            }
            default: {
                /*SL:2231*/a1[a2] = this.readLabel(this.readUnsignedShort(a3), a5);
                /*SL:2232*/a3 += 2;
                break;
            }
        }
        /*SL:2234*/return a3;
    }
    
    protected Label readLabel(final int a1, final Label[] a2) {
        /*SL:2251*/if (a2[a1] == null) {
            /*SL:2252*/a2[a1] = new Label();
        }
        /*SL:2254*/return a2[a1];
    }
    
    private int getAttributes() {
        int n = /*EL:2264*/this.header + 8 + this.readUnsignedShort(this.header + 6) * 2;
        /*SL:2266*/for (int v0 = this.readUnsignedShort(n); v0 > 0; --v0) {
            /*SL:2267*/for (int v = this.readUnsignedShort(n + 8); v > 0; --v) {
                /*SL:2268*/n += 6 + this.readInt(n + 12);
            }
            /*SL:2270*/n += 8;
        }
        /*SL:2272*/n += 2;
        /*SL:2273*/for (int v0 = this.readUnsignedShort(n); v0 > 0; --v0) {
            /*SL:2274*/for (int v = this.readUnsignedShort(n + 8); v > 0; --v) {
                /*SL:2275*/n += 6 + this.readInt(n + 12);
            }
            /*SL:2277*/n += 8;
        }
        /*SL:2280*/return n + 2;
    }
    
    private Attribute readAttribute(final Attribute[] a3, final String a4, final int a5, final int a6, final char[] a7, final int v1, final Label[] v2) {
        /*SL:2319*/for (int a8 = 0; a8 < a3.length; ++a8) {
            /*SL:2320*/if (a3[a8].type.equals(a4)) {
                /*SL:2321*/return a3[a8].read(this, a5, a6, a7, v1, v2);
            }
        }
        /*SL:2324*/return new Attribute(a4).read(this, a5, a6, null, -1, null);
    }
    
    public int getItemCount() {
        /*SL:2337*/return this.items.length;
    }
    
    public int getItem(final int a1) {
        /*SL:2351*/return this.items[a1];
    }
    
    public int getMaxStringLength() {
        /*SL:2362*/return this.maxStringLength;
    }
    
    public int readByte(final int a1) {
        /*SL:2375*/return this.b[a1] & 0xFF;
    }
    
    public int readUnsignedShort(final int a1) {
        final byte[] v1 = /*EL:2388*/this.b;
        /*SL:2389*/return (v1[a1] & 0xFF) << 8 | (v1[a1 + 1] & 0xFF);
    }
    
    public short readShort(final int a1) {
        final byte[] v1 = /*EL:2402*/this.b;
        /*SL:2403*/return (short)((v1[a1] & 0xFF) << 8 | (v1[a1 + 1] & 0xFF));
    }
    
    public int readInt(final int a1) {
        final byte[] v1 = /*EL:2416*/this.b;
        /*SL:2417*/return (v1[a1] & 0xFF) << 24 | (v1[a1 + 1] & 0xFF) << 16 | (v1[a1 + 2] & 0xFF) << 8 | (v1[a1 + 3] & 0xFF);
    }
    
    public long readLong(final int a1) {
        final long v1 = /*EL:2431*/this.readInt(a1);
        final long v2 = /*EL:2432*/this.readInt(a1 + 4) & 0xFFFFFFFFL;
        /*SL:2433*/return v1 << 32 | v2;
    }
    
    public String readUTF8(int a1, final char[] a2) {
        final int v1 = /*EL:2450*/this.readUnsignedShort(a1);
        /*SL:2451*/if (a1 == 0 || v1 == 0) {
            /*SL:2452*/return null;
        }
        final String v2 = /*EL:2454*/this.strings[v1];
        /*SL:2455*/if (v2 != null) {
            /*SL:2456*/return v2;
        }
        /*SL:2458*/a1 = this.items[v1];
        /*SL:2459*/return this.strings[v1] = this.readUTF(a1 + 2, this.readUnsignedShort(a1), a2);
    }
    
    private String readUTF(int a3, final int v1, final char[] v2) {
        final int v3 = /*EL:2475*/a3 + v1;
        final byte[] v4 = /*EL:2476*/this.b;
        int v5 = /*EL:2477*/0;
        int v6 = /*EL:2479*/0;
        char v7 = /*EL:2480*/'\0';
        /*SL:2481*/while (a3 < v3) {
            int a4 = /*EL:2482*/v4[a3++];
            /*SL:2483*/switch (v6) {
                case 0: {
                    /*SL:2485*/a4 &= 0xFF;
                    /*SL:2486*/if (a4 < 128) {
                        /*SL:2487*/v2[v5++] = (char)a4;
                        continue;
                    }
                    /*SL:2488*/if (a4 < 224 && a4 > 191) {
                        /*SL:2489*/v7 = (char)(a4 & 0x1F);
                        /*SL:2490*/v6 = 1;
                        continue;
                    }
                    /*SL:2492*/v7 = (char)(a4 & 0xF);
                    /*SL:2493*/v6 = 2;
                    /*SL:2495*/continue;
                }
                case 1: {
                    /*SL:2498*/v2[v5++] = (char)(v7 << 6 | (a4 & 0x3F));
                    /*SL:2499*/v6 = 0;
                    /*SL:2500*/continue;
                }
                case 2: {
                    /*SL:2503*/v7 = (char)(v7 << 6 | (a4 & 0x3F));
                    /*SL:2504*/v6 = 1;
                    continue;
                }
            }
        }
        /*SL:2508*/return new String(v2, 0, v5);
    }
    
    public String readClass(final int a1, final char[] a2) {
        /*SL:2528*/return this.readUTF8(this.items[this.readUnsignedShort(a1)], a2);
    }
    
    public Object readConst(final int v-4, final char[] v-3) {
        final int n = /*EL:2546*/this.items[v-4];
        /*SL:2547*/switch (this.b[n - 1]) {
            case 3: {
                /*SL:2549*/return this.readInt(n);
            }
            case 4: {
                /*SL:2551*/return Float.intBitsToFloat(this.readInt(n));
            }
            case 5: {
                /*SL:2553*/return this.readLong(n);
            }
            case 6: {
                /*SL:2555*/return Double.longBitsToDouble(this.readLong(n));
            }
            case 7: {
                /*SL:2557*/return Type.getObjectType(this.readUTF8(n, v-3));
            }
            case 8: {
                /*SL:2559*/return this.readUTF8(n, v-3);
            }
            case 16: {
                /*SL:2561*/return Type.getMethodType(this.readUTF8(n, v-3));
            }
            default: {
                final int a1 = /*EL:2563*/this.readByte(n);
                final int[] a2 = /*EL:2564*/this.items;
                int v1 = /*EL:2565*/a2[this.readUnsignedShort(n + 1)];
                final boolean v2 = /*EL:2566*/this.b[v1 - 1] == 11;
                final String v3 = /*EL:2567*/this.readClass(v1, v-3);
                /*SL:2568*/v1 = a2[this.readUnsignedShort(v1 + 2)];
                final String v4 = /*EL:2569*/this.readUTF8(v1, v-3);
                final String v5 = /*EL:2570*/this.readUTF8(v1 + 2, v-3);
                /*SL:2571*/return new Handle(a1, v3, v4, v5, v2);
            }
        }
    }
}

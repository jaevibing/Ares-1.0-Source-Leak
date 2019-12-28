package javassist.bytecode;

import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.ArrayList;
import java.io.DataOutputStream;
import java.util.Map;
import java.io.IOException;
import java.io.DataInputStream;

public class AttributeInfo
{
    protected ConstPool constPool;
    int name;
    byte[] info;
    
    protected AttributeInfo(final ConstPool a1, final int a2, final byte[] a3) {
        this.constPool = a1;
        this.name = a2;
        this.info = a3;
    }
    
    protected AttributeInfo(final ConstPool a1, final String a2) {
        this(a1, a2, null);
    }
    
    public AttributeInfo(final ConstPool a1, final String a2, final byte[] a3) {
        this(a1, a1.addUtf8Info(a2), a3);
    }
    
    protected AttributeInfo(final ConstPool a1, final int a2, final DataInputStream a3) throws IOException {
        this.constPool = a1;
        this.name = a2;
        final int v1 = a3.readInt();
        this.info = new byte[v1];
        if (v1 > 0) {
            a3.readFully(this.info);
        }
    }
    
    static AttributeInfo read(final ConstPool a1, final DataInputStream a2) throws IOException {
        final int v1 = /*EL:79*/a2.readUnsignedShort();
        final String v2 = /*EL:80*/a1.getUtf8Info(v1);
        final char v3 = /*EL:81*/v2.charAt(0);
        /*SL:82*/if (v3 < 'M') {
            /*SL:83*/if (v3 < 'E') {
                /*SL:84*/if (v2.equals("AnnotationDefault")) {
                    /*SL:85*/return new AnnotationDefaultAttribute(a1, v1, a2);
                }
                /*SL:86*/if (v2.equals("BootstrapMethods")) {
                    /*SL:87*/return new BootstrapMethodsAttribute(a1, v1, a2);
                }
                /*SL:88*/if (v2.equals("Code")) {
                    /*SL:89*/return new CodeAttribute(a1, v1, a2);
                }
                /*SL:90*/if (v2.equals("ConstantValue")) {
                    /*SL:91*/return new ConstantAttribute(a1, v1, a2);
                }
                /*SL:92*/if (v2.equals("Deprecated")) {
                    /*SL:93*/return new DeprecatedAttribute(a1, v1, a2);
                }
            }
            else {
                /*SL:96*/if (v2.equals("EnclosingMethod")) {
                    /*SL:97*/return new EnclosingMethodAttribute(a1, v1, a2);
                }
                /*SL:98*/if (v2.equals("Exceptions")) {
                    /*SL:99*/return new ExceptionsAttribute(a1, v1, a2);
                }
                /*SL:100*/if (v2.equals("InnerClasses")) {
                    /*SL:101*/return new InnerClassesAttribute(a1, v1, a2);
                }
                /*SL:102*/if (v2.equals("LineNumberTable")) {
                    /*SL:103*/return new LineNumberAttribute(a1, v1, a2);
                }
                /*SL:104*/if (v2.equals("LocalVariableTable")) {
                    /*SL:105*/return new LocalVariableAttribute(a1, v1, a2);
                }
                /*SL:106*/if (v2.equals("LocalVariableTypeTable")) {
                    /*SL:107*/return new LocalVariableTypeAttribute(a1, v1, a2);
                }
            }
        }
        else/*SL:111*/ if (v3 < 'S') {
            /*SL:114*/if (v2.equals("MethodParameters")) {
                /*SL:115*/return new MethodParametersAttribute(a1, v1, a2);
            }
            /*SL:116*/if (v2.equals("RuntimeVisibleAnnotations") || v2.equals("RuntimeInvisibleAnnotations")) {
                /*SL:119*/return new AnnotationsAttribute(a1, v1, a2);
            }
            /*SL:121*/if (v2.equals("RuntimeVisibleParameterAnnotations") || v2.equals("RuntimeInvisibleParameterAnnotations")) {
                /*SL:123*/return new ParameterAnnotationsAttribute(a1, v1, a2);
            }
            /*SL:124*/if (v2.equals("RuntimeVisibleTypeAnnotations") || v2.equals("RuntimeInvisibleTypeAnnotations")) {
                /*SL:126*/return new TypeAnnotationsAttribute(a1, v1, a2);
            }
        }
        else {
            /*SL:129*/if (v2.equals("Signature")) {
                /*SL:130*/return new SignatureAttribute(a1, v1, a2);
            }
            /*SL:131*/if (v2.equals("SourceFile")) {
                /*SL:132*/return new SourceFileAttribute(a1, v1, a2);
            }
            /*SL:133*/if (v2.equals("Synthetic")) {
                /*SL:134*/return new SyntheticAttribute(a1, v1, a2);
            }
            /*SL:135*/if (v2.equals("StackMap")) {
                /*SL:136*/return new StackMap(a1, v1, a2);
            }
            /*SL:137*/if (v2.equals("StackMapTable")) {
                /*SL:138*/return new StackMapTable(a1, v1, a2);
            }
        }
        /*SL:142*/return new AttributeInfo(a1, v1, a2);
    }
    
    public String getName() {
        /*SL:149*/return this.constPool.getUtf8Info(this.name);
    }
    
    public ConstPool getConstPool() {
        /*SL:155*/return this.constPool;
    }
    
    public int length() {
        /*SL:163*/return this.info.length + 6;
    }
    
    public byte[] get() {
        /*SL:173*/return this.info;
    }
    
    public void set(final byte[] a1) {
        /*SL:182*/this.info = a1;
    }
    
    public AttributeInfo copy(final ConstPool v1, final Map v2) {
        final int v3 = /*EL:193*/this.info.length;
        final byte[] v4 = /*EL:194*/this.info;
        final byte[] v5 = /*EL:195*/new byte[v3];
        /*SL:196*/for (int a1 = 0; a1 < v3; ++a1) {
            /*SL:197*/v5[a1] = v4[a1];
        }
        /*SL:199*/return new AttributeInfo(v1, this.getName(), v5);
    }
    
    void write(final DataOutputStream a1) throws IOException {
        /*SL:203*/a1.writeShort(this.name);
        /*SL:204*/a1.writeInt(this.info.length);
        /*SL:205*/if (this.info.length > 0) {
            /*SL:206*/a1.write(this.info);
        }
    }
    
    static int getLength(final ArrayList v-2) {
        int n = /*EL:210*/0;
        /*SL:212*/for (int v0 = v-2.size(), v = 0; v < v0; ++v) {
            final AttributeInfo a1 = /*EL:213*/v-2.get(v);
            /*SL:214*/n += a1.length();
        }
        /*SL:217*/return n;
    }
    
    static AttributeInfo lookup(final ArrayList a2, final String v1) {
        /*SL:221*/if (a2 == null) {
            /*SL:222*/return null;
        }
        final ListIterator v2 = /*EL:224*/a2.listIterator();
        /*SL:225*/while (v2.hasNext()) {
            final AttributeInfo a3 = /*EL:226*/v2.next();
            /*SL:227*/if (a3.getName().equals(v1)) {
                /*SL:228*/return a3;
            }
        }
        /*SL:231*/return null;
    }
    
    static synchronized AttributeInfo remove(final ArrayList a2, final String v1) {
        /*SL:235*/if (a2 == null) {
            /*SL:236*/return null;
        }
        AttributeInfo v2 = /*EL:238*/null;
        final ListIterator v3 = /*EL:239*/a2.listIterator();
        /*SL:240*/while (v3.hasNext()) {
            final AttributeInfo a3 = /*EL:241*/v3.next();
            /*SL:242*/if (a3.getName().equals(v1)) {
                /*SL:243*/v3.remove();
                /*SL:244*/v2 = a3;
            }
        }
        /*SL:248*/return v2;
    }
    
    static void writeAll(final ArrayList v1, final DataOutputStream v2) throws IOException {
        /*SL:254*/if (v1 == null) {
            /*SL:255*/return;
        }
        final int v3 = /*EL:257*/v1.size();
        /*SL:258*/for (AttributeInfo a2 = (AttributeInfo)0; a2 < v3; ++a2) {
            /*SL:259*/a2 = v1.get(a2);
            /*SL:260*/a2.write(v2);
        }
    }
    
    static ArrayList copyAll(final ArrayList v1, final ConstPool v2) {
        /*SL:265*/if (v1 == null) {
            /*SL:266*/return null;
        }
        final ArrayList v3 = /*EL:268*/new ArrayList();
        final int v4 = /*EL:269*/v1.size();
        /*SL:270*/for (AttributeInfo a2 = (AttributeInfo)0; a2 < v4; ++a2) {
            /*SL:271*/a2 = v1.get(a2);
            /*SL:272*/v3.add(a2.copy(v2, null));
        }
        /*SL:275*/return v3;
    }
    
    void renameClass(final String a1, final String a2) {
    }
    
    void renameClass(final Map a1) {
    }
    
    static void renameClass(final List a2, final String a3, final String v1) {
        /*SL:288*/for (final AttributeInfo a4 : a2) {
            /*SL:291*/a4.renameClass(a3, v1);
        }
    }
    
    static void renameClass(final List a2, final Map v1) {
        /*SL:296*/for (final AttributeInfo a3 : a2) {
            /*SL:299*/a3.renameClass(v1);
        }
    }
    
    void getRefClasses(final Map a1) {
    }
    
    static void getRefClasses(final List a2, final Map v1) {
        /*SL:306*/for (final AttributeInfo a3 : a2) {
            /*SL:309*/a3.getRefClasses(v1);
        }
    }
}

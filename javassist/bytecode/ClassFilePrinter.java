package javassist.bytecode;

import java.util.List;
import javassist.Modifier;
import java.io.OutputStream;
import java.io.PrintWriter;

public class ClassFilePrinter
{
    public static void print(final ClassFile a1) {
        print(/*EL:33*/a1, new PrintWriter(System.out, true));
    }
    
    public static void print(final ClassFile v-7, final PrintWriter v-6) {
        final int modifier = /*EL:47*/AccessFlag.toModifier(v-7.getAccessFlags() & 0xFFFFFFDF);
        /*SL:49*/v-6.println("major: " + v-7.major + ", minor: " + v-7.minor + " modifiers: " + /*EL:50*/Integer.toHexString(v-7.getAccessFlags()));
        /*SL:51*/v-6.println(Modifier.toString(modifier) + " class " + v-7.getName() + /*EL:52*/" extends " + v-7.getSuperclass());
        final String[] interfaces = /*EL:54*/v-7.getInterfaces();
        /*SL:55*/if (interfaces != null && interfaces.length > 0) {
            /*SL:56*/v-6.print("    implements ");
            /*SL:57*/v-6.print(interfaces[0]);
            /*SL:58*/for (int a1 = 1; a1 < interfaces.length; ++a1) {
                /*SL:59*/v-6.print(", " + interfaces[a1]);
            }
            /*SL:61*/v-6.println();
        }
        /*SL:64*/v-6.println();
        List list = /*EL:65*/v-7.getFields();
        /*SL:67*/for (int n = list.size(), i = 0; i < n; ++i) {
            final FieldInfo a2 = /*EL:68*/list.get(i);
            final int v1 = /*EL:69*/a2.getAccessFlags();
            /*SL:70*/v-6.println(Modifier.toString(AccessFlag.toModifier(v1)) + " " + a2.getName() + /*EL:71*/"\t" + a2.getDescriptor());
            printAttributes(/*EL:73*/a2.getAttributes(), v-6, 'f');
        }
        /*SL:76*/v-6.println();
        /*SL:77*/list = v-7.getMethods();
        /*SL:79*/for (int n = list.size(), i = 0; i < n; ++i) {
            final MethodInfo v2 = /*EL:80*/list.get(i);
            final int v1 = /*EL:81*/v2.getAccessFlags();
            /*SL:82*/v-6.println(Modifier.toString(AccessFlag.toModifier(v1)) + " " + v2.getName() + /*EL:83*/"\t" + v2.getDescriptor());
            printAttributes(/*EL:85*/v2.getAttributes(), v-6, 'm');
            /*SL:86*/v-6.println();
        }
        /*SL:89*/v-6.println();
        printAttributes(/*EL:90*/v-7.getAttributes(), v-6, 'c');
    }
    
    static void printAttributes(final List v-7, final PrintWriter v-6, final char v-5) {
        /*SL:94*/if (v-7 == null) {
            /*SL:95*/return;
        }
        /*SL:98*/for (int size = v-7.size(), i = 0; i < size; ++i) {
            final AttributeInfo attributeInfo = /*EL:99*/v-7.get(i);
            /*SL:100*/if (attributeInfo instanceof CodeAttribute) {
                final CodeAttribute a1 = /*EL:101*/(CodeAttribute)attributeInfo;
                /*SL:102*/v-6.println("attribute: " + attributeInfo.getName() + ": " + attributeInfo.getClass().getName());
                /*SL:104*/v-6.println("max stack " + a1.getMaxStack() + ", max locals " + a1.getMaxLocals() + /*EL:105*/", " + a1.getExceptionTable().size() + /*EL:106*/" catch blocks");
                /*SL:108*/v-6.println("<code attribute begin>");
                printAttributes(/*EL:109*/a1.getAttributes(), v-6, v-5);
                /*SL:110*/v-6.println("<code attribute end>");
            }
            else/*SL:112*/ if (attributeInfo instanceof AnnotationsAttribute) {
                /*SL:113*/v-6.println("annnotation: " + attributeInfo.toString());
            }
            else/*SL:115*/ if (attributeInfo instanceof ParameterAnnotationsAttribute) {
                /*SL:116*/v-6.println("parameter annnotations: " + attributeInfo.toString());
            }
            else/*SL:118*/ if (attributeInfo instanceof StackMapTable) {
                /*SL:119*/v-6.println("<stack map table begin>");
                /*SL:120*/StackMapTable.Printer.print((StackMapTable)attributeInfo, v-6);
                /*SL:121*/v-6.println("<stack map table end>");
            }
            else/*SL:123*/ if (attributeInfo instanceof StackMap) {
                /*SL:124*/v-6.println("<stack map begin>");
                /*SL:125*/((StackMap)attributeInfo).print(v-6);
                /*SL:126*/v-6.println("<stack map end>");
            }
            else/*SL:128*/ if (attributeInfo instanceof SignatureAttribute) {
                final SignatureAttribute signatureAttribute = /*EL:129*/(SignatureAttribute)attributeInfo;
                final String v0 = /*EL:130*/signatureAttribute.getSignature();
                /*SL:131*/v-6.println("signature: " + v0);
                try {
                    final String v;
                    /*SL:134*/if (v-5 == 'c') {
                        final String a2 = /*EL:135*/SignatureAttribute.toClassSignature(v0).toString();
                    }
                    else/*SL:136*/ if (v-5 == 'm') {
                        final String a3 = /*EL:137*/SignatureAttribute.toMethodSignature(v0).toString();
                    }
                    else {
                        /*SL:139*/v = SignatureAttribute.toFieldSignature(v0).toString();
                    }
                    /*SL:141*/v-6.println("           " + v);
                }
                catch (BadBytecode v2) {
                    /*SL:144*/v-6.println("           syntax error");
                }
            }
            else {
                /*SL:148*/v-6.println("attribute: " + attributeInfo.getName() + " (" + attributeInfo.get().length + /*EL:149*/" byte): " + attributeInfo.getClass().getName());
            }
        }
    }
}

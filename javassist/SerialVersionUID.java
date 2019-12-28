package javassist;

import javassist.bytecode.Descriptor;
import javassist.bytecode.ClassFile;
import java.security.NoSuchAlgorithmException;
import java.io.IOException;
import java.security.MessageDigest;
import java.util.Comparator;
import java.util.Arrays;
import java.io.OutputStream;
import java.io.DataOutputStream;
import java.io.ByteArrayOutputStream;

public class SerialVersionUID
{
    public static void setSerialVersionUID(final CtClass a1) throws CannotCompileException, NotFoundException {
        try {
            /*SL:43*/a1.getDeclaredField("serialVersionUID");
        }
        catch (NotFoundException ex) {
            /*SL:49*/if (!isSerializable(a1)) {
                /*SL:50*/return;
            }
            final CtField v1 = /*EL:53*/new CtField(CtClass.longType, "serialVersionUID", a1);
            /*SL:55*/v1.setModifiers(26);
            /*SL:57*/a1.addField(v1, calculateDefault(a1) + "L");
        }
    }
    
    private static boolean isSerializable(final CtClass a1) throws NotFoundException {
        final ClassPool v1 = /*EL:66*/a1.getClassPool();
        /*SL:67*/return a1.subtypeOf(v1.get("java.io.Serializable"));
    }
    
    public static long calculateDefault(final CtClass v-7) throws CannotCompileException {
        try {
            final ByteArrayOutputStream byteArrayOutputStream = /*EL:80*/new ByteArrayOutputStream();
            final DataOutputStream dataOutputStream = /*EL:81*/new DataOutputStream(byteArrayOutputStream);
            final ClassFile classFile = /*EL:82*/v-7.getClassFile();
            final String javaName = javaName(/*EL:85*/v-7);
            /*SL:86*/dataOutputStream.writeUTF(javaName);
            final CtMethod[] declaredMethods = /*EL:88*/v-7.getDeclaredMethods();
            int modifiers = /*EL:91*/v-7.getModifiers();
            /*SL:92*/if ((modifiers & 0x200) != 0x0) {
                /*SL:93*/if (declaredMethods.length > 0) {
                    /*SL:94*/modifiers |= 0x400;
                }
                else {
                    /*SL:96*/modifiers &= 0xFFFFFBFF;
                }
            }
            /*SL:98*/dataOutputStream.writeInt(modifiers);
            final String[] v0 = /*EL:101*/classFile.getInterfaces();
            /*SL:102*/for (int a1 = 0; a1 < v0.length; ++a1) {
                /*SL:103*/v0[a1] = javaName(v0[a1]);
            }
            /*SL:105*/Arrays.sort(v0);
            /*SL:106*/for (int v = 0; v < v0.length; ++v) {
                /*SL:107*/dataOutputStream.writeUTF(v0[v]);
            }
            final CtField[] v2 = /*EL:110*/v-7.getDeclaredFields();
            /*SL:111*/Arrays.<CtField>sort(v2, new Comparator() {
                @Override
                public int compare(final Object a1, final Object a2) {
                    final CtField v1 = /*EL:113*/(CtField)a1;
                    final CtField v2 = /*EL:114*/(CtField)a2;
                    /*SL:115*/return v1.getName().compareTo(v2.getName());
                }
            });
            /*SL:119*/for (int v3 = 0; v3 < v2.length; ++v3) {
                final CtField v4 = /*EL:120*/v2[v3];
                final int v5 = /*EL:121*/v4.getModifiers();
                /*SL:122*/if ((v5 & 0x2) == 0x0 || (v5 & 0x88) == 0x0) {
                    /*SL:124*/dataOutputStream.writeUTF(v4.getName());
                    /*SL:125*/dataOutputStream.writeInt(v5);
                    /*SL:126*/dataOutputStream.writeUTF(v4.getFieldInfo2().getDescriptor());
                }
            }
            /*SL:131*/if (classFile.getStaticInitializer() != null) {
                /*SL:132*/dataOutputStream.writeUTF("<clinit>");
                /*SL:133*/dataOutputStream.writeInt(8);
                /*SL:134*/dataOutputStream.writeUTF("()V");
            }
            final CtConstructor[] v6 = /*EL:138*/v-7.getDeclaredConstructors();
            /*SL:139*/Arrays.<CtConstructor>sort(v6, new Comparator() {
                @Override
                public int compare(final Object a1, final Object a2) {
                    final CtConstructor v1 = /*EL:141*/(CtConstructor)a1;
                    final CtConstructor v2 = /*EL:142*/(CtConstructor)a2;
                    /*SL:143*/return v1.getMethodInfo2().getDescriptor().compareTo(v2.getMethodInfo2().getDescriptor());
                }
            });
            /*SL:148*/for (int v7 = 0; v7 < v6.length; ++v7) {
                final CtConstructor v8 = /*EL:149*/v6[v7];
                final int v9 = /*EL:150*/v8.getModifiers();
                /*SL:151*/if ((v9 & 0x2) == 0x0) {
                    /*SL:152*/dataOutputStream.writeUTF("<init>");
                    /*SL:153*/dataOutputStream.writeInt(v9);
                    /*SL:154*/dataOutputStream.writeUTF(v8.getMethodInfo2().getDescriptor().replace(/*EL:155*/'/', '.'));
                }
            }
            /*SL:160*/Arrays.<CtMethod>sort(declaredMethods, new Comparator() {
                @Override
                public int compare(final Object a1, final Object a2) {
                    final CtMethod v1 = /*EL:162*/(CtMethod)a1;
                    final CtMethod v2 = /*EL:163*/(CtMethod)a2;
                    int v3 = /*EL:164*/v1.getName().compareTo(v2.getName());
                    /*SL:165*/if (v3 == 0) {
                        /*SL:167*/v3 = v1.getMethodInfo2().getDescriptor().compareTo(v2.getMethodInfo2().getDescriptor());
                    }
                    /*SL:169*/return v3;
                }
            });
            /*SL:173*/for (int v7 = 0; v7 < declaredMethods.length; ++v7) {
                final CtMethod v10 = /*EL:174*/declaredMethods[v7];
                final int v9 = /*EL:175*/v10.getModifiers() & 0xD3F;
                /*SL:180*/if ((v9 & 0x2) == 0x0) {
                    /*SL:181*/dataOutputStream.writeUTF(v10.getName());
                    /*SL:182*/dataOutputStream.writeInt(v9);
                    /*SL:183*/dataOutputStream.writeUTF(v10.getMethodInfo2().getDescriptor().replace(/*EL:184*/'/', '.'));
                }
            }
            /*SL:189*/dataOutputStream.flush();
            final MessageDigest v11 = /*EL:190*/MessageDigest.getInstance("SHA");
            final byte[] v12 = /*EL:191*/v11.digest(byteArrayOutputStream.toByteArray());
            long v13 = /*EL:192*/0L;
            /*SL:193*/for (int v14 = Math.min(v12.length, 8) - 1; v14 >= 0; --v14) {
                /*SL:194*/v13 = (v13 << 8 | (v12[v14] & 0xFF));
            }
            /*SL:196*/return v13;
        }
        catch (IOException a2) {
            /*SL:199*/throw new CannotCompileException(a2);
        }
        catch (NoSuchAlgorithmException a3) {
            /*SL:202*/throw new CannotCompileException(a3);
        }
    }
    
    private static String javaName(final CtClass a1) {
        /*SL:207*/return Descriptor.toJavaName(Descriptor.toJvmName(a1));
    }
    
    private static String javaName(final String a1) {
        /*SL:211*/return Descriptor.toJavaName(Descriptor.toJvmName(a1));
    }
}

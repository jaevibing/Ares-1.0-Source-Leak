package org.reflections.adapters;

import javassist.bytecode.Descriptor;
import java.util.Arrays;
import com.google.common.base.Joiner;
import javassist.bytecode.AccessFlag;
import org.reflections.util.Utils;
import java.io.IOException;
import org.reflections.ReflectionsException;
import java.io.InputStream;
import java.io.DataInputStream;
import java.io.BufferedInputStream;
import org.reflections.vfs.Vfs;
import javassist.bytecode.annotation.Annotation;
import java.util.Iterator;
import java.util.Collection;
import javassist.bytecode.ParameterAnnotationsAttribute;
import com.google.common.collect.Lists;
import javassist.bytecode.AnnotationsAttribute;
import java.util.List;
import javassist.bytecode.MethodInfo;
import javassist.bytecode.FieldInfo;
import javassist.bytecode.ClassFile;

public class JavassistAdapter implements MetadataAdapter<ClassFile, FieldInfo, MethodInfo>
{
    public static boolean includeInvisibleTag;
    
    @Override
    public List<FieldInfo> getFields(final ClassFile a1) {
        /*SL:31*/return (List<FieldInfo>)a1.getFields();
    }
    
    @Override
    public List<MethodInfo> getMethods(final ClassFile a1) {
        /*SL:36*/return (List<MethodInfo>)a1.getMethods();
    }
    
    @Override
    public String getMethodName(final MethodInfo a1) {
        /*SL:40*/return a1.getName();
    }
    
    @Override
    public List<String> getParameterNames(final MethodInfo a1) {
        String v1 = /*EL:44*/a1.getDescriptor();
        /*SL:45*/v1 = v1.substring(v1.indexOf("(") + 1, v1.lastIndexOf(")"));
        /*SL:46*/return this.splitDescriptorToTypeNames(v1);
    }
    
    @Override
    public List<String> getClassAnnotationNames(final ClassFile a1) {
        /*SL:50*/return this.getAnnotationNames((AnnotationsAttribute)a1.getAttribute("RuntimeVisibleAnnotations"), JavassistAdapter.includeInvisibleTag ? ((AnnotationsAttribute)a1.getAttribute("RuntimeInvisibleAnnotations")) : /*EL:51*/null);
    }
    
    @Override
    public List<String> getFieldAnnotationNames(final FieldInfo a1) {
        /*SL:55*/return this.getAnnotationNames((AnnotationsAttribute)a1.getAttribute("RuntimeVisibleAnnotations"), JavassistAdapter.includeInvisibleTag ? ((AnnotationsAttribute)a1.getAttribute("RuntimeInvisibleAnnotations")) : /*EL:56*/null);
    }
    
    @Override
    public List<String> getMethodAnnotationNames(final MethodInfo a1) {
        /*SL:60*/return this.getAnnotationNames((AnnotationsAttribute)a1.getAttribute("RuntimeVisibleAnnotations"), JavassistAdapter.includeInvisibleTag ? ((AnnotationsAttribute)a1.getAttribute("RuntimeInvisibleAnnotations")) : /*EL:61*/null);
    }
    
    @Override
    public List<String> getParameterAnnotationNames(final MethodInfo v-4, final int v-3) {
        final List<String> arrayList = /*EL:65*/(List<String>)Lists.<Object>newArrayList();
        final List<ParameterAnnotationsAttribute> arrayList2 = /*EL:67*/Lists.<ParameterAnnotationsAttribute>newArrayList((ParameterAnnotationsAttribute)v-4.getAttribute("RuntimeVisibleParameterAnnotations"), (ParameterAnnotationsAttribute)v-4.getAttribute("RuntimeInvisibleParameterAnnotations"));
        /*SL:70*/if (arrayList2 != null) {
            /*SL:71*/for (final ParameterAnnotationsAttribute v1 : arrayList2) {
                /*SL:72*/if (v1 != null) {
                    Annotation[][] a2 = /*EL:73*/v1.getAnnotations();
                    /*SL:74*/if (v-3 >= a2.length) {
                        continue;
                    }
                    /*SL:75*/a2 = a2[v-3];
                    /*SL:76*/arrayList.addAll(this.getAnnotationNames(a2));
                }
            }
        }
        /*SL:82*/return arrayList;
    }
    
    @Override
    public String getReturnTypeName(final MethodInfo a1) {
        String v1 = /*EL:86*/a1.getDescriptor();
        /*SL:87*/v1 = v1.substring(v1.lastIndexOf(")") + 1);
        /*SL:88*/return this.splitDescriptorToTypeNames(v1).get(0);
    }
    
    @Override
    public String getFieldName(final FieldInfo a1) {
        /*SL:92*/return a1.getName();
    }
    
    @Override
    public ClassFile getOfCreateClassObject(final Vfs.File v-1) {
        InputStream v0 = /*EL:96*/null;
        try {
            /*SL:98*/v0 = v-1.openInputStream();
            final DataInputStream a1 = /*EL:99*/new DataInputStream(new BufferedInputStream(v0));
            /*SL:100*/return new ClassFile(a1);
        }
        catch (IOException v) {
            /*SL:102*/throw new ReflectionsException("could not create class file from " + v-1.getName(), v);
        }
        finally {
            /*SL:104*/Utils.close(v0);
        }
    }
    
    @Override
    public String getMethodModifier(final MethodInfo a1) {
        final int v1 = /*EL:109*/a1.getAccessFlags();
        /*SL:110*/return AccessFlag.isPrivate(v1) ? "private" : /*EL:111*/(AccessFlag.isProtected(v1) ? "protected" : (this.isPublic(v1) ? /*EL:112*/"public" : ""));
    }
    
    @Override
    public String getMethodKey(final ClassFile a1, final MethodInfo a2) {
        /*SL:116*/return this.getMethodName(a2) + "(" + Joiner.on(", ").join(this.getParameterNames(a2)) + ")";
    }
    
    @Override
    public String getMethodFullKey(final ClassFile a1, final MethodInfo a2) {
        /*SL:120*/return this.getClassName(a1) + "." + this.getMethodKey(a1, a2);
    }
    
    @Override
    public boolean isPublic(final Object a1) {
        final Integer v1 = /*EL:124*/(a1 instanceof ClassFile) ? ((ClassFile)a1).getAccessFlags() : /*EL:125*/((a1 instanceof FieldInfo) ? ((FieldInfo)a1).getAccessFlags() : /*EL:126*/((a1 instanceof MethodInfo) ? ((MethodInfo)a1).getAccessFlags() : /*EL:127*/null));
        /*SL:129*/return v1 != null && AccessFlag.isPublic(v1);
    }
    
    @Override
    public String getClassName(final ClassFile a1) {
        /*SL:134*/return a1.getName();
    }
    
    @Override
    public String getSuperclassName(final ClassFile a1) {
        /*SL:138*/return a1.getSuperclass();
    }
    
    @Override
    public List<String> getInterfacesNames(final ClassFile a1) {
        /*SL:142*/return Arrays.<String>asList(a1.getInterfaces());
    }
    
    @Override
    public boolean acceptsInput(final String a1) {
        /*SL:146*/return a1.endsWith(".class");
    }
    
    private List<String> getAnnotationNames(final AnnotationsAttribute... v-4) {
        final List<String> arrayList = /*EL:151*/(List<String>)Lists.<Object>newArrayList();
        /*SL:153*/if (v-4 != null) {
            /*SL:154*/for (final AnnotationsAttribute v1 : v-4) {
                /*SL:155*/if (v1 != null) {
                    /*SL:156*/for (final Annotation a1 : v1.getAnnotations()) {
                        /*SL:157*/arrayList.add(a1.getTypeName());
                    }
                }
            }
        }
        /*SL:163*/return arrayList;
    }
    
    private List<String> getAnnotationNames(final Annotation[] v2) {
        final List<String> v3 = /*EL:167*/(List<String>)Lists.<Object>newArrayList();
        /*SL:169*/for (final Annotation a1 : v2) {
            /*SL:170*/v3.add(a1.getTypeName());
        }
        /*SL:173*/return v3;
    }
    
    private List<String> splitDescriptorToTypeNames(final String v-3) {
        final List<String> arrayList = /*EL:177*/(List<String>)Lists.<Object>newArrayList();
        /*SL:179*/if (v-3 != null && v-3.length() != 0) {
            final List<Integer> arrayList2 = /*EL:181*/(List<Integer>)Lists.<Object>newArrayList();
            final Descriptor.Iterator v0 = /*EL:182*/new Descriptor.Iterator(v-3);
            /*SL:183*/while (v0.hasNext()) {
                /*SL:184*/arrayList2.add(v0.next());
            }
            /*SL:186*/arrayList2.add(v-3.length());
            /*SL:188*/for (int v = 0; v < arrayList2.size() - 1; ++v) {
                final String a1 = /*EL:189*/Descriptor.toString(v-3.substring(arrayList2.get(v), arrayList2.get(v + 1)));
                /*SL:190*/arrayList.add(a1);
            }
        }
        /*SL:195*/return arrayList;
    }
    
    static {
        JavassistAdapter.includeInvisibleTag = true;
    }
}

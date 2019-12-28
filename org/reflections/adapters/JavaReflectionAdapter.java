package org.reflections.adapters;

import org.reflections.util.Utils;
import java.util.ArrayList;
import com.google.common.base.Joiner;
import java.lang.reflect.Modifier;
import org.reflections.ReflectionUtils;
import javax.annotation.Nullable;
import org.reflections.vfs.Vfs;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.util.Collection;
import java.lang.reflect.Method;
import java.util.Arrays;
import com.google.common.collect.Lists;
import java.util.List;
import java.lang.reflect.Member;
import java.lang.reflect.Field;

public class JavaReflectionAdapter implements MetadataAdapter<Class, Field, Member>
{
    @Override
    public List<Field> getFields(final Class a1) {
        /*SL:21*/return Lists.<Field>newArrayList(a1.getDeclaredFields());
    }
    
    @Override
    public List<Member> getMethods(final Class a1) {
        final List<Member> v1 = /*EL:25*/(List<Member>)Lists.<Object>newArrayList();
        /*SL:26*/v1.addAll(Arrays.<Method>asList(a1.getDeclaredMethods()));
        /*SL:27*/v1.addAll(Arrays.<Constructor>asList(a1.getDeclaredConstructors()));
        /*SL:28*/return v1;
    }
    
    @Override
    public String getMethodName(final Member a1) {
        /*SL:32*/return (a1 instanceof Method) ? a1.getName() : ((a1 instanceof Constructor) ? "<init>" : null);
    }
    
    @Override
    public List<String> getParameterNames(final Member v-5) {
        final List<String> arrayList = /*EL:37*/(List<String>)Lists.<Object>newArrayList();
        final Class<?>[] array = /*EL:39*/(v-5 instanceof Method) ? ((Method)v-5).getParameterTypes() : ((v-5 instanceof Constructor) ? ((Constructor)v-5).getParameterTypes() : /*EL:40*/null);
        /*SL:42*/if (array != null) {
            /*SL:43*/for (final Class<?> v1 : array) {
                final String a1 = getName(/*EL:44*/v1);
                /*SL:45*/arrayList.add(a1);
            }
        }
        /*SL:49*/return arrayList;
    }
    
    @Override
    public List<String> getClassAnnotationNames(final Class a1) {
        /*SL:53*/return this.getAnnotationNames(a1.getDeclaredAnnotations());
    }
    
    @Override
    public List<String> getFieldAnnotationNames(final Field a1) {
        /*SL:57*/return this.getAnnotationNames(a1.getDeclaredAnnotations());
    }
    
    @Override
    public List<String> getMethodAnnotationNames(final Member a1) {
        final Annotation[] v1 = /*EL:61*/(a1 instanceof Method) ? ((Method)a1).getDeclaredAnnotations() : /*EL:62*/((a1 instanceof Constructor) ? ((Constructor)a1).getDeclaredAnnotations() : /*EL:63*/null);
        /*SL:64*/return this.getAnnotationNames(v1);
    }
    
    @Override
    public List<String> getParameterAnnotationNames(final Member a1, final int a2) {
        final Annotation[][] v1 = /*EL:68*/(a1 instanceof Method) ? ((Method)a1).getParameterAnnotations() : /*EL:69*/((a1 instanceof Constructor) ? ((Constructor)a1).getParameterAnnotations() : /*EL:70*/null);
        /*SL:72*/return this.getAnnotationNames((Annotation[])((v1 != null) ? v1[a2] : null));
    }
    
    @Override
    public String getReturnTypeName(final Member a1) {
        /*SL:76*/return ((Method)a1).getReturnType().getName();
    }
    
    @Override
    public String getFieldName(final Field a1) {
        /*SL:80*/return a1.getName();
    }
    
    @Override
    public Class getOfCreateClassObject(final Vfs.File a1) throws Exception {
        /*SL:84*/return this.getOfCreateClassObject(a1, (ClassLoader[])null);
    }
    
    public Class getOfCreateClassObject(final Vfs.File a1, @Nullable final ClassLoader... a2) throws Exception {
        final String v1 = /*EL:88*/a1.getRelativePath().replace("/", ".").replace(".class", "");
        /*SL:89*/return ReflectionUtils.forName(v1, a2);
    }
    
    @Override
    public String getMethodModifier(final Member a1) {
        /*SL:93*/return Modifier.toString(a1.getModifiers());
    }
    
    @Override
    public String getMethodKey(final Class a1, final Member a2) {
        /*SL:97*/return this.getMethodName(a2) + "(" + Joiner.on(", ").join(this.getParameterNames(a2)) + ")";
    }
    
    @Override
    public String getMethodFullKey(final Class a1, final Member a2) {
        /*SL:101*/return this.getClassName(a1) + "." + this.getMethodKey(a1, a2);
    }
    
    @Override
    public boolean isPublic(final Object a1) {
        final Integer v1 = /*EL:105*/(a1 instanceof Class) ? ((Class)a1).getModifiers() : /*EL:106*/((a1 instanceof Member) ? ((Member)a1).getModifiers() : /*EL:107*/null);
        /*SL:109*/return v1 != null && Modifier.isPublic(v1);
    }
    
    @Override
    public String getClassName(final Class a1) {
        /*SL:113*/return a1.getName();
    }
    
    @Override
    public String getSuperclassName(final Class a1) {
        final Class v1 = /*EL:117*/a1.getSuperclass();
        /*SL:118*/return (v1 != null) ? v1.getName() : "";
    }
    
    @Override
    public List<String> getInterfacesNames(final Class v2) {
        final Class[] v3 = /*EL:122*/v2.getInterfaces();
        final List<String> v4 = /*EL:123*/new ArrayList<String>((v3 != null) ? v3.length : 0);
        /*SL:124*/if (v3 != null) {
            for (final Class a1 : v3) {
                v4.add(a1.getName());
            }
        }
        /*SL:125*/return v4;
    }
    
    @Override
    public boolean acceptsInput(final String a1) {
        /*SL:129*/return a1.endsWith(".class");
    }
    
    private List<String> getAnnotationNames(final Annotation[] v2) {
        final List<String> v3 = /*EL:134*/new ArrayList<String>(v2.length);
        /*SL:135*/for (final Annotation a1 : v2) {
            /*SL:136*/v3.add(a1.annotationType().getName());
        }
        /*SL:138*/return v3;
    }
    
    public static String getName(final Class v-1) {
        /*SL:142*/if (v-1.isArray()) {
            try {
                Class a1 = /*EL:144*/v-1;
                int v1 = /*EL:145*/0;
                while (a1.isArray()) {
                    ++v1;
                    a1 = a1.getComponentType();
                }
                /*SL:146*/return a1.getName() + Utils.repeat("[]", v1);
            }
            catch (Throwable t) {}
        }
        /*SL:151*/return v-1.getName();
    }
}

package org.reflections.serializers;

import java.lang.reflect.Method;
import org.reflections.ReflectionUtils;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import org.reflections.ReflectionsException;
import java.util.LinkedList;
import com.google.common.base.Joiner;
import com.google.common.collect.Multimap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import com.google.common.collect.Multimaps;
import com.google.common.collect.Sets;
import java.util.Set;
import com.google.common.base.Supplier;
import java.util.Collection;
import java.util.HashMap;
import java.util.Collections;
import com.google.common.collect.Lists;
import org.reflections.scanners.TypeElementsScanner;
import java.io.IOException;
import com.google.common.io.Files;
import java.nio.charset.Charset;
import java.util.Date;
import org.reflections.util.Utils;
import java.io.File;
import org.reflections.Reflections;
import java.io.InputStream;

public class JavaCodeSerializer implements Serializer
{
    private static final String pathSeparator = "_";
    private static final String doubleSeparator = "__";
    private static final String dotSeparator = ".";
    private static final String arrayDescriptor = "$$";
    private static final String tokenSeparator = "_";
    
    @Override
    public Reflections read(final InputStream a1) {
        /*SL:76*/throw new UnsupportedOperationException("read is not implemented on JavaCodeSerializer");
    }
    
    @Override
    public File save(final Reflections v-6, String v-5) {
        /*SL:85*/if (v-5.endsWith("/")) {
            /*SL:86*/v-5 = v-5.substring(0, v-5.length() - 1);
        }
        final String concat = /*EL:90*/v-5.replace('.', '/').concat(".java");
        final File prepareFile = /*EL:91*/Utils.prepareFile(concat);
        final int v0 = /*EL:96*/v-5.lastIndexOf(46);
        final String substring;
        final String substring2;
        /*SL:97*/if (v0 == -1) {
            final String a1 = /*EL:98*/"";
            final String a2 = /*EL:99*/v-5.substring(v-5.lastIndexOf(47) + 1);
        }
        else {
            /*SL:101*/substring = v-5.substring(v-5.lastIndexOf(47) + 1, v0);
            /*SL:102*/substring2 = v-5.substring(v0 + 1);
        }
        try {
            final StringBuilder v = /*EL:107*/new StringBuilder();
            /*SL:108*/v.append("//generated using Reflections JavaCodeSerializer").append(" [").append(/*EL:109*/new Date()).append("]").append("\n");
            /*SL:111*/if (substring.length() != 0) {
                /*SL:112*/v.append("package ").append(substring).append(";\n");
                /*SL:113*/v.append("\n");
            }
            /*SL:115*/v.append("public interface ").append(substring2).append(" {\n\n");
            /*SL:116*/v.append(this.toString(v-6));
            /*SL:117*/v.append("}\n");
            /*SL:119*/Files.write(v.toString(), new File(concat), Charset.defaultCharset());
        }
        catch (IOException v2) {
            /*SL:122*/throw new RuntimeException();
        }
        /*SL:125*/return prepareFile;
    }
    
    @Override
    public String toString(final Reflections v-8) {
        /*SL:129*/if (v-8.getStore().get(TypeElementsScanner.class.getSimpleName()).isEmpty() && Reflections.log != /*EL:130*/null) {
            Reflections.log.warn("JavaCodeSerializer needs TypeElementsScanner configured");
        }
        final StringBuilder sb = /*EL:133*/new StringBuilder();
        List<String> arrayList = /*EL:135*/(List<String>)Lists.<Object>newArrayList();
        int v14 = /*EL:136*/1;
        final List<String> arrayList2 = /*EL:138*/(List<String>)Lists.<Object>newArrayList((Iterable<?>)v-8.getStore().get(TypeElementsScanner.class.getSimpleName()).keySet());
        /*SL:139*/Collections.<String>sort(arrayList2);
        /*SL:140*/for (final String s : arrayList2) {
            List<String> arrayList3;
            int v0;
            /*SL:145*/for (arrayList3 = Lists.<String>newArrayList(s.split("\\.")), v0 = 0; v0 < Math.min(arrayList3.size(), arrayList.size()) && arrayList3.get(v0).equals(arrayList.get(v0)); /*SL:146*/++v0) {}
            /*SL:150*/for (int a1 = arrayList.size(); a1 > v0; --a1) {
                /*SL:151*/sb.append(Utils.repeat("\t", --v14)).append("}\n");
            }
            /*SL:155*/for (int v = v0; v < arrayList3.size() - 1; ++v) {
                /*SL:156*/sb.append(Utils.repeat("\t", v14++)).append("public interface ").append(this.getNonDuplicateName(arrayList3.get(v), arrayList3, v)).append(" {\n");
            }
            final String v2 = /*EL:160*/arrayList3.get(arrayList3.size() - 1);
            final List<String> v3 = /*EL:163*/(List<String>)Lists.<Object>newArrayList();
            final List<String> v4 = /*EL:164*/(List<String>)Lists.<Object>newArrayList();
            final Multimap<String, String> v5 = /*EL:165*/(Multimap<String, String>)Multimaps.<Object, Object>newSetMultimap(new HashMap<Object, Collection<Object>>(), (Supplier<? extends Set<Object>>)new Supplier<Set<String>>() {
                @Override
                public Set<String> get() {
                    /*SL:167*/return (Set<String>)Sets.<Object>newHashSet();
                }
            });
            /*SL:171*/for (final String v6 : v-8.getStore().get(TypeElementsScanner.class.getSimpleName(), s)) {
                /*SL:172*/if (v6.startsWith("@")) {
                    /*SL:173*/v3.add(v6.substring(1));
                }
                else/*SL:174*/ if (v6.contains("(")) {
                    /*SL:176*/if (v6.startsWith("<")) {
                        continue;
                    }
                    final int v7 = /*EL:177*/v6.indexOf(40);
                    final String v8 = /*EL:178*/v6.substring(0, v7);
                    final String v9 = /*EL:179*/v6.substring(v7 + 1, v6.indexOf(")"));
                    String v10 = /*EL:181*/"";
                    /*SL:182*/if (v9.length() != 0) {
                        /*SL:183*/v10 = "_" + v9.replace(".", "_").replace(", ", "__").replace("[]", "$$");
                    }
                    final String v11 = /*EL:185*/v8 + v10;
                    /*SL:186*/v5.put(v8, v11);
                }
                else {
                    /*SL:188*/if (Utils.isEmpty(v6)) {
                        continue;
                    }
                    /*SL:190*/v4.add(v6);
                }
            }
            /*SL:195*/sb.append(Utils.repeat("\t", v14++)).append("public interface ").append(this.getNonDuplicateName(v2, arrayList3, arrayList3.size() - 1)).append(" {\n");
            /*SL:198*/if (!v4.isEmpty()) {
                /*SL:199*/sb.append(Utils.repeat("\t", v14++)).append("public interface fields {\n");
                /*SL:200*/for (final String v6 : v4) {
                    /*SL:201*/sb.append(Utils.repeat("\t", v14)).append("public interface ").append(this.getNonDuplicateName(v6, arrayList3)).append(" {}\n");
                }
                /*SL:203*/sb.append(Utils.repeat("\t", --v14)).append("}\n");
            }
            /*SL:207*/if (!v5.isEmpty()) {
                /*SL:208*/sb.append(Utils.repeat("\t", v14++)).append("public interface methods {\n");
                /*SL:209*/for (final Map.Entry<String, String> v12 : v5.entries()) {
                    final String v13 = /*EL:210*/v12.getKey();
                    final String v8 = /*EL:211*/v12.getValue();
                    String v9 = /*EL:213*/(v5.get(v13).size() == 1) ? v13 : v8;
                    /*SL:215*/v9 = this.getNonDuplicateName(v9, v4);
                    /*SL:217*/sb.append(Utils.repeat("\t", v14)).append("public interface ").append(this.getNonDuplicateName(v9, arrayList3)).append(" {}\n");
                }
                /*SL:219*/sb.append(Utils.repeat("\t", --v14)).append("}\n");
            }
            /*SL:223*/if (!v3.isEmpty()) {
                /*SL:224*/sb.append(Utils.repeat("\t", v14++)).append("public interface annotations {\n");
                /*SL:225*/for (String v13 : v3) {
                    final String v6 = /*EL:226*/v13;
                    /*SL:227*/v13 = this.getNonDuplicateName(v13, arrayList3);
                    /*SL:228*/sb.append(Utils.repeat("\t", v14)).append("public interface ").append(v13).append(" {}\n");
                }
                /*SL:230*/sb.append(Utils.repeat("\t", --v14)).append("}\n");
            }
            /*SL:233*/arrayList = arrayList3;
        }
        /*SL:238*/for (int i = arrayList.size(); i >= 1; --i) {
            /*SL:239*/sb.append(Utils.repeat("\t", i)).append("}\n");
        }
        /*SL:242*/return sb.toString();
    }
    
    private String getNonDuplicateName(final String a3, final List<String> v1, final int v2) {
        final String v3 = /*EL:246*/this.normalize(a3);
        /*SL:247*/for (int a4 = 0; a4 < v2; ++a4) {
            /*SL:248*/if (v3.equals(v1.get(a4))) {
                /*SL:249*/return this.getNonDuplicateName(v3 + "_", v1, v2);
            }
        }
        /*SL:253*/return v3;
    }
    
    private String normalize(final String a1) {
        /*SL:257*/return a1.replace(".", "_");
    }
    
    private String getNonDuplicateName(final String a1, final List<String> a2) {
        /*SL:261*/return this.getNonDuplicateName(a1, a2, a2.size());
    }
    
    public static Class<?> resolveClassOf(final Class a1) throws ClassNotFoundException {
        Class<?> v1 = /*EL:266*/(Class<?>)a1;
        final LinkedList<String> v2 = /*EL:267*/Lists.<String>newLinkedList();
        /*SL:269*/while (v1 != null) {
            /*SL:270*/v2.addFirst(v1.getSimpleName());
            /*SL:271*/v1 = v1.getDeclaringClass();
        }
        final String v3 = /*EL:274*/Joiner.on(".").join(v2.subList(1, v2.size())).replace(".$", "$");
        /*SL:275*/return Class.forName(v3);
    }
    
    public static Class<?> resolveClass(final Class v1) {
        try {
            /*SL:280*/return resolveClassOf(v1);
        }
        catch (Exception a1) {
            /*SL:282*/throw new ReflectionsException("could not resolve to class " + v1.getName(), a1);
        }
    }
    
    public static Field resolveField(final Class v-1) {
        try {
            final String a1 = /*EL:288*/v-1.getSimpleName();
            final Class<?> v1 = /*EL:289*/(Class<?>)v-1.getDeclaringClass().getDeclaringClass();
            /*SL:290*/return resolveClassOf(v1).getDeclaredField(a1);
        }
        catch (Exception v2) {
            /*SL:292*/throw new ReflectionsException("could not resolve to field " + v-1.getName(), v2);
        }
    }
    
    public static Annotation resolveAnnotation(final Class v-1) {
        try {
            final String a1 = /*EL:298*/v-1.getSimpleName().replace("_", ".");
            final Class<?> v1 = /*EL:299*/(Class<?>)v-1.getDeclaringClass().getDeclaringClass();
            final Class<?> v2 = resolveClassOf(/*EL:300*/v1);
            final Class<? extends Annotation> v3 = /*EL:301*/(Class<? extends Annotation>)ReflectionUtils.forName(a1, new ClassLoader[0]);
            final Annotation v4 = /*EL:302*/v2.<Annotation>getAnnotation(v3);
            /*SL:303*/return v4;
        }
        catch (Exception v5) {
            /*SL:305*/throw new ReflectionsException("could not resolve to annotation " + v-1.getName(), v5);
        }
    }
    
    public static Method resolveMethod(final Class v-4) {
        final String simpleName = /*EL:310*/v-4.getSimpleName();
        try {
            String substring;
            Class<?>[] array;
            /*SL:315*/if (simpleName.contains("_")) {
                /*SL:316*/substring = simpleName.substring(0, simpleName.indexOf("_"));
                final String[] v0 = /*EL:317*/simpleName.substring(simpleName.indexOf("_") + 1).split("__");
                /*SL:318*/array = (Class<?>[])new Class[v0.length];
                /*SL:319*/for (int v = 0; v < v0.length; ++v) {
                    final String a1 = /*EL:320*/v0[v].replace("$$", "[]").replace("_", ".");
                    /*SL:321*/array[v] = ReflectionUtils.forName(a1, new ClassLoader[0]);
                }
            }
            else {
                /*SL:324*/substring = simpleName;
                /*SL:325*/array = null;
            }
            final Class<?> v2 = /*EL:328*/(Class<?>)v-4.getDeclaringClass().getDeclaringClass();
            /*SL:329*/return resolveClassOf(v2).getDeclaredMethod(substring, array);
        }
        catch (Exception a2) {
            /*SL:331*/throw new ReflectionsException("could not resolve to method " + v-4.getName(), a2);
        }
    }
}

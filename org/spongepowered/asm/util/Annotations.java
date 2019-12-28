package org.spongepowered.asm.util;

import java.util.ListIterator;
import java.util.Collections;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.base.Function;
import java.util.Iterator;
import org.spongepowered.asm.lib.tree.ClassNode;
import java.util.ArrayList;
import java.util.List;
import org.spongepowered.asm.lib.tree.MethodNode;
import org.spongepowered.asm.lib.tree.AnnotationNode;
import org.spongepowered.asm.lib.Type;
import java.lang.annotation.Annotation;
import org.spongepowered.asm.lib.tree.FieldNode;

public final class Annotations
{
    public static void setVisible(final FieldNode a1, final Class<? extends Annotation> a2, final Object... a3) {
        final AnnotationNode v1 = createNode(/*EL:61*/Type.getDescriptor(a2), a3);
        /*SL:62*/a1.visibleAnnotations = add(a1.visibleAnnotations, v1);
    }
    
    public static void setInvisible(final FieldNode a1, final Class<? extends Annotation> a2, final Object... a3) {
        final AnnotationNode v1 = createNode(/*EL:74*/Type.getDescriptor(a2), a3);
        /*SL:75*/a1.invisibleAnnotations = add(a1.invisibleAnnotations, v1);
    }
    
    public static void setVisible(final MethodNode a1, final Class<? extends Annotation> a2, final Object... a3) {
        final AnnotationNode v1 = createNode(/*EL:87*/Type.getDescriptor(a2), a3);
        /*SL:88*/a1.visibleAnnotations = add(a1.visibleAnnotations, v1);
    }
    
    public static void setInvisible(final MethodNode a1, final Class<? extends Annotation> a2, final Object... a3) {
        final AnnotationNode v1 = createNode(/*EL:100*/Type.getDescriptor(a2), a3);
        /*SL:101*/a1.invisibleAnnotations = add(a1.invisibleAnnotations, v1);
    }
    
    private static AnnotationNode createNode(final String a2, final Object... v1) {
        final AnnotationNode v2 = /*EL:113*/new AnnotationNode(a2);
        /*SL:114*/for (int a3 = 0; a3 < v1.length - 1; a3 += 2) {
            /*SL:115*/if (!(v1[a3] instanceof String)) {
                /*SL:116*/throw new IllegalArgumentException("Annotation keys must be strings, found " + v1[a3].getClass().getSimpleName() + " with " + v1[a3].toString() + /*EL:117*/" at index " + a3 + " creating " + a2);
            }
            /*SL:119*/v2.visit((String)v1[a3], v1[a3 + 1]);
        }
        /*SL:121*/return v2;
    }
    
    private static List<AnnotationNode> add(List<AnnotationNode> a1, final AnnotationNode a2) {
        /*SL:125*/if (a1 == null) {
            /*SL:126*/a1 = new ArrayList<AnnotationNode>(1);
        }
        else {
            /*SL:128*/a1.remove(get(a1, a2.desc));
        }
        /*SL:130*/a1.add(a2);
        /*SL:131*/return a1;
    }
    
    public static AnnotationNode getVisible(final FieldNode a1, final Class<? extends Annotation> a2) {
        /*SL:143*/return get(a1.visibleAnnotations, Type.getDescriptor(a2));
    }
    
    public static AnnotationNode getInvisible(final FieldNode a1, final Class<? extends Annotation> a2) {
        /*SL:155*/return get(a1.invisibleAnnotations, Type.getDescriptor(a2));
    }
    
    public static AnnotationNode getVisible(final MethodNode a1, final Class<? extends Annotation> a2) {
        /*SL:167*/return get(a1.visibleAnnotations, Type.getDescriptor(a2));
    }
    
    public static AnnotationNode getInvisible(final MethodNode a1, final Class<? extends Annotation> a2) {
        /*SL:179*/return get(a1.invisibleAnnotations, Type.getDescriptor(a2));
    }
    
    public static AnnotationNode getSingleVisible(final MethodNode a1, final Class<? extends Annotation>... a2) {
        /*SL:191*/return getSingle(a1.visibleAnnotations, a2);
    }
    
    public static AnnotationNode getSingleInvisible(final MethodNode a1, final Class<? extends Annotation>... a2) {
        /*SL:203*/return getSingle(a1.invisibleAnnotations, a2);
    }
    
    public static AnnotationNode getVisible(final ClassNode a1, final Class<? extends Annotation> a2) {
        /*SL:215*/return get(a1.visibleAnnotations, Type.getDescriptor(a2));
    }
    
    public static AnnotationNode getInvisible(final ClassNode a1, final Class<? extends Annotation> a2) {
        /*SL:227*/return get(a1.invisibleAnnotations, Type.getDescriptor(a2));
    }
    
    public static AnnotationNode getVisibleParameter(final MethodNode a1, final Class<? extends Annotation> a2, final int a3) {
        /*SL:240*/return getParameter(a1.visibleParameterAnnotations, Type.getDescriptor(a2), a3);
    }
    
    public static AnnotationNode getInvisibleParameter(final MethodNode a1, final Class<? extends Annotation> a2, final int a3) {
        /*SL:253*/return getParameter(a1.invisibleParameterAnnotations, Type.getDescriptor(a2), a3);
    }
    
    public static AnnotationNode getParameter(final List<AnnotationNode>[] a1, final String a2, final int a3) {
        /*SL:266*/if (a1 == null || a3 < 0 || a3 >= a1.length) {
            /*SL:267*/return null;
        }
        /*SL:270*/return get(a1[a3], a2);
    }
    
    public static AnnotationNode get(final List<AnnotationNode> a2, final String v1) {
        /*SL:283*/if (a2 == null) {
            /*SL:284*/return null;
        }
        /*SL:287*/for (final AnnotationNode a3 : a2) {
            /*SL:288*/if (v1.equals(a3.desc)) {
                /*SL:289*/return a3;
            }
        }
        /*SL:293*/return null;
    }
    
    private static AnnotationNode getSingle(final List<AnnotationNode> v1, final Class<? extends Annotation>[] v2) {
        final List<AnnotationNode> v3 = /*EL:297*/new ArrayList<AnnotationNode>();
        /*SL:298*/for (AnnotationNode a2 : v2) {
            /*SL:299*/a2 = get(v1, Type.getDescriptor(a2));
            /*SL:300*/if (a2 != null) {
                /*SL:301*/v3.add(a2);
            }
        }
        final int v4 = /*EL:305*/v3.size();
        /*SL:306*/if (v4 > 1) {
            /*SL:307*/throw new IllegalArgumentException("Conflicting annotations found: " + Lists.<AnnotationNode, Object>transform(v3, (Function<? super AnnotationNode, ?>)new Function<AnnotationNode, String>() {
                @Override
                public String apply(final AnnotationNode a1) {
                    /*SL:309*/return a1.desc;
                }
            }));
        }
        /*SL:314*/return (v4 == 0) ? null : v3.get(0);
    }
    
    public static <T> T getValue(final AnnotationNode a1) {
        /*SL:326*/return Annotations.<T>getValue(a1, "value");
    }
    
    public static <T> T getValue(final AnnotationNode a1, final String a2, final T a3) {
        final T v1 = /*EL:342*/Annotations.<T>getValue(a1, a2);
        /*SL:343*/return (v1 != null) ? v1 : a3;
    }
    
    public static <T> T getValue(final AnnotationNode a1, final String a2, final Class<?> a3) {
        /*SL:360*/Preconditions.<Class<?>>checkNotNull(a3, (Object)"annotationClass cannot be null");
        T v1 = /*EL:361*/Annotations.<T>getValue(a1, a2);
        /*SL:362*/if (v1 == null) {
            try {
                /*SL:364*/v1 = (T)a3.getDeclaredMethod(a2, (Class<?>[])new Class[0]).getDefaultValue();
            }
            catch (NoSuchMethodException ex) {}
        }
        /*SL:369*/return v1;
    }
    
    public static <T> T getValue(final AnnotationNode a2, final String v1) {
        boolean v2 = /*EL:384*/false;
        /*SL:386*/if (a2 == null || a2.values == null) {
            /*SL:387*/return null;
        }
        /*SL:391*/for (final Object a3 : a2.values) {
            /*SL:392*/if (v2) {
                /*SL:393*/return (T)a3;
            }
            /*SL:395*/if (!a3.equals(v1)) {
                continue;
            }
            /*SL:396*/v2 = true;
        }
        /*SL:400*/return null;
    }
    
    public static <T extends Enum<T>> T getValue(final AnnotationNode a1, final String a2, final Class<T> a3, final T a4) {
        final String[] v1 = /*EL:415*/Annotations.<String[]>getValue(a1, a2);
        /*SL:416*/if (v1 == null) {
            /*SL:417*/return a4;
        }
        /*SL:419*/return Annotations.<T>toEnumValue(a3, v1);
    }
    
    public static <T> List<T> getValue(final AnnotationNode a2, final String a3, final boolean v1) {
        final Object v2 = /*EL:433*/Annotations.<Object>getValue(a2, a3);
        /*SL:434*/if (v2 instanceof List) {
            /*SL:435*/return (List<T>)v2;
        }
        /*SL:436*/if (v2 != null) {
            final List<T> a4 = /*EL:437*/new ArrayList<T>();
            /*SL:438*/a4.add((T)v2);
            /*SL:439*/return a4;
        }
        /*SL:441*/return Collections.<T>emptyList();
    }
    
    public static <T extends Enum<T>> List<T> getValue(final AnnotationNode a3, final String a4, final boolean v1, final Class<T> v2) {
        final Object v3 = /*EL:456*/Annotations.<Object>getValue(a3, a4);
        /*SL:457*/if (v3 instanceof List) {
            final ListIterator<Object> a5 = /*EL:458*/((List)v3).listIterator();
            while (a5.hasNext()) {
                /*SL:459*/a5.set(Annotations.<T>toEnumValue(v2, a5.next()));
            }
            /*SL:461*/return (List<T>)v3;
        }
        /*SL:462*/if (v3 instanceof String[]) {
            final List<T> a6 = /*EL:463*/new ArrayList<T>();
            /*SL:464*/a6.add(Annotations.<T>toEnumValue(v2, (String[])v3));
            /*SL:465*/return a6;
        }
        /*SL:467*/return Collections.<T>emptyList();
    }
    
    private static <T extends Enum<T>> T toEnumValue(final Class<T> a1, final String[] a2) {
        /*SL:471*/if (!a1.getName().equals(Type.getType(a2[0]).getClassName())) {
            /*SL:472*/throw new IllegalArgumentException("The supplied enum class does not match the stored enum value");
        }
        /*SL:474*/return Enum.<T>valueOf(a1, a2[1]);
    }
}

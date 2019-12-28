package org.spongepowered.asm.lib.tree;

import java.util.ArrayList;
import java.util.List;
import org.spongepowered.asm.lib.AnnotationVisitor;

public class AnnotationNode extends AnnotationVisitor
{
    public String desc;
    public List<Object> values;
    
    public AnnotationNode(final String a1) {
        this(327680, a1);
        if (this.getClass() != AnnotationNode.class) {
            throw new IllegalStateException();
        }
    }
    
    public AnnotationNode(final int a1, final String a2) {
        super(a1);
        this.desc = a2;
    }
    
    AnnotationNode(final List<Object> a1) {
        super(327680);
        this.values = a1;
    }
    
    public void visit(final String v-2, final Object v-1) {
        /*SL:111*/if (this.values == null) {
            /*SL:112*/this.values = new ArrayList<Object>((this.desc != null) ? 2 : 1);
        }
        /*SL:114*/if (this.desc != null) {
            /*SL:115*/this.values.add(v-2);
        }
        /*SL:117*/if (v-1 instanceof byte[]) {
            byte[] a2 = /*EL:118*/(byte[])v-1;
            final ArrayList<Byte> v1 = /*EL:119*/new ArrayList<Byte>(a2.length);
            final byte[] array = /*EL:120*/a2;
            for (int n = array.length, i = 0; i < n; ++i) {
                a2 = array[i];
                /*SL:121*/v1.add(a2);
            }
            /*SL:123*/this.values.add(v1);
        }
        else/*SL:124*/ if (v-1 instanceof boolean[]) {
            final boolean[] v2 = /*EL:125*/(boolean[])v-1;
            final ArrayList<Boolean> v3 = /*EL:126*/new ArrayList<Boolean>(v2.length);
            /*SL:127*/for (final boolean v4 : v2) {
                /*SL:128*/v3.add(v4);
            }
            /*SL:130*/this.values.add(v3);
        }
        else/*SL:131*/ if (v-1 instanceof short[]) {
            final short[] v5 = /*EL:132*/(short[])v-1;
            final ArrayList<Short> v6 = /*EL:133*/new ArrayList<Short>(v5.length);
            /*SL:134*/for (final short v7 : v5) {
                /*SL:135*/v6.add(v7);
            }
            /*SL:137*/this.values.add(v6);
        }
        else/*SL:138*/ if (v-1 instanceof char[]) {
            final char[] v8 = /*EL:139*/(char[])v-1;
            final ArrayList<Character> v9 = /*EL:140*/new ArrayList<Character>(v8.length);
            /*SL:141*/for (final char v10 : v8) {
                /*SL:142*/v9.add(v10);
            }
            /*SL:144*/this.values.add(v9);
        }
        else/*SL:145*/ if (v-1 instanceof int[]) {
            final int[] v11 = /*EL:146*/(int[])v-1;
            final ArrayList<Integer> v12 = /*EL:147*/new ArrayList<Integer>(v11.length);
            /*SL:148*/for (final int v13 : v11) {
                /*SL:149*/v12.add(v13);
            }
            /*SL:151*/this.values.add(v12);
        }
        else/*SL:152*/ if (v-1 instanceof long[]) {
            final long[] v14 = /*EL:153*/(long[])v-1;
            final ArrayList<Long> v15 = /*EL:154*/new ArrayList<Long>(v14.length);
            /*SL:155*/for (final long v16 : v14) {
                /*SL:156*/v15.add(v16);
            }
            /*SL:158*/this.values.add(v15);
        }
        else/*SL:159*/ if (v-1 instanceof float[]) {
            final float[] v17 = /*EL:160*/(float[])v-1;
            final ArrayList<Float> v18 = /*EL:161*/new ArrayList<Float>(v17.length);
            /*SL:162*/for (final float v19 : v17) {
                /*SL:163*/v18.add(v19);
            }
            /*SL:165*/this.values.add(v18);
        }
        else/*SL:166*/ if (v-1 instanceof double[]) {
            final double[] v20 = /*EL:167*/(double[])v-1;
            final ArrayList<Double> v21 = /*EL:168*/new ArrayList<Double>(v20.length);
            /*SL:169*/for (final double v22 : v20) {
                /*SL:170*/v21.add(v22);
            }
            /*SL:172*/this.values.add(v21);
        }
        else {
            /*SL:174*/this.values.add(v-1);
        }
    }
    
    public void visitEnum(final String a1, final String a2, final String a3) {
        /*SL:181*/if (this.values == null) {
            /*SL:182*/this.values = new ArrayList<Object>((this.desc != null) ? 2 : 1);
        }
        /*SL:184*/if (this.desc != null) {
            /*SL:185*/this.values.add(a1);
        }
        /*SL:187*/this.values.add(new String[] { a2, a3 });
    }
    
    public AnnotationVisitor visitAnnotation(final String a1, final String a2) {
        /*SL:193*/if (this.values == null) {
            /*SL:194*/this.values = new ArrayList<Object>((this.desc != null) ? 2 : 1);
        }
        /*SL:196*/if (this.desc != null) {
            /*SL:197*/this.values.add(a1);
        }
        final AnnotationNode v1 = /*EL:199*/new AnnotationNode(a2);
        /*SL:200*/this.values.add(v1);
        /*SL:201*/return v1;
    }
    
    public AnnotationVisitor visitArray(final String a1) {
        /*SL:206*/if (this.values == null) {
            /*SL:207*/this.values = new ArrayList<Object>((this.desc != null) ? 2 : 1);
        }
        /*SL:209*/if (this.desc != null) {
            /*SL:210*/this.values.add(a1);
        }
        final List<Object> v1 = /*EL:212*/new ArrayList<Object>();
        /*SL:213*/this.values.add(v1);
        /*SL:214*/return new AnnotationNode(v1);
    }
    
    public void visitEnd() {
    }
    
    public void check(final int a1) {
    }
    
    public void accept(final AnnotationVisitor v-2) {
        /*SL:246*/if (v-2 != null) {
            /*SL:247*/if (this.values != null) {
                /*SL:248*/for (int i = 0; i < this.values.size(); i += 2) {
                    final String a1 = /*EL:249*/this.values.get(i);
                    final Object v1 = /*EL:250*/this.values.get(i + 1);
                    accept(/*EL:251*/v-2, a1, v1);
                }
            }
            /*SL:254*/v-2.visitEnd();
        }
    }
    
    static void accept(final AnnotationVisitor v-3, final String v-2, final Object v-1) {
        /*SL:270*/if (v-3 != null) {
            /*SL:271*/if (v-1 instanceof String[]) {
                final String[] a1 = /*EL:272*/(String[])v-1;
                /*SL:273*/v-3.visitEnum(v-2, a1[0], a1[1]);
            }
            else/*SL:274*/ if (v-1 instanceof AnnotationNode) {
                final AnnotationNode a2 = /*EL:275*/(AnnotationNode)v-1;
                /*SL:276*/a2.accept(v-3.visitAnnotation(v-2, a2.desc));
            }
            else/*SL:277*/ if (v-1 instanceof List) {
                final AnnotationVisitor v0 = /*EL:278*/v-3.visitArray(v-2);
                /*SL:279*/if (v0 != null) {
                    final List<?> v = /*EL:280*/(List<?>)v-1;
                    /*SL:281*/for (int a3 = 0; a3 < v.size(); ++a3) {
                        accept(/*EL:282*/v0, null, v.get(a3));
                    }
                    /*SL:284*/v0.visitEnd();
                }
            }
            else {
                /*SL:287*/v-3.visit(v-2, v-1);
            }
        }
    }
}

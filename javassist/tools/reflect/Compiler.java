package javassist.tools.reflect;

import java.io.PrintStream;
import javassist.CtClass;
import javassist.ClassPool;

public class Compiler
{
    public static void main(final String[] a1) throws Exception {
        /*SL:74*/if (a1.length == 0) {
            help(System.err);
            /*SL:76*/return;
        }
        final CompiledClass[] v1 = /*EL:79*/new CompiledClass[a1.length];
        final int v2 = parse(/*EL:80*/a1, v1);
        /*SL:82*/if (v2 < 1) {
            System.err.println(/*EL:83*/"bad parameter.");
            /*SL:84*/return;
        }
        processClasses(/*EL:87*/v1, v2);
    }
    
    private static void processClasses(final CompiledClass[] v-5, final int v-4) throws Exception {
        final Reflection reflection = /*EL:93*/new Reflection();
        final ClassPool default1 = /*EL:94*/ClassPool.getDefault();
        /*SL:95*/reflection.start(default1);
        /*SL:97*/for (int i = 0; i < v-4; ++i) {
            final CtClass v0 = /*EL:98*/default1.get(v-5[i].classname);
            /*SL:99*/if (v-5[i].metaobject != null || v-5[i].classobject != null) {
                final String v;
                /*SL:103*/if (v-5[i].metaobject == null) {
                    final String a1 = /*EL:104*/"javassist.tools.reflect.Metaobject";
                }
                else {
                    /*SL:106*/v = v-5[i].metaobject;
                }
                final String v2;
                /*SL:108*/if (v-5[i].classobject == null) {
                    final String a2 = /*EL:109*/"javassist.tools.reflect.ClassMetaobject";
                }
                else {
                    /*SL:111*/v2 = v-5[i].classobject;
                }
                /*SL:113*/if (!reflection.makeReflective(v0, default1.get(v), default1.get(v2))) {
                    System.err.println(/*EL:115*/"Warning: " + v0.getName() + " is reflective.  It was not changed.");
                }
                System.err.println(/*EL:118*/v0.getName() + ": " + v + ", " + v2);
            }
            else {
                System.err.println(/*EL:122*/v0.getName() + ": not reflective");
            }
        }
        /*SL:125*/for (int i = 0; i < v-4; ++i) {
            /*SL:126*/reflection.onLoad(default1, v-5[i].classname);
            /*SL:127*/default1.get(v-5[i].classname).writeFile();
        }
    }
    
    private static int parse(final String[] v-2, final CompiledClass[] v-1) {
        int v0 = /*EL:132*/-1;
        /*SL:133*/for (int v = 0; v < v-2.length; ++v) {
            String a2 = /*EL:134*/v-2[v];
            /*SL:135*/if (a2.equals("-m")) {
                /*SL:136*/if (v0 < 0 || v + 1 > v-2.length) {
                    /*SL:137*/return -1;
                }
                /*SL:139*/v-1[v0].metaobject = v-2[++v];
            }
            else/*SL:140*/ if (a2.equals("-c")) {
                /*SL:141*/if (v0 < 0 || v + 1 > v-2.length) {
                    /*SL:142*/return -1;
                }
                /*SL:144*/v-1[v0].classobject = v-2[++v];
            }
            else {
                /*SL:145*/if (a2.charAt(0) == '-') {
                    /*SL:146*/return -1;
                }
                /*SL:148*/a2 = new CompiledClass();
                /*SL:149*/a2.classname = a2;
                /*SL:150*/a2.metaobject = null;
                /*SL:151*/a2.classobject = null;
                /*SL:152*/v-1[++v0] = a2;
            }
        }
        /*SL:156*/return v0 + 1;
    }
    
    private static void help(final PrintStream a1) {
        /*SL:160*/a1.println("Usage: java javassist.tools.reflect.Compiler");
        /*SL:161*/a1.println("            (<class> [-m <metaobject>] [-c <class metaobject>])+");
    }
}

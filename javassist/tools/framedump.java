package javassist.tools;

import javassist.CtClass;
import javassist.bytecode.analysis.FramePrinter;
import javassist.ClassPool;

public class framedump
{
    public static void main(final String[] a1) throws Exception {
        /*SL:38*/if (a1.length != 1) {
            System.err.println(/*EL:39*/"Usage: java javassist.tools.framedump <fully-qualified class name>");
            /*SL:40*/return;
        }
        final ClassPool v1 = /*EL:43*/ClassPool.getDefault();
        final CtClass v2 = /*EL:44*/v1.get(a1[0]);
        System.out.println(/*EL:45*/"Frame Dump of " + v2.getName() + ":");
        /*SL:46*/FramePrinter.print(v2, System.out);
    }
}

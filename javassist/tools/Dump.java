package javassist.tools;

import javassist.bytecode.ClassFilePrinter;
import java.io.OutputStream;
import java.io.PrintWriter;
import javassist.bytecode.ClassFile;
import java.io.InputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;

public class Dump
{
    public static void main(final String[] a1) throws Exception {
        /*SL:43*/if (a1.length != 1) {
            System.err.println(/*EL:44*/"Usage: java Dump <class file name>");
            /*SL:45*/return;
        }
        final DataInputStream v1 = /*EL:48*/new DataInputStream(new FileInputStream(a1[0]));
        final ClassFile v2 = /*EL:50*/new ClassFile(v1);
        final PrintWriter v3 = /*EL:51*/new PrintWriter(System.out, true);
        /*SL:52*/v3.println("*** constant pool ***");
        /*SL:53*/v2.getConstPool().print(v3);
        /*SL:54*/v3.println();
        /*SL:55*/v3.println("*** members ***");
        /*SL:56*/ClassFilePrinter.print(v2, v3);
    }
}

package javassist.bytecode.analysis;

import javassist.bytecode.CodeIterator;
import javassist.bytecode.Opcode;

public class Util implements Opcode
{
    public static int getJumpTarget(int a1, final CodeIterator a2) {
        final int v1 = /*EL:28*/a2.byteAt(a1);
        /*SL:29*/a1 += ((v1 == 201 || v1 == 200) ? a2.s32bitAt(a1 + 1) : a2.s16bitAt(a1 + 1));
        /*SL:30*/return a1;
    }
    
    public static boolean isJumpInstruction(final int a1) {
        /*SL:34*/return (a1 >= 153 && a1 <= 168) || a1 == 198 || a1 == 199 || a1 == 201 || a1 == 200;
    }
    
    public static boolean isGoto(final int a1) {
        /*SL:38*/return a1 == 167 || a1 == 200;
    }
    
    public static boolean isJsr(final int a1) {
        /*SL:42*/return a1 == 168 || a1 == 201;
    }
    
    public static boolean isReturn(final int a1) {
        /*SL:46*/return a1 >= 172 && a1 <= 177;
    }
}

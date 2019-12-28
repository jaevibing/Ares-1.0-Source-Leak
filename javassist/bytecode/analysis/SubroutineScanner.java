package javassist.bytecode.analysis;

import javassist.bytecode.BadBytecode;
import javassist.bytecode.ExceptionTable;
import javassist.bytecode.CodeIterator;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.MethodInfo;
import java.util.HashSet;
import java.util.HashMap;
import java.util.Set;
import java.util.Map;
import javassist.bytecode.Opcode;

public class SubroutineScanner implements Opcode
{
    private Subroutine[] subroutines;
    Map subTable;
    Set done;
    
    public SubroutineScanner() {
        this.subTable = new HashMap();
        this.done = new HashSet();
    }
    
    public Subroutine[] scan(final MethodInfo v-3) throws BadBytecode {
        final CodeAttribute codeAttribute = /*EL:43*/v-3.getCodeAttribute();
        final CodeIterator iterator = /*EL:44*/codeAttribute.iterator();
        /*SL:46*/this.subroutines = new Subroutine[codeAttribute.getCodeLength()];
        /*SL:47*/this.subTable.clear();
        /*SL:48*/this.done.clear();
        /*SL:50*/this.scan(0, iterator, null);
        final ExceptionTable v0 = /*EL:52*/codeAttribute.getExceptionTable();
        /*SL:53*/for (int v = 0; v < v0.size(); ++v) {
            final int a1 = /*EL:54*/v0.handlerPc(v);
            /*SL:57*/this.scan(a1, iterator, this.subroutines[v0.startPc(v)]);
        }
        /*SL:60*/return this.subroutines;
    }
    
    private void scan(int a1, final CodeIterator a2, final Subroutine a3) throws BadBytecode {
        /*SL:65*/if (this.done.contains(new Integer(a1))) {
            /*SL:66*/return;
        }
        /*SL:68*/this.done.add(new Integer(a1));
        final int v1 = /*EL:70*/a2.lookAhead();
        /*SL:71*/a2.move(a1);
        boolean v2;
        /*SL:77*/do {
            a1 = a2.next();
            v2 = (this.scanOp(a1, a2, a3) && a2.hasNext());
        } while (v2);
        /*SL:79*/a2.move(v1);
    }
    
    private boolean scanOp(final int v1, final CodeIterator v2, final Subroutine v3) throws BadBytecode {
        /*SL:83*/this.subroutines[v1] = v3;
        final int v4 = /*EL:85*/v2.byteAt(v1);
        /*SL:87*/if (v4 == 170) {
            /*SL:88*/this.scanTableSwitch(v1, v2, v3);
            /*SL:90*/return false;
        }
        /*SL:93*/if (v4 == 171) {
            /*SL:94*/this.scanLookupSwitch(v1, v2, v3);
            /*SL:96*/return false;
        }
        /*SL:100*/if (Util.isReturn(v4) || v4 == 169 || v4 == 191) {
            /*SL:101*/return false;
        }
        /*SL:103*/if (Util.isJumpInstruction(v4)) {
            int a2 = /*EL:104*/Util.getJumpTarget(v1, v2);
            /*SL:105*/if (v4 == 168 || v4 == 201) {
                /*SL:106*/a2 = this.subTable.get(new Integer(a2));
                /*SL:107*/if (a2 == null) {
                    /*SL:108*/a2 = new Subroutine(a2, v1);
                    /*SL:109*/this.subTable.put(new Integer(a2), a2);
                    /*SL:110*/this.scan(a2, v2, a2);
                }
                else {
                    /*SL:112*/a2.addCaller(v1);
                }
            }
            else {
                /*SL:115*/this.scan(a2, v2, v3);
                /*SL:118*/if (Util.isGoto(v4)) {
                    /*SL:119*/return false;
                }
            }
        }
        /*SL:123*/return true;
    }
    
    private void scanLookupSwitch(final int a3, final CodeIterator v1, final Subroutine v2) throws BadBytecode {
        int v3 = /*EL:127*/(a3 & 0xFFFFFFFC) + 4;
        /*SL:129*/this.scan(a3 + v1.s32bitAt(v3), v1, v2);
        /*SL:130*/v3 += 4;
        final int v4 = v1.s32bitAt(v3);
        final int n = /*EL:131*/v4 * 8;
        /*SL:134*/for (v3 += 4, final int v5 = n + v3, v3 += 4; v3 < v5; v3 += 8) {
            final int a4 = /*EL:135*/v1.s32bitAt(v3) + a3;
            /*SL:136*/this.scan(a4, v1, v2);
        }
    }
    
    private void scanTableSwitch(final int a3, final CodeIterator v1, final Subroutine v2) throws BadBytecode {
        int v3 = /*EL:142*/(a3 & 0xFFFFFFFC) + 4;
        /*SL:144*/this.scan(a3 + v1.s32bitAt(v3), v1, v2);
        /*SL:145*/v3 += 4;
        final int v4 = v1.s32bitAt(v3);
        /*SL:146*/v3 += 4;
        final int v5 = v1.s32bitAt(v3);
        final int n = /*EL:147*/(v5 - v4 + 1) * 4;
        v3 += 4;
        /*SL:150*/for (int v6 = n + v3; v3 < v6; v3 += 4) {
            final int a4 = /*EL:151*/v1.s32bitAt(v3) + a3;
            /*SL:152*/this.scan(a4, v1, v2);
        }
    }
}

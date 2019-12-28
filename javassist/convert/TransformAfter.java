package javassist.convert;

import javassist.bytecode.BadBytecode;
import javassist.bytecode.CodeIterator;
import javassist.NotFoundException;
import javassist.CtMethod;

public class TransformAfter extends TransformBefore
{
    public TransformAfter(final Transformer a1, final CtMethod a2, final CtMethod a3) throws NotFoundException {
        super(a1, a2, a3);
    }
    
    @Override
    protected int match2(int a1, final CodeIterator a2) throws BadBytecode {
        /*SL:32*/a2.move(a1);
        /*SL:33*/a2.insert(this.saveCode);
        /*SL:34*/a2.insert(this.loadCode);
        int v1 = /*EL:35*/a2.insertGap(3);
        /*SL:36*/a2.setMark(v1);
        /*SL:37*/a2.insert(this.loadCode);
        /*SL:38*/a1 = a2.next();
        /*SL:39*/v1 = a2.getMark();
        /*SL:40*/a2.writeByte(a2.byteAt(a1), v1);
        /*SL:41*/a2.write16bit(a2.u16bitAt(a1 + 1), v1 + 1);
        /*SL:42*/a2.writeByte(184, a1);
        /*SL:43*/a2.write16bit(this.newIndex, a1 + 1);
        /*SL:44*/a2.move(v1);
        /*SL:45*/return a2.next();
    }
}

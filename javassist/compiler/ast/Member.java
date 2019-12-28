package javassist.compiler.ast;

import javassist.compiler.CompileError;
import javassist.CtField;

public class Member extends Symbol
{
    private CtField field;
    
    public Member(final String a1) {
        super(a1);
        this.field = null;
    }
    
    public void setField(final CtField a1) {
        /*SL:35*/this.field = a1;
    }
    
    public CtField getField() {
        /*SL:37*/return this.field;
    }
    
    @Override
    public void accept(final Visitor a1) throws CompileError {
        /*SL:39*/a1.atMember(this);
    }
}

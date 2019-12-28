package javassist.bytecode;

import javassist.CannotCompileException;

public class DuplicateMemberException extends CannotCompileException
{
    public DuplicateMemberException(final String a1) {
        super(a1);
    }
}

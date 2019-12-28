package javassist.compiler;

import javassist.NotFoundException;
import javassist.CannotCompileException;

public class CompileError extends Exception
{
    private Lex lex;
    private String reason;
    
    public CompileError(final String a1, final Lex a2) {
        this.reason = a1;
        this.lex = a2;
    }
    
    public CompileError(final String a1) {
        this.reason = a1;
        this.lex = null;
    }
    
    public CompileError(final CannotCompileException a1) {
        this(a1.getReason());
    }
    
    public CompileError(final NotFoundException a1) {
        this("cannot find " + a1.getMessage());
    }
    
    public Lex getLex() {
        /*SL:44*/return this.lex;
    }
    
    @Override
    public String getMessage() {
        /*SL:47*/return this.reason;
    }
    
    @Override
    public String toString() {
        /*SL:51*/return "compile error: " + this.reason;
    }
}

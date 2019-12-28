package javassist;

import javassist.compiler.CompileError;

public class CannotCompileException extends Exception
{
    private Throwable myCause;
    private String message;
    
    @Override
    public Throwable getCause() {
        /*SL:32*/return (this.myCause == this) ? null : this.myCause;
    }
    
    @Override
    public synchronized Throwable initCause(final Throwable a1) {
        /*SL:40*/this.myCause = a1;
        /*SL:41*/return this;
    }
    
    public String getReason() {
        /*SL:50*/if (this.message != null) {
            /*SL:51*/return this.message;
        }
        /*SL:53*/return this.toString();
    }
    
    public CannotCompileException(final String a1) {
        super(a1);
        this.message = a1;
        this.initCause(null);
    }
    
    public CannotCompileException(final Throwable a1) {
        super("by " + a1.toString());
        this.message = null;
        this.initCause(a1);
    }
    
    public CannotCompileException(final String a1, final Throwable a2) {
        this(a1);
        this.initCause(a2);
    }
    
    public CannotCompileException(final NotFoundException a1) {
        this("cannot find " + a1.getMessage(), a1);
    }
    
    public CannotCompileException(final CompileError a1) {
        this("[source error] " + a1.getMessage(), a1);
    }
    
    public CannotCompileException(final ClassNotFoundException a1, final String a2) {
        this("cannot find " + a2, a1);
    }
    
    public CannotCompileException(final ClassFormatError a1, final String a2) {
        this("invalid class format: " + a2, a1);
    }
}

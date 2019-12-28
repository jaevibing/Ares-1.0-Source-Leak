package org.yaml.snakeyaml.error;

public class MarkedYAMLException extends YAMLException
{
    private static final long serialVersionUID = -9119388488683035101L;
    private String context;
    private Mark contextMark;
    private String problem;
    private Mark problemMark;
    private String note;
    
    protected MarkedYAMLException(final String a1, final Mark a2, final String a3, final Mark a4, final String a5) {
        this(a1, a2, a3, a4, a5, null);
    }
    
    protected MarkedYAMLException(final String a1, final Mark a2, final String a3, final Mark a4, final String a5, final Throwable a6) {
        super(a1 + "; " + a3 + "; " + a4, a6);
        this.context = a1;
        this.contextMark = a2;
        this.problem = a3;
        this.problemMark = a4;
        this.note = a5;
    }
    
    protected MarkedYAMLException(final String a1, final Mark a2, final String a3, final Mark a4) {
        this(a1, a2, a3, a4, null, null);
    }
    
    protected MarkedYAMLException(final String a1, final Mark a2, final String a3, final Mark a4, final Throwable a5) {
        this(a1, a2, a3, a4, null, a5);
    }
    
    @Override
    public String getMessage() {
        /*SL:53*/return this.toString();
    }
    
    @Override
    public String toString() {
        final StringBuilder v1 = /*EL:58*/new StringBuilder();
        /*SL:59*/if (this.context != null) {
            /*SL:60*/v1.append(this.context);
            /*SL:61*/v1.append("\n");
        }
        /*SL:63*/if (this.contextMark != null && (this.problem == null || this.problemMark == null || this.contextMark.getName().equals(/*EL:65*/this.problemMark.getName()) || this.contextMark.getLine() != /*EL:66*/this.problemMark.getLine() || this.contextMark.getColumn() != /*EL:67*/this.problemMark.getColumn())) {
            /*SL:68*/v1.append(this.contextMark.toString());
            /*SL:69*/v1.append("\n");
        }
        /*SL:71*/if (this.problem != null) {
            /*SL:72*/v1.append(this.problem);
            /*SL:73*/v1.append("\n");
        }
        /*SL:75*/if (this.problemMark != null) {
            /*SL:76*/v1.append(this.problemMark.toString());
            /*SL:77*/v1.append("\n");
        }
        /*SL:79*/if (this.note != null) {
            /*SL:80*/v1.append(this.note);
            /*SL:81*/v1.append("\n");
        }
        /*SL:83*/return v1.toString();
    }
    
    public String getContext() {
        /*SL:87*/return this.context;
    }
    
    public Mark getContextMark() {
        /*SL:91*/return this.contextMark;
    }
    
    public String getProblem() {
        /*SL:95*/return this.problem;
    }
    
    public Mark getProblemMark() {
        /*SL:99*/return this.problemMark;
    }
}

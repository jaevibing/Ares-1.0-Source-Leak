package org.yaml.snakeyaml.emitter;

public final class ScalarAnalysis
{
    public String scalar;
    public boolean empty;
    public boolean multiline;
    public boolean allowFlowPlain;
    public boolean allowBlockPlain;
    public boolean allowSingleQuoted;
    public boolean allowBlock;
    
    public ScalarAnalysis(final String a1, final boolean a2, final boolean a3, final boolean a4, final boolean a5, final boolean a6, final boolean a7) {
        this.scalar = a1;
        this.empty = a2;
        this.multiline = a3;
        this.allowFlowPlain = a4;
        this.allowBlockPlain = a5;
        this.allowSingleQuoted = a6;
        this.allowBlock = a7;
    }
}

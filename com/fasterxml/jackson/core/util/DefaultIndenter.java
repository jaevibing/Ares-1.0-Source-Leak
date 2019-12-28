package com.fasterxml.jackson.core.util;

import java.io.IOException;
import com.fasterxml.jackson.core.JsonGenerator;

public class DefaultIndenter extends DefaultPrettyPrinter.NopIndenter
{
    private static final long serialVersionUID = 1L;
    public static final String SYS_LF;
    public static final DefaultIndenter SYSTEM_LINEFEED_INSTANCE;
    private static final int INDENT_LEVELS = 16;
    private final char[] indents;
    private final int charsPerLevel;
    private final String eol;
    
    public DefaultIndenter() {
        this("  ", DefaultIndenter.SYS_LF);
    }
    
    public DefaultIndenter(final String v1, final String v2) {
        this.charsPerLevel = v1.length();
        this.indents = new char[v1.length() * 16];
        int v3 = 0;
        for (int a1 = 0; a1 < 16; ++a1) {
            v1.getChars(0, v1.length(), this.indents, v3);
            v3 += v1.length();
        }
        this.eol = v2;
    }
    
    public DefaultIndenter withLinefeed(final String a1) {
        /*SL:67*/if (a1.equals(this.eol)) {
            /*SL:68*/return this;
        }
        /*SL:70*/return new DefaultIndenter(this.getIndent(), a1);
    }
    
    public DefaultIndenter withIndent(final String a1) {
        /*SL:75*/if (a1.equals(this.getIndent())) {
            /*SL:76*/return this;
        }
        /*SL:78*/return new DefaultIndenter(a1, this.eol);
    }
    
    @Override
    public boolean isInline() {
        /*SL:82*/return false;
    }
    
    @Override
    public void writeIndentation(final JsonGenerator a1, int a2) throws IOException {
        /*SL:87*/a1.writeRaw(this.eol);
        /*SL:88*/if (a2 > 0) {
            /*SL:90*/for (a2 *= this.charsPerLevel; a2 > this.indents.length; /*SL:92*/a2 -= this.indents.length) {
                a1.writeRaw(this.indents, 0, this.indents.length);
            }
            /*SL:94*/a1.writeRaw(this.indents, 0, a2);
        }
    }
    
    public String getEol() {
        /*SL:99*/return this.eol;
    }
    
    public String getIndent() {
        /*SL:103*/return new String(this.indents, 0, this.charsPerLevel);
    }
    
    static {
        String v0;
        try {
            v0 = System.getProperty("line.separator");
        }
        catch (Throwable v) {
            v0 = "\n";
        }
        SYS_LF = v0;
        SYSTEM_LINEFEED_INSTANCE = new DefaultIndenter("  ", DefaultIndenter.SYS_LF);
    }
}

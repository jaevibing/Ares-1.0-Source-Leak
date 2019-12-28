package org.yaml.snakeyaml.error;

import org.yaml.snakeyaml.scanner.Constant;
import java.io.Serializable;

public final class Mark implements Serializable
{
    private String name;
    private int index;
    private int line;
    private int column;
    private String buffer;
    private int pointer;
    
    public Mark(final String a1, final int a2, final int a3, final int a4, final String a5, final int a6) {
        this.name = a1;
        this.index = a2;
        this.line = a3;
        this.column = a4;
        this.buffer = a5;
        this.pointer = a6;
    }
    
    private boolean isLineBreak(final int a1) {
        /*SL:45*/return Constant.NULL_OR_LINEBR.has(a1);
    }
    
    public String get_snippet(final int v2, final int v3) {
        /*SL:49*/if (this.buffer == null) {
            /*SL:50*/return null;
        }
        final float v4 = /*EL:52*/v3 / 2 - 1;
        int v5 = /*EL:53*/this.pointer;
        String v6 = /*EL:54*/"";
        /*SL:55*/while (v5 > 0 && !this.isLineBreak(this.buffer.codePointAt(v5 - 1))) {
            /*SL:56*/--v5;
            /*SL:57*/if (this.pointer - v5 > v4) {
                /*SL:58*/v6 = " ... ";
                /*SL:59*/v5 += 5;
                break;
            }
        }
        String v7 = /*EL:63*/"";
        int v8 = /*EL:64*/this.pointer;
        /*SL:65*/while (v8 < this.buffer.length() && !this.isLineBreak(this.buffer.codePointAt(v8))) {
            /*SL:67*/if (++v8 - this.pointer > v4) {
                /*SL:68*/v7 = " ... ";
                /*SL:69*/v8 -= 5;
                break;
            }
        }
        final String v9 = /*EL:73*/this.buffer.substring(v5, v8);
        final StringBuilder v10 = /*EL:74*/new StringBuilder();
        /*SL:75*/for (int a1 = 0; a1 < v2; ++a1) {
            /*SL:76*/v10.append(" ");
        }
        /*SL:78*/v10.append(v6);
        /*SL:79*/v10.append(v9);
        /*SL:80*/v10.append(v7);
        /*SL:81*/v10.append("\n");
        /*SL:82*/for (int a2 = 0; a2 < v2 + this.pointer - v5 + v6.length(); ++a2) {
            /*SL:83*/v10.append(" ");
        }
        /*SL:85*/v10.append("^");
        /*SL:86*/return v10.toString();
    }
    
    public String get_snippet() {
        /*SL:90*/return this.get_snippet(4, 75);
    }
    
    @Override
    public String toString() {
        final String v1 = /*EL:95*/this.get_snippet();
        final StringBuilder v2 = /*EL:96*/new StringBuilder(" in ");
        /*SL:97*/v2.append(this.name);
        /*SL:98*/v2.append(", line ");
        /*SL:99*/v2.append(this.line + 1);
        /*SL:100*/v2.append(", column ");
        /*SL:101*/v2.append(this.column + 1);
        /*SL:102*/if (v1 != null) {
            /*SL:103*/v2.append(":\n");
            /*SL:104*/v2.append(v1);
        }
        /*SL:106*/return v2.toString();
    }
    
    public String getName() {
        /*SL:110*/return this.name;
    }
    
    public int getLine() {
        /*SL:118*/return this.line;
    }
    
    public int getColumn() {
        /*SL:126*/return this.column;
    }
    
    public int getIndex() {
        /*SL:134*/return this.index;
    }
}

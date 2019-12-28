package org.yaml.snakeyaml.reader;

import java.nio.charset.Charset;
import java.io.IOException;
import org.yaml.snakeyaml.error.YAMLException;
import org.yaml.snakeyaml.scanner.Constant;
import org.yaml.snakeyaml.error.Mark;
import java.io.Reader;

public class StreamReader
{
    private String name;
    private final Reader stream;
    private int pointer;
    private boolean eof;
    private String buffer;
    private int index;
    private int line;
    private int column;
    private char[] data;
    private static final int BUFFER_SIZE = 1025;
    
    public StreamReader(final String a1) {
        this.pointer = 0;
        this.eof = true;
        this.index = 0;
        this.line = 0;
        this.column = 0;
        this.name = "'string'";
        this.buffer = "";
        this.checkPrintable(a1);
        this.buffer = a1 + "\u0000";
        this.stream = null;
        this.eof = true;
        this.data = null;
    }
    
    public StreamReader(final Reader a1) {
        this.pointer = 0;
        this.eof = true;
        this.index = 0;
        this.line = 0;
        this.column = 0;
        this.name = "'reader'";
        this.buffer = "";
        this.stream = a1;
        this.eof = false;
        this.data = new char[1025];
        this.update();
    }
    
    void checkPrintable(final String v-1) {
        int a1;
        /*SL:63*/for (int v0 = v-1.length(), v = 0; v < v0; /*SL:71*/v += Character.charCount(a1)) {
            a1 = v-1.codePointAt(v);
            if (!isPrintable(a1)) {
                throw new ReaderException(this.name, v, a1, "special characters are not allowed");
            }
        }
    }
    
    public static boolean isPrintable(final String v-1) {
        int a1;
        /*SL:77*/for (int v0 = v-1.length(), v = 0; v < v0; /*SL:84*/v += Character.charCount(a1)) {
            a1 = v-1.codePointAt(v);
            if (!isPrintable(a1)) {
                return false;
            }
        }
        /*SL:87*/return true;
    }
    
    public static boolean isPrintable(final int a1) {
        /*SL:91*/return (a1 >= 32 && a1 <= 126) || a1 == 9 || a1 == 10 || a1 == 13 || a1 == 133 || (a1 >= 160 && a1 <= 55295) || (a1 >= 57344 && a1 <= 65533) || (a1 >= 65536 && a1 <= 1114111);
    }
    
    public Mark getMark() {
        /*SL:98*/return new Mark(this.name, this.index, this.line, this.column, this.buffer, this.pointer);
    }
    
    public void forward() {
        /*SL:102*/this.forward(1);
    }
    
    public void forward(final int v-1) {
        /*SL:113*/for (int v1 = 0; v1 < v-1; ++v1) {
            /*SL:114*/if (this.pointer == this.buffer.length()) {
                /*SL:115*/this.update();
            }
            /*SL:117*/if (this.pointer == this.buffer.length()) {
                /*SL:118*/break;
            }
            final int a1 = /*EL:121*/this.buffer.codePointAt(this.pointer);
            /*SL:122*/this.pointer += Character.charCount(a1);
            /*SL:123*/this.index += Character.charCount(a1);
            /*SL:124*/if (Constant.LINEBR.has(a1) || (a1 == 13 && this.buffer.charAt(this.pointer) != '\n')) {
                /*SL:125*/++this.line;
                /*SL:126*/this.column = 0;
            }
            else/*SL:127*/ if (a1 != 65279) {
                /*SL:128*/++this.column;
            }
        }
        /*SL:132*/if (this.pointer == this.buffer.length()) {
            /*SL:133*/this.update();
        }
    }
    
    public int peek() {
        /*SL:138*/if (this.pointer == this.buffer.length()) {
            /*SL:139*/this.update();
        }
        /*SL:141*/if (this.pointer == this.buffer.length()) {
            /*SL:142*/return -1;
        }
        /*SL:145*/return this.buffer.codePointAt(this.pointer);
    }
    
    public int peek(final int a1) {
        int v1 = /*EL:155*/0;
        int v2 = /*EL:156*/0;
        int v3;
        /*SL:170*/do {
            if (this.pointer + v1 == this.buffer.length()) {
                this.update();
            }
            if (this.pointer + v1 == this.buffer.length()) {
                return -1;
            }
            v3 = this.buffer.codePointAt(this.pointer + v1);
            v1 += Character.charCount(v3);
        } while (++v2 <= a1);
        /*SL:172*/return v3;
    }
    
    public String prefix(final int v2) {
        final StringBuilder v3 = /*EL:182*/new StringBuilder();
        int v4 = /*EL:184*/0;
        /*SL:186*/for (int v5 = 0; v5 < v2; /*SL:197*/++v5) {
            if (this.pointer + v4 == this.buffer.length()) {
                this.update();
            }
            if (this.pointer + v4 == this.buffer.length()) {
                break;
            }
            final int a1 = this.buffer.codePointAt(this.pointer + v4);
            v3.appendCodePoint(a1);
            v4 += Character.charCount(a1);
        }
        /*SL:200*/return v3.toString();
    }
    
    public String prefixForward(final int a1) {
        final String v1 = /*EL:209*/this.prefix(a1);
        /*SL:210*/this.pointer += v1.length();
        /*SL:211*/this.index += v1.length();
        /*SL:213*/this.column += a1;
        /*SL:214*/return v1;
    }
    
    private void update() {
        /*SL:218*/if (!this.eof) {
            /*SL:219*/this.buffer = this.buffer.substring(this.pointer);
            /*SL:220*/this.pointer = 0;
            try {
                boolean b = /*EL:222*/false;
                int v0 = /*EL:223*/this.stream.read(this.data, 0, 1024);
                /*SL:224*/if (v0 > 0) {
                    /*SL:225*/if (Character.isHighSurrogate(this.data[v0 - 1])) {
                        final int v = /*EL:226*/this.stream.read(this.data, v0, 1);
                        /*SL:227*/if (v != -1) {
                            /*SL:228*/v0 += v;
                        }
                        else {
                            /*SL:230*/b = true;
                        }
                    }
                    final StringBuilder v2 = /*EL:240*/new StringBuilder(this.buffer.length() + v0).append(this.buffer).append(/*EL:241*/this.data, 0, v0);
                    /*SL:243*/if (b) {
                        /*SL:244*/this.eof = true;
                        /*SL:245*/v2.append('\0');
                    }
                    /*SL:248*/this.checkPrintable(this.buffer = v2.toString());
                }
                else {
                    /*SL:250*/this.eof = true;
                    /*SL:251*/this.buffer += "\u0000";
                }
            }
            catch (IOException a1) {
                /*SL:254*/throw new YAMLException(a1);
            }
        }
    }
    
    public int getColumn() {
        /*SL:260*/return this.column;
    }
    
    public Charset getEncoding() {
        /*SL:264*/return Charset.forName(((UnicodeReader)this.stream).getEncoding());
    }
    
    public int getIndex() {
        /*SL:268*/return this.index;
    }
    
    public int getLine() {
        /*SL:272*/return this.line;
    }
}

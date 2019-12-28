package org.yaml.snakeyaml.reader;

import java.io.IOException;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CodingErrorAction;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PushbackInputStream;
import java.nio.charset.Charset;
import java.io.Reader;

public class UnicodeReader extends Reader
{
    private static final Charset UTF8;
    private static final Charset UTF16BE;
    private static final Charset UTF16LE;
    PushbackInputStream internalIn;
    InputStreamReader internalIn2;
    private static final int BOM_SIZE = 3;
    
    public UnicodeReader(final InputStream a1) {
        this.internalIn2 = null;
        this.internalIn = new PushbackInputStream(a1, 3);
    }
    
    public String getEncoding() {
        /*SL:77*/return this.internalIn2.getEncoding();
    }
    
    protected void init() throws IOException {
        /*SL:86*/if (this.internalIn2 != null) {
            /*SL:87*/return;
        }
        byte[] v2 = /*EL:90*/new byte[3];
        /*SL:92*/v2 = this.internalIn.read(v2, 0, v2.length);
        Charset v3;
        int v4;
        /*SL:94*/if (v2[0] == -17 && v2[1] == -69 && v2[2] == -65) {
            /*SL:95*/v3 = UnicodeReader.UTF8;
            /*SL:96*/v4 = v2 - 3;
        }
        else/*SL:97*/ if (v2[0] == -2 && v2[1] == -1) {
            /*SL:98*/v3 = UnicodeReader.UTF16BE;
            /*SL:99*/v4 = v2 - 2;
        }
        else/*SL:100*/ if (v2[0] == -1 && v2[1] == -2) {
            /*SL:101*/v3 = UnicodeReader.UTF16LE;
            /*SL:102*/v4 = v2 - 2;
        }
        else {
            /*SL:105*/v3 = UnicodeReader.UTF8;
            /*SL:106*/v4 = v2;
        }
        /*SL:109*/if (v4 > 0) {
            /*SL:110*/this.internalIn.unread(v2, v2 - v4, v4);
        }
        final CharsetDecoder v5 = /*EL:113*/v3.newDecoder().onUnmappableCharacter(CodingErrorAction.REPORT);
        /*SL:115*/this.internalIn2 = new InputStreamReader(this.internalIn, v5);
    }
    
    @Override
    public void close() throws IOException {
        /*SL:119*/this.init();
        /*SL:120*/this.internalIn2.close();
    }
    
    @Override
    public int read(final char[] a1, final int a2, final int a3) throws IOException {
        /*SL:124*/this.init();
        /*SL:125*/return this.internalIn2.read(a1, a2, a3);
    }
    
    static {
        UTF8 = Charset.forName("UTF-8");
        UTF16BE = Charset.forName("UTF-16BE");
        UTF16LE = Charset.forName("UTF-16LE");
    }
}

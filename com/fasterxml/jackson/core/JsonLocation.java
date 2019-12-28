package com.fasterxml.jackson.core;

import java.nio.charset.Charset;
import java.io.Serializable;

public class JsonLocation implements Serializable
{
    private static final long serialVersionUID = 1L;
    public static final int MAX_CONTENT_SNIPPET = 500;
    public static final JsonLocation NA;
    protected final long _totalBytes;
    protected final long _totalChars;
    protected final int _lineNr;
    protected final int _columnNr;
    final transient Object _sourceRef;
    
    public JsonLocation(final Object a1, final long a2, final int a3, final int a4) {
        this(a1, -1L, a2, a3, a4);
    }
    
    public JsonLocation(final Object a1, final long a2, final long a3, final int a4, final int a5) {
        this._sourceRef = a1;
        this._totalBytes = a2;
        this._totalChars = a3;
        this._lineNr = a4;
        this._columnNr = a5;
    }
    
    public Object getSourceRef() {
        /*SL:79*/return this._sourceRef;
    }
    
    public int getLineNr() {
        /*SL:84*/return this._lineNr;
    }
    
    public int getColumnNr() {
        /*SL:89*/return this._columnNr;
    }
    
    public long getCharOffset() {
        /*SL:95*/return this._totalChars;
    }
    
    public long getByteOffset() {
        /*SL:103*/return this._totalBytes;
    }
    
    public String sourceDescription() {
        /*SL:116*/return this._appendSourceDesc(new StringBuilder(100)).toString();
    }
    
    @Override
    public int hashCode() {
        int v1 = /*EL:128*/(this._sourceRef == null) ? 1 : this._sourceRef.hashCode();
        /*SL:129*/v1 ^= this._lineNr;
        /*SL:130*/v1 += this._columnNr;
        /*SL:131*/v1 ^= (int)this._totalChars;
        /*SL:132*/v1 += (int)this._totalBytes;
        /*SL:133*/return v1;
    }
    
    @Override
    public boolean equals(final Object a1) {
        /*SL:139*/if (a1 == this) {
            return true;
        }
        /*SL:140*/if (a1 == null) {
            return false;
        }
        /*SL:141*/if (!(a1 instanceof JsonLocation)) {
            return false;
        }
        final JsonLocation v1 = /*EL:142*/(JsonLocation)a1;
        /*SL:144*/if (this._sourceRef == null) {
            /*SL:145*/if (v1._sourceRef != null) {
                return false;
            }
        }
        else/*SL:146*/ if (!this._sourceRef.equals(v1._sourceRef)) {
            return false;
        }
        /*SL:148*/return this._lineNr == v1._lineNr && this._columnNr == v1._columnNr && this._totalChars == v1._totalChars && this.getByteOffset() == v1.getByteOffset();
    }
    
    @Override
    public String toString() {
        final StringBuilder v1 = /*EL:158*/new StringBuilder(80);
        /*SL:159*/v1.append("[Source: ");
        /*SL:160*/this._appendSourceDesc(v1);
        /*SL:161*/v1.append("; line: ");
        /*SL:162*/v1.append(this._lineNr);
        /*SL:163*/v1.append(", column: ");
        /*SL:164*/v1.append(this._columnNr);
        /*SL:165*/v1.append(']');
        /*SL:166*/return v1.toString();
    }
    
    protected StringBuilder _appendSourceDesc(final StringBuilder v-3) {
        final Object sourceRef = /*EL:171*/this._sourceRef;
        /*SL:173*/if (sourceRef == null) {
            /*SL:174*/v-3.append("UNKNOWN");
            /*SL:175*/return v-3;
        }
        final Class<?> clazz = /*EL:178*/(Class<?>)((sourceRef instanceof Class) ? ((Class)sourceRef) : sourceRef.getClass());
        String v0 = /*EL:180*/clazz.getName();
        /*SL:182*/if (v0.startsWith("java.")) {
            /*SL:183*/v0 = clazz.getSimpleName();
        }
        else/*SL:184*/ if (sourceRef instanceof byte[]) {
            /*SL:185*/v0 = "byte[]";
        }
        else/*SL:186*/ if (sourceRef instanceof char[]) {
            /*SL:187*/v0 = "char[]";
        }
        /*SL:189*/v-3.append('(').append(v0).append(')');
        String v = /*EL:192*/" chars";
        int v2;
        /*SL:194*/if (sourceRef instanceof CharSequence) {
            final CharSequence a1 = /*EL:195*/(CharSequence)sourceRef;
            /*SL:196*/v2 = a1.length();
            /*SL:197*/v2 -= this._append(v-3, a1.subSequence(0, Math.min(v2, 500)).toString());
        }
        else/*SL:198*/ if (sourceRef instanceof char[]) {
            final char[] v3 = /*EL:199*/(char[])sourceRef;
            /*SL:200*/v2 = v3.length;
            /*SL:201*/v2 -= this._append(v-3, new String(v3, 0, Math.min(v2, 500)));
        }
        else/*SL:202*/ if (sourceRef instanceof byte[]) {
            final byte[] v4 = /*EL:203*/(byte[])sourceRef;
            final int v5 = /*EL:204*/Math.min(v4.length, 500);
            /*SL:205*/this._append(v-3, new String(v4, 0, v5, Charset.forName("UTF-8")));
            /*SL:206*/v2 = v4.length - v5;
            /*SL:207*/v = " bytes";
        }
        else {
            /*SL:209*/v2 = 0;
        }
        /*SL:211*/if (v2 > 0) {
            /*SL:212*/v-3.append("[truncated ").append(v2).append(v).append(']');
        }
        /*SL:214*/return v-3;
    }
    
    private int _append(final StringBuilder a1, final String a2) {
        /*SL:218*/a1.append('\"').append(a2).append('\"');
        /*SL:219*/return a2.length();
    }
    
    static {
        NA = new JsonLocation(null, -1L, -1L, -1, -1);
    }
}

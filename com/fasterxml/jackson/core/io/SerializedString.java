package com.fasterxml.jackson.core.io;

import java.nio.ByteBuffer;
import java.io.OutputStream;
import com.fasterxml.jackson.core.util.BufferRecyclers;
import java.io.ObjectOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import com.fasterxml.jackson.core.SerializableString;

public class SerializedString implements SerializableString, Serializable
{
    private static final long serialVersionUID = 1L;
    protected final String _value;
    protected byte[] _quotedUTF8Ref;
    protected byte[] _unquotedUTF8Ref;
    protected char[] _quotedChars;
    protected transient String _jdkSerializeValue;
    
    public SerializedString(final String a1) {
        if (a1 == null) {
            throw new IllegalStateException("Null String illegal for SerializedString");
        }
        this._value = a1;
    }
    
    private void readObject(final ObjectInputStream a1) throws IOException {
        /*SL:72*/this._jdkSerializeValue = a1.readUTF();
    }
    
    private void writeObject(final ObjectOutputStream a1) throws IOException {
        /*SL:76*/a1.writeUTF(this._value);
    }
    
    protected Object readResolve() {
        /*SL:80*/return new SerializedString(this._jdkSerializeValue);
    }
    
    @Override
    public final String getValue() {
        /*SL:90*/return this._value;
    }
    
    @Override
    public final int charLength() {
        /*SL:96*/return this._value.length();
    }
    
    @Override
    public final char[] asQuotedChars() {
        char[] v1 = /*EL:100*/this._quotedChars;
        /*SL:101*/if (v1 == null) {
            /*SL:102*/v1 = BufferRecyclers.quoteAsJsonText(this._value);
            /*SL:103*/this._quotedChars = v1;
        }
        /*SL:105*/return v1;
    }
    
    @Override
    public final byte[] asUnquotedUTF8() {
        byte[] v1 = /*EL:114*/this._unquotedUTF8Ref;
        /*SL:115*/if (v1 == null) {
            /*SL:116*/v1 = BufferRecyclers.encodeAsUTF8(this._value);
            /*SL:117*/this._unquotedUTF8Ref = v1;
        }
        /*SL:119*/return v1;
    }
    
    @Override
    public final byte[] asQuotedUTF8() {
        byte[] v1 = /*EL:128*/this._quotedUTF8Ref;
        /*SL:129*/if (v1 == null) {
            /*SL:130*/v1 = BufferRecyclers.quoteAsJsonUTF8(this._value);
            /*SL:131*/this._quotedUTF8Ref = v1;
        }
        /*SL:133*/return v1;
    }
    
    @Override
    public int appendQuotedUTF8(final byte[] a1, final int a2) {
        byte[] v1 = /*EL:144*/this._quotedUTF8Ref;
        /*SL:145*/if (v1 == null) {
            /*SL:146*/v1 = BufferRecyclers.quoteAsJsonUTF8(this._value);
            /*SL:147*/this._quotedUTF8Ref = v1;
        }
        final int v2 = /*EL:149*/v1.length;
        /*SL:150*/if (a2 + v2 > a1.length) {
            /*SL:151*/return -1;
        }
        /*SL:153*/System.arraycopy(v1, 0, a1, a2, v2);
        /*SL:154*/return v2;
    }
    
    @Override
    public int appendQuoted(final char[] a1, final int a2) {
        char[] v1 = /*EL:159*/this._quotedChars;
        /*SL:160*/if (v1 == null) {
            /*SL:161*/v1 = BufferRecyclers.quoteAsJsonText(this._value);
            /*SL:162*/this._quotedChars = v1;
        }
        final int v2 = /*EL:164*/v1.length;
        /*SL:165*/if (a2 + v2 > a1.length) {
            /*SL:166*/return -1;
        }
        /*SL:168*/System.arraycopy(v1, 0, a1, a2, v2);
        /*SL:169*/return v2;
    }
    
    @Override
    public int appendUnquotedUTF8(final byte[] a1, final int a2) {
        byte[] v1 = /*EL:174*/this._unquotedUTF8Ref;
        /*SL:175*/if (v1 == null) {
            /*SL:176*/v1 = BufferRecyclers.encodeAsUTF8(this._value);
            /*SL:177*/this._unquotedUTF8Ref = v1;
        }
        final int v2 = /*EL:179*/v1.length;
        /*SL:180*/if (a2 + v2 > a1.length) {
            /*SL:181*/return -1;
        }
        /*SL:183*/System.arraycopy(v1, 0, a1, a2, v2);
        /*SL:184*/return v2;
    }
    
    @Override
    public int appendUnquoted(final char[] a1, final int a2) {
        final String v1 = /*EL:189*/this._value;
        final int v2 = /*EL:190*/v1.length();
        /*SL:191*/if (a2 + v2 > a1.length) {
            /*SL:192*/return -1;
        }
        /*SL:194*/v1.getChars(0, v2, a1, a2);
        /*SL:195*/return v2;
    }
    
    @Override
    public int writeQuotedUTF8(final OutputStream a1) throws IOException {
        byte[] v1 = /*EL:200*/this._quotedUTF8Ref;
        /*SL:201*/if (v1 == null) {
            /*SL:202*/v1 = BufferRecyclers.quoteAsJsonUTF8(this._value);
            /*SL:203*/this._quotedUTF8Ref = v1;
        }
        final int v2 = /*EL:205*/v1.length;
        /*SL:206*/a1.write(v1, 0, v2);
        /*SL:207*/return v2;
    }
    
    @Override
    public int writeUnquotedUTF8(final OutputStream a1) throws IOException {
        byte[] v1 = /*EL:212*/this._unquotedUTF8Ref;
        /*SL:213*/if (v1 == null) {
            /*SL:214*/v1 = BufferRecyclers.encodeAsUTF8(this._value);
            /*SL:215*/this._unquotedUTF8Ref = v1;
        }
        final int v2 = /*EL:217*/v1.length;
        /*SL:218*/a1.write(v1, 0, v2);
        /*SL:219*/return v2;
    }
    
    @Override
    public int putQuotedUTF8(final ByteBuffer a1) {
        byte[] v1 = /*EL:224*/this._quotedUTF8Ref;
        /*SL:225*/if (v1 == null) {
            /*SL:226*/v1 = BufferRecyclers.quoteAsJsonUTF8(this._value);
            /*SL:227*/this._quotedUTF8Ref = v1;
        }
        final int v2 = /*EL:229*/v1.length;
        /*SL:230*/if (v2 > a1.remaining()) {
            /*SL:231*/return -1;
        }
        /*SL:233*/a1.put(v1, 0, v2);
        /*SL:234*/return v2;
    }
    
    @Override
    public int putUnquotedUTF8(final ByteBuffer a1) {
        byte[] v1 = /*EL:239*/this._unquotedUTF8Ref;
        /*SL:240*/if (v1 == null) {
            /*SL:241*/v1 = BufferRecyclers.encodeAsUTF8(this._value);
            /*SL:242*/this._unquotedUTF8Ref = v1;
        }
        final int v2 = /*EL:244*/v1.length;
        /*SL:245*/if (v2 > a1.remaining()) {
            /*SL:246*/return -1;
        }
        /*SL:248*/a1.put(v1, 0, v2);
        /*SL:249*/return v2;
    }
    
    @Override
    public final String toString() {
        /*SL:260*/return this._value;
    }
    
    @Override
    public final int hashCode() {
        /*SL:263*/return this._value.hashCode();
    }
    
    @Override
    public final boolean equals(final Object a1) {
        /*SL:267*/if (a1 == this) {
            return true;
        }
        /*SL:268*/if (a1 == null || a1.getClass() != this.getClass()) {
            return false;
        }
        final SerializedString v1 = /*EL:269*/(SerializedString)a1;
        /*SL:270*/return this._value.equals(v1._value);
    }
}

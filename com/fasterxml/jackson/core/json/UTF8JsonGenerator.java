package com.fasterxml.jackson.core.json;

import com.fasterxml.jackson.core.io.CharTypes;
import com.fasterxml.jackson.core.io.CharacterEscapes;
import com.fasterxml.jackson.core.JsonStreamContext;
import java.math.BigDecimal;
import java.math.BigInteger;
import com.fasterxml.jackson.core.io.NumberOutput;
import java.io.InputStream;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.Base64Variant;
import java.io.Reader;
import com.fasterxml.jackson.core.SerializableString;
import java.io.IOException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.core.io.IOContext;
import java.io.OutputStream;

public class UTF8JsonGenerator extends JsonGeneratorImpl
{
    private static final byte BYTE_u = 117;
    private static final byte BYTE_0 = 48;
    private static final byte BYTE_LBRACKET = 91;
    private static final byte BYTE_RBRACKET = 93;
    private static final byte BYTE_LCURLY = 123;
    private static final byte BYTE_RCURLY = 125;
    private static final byte BYTE_BACKSLASH = 92;
    private static final byte BYTE_COMMA = 44;
    private static final byte BYTE_COLON = 58;
    private static final int MAX_BYTES_TO_BUFFER = 512;
    private static final byte[] HEX_CHARS;
    private static final byte[] NULL_BYTES;
    private static final byte[] TRUE_BYTES;
    private static final byte[] FALSE_BYTES;
    protected final OutputStream _outputStream;
    protected byte _quoteChar;
    protected byte[] _outputBuffer;
    protected int _outputTail;
    protected final int _outputEnd;
    protected final int _outputMaxContiguous;
    protected char[] _charBuffer;
    protected final int _charBufferLength;
    protected byte[] _entityBuffer;
    protected boolean _bufferRecyclable;
    
    public UTF8JsonGenerator(final IOContext a1, final int a2, final ObjectCodec a3, final OutputStream a4) {
        super(a1, a2, a3);
        this._quoteChar = 34;
        this._outputStream = a4;
        this._bufferRecyclable = true;
        this._outputBuffer = a1.allocWriteEncodingBuffer();
        this._outputEnd = this._outputBuffer.length;
        this._outputMaxContiguous = this._outputEnd >> 3;
        this._charBuffer = a1.allocConcatBuffer();
        this._charBufferLength = this._charBuffer.length;
        if (this.isEnabled(Feature.ESCAPE_NON_ASCII)) {
            this.setHighestNonEscapedChar(127);
        }
    }
    
    public UTF8JsonGenerator(final IOContext a1, final int a2, final ObjectCodec a3, final OutputStream a4, final byte[] a5, final int a6, final boolean a7) {
        super(a1, a2, a3);
        this._quoteChar = 34;
        this._outputStream = a4;
        this._bufferRecyclable = a7;
        this._outputTail = a6;
        this._outputBuffer = a5;
        this._outputEnd = this._outputBuffer.length;
        this._outputMaxContiguous = this._outputEnd >> 3;
        this._charBuffer = a1.allocConcatBuffer();
        this._charBufferLength = this._charBuffer.length;
    }
    
    @Override
    public Object getOutputTarget() {
        /*SL:164*/return this._outputStream;
    }
    
    @Override
    public int getOutputBuffered() {
        /*SL:170*/return this._outputTail;
    }
    
    @Override
    public void writeFieldName(final String a1) throws IOException {
        /*SL:182*/if (this._cfgPrettyPrinter != null) {
            /*SL:183*/this._writePPFieldName(a1);
            /*SL:184*/return;
        }
        final int v1 = /*EL:186*/this._writeContext.writeFieldName(a1);
        /*SL:187*/if (v1 == 4) {
            /*SL:188*/this._reportError("Can not write a field name, expecting a value");
        }
        /*SL:190*/if (v1 == 1) {
            /*SL:191*/if (this._outputTail >= this._outputEnd) {
                /*SL:192*/this._flushBuffer();
            }
            /*SL:194*/this._outputBuffer[this._outputTail++] = 44;
        }
        /*SL:199*/if (this._cfgUnqNames) {
            /*SL:200*/this._writeStringSegments(a1, false);
            /*SL:201*/return;
        }
        final int v2 = /*EL:203*/a1.length();
        /*SL:205*/if (v2 > this._charBufferLength) {
            /*SL:206*/this._writeStringSegments(a1, true);
            /*SL:207*/return;
        }
        /*SL:209*/if (this._outputTail >= this._outputEnd) {
            /*SL:210*/this._flushBuffer();
        }
        /*SL:212*/this._outputBuffer[this._outputTail++] = this._quoteChar;
        /*SL:214*/if (v2 <= this._outputMaxContiguous) {
            /*SL:215*/if (this._outputTail + v2 > this._outputEnd) {
                /*SL:216*/this._flushBuffer();
            }
            /*SL:218*/this._writeStringSegment(a1, 0, v2);
        }
        else {
            /*SL:220*/this._writeStringSegments(a1, 0, v2);
        }
        /*SL:223*/if (this._outputTail >= this._outputEnd) {
            /*SL:224*/this._flushBuffer();
        }
        /*SL:226*/this._outputBuffer[this._outputTail++] = this._quoteChar;
    }
    
    @Override
    public void writeFieldName(final SerializableString a1) throws IOException {
        /*SL:232*/if (this._cfgPrettyPrinter != null) {
            /*SL:233*/this._writePPFieldName(a1);
            /*SL:234*/return;
        }
        final int v1 = /*EL:236*/this._writeContext.writeFieldName(a1.getValue());
        /*SL:237*/if (v1 == 4) {
            /*SL:238*/this._reportError("Can not write a field name, expecting a value");
        }
        /*SL:240*/if (v1 == 1) {
            /*SL:241*/if (this._outputTail >= this._outputEnd) {
                /*SL:242*/this._flushBuffer();
            }
            /*SL:244*/this._outputBuffer[this._outputTail++] = 44;
        }
        /*SL:246*/if (this._cfgUnqNames) {
            /*SL:247*/this._writeUnq(a1);
            /*SL:248*/return;
        }
        /*SL:250*/if (this._outputTail >= this._outputEnd) {
            /*SL:251*/this._flushBuffer();
        }
        /*SL:253*/this._outputBuffer[this._outputTail++] = this._quoteChar;
        final int v2 = /*EL:254*/a1.appendQuotedUTF8(this._outputBuffer, this._outputTail);
        /*SL:255*/if (v2 < 0) {
            /*SL:256*/this._writeBytes(a1.asQuotedUTF8());
        }
        else {
            /*SL:258*/this._outputTail += v2;
        }
        /*SL:260*/if (this._outputTail >= this._outputEnd) {
            /*SL:261*/this._flushBuffer();
        }
        /*SL:263*/this._outputBuffer[this._outputTail++] = this._quoteChar;
    }
    
    private final void _writeUnq(final SerializableString a1) throws IOException {
        final int v1 = /*EL:267*/a1.appendQuotedUTF8(this._outputBuffer, this._outputTail);
        /*SL:268*/if (v1 < 0) {
            /*SL:269*/this._writeBytes(a1.asQuotedUTF8());
        }
        else {
            /*SL:271*/this._outputTail += v1;
        }
    }
    
    @Override
    public final void writeStartArray() throws IOException {
        /*SL:284*/this._verifyValueWrite("start an array");
        /*SL:285*/this._writeContext = this._writeContext.createChildArrayContext();
        /*SL:286*/if (this._cfgPrettyPrinter != null) {
            /*SL:287*/this._cfgPrettyPrinter.writeStartArray(this);
        }
        else {
            /*SL:289*/if (this._outputTail >= this._outputEnd) {
                /*SL:290*/this._flushBuffer();
            }
            /*SL:292*/this._outputBuffer[this._outputTail++] = 91;
        }
    }
    
    @Override
    public final void writeEndArray() throws IOException {
        /*SL:299*/if (!this._writeContext.inArray()) {
            /*SL:300*/this._reportError("Current context not Array but " + this._writeContext.typeDesc());
        }
        /*SL:302*/if (this._cfgPrettyPrinter != null) {
            /*SL:303*/this._cfgPrettyPrinter.writeEndArray(this, this._writeContext.getEntryCount());
        }
        else {
            /*SL:305*/if (this._outputTail >= this._outputEnd) {
                /*SL:306*/this._flushBuffer();
            }
            /*SL:308*/this._outputBuffer[this._outputTail++] = 93;
        }
        /*SL:310*/this._writeContext = this._writeContext.clearAndGetParent();
    }
    
    @Override
    public final void writeStartObject() throws IOException {
        /*SL:316*/this._verifyValueWrite("start an object");
        /*SL:317*/this._writeContext = this._writeContext.createChildObjectContext();
        /*SL:318*/if (this._cfgPrettyPrinter != null) {
            /*SL:319*/this._cfgPrettyPrinter.writeStartObject(this);
        }
        else {
            /*SL:321*/if (this._outputTail >= this._outputEnd) {
                /*SL:322*/this._flushBuffer();
            }
            /*SL:324*/this._outputBuffer[this._outputTail++] = 123;
        }
    }
    
    @Override
    public void writeStartObject(final Object a1) throws IOException {
        /*SL:331*/this._verifyValueWrite("start an object");
        final JsonWriteContext v1 = /*EL:332*/this._writeContext.createChildObjectContext();
        /*SL:333*/this._writeContext = v1;
        /*SL:334*/if (a1 != null) {
            /*SL:335*/v1.setCurrentValue(a1);
        }
        /*SL:337*/if (this._cfgPrettyPrinter != null) {
            /*SL:338*/this._cfgPrettyPrinter.writeStartObject(this);
        }
        else {
            /*SL:340*/if (this._outputTail >= this._outputEnd) {
                /*SL:341*/this._flushBuffer();
            }
            /*SL:343*/this._outputBuffer[this._outputTail++] = 123;
        }
    }
    
    @Override
    public final void writeEndObject() throws IOException {
        /*SL:350*/if (!this._writeContext.inObject()) {
            /*SL:351*/this._reportError("Current context not Object but " + this._writeContext.typeDesc());
        }
        /*SL:353*/if (this._cfgPrettyPrinter != null) {
            /*SL:354*/this._cfgPrettyPrinter.writeEndObject(this, this._writeContext.getEntryCount());
        }
        else {
            /*SL:356*/if (this._outputTail >= this._outputEnd) {
                /*SL:357*/this._flushBuffer();
            }
            /*SL:359*/this._outputBuffer[this._outputTail++] = 125;
        }
        /*SL:361*/this._writeContext = this._writeContext.clearAndGetParent();
    }
    
    protected final void _writePPFieldName(final String a1) throws IOException {
        final int v1 = /*EL:370*/this._writeContext.writeFieldName(a1);
        /*SL:371*/if (v1 == 4) {
            /*SL:372*/this._reportError("Can not write a field name, expecting a value");
        }
        /*SL:374*/if (v1 == 1) {
            /*SL:375*/this._cfgPrettyPrinter.writeObjectEntrySeparator(this);
        }
        else {
            /*SL:377*/this._cfgPrettyPrinter.beforeObjectEntries(this);
        }
        /*SL:379*/if (this._cfgUnqNames) {
            /*SL:380*/this._writeStringSegments(a1, false);
            /*SL:381*/return;
        }
        final int v2 = /*EL:383*/a1.length();
        /*SL:384*/if (v2 > this._charBufferLength) {
            /*SL:385*/this._writeStringSegments(a1, true);
            /*SL:386*/return;
        }
        /*SL:388*/if (this._outputTail >= this._outputEnd) {
            /*SL:389*/this._flushBuffer();
        }
        /*SL:391*/this._outputBuffer[this._outputTail++] = this._quoteChar;
        /*SL:392*/a1.getChars(0, v2, this._charBuffer, 0);
        /*SL:394*/if (v2 <= this._outputMaxContiguous) {
            /*SL:395*/if (this._outputTail + v2 > this._outputEnd) {
                /*SL:396*/this._flushBuffer();
            }
            /*SL:398*/this._writeStringSegment(this._charBuffer, 0, v2);
        }
        else {
            /*SL:400*/this._writeStringSegments(this._charBuffer, 0, v2);
        }
        /*SL:402*/if (this._outputTail >= this._outputEnd) {
            /*SL:403*/this._flushBuffer();
        }
        /*SL:405*/this._outputBuffer[this._outputTail++] = this._quoteChar;
    }
    
    protected final void _writePPFieldName(final SerializableString a1) throws IOException {
        final int v1 = /*EL:410*/this._writeContext.writeFieldName(a1.getValue());
        /*SL:411*/if (v1 == 4) {
            /*SL:412*/this._reportError("Can not write a field name, expecting a value");
        }
        /*SL:414*/if (v1 == 1) {
            /*SL:415*/this._cfgPrettyPrinter.writeObjectEntrySeparator(this);
        }
        else {
            /*SL:417*/this._cfgPrettyPrinter.beforeObjectEntries(this);
        }
        final boolean v2 = /*EL:420*/!this._cfgUnqNames;
        /*SL:421*/if (v2) {
            /*SL:422*/if (this._outputTail >= this._outputEnd) {
                /*SL:423*/this._flushBuffer();
            }
            /*SL:425*/this._outputBuffer[this._outputTail++] = this._quoteChar;
        }
        /*SL:427*/this._writeBytes(a1.asQuotedUTF8());
        /*SL:428*/if (v2) {
            /*SL:429*/if (this._outputTail >= this._outputEnd) {
                /*SL:430*/this._flushBuffer();
            }
            /*SL:432*/this._outputBuffer[this._outputTail++] = this._quoteChar;
        }
    }
    
    @Override
    public void writeString(final String a1) throws IOException {
        /*SL:445*/this._verifyValueWrite("write a string");
        /*SL:446*/if (a1 == null) {
            /*SL:447*/this._writeNull();
            /*SL:448*/return;
        }
        final int v1 = /*EL:451*/a1.length();
        /*SL:452*/if (v1 > this._outputMaxContiguous) {
            /*SL:453*/this._writeStringSegments(a1, true);
            /*SL:454*/return;
        }
        /*SL:456*/if (this._outputTail + v1 >= this._outputEnd) {
            /*SL:457*/this._flushBuffer();
        }
        /*SL:459*/this._outputBuffer[this._outputTail++] = this._quoteChar;
        /*SL:460*/this._writeStringSegment(a1, 0, v1);
        /*SL:461*/if (this._outputTail >= this._outputEnd) {
            /*SL:462*/this._flushBuffer();
        }
        /*SL:464*/this._outputBuffer[this._outputTail++] = this._quoteChar;
    }
    
    @Override
    public void writeString(final Reader v2, final int v3) throws IOException {
        /*SL:469*/this._verifyValueWrite("write a string");
        /*SL:470*/if (v2 == null) {
            /*SL:471*/this._reportError("null reader");
        }
        int v4 = /*EL:474*/(v3 >= 0) ? v3 : Integer.MAX_VALUE;
        final char[] v5 = /*EL:476*/this._charBuffer;
        /*SL:479*/if (this._outputTail + v3 >= this._outputEnd) {
            /*SL:480*/this._flushBuffer();
        }
        /*SL:482*/this._outputBuffer[this._outputTail++] = this._quoteChar;
        /*SL:485*/while (v4 > 0) {
            final int a1 = /*EL:486*/Math.min(v4, v5.length);
            final int a2 = /*EL:487*/v2.read(v5, 0, a1);
            /*SL:488*/if (a2 <= 0) {
                /*SL:489*/break;
            }
            /*SL:491*/if (this._outputTail + v3 >= this._outputEnd) {
                /*SL:492*/this._flushBuffer();
            }
            /*SL:494*/this._writeStringSegments(v5, 0, a2);
            /*SL:496*/v4 -= a2;
        }
        /*SL:500*/if (this._outputTail + v3 >= this._outputEnd) {
            /*SL:501*/this._flushBuffer();
        }
        /*SL:503*/this._outputBuffer[this._outputTail++] = this._quoteChar;
        /*SL:505*/if (v4 > 0 && v3 >= 0) {
            /*SL:506*/this._reportError("Didn't read enough from reader");
        }
    }
    
    @Override
    public void writeString(final char[] a1, final int a2, final int a3) throws IOException {
        /*SL:513*/this._verifyValueWrite("write a string");
        /*SL:514*/if (this._outputTail >= this._outputEnd) {
            /*SL:515*/this._flushBuffer();
        }
        /*SL:517*/this._outputBuffer[this._outputTail++] = this._quoteChar;
        /*SL:519*/if (a3 <= this._outputMaxContiguous) {
            /*SL:520*/if (this._outputTail + a3 > this._outputEnd) {
                /*SL:521*/this._flushBuffer();
            }
            /*SL:523*/this._writeStringSegment(a1, a2, a3);
        }
        else {
            /*SL:525*/this._writeStringSegments(a1, a2, a3);
        }
        /*SL:528*/if (this._outputTail >= this._outputEnd) {
            /*SL:529*/this._flushBuffer();
        }
        /*SL:531*/this._outputBuffer[this._outputTail++] = this._quoteChar;
    }
    
    @Override
    public final void writeString(final SerializableString a1) throws IOException {
        /*SL:537*/this._verifyValueWrite("write a string");
        /*SL:538*/if (this._outputTail >= this._outputEnd) {
            /*SL:539*/this._flushBuffer();
        }
        /*SL:541*/this._outputBuffer[this._outputTail++] = this._quoteChar;
        final int v1 = /*EL:542*/a1.appendQuotedUTF8(this._outputBuffer, this._outputTail);
        /*SL:543*/if (v1 < 0) {
            /*SL:544*/this._writeBytes(a1.asQuotedUTF8());
        }
        else {
            /*SL:546*/this._outputTail += v1;
        }
        /*SL:548*/if (this._outputTail >= this._outputEnd) {
            /*SL:549*/this._flushBuffer();
        }
        /*SL:551*/this._outputBuffer[this._outputTail++] = this._quoteChar;
    }
    
    @Override
    public void writeRawUTF8String(final byte[] a1, final int a2, final int a3) throws IOException {
        /*SL:557*/this._verifyValueWrite("write a string");
        /*SL:558*/if (this._outputTail >= this._outputEnd) {
            /*SL:559*/this._flushBuffer();
        }
        /*SL:561*/this._outputBuffer[this._outputTail++] = this._quoteChar;
        /*SL:562*/this._writeBytes(a1, a2, a3);
        /*SL:563*/if (this._outputTail >= this._outputEnd) {
            /*SL:564*/this._flushBuffer();
        }
        /*SL:566*/this._outputBuffer[this._outputTail++] = this._quoteChar;
    }
    
    @Override
    public void writeUTF8String(final byte[] a1, final int a2, final int a3) throws IOException {
        /*SL:572*/this._verifyValueWrite("write a string");
        /*SL:573*/if (this._outputTail >= this._outputEnd) {
            /*SL:574*/this._flushBuffer();
        }
        /*SL:576*/this._outputBuffer[this._outputTail++] = this._quoteChar;
        /*SL:578*/if (a3 <= this._outputMaxContiguous) {
            /*SL:579*/this._writeUTF8Segment(a1, a2, a3);
        }
        else {
            /*SL:581*/this._writeUTF8Segments(a1, a2, a3);
        }
        /*SL:583*/if (this._outputTail >= this._outputEnd) {
            /*SL:584*/this._flushBuffer();
        }
        /*SL:586*/this._outputBuffer[this._outputTail++] = this._quoteChar;
    }
    
    @Override
    public void writeRaw(final String a1) throws IOException {
        final int v1 = /*EL:597*/a1.length();
        final char[] v2 = /*EL:598*/this._charBuffer;
        /*SL:599*/if (v1 <= v2.length) {
            /*SL:600*/a1.getChars(0, v1, v2, 0);
            /*SL:601*/this.writeRaw(v2, 0, v1);
        }
        else {
            /*SL:603*/this.writeRaw(a1, 0, v1);
        }
    }
    
    @Override
    public void writeRaw(final String v1, int v2, int v3) throws IOException {
        final char[] v4 = /*EL:610*/this._charBuffer;
        final int v5 = /*EL:611*/v4.length;
        /*SL:614*/if (v3 <= v5) {
            /*SL:615*/v1.getChars(v2, v2 + v3, v4, 0);
            /*SL:616*/this.writeRaw(v4, 0, v3);
            /*SL:617*/return;
        }
        final int v6 = /*EL:623*/Math.min(v5, (this._outputEnd >> 2) + (this._outputEnd >> 4));
        final int v7 = /*EL:625*/v6 * 3;
        /*SL:627*/while (v3 > 0) {
            int a2 = /*EL:628*/Math.min(v6, v3);
            /*SL:629*/v1.getChars(v2, v2 + a2, v4, 0);
            /*SL:630*/if (this._outputTail + v7 > this._outputEnd) {
                /*SL:631*/this._flushBuffer();
            }
            /*SL:639*/if (a2 > 1) {
                /*SL:640*/a2 = v4[a2 - 1];
                /*SL:641*/if (a2 >= '\ud800' && a2 <= '\udbff') {
                    /*SL:642*/--a2;
                }
            }
            /*SL:645*/this._writeRawSegment(v4, 0, a2);
            /*SL:646*/v2 += a2;
            /*SL:647*/v3 -= a2;
        }
    }
    
    @Override
    public void writeRaw(final SerializableString a1) throws IOException {
        final byte[] v1 = /*EL:654*/a1.asUnquotedUTF8();
        /*SL:655*/if (v1.length > 0) {
            /*SL:656*/this._writeBytes(v1);
        }
    }
    
    @Override
    public void writeRawValue(final SerializableString a1) throws IOException {
        /*SL:663*/this._verifyValueWrite("write a raw (unencoded) value");
        final byte[] v1 = /*EL:664*/a1.asUnquotedUTF8();
        /*SL:665*/if (v1.length > 0) {
            /*SL:666*/this._writeBytes(v1);
        }
    }
    
    @Override
    public final void writeRaw(final char[] v2, int v3, int v4) throws IOException {
        final int a1 = /*EL:676*/v4 + v4 + v4;
        /*SL:677*/if (this._outputTail + a1 > this._outputEnd) {
            /*SL:679*/if (this._outputEnd < a1) {
                /*SL:680*/this._writeSegmentedRaw(v2, v3, v4);
                /*SL:681*/return;
            }
            /*SL:684*/this._flushBuffer();
        }
        /*SL:687*/v4 += v3;
        /*SL:691*/Label_0183:
        while (v3 < v4) {
            while (true) {
                final int a2 = /*EL:694*/v2[v3];
                /*SL:695*/if (a2 > 127) {
                    final char a3 = /*EL:703*/v2[v3++];
                    /*SL:704*/if (a3 < '\u0800') {
                        /*SL:705*/this._outputBuffer[this._outputTail++] = (byte)('\u00c0' | a3 >> 6);
                        /*SL:706*/this._outputBuffer[this._outputTail++] = (byte)('\u0080' | (a3 & '?'));
                    }
                    else {
                        /*SL:708*/v3 = this._outputRawMultiByteChar(a3, v2, v3, v4);
                    }
                    /*SL:710*/break;
                }
                this._outputBuffer[this._outputTail++] = (byte)a2;
                if (++v3 >= v4) {
                    break Label_0183;
                }
            }
        }
    }
    
    @Override
    public void writeRaw(final char a1) throws IOException {
        /*SL:716*/if (this._outputTail + 3 >= this._outputEnd) {
            /*SL:717*/this._flushBuffer();
        }
        final byte[] v1 = /*EL:719*/this._outputBuffer;
        /*SL:720*/if (a1 <= '\u007f') {
            /*SL:721*/v1[this._outputTail++] = (byte)a1;
        }
        else/*SL:722*/ if (a1 < '\u0800') {
            /*SL:723*/v1[this._outputTail++] = (byte)('\u00c0' | a1 >> 6);
            /*SL:724*/v1[this._outputTail++] = (byte)('\u0080' | (a1 & '?'));
        }
        else {
            /*SL:726*/this._outputRawMultiByteChar(a1, null, 0, 0);
        }
    }
    
    private final void _writeSegmentedRaw(final char[] v1, int v2, final int v3) throws IOException {
        final int v4 = /*EL:736*/this._outputEnd;
        final byte[] v5 = /*EL:737*/this._outputBuffer;
        final int v6 = /*EL:738*/v2 + v3;
        /*SL:741*/Label_0182:
        while (v2 < v6) {
            while (true) {
                final int a1 = /*EL:744*/v1[v2];
                /*SL:745*/if (a1 >= 128) {
                    /*SL:757*/if (this._outputTail + 3 >= this._outputEnd) {
                        /*SL:758*/this._flushBuffer();
                    }
                    final char a2 = /*EL:760*/v1[v2++];
                    /*SL:761*/if (a2 < '\u0800') {
                        /*SL:762*/v5[this._outputTail++] = (byte)('\u00c0' | a2 >> 6);
                        /*SL:763*/v5[this._outputTail++] = (byte)('\u0080' | (a2 & '?'));
                    }
                    else {
                        /*SL:765*/v2 = this._outputRawMultiByteChar(a2, v1, v2, v6);
                    }
                    /*SL:767*/break;
                }
                if (this._outputTail >= v4) {
                    this._flushBuffer();
                }
                v5[this._outputTail++] = (byte)a1;
                if (++v2 >= v6) {
                    break Label_0182;
                }
            }
        }
    }
    
    private void _writeRawSegment(final char[] v1, int v2, final int v3) throws IOException {
        /*SL:782*/Label_0137:
        while (v2 < v3) {
            while (true) {
                final int a1 = /*EL:785*/v1[v2];
                /*SL:786*/if (a1 > 127) {
                    final char a2 = /*EL:794*/v1[v2++];
                    /*SL:795*/if (a2 < '\u0800') {
                        /*SL:796*/this._outputBuffer[this._outputTail++] = (byte)('\u00c0' | a2 >> 6);
                        /*SL:797*/this._outputBuffer[this._outputTail++] = (byte)('\u0080' | (a2 & '?'));
                    }
                    else {
                        /*SL:799*/v2 = this._outputRawMultiByteChar(a2, v1, v2, v3);
                    }
                    /*SL:801*/break;
                }
                this._outputBuffer[this._outputTail++] = (byte)a1;
                if (++v2 >= v3) {
                    break Label_0137;
                }
            }
        }
    }
    
    @Override
    public void writeBinary(final Base64Variant a1, final byte[] a2, final int a3, final int a4) throws IOException, JsonGenerationException {
        /*SL:815*/this._verifyValueWrite("write a binary value");
        /*SL:817*/if (this._outputTail >= this._outputEnd) {
            /*SL:818*/this._flushBuffer();
        }
        /*SL:820*/this._outputBuffer[this._outputTail++] = this._quoteChar;
        /*SL:821*/this._writeBinary(a1, a2, a3, a3 + a4);
        /*SL:823*/if (this._outputTail >= this._outputEnd) {
            /*SL:824*/this._flushBuffer();
        }
        /*SL:826*/this._outputBuffer[this._outputTail++] = this._quoteChar;
    }
    
    @Override
    public int writeBinary(final Base64Variant v1, final InputStream v2, final int v3) throws IOException, JsonGenerationException {
        /*SL:834*/this._verifyValueWrite("write a binary value");
        /*SL:836*/if (this._outputTail >= this._outputEnd) {
            /*SL:837*/this._flushBuffer();
        }
        /*SL:839*/this._outputBuffer[this._outputTail++] = this._quoteChar;
        final byte[] v4 = /*EL:840*/this._ioContext.allocBase64Buffer();
        try {
            /*SL:843*/if (v3 < 0) {
                final int a1 = /*EL:844*/this._writeBinary(v1, v2, v4);
            }
            else {
                final int a2 = /*EL:846*/this._writeBinary(v1, v2, v4, v3);
                /*SL:847*/if (a2 > 0) {
                    /*SL:848*/this._reportError("Too few bytes available: missing " + a2 + " bytes (out of " + v3 + ")");
                }
            }
        }
        finally {
            /*SL:853*/this._ioContext.releaseBase64Buffer(v4);
        }
        /*SL:856*/if (this._outputTail >= this._outputEnd) {
            /*SL:857*/this._flushBuffer();
        }
        /*SL:859*/this._outputBuffer[this._outputTail++] = this._quoteChar;
        /*SL:860*/return v3;
    }
    
    @Override
    public void writeNumber(final short a1) throws IOException {
        /*SL:872*/this._verifyValueWrite("write a number");
        /*SL:874*/if (this._outputTail + 6 >= this._outputEnd) {
            /*SL:875*/this._flushBuffer();
        }
        /*SL:877*/if (this._cfgNumbersAsStrings) {
            /*SL:878*/this._writeQuotedShort(a1);
            /*SL:879*/return;
        }
        /*SL:881*/this._outputTail = NumberOutput.outputInt(a1, this._outputBuffer, this._outputTail);
    }
    
    private final void _writeQuotedShort(final short a1) throws IOException {
        /*SL:885*/if (this._outputTail + 8 >= this._outputEnd) {
            /*SL:886*/this._flushBuffer();
        }
        /*SL:888*/this._outputBuffer[this._outputTail++] = this._quoteChar;
        /*SL:889*/this._outputTail = NumberOutput.outputInt(a1, this._outputBuffer, this._outputTail);
        /*SL:890*/this._outputBuffer[this._outputTail++] = this._quoteChar;
    }
    
    @Override
    public void writeNumber(final int a1) throws IOException {
        /*SL:896*/this._verifyValueWrite("write a number");
        /*SL:898*/if (this._outputTail + 11 >= this._outputEnd) {
            /*SL:899*/this._flushBuffer();
        }
        /*SL:901*/if (this._cfgNumbersAsStrings) {
            /*SL:902*/this._writeQuotedInt(a1);
            /*SL:903*/return;
        }
        /*SL:905*/this._outputTail = NumberOutput.outputInt(a1, this._outputBuffer, this._outputTail);
    }
    
    private final void _writeQuotedInt(final int a1) throws IOException {
        /*SL:910*/if (this._outputTail + 13 >= this._outputEnd) {
            /*SL:911*/this._flushBuffer();
        }
        /*SL:913*/this._outputBuffer[this._outputTail++] = this._quoteChar;
        /*SL:914*/this._outputTail = NumberOutput.outputInt(a1, this._outputBuffer, this._outputTail);
        /*SL:915*/this._outputBuffer[this._outputTail++] = this._quoteChar;
    }
    
    @Override
    public void writeNumber(final long a1) throws IOException {
        /*SL:921*/this._verifyValueWrite("write a number");
        /*SL:922*/if (this._cfgNumbersAsStrings) {
            /*SL:923*/this._writeQuotedLong(a1);
            /*SL:924*/return;
        }
        /*SL:926*/if (this._outputTail + 21 >= this._outputEnd) {
            /*SL:928*/this._flushBuffer();
        }
        /*SL:930*/this._outputTail = NumberOutput.outputLong(a1, this._outputBuffer, this._outputTail);
    }
    
    private final void _writeQuotedLong(final long a1) throws IOException {
        /*SL:935*/if (this._outputTail + 23 >= this._outputEnd) {
            /*SL:936*/this._flushBuffer();
        }
        /*SL:938*/this._outputBuffer[this._outputTail++] = this._quoteChar;
        /*SL:939*/this._outputTail = NumberOutput.outputLong(a1, this._outputBuffer, this._outputTail);
        /*SL:940*/this._outputBuffer[this._outputTail++] = this._quoteChar;
    }
    
    @Override
    public void writeNumber(final BigInteger a1) throws IOException {
        /*SL:946*/this._verifyValueWrite("write a number");
        /*SL:947*/if (a1 == null) {
            /*SL:948*/this._writeNull();
        }
        else/*SL:949*/ if (this._cfgNumbersAsStrings) {
            /*SL:950*/this._writeQuotedRaw(a1.toString());
        }
        else {
            /*SL:952*/this.writeRaw(a1.toString());
        }
    }
    
    @Override
    public void writeNumber(final double a1) throws IOException {
        /*SL:960*/if (this._cfgNumbersAsStrings || ((Double.isNaN(a1) || Double.isInfinite(a1)) && Feature.QUOTE_NON_NUMERIC_NUMBERS.enabledIn(this._features))) {
            /*SL:963*/this.writeString(String.valueOf(a1));
            /*SL:964*/return;
        }
        /*SL:967*/this._verifyValueWrite("write a number");
        /*SL:968*/this.writeRaw(String.valueOf(a1));
    }
    
    @Override
    public void writeNumber(final float a1) throws IOException {
        /*SL:974*/if (this._cfgNumbersAsStrings || ((Float.isNaN(a1) || Float.isInfinite(a1)) && Feature.QUOTE_NON_NUMERIC_NUMBERS.enabledIn(this._features))) {
            /*SL:978*/this.writeString(String.valueOf(a1));
            /*SL:979*/return;
        }
        /*SL:982*/this._verifyValueWrite("write a number");
        /*SL:983*/this.writeRaw(String.valueOf(a1));
    }
    
    @Override
    public void writeNumber(final BigDecimal a1) throws IOException {
        /*SL:990*/this._verifyValueWrite("write a number");
        /*SL:991*/if (a1 == null) {
            /*SL:992*/this._writeNull();
        }
        else/*SL:993*/ if (this._cfgNumbersAsStrings) {
            /*SL:994*/this._writeQuotedRaw(this._asString(a1));
        }
        else {
            /*SL:996*/this.writeRaw(this._asString(a1));
        }
    }
    
    @Override
    public void writeNumber(final String a1) throws IOException {
        /*SL:1003*/this._verifyValueWrite("write a number");
        /*SL:1004*/if (this._cfgNumbersAsStrings) {
            /*SL:1005*/this._writeQuotedRaw(a1);
        }
        else {
            /*SL:1007*/this.writeRaw(a1);
        }
    }
    
    private final void _writeQuotedRaw(final String a1) throws IOException {
        /*SL:1013*/if (this._outputTail >= this._outputEnd) {
            /*SL:1014*/this._flushBuffer();
        }
        /*SL:1016*/this._outputBuffer[this._outputTail++] = this._quoteChar;
        /*SL:1017*/this.writeRaw(a1);
        /*SL:1018*/if (this._outputTail >= this._outputEnd) {
            /*SL:1019*/this._flushBuffer();
        }
        /*SL:1021*/this._outputBuffer[this._outputTail++] = this._quoteChar;
    }
    
    @Override
    public void writeBoolean(final boolean a1) throws IOException {
        /*SL:1027*/this._verifyValueWrite("write a boolean value");
        /*SL:1028*/if (this._outputTail + 5 >= this._outputEnd) {
            /*SL:1029*/this._flushBuffer();
        }
        final byte[] v1 = /*EL:1031*/a1 ? UTF8JsonGenerator.TRUE_BYTES : UTF8JsonGenerator.FALSE_BYTES;
        final int v2 = /*EL:1032*/v1.length;
        /*SL:1033*/System.arraycopy(v1, 0, this._outputBuffer, this._outputTail, v2);
        /*SL:1034*/this._outputTail += v2;
    }
    
    @Override
    public void writeNull() throws IOException {
        /*SL:1040*/this._verifyValueWrite("write a null");
        /*SL:1041*/this._writeNull();
    }
    
    @Override
    protected final void _verifyValueWrite(final String v2) throws IOException {
        final int v3 = /*EL:1053*/this._writeContext.writeValue();
        /*SL:1054*/if (this._cfgPrettyPrinter != null) {
            /*SL:1056*/this._verifyPrettyValueWrite(v2, v3);
            /*SL:1057*/return;
        }
        byte v4 = 0;
        /*SL:1060*/switch (v3) {
            default: {
                /*SL:1063*/return;
            }
            case 1: {
                /*SL:1065*/v4 = 44;
                /*SL:1066*/break;
            }
            case 2: {
                /*SL:1068*/v4 = 58;
                /*SL:1069*/break;
            }
            case 3: {
                /*SL:1071*/if (this._rootValueSeparator != null) {
                    final byte[] a1 = /*EL:1072*/this._rootValueSeparator.asUnquotedUTF8();
                    /*SL:1073*/if (a1.length > 0) {
                        /*SL:1074*/this._writeBytes(a1);
                    }
                }
                /*SL:1077*/return;
            }
            case 5: {
                /*SL:1079*/this._reportCantWriteValueExpectName(v2);
                /*SL:1080*/return;
            }
        }
        /*SL:1082*/if (this._outputTail >= this._outputEnd) {
            /*SL:1083*/this._flushBuffer();
        }
        /*SL:1085*/this._outputBuffer[this._outputTail++] = v4;
    }
    
    @Override
    public void flush() throws IOException {
        /*SL:1097*/this._flushBuffer();
        /*SL:1098*/if (this._outputStream != null && /*EL:1099*/this.isEnabled(Feature.FLUSH_PASSED_TO_STREAM)) {
            /*SL:1100*/this._outputStream.flush();
        }
    }
    
    @Override
    public void close() throws IOException {
        /*SL:1108*/super.close();
        /*SL:1114*/if (this._outputBuffer != null && this.isEnabled(Feature.AUTO_CLOSE_JSON_CONTENT)) {
            while (true) {
                final JsonStreamContext v1 = /*EL:1117*/this.getOutputContext();
                /*SL:1118*/if (v1.inArray()) {
                    /*SL:1119*/this.writeEndArray();
                }
                else {
                    /*SL:1120*/if (!v1.inObject()) {
                        break;
                    }
                    /*SL:1121*/this.writeEndObject();
                }
            }
        }
        /*SL:1127*/this._flushBuffer();
        /*SL:1128*/this._outputTail = 0;
        /*SL:1136*/if (this._outputStream != null) {
            /*SL:1137*/if (this._ioContext.isResourceManaged() || this.isEnabled(Feature.AUTO_CLOSE_TARGET)) {
                /*SL:1138*/this._outputStream.close();
            }
            else/*SL:1139*/ if (this.isEnabled(Feature.FLUSH_PASSED_TO_STREAM)) {
                /*SL:1141*/this._outputStream.flush();
            }
        }
        /*SL:1145*/this._releaseBuffers();
    }
    
    @Override
    protected void _releaseBuffers() {
        final byte[] v1 = /*EL:1151*/this._outputBuffer;
        /*SL:1152*/if (v1 != null && this._bufferRecyclable) {
            /*SL:1153*/this._outputBuffer = null;
            /*SL:1154*/this._ioContext.releaseWriteEncodingBuffer(v1);
        }
        final char[] v2 = /*EL:1156*/this._charBuffer;
        /*SL:1157*/if (v2 != null) {
            /*SL:1158*/this._charBuffer = null;
            /*SL:1159*/this._ioContext.releaseConcatBuffer(v2);
        }
    }
    
    private final void _writeBytes(final byte[] a1) throws IOException {
        final int v1 = /*EL:1171*/a1.length;
        /*SL:1172*/if (this._outputTail + v1 > this._outputEnd) {
            /*SL:1173*/this._flushBuffer();
            /*SL:1175*/if (v1 > 512) {
                /*SL:1176*/this._outputStream.write(a1, 0, v1);
                /*SL:1177*/return;
            }
        }
        /*SL:1180*/System.arraycopy(a1, 0, this._outputBuffer, this._outputTail, v1);
        /*SL:1181*/this._outputTail += v1;
    }
    
    private final void _writeBytes(final byte[] a1, final int a2, final int a3) throws IOException {
        /*SL:1186*/if (this._outputTail + a3 > this._outputEnd) {
            /*SL:1187*/this._flushBuffer();
            /*SL:1189*/if (a3 > 512) {
                /*SL:1190*/this._outputStream.write(a1, a2, a3);
                /*SL:1191*/return;
            }
        }
        /*SL:1194*/System.arraycopy(a1, a2, this._outputBuffer, this._outputTail, a3);
        /*SL:1195*/this._outputTail += a3;
    }
    
    private final void _writeStringSegments(final String v1, final boolean v2) throws IOException {
        /*SL:1213*/if (v2) {
            /*SL:1214*/if (this._outputTail >= this._outputEnd) {
                /*SL:1215*/this._flushBuffer();
            }
            /*SL:1217*/this._outputBuffer[this._outputTail++] = this._quoteChar;
        }
        int v3 = /*EL:1220*/v1.length();
        int v4 = /*EL:1221*/0;
        /*SL:1223*/while (v3 > 0) {
            final int a1 = /*EL:1224*/Math.min(this._outputMaxContiguous, v3);
            /*SL:1225*/if (this._outputTail + a1 > this._outputEnd) {
                /*SL:1226*/this._flushBuffer();
            }
            /*SL:1228*/this._writeStringSegment(v1, v4, a1);
            /*SL:1229*/v4 += a1;
            /*SL:1230*/v3 -= a1;
        }
        /*SL:1233*/if (v2) {
            /*SL:1234*/if (this._outputTail >= this._outputEnd) {
                /*SL:1235*/this._flushBuffer();
            }
            /*SL:1237*/this._outputBuffer[this._outputTail++] = this._quoteChar;
        }
    }
    
    private final void _writeStringSegments(final char[] a3, int v1, int v2) throws IOException {
        /*SL:1257*/do {
            final int a4 = Math.min(this._outputMaxContiguous, v2);
            if (this._outputTail + a4 > this._outputEnd) {
                this._flushBuffer();
            }
            this._writeStringSegment(a3, v1, a4);
            v1 += a4;
            v2 -= a4;
        } while (v2 > 0);
    }
    
    private final void _writeStringSegments(final String a3, int v1, int v2) throws IOException {
        /*SL:1270*/do {
            final int a4 = Math.min(this._outputMaxContiguous, v2);
            if (this._outputTail + a4 > this._outputEnd) {
                this._flushBuffer();
            }
            this._writeStringSegment(a3, v1, a4);
            v1 += a4;
            v2 -= a4;
        } while (v2 > 0);
    }
    
    private final void _writeStringSegment(final char[] a3, int v1, int v2) throws IOException {
        /*SL:1293*/v2 += v1;
        int v3 = /*EL:1295*/this._outputTail;
        final byte[] v4 = /*EL:1296*/this._outputBuffer;
        final int[] v5 = /*EL:1297*/this._outputEscapes;
        /*SL:1299*/while (v1 < v2) {
            final int a4 = /*EL:1300*/a3[v1];
            /*SL:1302*/if (a4 > 127) {
                break;
            }
            if (v5[a4] != 0) {
                /*SL:1303*/break;
            }
            /*SL:1305*/v4[v3++] = (byte)a4;
            /*SL:1306*/++v1;
        }
        /*SL:1308*/this._outputTail = v3;
        /*SL:1309*/if (v1 < v2) {
            /*SL:1311*/if (this._characterEscapes != null) {
                /*SL:1312*/this._writeCustomStringSegment2(a3, v1, v2);
            }
            else/*SL:1314*/ if (this._maximumNonEscapedChar == 0) {
                /*SL:1315*/this._writeStringSegment2(a3, v1, v2);
            }
            else {
                /*SL:1317*/this._writeStringSegmentASCII2(a3, v1, v2);
            }
        }
    }
    
    private final void _writeStringSegment(final String a3, int v1, int v2) throws IOException {
        /*SL:1327*/v2 += v1;
        int v3 = /*EL:1329*/this._outputTail;
        final byte[] v4 = /*EL:1330*/this._outputBuffer;
        final int[] v5 = /*EL:1331*/this._outputEscapes;
        /*SL:1333*/while (v1 < v2) {
            final int a4 = /*EL:1334*/a3.charAt(v1);
            /*SL:1336*/if (a4 > 127) {
                break;
            }
            if (v5[a4] != 0) {
                /*SL:1337*/break;
            }
            /*SL:1339*/v4[v3++] = (byte)a4;
            /*SL:1340*/++v1;
        }
        /*SL:1342*/this._outputTail = v3;
        /*SL:1343*/if (v1 < v2) {
            /*SL:1344*/if (this._characterEscapes != null) {
                /*SL:1345*/this._writeCustomStringSegment2(a3, v1, v2);
            }
            else/*SL:1346*/ if (this._maximumNonEscapedChar == 0) {
                /*SL:1347*/this._writeStringSegment2(a3, v1, v2);
            }
            else {
                /*SL:1349*/this._writeStringSegmentASCII2(a3, v1, v2);
            }
        }
    }
    
    private final void _writeStringSegment2(final char[] v1, int v2, final int v3) throws IOException {
        /*SL:1361*/if (this._outputTail + 6 * (v3 - v2) > this._outputEnd) {
            /*SL:1362*/this._flushBuffer();
        }
        int v4 = /*EL:1365*/this._outputTail;
        final byte[] v5 = /*EL:1367*/this._outputBuffer;
        final int[] v6 = /*EL:1368*/this._outputEscapes;
        /*SL:1370*/while (v2 < v3) {
            int a2 = /*EL:1371*/v1[v2++];
            /*SL:1372*/if (a2 <= 127) {
                /*SL:1373*/if (v6[a2] == 0) {
                    /*SL:1374*/v5[v4++] = (byte)a2;
                }
                else {
                    /*SL:1377*/a2 = v6[a2];
                    /*SL:1378*/if (a2 > 0) {
                        /*SL:1379*/v5[v4++] = 92;
                        /*SL:1380*/v5[v4++] = (byte)a2;
                    }
                    else {
                        /*SL:1383*/v4 = this._writeGenericEscape(a2, v4);
                    }
                }
            }
            else/*SL:1387*/ if (a2 <= 2047) {
                /*SL:1388*/v5[v4++] = (byte)(0xC0 | a2 >> 6);
                /*SL:1389*/v5[v4++] = (byte)(0x80 | (a2 & 0x3F));
            }
            else {
                /*SL:1391*/v4 = this._outputMultiByteChar(a2, v4);
            }
        }
        /*SL:1394*/this._outputTail = v4;
    }
    
    private final void _writeStringSegment2(final String v1, int v2, final int v3) throws IOException {
        /*SL:1399*/if (this._outputTail + 6 * (v3 - v2) > this._outputEnd) {
            /*SL:1400*/this._flushBuffer();
        }
        int v4 = /*EL:1403*/this._outputTail;
        final byte[] v5 = /*EL:1405*/this._outputBuffer;
        final int[] v6 = /*EL:1406*/this._outputEscapes;
        /*SL:1408*/while (v2 < v3) {
            int a2 = /*EL:1409*/v1.charAt(v2++);
            /*SL:1410*/if (a2 <= 127) {
                /*SL:1411*/if (v6[a2] == 0) {
                    /*SL:1412*/v5[v4++] = (byte)a2;
                }
                else {
                    /*SL:1415*/a2 = v6[a2];
                    /*SL:1416*/if (a2 > 0) {
                        /*SL:1417*/v5[v4++] = 92;
                        /*SL:1418*/v5[v4++] = (byte)a2;
                    }
                    else {
                        /*SL:1421*/v4 = this._writeGenericEscape(a2, v4);
                    }
                }
            }
            else/*SL:1425*/ if (a2 <= 2047) {
                /*SL:1426*/v5[v4++] = (byte)(0xC0 | a2 >> 6);
                /*SL:1427*/v5[v4++] = (byte)(0x80 | (a2 & 0x3F));
            }
            else {
                /*SL:1429*/v4 = this._outputMultiByteChar(a2, v4);
            }
        }
        /*SL:1432*/this._outputTail = v4;
    }
    
    private final void _writeStringSegmentASCII2(final char[] v1, int v2, final int v3) throws IOException {
        /*SL:1449*/if (this._outputTail + 6 * (v3 - v2) > this._outputEnd) {
            /*SL:1450*/this._flushBuffer();
        }
        int v4 = /*EL:1453*/this._outputTail;
        final byte[] v5 = /*EL:1455*/this._outputBuffer;
        final int[] v6 = /*EL:1456*/this._outputEscapes;
        final int v7 = /*EL:1457*/this._maximumNonEscapedChar;
        /*SL:1459*/while (v2 < v3) {
            int a2 = /*EL:1460*/v1[v2++];
            /*SL:1461*/if (a2 <= 127) {
                /*SL:1462*/if (v6[a2] == 0) {
                    /*SL:1463*/v5[v4++] = (byte)a2;
                }
                else {
                    /*SL:1466*/a2 = v6[a2];
                    /*SL:1467*/if (a2 > 0) {
                        /*SL:1468*/v5[v4++] = 92;
                        /*SL:1469*/v5[v4++] = (byte)a2;
                    }
                    else {
                        /*SL:1472*/v4 = this._writeGenericEscape(a2, v4);
                    }
                }
            }
            else/*SL:1476*/ if (a2 > v7) {
                /*SL:1477*/v4 = this._writeGenericEscape(a2, v4);
            }
            else/*SL:1480*/ if (a2 <= 2047) {
                /*SL:1481*/v5[v4++] = (byte)(0xC0 | a2 >> 6);
                /*SL:1482*/v5[v4++] = (byte)(0x80 | (a2 & 0x3F));
            }
            else {
                /*SL:1484*/v4 = this._outputMultiByteChar(a2, v4);
            }
        }
        /*SL:1487*/this._outputTail = v4;
    }
    
    private final void _writeStringSegmentASCII2(final String v1, int v2, final int v3) throws IOException {
        /*SL:1493*/if (this._outputTail + 6 * (v3 - v2) > this._outputEnd) {
            /*SL:1494*/this._flushBuffer();
        }
        int v4 = /*EL:1497*/this._outputTail;
        final byte[] v5 = /*EL:1499*/this._outputBuffer;
        final int[] v6 = /*EL:1500*/this._outputEscapes;
        final int v7 = /*EL:1501*/this._maximumNonEscapedChar;
        /*SL:1503*/while (v2 < v3) {
            int a2 = /*EL:1504*/v1.charAt(v2++);
            /*SL:1505*/if (a2 <= 127) {
                /*SL:1506*/if (v6[a2] == 0) {
                    /*SL:1507*/v5[v4++] = (byte)a2;
                }
                else {
                    /*SL:1510*/a2 = v6[a2];
                    /*SL:1511*/if (a2 > 0) {
                        /*SL:1512*/v5[v4++] = 92;
                        /*SL:1513*/v5[v4++] = (byte)a2;
                    }
                    else {
                        /*SL:1516*/v4 = this._writeGenericEscape(a2, v4);
                    }
                }
            }
            else/*SL:1520*/ if (a2 > v7) {
                /*SL:1521*/v4 = this._writeGenericEscape(a2, v4);
            }
            else/*SL:1524*/ if (a2 <= 2047) {
                /*SL:1525*/v5[v4++] = (byte)(0xC0 | a2 >> 6);
                /*SL:1526*/v5[v4++] = (byte)(0x80 | (a2 & 0x3F));
            }
            else {
                /*SL:1528*/v4 = this._outputMultiByteChar(a2, v4);
            }
        }
        /*SL:1531*/this._outputTail = v4;
    }
    
    private final void _writeCustomStringSegment2(final char[] v-8, int v-7, final int v-6) throws IOException {
        /*SL:1548*/if (this._outputTail + 6 * (v-6 - v-7) > this._outputEnd) {
            /*SL:1549*/this._flushBuffer();
        }
        int n = /*EL:1551*/this._outputTail;
        final byte[] outputBuffer = /*EL:1553*/this._outputBuffer;
        final int[] outputEscapes = /*EL:1554*/this._outputEscapes;
        final int n2 = /*EL:1556*/(this._maximumNonEscapedChar <= 0) ? 65535 : this._maximumNonEscapedChar;
        final CharacterEscapes characterEscapes = /*EL:1557*/this._characterEscapes;
        /*SL:1559*/while (v-7 < v-6) {
            int a3 = /*EL:1560*/v-8[v-7++];
            /*SL:1561*/if (a3 <= 127) {
                /*SL:1562*/if (outputEscapes[a3] == 0) {
                    /*SL:1563*/outputBuffer[n++] = (byte)a3;
                }
                else {
                    final int a2 = /*EL:1566*/outputEscapes[a3];
                    /*SL:1567*/if (a2 > 0) {
                        /*SL:1568*/outputBuffer[n++] = 92;
                        /*SL:1569*/outputBuffer[n++] = (byte)a2;
                    }
                    else/*SL:1570*/ if (a2 == -2) {
                        /*SL:1571*/a3 = characterEscapes.getEscapeSequence(a3);
                        /*SL:1572*/if (a3 == null) {
                            /*SL:1573*/this._reportError("Invalid custom escape definitions; custom escape not found for character code 0x" + Integer.toHexString(a3) + ", although was supposed to have one");
                        }
                        /*SL:1576*/n = this._writeCustomEscape(outputBuffer, n, a3, v-6 - v-7);
                    }
                    else {
                        /*SL:1579*/n = this._writeGenericEscape(a3, n);
                    }
                }
            }
            else/*SL:1583*/ if (a3 > n2) {
                /*SL:1584*/n = this._writeGenericEscape(a3, n);
            }
            else {
                final SerializableString v1 = /*EL:1587*/characterEscapes.getEscapeSequence(a3);
                /*SL:1588*/if (v1 != null) {
                    /*SL:1589*/n = this._writeCustomEscape(outputBuffer, n, v1, v-6 - v-7);
                }
                else/*SL:1592*/ if (a3 <= 2047) {
                    /*SL:1593*/outputBuffer[n++] = (byte)(0xC0 | a3 >> 6);
                    /*SL:1594*/outputBuffer[n++] = (byte)(0x80 | (a3 & 0x3F));
                }
                else {
                    /*SL:1596*/n = this._outputMultiByteChar(a3, n);
                }
            }
        }
        /*SL:1599*/this._outputTail = n;
    }
    
    private final void _writeCustomStringSegment2(final String v-8, int v-7, final int v-6) throws IOException {
        /*SL:1605*/if (this._outputTail + 6 * (v-6 - v-7) > this._outputEnd) {
            /*SL:1606*/this._flushBuffer();
        }
        int n = /*EL:1608*/this._outputTail;
        final byte[] outputBuffer = /*EL:1610*/this._outputBuffer;
        final int[] outputEscapes = /*EL:1611*/this._outputEscapes;
        final int n2 = /*EL:1613*/(this._maximumNonEscapedChar <= 0) ? 65535 : this._maximumNonEscapedChar;
        final CharacterEscapes characterEscapes = /*EL:1614*/this._characterEscapes;
        /*SL:1616*/while (v-7 < v-6) {
            int a3 = /*EL:1617*/v-8.charAt(v-7++);
            /*SL:1618*/if (a3 <= 127) {
                /*SL:1619*/if (outputEscapes[a3] == 0) {
                    /*SL:1620*/outputBuffer[n++] = (byte)a3;
                }
                else {
                    final int a2 = /*EL:1623*/outputEscapes[a3];
                    /*SL:1624*/if (a2 > 0) {
                        /*SL:1625*/outputBuffer[n++] = 92;
                        /*SL:1626*/outputBuffer[n++] = (byte)a2;
                    }
                    else/*SL:1627*/ if (a2 == -2) {
                        /*SL:1628*/a3 = characterEscapes.getEscapeSequence(a3);
                        /*SL:1629*/if (a3 == null) {
                            /*SL:1630*/this._reportError("Invalid custom escape definitions; custom escape not found for character code 0x" + Integer.toHexString(a3) + ", although was supposed to have one");
                        }
                        /*SL:1633*/n = this._writeCustomEscape(outputBuffer, n, a3, v-6 - v-7);
                    }
                    else {
                        /*SL:1636*/n = this._writeGenericEscape(a3, n);
                    }
                }
            }
            else/*SL:1640*/ if (a3 > n2) {
                /*SL:1641*/n = this._writeGenericEscape(a3, n);
            }
            else {
                final SerializableString v1 = /*EL:1644*/characterEscapes.getEscapeSequence(a3);
                /*SL:1645*/if (v1 != null) {
                    /*SL:1646*/n = this._writeCustomEscape(outputBuffer, n, v1, v-6 - v-7);
                }
                else/*SL:1649*/ if (a3 <= 2047) {
                    /*SL:1650*/outputBuffer[n++] = (byte)(0xC0 | a3 >> 6);
                    /*SL:1651*/outputBuffer[n++] = (byte)(0x80 | (a3 & 0x3F));
                }
                else {
                    /*SL:1653*/n = this._outputMultiByteChar(a3, n);
                }
            }
        }
        /*SL:1656*/this._outputTail = n;
    }
    
    private final int _writeCustomEscape(final byte[] a1, final int a2, final SerializableString a3, final int a4) throws IOException, JsonGenerationException {
        final byte[] v1 = /*EL:1662*/a3.asUnquotedUTF8();
        final int v2 = /*EL:1663*/v1.length;
        /*SL:1664*/if (v2 > 6) {
            /*SL:1665*/return this._handleLongCustomEscape(a1, a2, this._outputEnd, v1, a4);
        }
        /*SL:1668*/System.arraycopy(v1, 0, a1, a2, v2);
        /*SL:1669*/return a2 + v2;
    }
    
    private final int _handleLongCustomEscape(final byte[] a1, int a2, final int a3, final byte[] a4, final int a5) throws IOException, JsonGenerationException {
        final int v1 = /*EL:1676*/a4.length;
        /*SL:1677*/if (a2 + v1 > a3) {
            /*SL:1678*/this._outputTail = a2;
            /*SL:1679*/this._flushBuffer();
            /*SL:1680*/a2 = this._outputTail;
            /*SL:1681*/if (v1 > a1.length) {
                /*SL:1682*/this._outputStream.write(a4, 0, v1);
                /*SL:1683*/return a2;
            }
            /*SL:1685*/System.arraycopy(a4, 0, a1, a2, v1);
            /*SL:1686*/a2 += v1;
        }
        /*SL:1689*/if (a2 + 6 * a5 > a3) {
            /*SL:1690*/this._flushBuffer();
            /*SL:1691*/return this._outputTail;
        }
        /*SL:1693*/return a2;
    }
    
    private final void _writeUTF8Segments(final byte[] a3, int v1, int v2) throws IOException, JsonGenerationException {
        /*SL:1715*/do {
            final int a4 = Math.min(this._outputMaxContiguous, v2);
            this._writeUTF8Segment(a3, v1, a4);
            v1 += a4;
            v2 -= a4;
        } while (v2 > 0);
    }
    
    private final void _writeUTF8Segment(final byte[] v2, final int v3, final int v4) throws IOException, JsonGenerationException {
        final int[] v5 = /*EL:1722*/this._outputEscapes;
        int a2 = /*EL:1724*/v3;
        a2 = v3 + v4;
        while (a2 < a2) {
            final int a3 = /*EL:1726*/v2[a2++];
            /*SL:1727*/if (a3 >= 0 && v5[a3] != 0) {
                /*SL:1728*/this._writeUTF8Segment2(v2, v3, v4);
                /*SL:1729*/return;
            }
        }
        /*SL:1734*/if (this._outputTail + v4 > this._outputEnd) {
            /*SL:1735*/this._flushBuffer();
        }
        /*SL:1737*/System.arraycopy(v2, v3, this._outputBuffer, this._outputTail, v4);
        /*SL:1738*/this._outputTail += v4;
    }
    
    private final void _writeUTF8Segment2(final byte[] v2, int v3, int v4) throws IOException, JsonGenerationException {
        int v5 = /*EL:1744*/this._outputTail;
        /*SL:1747*/if (v5 + v4 * 6 > this._outputEnd) {
            /*SL:1748*/this._flushBuffer();
            /*SL:1749*/v5 = this._outputTail;
        }
        final byte[] v6 = /*EL:1752*/this._outputBuffer;
        final int[] v7 = /*EL:1753*/this._outputEscapes;
        /*SL:1754*/v4 += v3;
        /*SL:1756*/while (v3 < v4) {
            final int a2;
            final byte a1 = /*EL:1758*/(byte)(a2 = v2[v3++]);
            /*SL:1759*/if (a2 < 0 || v7[a2] == 0) {
                /*SL:1760*/v6[v5++] = a1;
            }
            else {
                final int a3 = /*EL:1763*/v7[a2];
                /*SL:1764*/if (a3 > 0) {
                    /*SL:1765*/v6[v5++] = 92;
                    /*SL:1766*/v6[v5++] = (byte)a3;
                }
                else {
                    /*SL:1769*/v5 = this._writeGenericEscape(a2, v5);
                }
            }
        }
        /*SL:1772*/this._outputTail = v5;
    }
    
    protected final void _writeBinary(final Base64Variant a4, final byte[] v1, int v2, final int v3) throws IOException, JsonGenerationException {
        final int v4 = /*EL:1786*/v3 - 3;
        final int v5 = /*EL:1788*/this._outputEnd - 6;
        int v6 = /*EL:1789*/a4.getMaxLineLength() >> 2;
        /*SL:1792*/while (v2 <= v4) {
            /*SL:1793*/if (this._outputTail > v5) {
                /*SL:1794*/this._flushBuffer();
            }
            int a5 = /*EL:1797*/v1[v2++] << 8;
            /*SL:1798*/a5 |= (v1[v2++] & 0xFF);
            /*SL:1799*/a5 = (a5 << 8 | (v1[v2++] & 0xFF));
            /*SL:1800*/this._outputTail = a4.encodeBase64Chunk(a5, this._outputBuffer, this._outputTail);
            /*SL:1801*/if (--v6 <= 0) {
                /*SL:1803*/this._outputBuffer[this._outputTail++] = 92;
                /*SL:1804*/this._outputBuffer[this._outputTail++] = 110;
                /*SL:1805*/v6 = a4.getMaxLineLength() >> 2;
            }
        }
        final int v7 = /*EL:1810*/v3 - v2;
        /*SL:1811*/if (v7 > 0) {
            /*SL:1812*/if (this._outputTail > v5) {
                /*SL:1813*/this._flushBuffer();
            }
            int a6 = /*EL:1815*/v1[v2++] << 16;
            /*SL:1816*/if (v7 == 2) {
                /*SL:1817*/a6 |= (v1[v2++] & 0xFF) << 8;
            }
            /*SL:1819*/this._outputTail = a4.encodeBase64Partial(a6, v7, this._outputBuffer, this._outputTail);
        }
    }
    
    protected final int _writeBinary(final Base64Variant v2, final InputStream v3, final byte[] v4, int v5) throws IOException, JsonGenerationException {
        int v6 = /*EL:1828*/0;
        int v7 = /*EL:1829*/0;
        int v8 = /*EL:1830*/-3;
        final int v9 = /*EL:1833*/this._outputEnd - 6;
        int v10 = /*EL:1834*/v2.getMaxLineLength() >> 2;
        /*SL:1836*/while (v5 > 2) {
            /*SL:1837*/if (v6 > v8) {
                /*SL:1838*/v7 = this._readMore(v3, v4, v6, v7, v5);
                /*SL:1839*/v6 = 0;
                /*SL:1840*/if (v7 < 3) {
                    /*SL:1841*/break;
                }
                /*SL:1843*/v8 = v7 - 3;
            }
            /*SL:1845*/if (this._outputTail > v9) {
                /*SL:1846*/this._flushBuffer();
            }
            int a1 = /*EL:1848*/v4[v6++] << 8;
            /*SL:1849*/a1 |= (v4[v6++] & 0xFF);
            /*SL:1850*/a1 = (a1 << 8 | (v4[v6++] & 0xFF));
            /*SL:1851*/v5 -= 3;
            /*SL:1852*/this._outputTail = v2.encodeBase64Chunk(a1, this._outputBuffer, this._outputTail);
            /*SL:1853*/if (--v10 <= 0) {
                /*SL:1854*/this._outputBuffer[this._outputTail++] = 92;
                /*SL:1855*/this._outputBuffer[this._outputTail++] = 110;
                /*SL:1856*/v10 = v2.getMaxLineLength() >> 2;
            }
        }
        /*SL:1861*/if (v5 > 0) {
            /*SL:1862*/v7 = this._readMore(v3, v4, v6, v7, v5);
            /*SL:1863*/v6 = 0;
            /*SL:1864*/if (v7 > 0) {
                /*SL:1865*/if (this._outputTail > v9) {
                    /*SL:1866*/this._flushBuffer();
                }
                int a2 = /*EL:1868*/v4[v6++] << 16;
                final int a4;
                /*SL:1870*/if (v6 < v7) {
                    /*SL:1871*/a2 |= (v4[v6] & 0xFF) << 8;
                    final int a3 = /*EL:1872*/2;
                }
                else {
                    /*SL:1874*/a4 = 1;
                }
                /*SL:1876*/this._outputTail = v2.encodeBase64Partial(a2, a4, this._outputBuffer, this._outputTail);
                /*SL:1877*/v5 -= a4;
            }
        }
        /*SL:1880*/return v5;
    }
    
    protected final int _writeBinary(final Base64Variant v2, final InputStream v3, final byte[] v4) throws IOException, JsonGenerationException {
        int v5 = /*EL:1888*/0;
        int v6 = /*EL:1889*/0;
        int v7 = /*EL:1890*/-3;
        int v8 = /*EL:1891*/0;
        final int v9 = /*EL:1894*/this._outputEnd - 6;
        int v10 = /*EL:1895*/v2.getMaxLineLength() >> 2;
        while (true) {
            /*SL:1899*/if (v5 > v7) {
                /*SL:1900*/v6 = this._readMore(v3, v4, v5, v6, v4.length);
                /*SL:1901*/v5 = 0;
                /*SL:1902*/if (v6 < 3) {
                    break;
                }
                /*SL:1905*/v7 = v6 - 3;
            }
            /*SL:1907*/if (this._outputTail > v9) {
                /*SL:1908*/this._flushBuffer();
            }
            int a1 = /*EL:1911*/v4[v5++] << 8;
            /*SL:1912*/a1 |= (v4[v5++] & 0xFF);
            /*SL:1913*/a1 = (a1 << 8 | (v4[v5++] & 0xFF));
            /*SL:1914*/v8 += 3;
            /*SL:1915*/this._outputTail = v2.encodeBase64Chunk(a1, this._outputBuffer, this._outputTail);
            /*SL:1916*/if (--v10 <= 0) {
                /*SL:1917*/this._outputBuffer[this._outputTail++] = 92;
                /*SL:1918*/this._outputBuffer[this._outputTail++] = 110;
                /*SL:1919*/v10 = v2.getMaxLineLength() >> 2;
            }
        }
        /*SL:1924*/if (v5 < v6) {
            /*SL:1925*/if (this._outputTail > v9) {
                /*SL:1926*/this._flushBuffer();
            }
            int a2 = /*EL:1928*/v4[v5++] << 16;
            int a3 = /*EL:1929*/1;
            /*SL:1930*/if (v5 < v6) {
                /*SL:1931*/a2 |= (v4[v5] & 0xFF) << 8;
                /*SL:1932*/a3 = 2;
            }
            /*SL:1934*/v8 += a3;
            /*SL:1935*/this._outputTail = v2.encodeBase64Partial(a2, a3, this._outputBuffer, this._outputTail);
        }
        /*SL:1937*/return v8;
    }
    
    private final int _readMore(final InputStream a4, final byte[] a5, int v1, int v2, int v3) throws IOException {
        int v4;
        /*SL:1946*/for (v4 = 0; v1 < v2; /*SL:1947*/a5[v4++] = a5[v1++]) {}
        /*SL:1949*/v1 = 0;
        /*SL:1950*/v2 = v4;
        /*SL:1951*/v3 = Math.min(v3, a5.length);
        /*SL:1963*/do {
            final int a6 = v3 - v2;
            if (a6 == 0) {
                break;
            }
            final int a7 = a4.read(a5, v2, a6);
            if (a7 < 0) {
                return v2;
            }
            v2 += a7;
        } while (v2 < 3);
        /*SL:1964*/return v2;
    }
    
    private final int _outputRawMultiByteChar(final int a1, final char[] a2, final int a3, final int a4) throws IOException {
        /*SL:1982*/if (a1 >= 55296 && /*EL:1983*/a1 <= 57343) {
            /*SL:1985*/if (a3 >= a4 || a2 == null) {
                /*SL:1986*/this._reportError(String.format("Split surrogate on writeRaw() input (last character): first character 0x%4x", a1));
            }
            /*SL:1989*/this._outputSurrogates(a1, a2[a3]);
            /*SL:1990*/return a3 + 1;
        }
        final byte[] v1 = /*EL:1993*/this._outputBuffer;
        /*SL:1994*/v1[this._outputTail++] = (byte)(0xE0 | a1 >> 12);
        /*SL:1995*/v1[this._outputTail++] = (byte)(0x80 | (a1 >> 6 & 0x3F));
        /*SL:1996*/v1[this._outputTail++] = (byte)(0x80 | (a1 & 0x3F));
        /*SL:1997*/return a3;
    }
    
    protected final void _outputSurrogates(final int a1, final int a2) throws IOException {
        final int v1 = /*EL:2002*/this._decodeSurrogate(a1, a2);
        /*SL:2003*/if (this._outputTail + 4 > this._outputEnd) {
            /*SL:2004*/this._flushBuffer();
        }
        final byte[] v2 = /*EL:2006*/this._outputBuffer;
        /*SL:2007*/v2[this._outputTail++] = (byte)(0xF0 | v1 >> 18);
        /*SL:2008*/v2[this._outputTail++] = (byte)(0x80 | (v1 >> 12 & 0x3F));
        /*SL:2009*/v2[this._outputTail++] = (byte)(0x80 | (v1 >> 6 & 0x3F));
        /*SL:2010*/v2[this._outputTail++] = (byte)(0x80 | (v1 & 0x3F));
    }
    
    private final int _outputMultiByteChar(final int a1, int a2) throws IOException {
        final byte[] v1 = /*EL:2024*/this._outputBuffer;
        /*SL:2025*/if (a1 >= 55296 && a1 <= 57343) {
            /*SL:2030*/v1[a2++] = 92;
            /*SL:2031*/v1[a2++] = 117;
            /*SL:2033*/v1[a2++] = UTF8JsonGenerator.HEX_CHARS[a1 >> 12 & 0xF];
            /*SL:2034*/v1[a2++] = UTF8JsonGenerator.HEX_CHARS[a1 >> 8 & 0xF];
            /*SL:2035*/v1[a2++] = UTF8JsonGenerator.HEX_CHARS[a1 >> 4 & 0xF];
            /*SL:2036*/v1[a2++] = UTF8JsonGenerator.HEX_CHARS[a1 & 0xF];
        }
        else {
            /*SL:2039*/v1[a2++] = (byte)(0xE0 | a1 >> 12);
            /*SL:2040*/v1[a2++] = (byte)(0x80 | (a1 >> 6 & 0x3F));
            /*SL:2041*/v1[a2++] = (byte)(0x80 | (a1 & 0x3F));
        }
        /*SL:2043*/return a2;
    }
    
    private final void _writeNull() throws IOException {
        /*SL:2048*/if (this._outputTail + 4 >= this._outputEnd) {
            /*SL:2049*/this._flushBuffer();
        }
        /*SL:2051*/System.arraycopy(UTF8JsonGenerator.NULL_BYTES, 0, this._outputBuffer, this._outputTail, 4);
        /*SL:2052*/this._outputTail += 4;
    }
    
    private int _writeGenericEscape(int v1, int v2) throws IOException {
        final byte[] v3 = /*EL:2062*/this._outputBuffer;
        /*SL:2063*/v3[v2++] = 92;
        /*SL:2064*/v3[v2++] = 117;
        /*SL:2065*/if (v1 > 255) {
            final int a1 = /*EL:2066*/v1 >> 8 & 0xFF;
            /*SL:2067*/v3[v2++] = UTF8JsonGenerator.HEX_CHARS[a1 >> 4];
            /*SL:2068*/v3[v2++] = UTF8JsonGenerator.HEX_CHARS[a1 & 0xF];
            /*SL:2069*/v1 &= 0xFF;
        }
        else {
            /*SL:2071*/v3[v2++] = 48;
            /*SL:2072*/v3[v2++] = 48;
        }
        /*SL:2075*/v3[v2++] = UTF8JsonGenerator.HEX_CHARS[v1 >> 4];
        /*SL:2076*/v3[v2++] = UTF8JsonGenerator.HEX_CHARS[v1 & 0xF];
        /*SL:2077*/return v2;
    }
    
    protected final void _flushBuffer() throws IOException {
        final int v1 = /*EL:2082*/this._outputTail;
        /*SL:2083*/if (v1 > 0) {
            /*SL:2084*/this._outputTail = 0;
            /*SL:2085*/this._outputStream.write(this._outputBuffer, 0, v1);
        }
    }
    
    static {
        HEX_CHARS = CharTypes.copyHexBytes();
        NULL_BYTES = new byte[] { 110, 117, 108, 108 };
        TRUE_BYTES = new byte[] { 116, 114, 117, 101 };
        FALSE_BYTES = new byte[] { 102, 97, 108, 115, 101 };
    }
}

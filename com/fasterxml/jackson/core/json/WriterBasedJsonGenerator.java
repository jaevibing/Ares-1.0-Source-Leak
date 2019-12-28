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
import com.fasterxml.jackson.core.JsonGenerator;
import java.io.IOException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.core.io.IOContext;
import com.fasterxml.jackson.core.SerializableString;
import java.io.Writer;

public class WriterBasedJsonGenerator extends JsonGeneratorImpl
{
    protected static final int SHORT_WRITE = 32;
    protected static final char[] HEX_CHARS;
    protected final Writer _writer;
    protected char _quoteChar;
    protected char[] _outputBuffer;
    protected int _outputHead;
    protected int _outputTail;
    protected int _outputEnd;
    protected char[] _entityBuffer;
    protected SerializableString _currentEscape;
    protected char[] _charBuffer;
    
    public WriterBasedJsonGenerator(final IOContext a1, final int a2, final ObjectCodec a3, final Writer a4) {
        super(a1, a2, a3);
        this._quoteChar = '\"';
        this._writer = a4;
        this._outputBuffer = a1.allocConcatBuffer();
        this._outputEnd = this._outputBuffer.length;
    }
    
    @Override
    public Object getOutputTarget() {
        /*SL:112*/return this._writer;
    }
    
    @Override
    public int getOutputBuffered() {
        final int v1 = /*EL:118*/this._outputTail - this._outputHead;
        /*SL:119*/return Math.max(0, v1);
    }
    
    @Override
    public boolean canWriteFormattedNumbers() {
        /*SL:124*/return true;
    }
    
    @Override
    public void writeFieldName(final String a1) throws IOException {
        final int v1 = /*EL:135*/this._writeContext.writeFieldName(a1);
        /*SL:136*/if (v1 == 4) {
            /*SL:137*/this._reportError("Can not write a field name, expecting a value");
        }
        /*SL:139*/this._writeFieldName(a1, v1 == 1);
    }
    
    @Override
    public void writeFieldName(final SerializableString a1) throws IOException {
        final int v1 = /*EL:146*/this._writeContext.writeFieldName(a1.getValue());
        /*SL:147*/if (v1 == 4) {
            /*SL:148*/this._reportError("Can not write a field name, expecting a value");
        }
        /*SL:150*/this._writeFieldName(a1, v1 == 1);
    }
    
    protected final void _writeFieldName(final String a1, final boolean a2) throws IOException {
        /*SL:155*/if (this._cfgPrettyPrinter != null) {
            /*SL:156*/this._writePPFieldName(a1, a2);
            /*SL:157*/return;
        }
        /*SL:160*/if (this._outputTail + 1 >= this._outputEnd) {
            /*SL:161*/this._flushBuffer();
        }
        /*SL:163*/if (a2) {
            /*SL:164*/this._outputBuffer[this._outputTail++] = ',';
        }
        /*SL:167*/if (this._cfgUnqNames) {
            /*SL:168*/this._writeString(a1);
            /*SL:169*/return;
        }
        /*SL:172*/this._outputBuffer[this._outputTail++] = this._quoteChar;
        /*SL:174*/this._writeString(a1);
        /*SL:176*/if (this._outputTail >= this._outputEnd) {
            /*SL:177*/this._flushBuffer();
        }
        /*SL:179*/this._outputBuffer[this._outputTail++] = this._quoteChar;
    }
    
    protected final void _writeFieldName(final SerializableString a1, final boolean a2) throws IOException {
        /*SL:184*/if (this._cfgPrettyPrinter != null) {
            /*SL:185*/this._writePPFieldName(a1, a2);
            /*SL:186*/return;
        }
        /*SL:189*/if (this._outputTail + 1 >= this._outputEnd) {
            /*SL:190*/this._flushBuffer();
        }
        /*SL:192*/if (a2) {
            /*SL:193*/this._outputBuffer[this._outputTail++] = ',';
        }
        final char[] v1 = /*EL:196*/a1.asQuotedChars();
        /*SL:197*/if (this._cfgUnqNames) {
            /*SL:198*/this.writeRaw(v1, 0, v1.length);
            /*SL:199*/return;
        }
        /*SL:202*/this._outputBuffer[this._outputTail++] = this._quoteChar;
        final int v2 = /*EL:204*/v1.length;
        /*SL:205*/if (this._outputTail + v2 + 1 >= this._outputEnd) {
            /*SL:206*/this.writeRaw(v1, 0, v2);
            /*SL:208*/if (this._outputTail >= this._outputEnd) {
                /*SL:209*/this._flushBuffer();
            }
            /*SL:211*/this._outputBuffer[this._outputTail++] = this._quoteChar;
        }
        else {
            /*SL:213*/System.arraycopy(v1, 0, this._outputBuffer, this._outputTail, v2);
            /*SL:214*/this._outputTail += v2;
            /*SL:215*/this._outputBuffer[this._outputTail++] = this._quoteChar;
        }
    }
    
    @Override
    public void writeStartArray() throws IOException {
        /*SL:228*/this._verifyValueWrite("start an array");
        /*SL:229*/this._writeContext = this._writeContext.createChildArrayContext();
        /*SL:230*/if (this._cfgPrettyPrinter != null) {
            /*SL:231*/this._cfgPrettyPrinter.writeStartArray(this);
        }
        else {
            /*SL:233*/if (this._outputTail >= this._outputEnd) {
                /*SL:234*/this._flushBuffer();
            }
            /*SL:236*/this._outputBuffer[this._outputTail++] = '[';
        }
    }
    
    @Override
    public void writeEndArray() throws IOException {
        /*SL:243*/if (!this._writeContext.inArray()) {
            /*SL:244*/this._reportError("Current context not Array but " + this._writeContext.typeDesc());
        }
        /*SL:246*/if (this._cfgPrettyPrinter != null) {
            /*SL:247*/this._cfgPrettyPrinter.writeEndArray(this, this._writeContext.getEntryCount());
        }
        else {
            /*SL:249*/if (this._outputTail >= this._outputEnd) {
                /*SL:250*/this._flushBuffer();
            }
            /*SL:252*/this._outputBuffer[this._outputTail++] = ']';
        }
        /*SL:254*/this._writeContext = this._writeContext.clearAndGetParent();
    }
    
    @Override
    public void writeStartObject(final Object a1) throws IOException {
        /*SL:260*/this._verifyValueWrite("start an object");
        final JsonWriteContext v1 = /*EL:261*/this._writeContext.createChildObjectContext();
        /*SL:262*/this._writeContext = v1;
        /*SL:263*/if (a1 != null) {
            /*SL:264*/v1.setCurrentValue(a1);
        }
        /*SL:266*/if (this._cfgPrettyPrinter != null) {
            /*SL:267*/this._cfgPrettyPrinter.writeStartObject(this);
        }
        else {
            /*SL:269*/if (this._outputTail >= this._outputEnd) {
                /*SL:270*/this._flushBuffer();
            }
            /*SL:272*/this._outputBuffer[this._outputTail++] = '{';
        }
    }
    
    @Override
    public void writeStartObject() throws IOException {
        /*SL:279*/this._verifyValueWrite("start an object");
        /*SL:280*/this._writeContext = this._writeContext.createChildObjectContext();
        /*SL:281*/if (this._cfgPrettyPrinter != null) {
            /*SL:282*/this._cfgPrettyPrinter.writeStartObject(this);
        }
        else {
            /*SL:284*/if (this._outputTail >= this._outputEnd) {
                /*SL:285*/this._flushBuffer();
            }
            /*SL:287*/this._outputBuffer[this._outputTail++] = '{';
        }
    }
    
    @Override
    public void writeEndObject() throws IOException {
        /*SL:294*/if (!this._writeContext.inObject()) {
            /*SL:295*/this._reportError("Current context not Object but " + this._writeContext.typeDesc());
        }
        /*SL:297*/if (this._cfgPrettyPrinter != null) {
            /*SL:298*/this._cfgPrettyPrinter.writeEndObject(this, this._writeContext.getEntryCount());
        }
        else {
            /*SL:300*/if (this._outputTail >= this._outputEnd) {
                /*SL:301*/this._flushBuffer();
            }
            /*SL:303*/this._outputBuffer[this._outputTail++] = '}';
        }
        /*SL:305*/this._writeContext = this._writeContext.clearAndGetParent();
    }
    
    protected final void _writePPFieldName(final String a1, final boolean a2) throws IOException {
        /*SL:314*/if (a2) {
            /*SL:315*/this._cfgPrettyPrinter.writeObjectEntrySeparator(this);
        }
        else {
            /*SL:317*/this._cfgPrettyPrinter.beforeObjectEntries(this);
        }
        /*SL:320*/if (this._cfgUnqNames) {
            /*SL:321*/this._writeString(a1);
        }
        else {
            /*SL:323*/if (this._outputTail >= this._outputEnd) {
                /*SL:324*/this._flushBuffer();
            }
            /*SL:326*/this._outputBuffer[this._outputTail++] = this._quoteChar;
            /*SL:327*/this._writeString(a1);
            /*SL:328*/if (this._outputTail >= this._outputEnd) {
                /*SL:329*/this._flushBuffer();
            }
            /*SL:331*/this._outputBuffer[this._outputTail++] = this._quoteChar;
        }
    }
    
    protected final void _writePPFieldName(final SerializableString a1, final boolean a2) throws IOException {
        /*SL:337*/if (a2) {
            /*SL:338*/this._cfgPrettyPrinter.writeObjectEntrySeparator(this);
        }
        else {
            /*SL:340*/this._cfgPrettyPrinter.beforeObjectEntries(this);
        }
        final char[] v1 = /*EL:343*/a1.asQuotedChars();
        /*SL:344*/if (this._cfgUnqNames) {
            /*SL:345*/this.writeRaw(v1, 0, v1.length);
        }
        else {
            /*SL:347*/if (this._outputTail >= this._outputEnd) {
                /*SL:348*/this._flushBuffer();
            }
            /*SL:350*/this._outputBuffer[this._outputTail++] = this._quoteChar;
            /*SL:351*/this.writeRaw(v1, 0, v1.length);
            /*SL:352*/if (this._outputTail >= this._outputEnd) {
                /*SL:353*/this._flushBuffer();
            }
            /*SL:355*/this._outputBuffer[this._outputTail++] = this._quoteChar;
        }
    }
    
    @Override
    public void writeString(final String a1) throws IOException {
        /*SL:368*/this._verifyValueWrite("write a string");
        /*SL:369*/if (a1 == null) {
            /*SL:370*/this._writeNull();
            /*SL:371*/return;
        }
        /*SL:373*/if (this._outputTail >= this._outputEnd) {
            /*SL:374*/this._flushBuffer();
        }
        /*SL:376*/this._outputBuffer[this._outputTail++] = this._quoteChar;
        /*SL:377*/this._writeString(a1);
        /*SL:379*/if (this._outputTail >= this._outputEnd) {
            /*SL:380*/this._flushBuffer();
        }
        /*SL:382*/this._outputBuffer[this._outputTail++] = this._quoteChar;
    }
    
    @Override
    public void writeString(final Reader v2, final int v3) throws IOException {
        /*SL:387*/this._verifyValueWrite("write a string");
        /*SL:388*/if (v2 == null) {
            /*SL:389*/this._reportError("null reader");
        }
        int v4 = /*EL:391*/(v3 >= 0) ? v3 : Integer.MAX_VALUE;
        final char[] v5 = /*EL:392*/this._allocateCopyBuffer();
        /*SL:394*/if (this._outputTail + v3 >= this._outputEnd) {
            /*SL:395*/this._flushBuffer();
        }
        /*SL:397*/this._outputBuffer[this._outputTail++] = this._quoteChar;
        /*SL:400*/while (v4 > 0) {
            final int a1 = /*EL:401*/Math.min(v4, v5.length);
            final int a2 = /*EL:402*/v2.read(v5, 0, a1);
            /*SL:403*/if (a2 <= 0) {
                /*SL:404*/break;
            }
            /*SL:406*/if (this._outputTail + v3 >= this._outputEnd) {
                /*SL:407*/this._flushBuffer();
            }
            /*SL:409*/this._writeString(v5, 0, a2);
            /*SL:412*/v4 -= a2;
        }
        /*SL:415*/if (this._outputTail + v3 >= this._outputEnd) {
            /*SL:416*/this._flushBuffer();
        }
        /*SL:418*/this._outputBuffer[this._outputTail++] = this._quoteChar;
        /*SL:420*/if (v4 > 0 && v3 >= 0) {
            /*SL:421*/this._reportError("Didn't read enough from reader");
        }
    }
    
    @Override
    public void writeString(final char[] a1, final int a2, final int a3) throws IOException {
        /*SL:428*/this._verifyValueWrite("write a string");
        /*SL:429*/if (this._outputTail >= this._outputEnd) {
            /*SL:430*/this._flushBuffer();
        }
        /*SL:432*/this._outputBuffer[this._outputTail++] = this._quoteChar;
        /*SL:433*/this._writeString(a1, a2, a3);
        /*SL:435*/if (this._outputTail >= this._outputEnd) {
            /*SL:436*/this._flushBuffer();
        }
        /*SL:438*/this._outputBuffer[this._outputTail++] = this._quoteChar;
    }
    
    @Override
    public void writeString(final SerializableString v2) throws IOException {
        /*SL:444*/this._verifyValueWrite("write a string");
        /*SL:445*/if (this._outputTail >= this._outputEnd) {
            /*SL:446*/this._flushBuffer();
        }
        /*SL:448*/this._outputBuffer[this._outputTail++] = this._quoteChar;
        final char[] v3 = /*EL:450*/v2.asQuotedChars();
        final int v4 = /*EL:451*/v3.length;
        /*SL:453*/if (v4 < 32) {
            final int a1 = /*EL:454*/this._outputEnd - this._outputTail;
            /*SL:455*/if (v4 > a1) {
                /*SL:456*/this._flushBuffer();
            }
            /*SL:458*/System.arraycopy(v3, 0, this._outputBuffer, this._outputTail, v4);
            /*SL:459*/this._outputTail += v4;
        }
        else {
            /*SL:462*/this._flushBuffer();
            /*SL:463*/this._writer.write(v3, 0, v4);
        }
        /*SL:465*/if (this._outputTail >= this._outputEnd) {
            /*SL:466*/this._flushBuffer();
        }
        /*SL:468*/this._outputBuffer[this._outputTail++] = this._quoteChar;
    }
    
    @Override
    public void writeRawUTF8String(final byte[] a1, final int a2, final int a3) throws IOException {
        /*SL:474*/this._reportUnsupportedOperation();
    }
    
    @Override
    public void writeUTF8String(final byte[] a1, final int a2, final int a3) throws IOException {
        /*SL:480*/this._reportUnsupportedOperation();
    }
    
    @Override
    public void writeRaw(final String a1) throws IOException {
        final int v1 = /*EL:493*/a1.length();
        int v2 = /*EL:494*/this._outputEnd - this._outputTail;
        /*SL:496*/if (v2 == 0) {
            /*SL:497*/this._flushBuffer();
            /*SL:498*/v2 = this._outputEnd - this._outputTail;
        }
        /*SL:501*/if (v2 >= v1) {
            /*SL:502*/a1.getChars(0, v1, this._outputBuffer, this._outputTail);
            /*SL:503*/this._outputTail += v1;
        }
        else {
            /*SL:505*/this.writeRawLong(a1);
        }
    }
    
    @Override
    public void writeRaw(final String a1, final int a2, final int a3) throws IOException {
        int v1 = /*EL:513*/this._outputEnd - this._outputTail;
        /*SL:515*/if (v1 < a3) {
            /*SL:516*/this._flushBuffer();
            /*SL:517*/v1 = this._outputEnd - this._outputTail;
        }
        /*SL:520*/if (v1 >= a3) {
            /*SL:521*/a1.getChars(a2, a2 + a3, this._outputBuffer, this._outputTail);
            /*SL:522*/this._outputTail += a3;
        }
        else {
            /*SL:524*/this.writeRawLong(a1.substring(a2, a2 + a3));
        }
    }
    
    @Override
    public void writeRaw(final SerializableString a1) throws IOException {
        /*SL:531*/this.writeRaw(a1.getValue());
    }
    
    @Override
    public void writeRaw(final char[] a3, final int v1, final int v2) throws IOException {
        /*SL:538*/if (v2 < 32) {
            final int a4 = /*EL:539*/this._outputEnd - this._outputTail;
            /*SL:540*/if (v2 > a4) {
                /*SL:541*/this._flushBuffer();
            }
            /*SL:543*/System.arraycopy(a3, v1, this._outputBuffer, this._outputTail, v2);
            /*SL:544*/this._outputTail += v2;
            /*SL:545*/return;
        }
        /*SL:548*/this._flushBuffer();
        /*SL:549*/this._writer.write(a3, v1, v2);
    }
    
    @Override
    public void writeRaw(final char a1) throws IOException {
        /*SL:555*/if (this._outputTail >= this._outputEnd) {
            /*SL:556*/this._flushBuffer();
        }
        /*SL:558*/this._outputBuffer[this._outputTail++] = a1;
    }
    
    private void writeRawLong(final String v2) throws IOException {
        final int v3 = /*EL:563*/this._outputEnd - this._outputTail;
        /*SL:565*/v2.getChars(0, v3, this._outputBuffer, this._outputTail);
        /*SL:566*/this._outputTail += v3;
        /*SL:567*/this._flushBuffer();
        int v4 = /*EL:568*/v3;
        int v5;
        int a1;
        /*SL:571*/for (v5 = v2.length() - v3; v5 > this._outputEnd; /*SL:578*/v5 -= a1) {
            a1 = this._outputEnd;
            v2.getChars(v4, v4 + a1, this._outputBuffer, 0);
            this._outputHead = 0;
            this._outputTail = a1;
            this._flushBuffer();
            v4 += a1;
        }
        /*SL:581*/v2.getChars(v4, v4 + v5, this._outputBuffer, 0);
        /*SL:582*/this._outputHead = 0;
        /*SL:583*/this._outputTail = v5;
    }
    
    @Override
    public void writeBinary(final Base64Variant a1, final byte[] a2, final int a3, final int a4) throws IOException, JsonGenerationException {
        /*SL:596*/this._verifyValueWrite("write a binary value");
        /*SL:598*/if (this._outputTail >= this._outputEnd) {
            /*SL:599*/this._flushBuffer();
        }
        /*SL:601*/this._outputBuffer[this._outputTail++] = this._quoteChar;
        /*SL:602*/this._writeBinary(a1, a2, a3, a3 + a4);
        /*SL:604*/if (this._outputTail >= this._outputEnd) {
            /*SL:605*/this._flushBuffer();
        }
        /*SL:607*/this._outputBuffer[this._outputTail++] = this._quoteChar;
    }
    
    @Override
    public int writeBinary(final Base64Variant v1, final InputStream v2, final int v3) throws IOException, JsonGenerationException {
        /*SL:615*/this._verifyValueWrite("write a binary value");
        /*SL:617*/if (this._outputTail >= this._outputEnd) {
            /*SL:618*/this._flushBuffer();
        }
        /*SL:620*/this._outputBuffer[this._outputTail++] = this._quoteChar;
        final byte[] v4 = /*EL:621*/this._ioContext.allocBase64Buffer();
        try {
            /*SL:624*/if (v3 < 0) {
                final int a1 = /*EL:625*/this._writeBinary(v1, v2, v4);
            }
            else {
                final int a2 = /*EL:627*/this._writeBinary(v1, v2, v4, v3);
                /*SL:628*/if (a2 > 0) {
                    /*SL:629*/this._reportError("Too few bytes available: missing " + a2 + " bytes (out of " + v3 + ")");
                }
            }
        }
        finally {
            /*SL:634*/this._ioContext.releaseBase64Buffer(v4);
        }
        /*SL:637*/if (this._outputTail >= this._outputEnd) {
            /*SL:638*/this._flushBuffer();
        }
        /*SL:640*/this._outputBuffer[this._outputTail++] = this._quoteChar;
        /*SL:641*/return v3;
    }
    
    @Override
    public void writeNumber(final short a1) throws IOException {
        /*SL:653*/this._verifyValueWrite("write a number");
        /*SL:654*/if (this._cfgNumbersAsStrings) {
            /*SL:655*/this._writeQuotedShort(a1);
            /*SL:656*/return;
        }
        /*SL:659*/if (this._outputTail + 6 >= this._outputEnd) {
            /*SL:660*/this._flushBuffer();
        }
        /*SL:662*/this._outputTail = NumberOutput.outputInt(a1, this._outputBuffer, this._outputTail);
    }
    
    private void _writeQuotedShort(final short a1) throws IOException {
        /*SL:666*/if (this._outputTail + 8 >= this._outputEnd) {
            /*SL:667*/this._flushBuffer();
        }
        /*SL:669*/this._outputBuffer[this._outputTail++] = this._quoteChar;
        /*SL:670*/this._outputTail = NumberOutput.outputInt(a1, this._outputBuffer, this._outputTail);
        /*SL:671*/this._outputBuffer[this._outputTail++] = this._quoteChar;
    }
    
    @Override
    public void writeNumber(final int a1) throws IOException {
        /*SL:677*/this._verifyValueWrite("write a number");
        /*SL:678*/if (this._cfgNumbersAsStrings) {
            /*SL:679*/this._writeQuotedInt(a1);
            /*SL:680*/return;
        }
        /*SL:683*/if (this._outputTail + 11 >= this._outputEnd) {
            /*SL:684*/this._flushBuffer();
        }
        /*SL:686*/this._outputTail = NumberOutput.outputInt(a1, this._outputBuffer, this._outputTail);
    }
    
    private void _writeQuotedInt(final int a1) throws IOException {
        /*SL:690*/if (this._outputTail + 13 >= this._outputEnd) {
            /*SL:691*/this._flushBuffer();
        }
        /*SL:693*/this._outputBuffer[this._outputTail++] = this._quoteChar;
        /*SL:694*/this._outputTail = NumberOutput.outputInt(a1, this._outputBuffer, this._outputTail);
        /*SL:695*/this._outputBuffer[this._outputTail++] = this._quoteChar;
    }
    
    @Override
    public void writeNumber(final long a1) throws IOException {
        /*SL:701*/this._verifyValueWrite("write a number");
        /*SL:702*/if (this._cfgNumbersAsStrings) {
            /*SL:703*/this._writeQuotedLong(a1);
            /*SL:704*/return;
        }
        /*SL:706*/if (this._outputTail + 21 >= this._outputEnd) {
            /*SL:708*/this._flushBuffer();
        }
        /*SL:710*/this._outputTail = NumberOutput.outputLong(a1, this._outputBuffer, this._outputTail);
    }
    
    private void _writeQuotedLong(final long a1) throws IOException {
        /*SL:714*/if (this._outputTail + 23 >= this._outputEnd) {
            /*SL:715*/this._flushBuffer();
        }
        /*SL:717*/this._outputBuffer[this._outputTail++] = this._quoteChar;
        /*SL:718*/this._outputTail = NumberOutput.outputLong(a1, this._outputBuffer, this._outputTail);
        /*SL:719*/this._outputBuffer[this._outputTail++] = this._quoteChar;
    }
    
    @Override
    public void writeNumber(final BigInteger a1) throws IOException {
        /*SL:727*/this._verifyValueWrite("write a number");
        /*SL:728*/if (a1 == null) {
            /*SL:729*/this._writeNull();
        }
        else/*SL:730*/ if (this._cfgNumbersAsStrings) {
            /*SL:731*/this._writeQuotedRaw(a1.toString());
        }
        else {
            /*SL:733*/this.writeRaw(a1.toString());
        }
    }
    
    @Override
    public void writeNumber(final double a1) throws IOException {
        /*SL:741*/if (this._cfgNumbersAsStrings || (this.isEnabled(Feature.QUOTE_NON_NUMERIC_NUMBERS) && (Double.isNaN(a1) || Double.isInfinite(a1)))) {
            /*SL:744*/this.writeString(String.valueOf(a1));
            /*SL:745*/return;
        }
        /*SL:748*/this._verifyValueWrite("write a number");
        /*SL:749*/this.writeRaw(String.valueOf(a1));
    }
    
    @Override
    public void writeNumber(final float a1) throws IOException {
        /*SL:755*/if (this._cfgNumbersAsStrings || (this.isEnabled(Feature.QUOTE_NON_NUMERIC_NUMBERS) && (Float.isNaN(a1) || Float.isInfinite(a1)))) {
            /*SL:758*/this.writeString(String.valueOf(a1));
            /*SL:759*/return;
        }
        /*SL:762*/this._verifyValueWrite("write a number");
        /*SL:763*/this.writeRaw(String.valueOf(a1));
    }
    
    @Override
    public void writeNumber(final BigDecimal a1) throws IOException {
        /*SL:770*/this._verifyValueWrite("write a number");
        /*SL:771*/if (a1 == null) {
            /*SL:772*/this._writeNull();
        }
        else/*SL:773*/ if (this._cfgNumbersAsStrings) {
            /*SL:774*/this._writeQuotedRaw(this._asString(a1));
        }
        else {
            /*SL:776*/this.writeRaw(this._asString(a1));
        }
    }
    
    @Override
    public void writeNumber(final String a1) throws IOException {
        /*SL:783*/this._verifyValueWrite("write a number");
        /*SL:784*/if (this._cfgNumbersAsStrings) {
            /*SL:785*/this._writeQuotedRaw(a1);
        }
        else {
            /*SL:787*/this.writeRaw(a1);
        }
    }
    
    private void _writeQuotedRaw(final String a1) throws IOException {
        /*SL:793*/if (this._outputTail >= this._outputEnd) {
            /*SL:794*/this._flushBuffer();
        }
        /*SL:796*/this._outputBuffer[this._outputTail++] = this._quoteChar;
        /*SL:797*/this.writeRaw(a1);
        /*SL:798*/if (this._outputTail >= this._outputEnd) {
            /*SL:799*/this._flushBuffer();
        }
        /*SL:801*/this._outputBuffer[this._outputTail++] = this._quoteChar;
    }
    
    @Override
    public void writeBoolean(final boolean a1) throws IOException {
        /*SL:807*/this._verifyValueWrite("write a boolean value");
        /*SL:808*/if (this._outputTail + 5 >= this._outputEnd) {
            /*SL:809*/this._flushBuffer();
        }
        int v1 = /*EL:811*/this._outputTail;
        final char[] v2 = /*EL:812*/this._outputBuffer;
        /*SL:813*/if (a1) {
            /*SL:814*/v2[v1] = 't';
            /*SL:815*/v2[++v1] = 'r';
            /*SL:816*/v2[++v1] = 'u';
            /*SL:817*/v2[++v1] = 'e';
        }
        else {
            /*SL:819*/v2[v1] = 'f';
            /*SL:820*/v2[++v1] = 'a';
            /*SL:821*/v2[++v1] = 'l';
            /*SL:822*/v2[++v1] = 's';
            /*SL:823*/v2[++v1] = 'e';
        }
        /*SL:825*/this._outputTail = v1 + 1;
    }
    
    @Override
    public void writeNull() throws IOException {
        /*SL:830*/this._verifyValueWrite("write a null");
        /*SL:831*/this._writeNull();
    }
    
    @Override
    protected final void _verifyValueWrite(final String a1) throws IOException {
        final int v1 = /*EL:843*/this._writeContext.writeValue();
        /*SL:844*/if (this._cfgPrettyPrinter != null) {
            /*SL:846*/this._verifyPrettyValueWrite(a1, v1);
            /*SL:847*/return;
        }
        char v2 = '\0';
        /*SL:850*/switch (v1) {
            default: {
                /*SL:853*/return;
            }
            case 1: {
                /*SL:855*/v2 = ',';
                /*SL:856*/break;
            }
            case 2: {
                /*SL:858*/v2 = ':';
                /*SL:859*/break;
            }
            case 3: {
                /*SL:861*/if (this._rootValueSeparator != null) {
                    /*SL:862*/this.writeRaw(this._rootValueSeparator.getValue());
                }
                /*SL:864*/return;
            }
            case 5: {
                /*SL:866*/this._reportCantWriteValueExpectName(a1);
                /*SL:867*/return;
            }
        }
        /*SL:869*/if (this._outputTail >= this._outputEnd) {
            /*SL:870*/this._flushBuffer();
        }
        /*SL:872*/this._outputBuffer[this._outputTail++] = v2;
    }
    
    @Override
    public void flush() throws IOException {
        /*SL:884*/this._flushBuffer();
        /*SL:885*/if (this._writer != null && /*EL:886*/this.isEnabled(Feature.FLUSH_PASSED_TO_STREAM)) {
            /*SL:887*/this._writer.flush();
        }
    }
    
    @Override
    public void close() throws IOException {
        /*SL:895*/super.close();
        /*SL:901*/if (this._outputBuffer != null && this.isEnabled(Feature.AUTO_CLOSE_JSON_CONTENT)) {
            while (true) {
                final JsonStreamContext v1 = /*EL:904*/this.getOutputContext();
                /*SL:905*/if (v1.inArray()) {
                    /*SL:906*/this.writeEndArray();
                }
                else {
                    /*SL:907*/if (!v1.inObject()) {
                        break;
                    }
                    /*SL:908*/this.writeEndObject();
                }
            }
        }
        /*SL:914*/this._flushBuffer();
        /*SL:915*/this._outputHead = 0;
        /*SL:916*/this._outputTail = 0;
        /*SL:924*/if (this._writer != null) {
            /*SL:925*/if (this._ioContext.isResourceManaged() || this.isEnabled(Feature.AUTO_CLOSE_TARGET)) {
                /*SL:926*/this._writer.close();
            }
            else/*SL:927*/ if (this.isEnabled(Feature.FLUSH_PASSED_TO_STREAM)) {
                /*SL:929*/this._writer.flush();
            }
        }
        /*SL:933*/this._releaseBuffers();
    }
    
    @Override
    protected void _releaseBuffers() {
        char[] v1 = /*EL:939*/this._outputBuffer;
        /*SL:940*/if (v1 != null) {
            /*SL:941*/this._outputBuffer = null;
            /*SL:942*/this._ioContext.releaseConcatBuffer(v1);
        }
        /*SL:944*/v1 = this._charBuffer;
        /*SL:945*/if (v1 != null) {
            /*SL:946*/this._charBuffer = null;
            /*SL:947*/this._ioContext.releaseNameCopyBuffer(v1);
        }
    }
    
    private void _writeString(final String a1) throws IOException {
        final int v1 = /*EL:964*/a1.length();
        /*SL:965*/if (v1 > this._outputEnd) {
            /*SL:966*/this._writeLongString(a1);
            /*SL:967*/return;
        }
        /*SL:972*/if (this._outputTail + v1 > this._outputEnd) {
            /*SL:973*/this._flushBuffer();
        }
        /*SL:975*/a1.getChars(0, v1, this._outputBuffer, this._outputTail);
        /*SL:977*/if (this._characterEscapes != null) {
            /*SL:978*/this._writeStringCustom(v1);
        }
        else/*SL:979*/ if (this._maximumNonEscapedChar != 0) {
            /*SL:980*/this._writeStringASCII(v1, this._maximumNonEscapedChar);
        }
        else {
            /*SL:982*/this._writeString2(v1);
        }
    }
    
    private void _writeString2(final int v-3) throws IOException {
        final int n = /*EL:989*/this._outputTail + v-3;
        final int[] outputEscapes = /*EL:990*/this._outputEscapes;
        final int v0 = /*EL:991*/outputEscapes.length;
        /*SL:994*/Label_0137:
        while (this._outputTail < n) {
            while (true) {
                final char a1 = /*EL:998*/this._outputBuffer[this._outputTail];
                /*SL:999*/if (a1 < v0 && outputEscapes[a1] != 0) {
                    final int v = /*EL:1011*/this._outputTail - this._outputHead;
                    /*SL:1012*/if (v > 0) {
                        /*SL:1013*/this._writer.write(this._outputBuffer, this._outputHead, v);
                    }
                    final char v2 = /*EL:1018*/this._outputBuffer[this._outputTail++];
                    /*SL:1019*/this._prependOrWriteCharacterEscape(v2, outputEscapes[v2]);
                    /*SL:1020*/break;
                }
                if (++this._outputTail >= n) {
                    break Label_0137;
                }
            }
        }
    }
    
    private void _writeLongString(final String v-3) throws IOException {
        /*SL:1030*/this._flushBuffer();
        final int length = /*EL:1033*/v-3.length();
        int i = /*EL:1034*/0;
        /*SL:1048*/do {
            final int a1 = this._outputEnd;
            final int v1 = (i + a1 > length) ? (length - i) : a1;
            v-3.getChars(i, i + v1, this._outputBuffer, 0);
            if (this._characterEscapes != null) {
                this._writeSegmentCustom(v1);
            }
            else if (this._maximumNonEscapedChar != 0) {
                this._writeSegmentASCII(v1, this._maximumNonEscapedChar);
            }
            else {
                this._writeSegment(v1);
            }
            i += v1;
        } while (i < length);
    }
    
    private void _writeSegment(final int v-5) throws IOException {
        final int[] outputEscapes = /*EL:1062*/this._outputEscapes;
        final int length = /*EL:1063*/outputEscapes.length;
        int prependOrWriteCharacterEscape;
        char a1;
        /*SL:1069*/for (int i = prependOrWriteCharacterEscape = 0; i < v-5; /*SL:1093*/++i, /*SL:1095*/prependOrWriteCharacterEscape = this._prependOrWriteCharacterEscape(this._outputBuffer, i, v-5, a1, outputEscapes[a1])) {
            do {
                a1 = this._outputBuffer[i];
                if (a1 < length && outputEscapes[a1] != 0) {
                    break;
                }
            } while (++i < v-5);
            final int v1 = i - prependOrWriteCharacterEscape;
            if (v1 > 0) {
                this._writer.write(this._outputBuffer, prependOrWriteCharacterEscape, v1);
                if (i >= v-5) {
                    break;
                }
            }
        }
    }
    
    private void _writeString(final char[] v-6, int v-5, int v-4) throws IOException {
        /*SL:1105*/if (this._characterEscapes != null) {
            /*SL:1106*/this._writeStringCustom(v-6, v-5, v-4);
            /*SL:1107*/return;
        }
        /*SL:1109*/if (this._maximumNonEscapedChar != 0) {
            /*SL:1110*/this._writeStringASCII(v-6, v-5, v-4, this._maximumNonEscapedChar);
            /*SL:1111*/return;
        }
        /*SL:1118*/v-4 += v-5;
        final int[] outputEscapes = /*EL:1119*/this._outputEscapes;
        final int length = /*EL:1120*/outputEscapes.length;
        /*SL:1121*/while (v-5 < v-4) {
            int a2 = /*EL:1122*/v-5;
            do {
                /*SL:1125*/a2 = v-6[v-5];
                /*SL:1126*/if (a2 < length && outputEscapes[a2] != 0) {
                    /*SL:1127*/break;
                }
            } while (/*EL:1129*/++v-5 < v-4);
            final int a3 = /*EL:1135*/v-5 - a2;
            /*SL:1136*/if (a3 < 32) {
                /*SL:1138*/if (this._outputTail + a3 > this._outputEnd) {
                    /*SL:1139*/this._flushBuffer();
                }
                /*SL:1141*/if (a3 > 0) {
                    /*SL:1142*/System.arraycopy(v-6, a2, this._outputBuffer, this._outputTail, a3);
                    /*SL:1143*/this._outputTail += a3;
                }
            }
            else {
                /*SL:1146*/this._flushBuffer();
                /*SL:1147*/this._writer.write(v-6, a2, a3);
            }
            /*SL:1150*/if (v-5 >= v-4) {
                /*SL:1151*/break;
            }
            final char v1 = /*EL:1154*/v-6[v-5++];
            /*SL:1155*/this._appendCharacterEscape(v1, outputEscapes[v1]);
        }
    }
    
    private void _writeStringASCII(final int v2, final int v3) throws IOException, JsonGenerationException {
        final int v4 = /*EL:1173*/this._outputTail + v2;
        final int[] v5 = /*EL:1174*/this._outputEscapes;
        final int v6 = /*EL:1175*/Math.min(v5.length, v3 + 1);
        int v7 = /*EL:1176*/0;
        /*SL:1179*/Label_0027:
        while (this._outputTail < v4) {
            /*SL:1194*/do {
                final char a1 = this._outputBuffer[this._outputTail];
                if (a1 < v6) {
                    v7 = v5[a1];
                    if (v7 == 0) {
                        continue;
                    }
                }
                else {
                    if (a1 <= v3) {
                        continue;
                    }
                    v7 = -1;
                }
                final int a2 = /*EL:1198*/this._outputTail - this._outputHead;
                /*SL:1199*/if (a2 > 0) {
                    /*SL:1200*/this._writer.write(this._outputBuffer, this._outputHead, a2);
                }
                /*SL:1202*/++this._outputTail;
                /*SL:1203*/this._prependOrWriteCharacterEscape(a1, v7);
                /*SL:1204*/continue Label_0027;
            } while (++this._outputTail < v4);
            break;
        }
    }
    
    private void _writeSegmentASCII(final int v2, final int v3) throws IOException, JsonGenerationException {
        final int[] v4 = /*EL:1210*/this._outputEscapes;
        final int v5 = /*EL:1211*/Math.min(v4.length, v3 + 1);
        int v6 = /*EL:1213*/0;
        int v7 = /*EL:1214*/0;
        int v8 = /*EL:1215*/v6;
        /*SL:1218*/while (v6 < v2) {
            char a1;
            /*SL:1232*/do {
                a1 = this._outputBuffer[v6];
                if (a1 < v5) {
                    v7 = v4[a1];
                    if (v7 != 0) {
                        break;
                    }
                    continue;
                }
                else {
                    if (a1 > v3) {
                        v7 = -1;
                        break;
                    }
                    continue;
                }
            } while (++v6 < v2);
            final int a2 = /*EL:1236*/v6 - v8;
            /*SL:1237*/if (a2 > 0) {
                /*SL:1238*/this._writer.write(this._outputBuffer, v8, a2);
                /*SL:1239*/if (v6 >= v2) {
                    /*SL:1240*/break;
                }
            }
            /*SL:1243*/++v6;
            /*SL:1244*/v8 = this._prependOrWriteCharacterEscape(this._outputBuffer, v6, v2, a1, v7);
        }
    }
    
    private void _writeStringASCII(final char[] v1, int v2, int v3, final int v4) throws IOException, JsonGenerationException {
        /*SL:1252*/v3 += v2;
        final int[] v5 = /*EL:1253*/this._outputEscapes;
        final int v6 = /*EL:1254*/Math.min(v5.length, v4 + 1);
        int v7 = /*EL:1256*/0;
        /*SL:1258*/while (v2 < v3) {
            final int a1 = /*EL:1259*/v2;
            char a2;
            /*SL:1273*/do {
                a2 = v1[v2];
                if (a2 < v6) {
                    v7 = v5[a2];
                    if (v7 != 0) {
                        break;
                    }
                    continue;
                }
                else {
                    if (a2 > v4) {
                        v7 = -1;
                        break;
                    }
                    continue;
                }
            } while (++v2 < v3);
            final int a3 = /*EL:1279*/v2 - a1;
            /*SL:1280*/if (a3 < 32) {
                /*SL:1282*/if (this._outputTail + a3 > this._outputEnd) {
                    /*SL:1283*/this._flushBuffer();
                }
                /*SL:1285*/if (a3 > 0) {
                    /*SL:1286*/System.arraycopy(v1, a1, this._outputBuffer, this._outputTail, a3);
                    /*SL:1287*/this._outputTail += a3;
                }
            }
            else {
                /*SL:1290*/this._flushBuffer();
                /*SL:1291*/this._writer.write(v1, a1, a3);
            }
            /*SL:1294*/if (v2 >= v3) {
                /*SL:1295*/break;
            }
            /*SL:1298*/++v2;
            /*SL:1299*/this._appendCharacterEscape(a2, v7);
        }
    }
    
    private void _writeStringCustom(final int v-7) throws IOException, JsonGenerationException {
        final int n = /*EL:1317*/this._outputTail + v-7;
        final int[] outputEscapes = /*EL:1318*/this._outputEscapes;
        final int n2 = /*EL:1319*/(this._maximumNonEscapedChar < 1) ? 65535 : this._maximumNonEscapedChar;
        final int min = /*EL:1320*/Math.min(outputEscapes.length, n2 + 1);
        int v-8 = /*EL:1321*/0;
        final CharacterEscapes characterEscapes = /*EL:1322*/this._characterEscapes;
        /*SL:1325*/Label_0052:
        while (this._outputTail < n) {
            /*SL:1345*/do {
                final char a1 = this._outputBuffer[this._outputTail];
                if (a1 < min) {
                    v-8 = outputEscapes[a1];
                    if (v-8 == 0) {
                        continue;
                    }
                }
                else if (a1 > n2) {
                    v-8 = -1;
                }
                else {
                    if ((this._currentEscape = characterEscapes.getEscapeSequence(a1)) == null) {
                        continue;
                    }
                    v-8 = -2;
                }
                final int v1 = /*EL:1349*/this._outputTail - this._outputHead;
                /*SL:1350*/if (v1 > 0) {
                    /*SL:1351*/this._writer.write(this._outputBuffer, this._outputHead, v1);
                }
                /*SL:1353*/++this._outputTail;
                /*SL:1354*/this._prependOrWriteCharacterEscape(a1, v-8);
                /*SL:1355*/continue Label_0052;
            } while (++this._outputTail < n);
            break;
        }
    }
    
    private void _writeSegmentCustom(final int v-8) throws IOException, JsonGenerationException {
        final int[] outputEscapes = /*EL:1361*/this._outputEscapes;
        final int n = /*EL:1362*/(this._maximumNonEscapedChar < 1) ? 65535 : this._maximumNonEscapedChar;
        final int min = /*EL:1363*/Math.min(outputEscapes.length, n + 1);
        final CharacterEscapes characterEscapes = /*EL:1364*/this._characterEscapes;
        int i = /*EL:1366*/0;
        int v2 = /*EL:1367*/0;
        int prependOrWriteCharacterEscape = /*EL:1368*/i;
        /*SL:1371*/while (i < v-8) {
            char a1;
            /*SL:1390*/do {
                a1 = this._outputBuffer[i];
                if (a1 < min) {
                    v2 = outputEscapes[a1];
                    if (v2 != 0) {
                        break;
                    }
                    continue;
                }
                else {
                    if (a1 > n) {
                        v2 = -1;
                        break;
                    }
                    if ((this._currentEscape = characterEscapes.getEscapeSequence(a1)) != null) {
                        v2 = -2;
                        break;
                    }
                    continue;
                }
            } while (++i < v-8);
            final int v1 = /*EL:1394*/i - prependOrWriteCharacterEscape;
            /*SL:1395*/if (v1 > 0) {
                /*SL:1396*/this._writer.write(this._outputBuffer, prependOrWriteCharacterEscape, v1);
                /*SL:1397*/if (i >= v-8) {
                    /*SL:1398*/break;
                }
            }
            /*SL:1401*/++i;
            /*SL:1402*/prependOrWriteCharacterEscape = this._prependOrWriteCharacterEscape(this._outputBuffer, i, v-8, a1, v2);
        }
    }
    
    private void _writeStringCustom(final char[] v2, int v3, int v4) throws IOException, JsonGenerationException {
        /*SL:1409*/v4 += v3;
        final int[] v5 = /*EL:1410*/this._outputEscapes;
        final int v6 = /*EL:1411*/(this._maximumNonEscapedChar < 1) ? 65535 : this._maximumNonEscapedChar;
        final int v7 = /*EL:1412*/Math.min(v5.length, v6 + 1);
        final CharacterEscapes v8 = /*EL:1413*/this._characterEscapes;
        int v9 = /*EL:1415*/0;
        /*SL:1417*/while (v3 < v4) {
            final int a1 = /*EL:1418*/v3;
            char a2;
            /*SL:1437*/do {
                a2 = v2[v3];
                if (a2 < v7) {
                    v9 = v5[a2];
                    if (v9 != 0) {
                        break;
                    }
                    continue;
                }
                else {
                    if (a2 > v6) {
                        v9 = -1;
                        break;
                    }
                    if ((this._currentEscape = v8.getEscapeSequence(a2)) != null) {
                        v9 = -2;
                        break;
                    }
                    continue;
                }
            } while (++v3 < v4);
            final int a3 = /*EL:1443*/v3 - a1;
            /*SL:1444*/if (a3 < 32) {
                /*SL:1446*/if (this._outputTail + a3 > this._outputEnd) {
                    /*SL:1447*/this._flushBuffer();
                }
                /*SL:1449*/if (a3 > 0) {
                    /*SL:1450*/System.arraycopy(v2, a1, this._outputBuffer, this._outputTail, a3);
                    /*SL:1451*/this._outputTail += a3;
                }
            }
            else {
                /*SL:1454*/this._flushBuffer();
                /*SL:1455*/this._writer.write(v2, a1, a3);
            }
            /*SL:1458*/if (v3 >= v4) {
                /*SL:1459*/break;
            }
            /*SL:1462*/++v3;
            /*SL:1463*/this._appendCharacterEscape(a2, v9);
        }
    }
    
    protected final void _writeBinary(final Base64Variant a4, final byte[] v1, int v2, final int v3) throws IOException, JsonGenerationException {
        final int v4 = /*EL:1477*/v3 - 3;
        final int v5 = /*EL:1479*/this._outputEnd - 6;
        int v6 = /*EL:1480*/a4.getMaxLineLength() >> 2;
        /*SL:1483*/while (v2 <= v4) {
            /*SL:1484*/if (this._outputTail > v5) {
                /*SL:1485*/this._flushBuffer();
            }
            int a5 = /*EL:1488*/v1[v2++] << 8;
            /*SL:1489*/a5 |= (v1[v2++] & 0xFF);
            /*SL:1490*/a5 = (a5 << 8 | (v1[v2++] & 0xFF));
            /*SL:1491*/this._outputTail = a4.encodeBase64Chunk(a5, this._outputBuffer, this._outputTail);
            /*SL:1492*/if (--v6 <= 0) {
                /*SL:1494*/this._outputBuffer[this._outputTail++] = '\\';
                /*SL:1495*/this._outputBuffer[this._outputTail++] = 'n';
                /*SL:1496*/v6 = a4.getMaxLineLength() >> 2;
            }
        }
        final int v7 = /*EL:1501*/v3 - v2;
        /*SL:1502*/if (v7 > 0) {
            /*SL:1503*/if (this._outputTail > v5) {
                /*SL:1504*/this._flushBuffer();
            }
            int a6 = /*EL:1506*/v1[v2++] << 16;
            /*SL:1507*/if (v7 == 2) {
                /*SL:1508*/a6 |= (v1[v2++] & 0xFF) << 8;
            }
            /*SL:1510*/this._outputTail = a4.encodeBase64Partial(a6, v7, this._outputBuffer, this._outputTail);
        }
    }
    
    protected final int _writeBinary(final Base64Variant v2, final InputStream v3, final byte[] v4, int v5) throws IOException, JsonGenerationException {
        int v6 = /*EL:1519*/0;
        int v7 = /*EL:1520*/0;
        int v8 = /*EL:1521*/-3;
        final int v9 = /*EL:1524*/this._outputEnd - 6;
        int v10 = /*EL:1525*/v2.getMaxLineLength() >> 2;
        /*SL:1527*/while (v5 > 2) {
            /*SL:1528*/if (v6 > v8) {
                /*SL:1529*/v7 = this._readMore(v3, v4, v6, v7, v5);
                /*SL:1530*/v6 = 0;
                /*SL:1531*/if (v7 < 3) {
                    /*SL:1532*/break;
                }
                /*SL:1534*/v8 = v7 - 3;
            }
            /*SL:1536*/if (this._outputTail > v9) {
                /*SL:1537*/this._flushBuffer();
            }
            int a1 = /*EL:1539*/v4[v6++] << 8;
            /*SL:1540*/a1 |= (v4[v6++] & 0xFF);
            /*SL:1541*/a1 = (a1 << 8 | (v4[v6++] & 0xFF));
            /*SL:1542*/v5 -= 3;
            /*SL:1543*/this._outputTail = v2.encodeBase64Chunk(a1, this._outputBuffer, this._outputTail);
            /*SL:1544*/if (--v10 <= 0) {
                /*SL:1545*/this._outputBuffer[this._outputTail++] = '\\';
                /*SL:1546*/this._outputBuffer[this._outputTail++] = 'n';
                /*SL:1547*/v10 = v2.getMaxLineLength() >> 2;
            }
        }
        /*SL:1552*/if (v5 > 0) {
            /*SL:1553*/v7 = this._readMore(v3, v4, v6, v7, v5);
            /*SL:1554*/v6 = 0;
            /*SL:1555*/if (v7 > 0) {
                /*SL:1556*/if (this._outputTail > v9) {
                    /*SL:1557*/this._flushBuffer();
                }
                int a2 = /*EL:1559*/v4[v6++] << 16;
                final int a4;
                /*SL:1561*/if (v6 < v7) {
                    /*SL:1562*/a2 |= (v4[v6] & 0xFF) << 8;
                    final int a3 = /*EL:1563*/2;
                }
                else {
                    /*SL:1565*/a4 = 1;
                }
                /*SL:1567*/this._outputTail = v2.encodeBase64Partial(a2, a4, this._outputBuffer, this._outputTail);
                /*SL:1568*/v5 -= a4;
            }
        }
        /*SL:1571*/return v5;
    }
    
    protected final int _writeBinary(final Base64Variant v2, final InputStream v3, final byte[] v4) throws IOException, JsonGenerationException {
        int v5 = /*EL:1579*/0;
        int v6 = /*EL:1580*/0;
        int v7 = /*EL:1581*/-3;
        int v8 = /*EL:1582*/0;
        final int v9 = /*EL:1585*/this._outputEnd - 6;
        int v10 = /*EL:1586*/v2.getMaxLineLength() >> 2;
        while (true) {
            /*SL:1590*/if (v5 > v7) {
                /*SL:1591*/v6 = this._readMore(v3, v4, v5, v6, v4.length);
                /*SL:1592*/v5 = 0;
                /*SL:1593*/if (v6 < 3) {
                    break;
                }
                /*SL:1596*/v7 = v6 - 3;
            }
            /*SL:1598*/if (this._outputTail > v9) {
                /*SL:1599*/this._flushBuffer();
            }
            int a1 = /*EL:1602*/v4[v5++] << 8;
            /*SL:1603*/a1 |= (v4[v5++] & 0xFF);
            /*SL:1604*/a1 = (a1 << 8 | (v4[v5++] & 0xFF));
            /*SL:1605*/v8 += 3;
            /*SL:1606*/this._outputTail = v2.encodeBase64Chunk(a1, this._outputBuffer, this._outputTail);
            /*SL:1607*/if (--v10 <= 0) {
                /*SL:1608*/this._outputBuffer[this._outputTail++] = '\\';
                /*SL:1609*/this._outputBuffer[this._outputTail++] = 'n';
                /*SL:1610*/v10 = v2.getMaxLineLength() >> 2;
            }
        }
        /*SL:1615*/if (v5 < v6) {
            /*SL:1616*/if (this._outputTail > v9) {
                /*SL:1617*/this._flushBuffer();
            }
            int a2 = /*EL:1619*/v4[v5++] << 16;
            int a3 = /*EL:1620*/1;
            /*SL:1621*/if (v5 < v6) {
                /*SL:1622*/a2 |= (v4[v5] & 0xFF) << 8;
                /*SL:1623*/a3 = 2;
            }
            /*SL:1625*/v8 += a3;
            /*SL:1626*/this._outputTail = v2.encodeBase64Partial(a2, a3, this._outputBuffer, this._outputTail);
        }
        /*SL:1628*/return v8;
    }
    
    private int _readMore(final InputStream a4, final byte[] a5, int v1, int v2, int v3) throws IOException {
        int v4;
        /*SL:1637*/for (v4 = 0; v1 < v2; /*SL:1638*/a5[v4++] = a5[v1++]) {}
        /*SL:1640*/v1 = 0;
        /*SL:1641*/v2 = v4;
        /*SL:1642*/v3 = Math.min(v3, a5.length);
        /*SL:1654*/do {
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
        /*SL:1655*/return v2;
    }
    
    private final void _writeNull() throws IOException {
        /*SL:1666*/if (this._outputTail + 4 >= this._outputEnd) {
            /*SL:1667*/this._flushBuffer();
        }
        int v1 = /*EL:1669*/this._outputTail;
        final char[] v2 = /*EL:1670*/this._outputBuffer;
        /*SL:1671*/v2[v1] = 'n';
        /*SL:1672*/v2[++v1] = 'u';
        /*SL:1673*/v2[++v1] = 'l';
        /*SL:1674*/v2[++v1] = 'l';
        /*SL:1675*/this._outputTail = v1 + 1;
    }
    
    private void _prependOrWriteCharacterEscape(char v-3, final int v-2) throws IOException, JsonGenerationException {
        /*SL:1692*/if (v-2 >= 0) {
            /*SL:1693*/if (this._outputTail >= 2) {
                int a1 = /*EL:1694*/this._outputTail - 2;
                /*SL:1695*/this._outputHead = a1;
                /*SL:1696*/this._outputBuffer[a1++] = '\\';
                /*SL:1697*/this._outputBuffer[a1] = (char)v-2;
                /*SL:1698*/return;
            }
            char[] a2 = /*EL:1701*/this._entityBuffer;
            /*SL:1702*/if (a2 == null) {
                /*SL:1703*/a2 = this._allocateEntityBuffer();
            }
            /*SL:1705*/this._outputHead = this._outputTail;
            /*SL:1706*/a2[1] = (char)v-2;
            /*SL:1707*/this._writer.write(a2, 0, 2);
        }
        else/*SL:1710*/ if (v-2 != -2) {
            /*SL:1711*/if (this._outputTail >= 6) {
                final char[] array = /*EL:1712*/this._outputBuffer;
                int v0 = /*EL:1713*/this._outputTail - 6;
                /*SL:1715*/array[this._outputHead = v0] = '\\';
                /*SL:1716*/array[++v0] = 'u';
                /*SL:1718*/if (v-3 > '\u00ff') {
                    final int v = /*EL:1719*/v-3 >> 8 & '\u00ff';
                    /*SL:1720*/array[++v0] = WriterBasedJsonGenerator.HEX_CHARS[v >> 4];
                    /*SL:1721*/array[++v0] = WriterBasedJsonGenerator.HEX_CHARS[v & 0xF];
                    /*SL:1722*/v-3 &= '\u00ff';
                }
                else {
                    /*SL:1724*/array[++v0] = '0';
                    /*SL:1725*/array[++v0] = '0';
                }
                /*SL:1727*/array[++v0] = WriterBasedJsonGenerator.HEX_CHARS[v-3 >> 4];
                /*SL:1728*/array[++v0] = WriterBasedJsonGenerator.HEX_CHARS[v-3 & '\u000f'];
                /*SL:1729*/return;
            }
            char[] array = /*EL:1732*/this._entityBuffer;
            /*SL:1733*/if (array == null) {
                /*SL:1734*/array = this._allocateEntityBuffer();
            }
            /*SL:1736*/this._outputHead = this._outputTail;
            /*SL:1737*/if (v-3 > '\u00ff') {
                final int v0 = /*EL:1738*/v-3 >> 8 & '\u00ff';
                final int v = /*EL:1739*/v-3 & '\u00ff';
                /*SL:1740*/array[10] = WriterBasedJsonGenerator.HEX_CHARS[v0 >> 4];
                /*SL:1741*/array[11] = WriterBasedJsonGenerator.HEX_CHARS[v0 & 0xF];
                /*SL:1742*/array[12] = WriterBasedJsonGenerator.HEX_CHARS[v >> 4];
                /*SL:1743*/array[13] = WriterBasedJsonGenerator.HEX_CHARS[v & 0xF];
                /*SL:1744*/this._writer.write(array, 8, 6);
            }
            else {
                /*SL:1746*/array[6] = WriterBasedJsonGenerator.HEX_CHARS[v-3 >> 4];
                /*SL:1747*/array[7] = WriterBasedJsonGenerator.HEX_CHARS[v-3 & '\u000f'];
                /*SL:1748*/this._writer.write(array, 2, 6);
            }
        }
        else {
            String s;
            /*SL:1754*/if (this._currentEscape == null) {
                /*SL:1755*/s = this._characterEscapes.getEscapeSequence(v-3).getValue();
            }
            else {
                /*SL:1757*/s = this._currentEscape.getValue();
                /*SL:1758*/this._currentEscape = null;
            }
            final int v0 = /*EL:1760*/s.length();
            /*SL:1761*/if (this._outputTail >= v0) {
                final int v = /*EL:1762*/this._outputTail - v0;
                /*SL:1763*/this._outputHead = v;
                /*SL:1764*/s.getChars(0, v0, this._outputBuffer, v);
                /*SL:1765*/return;
            }
            /*SL:1768*/this._outputHead = this._outputTail;
            /*SL:1769*/this._writer.write(s);
        }
    }
    
    private int _prependOrWriteCharacterEscape(final char[] v-4, int v-3, final int v-2, char v-1, final int v0) throws IOException, JsonGenerationException {
        /*SL:1783*/if (v0 >= 0) {
            /*SL:1784*/if (v-3 > 1 && v-3 < v-2) {
                /*SL:1785*/v-3 -= 2;
                /*SL:1786*/v-4[v-3] = '\\';
                /*SL:1787*/v-4[v-3 + 1] = (char)v0;
            }
            else {
                char[] a1 = /*EL:1789*/this._entityBuffer;
                /*SL:1790*/if (a1 == null) {
                    /*SL:1791*/a1 = this._allocateEntityBuffer();
                }
                /*SL:1793*/a1[1] = (char)v0;
                /*SL:1794*/this._writer.write(a1, 0, 2);
            }
            /*SL:1796*/return v-3;
        }
        /*SL:1798*/if (v0 != -2) {
            /*SL:1799*/if (v-3 > 5 && v-3 < v-2) {
                /*SL:1800*/v-3 -= 6;
                /*SL:1801*/v-4[v-3++] = '\\';
                /*SL:1802*/v-4[v-3++] = 'u';
                /*SL:1804*/if (v-1 > '\u00ff') {
                    final int a2 = /*EL:1805*/v-1 >> 8 & '\u00ff';
                    /*SL:1806*/v-4[v-3++] = WriterBasedJsonGenerator.HEX_CHARS[a2 >> 4];
                    /*SL:1807*/v-4[v-3++] = WriterBasedJsonGenerator.HEX_CHARS[a2 & 0xF];
                    /*SL:1808*/v-1 &= '\u00ff';
                }
                else {
                    /*SL:1810*/v-4[v-3++] = '0';
                    /*SL:1811*/v-4[v-3++] = '0';
                }
                /*SL:1813*/v-4[v-3++] = WriterBasedJsonGenerator.HEX_CHARS[v-1 >> 4];
                /*SL:1814*/v-4[v-3] = WriterBasedJsonGenerator.HEX_CHARS[v-1 & '\u000f'];
                /*SL:1815*/v-3 -= 5;
            }
            else {
                char[] a3 = /*EL:1818*/this._entityBuffer;
                /*SL:1819*/if (a3 == null) {
                    /*SL:1820*/a3 = this._allocateEntityBuffer();
                }
                /*SL:1822*/this._outputHead = this._outputTail;
                /*SL:1823*/if (v-1 > '\u00ff') {
                    final int a4 = /*EL:1824*/v-1 >> 8 & '\u00ff';
                    final int a5 = /*EL:1825*/v-1 & '\u00ff';
                    /*SL:1826*/a3[10] = WriterBasedJsonGenerator.HEX_CHARS[a4 >> 4];
                    /*SL:1827*/a3[11] = WriterBasedJsonGenerator.HEX_CHARS[a4 & 0xF];
                    /*SL:1828*/a3[12] = WriterBasedJsonGenerator.HEX_CHARS[a5 >> 4];
                    /*SL:1829*/a3[13] = WriterBasedJsonGenerator.HEX_CHARS[a5 & 0xF];
                    /*SL:1830*/this._writer.write(a3, 8, 6);
                }
                else {
                    /*SL:1832*/a3[6] = WriterBasedJsonGenerator.HEX_CHARS[v-1 >> 4];
                    /*SL:1833*/a3[7] = WriterBasedJsonGenerator.HEX_CHARS[v-1 & '\u000f'];
                    /*SL:1834*/this._writer.write(a3, 2, 6);
                }
            }
            /*SL:1837*/return v-3;
        }
        String v;
        /*SL:1840*/if (this._currentEscape == null) {
            /*SL:1841*/v = this._characterEscapes.getEscapeSequence(v-1).getValue();
        }
        else {
            /*SL:1843*/v = this._currentEscape.getValue();
            /*SL:1844*/this._currentEscape = null;
        }
        final int v2 = /*EL:1846*/v.length();
        /*SL:1847*/if (v-3 >= v2 && v-3 < v-2) {
            /*SL:1848*/v-3 -= v2;
            /*SL:1849*/v.getChars(0, v2, v-4, v-3);
        }
        else {
            /*SL:1851*/this._writer.write(v);
        }
        /*SL:1853*/return v-3;
    }
    
    private void _appendCharacterEscape(char v-2, final int v-1) throws IOException, JsonGenerationException {
        /*SL:1863*/if (v-1 >= 0) {
            /*SL:1864*/if (this._outputTail + 2 > this._outputEnd) {
                /*SL:1865*/this._flushBuffer();
            }
            /*SL:1867*/this._outputBuffer[this._outputTail++] = '\\';
            /*SL:1868*/this._outputBuffer[this._outputTail++] = (char)v-1;
            /*SL:1869*/return;
        }
        /*SL:1871*/if (v-1 != -2) {
            /*SL:1872*/if (this._outputTail + 5 >= this._outputEnd) {
                /*SL:1873*/this._flushBuffer();
            }
            int a2 = /*EL:1875*/this._outputTail;
            final char[] v1 = /*EL:1876*/this._outputBuffer;
            /*SL:1877*/v1[a2++] = '\\';
            /*SL:1878*/v1[a2++] = 'u';
            /*SL:1880*/if (v-2 > '\u00ff') {
                /*SL:1881*/a2 = (v-2 >> 8 & '\u00ff');
                /*SL:1882*/v1[a2++] = WriterBasedJsonGenerator.HEX_CHARS[a2 >> 4];
                /*SL:1883*/v1[a2++] = WriterBasedJsonGenerator.HEX_CHARS[a2 & 0xF];
                /*SL:1884*/v-2 &= '\u00ff';
            }
            else {
                /*SL:1886*/v1[a2++] = '0';
                /*SL:1887*/v1[a2++] = '0';
            }
            /*SL:1889*/v1[a2++] = WriterBasedJsonGenerator.HEX_CHARS[v-2 >> 4];
            /*SL:1890*/v1[a2++] = WriterBasedJsonGenerator.HEX_CHARS[v-2 & '\u000f'];
            /*SL:1891*/this._outputTail = a2;
            /*SL:1892*/return;
        }
        String v2;
        /*SL:1895*/if (this._currentEscape == null) {
            /*SL:1896*/v2 = this._characterEscapes.getEscapeSequence(v-2).getValue();
        }
        else {
            /*SL:1898*/v2 = this._currentEscape.getValue();
            /*SL:1899*/this._currentEscape = null;
        }
        final int v3 = /*EL:1901*/v2.length();
        /*SL:1902*/if (this._outputTail + v3 > this._outputEnd) {
            /*SL:1903*/this._flushBuffer();
            /*SL:1904*/if (v3 > this._outputEnd) {
                /*SL:1905*/this._writer.write(v2);
                /*SL:1906*/return;
            }
        }
        /*SL:1909*/v2.getChars(0, v3, this._outputBuffer, this._outputTail);
        /*SL:1910*/this._outputTail += v3;
    }
    
    private char[] _allocateEntityBuffer() {
        final char[] v1 = /*EL:1915*/{ /*EL:1917*/'\\', '\0', /*EL:1919*/'\\', /*EL:1920*/'u', /*EL:1921*/'0', /*EL:1922*/'0', '\0', '\0', /*EL:1924*/'\\', /*EL:1925*/'u', '\0', '\0', '\0', '\0' };
        /*SL:1927*/return this._entityBuffer = v1;
    }
    
    private char[] _allocateCopyBuffer() {
        /*SL:1934*/if (this._charBuffer == null) {
            /*SL:1935*/this._charBuffer = this._ioContext.allocNameCopyBuffer(2000);
        }
        /*SL:1937*/return this._charBuffer;
    }
    
    protected void _flushBuffer() throws IOException {
        final int v0 = /*EL:1942*/this._outputTail - this._outputHead;
        /*SL:1943*/if (v0 > 0) {
            final int v = /*EL:1944*/this._outputHead;
            final boolean b = /*EL:1945*/false;
            this._outputHead = (b ? 1 : 0);
            this._outputTail = (b ? 1 : 0);
            /*SL:1946*/this._writer.write(this._outputBuffer, v, v0);
        }
    }
    
    static {
        HEX_CHARS = CharTypes.copyHexChars();
    }
}

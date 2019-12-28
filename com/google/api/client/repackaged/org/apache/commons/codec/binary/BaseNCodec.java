package com.google.api.client.repackaged.org.apache.commons.codec.binary;

import com.google.api.client.repackaged.org.apache.commons.codec.DecoderException;
import com.google.api.client.repackaged.org.apache.commons.codec.EncoderException;
import com.google.api.client.repackaged.org.apache.commons.codec.BinaryDecoder;
import com.google.api.client.repackaged.org.apache.commons.codec.BinaryEncoder;

public abstract class BaseNCodec implements BinaryEncoder, BinaryDecoder
{
    public static final int MIME_CHUNK_SIZE = 76;
    public static final int PEM_CHUNK_SIZE = 64;
    private static final int DEFAULT_BUFFER_RESIZE_FACTOR = 2;
    private static final int DEFAULT_BUFFER_SIZE = 8192;
    protected static final int MASK_8BITS = 255;
    protected static final byte PAD_DEFAULT = 61;
    protected final byte PAD = 61;
    private final int unencodedBlockSize;
    private final int encodedBlockSize;
    protected final int lineLength;
    private final int chunkSeparatorLength;
    protected byte[] buffer;
    protected int pos;
    private int readPos;
    protected boolean eof;
    protected int currentLinePos;
    protected int modulus;
    
    protected BaseNCodec(final int a1, final int a2, final int a3, final int a4) {
        this.unencodedBlockSize = a1;
        this.encodedBlockSize = a2;
        this.lineLength = ((a3 > 0 && a4 > 0) ? (a3 / a2 * a2) : 0);
        this.chunkSeparatorLength = a4;
    }
    
    boolean hasData() {
        /*SL:149*/return this.buffer != null;
    }
    
    int available() {
        /*SL:158*/return (this.buffer != null) ? (this.pos - this.readPos) : 0;
    }
    
    protected int getDefaultBufferSize() {
        /*SL:167*/return 8192;
    }
    
    private void resizeBuffer() {
        /*SL:172*/if (this.buffer == null) {
            /*SL:173*/this.buffer = new byte[this.getDefaultBufferSize()];
            /*SL:174*/this.pos = 0;
            /*SL:175*/this.readPos = 0;
        }
        else {
            final byte[] v1 = /*EL:177*/new byte[this.buffer.length * 2];
            /*SL:178*/System.arraycopy(this.buffer, 0, v1, 0, this.buffer.length);
            /*SL:179*/this.buffer = v1;
        }
    }
    
    protected void ensureBufferSize(final int a1) {
        /*SL:189*/if (this.buffer == null || this.buffer.length < this.pos + a1) {
            /*SL:190*/this.resizeBuffer();
        }
    }
    
    int readResults(final byte[] a3, final int v1, final int v2) {
        /*SL:207*/if (this.buffer != null) {
            final int a4 = /*EL:208*/Math.min(this.available(), v2);
            /*SL:209*/System.arraycopy(this.buffer, this.readPos, a3, v1, a4);
            /*SL:210*/this.readPos += a4;
            /*SL:211*/if (this.readPos >= this.pos) {
                /*SL:212*/this.buffer = null;
            }
            /*SL:214*/return a4;
        }
        /*SL:216*/return this.eof ? -1 : 0;
    }
    
    protected static boolean isWhiteSpace(final byte a1) {
        /*SL:227*/switch (a1) {
            case 9:
            case 10:
            case 13:
            case 32: {
                /*SL:232*/return true;
            }
            default: {
                /*SL:234*/return false;
            }
        }
    }
    
    private void reset() {
        /*SL:242*/this.buffer = null;
        /*SL:243*/this.pos = 0;
        /*SL:244*/this.readPos = 0;
        /*SL:245*/this.currentLinePos = 0;
        /*SL:246*/this.modulus = 0;
        /*SL:247*/this.eof = false;
    }
    
    public Object encode(final Object a1) throws EncoderException {
        /*SL:261*/if (!(a1 instanceof byte[])) {
            /*SL:262*/throw new EncoderException("Parameter supplied to Base-N encode is not a byte[]");
        }
        /*SL:264*/return this.encode((byte[])a1);
    }
    
    public String encodeToString(final byte[] a1) {
        /*SL:275*/return StringUtils.newStringUtf8(this.encode(a1));
    }
    
    public Object decode(final Object a1) throws DecoderException {
        /*SL:289*/if (a1 instanceof byte[]) {
            /*SL:290*/return this.decode((byte[])a1);
        }
        /*SL:291*/if (a1 instanceof String) {
            /*SL:292*/return this.decode((String)a1);
        }
        /*SL:294*/throw new DecoderException("Parameter supplied to Base-N decode is not a byte[] or a String");
    }
    
    public byte[] decode(final String a1) {
        /*SL:306*/return this.decode(StringUtils.getBytesUtf8(a1));
    }
    
    public byte[] decode(final byte[] a1) {
        /*SL:317*/this.reset();
        /*SL:318*/if (a1 == null || a1.length == 0) {
            /*SL:319*/return a1;
        }
        /*SL:321*/this.decode(a1, 0, a1.length);
        /*SL:322*/this.decode(a1, 0, -1);
        final byte[] v1 = /*EL:323*/new byte[this.pos];
        /*SL:324*/this.readResults(v1, 0, v1.length);
        /*SL:325*/return v1;
    }
    
    public byte[] encode(final byte[] a1) {
        /*SL:336*/this.reset();
        /*SL:337*/if (a1 == null || a1.length == 0) {
            /*SL:338*/return a1;
        }
        /*SL:340*/this.encode(a1, 0, a1.length);
        /*SL:341*/this.encode(a1, 0, -1);
        final byte[] v1 = /*EL:342*/new byte[this.pos - this.readPos];
        /*SL:343*/this.readResults(v1, 0, v1.length);
        /*SL:344*/return v1;
    }
    
    public String encodeAsString(final byte[] a1) {
        /*SL:355*/return StringUtils.newStringUtf8(this.encode(a1));
    }
    
    abstract void encode(final byte[] p0, final int p1, final int p2);
    
    abstract void decode(final byte[] p0, final int p1, final int p2);
    
    protected abstract boolean isInAlphabet(final byte p0);
    
    public boolean isInAlphabet(final byte[] v1, final boolean v2) {
        /*SL:383*/for (int a1 = 0; a1 < v1.length; ++a1) {
            /*SL:384*/if (!this.isInAlphabet(v1[a1]) && (!v2 || (v1[a1] != 61 && !isWhiteSpace(v1[a1])))) {
                /*SL:386*/return false;
            }
        }
        /*SL:389*/return true;
    }
    
    public boolean isInAlphabet(final String a1) {
        /*SL:402*/return this.isInAlphabet(StringUtils.getBytesUtf8(a1), true);
    }
    
    protected boolean containsAlphabetOrPad(final byte[] v0) {
        /*SL:415*/if (v0 == null) {
            /*SL:416*/return false;
        }
        /*SL:418*/for (final byte a1 : v0) {
            /*SL:419*/if (61 == a1 || this.isInAlphabet(a1)) {
                /*SL:420*/return true;
            }
        }
        /*SL:423*/return false;
    }
    
    public long getEncodedLength(final byte[] a1) {
        long v1 = /*EL:437*/(a1.length + this.unencodedBlockSize - 1) / this.unencodedBlockSize * this.encodedBlockSize;
        /*SL:438*/if (this.lineLength > 0) {
            /*SL:440*/v1 += (v1 + this.lineLength - 1L) / this.lineLength * this.chunkSeparatorLength;
        }
        /*SL:442*/return v1;
    }
}

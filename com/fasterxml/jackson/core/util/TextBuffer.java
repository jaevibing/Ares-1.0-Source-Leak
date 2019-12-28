package com.fasterxml.jackson.core.util;

import java.util.Arrays;
import java.io.IOException;
import java.io.Writer;
import com.fasterxml.jackson.core.io.NumberInput;
import java.math.BigDecimal;
import java.util.ArrayList;

public final class TextBuffer
{
    static final char[] NO_CHARS;
    static final int MIN_SEGMENT_LEN = 1000;
    static final int MAX_SEGMENT_LEN = 262144;
    private final BufferRecycler _allocator;
    private char[] _inputBuffer;
    private int _inputStart;
    private int _inputLen;
    private ArrayList<char[]> _segments;
    private boolean _hasSegments;
    private int _segmentSize;
    private char[] _currentSegment;
    private int _currentSize;
    private String _resultString;
    private char[] _resultArray;
    
    public TextBuffer(final BufferRecycler a1) {
        this._allocator = a1;
    }
    
    public void releaseBuffers() {
        /*SL:136*/if (this._allocator == null) {
            /*SL:137*/this.resetWithEmpty();
        }
        else/*SL:139*/ if (this._currentSegment != null) {
            /*SL:141*/this.resetWithEmpty();
            final char[] v1 = /*EL:143*/this._currentSegment;
            /*SL:144*/this._currentSegment = null;
            /*SL:145*/this._allocator.releaseCharBuffer(2, v1);
        }
    }
    
    public void resetWithEmpty() {
        /*SL:156*/this._inputStart = -1;
        /*SL:157*/this._currentSize = 0;
        /*SL:158*/this._inputLen = 0;
        /*SL:160*/this._inputBuffer = null;
        /*SL:161*/this._resultString = null;
        /*SL:162*/this._resultArray = null;
        /*SL:165*/if (this._hasSegments) {
            /*SL:166*/this.clearSegments();
        }
    }
    
    public void resetWith(final char a1) {
        /*SL:175*/this._inputStart = -1;
        /*SL:176*/this._inputLen = 0;
        /*SL:178*/this._resultString = null;
        /*SL:179*/this._resultArray = null;
        /*SL:181*/if (this._hasSegments) {
            /*SL:182*/this.clearSegments();
        }
        else/*SL:183*/ if (this._currentSegment == null) {
            /*SL:184*/this._currentSegment = this.buf(1);
        }
        /*SL:186*/this._currentSegment[0] = a1;
        final boolean b = /*EL:187*/true;
        this._segmentSize = (b ? 1 : 0);
        this._currentSize = (b ? 1 : 0);
    }
    
    public void resetWithShared(final char[] a1, final int a2, final int a3) {
        /*SL:199*/this._resultString = null;
        /*SL:200*/this._resultArray = null;
        /*SL:203*/this._inputBuffer = a1;
        /*SL:204*/this._inputStart = a2;
        /*SL:205*/this._inputLen = a3;
        /*SL:208*/if (this._hasSegments) {
            /*SL:209*/this.clearSegments();
        }
    }
    
    public void resetWithCopy(final char[] a1, final int a2, final int a3) {
        /*SL:215*/this._inputBuffer = null;
        /*SL:216*/this._inputStart = -1;
        /*SL:217*/this._inputLen = 0;
        /*SL:219*/this._resultString = null;
        /*SL:220*/this._resultArray = null;
        /*SL:223*/if (this._hasSegments) {
            /*SL:224*/this.clearSegments();
        }
        else/*SL:225*/ if (this._currentSegment == null) {
            /*SL:226*/this._currentSegment = this.buf(a3);
        }
        final boolean b = /*EL:228*/false;
        this._segmentSize = (b ? 1 : 0);
        this._currentSize = (b ? 1 : 0);
        /*SL:229*/this.append(a1, a2, a3);
    }
    
    public void resetWithCopy(final String a1, final int a2, final int a3) {
        /*SL:237*/this._inputBuffer = null;
        /*SL:238*/this._inputStart = -1;
        /*SL:239*/this._inputLen = 0;
        /*SL:241*/this._resultString = null;
        /*SL:242*/this._resultArray = null;
        /*SL:244*/if (this._hasSegments) {
            /*SL:245*/this.clearSegments();
        }
        else/*SL:246*/ if (this._currentSegment == null) {
            /*SL:247*/this._currentSegment = this.buf(a3);
        }
        final boolean b = /*EL:249*/false;
        this._segmentSize = (b ? 1 : 0);
        this._currentSize = (b ? 1 : 0);
        /*SL:250*/this.append(a1, a2, a3);
    }
    
    public void resetWithString(final String a1) {
        /*SL:255*/this._inputBuffer = null;
        /*SL:256*/this._inputStart = -1;
        /*SL:257*/this._inputLen = 0;
        /*SL:259*/this._resultString = a1;
        /*SL:260*/this._resultArray = null;
        /*SL:262*/if (this._hasSegments) {
            /*SL:263*/this.clearSegments();
        }
        /*SL:265*/this._currentSize = 0;
    }
    
    public char[] getBufferWithoutReset() {
        /*SL:273*/return this._currentSegment;
    }
    
    private char[] buf(final int a1) {
        /*SL:282*/if (this._allocator != null) {
            /*SL:283*/return this._allocator.allocCharBuffer(2, a1);
        }
        /*SL:285*/return new char[Math.max(a1, 1000)];
    }
    
    private void clearSegments() {
        /*SL:290*/this._hasSegments = false;
        /*SL:298*/this._segments.clear();
        final boolean b = /*EL:299*/false;
        this._segmentSize = (b ? 1 : 0);
        this._currentSize = (b ? 1 : 0);
    }
    
    public int size() {
        /*SL:312*/if (this._inputStart >= 0) {
            /*SL:313*/return this._inputLen;
        }
        /*SL:315*/if (this._resultArray != null) {
            /*SL:316*/return this._resultArray.length;
        }
        /*SL:318*/if (this._resultString != null) {
            /*SL:319*/return this._resultString.length();
        }
        /*SL:322*/return this._segmentSize + this._currentSize;
    }
    
    public int getTextOffset() {
        /*SL:330*/return (this._inputStart >= 0) ? this._inputStart : 0;
    }
    
    public boolean hasTextAsCharacters() {
        /*SL:340*/return this._inputStart >= 0 || this._resultArray != null || /*EL:342*/this._resultString == null;
    }
    
    public char[] getTextBuffer() {
        /*SL:354*/if (this._inputStart >= 0) {
            return this._inputBuffer;
        }
        /*SL:355*/if (this._resultArray != null) {
            return this._resultArray;
        }
        /*SL:356*/if (this._resultString != null) {
            /*SL:357*/return this._resultArray = this._resultString.toCharArray();
        }
        /*SL:360*/if (!this._hasSegments) {
            /*SL:361*/return (this._currentSegment == null) ? TextBuffer.NO_CHARS : this._currentSegment;
        }
        /*SL:364*/return this.contentsAsArray();
    }
    
    public String contentsAsString() {
        /*SL:375*/if (this._resultString == null) {
            /*SL:377*/if (this._resultArray != null) {
                /*SL:378*/this._resultString = new String(this._resultArray);
            }
            else/*SL:381*/ if (this._inputStart >= 0) {
                /*SL:382*/if (this._inputLen < 1) {
                    /*SL:383*/return this._resultString = "";
                }
                /*SL:385*/this._resultString = new String(this._inputBuffer, this._inputStart, this._inputLen);
            }
            else {
                final int segmentSize = /*EL:388*/this._segmentSize;
                final int currentSize = /*EL:389*/this._currentSize;
                /*SL:391*/if (segmentSize == 0) {
                    /*SL:392*/this._resultString = ((currentSize == 0) ? "" : new String(this._currentSegment, 0, currentSize));
                }
                else {
                    final StringBuilder sb = /*EL:394*/new StringBuilder(segmentSize + currentSize);
                    /*SL:396*/if (this._segments != null) {
                        /*SL:397*/for (int i = 0, v0 = this._segments.size(); i < v0; ++i) {
                            final char[] v = /*EL:398*/this._segments.get(i);
                            /*SL:399*/sb.append(v, 0, v.length);
                        }
                    }
                    /*SL:403*/sb.append(this._currentSegment, 0, this._currentSize);
                    /*SL:404*/this._resultString = sb.toString();
                }
            }
        }
        /*SL:409*/return this._resultString;
    }
    
    public char[] contentsAsArray() {
        char[] v1 = /*EL:413*/this._resultArray;
        /*SL:414*/if (v1 == null) {
            /*SL:415*/v1 = (this._resultArray = this.resultArray());
        }
        /*SL:417*/return v1;
    }
    
    public BigDecimal contentsAsDecimal() throws NumberFormatException {
        /*SL:427*/if (this._resultArray != null) {
            /*SL:428*/return NumberInput.parseBigDecimal(this._resultArray);
        }
        /*SL:431*/if (this._inputStart >= 0 && this._inputBuffer != null) {
            /*SL:432*/return NumberInput.parseBigDecimal(this._inputBuffer, this._inputStart, this._inputLen);
        }
        /*SL:435*/if (this._segmentSize == 0 && this._currentSegment != null) {
            /*SL:436*/return NumberInput.parseBigDecimal(this._currentSegment, 0, this._currentSize);
        }
        /*SL:439*/return NumberInput.parseBigDecimal(this.contentsAsArray());
    }
    
    public double contentsAsDouble() throws NumberFormatException {
        /*SL:447*/return NumberInput.parseDouble(this.contentsAsString());
    }
    
    public int contentsAsInt(final boolean a1) {
        /*SL:459*/if (this._inputStart >= 0 && this._inputBuffer != null) {
            /*SL:460*/if (a1) {
                /*SL:461*/return -NumberInput.parseInt(this._inputBuffer, this._inputStart + 1, this._inputLen - 1);
            }
            /*SL:463*/return NumberInput.parseInt(this._inputBuffer, this._inputStart, this._inputLen);
        }
        else {
            /*SL:465*/if (a1) {
                /*SL:466*/return -NumberInput.parseInt(this._currentSegment, 1, this._currentSize - 1);
            }
            /*SL:468*/return NumberInput.parseInt(this._currentSegment, 0, this._currentSize);
        }
    }
    
    public long contentsAsLong(final boolean a1) {
        /*SL:480*/if (this._inputStart >= 0 && this._inputBuffer != null) {
            /*SL:481*/if (a1) {
                /*SL:482*/return -NumberInput.parseLong(this._inputBuffer, this._inputStart + 1, this._inputLen - 1);
            }
            /*SL:484*/return NumberInput.parseLong(this._inputBuffer, this._inputStart, this._inputLen);
        }
        else {
            /*SL:486*/if (a1) {
                /*SL:487*/return -NumberInput.parseLong(this._currentSegment, 1, this._currentSize - 1);
            }
            /*SL:489*/return NumberInput.parseLong(this._currentSegment, 0, this._currentSize);
        }
    }
    
    public int contentsToWriter(final Writer v-3) throws IOException {
        /*SL:497*/if (this._resultArray != null) {
            /*SL:498*/v-3.write(this._resultArray);
            /*SL:499*/return this._resultArray.length;
        }
        /*SL:501*/if (this._resultString != null) {
            /*SL:502*/v-3.write(this._resultString);
            /*SL:503*/return this._resultString.length();
        }
        /*SL:506*/if (this._inputStart >= 0) {
            final int a1 = /*EL:507*/this._inputLen;
            /*SL:508*/if (a1 > 0) {
                /*SL:509*/v-3.write(this._inputBuffer, this._inputStart, a1);
            }
            /*SL:511*/return a1;
        }
        int n = /*EL:514*/0;
        /*SL:515*/if (this._segments != null) {
            /*SL:516*/for (int i = 0, v0 = this._segments.size(); i < v0; ++i) {
                final char[] v = /*EL:517*/this._segments.get(i);
                final int v2 = /*EL:518*/v.length;
                /*SL:519*/v-3.write(v, 0, v2);
                /*SL:520*/n += v2;
            }
        }
        int i = /*EL:523*/this._currentSize;
        /*SL:524*/if (i > 0) {
            /*SL:525*/v-3.write(this._currentSegment, 0, i);
            /*SL:526*/n += i;
        }
        /*SL:528*/return n;
    }
    
    public void ensureNotShared() {
        /*SL:542*/if (this._inputStart >= 0) {
            /*SL:543*/this.unshare(16);
        }
    }
    
    public void append(final char a1) {
        /*SL:549*/if (this._inputStart >= 0) {
            /*SL:550*/this.unshare(16);
        }
        /*SL:552*/this._resultString = null;
        /*SL:553*/this._resultArray = null;
        char[] v1 = /*EL:555*/this._currentSegment;
        /*SL:556*/if (this._currentSize >= v1.length) {
            /*SL:557*/this.expand(1);
            /*SL:558*/v1 = this._currentSegment;
        }
        /*SL:560*/v1[this._currentSize++] = a1;
    }
    
    public void append(final char[] a3, int v1, int v2) {
        /*SL:566*/if (this._inputStart >= 0) {
            /*SL:567*/this.unshare(v2);
        }
        /*SL:569*/this._resultString = null;
        /*SL:570*/this._resultArray = null;
        final char[] v3 = /*EL:573*/this._currentSegment;
        final int v4 = /*EL:574*/v3.length - this._currentSize;
        /*SL:576*/if (v4 >= v2) {
            /*SL:577*/System.arraycopy(a3, v1, v3, this._currentSize, v2);
            /*SL:578*/this._currentSize += v2;
            /*SL:579*/return;
        }
        /*SL:582*/if (v4 > 0) {
            /*SL:583*/System.arraycopy(a3, v1, v3, this._currentSize, v4);
            /*SL:584*/v1 += v4;
            /*SL:585*/v2 -= v4;
        }
        /*SL:596*/do {
            this.expand(v2);
            final int a4 = Math.min(this._currentSegment.length, v2);
            System.arraycopy(a3, v1, this._currentSegment, 0, a4);
            this._currentSize += a4;
            v1 += a4;
            v2 -= a4;
        } while (v2 > 0);
    }
    
    public void append(final String a3, int v1, int v2) {
        /*SL:602*/if (this._inputStart >= 0) {
            /*SL:603*/this.unshare(v2);
        }
        /*SL:605*/this._resultString = null;
        /*SL:606*/this._resultArray = null;
        final char[] v3 = /*EL:609*/this._currentSegment;
        final int v4 = /*EL:610*/v3.length - this._currentSize;
        /*SL:611*/if (v4 >= v2) {
            /*SL:612*/a3.getChars(v1, v1 + v2, v3, this._currentSize);
            /*SL:613*/this._currentSize += v2;
            /*SL:614*/return;
        }
        /*SL:617*/if (v4 > 0) {
            /*SL:618*/a3.getChars(v1, v1 + v4, v3, this._currentSize);
            /*SL:619*/v2 -= v4;
            /*SL:620*/v1 += v4;
        }
        /*SL:631*/do {
            this.expand(v2);
            final int a4 = Math.min(this._currentSegment.length, v2);
            a3.getChars(v1, v1 + a4, this._currentSegment, 0);
            this._currentSize += a4;
            v1 += a4;
            v2 -= a4;
        } while (v2 > 0);
    }
    
    public char[] getCurrentSegment() {
        /*SL:646*/if (this._inputStart >= 0) {
            /*SL:647*/this.unshare(1);
        }
        else {
            final char[] v1 = /*EL:649*/this._currentSegment;
            /*SL:650*/if (v1 == null) {
                /*SL:651*/this._currentSegment = this.buf(0);
            }
            else/*SL:652*/ if (this._currentSize >= v1.length) {
                /*SL:654*/this.expand(1);
            }
        }
        /*SL:657*/return this._currentSegment;
    }
    
    public char[] emptyAndGetCurrentSegment() {
        /*SL:663*/this._inputStart = -1;
        /*SL:664*/this._currentSize = 0;
        /*SL:665*/this._inputLen = 0;
        /*SL:667*/this._inputBuffer = null;
        /*SL:668*/this._resultString = null;
        /*SL:669*/this._resultArray = null;
        /*SL:672*/if (this._hasSegments) {
            /*SL:673*/this.clearSegments();
        }
        char[] v1 = /*EL:675*/this._currentSegment;
        /*SL:676*/if (v1 == null) {
            /*SL:677*/v1 = (this._currentSegment = this.buf(0));
        }
        /*SL:679*/return v1;
    }
    
    public int getCurrentSegmentSize() {
        /*SL:682*/return this._currentSize;
    }
    
    public void setCurrentLength(final int a1) {
        /*SL:683*/this._currentSize = a1;
    }
    
    public String setCurrentAndReturn(final int a1) {
        /*SL:689*/this._currentSize = a1;
        /*SL:691*/if (this._segmentSize > 0) {
            /*SL:692*/return this.contentsAsString();
        }
        final int v1 = /*EL:695*/this._currentSize;
        final String v2 = /*EL:696*/(v1 == 0) ? "" : new String(this._currentSegment, 0, v1);
        /*SL:698*/return this._resultString = v2;
    }
    
    public char[] finishCurrentSegment() {
        /*SL:702*/if (this._segments == null) {
            /*SL:703*/this._segments = new ArrayList<char[]>();
        }
        /*SL:705*/this._hasSegments = true;
        /*SL:706*/this._segments.add(this._currentSegment);
        final int v1 = /*EL:707*/this._currentSegment.length;
        /*SL:708*/this._segmentSize += v1;
        /*SL:709*/this._currentSize = 0;
        int v2 = /*EL:712*/v1 + (v1 >> 1);
        /*SL:713*/if (v2 < 1000) {
            /*SL:714*/v2 = 1000;
        }
        else/*SL:715*/ if (v2 > 262144) {
            /*SL:716*/v2 = 262144;
        }
        final char[] v3 = /*EL:718*/this.carr(v2);
        /*SL:720*/return this._currentSegment = v3;
    }
    
    public char[] expandCurrentSegment() {
        final char[] v1 = /*EL:730*/this._currentSegment;
        final int v2 = /*EL:732*/v1.length;
        int v3 = /*EL:733*/v2 + (v2 >> 1);
        /*SL:735*/if (v3 > 262144) {
            /*SL:736*/v3 = v2 + (v2 >> 2);
        }
        /*SL:738*/return this._currentSegment = Arrays.copyOf(v1, v3);
    }
    
    public char[] expandCurrentSegment(final int a1) {
        char[] v1 = /*EL:751*/this._currentSegment;
        /*SL:752*/if (v1.length >= a1) {
            return v1;
        }
        /*SL:753*/v1 = (this._currentSegment = Arrays.copyOf(v1, a1));
        /*SL:754*/return v1;
    }
    
    @Override
    public String toString() {
        /*SL:768*/return this.contentsAsString();
    }
    
    private void unshare(final int a1) {
        final int v1 = /*EL:782*/this._inputLen;
        /*SL:783*/this._inputLen = 0;
        final char[] v2 = /*EL:784*/this._inputBuffer;
        /*SL:785*/this._inputBuffer = null;
        final int v3 = /*EL:786*/this._inputStart;
        /*SL:787*/this._inputStart = -1;
        final int v4 = /*EL:790*/v1 + a1;
        /*SL:791*/if (this._currentSegment == null || v4 > this._currentSegment.length) {
            /*SL:792*/this._currentSegment = this.buf(v4);
        }
        /*SL:794*/if (v1 > 0) {
            /*SL:795*/System.arraycopy(v2, v3, this._currentSegment, 0, v1);
        }
        /*SL:797*/this._segmentSize = 0;
        /*SL:798*/this._currentSize = v1;
    }
    
    private void expand(final int a1) {
        /*SL:808*/if (this._segments == null) {
            /*SL:809*/this._segments = new ArrayList<char[]>();
        }
        final char[] v1 = /*EL:811*/this._currentSegment;
        /*SL:812*/this._hasSegments = true;
        /*SL:813*/this._segments.add(v1);
        /*SL:814*/this._segmentSize += v1.length;
        /*SL:815*/this._currentSize = 0;
        final int v2 = /*EL:816*/v1.length;
        int v3 = /*EL:819*/v2 + (v2 >> 1);
        /*SL:820*/if (v3 < 1000) {
            /*SL:821*/v3 = 1000;
        }
        else/*SL:822*/ if (v3 > 262144) {
            /*SL:823*/v3 = 262144;
        }
        /*SL:825*/this._currentSegment = this.carr(v3);
    }
    
    private char[] resultArray() {
        /*SL:830*/if (this._resultString != null) {
            /*SL:831*/return this._resultString.toCharArray();
        }
        /*SL:834*/if (this._inputStart >= 0) {
            final int v1 = /*EL:835*/this._inputLen;
            /*SL:836*/if (v1 < 1) {
                /*SL:837*/return TextBuffer.NO_CHARS;
            }
            final int v2 = /*EL:839*/this._inputStart;
            /*SL:840*/if (v2 == 0) {
                /*SL:841*/return Arrays.copyOf(this._inputBuffer, v1);
            }
            /*SL:843*/return Arrays.copyOfRange(this._inputBuffer, v2, v2 + v1);
        }
        else {
            final int v1 = /*EL:846*/this.size();
            /*SL:847*/if (v1 < 1) {
                /*SL:848*/return TextBuffer.NO_CHARS;
            }
            int v2 = /*EL:850*/0;
            final char[] v3 = /*EL:851*/this.carr(v1);
            /*SL:852*/if (this._segments != null) {
                /*SL:853*/for (int v4 = 0, v5 = this._segments.size(); v4 < v5; ++v4) {
                    final char[] v6 = /*EL:854*/this._segments.get(v4);
                    final int v7 = /*EL:855*/v6.length;
                    /*SL:856*/System.arraycopy(v6, 0, v3, v2, v7);
                    /*SL:857*/v2 += v7;
                }
            }
            /*SL:860*/System.arraycopy(this._currentSegment, 0, v3, v2, this._currentSize);
            /*SL:861*/return v3;
        }
    }
    
    private char[] carr(final int a1) {
        /*SL:864*/return new char[a1];
    }
    
    static {
        NO_CHARS = new char[0];
    }
}

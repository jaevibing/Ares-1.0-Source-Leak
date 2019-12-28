package com.fasterxml.jackson.core.json.async;

import com.fasterxml.jackson.core.async.NonBlockingInputFeeder;
import com.fasterxml.jackson.core.io.CharTypes;
import com.fasterxml.jackson.core.base.ParserBase;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.util.VersionUtil;
import java.io.OutputStream;
import java.io.IOException;
import com.fasterxml.jackson.core.sym.ByteQuadsCanonicalizer;
import com.fasterxml.jackson.core.io.IOContext;
import com.fasterxml.jackson.core.async.ByteArrayFeeder;

public class NonBlockingJsonParser extends NonBlockingJsonParserBase implements ByteArrayFeeder
{
    private static final int[] _icUTF8;
    protected static final int[] _icLatin1;
    protected byte[] _inputBuffer;
    protected int _origBufferLen;
    
    public NonBlockingJsonParser(final IOContext a1, final int a2, final ByteQuadsCanonicalizer a3) {
        super(a1, a2, a3);
        this._inputBuffer = NonBlockingJsonParser.NO_BYTES;
    }
    
    @Override
    public ByteArrayFeeder getNonBlockingInputFeeder() {
        /*SL:68*/return this;
    }
    
    @Override
    public final boolean needMoreInput() {
        /*SL:73*/return this._inputPtr >= this._inputEnd && !this._endOfInput;
    }
    
    @Override
    public void feedInput(final byte[] a1, final int a2, final int a3) throws IOException {
        /*SL:80*/if (this._inputPtr < this._inputEnd) {
            /*SL:81*/this._reportError("Still have %d undecoded bytes, should not call 'feedInput'", this._inputEnd - this._inputPtr);
        }
        /*SL:83*/if (a3 < a2) {
            /*SL:84*/this._reportError("Input end (%d) may not be before start (%d)", a3, a2);
        }
        /*SL:87*/if (this._endOfInput) {
            /*SL:88*/this._reportError("Already closed, can not feed more input");
        }
        /*SL:91*/this._currInputProcessed += this._origBufferLen;
        /*SL:94*/this._currInputRowStart = a2 - (this._inputEnd - this._currInputRowStart);
        /*SL:97*/this._inputBuffer = a1;
        /*SL:98*/this._inputPtr = a2;
        /*SL:99*/this._inputEnd = a3;
        /*SL:100*/this._origBufferLen = a3 - a2;
    }
    
    @Override
    public void endOfInput() {
        /*SL:105*/this._endOfInput = true;
    }
    
    @Override
    public int releaseBuffered(final OutputStream a1) throws IOException {
        final int v1 = /*EL:127*/this._inputEnd - this._inputPtr;
        /*SL:128*/if (v1 > 0) {
            /*SL:129*/a1.write(this._inputBuffer, this._inputPtr, v1);
        }
        /*SL:131*/return v1;
    }
    
    @Override
    protected char _decodeEscaped() throws IOException {
        /*SL:138*/VersionUtil.throwInternal();
        /*SL:139*/return ' ';
    }
    
    @Override
    public JsonToken nextToken() throws IOException {
        /*SL:153*/if (this._inputPtr >= this._inputEnd) {
            /*SL:154*/if (this._closed) {
                /*SL:155*/return null;
            }
            /*SL:158*/if (!this._endOfInput) {
                /*SL:166*/return JsonToken.NOT_AVAILABLE;
            }
            if (this._currToken == JsonToken.NOT_AVAILABLE) {
                return this._finishTokenWithEOF();
            }
            return this._eofAsNextToken();
        }
        else {
            /*SL:169*/if (this._currToken == JsonToken.NOT_AVAILABLE) {
                /*SL:170*/return this._finishToken();
            }
            /*SL:174*/this._numTypesValid = 0;
            /*SL:175*/this._tokenInputTotal = this._currInputProcessed + this._inputPtr;
            /*SL:177*/this._binaryValue = null;
            final int v1 = /*EL:178*/this._inputBuffer[this._inputPtr++] & 0xFF;
            /*SL:180*/switch (this._majorState) {
                case 0: {
                    /*SL:182*/return this._startDocument(v1);
                }
                case 1: {
                    /*SL:185*/return this._startValue(v1);
                }
                case 2: {
                    /*SL:188*/return this._startFieldName(v1);
                }
                case 3: {
                    /*SL:190*/return this._startFieldNameAfterComma(v1);
                }
                case 4: {
                    /*SL:193*/return this._startValueExpectColon(v1);
                }
                case 5: {
                    /*SL:196*/return this._startValue(v1);
                }
                case 6: {
                    /*SL:199*/return this._startValueExpectComma(v1);
                }
                default: {
                    /*SL:203*/VersionUtil.throwInternal();
                    /*SL:204*/return null;
                }
            }
        }
    }
    
    protected final JsonToken _finishToken() throws IOException {
        /*SL:215*/switch (this._minorState) {
            case 1: {
                /*SL:217*/return this._finishBOM(this._pending32);
            }
            case 4: {
                /*SL:219*/return this._startFieldName(this._inputBuffer[this._inputPtr++] & 0xFF);
            }
            case 5: {
                /*SL:221*/return this._startFieldNameAfterComma(this._inputBuffer[this._inputPtr++] & 0xFF);
            }
            case 7: {
                /*SL:225*/return this._parseEscapedName(this._quadLength, this._pending32, this._pendingBytes);
            }
            case 8: {
                /*SL:227*/return this._finishFieldWithEscape();
            }
            case 9: {
                /*SL:229*/return this._finishAposName(this._quadLength, this._pending32, this._pendingBytes);
            }
            case 10: {
                /*SL:231*/return this._finishUnquotedName(this._quadLength, this._pending32, this._pendingBytes);
            }
            case 12: {
                /*SL:236*/return this._startValue(this._inputBuffer[this._inputPtr++] & 0xFF);
            }
            case 15: {
                /*SL:238*/return this._startValueAfterComma(this._inputBuffer[this._inputPtr++] & 0xFF);
            }
            case 13: {
                /*SL:240*/return this._startValueExpectComma(this._inputBuffer[this._inputPtr++] & 0xFF);
            }
            case 14: {
                /*SL:242*/return this._startValueExpectColon(this._inputBuffer[this._inputPtr++] & 0xFF);
            }
            case 16: {
                /*SL:245*/return this._finishKeywordToken("null", this._pending32, JsonToken.VALUE_NULL);
            }
            case 17: {
                /*SL:247*/return this._finishKeywordToken("true", this._pending32, JsonToken.VALUE_TRUE);
            }
            case 18: {
                /*SL:249*/return this._finishKeywordToken("false", this._pending32, JsonToken.VALUE_FALSE);
            }
            case 19: {
                /*SL:251*/return this._finishNonStdToken(this._nonStdTokenType, this._pending32);
            }
            case 23: {
                /*SL:254*/return this._finishNumberMinus(this._inputBuffer[this._inputPtr++] & 0xFF);
            }
            case 24: {
                /*SL:256*/return this._finishNumberLeadingZeroes();
            }
            case 25: {
                /*SL:258*/return this._finishNumberLeadingNegZeroes();
            }
            case 26: {
                /*SL:260*/return this._finishNumberIntegralPart(this._textBuffer.getBufferWithoutReset(), this._textBuffer.getCurrentSegmentSize());
            }
            case 30: {
                /*SL:263*/return this._finishFloatFraction();
            }
            case 31: {
                /*SL:265*/return this._finishFloatExponent(true, this._inputBuffer[this._inputPtr++] & 0xFF);
            }
            case 32: {
                /*SL:267*/return this._finishFloatExponent(false, this._inputBuffer[this._inputPtr++] & 0xFF);
            }
            case 40: {
                /*SL:270*/return this._finishRegularString();
            }
            case 42: {
                /*SL:272*/this._textBuffer.append((char)this._decodeUTF8_2(this._pending32, this._inputBuffer[this._inputPtr++]));
                /*SL:273*/if (this._minorStateAfterSplit == 45) {
                    /*SL:274*/return this._finishAposString();
                }
                /*SL:276*/return this._finishRegularString();
            }
            case 43: {
                /*SL:278*/if (!this._decodeSplitUTF8_3(this._pending32, this._pendingBytes, this._inputBuffer[this._inputPtr++])) {
                    /*SL:279*/return JsonToken.NOT_AVAILABLE;
                }
                /*SL:281*/if (this._minorStateAfterSplit == 45) {
                    /*SL:282*/return this._finishAposString();
                }
                /*SL:284*/return this._finishRegularString();
            }
            case 44: {
                /*SL:286*/if (!this._decodeSplitUTF8_4(this._pending32, this._pendingBytes, this._inputBuffer[this._inputPtr++])) {
                    /*SL:287*/return JsonToken.NOT_AVAILABLE;
                }
                /*SL:289*/if (this._minorStateAfterSplit == 45) {
                    /*SL:290*/return this._finishAposString();
                }
                /*SL:292*/return this._finishRegularString();
            }
            case 41: {
                final int v1 = /*EL:296*/this._decodeSplitEscaped(this._quoted32, this._quotedDigits);
                /*SL:297*/if (v1 < 0) {
                    /*SL:298*/return JsonToken.NOT_AVAILABLE;
                }
                /*SL:300*/this._textBuffer.append((char)v1);
                /*SL:302*/if (this._minorStateAfterSplit == 45) {
                    /*SL:303*/return this._finishAposString();
                }
                /*SL:305*/return this._finishRegularString();
            }
            case 45: {
                /*SL:308*/return this._finishAposString();
            }
            case 50: {
                /*SL:311*/return this._finishErrorToken();
            }
            case 51: {
                /*SL:316*/return this._startSlashComment(this._pending32);
            }
            case 52: {
                /*SL:318*/return this._finishCComment(this._pending32, true);
            }
            case 53: {
                /*SL:320*/return this._finishCComment(this._pending32, false);
            }
            case 54: {
                /*SL:322*/return this._finishCppComment(this._pending32);
            }
            case 55: {
                /*SL:324*/return this._finishHashComment(this._pending32);
            }
            default: {
                /*SL:326*/VersionUtil.throwInternal();
                /*SL:327*/return null;
            }
        }
    }
    
    protected final JsonToken _finishTokenWithEOF() throws IOException {
        final JsonToken v0 = /*EL:339*/this._currToken;
        /*SL:340*/switch (this._minorState) {
            case 3: {
                /*SL:342*/return this._eofAsNextToken();
            }
            case 12: {
                /*SL:344*/return this._eofAsNextToken();
            }
            case 16: {
                /*SL:348*/return this._finishKeywordTokenWithEOF("null", this._pending32, JsonToken.VALUE_NULL);
            }
            case 17: {
                /*SL:350*/return this._finishKeywordTokenWithEOF("true", this._pending32, JsonToken.VALUE_TRUE);
            }
            case 18: {
                /*SL:352*/return this._finishKeywordTokenWithEOF("false", this._pending32, JsonToken.VALUE_FALSE);
            }
            case 19: {
                /*SL:354*/return this._finishNonStdTokenWithEOF(this._nonStdTokenType, this._pending32);
            }
            case 50: {
                /*SL:356*/return this._finishErrorTokenWithEOF();
            }
            case 24:
            case 25: {
                /*SL:363*/return this._valueCompleteInt(0, "0");
            }
            case 26: {
                int v = /*EL:367*/this._textBuffer.getCurrentSegmentSize();
                /*SL:368*/if (this._numberNegative) {
                    /*SL:369*/--v;
                }
                /*SL:371*/this._intLength = v;
                /*SL:373*/return this._valueComplete(JsonToken.VALUE_NUMBER_INT);
            }
            case 30: {
                /*SL:376*/this._expLength = 0;
            }
            case 32: {
                /*SL:379*/return this._valueComplete(JsonToken.VALUE_NUMBER_FLOAT);
            }
            case 31: {
                /*SL:382*/this._reportInvalidEOF(": was expecting fraction after exponent marker", JsonToken.VALUE_NUMBER_FLOAT);
            }
            case 52:
            case 53: {
                /*SL:390*/this._reportInvalidEOF(": was expecting closing '*/' for comment", JsonToken.NOT_AVAILABLE);
            }
            case 54:
            case 55: {
                /*SL:395*/return this._eofAsNextToken();
            }
            default: {
                /*SL:399*/this._reportInvalidEOF(": was expecting rest of token (internal state: " + this._minorState + ")", this._currToken);
                /*SL:400*/return v0;
            }
        }
    }
    
    private final JsonToken _startDocument(int a1) throws IOException {
        /*SL:411*/a1 &= 0xFF;
        /*SL:414*/if (a1 == 239 && this._minorState != 1) {
            /*SL:415*/return this._finishBOM(1);
        }
        /*SL:419*/while (a1 <= 32) {
            /*SL:420*/if (a1 != 32) {
                /*SL:421*/if (a1 == 10) {
                    /*SL:422*/++this._currInputRow;
                    /*SL:423*/this._currInputRowStart = this._inputPtr;
                }
                else/*SL:424*/ if (a1 == 13) {
                    /*SL:425*/++this._currInputRowAlt;
                    /*SL:426*/this._currInputRowStart = this._inputPtr;
                }
                else/*SL:427*/ if (a1 != 9) {
                    /*SL:428*/this._throwInvalidSpace(a1);
                }
            }
            /*SL:431*/if (this._inputPtr >= this._inputEnd) {
                /*SL:432*/this._minorState = 3;
                /*SL:433*/if (this._closed) {
                    /*SL:434*/return null;
                }
                /*SL:437*/if (this._endOfInput) {
                    /*SL:438*/return this._eofAsNextToken();
                }
                /*SL:440*/return JsonToken.NOT_AVAILABLE;
            }
            else {
                /*SL:442*/a1 = (this._inputBuffer[this._inputPtr++] & 0xFF);
            }
        }
        /*SL:444*/return this._startValue(a1);
    }
    
    private final JsonToken _finishBOM(int v2) throws IOException {
        /*SL:453*/while (this._inputPtr < this._inputEnd) {
            final int a1 = /*EL:454*/this._inputBuffer[this._inputPtr++] & 0xFF;
            /*SL:455*/switch (v2) {
                case 3: {
                    /*SL:459*/this._currInputProcessed -= 3L;
                    /*SL:460*/return this._startDocument(a1);
                }
                case 2: {
                    /*SL:462*/if (a1 != 191) {
                        /*SL:463*/this._reportError("Unexpected byte 0x%02x following 0xEF 0xBB; should get 0xBF as third byte of UTF-8 BOM", a1);
                        break;
                    }
                    break;
                }
                case 1: {
                    /*SL:467*/if (a1 != 187) {
                        /*SL:468*/this._reportError("Unexpected byte 0x%02x following 0xEF; should get 0xBB as second byte UTF-8 BOM", a1);
                        break;
                    }
                    break;
                }
            }
            /*SL:472*/++v2;
        }
        /*SL:474*/this._pending32 = v2;
        /*SL:475*/this._minorState = 1;
        /*SL:476*/return this._currToken = JsonToken.NOT_AVAILABLE;
    }
    
    private final JsonToken _startFieldName(int v2) throws IOException {
        /*SL:492*/if (v2 <= 32) {
            /*SL:493*/v2 = this._skipWS(v2);
            /*SL:494*/if (v2 <= 0) {
                /*SL:495*/this._minorState = 4;
                /*SL:496*/return this._currToken;
            }
        }
        /*SL:499*/this._updateTokenLocation();
        /*SL:500*/if (v2 == 34) {
            /*SL:507*/if (this._inputPtr + 13 <= this._inputEnd) {
                final String a1 = /*EL:508*/this._fastParseName();
                /*SL:509*/if (a1 != null) {
                    /*SL:510*/return this._fieldComplete(a1);
                }
            }
            /*SL:513*/return this._parseEscapedName(0, 0, 0);
        }
        if (v2 == 125) {
            return this._closeObjectScope();
        }
        return this._handleOddName(v2);
    }
    
    private final JsonToken _startFieldNameAfterComma(int v2) throws IOException {
        /*SL:519*/if (v2 <= 32) {
            /*SL:520*/v2 = this._skipWS(v2);
            /*SL:521*/if (v2 <= 0) {
                /*SL:522*/this._minorState = 5;
                /*SL:523*/return this._currToken;
            }
        }
        /*SL:526*/if (v2 != 44) {
            /*SL:527*/if (v2 == 125) {
                /*SL:528*/return this._closeObjectScope();
            }
            /*SL:530*/if (v2 == 35) {
                /*SL:531*/return this._finishHashComment(5);
            }
            /*SL:533*/if (v2 == 47) {
                /*SL:534*/return this._startSlashComment(5);
            }
            /*SL:536*/this._reportUnexpectedChar(v2, "was expecting comma to separate " + this._parsingContext.typeDesc() + " entries");
        }
        final int v3 = /*EL:538*/this._inputPtr;
        /*SL:539*/if (v3 >= this._inputEnd) {
            /*SL:540*/this._minorState = 4;
            /*SL:541*/return this._currToken = JsonToken.NOT_AVAILABLE;
        }
        /*SL:543*/v2 = this._inputBuffer[v3];
        /*SL:544*/this._inputPtr = v3 + 1;
        /*SL:545*/if (v2 <= 32) {
            /*SL:546*/v2 = this._skipWS(v2);
            /*SL:547*/if (v2 <= 0) {
                /*SL:548*/this._minorState = 4;
                /*SL:549*/return this._currToken;
            }
        }
        /*SL:552*/this._updateTokenLocation();
        /*SL:553*/if (v2 == 34) {
            /*SL:562*/if (this._inputPtr + 13 <= this._inputEnd) {
                final String a1 = /*EL:563*/this._fastParseName();
                /*SL:564*/if (a1 != null) {
                    /*SL:565*/return this._fieldComplete(a1);
                }
            }
            /*SL:568*/return this._parseEscapedName(0, 0, 0);
        }
        if (v2 == 125 && Feature.ALLOW_TRAILING_COMMA.enabledIn(this._features)) {
            return this._closeObjectScope();
        }
        return this._handleOddName(v2);
    }
    
    private final JsonToken _startValue(int a1) throws IOException {
        /*SL:585*/if (a1 <= 32) {
            /*SL:586*/a1 = this._skipWS(a1);
            /*SL:587*/if (a1 <= 0) {
                /*SL:588*/this._minorState = 12;
                /*SL:589*/return this._currToken;
            }
        }
        /*SL:592*/this._updateTokenLocation();
        /*SL:593*/if (a1 == 34) {
            /*SL:594*/return this._startString();
        }
        /*SL:596*/switch (a1) {
            case 35: {
                /*SL:598*/return this._finishHashComment(12);
            }
            case 45: {
                /*SL:600*/return this._startNegativeNumber();
            }
            case 47: {
                /*SL:602*/return this._startSlashComment(12);
            }
            case 48: {
                /*SL:608*/return this._startNumberLeadingZero();
            }
            case 49:
            case 50:
            case 51:
            case 52:
            case 53:
            case 54:
            case 55:
            case 56:
            case 57: {
                /*SL:618*/return this._startPositiveNumber(a1);
            }
            case 102: {
                /*SL:620*/return this._startFalseToken();
            }
            case 110: {
                /*SL:622*/return this._startNullToken();
            }
            case 116: {
                /*SL:624*/return this._startTrueToken();
            }
            case 91: {
                /*SL:626*/return this._startArrayScope();
            }
            case 93: {
                /*SL:628*/return this._closeArrayScope();
            }
            case 123: {
                /*SL:630*/return this._startObjectScope();
            }
            case 125: {
                /*SL:632*/return this._closeObjectScope();
            }
            default: {
                /*SL:635*/return this._startUnexpectedValue(false, a1);
            }
        }
    }
    
    private final JsonToken _startValueExpectComma(int a1) throws IOException {
        /*SL:645*/if (a1 <= 32) {
            /*SL:646*/a1 = this._skipWS(a1);
            /*SL:647*/if (a1 <= 0) {
                /*SL:648*/this._minorState = 13;
                /*SL:649*/return this._currToken;
            }
        }
        /*SL:652*/if (a1 != 44) {
            /*SL:653*/if (a1 == 93) {
                /*SL:654*/return this._closeArrayScope();
            }
            /*SL:656*/if (a1 == 125) {
                /*SL:657*/return this._closeObjectScope();
            }
            /*SL:659*/if (a1 == 47) {
                /*SL:660*/return this._startSlashComment(13);
            }
            /*SL:662*/if (a1 == 35) {
                /*SL:663*/return this._finishHashComment(13);
            }
            /*SL:665*/this._reportUnexpectedChar(a1, "was expecting comma to separate " + this._parsingContext.typeDesc() + " entries");
        }
        final int v1 = /*EL:667*/this._inputPtr;
        /*SL:668*/if (v1 >= this._inputEnd) {
            /*SL:669*/this._minorState = 15;
            /*SL:670*/return this._currToken = JsonToken.NOT_AVAILABLE;
        }
        /*SL:672*/a1 = this._inputBuffer[v1];
        /*SL:673*/this._inputPtr = v1 + 1;
        /*SL:674*/if (a1 <= 32) {
            /*SL:675*/a1 = this._skipWS(a1);
            /*SL:676*/if (a1 <= 0) {
                /*SL:677*/this._minorState = 15;
                /*SL:678*/return this._currToken;
            }
        }
        /*SL:681*/this._updateTokenLocation();
        /*SL:682*/if (a1 == 34) {
            /*SL:683*/return this._startString();
        }
        /*SL:685*/switch (a1) {
            case 35: {
                /*SL:687*/return this._finishHashComment(15);
            }
            case 45: {
                /*SL:689*/return this._startNegativeNumber();
            }
            case 47: {
                /*SL:691*/return this._startSlashComment(15);
            }
            case 48: {
                /*SL:697*/return this._startNumberLeadingZero();
            }
            case 49:
            case 50:
            case 51:
            case 52:
            case 53:
            case 54:
            case 55:
            case 56:
            case 57: {
                /*SL:704*/return this._startPositiveNumber(a1);
            }
            case 102: {
                /*SL:706*/return this._startFalseToken();
            }
            case 110: {
                /*SL:708*/return this._startNullToken();
            }
            case 116: {
                /*SL:710*/return this._startTrueToken();
            }
            case 91: {
                /*SL:712*/return this._startArrayScope();
            }
            case 93: {
                /*SL:715*/if (this.isEnabled(Feature.ALLOW_TRAILING_COMMA)) {
                    /*SL:716*/return this._closeArrayScope();
                }
                break;
            }
            case 123: {
                /*SL:720*/return this._startObjectScope();
            }
            case 125: {
                /*SL:723*/if (this.isEnabled(Feature.ALLOW_TRAILING_COMMA)) {
                    /*SL:724*/return this._closeObjectScope();
                }
                break;
            }
        }
        /*SL:729*/return this._startUnexpectedValue(true, a1);
    }
    
    private final JsonToken _startValueExpectColon(int a1) throws IOException {
        /*SL:740*/if (a1 <= 32) {
            /*SL:741*/a1 = this._skipWS(a1);
            /*SL:742*/if (a1 <= 0) {
                /*SL:743*/this._minorState = 14;
                /*SL:744*/return this._currToken;
            }
        }
        /*SL:747*/if (a1 != 58) {
            /*SL:748*/if (a1 == 47) {
                /*SL:749*/return this._startSlashComment(14);
            }
            /*SL:751*/if (a1 == 35) {
                /*SL:752*/return this._finishHashComment(14);
            }
            /*SL:755*/this._reportUnexpectedChar(a1, "was expecting a colon to separate field name and value");
        }
        final int v1 = /*EL:757*/this._inputPtr;
        /*SL:758*/if (v1 >= this._inputEnd) {
            /*SL:759*/this._minorState = 12;
            /*SL:760*/return this._currToken = JsonToken.NOT_AVAILABLE;
        }
        /*SL:762*/a1 = this._inputBuffer[v1];
        /*SL:763*/this._inputPtr = v1 + 1;
        /*SL:764*/if (a1 <= 32) {
            /*SL:765*/a1 = this._skipWS(a1);
            /*SL:766*/if (a1 <= 0) {
                /*SL:767*/this._minorState = 12;
                /*SL:768*/return this._currToken;
            }
        }
        /*SL:771*/this._updateTokenLocation();
        /*SL:772*/if (a1 == 34) {
            /*SL:773*/return this._startString();
        }
        /*SL:775*/switch (a1) {
            case 35: {
                /*SL:777*/return this._finishHashComment(12);
            }
            case 45: {
                /*SL:779*/return this._startNegativeNumber();
            }
            case 47: {
                /*SL:781*/return this._startSlashComment(12);
            }
            case 48: {
                /*SL:787*/return this._startNumberLeadingZero();
            }
            case 49:
            case 50:
            case 51:
            case 52:
            case 53:
            case 54:
            case 55:
            case 56:
            case 57: {
                /*SL:794*/return this._startPositiveNumber(a1);
            }
            case 102: {
                /*SL:796*/return this._startFalseToken();
            }
            case 110: {
                /*SL:798*/return this._startNullToken();
            }
            case 116: {
                /*SL:800*/return this._startTrueToken();
            }
            case 91: {
                /*SL:802*/return this._startArrayScope();
            }
            case 123: {
                /*SL:804*/return this._startObjectScope();
            }
            default: {
                /*SL:807*/return this._startUnexpectedValue(false, a1);
            }
        }
    }
    
    private final JsonToken _startValueAfterComma(int a1) throws IOException {
        /*SL:815*/if (a1 <= 32) {
            /*SL:816*/a1 = this._skipWS(a1);
            /*SL:817*/if (a1 <= 0) {
                /*SL:818*/this._minorState = 15;
                /*SL:819*/return this._currToken;
            }
        }
        /*SL:822*/this._updateTokenLocation();
        /*SL:823*/if (a1 == 34) {
            /*SL:824*/return this._startString();
        }
        /*SL:826*/switch (a1) {
            case 35: {
                /*SL:828*/return this._finishHashComment(15);
            }
            case 45: {
                /*SL:830*/return this._startNegativeNumber();
            }
            case 47: {
                /*SL:832*/return this._startSlashComment(15);
            }
            case 48: {
                /*SL:838*/return this._startNumberLeadingZero();
            }
            case 49:
            case 50:
            case 51:
            case 52:
            case 53:
            case 54:
            case 55:
            case 56:
            case 57: {
                /*SL:848*/return this._startPositiveNumber(a1);
            }
            case 102: {
                /*SL:850*/return this._startFalseToken();
            }
            case 110: {
                /*SL:852*/return this._startNullToken();
            }
            case 116: {
                /*SL:854*/return this._startTrueToken();
            }
            case 91: {
                /*SL:856*/return this._startArrayScope();
            }
            case 93: {
                /*SL:859*/if (this.isEnabled(Feature.ALLOW_TRAILING_COMMA)) {
                    /*SL:860*/return this._closeArrayScope();
                }
                break;
            }
            case 123: {
                /*SL:864*/return this._startObjectScope();
            }
            case 125: {
                /*SL:867*/if (this.isEnabled(Feature.ALLOW_TRAILING_COMMA)) {
                    /*SL:868*/return this._closeObjectScope();
                }
                break;
            }
        }
        /*SL:873*/return this._startUnexpectedValue(true, a1);
    }
    
    protected JsonToken _startUnexpectedValue(final boolean a1, final int a2) throws IOException {
        /*SL:878*/switch (a2) {
            case 93: {
                /*SL:880*/if (!this._parsingContext.inArray()) {
                    /*SL:881*/break;
                }
            }
            case 44: {
                /*SL:888*/if (this.isEnabled(Feature.ALLOW_MISSING_VALUES)) {
                    /*SL:889*/--this._inputPtr;
                    /*SL:890*/return this._valueComplete(JsonToken.VALUE_NULL);
                }
                break;
            }
            case 39: {
                /*SL:898*/if (this.isEnabled(Feature.ALLOW_SINGLE_QUOTES)) {
                    /*SL:899*/return this._startAposString();
                }
                break;
            }
            case 43: {
                /*SL:903*/return this._finishNonStdToken(2, 1);
            }
            case 78: {
                /*SL:905*/return this._finishNonStdToken(0, 1);
            }
            case 73: {
                /*SL:907*/return this._finishNonStdToken(1, 1);
            }
        }
        /*SL:910*/this._reportUnexpectedChar(a2, "expected a valid value (number, String, array, object, 'true', 'false' or 'null')");
        /*SL:911*/return null;
    }
    
    private final int _skipWS(int a1) throws IOException {
        /*SL:939*/do {
            if (a1 != 32) {
                if (a1 == 10) {
                    ++this._currInputRow;
                    this._currInputRowStart = this._inputPtr;
                }
                else if (a1 == 13) {
                    ++this._currInputRowAlt;
                    this._currInputRowStart = this._inputPtr;
                }
                else if (a1 != 9) {
                    this._throwInvalidSpace(a1);
                }
            }
            if (this._inputPtr >= this._inputEnd) {
                this._currToken = JsonToken.NOT_AVAILABLE;
                return 0;
            }
            a1 = (this._inputBuffer[this._inputPtr++] & 0xFF);
        } while (a1 <= 32);
        /*SL:940*/return a1;
    }
    
    private final JsonToken _startSlashComment(final int a1) throws IOException {
        /*SL:945*/if (!Feature.ALLOW_COMMENTS.enabledIn(this._features)) {
            /*SL:946*/this._reportUnexpectedChar(47, "maybe a (non-standard) comment? (not recognized as one since Feature 'ALLOW_COMMENTS' not enabled for parser)");
        }
        /*SL:950*/if (this._inputPtr >= this._inputEnd) {
            /*SL:951*/this._pending32 = a1;
            /*SL:952*/this._minorState = 51;
            /*SL:953*/return this._currToken = JsonToken.NOT_AVAILABLE;
        }
        final int v1 = /*EL:955*/this._inputBuffer[this._inputPtr++];
        /*SL:956*/if (v1 == 42) {
            /*SL:957*/return this._finishCComment(a1, false);
        }
        /*SL:959*/if (v1 == 47) {
            /*SL:960*/return this._finishCppComment(a1);
        }
        /*SL:962*/this._reportUnexpectedChar(v1 & 0xFF, "was expecting either '*' or '/' for a comment");
        /*SL:963*/return null;
    }
    
    private final JsonToken _finishHashComment(final int v2) throws IOException {
        /*SL:969*/if (!Feature.ALLOW_YAML_COMMENTS.enabledIn(this._features)) {
            /*SL:970*/this._reportUnexpectedChar(35, "maybe a (non-standard) comment? (not recognized as one since Feature 'ALLOW_YAML_COMMENTS' not enabled for parser)");
        }
        /*SL:973*/while (this._inputPtr < this._inputEnd) {
            final int a1 = /*EL:978*/this._inputBuffer[this._inputPtr++] & 0xFF;
            /*SL:979*/if (a1 < 32) {
                /*SL:980*/if (a1 == 10) {
                    /*SL:981*/++this._currInputRow;
                    /*SL:982*/this._currInputRowStart = this._inputPtr;
                }
                else/*SL:984*/ if (a1 == 13) {
                    /*SL:985*/++this._currInputRowAlt;
                    /*SL:986*/this._currInputRowStart = this._inputPtr;
                }
                else {
                    /*SL:988*/if (a1 != 9) {
                        /*SL:989*/this._throwInvalidSpace(a1);
                        continue;
                    }
                    continue;
                }
                /*SL:993*/return this._startAfterComment(v2);
            }
        }
        this._minorState = 55;
        this._pending32 = v2;
        return this._currToken = JsonToken.NOT_AVAILABLE;
    }
    
    private final JsonToken _finishCppComment(final int v2) throws IOException {
        /*SL:999*/while (this._inputPtr < this._inputEnd) {
            final int a1 = /*EL:1004*/this._inputBuffer[this._inputPtr++] & 0xFF;
            /*SL:1005*/if (a1 < 32) {
                /*SL:1006*/if (a1 == 10) {
                    /*SL:1007*/++this._currInputRow;
                    /*SL:1008*/this._currInputRowStart = this._inputPtr;
                }
                else/*SL:1010*/ if (a1 == 13) {
                    /*SL:1011*/++this._currInputRowAlt;
                    /*SL:1012*/this._currInputRowStart = this._inputPtr;
                }
                else {
                    /*SL:1014*/if (a1 != 9) {
                        /*SL:1015*/this._throwInvalidSpace(a1);
                        continue;
                    }
                    continue;
                }
                /*SL:1019*/return this._startAfterComment(v2);
            }
        }
        this._minorState = 54;
        this._pending32 = v2;
        return this._currToken = JsonToken.NOT_AVAILABLE;
    }
    
    private final JsonToken _finishCComment(final int v1, boolean v2) throws IOException {
        /*SL:1025*/while (this._inputPtr < this._inputEnd) {
            final int a1 = /*EL:1030*/this._inputBuffer[this._inputPtr++] & 0xFF;
            /*SL:1031*/if (a1 < 32) {
                /*SL:1032*/if (a1 == 10) {
                    /*SL:1033*/++this._currInputRow;
                    /*SL:1034*/this._currInputRowStart = this._inputPtr;
                }
                else/*SL:1035*/ if (a1 == 13) {
                    /*SL:1036*/++this._currInputRowAlt;
                    /*SL:1037*/this._currInputRowStart = this._inputPtr;
                }
                else/*SL:1038*/ if (a1 != 9) {
                    /*SL:1039*/this._throwInvalidSpace(a1);
                }
            }
            else {
                /*SL:1041*/if (a1 == 42) {
                    /*SL:1042*/v2 = true;
                    /*SL:1043*/continue;
                }
                /*SL:1044*/if (a1 == 47 && /*EL:1045*/v2) {
                    /*SL:1051*/return this._startAfterComment(v1);
                }
            }
            v2 = false;
        }
        this._minorState = (v2 ? 52 : 53);
        this._pending32 = v1;
        return this._currToken = JsonToken.NOT_AVAILABLE;
    }
    
    private final JsonToken _startAfterComment(final int a1) throws IOException {
        /*SL:1057*/if (this._inputPtr >= this._inputEnd) {
            /*SL:1058*/this._minorState = a1;
            /*SL:1059*/return this._currToken = JsonToken.NOT_AVAILABLE;
        }
        final int v1 = /*EL:1061*/this._inputBuffer[this._inputPtr++] & 0xFF;
        /*SL:1062*/switch (a1) {
            case 4: {
                /*SL:1064*/return this._startFieldName(v1);
            }
            case 5: {
                /*SL:1066*/return this._startFieldNameAfterComma(v1);
            }
            case 12: {
                /*SL:1068*/return this._startValue(v1);
            }
            case 13: {
                /*SL:1070*/return this._startValueExpectComma(v1);
            }
            case 14: {
                /*SL:1072*/return this._startValueExpectColon(v1);
            }
            case 15: {
                /*SL:1074*/return this._startValueAfterComma(v1);
            }
            default: {
                /*SL:1077*/VersionUtil.throwInternal();
                /*SL:1078*/return null;
            }
        }
    }
    
    protected JsonToken _startFalseToken() throws IOException {
        int inputPtr = /*EL:1089*/this._inputPtr;
        /*SL:1090*/if (inputPtr + 4 < this._inputEnd) {
            final byte[] v0 = /*EL:1091*/this._inputBuffer;
            /*SL:1092*/if (v0[inputPtr++] == 97 && v0[inputPtr++] == 108 && v0[inputPtr++] == 115 && v0[inputPtr++] == 101) {
                final int v = /*EL:1096*/v0[inputPtr] & 0xFF;
                /*SL:1097*/if (v < 48 || v == 93 || v == 125) {
                    /*SL:1098*/this._inputPtr = inputPtr;
                    /*SL:1099*/return this._valueComplete(JsonToken.VALUE_FALSE);
                }
            }
        }
        /*SL:1103*/this._minorState = 18;
        /*SL:1104*/return this._finishKeywordToken("false", 1, JsonToken.VALUE_FALSE);
    }
    
    protected JsonToken _startTrueToken() throws IOException {
        int inputPtr = /*EL:1109*/this._inputPtr;
        /*SL:1110*/if (inputPtr + 3 < this._inputEnd) {
            final byte[] v0 = /*EL:1111*/this._inputBuffer;
            /*SL:1112*/if (v0[inputPtr++] == 114 && v0[inputPtr++] == 117 && v0[inputPtr++] == 101) {
                final int v = /*EL:1115*/v0[inputPtr] & 0xFF;
                /*SL:1116*/if (v < 48 || v == 93 || v == 125) {
                    /*SL:1117*/this._inputPtr = inputPtr;
                    /*SL:1118*/return this._valueComplete(JsonToken.VALUE_TRUE);
                }
            }
        }
        /*SL:1122*/this._minorState = 17;
        /*SL:1123*/return this._finishKeywordToken("true", 1, JsonToken.VALUE_TRUE);
    }
    
    protected JsonToken _startNullToken() throws IOException {
        int inputPtr = /*EL:1128*/this._inputPtr;
        /*SL:1129*/if (inputPtr + 3 < this._inputEnd) {
            final byte[] v0 = /*EL:1130*/this._inputBuffer;
            /*SL:1131*/if (v0[inputPtr++] == 117 && v0[inputPtr++] == 108 && v0[inputPtr++] == 108) {
                final int v = /*EL:1134*/v0[inputPtr] & 0xFF;
                /*SL:1135*/if (v < 48 || v == 93 || v == 125) {
                    /*SL:1136*/this._inputPtr = inputPtr;
                    /*SL:1137*/return this._valueComplete(JsonToken.VALUE_NULL);
                }
            }
        }
        /*SL:1141*/this._minorState = 16;
        /*SL:1142*/return this._finishKeywordToken("null", 1, JsonToken.VALUE_NULL);
    }
    
    protected JsonToken _finishKeywordToken(final String a3, int v1, final JsonToken v2) throws IOException {
        final int v3 = /*EL:1148*/a3.length();
        /*SL:1151*/while (this._inputPtr < this._inputEnd) {
            final int a4 = /*EL:1155*/this._inputBuffer[this._inputPtr];
            /*SL:1156*/if (v1 == v3) {
                /*SL:1157*/if (a4 < 48 || a4 == 93 || a4 == 125) {
                    /*SL:1158*/return this._valueComplete(v2);
                }
            }
            else/*SL:1162*/ if (a4 == a3.charAt(v1)) {
                /*SL:1165*/++v1;
                /*SL:1166*/++this._inputPtr;
                /*SL:1167*/continue;
            }
            /*SL:1168*/this._minorState = 50;
            /*SL:1169*/this._textBuffer.resetWithCopy(a3, 0, v1);
            /*SL:1170*/return this._finishErrorToken();
        }
        this._pending32 = v1;
        return this._currToken = JsonToken.NOT_AVAILABLE;
    }
    
    protected JsonToken _finishKeywordTokenWithEOF(final String a1, final int a2, final JsonToken a3) throws IOException {
        /*SL:1176*/if (a2 == a1.length()) {
            /*SL:1177*/return this._currToken = a3;
        }
        /*SL:1179*/this._textBuffer.resetWithCopy(a1, 0, a2);
        /*SL:1180*/return this._finishErrorTokenWithEOF();
    }
    
    protected JsonToken _finishNonStdToken(final int v1, int v2) throws IOException {
        final String v3 = /*EL:1185*/this._nonStdToken(v1);
        final int v4 = /*EL:1186*/v3.length();
        /*SL:1189*/while (this._inputPtr < this._inputEnd) {
            final int a1 = /*EL:1195*/this._inputBuffer[this._inputPtr];
            /*SL:1196*/if (v2 == v4) {
                /*SL:1197*/if (a1 < 48 || a1 == 93 || a1 == 125) {
                    /*SL:1198*/return this._valueNonStdNumberComplete(v1);
                }
            }
            else/*SL:1202*/ if (a1 == v3.charAt(v2)) {
                /*SL:1205*/++v2;
                /*SL:1206*/++this._inputPtr;
                /*SL:1207*/continue;
            }
            /*SL:1208*/this._minorState = 50;
            /*SL:1209*/this._textBuffer.resetWithCopy(v3, 0, v2);
            /*SL:1210*/return this._finishErrorToken();
        }
        this._nonStdTokenType = v1;
        this._pending32 = v2;
        this._minorState = 19;
        return this._currToken = JsonToken.NOT_AVAILABLE;
    }
    
    protected JsonToken _finishNonStdTokenWithEOF(final int a1, final int a2) throws IOException {
        final String v1 = /*EL:1215*/this._nonStdToken(a1);
        /*SL:1216*/if (a2 == v1.length()) {
            /*SL:1217*/return this._valueNonStdNumberComplete(a1);
        }
        /*SL:1219*/this._textBuffer.resetWithCopy(v1, 0, a2);
        /*SL:1220*/return this._finishErrorTokenWithEOF();
    }
    
    protected JsonToken _finishErrorToken() throws IOException {
        /*SL:1225*/while (this._inputPtr < this._inputEnd) {
            final int v1 = /*EL:1226*/this._inputBuffer[this._inputPtr++];
            final char v2 = /*EL:1231*/(char)v1;
            /*SL:1232*/if (Character.isJavaIdentifierPart(v2)) {
                /*SL:1235*/this._textBuffer.append(v2);
                /*SL:1236*/if (this._textBuffer.size() < 256) {
                    /*SL:1237*/continue;
                }
            }
            /*SL:1240*/return this._reportErrorToken(this._textBuffer.contentsAsString());
        }
        /*SL:1242*/return this._currToken = JsonToken.NOT_AVAILABLE;
    }
    
    protected JsonToken _finishErrorTokenWithEOF() throws IOException {
        /*SL:1247*/return this._reportErrorToken(this._textBuffer.contentsAsString());
    }
    
    protected JsonToken _reportErrorToken(final String a1) throws IOException {
        /*SL:1253*/this._reportError("Unrecognized token '%s': was expecting %s", this._textBuffer.contentsAsString(), "'null', 'true' or 'false'");
        /*SL:1255*/return JsonToken.NOT_AVAILABLE;
    }
    
    protected JsonToken _startPositiveNumber(int a1) throws IOException {
        /*SL:1266*/this._numberNegative = false;
        char[] v1 = /*EL:1267*/this._textBuffer.emptyAndGetCurrentSegment();
        /*SL:1268*/v1[0] = (char)a1;
        /*SL:1270*/if (this._inputPtr >= this._inputEnd) {
            /*SL:1271*/this._minorState = 26;
            /*SL:1272*/this._textBuffer.setCurrentLength(1);
            /*SL:1273*/return this._currToken = JsonToken.NOT_AVAILABLE;
        }
        int v2 = /*EL:1276*/1;
        /*SL:1278*/a1 = (this._inputBuffer[this._inputPtr] & 0xFF);
        while (true) {
            /*SL:1280*/while (a1 >= 48) {
                /*SL:1288*/if (a1 > 57) {
                    /*SL:1289*/if (a1 == 101 || a1 == 69) {
                        /*SL:1290*/this._intLength = v2;
                        /*SL:1291*/++this._inputPtr;
                        /*SL:1292*/return this._startFloat(v1, v2, a1);
                    }
                    /*SL:1309*/this._intLength = v2;
                    /*SL:1310*/this._textBuffer.setCurrentLength(v2);
                    /*SL:1311*/return this._valueComplete(JsonToken.VALUE_NUMBER_INT);
                }
                else {
                    if (v2 >= v1.length) {
                        v1 = this._textBuffer.expandCurrentSegment();
                    }
                    v1[v2++] = (char)a1;
                    if (++this._inputPtr >= this._inputEnd) {
                        this._minorState = 26;
                        this._textBuffer.setCurrentLength(v2);
                        return this._currToken = JsonToken.NOT_AVAILABLE;
                    }
                    a1 = (this._inputBuffer[this._inputPtr] & 0xFF);
                }
            }
            if (a1 == 46) {
                this._intLength = v2;
                ++this._inputPtr;
                return this._startFloat(v1, v2, a1);
            }
            continue;
        }
    }
    
    protected JsonToken _startNegativeNumber() throws IOException {
        /*SL:1316*/this._numberNegative = true;
        /*SL:1317*/if (this._inputPtr >= this._inputEnd) {
            /*SL:1318*/this._minorState = 23;
            /*SL:1319*/return this._currToken = JsonToken.NOT_AVAILABLE;
        }
        int v1 = /*EL:1321*/this._inputBuffer[this._inputPtr++] & 0xFF;
        /*SL:1322*/if (v1 <= 48) {
            /*SL:1323*/if (v1 == 48) {
                /*SL:1324*/return this._finishNumberLeadingNegZeroes();
            }
            /*SL:1327*/this.reportUnexpectedNumberChar(v1, "expected digit (0-9) to follow minus sign, for valid numeric value");
        }
        else/*SL:1328*/ if (v1 > 57) {
            /*SL:1329*/if (v1 == 73) {
                /*SL:1330*/return this._finishNonStdToken(3, 2);
            }
            /*SL:1332*/this.reportUnexpectedNumberChar(v1, "expected digit (0-9) to follow minus sign, for valid numeric value");
        }
        char[] v2 = /*EL:1334*/this._textBuffer.emptyAndGetCurrentSegment();
        /*SL:1335*/v2[0] = '-';
        /*SL:1336*/v2[1] = (char)v1;
        /*SL:1337*/if (this._inputPtr >= this._inputEnd) {
            /*SL:1338*/this._minorState = 26;
            /*SL:1339*/this._textBuffer.setCurrentLength(2);
            /*SL:1340*/this._intLength = 1;
            /*SL:1341*/return this._currToken = JsonToken.NOT_AVAILABLE;
        }
        /*SL:1343*/v1 = this._inputBuffer[this._inputPtr];
        int v3 = /*EL:1344*/2;
        while (true) {
            /*SL:1347*/while (v1 >= 48) {
                /*SL:1355*/if (v1 > 57) {
                    /*SL:1356*/if (v1 == 101 || v1 == 69) {
                        /*SL:1357*/this._intLength = v3 - 1;
                        /*SL:1358*/++this._inputPtr;
                        /*SL:1359*/return this._startFloat(v2, v3, v1);
                    }
                    /*SL:1375*/this._intLength = v3 - 1;
                    /*SL:1376*/this._textBuffer.setCurrentLength(v3);
                    /*SL:1377*/return this._valueComplete(JsonToken.VALUE_NUMBER_INT);
                }
                else {
                    if (v3 >= v2.length) {
                        v2 = this._textBuffer.expandCurrentSegment();
                    }
                    v2[v3++] = (char)v1;
                    if (++this._inputPtr >= this._inputEnd) {
                        this._minorState = 26;
                        this._textBuffer.setCurrentLength(v3);
                        return this._currToken = JsonToken.NOT_AVAILABLE;
                    }
                    v1 = (this._inputBuffer[this._inputPtr] & 0xFF);
                }
            }
            if (v1 == 46) {
                this._intLength = v3 - 1;
                ++this._inputPtr;
                return this._startFloat(v2, v3, v1);
            }
            continue;
        }
    }
    
    protected JsonToken _startNumberLeadingZero() throws IOException {
        int inputPtr = /*EL:1382*/this._inputPtr;
        /*SL:1383*/if (inputPtr >= this._inputEnd) {
            /*SL:1384*/this._minorState = 24;
            /*SL:1385*/return this._currToken = JsonToken.NOT_AVAILABLE;
        }
        final int v0 = /*EL:1392*/this._inputBuffer[inputPtr++] & 0xFF;
        /*SL:1394*/if (v0 < 48) {
            /*SL:1395*/if (v0 == 46) {
                /*SL:1396*/this._inputPtr = inputPtr;
                /*SL:1397*/this._intLength = 1;
                final char[] v = /*EL:1398*/this._textBuffer.emptyAndGetCurrentSegment();
                /*SL:1399*/v[0] = '0';
                /*SL:1400*/return this._startFloat(v, 1, v0);
            }
        }
        else {
            /*SL:1402*/if (v0 <= 57) {
                /*SL:1419*/return this._finishNumberLeadingZeroes();
            }
            if (v0 == 101 || v0 == 69) {
                this._inputPtr = inputPtr;
                this._intLength = 1;
                final char[] v = this._textBuffer.emptyAndGetCurrentSegment();
                v[0] = '0';
                return this._startFloat(v, 1, v0);
            }
            if (v0 != 93 && v0 != 125) {
                this.reportUnexpectedNumberChar(v0, "expected digit (0-9), decimal point (.) or exponent indicator (e/E) to follow '0'");
            }
        }
        /*SL:1422*/return this._valueCompleteInt(0, "0");
    }
    
    protected JsonToken _finishNumberMinus(final int a1) throws IOException {
        /*SL:1427*/if (a1 <= 48) {
            /*SL:1428*/if (a1 == 48) {
                /*SL:1429*/return this._finishNumberLeadingNegZeroes();
            }
            /*SL:1431*/this.reportUnexpectedNumberChar(a1, "expected digit (0-9) to follow minus sign, for valid numeric value");
        }
        else/*SL:1432*/ if (a1 > 57) {
            /*SL:1433*/if (a1 == 73) {
                /*SL:1434*/return this._finishNonStdToken(3, 2);
            }
            /*SL:1436*/this.reportUnexpectedNumberChar(a1, "expected digit (0-9) to follow minus sign, for valid numeric value");
        }
        final char[] v1 = /*EL:1438*/this._textBuffer.emptyAndGetCurrentSegment();
        /*SL:1439*/v1[0] = '-';
        /*SL:1440*/v1[1] = (char)a1;
        /*SL:1441*/this._intLength = 1;
        /*SL:1442*/return this._finishNumberIntegralPart(v1, 2);
    }
    
    protected JsonToken _finishNumberLeadingZeroes() throws IOException {
        /*SL:1450*/while (this._inputPtr < this._inputEnd) {
            final int v0 = /*EL:1454*/this._inputBuffer[this._inputPtr++] & 0xFF;
            /*SL:1455*/if (v0 < 48) {
                /*SL:1456*/if (v0 == 46) {
                    final char[] v = /*EL:1457*/this._textBuffer.emptyAndGetCurrentSegment();
                    /*SL:1458*/v[0] = '0';
                    /*SL:1459*/this._intLength = 1;
                    /*SL:1460*/return this._startFloat(v, 1, v0);
                }
            }
            else/*SL:1462*/ if (v0 > 57) {
                /*SL:1463*/if (v0 == 101 || v0 == 69) {
                    final char[] v = /*EL:1464*/this._textBuffer.emptyAndGetCurrentSegment();
                    /*SL:1465*/v[0] = '0';
                    /*SL:1466*/this._intLength = 1;
                    /*SL:1467*/return this._startFloat(v, 1, v0);
                }
                /*SL:1472*/if (v0 != 93 && v0 != 125) {
                    /*SL:1473*/this.reportUnexpectedNumberChar(v0, "expected digit (0-9), decimal point (.) or exponent indicator (e/E) to follow '0'");
                }
            }
            else {
                /*SL:1479*/if (!this.isEnabled(Feature.ALLOW_NUMERIC_LEADING_ZEROS)) {
                    /*SL:1480*/this.reportInvalidNumber("Leading zeroes not allowed");
                }
                /*SL:1482*/if (v0 == 48) {
                    /*SL:1483*/continue;
                }
                final char[] v = /*EL:1485*/this._textBuffer.emptyAndGetCurrentSegment();
                /*SL:1487*/v[0] = (char)v0;
                /*SL:1488*/this._intLength = 1;
                /*SL:1489*/return this._finishNumberIntegralPart(v, 1);
            }
            /*SL:1491*/--this._inputPtr;
            /*SL:1492*/return this._valueCompleteInt(0, "0");
        }
        this._minorState = 24;
        return this._currToken = JsonToken.NOT_AVAILABLE;
    }
    
    protected JsonToken _finishNumberLeadingNegZeroes() throws IOException {
        /*SL:1501*/while (this._inputPtr < this._inputEnd) {
            final int v0 = /*EL:1505*/this._inputBuffer[this._inputPtr++] & 0xFF;
            /*SL:1506*/if (v0 < 48) {
                /*SL:1507*/if (v0 == 46) {
                    final char[] v = /*EL:1508*/this._textBuffer.emptyAndGetCurrentSegment();
                    /*SL:1509*/v[0] = '-';
                    /*SL:1510*/v[1] = '0';
                    /*SL:1511*/this._intLength = 1;
                    /*SL:1512*/return this._startFloat(v, 2, v0);
                }
            }
            else/*SL:1514*/ if (v0 > 57) {
                /*SL:1515*/if (v0 == 101 || v0 == 69) {
                    final char[] v = /*EL:1516*/this._textBuffer.emptyAndGetCurrentSegment();
                    /*SL:1517*/v[0] = '-';
                    /*SL:1518*/v[1] = '0';
                    /*SL:1519*/this._intLength = 1;
                    /*SL:1520*/return this._startFloat(v, 2, v0);
                }
                /*SL:1525*/if (v0 != 93 && v0 != 125) {
                    /*SL:1526*/this.reportUnexpectedNumberChar(v0, "expected digit (0-9), decimal point (.) or exponent indicator (e/E) to follow '0'");
                }
            }
            else {
                /*SL:1532*/if (!this.isEnabled(Feature.ALLOW_NUMERIC_LEADING_ZEROS)) {
                    /*SL:1533*/this.reportInvalidNumber("Leading zeroes not allowed");
                }
                /*SL:1535*/if (v0 == 48) {
                    /*SL:1536*/continue;
                }
                final char[] v = /*EL:1538*/this._textBuffer.emptyAndGetCurrentSegment();
                /*SL:1540*/v[0] = '-';
                /*SL:1541*/v[1] = (char)v0;
                /*SL:1542*/this._intLength = 1;
                /*SL:1543*/return this._finishNumberIntegralPart(v, 2);
            }
            /*SL:1545*/--this._inputPtr;
            /*SL:1546*/return this._valueCompleteInt(0, "0");
        }
        this._minorState = 25;
        return this._currToken = JsonToken.NOT_AVAILABLE;
    }
    
    protected JsonToken _finishNumberIntegralPart(char[] v1, int v2) throws IOException {
        final int v3 = /*EL:1552*/this._numberNegative ? -1 : 0;
        /*SL:1555*/while (this._inputPtr < this._inputEnd) {
            final int a1 = /*EL:1560*/this._inputBuffer[this._inputPtr] & 0xFF;
            /*SL:1561*/if (a1 < 48) {
                /*SL:1562*/if (a1 == 46) {
                    /*SL:1563*/this._intLength = v2 + v3;
                    /*SL:1564*/++this._inputPtr;
                    /*SL:1565*/return this._startFloat(v1, v2, a1);
                }
            }
            else {
                /*SL:1569*/if (a1 <= 57) {
                    /*SL:1577*/++this._inputPtr;
                    /*SL:1578*/if (v2 >= v1.length) {
                        /*SL:1581*/v1 = this._textBuffer.expandCurrentSegment();
                    }
                    /*SL:1583*/v1[v2++] = (char)a1;
                    /*SL:1584*/continue;
                }
                if (a1 == 101 || a1 == 69) {
                    this._intLength = v2 + v3;
                    ++this._inputPtr;
                    return this._startFloat(v1, v2, a1);
                }
            }
            /*SL:1585*/this._intLength = v2 + v3;
            /*SL:1586*/this._textBuffer.setCurrentLength(v2);
            /*SL:1587*/return this._valueComplete(JsonToken.VALUE_NUMBER_INT);
        }
        this._minorState = 26;
        this._textBuffer.setCurrentLength(v2);
        return this._currToken = JsonToken.NOT_AVAILABLE;
    }
    
    protected JsonToken _startFloat(char[] a1, int a2, int a3) throws IOException {
        int v1 = /*EL:1592*/0;
        Label_0150: {
            /*SL:1593*/if (a3 == 46) {
                /*SL:1594*/if (a2 >= a1.length) {
                    /*SL:1595*/a1 = this._textBuffer.expandCurrentSegment();
                }
                /*SL:1597*/a1[a2++] = '.';
                /*SL:1599*/while (this._inputPtr < this._inputEnd) {
                    /*SL:1605*/a3 = this._inputBuffer[this._inputPtr++];
                    /*SL:1606*/if (a3 < 48 || a3 > 57) {
                        /*SL:1607*/a3 &= 0xFF;
                        /*SL:1609*/if (v1 == 0) {
                            /*SL:1610*/this.reportUnexpectedNumberChar(a3, "Decimal point not followed by a digit");
                        }
                        break Label_0150;
                    }
                    else {
                        /*SL:1614*/if (a2 >= a1.length) {
                            /*SL:1615*/a1 = this._textBuffer.expandCurrentSegment();
                        }
                        /*SL:1617*/a1[a2++] = (char)a3;
                        /*SL:1618*/++v1;
                    }
                }
                this._textBuffer.setCurrentLength(a2);
                this._minorState = 30;
                this._fractLength = v1;
                return this._currToken = JsonToken.NOT_AVAILABLE;
            }
        }
        /*SL:1621*/this._fractLength = v1;
        int v2 = /*EL:1622*/0;
        /*SL:1623*/if (a3 == 101 || a3 == 69) {
            /*SL:1624*/if (a2 >= a1.length) {
                /*SL:1625*/a1 = this._textBuffer.expandCurrentSegment();
            }
            /*SL:1627*/a1[a2++] = (char)a3;
            /*SL:1628*/if (this._inputPtr >= this._inputEnd) {
                /*SL:1629*/this._textBuffer.setCurrentLength(a2);
                /*SL:1630*/this._minorState = 31;
                /*SL:1631*/this._expLength = 0;
                /*SL:1632*/return this._currToken = JsonToken.NOT_AVAILABLE;
            }
            /*SL:1634*/a3 = this._inputBuffer[this._inputPtr++];
            /*SL:1635*/if (a3 == 45 || a3 == 43) {
                /*SL:1636*/if (a2 >= a1.length) {
                    /*SL:1637*/a1 = this._textBuffer.expandCurrentSegment();
                }
                /*SL:1639*/a1[a2++] = (char)a3;
                /*SL:1640*/if (this._inputPtr >= this._inputEnd) {
                    /*SL:1641*/this._textBuffer.setCurrentLength(a2);
                    /*SL:1642*/this._minorState = 32;
                    /*SL:1643*/this._expLength = 0;
                    /*SL:1644*/return this._currToken = JsonToken.NOT_AVAILABLE;
                }
                /*SL:1646*/a3 = this._inputBuffer[this._inputPtr++];
            }
            /*SL:1648*/while (a3 >= 48 && a3 <= 57) {
                /*SL:1649*/++v2;
                /*SL:1650*/if (a2 >= a1.length) {
                    /*SL:1651*/a1 = this._textBuffer.expandCurrentSegment();
                }
                /*SL:1653*/a1[a2++] = (char)a3;
                /*SL:1654*/if (this._inputPtr >= this._inputEnd) {
                    /*SL:1655*/this._textBuffer.setCurrentLength(a2);
                    /*SL:1656*/this._minorState = 32;
                    /*SL:1657*/this._expLength = v2;
                    /*SL:1658*/return this._currToken = JsonToken.NOT_AVAILABLE;
                }
                /*SL:1660*/a3 = this._inputBuffer[this._inputPtr++];
            }
            /*SL:1663*/a3 &= 0xFF;
            /*SL:1664*/if (v2 == 0) {
                /*SL:1665*/this.reportUnexpectedNumberChar(a3, "Exponent indicator not followed by a digit");
            }
        }
        /*SL:1669*/--this._inputPtr;
        /*SL:1670*/this._textBuffer.setCurrentLength(a2);
        /*SL:1672*/this._expLength = v2;
        /*SL:1673*/return this._valueComplete(JsonToken.VALUE_NUMBER_FLOAT);
    }
    
    protected JsonToken _finishFloatFraction() throws IOException {
        int v1 = /*EL:1678*/this._fractLength;
        char[] v2 = /*EL:1679*/this._textBuffer.getBufferWithoutReset();
        int v3 = /*EL:1680*/this._textBuffer.getCurrentSegmentSize();
        int v4;
        /*SL:1684*/while ((v4 = this._inputBuffer[this._inputPtr++]) >= 48 && v4 <= 57) {
            /*SL:1685*/++v1;
            /*SL:1686*/if (v3 >= v2.length) {
                /*SL:1687*/v2 = this._textBuffer.expandCurrentSegment();
            }
            /*SL:1689*/v2[v3++] = (char)v4;
            /*SL:1690*/if (this._inputPtr >= this._inputEnd) {
                /*SL:1691*/this._textBuffer.setCurrentLength(v3);
                /*SL:1692*/this._fractLength = v1;
                /*SL:1693*/return JsonToken.NOT_AVAILABLE;
            }
        }
        /*SL:1699*/if (v1 == 0) {
            /*SL:1700*/this.reportUnexpectedNumberChar(v4, "Decimal point not followed by a digit");
        }
        /*SL:1702*/this._fractLength = v1;
        /*SL:1703*/this._textBuffer.setCurrentLength(v3);
        /*SL:1706*/if (v4 != 101 && v4 != 69) {
            /*SL:1718*/--this._inputPtr;
            /*SL:1719*/this._textBuffer.setCurrentLength(v3);
            /*SL:1721*/this._expLength = 0;
            /*SL:1722*/return this._valueComplete(JsonToken.VALUE_NUMBER_FLOAT);
        }
        this._textBuffer.append((char)v4);
        this._expLength = 0;
        if (this._inputPtr >= this._inputEnd) {
            this._minorState = 31;
            return JsonToken.NOT_AVAILABLE;
        }
        this._minorState = 32;
        return this._finishFloatExponent(true, this._inputBuffer[this._inputPtr++] & 0xFF);
    }
    
    protected JsonToken _finishFloatExponent(final boolean a1, int a2) throws IOException {
        /*SL:1727*/if (a1) {
            /*SL:1728*/this._minorState = 32;
            /*SL:1729*/if (a2 == 45 || a2 == 43) {
                /*SL:1730*/this._textBuffer.append((char)a2);
                /*SL:1731*/if (this._inputPtr >= this._inputEnd) {
                    /*SL:1732*/this._minorState = 32;
                    /*SL:1733*/this._expLength = 0;
                    /*SL:1734*/return JsonToken.NOT_AVAILABLE;
                }
                /*SL:1736*/a2 = this._inputBuffer[this._inputPtr++];
            }
        }
        char[] v1 = /*EL:1740*/this._textBuffer.getBufferWithoutReset();
        int v2 = /*EL:1741*/this._textBuffer.getCurrentSegmentSize();
        int v3 = /*EL:1742*/this._expLength;
        /*SL:1744*/while (a2 >= 48 && a2 <= 57) {
            /*SL:1745*/++v3;
            /*SL:1746*/if (v2 >= v1.length) {
                /*SL:1747*/v1 = this._textBuffer.expandCurrentSegment();
            }
            /*SL:1749*/v1[v2++] = (char)a2;
            /*SL:1750*/if (this._inputPtr >= this._inputEnd) {
                /*SL:1751*/this._textBuffer.setCurrentLength(v2);
                /*SL:1752*/this._expLength = v3;
                /*SL:1753*/return JsonToken.NOT_AVAILABLE;
            }
            /*SL:1755*/a2 = this._inputBuffer[this._inputPtr++];
        }
        /*SL:1758*/a2 &= 0xFF;
        /*SL:1759*/if (v3 == 0) {
            /*SL:1760*/this.reportUnexpectedNumberChar(a2, "Exponent indicator not followed by a digit");
        }
        /*SL:1763*/--this._inputPtr;
        /*SL:1764*/this._textBuffer.setCurrentLength(v2);
        /*SL:1766*/this._expLength = v3;
        /*SL:1767*/return this._valueComplete(JsonToken.VALUE_NUMBER_FLOAT);
    }
    
    private final String _fastParseName() throws IOException {
        final byte[] inputBuffer = /*EL:1783*/this._inputBuffer;
        final int[] icLatin1 = NonBlockingJsonParser._icLatin1;
        int inputPtr = /*EL:1785*/this._inputPtr;
        final int a1 = /*EL:1787*/inputBuffer[inputPtr++] & 0xFF;
        /*SL:1788*/if (icLatin1[a1] == 0) {
            int v0 = /*EL:1789*/inputBuffer[inputPtr++] & 0xFF;
            /*SL:1790*/if (icLatin1[v0] == 0) {
                int v = /*EL:1791*/a1 << 8 | v0;
                /*SL:1792*/v0 = (inputBuffer[inputPtr++] & 0xFF);
                /*SL:1793*/if (icLatin1[v0] == 0) {
                    /*SL:1794*/v = (v << 8 | v0);
                    /*SL:1795*/v0 = (inputBuffer[inputPtr++] & 0xFF);
                    /*SL:1796*/if (icLatin1[v0] == 0) {
                        /*SL:1797*/v = (v << 8 | v0);
                        /*SL:1798*/v0 = (inputBuffer[inputPtr++] & 0xFF);
                        /*SL:1799*/if (icLatin1[v0] == 0) {
                            /*SL:1800*/this._quad1 = v;
                            /*SL:1801*/return this._parseMediumName(inputPtr, v0);
                        }
                        /*SL:1803*/if (v0 == 34) {
                            /*SL:1804*/this._inputPtr = inputPtr;
                            /*SL:1805*/return this._findName(v, 4);
                        }
                        /*SL:1807*/return null;
                    }
                    else {
                        /*SL:1809*/if (v0 == 34) {
                            /*SL:1810*/this._inputPtr = inputPtr;
                            /*SL:1811*/return this._findName(v, 3);
                        }
                        /*SL:1813*/return null;
                    }
                }
                else {
                    /*SL:1815*/if (v0 == 34) {
                        /*SL:1816*/this._inputPtr = inputPtr;
                        /*SL:1817*/return this._findName(v, 2);
                    }
                    /*SL:1819*/return null;
                }
            }
            else {
                /*SL:1821*/if (v0 == 34) {
                    /*SL:1822*/this._inputPtr = inputPtr;
                    /*SL:1823*/return this._findName(a1, 1);
                }
                /*SL:1825*/return null;
            }
        }
        else {
            /*SL:1827*/if (a1 == 34) {
                /*SL:1828*/this._inputPtr = inputPtr;
                /*SL:1829*/return "";
            }
            /*SL:1831*/return null;
        }
    }
    
    private final String _parseMediumName(int a1, int a2) throws IOException {
        final byte[] v1 = /*EL:1836*/this._inputBuffer;
        final int[] v2 = NonBlockingJsonParser._icLatin1;
        int v3 = /*EL:1840*/v1[a1++] & 0xFF;
        /*SL:1841*/if (v2[v3] == 0) {
            /*SL:1842*/a2 = (a2 << 8 | v3);
            /*SL:1843*/v3 = (v1[a1++] & 0xFF);
            /*SL:1844*/if (v2[v3] == 0) {
                /*SL:1845*/a2 = (a2 << 8 | v3);
                /*SL:1846*/v3 = (v1[a1++] & 0xFF);
                /*SL:1847*/if (v2[v3] == 0) {
                    /*SL:1848*/a2 = (a2 << 8 | v3);
                    /*SL:1849*/v3 = (v1[a1++] & 0xFF);
                    /*SL:1850*/if (v2[v3] == 0) {
                        /*SL:1851*/return this._parseMediumName2(a1, v3, a2);
                    }
                    /*SL:1853*/if (v3 == 34) {
                        /*SL:1854*/this._inputPtr = a1;
                        /*SL:1855*/return this._findName(this._quad1, a2, 4);
                    }
                    /*SL:1857*/return null;
                }
                else {
                    /*SL:1859*/if (v3 == 34) {
                        /*SL:1860*/this._inputPtr = a1;
                        /*SL:1861*/return this._findName(this._quad1, a2, 3);
                    }
                    /*SL:1863*/return null;
                }
            }
            else {
                /*SL:1865*/if (v3 == 34) {
                    /*SL:1866*/this._inputPtr = a1;
                    /*SL:1867*/return this._findName(this._quad1, a2, 2);
                }
                /*SL:1869*/return null;
            }
        }
        else {
            /*SL:1871*/if (v3 == 34) {
                /*SL:1872*/this._inputPtr = a1;
                /*SL:1873*/return this._findName(this._quad1, a2, 1);
            }
            /*SL:1875*/return null;
        }
    }
    
    private final String _parseMediumName2(int a1, int a2, final int a3) throws IOException {
        final byte[] v1 = /*EL:1880*/this._inputBuffer;
        final int[] v2 = NonBlockingJsonParser._icLatin1;
        int v3 = /*EL:1884*/v1[a1++] & 0xFF;
        /*SL:1885*/if (v2[v3] != 0) {
            /*SL:1886*/if (v3 == 34) {
                /*SL:1887*/this._inputPtr = a1;
                /*SL:1888*/return this._findName(this._quad1, a3, a2, 1);
            }
            /*SL:1890*/return null;
        }
        else {
            /*SL:1892*/a2 = (a2 << 8 | v3);
            /*SL:1893*/v3 = (v1[a1++] & 0xFF);
            /*SL:1894*/if (v2[v3] != 0) {
                /*SL:1895*/if (v3 == 34) {
                    /*SL:1896*/this._inputPtr = a1;
                    /*SL:1897*/return this._findName(this._quad1, a3, a2, 2);
                }
                /*SL:1899*/return null;
            }
            else {
                /*SL:1901*/a2 = (a2 << 8 | v3);
                /*SL:1902*/v3 = (v1[a1++] & 0xFF);
                /*SL:1903*/if (v2[v3] != 0) {
                    /*SL:1904*/if (v3 == 34) {
                        /*SL:1905*/this._inputPtr = a1;
                        /*SL:1906*/return this._findName(this._quad1, a3, a2, 3);
                    }
                    /*SL:1908*/return null;
                }
                else {
                    /*SL:1910*/a2 = (a2 << 8 | v3);
                    /*SL:1911*/v3 = (v1[a1++] & 0xFF);
                    /*SL:1912*/if (v3 == 34) {
                        /*SL:1913*/this._inputPtr = a1;
                        /*SL:1914*/return this._findName(this._quad1, a3, a2, 4);
                    }
                    /*SL:1917*/return null;
                }
            }
        }
    }
    
    private final JsonToken _parseEscapedName(int a3, int v1, int v2) throws IOException {
        int[] v3 = /*EL:1933*/this._quadBuffer;
        final int[] v4 = NonBlockingJsonParser._icLatin1;
        /*SL:1937*/while (this._inputPtr < this._inputEnd) {
            int a4 = /*EL:1944*/this._inputBuffer[this._inputPtr++] & 0xFF;
            /*SL:1945*/if (v4[a4] == 0) {
                /*SL:1946*/if (v2 < 4) {
                    /*SL:1947*/++v2;
                    /*SL:1948*/v1 = (v1 << 8 | a4);
                }
                else {
                    /*SL:1951*/if (a3 >= v3.length) {
                        /*SL:1952*/v3 = (this._quadBuffer = ParserBase.growArrayBy(v3, v3.length));
                    }
                    /*SL:1954*/v3[a3++] = v1;
                    /*SL:1955*/v1 = a4;
                    /*SL:1956*/v2 = 1;
                }
            }
            else {
                /*SL:1961*/if (a4 == 34) {
                    /*SL:2023*/if (v2 > 0) {
                        /*SL:2024*/if (a3 >= v3.length) {
                            /*SL:2025*/v3 = (this._quadBuffer = ParserBase.growArrayBy(v3, v3.length));
                        }
                        /*SL:2027*/v3[a3++] = NonBlockingJsonParserBase._padLastQuad(v1, v2);
                    }
                    else/*SL:2028*/ if (a3 == 0) {
                        /*SL:2029*/return this._fieldComplete("");
                    }
                    String v5 = /*EL:2031*/this._symbols.findName(v3, a3);
                    /*SL:2032*/if (v5 == null) {
                        /*SL:2033*/v5 = this._addName(v3, a3, v2);
                    }
                    /*SL:2035*/return this._fieldComplete(v5);
                }
                if (a4 != 92) {
                    this._throwUnquotedSpace(a4, "name");
                }
                else {
                    a4 = this._decodeCharEscape();
                    if (a4 < 0) {
                        this._minorState = 8;
                        this._minorStateAfterSplit = 7;
                        this._quadLength = a3;
                        this._pending32 = v1;
                        this._pendingBytes = v2;
                        return this._currToken = JsonToken.NOT_AVAILABLE;
                    }
                }
                if (a3 >= v3.length) {
                    v3 = (this._quadBuffer = ParserBase.growArrayBy(v3, v3.length));
                }
                if (a4 > 127) {
                    if (v2 >= 4) {
                        v3[a3++] = v1;
                        v1 = 0;
                        v2 = 0;
                    }
                    if (a4 < 2048) {
                        v1 = (v1 << 8 | (0xC0 | a4 >> 6));
                        ++v2;
                    }
                    else {
                        v1 = (v1 << 8 | (0xE0 | a4 >> 12));
                        if (++v2 >= 4) {
                            v3[a3++] = v1;
                            v1 = 0;
                            v2 = 0;
                        }
                        v1 = (v1 << 8 | (0x80 | (a4 >> 6 & 0x3F)));
                        ++v2;
                    }
                    a4 = (0x80 | (a4 & 0x3F));
                }
                if (v2 < 4) {
                    ++v2;
                    v1 = (v1 << 8 | a4);
                }
                else {
                    v3[a3++] = v1;
                    v1 = a4;
                    v2 = 1;
                }
            }
        }
        this._quadLength = a3;
        this._pending32 = v1;
        this._pendingBytes = v2;
        this._minorState = 7;
        return this._currToken = JsonToken.NOT_AVAILABLE;
    }
    
    private JsonToken _handleOddName(final int v2) throws IOException {
        /*SL:2047*/switch (v2) {
            case 35: {
                /*SL:2051*/if (Feature.ALLOW_YAML_COMMENTS.enabledIn(this._features)) {
                    /*SL:2052*/return this._finishHashComment(4);
                }
                break;
            }
            case 47: {
                /*SL:2056*/return this._startSlashComment(4);
            }
            case 39: {
                /*SL:2058*/if (this.isEnabled(Feature.ALLOW_SINGLE_QUOTES)) {
                    /*SL:2059*/return this._finishAposName(0, 0, 0);
                }
                break;
            }
            case 93: {
                /*SL:2063*/return this._closeArrayScope();
            }
        }
        /*SL:2066*/if (!this.isEnabled(Feature.ALLOW_UNQUOTED_FIELD_NAMES)) {
            final char a1 = /*EL:2069*/(char)v2;
            /*SL:2070*/this._reportUnexpectedChar(a1, "was expecting double-quote to start field name");
        }
        final int[] v3 = /*EL:2074*/CharTypes.getInputCodeUtf8JsNames();
        /*SL:2076*/if (v3[v2] != 0) {
            /*SL:2077*/this._reportUnexpectedChar(v2, "was expecting either valid name character (for unquoted name) or double-quote (for quoted) to start field name");
        }
        /*SL:2080*/return this._finishUnquotedName(0, v2, 1);
    }
    
    private JsonToken _finishUnquotedName(int a3, int v1, int v2) throws IOException {
        int[] v3 = /*EL:2091*/this._quadBuffer;
        final int[] v4 = /*EL:2092*/CharTypes.getInputCodeUtf8JsNames();
        /*SL:2097*/while (this._inputPtr < this._inputEnd) {
            final int a4 = /*EL:2104*/this._inputBuffer[this._inputPtr] & 0xFF;
            /*SL:2105*/if (v4[a4] != 0) {
                /*SL:2123*/if (v2 > 0) {
                    /*SL:2124*/if (a3 >= v3.length) {
                        /*SL:2125*/v3 = (this._quadBuffer = ParserBase.growArrayBy(v3, v3.length));
                    }
                    /*SL:2127*/v3[a3++] = v1;
                }
                String v5 = /*EL:2129*/this._symbols.findName(v3, a3);
                /*SL:2130*/if (v5 == null) {
                    /*SL:2131*/v5 = this._addName(v3, a3, v2);
                }
                /*SL:2133*/return this._fieldComplete(v5);
            }
            ++this._inputPtr;
            if (v2 < 4) {
                ++v2;
                v1 = (v1 << 8 | a4);
            }
            else {
                if (a3 >= v3.length) {
                    v3 = (this._quadBuffer = ParserBase.growArrayBy(v3, v3.length));
                }
                v3[a3++] = v1;
                v1 = a4;
                v2 = 1;
            }
        }
        this._quadLength = a3;
        this._pending32 = v1;
        this._pendingBytes = v2;
        this._minorState = 10;
        return this._currToken = JsonToken.NOT_AVAILABLE;
    }
    
    private JsonToken _finishAposName(int a3, int v1, int v2) throws IOException {
        int[] v3 = /*EL:2139*/this._quadBuffer;
        final int[] v4 = NonBlockingJsonParser._icLatin1;
        /*SL:2143*/while (this._inputPtr < this._inputEnd) {
            int a4 = /*EL:2150*/this._inputBuffer[this._inputPtr++] & 0xFF;
            /*SL:2151*/if (a4 == 39) {
                /*SL:2218*/if (v2 > 0) {
                    /*SL:2219*/if (a3 >= v3.length) {
                        /*SL:2220*/v3 = (this._quadBuffer = ParserBase.growArrayBy(v3, v3.length));
                    }
                    /*SL:2222*/v3[a3++] = NonBlockingJsonParserBase._padLastQuad(v1, v2);
                }
                else/*SL:2223*/ if (a3 == 0) {
                    /*SL:2224*/return this._fieldComplete("");
                }
                String v5 = /*EL:2226*/this._symbols.findName(v3, a3);
                /*SL:2227*/if (v5 == null) {
                    /*SL:2228*/v5 = this._addName(v3, a3, v2);
                }
                /*SL:2230*/return this._fieldComplete(v5);
            }
            if (a4 != 34 && v4[a4] != 0) {
                if (a4 != 92) {
                    this._throwUnquotedSpace(a4, "name");
                }
                else {
                    a4 = this._decodeCharEscape();
                    if (a4 < 0) {
                        this._minorState = 8;
                        this._minorStateAfterSplit = 9;
                        this._quadLength = a3;
                        this._pending32 = v1;
                        this._pendingBytes = v2;
                        return this._currToken = JsonToken.NOT_AVAILABLE;
                    }
                }
                if (a4 > 127) {
                    if (v2 >= 4) {
                        if (a3 >= v3.length) {
                            v3 = (this._quadBuffer = ParserBase.growArrayBy(v3, v3.length));
                        }
                        v3[a3++] = v1;
                        v1 = 0;
                        v2 = 0;
                    }
                    if (a4 < 2048) {
                        v1 = (v1 << 8 | (0xC0 | a4 >> 6));
                        ++v2;
                    }
                    else {
                        v1 = (v1 << 8 | (0xE0 | a4 >> 12));
                        if (++v2 >= 4) {
                            if (a3 >= v3.length) {
                                v3 = (this._quadBuffer = ParserBase.growArrayBy(v3, v3.length));
                            }
                            v3[a3++] = v1;
                            v1 = 0;
                            v2 = 0;
                        }
                        v1 = (v1 << 8 | (0x80 | (a4 >> 6 & 0x3F)));
                        ++v2;
                    }
                    a4 = (0x80 | (a4 & 0x3F));
                }
            }
            if (v2 < 4) {
                ++v2;
                v1 = (v1 << 8 | a4);
            }
            else {
                if (a3 >= v3.length) {
                    v3 = (this._quadBuffer = ParserBase.growArrayBy(v3, v3.length));
                }
                v3[a3++] = v1;
                v1 = a4;
                v2 = 1;
            }
        }
        this._quadLength = a3;
        this._pending32 = v1;
        this._pendingBytes = v2;
        this._minorState = 9;
        return this._currToken = JsonToken.NOT_AVAILABLE;
    }
    
    protected final JsonToken _finishFieldWithEscape() throws IOException {
        int v1 = /*EL:2236*/this._decodeSplitEscaped(this._quoted32, this._quotedDigits);
        /*SL:2237*/if (v1 < 0) {
            /*SL:2238*/this._minorState = 8;
            /*SL:2239*/return JsonToken.NOT_AVAILABLE;
        }
        /*SL:2241*/if (this._quadLength >= this._quadBuffer.length) {
            /*SL:2242*/this._quadBuffer = ParserBase.growArrayBy(this._quadBuffer, 32);
        }
        int v2 = /*EL:2244*/this._pending32;
        int v3 = /*EL:2245*/this._pendingBytes;
        /*SL:2246*/if (v1 > 127) {
            /*SL:2248*/if (v3 >= 4) {
                /*SL:2249*/this._quadBuffer[this._quadLength++] = v2;
                /*SL:2250*/v2 = 0;
                /*SL:2251*/v3 = 0;
            }
            /*SL:2253*/if (v1 < 2048) {
                /*SL:2254*/v2 = (v2 << 8 | (0xC0 | v1 >> 6));
                /*SL:2255*/++v3;
            }
            else {
                /*SL:2258*/v2 = (v2 << 8 | (0xE0 | v1 >> 12));
                /*SL:2260*/if (++v3 >= 4) {
                    /*SL:2261*/this._quadBuffer[this._quadLength++] = v2;
                    /*SL:2262*/v2 = 0;
                    /*SL:2263*/v3 = 0;
                }
                /*SL:2265*/v2 = (v2 << 8 | (0x80 | (v1 >> 6 & 0x3F)));
                /*SL:2266*/++v3;
            }
            /*SL:2269*/v1 = (0x80 | (v1 & 0x3F));
        }
        /*SL:2271*/if (v3 < 4) {
            /*SL:2272*/++v3;
            /*SL:2273*/v2 = (v2 << 8 | v1);
        }
        else {
            /*SL:2275*/this._quadBuffer[this._quadLength++] = v2;
            /*SL:2276*/v2 = v1;
            /*SL:2277*/v3 = 1;
        }
        /*SL:2279*/if (this._minorStateAfterSplit == 9) {
            /*SL:2280*/return this._finishAposName(this._quadLength, v2, v3);
        }
        /*SL:2282*/return this._parseEscapedName(this._quadLength, v2, v3);
    }
    
    private int _decodeSplitEscaped(int v2, int v3) throws IOException {
        /*SL:2287*/if (this._inputPtr >= this._inputEnd) {
            /*SL:2288*/this._quoted32 = v2;
            /*SL:2289*/this._quotedDigits = v3;
            /*SL:2290*/return -1;
        }
        int v4 = /*EL:2292*/this._inputBuffer[this._inputPtr++];
        /*SL:2293*/if (v3 == -1) {
            /*SL:2294*/switch (v4) {
                case 98: {
                    /*SL:2297*/return 8;
                }
                case 116: {
                    /*SL:2299*/return 9;
                }
                case 110: {
                    /*SL:2301*/return 10;
                }
                case 102: {
                    /*SL:2303*/return 12;
                }
                case 114: {
                    /*SL:2305*/return 13;
                }
                case 34:
                case 47:
                case 92: {
                    /*SL:2311*/return v4;
                }
                case 117: {
                    /*SL:2324*/if (this._inputPtr >= this._inputEnd) {
                        /*SL:2325*/this._quotedDigits = 0;
                        /*SL:2326*/this._quoted32 = 0;
                        /*SL:2327*/return -1;
                    }
                    /*SL:2329*/v4 = this._inputBuffer[this._inputPtr++];
                    /*SL:2330*/v3 = 0;
                    break;
                }
                default: {
                    final char a1 = (char)v4;
                    return this._handleUnrecognizedCharacterEscape(a1);
                }
            }
        }
        /*SL:2332*/v4 &= 0xFF;
        while (true) {
            final int a2 = /*EL:2334*/CharTypes.charToHex(v4);
            /*SL:2335*/if (a2 < 0) {
                /*SL:2336*/this._reportUnexpectedChar(v4, "expected a hex-digit for character escape sequence");
            }
            /*SL:2338*/v2 = (v2 << 4 | a2);
            /*SL:2339*/if (++v3 == 4) {
                /*SL:2340*/return v2;
            }
            /*SL:2342*/if (this._inputPtr >= this._inputEnd) {
                /*SL:2343*/this._quotedDigits = v3;
                /*SL:2344*/this._quoted32 = v2;
                /*SL:2345*/return -1;
            }
            /*SL:2347*/v4 = (this._inputBuffer[this._inputPtr++] & 0xFF);
        }
    }
    
    protected JsonToken _startString() throws IOException {
        int i = /*EL:2359*/this._inputPtr;
        int n = /*EL:2360*/0;
        final char[] emptyAndGetCurrentSegment = /*EL:2361*/this._textBuffer.emptyAndGetCurrentSegment();
        final int[] icUTF8 = NonBlockingJsonParser._icUTF8;
        final int min = /*EL:2364*/Math.min(this._inputEnd, i + emptyAndGetCurrentSegment.length);
        final byte[] v0 = /*EL:2365*/this._inputBuffer;
        /*SL:2366*/while (i < min) {
            final int v = /*EL:2367*/v0[i] & 0xFF;
            /*SL:2368*/if (icUTF8[v] != 0) {
                /*SL:2369*/if (v == 34) {
                    /*SL:2370*/this._inputPtr = i + 1;
                    /*SL:2371*/this._textBuffer.setCurrentLength(n);
                    /*SL:2372*/return this._valueComplete(JsonToken.VALUE_STRING);
                }
                break;
            }
            else {
                /*SL:2376*/++i;
                /*SL:2377*/emptyAndGetCurrentSegment[n++] = (char)v;
            }
        }
        /*SL:2379*/this._textBuffer.setCurrentLength(n);
        /*SL:2380*/this._inputPtr = i;
        /*SL:2381*/return this._finishRegularString();
    }
    
    private final JsonToken _finishRegularString() throws IOException {
        int[] v2 = NonBlockingJsonParser._icUTF8;
        /*SL:2390*/v2 = this._inputBuffer;
        char[] v3 = /*EL:2392*/this._textBuffer.getBufferWithoutReset();
        int v4 = /*EL:2393*/this._textBuffer.getCurrentSegmentSize();
        int v5 = /*EL:2394*/this._inputPtr;
        final int v6 = /*EL:2395*/this._inputEnd - 5;
        /*SL:2402*/while (v5 < this._inputEnd) {
            /*SL:2408*/if (v4 >= v3.length) {
                /*SL:2409*/v3 = this._textBuffer.finishCurrentSegment();
                /*SL:2410*/v4 = 0;
            }
            final int v7 = /*EL:2412*/Math.min(this._inputEnd, v5 + (v3.length - v4));
            /*SL:2413*/while (v5 < v7) {
                int v8 = /*EL:2414*/v2[v5++] & 0xFF;
                /*SL:2415*/if (v2[v8] != 0) {
                    /*SL:2422*/if (v8 == 34) {
                        /*SL:2423*/this._inputPtr = v5;
                        /*SL:2424*/this._textBuffer.setCurrentLength(v4);
                        /*SL:2425*/return this._valueComplete(JsonToken.VALUE_STRING);
                    }
                    /*SL:2428*/if (v5 < v6) {
                        /*SL:2441*/switch (v2[v8]) {
                            case 1: {
                                /*SL:2443*/this._inputPtr = v5;
                                /*SL:2444*/v8 = this._decodeFastCharEscape();
                                /*SL:2445*/v5 = this._inputPtr;
                                /*SL:2446*/break;
                            }
                            case 2: {
                                /*SL:2448*/v8 = this._decodeUTF8_2(v8, this._inputBuffer[v5++]);
                                /*SL:2449*/break;
                            }
                            case 3: {
                                /*SL:2451*/v8 = this._decodeUTF8_3(v8, this._inputBuffer[v5++], this._inputBuffer[v5++]);
                                /*SL:2452*/break;
                            }
                            case 4: {
                                /*SL:2454*/v8 = this._decodeUTF8_4(v8, this._inputBuffer[v5++], this._inputBuffer[v5++], this._inputBuffer[v5++]);
                                /*SL:2457*/v3[v4++] = (char)(0xD800 | v8 >> 10);
                                /*SL:2458*/if (v4 >= v3.length) {
                                    /*SL:2459*/v3 = this._textBuffer.finishCurrentSegment();
                                    /*SL:2460*/v4 = 0;
                                }
                                /*SL:2462*/v8 = (0xDC00 | (v8 & 0x3FF));
                                /*SL:2464*/break;
                            }
                            default: {
                                /*SL:2466*/if (v8 < 32) {
                                    /*SL:2468*/this._throwUnquotedSpace(v8, "string value");
                                    break;
                                }
                                /*SL:2471*/this._reportInvalidChar(v8);
                                break;
                            }
                        }
                        /*SL:2475*/if (v4 >= v3.length) {
                            /*SL:2476*/v3 = this._textBuffer.finishCurrentSegment();
                            /*SL:2477*/v4 = 0;
                        }
                        /*SL:2480*/v3[v4++] = (char)v8;
                        break;
                    }
                    this._inputPtr = v5;
                    this._textBuffer.setCurrentLength(v4);
                    if (!this._decodeSplitMultiByte(v8, v2[v8], v5 < this._inputEnd)) {
                        this._minorStateAfterSplit = 40;
                        return this._currToken = JsonToken.NOT_AVAILABLE;
                    }
                    v3 = this._textBuffer.getBufferWithoutReset();
                    v4 = this._textBuffer.getCurrentSegmentSize();
                    v5 = this._inputPtr;
                    break;
                }
                else {
                    v3[v4++] = (char)v8;
                }
            }
        }
        this._inputPtr = v5;
        this._minorState = 40;
        this._textBuffer.setCurrentLength(v4);
        return this._currToken = JsonToken.NOT_AVAILABLE;
    }
    
    protected JsonToken _startAposString() throws IOException {
        int i = /*EL:2486*/this._inputPtr;
        int n = /*EL:2487*/0;
        final char[] emptyAndGetCurrentSegment = /*EL:2488*/this._textBuffer.emptyAndGetCurrentSegment();
        final int[] icUTF8 = NonBlockingJsonParser._icUTF8;
        final int min = /*EL:2491*/Math.min(this._inputEnd, i + emptyAndGetCurrentSegment.length);
        final byte[] v0 = /*EL:2492*/this._inputBuffer;
        /*SL:2493*/while (i < min) {
            final int v = /*EL:2494*/v0[i] & 0xFF;
            /*SL:2495*/if (v == 39) {
                /*SL:2496*/this._inputPtr = i + 1;
                /*SL:2497*/this._textBuffer.setCurrentLength(n);
                /*SL:2498*/return this._valueComplete(JsonToken.VALUE_STRING);
            }
            /*SL:2501*/if (icUTF8[v] != 0) {
                /*SL:2502*/break;
            }
            /*SL:2504*/++i;
            /*SL:2505*/emptyAndGetCurrentSegment[n++] = (char)v;
        }
        /*SL:2507*/this._textBuffer.setCurrentLength(n);
        /*SL:2508*/this._inputPtr = i;
        /*SL:2509*/return this._finishAposString();
    }
    
    private final JsonToken _finishAposString() throws IOException {
        int[] v2 = NonBlockingJsonParser._icUTF8;
        /*SL:2516*/v2 = this._inputBuffer;
        char[] v3 = /*EL:2518*/this._textBuffer.getBufferWithoutReset();
        int v4 = /*EL:2519*/this._textBuffer.getCurrentSegmentSize();
        int v5 = /*EL:2520*/this._inputPtr;
        final int v6 = /*EL:2521*/this._inputEnd - 5;
        /*SL:2527*/while (v5 < this._inputEnd) {
            /*SL:2533*/if (v4 >= v3.length) {
                /*SL:2534*/v3 = this._textBuffer.finishCurrentSegment();
                /*SL:2535*/v4 = 0;
            }
            final int v7 = /*EL:2537*/Math.min(this._inputEnd, v5 + (v3.length - v4));
            /*SL:2538*/while (v5 < v7) {
                int v8 = /*EL:2539*/v2[v5++] & 0xFF;
                /*SL:2540*/if (v2[v8] != 0 && v8 != 34) {
                    /*SL:2553*/if (v5 < v6) {
                        /*SL:2566*/switch (v2[v8]) {
                            case 1: {
                                /*SL:2568*/this._inputPtr = v5;
                                /*SL:2569*/v8 = this._decodeFastCharEscape();
                                /*SL:2570*/v5 = this._inputPtr;
                                /*SL:2571*/break;
                            }
                            case 2: {
                                /*SL:2573*/v8 = this._decodeUTF8_2(v8, this._inputBuffer[v5++]);
                                /*SL:2574*/break;
                            }
                            case 3: {
                                /*SL:2576*/v8 = this._decodeUTF8_3(v8, this._inputBuffer[v5++], this._inputBuffer[v5++]);
                                /*SL:2577*/break;
                            }
                            case 4: {
                                /*SL:2579*/v8 = this._decodeUTF8_4(v8, this._inputBuffer[v5++], this._inputBuffer[v5++], this._inputBuffer[v5++]);
                                /*SL:2582*/v3[v4++] = (char)(0xD800 | v8 >> 10);
                                /*SL:2583*/if (v4 >= v3.length) {
                                    /*SL:2584*/v3 = this._textBuffer.finishCurrentSegment();
                                    /*SL:2585*/v4 = 0;
                                }
                                /*SL:2587*/v8 = (0xDC00 | (v8 & 0x3FF));
                                /*SL:2589*/break;
                            }
                            default: {
                                /*SL:2591*/if (v8 < 32) {
                                    /*SL:2593*/this._throwUnquotedSpace(v8, "string value");
                                    break;
                                }
                                /*SL:2596*/this._reportInvalidChar(v8);
                                break;
                            }
                        }
                        /*SL:2600*/if (v4 >= v3.length) {
                            /*SL:2601*/v3 = this._textBuffer.finishCurrentSegment();
                            /*SL:2602*/v4 = 0;
                        }
                        /*SL:2605*/v3[v4++] = (char)v8;
                        break;
                    }
                    this._inputPtr = v5;
                    this._textBuffer.setCurrentLength(v4);
                    if (!this._decodeSplitMultiByte(v8, v2[v8], v5 < this._inputEnd)) {
                        this._minorStateAfterSplit = 45;
                        return this._currToken = JsonToken.NOT_AVAILABLE;
                    }
                    v3 = this._textBuffer.getBufferWithoutReset();
                    v4 = this._textBuffer.getCurrentSegmentSize();
                    v5 = this._inputPtr;
                    break;
                }
                else {
                    if (v8 == 39) {
                        this._inputPtr = v5;
                        this._textBuffer.setCurrentLength(v4);
                        return this._valueComplete(JsonToken.VALUE_STRING);
                    }
                    v3[v4++] = (char)v8;
                }
            }
        }
        this._inputPtr = v5;
        this._minorState = 45;
        this._textBuffer.setCurrentLength(v4);
        return this._currToken = JsonToken.NOT_AVAILABLE;
    }
    
    private final boolean _decodeSplitMultiByte(int a1, final int a2, final boolean a3) throws IOException {
        /*SL:2612*/switch (a2) {
            case 1: {
                /*SL:2614*/a1 = this._decodeSplitEscaped(0, -1);
                /*SL:2615*/if (a1 < 0) {
                    /*SL:2616*/this._minorState = 41;
                    /*SL:2617*/return false;
                }
                /*SL:2619*/this._textBuffer.append((char)a1);
                /*SL:2620*/return true;
            }
            case 2: {
                /*SL:2622*/if (a3) {
                    /*SL:2624*/a1 = this._decodeUTF8_2(a1, this._inputBuffer[this._inputPtr++]);
                    /*SL:2625*/this._textBuffer.append((char)a1);
                    /*SL:2626*/return true;
                }
                /*SL:2628*/this._minorState = 42;
                /*SL:2629*/this._pending32 = a1;
                /*SL:2630*/return false;
            }
            case 3: {
                /*SL:2632*/a1 &= 0xF;
                /*SL:2633*/if (a3) {
                    /*SL:2634*/return this._decodeSplitUTF8_3(a1, 1, this._inputBuffer[this._inputPtr++]);
                }
                /*SL:2636*/this._minorState = 43;
                /*SL:2637*/this._pending32 = a1;
                /*SL:2638*/this._pendingBytes = 1;
                /*SL:2639*/return false;
            }
            case 4: {
                /*SL:2641*/a1 &= 0x7;
                /*SL:2642*/if (a3) {
                    /*SL:2643*/return this._decodeSplitUTF8_4(a1, 1, this._inputBuffer[this._inputPtr++]);
                }
                /*SL:2645*/this._pending32 = a1;
                /*SL:2646*/this._pendingBytes = 1;
                /*SL:2647*/this._minorState = 44;
                /*SL:2648*/return false;
            }
            default: {
                /*SL:2650*/if (a1 < 32) {
                    /*SL:2652*/this._throwUnquotedSpace(a1, "string value");
                }
                else {
                    /*SL:2655*/this._reportInvalidChar(a1);
                }
                /*SL:2657*/this._textBuffer.append((char)a1);
                /*SL:2658*/return true;
            }
        }
    }
    
    private final boolean _decodeSplitUTF8_3(int a1, final int a2, int a3) throws IOException {
        /*SL:2665*/if (a2 == 1) {
            /*SL:2666*/if ((a3 & 0xC0) != 0x80) {
                /*SL:2667*/this._reportInvalidOther(a3 & 0xFF, this._inputPtr);
            }
            /*SL:2669*/a1 = (a1 << 6 | (a3 & 0x3F));
            /*SL:2670*/if (this._inputPtr >= this._inputEnd) {
                /*SL:2671*/this._minorState = 43;
                /*SL:2672*/this._pending32 = a1;
                /*SL:2673*/this._pendingBytes = 2;
                /*SL:2674*/return false;
            }
            /*SL:2676*/a3 = this._inputBuffer[this._inputPtr++];
        }
        /*SL:2678*/if ((a3 & 0xC0) != 0x80) {
            /*SL:2679*/this._reportInvalidOther(a3 & 0xFF, this._inputPtr);
        }
        /*SL:2681*/this._textBuffer.append((char)(a1 << 6 | (a3 & 0x3F)));
        /*SL:2682*/return true;
    }
    
    private final boolean _decodeSplitUTF8_4(int a1, int a2, int a3) throws IOException {
        /*SL:2690*/if (a2 == 1) {
            /*SL:2691*/if ((a3 & 0xC0) != 0x80) {
                /*SL:2692*/this._reportInvalidOther(a3 & 0xFF, this._inputPtr);
            }
            /*SL:2694*/a1 = (a1 << 6 | (a3 & 0x3F));
            /*SL:2695*/if (this._inputPtr >= this._inputEnd) {
                /*SL:2696*/this._minorState = 44;
                /*SL:2697*/this._pending32 = a1;
                /*SL:2698*/this._pendingBytes = 2;
                /*SL:2699*/return false;
            }
            /*SL:2701*/a2 = 2;
            /*SL:2702*/a3 = this._inputBuffer[this._inputPtr++];
        }
        /*SL:2704*/if (a2 == 2) {
            /*SL:2705*/if ((a3 & 0xC0) != 0x80) {
                /*SL:2706*/this._reportInvalidOther(a3 & 0xFF, this._inputPtr);
            }
            /*SL:2708*/a1 = (a1 << 6 | (a3 & 0x3F));
            /*SL:2709*/if (this._inputPtr >= this._inputEnd) {
                /*SL:2710*/this._minorState = 44;
                /*SL:2711*/this._pending32 = a1;
                /*SL:2712*/this._pendingBytes = 3;
                /*SL:2713*/return false;
            }
            /*SL:2715*/a3 = this._inputBuffer[this._inputPtr++];
        }
        /*SL:2717*/if ((a3 & 0xC0) != 0x80) {
            /*SL:2718*/this._reportInvalidOther(a3 & 0xFF, this._inputPtr);
        }
        int v1 = /*EL:2720*/(a1 << 6 | (a3 & 0x3F)) - 65536;
        /*SL:2722*/this._textBuffer.append((char)(0xD800 | v1 >> 10));
        /*SL:2723*/v1 = (0xDC00 | (v1 & 0x3FF));
        /*SL:2725*/this._textBuffer.append((char)v1);
        /*SL:2726*/return true;
    }
    
    private final int _decodeCharEscape() throws IOException {
        final int v1 = /*EL:2737*/this._inputEnd - this._inputPtr;
        /*SL:2738*/if (v1 < 5) {
            /*SL:2739*/return this._decodeSplitEscaped(0, -1);
        }
        /*SL:2741*/return this._decodeFastCharEscape();
    }
    
    private final int _decodeFastCharEscape() throws IOException {
        final int v0 = /*EL:2746*/this._inputBuffer[this._inputPtr++];
        /*SL:2747*/switch (v0) {
            case 98: {
                /*SL:2750*/return 8;
            }
            case 116: {
                /*SL:2752*/return 9;
            }
            case 110: {
                /*SL:2754*/return 10;
            }
            case 102: {
                /*SL:2756*/return 12;
            }
            case 114: {
                /*SL:2758*/return 13;
            }
            case 34:
            case 47:
            case 92: {
                /*SL:2764*/return (char)v0;
            }
            case 117: {
                int v = /*EL:2778*/this._inputBuffer[this._inputPtr++];
                int v3;
                int v2 = /*EL:2780*/v3 = CharTypes.charToHex(v);
                /*SL:2782*/if (v2 >= 0) {
                    /*SL:2783*/v = this._inputBuffer[this._inputPtr++];
                    /*SL:2784*/v2 = CharTypes.charToHex(v);
                    /*SL:2785*/if (v2 >= 0) {
                        /*SL:2786*/v3 = (v3 << 4 | v2);
                        /*SL:2787*/v = this._inputBuffer[this._inputPtr++];
                        /*SL:2788*/v2 = CharTypes.charToHex(v);
                        /*SL:2789*/if (v2 >= 0) {
                            /*SL:2790*/v3 = (v3 << 4 | v2);
                            /*SL:2791*/v = this._inputBuffer[this._inputPtr++];
                            /*SL:2792*/v2 = CharTypes.charToHex(v);
                            /*SL:2793*/if (v2 >= 0) {
                                /*SL:2794*/return v3 << 4 | v2;
                            }
                        }
                    }
                }
                /*SL:2799*/this._reportUnexpectedChar(v & 0xFF, "expected a hex-digit for character escape sequence");
                /*SL:2800*/return -1;
            }
            default: {
                final char v4 = (char)v0;
                return this._handleUnrecognizedCharacterEscape(v4);
            }
        }
    }
    
    private final int _decodeUTF8_2(final int a1, final int a2) throws IOException {
        /*SL:2811*/if ((a2 & 0xC0) != 0x80) {
            /*SL:2812*/this._reportInvalidOther(a2 & 0xFF, this._inputPtr);
        }
        /*SL:2814*/return (a1 & 0x1F) << 6 | (a2 & 0x3F);
    }
    
    private final int _decodeUTF8_3(int a1, final int a2, final int a3) throws IOException {
        /*SL:2819*/a1 &= 0xF;
        /*SL:2820*/if ((a2 & 0xC0) != 0x80) {
            /*SL:2821*/this._reportInvalidOther(a2 & 0xFF, this._inputPtr);
        }
        /*SL:2823*/a1 = (a1 << 6 | (a2 & 0x3F));
        /*SL:2824*/if ((a3 & 0xC0) != 0x80) {
            /*SL:2825*/this._reportInvalidOther(a3 & 0xFF, this._inputPtr);
        }
        /*SL:2827*/return a1 << 6 | (a3 & 0x3F);
    }
    
    private final int _decodeUTF8_4(int a1, final int a2, final int a3, final int a4) throws IOException {
        /*SL:2834*/if ((a2 & 0xC0) != 0x80) {
            /*SL:2835*/this._reportInvalidOther(a2 & 0xFF, this._inputPtr);
        }
        /*SL:2837*/a1 = ((a1 & 0x7) << 6 | (a2 & 0x3F));
        /*SL:2838*/if ((a3 & 0xC0) != 0x80) {
            /*SL:2839*/this._reportInvalidOther(a3 & 0xFF, this._inputPtr);
        }
        /*SL:2841*/a1 = (a1 << 6 | (a3 & 0x3F));
        /*SL:2842*/if ((a4 & 0xC0) != 0x80) {
            /*SL:2843*/this._reportInvalidOther(a4 & 0xFF, this._inputPtr);
        }
        /*SL:2845*/return (a1 << 6 | (a4 & 0x3F)) - 65536;
    }
    
    static {
        _icUTF8 = CharTypes.getInputCodeUtf8();
        _icLatin1 = CharTypes.getInputCodeLatin1();
    }
}

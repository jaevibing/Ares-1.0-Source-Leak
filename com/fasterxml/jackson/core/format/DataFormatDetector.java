package com.fasterxml.jackson.core.format;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import com.fasterxml.jackson.core.JsonFactory;

public class DataFormatDetector
{
    public static final int DEFAULT_MAX_INPUT_LOOKAHEAD = 64;
    protected final JsonFactory[] _detectors;
    protected final MatchStrength _optimalMatch;
    protected final MatchStrength _minimalMatch;
    protected final int _maxInputLookahead;
    
    public DataFormatDetector(final JsonFactory... a1) {
        this(a1, MatchStrength.SOLID_MATCH, MatchStrength.WEAK_MATCH, 64);
    }
    
    public DataFormatDetector(final Collection<JsonFactory> a1) {
        this((JsonFactory[])a1.<JsonFactory>toArray(new JsonFactory[a1.size()]));
    }
    
    public DataFormatDetector withOptimalMatch(final MatchStrength a1) {
        /*SL:72*/if (a1 == this._optimalMatch) {
            /*SL:73*/return this;
        }
        /*SL:75*/return new DataFormatDetector(this._detectors, a1, this._minimalMatch, this._maxInputLookahead);
    }
    
    public DataFormatDetector withMinimalMatch(final MatchStrength a1) {
        /*SL:83*/if (a1 == this._minimalMatch) {
            /*SL:84*/return this;
        }
        /*SL:86*/return new DataFormatDetector(this._detectors, this._optimalMatch, a1, this._maxInputLookahead);
    }
    
    public DataFormatDetector withMaxInputLookahead(final int a1) {
        /*SL:94*/if (a1 == this._maxInputLookahead) {
            /*SL:95*/return this;
        }
        /*SL:97*/return new DataFormatDetector(this._detectors, this._optimalMatch, this._minimalMatch, a1);
    }
    
    private DataFormatDetector(final JsonFactory[] a1, final MatchStrength a2, final MatchStrength a3, final int a4) {
        this._detectors = a1;
        this._optimalMatch = a2;
        this._minimalMatch = a3;
        this._maxInputLookahead = a4;
    }
    
    public DataFormatMatcher findFormat(final InputStream a1) throws IOException {
        /*SL:123*/return this._findFormat(new InputAccessor.Std(a1, new byte[this._maxInputLookahead]));
    }
    
    public DataFormatMatcher findFormat(final byte[] a1) throws IOException {
        /*SL:134*/return this._findFormat(new InputAccessor.Std(a1));
    }
    
    public DataFormatMatcher findFormat(final byte[] a1, final int a2, final int a3) throws IOException {
        /*SL:147*/return this._findFormat(new InputAccessor.Std(a1, a2, a3));
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = /*EL:157*/new StringBuilder();
        /*SL:158*/sb.append('[');
        final int v0 = /*EL:159*/this._detectors.length;
        /*SL:160*/if (v0 > 0) {
            /*SL:161*/sb.append(this._detectors[0].getFormatName());
            /*SL:162*/for (int v = 1; v < v0; ++v) {
                /*SL:163*/sb.append(", ");
                /*SL:164*/sb.append(this._detectors[v].getFormatName());
            }
        }
        /*SL:167*/sb.append(']');
        /*SL:168*/return sb.toString();
    }
    
    private DataFormatMatcher _findFormat(final InputAccessor.Std v-5) throws IOException {
        JsonFactory a2 = /*EL:178*/null;
        MatchStrength a3 = /*EL:179*/null;
        /*SL:180*/for (final JsonFactory v : this._detectors) {
            /*SL:181*/v-5.reset();
            final MatchStrength a1 = /*EL:182*/v.hasFormat(v-5);
            /*SL:184*/if (a1 != null) {
                if (a1.ordinal() >= this._minimalMatch.ordinal()) {
                    /*SL:188*/if (a2 == null || /*EL:189*/a3.ordinal() < a1.ordinal()) {
                        /*SL:194*/a2 = v;
                        /*SL:195*/a3 = a1;
                        /*SL:196*/if (a1.ordinal() >= this._optimalMatch.ordinal()) {
                            /*SL:197*/break;
                        }
                    }
                }
            }
        }
        /*SL:200*/return v-5.createMatcher(a2, a3);
    }
}

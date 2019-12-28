package com.fasterxml.jackson.core.format;

import com.fasterxml.jackson.core.io.IOContext;
import com.fasterxml.jackson.core.io.MergedStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonFactory;
import java.io.InputStream;

public class DataFormatMatcher
{
    protected final InputStream _originalStream;
    protected final byte[] _bufferedData;
    protected final int _bufferedStart;
    protected final int _bufferedLength;
    protected final JsonFactory _match;
    protected final MatchStrength _matchStrength;
    
    protected DataFormatMatcher(final InputStream a1, final byte[] a2, final int a3, final int a4, final JsonFactory a5, final MatchStrength a6) {
        this._originalStream = a1;
        this._bufferedData = a2;
        this._bufferedStart = a3;
        this._bufferedLength = a4;
        this._match = a5;
        this._matchStrength = a6;
        if ((a3 | a4) < 0 || a3 + a4 > a2.length) {
            throw new IllegalArgumentException(String.format("Illegal start/length (%d/%d) wrt input array of %d bytes", a3, a4, a2.length));
        }
    }
    
    public boolean hasMatch() {
        /*SL:71*/return this._match != null;
    }
    
    public MatchStrength getMatchStrength() {
        /*SL:78*/return (this._matchStrength == null) ? MatchStrength.INCONCLUSIVE : this._matchStrength;
    }
    
    public JsonFactory getMatch() {
        /*SL:84*/return this._match;
    }
    
    public String getMatchedFormatName() {
        /*SL:94*/return this._match.getFormatName();
    }
    
    public JsonParser createParserWithMatch() throws IOException {
        /*SL:109*/if (this._match == null) {
            /*SL:110*/return null;
        }
        /*SL:112*/if (this._originalStream == null) {
            /*SL:113*/return this._match.createParser(this._bufferedData, this._bufferedStart, this._bufferedLength);
        }
        /*SL:115*/return this._match.createParser(this.getDataStream());
    }
    
    public InputStream getDataStream() {
        /*SL:126*/if (this._originalStream == null) {
            /*SL:127*/return new ByteArrayInputStream(this._bufferedData, this._bufferedStart, this._bufferedLength);
        }
        /*SL:129*/return new MergedStream(null, this._originalStream, this._bufferedData, this._bufferedStart, this._bufferedLength);
    }
}

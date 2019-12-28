package com.fasterxml.jackson.core.util;

import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import com.fasterxml.jackson.core.JsonParser;

public class JsonParserSequence extends JsonParserDelegate
{
    protected final JsonParser[] _parsers;
    protected final boolean _checkForExistingToken;
    protected int _nextParserIndex;
    protected boolean _hasToken;
    
    protected JsonParserSequence(final JsonParser[] a1) {
        this(false, a1);
    }
    
    protected JsonParserSequence(final boolean a1, final JsonParser[] a2) {
        super(a2[0]);
        this._checkForExistingToken = a1;
        this._hasToken = (a1 && this.delegate.hasCurrentToken());
        this._parsers = a2;
        this._nextParserIndex = 1;
    }
    
    public static JsonParserSequence createFlattened(final boolean a1, final JsonParser a2, final JsonParser a3) {
        /*SL:87*/if (!(a2 instanceof JsonParserSequence) && !(a3 instanceof JsonParserSequence)) {
            /*SL:88*/return new JsonParserSequence(a1, new JsonParser[] { a2, a3 });
        }
        final ArrayList<JsonParser> v1 = /*EL:91*/new ArrayList<JsonParser>();
        /*SL:92*/if (a2 instanceof JsonParserSequence) {
            /*SL:93*/((JsonParserSequence)a2).addFlattenedActiveParsers(v1);
        }
        else {
            /*SL:95*/v1.add(a2);
        }
        /*SL:97*/if (a3 instanceof JsonParserSequence) {
            /*SL:98*/((JsonParserSequence)a3).addFlattenedActiveParsers(v1);
        }
        else {
            /*SL:100*/v1.add(a3);
        }
        /*SL:102*/return new JsonParserSequence(a1, v1.<JsonParser>toArray(new JsonParser[v1.size()]));
    }
    
    @Deprecated
    public static JsonParserSequence createFlattened(final JsonParser a1, final JsonParser a2) {
        /*SL:112*/return createFlattened(false, a1, a2);
    }
    
    protected void addFlattenedActiveParsers(final List<JsonParser> v0) {
        /*SL:118*/for (int v = this._nextParserIndex - 1, v2 = this._parsers.length; v < v2; ++v) {
            final JsonParser a1 = /*EL:119*/this._parsers[v];
            /*SL:120*/if (a1 instanceof JsonParserSequence) {
                /*SL:121*/((JsonParserSequence)a1).addFlattenedActiveParsers(v0);
            }
            else {
                /*SL:123*/v0.add(a1);
            }
        }
    }
    
    @Override
    public void close() throws IOException {
        /*SL:137*/do {
            this.delegate.close();
        } while (this.switchToNext());
    }
    
    @Override
    public JsonToken nextToken() throws IOException {
        /*SL:143*/if (this.delegate == null) {
            /*SL:144*/return null;
        }
        /*SL:146*/if (this._hasToken) {
            /*SL:147*/this._hasToken = false;
            /*SL:148*/return this.delegate.currentToken();
        }
        final JsonToken v1 = /*EL:150*/this.delegate.nextToken();
        /*SL:151*/if (v1 == null) {
            /*SL:152*/return this.switchAndReturnNext();
        }
        /*SL:154*/return v1;
    }
    
    @Override
    public JsonParser skipChildren() throws IOException {
        /*SL:165*/if (this.delegate.currentToken() != JsonToken.START_OBJECT && this.delegate.currentToken() != JsonToken.START_ARRAY) {
            /*SL:167*/return this;
        }
        int v0 = /*EL:169*/1;
        while (true) {
            final JsonToken v = /*EL:174*/this.nextToken();
            /*SL:175*/if (v == null) {
                /*SL:176*/return this;
            }
            /*SL:178*/if (v.isStructStart()) {
                /*SL:179*/++v0;
            }
            else {
                /*SL:180*/if (v.isStructEnd() && /*EL:181*/--v0 == 0) {
                    /*SL:182*/return this;
                }
                continue;
            }
        }
    }
    
    public int containedParsersCount() {
        /*SL:200*/return this._parsers.length;
    }
    
    protected boolean switchToNext() {
        /*SL:220*/if (this._nextParserIndex < this._parsers.length) {
            /*SL:221*/this.delegate = this._parsers[this._nextParserIndex++];
            /*SL:222*/return true;
        }
        /*SL:224*/return false;
    }
    
    protected JsonToken switchAndReturnNext() throws IOException {
        /*SL:229*/while (this._nextParserIndex < this._parsers.length) {
            /*SL:230*/this.delegate = this._parsers[this._nextParserIndex++];
            /*SL:231*/if (this._checkForExistingToken && this.delegate.hasCurrentToken()) {
                /*SL:232*/return this.delegate.getCurrentToken();
            }
            final JsonToken v1 = /*EL:234*/this.delegate.nextToken();
            /*SL:235*/if (v1 != null) {
                /*SL:236*/return v1;
            }
        }
        /*SL:239*/return null;
    }
}

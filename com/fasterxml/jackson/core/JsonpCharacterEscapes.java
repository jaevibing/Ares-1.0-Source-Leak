package com.fasterxml.jackson.core;

import com.fasterxml.jackson.core.io.SerializedString;
import com.fasterxml.jackson.core.io.CharacterEscapes;

public class JsonpCharacterEscapes extends CharacterEscapes
{
    private static final long serialVersionUID = 1L;
    private static final int[] asciiEscapes;
    private static final SerializedString escapeFor2028;
    private static final SerializedString escapeFor2029;
    private static final JsonpCharacterEscapes sInstance;
    
    public static JsonpCharacterEscapes instance() {
        /*SL:26*/return JsonpCharacterEscapes.sInstance;
    }
    
    @Override
    public SerializableString getEscapeSequence(final int a1) {
        /*SL:32*/switch (a1) {
            case 8232: {
                /*SL:34*/return JsonpCharacterEscapes.escapeFor2028;
            }
            case 8233: {
                /*SL:36*/return JsonpCharacterEscapes.escapeFor2029;
            }
            default: {
                /*SL:38*/return null;
            }
        }
    }
    
    @Override
    public int[] getEscapeCodesForAscii() {
        /*SL:44*/return JsonpCharacterEscapes.asciiEscapes;
    }
    
    static {
        asciiEscapes = CharacterEscapes.standardAsciiEscapesForJSON();
        escapeFor2028 = new SerializedString("\\u2028");
        escapeFor2029 = new SerializedString("\\u2029");
        sInstance = new JsonpCharacterEscapes();
    }
}

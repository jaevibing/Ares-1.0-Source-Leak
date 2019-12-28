package org.json;

import java.io.IOException;
import java.io.StringReader;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.io.BufferedReader;
import java.io.Reader;

public class JSONTokener
{
    private long character;
    private boolean eof;
    private long index;
    private long line;
    private char previous;
    private final Reader reader;
    private boolean usePrevious;
    private long characterPreviousLine;
    
    public JSONTokener(final Reader a1) {
        this.reader = (a1.markSupported() ? a1 : new BufferedReader(a1));
        this.eof = false;
        this.usePrevious = false;
        this.previous = '\0';
        this.index = 0L;
        this.character = 1L;
        this.characterPreviousLine = 0L;
        this.line = 1L;
    }
    
    public JSONTokener(final InputStream a1) {
        this(new InputStreamReader(a1));
    }
    
    public JSONTokener(final String a1) {
        this(new StringReader(a1));
    }
    
    public void back() throws JSONException {
        /*SL:106*/if (this.usePrevious || this.index <= 0L) {
            /*SL:107*/throw new JSONException("Stepping back two steps is not supported");
        }
        /*SL:109*/this.decrementIndexes();
        /*SL:110*/this.usePrevious = true;
        /*SL:111*/this.eof = false;
    }
    
    private void decrementIndexes() {
        /*SL:118*/--this.index;
        /*SL:119*/if (this.previous == '\r' || this.previous == '\n') {
            /*SL:120*/--this.line;
            /*SL:121*/this.character = this.characterPreviousLine;
        }
        else/*SL:122*/ if (this.character > 0L) {
            /*SL:123*/--this.character;
        }
    }
    
    public static int dehexchar(final char a1) {
        /*SL:134*/if (a1 >= '0' && a1 <= '9') {
            /*SL:135*/return a1 - '0';
        }
        /*SL:137*/if (a1 >= 'A' && a1 <= 'F') {
            /*SL:138*/return a1 - '7';
        }
        /*SL:140*/if (a1 >= 'a' && a1 <= 'f') {
            /*SL:141*/return a1 - 'W';
        }
        /*SL:143*/return -1;
    }
    
    public boolean end() {
        /*SL:152*/return this.eof && !this.usePrevious;
    }
    
    public boolean more() throws JSONException {
        /*SL:164*/if (this.usePrevious) {
            /*SL:165*/return true;
        }
        try {
            /*SL:168*/this.reader.mark(1);
        }
        catch (IOException v1) {
            /*SL:170*/throw new JSONException("Unable to preserve stream position", v1);
        }
        try {
            /*SL:174*/if (this.reader.read() <= 0) {
                /*SL:175*/this.eof = true;
                /*SL:176*/return false;
            }
            /*SL:178*/this.reader.reset();
        }
        catch (IOException v1) {
            /*SL:180*/throw new JSONException("Unable to read the next character from the stream", v1);
        }
        /*SL:182*/return true;
    }
    
    public char next() throws JSONException {
        int v1;
        /*SL:194*/if (this.usePrevious) {
            /*SL:195*/this.usePrevious = false;
            /*SL:196*/v1 = this.previous;
        }
        else {
            try {
                /*SL:199*/v1 = this.reader.read();
            }
            catch (IOException v2) {
                /*SL:201*/throw new JSONException(v2);
            }
        }
        /*SL:204*/if (v1 <= 0) {
            /*SL:205*/this.eof = true;
            /*SL:206*/return '\0';
        }
        /*SL:208*/this.incrementIndexes(v1);
        /*SL:210*/return this.previous = (char)v1;
    }
    
    private void incrementIndexes(final int a1) {
        /*SL:219*/if (a1 > 0) {
            /*SL:220*/++this.index;
            /*SL:221*/if (a1 == 13) {
                /*SL:222*/++this.line;
                /*SL:223*/this.characterPreviousLine = this.character;
                /*SL:224*/this.character = 0L;
            }
            else/*SL:225*/ if (a1 == 10) {
                /*SL:226*/if (this.previous != '\r') {
                    /*SL:227*/++this.line;
                    /*SL:228*/this.characterPreviousLine = this.character;
                }
                /*SL:230*/this.character = 0L;
            }
            else {
                /*SL:232*/++this.character;
            }
        }
    }
    
    public char next(final char a1) throws JSONException {
        final char v1 = /*EL:245*/this.next();
        /*SL:246*/if (v1 == a1) {
            /*SL:253*/return v1;
        }
        if (v1 > '\0') {
            throw this.syntaxError("Expected '" + a1 + "' and instead saw '" + v1 + "'");
        }
        throw this.syntaxError("Expected '" + a1 + "' and instead saw ''");
    }
    
    public String next(final int a1) throws JSONException {
        /*SL:267*/if (a1 == 0) {
            /*SL:268*/return "";
        }
        final char[] v1 = /*EL:271*/new char[a1];
        /*SL:274*/for (int v2 = 0; v2 < a1; /*SL:279*/++v2) {
            v1[v2] = this.next();
            if (this.end()) {
                throw this.syntaxError("Substring bounds error");
            }
        }
        /*SL:281*/return new String(v1);
    }
    
    public char nextClean() throws JSONException {
        char v1;
        do {
            /*SL:292*/v1 = this.next();
        } while (/*EL:293*/v1 != '\0' && v1 <= ' ');
        /*SL:294*/return v1;
    }
    
    public String nextString(final char v2) throws JSONException {
        final StringBuilder v3 = /*EL:313*/new StringBuilder();
        while (true) {
            char v4 = /*EL:315*/this.next();
            /*SL:316*/switch (v4) {
                case '\0':
                case '\n':
                case '\r': {
                    /*SL:320*/throw this.syntaxError("Unterminated string");
                }
                case '\\': {
                    /*SL:322*/v4 = this.next();
                    /*SL:323*/switch (v4) {
                        case 'b': {
                            /*SL:325*/v3.append('\b');
                            /*SL:326*/continue;
                        }
                        case 't': {
                            /*SL:328*/v3.append('\t');
                            /*SL:329*/continue;
                        }
                        case 'n': {
                            /*SL:331*/v3.append('\n');
                            /*SL:332*/continue;
                        }
                        case 'f': {
                            /*SL:334*/v3.append('\f');
                            /*SL:335*/continue;
                        }
                        case 'r': {
                            /*SL:337*/v3.append('\r');
                            /*SL:338*/continue;
                        }
                        case 'u': {
                            try {
                                /*SL:341*/v3.append((char)Integer.parseInt(this.next(4), 16));
                                /*SL:344*/continue;
                            }
                            catch (NumberFormatException a1) {
                                throw this.syntaxError("Illegal escape.", a1);
                            }
                        }
                        case '\"':
                        case '\'':
                        case '/':
                        case '\\': {
                            /*SL:350*/v3.append(v4);
                            /*SL:351*/continue;
                        }
                        default: {
                            /*SL:353*/throw this.syntaxError("Illegal escape.");
                        }
                    }
                    break;
                }
                default: {
                    /*SL:357*/if (v4 == v2) {
                        /*SL:358*/return v3.toString();
                    }
                    /*SL:360*/v3.append(v4);
                    continue;
                }
            }
        }
    }
    
    public String nextTo(final char v2) throws JSONException {
        final StringBuilder v3 = /*EL:375*/new StringBuilder();
        char a1;
        while (true) {
            /*SL:377*/a1 = this.next();
            /*SL:378*/if (a1 == v2 || a1 == '\0' || a1 == '\n' || a1 == '\r') {
                break;
            }
            /*SL:384*/v3.append(a1);
        }
        if (a1 != '\0') {
            this.back();
        }
        return v3.toString().trim();
    }
    
    public String nextTo(final String a1) throws JSONException {
        StringBuilder v2 = /*EL:399*/new StringBuilder();
        while (true) {
            /*SL:401*/v2 = this.next();
            /*SL:402*/if (a1.indexOf(v2) >= 0 || v2 == '\0' || v2 == '\n' || v2 == '\r') {
                break;
            }
            /*SL:409*/v2.append(v2);
        }
        if (v2 != '\0') {
            this.back();
        }
        return v2.toString().trim();
    }
    
    public Object nextValue() throws JSONException {
        char v1 = /*EL:422*/this.nextClean();
        /*SL:425*/switch (v1) {
            case '\"':
            case '\'': {
                /*SL:428*/return this.nextString(v1);
            }
            case '{': {
                /*SL:430*/this.back();
                /*SL:431*/return new JSONObject(this);
            }
            case '[': {
                /*SL:433*/this.back();
                /*SL:434*/return new JSONArray(this);
            }
            default: {
                final StringBuilder v2 = /*EL:446*/new StringBuilder();
                /*SL:447*/while (v1 >= ' ' && ",:]}/\\\"[{;=#".indexOf(v1) < 0) {
                    /*SL:448*/v2.append(v1);
                    /*SL:449*/v1 = this.next();
                }
                /*SL:451*/if (!this.eof) {
                    /*SL:452*/this.back();
                }
                final String v3 = /*EL:455*/v2.toString().trim();
                /*SL:456*/if ("".equals(v3)) {
                    /*SL:457*/throw this.syntaxError("Missing value");
                }
                /*SL:459*/return JSONObject.stringToValue(v3);
            }
        }
    }
    
    public char skipTo(final char v-3) throws JSONException {
        char next;
        try {
            final long a1 = /*EL:475*/this.index;
            final long v1 = /*EL:476*/this.character;
            final long v2 = /*EL:477*/this.line;
            /*SL:478*/this.reader.mark(1000000);
            /*SL:491*/do {
                next = this.next();
                if (next == '\0') {
                    this.reader.reset();
                    this.index = a1;
                    this.character = v1;
                    this.line = v2;
                    return '\0';
                }
            } while (next != v-3);
            /*SL:492*/this.reader.mark(1);
        }
        catch (IOException a2) {
            /*SL:494*/throw new JSONException(a2);
        }
        /*SL:496*/this.back();
        /*SL:497*/return next;
    }
    
    public JSONException syntaxError(final String a1) {
        /*SL:507*/return new JSONException(a1 + this.toString());
    }
    
    public JSONException syntaxError(final String a1, final Throwable a2) {
        /*SL:518*/return new JSONException(a1 + this.toString(), a2);
    }
    
    @Override
    public String toString() {
        /*SL:528*/return " at " + this.index + " [character " + this.character + " line " + this.line + "]";
    }
}

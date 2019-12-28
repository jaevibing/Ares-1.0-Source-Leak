package com.fasterxml.jackson.core;

import com.fasterxml.jackson.core.io.NumberInput;

public class JsonPointer
{
    public static final char SEPARATOR = '/';
    protected static final JsonPointer EMPTY;
    protected final JsonPointer _nextSegment;
    protected volatile JsonPointer _head;
    protected final String _asString;
    protected final String _matchingPropertyName;
    protected final int _matchingElementIndex;
    
    protected JsonPointer() {
        this._nextSegment = null;
        this._matchingPropertyName = "";
        this._matchingElementIndex = -1;
        this._asString = "";
    }
    
    protected JsonPointer(final String a1, final String a2, final JsonPointer a3) {
        this._asString = a1;
        this._nextSegment = a3;
        this._matchingPropertyName = a2;
        this._matchingElementIndex = _parseIndex(a2);
    }
    
    protected JsonPointer(final String a1, final String a2, final int a3, final JsonPointer a4) {
        this._asString = a1;
        this._nextSegment = a4;
        this._matchingPropertyName = a2;
        this._matchingElementIndex = a3;
    }
    
    public static JsonPointer compile(final String a1) throws IllegalArgumentException {
        /*SL:124*/if (a1 == null || a1.length() == 0) {
            /*SL:125*/return JsonPointer.EMPTY;
        }
        /*SL:128*/if (a1.charAt(0) != '/') {
            /*SL:129*/throw new IllegalArgumentException("Invalid input: JSON Pointer expression must start with '/': \"" + a1 + "\"");
        }
        /*SL:131*/return _parseTail(a1);
    }
    
    public static JsonPointer valueOf(final String a1) {
        /*SL:138*/return compile(a1);
    }
    
    public static JsonPointer forPath(JsonStreamContext v-3, final boolean v-2) {
        /*SL:155*/if (v-3 == null) {
            /*SL:156*/return JsonPointer.EMPTY;
        }
        /*SL:158*/if (!v-3.hasPathSegment() && /*EL:160*/(!v-2 || !v-3.inRoot() || !v-3.hasCurrentIndex())) {
            /*SL:161*/v-3 = v-3.getParent();
        }
        JsonPointer jsonPointer = /*EL:164*/null;
        /*SL:166*/while (v-3 != null) {
            /*SL:167*/if (v-3.inObject()) {
                String a1 = /*EL:168*/v-3.getCurrentName();
                /*SL:169*/if (a1 == null) {
                    /*SL:170*/a1 = "";
                }
                /*SL:172*/jsonPointer = new JsonPointer(_fullPath(jsonPointer, a1), a1, jsonPointer);
            }
            else/*SL:173*/ if (v-3.inArray() || v-2) {
                final int a2 = /*EL:174*/v-3.getCurrentIndex();
                final String v1 = /*EL:175*/String.valueOf(a2);
                /*SL:176*/jsonPointer = new JsonPointer(_fullPath(jsonPointer, v1), v1, a2, jsonPointer);
            }
            v-3 = v-3.getParent();
        }
        /*SL:182*/if (jsonPointer == null) {
            /*SL:183*/return JsonPointer.EMPTY;
        }
        /*SL:185*/return jsonPointer;
    }
    
    private static String _fullPath(final JsonPointer a2, final String v1) {
        /*SL:190*/if (a2 == null) {
            final StringBuilder a3 = /*EL:191*/new StringBuilder(v1.length() + 1);
            /*SL:192*/a3.append('/');
            _appendEscaped(/*EL:193*/a3, v1);
            /*SL:194*/return a3.toString();
        }
        final String v2 = /*EL:196*/a2._asString;
        final StringBuilder v3 = /*EL:197*/new StringBuilder(v1.length() + 1 + v2.length());
        /*SL:198*/v3.append('/');
        _appendEscaped(/*EL:199*/v3, v1);
        /*SL:200*/v3.append(v2);
        /*SL:201*/return v3.toString();
    }
    
    private static void _appendEscaped(final StringBuilder v-2, final String v-1) {
        int a2 = /*EL:206*/0;
        for (int v1 = v-1.length(); a2 < v1; ++a2) {
            /*SL:207*/a2 = v-1.charAt(a2);
            /*SL:208*/if (a2 == '/') {
                /*SL:209*/v-2.append("~1");
            }
            else/*SL:212*/ if (a2 == '~') {
                /*SL:213*/v-2.append("~0");
            }
            else {
                /*SL:216*/v-2.append(a2);
            }
        }
    }
    
    public boolean matches() {
        /*SL:248*/return this._nextSegment == null;
    }
    
    public String getMatchingProperty() {
        /*SL:249*/return this._matchingPropertyName;
    }
    
    public int getMatchingIndex() {
        /*SL:250*/return this._matchingElementIndex;
    }
    
    public boolean mayMatchProperty() {
        /*SL:251*/return this._matchingPropertyName != null;
    }
    
    public boolean mayMatchElement() {
        /*SL:252*/return this._matchingElementIndex >= 0;
    }
    
    public JsonPointer last() {
        JsonPointer v1 = /*EL:261*/this;
        /*SL:262*/if (v1 == JsonPointer.EMPTY) {
            /*SL:263*/return null;
        }
        JsonPointer v2;
        /*SL:266*/while ((v2 = v1._nextSegment) != JsonPointer.EMPTY) {
            /*SL:267*/v1 = v2;
        }
        /*SL:269*/return v1;
    }
    
    public JsonPointer append(final JsonPointer a1) {
        /*SL:289*/if (this == JsonPointer.EMPTY) {
            /*SL:290*/return a1;
        }
        /*SL:292*/if (a1 == JsonPointer.EMPTY) {
            /*SL:293*/return this;
        }
        String v1 = /*EL:298*/this._asString;
        /*SL:299*/if (v1.endsWith("/")) {
            /*SL:301*/v1 = v1.substring(0, v1.length() - 1);
        }
        /*SL:303*/return compile(v1 + a1._asString);
    }
    
    public boolean matchesProperty(final String a1) {
        /*SL:313*/return this._nextSegment != null && this._matchingPropertyName.equals(a1);
    }
    
    public JsonPointer matchProperty(final String a1) {
        /*SL:317*/if (this._nextSegment != null && this._matchingPropertyName.equals(a1)) {
            /*SL:318*/return this._nextSegment;
        }
        /*SL:320*/return null;
    }
    
    public boolean matchesElement(final int a1) {
        /*SL:330*/return a1 == this._matchingElementIndex && a1 >= 0;
    }
    
    public JsonPointer matchElement(final int a1) {
        /*SL:337*/if (a1 != this._matchingElementIndex || a1 < 0) {
            /*SL:338*/return null;
        }
        /*SL:340*/return this._nextSegment;
    }
    
    public JsonPointer tail() {
        /*SL:349*/return this._nextSegment;
    }
    
    public JsonPointer head() {
        JsonPointer v1 = /*EL:363*/this._head;
        /*SL:364*/if (v1 == null) {
            /*SL:365*/if (this != JsonPointer.EMPTY) {
                /*SL:366*/v1 = this._constructHead();
            }
            /*SL:368*/this._head = v1;
        }
        /*SL:370*/return v1;
    }
    
    @Override
    public String toString() {
        /*SL:379*/return this._asString;
    }
    
    @Override
    public int hashCode() {
        /*SL:380*/return this._asString.hashCode();
    }
    
    @Override
    public boolean equals(final Object a1) {
        /*SL:383*/return a1 == this || /*EL:384*/(a1 != null && /*EL:385*/a1 instanceof JsonPointer && /*EL:386*/this._asString.equals(((JsonPointer)a1)._asString));
    }
    
    private static final int _parseIndex(final String v-2) {
        final int length = /*EL:396*/v-2.length();
        /*SL:399*/if (length == 0 || length > 10) {
            /*SL:400*/return -1;
        }
        char v0 = /*EL:403*/v-2.charAt(0);
        /*SL:404*/if (v0 <= '0') {
            /*SL:405*/return (length == 1 && v0 == '0') ? 0 : -1;
        }
        /*SL:407*/if (v0 > '9') {
            /*SL:408*/return -1;
        }
        /*SL:410*/for (int a1 = 1; a1 < length; ++a1) {
            /*SL:411*/v0 = v-2.charAt(a1);
            /*SL:412*/if (v0 > '9' || v0 < '0') {
                /*SL:413*/return -1;
            }
        }
        /*SL:416*/if (length == 10) {
            final long v = /*EL:417*/NumberInput.parseLong(v-2);
            /*SL:418*/if (v > 2147483647L) {
                /*SL:419*/return -1;
            }
        }
        /*SL:422*/return NumberInput.parseInt(v-2);
    }
    
    protected static JsonPointer _parseTail(final String v-1) {
        final int v0 = /*EL:426*/v-1.length();
        int v = /*EL:429*/1;
        while (v < v0) {
            final char a1 = /*EL:430*/v-1.charAt(v);
            /*SL:431*/if (a1 == '/') {
                /*SL:432*/return new JsonPointer(v-1, v-1.substring(1, v), _parseTail(v-1.substring(v)));
            }
            /*SL:435*/++v;
            /*SL:437*/if (a1 == '~' && v < v0) {
                /*SL:438*/return _parseQuotedTail(v-1, v);
            }
        }
        /*SL:443*/return new JsonPointer(v-1, v-1.substring(1), JsonPointer.EMPTY);
    }
    
    protected static JsonPointer _parseQuotedTail(final String a2, int v1) {
        final int v2 = /*EL:454*/a2.length();
        final StringBuilder v3 = /*EL:455*/new StringBuilder(Math.max(16, v2));
        /*SL:456*/if (v1 > 2) {
            /*SL:457*/v3.append(a2, 1, v1 - 1);
        }
        _appendEscape(/*EL:459*/v3, a2.charAt(v1++));
        /*SL:460*/while (v1 < v2) {
            final char a3 = /*EL:461*/a2.charAt(v1);
            /*SL:462*/if (a3 == '/') {
                /*SL:463*/return new JsonPointer(a2, v3.toString(), _parseTail(a2.substring(v1)));
            }
            /*SL:466*/++v1;
            /*SL:467*/if (a3 == '~' && v1 < v2) {
                _appendEscape(/*EL:468*/v3, a2.charAt(v1++));
            }
            else {
                /*SL:471*/v3.append(a3);
            }
        }
        /*SL:474*/return new JsonPointer(a2, v3.toString(), JsonPointer.EMPTY);
    }
    
    protected JsonPointer _constructHead() {
        final JsonPointer v1 = /*EL:480*/this.last();
        /*SL:481*/if (v1 == this) {
            /*SL:482*/return JsonPointer.EMPTY;
        }
        final int v2 = /*EL:485*/v1._asString.length();
        final JsonPointer v3 = /*EL:486*/this._nextSegment;
        /*SL:487*/return new JsonPointer(this._asString.substring(0, this._asString.length() - v2), this._matchingPropertyName, this._matchingElementIndex, v3._constructHead(v2, v1));
    }
    
    protected JsonPointer _constructHead(final int a1, final JsonPointer a2) {
        /*SL:493*/if (this == a2) {
            /*SL:494*/return JsonPointer.EMPTY;
        }
        final JsonPointer v1 = /*EL:496*/this._nextSegment;
        final String v2 = /*EL:497*/this._asString;
        /*SL:498*/return new JsonPointer(v2.substring(0, v2.length() - a1), this._matchingPropertyName, this._matchingElementIndex, v1._constructHead(a1, a2));
    }
    
    private static void _appendEscape(final StringBuilder a1, char a2) {
        /*SL:503*/if (a2 == '0') {
            /*SL:504*/a2 = '~';
        }
        else/*SL:505*/ if (a2 == '1') {
            /*SL:506*/a2 = '/';
        }
        else {
            /*SL:508*/a1.append('~');
        }
        /*SL:510*/a1.append(a2);
    }
    
    static {
        EMPTY = new JsonPointer();
    }
}

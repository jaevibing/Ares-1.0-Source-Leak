package org.yaml.snakeyaml.external.com.google.gdata.util.common.base;

public class PercentEscaper extends UnicodeEscaper
{
    public static final String SAFECHARS_URLENCODER = "-_.*";
    public static final String SAFEPATHCHARS_URLENCODER = "-_.!~*'()@:$&,;=";
    public static final String SAFEQUERYSTRINGCHARS_URLENCODER = "-_.!~*'()@:$,;/?:";
    private static final char[] URI_ESCAPED_SPACE;
    private static final char[] UPPER_HEX_DIGITS;
    private final boolean plusForSpace;
    private final boolean[] safeOctets;
    
    public PercentEscaper(final String a1, final boolean a2) {
        if (a1.matches(".*[0-9A-Za-z].*")) {
            throw new IllegalArgumentException("Alphanumeric characters are always 'safe' and should not be explicitly specified");
        }
        if (a2 && a1.contains(" ")) {
            throw new IllegalArgumentException("plusForSpace cannot be specified when space is a 'safe' character");
        }
        if (a1.contains("%")) {
            throw new IllegalArgumentException("The '%' character cannot be specified as 'safe'");
        }
        this.plusForSpace = a2;
        this.safeOctets = createSafeOctets(a1);
    }
    
    private static boolean[] createSafeOctets(final String v-3) {
        int max = /*EL:146*/122;
        final char[] charArray;
        final char[] array = /*EL:148*/charArray = v-3.toCharArray();
        for (final char a1 : charArray) {
            /*SL:149*/max = Math.max(a1, max);
        }
        final boolean[] v0 = /*EL:151*/new boolean[max + 1];
        /*SL:152*/for (int v = 48; v <= 57; ++v) {
            /*SL:153*/v0[v] = true;
        }
        /*SL:155*/for (int v = 65; v <= 90; ++v) {
            /*SL:156*/v0[v] = true;
        }
        /*SL:158*/for (int v = 97; v <= 122; ++v) {
            /*SL:159*/v0[v] = true;
        }
        /*SL:161*/for (final char v2 : array) {
            /*SL:162*/v0[v2] = true;
        }
        /*SL:164*/return v0;
    }
    
    @Override
    protected int nextEscapeIndex(final CharSequence a3, int v1, final int v2) {
        /*SL:174*/while (v1 < v2) {
            final char a4 = /*EL:175*/a3.charAt(v1);
            /*SL:176*/if (a4 >= this.safeOctets.length) {
                break;
            }
            if (!this.safeOctets[a4]) {
                /*SL:177*/break;
            }
            ++v1;
        }
        /*SL:180*/return v1;
    }
    
    @Override
    public String escape(final String v-1) {
        /*SL:191*/for (int v0 = v-1.length(), v = 0; v < v0; ++v) {
            final char a1 = /*EL:192*/v-1.charAt(v);
            /*SL:193*/if (a1 >= this.safeOctets.length || !this.safeOctets[a1]) {
                /*SL:194*/return this.escapeSlow(v-1, v);
            }
        }
        /*SL:197*/return v-1;
    }
    
    @Override
    protected char[] escape(int v0) {
        /*SL:208*/if (v0 < this.safeOctets.length && this.safeOctets[v0]) {
            /*SL:209*/return null;
        }
        /*SL:210*/if (v0 == 32 && this.plusForSpace) {
            /*SL:211*/return PercentEscaper.URI_ESCAPED_SPACE;
        }
        /*SL:212*/if (v0 <= 127) {
            final char[] a1 = /*EL:215*/{ /*EL:216*/'%', /*EL:218*/PercentEscaper.UPPER_HEX_DIGITS[v0 >>> 4], PercentEscaper.UPPER_HEX_DIGITS[v0 & 0xF] };
            /*SL:219*/return a1;
        }
        /*SL:220*/if (v0 <= 2047) {
            final char[] v = /*EL:223*/{ /*EL:224*/'%', '\0', '\0', /*EL:225*/'%', '\0', /*EL:226*/PercentEscaper.UPPER_HEX_DIGITS[v0 & 0xF] };
            /*SL:227*/v0 >>>= 4;
            /*SL:228*/v[4] = PercentEscaper.UPPER_HEX_DIGITS[0x8 | (v0 & 0x3)];
            /*SL:229*/v0 >>>= 2;
            /*SL:230*/v[2] = PercentEscaper.UPPER_HEX_DIGITS[v0 & 0xF];
            /*SL:231*/v0 >>>= 4;
            /*SL:232*/v[1] = PercentEscaper.UPPER_HEX_DIGITS[0xC | v0];
            /*SL:233*/return v;
        }
        /*SL:234*/if (v0 <= 65535) {
            final char[] v = /*EL:237*/{ /*EL:238*/'%', /*EL:239*/'E', '\0', /*EL:240*/'%', '\0', '\0', /*EL:241*/'%', '\0', /*EL:242*/PercentEscaper.UPPER_HEX_DIGITS[v0 & 0xF] };
            /*SL:243*/v0 >>>= 4;
            /*SL:244*/v[7] = PercentEscaper.UPPER_HEX_DIGITS[0x8 | (v0 & 0x3)];
            /*SL:245*/v0 >>>= 2;
            /*SL:246*/v[5] = PercentEscaper.UPPER_HEX_DIGITS[v0 & 0xF];
            /*SL:247*/v0 >>>= 4;
            /*SL:248*/v[4] = PercentEscaper.UPPER_HEX_DIGITS[0x8 | (v0 & 0x3)];
            /*SL:249*/v0 >>>= 2;
            /*SL:250*/v[2] = PercentEscaper.UPPER_HEX_DIGITS[v0];
            /*SL:251*/return v;
        }
        /*SL:252*/if (v0 <= 1114111) {
            final char[] v = /*EL:253*/{ /*EL:256*/'%', /*EL:257*/'F', '\0', /*EL:258*/'%', '\0', '\0', /*EL:259*/'%', '\0', '\0', /*EL:260*/'%', '\0', /*EL:261*/PercentEscaper.UPPER_HEX_DIGITS[v0 & 0xF] };
            /*SL:262*/v0 >>>= 4;
            /*SL:263*/v[10] = PercentEscaper.UPPER_HEX_DIGITS[0x8 | (v0 & 0x3)];
            /*SL:264*/v0 >>>= 2;
            /*SL:265*/v[8] = PercentEscaper.UPPER_HEX_DIGITS[v0 & 0xF];
            /*SL:266*/v0 >>>= 4;
            /*SL:267*/v[7] = PercentEscaper.UPPER_HEX_DIGITS[0x8 | (v0 & 0x3)];
            /*SL:268*/v0 >>>= 2;
            /*SL:269*/v[5] = PercentEscaper.UPPER_HEX_DIGITS[v0 & 0xF];
            /*SL:270*/v0 >>>= 4;
            /*SL:271*/v[4] = PercentEscaper.UPPER_HEX_DIGITS[0x8 | (v0 & 0x3)];
            /*SL:272*/v0 >>>= 2;
            /*SL:273*/v[2] = PercentEscaper.UPPER_HEX_DIGITS[v0 & 0x7];
            /*SL:274*/return v;
        }
        /*SL:278*/throw new IllegalArgumentException("Invalid unicode character value " + v0);
    }
    
    static {
        URI_ESCAPED_SPACE = new char[] { '+' };
        UPPER_HEX_DIGITS = "0123456789ABCDEF".toCharArray();
    }
}

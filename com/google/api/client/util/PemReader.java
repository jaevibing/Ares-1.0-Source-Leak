package com.google.api.client.util;

import java.util.regex.Matcher;
import java.io.IOException;
import java.io.Reader;
import java.io.BufferedReader;
import java.util.regex.Pattern;

@Beta
public final class PemReader
{
    private static final Pattern BEGIN_PATTERN;
    private static final Pattern END_PATTERN;
    private BufferedReader reader;
    
    public PemReader(final Reader a1) {
        this.reader = new BufferedReader(a1);
    }
    
    public Section readNextSection() throws IOException {
        /*SL:73*/return this.readNextSection(null);
    }
    
    public Section readNextSection(final String v-3) throws IOException {
        String a2 = /*EL:83*/null;
        StringBuilder sb = /*EL:84*/null;
        while (true) {
            final String v0 = /*EL:86*/this.reader.readLine();
            /*SL:87*/if (v0 == null) {
                /*SL:88*/Preconditions.checkArgument(a2 == null, "missing end tag (%s)", a2);
                /*SL:89*/return null;
            }
            /*SL:91*/if (sb == null) {
                final Matcher v = PemReader.BEGIN_PATTERN.matcher(/*EL:92*/v0);
                /*SL:93*/if (!v.matches()) {
                    continue;
                }
                final String a1 = /*EL:94*/v.group(1);
                /*SL:95*/if (v-3 != null && !a1.equals(v-3)) {
                    continue;
                }
                /*SL:96*/sb = new StringBuilder();
                /*SL:97*/a2 = a1;
            }
            else {
                final Matcher v = PemReader.END_PATTERN.matcher(/*EL:101*/v0);
                /*SL:102*/if (v.matches()) {
                    final String v2 = /*EL:103*/v.group(1);
                    /*SL:104*/Preconditions.checkArgument(v2.equals(a2), "end tag (%s) doesn't match begin tag (%s)", v2, a2);
                    /*SL:106*/return new Section(a2, Base64.decodeBase64(sb.toString()));
                }
                /*SL:108*/sb.append(v0);
            }
        }
    }
    
    public static Section readFirstSectionAndClose(final Reader a1) throws IOException {
        /*SL:120*/return readFirstSectionAndClose(a1, null);
    }
    
    public static Section readFirstSectionAndClose(final Reader a1, final String a2) throws IOException {
        final PemReader v1 = /*EL:133*/new PemReader(a1);
        try {
            /*SL:135*/return v1.readNextSection(a2);
        }
        finally {
            /*SL:137*/v1.close();
        }
    }
    
    public void close() throws IOException {
        /*SL:149*/this.reader.close();
    }
    
    static {
        BEGIN_PATTERN = Pattern.compile("-----BEGIN ([A-Z ]+)-----");
        END_PATTERN = Pattern.compile("-----END ([A-Z ]+)-----");
    }
    
    public static final class Section
    {
        private final String title;
        private final byte[] base64decodedBytes;
        
        Section(final String a1, final byte[] a2) {
            this.title = Preconditions.<String>checkNotNull(a1);
            this.base64decodedBytes = Preconditions.<byte[]>checkNotNull(a2);
        }
        
        public String getTitle() {
            /*SL:172*/return this.title;
        }
        
        public byte[] getBase64DecodedBytes() {
            /*SL:177*/return this.base64decodedBytes;
        }
    }
}

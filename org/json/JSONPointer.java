package org.json;

import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Collection;
import java.util.ArrayList;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Collections;
import java.util.List;

public class JSONPointer
{
    private static final String ENCODING = "utf-8";
    private final List<String> refTokens;
    
    public static Builder builder() {
        /*SL:125*/return new Builder();
    }
    
    public JSONPointer(final String v0) {
        if (v0 == null) {
            throw new NullPointerException("pointer cannot be null");
        }
        if (v0.isEmpty() || v0.equals("#")) {
            this.refTokens = Collections.<String>emptyList();
            return;
        }
        String v = null;
        Label_0105: {
            if (v0.startsWith("#/")) {
                v = v0.substring(2);
                try {
                    v = URLDecoder.decode(v, "utf-8");
                    break Label_0105;
                }
                catch (UnsupportedEncodingException a1) {
                    throw new RuntimeException(a1);
                }
            }
            if (!v0.startsWith("/")) {
                throw new IllegalArgumentException("a JSON pointer should start with '/' or '#/'");
            }
            v = v0.substring(1);
        }
        this.refTokens = new ArrayList<String>();
        int v2 = -1;
        int v3 = 0;
        do {
            v3 = v2 + 1;
            v2 = v.indexOf(47, v3);
            if (v3 == v2 || v3 == v.length()) {
                this.refTokens.add("");
            }
            else if (v2 >= 0) {
                final String v4 = v.substring(v3, v2);
                this.refTokens.add(this.unescape(v4));
            }
            else {
                final String v4 = v.substring(v3);
                this.refTokens.add(this.unescape(v4));
            }
        } while (v2 >= 0);
    }
    
    public JSONPointer(final List<String> a1) {
        this.refTokens = new ArrayList<String>(a1);
    }
    
    private String unescape(final String a1) {
        /*SL:190*/return a1.replace("~1", "/").replace("~0", "~").replace("\\\"", "\"").replace(/*EL:191*/"\\\\", "\\");
    }
    
    public Object queryFrom(final Object v2) throws JSONPointerException {
        /*SL:206*/if (this.refTokens.isEmpty()) {
            /*SL:207*/return v2;
        }
        Object v3 = /*EL:209*/v2;
        /*SL:210*/for (final String a1 : this.refTokens) {
            /*SL:211*/if (v3 instanceof JSONObject) {
                /*SL:212*/v3 = ((JSONObject)v3).opt(this.unescape(a1));
            }
            else {
                /*SL:213*/if (!(v3 instanceof JSONArray)) {
                    /*SL:216*/throw new JSONPointerException(String.format("value [%s] is not an array or object therefore its key %s cannot be resolved", v3, a1));
                }
                v3 = this.readByIndexToken(v3, a1);
            }
        }
        /*SL:221*/return v3;
    }
    
    private Object readByIndexToken(final Object v-2, final String v-1) throws JSONPointerException {
        try {
            final int a2 = /*EL:233*/Integer.parseInt(v-1);
            final JSONArray v1 = /*EL:234*/(JSONArray)v-2;
            /*SL:235*/if (a2 >= v1.length()) {
                /*SL:236*/throw new JSONPointerException(String.format("index %s is out of bounds - the array has %d elements", v-1, v1.length()));
            }
            try {
                /*SL:240*/return v1.get(a2);
            }
            catch (JSONException a2) {
                /*SL:242*/throw new JSONPointerException("Error reading value at index position " + a2, a2);
            }
        }
        catch (NumberFormatException v2) {
            /*SL:245*/throw new JSONPointerException(String.format("%s is not an array index", v-1), v2);
        }
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = /*EL:255*/new StringBuilder("");
        /*SL:256*/for (final String v1 : this.refTokens) {
            /*SL:257*/sb.append('/').append(this.escape(v1));
        }
        /*SL:259*/return sb.toString();
    }
    
    private String escape(final String a1) {
        /*SL:271*/return a1.replace("~", "~0").replace("/", "~1").replace(/*EL:272*/"\\", "\\\\").replace(/*EL:273*/"\"", "\\\"");
    }
    
    public String toURIFragment() {
        try {
            final StringBuilder sb = /*EL:283*/new StringBuilder("#");
            /*SL:284*/for (final String v1 : this.refTokens) {
                /*SL:285*/sb.append('/').append(URLEncoder.encode(v1, "utf-8"));
            }
            /*SL:287*/return sb.toString();
        }
        catch (UnsupportedEncodingException ex) {
            /*SL:289*/throw new RuntimeException(ex);
        }
    }
    
    public static class Builder
    {
        private final List<String> refTokens;
        
        public Builder() {
            this.refTokens = new ArrayList<String>();
        }
        
        public JSONPointer build() {
            /*SL:73*/return new JSONPointer(this.refTokens);
        }
        
        public Builder append(final String a1) {
            /*SL:89*/if (a1 == null) {
                /*SL:90*/throw new NullPointerException("token cannot be null");
            }
            /*SL:92*/this.refTokens.add(a1);
            /*SL:93*/return this;
        }
        
        public Builder append(final int a1) {
            /*SL:104*/this.refTokens.add(String.valueOf(a1));
            /*SL:105*/return this;
        }
    }
}

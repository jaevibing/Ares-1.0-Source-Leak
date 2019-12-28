package org.json;

public class HTTPTokener extends JSONTokener
{
    public HTTPTokener(final String a1) {
        super(a1);
    }
    
    public String nextToken() throws JSONException {
        StringBuilder v2 = /*EL:52*/new StringBuilder();
        /*SL:55*/do {
            v2 = this.next();
        } while (Character.isWhitespace(v2));
        /*SL:56*/if (v2 != '\"' && v2 != '\'') {
            /*SL:70*/while (v2 != '\0' && !Character.isWhitespace(v2)) {
                /*SL:73*/v2.append(v2);
                /*SL:74*/v2 = this.next();
            }
            return v2.toString();
        }
        final char v3 = v2;
        while (true) {
            v2 = this.next();
            if (v2 < ' ') {
                throw this.syntaxError("Unterminated string.");
            }
            if (v2 == v3) {
                return v2.toString();
            }
            v2.append(v2);
        }
    }
}

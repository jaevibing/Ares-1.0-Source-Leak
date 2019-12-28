package org.json;

import java.io.ObjectInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.Reader;
import java.util.HashMap;

public class XMLTokener extends JSONTokener
{
    public static final HashMap<String, Character> entity;
    
    public XMLTokener(final Reader a1) {
        super(a1);
    }
    
    public XMLTokener(final String a1) {
        super(a1);
    }
    
    public String nextCDATA() throws JSONException {
        StringBuilder v3 = /*EL:76*/new StringBuilder();
        /*SL:77*/while (this.more()) {
            final char v2 = /*EL:78*/this.next();
            /*SL:79*/v3.append(v2);
            /*SL:80*/v3 = v3.length() - 3;
            /*SL:82*/if (v3 >= 0 && v3.charAt(v3) == ']' && v3.charAt(v3 + 1) == ']' && v3.charAt(v3 + 2) == '>') {
                /*SL:83*/v3.setLength(v3);
                /*SL:84*/return v3.toString();
            }
        }
        /*SL:87*/throw this.syntaxError("Unclosed CDATA");
    }
    
    public Object nextContent() throws JSONException {
        char v1;
        /*SL:105*/do {
            v1 = this.next();
        } while (Character.isWhitespace(v1));
        /*SL:106*/if (v1 == '\0') {
            /*SL:107*/return null;
        }
        /*SL:109*/if (v1 == '<') {
            /*SL:110*/return XML.LT;
        }
        final StringBuilder v2 = /*EL:112*/new StringBuilder();
        /*SL:114*/while (v1 != '\0') {
            /*SL:117*/if (v1 == '<') {
                /*SL:118*/this.back();
                /*SL:119*/return v2.toString().trim();
            }
            /*SL:121*/if (v1 == '&') {
                /*SL:122*/v2.append(this.nextEntity(v1));
            }
            else {
                /*SL:124*/v2.append(v1);
            }
            /*SL:126*/v1 = this.next();
        }
        return v2.toString().trim();
    }
    
    public Object nextEntity(final char v2) throws JSONException {
        final StringBuilder v3 = /*EL:139*/new StringBuilder();
        char a1;
        while (true) {
            /*SL:141*/a1 = this.next();
            /*SL:142*/if (!Character.isLetterOrDigit(a1) && a1 != '#') {
                break;
            }
            /*SL:143*/v3.append(Character.toLowerCase(a1));
        }
        /*SL:144*/if (a1 == ';') {
            final String v4 = /*EL:150*/v3.toString();
            /*SL:151*/return unescapeEntity(v4);
        }
        throw this.syntaxError("Missing ';' in XML entity: &" + (Object)v3);
    }
    
    static String unescapeEntity(final String v0) {
        /*SL:161*/if (v0 == null || v0.isEmpty()) {
            /*SL:162*/return "";
        }
        /*SL:165*/if (v0.charAt(0) == '#') {
            final int v;
            /*SL:167*/if (v0.charAt(1) == 'x') {
                final int a1 = /*EL:169*/Integer.parseInt(v0.substring(2), 16);
            }
            else {
                /*SL:172*/v = Integer.parseInt(v0.substring(1));
            }
            /*SL:174*/return new String(new int[] { v }, 0, 1);
        }
        final Character v2 = XMLTokener.entity.get(/*EL:176*/v0);
        /*SL:177*/if (v2 == null) {
            /*SL:179*/return '&' + v0 + ';';
        }
        /*SL:181*/return v2.toString();
    }
    
    public Object nextMeta() throws JSONException {
        char v0;
        /*SL:199*/do {
            v0 = this.next();
        } while (Character.isWhitespace(v0));
        /*SL:200*/switch (v0) {
            case '\0': {
                /*SL:202*/throw this.syntaxError("Misshaped meta tag");
            }
            case '<': {
                /*SL:204*/return XML.LT;
            }
            case '>': {
                /*SL:206*/return XML.GT;
            }
            case '/': {
                /*SL:208*/return XML.SLASH;
            }
            case '=': {
                /*SL:210*/return XML.EQ;
            }
            case '!': {
                /*SL:212*/return XML.BANG;
            }
            case '?': {
                /*SL:214*/return XML.QUEST;
            }
            case '\"':
            case '\'': {
                final char v = /*EL:217*/v0;
                /*SL:223*/do {
                    v0 = this.next();
                    if (v0 == '\0') {
                        throw this.syntaxError("Unterminated string");
                    }
                } while (v0 != v);
                /*SL:224*/return Boolean.TRUE;
            }
            default: {
                while (true) {
                    /*SL:229*/v0 = this.next();
                    /*SL:230*/if (Character.isWhitespace(v0)) {
                        /*SL:231*/return Boolean.TRUE;
                    }
                    /*SL:233*/switch (v0) {
                        case '\0':
                        case '!':
                        case '\"':
                        case '\'':
                        case '/':
                        case '<':
                        case '=':
                        case '>':
                        case '?': {
                            /*SL:243*/this.back();
                            /*SL:244*/return Boolean.TRUE;
                        }
                        default: {
                            continue;
                        }
                    }
                }
                break;
            }
        }
    }
    
    public Object nextToken() throws JSONException {
        char v0;
        /*SL:265*/do {
            v0 = this.next();
        } while (Character.isWhitespace(v0));
        /*SL:266*/switch (v0) {
            case '\0': {
                /*SL:268*/throw this.syntaxError("Misshaped element");
            }
            case '<': {
                /*SL:270*/throw this.syntaxError("Misplaced '<'");
            }
            case '>': {
                /*SL:272*/return XML.GT;
            }
            case '/': {
                /*SL:274*/return XML.SLASH;
            }
            case '=': {
                /*SL:276*/return XML.EQ;
            }
            case '!': {
                /*SL:278*/return XML.BANG;
            }
            case '?': {
                /*SL:280*/return XML.QUEST;
            }
            case '\"':
            case '\'': {
                final char v = /*EL:286*/v0;
                final StringBuilder v2 = /*EL:287*/new StringBuilder();
                while (true) {
                    /*SL:289*/v0 = this.next();
                    /*SL:290*/if (v0 == '\0') {
                        /*SL:291*/throw this.syntaxError("Unterminated string");
                    }
                    /*SL:293*/if (v0 == v) {
                        /*SL:294*/return v2.toString();
                    }
                    /*SL:296*/if (v0 == '&') {
                        /*SL:297*/v2.append(this.nextEntity(v0));
                    }
                    else {
                        /*SL:299*/v2.append(v0);
                    }
                }
                break;
            }
            default: {
                final StringBuilder v2 = /*EL:306*/new StringBuilder();
                while (true) {
                    /*SL:308*/v2.append(v0);
                    /*SL:309*/v0 = this.next();
                    /*SL:310*/if (Character.isWhitespace(v0)) {
                        /*SL:311*/return v2.toString();
                    }
                    /*SL:313*/switch (v0) {
                        case '\0': {
                            /*SL:315*/return v2.toString();
                        }
                        case '!':
                        case '/':
                        case '=':
                        case '>':
                        case '?':
                        case '[':
                        case ']': {
                            /*SL:323*/this.back();
                            /*SL:324*/return v2.toString();
                        }
                        case '\"':
                        case '\'':
                        case '<': {
                            /*SL:328*/throw this.syntaxError("Bad character in a name");
                        }
                        default: {
                            continue;
                        }
                    }
                }
                break;
            }
        }
    }
    
    public void skipPast(final String v2) {
        int v3 = /*EL:348*/0;
        final int v4 = /*EL:349*/v2.length();
        final char[] v5 = /*EL:350*/new char[v4];
        /*SL:357*/for (int v6 = 0; v6 < v4; ++v6) {
            final char a1 = /*EL:358*/this.next();
            /*SL:359*/if (a1 == '\0') {
                /*SL:360*/return;
            }
            /*SL:362*/v5[v6] = a1;
        }
        while (true) {
            int v7 = /*EL:368*/v3;
            boolean v8 = /*EL:369*/true;
            /*SL:373*/for (int v6 = 0; v6 < v4; ++v6) {
                /*SL:374*/if (v5[v7] != v2.charAt(v6)) {
                    /*SL:375*/v8 = false;
                    /*SL:376*/break;
                }
                /*SL:379*/if (++v7 >= v4) {
                    /*SL:380*/v7 -= v4;
                }
            }
            /*SL:386*/if (v8) {
                /*SL:387*/return;
            }
            final char v9 = /*EL:392*/this.next();
            /*SL:393*/if (v9 == '\0') {
                /*SL:394*/return;
            }
            /*SL:400*/v5[v3] = v9;
            /*SL:402*/if (++v3 < v4) {
                continue;
            }
            /*SL:403*/v3 -= v4;
        }
    }
    
    static {
        (entity = new HashMap<String, Character>(8)).put("amp", XML.AMP);
        XMLTokener.entity.put("apos", XML.APOS);
        XMLTokener.entity.put("gt", XML.GT);
        XMLTokener.entity.put("lt", XML.LT);
        XMLTokener.entity.put("quot", XML.QUOT);
    }
}

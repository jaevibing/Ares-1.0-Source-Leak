package com.google.api.client.http;

import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Collections;
import java.util.Map;
import java.util.Locale;
import java.util.regex.Matcher;
import com.google.api.client.util.Preconditions;
import java.util.TreeMap;
import java.util.SortedMap;
import java.util.regex.Pattern;

public final class HttpMediaType
{
    private static final Pattern TYPE_REGEX;
    private static final Pattern TOKEN_REGEX;
    private static final Pattern FULL_MEDIA_TYPE_REGEX;
    private static final Pattern PARAMETER_REGEX;
    private String type;
    private String subType;
    private final SortedMap<String, String> parameters;
    private String cachedBuildResult;
    
    public HttpMediaType(final String a1, final String a2) {
        this.type = "application";
        this.subType = "octet-stream";
        this.parameters = new TreeMap<String, String>();
        this.setType(a1);
        this.setSubType(a2);
    }
    
    public HttpMediaType(final String a1) {
        this.type = "application";
        this.subType = "octet-stream";
        this.parameters = new TreeMap<String, String>();
        this.fromString(a1);
    }
    
    public HttpMediaType setType(final String a1) {
        /*SL:121*/Preconditions.checkArgument(HttpMediaType.TYPE_REGEX.matcher(a1).matches(), /*EL:122*/(Object)"Type contains reserved characters");
        /*SL:123*/this.type = a1;
        /*SL:124*/this.cachedBuildResult = null;
        /*SL:125*/return this;
    }
    
    public String getType() {
        /*SL:132*/return this.type;
    }
    
    public HttpMediaType setSubType(final String a1) {
        /*SL:141*/Preconditions.checkArgument(HttpMediaType.TYPE_REGEX.matcher(a1).matches(), /*EL:142*/(Object)"Subtype contains reserved characters");
        /*SL:143*/this.subType = a1;
        /*SL:144*/this.cachedBuildResult = null;
        /*SL:145*/return this;
    }
    
    public String getSubType() {
        /*SL:152*/return this.subType;
    }
    
    private HttpMediaType fromString(final String v-3) {
        Matcher matcher = HttpMediaType.FULL_MEDIA_TYPE_REGEX.matcher(/*EL:167*/v-3);
        /*SL:168*/Preconditions.checkArgument(matcher.matches(), /*EL:169*/(Object)"Type must be in the 'maintype/subtype; parameter=value' format");
        /*SL:171*/this.setType(matcher.group(1));
        /*SL:172*/this.setSubType(matcher.group(2));
        final String group = /*EL:173*/matcher.group(3);
        /*SL:174*/if (group != null) {
            /*SL:175*/matcher = HttpMediaType.PARAMETER_REGEX.matcher(group);
            /*SL:176*/while (matcher.find()) {
                final String a1 = /*EL:178*/matcher.group(1);
                String v1 = /*EL:179*/matcher.group(3);
                /*SL:180*/if (v1 == null) {
                    /*SL:181*/v1 = matcher.group(2);
                }
                /*SL:183*/this.setParameter(a1, v1);
            }
        }
        /*SL:186*/return this;
    }
    
    public HttpMediaType setParameter(final String a1, final String a2) {
        /*SL:196*/if (a2 == null) {
            /*SL:197*/this.removeParameter(a1);
            /*SL:198*/return this;
        }
        /*SL:201*/Preconditions.checkArgument(HttpMediaType.TOKEN_REGEX.matcher(a1).matches(), /*EL:202*/(Object)"Name contains reserved characters");
        /*SL:203*/this.cachedBuildResult = null;
        /*SL:204*/this.parameters.put(a1.toLowerCase(Locale.US), a2);
        /*SL:205*/return this;
    }
    
    public String getParameter(final String a1) {
        /*SL:214*/return this.parameters.get(a1.toLowerCase(Locale.US));
    }
    
    public HttpMediaType removeParameter(final String a1) {
        /*SL:223*/this.cachedBuildResult = null;
        /*SL:224*/this.parameters.remove(a1.toLowerCase(Locale.US));
        /*SL:225*/return this;
    }
    
    public void clearParameters() {
        /*SL:232*/this.cachedBuildResult = null;
        /*SL:233*/this.parameters.clear();
    }
    
    public Map<String, String> getParameters() {
        /*SL:241*/return Collections.<String, String>unmodifiableMap((Map<? extends String, ? extends String>)this.parameters);
    }
    
    static boolean matchesToken(final String a1) {
        /*SL:249*/return HttpMediaType.TOKEN_REGEX.matcher(a1).matches();
    }
    
    private static String quoteString(final String a1) {
        String v1 = /*EL:253*/a1.replace("\\", "\\\\");
        /*SL:254*/v1 = v1.replace("\"", "\\\"");
        /*SL:255*/return "\"" + v1 + "\"";
    }
    
    public String build() {
        /*SL:262*/if (this.cachedBuildResult != null) {
            /*SL:263*/return this.cachedBuildResult;
        }
        final StringBuilder sb = /*EL:266*/new StringBuilder();
        /*SL:267*/sb.append(this.type);
        /*SL:268*/sb.append('/');
        /*SL:269*/sb.append(this.subType);
        /*SL:270*/if (this.parameters != null) {
            /*SL:271*/for (final Map.Entry<String, String> v0 : this.parameters.entrySet()) {
                final String v = /*EL:272*/v0.getValue();
                /*SL:273*/sb.append("; ");
                /*SL:274*/sb.append(v0.getKey());
                /*SL:275*/sb.append("=");
                /*SL:276*/sb.append(matchesToken(v) ? v : quoteString(v));
            }
        }
        /*SL:280*/return this.cachedBuildResult = sb.toString();
    }
    
    @Override
    public String toString() {
        /*SL:285*/return this.build();
    }
    
    public boolean equalsIgnoreParameters(final HttpMediaType a1) {
        /*SL:293*/return a1 != null && this.getType().equalsIgnoreCase(a1.getType()) && this.getSubType().equalsIgnoreCase(/*EL:294*/a1.getSubType());
    }
    
    public static boolean equalsIgnoreParameters(final String a1, final String a2) {
        /*SL:303*/return (a1 == null && a2 == null) || (a1 != null && a2 != null && new HttpMediaType(a1).equalsIgnoreParameters(new HttpMediaType(a2)));
    }
    
    public HttpMediaType setCharsetParameter(final Charset a1) {
        /*SL:313*/this.setParameter("charset", (a1 == null) ? null : a1.name());
        /*SL:314*/return this;
    }
    
    public Charset getCharsetParameter() {
        final String v1 = /*EL:321*/this.getParameter("charset");
        /*SL:322*/return (v1 == null) ? null : Charset.forName(v1);
    }
    
    @Override
    public int hashCode() {
        /*SL:327*/return this.build().hashCode();
    }
    
    @Override
    public boolean equals(final Object a1) {
        /*SL:332*/if (!(a1 instanceof HttpMediaType)) {
            /*SL:333*/return false;
        }
        final HttpMediaType v1 = /*EL:336*/(HttpMediaType)a1;
        /*SL:338*/return this.equalsIgnoreParameters(v1) && this.parameters.equals(v1.parameters);
    }
    
    static {
        TYPE_REGEX = Pattern.compile("[\\w!#$&.+\\-\\^_]+|[*]");
        TOKEN_REGEX = Pattern.compile("[\\p{ASCII}&&[^\\p{Cntrl} ;/=\\[\\]\\(\\)\\<\\>\\@\\,\\:\\\"\\?\\=]]+");
        final String v1 = "[^\\s/=;\"]+";
        final String v2 = ";.*";
        FULL_MEDIA_TYPE_REGEX = Pattern.compile("\\s*(" + v1 + ")/(" + v1 + ")\\s*(" + v2 + ")?", 32);
        final String v3 = "\"([^\"]*)\"";
        final String v4 = "[^\\s;\"]*";
        final String v5 = v3 + "|" + v4;
        PARAMETER_REGEX = Pattern.compile("\\s*;\\s*(" + v1 + ")=(" + v5 + ")");
    }
}

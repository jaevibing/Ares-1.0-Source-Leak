package com.google.api.client.http;

import java.util.Arrays;
import com.google.api.client.util.ArrayValueMap;
import com.google.api.client.util.ClassInfo;
import java.lang.reflect.Type;
import com.google.api.client.util.Throwables;
import java.util.Locale;
import java.util.Iterator;
import com.google.api.client.util.Types;
import java.util.Map;
import java.util.HashSet;
import com.google.api.client.util.FieldInfo;
import java.io.IOException;
import java.util.logging.Level;
import com.google.api.client.util.Data;
import java.io.Writer;
import java.util.logging.Logger;
import com.google.api.client.util.Base64;
import com.google.api.client.util.StringUtils;
import com.google.api.client.util.Preconditions;
import java.util.Collection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import com.google.api.client.util.Key;
import java.util.List;
import com.google.api.client.util.GenericData;

public class HttpHeaders extends GenericData
{
    @Key("Accept")
    private List<String> accept;
    @Key("Accept-Encoding")
    private List<String> acceptEncoding;
    @Key("Authorization")
    private List<String> authorization;
    @Key("Cache-Control")
    private List<String> cacheControl;
    @Key("Content-Encoding")
    private List<String> contentEncoding;
    @Key("Content-Length")
    private List<Long> contentLength;
    @Key("Content-MD5")
    private List<String> contentMD5;
    @Key("Content-Range")
    private List<String> contentRange;
    @Key("Content-Type")
    private List<String> contentType;
    @Key("Cookie")
    private List<String> cookie;
    @Key("Date")
    private List<String> date;
    @Key("ETag")
    private List<String> etag;
    @Key("Expires")
    private List<String> expires;
    @Key("If-Modified-Since")
    private List<String> ifModifiedSince;
    @Key("If-Match")
    private List<String> ifMatch;
    @Key("If-None-Match")
    private List<String> ifNoneMatch;
    @Key("If-Unmodified-Since")
    private List<String> ifUnmodifiedSince;
    @Key("If-Range")
    private List<String> ifRange;
    @Key("Last-Modified")
    private List<String> lastModified;
    @Key("Location")
    private List<String> location;
    @Key("MIME-Version")
    private List<String> mimeVersion;
    @Key("Range")
    private List<String> range;
    @Key("Retry-After")
    private List<String> retryAfter;
    @Key("User-Agent")
    private List<String> userAgent;
    @Key("WWW-Authenticate")
    private List<String> authenticate;
    @Key("Age")
    private List<Long> age;
    
    public HttpHeaders() {
        super(EnumSet.<Flags>of(Flags.IGNORE_CASE));
        this.acceptEncoding = new ArrayList<String>(Collections.<String>singleton("gzip"));
    }
    
    @Override
    public HttpHeaders clone() {
        /*SL:171*/return (HttpHeaders)super.clone();
    }
    
    @Override
    public HttpHeaders set(final String a1, final Object a2) {
        /*SL:176*/return (HttpHeaders)super.set(a1, a2);
    }
    
    public final String getAccept() {
        /*SL:185*/return this.<String>getFirstHeaderValue(this.accept);
    }
    
    public HttpHeaders setAccept(final String a1) {
        /*SL:199*/this.accept = this.<String>getAsList(a1);
        /*SL:200*/return this;
    }
    
    public final String getAcceptEncoding() {
        /*SL:209*/return this.<String>getFirstHeaderValue(this.acceptEncoding);
    }
    
    public HttpHeaders setAcceptEncoding(final String a1) {
        /*SL:222*/this.acceptEncoding = this.<String>getAsList(a1);
        /*SL:223*/return this;
    }
    
    public final String getAuthorization() {
        /*SL:232*/return this.<String>getFirstHeaderValue(this.authorization);
    }
    
    public final List<String> getAuthorizationAsList() {
        /*SL:241*/return this.authorization;
    }
    
    public HttpHeaders setAuthorization(final String a1) {
        /*SL:255*/return this.setAuthorization(this.<String>getAsList(a1));
    }
    
    public HttpHeaders setAuthorization(final List<String> a1) {
        /*SL:269*/this.authorization = a1;
        /*SL:270*/return this;
    }
    
    public final String getCacheControl() {
        /*SL:279*/return this.<String>getFirstHeaderValue(this.cacheControl);
    }
    
    public HttpHeaders setCacheControl(final String a1) {
        /*SL:293*/this.cacheControl = this.<String>getAsList(a1);
        /*SL:294*/return this;
    }
    
    public final String getContentEncoding() {
        /*SL:303*/return this.<String>getFirstHeaderValue(this.contentEncoding);
    }
    
    public HttpHeaders setContentEncoding(final String a1) {
        /*SL:317*/this.contentEncoding = this.<String>getAsList(a1);
        /*SL:318*/return this;
    }
    
    public final Long getContentLength() {
        /*SL:327*/return this.<Long>getFirstHeaderValue(this.contentLength);
    }
    
    public HttpHeaders setContentLength(final Long a1) {
        /*SL:341*/this.contentLength = this.<Long>getAsList(a1);
        /*SL:342*/return this;
    }
    
    public final String getContentMD5() {
        /*SL:351*/return this.<String>getFirstHeaderValue(this.contentMD5);
    }
    
    public HttpHeaders setContentMD5(final String a1) {
        /*SL:365*/this.contentMD5 = this.<String>getAsList(a1);
        /*SL:366*/return this;
    }
    
    public final String getContentRange() {
        /*SL:375*/return this.<String>getFirstHeaderValue(this.contentRange);
    }
    
    public HttpHeaders setContentRange(final String a1) {
        /*SL:389*/this.contentRange = this.<String>getAsList(a1);
        /*SL:390*/return this;
    }
    
    public final String getContentType() {
        /*SL:399*/return this.<String>getFirstHeaderValue(this.contentType);
    }
    
    public HttpHeaders setContentType(final String a1) {
        /*SL:413*/this.contentType = this.<String>getAsList(a1);
        /*SL:414*/return this;
    }
    
    public final String getCookie() {
        /*SL:427*/return this.<String>getFirstHeaderValue(this.cookie);
    }
    
    public HttpHeaders setCookie(final String a1) {
        /*SL:441*/this.cookie = this.<String>getAsList(a1);
        /*SL:442*/return this;
    }
    
    public final String getDate() {
        /*SL:451*/return this.<String>getFirstHeaderValue(this.date);
    }
    
    public HttpHeaders setDate(final String a1) {
        /*SL:465*/this.date = this.<String>getAsList(a1);
        /*SL:466*/return this;
    }
    
    public final String getETag() {
        /*SL:475*/return this.<String>getFirstHeaderValue(this.etag);
    }
    
    public HttpHeaders setETag(final String a1) {
        /*SL:489*/this.etag = this.<String>getAsList(a1);
        /*SL:490*/return this;
    }
    
    public final String getExpires() {
        /*SL:499*/return this.<String>getFirstHeaderValue(this.expires);
    }
    
    public HttpHeaders setExpires(final String a1) {
        /*SL:513*/this.expires = this.<String>getAsList(a1);
        /*SL:514*/return this;
    }
    
    public final String getIfModifiedSince() {
        /*SL:523*/return this.<String>getFirstHeaderValue(this.ifModifiedSince);
    }
    
    public HttpHeaders setIfModifiedSince(final String a1) {
        /*SL:537*/this.ifModifiedSince = this.<String>getAsList(a1);
        /*SL:538*/return this;
    }
    
    public final String getIfMatch() {
        /*SL:547*/return this.<String>getFirstHeaderValue(this.ifMatch);
    }
    
    public HttpHeaders setIfMatch(final String a1) {
        /*SL:561*/this.ifMatch = this.<String>getAsList(a1);
        /*SL:562*/return this;
    }
    
    public final String getIfNoneMatch() {
        /*SL:571*/return this.<String>getFirstHeaderValue(this.ifNoneMatch);
    }
    
    public HttpHeaders setIfNoneMatch(final String a1) {
        /*SL:585*/this.ifNoneMatch = this.<String>getAsList(a1);
        /*SL:586*/return this;
    }
    
    public final String getIfUnmodifiedSince() {
        /*SL:595*/return this.<String>getFirstHeaderValue(this.ifUnmodifiedSince);
    }
    
    public HttpHeaders setIfUnmodifiedSince(final String a1) {
        /*SL:609*/this.ifUnmodifiedSince = this.<String>getAsList(a1);
        /*SL:610*/return this;
    }
    
    public final String getIfRange() {
        /*SL:619*/return this.<String>getFirstHeaderValue(this.ifRange);
    }
    
    public HttpHeaders setIfRange(final String a1) {
        /*SL:633*/this.ifRange = this.<String>getAsList(a1);
        /*SL:634*/return this;
    }
    
    public final String getLastModified() {
        /*SL:643*/return this.<String>getFirstHeaderValue(this.lastModified);
    }
    
    public HttpHeaders setLastModified(final String a1) {
        /*SL:657*/this.lastModified = this.<String>getAsList(a1);
        /*SL:658*/return this;
    }
    
    public final String getLocation() {
        /*SL:667*/return this.<String>getFirstHeaderValue(this.location);
    }
    
    public HttpHeaders setLocation(final String a1) {
        /*SL:681*/this.location = this.<String>getAsList(a1);
        /*SL:682*/return this;
    }
    
    public final String getMimeVersion() {
        /*SL:691*/return this.<String>getFirstHeaderValue(this.mimeVersion);
    }
    
    public HttpHeaders setMimeVersion(final String a1) {
        /*SL:705*/this.mimeVersion = this.<String>getAsList(a1);
        /*SL:706*/return this;
    }
    
    public final String getRange() {
        /*SL:715*/return this.<String>getFirstHeaderValue(this.range);
    }
    
    public HttpHeaders setRange(final String a1) {
        /*SL:729*/this.range = this.<String>getAsList(a1);
        /*SL:730*/return this;
    }
    
    public final String getRetryAfter() {
        /*SL:739*/return this.<String>getFirstHeaderValue(this.retryAfter);
    }
    
    public HttpHeaders setRetryAfter(final String a1) {
        /*SL:753*/this.retryAfter = this.<String>getAsList(a1);
        /*SL:754*/return this;
    }
    
    public final String getUserAgent() {
        /*SL:763*/return this.<String>getFirstHeaderValue(this.userAgent);
    }
    
    public HttpHeaders setUserAgent(final String a1) {
        /*SL:777*/this.userAgent = this.<String>getAsList(a1);
        /*SL:778*/return this;
    }
    
    public final String getAuthenticate() {
        /*SL:787*/return this.<String>getFirstHeaderValue(this.authenticate);
    }
    
    public final List<String> getAuthenticateAsList() {
        /*SL:796*/return this.authenticate;
    }
    
    public HttpHeaders setAuthenticate(final String a1) {
        /*SL:810*/this.authenticate = this.<String>getAsList(a1);
        /*SL:811*/return this;
    }
    
    public final Long getAge() {
        /*SL:820*/return this.<Long>getFirstHeaderValue(this.age);
    }
    
    public HttpHeaders setAge(final Long a1) {
        /*SL:834*/this.age = this.<Long>getAsList(a1);
        /*SL:835*/return this;
    }
    
    public HttpHeaders setBasicAuthentication(final String a1, final String a2) {
        final String v1 = /*EL:851*/Preconditions.<String>checkNotNull(a1) + ":" + Preconditions.<String>checkNotNull(a2);
        final String v2 = /*EL:852*/Base64.encodeBase64String(StringUtils.getBytesUtf8(v1));
        /*SL:853*/return this.setAuthorization("Basic " + v2);
    }
    
    private static void addHeader(final Logger a1, final StringBuilder a2, final StringBuilder a3, final LowLevelHttpRequest a4, final String a5, final Object a6, final Writer a7) throws IOException {
        /*SL:864*/if (a6 == null || Data.isNull(a6)) {
            /*SL:865*/return;
        }
        String v2;
        final String v1 = /*EL:870*/v2 = toStringValue(a6);
        /*SL:871*/if (("Authorization".equalsIgnoreCase(a5) || "Cookie".equalsIgnoreCase(a5)) && (a1 == null || !a1.isLoggable(Level.ALL))) {
            /*SL:873*/v2 = "<Not Logged>";
        }
        /*SL:875*/if (a2 != null) {
            /*SL:876*/a2.append(a5).append(": ");
            /*SL:877*/a2.append(v2);
            /*SL:878*/a2.append(StringUtils.LINE_SEPARATOR);
        }
        /*SL:880*/if (a3 != null) {
            /*SL:881*/a3.append(" -H '").append(a5).append(": ").append(v2).append("'");
        }
        /*SL:884*/if (a4 != null) {
            /*SL:885*/a4.addHeader(a5, v1);
        }
        /*SL:888*/if (a7 != null) {
            /*SL:889*/a7.write(a5);
            /*SL:890*/a7.write(": ");
            /*SL:891*/a7.write(v1);
            /*SL:892*/a7.write("\r\n");
        }
    }
    
    private static String toStringValue(final Object a1) {
        /*SL:900*/return (a1 instanceof Enum) ? /*EL:901*/FieldInfo.of((Enum<?>)a1).getName() : a1.toString();
    }
    
    static void serializeHeaders(final HttpHeaders a1, final StringBuilder a2, final StringBuilder a3, final Logger a4, final LowLevelHttpRequest a5) throws IOException {
        serializeHeaders(/*EL:917*/a1, a2, a3, a4, a5, null);
    }
    
    public static void serializeHeadersForMultipartRequests(final HttpHeaders a1, final StringBuilder a2, final Logger a3, final Writer a4) throws IOException {
        serializeHeaders(/*EL:933*/a1, a2, null, a3, null, a4);
    }
    
    static void serializeHeaders(final HttpHeaders v-7, final StringBuilder v-6, final StringBuilder v-5, final Logger v-4, final LowLevelHttpRequest v-3, final Writer v-2) throws IOException {
        final HashSet<String> set = /*EL:942*/new HashSet<String>();
        /*SL:943*/for (final Map.Entry<String, Object> v1 : v-7.entrySet()) {
            String a5 = /*EL:944*/v1.getKey();
            /*SL:945*/Preconditions.checkArgument(set.add(a5), "multiple headers of the same name (headers are case insensitive): %s", a5);
            final Object a2 = /*EL:947*/v1.getValue();
            /*SL:948*/if (a2 != null) {
                String a3 = /*EL:950*/a5;
                final FieldInfo a4 = /*EL:951*/v-7.getClassInfo().getFieldInfo(a5);
                /*SL:952*/if (a4 != null) {
                    /*SL:953*/a3 = a4.getName();
                }
                /*SL:955*/a5 = a2.getClass();
                /*SL:956*/if (a2 instanceof Iterable || a5.isArray()) {
                    /*SL:957*/for (final Object a6 : Types.<Object>iterableOf(a2)) {
                        addHeader(/*EL:958*/v-4, v-6, v-5, v-3, a3, a6, v-2);
                    }
                }
                else {
                    addHeader(/*EL:967*/v-4, v-6, v-5, v-3, a3, a2, v-2);
                }
            }
        }
        /*SL:971*/if (v-2 != null) {
            /*SL:972*/v-2.flush();
        }
    }
    
    public final void fromHttpResponse(final LowLevelHttpResponse v1, final StringBuilder v2) throws IOException {
        /*SL:986*/this.clear();
        final ParseHeaderState v3 = /*EL:987*/new ParseHeaderState(this, v2);
        /*SL:989*/for (int v4 = v1.getHeaderCount(), a1 = 0; a1 < v4; ++a1) {
            /*SL:990*/this.parseHeader(v1.getHeaderName(a1), v1.getHeaderValue(a1), v3);
        }
        /*SL:992*/v3.finish();
    }
    
    private <T> T getFirstHeaderValue(final List<T> a1) {
        /*SL:1018*/return (a1 == null) ? null : a1.get(0);
    }
    
    private <T> List<T> getAsList(final T a1) {
        /*SL:1023*/if (a1 == null) {
            /*SL:1024*/return null;
        }
        final List<T> v1 = /*EL:1026*/new ArrayList<T>();
        /*SL:1027*/v1.add(a1);
        /*SL:1028*/return v1;
    }
    
    public String getFirstHeaderStringValue(final String v2) {
        final Object v3 = /*EL:1039*/this.get(v2.toLowerCase(Locale.US));
        /*SL:1040*/if (v3 == null) {
            /*SL:1041*/return null;
        }
        final Class<?> v4 = /*EL:1043*/v3.getClass();
        /*SL:1044*/if (v3 instanceof Iterable || v4.isArray()) {
            final Iterator<Object> iterator = /*EL:1045*/Types.<Object>iterableOf(v3).iterator();
            if (iterator.hasNext()) {
                final Object a1 = iterator.next();
                /*SL:1046*/return toStringValue(a1);
            }
        }
        /*SL:1049*/return toStringValue(v3);
    }
    
    public List<String> getHeaderStringValues(final String v-2) {
        final Object value = /*EL:1060*/this.get(v-2.toLowerCase(Locale.US));
        /*SL:1061*/if (value == null) {
            /*SL:1062*/return Collections.<String>emptyList();
        }
        final Class<?> v0 = /*EL:1064*/value.getClass();
        /*SL:1065*/if (value instanceof Iterable || v0.isArray()) {
            final List<String> v = /*EL:1066*/new ArrayList<String>();
            /*SL:1067*/for (final Object a1 : Types.<Object>iterableOf(value)) {
                /*SL:1068*/v.add(toStringValue(a1));
            }
            /*SL:1070*/return Collections.<String>unmodifiableList((List<? extends String>)v);
        }
        /*SL:1072*/return Collections.<String>singletonList(toStringValue(value));
    }
    
    public final void fromHttpHeaders(final HttpHeaders v0) {
        try {
            final ParseHeaderState a1 = /*EL:1083*/new ParseHeaderState(this, null);
            serializeHeaders(/*EL:1084*/v0, null, null, null, new HeaderParsingFakeLevelHttpRequest(this, a1));
            /*SL:1086*/a1.finish();
        }
        catch (IOException v) {
            /*SL:1089*/throw Throwables.propagate(v);
        }
    }
    
    void parseHeader(final String v-7, final String v-6, final ParseHeaderState v-5) {
        final List<Type> context = /*EL:1131*/v-5.context;
        final ClassInfo classInfo = /*EL:1132*/v-5.classInfo;
        final ArrayValueMap arrayValueMap = /*EL:1133*/v-5.arrayValueMap;
        final StringBuilder logger = /*EL:1134*/v-5.logger;
        /*SL:1136*/if (logger != null) {
            /*SL:1137*/logger.append(v-7 + ": " + v-6).append(StringUtils.LINE_SEPARATOR);
        }
        final FieldInfo v0 = /*EL:1140*/classInfo.getFieldInfo(v-7);
        /*SL:1141*/if (v0 != null) {
            final Type v = /*EL:1142*/Data.resolveWildcardTypeOrTypeVariable(context, v0.getGenericType());
            /*SL:1144*/if (Types.isArray(v)) {
                final Class<?> a1 = /*EL:1147*/Types.getRawArrayComponentType(context, Types.getArrayComponentType(v));
                /*SL:1148*/arrayValueMap.put(v0.getField(), a1, parseValue(a1, context, v-6));
            }
            else/*SL:1150*/ if (Types.isAssignableToOrFrom(/*EL:1151*/Types.getRawArrayComponentType(context, v), Iterable.class)) {
                Collection<Object> a2 = /*EL:1154*/(Collection<Object>)v0.getValue(this);
                /*SL:1155*/if (a2 == null) {
                    /*SL:1156*/a2 = Data.newCollectionInstance(v);
                    /*SL:1157*/v0.setValue(this, a2);
                }
                final Type a3 = /*EL:1159*/(v == Object.class) ? null : Types.getIterableParameter(v);
                /*SL:1160*/a2.add(parseValue(a3, context, v-6));
            }
            else {
                /*SL:1163*/v0.setValue(this, parseValue(v, context, v-6));
            }
        }
        else {
            ArrayList<String> v2 = /*EL:1168*/(ArrayList<String>)this.get(v-7);
            /*SL:1169*/if (v2 == null) {
                /*SL:1170*/v2 = new ArrayList<String>();
                /*SL:1171*/this.set(v-7, v2);
            }
            /*SL:1173*/v2.add(v-6);
        }
    }
    
    private static Object parseValue(final Type a1, final List<Type> a2, final String a3) {
        final Type v1 = /*EL:1178*/Data.resolveWildcardTypeOrTypeVariable(a2, a1);
        /*SL:1179*/return Data.parsePrimitiveValue(v1, a3);
    }
    
    private static class HeaderParsingFakeLevelHttpRequest extends LowLevelHttpRequest
    {
        private final HttpHeaders target;
        private final ParseHeaderState state;
        
        HeaderParsingFakeLevelHttpRequest(final HttpHeaders a1, final ParseHeaderState a2) {
            this.target = a1;
            this.state = a2;
        }
        
        @Override
        public void addHeader(final String a1, final String a2) {
            /*SL:1007*/this.target.parseHeader(a1, a2, this.state);
        }
        
        @Override
        public LowLevelHttpResponse execute() throws IOException {
            /*SL:1012*/throw new UnsupportedOperationException();
        }
    }
    
    private static final class ParseHeaderState
    {
        final ArrayValueMap arrayValueMap;
        final StringBuilder logger;
        final ClassInfo classInfo;
        final List<Type> context;
        
        public ParseHeaderState(final HttpHeaders a1, final StringBuilder a2) {
            final Class<? extends HttpHeaders> v1 = a1.getClass();
            this.context = Arrays.<Type>asList(v1);
            this.classInfo = ClassInfo.of(v1, true);
            this.logger = a2;
            this.arrayValueMap = new ArrayValueMap(a1);
        }
        
        void finish() {
            /*SL:1125*/this.arrayValueMap.setValues();
        }
    }
}

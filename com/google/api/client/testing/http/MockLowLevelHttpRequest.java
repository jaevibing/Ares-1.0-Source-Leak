package com.google.api.client.testing.http;

import java.nio.charset.Charset;
import com.google.api.client.util.Charsets;
import com.google.api.client.http.HttpMediaType;
import com.google.api.client.util.IOUtils;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;
import java.io.ByteArrayInputStream;
import java.io.OutputStream;
import java.io.ByteArrayOutputStream;
import java.util.Collections;
import com.google.api.client.http.LowLevelHttpResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.google.api.client.util.Beta;
import com.google.api.client.http.LowLevelHttpRequest;

@Beta
public class MockLowLevelHttpRequest extends LowLevelHttpRequest
{
    private String url;
    private final Map<String, List<String>> headersMap;
    private MockLowLevelHttpResponse response;
    
    public MockLowLevelHttpRequest() {
        this.headersMap = new HashMap<String, List<String>>();
        this.response = new MockLowLevelHttpResponse();
    }
    
    public MockLowLevelHttpRequest(final String a1) {
        this.headersMap = new HashMap<String, List<String>>();
        this.response = new MockLowLevelHttpResponse();
        this.url = a1;
    }
    
    @Override
    public void addHeader(String a1, final String a2) throws IOException {
        /*SL:79*/a1 = a1.toLowerCase(Locale.US);
        List<String> v1 = /*EL:80*/this.headersMap.get(a1);
        /*SL:81*/if (v1 == null) {
            /*SL:82*/v1 = new ArrayList<String>();
            /*SL:83*/this.headersMap.put(a1, v1);
        }
        /*SL:85*/v1.add(a2);
    }
    
    @Override
    public LowLevelHttpResponse execute() throws IOException {
        /*SL:90*/return this.response;
    }
    
    public String getUrl() {
        /*SL:99*/return this.url;
    }
    
    public Map<String, List<String>> getHeaders() {
        /*SL:113*/return Collections.<String, List<String>>unmodifiableMap((Map<? extends String, ? extends List<String>>)this.headersMap);
    }
    
    public String getFirstHeaderValue(final String a1) {
        final List<String> v1 = /*EL:123*/this.headersMap.get(a1.toLowerCase(Locale.US));
        /*SL:124*/return (v1 == null) ? null : v1.get(0);
    }
    
    public List<String> getHeaderValues(final String a1) {
        final List<String> v1 = /*EL:134*/this.headersMap.get(a1.toLowerCase(Locale.US));
        /*SL:135*/return (v1 == null) ? Collections.<String>emptyList() : Collections.<String>unmodifiableList((List<? extends String>)v1);
    }
    
    public MockLowLevelHttpRequest setUrl(final String a1) {
        /*SL:144*/this.url = a1;
        /*SL:145*/return this;
    }
    
    public String getContentAsString() throws IOException {
        /*SL:158*/if (this.getStreamingContent() == null) {
            /*SL:159*/return "";
        }
        ByteArrayOutputStream a2 = /*EL:162*/new ByteArrayOutputStream();
        /*SL:163*/this.getStreamingContent().writeTo(a2);
        final String v0 = /*EL:165*/this.getContentEncoding();
        /*SL:166*/if (v0 != null && v0.contains("gzip")) {
            final InputStream v = /*EL:167*/new GZIPInputStream(/*EL:168*/new ByteArrayInputStream(a2.toByteArray()));
            /*SL:169*/a2 = new ByteArrayOutputStream();
            /*SL:170*/IOUtils.copy(v, a2);
        }
        final String v2 = /*EL:173*/this.getContentType();
        final HttpMediaType v3 = /*EL:174*/(v2 != null) ? new HttpMediaType(v2) : null;
        final Charset v4 = /*EL:175*/(v3 == null || v3.getCharsetParameter() == null) ? Charsets.ISO_8859_1 : v3.getCharsetParameter();
        /*SL:177*/return a2.toString(v4.name());
    }
    
    public MockLowLevelHttpResponse getResponse() {
        /*SL:186*/return this.response;
    }
    
    public MockLowLevelHttpRequest setResponse(final MockLowLevelHttpResponse a1) {
        /*SL:197*/this.response = a1;
        /*SL:198*/return this;
    }
}

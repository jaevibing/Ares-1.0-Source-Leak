package com.google.api.client.http;

import com.google.api.client.util.Preconditions;
import java.util.Collections;
import java.util.Collection;
import java.io.IOException;
import java.util.Iterator;
import java.io.Writer;
import java.util.logging.Logger;
import com.google.api.client.util.StreamingContent;
import java.util.Arrays;
import java.io.OutputStreamWriter;
import java.io.OutputStream;
import java.util.ArrayList;

public class MultipartContent extends AbstractHttpContent
{
    static final String NEWLINE = "\r\n";
    private static final String TWO_DASHES = "--";
    private ArrayList<Part> parts;
    
    public MultipartContent() {
        super(new HttpMediaType("multipart/related").setParameter("boundary", "__END_OF_PART__"));
        this.parts = new ArrayList<Part>();
    }
    
    @Override
    public void writeTo(final OutputStream v-7) throws IOException {
        final Writer a2 = /*EL:62*/new OutputStreamWriter(v-7, this.getCharset());
        final String boundary = /*EL:63*/this.getBoundary();
        /*SL:64*/for (final Part part : this.parts) {
            final HttpHeaders setAcceptEncoding = /*EL:65*/new HttpHeaders().setAcceptEncoding(null);
            /*SL:66*/if (part.headers != null) {
                /*SL:67*/setAcceptEncoding.fromHttpHeaders(part.headers);
            }
            /*SL:69*/setAcceptEncoding.setContentEncoding(null).setUserAgent(null).setContentType(/*EL:70*/null).setContentLength(/*EL:71*/null).set(/*EL:72*/"Content-Transfer-Encoding", null);
            final HttpContent content = /*EL:75*/part.content;
            StreamingContent v0 = /*EL:76*/null;
            /*SL:77*/if (content != null) {
                /*SL:78*/setAcceptEncoding.set("Content-Transfer-Encoding", Arrays.<String>asList("binary"));
                /*SL:79*/setAcceptEncoding.setContentType(content.getType());
                final HttpEncoding v = /*EL:80*/part.encoding;
                final long v2;
                /*SL:82*/if (v == null) {
                    final long a1 = /*EL:83*/content.getLength();
                    /*SL:84*/v0 = content;
                }
                else {
                    /*SL:86*/setAcceptEncoding.setContentEncoding(v.getName());
                    /*SL:87*/v0 = new HttpEncodingStreamingContent(content, v);
                    /*SL:88*/v2 = AbstractHttpContent.computeLength(content);
                }
                /*SL:90*/if (v2 != -1L) {
                    /*SL:91*/setAcceptEncoding.setContentLength(v2);
                }
            }
            /*SL:97*/a2.write("--");
            /*SL:98*/a2.write(boundary);
            /*SL:99*/a2.write("\r\n");
            /*SL:102*/HttpHeaders.serializeHeadersForMultipartRequests(setAcceptEncoding, null, null, a2);
            /*SL:103*/if (v0 != null) {
                /*SL:104*/a2.write("\r\n");
                /*SL:105*/a2.flush();
                /*SL:107*/v0.writeTo(v-7);
            }
            /*SL:110*/a2.write("\r\n");
        }
        /*SL:113*/a2.write("--");
        /*SL:114*/a2.write(boundary);
        /*SL:115*/a2.write("--");
        /*SL:116*/a2.write("\r\n");
        /*SL:117*/a2.flush();
    }
    
    @Override
    public boolean retrySupported() {
        /*SL:122*/for (final Part v1 : this.parts) {
            /*SL:123*/if (!v1.content.retrySupported()) {
                /*SL:124*/return false;
            }
        }
        /*SL:127*/return true;
    }
    
    @Override
    public MultipartContent setMediaType(final HttpMediaType a1) {
        /*SL:132*/super.setMediaType(a1);
        /*SL:133*/return this;
    }
    
    public final Collection<Part> getParts() {
        /*SL:138*/return Collections.<Part>unmodifiableCollection((Collection<? extends Part>)this.parts);
    }
    
    public MultipartContent addPart(final Part a1) {
        /*SL:150*/this.parts.add(Preconditions.<Part>checkNotNull(a1));
        /*SL:151*/return this;
    }
    
    public MultipartContent setParts(final Collection<Part> a1) {
        /*SL:163*/this.parts = new ArrayList<Part>(a1);
        /*SL:164*/return this;
    }
    
    public MultipartContent setContentParts(final Collection<? extends HttpContent> v2) {
        /*SL:177*/this.parts = new ArrayList<Part>(v2.size());
        /*SL:178*/for (final HttpContent a1 : v2) {
            /*SL:179*/this.addPart(new Part(a1));
        }
        /*SL:181*/return this;
    }
    
    public final String getBoundary() {
        /*SL:186*/return this.getMediaType().getParameter("boundary");
    }
    
    public MultipartContent setBoundary(final String a1) {
        /*SL:202*/this.getMediaType().setParameter("boundary", Preconditions.<String>checkNotNull(a1));
        /*SL:203*/return this;
    }
    
    public static final class Part
    {
        HttpContent content;
        HttpHeaders headers;
        HttpEncoding encoding;
        
        public Part() {
            this(null);
        }
        
        public Part(final HttpContent a1) {
            this(null, a1);
        }
        
        public Part(final HttpHeaders a1, final HttpContent a2) {
            this.setHeaders(a1);
            this.setContent(a2);
        }
        
        public Part setContent(final HttpContent a1) {
            /*SL:246*/this.content = a1;
            /*SL:247*/return this;
        }
        
        public HttpContent getContent() {
            /*SL:252*/return this.content;
        }
        
        public Part setHeaders(final HttpHeaders a1) {
            /*SL:257*/this.headers = a1;
            /*SL:258*/return this;
        }
        
        public HttpHeaders getHeaders() {
            /*SL:263*/return this.headers;
        }
        
        public Part setEncoding(final HttpEncoding a1) {
            /*SL:268*/this.encoding = a1;
            /*SL:269*/return this;
        }
        
        public HttpEncoding getEncoding() {
            /*SL:274*/return this.encoding;
        }
    }
}

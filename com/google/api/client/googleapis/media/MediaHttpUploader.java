package com.google.api.client.googleapis.media;

import com.google.api.client.util.Beta;
import com.google.api.client.http.ByteArrayContent;
import com.google.api.client.http.InputStreamContent;
import com.google.api.client.util.ByteStreams;
import com.google.api.client.http.HttpEncoding;
import com.google.api.client.http.GZipEncoding;
import com.google.api.client.googleapis.MethodOverride;
import com.google.api.client.http.EmptyContent;
import java.io.BufferedInputStream;
import java.util.Map;
import java.util.Collection;
import java.util.Arrays;
import com.google.api.client.http.MultipartContent;
import java.io.IOException;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.util.Preconditions;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.util.Sleeper;
import java.io.InputStream;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpHeaders;
import com.google.api.client.http.HttpContent;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.AbstractInputStreamContent;

public final class MediaHttpUploader
{
    public static final String CONTENT_LENGTH_HEADER = "X-Upload-Content-Length";
    public static final String CONTENT_TYPE_HEADER = "X-Upload-Content-Type";
    private UploadState uploadState;
    static final int MB = 1048576;
    private static final int KB = 1024;
    public static final int MINIMUM_CHUNK_SIZE = 262144;
    public static final int DEFAULT_CHUNK_SIZE = 10485760;
    private final AbstractInputStreamContent mediaContent;
    private final HttpRequestFactory requestFactory;
    private final HttpTransport transport;
    private HttpContent metadata;
    private long mediaContentLength;
    private boolean isMediaContentLengthCalculated;
    private String initiationRequestMethod;
    private HttpHeaders initiationHeaders;
    private HttpRequest currentRequest;
    private InputStream contentInputStream;
    private boolean directUploadEnabled;
    private MediaHttpUploaderProgressListener progressListener;
    String mediaContentLengthStr;
    private long totalBytesServerReceived;
    private int chunkSize;
    private Byte cachedByte;
    private long totalBytesClientSent;
    private int currentChunkLength;
    private byte[] currentRequestContentBuffer;
    private boolean disableGZipContent;
    Sleeper sleeper;
    
    public MediaHttpUploader(final AbstractInputStreamContent a1, final HttpTransport a2, final HttpRequestInitializer a3) {
        this.uploadState = UploadState.NOT_STARTED;
        this.initiationRequestMethod = "POST";
        this.initiationHeaders = new HttpHeaders();
        this.mediaContentLengthStr = "*";
        this.chunkSize = 10485760;
        this.sleeper = Sleeper.DEFAULT;
        this.mediaContent = Preconditions.<AbstractInputStreamContent>checkNotNull(a1);
        this.transport = Preconditions.<HttpTransport>checkNotNull(a2);
        this.requestFactory = ((a3 == null) ? a2.createRequestFactory() : a2.createRequestFactory(a3));
    }
    
    public HttpResponse upload(final GenericUrl a1) throws IOException {
        /*SL:331*/Preconditions.checkArgument(this.uploadState == UploadState.NOT_STARTED);
        /*SL:333*/if (this.directUploadEnabled) {
            /*SL:334*/return this.directUpload(a1);
        }
        /*SL:336*/return this.resumableUpload(a1);
    }
    
    private HttpResponse directUpload(final GenericUrl a1) throws IOException {
        /*SL:346*/this.updateStateAndNotifyListener(UploadState.MEDIA_IN_PROGRESS);
        HttpContent v1 = /*EL:348*/this.mediaContent;
        /*SL:349*/if (this.metadata != null) {
            /*SL:350*/v1 = new MultipartContent().setContentParts(Arrays.<HttpContent>asList(this.metadata, this.mediaContent));
            /*SL:351*/a1.put("uploadType", "multipart");
        }
        else {
            /*SL:353*/a1.put("uploadType", "media");
        }
        final HttpRequest v2 = /*EL:355*/this.requestFactory.buildRequest(this.initiationRequestMethod, a1, v1);
        /*SL:357*/v2.getHeaders().putAll(this.initiationHeaders);
        final HttpResponse v3 = /*EL:360*/this.executeCurrentRequest(v2);
        boolean v4 = /*EL:361*/false;
        try {
            /*SL:363*/if (this.isMediaLengthKnown()) {
                /*SL:364*/this.totalBytesServerReceived = this.getMediaContentLength();
            }
            /*SL:366*/this.updateStateAndNotifyListener(UploadState.MEDIA_COMPLETE);
            /*SL:367*/v4 = true;
        }
        finally {
            /*SL:369*/if (!v4) {
                /*SL:370*/v3.disconnect();
            }
        }
        /*SL:373*/return v3;
    }
    
    private HttpResponse resumableUpload(final GenericUrl v-2) throws IOException {
        final HttpResponse executeUploadInitiation = /*EL:384*/this.executeUploadInitiation(v-2);
        /*SL:385*/if (!executeUploadInitiation.isSuccessStatusCode()) {
            /*SL:387*/return executeUploadInitiation;
        }
        try {
            final GenericUrl a1 = /*EL:391*/new GenericUrl(executeUploadInitiation.getHeaders().getLocation());
        }
        finally {
            /*SL:393*/executeUploadInitiation.disconnect();
        }
        /*SL:397*/this.contentInputStream = this.mediaContent.getInputStream();
        /*SL:398*/if (!this.contentInputStream.markSupported() && this.isMediaLengthKnown()) {
            /*SL:402*/this.contentInputStream = new BufferedInputStream(this.contentInputStream);
        }
        while (true) {
            GenericUrl v0 = null;
            /*SL:408*/this.currentRequest = this.requestFactory.buildPutRequest(v0, null);
            /*SL:409*/this.setContentAndHeadersOnCurrentRequest();
            /*SL:412*/new MediaUploadErrorHandler(this, this.currentRequest);
            HttpResponse v;
            /*SL:414*/if (this.isMediaLengthKnown()) {
                /*SL:417*/v = this.executeCurrentRequestWithoutGZip(this.currentRequest);
            }
            else {
                /*SL:419*/v = this.executeCurrentRequest(this.currentRequest);
            }
            boolean v2 = /*EL:421*/false;
            try {
                /*SL:423*/if (v.isSuccessStatusCode()) {
                    /*SL:424*/this.totalBytesServerReceived = this.getMediaContentLength();
                    /*SL:425*/if (this.mediaContent.getCloseInputStream()) {
                        /*SL:426*/this.contentInputStream.close();
                    }
                    /*SL:428*/this.updateStateAndNotifyListener(UploadState.MEDIA_COMPLETE);
                    /*SL:429*/v2 = true;
                    /*SL:474*/return v;
                }
                if (v.getStatusCode() != 308) {
                    v2 = true;
                    return v;
                }
                final String v3 = v.getHeaders().getLocation();
                if (v3 != null) {
                    v0 = new GenericUrl(v3);
                }
                final long v4 = this.getNextByteIndex(v.getHeaders().getRange());
                final long v5 = v4 - this.totalBytesServerReceived;
                Preconditions.checkState(v5 >= 0L && v5 <= this.currentChunkLength);
                final long v6 = this.currentChunkLength - v5;
                if (this.isMediaLengthKnown()) {
                    if (v6 > 0L) {
                        this.contentInputStream.reset();
                        final long v7 = this.contentInputStream.skip(v5);
                        Preconditions.checkState(v5 == v7);
                    }
                }
                else if (v6 == 0L) {
                    this.currentRequestContentBuffer = null;
                }
                this.totalBytesServerReceived = v4;
                this.updateStateAndNotifyListener(UploadState.MEDIA_IN_PROGRESS);
            }
            finally {
                if (!v2) {
                    v.disconnect();
                }
            }
        }
    }
    
    private boolean isMediaLengthKnown() throws IOException {
        /*SL:484*/return this.getMediaContentLength() >= 0L;
    }
    
    private long getMediaContentLength() throws IOException {
        /*SL:495*/if (!this.isMediaContentLengthCalculated) {
            /*SL:496*/this.mediaContentLength = this.mediaContent.getLength();
            /*SL:497*/this.isMediaContentLengthCalculated = true;
        }
        /*SL:499*/return this.mediaContentLength;
    }
    
    private HttpResponse executeUploadInitiation(final GenericUrl a1) throws IOException {
        /*SL:508*/this.updateStateAndNotifyListener(UploadState.INITIATION_STARTED);
        /*SL:510*/a1.put("uploadType", "resumable");
        final HttpContent v1 = /*EL:511*/(this.metadata == null) ? new EmptyContent() : this.metadata;
        final HttpRequest v2 = /*EL:512*/this.requestFactory.buildRequest(this.initiationRequestMethod, a1, v1);
        /*SL:514*/this.initiationHeaders.set("X-Upload-Content-Type", this.mediaContent.getType());
        /*SL:515*/if (this.isMediaLengthKnown()) {
            /*SL:516*/this.initiationHeaders.set("X-Upload-Content-Length", this.getMediaContentLength());
        }
        /*SL:518*/v2.getHeaders().putAll(this.initiationHeaders);
        final HttpResponse v3 = /*EL:519*/this.executeCurrentRequest(v2);
        boolean v4 = /*EL:520*/false;
        try {
            /*SL:523*/this.updateStateAndNotifyListener(UploadState.INITIATION_COMPLETE);
            /*SL:524*/v4 = true;
        }
        finally {
            /*SL:526*/if (!v4) {
                /*SL:527*/v3.disconnect();
            }
        }
        /*SL:530*/return v3;
    }
    
    private HttpResponse executeCurrentRequestWithoutGZip(final HttpRequest a1) throws IOException {
        /*SL:541*/new MethodOverride().intercept(a1);
        /*SL:543*/a1.setThrowExceptionOnExecuteError(false);
        final HttpResponse v1 = /*EL:545*/a1.execute();
        /*SL:546*/return v1;
    }
    
    private HttpResponse executeCurrentRequest(final HttpRequest a1) throws IOException {
        /*SL:558*/if (!this.disableGZipContent && !(a1.getContent() instanceof EmptyContent)) {
            /*SL:559*/a1.setEncoding(new GZipEncoding());
        }
        final HttpResponse v1 = /*EL:562*/this.executeCurrentRequestWithoutGZip(a1);
        /*SL:563*/return v1;
    }
    
    private void setContentAndHeadersOnCurrentRequest() throws IOException {
        int v1;
        /*SL:572*/if (this.isMediaLengthKnown()) {
            /*SL:574*/v1 = (int)Math.min(this.chunkSize, this.getMediaContentLength() - this.totalBytesServerReceived);
        }
        else {
            /*SL:577*/v1 = this.chunkSize;
        }
        int v2 = /*EL:581*/v1;
        AbstractInputStreamContent v4;
        /*SL:582*/if (this.isMediaLengthKnown()) {
            /*SL:584*/this.contentInputStream.mark(v1);
            final InputStream v3 = /*EL:586*/ByteStreams.limit(this.contentInputStream, v1);
            /*SL:587*/v4 = new InputStreamContent(this.mediaContent.getType(), v3).setRetrySupported(true).setLength(v1).setCloseInputStream(false);
            /*SL:590*/this.mediaContentLengthStr = String.valueOf(this.getMediaContentLength());
        }
        else {
            int v5 = /*EL:600*/0;
            int v6;
            /*SL:601*/if (this.currentRequestContentBuffer == null) {
                /*SL:602*/v6 = ((this.cachedByte == null) ? (v1 + 1) : v1);
                /*SL:603*/this.currentRequestContentBuffer = new byte[v1 + 1];
                /*SL:604*/if (this.cachedByte != null) {
                    /*SL:605*/this.currentRequestContentBuffer[0] = this.cachedByte;
                }
            }
            else {
                /*SL:616*/v5 = (int)(this.totalBytesClientSent - this.totalBytesServerReceived);
                /*SL:619*/System.arraycopy(this.currentRequestContentBuffer, this.currentChunkLength - v5, this.currentRequestContentBuffer, 0, v5);
                /*SL:621*/if (this.cachedByte != null) {
                    /*SL:623*/this.currentRequestContentBuffer[v5] = this.cachedByte;
                }
                /*SL:626*/v6 = v1 - v5;
            }
            final int v7 = /*EL:629*/ByteStreams.read(this.contentInputStream, this.currentRequestContentBuffer, v1 + 1 - v6, v6);
            /*SL:633*/if (v7 < v6) {
                /*SL:634*/v2 = v5 + Math.max(0, v7);
                /*SL:635*/if (this.cachedByte != null) {
                    /*SL:636*/++v2;
                    /*SL:637*/this.cachedByte = null;
                }
                /*SL:640*/if (this.mediaContentLengthStr.equals("*")) {
                    /*SL:643*/this.mediaContentLengthStr = String.valueOf(this.totalBytesServerReceived + v2);
                }
            }
            else {
                /*SL:646*/this.cachedByte = this.currentRequestContentBuffer[v1];
            }
            /*SL:649*/v4 = new ByteArrayContent(this.mediaContent.getType(), this.currentRequestContentBuffer, 0, v2);
            /*SL:651*/this.totalBytesClientSent = this.totalBytesServerReceived + v2;
        }
        /*SL:654*/this.currentChunkLength = v2;
        /*SL:655*/this.currentRequest.setContent(v4);
        /*SL:656*/if (v2 == 0) {
            final HttpHeaders headers = /*EL:660*/this.currentRequest.getHeaders();
            final String s = "bytes */";
            final String value = String.valueOf(this.mediaContentLengthStr);
            headers.setContentRange((value.length() != 0) ? s.concat(value) : new String(s));
        }
        else {
            final HttpHeaders headers2 = /*EL:662*/this.currentRequest.getHeaders();
            final long totalBytesServerReceived = this.totalBytesServerReceived;
            final long n = this.totalBytesServerReceived + v2 - 1L;
            final String value2 = String.valueOf(String.valueOf(this.mediaContentLengthStr));
            headers2.setContentRange(new StringBuilder(48 + value2.length()).append("bytes ").append(totalBytesServerReceived).append("-").append(n).append("/").append(value2).toString());
        }
    }
    
    @Beta
    void serverErrorCallback() throws IOException {
        /*SL:679*/Preconditions.<HttpRequest>checkNotNull(this.currentRequest, (Object)"The current request should not be null");
        /*SL:682*/this.currentRequest.setContent(new EmptyContent());
        final HttpHeaders headers = /*EL:683*/this.currentRequest.getHeaders();
        final String s = "bytes */";
        final String value = String.valueOf(this.mediaContentLengthStr);
        headers.setContentRange((value.length() != 0) ? s.concat(value) : new String(s));
    }
    
    private long getNextByteIndex(final String a1) {
        /*SL:695*/if (a1 == null) {
            /*SL:696*/return 0L;
        }
        /*SL:698*/return Long.parseLong(a1.substring(a1.indexOf(45) + 1)) + 1L;
    }
    
    public HttpContent getMetadata() {
        /*SL:703*/return this.metadata;
    }
    
    public MediaHttpUploader setMetadata(final HttpContent a1) {
        /*SL:708*/this.metadata = a1;
        /*SL:709*/return this;
    }
    
    public HttpContent getMediaContent() {
        /*SL:714*/return this.mediaContent;
    }
    
    public HttpTransport getTransport() {
        /*SL:719*/return this.transport;
    }
    
    public MediaHttpUploader setDirectUploadEnabled(final boolean a1) {
        /*SL:745*/this.directUploadEnabled = a1;
        /*SL:746*/return this;
    }
    
    public boolean isDirectUploadEnabled() {
        /*SL:758*/return this.directUploadEnabled;
    }
    
    public MediaHttpUploader setProgressListener(final MediaHttpUploaderProgressListener a1) {
        /*SL:765*/this.progressListener = a1;
        /*SL:766*/return this;
    }
    
    public MediaHttpUploaderProgressListener getProgressListener() {
        /*SL:773*/return this.progressListener;
    }
    
    public MediaHttpUploader setChunkSize(final int a1) {
        /*SL:786*/Preconditions.checkArgument(a1 > 0 && a1 % 262144 == 0, (Object)"chunkSize must be a positive multiple of 262144.");
        /*SL:788*/this.chunkSize = a1;
        /*SL:789*/return this;
    }
    
    public int getChunkSize() {
        /*SL:797*/return this.chunkSize;
    }
    
    public boolean getDisableGZipContent() {
        /*SL:806*/return this.disableGZipContent;
    }
    
    public MediaHttpUploader setDisableGZipContent(final boolean a1) {
        /*SL:826*/this.disableGZipContent = a1;
        /*SL:827*/return this;
    }
    
    public Sleeper getSleeper() {
        /*SL:836*/return this.sleeper;
    }
    
    public MediaHttpUploader setSleeper(final Sleeper a1) {
        /*SL:845*/this.sleeper = a1;
        /*SL:846*/return this;
    }
    
    public String getInitiationRequestMethod() {
        /*SL:859*/return this.initiationRequestMethod;
    }
    
    public MediaHttpUploader setInitiationRequestMethod(final String a1) {
        /*SL:873*/Preconditions.checkArgument(a1.equals("POST") || a1.equals("PUT") || a1.equals("PATCH"));
        /*SL:876*/this.initiationRequestMethod = a1;
        /*SL:877*/return this;
    }
    
    public MediaHttpUploader setInitiationHeaders(final HttpHeaders a1) {
        /*SL:882*/this.initiationHeaders = a1;
        /*SL:883*/return this;
    }
    
    public HttpHeaders getInitiationHeaders() {
        /*SL:888*/return this.initiationHeaders;
    }
    
    public long getNumBytesUploaded() {
        /*SL:898*/return this.totalBytesServerReceived;
    }
    
    private void updateStateAndNotifyListener(final UploadState a1) throws IOException {
        /*SL:907*/this.uploadState = a1;
        /*SL:908*/if (this.progressListener != null) {
            /*SL:909*/this.progressListener.progressChanged(this);
        }
    }
    
    public UploadState getUploadState() {
        /*SL:919*/return this.uploadState;
    }
    
    public double getProgress() throws IOException {
        /*SL:936*/Preconditions.checkArgument(this.isMediaLengthKnown(), (Object)"Cannot call getProgress() if the specified AbstractInputStreamContent has no content length. Use  getNumBytesUploaded() to denote progress instead.");
        /*SL:939*/return (this.getMediaContentLength() == 0L) ? 0.0 : (this.totalBytesServerReceived / this.getMediaContentLength());
    }
    
    public enum UploadState
    {
        NOT_STARTED, 
        INITIATION_STARTED, 
        INITIATION_COMPLETE, 
        MEDIA_IN_PROGRESS, 
        MEDIA_COMPLETE;
    }
}

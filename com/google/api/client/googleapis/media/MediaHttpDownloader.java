package com.google.api.client.googleapis.media;

import com.google.api.client.http.HttpRequest;
import com.google.api.client.util.IOUtils;
import java.util.Map;
import com.google.api.client.http.HttpResponse;
import java.io.IOException;
import com.google.api.client.http.HttpHeaders;
import java.io.OutputStream;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.util.Preconditions;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.HttpRequestFactory;

public final class MediaHttpDownloader
{
    public static final int MAXIMUM_CHUNK_SIZE = 33554432;
    private final HttpRequestFactory requestFactory;
    private final HttpTransport transport;
    private boolean directDownloadEnabled;
    private MediaHttpDownloaderProgressListener progressListener;
    private int chunkSize;
    private long mediaContentLength;
    private DownloadState downloadState;
    private long bytesDownloaded;
    private long lastBytePos;
    
    public MediaHttpDownloader(final HttpTransport a1, final HttpRequestInitializer a2) {
        this.directDownloadEnabled = false;
        this.chunkSize = 33554432;
        this.downloadState = DownloadState.NOT_STARTED;
        this.lastBytePos = -1L;
        this.transport = Preconditions.<HttpTransport>checkNotNull(a1);
        this.requestFactory = ((a2 == null) ? a1.createRequestFactory() : a1.createRequestFactory(a2));
    }
    
    public void download(final GenericUrl a1, final OutputStream a2) throws IOException {
        /*SL:156*/this.download(a1, null, a2);
    }
    
    public void download(final GenericUrl v-5, final HttpHeaders v-4, final OutputStream v-3) throws IOException {
        /*SL:178*/Preconditions.checkArgument(this.downloadState == DownloadState.NOT_STARTED);
        /*SL:179*/v-5.put("alt", "media");
        /*SL:181*/if (this.directDownloadEnabled) {
            /*SL:182*/this.updateStateAndNotifyListener(DownloadState.MEDIA_IN_PROGRESS);
            final HttpResponse a1 = /*EL:183*/this.executeCurrentRequest(this.lastBytePos, v-5, v-4, v-3);
            /*SL:186*/this.mediaContentLength = a1.getHeaders().getContentLength();
            /*SL:187*/this.bytesDownloaded = this.mediaContentLength;
            /*SL:188*/this.updateStateAndNotifyListener(DownloadState.MEDIA_COMPLETE);
            /*SL:189*/return;
        }
        while (true) {
            long a2 = /*EL:194*/this.bytesDownloaded + this.chunkSize - 1L;
            /*SL:195*/if (this.lastBytePos != -1L) {
                /*SL:197*/a2 = Math.min(this.lastBytePos, a2);
            }
            final HttpResponse a3 = /*EL:199*/this.executeCurrentRequest(a2, v-5, v-4, v-3);
            final String v1 = /*EL:202*/a3.getHeaders().getContentRange();
            final long v2 = /*EL:203*/this.getNextByteIndex(v1);
            /*SL:204*/this.setMediaContentLength(v1);
            /*SL:206*/if (this.mediaContentLength <= v2) {
                break;
            }
            /*SL:213*/this.bytesDownloaded = v2;
            /*SL:214*/this.updateStateAndNotifyListener(DownloadState.MEDIA_IN_PROGRESS);
        }
        this.bytesDownloaded = this.mediaContentLength;
        this.updateStateAndNotifyListener(DownloadState.MEDIA_COMPLETE);
    }
    
    private HttpResponse executeCurrentRequest(final long a3, final GenericUrl a4, final HttpHeaders v1, final OutputStream v2) throws IOException {
        final HttpRequest v3 = /*EL:230*/this.requestFactory.buildGetRequest(a4);
        /*SL:232*/if (v1 != null) {
            /*SL:233*/v3.getHeaders().putAll(v1);
        }
        /*SL:236*/if (this.bytesDownloaded != 0L || a3 != -1L) {
            final StringBuilder a5 = /*EL:237*/new StringBuilder();
            /*SL:238*/a5.append("bytes=").append(this.bytesDownloaded).append("-");
            /*SL:239*/if (a3 != -1L) {
                /*SL:240*/a5.append(a3);
            }
            /*SL:242*/v3.getHeaders().setRange(a5.toString());
        }
        final HttpResponse v4 = /*EL:245*/v3.execute();
        try {
            /*SL:247*/IOUtils.copy(v4.getContent(), v2);
        }
        finally {
            /*SL:249*/v4.disconnect();
        }
        /*SL:251*/return v4;
    }
    
    private long getNextByteIndex(final String a1) {
        /*SL:263*/if (a1 == null) {
            /*SL:264*/return 0L;
        }
        /*SL:266*/return Long.parseLong(a1.substring(a1.indexOf(45) + 1, a1.indexOf(47))) + 1L;
    }
    
    public MediaHttpDownloader setBytesDownloaded(final long a1) {
        /*SL:286*/Preconditions.checkArgument(a1 >= 0L);
        /*SL:287*/this.bytesDownloaded = a1;
        /*SL:288*/return this;
    }
    
    public MediaHttpDownloader setContentRange(final long a1, final int a2) {
        /*SL:308*/Preconditions.checkArgument(a2 >= a1);
        /*SL:309*/this.setBytesDownloaded(a1);
        /*SL:310*/this.lastBytePos = a2;
        /*SL:311*/return this;
    }
    
    private void setMediaContentLength(final String a1) {
        /*SL:322*/if (a1 == null) {
            /*SL:323*/return;
        }
        /*SL:325*/if (this.mediaContentLength == 0L) {
            /*SL:326*/this.mediaContentLength = Long.parseLong(a1.substring(a1.indexOf(47) + 1));
        }
    }
    
    public boolean isDirectDownloadEnabled() {
        /*SL:337*/return this.directDownloadEnabled;
    }
    
    public MediaHttpDownloader setDirectDownloadEnabled(final boolean a1) {
        /*SL:347*/this.directDownloadEnabled = a1;
        /*SL:348*/return this;
    }
    
    public MediaHttpDownloader setProgressListener(final MediaHttpDownloaderProgressListener a1) {
        /*SL:356*/this.progressListener = a1;
        /*SL:357*/return this;
    }
    
    public MediaHttpDownloaderProgressListener getProgressListener() {
        /*SL:364*/return this.progressListener;
    }
    
    public HttpTransport getTransport() {
        /*SL:369*/return this.transport;
    }
    
    public MediaHttpDownloader setChunkSize(final int a1) {
        /*SL:381*/Preconditions.checkArgument(a1 > 0 && a1 <= 33554432);
        /*SL:382*/this.chunkSize = a1;
        /*SL:383*/return this;
    }
    
    public int getChunkSize() {
        /*SL:391*/return this.chunkSize;
    }
    
    public long getNumBytesDownloaded() {
        /*SL:400*/return this.bytesDownloaded;
    }
    
    public long getLastBytePosition() {
        /*SL:411*/return this.lastBytePos;
    }
    
    private void updateStateAndNotifyListener(final DownloadState a1) throws IOException {
        /*SL:420*/this.downloadState = a1;
        /*SL:421*/if (this.progressListener != null) {
            /*SL:422*/this.progressListener.progressChanged(this);
        }
    }
    
    public DownloadState getDownloadState() {
        /*SL:432*/return this.downloadState;
    }
    
    public double getProgress() {
        /*SL:442*/return (this.mediaContentLength == 0L) ? 0.0 : (this.bytesDownloaded / this.mediaContentLength);
    }
    
    public enum DownloadState
    {
        NOT_STARTED, 
        MEDIA_IN_PROGRESS, 
        MEDIA_COMPLETE;
    }
}

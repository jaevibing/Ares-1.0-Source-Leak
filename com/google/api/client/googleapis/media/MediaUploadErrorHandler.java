package com.google.api.client.googleapis.media;

import com.google.api.client.http.HttpResponse;
import java.io.IOException;
import java.util.logging.Level;
import com.google.api.client.util.Preconditions;
import com.google.api.client.http.HttpRequest;
import java.util.logging.Logger;
import com.google.api.client.util.Beta;
import com.google.api.client.http.HttpIOExceptionHandler;
import com.google.api.client.http.HttpUnsuccessfulResponseHandler;

@Beta
class MediaUploadErrorHandler implements HttpUnsuccessfulResponseHandler, HttpIOExceptionHandler
{
    static final Logger LOGGER;
    private final MediaHttpUploader uploader;
    private final HttpIOExceptionHandler originalIOExceptionHandler;
    private final HttpUnsuccessfulResponseHandler originalUnsuccessfulHandler;
    
    public MediaUploadErrorHandler(final MediaHttpUploader a1, final HttpRequest a2) {
        this.uploader = Preconditions.<MediaHttpUploader>checkNotNull(a1);
        this.originalIOExceptionHandler = a2.getIOExceptionHandler();
        this.originalUnsuccessfulHandler = a2.getUnsuccessfulResponseHandler();
        a2.setIOExceptionHandler(this);
        a2.setUnsuccessfulResponseHandler(this);
    }
    
    public boolean handleIOException(final HttpRequest v1, final boolean v2) throws IOException {
        final boolean v3 = /*EL:61*/this.originalIOExceptionHandler != null && this.originalIOExceptionHandler.handleIOException(v1, v2);
        /*SL:66*/if (v3) {
            try {
                /*SL:68*/this.uploader.serverErrorCallback();
            }
            catch (IOException a1) {
                MediaUploadErrorHandler.LOGGER.log(Level.WARNING, /*EL:70*/"exception thrown while calling server callback", a1);
            }
        }
        /*SL:73*/return v3;
    }
    
    public boolean handleResponse(final HttpRequest a3, final HttpResponse v1, final boolean v2) throws IOException {
        final boolean v3 = /*EL:78*/this.originalUnsuccessfulHandler != null && this.originalUnsuccessfulHandler.handleResponse(a3, v1, v2);
        /*SL:83*/if (v3 && v2 && v1.getStatusCode() / 100 == 5) {
            try {
                /*SL:85*/this.uploader.serverErrorCallback();
            }
            catch (IOException a4) {
                MediaUploadErrorHandler.LOGGER.log(Level.WARNING, /*EL:87*/"exception thrown while calling server callback", a4);
            }
        }
        /*SL:90*/return v3;
    }
    
    static {
        LOGGER = Logger.getLogger(MediaUploadErrorHandler.class.getName());
    }
}

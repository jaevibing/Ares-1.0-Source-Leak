package com.google.api.client.googleapis.notifications;

import java.io.InputStream;
import com.google.api.client.util.Beta;

@Beta
public class UnparsedNotification extends AbstractNotification
{
    private String contentType;
    private InputStream contentStream;
    
    public UnparsedNotification(final long a1, final String a2, final String a3, final String a4, final String a5) {
        super(a1, a2, a3, a4, a5);
    }
    
    public final String getContentType() {
        /*SL:60*/return this.contentType;
    }
    
    public UnparsedNotification setContentType(final String a1) {
        /*SL:73*/this.contentType = a1;
        /*SL:74*/return this;
    }
    
    public final InputStream getContentStream() {
        /*SL:81*/return this.contentStream;
    }
    
    public UnparsedNotification setContentStream(final InputStream a1) {
        /*SL:93*/this.contentStream = a1;
        /*SL:94*/return this;
    }
    
    public UnparsedNotification setMessageNumber(final long a1) {
        /*SL:99*/return (UnparsedNotification)super.setMessageNumber(a1);
    }
    
    public UnparsedNotification setResourceState(final String a1) {
        /*SL:104*/return (UnparsedNotification)super.setResourceState(a1);
    }
    
    public UnparsedNotification setResourceId(final String a1) {
        /*SL:109*/return (UnparsedNotification)super.setResourceId(a1);
    }
    
    public UnparsedNotification setResourceUri(final String a1) {
        /*SL:114*/return (UnparsedNotification)super.setResourceUri(a1);
    }
    
    public UnparsedNotification setChannelId(final String a1) {
        /*SL:119*/return (UnparsedNotification)super.setChannelId(a1);
    }
    
    public UnparsedNotification setChannelExpiration(final String a1) {
        /*SL:124*/return (UnparsedNotification)super.setChannelExpiration(a1);
    }
    
    public UnparsedNotification setChannelToken(final String a1) {
        /*SL:129*/return (UnparsedNotification)super.setChannelToken(a1);
    }
    
    public UnparsedNotification setChanged(final String a1) {
        /*SL:134*/return (UnparsedNotification)super.setChanged(a1);
    }
    
    public String toString() {
        /*SL:139*/return super.toStringHelper().add("contentType", this.contentType).toString();
    }
}

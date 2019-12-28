package com.google.api.client.googleapis.notifications;

import com.google.api.client.util.Preconditions;
import com.google.api.client.util.Objects;
import com.google.api.client.util.Beta;

@Beta
public abstract class AbstractNotification
{
    private long messageNumber;
    private String resourceState;
    private String resourceId;
    private String resourceUri;
    private String channelId;
    private String channelExpiration;
    private String channelToken;
    private String changed;
    
    protected AbstractNotification(final long a1, final String a2, final String a3, final String a4, final String a5) {
        this.setMessageNumber(a1);
        this.setResourceState(a2);
        this.setResourceId(a3);
        this.setResourceUri(a4);
        this.setChannelId(a5);
    }
    
    protected AbstractNotification(final AbstractNotification a1) {
        this(a1.getMessageNumber(), a1.getResourceState(), a1.getResourceId(), a1.getResourceUri(), a1.getChannelId());
        this.setChannelExpiration(a1.getChannelExpiration());
        this.setChannelToken(a1.getChannelToken());
        this.setChanged(a1.getChanged());
    }
    
    public String toString() {
        /*SL:94*/return this.toStringHelper().toString();
    }
    
    protected Objects.ToStringHelper toStringHelper() {
        /*SL:99*/return Objects.toStringHelper(this).add("messageNumber", this.messageNumber).add("resourceState", this.resourceState).add("resourceId", this.resourceId).add("resourceUri", this.resourceUri).add("channelId", this.channelId).add("channelExpiration", this.channelExpiration).add("channelToken", this.channelToken).add("changed", this.changed);
    }
    
    public final long getMessageNumber() {
        /*SL:108*/return this.messageNumber;
    }
    
    public AbstractNotification setMessageNumber(final long a1) {
        /*SL:120*/Preconditions.checkArgument(a1 >= 1L);
        /*SL:121*/this.messageNumber = a1;
        /*SL:122*/return this;
    }
    
    public final String getResourceState() {
        /*SL:127*/return this.resourceState;
    }
    
    public AbstractNotification setResourceState(final String a1) {
        /*SL:139*/this.resourceState = Preconditions.<String>checkNotNull(a1);
        /*SL:140*/return this;
    }
    
    public final String getResourceId() {
        /*SL:145*/return this.resourceId;
    }
    
    public AbstractNotification setResourceId(final String a1) {
        /*SL:157*/this.resourceId = Preconditions.<String>checkNotNull(a1);
        /*SL:158*/return this;
    }
    
    public final String getResourceUri() {
        /*SL:166*/return this.resourceUri;
    }
    
    public AbstractNotification setResourceUri(final String a1) {
        /*SL:179*/this.resourceUri = Preconditions.<String>checkNotNull(a1);
        /*SL:180*/return this;
    }
    
    public final String getChannelId() {
        /*SL:185*/return this.channelId;
    }
    
    public AbstractNotification setChannelId(final String a1) {
        /*SL:197*/this.channelId = Preconditions.<String>checkNotNull(a1);
        /*SL:198*/return this;
    }
    
    public final String getChannelExpiration() {
        /*SL:203*/return this.channelExpiration;
    }
    
    public AbstractNotification setChannelExpiration(final String a1) {
        /*SL:215*/this.channelExpiration = a1;
        /*SL:216*/return this;
    }
    
    public final String getChannelToken() {
        /*SL:224*/return this.channelToken;
    }
    
    public AbstractNotification setChannelToken(final String a1) {
        /*SL:237*/this.channelToken = a1;
        /*SL:238*/return this;
    }
    
    public final String getChanged() {
        /*SL:245*/return this.changed;
    }
    
    public AbstractNotification setChanged(final String a1) {
        /*SL:257*/this.changed = a1;
        /*SL:258*/return this;
    }
}

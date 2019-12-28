package com.google.api.client.googleapis.notifications;

import com.google.api.client.util.Beta;

@Beta
public class TypedNotification<T> extends AbstractNotification
{
    private T content;
    
    public TypedNotification(final long a1, final String a2, final String a3, final String a4, final String a5) {
        super(a1, a2, a3, a4, a5);
    }
    
    public TypedNotification(final UnparsedNotification a1) {
        super(a1);
    }
    
    public final T getContent() {
        /*SL:63*/return this.content;
    }
    
    public TypedNotification<T> setContent(final T a1) {
        /*SL:75*/this.content = a1;
        /*SL:76*/return this;
    }
    
    public TypedNotification<T> setMessageNumber(final long a1) {
        /*SL:82*/return (TypedNotification)super.setMessageNumber(a1);
    }
    
    public TypedNotification<T> setResourceState(final String a1) {
        /*SL:88*/return (TypedNotification)super.setResourceState(a1);
    }
    
    public TypedNotification<T> setResourceId(final String a1) {
        /*SL:94*/return (TypedNotification)super.setResourceId(a1);
    }
    
    public TypedNotification<T> setResourceUri(final String a1) {
        /*SL:100*/return (TypedNotification)super.setResourceUri(a1);
    }
    
    public TypedNotification<T> setChannelId(final String a1) {
        /*SL:106*/return (TypedNotification)super.setChannelId(a1);
    }
    
    public TypedNotification<T> setChannelExpiration(final String a1) {
        /*SL:112*/return (TypedNotification)super.setChannelExpiration(a1);
    }
    
    public TypedNotification<T> setChannelToken(final String a1) {
        /*SL:118*/return (TypedNotification)super.setChannelToken(a1);
    }
    
    public TypedNotification<T> setChanged(final String a1) {
        /*SL:124*/return (TypedNotification)super.setChanged(a1);
    }
    
    public String toString() {
        /*SL:129*/return super.toStringHelper().add("content", this.content).toString();
    }
}

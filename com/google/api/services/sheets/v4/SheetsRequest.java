package com.google.api.services.sheets.v4;

import com.google.api.client.util.GenericData;
import com.google.api.client.googleapis.services.AbstractGoogleClient;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.http.HttpHeaders;
import com.google.api.client.googleapis.services.json.AbstractGoogleJsonClient;
import com.google.api.client.util.Key;
import com.google.api.client.googleapis.services.json.AbstractGoogleJsonClientRequest;

public abstract class SheetsRequest<T> extends AbstractGoogleJsonClientRequest<T>
{
    @Key("$.xgafv")
    private String $Xgafv;
    @Key("access_token")
    private String accessToken;
    @Key
    private String alt;
    @Key
    private String callback;
    @Key
    private String fields;
    @Key
    private String key;
    @Key("oauth_token")
    private String oauthToken;
    @Key
    private Boolean prettyPrint;
    @Key
    private String quotaUser;
    @Key
    private String uploadType;
    @Key("upload_protocol")
    private String uploadProtocol;
    
    public SheetsRequest(final Sheets a1, final String a2, final String a3, final Object a4, final Class<T> a5) {
        super(a1, a2, a3, a4, a5);
    }
    
    public String get$Xgafv() {
        /*SL:59*/return this.$Xgafv;
    }
    
    public SheetsRequest<T> set$Xgafv(final String $Xgafv) {
        /*SL:64*/this.$Xgafv = $Xgafv;
        /*SL:65*/return this;
    }
    
    public String getAccessToken() {
        /*SL:76*/return this.accessToken;
    }
    
    public SheetsRequest<T> setAccessToken(final String accessToken) {
        /*SL:81*/this.accessToken = accessToken;
        /*SL:82*/return this;
    }
    
    public String getAlt() {
        /*SL:93*/return this.alt;
    }
    
    public SheetsRequest<T> setAlt(final String alt) {
        /*SL:98*/this.alt = alt;
        /*SL:99*/return this;
    }
    
    public String getCallback() {
        /*SL:110*/return this.callback;
    }
    
    public SheetsRequest<T> setCallback(final String callback) {
        /*SL:115*/this.callback = callback;
        /*SL:116*/return this;
    }
    
    public String getFields() {
        /*SL:127*/return this.fields;
    }
    
    public SheetsRequest<T> setFields(final String fields) {
        /*SL:132*/this.fields = fields;
        /*SL:133*/return this;
    }
    
    public String getKey() {
        /*SL:148*/return this.key;
    }
    
    public SheetsRequest<T> setKey(final String key) {
        /*SL:156*/this.key = key;
        /*SL:157*/return this;
    }
    
    public String getOauthToken() {
        /*SL:168*/return this.oauthToken;
    }
    
    public SheetsRequest<T> setOauthToken(final String oauthToken) {
        /*SL:173*/this.oauthToken = oauthToken;
        /*SL:174*/return this;
    }
    
    public Boolean getPrettyPrint() {
        /*SL:185*/return this.prettyPrint;
    }
    
    public SheetsRequest<T> setPrettyPrint(final Boolean prettyPrint) {
        /*SL:190*/this.prettyPrint = prettyPrint;
        /*SL:191*/return this;
    }
    
    public String getQuotaUser() {
        /*SL:206*/return this.quotaUser;
    }
    
    public SheetsRequest<T> setQuotaUser(final String quotaUser) {
        /*SL:214*/this.quotaUser = quotaUser;
        /*SL:215*/return this;
    }
    
    public String getUploadType() {
        /*SL:226*/return this.uploadType;
    }
    
    public SheetsRequest<T> setUploadType(final String uploadType) {
        /*SL:231*/this.uploadType = uploadType;
        /*SL:232*/return this;
    }
    
    public String getUploadProtocol() {
        /*SL:243*/return this.uploadProtocol;
    }
    
    public SheetsRequest<T> setUploadProtocol(final String uploadProtocol) {
        /*SL:248*/this.uploadProtocol = uploadProtocol;
        /*SL:249*/return this;
    }
    
    public final Sheets getAbstractGoogleClient() {
        /*SL:254*/return (Sheets)super.getAbstractGoogleClient();
    }
    
    public SheetsRequest<T> setDisableGZipContent(final boolean disableGZipContent) {
        /*SL:259*/return (SheetsRequest)super.setDisableGZipContent(disableGZipContent);
    }
    
    public SheetsRequest<T> setRequestHeaders(final HttpHeaders requestHeaders) {
        /*SL:264*/return (SheetsRequest)super.setRequestHeaders(requestHeaders);
    }
    
    public SheetsRequest<T> set(final String a1, final Object a2) {
        /*SL:269*/return (SheetsRequest)super.set(a1, a2);
    }
}

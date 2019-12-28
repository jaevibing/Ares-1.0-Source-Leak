package com.google.api.client.json.rpc2;

import com.google.api.client.util.Key;
import com.google.api.client.util.Beta;
import com.google.api.client.util.GenericData;

@Beta
public class JsonRpcRequest extends GenericData
{
    @Key
    private final String jsonrpc = "2.0";
    @Key
    private Object id;
    @Key
    private String method;
    @Key
    private Object params;
    
    public String getVersion() {
        /*SL:63*/return "2.0";
    }
    
    public Object getId() {
        /*SL:73*/return this.id;
    }
    
    public void setId(final Object a1) {
        /*SL:83*/this.id = a1;
    }
    
    public String getMethod() {
        /*SL:92*/return this.method;
    }
    
    public void setMethod(final String a1) {
        /*SL:101*/this.method = a1;
    }
    
    public Object getParameters() {
        /*SL:111*/return this.params;
    }
    
    public void setParameters(final Object a1) {
        /*SL:121*/this.params = a1;
    }
    
    @Override
    public JsonRpcRequest set(final String a1, final Object a2) {
        /*SL:126*/return (JsonRpcRequest)super.set(a1, a2);
    }
    
    @Override
    public JsonRpcRequest clone() {
        /*SL:131*/return (JsonRpcRequest)super.clone();
    }
}

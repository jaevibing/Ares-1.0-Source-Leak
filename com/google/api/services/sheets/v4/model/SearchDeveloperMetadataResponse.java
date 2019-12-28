package com.google.api.services.sheets.v4.model;

import com.google.api.client.util.Data;
import com.google.api.client.util.GenericData;
import com.google.api.client.util.Key;
import java.util.List;
import com.google.api.client.json.GenericJson;

public final class SearchDeveloperMetadataResponse extends GenericJson
{
    @Key
    private List<MatchedDeveloperMetadata> matchedDeveloperMetadata;
    
    public List<MatchedDeveloperMetadata> getMatchedDeveloperMetadata() {
        /*SL:54*/return this.matchedDeveloperMetadata;
    }
    
    public SearchDeveloperMetadataResponse setMatchedDeveloperMetadata(final List<MatchedDeveloperMetadata> matchedDeveloperMetadata) {
        /*SL:62*/this.matchedDeveloperMetadata = matchedDeveloperMetadata;
        /*SL:63*/return this;
    }
    
    public SearchDeveloperMetadataResponse set(final String a1, final Object a2) {
        /*SL:68*/return (SearchDeveloperMetadataResponse)super.set(a1, a2);
    }
    
    public SearchDeveloperMetadataResponse clone() {
        /*SL:73*/return (SearchDeveloperMetadataResponse)super.clone();
    }
    
    static {
        Data.<Object>nullOf(MatchedDeveloperMetadata.class);
    }
}

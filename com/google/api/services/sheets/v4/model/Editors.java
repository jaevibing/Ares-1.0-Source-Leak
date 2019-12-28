package com.google.api.services.sheets.v4.model;

import com.google.api.client.util.GenericData;
import java.util.List;
import com.google.api.client.util.Key;
import com.google.api.client.json.GenericJson;

public final class Editors extends GenericJson
{
    @Key
    private Boolean domainUsersCanEdit;
    @Key
    private List<String> groups;
    @Key
    private List<String> users;
    
    public Boolean getDomainUsersCanEdit() {
        /*SL:64*/return this.domainUsersCanEdit;
    }
    
    public Editors setDomainUsersCanEdit(final Boolean domainUsersCanEdit) {
        /*SL:73*/this.domainUsersCanEdit = domainUsersCanEdit;
        /*SL:74*/return this;
    }
    
    public List<String> getGroups() {
        /*SL:82*/return this.groups;
    }
    
    public Editors setGroups(final List<String> groups) {
        /*SL:90*/this.groups = groups;
        /*SL:91*/return this;
    }
    
    public List<String> getUsers() {
        /*SL:99*/return this.users;
    }
    
    public Editors setUsers(final List<String> users) {
        /*SL:107*/this.users = users;
        /*SL:108*/return this;
    }
    
    public Editors set(final String a1, final Object a2) {
        /*SL:113*/return (Editors)super.set(a1, a2);
    }
    
    public Editors clone() {
        /*SL:118*/return (Editors)super.clone();
    }
}

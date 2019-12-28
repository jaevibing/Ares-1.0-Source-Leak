package com.google.cloud.storage;

import java.util.Objects;
import com.google.common.base.MoreObjects;
import com.google.api.services.storage.model.ServiceAccount;
import com.google.common.base.Function;
import java.io.Serializable;

public final class ServiceAccount implements Serializable
{
    static final Function<com.google.api.services.storage.model.ServiceAccount, ServiceAccount> FROM_PB_FUNCTION;
    static final Function<ServiceAccount, com.google.api.services.storage.model.ServiceAccount> TO_PB_FUNCTION;
    private static final long serialVersionUID = 4199610694227857331L;
    private final String email;
    
    private ServiceAccount(final String a1) {
        this.email = a1;
    }
    
    public String getEmail() {
        /*SL:60*/return this.email;
    }
    
    @Override
    public String toString() {
        /*SL:65*/return MoreObjects.toStringHelper((Object)this).add("email", (Object)this.email).toString();
    }
    
    @Override
    public int hashCode() {
        /*SL:70*/return Objects.hash(this.email);
    }
    
    @Override
    public boolean equals(final Object a1) {
        /*SL:75*/return a1 == this || (a1 instanceof ServiceAccount && /*EL:76*/Objects.equals(this.toPb(), ((ServiceAccount)a1).toPb()));
    }
    
    com.google.api.services.storage.model.ServiceAccount toPb() {
        final com.google.api.services.storage.model.ServiceAccount v1 = /*EL:80*/new com.google.api.services.storage.model.ServiceAccount();
        /*SL:82*/v1.setEmailAddress(this.email);
        /*SL:83*/return v1;
    }
    
    public static ServiceAccount of(final String a1) {
        /*SL:88*/return new ServiceAccount(a1);
    }
    
    static ServiceAccount fromPb(final com.google.api.services.storage.model.ServiceAccount a1) {
        /*SL:92*/return new ServiceAccount(a1.getEmailAddress());
    }
    
    static {
        FROM_PB_FUNCTION = new Function<com.google.api.services.storage.model.ServiceAccount, ServiceAccount>() {
            @Override
            public ServiceAccount apply(final com.google.api.services.storage.model.ServiceAccount a1) {
                /*SL:37*/return ServiceAccount.fromPb(a1);
            }
        };
        TO_PB_FUNCTION = new Function<ServiceAccount, com.google.api.services.storage.model.ServiceAccount>() {
            @Override
            public com.google.api.services.storage.model.ServiceAccount apply(final ServiceAccount a1) {
                /*SL:46*/return a1.toPb();
            }
        };
    }
}

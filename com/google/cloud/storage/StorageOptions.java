package com.google.cloud.storage;

import com.google.cloud.ServiceFactory;
import com.google.cloud.spi.ServiceRpcFactory;
import com.google.cloud.TransportOptions;
import com.google.cloud.storage.spi.v1.HttpStorageRpc;
import com.google.cloud.ServiceRpc;
import com.google.cloud.Service;
import com.google.common.collect.ImmutableSet;
import com.google.auth.Credentials;
import com.google.cloud.NoCredentials;
import com.google.cloud.storage.spi.v1.StorageRpc;
import com.google.cloud.http.HttpTransportOptions;
import com.google.cloud.ServiceDefaults;
import com.google.cloud.storage.spi.StorageRpcFactory;
import java.util.Set;
import com.google.cloud.ServiceOptions;

public class StorageOptions extends ServiceOptions<Storage, StorageOptions>
{
    private static final long serialVersionUID = -2907268477247502947L;
    private static final String API_SHORT_NAME = "Storage";
    private static final String GCS_SCOPE = "https://www.googleapis.com/auth/devstorage.full_control";
    private static final Set<String> SCOPES;
    
    private StorageOptions(final Builder a1) {
        super((Class)StorageFactory.class, (Class)StorageRpcFactory.class, (ServiceOptions.Builder)a1, (ServiceDefaults)new StorageDefaults());
    }
    
    public static HttpTransportOptions getDefaultHttpTransportOptions() {
        /*SL:104*/return HttpTransportOptions.newBuilder().build();
    }
    
    protected boolean projectIdRequired() {
        /*SL:111*/return false;
    }
    
    protected Set<String> getScopes() {
        /*SL:116*/return StorageOptions.SCOPES;
    }
    
    protected StorageRpc getStorageRpcV1() {
        /*SL:120*/return (StorageRpc)this.getRpc();
    }
    
    public static StorageOptions getDefaultInstance() {
        /*SL:125*/return newBuilder().build();
    }
    
    public static StorageOptions getUnauthenticatedInstance() {
        /*SL:130*/return ((Builder)newBuilder().setCredentials((Credentials)NoCredentials.getInstance())).build();
    }
    
    public Builder toBuilder() {
        /*SL:136*/return new Builder(this);
    }
    
    public int hashCode() {
        /*SL:141*/return this.baseHashCode();
    }
    
    public boolean equals(final Object a1) {
        /*SL:146*/return a1 instanceof StorageOptions && this.baseEquals((ServiceOptions)a1);
    }
    
    public static Builder newBuilder() {
        /*SL:150*/return new Builder();
    }
    
    static {
        SCOPES = ImmutableSet.<String>of("https://www.googleapis.com/auth/devstorage.full_control");
    }
    
    public static class DefaultStorageFactory implements StorageFactory
    {
        private static final StorageFactory INSTANCE;
        
        public Storage create(final StorageOptions a1) {
            /*SL:44*/return new StorageImpl(a1);
        }
        
        static {
            INSTANCE = new DefaultStorageFactory();
        }
    }
    
    public static class DefaultStorageRpcFactory implements StorageRpcFactory
    {
        private static final StorageRpcFactory INSTANCE;
        
        public ServiceRpc create(final StorageOptions a1) {
            /*SL:54*/return (ServiceRpc)new HttpStorageRpc(a1);
        }
        
        static {
            INSTANCE = new DefaultStorageRpcFactory();
        }
    }
    
    public static class Builder extends ServiceOptions.Builder<Storage, StorageOptions, Builder>
    {
        private Builder() {
        }
        
        private Builder(final StorageOptions a1) {
            super((ServiceOptions)a1);
        }
        
        public Builder setTransportOptions(final TransportOptions a1) {
            /*SL:68*/if (!(a1 instanceof HttpTransportOptions)) {
                /*SL:69*/throw new IllegalArgumentException("Only http transport is allowed for Storage.");
            }
            /*SL:72*/return (Builder)super.setTransportOptions(a1);
        }
        
        public StorageOptions build() {
            /*SL:77*/return new StorageOptions(this, null);
        }
    }
    
    private static class StorageDefaults implements ServiceDefaults<Storage, StorageOptions>
    {
        public StorageFactory getDefaultServiceFactory() {
            /*SL:89*/return DefaultStorageFactory.INSTANCE;
        }
        
        public StorageRpcFactory getDefaultRpcFactory() {
            /*SL:94*/return DefaultStorageRpcFactory.INSTANCE;
        }
        
        public TransportOptions getDefaultTransportOptions() {
            /*SL:99*/return (TransportOptions)StorageOptions.getDefaultHttpTransportOptions();
        }
    }
}

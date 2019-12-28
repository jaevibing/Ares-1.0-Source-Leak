package com.google.cloud.storage;

import com.google.api.client.util.DateTime;
import com.google.api.services.storage.model.HmacKeyMetadata;
import com.google.api.services.storage.model.HmacKey;
import java.util.Objects;
import java.io.Serializable;

public class HmacKey implements Serializable
{
    private static final long serialVersionUID = -1809610424373783062L;
    private final String secretKey;
    private final HmacKeyMetadata metadata;
    
    private HmacKey(final Builder a1) {
        this.secretKey = a1.secretKey;
        this.metadata = a1.metadata;
    }
    
    public static Builder newBuilder(final String a1) {
        /*SL:36*/return new Builder(a1);
    }
    
    public String getSecretKey() {
        /*SL:66*/return this.secretKey;
    }
    
    public HmacKeyMetadata getMetadata() {
        /*SL:71*/return this.metadata;
    }
    
    @Override
    public int hashCode() {
        /*SL:76*/return Objects.hash(this.secretKey, this.metadata);
    }
    
    @Override
    public boolean equals(final Object a1) {
        /*SL:81*/if (this == a1) {
            /*SL:82*/return true;
        }
        /*SL:84*/if (a1 == null || this.getClass() != a1.getClass()) {
            /*SL:85*/return false;
        }
        final HmacKeyMetadata v1 = /*EL:87*/(HmacKeyMetadata)a1;
        /*SL:88*/return Objects.equals(this.secretKey, this.secretKey) && Objects.equals(this.metadata, this.metadata);
    }
    
    com.google.api.services.storage.model.HmacKey toPb() {
        final com.google.api.services.storage.model.HmacKey v1 = /*EL:92*/new com.google.api.services.storage.model.HmacKey();
        /*SL:94*/v1.setSecret(this.secretKey);
        /*SL:96*/if (this.metadata != null) {
            /*SL:97*/v1.setMetadata(this.metadata.toPb());
        }
        /*SL:100*/return v1;
    }
    
    static HmacKey fromPb(final com.google.api.services.storage.model.HmacKey a1) {
        /*SL:104*/return newBuilder(a1.getSecret()).setMetadata(/*EL:105*/HmacKeyMetadata.fromPb(a1.getMetadata())).build();
    }
    
    public static class Builder
    {
        private String secretKey;
        private HmacKeyMetadata metadata;
        
        private Builder(final String a1) {
            this.secretKey = a1;
        }
        
        public Builder setSecretKey(final String a1) {
            /*SL:49*/this.secretKey = a1;
            /*SL:50*/return this;
        }
        
        public Builder setMetadata(final HmacKeyMetadata a1) {
            /*SL:54*/this.metadata = a1;
            /*SL:55*/return this;
        }
        
        public HmacKey build() {
            /*SL:60*/return new HmacKey(this, null);
        }
    }
    
    public enum HmacKeyState
    {
        ACTIVE("ACTIVE"), 
        INACTIVE("INACTIVE"), 
        DELETED("DELETED");
        
        private final String state;
        
        private HmacKeyState(final String a1) {
            this.state = a1;
        }
    }
    
    public static class HmacKeyMetadata implements Serializable
    {
        private static final long serialVersionUID = 4571684785352640737L;
        private final String accessId;
        private final String etag;
        private final String id;
        private final String projectId;
        private final ServiceAccount serviceAccount;
        private final HmacKeyState state;
        private final Long createTime;
        private final Long updateTime;
        
        private HmacKeyMetadata(final Builder a1) {
            this.accessId = a1.accessId;
            this.etag = a1.etag;
            this.id = a1.id;
            this.projectId = a1.projectId;
            this.serviceAccount = a1.serviceAccount;
            this.state = a1.state;
            this.createTime = a1.createTime;
            this.updateTime = a1.updateTime;
        }
        
        public static Builder newBuilder(final ServiceAccount a1) {
            /*SL:149*/return new Builder(a1);
        }
        
        public Builder toBuilder() {
            /*SL:153*/return new Builder(this);
        }
        
        public static HmacKeyMetadata of(final ServiceAccount a1, final String a2, final String a3) {
            /*SL:158*/return newBuilder(a1).setAccessId(a2).setProjectId(a3).build();
        }
        
        @Override
        public int hashCode() {
            /*SL:163*/return Objects.hash(this.accessId, this.projectId);
        }
        
        @Override
        public boolean equals(final Object a1) {
            /*SL:168*/if (this == a1) {
                /*SL:169*/return true;
            }
            /*SL:171*/if (a1 == null || this.getClass() != a1.getClass()) {
                /*SL:172*/return false;
            }
            final HmacKeyMetadata v1 = /*EL:174*/(HmacKeyMetadata)a1;
            /*SL:175*/return Objects.equals(this.accessId, v1.accessId) && /*EL:176*/Objects.equals(this.etag, v1.etag) && /*EL:177*/Objects.equals(this.id, v1.id) && /*EL:178*/Objects.equals(this.projectId, v1.projectId) && /*EL:179*/Objects.equals(this.serviceAccount, v1.serviceAccount) && /*EL:180*/Objects.equals(this.state, v1.state) && /*EL:181*/Objects.equals(this.createTime, v1.createTime) && /*EL:182*/Objects.equals(this.updateTime, v1.updateTime);
        }
        
        public com.google.api.services.storage.model.HmacKeyMetadata toPb() {
            final com.google.api.services.storage.model.HmacKeyMetadata v1 = /*EL:186*/new com.google.api.services.storage.model.HmacKeyMetadata();
            /*SL:188*/v1.setAccessId(this.accessId);
            /*SL:189*/v1.setEtag(this.etag);
            /*SL:190*/v1.setId(this.id);
            /*SL:191*/v1.setProjectId(this.projectId);
            /*SL:192*/v1.setServiceAccountEmail((this.serviceAccount == null) ? null : this.serviceAccount.getEmail());
            /*SL:194*/v1.setState((this.state == null) ? null : this.state.toString());
            /*SL:195*/v1.setTimeCreated((this.createTime == null) ? null : new DateTime(this.createTime));
            /*SL:196*/v1.setUpdated((this.updateTime == null) ? null : new DateTime(this.updateTime));
            /*SL:198*/return v1;
        }
        
        static HmacKeyMetadata fromPb(final com.google.api.services.storage.model.HmacKeyMetadata a1) {
            /*SL:202*/return newBuilder(ServiceAccount.of(a1.getServiceAccountEmail())).setAccessId(a1.getAccessId()).setCreateTime(/*EL:203*/a1.getTimeCreated().getValue()).setEtag(/*EL:204*/a1.getEtag()).setId(/*EL:205*/a1.getId()).setProjectId(/*EL:206*/a1.getProjectId()).setState(/*EL:208*/HmacKeyState.valueOf(a1.getState())).setUpdateTime(a1.getUpdated().getValue()).build();
        }
        
        public String getAccessId() {
            /*SL:217*/return this.accessId;
        }
        
        public String getEtag() {
            /*SL:226*/return this.etag;
        }
        
        public String getId() {
            /*SL:231*/return this.id;
        }
        
        public String getProjectId() {
            /*SL:236*/return this.projectId;
        }
        
        public ServiceAccount getServiceAccount() {
            /*SL:241*/return this.serviceAccount;
        }
        
        public HmacKeyState getState() {
            /*SL:246*/return this.state;
        }
        
        public Long getCreateTime() {
            /*SL:251*/return this.createTime;
        }
        
        public Long getUpdateTime() {
            /*SL:256*/return this.updateTime;
        }
        
        public static class Builder
        {
            private String accessId;
            private String etag;
            private String id;
            private String projectId;
            private ServiceAccount serviceAccount;
            private HmacKeyState state;
            private Long createTime;
            private Long updateTime;
            
            private Builder(final ServiceAccount a1) {
                this.serviceAccount = a1;
            }
            
            private Builder(final HmacKeyMetadata a1) {
                this.accessId = a1.accessId;
                this.etag = a1.etag;
                this.id = a1.id;
                this.projectId = a1.projectId;
                this.serviceAccount = a1.serviceAccount;
                this.state = a1.state;
                this.createTime = a1.createTime;
                this.updateTime = a1.updateTime;
            }
            
            public Builder setAccessId(final String a1) {
                /*SL:286*/this.accessId = a1;
                /*SL:287*/return this;
            }
            
            public Builder setEtag(final String a1) {
                /*SL:291*/this.etag = a1;
                /*SL:292*/return this;
            }
            
            public Builder setId(final String a1) {
                /*SL:296*/this.id = a1;
                /*SL:297*/return this;
            }
            
            public Builder setServiceAccount(final ServiceAccount a1) {
                /*SL:301*/this.serviceAccount = a1;
                /*SL:302*/return this;
            }
            
            public Builder setState(final HmacKeyState a1) {
                /*SL:306*/this.state = a1;
                /*SL:307*/return this;
            }
            
            public Builder setCreateTime(final long a1) {
                /*SL:311*/this.createTime = a1;
                /*SL:312*/return this;
            }
            
            public Builder setProjectId(final String a1) {
                /*SL:316*/this.projectId = a1;
                /*SL:317*/return this;
            }
            
            public HmacKeyMetadata build() {
                /*SL:322*/return new HmacKeyMetadata(this);
            }
            
            public Builder setUpdateTime(final long a1) {
                /*SL:326*/this.updateTime = a1;
                /*SL:327*/return this;
            }
        }
    }
}

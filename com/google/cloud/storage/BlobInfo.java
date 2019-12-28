package com.google.cloud.storage;

import java.util.HashMap;
import java.util.Arrays;
import java.util.Collection;
import com.google.common.collect.ImmutableList;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import java.util.Set;
import java.util.AbstractMap;
import java.util.Iterator;
import com.google.common.collect.Maps;
import java.math.BigInteger;
import com.google.api.client.util.DateTime;
import com.google.common.collect.Lists;
import com.google.api.services.storage.model.ObjectAccessControl;
import java.util.Objects;
import com.google.api.core.BetaApi;
import java.util.Collections;
import com.google.common.io.BaseEncoding;
import com.google.api.client.util.Data;
import com.google.common.base.MoreObjects;
import java.util.Map;
import java.util.List;
import com.google.api.services.storage.model.StorageObject;
import com.google.common.base.Function;
import java.io.Serializable;

public class BlobInfo implements Serializable
{
    static final Function<BlobInfo, StorageObject> INFO_TO_PB_FUNCTION;
    private static final long serialVersionUID = -5625857076205028976L;
    private final BlobId blobId;
    private final String generatedId;
    private final String selfLink;
    private final String cacheControl;
    private final List<Acl> acl;
    private final Acl.Entity owner;
    private final Long size;
    private final String etag;
    private final String md5;
    private final String crc32c;
    private final String mediaLink;
    private final Map<String, String> metadata;
    private final Long metageneration;
    private final Long deleteTime;
    private final Long updateTime;
    private final Long createTime;
    private final String contentType;
    private final String contentEncoding;
    private final String contentDisposition;
    private final String contentLanguage;
    private final StorageClass storageClass;
    private final Integer componentCount;
    private final boolean isDirectory;
    private final CustomerEncryption customerEncryption;
    private final String kmsKeyName;
    private final Boolean eventBasedHold;
    private final Boolean temporaryHold;
    private final Long retentionExpirationTime;
    
    BlobInfo(final BuilderImpl a1) {
        this.blobId = a1.blobId;
        this.generatedId = a1.generatedId;
        this.cacheControl = a1.cacheControl;
        this.contentEncoding = a1.contentEncoding;
        this.contentType = a1.contentType;
        this.contentDisposition = a1.contentDisposition;
        this.contentLanguage = a1.contentLanguage;
        this.componentCount = a1.componentCount;
        this.customerEncryption = a1.customerEncryption;
        this.acl = a1.acl;
        this.owner = a1.owner;
        this.size = a1.size;
        this.etag = a1.etag;
        this.selfLink = a1.selfLink;
        this.md5 = a1.md5;
        this.crc32c = a1.crc32c;
        this.mediaLink = a1.mediaLink;
        this.metadata = a1.metadata;
        this.metageneration = a1.metageneration;
        this.deleteTime = a1.deleteTime;
        this.updateTime = a1.updateTime;
        this.createTime = a1.createTime;
        this.isDirectory = (boolean)MoreObjects.firstNonNull((Object)a1.isDirectory, (Object)Boolean.FALSE);
        this.storageClass = a1.storageClass;
        this.kmsKeyName = a1.kmsKeyName;
        this.eventBasedHold = a1.eventBasedHold;
        this.temporaryHold = a1.temporaryHold;
        this.retentionExpirationTime = a1.retentionExpirationTime;
    }
    
    public BlobId getBlobId() {
        /*SL:600*/return this.blobId;
    }
    
    public String getBucket() {
        /*SL:605*/return this.getBlobId().getBucket();
    }
    
    public String getGeneratedId() {
        /*SL:610*/return this.generatedId;
    }
    
    public String getName() {
        /*SL:615*/return this.getBlobId().getName();
    }
    
    public String getCacheControl() {
        /*SL:624*/return Data.isNull(this.cacheControl) ? null : this.cacheControl;
    }
    
    public List<Acl> getAcl() {
        /*SL:634*/return this.acl;
    }
    
    public Acl.Entity getOwner() {
        /*SL:639*/return this.owner;
    }
    
    public Long getSize() {
        /*SL:648*/return this.size;
    }
    
    public String getContentType() {
        /*SL:657*/return Data.isNull(this.contentType) ? null : this.contentType;
    }
    
    public String getContentEncoding() {
        /*SL:666*/return Data.isNull(this.contentEncoding) ? null : this.contentEncoding;
    }
    
    public String getContentDisposition() {
        /*SL:675*/return Data.isNull(this.contentDisposition) ? null : this.contentDisposition;
    }
    
    public String getContentLanguage() {
        /*SL:684*/return Data.isNull(this.contentLanguage) ? null : this.contentLanguage;
    }
    
    public Integer getComponentCount() {
        /*SL:697*/return this.componentCount;
    }
    
    public String getEtag() {
        /*SL:706*/return this.etag;
    }
    
    public String getSelfLink() {
        /*SL:711*/return this.selfLink;
    }
    
    public String getMd5() {
        /*SL:721*/return Data.isNull(this.md5) ? null : this.md5;
    }
    
    public String getMd5ToHexString() {
        /*SL:731*/if (this.md5 == null) {
            /*SL:732*/return null;
        }
        final byte[] decode = /*EL:734*/BaseEncoding.base64().decode(this.md5);
        final StringBuilder sb = /*EL:735*/new StringBuilder();
        /*SL:736*/for (final byte v1 : decode) {
            /*SL:737*/sb.append(String.format("%02x", v1 & 0xFF));
        }
        /*SL:739*/return sb.toString();
    }
    
    public String getCrc32c() {
        /*SL:751*/return Data.isNull(this.crc32c) ? null : this.crc32c;
    }
    
    public String getCrc32cToHexString() {
        /*SL:763*/if (this.crc32c == null) {
            /*SL:764*/return null;
        }
        final byte[] decode = /*EL:766*/BaseEncoding.base64().decode(this.crc32c);
        final StringBuilder sb = /*EL:767*/new StringBuilder();
        /*SL:768*/for (final byte v1 : decode) {
            /*SL:769*/sb.append(String.format("%02x", v1 & 0xFF));
        }
        /*SL:771*/return sb.toString();
    }
    
    public String getMediaLink() {
        /*SL:776*/return this.mediaLink;
    }
    
    public Map<String, String> getMetadata() {
        /*SL:781*/return (this.metadata == null || Data.isNull(this.metadata)) ? null : Collections.<String, String>unmodifiableMap((Map<? extends String, ? extends String>)this.metadata);
    }
    
    public Long getGeneration() {
        /*SL:786*/return this.getBlobId().getGeneration();
    }
    
    public Long getMetageneration() {
        /*SL:795*/return this.metageneration;
    }
    
    public Long getDeleteTime() {
        /*SL:800*/return this.deleteTime;
    }
    
    public Long getUpdateTime() {
        /*SL:805*/return this.updateTime;
    }
    
    public Long getCreateTime() {
        /*SL:810*/return this.createTime;
    }
    
    public boolean isDirectory() {
        /*SL:822*/return this.isDirectory;
    }
    
    public CustomerEncryption getCustomerEncryption() {
        /*SL:830*/return this.customerEncryption;
    }
    
    public StorageClass getStorageClass() {
        /*SL:835*/return this.storageClass;
    }
    
    public String getKmsKeyName() {
        /*SL:840*/return this.kmsKeyName;
    }
    
    @BetaApi
    public Boolean getEventBasedHold() {
        /*SL:867*/return Data.isNull(this.eventBasedHold) ? null : this.eventBasedHold;
    }
    
    @BetaApi
    public Boolean getTemporaryHold() {
        /*SL:894*/return Data.isNull(this.temporaryHold) ? null : this.temporaryHold;
    }
    
    @BetaApi
    public Long getRetentionExpirationTime() {
        /*SL:903*/return Data.isNull(this.retentionExpirationTime) ? null : this.retentionExpirationTime;
    }
    
    public Builder toBuilder() {
        /*SL:908*/return new BuilderImpl(this);
    }
    
    @Override
    public String toString() {
        /*SL:913*/return MoreObjects.toStringHelper((Object)this).add("bucket", (Object)this.getBucket()).add(/*EL:914*/"name", (Object)this.getName()).add(/*EL:915*/"generation", (Object)this.getGeneration()).add(/*EL:916*/"size", (Object)this.getSize()).add(/*EL:917*/"content-type", (Object)this.getContentType()).add(/*EL:918*/"metadata", (Object)this.getMetadata()).toString();
    }
    
    @Override
    public int hashCode() {
        /*SL:925*/return Objects.hash(this.blobId);
    }
    
    @Override
    public boolean equals(final Object a1) {
        /*SL:930*/return a1 == this || (a1 != null && a1.getClass().equals(/*EL:932*/BlobInfo.class) && /*EL:933*/Objects.equals(this.toPb(), ((BlobInfo)a1).toPb()));
    }
    
    StorageObject toPb() {
        final StorageObject pb = /*EL:937*/this.blobId.toPb();
        /*SL:938*/if (this.acl != null) {
            /*SL:939*/pb.setAcl(/*EL:940*/(List)Lists.<Acl, Object>transform(this.acl, (Function<? super Acl, ?>)new Function<Acl, ObjectAccessControl>() {
                @Override
                public ObjectAccessControl apply(final Acl a1) {
                    /*SL:945*/return a1.toObjectPb();
                }
            }));
        }
        /*SL:949*/if (this.deleteTime != null) {
            /*SL:950*/pb.setTimeDeleted(new DateTime(this.deleteTime));
        }
        /*SL:952*/if (this.updateTime != null) {
            /*SL:953*/pb.setUpdated(new DateTime(this.updateTime));
        }
        /*SL:955*/if (this.createTime != null) {
            /*SL:956*/pb.setTimeCreated(new DateTime(this.createTime));
        }
        /*SL:958*/if (this.size != null) {
            /*SL:959*/pb.setSize(BigInteger.valueOf(this.size));
        }
        /*SL:961*/if (this.owner != null) {
            /*SL:962*/pb.setOwner(new StorageObject.Owner().setEntity(this.owner.toPb()));
        }
        /*SL:964*/if (this.storageClass != null) {
            /*SL:965*/pb.setStorageClass(this.storageClass.toString());
        }
        Map<String, String> metadata = /*EL:968*/this.metadata;
        /*SL:969*/if (this.metadata != null && !Data.isNull(this.metadata)) {
            /*SL:970*/metadata = (Map<String, String>)Maps.<Object, Object>newHashMapWithExpectedSize(this.metadata.size());
            /*SL:971*/for (final Map.Entry<String, String> v1 : this.metadata.entrySet()) {
                /*SL:972*/metadata.put(v1.getKey(), /*EL:973*/(String)MoreObjects.firstNonNull((Object)v1.getValue(), Data.<Object>nullOf(String.class)));
            }
        }
        /*SL:976*/if (this.customerEncryption != null) {
            /*SL:977*/pb.setCustomerEncryption(this.customerEncryption.toPb());
        }
        /*SL:979*/if (this.retentionExpirationTime != null) {
            /*SL:980*/pb.setRetentionExpirationTime(new DateTime(this.retentionExpirationTime));
        }
        /*SL:983*/pb.setKmsKeyName(this.kmsKeyName);
        /*SL:984*/pb.setEventBasedHold(this.eventBasedHold);
        /*SL:985*/pb.setTemporaryHold(this.temporaryHold);
        /*SL:986*/pb.setMetadata((Map)metadata);
        /*SL:987*/pb.setCacheControl(this.cacheControl);
        /*SL:988*/pb.setContentEncoding(this.contentEncoding);
        /*SL:989*/pb.setCrc32c(this.crc32c);
        /*SL:990*/pb.setContentType(this.contentType);
        /*SL:991*/pb.setMd5Hash(this.md5);
        /*SL:992*/pb.setMediaLink(this.mediaLink);
        /*SL:993*/pb.setMetageneration(this.metageneration);
        /*SL:994*/pb.setContentDisposition(this.contentDisposition);
        /*SL:995*/pb.setComponentCount(this.componentCount);
        /*SL:996*/pb.setContentLanguage(this.contentLanguage);
        /*SL:997*/pb.setEtag(this.etag);
        /*SL:998*/pb.setId(this.generatedId);
        /*SL:999*/pb.setSelfLink(this.selfLink);
        /*SL:1000*/return pb;
    }
    
    public static Builder newBuilder(final BucketInfo a1, final String a2) {
        /*SL:1005*/return newBuilder(a1.getName(), a2);
    }
    
    public static Builder newBuilder(final String a1, final String a2) {
        /*SL:1010*/return newBuilder(BlobId.of(a1, a2));
    }
    
    public static Builder newBuilder(final BucketInfo a1, final String a2, final Long a3) {
        /*SL:1015*/return newBuilder(a1.getName(), a2, a3);
    }
    
    public static Builder newBuilder(final String a1, final String a2, final Long a3) {
        /*SL:1020*/return newBuilder(BlobId.of(a1, a2, a3));
    }
    
    public static Builder newBuilder(final BlobId a1) {
        /*SL:1025*/return new BuilderImpl(a1);
    }
    
    static BlobInfo fromPb(final StorageObject a1) {
        final Builder v1 = newBuilder(/*EL:1029*/BlobId.fromPb(a1));
        /*SL:1030*/if (a1.getCacheControl() != null) {
            /*SL:1031*/v1.setCacheControl(a1.getCacheControl());
        }
        /*SL:1033*/if (a1.getContentEncoding() != null) {
            /*SL:1034*/v1.setContentEncoding(a1.getContentEncoding());
        }
        /*SL:1036*/if (a1.getCrc32c() != null) {
            /*SL:1037*/v1.setCrc32c(a1.getCrc32c());
        }
        /*SL:1039*/if (a1.getContentType() != null) {
            /*SL:1040*/v1.setContentType(a1.getContentType());
        }
        /*SL:1042*/if (a1.getMd5Hash() != null) {
            /*SL:1043*/v1.setMd5(a1.getMd5Hash());
        }
        /*SL:1045*/if (a1.getMediaLink() != null) {
            /*SL:1046*/v1.setMediaLink(a1.getMediaLink());
        }
        /*SL:1048*/if (a1.getMetageneration() != null) {
            /*SL:1049*/v1.setMetageneration(a1.getMetageneration());
        }
        /*SL:1051*/if (a1.getContentDisposition() != null) {
            /*SL:1052*/v1.setContentDisposition(a1.getContentDisposition());
        }
        /*SL:1054*/if (a1.getComponentCount() != null) {
            /*SL:1055*/v1.setComponentCount(a1.getComponentCount());
        }
        /*SL:1057*/if (a1.getContentLanguage() != null) {
            /*SL:1058*/v1.setContentLanguage(a1.getContentLanguage());
        }
        /*SL:1060*/if (a1.getEtag() != null) {
            /*SL:1061*/v1.setEtag(a1.getEtag());
        }
        /*SL:1063*/if (a1.getId() != null) {
            /*SL:1064*/v1.setGeneratedId(a1.getId());
        }
        /*SL:1066*/if (a1.getSelfLink() != null) {
            /*SL:1067*/v1.setSelfLink(a1.getSelfLink());
        }
        /*SL:1069*/if (a1.getMetadata() != null) {
            /*SL:1070*/v1.setMetadata(a1.getMetadata());
        }
        /*SL:1072*/if (a1.getTimeDeleted() != null) {
            /*SL:1073*/v1.setDeleteTime(a1.getTimeDeleted().getValue());
        }
        /*SL:1075*/if (a1.getUpdated() != null) {
            /*SL:1076*/v1.setUpdateTime(a1.getUpdated().getValue());
        }
        /*SL:1078*/if (a1.getTimeCreated() != null) {
            /*SL:1079*/v1.setCreateTime(a1.getTimeCreated().getValue());
        }
        /*SL:1081*/if (a1.getSize() != null) {
            /*SL:1082*/v1.setSize(a1.getSize().longValue());
        }
        /*SL:1084*/if (a1.getOwner() != null) {
            /*SL:1085*/v1.setOwner(Acl.Entity.fromPb(a1.getOwner().getEntity()));
        }
        /*SL:1087*/if (a1.getAcl() != null) {
            /*SL:1088*/v1.setAcl(/*EL:1089*/Lists.<Object, Acl>transform((List<Object>)a1.getAcl(), /*EL:1090*/(Function<? super Object, ? extends Acl>)new Function<ObjectAccessControl, Acl>() {
                @Override
                public Acl apply(final ObjectAccessControl a1) {
                    /*SL:1094*/return Acl.fromPb(a1);
                }
            }));
        }
        /*SL:1098*/if (a1.containsKey((Object)"isDirectory")) {
            /*SL:1099*/v1.setIsDirectory(Boolean.TRUE);
        }
        /*SL:1101*/if (a1.getCustomerEncryption() != null) {
            /*SL:1102*/v1.setCustomerEncryption(/*EL:1103*/CustomerEncryption.fromPb(a1.getCustomerEncryption()));
        }
        /*SL:1105*/if (a1.getStorageClass() != null) {
            /*SL:1106*/v1.setStorageClass(StorageClass.valueOf(a1.getStorageClass()));
        }
        /*SL:1108*/if (a1.getKmsKeyName() != null) {
            /*SL:1109*/v1.setKmsKeyName(a1.getKmsKeyName());
        }
        /*SL:1111*/if (a1.getEventBasedHold() != null) {
            /*SL:1112*/v1.setEventBasedHold(a1.getEventBasedHold());
        }
        /*SL:1114*/if (a1.getTemporaryHold() != null) {
            /*SL:1115*/v1.setTemporaryHold(a1.getTemporaryHold());
        }
        /*SL:1117*/if (a1.getRetentionExpirationTime() != null) {
            /*SL:1118*/v1.setRetentionExpirationTime(a1.getRetentionExpirationTime().getValue());
        }
        /*SL:1120*/return v1.build();
    }
    
    static {
        INFO_TO_PB_FUNCTION = new Function<BlobInfo, StorageObject>() {
            @Override
            public StorageObject apply(final BlobInfo a1) {
                /*SL:58*/return a1.toPb();
            }
        };
    }
    
    public static final class ImmutableEmptyMap<K, V> extends AbstractMap<K, V>
    {
        @Override
        public Set<Map.Entry<K, V>> entrySet() {
            /*SL:97*/return (Set<Map.Entry<K, V>>)ImmutableSet.<Object>of();
        }
    }
    
    public static class CustomerEncryption implements Serializable
    {
        private static final long serialVersionUID = -2133042982786959351L;
        private final String encryptionAlgorithm;
        private final String keySha256;
        
        CustomerEncryption(final String a1, final String a2) {
            this.encryptionAlgorithm = a1;
            this.keySha256 = a2;
        }
        
        public String getEncryptionAlgorithm() {
            /*SL:119*/return this.encryptionAlgorithm;
        }
        
        public String getKeySha256() {
            /*SL:124*/return this.keySha256;
        }
        
        @Override
        public String toString() {
            /*SL:129*/return MoreObjects.toStringHelper((Object)this).add("encryptionAlgorithm", (Object)this.getEncryptionAlgorithm()).add(/*EL:130*/"keySha256", (Object)this.getKeySha256()).toString();
        }
        
        @Override
        public final int hashCode() {
            /*SL:137*/return Objects.hash(this.encryptionAlgorithm, this.keySha256);
        }
        
        @Override
        public final boolean equals(final Object a1) {
            /*SL:142*/return a1 == this || (a1 != null && a1.getClass().equals(/*EL:144*/CustomerEncryption.class) && /*EL:145*/Objects.equals(this.toPb(), ((CustomerEncryption)a1).toPb()));
        }
        
        StorageObject.CustomerEncryption toPb() {
            /*SL:149*/return new StorageObject.CustomerEncryption().setEncryptionAlgorithm(this.encryptionAlgorithm).setKeySha256(/*EL:150*/this.keySha256);
        }
        
        static CustomerEncryption fromPb(final StorageObject.CustomerEncryption a1) {
            /*SL:155*/return /*EL:156*/new CustomerEncryption(a1.getEncryptionAlgorithm(), a1.getKeySha256());
        }
    }
    
    public abstract static class Builder
    {
        public abstract Builder setBlobId(final BlobId p0);
        
        abstract Builder setGeneratedId(final String p0);
        
        public abstract Builder setContentType(final String p0);
        
        public abstract Builder setContentDisposition(final String p0);
        
        public abstract Builder setContentLanguage(final String p0);
        
        public abstract Builder setContentEncoding(final String p0);
        
        abstract Builder setComponentCount(final Integer p0);
        
        public abstract Builder setCacheControl(final String p0);
        
        public abstract Builder setAcl(final List<Acl> p0);
        
        abstract Builder setOwner(final Acl.Entity p0);
        
        abstract Builder setSize(final Long p0);
        
        abstract Builder setEtag(final String p0);
        
        abstract Builder setSelfLink(final String p0);
        
        public abstract Builder setMd5(final String p0);
        
        public abstract Builder setMd5FromHexString(final String p0);
        
        public abstract Builder setCrc32c(final String p0);
        
        public abstract Builder setCrc32cFromHexString(final String p0);
        
        abstract Builder setMediaLink(final String p0);
        
        public abstract Builder setStorageClass(final StorageClass p0);
        
        public abstract Builder setMetadata(final Map<String, String> p0);
        
        abstract Builder setMetageneration(final Long p0);
        
        abstract Builder setDeleteTime(final Long p0);
        
        abstract Builder setUpdateTime(final Long p0);
        
        abstract Builder setCreateTime(final Long p0);
        
        abstract Builder setIsDirectory(final boolean p0);
        
        abstract Builder setCustomerEncryption(final CustomerEncryption p0);
        
        abstract Builder setKmsKeyName(final String p0);
        
        @BetaApi
        public abstract Builder setEventBasedHold(final Boolean p0);
        
        @BetaApi
        public abstract Builder setTemporaryHold(final Boolean p0);
        
        @BetaApi
        abstract Builder setRetentionExpirationTime(final Long p0);
        
        public abstract BlobInfo build();
    }
    
    static final class BuilderImpl extends Builder
    {
        private BlobId blobId;
        private String generatedId;
        private String contentType;
        private String contentEncoding;
        private String contentDisposition;
        private String contentLanguage;
        private Integer componentCount;
        private String cacheControl;
        private List<Acl> acl;
        private Acl.Entity owner;
        private Long size;
        private String etag;
        private String selfLink;
        private String md5;
        private String crc32c;
        private String mediaLink;
        private Map<String, String> metadata;
        private Long metageneration;
        private Long deleteTime;
        private Long updateTime;
        private Long createTime;
        private Boolean isDirectory;
        private CustomerEncryption customerEncryption;
        private StorageClass storageClass;
        private String kmsKeyName;
        private Boolean eventBasedHold;
        private Boolean temporaryHold;
        private Long retentionExpirationTime;
        
        BuilderImpl(final BlobId a1) {
            this.blobId = a1;
        }
        
        BuilderImpl(final BlobInfo a1) {
            this.blobId = a1.blobId;
            this.generatedId = a1.generatedId;
            this.cacheControl = a1.cacheControl;
            this.contentEncoding = a1.contentEncoding;
            this.contentType = a1.contentType;
            this.contentDisposition = a1.contentDisposition;
            this.contentLanguage = a1.contentLanguage;
            this.componentCount = a1.componentCount;
            this.customerEncryption = a1.customerEncryption;
            this.acl = a1.acl;
            this.owner = a1.owner;
            this.size = a1.size;
            this.etag = a1.etag;
            this.selfLink = a1.selfLink;
            this.md5 = a1.md5;
            this.crc32c = a1.crc32c;
            this.mediaLink = a1.mediaLink;
            this.metadata = a1.metadata;
            this.metageneration = a1.metageneration;
            this.deleteTime = a1.deleteTime;
            this.updateTime = a1.updateTime;
            this.createTime = a1.createTime;
            this.isDirectory = a1.isDirectory;
            this.storageClass = a1.storageClass;
            this.kmsKeyName = a1.kmsKeyName;
            this.eventBasedHold = a1.eventBasedHold;
            this.temporaryHold = a1.temporaryHold;
            this.retentionExpirationTime = a1.retentionExpirationTime;
        }
        
        @Override
        public Builder setBlobId(final BlobId a1) {
            /*SL:363*/this.blobId = Preconditions.<BlobId>checkNotNull(a1);
            /*SL:364*/return this;
        }
        
        @Override
        Builder setGeneratedId(final String a1) {
            /*SL:369*/this.generatedId = a1;
            /*SL:370*/return this;
        }
        
        @Override
        public Builder setContentType(final String a1) {
            /*SL:375*/this.contentType = (String)MoreObjects.firstNonNull((Object)a1, Data.<Object>nullOf(String.class));
            /*SL:376*/return this;
        }
        
        @Override
        public Builder setContentDisposition(final String a1) {
            /*SL:381*/this.contentDisposition = (String)MoreObjects.firstNonNull((Object)a1, Data.<Object>nullOf(String.class));
            /*SL:382*/return this;
        }
        
        @Override
        public Builder setContentLanguage(final String a1) {
            /*SL:387*/this.contentLanguage = (String)MoreObjects.firstNonNull((Object)a1, Data.<Object>nullOf(String.class));
            /*SL:388*/return this;
        }
        
        @Override
        public Builder setContentEncoding(final String a1) {
            /*SL:393*/this.contentEncoding = (String)MoreObjects.firstNonNull((Object)a1, Data.<Object>nullOf(String.class));
            /*SL:394*/return this;
        }
        
        @Override
        Builder setComponentCount(final Integer a1) {
            /*SL:399*/this.componentCount = a1;
            /*SL:400*/return this;
        }
        
        @Override
        public Builder setCacheControl(final String a1) {
            /*SL:405*/this.cacheControl = (String)MoreObjects.firstNonNull((Object)a1, Data.<Object>nullOf(String.class));
            /*SL:406*/return this;
        }
        
        @Override
        public Builder setAcl(final List<Acl> a1) {
            /*SL:411*/this.acl = (List<Acl>)((a1 != null) ? ImmutableList.<Object>copyOf((Collection<?>)a1) : null);
            /*SL:412*/return this;
        }
        
        @Override
        Builder setOwner(final Acl.Entity a1) {
            /*SL:417*/this.owner = a1;
            /*SL:418*/return this;
        }
        
        @Override
        Builder setSize(final Long a1) {
            /*SL:423*/this.size = a1;
            /*SL:424*/return this;
        }
        
        @Override
        Builder setEtag(final String a1) {
            /*SL:429*/this.etag = a1;
            /*SL:430*/return this;
        }
        
        @Override
        Builder setSelfLink(final String a1) {
            /*SL:435*/this.selfLink = a1;
            /*SL:436*/return this;
        }
        
        @Override
        public Builder setMd5(final String a1) {
            /*SL:441*/this.md5 = (String)MoreObjects.firstNonNull((Object)a1, Data.<Object>nullOf(String.class));
            /*SL:442*/return this;
        }
        
        @Override
        public Builder setMd5FromHexString(final String a1) {
            /*SL:446*/if (a1 == null) {
                /*SL:447*/return this;
            }
            byte[] v1 = /*EL:449*/new BigInteger(a1, 16).toByteArray();
            final int v2 = /*EL:450*/v1.length - a1.length() / 2;
            /*SL:451*/if (v2 > 0) {
                /*SL:452*/v1 = Arrays.copyOfRange(v1, v2, v1.length);
            }
            /*SL:454*/this.md5 = BaseEncoding.base64().encode(v1);
            /*SL:455*/return this;
        }
        
        @Override
        public Builder setCrc32c(final String a1) {
            /*SL:460*/this.crc32c = (String)MoreObjects.firstNonNull((Object)a1, Data.<Object>nullOf(String.class));
            /*SL:461*/return this;
        }
        
        @Override
        public Builder setCrc32cFromHexString(final String a1) {
            /*SL:466*/if (a1 == null) {
                /*SL:467*/return this;
            }
            byte[] v1 = /*EL:469*/new BigInteger(a1, 16).toByteArray();
            final int v2 = /*EL:470*/v1.length - a1.length() / 2;
            /*SL:471*/if (v2 > 0) {
                /*SL:472*/v1 = Arrays.copyOfRange(v1, v2, v1.length);
            }
            /*SL:474*/this.crc32c = BaseEncoding.base64().encode(v1);
            /*SL:475*/return this;
        }
        
        @Override
        Builder setMediaLink(final String a1) {
            /*SL:480*/this.mediaLink = a1;
            /*SL:481*/return this;
        }
        
        @Override
        public Builder setMetadata(final Map<String, String> a1) {
            /*SL:486*/if (a1 != null) {
                /*SL:487*/this.metadata = new HashMap<String, String>(a1);
            }
            else {
                /*SL:489*/this.metadata = Data.<Map<String, String>>nullOf(ImmutableEmptyMap.class);
            }
            /*SL:491*/return this;
        }
        
        @Override
        public Builder setStorageClass(final StorageClass a1) {
            /*SL:496*/this.storageClass = a1;
            /*SL:497*/return this;
        }
        
        @Override
        Builder setMetageneration(final Long a1) {
            /*SL:502*/this.metageneration = a1;
            /*SL:503*/return this;
        }
        
        @Override
        Builder setDeleteTime(final Long a1) {
            /*SL:508*/this.deleteTime = a1;
            /*SL:509*/return this;
        }
        
        @Override
        Builder setUpdateTime(final Long a1) {
            /*SL:514*/this.updateTime = a1;
            /*SL:515*/return this;
        }
        
        @Override
        Builder setCreateTime(final Long a1) {
            /*SL:520*/this.createTime = a1;
            /*SL:521*/return this;
        }
        
        @Override
        Builder setIsDirectory(final boolean a1) {
            /*SL:526*/this.isDirectory = a1;
            /*SL:527*/return this;
        }
        
        @Override
        Builder setCustomerEncryption(final CustomerEncryption a1) {
            /*SL:532*/this.customerEncryption = a1;
            /*SL:533*/return this;
        }
        
        @Override
        Builder setKmsKeyName(final String a1) {
            /*SL:538*/this.kmsKeyName = a1;
            /*SL:539*/return this;
        }
        
        @Override
        public Builder setEventBasedHold(final Boolean a1) {
            /*SL:544*/this.eventBasedHold = a1;
            /*SL:545*/return this;
        }
        
        @Override
        public Builder setTemporaryHold(final Boolean a1) {
            /*SL:550*/this.temporaryHold = a1;
            /*SL:551*/return this;
        }
        
        @Override
        Builder setRetentionExpirationTime(final Long a1) {
            /*SL:556*/this.retentionExpirationTime = a1;
            /*SL:557*/return this;
        }
        
        @Override
        public BlobInfo build() {
            /*SL:562*/Preconditions.<BlobId>checkNotNull(this.blobId);
            /*SL:563*/return new BlobInfo(this);
        }
    }
}

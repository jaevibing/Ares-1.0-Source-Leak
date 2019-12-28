package com.google.cloud.storage;

import com.google.common.base.Preconditions;
import com.google.api.services.storage.model.StorageObject;
import java.util.Objects;
import com.google.common.base.MoreObjects;
import java.io.Serializable;

public final class BlobId implements Serializable
{
    private static final long serialVersionUID = -6156002883225601925L;
    private final String bucket;
    private final String name;
    private final Long generation;
    
    private BlobId(final String a1, final String a2, final Long a3) {
        this.bucket = a1;
        this.name = a2;
        this.generation = a3;
    }
    
    public String getBucket() {
        /*SL:46*/return this.bucket;
    }
    
    public String getName() {
        /*SL:51*/return this.name;
    }
    
    public Long getGeneration() {
        /*SL:56*/return this.generation;
    }
    
    @Override
    public String toString() {
        /*SL:61*/return MoreObjects.toStringHelper((Object)this).add("bucket", (Object)this.getBucket()).add(/*EL:62*/"name", (Object)this.getName()).add(/*EL:63*/"generation", (Object)this.getGeneration()).toString();
    }
    
    @Override
    public int hashCode() {
        /*SL:70*/return Objects.hash(this.bucket, this.name, this.generation);
    }
    
    @Override
    public boolean equals(final Object a1) {
        /*SL:75*/if (a1 == this) {
            /*SL:76*/return true;
        }
        /*SL:78*/if (a1 == null || !a1.getClass().equals(BlobId.class)) {
            /*SL:79*/return false;
        }
        final BlobId v1 = /*EL:81*/(BlobId)a1;
        /*SL:82*/return Objects.equals(this.bucket, v1.bucket) && /*EL:83*/Objects.equals(this.name, v1.name) && /*EL:84*/Objects.equals(this.generation, v1.generation);
    }
    
    StorageObject toPb() {
        final StorageObject v1 = /*EL:88*/new StorageObject();
        /*SL:89*/v1.setBucket(this.bucket);
        /*SL:90*/v1.setName(this.name);
        /*SL:91*/v1.setGeneration(this.generation);
        /*SL:92*/return v1;
    }
    
    public static BlobId of(final String a1, final String a2) {
        /*SL:102*/return new BlobId(Preconditions.<String>checkNotNull(a1), Preconditions.<String>checkNotNull(a2), null);
    }
    
    public static BlobId of(final String a1, final String a2, final Long a3) {
        /*SL:114*/return new BlobId(Preconditions.<String>checkNotNull(a1), Preconditions.<String>checkNotNull(a2), a3);
    }
    
    static BlobId fromPb(final StorageObject a1) {
        /*SL:118*/return of(a1.getBucket(), /*EL:119*/a1.getName(), a1.getGeneration());
    }
}

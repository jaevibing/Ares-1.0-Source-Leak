package com.google.cloud.storage;

import com.google.common.base.MoreObjects;
import java.util.Objects;
import java.util.Map;
import java.io.Serializable;
import com.google.cloud.RestorableState;
import com.google.api.gax.retrying.ResultRetryAlgorithm;
import com.google.cloud.RetryHelper;
import java.util.concurrent.Callable;
import com.google.cloud.storage.spi.v1.StorageRpc;
import com.google.cloud.Restorable;

public class CopyWriter implements Restorable<CopyWriter>
{
    private final StorageOptions serviceOptions;
    private final StorageRpc storageRpc;
    private StorageRpc.RewriteResponse rewriteResponse;
    
    CopyWriter(final StorageOptions a1, final StorageRpc.RewriteResponse a2) {
        this.serviceOptions = a1;
        this.rewriteResponse = a2;
        this.storageRpc = a1.getStorageRpcV1();
    }
    
    public Blob getResult() {
        /*SL:74*/while (!this.isDone()) {
            /*SL:75*/this.copyChunk();
        }
        /*SL:77*/return Blob.fromPb((Storage)this.serviceOptions.getService(), this.rewriteResponse.result);
    }
    
    public long getBlobSize() {
        /*SL:82*/return this.rewriteResponse.blobSize;
    }
    
    public boolean isDone() {
        /*SL:87*/return this.rewriteResponse.isDone;
    }
    
    public long getTotalBytesCopied() {
        /*SL:92*/return this.rewriteResponse.totalBytesRewritten;
    }
    
    public void copyChunk() {
        /*SL:102*/if (!this.isDone()) {
            try {
                /*SL:105*/this.rewriteResponse = (StorageRpc.RewriteResponse)RetryHelper.runWithRetries((Callable)new Callable<StorageRpc.RewriteResponse>() {
                    @Override
                    public StorageRpc.RewriteResponse call() {
                        /*SL:109*/return CopyWriter.this.storageRpc.continueRewrite(CopyWriter.this.rewriteResponse);
                    }
                }, this.serviceOptions.getRetrySettings(), (ResultRetryAlgorithm)StorageImpl.EXCEPTION_HANDLER, /*EL:112*/this.serviceOptions.getClock());
            }
            catch (RetryHelper.RetryHelperException v1) {
                /*SL:116*/throw StorageException.translateAndThrow(v1);
            }
        }
    }
    
    public RestorableState<CopyWriter> capture() {
        /*SL:123*/return StateImpl.newBuilder(this.serviceOptions, /*EL:125*/BlobId.fromPb(this.rewriteResponse.rewriteRequest.source), this.rewriteResponse.rewriteRequest.sourceOptions, this.rewriteResponse.rewriteRequest.overrideInfo, /*EL:128*/BlobInfo.fromPb(this.rewriteResponse.rewriteRequest.target), this.rewriteResponse.rewriteRequest.targetOptions).setResult((this.rewriteResponse.result != null) ? /*EL:130*/BlobInfo.fromPb(this.rewriteResponse.result) : null).setBlobSize(this.getBlobSize()).setIsDone(/*EL:131*/this.isDone()).setMegabytesCopiedPerChunk(/*EL:132*/this.rewriteResponse.rewriteRequest.megabytesRewrittenPerCall).setRewriteToken(/*EL:133*/this.rewriteResponse.rewriteToken).setTotalBytesRewritten(/*EL:134*/this.getTotalBytesCopied()).build();
    }
    
    static class StateImpl implements RestorableState<CopyWriter>, Serializable
    {
        private static final long serialVersionUID = 1693964441435822700L;
        private final StorageOptions serviceOptions;
        private final BlobId source;
        private final Map<StorageRpc.Option, ?> sourceOptions;
        private final boolean overrideInfo;
        private final BlobInfo target;
        private final Map<StorageRpc.Option, ?> targetOptions;
        private final BlobInfo result;
        private final long blobSize;
        private final boolean isDone;
        private final String rewriteToken;
        private final long totalBytesCopied;
        private final Long megabytesCopiedPerChunk;
        
        StateImpl(final Builder a1) {
            this.serviceOptions = a1.serviceOptions;
            this.source = a1.source;
            this.sourceOptions = a1.sourceOptions;
            this.overrideInfo = a1.overrideInfo;
            this.target = a1.target;
            this.targetOptions = a1.targetOptions;
            this.result = a1.result;
            this.blobSize = a1.blobSize;
            this.isDone = a1.isDone;
            this.rewriteToken = a1.rewriteToken;
            this.totalBytesCopied = a1.totalBytesCopied;
            this.megabytesCopiedPerChunk = a1.megabytesCopiedPerChunk;
        }
        
        static Builder newBuilder(final StorageOptions a1, final BlobId a2, final Map<StorageRpc.Option, ?> a3, final boolean a4, final BlobInfo a5, final Map<StorageRpc.Option, ?> a6) {
            /*SL:243*/return new Builder(a1, a2, (Map)a3, a4, a5, (Map)a6);
        }
        
        public CopyWriter restore() {
            final StorageRpc.RewriteRequest v1 = /*EL:253*/new StorageRpc.RewriteRequest(this.source.toPb(), this.sourceOptions, this.overrideInfo, this.target.toPb(), this.targetOptions, this.megabytesCopiedPerChunk);
            final StorageRpc.RewriteResponse v2 = /*EL:256*/new StorageRpc.RewriteResponse(v1, (this.result != null) ? this.result.toPb() : /*EL:259*/null, this.blobSize, this.isDone, this.rewriteToken, this.totalBytesCopied);
            /*SL:264*/return new CopyWriter(this.serviceOptions, v2);
        }
        
        @Override
        public int hashCode() {
            /*SL:269*/return Objects.hash(this.serviceOptions, this.source, this.sourceOptions, this.overrideInfo, /*EL:273*/this.target, this.targetOptions, this.result, this.blobSize, /*EL:277*/this.isDone, /*EL:278*/this.megabytesCopiedPerChunk, this.rewriteToken, this.totalBytesCopied);
        }
        
        @Override
        public boolean equals(final Object a1) {
            /*SL:286*/if (a1 == null) {
                /*SL:287*/return false;
            }
            /*SL:289*/if (!(a1 instanceof StateImpl)) {
                /*SL:290*/return false;
            }
            final StateImpl v1 = /*EL:292*/(StateImpl)a1;
            /*SL:293*/return Objects.equals(this.serviceOptions, v1.serviceOptions) && /*EL:294*/Objects.equals(this.source, v1.source) && /*EL:295*/Objects.equals(this.sourceOptions, v1.sourceOptions) && /*EL:296*/Objects.equals(this.overrideInfo, v1.overrideInfo) && /*EL:297*/Objects.equals(this.target, v1.target) && /*EL:298*/Objects.equals(this.targetOptions, v1.targetOptions) && /*EL:299*/Objects.equals(this.result, v1.result) && /*EL:300*/Objects.equals(this.rewriteToken, v1.rewriteToken) && /*EL:301*/Objects.equals(this.megabytesCopiedPerChunk, v1.megabytesCopiedPerChunk) && this.blobSize == v1.blobSize && this.isDone == v1.isDone && this.totalBytesCopied == v1.totalBytesCopied;
        }
        
        @Override
        public String toString() {
            /*SL:309*/return MoreObjects.toStringHelper((Object)this).add("source", (Object)this.source).add(/*EL:310*/"overrideInfo", this.overrideInfo).add(/*EL:311*/"target", (Object)this.target).add(/*EL:312*/"result", (Object)this.result).add(/*EL:313*/"blobSize", this.blobSize).add(/*EL:314*/"isDone", this.isDone).add(/*EL:315*/"rewriteToken", (Object)this.rewriteToken).add(/*EL:316*/"totalBytesCopied", this.totalBytesCopied).add(/*EL:317*/"megabytesCopiedPerChunk", (Object)this.megabytesCopiedPerChunk).toString();
        }
        
        static class Builder
        {
            private final StorageOptions serviceOptions;
            private final BlobId source;
            private final Map<StorageRpc.Option, ?> sourceOptions;
            private final boolean overrideInfo;
            private final BlobInfo target;
            private final Map<StorageRpc.Option, ?> targetOptions;
            private BlobInfo result;
            private long blobSize;
            private boolean isDone;
            private String rewriteToken;
            private long totalBytesCopied;
            private Long megabytesCopiedPerChunk;
            
            private Builder(final StorageOptions a1, final BlobId a2, final Map<StorageRpc.Option, ?> a3, final boolean a4, final BlobInfo a5, final Map<StorageRpc.Option, ?> a6) {
                this.serviceOptions = a1;
                this.source = a2;
                this.sourceOptions = a3;
                this.overrideInfo = a4;
                this.target = a5;
                this.targetOptions = a6;
            }
            
            Builder setResult(final BlobInfo a1) {
                /*SL:202*/this.result = a1;
                /*SL:203*/return this;
            }
            
            Builder setBlobSize(final long a1) {
                /*SL:207*/this.blobSize = a1;
                /*SL:208*/return this;
            }
            
            Builder setIsDone(final boolean a1) {
                /*SL:212*/this.isDone = a1;
                /*SL:213*/return this;
            }
            
            Builder setRewriteToken(final String a1) {
                /*SL:217*/this.rewriteToken = a1;
                /*SL:218*/return this;
            }
            
            Builder setTotalBytesRewritten(final long a1) {
                /*SL:222*/this.totalBytesCopied = a1;
                /*SL:223*/return this;
            }
            
            Builder setMegabytesCopiedPerChunk(final Long a1) {
                /*SL:227*/this.megabytesCopiedPerChunk = a1;
                /*SL:228*/return this;
            }
            
            RestorableState<CopyWriter> build() {
                /*SL:232*/return (RestorableState<CopyWriter>)new StateImpl(this);
            }
        }
    }
}

package com.google.cloud.storage;

import com.google.cloud.Restorable;
import com.google.common.base.MoreObjects;
import java.io.Serializable;
import java.util.Objects;
import com.google.api.gax.retrying.ResultRetryAlgorithm;
import com.google.cloud.RetryHelper;
import java.util.concurrent.Callable;
import com.google.cloud.Tuple;
import java.nio.ByteBuffer;
import java.io.IOException;
import java.nio.channels.ClosedChannelException;
import com.google.cloud.RestorableState;
import com.google.api.services.storage.model.StorageObject;
import com.google.cloud.storage.spi.v1.StorageRpc;
import java.util.Map;
import com.google.cloud.ReadChannel;

class BlobReadChannel implements ReadChannel
{
    private static final int DEFAULT_CHUNK_SIZE = 2097152;
    private final StorageOptions serviceOptions;
    private final BlobId blob;
    private final Map<StorageRpc.Option, ?> requestOptions;
    private String lastEtag;
    private long position;
    private boolean isOpen;
    private boolean endOfStream;
    private int chunkSize;
    private final StorageRpc storageRpc;
    private final StorageObject storageObject;
    private int bufferPos;
    private byte[] buffer;
    
    BlobReadChannel(final StorageOptions a1, final BlobId a2, final Map<StorageRpc.Option, ?> a3) {
        this.chunkSize = 2097152;
        this.serviceOptions = a1;
        this.blob = a2;
        this.requestOptions = a3;
        this.isOpen = true;
        this.storageRpc = a1.getStorageRpcV1();
        this.storageObject = a2.toPb();
    }
    
    public RestorableState<ReadChannel> capture() {
        final StateImpl.Builder v1 = /*EL:68*/StateImpl.builder(this.serviceOptions, this.blob, this.requestOptions).setPosition(this.position).setIsOpen(/*EL:69*/this.isOpen).setEndOfStream(/*EL:70*/this.endOfStream).setChunkSize(/*EL:71*/this.chunkSize);
        /*SL:73*/if (this.buffer != null) {
            /*SL:74*/v1.setPosition(this.position + this.bufferPos);
            /*SL:75*/v1.setEndOfStream(false);
        }
        /*SL:77*/return v1.build();
    }
    
    public boolean isOpen() {
        /*SL:82*/return this.isOpen;
    }
    
    public void close() {
        /*SL:87*/if (this.isOpen) {
            /*SL:88*/this.buffer = null;
            /*SL:89*/this.isOpen = false;
        }
    }
    
    private void validateOpen() throws ClosedChannelException {
        /*SL:94*/if (!this.isOpen) {
            /*SL:95*/throw new ClosedChannelException();
        }
    }
    
    public void seek(final long a1) throws IOException {
        /*SL:101*/this.validateOpen();
        /*SL:102*/this.position = a1;
        /*SL:103*/this.buffer = null;
        /*SL:104*/this.bufferPos = 0;
        /*SL:105*/this.endOfStream = false;
    }
    
    public void setChunkSize(final int a1) {
        /*SL:110*/this.chunkSize = ((a1 <= 0) ? 2097152 : a1);
    }
    
    public int read(final ByteBuffer v-1) throws IOException {
        /*SL:115*/this.validateOpen();
        /*SL:116*/if (this.buffer == null) {
            /*SL:117*/if (this.endOfStream) {
                /*SL:118*/return -1;
            }
            final int v0 = /*EL:120*/Math.max(v-1.remaining(), this.chunkSize);
            try {
                final Tuple<String, byte[]> v = /*EL:123*/(Tuple<String, byte[]>)RetryHelper.runWithRetries((Callable)new Callable<Tuple<String, byte[]>>() {
                    @Override
                    public Tuple<String, byte[]> call() {
                        /*SL:127*/return BlobReadChannel.this.storageRpc.read(BlobReadChannel.this.storageObject, BlobReadChannel.this.requestOptions, BlobReadChannel.this.position, v0);
                    }
                }, this.serviceOptions.getRetrySettings(), (ResultRetryAlgorithm)StorageImpl.EXCEPTION_HANDLER, /*EL:130*/this.serviceOptions.getClock());
                /*SL:133*/if (((byte[])v.y()).length > 0 && this.lastEtag != null && !Objects.equals(v.x(), this.lastEtag)) {
                    final StringBuilder a1 = /*EL:134*/new StringBuilder();
                    /*SL:135*/a1.append("Blob ").append(this.blob).append(" was updated while reading");
                    /*SL:136*/throw new StorageException(0, a1.toString());
                }
                /*SL:138*/this.lastEtag = (String)v.x();
                /*SL:139*/this.buffer = (byte[])v.y();
            }
            catch (RetryHelper.RetryHelperException v2) {
                /*SL:141*/throw StorageException.translateAndThrow(v2);
            }
            /*SL:143*/if (v0 > this.buffer.length) {
                /*SL:144*/this.endOfStream = true;
                /*SL:145*/if (this.buffer.length == 0) {
                    /*SL:146*/this.buffer = null;
                    /*SL:147*/return -1;
                }
            }
        }
        final int v0 = /*EL:151*/Math.min(this.buffer.length - this.bufferPos, v-1.remaining());
        /*SL:152*/v-1.put(this.buffer, this.bufferPos, v0);
        /*SL:153*/this.bufferPos += v0;
        /*SL:154*/if (this.bufferPos >= this.buffer.length) {
            /*SL:155*/this.position += this.buffer.length;
            /*SL:156*/this.buffer = null;
            /*SL:157*/this.bufferPos = 0;
        }
        /*SL:159*/return v0;
    }
    
    static class StateImpl implements RestorableState<ReadChannel>, Serializable
    {
        private static final long serialVersionUID = 3889420316004453706L;
        private final StorageOptions serviceOptions;
        private final BlobId blob;
        private final Map<StorageRpc.Option, ?> requestOptions;
        private final String lastEtag;
        private final long position;
        private final boolean isOpen;
        private final boolean endOfStream;
        private final int chunkSize;
        
        StateImpl(final Builder a1) {
            this.serviceOptions = a1.serviceOptions;
            this.blob = a1.blob;
            this.requestOptions = a1.requestOptions;
            this.lastEtag = a1.lastEtag;
            this.position = a1.position;
            this.isOpen = a1.isOpen;
            this.endOfStream = a1.endOfStream;
            this.chunkSize = a1.chunkSize;
        }
        
        static Builder builder(final StorageOptions a1, final BlobId a2, final Map<StorageRpc.Option, ?> a3) {
            /*SL:234*/return new Builder(a1, a2, (Map)a3);
        }
        
        public ReadChannel restore() {
            final BlobReadChannel v1 = /*EL:239*/new BlobReadChannel(this.serviceOptions, this.blob, this.requestOptions);
            /*SL:240*/v1.lastEtag = this.lastEtag;
            /*SL:241*/v1.position = this.position;
            /*SL:242*/v1.isOpen = this.isOpen;
            /*SL:243*/v1.endOfStream = this.endOfStream;
            /*SL:244*/v1.chunkSize = this.chunkSize;
            /*SL:245*/return (ReadChannel)v1;
        }
        
        @Override
        public int hashCode() {
            /*SL:250*/return Objects.hash(this.serviceOptions, this.blob, this.requestOptions, this.lastEtag, this.position, /*EL:251*/this.isOpen, this.endOfStream, this.chunkSize);
        }
        
        @Override
        public boolean equals(final Object a1) {
            /*SL:256*/if (a1 == null) {
                /*SL:257*/return false;
            }
            /*SL:259*/if (!(a1 instanceof StateImpl)) {
                /*SL:260*/return false;
            }
            final StateImpl v1 = /*EL:262*/(StateImpl)a1;
            /*SL:263*/return Objects.equals(this.serviceOptions, v1.serviceOptions) && /*EL:264*/Objects.equals(this.blob, v1.blob) && /*EL:265*/Objects.equals(this.requestOptions, v1.requestOptions) && /*EL:266*/Objects.equals(this.lastEtag, v1.lastEtag) && this.position == v1.position && this.isOpen == v1.isOpen && this.endOfStream == v1.endOfStream && this.chunkSize == v1.chunkSize;
        }
        
        @Override
        public String toString() {
            /*SL:275*/return MoreObjects.toStringHelper((Object)this).add("blob", (Object)this.blob).add(/*EL:276*/"position", this.position).add(/*EL:277*/"isOpen", this.isOpen).add(/*EL:278*/"endOfStream", this.endOfStream).toString();
        }
        
        static class Builder
        {
            private final StorageOptions serviceOptions;
            private final BlobId blob;
            private final Map<StorageRpc.Option, ?> requestOptions;
            private String lastEtag;
            private long position;
            private boolean isOpen;
            private boolean endOfStream;
            private int chunkSize;
            
            private Builder(final StorageOptions a1, final BlobId a2, final Map<StorageRpc.Option, ?> a3) {
                this.serviceOptions = a1;
                this.blob = a2;
                this.requestOptions = a3;
            }
            
            Builder setLastEtag(final String a1) {
                /*SL:203*/this.lastEtag = a1;
                /*SL:204*/return this;
            }
            
            Builder setPosition(final long a1) {
                /*SL:208*/this.position = a1;
                /*SL:209*/return this;
            }
            
            Builder setIsOpen(final boolean a1) {
                /*SL:213*/this.isOpen = a1;
                /*SL:214*/return this;
            }
            
            Builder setEndOfStream(final boolean a1) {
                /*SL:218*/this.endOfStream = a1;
                /*SL:219*/return this;
            }
            
            Builder setChunkSize(final int a1) {
                /*SL:223*/this.chunkSize = a1;
                /*SL:224*/return this;
            }
            
            RestorableState<ReadChannel> build() {
                /*SL:228*/return (RestorableState<ReadChannel>)new StateImpl(this);
            }
        }
    }
}

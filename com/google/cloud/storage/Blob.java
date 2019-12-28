package com.google.cloud.storage;

import com.google.common.io.BaseEncoding;
import java.security.Key;
import java.io.ObjectInputStream;
import java.util.Objects;
import java.util.List;
import java.net.URL;
import java.util.concurrent.TimeUnit;
import com.google.cloud.WriteChannel;
import com.google.cloud.ReadChannel;
import java.util.Arrays;
import com.google.common.base.Preconditions;
import com.google.api.gax.retrying.ResultRetryAlgorithm;
import java.util.concurrent.Callable;
import com.google.cloud.RetryHelper;
import java.util.concurrent.Executors;
import java.util.Map;
import com.google.cloud.storage.spi.v1.StorageRpc;
import com.google.common.io.CountingOutputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import com.google.api.services.storage.model.StorageObject;
import com.google.cloud.Tuple;
import com.google.common.base.Function;

public class Blob extends BlobInfo
{
    private static final long serialVersionUID = -6806832496717441434L;
    private final StorageOptions options;
    private transient Storage storage;
    static final Function<Tuple<Storage, StorageObject>, Blob> BLOB_FROM_PB_FUNCTION;
    private static final int DEFAULT_CHUNK_SIZE = 2097152;
    
    public void downloadTo(final Path v2, final BlobSourceOption... v3) {
        try (/*SL:216*/final OutputStream a1 = Files.newOutputStream(v2, new OpenOption[0])) {
            /*SL:217*/this.downloadTo(a1, v3);
        }
        catch (IOException a2) {
            /*SL:219*/throw new StorageException(a2);
        }
    }
    
    public void downloadTo(final OutputStream a1, final BlobSourceOption... a2) {
        final CountingOutputStream v1 = /*EL:230*/new CountingOutputStream(a1);
        final StorageRpc v2 = /*EL:231*/this.options.getStorageRpcV1();
        final Map<StorageRpc.Option, ?> v3 = /*EL:232*/StorageImpl.optionMap(this.getBlobId(), (Option[])a2);
        /*SL:233*/RetryHelper.runWithRetries(/*EL:234*/(Callable)Executors.callable(new Runnable() {
            @Override
            public void run() {
                /*SL:238*/v2.read(Blob.this.getBlobId().toPb(), /*EL:243*/v3, v1.getCount(), v1);
            }
        }), this.options.getRetrySettings(), (ResultRetryAlgorithm)StorageImpl.EXCEPTION_HANDLER, /*EL:245*/this.options.getClock());
    }
    
    public void downloadTo(final Path a1) {
        /*SL:260*/this.downloadTo(a1, new BlobSourceOption[0]);
    }
    
    Blob(final Storage a1, final BuilderImpl a2) {
        super(a2);
        this.storage = Preconditions.<Storage>checkNotNull(a1);
        /*SL:261*/this.options = (StorageOptions)a1.getOptions();
    }
    
    public boolean exists(final BlobSourceOption... a1) {
        final int v1 = /*EL:485*/a1.length;
        final Storage.BlobGetOption[] v2 = /*EL:486*/Arrays.<Storage.BlobGetOption>copyOf(BlobSourceOption.toGetOptions(this, a1), v1 + 1);
        /*SL:487*/v2[v1] = Storage.BlobGetOption.fields(new Storage.BlobField[0]);
        /*SL:488*/return this.storage.get(this.getBlobId(), v2) != null;
    }
    
    public byte[] getContent(final BlobSourceOption... a1) {
        /*SL:505*/return this.storage.readAllBytes(this.getBlobId(), BlobSourceOption.toSourceOptions(this, a1));
    }
    
    public Blob reload(final BlobSourceOption... a1) {
        /*SL:526*/return this.storage.get(this.getBlobId(), BlobSourceOption.toGetOptions(this, a1));
    }
    
    public Blob update(final Storage.BlobTargetOption... a1) {
        /*SL:571*/return this.storage.update(this, a1);
    }
    
    public boolean delete(final BlobSourceOption... a1) {
        /*SL:594*/return this.storage.delete(this.getBlobId(), BlobSourceOption.toSourceOptions(this, a1));
    }
    
    public CopyWriter copyTo(final BlobId a1, final BlobSourceOption... a2) {
        final Storage.CopyRequest v1 = /*EL:618*/Storage.CopyRequest.newBuilder().setSource(this.getBucket(), /*EL:619*/this.getName()).setSourceOptions(/*EL:620*/BlobSourceOption.toSourceOptions(this, a2)).setTarget(a1).build();
        /*SL:623*/return this.storage.copy(v1);
    }
    
    public CopyWriter copyTo(final String a1, final BlobSourceOption... a2) {
        /*SL:645*/return this.copyTo(a1, this.getName(), a2);
    }
    
    public CopyWriter copyTo(final String a1, final String a2, final BlobSourceOption... a3) {
        /*SL:679*/return this.copyTo(BlobId.of(a1, a2), a3);
    }
    
    public ReadChannel reader(final BlobSourceOption... a1) {
        /*SL:715*/return this.storage.reader(this.getBlobId(), BlobSourceOption.toSourceOptions(this, a1));
    }
    
    public WriteChannel writer(final Storage.BlobWriteOption... a1) {
        /*SL:740*/return this.storage.writer(this, a1);
    }
    
    public URL signUrl(final long a1, final TimeUnit a2, final Storage.SignUrlOption... a3) {
        /*SL:798*/return this.storage.signUrl(this, a1, a2, a3);
    }
    
    public Acl getAcl(final Acl.Entity a1) {
        /*SL:813*/return this.storage.getAcl(this.getBlobId(), a1);
    }
    
    public boolean deleteAcl(final Acl.Entity a1) {
        /*SL:834*/return this.storage.deleteAcl(this.getBlobId(), a1);
    }
    
    public Acl createAcl(final Acl a1) {
        /*SL:849*/return this.storage.createAcl(this.getBlobId(), a1);
    }
    
    public Acl updateAcl(final Acl a1) {
        /*SL:864*/return this.storage.updateAcl(this.getBlobId(), a1);
    }
    
    public List<Acl> listAcls() {
        /*SL:882*/return this.storage.listAcls(this.getBlobId());
    }
    
    public Storage getStorage() {
        /*SL:887*/return this.storage;
    }
    
    @Override
    public Builder toBuilder() {
        /*SL:892*/return new Builder(this);
    }
    
    @Override
    public final boolean equals(final Object a1) {
        /*SL:897*/if (a1 == this) {
            /*SL:898*/return true;
        }
        /*SL:900*/if (a1 == null || !a1.getClass().equals(Blob.class)) {
            /*SL:901*/return false;
        }
        final Blob v1 = /*EL:903*/(Blob)a1;
        /*SL:904*/return Objects.equals(this.toPb(), v1.toPb()) && Objects.equals(this.options, v1.options);
    }
    
    @Override
    public final int hashCode() {
        /*SL:909*/return Objects.hash(super.hashCode(), this.options);
    }
    
    private void readObject(final ObjectInputStream a1) throws IOException, ClassNotFoundException {
        /*SL:913*/a1.defaultReadObject();
        /*SL:914*/this.storage = (Storage)this.options.getService();
    }
    
    static Blob fromPb(final Storage a1, final StorageObject a2) {
        final BlobInfo v1 = /*EL:918*/BlobInfo.fromPb(a2);
        /*SL:919*/return new Blob(a1, new BuilderImpl(v1));
    }
    
    static {
        BLOB_FROM_PB_FUNCTION = new Function<Tuple<Storage, StorageObject>, Blob>() {
            @Override
            public Blob apply(final Tuple<Storage, StorageObject> a1) {
                /*SL:72*/return Blob.fromPb((Storage)a1.x(), (StorageObject)a1.y());
            }
        };
    }
    
    public static class BlobSourceOption extends Option
    {
        private static final long serialVersionUID = 214616862061934846L;
        
        private BlobSourceOption(final StorageRpc.Option a1) {
            super(a1, null);
        }
        
        private BlobSourceOption(final StorageRpc.Option a1, final Object a2) {
            super(a1, a2);
        }
        
        private Storage.BlobSourceOption toSourceOptions(final BlobInfo a1) {
            /*SL:92*/switch (this.getRpcOption()) {
                case IF_GENERATION_MATCH: {
                    /*SL:94*/return Storage.BlobSourceOption.generationMatch(a1.getGeneration());
                }
                case IF_GENERATION_NOT_MATCH: {
                    /*SL:96*/return Storage.BlobSourceOption.generationNotMatch(a1.getGeneration());
                }
                case IF_METAGENERATION_MATCH: {
                    /*SL:98*/return Storage.BlobSourceOption.metagenerationMatch(a1.getMetageneration());
                }
                case IF_METAGENERATION_NOT_MATCH: {
                    /*SL:100*/return Storage.BlobSourceOption.metagenerationNotMatch(a1.getMetageneration());
                }
                case CUSTOMER_SUPPLIED_KEY: {
                    /*SL:102*/return Storage.BlobSourceOption.decryptionKey((String)this.getValue());
                }
                case USER_PROJECT: {
                    /*SL:104*/return Storage.BlobSourceOption.userProject((String)this.getValue());
                }
                default: {
                    /*SL:106*/throw new AssertionError((Object)"Unexpected enum value");
                }
            }
        }
        
        private Storage.BlobGetOption toGetOption(final BlobInfo a1) {
            /*SL:111*/switch (this.getRpcOption()) {
                case IF_GENERATION_MATCH: {
                    /*SL:113*/return Storage.BlobGetOption.generationMatch(a1.getGeneration());
                }
                case IF_GENERATION_NOT_MATCH: {
                    /*SL:115*/return Storage.BlobGetOption.generationNotMatch(a1.getGeneration());
                }
                case IF_METAGENERATION_MATCH: {
                    /*SL:117*/return Storage.BlobGetOption.metagenerationMatch(a1.getMetageneration());
                }
                case IF_METAGENERATION_NOT_MATCH: {
                    /*SL:119*/return Storage.BlobGetOption.metagenerationNotMatch(a1.getMetageneration());
                }
                case USER_PROJECT: {
                    /*SL:121*/return Storage.BlobGetOption.userProject((String)this.getValue());
                }
                case CUSTOMER_SUPPLIED_KEY: {
                    /*SL:123*/return Storage.BlobGetOption.decryptionKey((String)this.getValue());
                }
                default: {
                    /*SL:125*/throw new AssertionError((Object)"Unexpected enum value");
                }
            }
        }
        
        public static BlobSourceOption generationMatch() {
            /*SL:134*/return new BlobSourceOption(StorageRpc.Option.IF_GENERATION_MATCH);
        }
        
        public static BlobSourceOption generationNotMatch() {
            /*SL:142*/return new BlobSourceOption(StorageRpc.Option.IF_GENERATION_NOT_MATCH);
        }
        
        public static BlobSourceOption metagenerationMatch() {
            /*SL:150*/return new BlobSourceOption(StorageRpc.Option.IF_METAGENERATION_MATCH);
        }
        
        public static BlobSourceOption metagenerationNotMatch() {
            /*SL:158*/return new BlobSourceOption(StorageRpc.Option.IF_METAGENERATION_NOT_MATCH);
        }
        
        public static BlobSourceOption decryptionKey(final Key a1) {
            final String v1 = /*EL:166*/BaseEncoding.base64().encode(a1.getEncoded());
            /*SL:167*/return new BlobSourceOption(StorageRpc.Option.CUSTOMER_SUPPLIED_KEY, v1);
        }
        
        public static BlobSourceOption decryptionKey(final String a1) {
            /*SL:177*/return new BlobSourceOption(StorageRpc.Option.CUSTOMER_SUPPLIED_KEY, a1);
        }
        
        public static BlobSourceOption userProject(final String a1) {
            /*SL:185*/return new BlobSourceOption(StorageRpc.Option.USER_PROJECT, a1);
        }
        
        static Storage.BlobSourceOption[] toSourceOptions(final BlobInfo a2, final BlobSourceOption... v1) {
            final Storage.BlobSourceOption[] v2 = /*EL:190*/new Storage.BlobSourceOption[v1.length];
            int v3 = /*EL:191*/0;
            /*SL:192*/for (final BlobSourceOption a3 : v1) {
                /*SL:193*/v2[v3++] = a3.toSourceOptions(a2);
            }
            /*SL:195*/return v2;
        }
        
        static Storage.BlobGetOption[] toGetOptions(final BlobInfo a2, final BlobSourceOption... v1) {
            final Storage.BlobGetOption[] v2 = /*EL:199*/new Storage.BlobGetOption[v1.length];
            int v3 = /*EL:200*/0;
            /*SL:201*/for (final BlobSourceOption a3 : v1) {
                /*SL:202*/v2[v3++] = a3.toGetOption(a2);
            }
            /*SL:204*/return v2;
        }
    }
    
    public static class Builder extends BlobInfo.Builder
    {
        private final Storage storage;
        private final BuilderImpl infoBuilder;
        
        Builder(final Blob a1) {
            this.storage = a1.getStorage();
            this.infoBuilder = new BuilderImpl(a1);
        }
        
        @Override
        public Builder setBlobId(final BlobId a1) {
            /*SL:276*/this.infoBuilder.setBlobId(a1);
            /*SL:277*/return this;
        }
        
        @Override
        Builder setGeneratedId(final String a1) {
            /*SL:282*/this.infoBuilder.setGeneratedId(a1);
            /*SL:283*/return this;
        }
        
        @Override
        public Builder setContentType(final String a1) {
            /*SL:288*/this.infoBuilder.setContentType(a1);
            /*SL:289*/return this;
        }
        
        @Override
        public Builder setContentDisposition(final String a1) {
            /*SL:294*/this.infoBuilder.setContentDisposition(a1);
            /*SL:295*/return this;
        }
        
        @Override
        public Builder setContentLanguage(final String a1) {
            /*SL:300*/this.infoBuilder.setContentLanguage(a1);
            /*SL:301*/return this;
        }
        
        @Override
        public Builder setContentEncoding(final String a1) {
            /*SL:306*/this.infoBuilder.setContentEncoding(a1);
            /*SL:307*/return this;
        }
        
        @Override
        Builder setComponentCount(final Integer a1) {
            /*SL:312*/this.infoBuilder.setComponentCount(a1);
            /*SL:313*/return this;
        }
        
        @Override
        public Builder setCacheControl(final String a1) {
            /*SL:318*/this.infoBuilder.setCacheControl(a1);
            /*SL:319*/return this;
        }
        
        @Override
        public Builder setAcl(final List<Acl> a1) {
            /*SL:324*/this.infoBuilder.setAcl(a1);
            /*SL:325*/return this;
        }
        
        @Override
        Builder setOwner(final Acl.Entity a1) {
            /*SL:330*/this.infoBuilder.setOwner(a1);
            /*SL:331*/return this;
        }
        
        @Override
        Builder setSize(final Long a1) {
            /*SL:336*/this.infoBuilder.setSize(a1);
            /*SL:337*/return this;
        }
        
        @Override
        Builder setEtag(final String a1) {
            /*SL:342*/this.infoBuilder.setEtag(a1);
            /*SL:343*/return this;
        }
        
        @Override
        Builder setSelfLink(final String a1) {
            /*SL:348*/this.infoBuilder.setSelfLink(a1);
            /*SL:349*/return this;
        }
        
        @Override
        public Builder setMd5(final String a1) {
            /*SL:354*/this.infoBuilder.setMd5(a1);
            /*SL:355*/return this;
        }
        
        @Override
        public Builder setMd5FromHexString(final String a1) {
            /*SL:360*/this.infoBuilder.setMd5FromHexString(a1);
            /*SL:361*/return this;
        }
        
        @Override
        public Builder setCrc32c(final String a1) {
            /*SL:366*/this.infoBuilder.setCrc32c(a1);
            /*SL:367*/return this;
        }
        
        @Override
        public Builder setCrc32cFromHexString(final String a1) {
            /*SL:372*/this.infoBuilder.setCrc32cFromHexString(a1);
            /*SL:373*/return this;
        }
        
        @Override
        Builder setMediaLink(final String a1) {
            /*SL:378*/this.infoBuilder.setMediaLink(a1);
            /*SL:379*/return this;
        }
        
        @Override
        public Builder setMetadata(final Map<String, String> a1) {
            /*SL:384*/this.infoBuilder.setMetadata(a1);
            /*SL:385*/return this;
        }
        
        @Override
        public Builder setStorageClass(final StorageClass a1) {
            /*SL:390*/this.infoBuilder.setStorageClass(a1);
            /*SL:391*/return this;
        }
        
        @Override
        Builder setMetageneration(final Long a1) {
            /*SL:396*/this.infoBuilder.setMetageneration(a1);
            /*SL:397*/return this;
        }
        
        @Override
        Builder setDeleteTime(final Long a1) {
            /*SL:402*/this.infoBuilder.setDeleteTime(a1);
            /*SL:403*/return this;
        }
        
        @Override
        Builder setUpdateTime(final Long a1) {
            /*SL:408*/this.infoBuilder.setUpdateTime(a1);
            /*SL:409*/return this;
        }
        
        @Override
        Builder setCreateTime(final Long a1) {
            /*SL:414*/this.infoBuilder.setCreateTime(a1);
            /*SL:415*/return this;
        }
        
        @Override
        Builder setIsDirectory(final boolean a1) {
            /*SL:420*/this.infoBuilder.setIsDirectory(a1);
            /*SL:421*/return this;
        }
        
        @Override
        Builder setCustomerEncryption(final CustomerEncryption a1) {
            /*SL:426*/this.infoBuilder.setCustomerEncryption(a1);
            /*SL:427*/return this;
        }
        
        @Override
        Builder setKmsKeyName(final String a1) {
            /*SL:432*/this.infoBuilder.setKmsKeyName(a1);
            /*SL:433*/return this;
        }
        
        @Override
        public Builder setEventBasedHold(final Boolean a1) {
            /*SL:438*/this.infoBuilder.setEventBasedHold(a1);
            /*SL:439*/return this;
        }
        
        @Override
        public Builder setTemporaryHold(final Boolean a1) {
            /*SL:444*/this.infoBuilder.setTemporaryHold(a1);
            /*SL:445*/return this;
        }
        
        @Override
        Builder setRetentionExpirationTime(final Long a1) {
            /*SL:450*/this.infoBuilder.setRetentionExpirationTime(a1);
            /*SL:451*/return this;
        }
        
        @Override
        public Blob build() {
            /*SL:456*/return new Blob(this.storage, this.infoBuilder);
        }
    }
}

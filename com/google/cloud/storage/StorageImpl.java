package com.google.cloud.storage;

import com.google.cloud.WriteChannel;
import com.google.common.collect.ImmutableMap;
import java.util.Set;
import java.util.Collection;
import com.google.common.collect.ImmutableSet;
import com.google.api.services.storage.model.TestIamPermissionsResponse;
import com.google.cloud.Policy;
import com.google.api.services.storage.model.HmacKeyMetadata;
import com.google.api.services.storage.model.ObjectAccessControl;
import com.google.api.services.storage.model.BucketAccessControl;
import com.google.cloud.BatchResult;
import java.util.Collections;
import java.io.IOException;
import java.util.EnumMap;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.net.URI;
import com.google.common.net.UrlEscapers;
import com.google.common.base.Strings;
import com.google.common.base.CharMatcher;
import com.google.auth.ServiceAccountSigner;
import com.google.common.collect.Maps;
import java.util.concurrent.TimeUnit;
import java.net.URL;
import com.google.cloud.ReadChannel;
import java.util.Iterator;
import java.util.List;
import com.google.common.collect.Lists;
import com.google.cloud.PageImpl;
import com.google.common.collect.Iterables;
import com.google.common.collect.ImmutableList;
import com.google.api.gax.paging.Page;
import com.google.common.base.Preconditions;
import com.google.api.services.storage.model.StorageObject;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Arrays;
import com.google.common.primitives.Ints;
import com.google.common.hash.Hashing;
import com.google.common.io.BaseEncoding;
import com.google.common.base.MoreObjects;
import com.google.api.gax.retrying.ResultRetryAlgorithm;
import com.google.cloud.RetryHelper;
import java.util.Map;
import java.util.concurrent.Callable;
import com.google.cloud.ServiceOptions;
import com.google.cloud.storage.spi.v1.StorageRpc;
import com.google.cloud.Tuple;
import com.google.common.base.Function;
import com.google.cloud.BaseService;

final class StorageImpl extends BaseService<StorageOptions> implements Storage
{
    private static final byte[] EMPTY_BYTE_ARRAY;
    private static final String EMPTY_BYTE_ARRAY_MD5 = "1B2M2Y8AsgTpgAmY7PhCfg==";
    private static final String EMPTY_BYTE_ARRAY_CRC32C = "AAAAAA==";
    private static final String PATH_DELIMITER = "/";
    private static final String STORAGE_XML_HOST_NAME = "https://storage.googleapis.com";
    private static final Function<Tuple<Storage, Boolean>, Boolean> DELETE_FUNCTION;
    private final StorageRpc storageRpc;
    
    StorageImpl(final StorageOptions a1) {
        super((ServiceOptions)a1);
        this.storageRpc = a1.getStorageRpcV1();
    }
    
    public Bucket create(final BucketInfo v1, final BucketTargetOption... v2) {
        final com.google.api.services.storage.model.Bucket v3 = /*EL:110*/v1.toPb();
        final Map<StorageRpc.Option, ?> v4 = optionMap(/*EL:111*/v1, (Option[])v2);
        try {
            /*SL:113*/return Bucket.fromPb(this, /*EL:115*/(com.google.api.services.storage.model.Bucket)RetryHelper.runWithRetries((Callable)new Callable<com.google.api.services.storage.model.Bucket>() {
                @Override
                public com.google.api.services.storage.model.Bucket call() {
                    /*SL:119*/return StorageImpl.this.storageRpc.create(v3, v4);
                }
            }, ((StorageOptions)this.getOptions()).getRetrySettings(), (ResultRetryAlgorithm)StorageImpl.EXCEPTION_HANDLER, /*EL:122*/((StorageOptions)this.getOptions()).getClock()));
        }
        catch (RetryHelper.RetryHelperException a1) {
            /*SL:126*/throw StorageException.translateAndThrow(a1);
        }
    }
    
    public Blob create(final BlobInfo a1, final BlobTargetOption... a2) {
        final BlobInfo v1 = /*EL:132*/a1.toBuilder().setMd5(/*EL:134*/"1B2M2Y8AsgTpgAmY7PhCfg==").setCrc32c(/*EL:135*/"AAAAAA==").build();
        /*SL:138*/return this.internalCreate(v1, StorageImpl.EMPTY_BYTE_ARRAY, a2);
    }
    
    public Blob create(final BlobInfo a1, byte[] a2, final BlobTargetOption... a3) {
        /*SL:143*/a2 = (byte[])MoreObjects.firstNonNull((Object)a2, (Object)StorageImpl.EMPTY_BYTE_ARRAY);
        final BlobInfo v1 = /*EL:144*/a1.toBuilder().setMd5(/*EL:147*/BaseEncoding.base64().encode(Hashing.md5().hashBytes(a2).asBytes())).setCrc32c(/*EL:149*/BaseEncoding.base64().encode(/*EL:150*/Ints.toByteArray(Hashing.crc32c().hashBytes(a2).asInt()))).build();
        /*SL:152*/return this.internalCreate(v1, a2, a3);
    }
    
    public Blob create(final BlobInfo a1, byte[] a2, final int a3, final int a4, final BlobTargetOption... a5) {
        /*SL:158*/a2 = (byte[])MoreObjects.firstNonNull((Object)a2, (Object)StorageImpl.EMPTY_BYTE_ARRAY);
        final byte[] v1 = /*EL:159*/Arrays.copyOfRange(a2, a3, a3 + a4);
        final BlobInfo v2 = /*EL:160*/a1.toBuilder().setMd5(/*EL:163*/BaseEncoding.base64().encode(Hashing.md5().hashBytes(v1).asBytes())).setCrc32c(/*EL:165*/BaseEncoding.base64().encode(/*EL:166*/Ints.toByteArray(Hashing.crc32c().hashBytes(v1).asInt()))).build();
        /*SL:168*/return this.internalCreate(v2, v1, a5);
    }
    
    @Deprecated
    public Blob create(final BlobInfo a1, final InputStream a2, final BlobWriteOption... a3) {
        final Tuple<BlobInfo, BlobTargetOption[]> v1 = /*EL:174*/BlobTargetOption.convert(a1, a3);
        final StorageObject v2 = /*EL:175*/((BlobInfo)v1.x()).toPb();
        final Map<StorageRpc.Option, ?> v3 = optionMap(/*EL:176*/(BlobInfo)v1.x(), (Option[])v1.y());
        final InputStream v4 = /*EL:178*/(InputStream)MoreObjects.firstNonNull((Object)a2, (Object)new ByteArrayInputStream(StorageImpl.EMPTY_BYTE_ARRAY));
        /*SL:180*/return Blob.fromPb(this, this.storageRpc.create(v2, v4, v3));
    }
    
    private Blob internalCreate(final BlobInfo a3, final byte[] v1, final BlobTargetOption... v2) {
        /*SL:184*/Preconditions.<byte[]>checkNotNull(v1);
        final StorageObject v3 = /*EL:185*/a3.toPb();
        final Map<StorageRpc.Option, ?> v4 = optionMap(/*EL:186*/a3, (Option[])v2);
        try {
            /*SL:188*/return Blob.fromPb(this, /*EL:190*/(StorageObject)RetryHelper.runWithRetries((Callable)new Callable<StorageObject>() {
                @Override
                public StorageObject call() {
                    /*SL:194*/return StorageImpl.this.storageRpc.create(v3, new ByteArrayInputStream(v1), v4);
                }
            }, ((StorageOptions)this.getOptions()).getRetrySettings(), (ResultRetryAlgorithm)StorageImpl.EXCEPTION_HANDLER, /*EL:197*/((StorageOptions)this.getOptions()).getClock()));
        }
        catch (RetryHelper.RetryHelperException a4) {
            /*SL:201*/throw StorageException.translateAndThrow(a4);
        }
    }
    
    public Bucket get(final String v2, final BucketGetOption... v3) {
        final com.google.api.services.storage.model.Bucket v4 = /*EL:207*/BucketInfo.of(v2).toPb();
        final Map<StorageRpc.Option, ?> v5 = optionMap(/*EL:208*/(Option[])v3);
        try {
            final com.google.api.services.storage.model.Bucket a1 = /*EL:211*/(com.google.api.services.storage.model.Bucket)RetryHelper.runWithRetries((Callable)new Callable<com.google.api.services.storage.model.Bucket>() {
                @Override
                public com.google.api.services.storage.model.Bucket call() {
                    /*SL:215*/return StorageImpl.this.storageRpc.get(v4, v5);
                }
            }, ((StorageOptions)this.getOptions()).getRetrySettings(), (ResultRetryAlgorithm)StorageImpl.EXCEPTION_HANDLER, /*EL:218*/((StorageOptions)this.getOptions()).getClock());
            /*SL:221*/return (a1 == null) ? null : Bucket.fromPb(this, a1);
        }
        catch (RetryHelper.RetryHelperException a2) {
            /*SL:223*/throw StorageException.translateAndThrow(a2);
        }
    }
    
    public Blob get(final String a1, final String a2, final BlobGetOption... a3) {
        /*SL:229*/return this.get(BlobId.of(a1, a2), a3);
    }
    
    public Blob get(final BlobId v2, final BlobGetOption... v3) {
        final StorageObject v4 = /*EL:234*/v2.toPb();
        final Map<StorageRpc.Option, ?> v5 = optionMap(/*EL:235*/v2, (Option[])v3);
        try {
            final StorageObject a1 = /*EL:238*/(StorageObject)RetryHelper.runWithRetries((Callable)new Callable<StorageObject>() {
                @Override
                public StorageObject call() {
                    /*SL:242*/return StorageImpl.this.storageRpc.get(v4, v5);
                }
            }, ((StorageOptions)this.getOptions()).getRetrySettings(), (ResultRetryAlgorithm)StorageImpl.EXCEPTION_HANDLER, /*EL:245*/((StorageOptions)this.getOptions()).getClock());
            /*SL:248*/return (a1 == null) ? null : Blob.fromPb(this, a1);
        }
        catch (RetryHelper.RetryHelperException a2) {
            /*SL:250*/throw StorageException.translateAndThrow(a2);
        }
    }
    
    public Blob get(final BlobId a1) {
        /*SL:256*/return this.get(a1, new BlobGetOption[0]);
    }
    
    public Page<Bucket> list(final BucketListOption... a1) {
        /*SL:321*/return listBuckets((StorageOptions)this.getOptions(), optionMap((Option[])a1));
    }
    
    public Page<Blob> list(final String a1, final BlobListOption... a2) {
        /*SL:326*/return listBlobs(a1, (StorageOptions)this.getOptions(), optionMap((Option[])a2));
    }
    
    private static Page<Bucket> listBuckets(final StorageOptions v-3, final Map<StorageRpc.Option, ?> v-2) {
        try {
            final Tuple<String, Iterable<com.google.api.services.storage.model.Bucket>> a1 = /*EL:333*/(Tuple<String, Iterable<com.google.api.services.storage.model.Bucket>>)RetryHelper.runWithRetries((Callable)new Callable<Tuple<String, Iterable<com.google.api.services.storage.model.Bucket>>>() {
                @Override
                public Tuple<String, Iterable<com.google.api.services.storage.model.Bucket>> call() {
                    /*SL:339*/return v-3.getStorageRpcV1().list(v-2);
                }
            }, v-3.getRetrySettings(), (ResultRetryAlgorithm)StorageImpl.EXCEPTION_HANDLER, /*EL:342*/v-3.getClock());
            final String a2 = /*EL:345*/(String)a1.x();
            final Iterable<Bucket> v1 = /*EL:346*/(Iterable<Bucket>)((a1.y() == /*EL:347*/null) ? /*EL:348*/ImmutableList.<Object>of() : /*EL:349*/Iterables.<Object, Object>transform((Iterable<Object>)a1.y(), /*EL:350*/(Function<? super Object, ?>)new Function<com.google.api.services.storage.model.Bucket, Bucket>() {
                @Override
                public Bucket apply(final com.google.api.services.storage.model.Bucket a1) {
                    /*SL:354*/return Bucket.fromPb((Storage)v-3.getService(), a1);
                }
            }));
            /*SL:357*/return (Page<Bucket>)new PageImpl((PageImpl.NextPageFetcher)new BucketPageFetcher(v-3, a2, v-2), a2, (Iterable)v1);
        }
        catch (RetryHelper.RetryHelperException a3) {
            /*SL:360*/throw StorageException.translateAndThrow(a3);
        }
    }
    
    private static Page<Blob> listBlobs(final String v-2, final StorageOptions v-1, final Map<StorageRpc.Option, ?> v0) {
        try {
            final Tuple<String, Iterable<StorageObject>> a1 = /*EL:370*/(Tuple<String, Iterable<StorageObject>>)RetryHelper.runWithRetries((Callable)new Callable<Tuple<String, Iterable<StorageObject>>>() {
                @Override
                public Tuple<String, Iterable<StorageObject>> call() {
                    /*SL:374*/return v-1.getStorageRpcV1().list(v-2, v0);
                }
            }, v-1.getRetrySettings(), (ResultRetryAlgorithm)StorageImpl.EXCEPTION_HANDLER, /*EL:377*/v-1.getClock());
            final String a2 = /*EL:380*/(String)a1.x();
            final Iterable<Blob> a3 = /*EL:381*/(Iterable<Blob>)((a1.y() == /*EL:382*/null) ? /*EL:383*/ImmutableList.<Object>of() : /*EL:384*/Iterables.<Object, Object>transform((Iterable<Object>)a1.y(), /*EL:385*/(Function<? super Object, ?>)new Function<StorageObject, Blob>() {
                @Override
                public Blob apply(final StorageObject a1) {
                    /*SL:389*/return Blob.fromPb((Storage)v-1.getService(), a1);
                }
            }));
            /*SL:392*/return (Page<Blob>)new PageImpl((PageImpl.NextPageFetcher)new BlobPageFetcher(v-2, v-1, a2, v0), a2, (Iterable)a3);
        }
        catch (RetryHelper.RetryHelperException v) {
            /*SL:395*/throw StorageException.translateAndThrow(v);
        }
    }
    
    public Bucket update(final BucketInfo v1, final BucketTargetOption... v2) {
        final com.google.api.services.storage.model.Bucket v3 = /*EL:401*/v1.toPb();
        final Map<StorageRpc.Option, ?> v4 = optionMap(/*EL:402*/v1, (Option[])v2);
        try {
            /*SL:404*/return Bucket.fromPb(this, /*EL:406*/(com.google.api.services.storage.model.Bucket)RetryHelper.runWithRetries((Callable)new Callable<com.google.api.services.storage.model.Bucket>() {
                @Override
                public com.google.api.services.storage.model.Bucket call() {
                    /*SL:410*/return StorageImpl.this.storageRpc.patch(v3, v4);
                }
            }, ((StorageOptions)this.getOptions()).getRetrySettings(), (ResultRetryAlgorithm)StorageImpl.EXCEPTION_HANDLER, /*EL:413*/((StorageOptions)this.getOptions()).getClock()));
        }
        catch (RetryHelper.RetryHelperException a1) {
            /*SL:417*/throw StorageException.translateAndThrow(a1);
        }
    }
    
    public Blob update(final BlobInfo v1, final BlobTargetOption... v2) {
        final StorageObject v3 = /*EL:423*/v1.toPb();
        final Map<StorageRpc.Option, ?> v4 = optionMap(/*EL:424*/v1, (Option[])v2);
        try {
            /*SL:426*/return Blob.fromPb(this, /*EL:428*/(StorageObject)RetryHelper.runWithRetries((Callable)new Callable<StorageObject>() {
                @Override
                public StorageObject call() {
                    /*SL:432*/return StorageImpl.this.storageRpc.patch(v3, v4);
                }
            }, ((StorageOptions)this.getOptions()).getRetrySettings(), (ResultRetryAlgorithm)StorageImpl.EXCEPTION_HANDLER, /*EL:435*/((StorageOptions)this.getOptions()).getClock()));
        }
        catch (RetryHelper.RetryHelperException a1) {
            /*SL:439*/throw StorageException.translateAndThrow(a1);
        }
    }
    
    public Blob update(final BlobInfo a1) {
        /*SL:445*/return this.update(a1, new BlobTargetOption[0]);
    }
    
    public boolean delete(final String v1, final BucketSourceOption... v2) {
        final com.google.api.services.storage.model.Bucket v3 = /*EL:450*/BucketInfo.of(v1).toPb();
        final Map<StorageRpc.Option, ?> v4 = optionMap(/*EL:451*/(Option[])v2);
        try {
            /*SL:453*/return (boolean)RetryHelper.runWithRetries((Callable)new Callable<Boolean>() {
                @Override
                public Boolean call() {
                    /*SL:457*/return StorageImpl.this.storageRpc.delete(v3, v4);
                }
            }, ((StorageOptions)this.getOptions()).getRetrySettings(), (ResultRetryAlgorithm)StorageImpl.EXCEPTION_HANDLER, /*EL:460*/((StorageOptions)this.getOptions()).getClock());
        }
        catch (RetryHelper.RetryHelperException a1) {
            /*SL:464*/throw StorageException.translateAndThrow(a1);
        }
    }
    
    public boolean delete(final String a1, final String a2, final BlobSourceOption... a3) {
        /*SL:470*/return this.delete(BlobId.of(a1, a2), a3);
    }
    
    public boolean delete(final BlobId v1, final BlobSourceOption... v2) {
        final StorageObject v3 = /*EL:475*/v1.toPb();
        final Map<StorageRpc.Option, ?> v4 = optionMap(/*EL:476*/v1, (Option[])v2);
        try {
            /*SL:478*/return (boolean)RetryHelper.runWithRetries((Callable)new Callable<Boolean>() {
                @Override
                public Boolean call() {
                    /*SL:482*/return StorageImpl.this.storageRpc.delete(v3, v4);
                }
            }, ((StorageOptions)this.getOptions()).getRetrySettings(), (ResultRetryAlgorithm)StorageImpl.EXCEPTION_HANDLER, /*EL:485*/((StorageOptions)this.getOptions()).getClock());
        }
        catch (RetryHelper.RetryHelperException a1) {
            /*SL:489*/throw StorageException.translateAndThrow(a1);
        }
    }
    
    public boolean delete(final BlobId a1) {
        /*SL:495*/return this.delete(a1, new BlobSourceOption[0]);
    }
    
    public Blob compose(final ComposeRequest v-3) {
        final List<StorageObject> arrayListWithCapacity = /*EL:501*/(List<StorageObject>)Lists.<Object>newArrayListWithCapacity(v-3.getSourceBlobs().size());
        /*SL:502*/for (final ComposeRequest.SourceBlob a1 : v-3.getSourceBlobs()) {
            /*SL:503*/arrayListWithCapacity.add(/*EL:504*/BlobInfo.newBuilder(/*EL:505*/BlobId.of(v-3.getTarget().getBucket(), /*EL:506*/a1.getName(), /*EL:507*/a1.getGeneration())).build().toPb());
        }
        final StorageObject pb = /*EL:512*/v-3.getTarget().toPb();
        final Map<StorageRpc.Option, ?> a2 = optionMap(/*EL:513*/v-3.getTarget().getGeneration(), /*EL:515*/v-3.getTarget().getMetageneration(), /*EL:516*/v-3.getTargetOptions());
        try {
            /*SL:519*/return Blob.fromPb(this, /*EL:521*/(StorageObject)RetryHelper.runWithRetries((Callable)new Callable<StorageObject>() {
                @Override
                public StorageObject call() {
                    /*SL:525*/return StorageImpl.this.storageRpc.compose(arrayListWithCapacity, pb, a2);
                }
            }, ((StorageOptions)this.getOptions()).getRetrySettings(), (ResultRetryAlgorithm)StorageImpl.EXCEPTION_HANDLER, /*EL:528*/((StorageOptions)this.getOptions()).getClock()));
        }
        catch (RetryHelper.RetryHelperException v1) {
            /*SL:532*/throw StorageException.translateAndThrow(v1);
        }
    }
    
    public CopyWriter copy(final CopyRequest v-4) {
        final StorageObject pb = /*EL:538*/v-4.getSource().toPb();
        final Map<StorageRpc.Option, ?> optionMap = optionMap(/*EL:539*/v-4.getSource().getGeneration(), /*EL:541*/null, v-4.getSourceOptions(), true);
        final StorageObject pb2 = /*EL:542*/v-4.getTarget().toPb();
        final Map<StorageRpc.Option, ?> v0 = optionMap(/*EL:543*/v-4.getTarget().getGeneration(), /*EL:545*/v-4.getTarget().getMetageneration(), /*EL:546*/v-4.getTargetOptions());
        try {
            final StorageRpc.RewriteResponse a1 = /*EL:550*/(StorageRpc.RewriteResponse)RetryHelper.runWithRetries((Callable)new Callable<StorageRpc.RewriteResponse>() {
                @Override
                public StorageRpc.RewriteResponse call() {
                    /*SL:554*/return StorageImpl.this.storageRpc.openRewrite(/*EL:561*/new StorageRpc.RewriteRequest(pb, optionMap, v-4.overrideInfo(), pb2, v0, v-4.getMegabytesCopiedPerChunk()));
                }
            }, ((StorageOptions)this.getOptions()).getRetrySettings(), (ResultRetryAlgorithm)StorageImpl.EXCEPTION_HANDLER, /*EL:564*/((StorageOptions)this.getOptions()).getClock());
            /*SL:567*/return new CopyWriter((StorageOptions)this.getOptions(), a1);
        }
        catch (RetryHelper.RetryHelperException v) {
            /*SL:569*/throw StorageException.translateAndThrow(v);
        }
    }
    
    public byte[] readAllBytes(final String a1, final String a2, final BlobSourceOption... a3) {
        /*SL:575*/return this.readAllBytes(BlobId.of(a1, a2), a3);
    }
    
    public byte[] readAllBytes(final BlobId v1, final BlobSourceOption... v2) {
        final StorageObject v3 = /*EL:580*/v1.toPb();
        final Map<StorageRpc.Option, ?> v4 = optionMap(/*EL:581*/v1, (Option[])v2);
        try {
            /*SL:583*/return (byte[])RetryHelper.runWithRetries((Callable)new Callable<byte[]>() {
                @Override
                public byte[] call() {
                    /*SL:587*/return StorageImpl.this.storageRpc.load(v3, v4);
                }
            }, ((StorageOptions)this.getOptions()).getRetrySettings(), (ResultRetryAlgorithm)StorageImpl.EXCEPTION_HANDLER, /*EL:590*/((StorageOptions)this.getOptions()).getClock());
        }
        catch (RetryHelper.RetryHelperException a1) {
            /*SL:594*/throw StorageException.translateAndThrow(a1);
        }
    }
    
    public StorageBatch batch() {
        /*SL:600*/return new StorageBatch((StorageOptions)this.getOptions());
    }
    
    public ReadChannel reader(final String a1, final String a2, final BlobSourceOption... a3) {
        final Map<StorageRpc.Option, ?> v1 = optionMap(/*EL:605*/(Option[])a3);
        /*SL:606*/return (ReadChannel)new BlobReadChannel((StorageOptions)this.getOptions(), BlobId.of(a1, a2), v1);
    }
    
    public ReadChannel reader(final BlobId a1, final BlobSourceOption... a2) {
        final Map<StorageRpc.Option, ?> v1 = optionMap(/*EL:611*/a1, (Option[])a2);
        /*SL:612*/return (ReadChannel)new BlobReadChannel((StorageOptions)this.getOptions(), a1, v1);
    }
    
    public BlobWriteChannel writer(final BlobInfo a1, final BlobWriteOption... a2) {
        final Tuple<BlobInfo, BlobTargetOption[]> v1 = /*EL:617*/BlobTargetOption.convert(a1, a2);
        /*SL:618*/return this.writer((BlobInfo)v1.x(), (BlobTargetOption[])v1.y());
    }
    
    public BlobWriteChannel writer(final URL a1) {
        /*SL:623*/return new BlobWriteChannel((StorageOptions)this.getOptions(), a1);
    }
    
    private BlobWriteChannel writer(final BlobInfo a1, final BlobTargetOption... a2) {
        final Map<StorageRpc.Option, ?> v1 = optionMap(/*EL:627*/a1, (Option[])a2);
        /*SL:628*/return new BlobWriteChannel((StorageOptions)this.getOptions(), a1, v1);
    }
    
    public URL signUrl(final BlobInfo v-19, final long v-18, final TimeUnit v-16, final SignUrlOption... v-15) {
        final EnumMap<SignUrlOption.Option, Object> enumMap = /*EL:633*/Maps.<SignUrlOption.Option, Object>newEnumMap(SignUrlOption.Option.class);
        /*SL:634*/for (final SignUrlOption a1 : v-15) {
            /*SL:635*/enumMap.put(a1.getOption(), a1.getValue());
        }
        final boolean equals = SignUrlOption.SignatureVersion.V4.equals(/*EL:638*/enumMap.get(SignUrlOption.Option.SIGNATURE_VERSION));
        ServiceAccountSigner serviceAccountSigner = /*EL:642*/enumMap.get(SignUrlOption.Option.SERVICE_ACCOUNT_CRED);
        /*SL:644*/if (serviceAccountSigner == null) {
            /*SL:645*/Preconditions.checkState(/*EL:646*/((StorageOptions)this.getOptions()).getCredentials() instanceof ServiceAccountSigner, (Object)"Signing key was not provided and could not be derived");
            /*SL:648*/serviceAccountSigner = (ServiceAccountSigner)((StorageOptions)this.getOptions()).getCredentials();
        }
        final long a5 = /*EL:651*/equals ? TimeUnit.SECONDS.convert(v-16.toMillis(v-18), TimeUnit.MILLISECONDS) : TimeUnit.SECONDS.convert(/*EL:653*/((StorageOptions)this.getOptions()).getClock().millisTime() + /*EL:655*/v-16.toMillis(v-18), TimeUnit.MILLISECONDS);
        final String s = /*EL:658*/(enumMap.get(SignUrlOption.Option.HOST_NAME) != /*EL:659*/null) ? enumMap.get(SignUrlOption.Option.HOST_NAME) : /*EL:660*/"https://storage.googleapis.com";
        final String trim = /*EL:665*/CharMatcher.anyOf("/").trimFrom(v-19.getBucket());
        String replace = /*EL:666*/"";
        /*SL:667*/if (!Strings.isNullOrEmpty(v-19.getName())) {
            /*SL:672*/replace = UrlEscapers.urlFragmentEscaper().escape(v-19.getName()).replace("?", "%3F").replace(";", "%3B");
        }
        final String constructResourceUriPath = /*EL:675*/this.constructResourceUriPath(trim, replace);
        final URI create = /*EL:676*/URI.create(constructResourceUriPath);
        try {
            final SignatureInfo buildSignatureInfo = /*EL:679*/this.buildSignatureInfo(enumMap, v-19, a5, create, serviceAccountSigner.getAccount());
            final String constructUnsignedPayload = /*EL:681*/buildSignatureInfo.constructUnsignedPayload();
            final byte[] sign = /*EL:682*/serviceAccountSigner.sign(constructUnsignedPayload.getBytes(StandardCharsets.UTF_8));
            final StringBuilder sb = /*EL:683*/new StringBuilder();
            /*SL:684*/sb.append(s).append(create);
            /*SL:686*/if (equals) {
                final BaseEncoding a2 = /*EL:687*/BaseEncoding.base16().lowerCase();
                final String a3 = /*EL:688*/URLEncoder.encode(a2.encode(sign), StandardCharsets.UTF_8.name());
                /*SL:689*/sb.append("?");
                /*SL:690*/sb.append(buildSignatureInfo.constructV4QueryString());
                /*SL:691*/sb.append("&X-Goog-Signature=").append(a3);
            }
            else {
                final BaseEncoding a4 = /*EL:693*/BaseEncoding.base64();
                final String v1 = /*EL:694*/URLEncoder.encode(a4.encode(sign), StandardCharsets.UTF_8.name());
                /*SL:695*/sb.append("?");
                /*SL:696*/sb.append("GoogleAccessId=").append(serviceAccountSigner.getAccount());
                /*SL:697*/sb.append("&Expires=").append(a5);
                /*SL:698*/sb.append("&Signature=").append(v1);
            }
            /*SL:701*/return new URL(sb.toString());
        }
        catch (MalformedURLException | UnsupportedEncodingException ex) {
            /*SL:704*/throw new IllegalStateException(ex);
        }
    }
    
    private String constructResourceUriPath(final String a1, final String a2) {
        final StringBuilder v1 = /*EL:709*/new StringBuilder();
        /*SL:710*/v1.append("/").append(a1);
        /*SL:711*/if (Strings.isNullOrEmpty(a2)) {
            /*SL:712*/return v1.toString();
        }
        /*SL:714*/if (!a2.startsWith("/")) {
            /*SL:715*/v1.append("/");
        }
        /*SL:717*/v1.append(a2);
        /*SL:718*/return v1.toString();
    }
    
    private SignatureInfo buildSignatureInfo(final Map<SignUrlOption.Option, Object> a1, final BlobInfo a2, final long a3, final URI a4, final String a5) {
        final HttpMethod v1 = /*EL:738*/a1.containsKey(SignUrlOption.Option.HTTP_METHOD) ? /*EL:739*/a1.get(SignUrlOption.Option.HTTP_METHOD) : HttpMethod.GET;
        final SignatureInfo.Builder v2 = /*EL:743*/new SignatureInfo.Builder(v1, a3, a4);
        /*SL:746*/if (MoreObjects.firstNonNull((Object)a1.get(SignUrlOption.Option.MD5), (Object)false)) {
            /*SL:747*/Preconditions.checkArgument(a2.getMd5() != null, (Object)"Blob is missing a value for md5");
            /*SL:748*/v2.setContentMd5(a2.getMd5());
        }
        /*SL:751*/if (MoreObjects.firstNonNull((Object)a1.get(SignUrlOption.Option.CONTENT_TYPE), (Object)false)) {
            /*SL:752*/Preconditions.checkArgument(a2.getContentType() != null, (Object)"Blob is missing a value for content-type");
            /*SL:753*/v2.setContentType(a2.getContentType());
        }
        /*SL:756*/v2.setSignatureVersion(a1.get(SignUrlOption.Option.SIGNATURE_VERSION));
        /*SL:759*/v2.setAccountEmail(a5);
        /*SL:761*/v2.setTimestamp(((StorageOptions)this.getOptions()).getClock().millisTime());
        final Map<String, String> v3 = /*EL:764*/a1.containsKey(SignUrlOption.Option.EXT_HEADERS) ? /*EL:766*/a1.get(SignUrlOption.Option.EXT_HEADERS) : /*EL:768*/Collections.<String, String>emptyMap();
        /*SL:770*/return v2.setCanonicalizedExtensionHeaders(v3).build();
    }
    
    public List<Blob> get(final BlobId... a1) {
        /*SL:775*/return this.get(Arrays.<BlobId>asList(a1));
    }
    
    public List<Blob> get(final Iterable<BlobId> v2) {
        final StorageBatch v3 = /*EL:780*/this.batch();
        final List<Blob> v4 = /*EL:781*/(List<Blob>)Lists.<Object>newArrayList();
        /*SL:782*/for (final BlobId a1 : v2) {
            /*SL:783*/v3.get(a1, new BlobGetOption[0]).notify(/*EL:784*/(BatchResult.Callback)new BatchResult.Callback<Blob, StorageException>() {
                public void success(final Blob a1) {
                    /*SL:789*//*EL:790*/v4.add(a1);
                }
                
                public void error(final StorageException a1) {
                    /*SL:794*//*EL:795*/v4.add(null);
                }
            });
        }
        /*SL:798*/v3.submit();
        /*SL:799*/return Collections.<Blob>unmodifiableList((List<? extends Blob>)v4);
    }
    
    public List<Blob> update(final BlobInfo... a1) {
        /*SL:804*/return this.update(Arrays.<BlobInfo>asList(a1));
    }
    
    public List<Blob> update(final Iterable<BlobInfo> v2) {
        final StorageBatch v3 = /*EL:809*/this.batch();
        final List<Blob> v4 = /*EL:810*/(List<Blob>)Lists.<Object>newArrayList();
        /*SL:811*/for (final BlobInfo a1 : v2) {
            /*SL:812*/v3.update(a1, new BlobTargetOption[0]).notify(/*EL:813*/(BatchResult.Callback)new BatchResult.Callback<Blob, StorageException>() {
                public void success(final Blob a1) {
                    /*SL:818*//*EL:819*/v4.add(a1);
                }
                
                public void error(final StorageException a1) {
                    /*SL:823*//*EL:824*/v4.add(null);
                }
            });
        }
        /*SL:827*/v3.submit();
        /*SL:828*/return Collections.<Blob>unmodifiableList((List<? extends Blob>)v4);
    }
    
    public List<Boolean> delete(final BlobId... a1) {
        /*SL:833*/return this.delete(Arrays.<BlobId>asList(a1));
    }
    
    public List<Boolean> delete(final Iterable<BlobId> v2) {
        final StorageBatch v3 = /*EL:838*/this.batch();
        final List<Boolean> v4 = /*EL:839*/(List<Boolean>)Lists.<Object>newArrayList();
        /*SL:840*/for (final BlobId a1 : v2) {
            /*SL:841*/v3.delete(a1, new BlobSourceOption[0]).notify(/*EL:842*/(BatchResult.Callback)new BatchResult.Callback<Boolean, StorageException>() {
                public void success(final Boolean a1) {
                    /*SL:847*//*EL:848*/v4.add(a1);
                }
                
                public void error(final StorageException a1) {
                    /*SL:852*//*EL:853*/v4.add(Boolean.FALSE);
                }
            });
        }
        /*SL:856*/v3.submit();
        /*SL:857*/return Collections.<Boolean>unmodifiableList((List<? extends Boolean>)v4);
    }
    
    public Acl getAcl(final String v2, final Acl.Entity v3, final BucketSourceOption... v4) {
        try {
            final Map<StorageRpc.Option, ?> a1 = optionMap(/*EL:863*/(Option[])v4);
            final BucketAccessControl a2 = /*EL:865*/(BucketAccessControl)RetryHelper.runWithRetries((Callable)new Callable<BucketAccessControl>() {
                @Override
                public BucketAccessControl call() {
                    /*SL:869*/return StorageImpl.this.storageRpc.getAcl(v2, v3.toPb(), a1);
                }
            }, ((StorageOptions)this.getOptions()).getRetrySettings(), (ResultRetryAlgorithm)StorageImpl.EXCEPTION_HANDLER, /*EL:872*/((StorageOptions)this.getOptions()).getClock());
            /*SL:875*/return (a2 == null) ? null : Acl.fromPb(a2);
        }
        catch (RetryHelper.RetryHelperException a3) {
            /*SL:877*/throw StorageException.translateAndThrow(a3);
        }
    }
    
    public Acl getAcl(final String a1, final Acl.Entity a2) {
        /*SL:883*/return this.getAcl(a1, a2, new BucketSourceOption[0]);
    }
    
    public boolean deleteAcl(final String v1, final Acl.Entity v2, final BucketSourceOption... v3) {
        try {
            final Map<StorageRpc.Option, ?> a1 = optionMap(/*EL:890*/(Option[])v3);
            /*SL:891*/return (boolean)RetryHelper.runWithRetries((Callable)new Callable<Boolean>() {
                @Override
                public Boolean call() {
                    /*SL:895*/return StorageImpl.this.storageRpc.deleteAcl(v1, v2.toPb(), a1);
                }
            }, ((StorageOptions)this.getOptions()).getRetrySettings(), (ResultRetryAlgorithm)StorageImpl.EXCEPTION_HANDLER, /*EL:898*/((StorageOptions)this.getOptions()).getClock());
        }
        catch (RetryHelper.RetryHelperException a2) {
            /*SL:902*/throw StorageException.translateAndThrow(a2);
        }
    }
    
    public boolean deleteAcl(final String a1, final Acl.Entity a2) {
        /*SL:908*/return this.deleteAcl(a1, a2, new BucketSourceOption[0]);
    }
    
    public Acl createAcl(final String v1, final Acl v2, final BucketSourceOption... v3) {
        final BucketAccessControl v4 = /*EL:913*/v2.toBucketPb().setBucket(v1);
        try {
            final Map<StorageRpc.Option, ?> a1 = optionMap(/*EL:915*/(Option[])v3);
            /*SL:916*/return Acl.fromPb(/*EL:917*/(BucketAccessControl)RetryHelper.runWithRetries((Callable)new Callable<BucketAccessControl>() {
                @Override
                public BucketAccessControl call() {
                    /*SL:921*/return StorageImpl.this.storageRpc.createAcl(v4, a1);
                }
            }, ((StorageOptions)this.getOptions()).getRetrySettings(), (ResultRetryAlgorithm)StorageImpl.EXCEPTION_HANDLER, /*EL:924*/((StorageOptions)this.getOptions()).getClock()));
        }
        catch (RetryHelper.RetryHelperException a2) {
            /*SL:928*/throw StorageException.translateAndThrow(a2);
        }
    }
    
    public Acl createAcl(final String a1, final Acl a2) {
        /*SL:934*/return this.createAcl(a1, a2, new BucketSourceOption[0]);
    }
    
    public Acl updateAcl(final String v1, final Acl v2, final BucketSourceOption... v3) {
        final BucketAccessControl v4 = /*EL:939*/v2.toBucketPb().setBucket(v1);
        try {
            final Map<StorageRpc.Option, ?> a1 = optionMap(/*EL:941*/(Option[])v3);
            /*SL:942*/return Acl.fromPb(/*EL:943*/(BucketAccessControl)RetryHelper.runWithRetries((Callable)new Callable<BucketAccessControl>() {
                @Override
                public BucketAccessControl call() {
                    /*SL:947*/return StorageImpl.this.storageRpc.patchAcl(v4, a1);
                }
            }, ((StorageOptions)this.getOptions()).getRetrySettings(), (ResultRetryAlgorithm)StorageImpl.EXCEPTION_HANDLER, /*EL:950*/((StorageOptions)this.getOptions()).getClock()));
        }
        catch (RetryHelper.RetryHelperException a2) {
            /*SL:954*/throw StorageException.translateAndThrow(a2);
        }
    }
    
    public Acl updateAcl(final String a1, final Acl a2) {
        /*SL:960*/return this.updateAcl(a1, a2, new BucketSourceOption[0]);
    }
    
    public List<Acl> listAcls(final String v-1, final BucketSourceOption... v0) {
        try {
            final Map<StorageRpc.Option, ?> a1 = optionMap(/*EL:966*/(Option[])v0);
            final List<BucketAccessControl> a2 = /*EL:968*/(List<BucketAccessControl>)RetryHelper.runWithRetries((Callable)new Callable<List<BucketAccessControl>>() {
                @Override
                public List<BucketAccessControl> call() {
                    /*SL:972*/return StorageImpl.this.storageRpc.listAcls(v-1, a1);
                }
            }, ((StorageOptions)this.getOptions()).getRetrySettings(), (ResultRetryAlgorithm)StorageImpl.EXCEPTION_HANDLER, /*EL:975*/((StorageOptions)this.getOptions()).getClock());
            /*SL:978*/return Lists.<BucketAccessControl, Acl>transform(a2, (Function<? super BucketAccessControl, ? extends Acl>)Acl.FROM_BUCKET_PB_FUNCTION);
        }
        catch (RetryHelper.RetryHelperException v) {
            /*SL:980*/throw StorageException.translateAndThrow(v);
        }
    }
    
    public List<Acl> listAcls(final String a1) {
        /*SL:986*/return this.listAcls(a1, new BucketSourceOption[0]);
    }
    
    public Acl getDefaultAcl(final String v2, final Acl.Entity v3) {
        try {
            final ObjectAccessControl a1 = /*EL:993*/(ObjectAccessControl)RetryHelper.runWithRetries((Callable)new Callable<ObjectAccessControl>() {
                @Override
                public ObjectAccessControl call() {
                    /*SL:997*/return StorageImpl.this.storageRpc.getDefaultAcl(v2, v3.toPb());
                }
            }, ((StorageOptions)this.getOptions()).getRetrySettings(), (ResultRetryAlgorithm)StorageImpl.EXCEPTION_HANDLER, /*EL:1000*/((StorageOptions)this.getOptions()).getClock());
            /*SL:1003*/return (a1 == null) ? null : Acl.fromPb(a1);
        }
        catch (RetryHelper.RetryHelperException a2) {
            /*SL:1005*/throw StorageException.translateAndThrow(a2);
        }
    }
    
    public boolean deleteDefaultAcl(final String v1, final Acl.Entity v2) {
        try {
            /*SL:1012*/return (boolean)RetryHelper.runWithRetries((Callable)new Callable<Boolean>() {
                @Override
                public Boolean call() {
                    /*SL:1016*/return StorageImpl.this.storageRpc.deleteDefaultAcl(v1, v2.toPb());
                }
            }, ((StorageOptions)this.getOptions()).getRetrySettings(), (ResultRetryAlgorithm)StorageImpl.EXCEPTION_HANDLER, /*EL:1019*/((StorageOptions)this.getOptions()).getClock());
        }
        catch (RetryHelper.RetryHelperException a1) {
            /*SL:1023*/throw StorageException.translateAndThrow(a1);
        }
    }
    
    public Acl createDefaultAcl(final String v1, final Acl v2) {
        final ObjectAccessControl v3 = /*EL:1029*/v2.toObjectPb().setBucket(v1);
        try {
            /*SL:1031*/return Acl.fromPb(/*EL:1032*/(ObjectAccessControl)RetryHelper.runWithRetries((Callable)new Callable<ObjectAccessControl>() {
                @Override
                public ObjectAccessControl call() {
                    /*SL:1036*/return StorageImpl.this.storageRpc.createDefaultAcl(v3);
                }
            }, ((StorageOptions)this.getOptions()).getRetrySettings(), (ResultRetryAlgorithm)StorageImpl.EXCEPTION_HANDLER, /*EL:1039*/((StorageOptions)this.getOptions()).getClock()));
        }
        catch (RetryHelper.RetryHelperException a1) {
            /*SL:1043*/throw StorageException.translateAndThrow(a1);
        }
    }
    
    public Acl updateDefaultAcl(final String v1, final Acl v2) {
        final ObjectAccessControl v3 = /*EL:1049*/v2.toObjectPb().setBucket(v1);
        try {
            /*SL:1051*/return Acl.fromPb(/*EL:1052*/(ObjectAccessControl)RetryHelper.runWithRetries((Callable)new Callable<ObjectAccessControl>() {
                @Override
                public ObjectAccessControl call() {
                    /*SL:1056*/return StorageImpl.this.storageRpc.patchDefaultAcl(v3);
                }
            }, ((StorageOptions)this.getOptions()).getRetrySettings(), (ResultRetryAlgorithm)StorageImpl.EXCEPTION_HANDLER, /*EL:1059*/((StorageOptions)this.getOptions()).getClock()));
        }
        catch (RetryHelper.RetryHelperException a1) {
            /*SL:1063*/throw StorageException.translateAndThrow(a1);
        }
    }
    
    public List<Acl> listDefaultAcls(final String v0) {
        try {
            final List<ObjectAccessControl> a1 = /*EL:1071*/(List<ObjectAccessControl>)RetryHelper.runWithRetries((Callable)new Callable<List<ObjectAccessControl>>() {
                @Override
                public List<ObjectAccessControl> call() {
                    /*SL:1075*/return StorageImpl.this.storageRpc.listDefaultAcls(v0);
                }
            }, ((StorageOptions)this.getOptions()).getRetrySettings(), (ResultRetryAlgorithm)StorageImpl.EXCEPTION_HANDLER, /*EL:1078*/((StorageOptions)this.getOptions()).getClock());
            /*SL:1081*/return Lists.<ObjectAccessControl, Acl>transform(a1, (Function<? super ObjectAccessControl, ? extends Acl>)Acl.FROM_OBJECT_PB_FUNCTION);
        }
        catch (RetryHelper.RetryHelperException v) {
            /*SL:1083*/throw StorageException.translateAndThrow(v);
        }
    }
    
    public Acl getAcl(final BlobId v2, final Acl.Entity v3) {
        try {
            final ObjectAccessControl a1 = /*EL:1091*/(ObjectAccessControl)RetryHelper.runWithRetries((Callable)new Callable<ObjectAccessControl>() {
                @Override
                public ObjectAccessControl call() {
                    /*SL:1095*/return StorageImpl.this.storageRpc.getAcl(v2.getBucket(), /*EL:1096*/v2.getName(), v2.getGeneration(), v3.toPb());
                }
            }, ((StorageOptions)this.getOptions()).getRetrySettings(), (ResultRetryAlgorithm)StorageImpl.EXCEPTION_HANDLER, /*EL:1099*/((StorageOptions)this.getOptions()).getClock());
            /*SL:1102*/return (a1 == null) ? null : Acl.fromPb(a1);
        }
        catch (RetryHelper.RetryHelperException a2) {
            /*SL:1104*/throw StorageException.translateAndThrow(a2);
        }
    }
    
    public boolean deleteAcl(final BlobId v1, final Acl.Entity v2) {
        try {
            /*SL:1111*/return (boolean)RetryHelper.runWithRetries((Callable)new Callable<Boolean>() {
                @Override
                public Boolean call() {
                    /*SL:1115*/return StorageImpl.this.storageRpc.deleteAcl(v1.getBucket(), /*EL:1116*/v1.getName(), v1.getGeneration(), v2.toPb());
                }
            }, ((StorageOptions)this.getOptions()).getRetrySettings(), (ResultRetryAlgorithm)StorageImpl.EXCEPTION_HANDLER, /*EL:1119*/((StorageOptions)this.getOptions()).getClock());
        }
        catch (RetryHelper.RetryHelperException a1) {
            /*SL:1123*/throw StorageException.translateAndThrow(a1);
        }
    }
    
    public Acl createAcl(final BlobId v1, final Acl v2) {
        final ObjectAccessControl v3 = /*EL:1129*/v2.toObjectPb().setBucket(/*EL:1130*/v1.getBucket()).setObject(/*EL:1131*/v1.getName()).setGeneration(/*EL:1132*/v1.getGeneration());
        try {
            /*SL:1135*/return Acl.fromPb(/*EL:1136*/(ObjectAccessControl)RetryHelper.runWithRetries((Callable)new Callable<ObjectAccessControl>() {
                @Override
                public ObjectAccessControl call() {
                    /*SL:1140*/return StorageImpl.this.storageRpc.createAcl(v3);
                }
            }, ((StorageOptions)this.getOptions()).getRetrySettings(), (ResultRetryAlgorithm)StorageImpl.EXCEPTION_HANDLER, /*EL:1143*/((StorageOptions)this.getOptions()).getClock()));
        }
        catch (RetryHelper.RetryHelperException a1) {
            /*SL:1147*/throw StorageException.translateAndThrow(a1);
        }
    }
    
    public Acl updateAcl(final BlobId v1, final Acl v2) {
        final ObjectAccessControl v3 = /*EL:1153*/v2.toObjectPb().setBucket(/*EL:1154*/v1.getBucket()).setObject(/*EL:1155*/v1.getName()).setGeneration(/*EL:1156*/v1.getGeneration());
        try {
            /*SL:1159*/return Acl.fromPb(/*EL:1160*/(ObjectAccessControl)RetryHelper.runWithRetries((Callable)new Callable<ObjectAccessControl>() {
                @Override
                public ObjectAccessControl call() {
                    /*SL:1164*/return StorageImpl.this.storageRpc.patchAcl(v3);
                }
            }, ((StorageOptions)this.getOptions()).getRetrySettings(), (ResultRetryAlgorithm)StorageImpl.EXCEPTION_HANDLER, /*EL:1167*/((StorageOptions)this.getOptions()).getClock()));
        }
        catch (RetryHelper.RetryHelperException a1) {
            /*SL:1171*/throw StorageException.translateAndThrow(a1);
        }
    }
    
    public List<Acl> listAcls(final BlobId v0) {
        try {
            final List<ObjectAccessControl> a1 = /*EL:1179*/(List<ObjectAccessControl>)RetryHelper.runWithRetries((Callable)new Callable<List<ObjectAccessControl>>() {
                @Override
                public List<ObjectAccessControl> call() {
                    /*SL:1183*/return StorageImpl.this.storageRpc.listAcls(v0.getBucket(), /*EL:1184*/v0.getName(), v0.getGeneration());
                }
            }, ((StorageOptions)this.getOptions()).getRetrySettings(), (ResultRetryAlgorithm)StorageImpl.EXCEPTION_HANDLER, /*EL:1187*/((StorageOptions)this.getOptions()).getClock());
            /*SL:1190*/return Lists.<ObjectAccessControl, Acl>transform(a1, (Function<? super ObjectAccessControl, ? extends Acl>)Acl.FROM_OBJECT_PB_FUNCTION);
        }
        catch (RetryHelper.RetryHelperException v) {
            /*SL:1192*/throw StorageException.translateAndThrow(v);
        }
    }
    
    public HmacKey createHmacKey(final ServiceAccount v1, final CreateHmacKeyOption... v2) {
        try {
            /*SL:1199*/return HmacKey.fromPb(/*EL:1200*/(com.google.api.services.storage.model.HmacKey)RetryHelper.runWithRetries((Callable)new Callable<com.google.api.services.storage.model.HmacKey>() {
                @Override
                public com.google.api.services.storage.model.HmacKey call() {
                    /*SL:1204*/return StorageImpl.this.storageRpc.createHmacKey(v1.getEmail(), optionMap((Option[])v2));
                }
            }, ((StorageOptions)this.getOptions()).getRetrySettings(), (ResultRetryAlgorithm)StorageImpl.EXCEPTION_HANDLER, /*EL:1207*/((StorageOptions)this.getOptions()).getClock()));
        }
        catch (RetryHelper.RetryHelperException a1) {
            /*SL:1211*/throw StorageException.translateAndThrow(a1);
        }
    }
    
    public Page<HmacKey.HmacKeyMetadata> listHmacKeys(final ListHmacKeysOption... a1) {
        /*SL:1217*/return listHmacKeys((StorageOptions)this.getOptions(), optionMap((Option[])a1));
    }
    
    public HmacKey.HmacKeyMetadata getHmacKey(final String v1, final GetHmacKeyOption... v2) {
        try {
            /*SL:1223*/return HmacKey.HmacKeyMetadata.fromPb(/*EL:1224*/(HmacKeyMetadata)RetryHelper.runWithRetries((Callable)new Callable<HmacKeyMetadata>() {
                @Override
                public HmacKeyMetadata call() {
                    /*SL:1228*/return StorageImpl.this.storageRpc.getHmacKey(v1, optionMap((Option[])v2));
                }
            }, ((StorageOptions)this.getOptions()).getRetrySettings(), (ResultRetryAlgorithm)StorageImpl.EXCEPTION_HANDLER, /*EL:1231*/((StorageOptions)this.getOptions()).getClock()));
        }
        catch (RetryHelper.RetryHelperException a1) {
            /*SL:1235*/throw StorageException.translateAndThrow(a1);
        }
    }
    
    private HmacKey.HmacKeyMetadata updateHmacKey(final HmacKey.HmacKeyMetadata v1, final UpdateHmacKeyOption... v2) {
        try {
            /*SL:1242*/return HmacKey.HmacKeyMetadata.fromPb(/*EL:1243*/(HmacKeyMetadata)RetryHelper.runWithRetries((Callable)new Callable<HmacKeyMetadata>() {
                @Override
                public HmacKeyMetadata call() {
                    /*SL:1247*/return StorageImpl.this.storageRpc.updateHmacKey(v1.toPb(), optionMap((Option[])v2));
                }
            }, ((StorageOptions)this.getOptions()).getRetrySettings(), (ResultRetryAlgorithm)StorageImpl.EXCEPTION_HANDLER, /*EL:1250*/((StorageOptions)this.getOptions()).getClock()));
        }
        catch (RetryHelper.RetryHelperException a1) {
            /*SL:1254*/throw StorageException.translateAndThrow(a1);
        }
    }
    
    public HmacKey.HmacKeyMetadata updateHmacKeyState(final HmacKey.HmacKeyMetadata a1, final HmacKey.HmacKeyState a2, final UpdateHmacKeyOption... a3) {
        final HmacKey.HmacKeyMetadata v1 = /*EL:1264*/HmacKey.HmacKeyMetadata.newBuilder(a1.getServiceAccount()).setProjectId(a1.getProjectId()).setAccessId(/*EL:1265*/a1.getAccessId()).setState(/*EL:1266*/a2).build();
        /*SL:1269*/return this.updateHmacKey(v1, a3);
    }
    
    public void deleteHmacKey(final HmacKey.HmacKeyMetadata v1, final DeleteHmacKeyOption... v2) {
        try {
            /*SL:1275*/RetryHelper.runWithRetries((Callable)new Callable<Void>() {
                @Override
                public Void call() {
                    /*SL:1279*/StorageImpl.this.storageRpc.deleteHmacKey(v1.toPb(), optionMap((Option[])v2));
                    /*SL:1280*/return null;
                }
            }, ((StorageOptions)this.getOptions()).getRetrySettings(), (ResultRetryAlgorithm)StorageImpl.EXCEPTION_HANDLER, /*EL:1283*/((StorageOptions)this.getOptions()).getClock());
        }
        catch (RetryHelper.RetryHelperException a1) {
            /*SL:1287*/throw StorageException.translateAndThrow(a1);
        }
    }
    
    private static Page<HmacKey.HmacKeyMetadata> listHmacKeys(final StorageOptions v-3, final Map<StorageRpc.Option, ?> v-2) {
        try {
            final Tuple<String, Iterable<HmacKeyMetadata>> a1 = /*EL:1295*/(Tuple<String, Iterable<HmacKeyMetadata>>)RetryHelper.runWithRetries((Callable)new Callable<Tuple<String, Iterable<HmacKeyMetadata>>>() {
                @Override
                public Tuple<String, Iterable<HmacKeyMetadata>> call() {
                    /*SL:1303*/return v-3.getStorageRpcV1().listHmacKeys(v-2);
                }
            }, v-3.getRetrySettings(), (ResultRetryAlgorithm)StorageImpl.EXCEPTION_HANDLER, /*EL:1306*/v-3.getClock());
            final String a2 = /*EL:1309*/(String)a1.x();
            final Iterable<HmacKey.HmacKeyMetadata> v1 = /*EL:1310*/(Iterable<HmacKey.HmacKeyMetadata>)((a1.y() == /*EL:1311*/null) ? /*EL:1312*/ImmutableList.<Object>of() : /*EL:1313*/Iterables.<Object, Object>transform((Iterable<Object>)a1.y(), /*EL:1314*/(Function<? super Object, ?>)new Function<HmacKeyMetadata, HmacKey.HmacKeyMetadata>() {
                @Override
                public HmacKey.HmacKeyMetadata apply(final HmacKeyMetadata a1) {
                    /*SL:1320*/return HmacKey.HmacKeyMetadata.fromPb(a1);
                }
            }));
            /*SL:1323*/return (Page<HmacKey.HmacKeyMetadata>)new PageImpl((PageImpl.NextPageFetcher)new HmacKeyMetadataPageFetcher(v-3, v-2), a2, (Iterable)v1);
        }
        catch (RetryHelper.RetryHelperException a3) {
            /*SL:1326*/throw StorageException.translateAndThrow(a3);
        }
    }
    
    public Policy getIamPolicy(final String v2, final BucketSourceOption... v3) {
        try {
            final Map<StorageRpc.Option, ?> a1 = optionMap(/*EL:1333*/(Option[])v3);
            /*SL:1334*/return PolicyHelper.convertFromApiPolicy(/*EL:1335*/(com.google.api.services.storage.model.Policy)RetryHelper.runWithRetries((Callable)new Callable<com.google.api.services.storage.model.Policy>() {
                @Override
                public com.google.api.services.storage.model.Policy call() {
                    /*SL:1339*/return StorageImpl.this.storageRpc.getIamPolicy(v2, a1);
                }
            }, ((StorageOptions)this.getOptions()).getRetrySettings(), (ResultRetryAlgorithm)StorageImpl.EXCEPTION_HANDLER, /*EL:1342*/((StorageOptions)this.getOptions()).getClock()));
        }
        catch (RetryHelper.RetryHelperException a2) {
            /*SL:1346*/throw StorageException.translateAndThrow(a2);
        }
    }
    
    public Policy setIamPolicy(final String v1, final Policy v2, final BucketSourceOption... v3) {
        try {
            final Map<StorageRpc.Option, ?> a1 = optionMap(/*EL:1354*/(Option[])v3);
            /*SL:1355*/return PolicyHelper.convertFromApiPolicy(/*EL:1356*/(com.google.api.services.storage.model.Policy)RetryHelper.runWithRetries((Callable)new Callable<com.google.api.services.storage.model.Policy>() {
                @Override
                public com.google.api.services.storage.model.Policy call() {
                    /*SL:1360*/return StorageImpl.this.storageRpc.setIamPolicy(v1, PolicyHelper.convertToApiPolicy(v2), a1);
                }
            }, ((StorageOptions)this.getOptions()).getRetrySettings(), (ResultRetryAlgorithm)StorageImpl.EXCEPTION_HANDLER, /*EL:1363*/((StorageOptions)this.getOptions()).getClock()));
        }
        catch (RetryHelper.RetryHelperException a2) {
            /*SL:1367*/throw StorageException.translateAndThrow(a2);
        }
    }
    
    public List<Boolean> testIamPermissions(final String v-2, final List<String> v-1, final BucketSourceOption... v0) {
        try {
            final Map<StorageRpc.Option, ?> a1 = optionMap(/*EL:1375*/(Option[])v0);
            final TestIamPermissionsResponse a2 = /*EL:1377*/(TestIamPermissionsResponse)RetryHelper.runWithRetries((Callable)new Callable<TestIamPermissionsResponse>() {
                @Override
                public TestIamPermissionsResponse call() {
                    /*SL:1381*/return StorageImpl.this.storageRpc.testIamPermissions(v-2, v-1, a1);
                }
            }, ((StorageOptions)this.getOptions()).getRetrySettings(), (ResultRetryAlgorithm)StorageImpl.EXCEPTION_HANDLER, /*EL:1384*/((StorageOptions)this.getOptions()).getClock());
            final Set<String> a3 = /*EL:1387*/(Set<String>)((a2.getPermissions() != /*EL:1388*/null) ? /*EL:1389*/ImmutableSet.<Object>copyOf((Collection<?>)a2.getPermissions()) : /*EL:1390*/ImmutableSet.<Object>of());
            /*SL:1391*/return Lists.<String, Boolean>transform(v-1, (Function<? super String, ? extends Boolean>)new Function<String, Boolean>() {
                @Override
                public Boolean apply(final String a1) {
                    /*SL:1396*/return a3.contains(a1);
                }
            });
        }
        catch (RetryHelper.RetryHelperException v) {
            /*SL:1400*/throw StorageException.translateAndThrow(v);
        }
    }
    
    public Bucket lockRetentionPolicy(final BucketInfo v1, final BucketTargetOption... v2) {
        final com.google.api.services.storage.model.Bucket v3 = /*EL:1406*/v1.toPb();
        final Map<StorageRpc.Option, ?> v4 = optionMap(/*EL:1407*/v1, (Option[])v2);
        try {
            /*SL:1409*/return Bucket.fromPb(this, /*EL:1411*/(com.google.api.services.storage.model.Bucket)RetryHelper.runWithRetries((Callable)new Callable<com.google.api.services.storage.model.Bucket>() {
                @Override
                public com.google.api.services.storage.model.Bucket call() {
                    /*SL:1415*/return StorageImpl.this.storageRpc.lockRetentionPolicy(v3, v4);
                }
            }, ((StorageOptions)this.getOptions()).getRetrySettings(), (ResultRetryAlgorithm)StorageImpl.EXCEPTION_HANDLER, /*EL:1418*/((StorageOptions)this.getOptions()).getClock()));
        }
        catch (RetryHelper.RetryHelperException a1) {
            /*SL:1422*/throw StorageException.translateAndThrow(a1);
        }
    }
    
    public ServiceAccount getServiceAccount(final String v0) {
        try {
            final com.google.api.services.storage.model.ServiceAccount a1 = /*EL:1430*/(com.google.api.services.storage.model.ServiceAccount)RetryHelper.runWithRetries((Callable)new Callable<com.google.api.services.storage.model.ServiceAccount>() {
                @Override
                public com.google.api.services.storage.model.ServiceAccount call() {
                    /*SL:1434*/return StorageImpl.this.storageRpc.getServiceAccount(v0);
                }
            }, ((StorageOptions)this.getOptions()).getRetrySettings(), (ResultRetryAlgorithm)StorageImpl.EXCEPTION_HANDLER, /*EL:1437*/((StorageOptions)this.getOptions()).getClock());
            /*SL:1440*/return (a1 == null) ? null : ServiceAccount.fromPb(a1);
        }
        catch (RetryHelper.RetryHelperException v) {
            /*SL:1442*/throw StorageException.translateAndThrow(v);
        }
    }
    
    private static <T> void addToOptionMap(final StorageRpc.Option a1, final T a2, final Map<StorageRpc.Option, Object> a3) {
        /*SL:1448*/StorageImpl.<T>addToOptionMap(a1, a1, a2, a3);
    }
    
    private static <T> void addToOptionMap(final StorageRpc.Option a2, final StorageRpc.Option a3, final T a4, final Map<StorageRpc.Option, Object> v1) {
        /*SL:1456*/if (v1.containsKey(a2)) {
            T a5 = /*EL:1458*/(T)v1.remove(a2);
            /*SL:1459*/Preconditions.checkArgument(a5 != null || a4 != null, (Object)("Option " + a2.value() + /*EL:1461*/" is missing a value"));
            /*SL:1462*/a5 = (T)MoreObjects.firstNonNull((Object)a5, (Object)a4);
            /*SL:1463*/v1.put(a3, a5);
        }
    }
    
    private static Map<StorageRpc.Option, ?> optionMap(final Long a1, final Long a2, final Iterable<? extends Option> a3) {
        /*SL:1469*/return optionMap(a1, a2, a3, false);
    }
    
    private static Map<StorageRpc.Option, ?> optionMap(final Long a3, final Long a4, final Iterable<? extends Option> v1, final boolean v2) {
        final Map<StorageRpc.Option, Object> v3 = /*EL:1477*/Maps.<StorageRpc.Option, Object>newEnumMap(StorageRpc.Option.class);
        /*SL:1478*/for (final Option a5 : v1) {
            final Object a6 = /*EL:1479*/v3.put(a5.getRpcOption(), a5.getValue());
            /*SL:1480*/Preconditions.checkArgument(a6 == null, "Duplicate option %s", (Object)a5);
        }
        final Boolean v4 = /*EL:1482*/v3.remove(StorageRpc.Option.DELIMITER);
        /*SL:1483*/if (Boolean.TRUE.equals(v4)) {
            /*SL:1484*/v3.put(StorageRpc.Option.DELIMITER, "/");
        }
        /*SL:1486*/if (v2) {
            /*SL:1487*/StorageImpl.<Long>addToOptionMap(StorageRpc.Option.IF_GENERATION_MATCH, StorageRpc.Option.IF_SOURCE_GENERATION_MATCH, a3, v3);
            /*SL:1488*/StorageImpl.<Long>addToOptionMap(StorageRpc.Option.IF_GENERATION_NOT_MATCH, StorageRpc.Option.IF_SOURCE_GENERATION_NOT_MATCH, a3, v3);
            /*SL:1489*/StorageImpl.<Long>addToOptionMap(StorageRpc.Option.IF_METAGENERATION_MATCH, StorageRpc.Option.IF_SOURCE_METAGENERATION_MATCH, a4, v3);
            /*SL:1490*/StorageImpl.<Long>addToOptionMap(StorageRpc.Option.IF_METAGENERATION_NOT_MATCH, StorageRpc.Option.IF_SOURCE_METAGENERATION_NOT_MATCH, a4, v3);
        }
        else {
            /*SL:1493*/StorageImpl.<Long>addToOptionMap(StorageRpc.Option.IF_GENERATION_MATCH, a3, v3);
            /*SL:1494*/StorageImpl.<Long>addToOptionMap(StorageRpc.Option.IF_GENERATION_NOT_MATCH, a3, v3);
            /*SL:1495*/StorageImpl.<Long>addToOptionMap(StorageRpc.Option.IF_METAGENERATION_MATCH, a4, v3);
            /*SL:1496*/StorageImpl.<Long>addToOptionMap(StorageRpc.Option.IF_METAGENERATION_NOT_MATCH, a4, v3);
        }
        /*SL:1498*/return (Map<StorageRpc.Option, ?>)ImmutableMap.<Object, Object>copyOf((Map<?, ?>)v3);
    }
    
    private static Map<StorageRpc.Option, ?> optionMap(final Option... a1) {
        /*SL:1502*/return optionMap(null, null, Arrays.<Option>asList(a1));
    }
    
    private static Map<StorageRpc.Option, ?> optionMap(final Long a1, final Long a2, final Option... a3) {
        /*SL:1507*/return optionMap(a1, a2, Arrays.<Option>asList(a3));
    }
    
    private static Map<StorageRpc.Option, ?> optionMap(final BucketInfo a1, final Option... a2) {
        /*SL:1511*/return optionMap(null, a1.getMetageneration(), a2);
    }
    
    static Map<StorageRpc.Option, ?> optionMap(final BlobInfo a1, final Option... a2) {
        /*SL:1515*/return optionMap(a1.getGeneration(), a1.getMetageneration(), a2);
    }
    
    static Map<StorageRpc.Option, ?> optionMap(final BlobId a1, final Option... a2) {
        /*SL:1519*/return optionMap(a1.getGeneration(), null, a2);
    }
    
    static {
        EMPTY_BYTE_ARRAY = new byte[0];
        DELETE_FUNCTION = new Function<Tuple<Storage, Boolean>, Boolean>() {
            @Override
            public Boolean apply(final Tuple<Storage, Boolean> a1) {
                /*SL:97*/return (Boolean)a1.y();
            }
        };
    }
    
    private static class BucketPageFetcher implements PageImpl.NextPageFetcher<Bucket>
    {
        private static final long serialVersionUID = 5850406828803613729L;
        private final Map<StorageRpc.Option, ?> requestOptions;
        private final StorageOptions serviceOptions;
        
        BucketPageFetcher(final StorageOptions a1, final String a2, final Map<StorageRpc.Option, ?> a3) {
            this.requestOptions = (Map<StorageRpc.Option, ?>)PageImpl.nextRequestOptions((Object)StorageRpc.Option.PAGE_TOKEN, a2, (Map)a3);
            this.serviceOptions = a1;
        }
        
        public Page<Bucket> getNextPage() {
            /*SL:274*/return listBuckets(this.serviceOptions, this.requestOptions);
        }
    }
    
    private static class BlobPageFetcher implements PageImpl.NextPageFetcher<Blob>
    {
        private static final long serialVersionUID = 81807334445874098L;
        private final Map<StorageRpc.Option, ?> requestOptions;
        private final StorageOptions serviceOptions;
        private final String bucket;
        
        BlobPageFetcher(final String a1, final StorageOptions a2, final String a3, final Map<StorageRpc.Option, ?> a4) {
            this.requestOptions = (Map<StorageRpc.Option, ?>)PageImpl.nextRequestOptions((Object)StorageRpc.Option.PAGE_TOKEN, a3, (Map)a4);
            this.serviceOptions = a2;
            this.bucket = a1;
        }
        
        public Page<Blob> getNextPage() {
            /*SL:298*/return listBlobs(this.bucket, this.serviceOptions, this.requestOptions);
        }
    }
    
    private static class HmacKeyMetadataPageFetcher implements PageImpl.NextPageFetcher<HmacKey.HmacKeyMetadata>
    {
        private static final long serialVersionUID = 308012320541700881L;
        private final StorageOptions serviceOptions;
        private final Map<StorageRpc.Option, ?> options;
        
        HmacKeyMetadataPageFetcher(final StorageOptions a1, final Map<StorageRpc.Option, ?> a2) {
            this.serviceOptions = a1;
            this.options = a2;
        }
        
        public Page<HmacKey.HmacKeyMetadata> getNextPage() {
            /*SL:315*/return listHmacKeys(this.serviceOptions, this.options);
        }
    }
}

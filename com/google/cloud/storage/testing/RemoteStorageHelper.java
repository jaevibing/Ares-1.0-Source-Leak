package com.google.cloud.storage.testing;

import java.util.List;
import com.google.cloud.storage.StorageException;
import com.google.common.base.Strings;
import com.google.cloud.storage.BlobId;
import java.util.ArrayList;
import org.threeten.bp.Duration;
import com.google.api.gax.retrying.RetrySettings;
import com.google.cloud.http.HttpTransportOptions;
import java.io.IOException;
import java.util.logging.Level;
import com.google.cloud.TransportOptions;
import com.google.auth.Credentials;
import com.google.auth.oauth2.GoogleCredentials;
import java.io.InputStream;
import java.util.UUID;
import java.util.concurrent.Future;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.Iterator;
import com.google.api.gax.paging.Page;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import java.util.logging.Logger;

public class RemoteStorageHelper
{
    private static final Logger log;
    private static final String BUCKET_NAME_PREFIX = "gcloud-test-bucket-temp-";
    private final StorageOptions options;
    
    private RemoteStorageHelper(final StorageOptions a1) {
        this.options = a1;
    }
    
    public StorageOptions getOptions() {
        /*SL:69*/return this.options;
    }
    
    public static void cleanBuckets(final Storage a2, final long a3, final long v1) {
        final Runnable v2 = /*EL:73*/new Runnable() {
            @Override
            public void run() {
                final Page<Bucket> list = /*EL:77*/a2.list(/*EL:78*/Storage.BucketListOption.prefix("gcloud-test-bucket-temp-"));
                /*SL:79*/for (final Bucket bucket : list.iterateAll()) {
                    /*SL:80*/if (bucket.getCreateTime() < a3) {
                        try {
                            /*SL:83*/for (final Blob v1 : bucket.list(/*EL:85*/Storage.BlobListOption.fields(Storage.BlobField.EVENT_BASED_HOLD, Storage.BlobField.TEMPORARY_HOLD)).iterateAll()) {
                                /*SL:89*/if (v1.getEventBasedHold() || v1.getTemporaryHold()) {
                                    /*SL:90*/a2.update(v1.toBuilder().setTemporaryHold(/*EL:92*/Boolean.valueOf(false)).setEventBasedHold(/*EL:93*/Boolean.valueOf(false)).build());
                                }
                            }
                            /*SL:97*/RemoteStorageHelper.forceDelete(a2, bucket.getName());
                        }
                        catch (Exception ex) {}
                    }
                }
            }
        };
        final Thread v3 = /*EL:105*/new Thread(v2);
        /*SL:106*/v3.start();
        try {
            /*SL:108*/v3.join(v1);
        }
        catch (InterruptedException a4) {
            RemoteStorageHelper.log.info(/*EL:110*/"cleanBuckets interrupted");
        }
    }
    
    public static Boolean forceDelete(final Storage a1, final String a2, final long a3, final TimeUnit a4) throws InterruptedException, ExecutionException {
        /*SL:131*/return forceDelete(a1, a2, a3, a4, "");
    }
    
    public static Boolean forceDelete(final Storage a2, final String a3, final long a4, final TimeUnit a5, final String v1) throws InterruptedException, ExecutionException {
        final ExecutorService v2 = /*EL:153*/Executors.newSingleThreadExecutor();
        final Future<Boolean> v3 = /*EL:154*/v2.<Boolean>submit((Callable<Boolean>)new DeleteBucketTask(a2, a3, v1));
        try {
            /*SL:156*/return v3.get(a4, a5);
        }
        catch (TimeoutException a6) {
            /*SL:158*/return false;
        }
        finally {
            /*SL:160*/v2.shutdown();
        }
    }
    
    public static void forceDelete(final Storage a1, final String a2) {
        /*SL:172*/new DeleteBucketTask(a1, a2).call();
    }
    
    public static String generateBucketName() {
        /*SL:177*/return "gcloud-test-bucket-temp-" + UUID.randomUUID().toString();
    }
    
    public static RemoteStorageHelper create(final String v-1, final InputStream v0) throws StorageHelperException {
        try {
            HttpTransportOptions a1 = /*EL:193*/StorageOptions.getDefaultHttpTransportOptions();
            /*SL:195*/a1 = a1.toBuilder().setConnectTimeout(60000).setReadTimeout(60000).build();
            final StorageOptions a2 = /*EL:197*/((StorageOptions.Builder)((StorageOptions.Builder)((StorageOptions.Builder)StorageOptions.newBuilder().setCredentials(/*EL:198*/(Credentials)GoogleCredentials.fromStream(v0))).setProjectId(v-1)).setRetrySettings(retrySettings())).setTransportOptions(/*EL:200*/(TransportOptions)a1).build();
            /*SL:203*/return new RemoteStorageHelper(a2);
        }
        catch (IOException v) {
            /*SL:205*/if (RemoteStorageHelper.log.isLoggable(Level.WARNING)) {
                RemoteStorageHelper.log.log(Level.WARNING, /*EL:206*/v.getMessage());
            }
            /*SL:208*/throw StorageHelperException.translate(v);
        }
    }
    
    public static RemoteStorageHelper create() throws StorageHelperException {
        HttpTransportOptions v1 = /*EL:217*/StorageOptions.getDefaultHttpTransportOptions();
        /*SL:219*/v1 = v1.toBuilder().setConnectTimeout(60000).setReadTimeout(60000).build();
        final StorageOptions v2 = /*EL:221*/((StorageOptions.Builder)StorageOptions.newBuilder().setRetrySettings(retrySettings())).setTransportOptions(/*EL:222*/(TransportOptions)v1).build();
        /*SL:225*/return new RemoteStorageHelper(v2);
    }
    
    private static RetrySettings retrySettings() {
        /*SL:229*/return RetrySettings.newBuilder().setMaxAttempts(10).setMaxRetryDelay(/*EL:231*/Duration.ofMillis(30000L)).setTotalTimeout(/*EL:232*/Duration.ofMillis(120000L)).setInitialRetryDelay(/*EL:233*/Duration.ofMillis(250L)).setRetryDelayMultiplier(1.0).setInitialRpcTimeout(/*EL:235*/Duration.ofMillis(120000L)).setRpcTimeoutMultiplier(1.0).setMaxRpcTimeout(/*EL:237*/Duration.ofMillis(120000L)).build();
    }
    
    static {
        log = Logger.getLogger(RemoteStorageHelper.class.getName());
    }
    
    private static class DeleteBucketTask implements Callable<Boolean>
    {
        private final Storage storage;
        private final String bucket;
        private final String userProject;
        
        public DeleteBucketTask(final Storage a1, final String a2) {
            this.storage = a1;
            this.bucket = a2;
            this.userProject = "";
        }
        
        public DeleteBucketTask(final Storage a1, final String a2, final String a3) {
            this.storage = a1;
            this.bucket = a2;
            this.userProject = a3;
        }
        
        @Override
        public Boolean call() {
            while (true) {
                final ArrayList<BlobId> v0 = /*EL:262*/new ArrayList<BlobId>();
                Page<Blob> v;
                /*SL:264*/if (Strings.isNullOrEmpty(this.userProject)) {
                    /*SL:265*/v = this.storage.list(this.bucket, Storage.BlobListOption.versions(true));
                }
                else {
                    /*SL:268*/v = this.storage.list(this.bucket, /*EL:269*/Storage.BlobListOption.versions(true), Storage.BlobListOption.userProject(this.userProject));
                }
                /*SL:271*/for (final BlobInfo v2 : v.getValues()) {
                    /*SL:272*/v0.add(v2.getBlobId());
                }
                /*SL:274*/if (!v0.isEmpty()) {
                    final List<Boolean> v3 = /*EL:275*/this.storage.delete(v0);
                    /*SL:276*/if (!Strings.isNullOrEmpty(this.userProject)) {
                        /*SL:277*/for (int v4 = 0; v4 < v3.size(); ++v4) {
                            /*SL:278*/if (!v3.get(v4)) {
                                /*SL:280*/this.storage.delete(this.bucket, v0.get(v4).getName(), /*EL:283*/Storage.BlobSourceOption.userProject(this.userProject));
                            }
                        }
                    }
                }
                try {
                    /*SL:289*/if (Strings.isNullOrEmpty(this.userProject)) {
                        /*SL:290*/this.storage.delete(this.bucket, new Storage.BucketSourceOption[0]);
                    }
                    else {
                        /*SL:292*/this.storage.delete(this.bucket, Storage.BucketSourceOption.userProject(this.userProject));
                    }
                    /*SL:294*/return true;
                }
                catch (StorageException v5) {
                    /*SL:296*/if (v5.getCode() == 409) {
                        try {
                            /*SL:298*/Thread.sleep(500L);
                            /*SL:302*/continue;
                        }
                        catch (InterruptedException v6) {
                            Thread.currentThread().interrupt();
                            throw v5;
                        }
                        /*SL:304*/throw v5;
                        /*SL:307*/continue;
                    }
                    throw v5;
                }
            }
        }
    }
    
    public static class StorageHelperException extends RuntimeException
    {
        private static final long serialVersionUID = -7756074894502258736L;
        
        public StorageHelperException(final String a1) {
            super(a1);
        }
        
        public StorageHelperException(final String a1, final Throwable a2) {
            super(a1, a2);
        }
        
        public static StorageHelperException translate(final Exception a1) {
            /*SL:324*/return new StorageHelperException(a1.getMessage(), a1);
        }
    }
}

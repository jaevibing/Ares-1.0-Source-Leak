package com.google.cloud.storage.spi.v1;

import java.util.Objects;
import com.google.api.services.storage.model.ServiceAccount;
import com.google.api.services.storage.model.Notification;
import com.google.api.services.storage.model.TestIamPermissionsResponse;
import com.google.api.services.storage.model.Policy;
import com.google.api.services.storage.model.HmacKeyMetadata;
import com.google.api.services.storage.model.HmacKey;
import com.google.api.services.storage.model.ObjectAccessControl;
import java.util.List;
import com.google.api.services.storage.model.BucketAccessControl;
import java.io.OutputStream;
import com.google.cloud.Tuple;
import java.io.InputStream;
import com.google.api.services.storage.model.StorageObject;
import java.util.Map;
import com.google.api.services.storage.model.Bucket;
import com.google.api.core.InternalApi;
import com.google.cloud.ServiceRpc;

@InternalApi
public interface StorageRpc extends ServiceRpc
{
    Bucket create(Bucket p0, Map<Option, ?> p1);
    
    StorageObject create(StorageObject p0, InputStream p1, Map<Option, ?> p2);
    
    Tuple<String, Iterable<Bucket>> list(Map<Option, ?> p0);
    
    Tuple<String, Iterable<StorageObject>> list(String p0, Map<Option, ?> p1);
    
    Bucket get(Bucket p0, Map<Option, ?> p1);
    
    StorageObject get(StorageObject p0, Map<Option, ?> p1);
    
    Bucket patch(Bucket p0, Map<Option, ?> p1);
    
    StorageObject patch(StorageObject p0, Map<Option, ?> p1);
    
    boolean delete(Bucket p0, Map<Option, ?> p1);
    
    boolean delete(StorageObject p0, Map<Option, ?> p1);
    
    RpcBatch createBatch();
    
    StorageObject compose(Iterable<StorageObject> p0, StorageObject p1, Map<Option, ?> p2);
    
    byte[] load(StorageObject p0, Map<Option, ?> p1);
    
    Tuple<String, byte[]> read(StorageObject p0, Map<Option, ?> p1, long p2, int p3);
    
    long read(StorageObject p0, Map<Option, ?> p1, long p2, OutputStream p3);
    
    String open(StorageObject p0, Map<Option, ?> p1);
    
    String open(String p0);
    
    void write(String p0, byte[] p1, int p2, long p3, int p4, boolean p5);
    
    RewriteResponse openRewrite(RewriteRequest p0);
    
    RewriteResponse continueRewrite(RewriteResponse p0);
    
    BucketAccessControl getAcl(String p0, String p1, Map<Option, ?> p2);
    
    boolean deleteAcl(String p0, String p1, Map<Option, ?> p2);
    
    BucketAccessControl createAcl(BucketAccessControl p0, Map<Option, ?> p1);
    
    BucketAccessControl patchAcl(BucketAccessControl p0, Map<Option, ?> p1);
    
    List<BucketAccessControl> listAcls(String p0, Map<Option, ?> p1);
    
    ObjectAccessControl getDefaultAcl(String p0, String p1);
    
    boolean deleteDefaultAcl(String p0, String p1);
    
    ObjectAccessControl createDefaultAcl(ObjectAccessControl p0);
    
    ObjectAccessControl patchDefaultAcl(ObjectAccessControl p0);
    
    List<ObjectAccessControl> listDefaultAcls(String p0);
    
    ObjectAccessControl getAcl(String p0, String p1, Long p2, String p3);
    
    boolean deleteAcl(String p0, String p1, Long p2, String p3);
    
    ObjectAccessControl createAcl(ObjectAccessControl p0);
    
    ObjectAccessControl patchAcl(ObjectAccessControl p0);
    
    List<ObjectAccessControl> listAcls(String p0, String p1, Long p2);
    
    HmacKey createHmacKey(String p0, Map<Option, ?> p1);
    
    Tuple<String, Iterable<HmacKeyMetadata>> listHmacKeys(Map<Option, ?> p0);
    
    HmacKeyMetadata updateHmacKey(HmacKeyMetadata p0, Map<Option, ?> p1);
    
    HmacKeyMetadata getHmacKey(String p0, Map<Option, ?> p1);
    
    void deleteHmacKey(HmacKeyMetadata p0, Map<Option, ?> p1);
    
    Policy getIamPolicy(String p0, Map<Option, ?> p1);
    
    Policy setIamPolicy(String p0, Policy p1, Map<Option, ?> p2);
    
    TestIamPermissionsResponse testIamPermissions(String p0, List<String> p1, Map<Option, ?> p2);
    
    boolean deleteNotification(String p0, String p1);
    
    List<Notification> listNotifications(String p0);
    
    Notification createNotification(String p0, Notification p1);
    
    Bucket lockRetentionPolicy(Bucket p0, Map<Option, ?> p1);
    
    ServiceAccount getServiceAccount(String p0);
    
    public enum Option
    {
        PREDEFINED_ACL("predefinedAcl"), 
        PREDEFINED_DEFAULT_OBJECT_ACL("predefinedDefaultObjectAcl"), 
        IF_METAGENERATION_MATCH("ifMetagenerationMatch"), 
        IF_METAGENERATION_NOT_MATCH("ifMetagenerationNotMatch"), 
        IF_GENERATION_MATCH("ifGenerationMatch"), 
        IF_GENERATION_NOT_MATCH("ifGenerationNotMatch"), 
        IF_SOURCE_METAGENERATION_MATCH("ifSourceMetagenerationMatch"), 
        IF_SOURCE_METAGENERATION_NOT_MATCH("ifSourceMetagenerationNotMatch"), 
        IF_SOURCE_GENERATION_MATCH("ifSourceGenerationMatch"), 
        IF_SOURCE_GENERATION_NOT_MATCH("ifSourceGenerationNotMatch"), 
        IF_DISABLE_GZIP_CONTENT("disableGzipContent"), 
        PREFIX("prefix"), 
        PROJECT_ID("projectId"), 
        PROJECTION("projection"), 
        MAX_RESULTS("maxResults"), 
        PAGE_TOKEN("pageToken"), 
        DELIMITER("delimiter"), 
        VERSIONS("versions"), 
        FIELDS("fields"), 
        CUSTOMER_SUPPLIED_KEY("customerSuppliedKey"), 
        USER_PROJECT("userProject"), 
        KMS_KEY_NAME("kmsKeyName"), 
        SERVICE_ACCOUNT_EMAIL("serviceAccount"), 
        SHOW_DELETED_KEYS("showDeletedKeys");
        
        private final String value;
        
        private Option(String a1) {
            this.value = a1;
        }
        
        public String value() {
            /*SL:76*/return this.value;
        }
        
         <T> T get(Map<Option, ?> a1) {
            /*SL:81*/return (T)a1.get(this);
        }
        
        String getString(Map<Option, ?> a1) {
            /*SL:85*/return this.<String>get(a1);
        }
        
        Long getLong(Map<Option, ?> a1) {
            /*SL:89*/return this.<Long>get(a1);
        }
        
        Boolean getBoolean(Map<Option, ?> a1) {
            /*SL:93*/return this.<Boolean>get(a1);
        }
    }
    
    public static class RewriteRequest
    {
        public final StorageObject source;
        public final Map<Option, ?> sourceOptions;
        public final boolean overrideInfo;
        public final StorageObject target;
        public final Map<Option, ?> targetOptions;
        public final Long megabytesRewrittenPerCall;
        
        public RewriteRequest(StorageObject a1, Map<Option, ?> a2, boolean a3, StorageObject a4, Map<Option, ?> a5, Long a6) {
            this.source = a1;
            this.sourceOptions = a2;
            this.overrideInfo = a3;
            this.target = a4;
            this.targetOptions = a5;
            this.megabytesRewrittenPerCall = a6;
        }
        
        @Override
        public boolean equals(Object a1) {
            RewriteRequest v1;
            /*SL:123*/if (a1 == null) {
                /*SL:124*/return false;
            }
            /*SL:126*/if (!(a1 instanceof RewriteRequest)) {
                /*SL:127*/return false;
            }
            /*SL:129*/v1 = (RewriteRequest)a1;
            /*SL:130*/return Objects.equals(this.source, v1.source) && /*EL:131*/Objects.equals(this.sourceOptions, v1.sourceOptions) && /*EL:132*/Objects.equals(this.overrideInfo, v1.overrideInfo) && /*EL:133*/Objects.equals(this.target, v1.target) && /*EL:134*/Objects.equals(this.targetOptions, v1.targetOptions) && /*EL:135*/Objects.equals(this.megabytesRewrittenPerCall, v1.megabytesRewrittenPerCall);
        }
        
        @Override
        public int hashCode() {
            /*SL:140*/return Objects.hash(this.source, this.sourceOptions, this.overrideInfo, /*EL:141*/this.target, this.targetOptions, this.megabytesRewrittenPerCall);
        }
    }
    
    public static class RewriteResponse
    {
        public final RewriteRequest rewriteRequest;
        public final StorageObject result;
        public final long blobSize;
        public final boolean isDone;
        public final String rewriteToken;
        public final long totalBytesRewritten;
        
        public RewriteResponse(RewriteRequest a1, StorageObject a2, long a3, boolean a4, String a5, long a6) {
            this.rewriteRequest = a1;
            this.result = a2;
            this.blobSize = a3;
            this.isDone = a4;
            this.rewriteToken = a5;
            this.totalBytesRewritten = a6;
        }
        
        @Override
        public boolean equals(Object a1) {
            RewriteResponse v1;
            /*SL:171*/if (a1 == null) {
                /*SL:172*/return false;
            }
            /*SL:174*/if (!(a1 instanceof RewriteResponse)) {
                /*SL:175*/return false;
            }
            /*SL:177*/v1 = (RewriteResponse)a1;
            /*SL:178*/return Objects.equals(this.rewriteRequest, v1.rewriteRequest) && /*EL:179*/Objects.equals(this.result, v1.result) && /*EL:180*/Objects.equals(this.rewriteToken, v1.rewriteToken) && this.blobSize == v1.blobSize && /*EL:182*/Objects.equals(this.isDone, v1.isDone) && this.totalBytesRewritten == v1.totalBytesRewritten;
        }
        
        @Override
        public int hashCode() {
            /*SL:188*/return Objects.hash(this.rewriteRequest, this.result, this.blobSize, /*EL:189*/this.isDone, this.rewriteToken, this.totalBytesRewritten);
        }
    }
}

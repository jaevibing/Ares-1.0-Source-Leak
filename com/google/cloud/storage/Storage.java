package com.google.cloud.storage;

import com.google.common.base.Preconditions;
import com.google.common.collect.Iterables;
import java.util.Collections;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Set;
import java.util.Collection;
import com.google.auth.ServiceAccountSigner;
import java.util.Map;
import java.util.Objects;
import java.io.Serializable;
import com.google.common.collect.Lists;
import com.google.cloud.Tuple;
import com.google.common.io.BaseEncoding;
import java.security.Key;
import com.google.cloud.storage.spi.v1.StorageRpc;
import com.google.common.collect.ImmutableList;
import com.google.cloud.FieldSelector;
import com.google.cloud.Policy;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.net.URL;
import com.google.cloud.WriteChannel;
import com.google.cloud.ReadChannel;
import com.google.api.gax.paging.Page;
import java.io.InputStream;
import com.google.cloud.Service;

public interface Storage extends Service<StorageOptions>
{
    Bucket create(BucketInfo p0, BucketTargetOption... p1);
    
    Blob create(BlobInfo p0, BlobTargetOption... p1);
    
    Blob create(BlobInfo p0, byte[] p1, BlobTargetOption... p2);
    
    Blob create(BlobInfo p0, byte[] p1, int p2, int p3, BlobTargetOption... p4);
    
    @Deprecated
    Blob create(BlobInfo p0, InputStream p1, BlobWriteOption... p2);
    
    Bucket get(String p0, BucketGetOption... p1);
    
    Bucket lockRetentionPolicy(BucketInfo p0, BucketTargetOption... p1);
    
    Blob get(String p0, String p1, BlobGetOption... p2);
    
    Blob get(BlobId p0, BlobGetOption... p1);
    
    Blob get(BlobId p0);
    
    Page<Bucket> list(BucketListOption... p0);
    
    Page<Blob> list(String p0, BlobListOption... p1);
    
    Bucket update(BucketInfo p0, BucketTargetOption... p1);
    
    Blob update(BlobInfo p0, BlobTargetOption... p1);
    
    Blob update(BlobInfo p0);
    
    boolean delete(String p0, BucketSourceOption... p1);
    
    boolean delete(String p0, String p1, BlobSourceOption... p2);
    
    boolean delete(BlobId p0, BlobSourceOption... p1);
    
    boolean delete(BlobId p0);
    
    Blob compose(ComposeRequest p0);
    
    CopyWriter copy(CopyRequest p0);
    
    byte[] readAllBytes(String p0, String p1, BlobSourceOption... p2);
    
    byte[] readAllBytes(BlobId p0, BlobSourceOption... p1);
    
    StorageBatch batch();
    
    ReadChannel reader(String p0, String p1, BlobSourceOption... p2);
    
    ReadChannel reader(BlobId p0, BlobSourceOption... p1);
    
    WriteChannel writer(BlobInfo p0, BlobWriteOption... p1);
    
    WriteChannel writer(URL p0);
    
    URL signUrl(BlobInfo p0, long p1, TimeUnit p2, SignUrlOption... p3);
    
    List<Blob> get(BlobId... p0);
    
    List<Blob> get(Iterable<BlobId> p0);
    
    List<Blob> update(BlobInfo... p0);
    
    List<Blob> update(Iterable<BlobInfo> p0);
    
    List<Boolean> delete(BlobId... p0);
    
    List<Boolean> delete(Iterable<BlobId> p0);
    
    Acl getAcl(String p0, Acl.Entity p1, BucketSourceOption... p2);
    
    Acl getAcl(String p0, Acl.Entity p1);
    
    boolean deleteAcl(String p0, Acl.Entity p1, BucketSourceOption... p2);
    
    boolean deleteAcl(String p0, Acl.Entity p1);
    
    Acl createAcl(String p0, Acl p1, BucketSourceOption... p2);
    
    Acl createAcl(String p0, Acl p1);
    
    Acl updateAcl(String p0, Acl p1, BucketSourceOption... p2);
    
    Acl updateAcl(String p0, Acl p1);
    
    List<Acl> listAcls(String p0, BucketSourceOption... p1);
    
    List<Acl> listAcls(String p0);
    
    Acl getDefaultAcl(String p0, Acl.Entity p1);
    
    boolean deleteDefaultAcl(String p0, Acl.Entity p1);
    
    Acl createDefaultAcl(String p0, Acl p1);
    
    Acl updateDefaultAcl(String p0, Acl p1);
    
    List<Acl> listDefaultAcls(String p0);
    
    Acl getAcl(BlobId p0, Acl.Entity p1);
    
    boolean deleteAcl(BlobId p0, Acl.Entity p1);
    
    Acl createAcl(BlobId p0, Acl p1);
    
    Acl updateAcl(BlobId p0, Acl p1);
    
    List<Acl> listAcls(BlobId p0);
    
    HmacKey createHmacKey(ServiceAccount p0, CreateHmacKeyOption... p1);
    
    Page<HmacKey.HmacKeyMetadata> listHmacKeys(ListHmacKeysOption... p0);
    
    HmacKey.HmacKeyMetadata getHmacKey(String p0, GetHmacKeyOption... p1);
    
    void deleteHmacKey(HmacKey.HmacKeyMetadata p0, DeleteHmacKeyOption... p1);
    
    HmacKey.HmacKeyMetadata updateHmacKeyState(HmacKey.HmacKeyMetadata p0, HmacKey.HmacKeyState p1, UpdateHmacKeyOption... p2);
    
    Policy getIamPolicy(String p0, BucketSourceOption... p1);
    
    Policy setIamPolicy(String p0, Policy p1, BucketSourceOption... p2);
    
    List<Boolean> testIamPermissions(String p0, List<String> p1, BucketSourceOption... p2);
    
    ServiceAccount getServiceAccount(String p0);
    
    public enum PredefinedAcl
    {
        AUTHENTICATED_READ("authenticatedRead"), 
        ALL_AUTHENTICATED_USERS("allAuthenticatedUsers"), 
        PRIVATE("private"), 
        PROJECT_PRIVATE("projectPrivate"), 
        PUBLIC_READ("publicRead"), 
        PUBLIC_READ_WRITE("publicReadWrite"), 
        BUCKET_OWNER_READ("bucketOwnerRead"), 
        BUCKET_OWNER_FULL_CONTROL("bucketOwnerFullControl");
        
        private final String entry;
        
        private PredefinedAcl(String a1) {
            this.entry = a1;
        }
        
        String getEntry() {
            /*SL:77*/return this.entry;
        }
    }
    
    public enum BucketField implements FieldSelector
    {
        ID("id"), 
        SELF_LINK("selfLink"), 
        NAME("name"), 
        TIME_CREATED("timeCreated"), 
        METAGENERATION("metageneration"), 
        ACL("acl"), 
        DEFAULT_OBJECT_ACL("defaultObjectAcl"), 
        OWNER("owner"), 
        LABELS("labels"), 
        LOCATION("location"), 
        LOCATION_TYPE("locationType"), 
        WEBSITE("website"), 
        VERSIONING("versioning"), 
        CORS("cors"), 
        LIFECYCLE("lifecycle"), 
        STORAGE_CLASS("storageClass"), 
        ETAG("etag"), 
        ENCRYPTION("encryption"), 
        BILLING("billing"), 
        DEFAULT_EVENT_BASED_HOLD("defaultEventBasedHold"), 
        RETENTION_POLICY("retentionPolicy"), 
        IAMCONFIGURATION("iamConfiguration");
        
        static final List<? extends FieldSelector> REQUIRED_FIELDS;
        private final String selector;
        
        private BucketField(String a1) {
            this.selector = a1;
        }
        
        public String getSelector() {
            /*SL:115*/return this.selector;
        }
        
        static {
            REQUIRED_FIELDS = ImmutableList.<BucketField>of(BucketField.NAME);
        }
    }
    
    public enum BlobField implements FieldSelector
    {
        ACL("acl"), 
        BUCKET("bucket"), 
        CACHE_CONTROL("cacheControl"), 
        COMPONENT_COUNT("componentCount"), 
        CONTENT_DISPOSITION("contentDisposition"), 
        CONTENT_ENCODING("contentEncoding"), 
        CONTENT_LANGUAGE("contentLanguage"), 
        CONTENT_TYPE("contentType"), 
        CRC32C("crc32c"), 
        ETAG("etag"), 
        GENERATION("generation"), 
        ID("id"), 
        KIND("kind"), 
        MD5HASH("md5Hash"), 
        MEDIA_LINK("mediaLink"), 
        METADATA("metadata"), 
        METAGENERATION("metageneration"), 
        NAME("name"), 
        OWNER("owner"), 
        SELF_LINK("selfLink"), 
        SIZE("size"), 
        STORAGE_CLASS("storageClass"), 
        TIME_DELETED("timeDeleted"), 
        TIME_CREATED("timeCreated"), 
        KMS_KEY_NAME("kmsKeyName"), 
        EVENT_BASED_HOLD("eventBasedHold"), 
        TEMPORARY_HOLD("temporaryHold"), 
        RETENTION_EXPIRATION_TIME("retentionExpirationTime"), 
        UPDATED("updated");
        
        static final List<? extends FieldSelector> REQUIRED_FIELDS;
        private final String selector;
        
        private BlobField(String a1) {
            this.selector = a1;
        }
        
        public String getSelector() {
            /*SL:160*/return this.selector;
        }
        
        static {
            REQUIRED_FIELDS = ImmutableList.<BlobField>of(BlobField.BUCKET, BlobField.NAME);
        }
    }
    
    public static class BucketTargetOption extends Option
    {
        private static final long serialVersionUID = -5880204616982900975L;
        
        private BucketTargetOption(StorageRpc.Option a1, Object a2) {
            super(a1, a2);
        }
        
        private BucketTargetOption(StorageRpc.Option a1) {
            this(a1, null);
        }
        
        public static BucketTargetOption predefinedAcl(PredefinedAcl a1) {
            /*SL:179*/return new BucketTargetOption(StorageRpc.Option.PREDEFINED_ACL, a1.getEntry());
        }
        
        public static BucketTargetOption predefinedDefaultObjectAcl(PredefinedAcl a1) {
            /*SL:184*/return /*EL:185*/new BucketTargetOption(StorageRpc.Option.PREDEFINED_DEFAULT_OBJECT_ACL, a1.getEntry());
        }
        
        public static BucketTargetOption metagenerationMatch() {
            /*SL:193*/return new BucketTargetOption(StorageRpc.Option.IF_METAGENERATION_MATCH);
        }
        
        public static BucketTargetOption metagenerationNotMatch() {
            /*SL:201*/return new BucketTargetOption(StorageRpc.Option.IF_METAGENERATION_NOT_MATCH);
        }
        
        public static BucketTargetOption userProject(String a1) {
            /*SL:209*/return new BucketTargetOption(StorageRpc.Option.USER_PROJECT, a1);
        }
        
        public static BucketTargetOption projection(String a1) {
            /*SL:221*/return new BucketTargetOption(StorageRpc.Option.PROJECTION, a1);
        }
    }
    
    public static class BucketSourceOption extends Option
    {
        private static final long serialVersionUID = 5185657617120212117L;
        
        private BucketSourceOption(StorageRpc.Option a1, Object a2) {
            super(a1, a2);
        }
        
        public static BucketSourceOption metagenerationMatch(long a1) {
            /*SL:239*/return new BucketSourceOption(StorageRpc.Option.IF_METAGENERATION_MATCH, a1);
        }
        
        public static BucketSourceOption metagenerationNotMatch(long a1) {
            /*SL:247*/return new BucketSourceOption(StorageRpc.Option.IF_METAGENERATION_NOT_MATCH, a1);
        }
        
        public static BucketSourceOption userProject(String a1) {
            /*SL:255*/return new BucketSourceOption(StorageRpc.Option.USER_PROJECT, a1);
        }
    }
    
    public static class ListHmacKeysOption extends Option
    {
        private ListHmacKeysOption(StorageRpc.Option a1, Object a2) {
            super(a1, a2);
        }
        
        public static ListHmacKeysOption serviceAccount(ServiceAccount a1) {
            /*SL:270*/return /*EL:271*/new ListHmacKeysOption(StorageRpc.Option.SERVICE_ACCOUNT_EMAIL, a1.getEmail());
        }
        
        public static ListHmacKeysOption maxResults(long a1) {
            /*SL:276*/return new ListHmacKeysOption(StorageRpc.Option.MAX_RESULTS, a1);
        }
        
        public static ListHmacKeysOption pageToken(String a1) {
            /*SL:281*/return new ListHmacKeysOption(StorageRpc.Option.PAGE_TOKEN, a1);
        }
        
        public static ListHmacKeysOption showDeletedKeys(boolean a1) {
            /*SL:289*/return new ListHmacKeysOption(StorageRpc.Option.SHOW_DELETED_KEYS, a1);
        }
        
        public static ListHmacKeysOption userProject(String a1) {
            /*SL:297*/return new ListHmacKeysOption(StorageRpc.Option.USER_PROJECT, a1);
        }
        
        public static ListHmacKeysOption projectId(String a1) {
            /*SL:305*/return new ListHmacKeysOption(StorageRpc.Option.PROJECT_ID, a1);
        }
    }
    
    public static class CreateHmacKeyOption extends Option
    {
        private CreateHmacKeyOption(StorageRpc.Option a1, Object a2) {
            super(a1, a2);
        }
        
        public static CreateHmacKeyOption userProject(String a1) {
            /*SL:320*/return new CreateHmacKeyOption(StorageRpc.Option.USER_PROJECT, a1);
        }
        
        public static CreateHmacKeyOption projectId(String a1) {
            /*SL:328*/return new CreateHmacKeyOption(StorageRpc.Option.PROJECT_ID, a1);
        }
    }
    
    public static class GetHmacKeyOption extends Option
    {
        private GetHmacKeyOption(StorageRpc.Option a1, Object a2) {
            super(a1, a2);
        }
        
        public static GetHmacKeyOption userProject(String a1) {
            /*SL:343*/return new GetHmacKeyOption(StorageRpc.Option.USER_PROJECT, a1);
        }
        
        public static GetHmacKeyOption projectId(String a1) {
            /*SL:351*/return new GetHmacKeyOption(StorageRpc.Option.PROJECT_ID, a1);
        }
    }
    
    public static class DeleteHmacKeyOption extends Option
    {
        private DeleteHmacKeyOption(StorageRpc.Option a1, Object a2) {
            super(a1, a2);
        }
        
        public static DeleteHmacKeyOption userProject(String a1) {
            /*SL:366*/return new DeleteHmacKeyOption(StorageRpc.Option.USER_PROJECT, a1);
        }
    }
    
    public static class UpdateHmacKeyOption extends Option
    {
        private UpdateHmacKeyOption(StorageRpc.Option a1, Object a2) {
            super(a1, a2);
        }
        
        public static UpdateHmacKeyOption userProject(String a1) {
            /*SL:381*/return new UpdateHmacKeyOption(StorageRpc.Option.USER_PROJECT, a1);
        }
    }
    
    public static class BucketGetOption extends Option
    {
        private static final long serialVersionUID = 1901844869484087395L;
        
        private BucketGetOption(StorageRpc.Option a1, long a2) {
            super(a1, a2);
        }
        
        private BucketGetOption(StorageRpc.Option a1, String a2) {
            super(a1, a2);
        }
        
        public static BucketGetOption metagenerationMatch(long a1) {
            /*SL:403*/return new BucketGetOption(StorageRpc.Option.IF_METAGENERATION_MATCH, a1);
        }
        
        public static BucketGetOption metagenerationNotMatch(long a1) {
            /*SL:411*/return new BucketGetOption(StorageRpc.Option.IF_METAGENERATION_NOT_MATCH, a1);
        }
        
        public static BucketGetOption userProject(String a1) {
            /*SL:419*/return new BucketGetOption(StorageRpc.Option.USER_PROJECT, a1);
        }
        
        public static BucketGetOption fields(BucketField... a1) {
            /*SL:429*/return /*EL:430*/new BucketGetOption(StorageRpc.Option.FIELDS, FieldSelector.Helper.selector((List)BucketField.REQUIRED_FIELDS, (FieldSelector[])a1));
        }
    }
    
    public static class BlobTargetOption extends Option
    {
        private static final long serialVersionUID = 214616862061934846L;
        
        private BlobTargetOption(StorageRpc.Option a1, Object a2) {
            super(a1, a2);
        }
        
        private BlobTargetOption(StorageRpc.Option a1) {
            this(a1, null);
        }
        
        public static BlobTargetOption predefinedAcl(PredefinedAcl a1) {
            /*SL:449*/return new BlobTargetOption(StorageRpc.Option.PREDEFINED_ACL, a1.getEntry());
        }
        
        public static BlobTargetOption doesNotExist() {
            /*SL:456*/return new BlobTargetOption(StorageRpc.Option.IF_GENERATION_MATCH, 0L);
        }
        
        public static BlobTargetOption generationMatch() {
            /*SL:464*/return new BlobTargetOption(StorageRpc.Option.IF_GENERATION_MATCH);
        }
        
        public static BlobTargetOption generationNotMatch() {
            /*SL:472*/return new BlobTargetOption(StorageRpc.Option.IF_GENERATION_NOT_MATCH);
        }
        
        public static BlobTargetOption metagenerationMatch() {
            /*SL:480*/return new BlobTargetOption(StorageRpc.Option.IF_METAGENERATION_MATCH);
        }
        
        public static BlobTargetOption metagenerationNotMatch() {
            /*SL:488*/return new BlobTargetOption(StorageRpc.Option.IF_METAGENERATION_NOT_MATCH);
        }
        
        public static BlobTargetOption disableGzipContent() {
            /*SL:496*/return new BlobTargetOption(StorageRpc.Option.IF_DISABLE_GZIP_CONTENT, true);
        }
        
        public static BlobTargetOption encryptionKey(Key a1) {
            String v1;
            /*SL:504*/v1 = BaseEncoding.base64().encode(a1.getEncoded());
            /*SL:505*/return new BlobTargetOption(StorageRpc.Option.CUSTOMER_SUPPLIED_KEY, v1);
        }
        
        public static BlobTargetOption userProject(String a1) {
            /*SL:513*/return new BlobTargetOption(StorageRpc.Option.USER_PROJECT, a1);
        }
        
        public static BlobTargetOption encryptionKey(String a1) {
            /*SL:523*/return new BlobTargetOption(StorageRpc.Option.CUSTOMER_SUPPLIED_KEY, a1);
        }
        
        public static BlobTargetOption kmsKeyName(String a1) {
            /*SL:528*/return new BlobTargetOption(StorageRpc.Option.KMS_KEY_NAME, a1);
        }
        
        static Tuple<BlobInfo, BlobTargetOption[]> convert(BlobInfo a2, BlobWriteOption... v1) {
            BlobInfo.Builder v2;
            List<BlobTargetOption> v3;
            int length;
            int i;
            BlobWriteOption a3;
            /*SL:532*/v2 = a2.toBuilder().setCrc32c(null).setMd5(null);
            /*SL:533*/v3 = (List<BlobTargetOption>)Lists.<Object>newArrayListWithCapacity(v1.length);
            /*SL:534*/for (final BlobWriteOption a3 : v1) {
                /*SL:535*/switch (a3.option) {
                    case IF_CRC32C_MATCH: {
                        /*SL:537*/v2.setCrc32c(a2.getCrc32c());
                        /*SL:538*/break;
                    }
                    case IF_MD5_MATCH: {
                        /*SL:540*/v2.setMd5(a2.getMd5());
                        /*SL:541*/break;
                    }
                    default: {
                        /*SL:543*/v3.add(a3.toTargetOption());
                        break;
                    }
                }
            }
            /*SL:547*/return (Tuple<BlobInfo, BlobTargetOption[]>)Tuple.of((Object)v2.build(), /*EL:548*/(Object)v3.<BlobTargetOption>toArray(new BlobTargetOption[v3.size()]));
        }
    }
    
    public static class BlobWriteOption implements Serializable
    {
        private static final long serialVersionUID = -3880421670966224580L;
        private final Option option;
        private final Object value;
        
        BlobTargetOption toTargetOption() {
            /*SL:578*/return new BlobTargetOption(this.option.toRpcOption(), this.value);
        }
        
        private BlobWriteOption(Option a1, Object a2) {
            this.option = a1;
            this.value = a2;
        }
        
        private BlobWriteOption(Option a1) {
            this(a1, null);
        }
        
        @Override
        public int hashCode() {
            /*SL:592*/return Objects.hash(this.option, this.value);
        }
        
        @Override
        public boolean equals(Object a1) {
            BlobWriteOption v1;
            /*SL:597*/if (a1 == null) {
                /*SL:598*/return false;
            }
            /*SL:600*/if (!(a1 instanceof BlobWriteOption)) {
                /*SL:601*/return false;
            }
            /*SL:603*/v1 = (BlobWriteOption)a1;
            /*SL:604*/return this.option == v1.option && Objects.equals(this.value, v1.value);
        }
        
        public static BlobWriteOption predefinedAcl(PredefinedAcl a1) {
            /*SL:609*/return new BlobWriteOption(Option.PREDEFINED_ACL, a1.getEntry());
        }
        
        public static BlobWriteOption doesNotExist() {
            /*SL:616*/return new BlobWriteOption(Option.IF_GENERATION_MATCH, 0L);
        }
        
        public static BlobWriteOption generationMatch() {
            /*SL:624*/return new BlobWriteOption(Option.IF_GENERATION_MATCH);
        }
        
        public static BlobWriteOption generationNotMatch() {
            /*SL:632*/return new BlobWriteOption(Option.IF_GENERATION_NOT_MATCH);
        }
        
        public static BlobWriteOption metagenerationMatch() {
            /*SL:640*/return new BlobWriteOption(Option.IF_METAGENERATION_MATCH);
        }
        
        public static BlobWriteOption metagenerationNotMatch() {
            /*SL:648*/return new BlobWriteOption(Option.IF_METAGENERATION_NOT_MATCH);
        }
        
        public static BlobWriteOption md5Match() {
            /*SL:656*/return new BlobWriteOption(Option.IF_MD5_MATCH, true);
        }
        
        public static BlobWriteOption crc32cMatch() {
            /*SL:664*/return new BlobWriteOption(Option.IF_CRC32C_MATCH, true);
        }
        
        public static BlobWriteOption encryptionKey(Key a1) {
            String v1;
            /*SL:672*/v1 = BaseEncoding.base64().encode(a1.getEncoded());
            /*SL:673*/return new BlobWriteOption(Option.CUSTOMER_SUPPLIED_KEY, v1);
        }
        
        public static BlobWriteOption encryptionKey(String a1) {
            /*SL:683*/return new BlobWriteOption(Option.CUSTOMER_SUPPLIED_KEY, a1);
        }
        
        public static BlobWriteOption kmsKeyName(String a1) {
            /*SL:692*/return new BlobWriteOption(Option.KMS_KEY_NAME, a1);
        }
        
        public static BlobWriteOption userProject(String a1) {
            /*SL:700*/return new BlobWriteOption(Option.USER_PROJECT, a1);
        }
        
        enum Option
        {
            PREDEFINED_ACL, 
            IF_GENERATION_MATCH, 
            IF_GENERATION_NOT_MATCH, 
            IF_METAGENERATION_MATCH, 
            IF_METAGENERATION_NOT_MATCH, 
            IF_MD5_MATCH, 
            IF_CRC32C_MATCH, 
            CUSTOMER_SUPPLIED_KEY, 
            KMS_KEY_NAME, 
            USER_PROJECT;
            
            StorageRpc.Option toRpcOption() {
                /*SL:573*/return StorageRpc.Option.valueOf(this.name());
            }
        }
    }
    
    public static class BlobSourceOption extends Option
    {
        private static final long serialVersionUID = -3712768261070182991L;
        
        private BlobSourceOption(StorageRpc.Option a1, Object a2) {
            super(a1, a2);
        }
        
        public static BlobSourceOption generationMatch() {
            /*SL:721*/return new BlobSourceOption(StorageRpc.Option.IF_GENERATION_MATCH, null);
        }
        
        public static BlobSourceOption generationMatch(long a1) {
            /*SL:729*/return new BlobSourceOption(StorageRpc.Option.IF_GENERATION_MATCH, a1);
        }
        
        public static BlobSourceOption generationNotMatch() {
            /*SL:740*/return new BlobSourceOption(StorageRpc.Option.IF_GENERATION_NOT_MATCH, null);
        }
        
        public static BlobSourceOption generationNotMatch(long a1) {
            /*SL:748*/return new BlobSourceOption(StorageRpc.Option.IF_GENERATION_NOT_MATCH, a1);
        }
        
        public static BlobSourceOption metagenerationMatch(long a1) {
            /*SL:756*/return new BlobSourceOption(StorageRpc.Option.IF_METAGENERATION_MATCH, a1);
        }
        
        public static BlobSourceOption metagenerationNotMatch(long a1) {
            /*SL:764*/return new BlobSourceOption(StorageRpc.Option.IF_METAGENERATION_NOT_MATCH, a1);
        }
        
        public static BlobSourceOption decryptionKey(Key a1) {
            String v1;
            /*SL:772*/v1 = BaseEncoding.base64().encode(a1.getEncoded());
            /*SL:773*/return new BlobSourceOption(StorageRpc.Option.CUSTOMER_SUPPLIED_KEY, v1);
        }
        
        public static BlobSourceOption decryptionKey(String a1) {
            /*SL:783*/return new BlobSourceOption(StorageRpc.Option.CUSTOMER_SUPPLIED_KEY, a1);
        }
        
        public static BlobSourceOption userProject(String a1) {
            /*SL:791*/return new BlobSourceOption(StorageRpc.Option.USER_PROJECT, a1);
        }
    }
    
    public static class BlobGetOption extends Option
    {
        private static final long serialVersionUID = 803817709703661480L;
        
        private BlobGetOption(StorageRpc.Option a1, Long a2) {
            super(a1, a2);
        }
        
        private BlobGetOption(StorageRpc.Option a1, String a2) {
            super(a1, a2);
        }
        
        public static BlobGetOption generationMatch() {
            /*SL:816*/return new BlobGetOption(StorageRpc.Option.IF_GENERATION_MATCH, (Long)null);
        }
        
        public static BlobGetOption generationMatch(long a1) {
            /*SL:824*/return new BlobGetOption(StorageRpc.Option.IF_GENERATION_MATCH, Long.valueOf(a1));
        }
        
        public static BlobGetOption generationNotMatch() {
            /*SL:835*/return new BlobGetOption(StorageRpc.Option.IF_GENERATION_NOT_MATCH, (Long)null);
        }
        
        public static BlobGetOption generationNotMatch(long a1) {
            /*SL:843*/return new BlobGetOption(StorageRpc.Option.IF_GENERATION_NOT_MATCH, Long.valueOf(a1));
        }
        
        public static BlobGetOption metagenerationMatch(long a1) {
            /*SL:851*/return new BlobGetOption(StorageRpc.Option.IF_METAGENERATION_MATCH, Long.valueOf(a1));
        }
        
        public static BlobGetOption metagenerationNotMatch(long a1) {
            /*SL:859*/return new BlobGetOption(StorageRpc.Option.IF_METAGENERATION_NOT_MATCH, Long.valueOf(a1));
        }
        
        public static BlobGetOption fields(BlobField... a1) {
            /*SL:869*/return /*EL:870*/new BlobGetOption(StorageRpc.Option.FIELDS, FieldSelector.Helper.selector((List)BlobField.REQUIRED_FIELDS, (FieldSelector[])a1));
        }
        
        public static BlobGetOption userProject(String a1) {
            /*SL:878*/return new BlobGetOption(StorageRpc.Option.USER_PROJECT, a1);
        }
        
        public static BlobGetOption decryptionKey(Key a1) {
            String v1;
            /*SL:886*/v1 = BaseEncoding.base64().encode(a1.getEncoded());
            /*SL:887*/return new BlobGetOption(StorageRpc.Option.CUSTOMER_SUPPLIED_KEY, v1);
        }
        
        public static BlobGetOption decryptionKey(String a1) {
            /*SL:897*/return new BlobGetOption(StorageRpc.Option.CUSTOMER_SUPPLIED_KEY, a1);
        }
    }
    
    public static class BucketListOption extends Option
    {
        private static final long serialVersionUID = 8754017079673290353L;
        
        private BucketListOption(StorageRpc.Option a1, Object a2) {
            super(a1, a2);
        }
        
        public static BucketListOption pageSize(long a1) {
            /*SL:912*/return new BucketListOption(StorageRpc.Option.MAX_RESULTS, a1);
        }
        
        public static BucketListOption pageToken(String a1) {
            /*SL:917*/return new BucketListOption(StorageRpc.Option.PAGE_TOKEN, a1);
        }
        
        public static BucketListOption prefix(String a1) {
            /*SL:925*/return new BucketListOption(StorageRpc.Option.PREFIX, a1);
        }
        
        public static BucketListOption userProject(String a1) {
            /*SL:933*/return new BucketListOption(StorageRpc.Option.USER_PROJECT, a1);
        }
        
        public static BucketListOption fields(BucketField... a1) {
            /*SL:943*/return /*EL:945*/new BucketListOption(StorageRpc.Option.FIELDS, FieldSelector.Helper.listSelector("items", (List)BucketField.REQUIRED_FIELDS, (FieldSelector[])a1));
        }
    }
    
    public static class BlobListOption extends Option
    {
        private static final String[] TOP_LEVEL_FIELDS;
        private static final long serialVersionUID = 9083383524788661294L;
        
        private BlobListOption(StorageRpc.Option a1, Object a2) {
            super(a1, a2);
        }
        
        public static BlobListOption pageSize(long a1) {
            /*SL:961*/return new BlobListOption(StorageRpc.Option.MAX_RESULTS, a1);
        }
        
        public static BlobListOption pageToken(String a1) {
            /*SL:966*/return new BlobListOption(StorageRpc.Option.PAGE_TOKEN, a1);
        }
        
        public static BlobListOption prefix(String a1) {
            /*SL:974*/return new BlobListOption(StorageRpc.Option.PREFIX, a1);
        }
        
        public static BlobListOption currentDirectory() {
            /*SL:988*/return new BlobListOption(StorageRpc.Option.DELIMITER, true);
        }
        
        public static BlobListOption userProject(String a1) {
            /*SL:998*/return new BlobListOption(StorageRpc.Option.USER_PROJECT, a1);
        }
        
        public static BlobListOption versions(boolean a1) {
            /*SL:1007*/return new BlobListOption(StorageRpc.Option.VERSIONS, a1);
        }
        
        public static BlobListOption fields(BlobField... a1) {
            /*SL:1017*/return /*EL:1019*/new BlobListOption(StorageRpc.Option.FIELDS, FieldSelector.Helper.listSelector(BlobListOption.TOP_LEVEL_FIELDS, "items", (List)BlobField.REQUIRED_FIELDS, (FieldSelector[])a1, new String[0]));
        }
        
        static {
            TOP_LEVEL_FIELDS = new String[] { "prefixes" };
        }
    }
    
    public static class SignUrlOption implements Serializable
    {
        private static final long serialVersionUID = 7850569877451099267L;
        private final Option option;
        private final Object value;
        
        private SignUrlOption(Option a1, Object a2) {
            this.option = a1;
            this.value = a2;
        }
        
        Option getOption() {
            /*SL:1052*/return this.option;
        }
        
        Object getValue() {
            /*SL:1056*/return this.value;
        }
        
        public static SignUrlOption httpMethod(HttpMethod a1) {
            /*SL:1064*/return new SignUrlOption(Option.HTTP_METHOD, a1);
        }
        
        public static SignUrlOption withContentType() {
            /*SL:1073*/return new SignUrlOption(Option.CONTENT_TYPE, true);
        }
        
        public static SignUrlOption withMd5() {
            /*SL:1081*/return new SignUrlOption(Option.MD5, true);
        }
        
        public static SignUrlOption withExtHeaders(Map<String, String> a1) {
            /*SL:1092*/return new SignUrlOption(Option.EXT_HEADERS, a1);
        }
        
        public static SignUrlOption withV2Signature() {
            /*SL:1100*/return new SignUrlOption(Option.SIGNATURE_VERSION, SignatureVersion.V2);
        }
        
        public static SignUrlOption withV4Signature() {
            /*SL:1109*/return new SignUrlOption(Option.SIGNATURE_VERSION, SignatureVersion.V4);
        }
        
        public static SignUrlOption signWith(ServiceAccountSigner a1) {
            /*SL:1120*/return new SignUrlOption(Option.SERVICE_ACCOUNT_CRED, a1);
        }
        
        public static SignUrlOption withHostName(String a1) {
            /*SL:1128*/return new SignUrlOption(Option.HOST_NAME, a1);
        }
        
        enum Option
        {
            HTTP_METHOD, 
            CONTENT_TYPE, 
            MD5, 
            EXT_HEADERS, 
            SERVICE_ACCOUNT_CRED, 
            SIGNATURE_VERSION, 
            HOST_NAME;
        }
        
        enum SignatureVersion
        {
            V2, 
            V4;
        }
    }
    
    public static class ComposeRequest implements Serializable
    {
        private static final long serialVersionUID = -7385681353748590911L;
        private final List<SourceBlob> sourceBlobs;
        private final BlobInfo target;
        private final List<BlobTargetOption> targetOptions;
        
        private ComposeRequest(Builder a1) {
            this.sourceBlobs = (List<SourceBlob>)ImmutableList.<Object>copyOf((Collection<?>)a1.sourceBlobs);
            this.target = a1.target;
            this.targetOptions = (List<BlobTargetOption>)ImmutableList.<Object>copyOf((Collection<?>)a1.targetOptions);
        }
        
        public List<SourceBlob> getSourceBlobs() {
            /*SL:1231*/return this.sourceBlobs;
        }
        
        public BlobInfo getTarget() {
            /*SL:1236*/return this.target;
        }
        
        public List<BlobTargetOption> getTargetOptions() {
            /*SL:1241*/return this.targetOptions;
        }
        
        public static ComposeRequest of(Iterable<String> a1, BlobInfo a2) {
            /*SL:1251*/return newBuilder().setTarget(a2).addSource(a1).build();
        }
        
        public static ComposeRequest of(String a1, Iterable<String> a2, String a3) {
            /*SL:1262*/return of(a2, BlobInfo.newBuilder(BlobId.of(a1, a3)).build());
        }
        
        public static Builder newBuilder() {
            /*SL:1267*/return new Builder();
        }
        
        public static class SourceBlob implements Serializable
        {
            private static final long serialVersionUID = 4094962795951990439L;
            final String name;
            final Long generation;
            
            SourceBlob(String a1) {
                this(a1, null);
            }
            
            SourceBlob(String a1, Long a2) {
                this.name = a1;
                this.generation = a2;
            }
            
            public String getName() {
                /*SL:1164*/return this.name;
            }
            
            public Long getGeneration() {
                /*SL:1168*/return this.generation;
            }
        }
        
        public static class Builder
        {
            private final List<SourceBlob> sourceBlobs;
            private final Set<BlobTargetOption> targetOptions;
            private BlobInfo target;
            
            public Builder() {
                this.sourceBlobs = new LinkedList<SourceBlob>();
                this.targetOptions = new LinkedHashSet<BlobTargetOption>();
            }
            
            public Builder addSource(Iterable<String> v2) {
                Iterator<String> iterator;
                String a1;
                /*SL:1180*/for (final String a1 : v2) {
                    /*SL:1181*/this.sourceBlobs.add(new SourceBlob(a1));
                }
                /*SL:1183*/return this;
            }
            
            public Builder addSource(String... a1) {
                /*SL:1188*/return this.addSource(Arrays.<String>asList(a1));
            }
            
            public Builder addSource(String a1, long a2) {
                /*SL:1193*/this.sourceBlobs.add(new SourceBlob(a1, a2));
                /*SL:1194*/return this;
            }
            
            public Builder setTarget(BlobInfo a1) {
                /*SL:1199*/this.target = a1;
                /*SL:1200*/return this;
            }
            
            public Builder setTargetOptions(BlobTargetOption... a1) {
                /*SL:1205*/Collections.<BlobTargetOption>addAll(this.targetOptions, a1);
                /*SL:1206*/return this;
            }
            
            public Builder setTargetOptions(Iterable<BlobTargetOption> a1) {
                /*SL:1211*/Iterables.<BlobTargetOption>addAll(this.targetOptions, a1);
                /*SL:1212*/return this;
            }
            
            public ComposeRequest build() {
                /*SL:1217*/Preconditions.checkArgument(!this.sourceBlobs.isEmpty());
                /*SL:1218*/Preconditions.<BlobInfo>checkNotNull(this.target);
                /*SL:1219*/return new ComposeRequest(this);
            }
        }
    }
    
    public static class CopyRequest implements Serializable
    {
        private static final long serialVersionUID = -4498650529476219937L;
        private final BlobId source;
        private final List<BlobSourceOption> sourceOptions;
        private final boolean overrideInfo;
        private final BlobInfo target;
        private final List<BlobTargetOption> targetOptions;
        private final Long megabytesCopiedPerChunk;
        
        private CopyRequest(Builder a1) {
            this.source = Preconditions.<BlobId>checkNotNull(a1.source);
            this.sourceOptions = (List<BlobSourceOption>)ImmutableList.<Object>copyOf((Collection<?>)a1.sourceOptions);
            this.overrideInfo = a1.overrideInfo;
            this.target = Preconditions.<BlobInfo>checkNotNull(a1.target);
            this.targetOptions = (List<BlobTargetOption>)ImmutableList.<Object>copyOf((Collection<?>)a1.targetOptions);
            this.megabytesCopiedPerChunk = a1.megabytesCopiedPerChunk;
        }
        
        public BlobId getSource() {
            /*SL:1428*/return this.source;
        }
        
        public List<BlobSourceOption> getSourceOptions() {
            /*SL:1433*/return this.sourceOptions;
        }
        
        public BlobInfo getTarget() {
            /*SL:1438*/return this.target;
        }
        
        public boolean overrideInfo() {
            /*SL:1449*/return this.overrideInfo;
        }
        
        public List<BlobTargetOption> getTargetOptions() {
            /*SL:1454*/return this.targetOptions;
        }
        
        public Long getMegabytesCopiedPerChunk() {
            /*SL:1463*/return this.megabytesCopiedPerChunk;
        }
        
        public static CopyRequest of(String a1, String a2, BlobInfo a3) {
            /*SL:1476*/return newBuilder().setSource(a1, a2).setTarget(a3, new BlobTargetOption[0]).build();
        }
        
        public static CopyRequest of(BlobId a1, BlobInfo a2) {
            /*SL:1489*/return newBuilder().setSource(a1).setTarget(a2, new BlobTargetOption[0]).build();
        }
        
        public static CopyRequest of(String a1, String a2, String a3) {
            /*SL:1501*/return newBuilder().setSource(a1, a2).setTarget(/*EL:1503*/BlobId.of(a1, a3)).build();
        }
        
        public static CopyRequest of(String a1, String a2, BlobId a3) {
            /*SL:1516*/return newBuilder().setSource(a1, a2).setTarget(a3).build();
        }
        
        public static CopyRequest of(BlobId a1, String a2) {
            /*SL:1527*/return newBuilder().setSource(a1).setTarget(/*EL:1529*/BlobId.of(a1.getBucket(), a2)).build();
        }
        
        public static CopyRequest of(BlobId a1, BlobId a2) {
            /*SL:1541*/return newBuilder().setSource(a1).setTarget(a2).build();
        }
        
        public static Builder newBuilder() {
            /*SL:1546*/return new Builder();
        }
        
        public static class Builder
        {
            private final Set<BlobSourceOption> sourceOptions;
            private final Set<BlobTargetOption> targetOptions;
            private BlobId source;
            private boolean overrideInfo;
            private BlobInfo target;
            private Long megabytesCopiedPerChunk;
            
            public Builder() {
                this.sourceOptions = new LinkedHashSet<BlobSourceOption>();
                this.targetOptions = new LinkedHashSet<BlobTargetOption>();
            }
            
            public Builder setSource(String a1, String a2) {
                /*SL:1298*/this.source = BlobId.of(a1, a2);
                /*SL:1299*/return this;
            }
            
            public Builder setSource(BlobId a1) {
                /*SL:1308*/this.source = a1;
                /*SL:1309*/return this;
            }
            
            public Builder setSourceOptions(BlobSourceOption... a1) {
                /*SL:1318*/Collections.<BlobSourceOption>addAll(this.sourceOptions, a1);
                /*SL:1319*/return this;
            }
            
            public Builder setSourceOptions(Iterable<BlobSourceOption> a1) {
                /*SL:1328*/Iterables.<BlobSourceOption>addAll(this.sourceOptions, a1);
                /*SL:1329*/return this;
            }
            
            public Builder setTarget(BlobId a1) {
                /*SL:1338*/this.overrideInfo = false;
                /*SL:1339*/this.target = BlobInfo.newBuilder(a1).build();
                /*SL:1340*/return this;
            }
            
            public Builder setTarget(BlobId a1, BlobTargetOption... a2) {
                /*SL:1350*/this.overrideInfo = false;
                /*SL:1351*/this.target = BlobInfo.newBuilder(a1).build();
                /*SL:1352*/Collections.<BlobTargetOption>addAll(this.targetOptions, a2);
                /*SL:1353*/return this;
            }
            
            public Builder setTarget(BlobInfo a1, BlobTargetOption... a2) {
                /*SL:1365*/this.overrideInfo = true;
                /*SL:1366*/this.target = Preconditions.<BlobInfo>checkNotNull(a1);
                /*SL:1367*/Collections.<BlobTargetOption>addAll(this.targetOptions, a2);
                /*SL:1368*/return this;
            }
            
            public Builder setTarget(BlobInfo a1, Iterable<BlobTargetOption> a2) {
                /*SL:1380*/this.overrideInfo = true;
                /*SL:1381*/this.target = Preconditions.<BlobInfo>checkNotNull(a1);
                /*SL:1382*/Iterables.<BlobTargetOption>addAll(this.targetOptions, a2);
                /*SL:1383*/return this;
            }
            
            public Builder setTarget(BlobId a1, Iterable<BlobTargetOption> a2) {
                /*SL:1393*/this.overrideInfo = false;
                /*SL:1394*/this.target = BlobInfo.newBuilder(a1).build();
                /*SL:1395*/Iterables.<BlobTargetOption>addAll(this.targetOptions, a2);
                /*SL:1396*/return this;
            }
            
            public Builder setMegabytesCopiedPerChunk(Long a1) {
                /*SL:1407*/this.megabytesCopiedPerChunk = a1;
                /*SL:1408*/return this;
            }
            
            public CopyRequest build() {
                /*SL:1413*/return new CopyRequest(this);
            }
        }
    }
}

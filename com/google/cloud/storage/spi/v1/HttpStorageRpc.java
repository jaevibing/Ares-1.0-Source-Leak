package com.google.cloud.storage.spi.v1;

import io.opencensus.trace.AttributeValue;
import com.google.api.client.googleapis.batch.BatchRequest;
import java.util.LinkedList;
import com.google.api.services.storage.model.ServiceAccount;
import com.google.api.services.storage.model.Notifications;
import com.google.api.services.storage.model.Notification;
import com.google.api.services.storage.model.TestIamPermissionsResponse;
import com.google.api.services.storage.model.Policy;
import com.google.api.services.storage.model.HmacKeysMetadata;
import com.google.api.services.storage.model.HmacKeyMetadata;
import com.google.api.services.storage.model.HmacKey;
import com.google.api.services.storage.model.ObjectAccessControls;
import com.google.api.services.storage.model.ObjectAccessControl;
import com.google.api.services.storage.model.BucketAccessControls;
import com.google.api.services.storage.model.BucketAccessControl;
import com.google.api.services.storage.model.RewriteResponse;
import com.google.api.client.http.ByteArrayContent;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpContent;
import com.google.api.client.http.json.JsonHttpContent;
import com.google.api.client.http.GenericUrl;
import com.google.common.base.Preconditions;
import java.io.OutputStream;
import java.io.ByteArrayOutputStream;
import java.util.Iterator;
import java.util.ArrayList;
import com.google.api.services.storage.model.ComposeRequest;
import java.math.BigInteger;
import com.google.common.collect.Iterables;
import com.google.common.base.Function;
import java.util.List;
import com.google.common.collect.Lists;
import com.google.common.base.MoreObjects;
import com.google.common.collect.ImmutableList;
import com.google.api.services.storage.model.Objects;
import com.google.api.services.storage.model.Buckets;
import com.google.cloud.Tuple;
import com.google.api.client.http.AbstractInputStreamContent;
import com.google.api.client.http.InputStreamContent;
import java.io.InputStream;
import com.google.api.services.storage.model.StorageObject;
import io.opencensus.common.Scope;
import io.opencensus.trace.Status;
import com.google.api.services.storage.model.Bucket;
import io.opencensus.trace.Span;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import com.google.common.io.BaseEncoding;
import java.util.Map;
import com.google.cloud.storage.StorageException;
import com.google.api.client.googleapis.json.GoogleJsonError;
import java.io.IOException;
import com.google.api.client.http.HttpHeaders;
import com.google.api.client.googleapis.batch.json.JsonBatchCallback;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.cloud.ServiceOptions;
import com.google.cloud.http.HttpTransportOptions;
import io.opencensus.trace.Tracing;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.cloud.http.CensusHttpModule;
import io.opencensus.trace.Tracer;
import com.google.api.services.storage.Storage;
import com.google.cloud.storage.StorageOptions;

public class HttpStorageRpc implements StorageRpc
{
    public static final String DEFAULT_PROJECTION = "full";
    public static final String NO_ACL_PROJECTION = "noAcl";
    private static final String ENCRYPTION_KEY_PREFIX = "x-goog-encryption-";
    private static final String SOURCE_ENCRYPTION_KEY_PREFIX = "x-goog-copy-source-encryption-";
    private static final int SC_REQUESTED_RANGE_NOT_SATISFIABLE = 416;
    private final StorageOptions options;
    private final Storage storage;
    private final Tracer tracer;
    private final CensusHttpModule censusHttpModule;
    private final HttpRequestInitializer batchRequestInitializer;
    private static final long MEGABYTE = 1048576L;
    
    public HttpStorageRpc(final StorageOptions a1) {
        this.tracer = Tracing.getTracer();
        final HttpTransportOptions v1 = (HttpTransportOptions)a1.getTransportOptions();
        final HttpTransport v2 = v1.getHttpTransportFactory().create();
        HttpRequestInitializer v3 = v1.getHttpRequestInitializer((ServiceOptions)a1);
        this.options = a1;
        this.censusHttpModule = new CensusHttpModule(this.tracer, true);
        v3 = this.censusHttpModule.getHttpRequestInitializer(v3);
        this.batchRequestInitializer = this.censusHttpModule.getHttpRequestInitializer((HttpRequestInitializer)null);
        HttpStorageRpcSpans.registerAllSpanNamesForCollection();
        this.storage = new Storage.Builder(v2, (JsonFactory)new JacksonFactory(), v3).setRootUrl(a1.getHost()).setApplicationName(a1.getApplicationName()).build();
    }
    
    private static <T> JsonBatchCallback<T> toJsonCallback(final RpcBatch.Callback<T> a1) {
        /*SL:214*/return new JsonBatchCallback<T>() {
            @Override
            public void onSuccess(final T a1, final HttpHeaders a2) throws IOException {
                /*SL:217*/a1.onSuccess(a1);
            }
            
            @Override
            public void onFailure(final GoogleJsonError a1, final HttpHeaders a2) throws IOException {
                /*SL:223*/a1.onFailure(a1);
            }
        };
    }
    
    private static StorageException translate(final IOException a1) {
        /*SL:229*/return new StorageException(a1);
    }
    
    private static StorageException translate(final GoogleJsonError a1) {
        /*SL:233*/return new StorageException(a1);
    }
    
    private static void setEncryptionHeaders(final HttpHeaders a3, final String v1, final Map<Option, ?> v2) {
        final String v3 = Option.CUSTOMER_SUPPLIED_KEY.getString(/*EL:238*/v2);
        /*SL:239*/if (v3 != null) {
            final BaseEncoding a4 = /*EL:240*/BaseEncoding.base64();
            final HashFunction a5 = /*EL:241*/Hashing.sha256();
            /*SL:242*/a3.set(v1 + "algorithm", "AES256");
            /*SL:243*/a3.set(v1 + "key", v3);
            /*SL:244*/a3.set(v1 + "key-sha256", a4.encode(a5.hashBytes(a4.decode(v3)).asBytes()));
        }
    }
    
    private Span startSpan(final String a1) {
        /*SL:252*/return this.tracer.spanBuilder(a1).setRecordEvents(/*EL:253*/this.censusHttpModule.isRecordEvents()).startSpan();
    }
    
    @Override
    public Bucket create(final Bucket v1, final Map<Option, ?> v2) {
        final Span v3 = /*EL:260*/this.startSpan(HttpStorageRpcSpans.SPAN_NAME_CREATE_BUCKET);
        final Scope v4 = /*EL:261*/this.tracer.withSpan(v3);
        try {
            /*SL:263*/return (Bucket)this.storage.buckets().insert(/*EL:264*/this.options.getProjectId(), /*EL:265*/v1).setProjection("full").setPredefinedAcl(Option.PREDEFINED_ACL.getString(/*EL:266*/v2)).setPredefinedDefaultObjectAcl(Option.PREDEFINED_DEFAULT_OBJECT_ACL.getString(/*EL:267*/v2)).execute();
        }
        catch (IOException a1) {
            /*SL:271*/v3.setStatus(Status.UNKNOWN.withDescription(a1.getMessage()));
            throw translate(/*EL:272*/a1);
        }
        finally {
            /*SL:274*/v4.close();
            /*SL:275*/v3.end();
        }
    }
    
    @Override
    public StorageObject create(final StorageObject v2, final InputStream v3, final Map<Option, ?> v4) {
        final Span v5 = /*EL:282*/this.startSpan(HttpStorageRpcSpans.SPAN_NAME_CREATE_OBJECT);
        final Scope v6 = /*EL:283*/this.tracer.withSpan(v5);
        try {
            final Storage.Objects.Insert a1 = /*EL:285*/this.storage.objects().insert(/*EL:287*/v2.getBucket(), /*EL:288*/v2, /*EL:289*/(AbstractInputStreamContent)new InputStreamContent(v2.getContentType(), /*EL:291*/v3));
            /*SL:292*/a1.getMediaHttpUploader().setDirectUploadEnabled(true);
            final Boolean a2 = Option.IF_DISABLE_GZIP_CONTENT.getBoolean(/*EL:293*/v4);
            /*SL:294*/if (a2 != null) {
                /*SL:295*/a1.setDisableGZipContent((boolean)a2);
            }
            setEncryptionHeaders(/*EL:297*/a1.getRequestHeaders(), "x-goog-encryption-", v4);
            /*SL:298*/return (StorageObject)a1.setProjection("full").setPredefinedAcl(Option.PREDEFINED_ACL.getString(/*EL:299*/v4)).setIfMetagenerationMatch(Option.IF_METAGENERATION_MATCH.getLong(/*EL:300*/v4)).setIfMetagenerationNotMatch(Option.IF_METAGENERATION_NOT_MATCH.getLong(/*EL:301*/v4)).setIfGenerationMatch(Option.IF_GENERATION_MATCH.getLong(/*EL:302*/v4)).setIfGenerationNotMatch(Option.IF_GENERATION_NOT_MATCH.getLong(/*EL:303*/v4)).setUserProject(Option.USER_PROJECT.getString(/*EL:304*/v4)).setKmsKeyName(Option.KMS_KEY_NAME.getString(/*EL:305*/v4)).execute();
        }
        catch (IOException a3) {
            /*SL:309*/v5.setStatus(Status.UNKNOWN.withDescription(a3.getMessage()));
            throw translate(/*EL:310*/a3);
        }
        finally {
            /*SL:312*/v6.close();
            /*SL:313*/v5.end();
        }
    }
    
    @Override
    public Tuple<String, Iterable<Bucket>> list(final Map<Option, ?> v-2) {
        final Span startSpan = /*EL:319*/this.startSpan(HttpStorageRpcSpans.SPAN_NAME_LIST_BUCKETS);
        final Scope v0 = /*EL:320*/this.tracer.withSpan(startSpan);
        try {
            final Buckets a1 = /*EL:322*/(Buckets)this.storage.buckets().list(/*EL:324*/this.options.getProjectId()).setProjection(/*EL:325*/"full").setPrefix(Option.PREFIX.getString(/*EL:326*/v-2)).setMaxResults(Option.MAX_RESULTS.getLong(/*EL:327*/v-2)).setPageToken(Option.PAGE_TOKEN.getString(/*EL:328*/v-2)).setFields(Option.FIELDS.getString(/*EL:329*/v-2)).setUserProject(Option.USER_PROJECT.getString(/*EL:330*/v-2)).execute();
            /*SL:333*/return (Tuple<String, Iterable<Bucket>>)Tuple.of((Object)a1.getNextPageToken(), (Object)a1.getItems());
        }
        catch (IOException v) {
            /*SL:335*/startSpan.setStatus(Status.UNKNOWN.withDescription(v.getMessage()));
            throw translate(/*EL:336*/v);
        }
        finally {
            /*SL:338*/v0.close();
            /*SL:339*/startSpan.end();
        }
    }
    
    @Override
    public Tuple<String, Iterable<StorageObject>> list(final String v-3, final Map<Option, ?> v-2) {
        final Span startSpan = /*EL:345*/this.startSpan(HttpStorageRpcSpans.SPAN_NAME_LIST_OBJECTS);
        final Scope v0 = /*EL:346*/this.tracer.withSpan(startSpan);
        try {
            final Objects a1 = /*EL:348*/(Objects)this.storage.objects().list(/*EL:350*/v-3).setProjection(/*EL:351*/"full").setVersions(Option.VERSIONS.getBoolean(/*EL:352*/v-2)).setDelimiter(Option.DELIMITER.getString(/*EL:353*/v-2)).setPrefix(Option.PREFIX.getString(/*EL:354*/v-2)).setMaxResults(Option.MAX_RESULTS.getLong(/*EL:355*/v-2)).setPageToken(Option.PAGE_TOKEN.getString(/*EL:356*/v-2)).setFields(Option.FIELDS.getString(/*EL:357*/v-2)).setUserProject(Option.USER_PROJECT.getString(/*EL:358*/v-2)).execute();
            final Iterable<StorageObject> a2 = /*EL:362*/Iterables.<StorageObject>concat(/*EL:363*/(Iterable<? extends StorageObject>)MoreObjects.firstNonNull((Object)a1.getItems(), (Object)ImmutableList.<Object>of()), (Iterable<? extends StorageObject>)((a1.getPrefixes() != /*EL:364*/null) ? /*EL:365*/Lists.<Object, Object>transform((List<Object>)a1.getPrefixes(), (Function<? super Object, ?>)objectFromPrefix(v-3)) : /*EL:366*/ImmutableList.<Object>of()));
            /*SL:367*/return (Tuple<String, Iterable<StorageObject>>)Tuple.of((Object)a1.getNextPageToken(), (Object)a2);
        }
        catch (IOException v) {
            /*SL:369*/startSpan.setStatus(Status.UNKNOWN.withDescription(v.getMessage()));
            throw translate(/*EL:370*/v);
        }
        finally {
            /*SL:372*/v0.close();
            /*SL:373*/startSpan.end();
        }
    }
    
    private static Function<String, StorageObject> objectFromPrefix(final String a1) {
        /*SL:378*/return new Function<String, StorageObject>() {
            @Override
            public StorageObject apply(final String a1) {
                /*SL:381*/return new StorageObject().set("isDirectory", /*EL:382*/(Object)true).setBucket(a1).setName(/*EL:383*/a1).setSize(BigInteger.ZERO);
            }
        };
    }
    
    @Override
    public Bucket get(final Bucket v2, final Map<Option, ?> v3) {
        final Span v4 = /*EL:392*/this.startSpan(HttpStorageRpcSpans.SPAN_NAME_GET_BUCKET);
        final Scope v5 = /*EL:393*/this.tracer.withSpan(v4);
        try {
            /*SL:395*/return (Bucket)this.storage.buckets().get(/*EL:396*/v2.getName()).setProjection(/*EL:397*/"full").setIfMetagenerationMatch(Option.IF_METAGENERATION_MATCH.getLong(/*EL:398*/v3)).setIfMetagenerationNotMatch(Option.IF_METAGENERATION_NOT_MATCH.getLong(/*EL:399*/v3)).setFields(Option.FIELDS.getString(/*EL:400*/v3)).setUserProject(Option.USER_PROJECT.getString(/*EL:401*/v3)).execute();
        }
        catch (IOException a2) {
            /*SL:405*/v4.setStatus(Status.UNKNOWN.withDescription(a2.getMessage()));
            /*SL:406*/a2 = translate(a2);
            /*SL:407*/if (a2.getCode() == 404) {
                /*SL:408*/return null;
            }
            /*SL:410*/throw a2;
        }
        finally {
            /*SL:412*/v5.close();
            /*SL:413*/v4.end();
        }
    }
    
    private Storage.Objects.Get getCall(final StorageObject a1, final Map<Option, ?> a2) throws IOException {
        final Storage.Objects.Get v1 = /*EL:419*/this.storage.objects().get(a1.getBucket(), a1.getName());
        setEncryptionHeaders(/*EL:420*/v1.getRequestHeaders(), "x-goog-encryption-", a2);
        /*SL:421*/return v1.setGeneration(a1.getGeneration()).setProjection("full").setIfMetagenerationMatch(Option.IF_METAGENERATION_MATCH.getLong(/*EL:422*/a2)).setIfMetagenerationNotMatch(Option.IF_METAGENERATION_NOT_MATCH.getLong(/*EL:423*/a2)).setIfGenerationMatch(Option.IF_GENERATION_MATCH.getLong(/*EL:424*/a2)).setIfGenerationNotMatch(Option.IF_GENERATION_NOT_MATCH.getLong(/*EL:425*/a2)).setFields(Option.FIELDS.getString(/*EL:426*/a2)).setUserProject(Option.USER_PROJECT.getString(/*EL:427*/a2));
    }
    
    @Override
    public StorageObject get(final StorageObject v2, final Map<Option, ?> v3) {
        final Span v4 = /*EL:433*/this.startSpan(HttpStorageRpcSpans.SPAN_NAME_GET_OBJECT);
        final Scope v5 = /*EL:434*/this.tracer.withSpan(v4);
        try {
            /*SL:436*/return (StorageObject)this.getCall(v2, v3).execute();
        }
        catch (IOException a2) {
            /*SL:438*/v4.setStatus(Status.UNKNOWN.withDescription(a2.getMessage()));
            /*SL:439*/a2 = translate(a2);
            /*SL:440*/if (a2.getCode() == 404) {
                /*SL:441*/return null;
            }
            /*SL:443*/throw a2;
        }
        finally {
            /*SL:445*/v5.close();
            /*SL:446*/v4.end();
        }
    }
    
    @Override
    public Bucket patch(final Bucket v2, final Map<Option, ?> v3) {
        final Span v4 = /*EL:452*/this.startSpan(HttpStorageRpcSpans.SPAN_NAME_PATCH_BUCKET);
        final Scope v5 = /*EL:453*/this.tracer.withSpan(v4);
        try {
            String a1 = Option.PROJECTION.getString(/*EL:455*/v3);
            /*SL:458*/if (v2.getIamConfiguration() != null && v2.getIamConfiguration().getBucketPolicyOnly() != null && v2.getIamConfiguration().getBucketPolicyOnly().getEnabled() != null && v2.getIamConfiguration().getBucketPolicyOnly().getEnabled()) {
                /*SL:462*/v2.setDefaultObjectAcl((List)null);
                /*SL:463*/v2.setAcl((List)null);
                /*SL:465*/if (a1 == null) {
                    /*SL:466*/a1 = "noAcl";
                }
            }
            /*SL:469*/return (Bucket)this.storage.buckets().patch(/*EL:470*/v2.getName(), /*EL:471*/v2).setProjection((a1 == null) ? "full" : a1).setPredefinedAcl(Option.PREDEFINED_ACL.getString(/*EL:472*/v3)).setPredefinedDefaultObjectAcl(Option.PREDEFINED_DEFAULT_OBJECT_ACL.getString(/*EL:473*/v3)).setIfMetagenerationMatch(Option.IF_METAGENERATION_MATCH.getLong(/*EL:474*/v3)).setIfMetagenerationNotMatch(Option.IF_METAGENERATION_NOT_MATCH.getLong(/*EL:475*/v3)).setUserProject(Option.USER_PROJECT.getString(/*EL:476*/v3)).execute();
        }
        catch (IOException a2) {
            /*SL:480*/v4.setStatus(Status.UNKNOWN.withDescription(a2.getMessage()));
            throw translate(/*EL:481*/a2);
        }
        finally {
            /*SL:483*/v5.close();
            /*SL:484*/v4.end();
        }
    }
    
    private Storage.Objects.Patch patchCall(final StorageObject a1, final Map<Option, ?> a2) throws IOException {
        /*SL:490*/return this.storage.objects().patch(/*EL:491*/a1.getBucket(), /*EL:492*/a1.getName(), a1).setProjection("full").setPredefinedAcl(Option.PREDEFINED_ACL.getString(/*EL:493*/a2)).setIfMetagenerationMatch(Option.IF_METAGENERATION_MATCH.getLong(/*EL:494*/a2)).setIfMetagenerationNotMatch(Option.IF_METAGENERATION_NOT_MATCH.getLong(/*EL:495*/a2)).setIfGenerationMatch(Option.IF_GENERATION_MATCH.getLong(/*EL:496*/a2)).setIfGenerationNotMatch(Option.IF_GENERATION_NOT_MATCH.getLong(/*EL:497*/a2)).setUserProject(Option.USER_PROJECT.getString(/*EL:498*/a2));
    }
    
    @Override
    public StorageObject patch(final StorageObject v1, final Map<Option, ?> v2) {
        final Span v3 = /*EL:504*/this.startSpan(HttpStorageRpcSpans.SPAN_NAME_PATCH_OBJECT);
        final Scope v4 = /*EL:505*/this.tracer.withSpan(v3);
        try {
            /*SL:507*/return (StorageObject)this.patchCall(v1, v2).execute();
        }
        catch (IOException a1) {
            /*SL:509*/v3.setStatus(Status.UNKNOWN.withDescription(a1.getMessage()));
            throw translate(/*EL:510*/a1);
        }
        finally {
            /*SL:512*/v4.close();
            /*SL:513*/v3.end();
        }
    }
    
    @Override
    public boolean delete(final Bucket v2, final Map<Option, ?> v3) {
        final Span v4 = /*EL:519*/this.startSpan(HttpStorageRpcSpans.SPAN_NAME_DELETE_BUCKET);
        final Scope v5 = /*EL:520*/this.tracer.withSpan(v4);
        try {
            /*SL:522*/this.storage.buckets().delete(/*EL:523*/v2.getName()).setIfMetagenerationMatch(Option.IF_METAGENERATION_MATCH.getLong(/*EL:524*/v3)).setIfMetagenerationNotMatch(Option.IF_METAGENERATION_NOT_MATCH.getLong(/*EL:525*/v3)).setUserProject(Option.USER_PROJECT.getString(/*EL:526*/v3)).execute();
            /*SL:529*/return true;
        }
        catch (IOException a2) {
            /*SL:531*/v4.setStatus(Status.UNKNOWN.withDescription(a2.getMessage()));
            /*SL:532*/a2 = translate(a2);
            /*SL:533*/if (a2.getCode() == 404) {
                /*SL:534*/return false;
            }
            /*SL:536*/throw a2;
        }
        finally {
            /*SL:538*/v5.close();
            /*SL:539*/v4.end();
        }
    }
    
    private Storage.Objects.Delete deleteCall(final StorageObject a1, final Map<Option, ?> a2) throws IOException {
        /*SL:545*/return this.storage.objects().delete(/*EL:546*/a1.getBucket(), /*EL:547*/a1.getName()).setGeneration(a1.getGeneration()).setIfMetagenerationMatch(Option.IF_METAGENERATION_MATCH.getLong(/*EL:548*/a2)).setIfMetagenerationNotMatch(Option.IF_METAGENERATION_NOT_MATCH.getLong(/*EL:549*/a2)).setIfGenerationMatch(Option.IF_GENERATION_MATCH.getLong(/*EL:550*/a2)).setIfGenerationNotMatch(Option.IF_GENERATION_NOT_MATCH.getLong(/*EL:551*/a2)).setUserProject(Option.USER_PROJECT.getString(/*EL:552*/a2));
    }
    
    @Override
    public boolean delete(final StorageObject v2, final Map<Option, ?> v3) {
        final Span v4 = /*EL:558*/this.startSpan(HttpStorageRpcSpans.SPAN_NAME_DELETE_OBJECT);
        final Scope v5 = /*EL:559*/this.tracer.withSpan(v4);
        try {
            /*SL:561*/this.deleteCall(v2, v3).execute();
            /*SL:562*/return true;
        }
        catch (IOException a2) {
            /*SL:564*/v4.setStatus(Status.UNKNOWN.withDescription(a2.getMessage()));
            /*SL:565*/a2 = translate(a2);
            /*SL:566*/if (a2.getCode() == 404) {
                /*SL:567*/return false;
            }
            /*SL:569*/throw a2;
        }
        finally {
            /*SL:571*/v5.close();
            /*SL:572*/v4.end();
        }
    }
    
    @Override
    public StorageObject compose(final Iterable<StorageObject> v-6, final StorageObject v-5, final Map<Option, ?> v-4) {
        final ComposeRequest composeRequest = /*EL:579*/new ComposeRequest();
        /*SL:580*/composeRequest.setDestination(v-5);
        final List<ComposeRequest.SourceObjects> sourceObjects = /*EL:581*/new ArrayList<ComposeRequest.SourceObjects>();
        /*SL:582*/for (Long a3 : v-6) {
            final ComposeRequest.SourceObjects a2 = /*EL:583*/new ComposeRequest.SourceObjects();
            /*SL:584*/a2.setName(a3.getName());
            /*SL:585*/a3 = a3.getGeneration();
            /*SL:586*/if (a3 != null) {
                /*SL:587*/a2.setGeneration(a3);
                /*SL:588*/a2.setObjectPreconditions(new ComposeRequest.SourceObjects.ObjectPreconditions().setIfGenerationMatch(a3));
            }
            /*SL:591*/sourceObjects.add(a2);
        }
        /*SL:593*/composeRequest.setSourceObjects((List)sourceObjects);
        final Span startSpan = /*EL:594*/this.startSpan(HttpStorageRpcSpans.SPAN_NAME_COMPOSE);
        final Scope v0 = /*EL:595*/this.tracer.withSpan(startSpan);
        try {
            /*SL:597*/return (StorageObject)this.storage.objects().compose(/*EL:598*/v-5.getBucket(), /*EL:599*/v-5.getName(), composeRequest).setIfMetagenerationMatch(Option.IF_METAGENERATION_MATCH.getLong(v-4)).setIfGenerationMatch(Option.IF_GENERATION_MATCH.getLong(/*EL:600*/v-4)).setUserProject(Option.USER_PROJECT.getString(/*EL:601*/v-4)).execute();
        }
        catch (IOException v) {
            /*SL:605*/startSpan.setStatus(Status.UNKNOWN.withDescription(v.getMessage()));
            throw translate(/*EL:606*/v);
        }
        finally {
            /*SL:608*/v0.close();
            /*SL:609*/startSpan.end();
        }
    }
    
    @Override
    public byte[] load(final StorageObject v-3, final Map<Option, ?> v-2) {
        final Span startSpan = /*EL:615*/this.startSpan(HttpStorageRpcSpans.SPAN_NAME_LOAD);
        final Scope v0 = /*EL:616*/this.tracer.withSpan(startSpan);
        try {
            final Storage.Objects.Get a1 = /*EL:618*/this.storage.objects().get(/*EL:620*/v-3.getBucket(), /*EL:621*/v-3.getName()).setGeneration(v-3.getGeneration()).setIfMetagenerationMatch(Option.IF_METAGENERATION_MATCH.getLong(/*EL:622*/v-2)).setIfMetagenerationNotMatch(Option.IF_METAGENERATION_NOT_MATCH.getLong(/*EL:623*/v-2)).setIfGenerationMatch(Option.IF_GENERATION_MATCH.getLong(/*EL:624*/v-2)).setIfGenerationNotMatch(Option.IF_GENERATION_NOT_MATCH.getLong(/*EL:625*/v-2)).setUserProject(Option.USER_PROJECT.getString(/*EL:626*/v-2));
            setEncryptionHeaders(/*EL:628*/a1.getRequestHeaders(), "x-goog-encryption-", v-2);
            final ByteArrayOutputStream a2 = /*EL:629*/new ByteArrayOutputStream();
            /*SL:630*/a1.executeMedia().download(a2);
            /*SL:631*/return a2.toByteArray();
        }
        catch (IOException v) {
            /*SL:633*/startSpan.setStatus(Status.UNKNOWN.withDescription(v.getMessage()));
            throw translate(/*EL:634*/v);
        }
        finally {
            /*SL:636*/v0.close();
            /*SL:637*/startSpan.end();
        }
    }
    
    @Override
    public RpcBatch createBatch() {
        /*SL:643*/return new DefaultRpcBatch(this.storage);
    }
    
    private Storage.Objects.Get createReadRequest(final StorageObject a1, final Map<Option, ?> a2) throws IOException {
        final Storage.Objects.Get v1 = /*EL:647*/this.storage.objects().get(/*EL:649*/a1.getBucket(), /*EL:650*/a1.getName()).setGeneration(a1.getGeneration()).setIfMetagenerationMatch(Option.IF_METAGENERATION_MATCH.getLong(/*EL:651*/a2)).setIfMetagenerationNotMatch(Option.IF_METAGENERATION_NOT_MATCH.getLong(/*EL:652*/a2)).setIfGenerationMatch(Option.IF_GENERATION_MATCH.getLong(/*EL:653*/a2)).setIfGenerationNotMatch(Option.IF_GENERATION_NOT_MATCH.getLong(/*EL:654*/a2)).setUserProject(Option.USER_PROJECT.getString(/*EL:655*/a2));
        setEncryptionHeaders(/*EL:657*/v1.getRequestHeaders(), "x-goog-encryption-", a2);
        /*SL:658*/v1.setReturnRawInputStream(true);
        /*SL:659*/return v1;
    }
    
    @Override
    public long read(final StorageObject v1, final Map<Option, ?> v2, final long v3, final OutputStream v5) {
        final Span v6 = /*EL:665*/this.startSpan(HttpStorageRpcSpans.SPAN_NAME_READ);
        final Scope v7 = /*EL:666*/this.tracer.withSpan(v6);
        try {
            final Storage.Objects.Get a1 = /*EL:668*/this.createReadRequest(v1, v2);
            /*SL:669*/a1.getMediaHttpDownloader().setBytesDownloaded(v3);
            /*SL:670*/a1.getMediaHttpDownloader().setDirectDownloadEnabled(true);
            /*SL:671*/a1.executeMediaAndDownloadTo(v5);
            /*SL:672*/return a1.getMediaHttpDownloader().getNumBytesDownloaded();
        }
        catch (IOException a2) {
            /*SL:674*/v6.setStatus(Status.UNKNOWN.withDescription(a2.getMessage()));
            final StorageException a3 = translate(/*EL:675*/a2);
            /*SL:676*/if (a3.getCode() == 416) {
                /*SL:677*/return 0L;
            }
            /*SL:679*/throw a3;
        }
        finally {
            /*SL:681*/v7.close();
            /*SL:682*/v6.end();
        }
    }
    
    @Override
    public Tuple<String, byte[]> read(final StorageObject v-10, final Map<Option, ?> v-9, final long v-8, final int v-6) {
        final Span startSpan = /*EL:689*/this.startSpan(HttpStorageRpcSpans.SPAN_NAME_READ);
        final Scope withSpan = /*EL:690*/this.tracer.withSpan(startSpan);
        try {
            /*SL:692*/Preconditions.checkArgument(v-8 >= 0L, "Position should be non-negative, is %d", v-8);
            final Storage.Objects.Get a1 = /*EL:693*/this.createReadRequest(v-10, v-9);
            final StringBuilder a2 = /*EL:694*/new StringBuilder();
            /*SL:695*/a2.append("bytes=").append(v-8).append("-").append(v-8 + v-6 - 1L);
            final HttpHeaders a3 = /*EL:696*/a1.getRequestHeaders();
            /*SL:697*/a3.setRange(a2.toString());
            final ByteArrayOutputStream a4 = /*EL:698*/new ByteArrayOutputStream(v-6);
            /*SL:699*/a1.executeMedia().download(a4);
            final String v1 = /*EL:700*/a1.getLastResponseHeaders().getETag();
            /*SL:701*/return (Tuple<String, byte[]>)Tuple.of((Object)v1, (Object)a4.toByteArray());
        }
        catch (IOException a5) {
            /*SL:703*/startSpan.setStatus(Status.UNKNOWN.withDescription(a5.getMessage()));
            final StorageException translate = translate(/*EL:704*/a5);
            /*SL:705*/if (translate.getCode() == 416) {
                /*SL:706*/return (Tuple<String, byte[]>)Tuple.of((Object)null, (Object)new byte[0]);
            }
            /*SL:708*/throw translate;
        }
        finally {
            /*SL:710*/withSpan.close();
            /*SL:711*/startSpan.end();
        }
    }
    
    @Override
    public void write(final String v-8, final byte[] v-7, final int v-6, final long v-5, final int v-3, final boolean v-2) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     0: aload_0         /* v-9 */
        //     1: getstatic       com/google/cloud/storage/spi/v1/HttpStorageRpcSpans.SPAN_NAME_WRITE:Ljava/lang/String;
        //     4: invokespecial   com/google/cloud/storage/spi/v1/HttpStorageRpc.startSpan:(Ljava/lang/String;)Lio/opencensus/trace/Span;
        //     7: astore          v-1
        //     9: aload_0         /* v-9 */
        //    10: getfield        com/google/cloud/storage/spi/v1/HttpStorageRpc.tracer:Lio/opencensus/trace/Tracer;
        //    13: aload           v-1
        //    15: invokevirtual   io/opencensus/trace/Tracer.withSpan:(Lio/opencensus/trace/Span;)Lio/opencensus/common/Scope;
        //    18: astore          v0
        //    20: iload           v-3
        //    22: ifne            43
        //    25: iload           v-2
        //    27: ifne            43
        //    30: aload           v0
        //    32: invokeinterface io/opencensus/common/Scope.close:()V
        //    37: aload           v-1
        //    39: invokevirtual   io/opencensus/trace/Span.end:()V
        //    42: return         
        //    43: new             Lcom/google/api/client/http/GenericUrl;
        //    46: dup            
        //    47: aload_1         /* v-8 */
        //    48: invokespecial   com/google/api/client/http/GenericUrl.<init>:(Ljava/lang/String;)V
        //    51: astore          v1
        //    53: aload_0         /* v-9 */
        //    54: getfield        com/google/cloud/storage/spi/v1/HttpStorageRpc.storage:Lcom/google/api/services/storage/Storage;
        //    57: invokevirtual   com/google/api/services/storage/Storage.getRequestFactory:()Lcom/google/api/client/http/HttpRequestFactory;
        //    60: aload           v1
        //    62: new             Lcom/google/api/client/http/ByteArrayContent;
        //    65: dup            
        //    66: aconst_null    
        //    67: aload_2         /* v-7 */
        //    68: iload_3         /* v-6 */
        //    69: iload           v-3
        //    71: invokespecial   com/google/api/client/http/ByteArrayContent.<init>:(Ljava/lang/String;[BII)V
        //    74: invokevirtual   com/google/api/client/http/HttpRequestFactory.buildPutRequest:(Lcom/google/api/client/http/GenericUrl;Lcom/google/api/client/http/HttpContent;)Lcom/google/api/client/http/HttpRequest;
        //    77: astore          v2
        //    79: lload           v-5
        //    81: iload           v-3
        //    83: i2l            
        //    84: ladd           
        //    85: lstore          v3
        //    87: new             Ljava/lang/StringBuilder;
        //    90: dup            
        //    91: ldc_w           "bytes "
        //    94: invokespecial   java/lang/StringBuilder.<init>:(Ljava/lang/String;)V
        //    97: astore          v5
        //    99: iload           v-3
        //   101: ifne            115
        //   104: aload           v5
        //   106: bipush          42
        //   108: invokevirtual   java/lang/StringBuilder.append:(C)Ljava/lang/StringBuilder;
        //   111: pop            
        //   112: goto            135
        //   115: aload           v5
        //   117: lload           v-5
        //   119: invokevirtual   java/lang/StringBuilder.append:(J)Ljava/lang/StringBuilder;
        //   122: bipush          45
        //   124: invokevirtual   java/lang/StringBuilder.append:(C)Ljava/lang/StringBuilder;
        //   127: lload           v3
        //   129: lconst_1       
        //   130: lsub           
        //   131: invokevirtual   java/lang/StringBuilder.append:(J)Ljava/lang/StringBuilder;
        //   134: pop            
        //   135: aload           v5
        //   137: bipush          47
        //   139: invokevirtual   java/lang/StringBuilder.append:(C)Ljava/lang/StringBuilder;
        //   142: pop            
        //   143: iload           v-2
        //   145: ifeq            159
        //   148: aload           v5
        //   150: lload           v3
        //   152: invokevirtual   java/lang/StringBuilder.append:(J)Ljava/lang/StringBuilder;
        //   155: pop            
        //   156: goto            167
        //   159: aload           v5
        //   161: bipush          42
        //   163: invokevirtual   java/lang/StringBuilder.append:(C)Ljava/lang/StringBuilder;
        //   166: pop            
        //   167: aload           v2
        //   169: invokevirtual   com/google/api/client/http/HttpRequest.getHeaders:()Lcom/google/api/client/http/HttpHeaders;
        //   172: aload           v5
        //   174: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   177: invokevirtual   com/google/api/client/http/HttpHeaders.setContentRange:(Ljava/lang/String;)Lcom/google/api/client/http/HttpHeaders;
        //   180: pop            
        //   181: aconst_null    
        //   182: astore          v8
        //   184: aconst_null    
        //   185: astore          v9
        //   187: aload           v2
        //   189: invokevirtual   com/google/api/client/http/HttpRequest.execute:()Lcom/google/api/client/http/HttpResponse;
        //   192: astore          v9
        //   194: aload           v9
        //   196: invokevirtual   com/google/api/client/http/HttpResponse.getStatusCode:()I
        //   199: istore          a1
        //   201: aload           v9
        //   203: invokevirtual   com/google/api/client/http/HttpResponse.getStatusMessage:()Ljava/lang/String;
        //   206: astore          a2
        //   208: aload           v9
        //   210: ifnull          269
        //   213: aload           v9
        //   215: invokevirtual   com/google/api/client/http/HttpResponse.disconnect:()V
        //   218: goto            269
        //   221: astore          a3
        //   223: aload           a3
        //   225: astore          v8
        //   227: aload           a3
        //   229: invokevirtual   com/google/api/client/http/HttpResponseException.getStatusCode:()I
        //   232: istore          a4
        //   234: aload           a3
        //   236: invokevirtual   com/google/api/client/http/HttpResponseException.getStatusMessage:()Ljava/lang/String;
        //   239: astore          a5
        //   241: aload           v9
        //   243: ifnull          269
        //   246: aload           v9
        //   248: invokevirtual   com/google/api/client/http/HttpResponse.disconnect:()V
        //   251: goto            269
        //   254: astore          20
        //   256: aload           v9
        //   258: ifnull          266
        //   261: aload           v9
        //   263: invokevirtual   com/google/api/client/http/HttpResponse.disconnect:()V
        //   266: aload           20
        //   268: athrow         
        //   269: iload           v-2
        //   271: ifne            282
        //   274: iload           v6
        //   276: sipush          308
        //   279: if_icmpne       303
        //   282: iload           v-2
        //   284: ifeq            340
        //   287: iload           v6
        //   289: sipush          200
        //   292: if_icmpeq       340
        //   295: iload           v6
        //   297: sipush          201
        //   300: if_icmpeq       340
        //   303: aload           v8
        //   305: ifnull          311
        //   308: aload           v8
        //   310: athrow         
        //   311: new             Lcom/google/api/client/googleapis/json/GoogleJsonError;
        //   314: dup            
        //   315: invokespecial   com/google/api/client/googleapis/json/GoogleJsonError.<init>:()V
        //   318: astore          a6
        //   320: aload           a6
        //   322: iload           v6
        //   324: invokevirtual   com/google/api/client/googleapis/json/GoogleJsonError.setCode:(I)V
        //   327: aload           a6
        //   329: aload           v7
        //   331: invokevirtual   com/google/api/client/googleapis/json/GoogleJsonError.setMessage:(Ljava/lang/String;)V
        //   334: aload           a6
        //   336: invokestatic    com/google/cloud/storage/spi/v1/HttpStorageRpc.translate:(Lcom/google/api/client/googleapis/json/GoogleJsonError;)Lcom/google/cloud/storage/StorageException;
        //   339: athrow         
        //   340: aload           v0
        //   342: invokeinterface io/opencensus/common/Scope.close:()V
        //   347: aload           v-1
        //   349: invokevirtual   io/opencensus/trace/Span.end:()V
        //   352: goto            396
        //   355: astore          v1
        //   357: aload           v-1
        //   359: getstatic       io/opencensus/trace/Status.UNKNOWN:Lio/opencensus/trace/Status;
        //   362: aload           v1
        //   364: invokevirtual   java/io/IOException.getMessage:()Ljava/lang/String;
        //   367: invokevirtual   io/opencensus/trace/Status.withDescription:(Ljava/lang/String;)Lio/opencensus/trace/Status;
        //   370: invokevirtual   io/opencensus/trace/Span.setStatus:(Lio/opencensus/trace/Status;)V
        //   373: aload           v1
        //   375: invokestatic    com/google/cloud/storage/spi/v1/HttpStorageRpc.translate:(Ljava/io/IOException;)Lcom/google/cloud/storage/StorageException;
        //   378: athrow         
        //   379: astore          21
        //   381: aload           v0
        //   383: invokeinterface io/opencensus/common/Scope.close:()V
        //   388: aload           v-1
        //   390: invokevirtual   io/opencensus/trace/Span.end:()V
        //   393: aload           21
        //   395: athrow         
        //   396: return         
        //    LocalVariableTable:
        //  Start  Length  Slot  Name  Signature
        //  -----  ------  ----  ----  -------------------------------------------------------
        //  201    20      15    a1    I
        //  208    13      16    a2    Ljava/lang/String;
        //  223    18      19    a3    Lcom/google/api/client/http/HttpResponseException;
        //  234    20      15    a4    I
        //  241    13      16    a5    Ljava/lang/String;
        //  320    20      19    a6    Lcom/google/api/client/googleapis/json/GoogleJsonError;
        //  53     287     10    v1    Lcom/google/api/client/http/GenericUrl;
        //  79     261     11    v2    Lcom/google/api/client/http/HttpRequest;
        //  87     253     12    v3    J
        //  99     241     14    v5    Ljava/lang/StringBuilder;
        //  269    71      15    v6    I
        //  269    71      16    v7    Ljava/lang/String;
        //  184    156     17    v8    Ljava/io/IOException;
        //  187    153     18    v9    Lcom/google/api/client/http/HttpResponse;
        //  357    22      10    v1    Ljava/io/IOException;
        //  0      397     0     v-9   Lcom/google/cloud/storage/spi/v1/HttpStorageRpc;
        //  0      397     1     v-8   Ljava/lang/String;
        //  0      397     2     v-7   [B
        //  0      397     3     v-6   I
        //  0      397     4     v-5   J
        //  0      397     6     v-3   I
        //  0      397     7     v-2   Z
        //  9      388     8     v-1   Lio/opencensus/trace/Span;
        //  20     377     9     v0    Lio/opencensus/common/Scope;
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                                              
        //  -----  -----  -----  -----  --------------------------------------------------
        //  187    208    221    254    Lcom/google/api/client/http/HttpResponseException;
        //  187    208    254    269    Any
        //  221    241    254    269    Any
        //  254    256    254    269    Any
        //  20     30     355    379    Ljava/io/IOException;
        //  43     340    355    379    Ljava/io/IOException;
        //  20     30     379    396    Any
        //  43     340    379    396    Any
        //  355    381    379    396    Any
        // 
        // The error that occurred was:
        // 
        // java.lang.IndexOutOfBoundsException: Index: 0, Size: 0
        //     at java.util.ArrayList.rangeCheck(ArrayList.java:657)
        //     at java.util.ArrayList.get(ArrayList.java:433)
        //     at com.strobel.decompiler.ast.AstBuilder.convertLocalVariables(AstBuilder.java:3037)
        //     at com.strobel.decompiler.ast.AstBuilder.performStackAnalysis(AstBuilder.java:2446)
        //     at com.strobel.decompiler.ast.AstBuilder.build(AstBuilder.java:109)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:210)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:757)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:655)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:532)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:499)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:141)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:130)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:105)
        //     at cuchaz.enigma.Deobfuscator.getSourceTree(Deobfuscator.java:224)
        //     at cuchaz.enigma.Deobfuscator.writeSources(Deobfuscator.java:306)
        //     at cuchaz.enigma.gui.GuiController$1.run(GuiController.java:110)
        //     at cuchaz.enigma.gui.ProgressDialog$1.run(ProgressDialog.java:98)
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    @Override
    public String open(final StorageObject v-14, final Map<Option, ?> v-13) {
        final Span startSpan = /*EL:785*/this.startSpan(HttpStorageRpcSpans.SPAN_NAME_OPEN);
        final Scope withSpan = /*EL:786*/this.tracer.withSpan(startSpan);
        try {
            final Storage.Objects.Insert insert = /*EL:788*/this.storage.objects().insert(v-14.getBucket(), v-14);
            GenericUrl url = /*EL:789*/insert.buildHttpRequest().getUrl();
            final String scheme = /*EL:790*/url.getScheme();
            final String host = /*EL:791*/url.getHost();
            int port = /*EL:792*/url.getPort();
            /*SL:793*/port = ((port > 0) ? port : url.toURL().getDefaultPort());
            final String string = /*EL:794*/"/upload" + url.getRawPath();
            /*SL:795*/url = new GenericUrl(scheme + "://" + host + ":" + port + string);
            /*SL:796*/url.set("uploadType", "resumable");
            /*SL:797*/url.set("name", v-14.getName());
            /*SL:798*/for (Object a2 : v-13.keySet()) {
                /*SL:799*/a2 = a2.<Object>get(v-13);
                /*SL:800*/if (a2 != null) {
                    /*SL:801*/url.set(a2.value(), a2.toString());
                }
            }
            final JsonFactory jsonFactory = /*EL:804*/this.storage.getJsonFactory();
            final HttpRequestFactory requestFactory = /*EL:805*/this.storage.getRequestFactory();
            final HttpRequest buildPostRequest = /*EL:806*/requestFactory.buildPostRequest(url, new JsonHttpContent(jsonFactory, v-14));
            final HttpHeaders headers = /*EL:808*/buildPostRequest.getHeaders();
            /*SL:809*/headers.set("X-Upload-Content-Type", /*EL:811*/MoreObjects.firstNonNull((Object)v-14.getContentType(), (Object)"application/octet-stream"));
            final String v0 = Option.CUSTOMER_SUPPLIED_KEY.getString(/*EL:812*/v-13);
            /*SL:813*/if (v0 != null) {
                final BaseEncoding v = /*EL:814*/BaseEncoding.base64();
                final HashFunction v2 = /*EL:815*/Hashing.sha256();
                /*SL:816*/headers.set("x-goog-encryption-algorithm", "AES256");
                /*SL:817*/headers.set("x-goog-encryption-key", v0);
                /*SL:818*/headers.set("x-goog-encryption-key-sha256", v.encode(v2.hashBytes(v.decode(v0)).asBytes()));
            }
            final HttpResponse v3 = /*EL:822*/buildPostRequest.execute();
            /*SL:823*/if (v3.getStatusCode() != 200) {
                final GoogleJsonError v4 = /*EL:824*/new GoogleJsonError();
                /*SL:825*/v4.setCode(v3.getStatusCode());
                /*SL:826*/v4.setMessage(v3.getStatusMessage());
                throw translate(/*EL:827*/v4);
            }
            /*SL:829*/return v3.getHeaders().getLocation();
        }
        catch (IOException a3) {
            /*SL:831*/startSpan.setStatus(Status.UNKNOWN.withDescription(a3.getMessage()));
            throw translate(/*EL:832*/a3);
        }
        finally {
            /*SL:834*/withSpan.close();
            /*SL:835*/startSpan.end();
        }
    }
    
    @Override
    public String open(final String v-2) {
        final Span startSpan = /*EL:841*/this.startSpan(HttpStorageRpcSpans.SPAN_NAME_OPEN);
        final Scope v0 = /*EL:842*/this.tracer.withSpan(startSpan);
        try {
            final GenericUrl v = /*EL:844*/new GenericUrl(v-2);
            /*SL:845*/v.set("uploadType", "resumable");
            final String v2 = /*EL:846*/"";
            final byte[] v3 = /*EL:847*/new byte[v2.length()];
            final HttpRequestFactory v4 = /*EL:848*/this.storage.getRequestFactory();
            final HttpRequest v5 = /*EL:849*/v4.buildPostRequest(v, new ByteArrayContent("", v3, 0, v3.length));
            final HttpHeaders v6 = /*EL:852*/v5.getHeaders();
            /*SL:853*/v6.set("X-Upload-Content-Type", "");
            /*SL:854*/v6.set("x-goog-resumable", "start");
            final HttpResponse v7 = /*EL:855*/v5.execute();
            /*SL:856*/if (v7.getStatusCode() != 201) {
                final GoogleJsonError a1 = /*EL:857*/new GoogleJsonError();
                /*SL:858*/a1.setCode(v7.getStatusCode());
                /*SL:859*/a1.setMessage(v7.getStatusMessage());
                throw translate(/*EL:860*/a1);
            }
            /*SL:862*/return v7.getHeaders().getLocation();
        }
        catch (IOException v8) {
            /*SL:864*/startSpan.setStatus(Status.UNKNOWN.withDescription(v8.getMessage()));
            throw translate(/*EL:865*/v8);
        }
        finally {
            /*SL:867*/v0.close();
            /*SL:868*/startSpan.end();
        }
    }
    
    @Override
    public RewriteResponse openRewrite(final RewriteRequest a1) {
        final Span v1 = /*EL:874*/this.startSpan(HttpStorageRpcSpans.SPAN_NAME_OPEN_REWRITE);
        final Scope v2 = /*EL:875*/this.tracer.withSpan(v1);
        try {
            /*SL:877*/return this.rewrite(a1, null);
        }
        finally {
            /*SL:879*/v2.close();
            /*SL:880*/v1.end();
        }
    }
    
    @Override
    public RewriteResponse continueRewrite(final RewriteResponse a1) {
        final Span v1 = /*EL:886*/this.startSpan(HttpStorageRpcSpans.SPAN_NAME_CONTINUE_REWRITE);
        final Scope v2 = /*EL:887*/this.tracer.withSpan(v1);
        try {
            /*SL:889*/return this.rewrite(a1.rewriteRequest, a1.rewriteToken);
        }
        finally {
            /*SL:891*/v2.close();
            /*SL:892*/v1.end();
        }
    }
    
    private RewriteResponse rewrite(final RewriteRequest v-3, final String v-2) {
        try {
            String a1 = Option.USER_PROJECT.getString(/*EL:898*/v-3.sourceOptions);
            /*SL:899*/if (a1 == null) {
                /*SL:900*/a1 = Option.USER_PROJECT.getString(v-3.targetOptions);
            }
            final Long a2 = /*EL:903*/(v-3.megabytesRewrittenPerCall != null) ? (v-3.megabytesRewrittenPerCall * /*EL:904*/1048576L) : null;
            final Storage.Objects.Rewrite v1 = /*EL:905*/this.storage.objects().rewrite(/*EL:907*/v-3.source.getBucket(), /*EL:909*/v-3.source.getName(), /*EL:910*/v-3.target.getBucket(), /*EL:911*/v-3.target.getName(), /*EL:912*/v-3.overrideInfo ? v-3.target : null).setSourceGeneration(v-3.source.getGeneration()).setRewriteToken(/*EL:914*/v-2).setMaxBytesRewrittenPerCall(/*EL:915*/a2).setProjection(/*EL:916*/"full").setIfSourceMetagenerationMatch(Option.IF_SOURCE_METAGENERATION_MATCH.getLong(/*EL:917*/v-3.sourceOptions)).setIfSourceMetagenerationNotMatch(Option.IF_SOURCE_METAGENERATION_NOT_MATCH.getLong(/*EL:918*/v-3.sourceOptions)).setIfSourceGenerationMatch(Option.IF_SOURCE_GENERATION_MATCH.getLong(/*EL:920*/v-3.sourceOptions)).setIfSourceGenerationNotMatch(Option.IF_SOURCE_GENERATION_NOT_MATCH.getLong(/*EL:922*/v-3.sourceOptions)).setIfMetagenerationMatch(Option.IF_METAGENERATION_MATCH.getLong(/*EL:924*/v-3.targetOptions)).setIfMetagenerationNotMatch(Option.IF_METAGENERATION_NOT_MATCH.getLong(/*EL:926*/v-3.targetOptions)).setIfGenerationMatch(Option.IF_GENERATION_MATCH.getLong(/*EL:927*/v-3.targetOptions)).setIfGenerationNotMatch(Option.IF_GENERATION_NOT_MATCH.getLong(/*EL:929*/v-3.targetOptions)).setDestinationPredefinedAcl(Option.PREDEFINED_ACL.getString(/*EL:930*/v-3.targetOptions)).setUserProject(/*EL:931*/a1).setDestinationKmsKeyName(Option.KMS_KEY_NAME.getString(/*EL:932*/v-3.targetOptions));
            final HttpHeaders v2 = /*EL:934*/v1.getRequestHeaders();
            setEncryptionHeaders(/*EL:935*/v2, "x-goog-copy-source-encryption-", v-3.sourceOptions);
            setEncryptionHeaders(/*EL:936*/v2, "x-goog-encryption-", v-3.targetOptions);
            final com.google.api.services.storage.model.RewriteResponse v3 = /*EL:937*/(com.google.api.services.storage.model.RewriteResponse)v1.execute();
            /*SL:938*/return new RewriteResponse(v-3, v3.getResource(), /*EL:940*/v3.getObjectSize(), /*EL:941*/v3.getDone(), /*EL:942*/v3.getRewriteToken(), /*EL:943*/v3.getTotalBytesRewritten());
        }
        catch (IOException a3) {
            /*SL:946*/this.tracer.getCurrentSpan().setStatus(Status.UNKNOWN.withDescription(a3.getMessage()));
            throw translate(/*EL:947*/a3);
        }
    }
    
    @Override
    public BucketAccessControl getAcl(final String v1, final String v2, final Map<Option, ?> v3) {
        final Span v4 = /*EL:953*/this.startSpan(HttpStorageRpcSpans.SPAN_NAME_GET_BUCKET_ACL);
        final Scope v5 = /*EL:954*/this.tracer.withSpan(v4);
        try {
            /*SL:956*/return (BucketAccessControl)this.storage.bucketAccessControls().get(/*EL:957*/v1, v2).setUserProject(Option.USER_PROJECT.getString(/*EL:958*/v3)).execute();
        }
        catch (IOException a2) {
            /*SL:962*/v4.setStatus(Status.UNKNOWN.withDescription(a2.getMessage()));
            /*SL:963*/a2 = translate(a2);
            /*SL:964*/if (a2.getCode() == 404) {
                /*SL:965*/return null;
            }
            /*SL:967*/throw a2;
        }
        finally {
            /*SL:969*/v5.close();
            /*SL:970*/v4.end();
        }
    }
    
    @Override
    public boolean deleteAcl(final String v1, final String v2, final Map<Option, ?> v3) {
        final Span v4 = /*EL:976*/this.startSpan(HttpStorageRpcSpans.SPAN_NAME_DELETE_BUCKET_ACL);
        final Scope v5 = /*EL:977*/this.tracer.withSpan(v4);
        try {
            /*SL:979*/this.storage.bucketAccessControls().delete(/*EL:980*/v1, v2).setUserProject(Option.USER_PROJECT.getString(/*EL:981*/v3)).execute();
            /*SL:984*/return true;
        }
        catch (IOException a2) {
            /*SL:986*/v4.setStatus(Status.UNKNOWN.withDescription(a2.getMessage()));
            /*SL:987*/a2 = translate(a2);
            /*SL:988*/if (a2.getCode() == 404) {
                /*SL:989*/return false;
            }
            /*SL:991*/throw a2;
        }
        finally {
            /*SL:993*/v5.close();
            /*SL:994*/v4.end();
        }
    }
    
    @Override
    public BucketAccessControl createAcl(final BucketAccessControl v1, final Map<Option, ?> v2) {
        final Span v3 = /*EL:1000*/this.startSpan(HttpStorageRpcSpans.SPAN_NAME_CREATE_BUCKET_ACL);
        final Scope v4 = /*EL:1001*/this.tracer.withSpan(v3);
        try {
            /*SL:1003*/return (BucketAccessControl)this.storage.bucketAccessControls().insert(/*EL:1004*/v1.getBucket(), /*EL:1005*/v1).setUserProject(Option.USER_PROJECT.getString(v2)).execute();
        }
        catch (IOException a1) {
            /*SL:1009*/v3.setStatus(Status.UNKNOWN.withDescription(a1.getMessage()));
            throw translate(/*EL:1010*/a1);
        }
        finally {
            /*SL:1012*/v4.close();
            /*SL:1013*/v3.end();
        }
    }
    
    @Override
    public BucketAccessControl patchAcl(final BucketAccessControl v1, final Map<Option, ?> v2) {
        final Span v3 = /*EL:1019*/this.startSpan(HttpStorageRpcSpans.SPAN_NAME_PATCH_BUCKET_ACL);
        final Scope v4 = /*EL:1020*/this.tracer.withSpan(v3);
        try {
            /*SL:1022*/return (BucketAccessControl)this.storage.bucketAccessControls().patch(/*EL:1023*/v1.getBucket(), /*EL:1024*/v1.getEntity(), v1).setUserProject(Option.USER_PROJECT.getString(v2)).execute();
        }
        catch (IOException a1) {
            /*SL:1028*/v3.setStatus(Status.UNKNOWN.withDescription(a1.getMessage()));
            throw translate(/*EL:1029*/a1);
        }
        finally {
            /*SL:1031*/v4.close();
            /*SL:1032*/v3.end();
        }
    }
    
    @Override
    public List<BucketAccessControl> listAcls(final String v1, final Map<Option, ?> v2) {
        final Span v3 = /*EL:1038*/this.startSpan(HttpStorageRpcSpans.SPAN_NAME_LIST_BUCKET_ACLS);
        final Scope v4 = /*EL:1039*/this.tracer.withSpan(v3);
        try {
            /*SL:1041*/return (List<BucketAccessControl>)((BucketAccessControls)this.storage.bucketAccessControls().list(/*EL:1042*/v1).setUserProject(Option.USER_PROJECT.getString(/*EL:1043*/v2)).execute()).getItems();
        }
        catch (IOException a1) {
            /*SL:1048*/v3.setStatus(Status.UNKNOWN.withDescription(a1.getMessage()));
            throw translate(/*EL:1049*/a1);
        }
        finally {
            /*SL:1051*/v4.close();
            /*SL:1052*/v3.end();
        }
    }
    
    @Override
    public ObjectAccessControl getDefaultAcl(final String v2, final String v3) {
        final Span v4 = /*EL:1058*/this.startSpan(HttpStorageRpcSpans.SPAN_NAME_GET_OBJECT_DEFAULT_ACL);
        final Scope v5 = /*EL:1059*/this.tracer.withSpan(v4);
        try {
            /*SL:1061*/return (ObjectAccessControl)this.storage.defaultObjectAccessControls().get(v2, v3).execute();
        }
        catch (IOException a2) {
            /*SL:1063*/v4.setStatus(Status.UNKNOWN.withDescription(a2.getMessage()));
            /*SL:1064*/a2 = translate(a2);
            /*SL:1065*/if (a2.getCode() == 404) {
                /*SL:1066*/return null;
            }
            /*SL:1068*/throw a2;
        }
        finally {
            /*SL:1070*/v5.close();
            /*SL:1071*/v4.end();
        }
    }
    
    @Override
    public boolean deleteDefaultAcl(final String v2, final String v3) {
        final Span v4 = /*EL:1077*/this.startSpan(HttpStorageRpcSpans.SPAN_NAME_DELETE_OBJECT_DEFAULT_ACL);
        final Scope v5 = /*EL:1078*/this.tracer.withSpan(v4);
        try {
            /*SL:1080*/this.storage.defaultObjectAccessControls().delete(v2, v3).execute();
            /*SL:1081*/return true;
        }
        catch (IOException a2) {
            /*SL:1083*/v4.setStatus(Status.UNKNOWN.withDescription(a2.getMessage()));
            /*SL:1084*/a2 = translate(a2);
            /*SL:1085*/if (a2.getCode() == 404) {
                /*SL:1086*/return false;
            }
            /*SL:1088*/throw a2;
        }
        finally {
            /*SL:1090*/v5.close();
            /*SL:1091*/v4.end();
        }
    }
    
    @Override
    public ObjectAccessControl createDefaultAcl(final ObjectAccessControl v2) {
        final Span v3 = /*EL:1097*/this.startSpan(HttpStorageRpcSpans.SPAN_NAME_CREATE_OBJECT_DEFAULT_ACL);
        final Scope v4 = /*EL:1098*/this.tracer.withSpan(v3);
        try {
            /*SL:1100*/return (ObjectAccessControl)this.storage.defaultObjectAccessControls().insert(v2.getBucket(), v2).execute();
        }
        catch (IOException a1) {
            /*SL:1102*/v3.setStatus(Status.UNKNOWN.withDescription(a1.getMessage()));
            throw translate(/*EL:1103*/a1);
        }
        finally {
            /*SL:1105*/v4.close();
            /*SL:1106*/v3.end();
        }
    }
    
    @Override
    public ObjectAccessControl patchDefaultAcl(final ObjectAccessControl v2) {
        final Span v3 = /*EL:1112*/this.startSpan(HttpStorageRpcSpans.SPAN_NAME_PATCH_OBJECT_DEFAULT_ACL);
        final Scope v4 = /*EL:1113*/this.tracer.withSpan(v3);
        try {
            /*SL:1115*/return (ObjectAccessControl)this.storage.defaultObjectAccessControls().patch(/*EL:1116*/v2.getBucket(), /*EL:1117*/v2.getEntity(), v2).execute();
        }
        catch (IOException a1) {
            /*SL:1120*/v3.setStatus(Status.UNKNOWN.withDescription(a1.getMessage()));
            throw translate(/*EL:1121*/a1);
        }
        finally {
            /*SL:1123*/v4.close();
            /*SL:1124*/v3.end();
        }
    }
    
    @Override
    public List<ObjectAccessControl> listDefaultAcls(final String v2) {
        final Span v3 = /*EL:1130*/this.startSpan(HttpStorageRpcSpans.SPAN_NAME_LIST_OBJECT_DEFAULT_ACLS);
        final Scope v4 = /*EL:1131*/this.tracer.withSpan(v3);
        try {
            /*SL:1133*/return (List<ObjectAccessControl>)((ObjectAccessControls)this.storage.defaultObjectAccessControls().list(v2).execute()).getItems();
        }
        catch (IOException a1) {
            /*SL:1135*/v3.setStatus(Status.UNKNOWN.withDescription(a1.getMessage()));
            throw translate(/*EL:1136*/a1);
        }
        finally {
            /*SL:1138*/v4.close();
            /*SL:1139*/v3.end();
        }
    }
    
    @Override
    public ObjectAccessControl getAcl(final String a4, final String v1, final Long v2, final String v3) {
        final Span v4 = /*EL:1145*/this.startSpan(HttpStorageRpcSpans.SPAN_NAME_GET_OBJECT_ACL);
        final Scope v5 = /*EL:1146*/this.tracer.withSpan(v4);
        try {
            /*SL:1148*/return (ObjectAccessControl)this.storage.objectAccessControls().get(/*EL:1149*/a4, v1, v3).setGeneration(/*EL:1150*/v2).execute();
        }
        catch (IOException a5) {
            /*SL:1154*/v4.setStatus(Status.UNKNOWN.withDescription(a5.getMessage()));
            final StorageException a6 = translate(/*EL:1155*/a5);
            /*SL:1156*/if (a6.getCode() == 404) {
                /*SL:1157*/return null;
            }
            /*SL:1159*/throw a6;
        }
        finally {
            /*SL:1161*/v5.close();
            /*SL:1162*/v4.end();
        }
    }
    
    @Override
    public boolean deleteAcl(final String a4, final String v1, final Long v2, final String v3) {
        final Span v4 = /*EL:1168*/this.startSpan(HttpStorageRpcSpans.SPAN_NAME_DELETE_OBJECT_ACL);
        final Scope v5 = /*EL:1169*/this.tracer.withSpan(v4);
        try {
            /*SL:1171*/this.storage.objectAccessControls().delete(/*EL:1172*/a4, v1, v3).setGeneration(/*EL:1173*/v2).execute();
            /*SL:1176*/return true;
        }
        catch (IOException a5) {
            /*SL:1178*/v4.setStatus(Status.UNKNOWN.withDescription(a5.getMessage()));
            final StorageException a6 = translate(/*EL:1179*/a5);
            /*SL:1180*/if (a6.getCode() == 404) {
                /*SL:1181*/return false;
            }
            /*SL:1183*/throw a6;
        }
        finally {
            /*SL:1185*/v5.close();
            /*SL:1186*/v4.end();
        }
    }
    
    @Override
    public ObjectAccessControl createAcl(final ObjectAccessControl v2) {
        final Span v3 = /*EL:1192*/this.startSpan(HttpStorageRpcSpans.SPAN_NAME_CREATE_OBJECT_ACL);
        final Scope v4 = /*EL:1193*/this.tracer.withSpan(v3);
        try {
            /*SL:1195*/return (ObjectAccessControl)this.storage.objectAccessControls().insert(/*EL:1196*/v2.getBucket(), /*EL:1197*/v2.getObject(), v2).setGeneration(v2.getGeneration()).execute();
        }
        catch (IOException a1) {
            /*SL:1201*/v3.setStatus(Status.UNKNOWN.withDescription(a1.getMessage()));
            throw translate(/*EL:1202*/a1);
        }
        finally {
            /*SL:1204*/v4.close();
            /*SL:1205*/v3.end();
        }
    }
    
    @Override
    public ObjectAccessControl patchAcl(final ObjectAccessControl v2) {
        final Span v3 = /*EL:1211*/this.startSpan(HttpStorageRpcSpans.SPAN_NAME_PATCH_OBJECT_ACL);
        final Scope v4 = /*EL:1212*/this.tracer.withSpan(v3);
        try {
            /*SL:1214*/return (ObjectAccessControl)this.storage.objectAccessControls().patch(/*EL:1215*/v2.getBucket(), /*EL:1216*/v2.getObject(), v2.getEntity(), v2).setGeneration(v2.getGeneration()).execute();
        }
        catch (IOException a1) {
            /*SL:1220*/v3.setStatus(Status.UNKNOWN.withDescription(a1.getMessage()));
            throw translate(/*EL:1221*/a1);
        }
        finally {
            /*SL:1223*/v4.close();
            /*SL:1224*/v3.end();
        }
    }
    
    @Override
    public List<ObjectAccessControl> listAcls(final String a3, final String v1, final Long v2) {
        final Span v3 = /*EL:1230*/this.startSpan(HttpStorageRpcSpans.SPAN_NAME_LIST_OBJECT_ACLS);
        final Scope v4 = /*EL:1231*/this.tracer.withSpan(v3);
        try {
            /*SL:1233*/return (List<ObjectAccessControl>)((ObjectAccessControls)this.storage.objectAccessControls().list(/*EL:1234*/a3, v1).setGeneration(/*EL:1235*/v2).execute()).getItems();
        }
        catch (IOException a4) {
            /*SL:1240*/v3.setStatus(Status.UNKNOWN.withDescription(a4.getMessage()));
            throw translate(/*EL:1241*/a4);
        }
        finally {
            /*SL:1243*/v4.close();
            /*SL:1244*/v3.end();
        }
    }
    
    @Override
    public HmacKey createHmacKey(final String v1, final Map<Option, ?> v2) {
        final Span v3 = /*EL:1250*/this.startSpan(HttpStorageRpcSpans.SPAN_NAME_CREATE_HMAC_KEY);
        final Scope v4 = /*EL:1251*/this.tracer.withSpan(v3);
        String v5 = Option.PROJECT_ID.getString(/*EL:1252*/v2);
        /*SL:1253*/if (v5 == null) {
            /*SL:1254*/v5 = this.options.getProjectId();
        }
        try {
            /*SL:1257*/return (HmacKey)this.storage.projects().hmacKeys().create(/*EL:1259*/v5, v1).setUserProject(Option.USER_PROJECT.getString(/*EL:1260*/v2)).execute();
        }
        catch (IOException a1) {
            /*SL:1264*/v3.setStatus(Status.UNKNOWN.withDescription(a1.getMessage()));
            throw translate(/*EL:1265*/a1);
        }
        finally {
            /*SL:1267*/v4.close();
            /*SL:1268*/v3.end();
        }
    }
    
    @Override
    public Tuple<String, Iterable<HmacKeyMetadata>> listHmacKeys(final Map<Option, ?> v-3) {
        final Span startSpan = /*EL:1274*/this.startSpan(HttpStorageRpcSpans.SPAN_NAME_LIST_HMAC_KEYS);
        final Scope withSpan = /*EL:1275*/this.tracer.withSpan(startSpan);
        String v0 = Option.PROJECT_ID.getString(/*EL:1276*/v-3);
        /*SL:1277*/if (v0 == null) {
            /*SL:1278*/v0 = this.options.getProjectId();
        }
        try {
            final HmacKeysMetadata a1 = /*EL:1281*/(HmacKeysMetadata)this.storage.projects().hmacKeys().list(/*EL:1284*/v0).setServiceAccountEmail(Option.SERVICE_ACCOUNT_EMAIL.getString(/*EL:1285*/v-3)).setPageToken(Option.PAGE_TOKEN.getString(/*EL:1286*/v-3)).setMaxResults(Option.MAX_RESULTS.getLong(/*EL:1287*/v-3)).setShowDeletedKeys(Option.SHOW_DELETED_KEYS.getBoolean(/*EL:1288*/v-3)).execute();
            /*SL:1291*/return (Tuple<String, Iterable<HmacKeyMetadata>>)Tuple.of((Object)a1.getNextPageToken(), /*EL:1292*/(Object)a1.getItems());
        }
        catch (IOException v) {
            /*SL:1294*/startSpan.setStatus(Status.UNKNOWN.withDescription(v.getMessage()));
            throw translate(/*EL:1295*/v);
        }
        finally {
            /*SL:1297*/withSpan.close();
            /*SL:1298*/startSpan.end();
        }
    }
    
    @Override
    public HmacKeyMetadata getHmacKey(final String v1, final Map<Option, ?> v2) {
        final Span v3 = /*EL:1304*/this.startSpan(HttpStorageRpcSpans.SPAN_NAME_GET_HMAC_KEY);
        final Scope v4 = /*EL:1305*/this.tracer.withSpan(v3);
        String v5 = Option.PROJECT_ID.getString(/*EL:1306*/v2);
        /*SL:1307*/if (v5 == null) {
            /*SL:1308*/v5 = this.options.getProjectId();
        }
        try {
            /*SL:1311*/return (HmacKeyMetadata)this.storage.projects().hmacKeys().get(/*EL:1313*/v5, v1).setUserProject(Option.USER_PROJECT.getString(/*EL:1314*/v2)).execute();
        }
        catch (IOException a1) {
            /*SL:1318*/v3.setStatus(Status.UNKNOWN.withDescription(a1.getMessage()));
            throw translate(/*EL:1319*/a1);
        }
        finally {
            /*SL:1321*/v4.close();
            /*SL:1322*/v3.end();
        }
    }
    
    @Override
    public HmacKeyMetadata updateHmacKey(final HmacKeyMetadata v1, final Map<Option, ?> v2) {
        final Span v3 = /*EL:1328*/this.startSpan(HttpStorageRpcSpans.SPAN_NAME_UPDATE_HMAC_KEY);
        final Scope v4 = /*EL:1329*/this.tracer.withSpan(v3);
        String v5 = /*EL:1330*/v1.getProjectId();
        /*SL:1331*/if (v5 == null) {
            /*SL:1332*/v5 = this.options.getProjectId();
        }
        try {
            /*SL:1335*/return (HmacKeyMetadata)this.storage.projects().hmacKeys().update(/*EL:1337*/v5, v1.getAccessId(), /*EL:1338*/v1).setUserProject(Option.USER_PROJECT.getString(v2)).execute();
        }
        catch (IOException a1) {
            /*SL:1342*/v3.setStatus(Status.UNKNOWN.withDescription(a1.getMessage()));
            throw translate(/*EL:1343*/a1);
        }
        finally {
            /*SL:1345*/v4.close();
            /*SL:1346*/v3.end();
        }
    }
    
    @Override
    public void deleteHmacKey(final HmacKeyMetadata v1, final Map<Option, ?> v2) {
        final Span v3 = /*EL:1352*/this.startSpan(HttpStorageRpcSpans.SPAN_NAME_DELETE_HMAC_KEY);
        final Scope v4 = /*EL:1353*/this.tracer.withSpan(v3);
        String v5 = /*EL:1354*/v1.getProjectId();
        /*SL:1355*/if (v5 == null) {
            /*SL:1356*/v5 = this.options.getProjectId();
        }
        try {
            /*SL:1359*/this.storage.projects().hmacKeys().delete(/*EL:1361*/v5, v1.getAccessId()).setUserProject(Option.USER_PROJECT.getString(/*EL:1362*/v2)).execute();
        }
        catch (IOException a1) {
            /*SL:1366*/v3.setStatus(Status.UNKNOWN.withDescription(a1.getMessage()));
            throw translate(/*EL:1367*/a1);
        }
        finally {
            /*SL:1369*/v4.close();
            /*SL:1370*/v3.end();
        }
    }
    
    @Override
    public Policy getIamPolicy(final String v1, final Map<Option, ?> v2) {
        final Span v3 = /*EL:1376*/this.startSpan(HttpStorageRpcSpans.SPAN_NAME_GET_BUCKET_IAM_POLICY);
        final Scope v4 = /*EL:1377*/this.tracer.withSpan(v3);
        try {
            /*SL:1379*/return (Policy)this.storage.buckets().getIamPolicy(/*EL:1380*/v1).setUserProject(Option.USER_PROJECT.getString(/*EL:1381*/v2)).execute();
        }
        catch (IOException a1) {
            /*SL:1385*/v3.setStatus(Status.UNKNOWN.withDescription(a1.getMessage()));
            throw translate(/*EL:1386*/a1);
        }
        finally {
            /*SL:1388*/v4.close();
            /*SL:1389*/v3.end();
        }
    }
    
    @Override
    public Policy setIamPolicy(final String a3, final Policy v1, final Map<Option, ?> v2) {
        final Span v3 = /*EL:1395*/this.startSpan(HttpStorageRpcSpans.SPAN_NAME_SET_BUCKET_IAM_POLICY);
        final Scope v4 = /*EL:1396*/this.tracer.withSpan(v3);
        try {
            /*SL:1398*/return (Policy)this.storage.buckets().setIamPolicy(/*EL:1399*/a3, v1).setUserProject(Option.USER_PROJECT.getString(/*EL:1400*/v2)).execute();
        }
        catch (IOException a4) {
            /*SL:1404*/v3.setStatus(Status.UNKNOWN.withDescription(a4.getMessage()));
            throw translate(/*EL:1405*/a4);
        }
        finally {
            /*SL:1407*/v4.close();
            /*SL:1408*/v3.end();
        }
    }
    
    @Override
    public TestIamPermissionsResponse testIamPermissions(final String a3, final List<String> v1, final Map<Option, ?> v2) {
        final Span v3 = /*EL:1415*/this.startSpan(HttpStorageRpcSpans.SPAN_NAME_TEST_BUCKET_IAM_PERMISSIONS);
        final Scope v4 = /*EL:1416*/this.tracer.withSpan(v3);
        try {
            /*SL:1418*/return (TestIamPermissionsResponse)this.storage.buckets().testIamPermissions(/*EL:1419*/a3, (List)v1).setUserProject(Option.USER_PROJECT.getString(/*EL:1420*/v2)).execute();
        }
        catch (IOException a4) {
            /*SL:1424*/v3.setStatus(Status.UNKNOWN.withDescription(a4.getMessage()));
            throw translate(/*EL:1425*/a4);
        }
        finally {
            /*SL:1427*/v4.close();
            /*SL:1428*/v3.end();
        }
    }
    
    @Override
    public boolean deleteNotification(final String v2, final String v3) {
        final Span v4 = /*EL:1434*/this.startSpan(HttpStorageRpcSpans.SPAN_NAME_DELETE_NOTIFICATION);
        final Scope v5 = /*EL:1435*/this.tracer.withSpan(v4);
        try {
            /*SL:1437*/this.storage.notifications().delete(v2, v3).execute();
            /*SL:1438*/return true;
        }
        catch (IOException a2) {
            /*SL:1440*/v4.setStatus(Status.UNKNOWN.withDescription(a2.getMessage()));
            /*SL:1441*/a2 = translate(a2);
            /*SL:1442*/if (a2.getCode() == 404) {
                /*SL:1443*/return false;
            }
            /*SL:1445*/throw a2;
        }
        finally {
            /*SL:1447*/v5.close();
            /*SL:1448*/v4.end();
        }
    }
    
    @Override
    public List<Notification> listNotifications(final String v2) {
        final Span v3 = /*EL:1454*/this.startSpan(HttpStorageRpcSpans.SPAN_NAME_LIST_NOTIFICATIONS);
        final Scope v4 = /*EL:1455*/this.tracer.withSpan(v3);
        try {
            /*SL:1457*/return (List<Notification>)((Notifications)this.storage.notifications().list(v2).execute()).getItems();
        }
        catch (IOException a1) {
            /*SL:1459*/v3.setStatus(Status.UNKNOWN.withDescription(a1.getMessage()));
            throw translate(/*EL:1460*/a1);
        }
        finally {
            /*SL:1462*/v4.close();
            /*SL:1463*/v3.end();
        }
    }
    
    @Override
    public Notification createNotification(final String v1, final Notification v2) {
        final Span v3 = /*EL:1469*/this.startSpan(HttpStorageRpcSpans.SPAN_NAME_CREATE_NOTIFICATION);
        final Scope v4 = /*EL:1470*/this.tracer.withSpan(v3);
        try {
            /*SL:1472*/return (Notification)this.storage.notifications().insert(v1, v2).execute();
        }
        catch (IOException a1) {
            /*SL:1474*/v3.setStatus(Status.UNKNOWN.withDescription(a1.getMessage()));
            throw translate(/*EL:1475*/a1);
        }
        finally {
            /*SL:1477*/v4.close();
            /*SL:1478*/v3.end();
        }
    }
    
    @Override
    public Bucket lockRetentionPolicy(final Bucket v1, final Map<Option, ?> v2) {
        final Span v3 = /*EL:1484*/this.startSpan(HttpStorageRpcSpans.SPAN_LOCK_RETENTION_POLICY);
        final Scope v4 = /*EL:1485*/this.tracer.withSpan(v3);
        try {
            /*SL:1487*/return (Bucket)this.storage.buckets().lockRetentionPolicy(/*EL:1488*/v1.getName(), Option.IF_METAGENERATION_MATCH.getLong(/*EL:1489*/v2)).setUserProject(Option.USER_PROJECT.getString(v2)).execute();
        }
        catch (IOException a1) {
            /*SL:1493*/v3.setStatus(Status.UNKNOWN.withDescription(a1.getMessage()));
            throw translate(/*EL:1494*/a1);
        }
        finally {
            /*SL:1496*/v4.close();
            /*SL:1497*/v3.end();
        }
    }
    
    @Override
    public ServiceAccount getServiceAccount(final String v2) {
        final Span v3 = /*EL:1503*/this.startSpan(HttpStorageRpcSpans.SPAN_NAME_GET_SERVICE_ACCOUNT);
        final Scope v4 = /*EL:1504*/this.tracer.withSpan(v3);
        try {
            /*SL:1506*/return (ServiceAccount)this.storage.projects().serviceAccount().get(v2).execute();
        }
        catch (IOException a1) {
            /*SL:1508*/v3.setStatus(Status.UNKNOWN.withDescription(a1.getMessage()));
            throw translate(/*EL:1509*/a1);
        }
        finally {
            /*SL:1511*/v4.close();
            /*SL:1512*/v3.end();
        }
    }
    
    private class DefaultRpcBatch implements RpcBatch
    {
        private static final int MAX_BATCH_SIZE = 100;
        private final Storage storage;
        private final LinkedList<BatchRequest> batches;
        private int currentBatchSize;
        
        private DefaultRpcBatch(final Storage a1) {
            this.storage = a1;
            (this.batches = new LinkedList<BatchRequest>()).add(a1.batch(HttpStorageRpc.this.batchRequestInitializer));
        }
        
        @Override
        public void addDelete(final StorageObject a3, final Callback<Void> v1, final Map<Option, ?> v2) {
            try {
                /*SL:144*/if (this.currentBatchSize == 100) {
                    /*SL:145*/this.batches.add(this.storage.batch());
                    /*SL:146*/this.currentBatchSize = 0;
                }
                /*SL:148*/HttpStorageRpc.this.deleteCall(a3, v2).queue((BatchRequest)this.batches.getLast(), HttpStorageRpc.<Object>toJsonCallback((RpcBatch.Callback<Object>)v1));
                /*SL:149*/++this.currentBatchSize;
            }
            catch (IOException a4) {
                /*SL:151*/throw translate(a4);
            }
        }
        
        @Override
        public void addPatch(final StorageObject a3, final Callback<StorageObject> v1, final Map<Option, ?> v2) {
            try {
                /*SL:161*/if (this.currentBatchSize == 100) {
                    /*SL:162*/this.batches.add(this.storage.batch());
                    /*SL:163*/this.currentBatchSize = 0;
                }
                /*SL:165*/HttpStorageRpc.this.patchCall(a3, v2).queue((BatchRequest)this.batches.getLast(), HttpStorageRpc.<Object>toJsonCallback((RpcBatch.Callback<Object>)v1));
                /*SL:166*/++this.currentBatchSize;
            }
            catch (IOException a4) {
                /*SL:168*/throw translate(a4);
            }
        }
        
        @Override
        public void addGet(final StorageObject a3, final Callback<StorageObject> v1, final Map<Option, ?> v2) {
            try {
                /*SL:178*/if (this.currentBatchSize == 100) {
                    /*SL:179*/this.batches.add(this.storage.batch());
                    /*SL:180*/this.currentBatchSize = 0;
                }
                /*SL:182*/HttpStorageRpc.this.getCall(a3, v2).queue((BatchRequest)this.batches.getLast(), HttpStorageRpc.<Object>toJsonCallback((RpcBatch.Callback<Object>)v1));
                /*SL:183*/++this.currentBatchSize;
            }
            catch (IOException a4) {
                /*SL:185*/throw translate(a4);
            }
        }
        
        @Override
        public void submit() {
            final Span access$600 = /*EL:191*/HttpStorageRpc.this.startSpan(HttpStorageRpcSpans.SPAN_NAME_BATCH_SUBMIT);
            final Scope withSpan = /*EL:192*/HttpStorageRpc.this.tracer.withSpan(access$600);
            try {
                /*SL:194*/access$600.putAttribute("batch size", AttributeValue.longAttributeValue((long)this.batches.size()));
                /*SL:195*/for (final BatchRequest v1 : this.batches) {
                    /*SL:198*/access$600.addAnnotation("Execute batch request");
                    /*SL:199*/v1.setBatchUrl(new GenericUrl(/*EL:200*/String.format("%s/batch/storage/v1", HttpStorageRpc.this.options.getHost())));
                    /*SL:201*/v1.execute();
                }
            }
            catch (IOException v2) {
                /*SL:204*/access$600.setStatus(Status.UNKNOWN.withDescription(v2.getMessage()));
                /*SL:205*/throw translate(v2);
            }
            finally {
                /*SL:207*/withSpan.close();
                /*SL:208*/access$600.end();
            }
        }
    }
}

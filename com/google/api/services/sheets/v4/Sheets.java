package com.google.api.services.sheets.v4;

import com.google.api.client.googleapis.services.AbstractGoogleClient;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;
import com.google.api.services.sheets.v4.model.UpdateValuesResponse;
import com.google.api.services.sheets.v4.model.ClearValuesResponse;
import com.google.api.services.sheets.v4.model.BatchUpdateValuesByDataFilterResponse;
import com.google.api.services.sheets.v4.model.BatchUpdateValuesResponse;
import com.google.api.services.sheets.v4.model.BatchGetValuesByDataFilterResponse;
import com.google.api.services.sheets.v4.model.BatchGetValuesResponse;
import com.google.api.services.sheets.v4.model.BatchClearValuesByDataFilterResponse;
import com.google.api.services.sheets.v4.model.BatchClearValuesResponse;
import com.google.api.services.sheets.v4.model.AppendValuesResponse;
import com.google.api.services.sheets.v4.model.ClearValuesRequest;
import com.google.api.services.sheets.v4.model.BatchUpdateValuesByDataFilterRequest;
import com.google.api.services.sheets.v4.model.BatchUpdateValuesRequest;
import com.google.api.services.sheets.v4.model.BatchGetValuesByDataFilterRequest;
import com.google.api.services.sheets.v4.model.BatchClearValuesByDataFilterRequest;
import com.google.api.services.sheets.v4.model.BatchClearValuesRequest;
import com.google.api.services.sheets.v4.model.ValueRange;
import com.google.api.services.sheets.v4.model.SheetProperties;
import com.google.api.services.sheets.v4.model.CopySheetToAnotherSpreadsheetRequest;
import com.google.api.services.sheets.v4.model.SearchDeveloperMetadataResponse;
import com.google.api.services.sheets.v4.model.DeveloperMetadata;
import com.google.api.services.sheets.v4.model.SearchDeveloperMetadataRequest;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpResponse;
import java.util.List;
import com.google.api.client.util.GenericData;
import com.google.api.client.googleapis.services.json.AbstractGoogleJsonClientRequest;
import com.google.api.client.util.Key;
import com.google.api.services.sheets.v4.model.BatchUpdateSpreadsheetResponse;
import com.google.api.services.sheets.v4.model.GetSpreadsheetByDataFilterRequest;
import com.google.api.services.sheets.v4.model.Spreadsheet;
import com.google.api.services.sheets.v4.model.BatchUpdateSpreadsheetRequest;
import com.google.api.client.util.Preconditions;
import com.google.api.client.googleapis.GoogleUtils;
import java.io.IOException;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.googleapis.services.json.AbstractGoogleJsonClient;

public class Sheets extends AbstractGoogleJsonClient
{
    public static final String DEFAULT_ROOT_URL = "https://sheets.googleapis.com/";
    public static final String DEFAULT_SERVICE_PATH = "";
    public static final String DEFAULT_BATCH_PATH = "batch";
    public static final String DEFAULT_BASE_URL = "https://sheets.googleapis.com/";
    
    public Sheets(final HttpTransport httpTransport, final JsonFactory jsonFactory, final HttpRequestInitializer httpRequestInitializer) {
        this(new Builder(httpTransport, jsonFactory, httpRequestInitializer));
    }
    
    Sheets(final Builder a1) {
        super(a1);
    }
    
    protected void initialize(final AbstractGoogleClientRequest<?> a1) throws IOException {
        /*SL:126*/super.initialize(a1);
    }
    
    public Spreadsheets spreadsheets() {
        /*SL:141*/return new Spreadsheets();
    }
    
    static {
        Preconditions.checkState(GoogleUtils.MAJOR_VERSION == 1 && GoogleUtils.MINOR_VERSION >= 15, "You are currently running with version %s of google-api-client. You need at least version 1.15 of google-api-client to run version 1.25.0 of the Google Sheets API library.", GoogleUtils.VERSION);
    }
    
    public class Spreadsheets
    {
        final /* synthetic */ Sheets this$0;
        
        public BatchUpdate batchUpdate(final String s, final BatchUpdateSpreadsheetRequest batchUpdateSpreadsheetRequest) throws IOException {
            final BatchUpdate batchUpdate = /*EL:175*/new BatchUpdate(s, batchUpdateSpreadsheetRequest);
            /*SL:176*/Sheets.this.initialize(batchUpdate);
            /*SL:177*/return batchUpdate;
        }
        
        public Create create(final Spreadsheet spreadsheet) throws IOException {
            final Create create = /*EL:306*/new Create(spreadsheet);
            /*SL:307*/Sheets.this.initialize(create);
            /*SL:308*/return create;
        }
        
        public Get get(final String s) throws IOException {
            final Get get = /*EL:418*/new Get(s);
            /*SL:419*/Sheets.this.initialize(get);
            /*SL:420*/return get;
        }
        
        public GetByDataFilter getByDataFilter(final String s, final GetSpreadsheetByDataFilterRequest getSpreadsheetByDataFilterRequest) throws IOException {
            final GetByDataFilter getByDataFilter = /*EL:612*/new GetByDataFilter(s, getSpreadsheetByDataFilterRequest);
            /*SL:613*/Sheets.this.initialize(getByDataFilter);
            /*SL:614*/return getByDataFilter;
        }
        
        public DeveloperMetadata developerMetadata() {
            /*SL:745*/return new DeveloperMetadata();
        }
        
        public SheetsOperations sheets() {
            /*SL:1036*/return new SheetsOperations();
        }
        
        public Values values() {
            /*SL:1196*/return new Values();
        }
        
        public class BatchUpdate extends SheetsRequest<BatchUpdateSpreadsheetResponse>
        {
            private static final String REST_PATH = "v4/spreadsheets/{spreadsheetId}:batchUpdate";
            @Key
            private String spreadsheetId;
            
            protected BatchUpdate(final String a1, final BatchUpdateSpreadsheetRequest batchUpdateSpreadsheetRequest) {
                super(Spreadsheets.this.this$0, "POST", "v4/spreadsheets/{spreadsheetId}:batchUpdate", batchUpdateSpreadsheetRequest, BatchUpdateSpreadsheetResponse.class);
                this.spreadsheetId = Preconditions.<String>checkNotNull(a1, (Object)"Required parameter spreadsheetId must be specified.");
            }
            
            public BatchUpdate set$Xgafv(final String s) {
                /*SL:220*/return (BatchUpdate)super.set$Xgafv(s);
            }
            
            public BatchUpdate setAccessToken(final String accessToken) {
                /*SL:225*/return (BatchUpdate)super.setAccessToken(accessToken);
            }
            
            public BatchUpdate setAlt(final String alt) {
                /*SL:230*/return (BatchUpdate)super.setAlt(alt);
            }
            
            public BatchUpdate setCallback(final String callback) {
                /*SL:235*/return (BatchUpdate)super.setCallback(callback);
            }
            
            public BatchUpdate setFields(final String fields) {
                /*SL:240*/return (BatchUpdate)super.setFields(fields);
            }
            
            public BatchUpdate setKey(final String key) {
                /*SL:245*/return (BatchUpdate)super.setKey(key);
            }
            
            public BatchUpdate setOauthToken(final String oauthToken) {
                /*SL:250*/return (BatchUpdate)super.setOauthToken(oauthToken);
            }
            
            public BatchUpdate setPrettyPrint(final Boolean prettyPrint) {
                /*SL:255*/return (BatchUpdate)super.setPrettyPrint(prettyPrint);
            }
            
            public BatchUpdate setQuotaUser(final String quotaUser) {
                /*SL:260*/return (BatchUpdate)super.setQuotaUser(quotaUser);
            }
            
            public BatchUpdate setUploadType(final String uploadType) {
                /*SL:265*/return (BatchUpdate)super.setUploadType(uploadType);
            }
            
            public BatchUpdate setUploadProtocol(final String uploadProtocol) {
                /*SL:270*/return (BatchUpdate)super.setUploadProtocol(uploadProtocol);
            }
            
            public String getSpreadsheetId() {
                /*SL:280*/return this.spreadsheetId;
            }
            
            public BatchUpdate setSpreadsheetId(final String spreadsheetId) {
                /*SL:285*/this.spreadsheetId = spreadsheetId;
                /*SL:286*/return this;
            }
            
            public BatchUpdate set(final String s, final Object o) {
                /*SL:291*/return (BatchUpdate)super.set(s, o);
            }
        }
        
        public class Create extends SheetsRequest<Spreadsheet>
        {
            private static final String REST_PATH = "v4/spreadsheets";
            
            protected Create(final Spreadsheet spreadsheet) {
                super(Spreadsheets.this.this$0, "POST", "v4/spreadsheets", spreadsheet, Spreadsheet.class);
            }
            
            public Create set$Xgafv(final String s) {
                /*SL:334*/return (Create)super.set$Xgafv(s);
            }
            
            public Create setAccessToken(final String accessToken) {
                /*SL:339*/return (Create)super.setAccessToken(accessToken);
            }
            
            public Create setAlt(final String alt) {
                /*SL:344*/return (Create)super.setAlt(alt);
            }
            
            public Create setCallback(final String callback) {
                /*SL:349*/return (Create)super.setCallback(callback);
            }
            
            public Create setFields(final String fields) {
                /*SL:354*/return (Create)super.setFields(fields);
            }
            
            public Create setKey(final String key) {
                /*SL:359*/return (Create)super.setKey(key);
            }
            
            public Create setOauthToken(final String oauthToken) {
                /*SL:364*/return (Create)super.setOauthToken(oauthToken);
            }
            
            public Create setPrettyPrint(final Boolean prettyPrint) {
                /*SL:369*/return (Create)super.setPrettyPrint(prettyPrint);
            }
            
            public Create setQuotaUser(final String quotaUser) {
                /*SL:374*/return (Create)super.setQuotaUser(quotaUser);
            }
            
            public Create setUploadType(final String uploadType) {
                /*SL:379*/return (Create)super.setUploadType(uploadType);
            }
            
            public Create setUploadProtocol(final String uploadProtocol) {
                /*SL:384*/return (Create)super.setUploadProtocol(uploadProtocol);
            }
            
            public Create set(final String s, final Object o) {
                /*SL:389*/return (Create)super.set(s, o);
            }
        }
        
        public class Get extends SheetsRequest<Spreadsheet>
        {
            private static final String REST_PATH = "v4/spreadsheets/{spreadsheetId}";
            @Key
            private String spreadsheetId;
            @Key
            private List<String> ranges;
            @Key
            private Boolean includeGridData;
            
            protected Get(final String a1) {
                super(Spreadsheets.this.this$0, "GET", "v4/spreadsheets/{spreadsheetId}", (Object)null, Spreadsheet.class);
                this.spreadsheetId = Preconditions.<String>checkNotNull(a1, (Object)"Required parameter spreadsheetId must be specified.");
            }
            
            public HttpResponse executeUsingHead() throws IOException {
                /*SL:461*/return super.executeUsingHead();
            }
            
            public HttpRequest buildHttpRequestUsingHead() throws IOException {
                /*SL:466*/return super.buildHttpRequestUsingHead();
            }
            
            public Get set$Xgafv(final String s) {
                /*SL:471*/return (Get)super.set$Xgafv(s);
            }
            
            public Get setAccessToken(final String accessToken) {
                /*SL:476*/return (Get)super.setAccessToken(accessToken);
            }
            
            public Get setAlt(final String alt) {
                /*SL:481*/return (Get)super.setAlt(alt);
            }
            
            public Get setCallback(final String callback) {
                /*SL:486*/return (Get)super.setCallback(callback);
            }
            
            public Get setFields(final String fields) {
                /*SL:491*/return (Get)super.setFields(fields);
            }
            
            public Get setKey(final String key) {
                /*SL:496*/return (Get)super.setKey(key);
            }
            
            public Get setOauthToken(final String oauthToken) {
                /*SL:501*/return (Get)super.setOauthToken(oauthToken);
            }
            
            public Get setPrettyPrint(final Boolean prettyPrint) {
                /*SL:506*/return (Get)super.setPrettyPrint(prettyPrint);
            }
            
            public Get setQuotaUser(final String quotaUser) {
                /*SL:511*/return (Get)super.setQuotaUser(quotaUser);
            }
            
            public Get setUploadType(final String uploadType) {
                /*SL:516*/return (Get)super.setUploadType(uploadType);
            }
            
            public Get setUploadProtocol(final String uploadProtocol) {
                /*SL:521*/return (Get)super.setUploadProtocol(uploadProtocol);
            }
            
            public String getSpreadsheetId() {
                /*SL:531*/return this.spreadsheetId;
            }
            
            public Get setSpreadsheetId(final String spreadsheetId) {
                /*SL:536*/this.spreadsheetId = spreadsheetId;
                /*SL:537*/return this;
            }
            
            public List<String> getRanges() {
                /*SL:547*/return this.ranges;
            }
            
            public Get setRanges(final List<String> ranges) {
                /*SL:552*/this.ranges = ranges;
                /*SL:553*/return this;
            }
            
            public Boolean getIncludeGridData() {
                /*SL:567*/return this.includeGridData;
            }
            
            public Get setIncludeGridData(final Boolean includeGridData) {
                /*SL:575*/this.includeGridData = includeGridData;
                /*SL:576*/return this;
            }
            
            public Get set(final String s, final Object o) {
                /*SL:581*/return (Get)super.set(s, o);
            }
        }
        
        public class GetByDataFilter extends SheetsRequest<Spreadsheet>
        {
            private static final String REST_PATH = "v4/spreadsheets/{spreadsheetId}:getByDataFilter";
            @Key
            private String spreadsheetId;
            
            protected GetByDataFilter(final String a1, final GetSpreadsheetByDataFilterRequest getSpreadsheetByDataFilterRequest) {
                super(Spreadsheets.this.this$0, "POST", "v4/spreadsheets/{spreadsheetId}:getByDataFilter", getSpreadsheetByDataFilterRequest, Spreadsheet.class);
                this.spreadsheetId = Preconditions.<String>checkNotNull(a1, (Object)"Required parameter spreadsheetId must be specified.");
            }
            
            public GetByDataFilter set$Xgafv(final String s) {
                /*SL:658*/return (GetByDataFilter)super.set$Xgafv(s);
            }
            
            public GetByDataFilter setAccessToken(final String accessToken) {
                /*SL:663*/return (GetByDataFilter)super.setAccessToken(accessToken);
            }
            
            public GetByDataFilter setAlt(final String alt) {
                /*SL:668*/return (GetByDataFilter)super.setAlt(alt);
            }
            
            public GetByDataFilter setCallback(final String callback) {
                /*SL:673*/return (GetByDataFilter)super.setCallback(callback);
            }
            
            public GetByDataFilter setFields(final String fields) {
                /*SL:678*/return (GetByDataFilter)super.setFields(fields);
            }
            
            public GetByDataFilter setKey(final String key) {
                /*SL:683*/return (GetByDataFilter)super.setKey(key);
            }
            
            public GetByDataFilter setOauthToken(final String oauthToken) {
                /*SL:688*/return (GetByDataFilter)super.setOauthToken(oauthToken);
            }
            
            public GetByDataFilter setPrettyPrint(final Boolean prettyPrint) {
                /*SL:693*/return (GetByDataFilter)super.setPrettyPrint(prettyPrint);
            }
            
            public GetByDataFilter setQuotaUser(final String quotaUser) {
                /*SL:698*/return (GetByDataFilter)super.setQuotaUser(quotaUser);
            }
            
            public GetByDataFilter setUploadType(final String uploadType) {
                /*SL:703*/return (GetByDataFilter)super.setUploadType(uploadType);
            }
            
            public GetByDataFilter setUploadProtocol(final String uploadProtocol) {
                /*SL:708*/return (GetByDataFilter)super.setUploadProtocol(uploadProtocol);
            }
            
            public String getSpreadsheetId() {
                /*SL:718*/return this.spreadsheetId;
            }
            
            public GetByDataFilter setSpreadsheetId(final String spreadsheetId) {
                /*SL:723*/this.spreadsheetId = spreadsheetId;
                /*SL:724*/return this;
            }
            
            public GetByDataFilter set(final String s, final Object o) {
                /*SL:729*/return (GetByDataFilter)super.set(s, o);
            }
        }
        
        public class DeveloperMetadata
        {
            final /* synthetic */ Spreadsheets this$1;
            
            public Get get(final String s, final Integer n) throws IOException {
                final Get get = /*EL:767*/new Get(s, n);
                /*SL:768*/Sheets.this.initialize(get);
                /*SL:769*/return get;
            }
            
            public Search search(final String s, final SearchDeveloperMetadataRequest searchDeveloperMetadataRequest) throws IOException {
                final Search search = /*EL:915*/new Search(s, searchDeveloperMetadataRequest);
                /*SL:916*/Sheets.this.initialize(search);
                /*SL:917*/return search;
            }
            
            public class Get extends SheetsRequest<com.google.api.services.sheets.v4.model.DeveloperMetadata>
            {
                private static final String REST_PATH = "v4/spreadsheets/{spreadsheetId}/developerMetadata/{metadataId}";
                @Key
                private String spreadsheetId;
                @Key
                private Integer metadataId;
                
                protected Get(final String a1, final Integer a2) {
                    super(DeveloperMetadata.this.this$1.this$0, "GET", "v4/spreadsheets/{spreadsheetId}/developerMetadata/{metadataId}", (Object)null, com.google.api.services.sheets.v4.model.DeveloperMetadata.class);
                    this.spreadsheetId = Preconditions.<String>checkNotNull(a1, (Object)"Required parameter spreadsheetId must be specified.");
                    this.metadataId = Preconditions.<Integer>checkNotNull(a2, (Object)"Required parameter metadataId must be specified.");
                }
                
                public HttpResponse executeUsingHead() throws IOException {
                    /*SL:799*/return super.executeUsingHead();
                }
                
                public HttpRequest buildHttpRequestUsingHead() throws IOException {
                    /*SL:804*/return super.buildHttpRequestUsingHead();
                }
                
                public Get set$Xgafv(final String s) {
                    /*SL:809*/return (Get)super.set$Xgafv(s);
                }
                
                public Get setAccessToken(final String accessToken) {
                    /*SL:814*/return (Get)super.setAccessToken(accessToken);
                }
                
                public Get setAlt(final String alt) {
                    /*SL:819*/return (Get)super.setAlt(alt);
                }
                
                public Get setCallback(final String callback) {
                    /*SL:824*/return (Get)super.setCallback(callback);
                }
                
                public Get setFields(final String fields) {
                    /*SL:829*/return (Get)super.setFields(fields);
                }
                
                public Get setKey(final String key) {
                    /*SL:834*/return (Get)super.setKey(key);
                }
                
                public Get setOauthToken(final String oauthToken) {
                    /*SL:839*/return (Get)super.setOauthToken(oauthToken);
                }
                
                public Get setPrettyPrint(final Boolean prettyPrint) {
                    /*SL:844*/return (Get)super.setPrettyPrint(prettyPrint);
                }
                
                public Get setQuotaUser(final String quotaUser) {
                    /*SL:849*/return (Get)super.setQuotaUser(quotaUser);
                }
                
                public Get setUploadType(final String uploadType) {
                    /*SL:854*/return (Get)super.setUploadType(uploadType);
                }
                
                public Get setUploadProtocol(final String uploadProtocol) {
                    /*SL:859*/return (Get)super.setUploadProtocol(uploadProtocol);
                }
                
                public String getSpreadsheetId() {
                    /*SL:869*/return this.spreadsheetId;
                }
                
                public Get setSpreadsheetId(final String spreadsheetId) {
                    /*SL:874*/this.spreadsheetId = spreadsheetId;
                    /*SL:875*/return this;
                }
                
                public Integer getMetadataId() {
                    /*SL:885*/return this.metadataId;
                }
                
                public Get setMetadataId(final Integer metadataId) {
                    /*SL:890*/this.metadataId = metadataId;
                    /*SL:891*/return this;
                }
                
                public Get set(final String s, final Object o) {
                    /*SL:896*/return (Get)super.set(s, o);
                }
            }
            
            public class Search extends SheetsRequest<SearchDeveloperMetadataResponse>
            {
                private static final String REST_PATH = "v4/spreadsheets/{spreadsheetId}/developerMetadata:search";
                @Key
                private String spreadsheetId;
                
                protected Search(final String a1, final SearchDeveloperMetadataRequest searchDeveloperMetadataRequest) {
                    super(DeveloperMetadata.this.this$1.this$0, "POST", "v4/spreadsheets/{spreadsheetId}/developerMetadata:search", searchDeveloperMetadataRequest, SearchDeveloperMetadataResponse.class);
                    this.spreadsheetId = Preconditions.<String>checkNotNull(a1, (Object)"Required parameter spreadsheetId must be specified.");
                }
                
                public Search set$Xgafv(final String s) {
                    /*SL:948*/return (Search)super.set$Xgafv(s);
                }
                
                public Search setAccessToken(final String accessToken) {
                    /*SL:953*/return (Search)super.setAccessToken(accessToken);
                }
                
                public Search setAlt(final String alt) {
                    /*SL:958*/return (Search)super.setAlt(alt);
                }
                
                public Search setCallback(final String callback) {
                    /*SL:963*/return (Search)super.setCallback(callback);
                }
                
                public Search setFields(final String fields) {
                    /*SL:968*/return (Search)super.setFields(fields);
                }
                
                public Search setKey(final String key) {
                    /*SL:973*/return (Search)super.setKey(key);
                }
                
                public Search setOauthToken(final String oauthToken) {
                    /*SL:978*/return (Search)super.setOauthToken(oauthToken);
                }
                
                public Search setPrettyPrint(final Boolean prettyPrint) {
                    /*SL:983*/return (Search)super.setPrettyPrint(prettyPrint);
                }
                
                public Search setQuotaUser(final String quotaUser) {
                    /*SL:988*/return (Search)super.setQuotaUser(quotaUser);
                }
                
                public Search setUploadType(final String uploadType) {
                    /*SL:993*/return (Search)super.setUploadType(uploadType);
                }
                
                public Search setUploadProtocol(final String uploadProtocol) {
                    /*SL:998*/return (Search)super.setUploadProtocol(uploadProtocol);
                }
                
                public String getSpreadsheetId() {
                    /*SL:1008*/return this.spreadsheetId;
                }
                
                public Search setSpreadsheetId(final String spreadsheetId) {
                    /*SL:1013*/this.spreadsheetId = spreadsheetId;
                    /*SL:1014*/return this;
                }
                
                public Search set(final String s, final Object o) {
                    /*SL:1019*/return (Search)super.set(s, o);
                }
            }
        }
        
        public class SheetsOperations
        {
            final /* synthetic */ Spreadsheets this$1;
            
            public CopyTo copyTo(final String s, final Integer n, final CopySheetToAnotherSpreadsheetRequest copySheetToAnotherSpreadsheetRequest) throws IOException {
                final CopyTo copyTo = /*EL:1059*/new CopyTo(s, n, copySheetToAnotherSpreadsheetRequest);
                /*SL:1060*/Sheets.this.initialize(copyTo);
                /*SL:1061*/return copyTo;
            }
            
            public class CopyTo extends SheetsRequest<SheetProperties>
            {
                private static final String REST_PATH = "v4/spreadsheets/{spreadsheetId}/sheets/{sheetId}:copyTo";
                @Key
                private String spreadsheetId;
                @Key
                private Integer sheetId;
                
                protected CopyTo(final String a1, final Integer a2, final CopySheetToAnotherSpreadsheetRequest copySheetToAnotherSpreadsheetRequest) {
                    super(SheetsOperations.this.this$1.this$0, "POST", "v4/spreadsheets/{spreadsheetId}/sheets/{sheetId}:copyTo", copySheetToAnotherSpreadsheetRequest, SheetProperties.class);
                    this.spreadsheetId = Preconditions.<String>checkNotNull(a1, (Object)"Required parameter spreadsheetId must be specified.");
                    this.sheetId = Preconditions.<Integer>checkNotNull(a2, (Object)"Required parameter sheetId must be specified.");
                }
                
                public CopyTo set$Xgafv(final String s) {
                    /*SL:1092*/return (CopyTo)super.set$Xgafv(s);
                }
                
                public CopyTo setAccessToken(final String accessToken) {
                    /*SL:1097*/return (CopyTo)super.setAccessToken(accessToken);
                }
                
                public CopyTo setAlt(final String alt) {
                    /*SL:1102*/return (CopyTo)super.setAlt(alt);
                }
                
                public CopyTo setCallback(final String callback) {
                    /*SL:1107*/return (CopyTo)super.setCallback(callback);
                }
                
                public CopyTo setFields(final String fields) {
                    /*SL:1112*/return (CopyTo)super.setFields(fields);
                }
                
                public CopyTo setKey(final String key) {
                    /*SL:1117*/return (CopyTo)super.setKey(key);
                }
                
                public CopyTo setOauthToken(final String oauthToken) {
                    /*SL:1122*/return (CopyTo)super.setOauthToken(oauthToken);
                }
                
                public CopyTo setPrettyPrint(final Boolean prettyPrint) {
                    /*SL:1127*/return (CopyTo)super.setPrettyPrint(prettyPrint);
                }
                
                public CopyTo setQuotaUser(final String quotaUser) {
                    /*SL:1132*/return (CopyTo)super.setQuotaUser(quotaUser);
                }
                
                public CopyTo setUploadType(final String uploadType) {
                    /*SL:1137*/return (CopyTo)super.setUploadType(uploadType);
                }
                
                public CopyTo setUploadProtocol(final String uploadProtocol) {
                    /*SL:1142*/return (CopyTo)super.setUploadProtocol(uploadProtocol);
                }
                
                public String getSpreadsheetId() {
                    /*SL:1152*/return this.spreadsheetId;
                }
                
                public CopyTo setSpreadsheetId(final String spreadsheetId) {
                    /*SL:1157*/this.spreadsheetId = spreadsheetId;
                    /*SL:1158*/return this;
                }
                
                public Integer getSheetId() {
                    /*SL:1168*/return this.sheetId;
                }
                
                public CopyTo setSheetId(final Integer sheetId) {
                    /*SL:1173*/this.sheetId = sheetId;
                    /*SL:1174*/return this;
                }
                
                public CopyTo set(final String s, final Object o) {
                    /*SL:1179*/return (CopyTo)super.set(s, o);
                }
            }
        }
        
        public class Values
        {
            final /* synthetic */ Spreadsheets this$1;
            
            public Append append(final String s, final String s2, final ValueRange valueRange) throws IOException {
                final Append append = /*EL:1228*/new Append(s, s2, valueRange);
                /*SL:1229*/Sheets.this.initialize(append);
                /*SL:1230*/return append;
            }
            
            public BatchClear batchClear(final String s, final BatchClearValuesRequest batchClearValuesRequest) throws IOException {
                final BatchClear batchClear = /*EL:1486*/new BatchClear(s, batchClearValuesRequest);
                /*SL:1487*/Sheets.this.initialize(batchClear);
                /*SL:1488*/return batchClear;
            }
            
            public BatchClearByDataFilter batchClearByDataFilter(final String s, final BatchClearValuesByDataFilterRequest batchClearValuesByDataFilterRequest) throws IOException {
                final BatchClearByDataFilter batchClearByDataFilter = /*EL:1610*/new BatchClearByDataFilter(s, batchClearValuesByDataFilterRequest);
                /*SL:1611*/Sheets.this.initialize(batchClearByDataFilter);
                /*SL:1612*/return batchClearByDataFilter;
            }
            
            public BatchGet batchGet(final String s) throws IOException {
                final BatchGet batchGet = /*EL:1731*/new BatchGet(s);
                /*SL:1732*/Sheets.this.initialize(batchGet);
                /*SL:1733*/return batchGet;
            }
            
            public BatchGetByDataFilter batchGetByDataFilter(final String s, final BatchGetValuesByDataFilterRequest batchGetValuesByDataFilterRequest) throws IOException {
                final BatchGetByDataFilter batchGetByDataFilter = /*EL:1959*/new BatchGetByDataFilter(s, batchGetValuesByDataFilterRequest);
                /*SL:1960*/Sheets.this.initialize(batchGetByDataFilter);
                /*SL:1961*/return batchGetByDataFilter;
            }
            
            public BatchUpdate batchUpdate(final String s, final BatchUpdateValuesRequest batchUpdateValuesRequest) throws IOException {
                final BatchUpdate batchUpdate = /*EL:2080*/new BatchUpdate(s, batchUpdateValuesRequest);
                /*SL:2081*/Sheets.this.initialize(batchUpdate);
                /*SL:2082*/return batchUpdate;
            }
            
            public BatchUpdateByDataFilter batchUpdateByDataFilter(final String s, final BatchUpdateValuesByDataFilterRequest batchUpdateValuesByDataFilterRequest) throws IOException {
                final BatchUpdateByDataFilter batchUpdateByDataFilter = /*EL:2201*/new BatchUpdateByDataFilter(s, batchUpdateValuesByDataFilterRequest);
                /*SL:2202*/Sheets.this.initialize(batchUpdateByDataFilter);
                /*SL:2203*/return batchUpdateByDataFilter;
            }
            
            public Clear clear(final String s, final String s2, final ClearValuesRequest clearValuesRequest) throws IOException {
                final Clear clear = /*EL:2323*/new Clear(s, s2, clearValuesRequest);
                /*SL:2324*/Sheets.this.initialize(clear);
                /*SL:2325*/return clear;
            }
            
            public Get get(final String s, final String s2) throws IOException {
                final Get get = /*EL:2461*/new Get(s, s2);
                /*SL:2462*/Sheets.this.initialize(get);
                /*SL:2463*/return get;
            }
            
            public Update update(final String s, final String s2, final ValueRange valueRange) throws IOException {
                final Update update = /*EL:2689*/new Update(s, s2, valueRange);
                /*SL:2690*/Sheets.this.initialize(update);
                /*SL:2691*/return update;
            }
            
            public class Append extends SheetsRequest<AppendValuesResponse>
            {
                private static final String REST_PATH = "v4/spreadsheets/{spreadsheetId}/values/{range}:append";
                @Key
                private String spreadsheetId;
                @Key
                private String range;
                @Key
                private String responseValueRenderOption;
                @Key
                private String insertDataOption;
                @Key
                private String valueInputOption;
                @Key
                private String responseDateTimeRenderOption;
                @Key
                private Boolean includeValuesInResponse;
                
                protected Append(final String a1, final String a2, final ValueRange valueRange) {
                    super(Values.this.this$1.this$0, "POST", "v4/spreadsheets/{spreadsheetId}/values/{range}:append", valueRange, AppendValuesResponse.class);
                    this.spreadsheetId = Preconditions.<String>checkNotNull(a1, (Object)"Required parameter spreadsheetId must be specified.");
                    this.range = Preconditions.<String>checkNotNull(a2, (Object)"Required parameter range must be specified.");
                }
                
                public Append set$Xgafv(final String s) {
                    /*SL:1270*/return (Append)super.set$Xgafv(s);
                }
                
                public Append setAccessToken(final String accessToken) {
                    /*SL:1275*/return (Append)super.setAccessToken(accessToken);
                }
                
                public Append setAlt(final String alt) {
                    /*SL:1280*/return (Append)super.setAlt(alt);
                }
                
                public Append setCallback(final String callback) {
                    /*SL:1285*/return (Append)super.setCallback(callback);
                }
                
                public Append setFields(final String fields) {
                    /*SL:1290*/return (Append)super.setFields(fields);
                }
                
                public Append setKey(final String key) {
                    /*SL:1295*/return (Append)super.setKey(key);
                }
                
                public Append setOauthToken(final String oauthToken) {
                    /*SL:1300*/return (Append)super.setOauthToken(oauthToken);
                }
                
                public Append setPrettyPrint(final Boolean prettyPrint) {
                    /*SL:1305*/return (Append)super.setPrettyPrint(prettyPrint);
                }
                
                public Append setQuotaUser(final String quotaUser) {
                    /*SL:1310*/return (Append)super.setQuotaUser(quotaUser);
                }
                
                public Append setUploadType(final String uploadType) {
                    /*SL:1315*/return (Append)super.setUploadType(uploadType);
                }
                
                public Append setUploadProtocol(final String uploadProtocol) {
                    /*SL:1320*/return (Append)super.setUploadProtocol(uploadProtocol);
                }
                
                public String getSpreadsheetId() {
                    /*SL:1330*/return this.spreadsheetId;
                }
                
                public Append setSpreadsheetId(final String spreadsheetId) {
                    /*SL:1335*/this.spreadsheetId = spreadsheetId;
                    /*SL:1336*/return this;
                }
                
                public String getRange() {
                    /*SL:1350*/return this.range;
                }
                
                public Append setRange(final String range) {
                    /*SL:1358*/this.range = range;
                    /*SL:1359*/return this;
                }
                
                public String getResponseValueRenderOption() {
                    /*SL:1373*/return this.responseValueRenderOption;
                }
                
                public Append setResponseValueRenderOption(final String responseValueRenderOption) {
                    /*SL:1381*/this.responseValueRenderOption = responseValueRenderOption;
                    /*SL:1382*/return this;
                }
                
                public String getInsertDataOption() {
                    /*SL:1392*/return this.insertDataOption;
                }
                
                public Append setInsertDataOption(final String insertDataOption) {
                    /*SL:1397*/this.insertDataOption = insertDataOption;
                    /*SL:1398*/return this;
                }
                
                public String getValueInputOption() {
                    /*SL:1408*/return this.valueInputOption;
                }
                
                public Append setValueInputOption(final String valueInputOption) {
                    /*SL:1413*/this.valueInputOption = valueInputOption;
                    /*SL:1414*/return this;
                }
                
                public String getResponseDateTimeRenderOption() {
                    /*SL:1430*/return this.responseDateTimeRenderOption;
                }
                
                public Append setResponseDateTimeRenderOption(final String responseDateTimeRenderOption) {
                    /*SL:1439*/this.responseDateTimeRenderOption = responseDateTimeRenderOption;
                    /*SL:1440*/return this;
                }
                
                public Boolean getIncludeValuesInResponse() {
                    /*SL:1454*/return this.includeValuesInResponse;
                }
                
                public Append setIncludeValuesInResponse(final Boolean includeValuesInResponse) {
                    /*SL:1462*/this.includeValuesInResponse = includeValuesInResponse;
                    /*SL:1463*/return this;
                }
                
                public Append set(final String s, final Object o) {
                    /*SL:1468*/return (Append)super.set(s, o);
                }
            }
            
            public class BatchClear extends SheetsRequest<BatchClearValuesResponse>
            {
                private static final String REST_PATH = "v4/spreadsheets/{spreadsheetId}/values:batchClear";
                @Key
                private String spreadsheetId;
                
                protected BatchClear(final String a1, final BatchClearValuesRequest batchClearValuesRequest) {
                    super(Values.this.this$1.this$0, "POST", "v4/spreadsheets/{spreadsheetId}/values:batchClear", batchClearValuesRequest, BatchClearValuesResponse.class);
                    this.spreadsheetId = Preconditions.<String>checkNotNull(a1, (Object)"Required parameter spreadsheetId must be specified.");
                }
                
                public BatchClear set$Xgafv(final String s) {
                    /*SL:1519*/return (BatchClear)super.set$Xgafv(s);
                }
                
                public BatchClear setAccessToken(final String accessToken) {
                    /*SL:1524*/return (BatchClear)super.setAccessToken(accessToken);
                }
                
                public BatchClear setAlt(final String alt) {
                    /*SL:1529*/return (BatchClear)super.setAlt(alt);
                }
                
                public BatchClear setCallback(final String callback) {
                    /*SL:1534*/return (BatchClear)super.setCallback(callback);
                }
                
                public BatchClear setFields(final String fields) {
                    /*SL:1539*/return (BatchClear)super.setFields(fields);
                }
                
                public BatchClear setKey(final String key) {
                    /*SL:1544*/return (BatchClear)super.setKey(key);
                }
                
                public BatchClear setOauthToken(final String oauthToken) {
                    /*SL:1549*/return (BatchClear)super.setOauthToken(oauthToken);
                }
                
                public BatchClear setPrettyPrint(final Boolean prettyPrint) {
                    /*SL:1554*/return (BatchClear)super.setPrettyPrint(prettyPrint);
                }
                
                public BatchClear setQuotaUser(final String quotaUser) {
                    /*SL:1559*/return (BatchClear)super.setQuotaUser(quotaUser);
                }
                
                public BatchClear setUploadType(final String uploadType) {
                    /*SL:1564*/return (BatchClear)super.setUploadType(uploadType);
                }
                
                public BatchClear setUploadProtocol(final String uploadProtocol) {
                    /*SL:1569*/return (BatchClear)super.setUploadProtocol(uploadProtocol);
                }
                
                public String getSpreadsheetId() {
                    /*SL:1579*/return this.spreadsheetId;
                }
                
                public BatchClear setSpreadsheetId(final String spreadsheetId) {
                    /*SL:1584*/this.spreadsheetId = spreadsheetId;
                    /*SL:1585*/return this;
                }
                
                public BatchClear set(final String s, final Object o) {
                    /*SL:1590*/return (BatchClear)super.set(s, o);
                }
            }
            
            public class BatchClearByDataFilter extends SheetsRequest<BatchClearValuesByDataFilterResponse>
            {
                private static final String REST_PATH = "v4/spreadsheets/{spreadsheetId}/values:batchClearByDataFilter";
                @Key
                private String spreadsheetId;
                
                protected BatchClearByDataFilter(final String a1, final BatchClearValuesByDataFilterRequest batchClearValuesByDataFilterRequest) {
                    super(Values.this.this$1.this$0, "POST", "v4/spreadsheets/{spreadsheetId}/values:batchClearByDataFilter", batchClearValuesByDataFilterRequest, BatchClearValuesByDataFilterResponse.class);
                    this.spreadsheetId = Preconditions.<String>checkNotNull(a1, (Object)"Required parameter spreadsheetId must be specified.");
                }
                
                public BatchClearByDataFilter set$Xgafv(final String s) {
                    /*SL:1644*/return (BatchClearByDataFilter)super.set$Xgafv(s);
                }
                
                public BatchClearByDataFilter setAccessToken(final String accessToken) {
                    /*SL:1649*/return (BatchClearByDataFilter)super.setAccessToken(accessToken);
                }
                
                public BatchClearByDataFilter setAlt(final String alt) {
                    /*SL:1654*/return (BatchClearByDataFilter)super.setAlt(alt);
                }
                
                public BatchClearByDataFilter setCallback(final String callback) {
                    /*SL:1659*/return (BatchClearByDataFilter)super.setCallback(callback);
                }
                
                public BatchClearByDataFilter setFields(final String fields) {
                    /*SL:1664*/return (BatchClearByDataFilter)super.setFields(fields);
                }
                
                public BatchClearByDataFilter setKey(final String key) {
                    /*SL:1669*/return (BatchClearByDataFilter)super.setKey(key);
                }
                
                public BatchClearByDataFilter setOauthToken(final String oauthToken) {
                    /*SL:1674*/return (BatchClearByDataFilter)super.setOauthToken(oauthToken);
                }
                
                public BatchClearByDataFilter setPrettyPrint(final Boolean prettyPrint) {
                    /*SL:1679*/return (BatchClearByDataFilter)super.setPrettyPrint(prettyPrint);
                }
                
                public BatchClearByDataFilter setQuotaUser(final String quotaUser) {
                    /*SL:1684*/return (BatchClearByDataFilter)super.setQuotaUser(quotaUser);
                }
                
                public BatchClearByDataFilter setUploadType(final String uploadType) {
                    /*SL:1689*/return (BatchClearByDataFilter)super.setUploadType(uploadType);
                }
                
                public BatchClearByDataFilter setUploadProtocol(final String uploadProtocol) {
                    /*SL:1694*/return (BatchClearByDataFilter)super.setUploadProtocol(uploadProtocol);
                }
                
                public String getSpreadsheetId() {
                    /*SL:1704*/return this.spreadsheetId;
                }
                
                public BatchClearByDataFilter setSpreadsheetId(final String spreadsheetId) {
                    /*SL:1709*/this.spreadsheetId = spreadsheetId;
                    /*SL:1710*/return this;
                }
                
                public BatchClearByDataFilter set(final String s, final Object o) {
                    /*SL:1715*/return (BatchClearByDataFilter)super.set(s, o);
                }
            }
            
            public class BatchGet extends SheetsRequest<BatchGetValuesResponse>
            {
                private static final String REST_PATH = "v4/spreadsheets/{spreadsheetId}/values:batchGet";
                @Key
                private String spreadsheetId;
                @Key
                private String valueRenderOption;
                @Key
                private String dateTimeRenderOption;
                @Key
                private List<String> ranges;
                @Key
                private String majorDimension;
                
                protected BatchGet(final String a1) {
                    super(Values.this.this$1.this$0, "GET", "v4/spreadsheets/{spreadsheetId}/values:batchGet", (Object)null, BatchGetValuesResponse.class);
                    this.spreadsheetId = Preconditions.<String>checkNotNull(a1, (Object)"Required parameter spreadsheetId must be specified.");
                }
                
                public HttpResponse executeUsingHead() throws IOException {
                    /*SL:1762*/return super.executeUsingHead();
                }
                
                public HttpRequest buildHttpRequestUsingHead() throws IOException {
                    /*SL:1767*/return super.buildHttpRequestUsingHead();
                }
                
                public BatchGet set$Xgafv(final String s) {
                    /*SL:1772*/return (BatchGet)super.set$Xgafv(s);
                }
                
                public BatchGet setAccessToken(final String accessToken) {
                    /*SL:1777*/return (BatchGet)super.setAccessToken(accessToken);
                }
                
                public BatchGet setAlt(final String alt) {
                    /*SL:1782*/return (BatchGet)super.setAlt(alt);
                }
                
                public BatchGet setCallback(final String callback) {
                    /*SL:1787*/return (BatchGet)super.setCallback(callback);
                }
                
                public BatchGet setFields(final String fields) {
                    /*SL:1792*/return (BatchGet)super.setFields(fields);
                }
                
                public BatchGet setKey(final String key) {
                    /*SL:1797*/return (BatchGet)super.setKey(key);
                }
                
                public BatchGet setOauthToken(final String oauthToken) {
                    /*SL:1802*/return (BatchGet)super.setOauthToken(oauthToken);
                }
                
                public BatchGet setPrettyPrint(final Boolean prettyPrint) {
                    /*SL:1807*/return (BatchGet)super.setPrettyPrint(prettyPrint);
                }
                
                public BatchGet setQuotaUser(final String quotaUser) {
                    /*SL:1812*/return (BatchGet)super.setQuotaUser(quotaUser);
                }
                
                public BatchGet setUploadType(final String uploadType) {
                    /*SL:1817*/return (BatchGet)super.setUploadType(uploadType);
                }
                
                public BatchGet setUploadProtocol(final String uploadProtocol) {
                    /*SL:1822*/return (BatchGet)super.setUploadProtocol(uploadProtocol);
                }
                
                public String getSpreadsheetId() {
                    /*SL:1832*/return this.spreadsheetId;
                }
                
                public BatchGet setSpreadsheetId(final String spreadsheetId) {
                    /*SL:1837*/this.spreadsheetId = spreadsheetId;
                    /*SL:1838*/return this;
                }
                
                public String getValueRenderOption() {
                    /*SL:1852*/return this.valueRenderOption;
                }
                
                public BatchGet setValueRenderOption(final String valueRenderOption) {
                    /*SL:1860*/this.valueRenderOption = valueRenderOption;
                    /*SL:1861*/return this;
                }
                
                public String getDateTimeRenderOption() {
                    /*SL:1877*/return this.dateTimeRenderOption;
                }
                
                public BatchGet setDateTimeRenderOption(final String dateTimeRenderOption) {
                    /*SL:1886*/this.dateTimeRenderOption = dateTimeRenderOption;
                    /*SL:1887*/return this;
                }
                
                public List<String> getRanges() {
                    /*SL:1897*/return this.ranges;
                }
                
                public BatchGet setRanges(final List<String> ranges) {
                    /*SL:1902*/this.ranges = ranges;
                    /*SL:1903*/return this;
                }
                
                public String getMajorDimension() {
                    /*SL:1923*/return this.majorDimension;
                }
                
                public BatchGet setMajorDimension(final String majorDimension) {
                    /*SL:1934*/this.majorDimension = majorDimension;
                    /*SL:1935*/return this;
                }
                
                public BatchGet set(final String s, final Object o) {
                    /*SL:1940*/return (BatchGet)super.set(s, o);
                }
            }
            
            public class BatchGetByDataFilter extends SheetsRequest<BatchGetValuesByDataFilterResponse>
            {
                private static final String REST_PATH = "v4/spreadsheets/{spreadsheetId}/values:batchGetByDataFilter";
                @Key
                private String spreadsheetId;
                
                protected BatchGetByDataFilter(final String a1, final BatchGetValuesByDataFilterRequest batchGetValuesByDataFilterRequest) {
                    super(Values.this.this$1.this$0, "POST", "v4/spreadsheets/{spreadsheetId}/values:batchGetByDataFilter", batchGetValuesByDataFilterRequest, BatchGetValuesByDataFilterResponse.class);
                    this.spreadsheetId = Preconditions.<String>checkNotNull(a1, (Object)"Required parameter spreadsheetId must be specified.");
                }
                
                public BatchGetByDataFilter set$Xgafv(final String s) {
                    /*SL:1992*/return (BatchGetByDataFilter)super.set$Xgafv(s);
                }
                
                public BatchGetByDataFilter setAccessToken(final String accessToken) {
                    /*SL:1997*/return (BatchGetByDataFilter)super.setAccessToken(accessToken);
                }
                
                public BatchGetByDataFilter setAlt(final String alt) {
                    /*SL:2002*/return (BatchGetByDataFilter)super.setAlt(alt);
                }
                
                public BatchGetByDataFilter setCallback(final String callback) {
                    /*SL:2007*/return (BatchGetByDataFilter)super.setCallback(callback);
                }
                
                public BatchGetByDataFilter setFields(final String fields) {
                    /*SL:2012*/return (BatchGetByDataFilter)super.setFields(fields);
                }
                
                public BatchGetByDataFilter setKey(final String key) {
                    /*SL:2017*/return (BatchGetByDataFilter)super.setKey(key);
                }
                
                public BatchGetByDataFilter setOauthToken(final String oauthToken) {
                    /*SL:2022*/return (BatchGetByDataFilter)super.setOauthToken(oauthToken);
                }
                
                public BatchGetByDataFilter setPrettyPrint(final Boolean prettyPrint) {
                    /*SL:2027*/return (BatchGetByDataFilter)super.setPrettyPrint(prettyPrint);
                }
                
                public BatchGetByDataFilter setQuotaUser(final String quotaUser) {
                    /*SL:2032*/return (BatchGetByDataFilter)super.setQuotaUser(quotaUser);
                }
                
                public BatchGetByDataFilter setUploadType(final String uploadType) {
                    /*SL:2037*/return (BatchGetByDataFilter)super.setUploadType(uploadType);
                }
                
                public BatchGetByDataFilter setUploadProtocol(final String uploadProtocol) {
                    /*SL:2042*/return (BatchGetByDataFilter)super.setUploadProtocol(uploadProtocol);
                }
                
                public String getSpreadsheetId() {
                    /*SL:2052*/return this.spreadsheetId;
                }
                
                public BatchGetByDataFilter setSpreadsheetId(final String spreadsheetId) {
                    /*SL:2057*/this.spreadsheetId = spreadsheetId;
                    /*SL:2058*/return this;
                }
                
                public BatchGetByDataFilter set(final String s, final Object o) {
                    /*SL:2063*/return (BatchGetByDataFilter)super.set(s, o);
                }
            }
            
            public class BatchUpdate extends SheetsRequest<BatchUpdateValuesResponse>
            {
                private static final String REST_PATH = "v4/spreadsheets/{spreadsheetId}/values:batchUpdate";
                @Key
                private String spreadsheetId;
                
                protected BatchUpdate(final String a1, final BatchUpdateValuesRequest batchUpdateValuesRequest) {
                    super(Values.this.this$1.this$0, "POST", "v4/spreadsheets/{spreadsheetId}/values:batchUpdate", batchUpdateValuesRequest, BatchUpdateValuesResponse.class);
                    this.spreadsheetId = Preconditions.<String>checkNotNull(a1, (Object)"Required parameter spreadsheetId must be specified.");
                }
                
                public BatchUpdate set$Xgafv(final String s) {
                    /*SL:2112*/return (BatchUpdate)super.set$Xgafv(s);
                }
                
                public BatchUpdate setAccessToken(final String accessToken) {
                    /*SL:2117*/return (BatchUpdate)super.setAccessToken(accessToken);
                }
                
                public BatchUpdate setAlt(final String alt) {
                    /*SL:2122*/return (BatchUpdate)super.setAlt(alt);
                }
                
                public BatchUpdate setCallback(final String callback) {
                    /*SL:2127*/return (BatchUpdate)super.setCallback(callback);
                }
                
                public BatchUpdate setFields(final String fields) {
                    /*SL:2132*/return (BatchUpdate)super.setFields(fields);
                }
                
                public BatchUpdate setKey(final String key) {
                    /*SL:2137*/return (BatchUpdate)super.setKey(key);
                }
                
                public BatchUpdate setOauthToken(final String oauthToken) {
                    /*SL:2142*/return (BatchUpdate)super.setOauthToken(oauthToken);
                }
                
                public BatchUpdate setPrettyPrint(final Boolean prettyPrint) {
                    /*SL:2147*/return (BatchUpdate)super.setPrettyPrint(prettyPrint);
                }
                
                public BatchUpdate setQuotaUser(final String quotaUser) {
                    /*SL:2152*/return (BatchUpdate)super.setQuotaUser(quotaUser);
                }
                
                public BatchUpdate setUploadType(final String uploadType) {
                    /*SL:2157*/return (BatchUpdate)super.setUploadType(uploadType);
                }
                
                public BatchUpdate setUploadProtocol(final String uploadProtocol) {
                    /*SL:2162*/return (BatchUpdate)super.setUploadProtocol(uploadProtocol);
                }
                
                public String getSpreadsheetId() {
                    /*SL:2172*/return this.spreadsheetId;
                }
                
                public BatchUpdate setSpreadsheetId(final String spreadsheetId) {
                    /*SL:2177*/this.spreadsheetId = spreadsheetId;
                    /*SL:2178*/return this;
                }
                
                public BatchUpdate set(final String s, final Object o) {
                    /*SL:2183*/return (BatchUpdate)super.set(s, o);
                }
            }
            
            public class BatchUpdateByDataFilter extends SheetsRequest<BatchUpdateValuesByDataFilterResponse>
            {
                private static final String REST_PATH = "v4/spreadsheets/{spreadsheetId}/values:batchUpdateByDataFilter";
                @Key
                private String spreadsheetId;
                
                protected BatchUpdateByDataFilter(final String a1, final BatchUpdateValuesByDataFilterRequest batchUpdateValuesByDataFilterRequest) {
                    super(Values.this.this$1.this$0, "POST", "v4/spreadsheets/{spreadsheetId}/values:batchUpdateByDataFilter", batchUpdateValuesByDataFilterRequest, BatchUpdateValuesByDataFilterResponse.class);
                    this.spreadsheetId = Preconditions.<String>checkNotNull(a1, (Object)"Required parameter spreadsheetId must be specified.");
                }
                
                public BatchUpdateByDataFilter set$Xgafv(final String s) {
                    /*SL:2233*/return (BatchUpdateByDataFilter)super.set$Xgafv(s);
                }
                
                public BatchUpdateByDataFilter setAccessToken(final String accessToken) {
                    /*SL:2238*/return (BatchUpdateByDataFilter)super.setAccessToken(accessToken);
                }
                
                public BatchUpdateByDataFilter setAlt(final String alt) {
                    /*SL:2243*/return (BatchUpdateByDataFilter)super.setAlt(alt);
                }
                
                public BatchUpdateByDataFilter setCallback(final String callback) {
                    /*SL:2248*/return (BatchUpdateByDataFilter)super.setCallback(callback);
                }
                
                public BatchUpdateByDataFilter setFields(final String fields) {
                    /*SL:2253*/return (BatchUpdateByDataFilter)super.setFields(fields);
                }
                
                public BatchUpdateByDataFilter setKey(final String key) {
                    /*SL:2258*/return (BatchUpdateByDataFilter)super.setKey(key);
                }
                
                public BatchUpdateByDataFilter setOauthToken(final String oauthToken) {
                    /*SL:2263*/return (BatchUpdateByDataFilter)super.setOauthToken(oauthToken);
                }
                
                public BatchUpdateByDataFilter setPrettyPrint(final Boolean prettyPrint) {
                    /*SL:2268*/return (BatchUpdateByDataFilter)super.setPrettyPrint(prettyPrint);
                }
                
                public BatchUpdateByDataFilter setQuotaUser(final String quotaUser) {
                    /*SL:2273*/return (BatchUpdateByDataFilter)super.setQuotaUser(quotaUser);
                }
                
                public BatchUpdateByDataFilter setUploadType(final String uploadType) {
                    /*SL:2278*/return (BatchUpdateByDataFilter)super.setUploadType(uploadType);
                }
                
                public BatchUpdateByDataFilter setUploadProtocol(final String uploadProtocol) {
                    /*SL:2283*/return (BatchUpdateByDataFilter)super.setUploadProtocol(uploadProtocol);
                }
                
                public String getSpreadsheetId() {
                    /*SL:2293*/return this.spreadsheetId;
                }
                
                public BatchUpdateByDataFilter setSpreadsheetId(final String spreadsheetId) {
                    /*SL:2298*/this.spreadsheetId = spreadsheetId;
                    /*SL:2299*/return this;
                }
                
                public BatchUpdateByDataFilter set(final String s, final Object o) {
                    /*SL:2304*/return (BatchUpdateByDataFilter)super.set(s, o);
                }
            }
            
            public class Clear extends SheetsRequest<ClearValuesResponse>
            {
                private static final String REST_PATH = "v4/spreadsheets/{spreadsheetId}/values/{range}:clear";
                @Key
                private String spreadsheetId;
                @Key
                private String range;
                
                protected Clear(final String a1, final String a2, final ClearValuesRequest clearValuesRequest) {
                    super(Values.this.this$1.this$0, "POST", "v4/spreadsheets/{spreadsheetId}/values/{range}:clear", clearValuesRequest, ClearValuesResponse.class);
                    this.spreadsheetId = Preconditions.<String>checkNotNull(a1, (Object)"Required parameter spreadsheetId must be specified.");
                    this.range = Preconditions.<String>checkNotNull(a2, (Object)"Required parameter range must be specified.");
                }
                
                public Clear set$Xgafv(final String s) {
                    /*SL:2357*/return (Clear)super.set$Xgafv(s);
                }
                
                public Clear setAccessToken(final String accessToken) {
                    /*SL:2362*/return (Clear)super.setAccessToken(accessToken);
                }
                
                public Clear setAlt(final String alt) {
                    /*SL:2367*/return (Clear)super.setAlt(alt);
                }
                
                public Clear setCallback(final String callback) {
                    /*SL:2372*/return (Clear)super.setCallback(callback);
                }
                
                public Clear setFields(final String fields) {
                    /*SL:2377*/return (Clear)super.setFields(fields);
                }
                
                public Clear setKey(final String key) {
                    /*SL:2382*/return (Clear)super.setKey(key);
                }
                
                public Clear setOauthToken(final String oauthToken) {
                    /*SL:2387*/return (Clear)super.setOauthToken(oauthToken);
                }
                
                public Clear setPrettyPrint(final Boolean prettyPrint) {
                    /*SL:2392*/return (Clear)super.setPrettyPrint(prettyPrint);
                }
                
                public Clear setQuotaUser(final String quotaUser) {
                    /*SL:2397*/return (Clear)super.setQuotaUser(quotaUser);
                }
                
                public Clear setUploadType(final String uploadType) {
                    /*SL:2402*/return (Clear)super.setUploadType(uploadType);
                }
                
                public Clear setUploadProtocol(final String uploadProtocol) {
                    /*SL:2407*/return (Clear)super.setUploadProtocol(uploadProtocol);
                }
                
                public String getSpreadsheetId() {
                    /*SL:2417*/return this.spreadsheetId;
                }
                
                public Clear setSpreadsheetId(final String spreadsheetId) {
                    /*SL:2422*/this.spreadsheetId = spreadsheetId;
                    /*SL:2423*/return this;
                }
                
                public String getRange() {
                    /*SL:2433*/return this.range;
                }
                
                public Clear setRange(final String range) {
                    /*SL:2438*/this.range = range;
                    /*SL:2439*/return this;
                }
                
                public Clear set(final String s, final Object o) {
                    /*SL:2444*/return (Clear)super.set(s, o);
                }
            }
            
            public class Get extends SheetsRequest<ValueRange>
            {
                private static final String REST_PATH = "v4/spreadsheets/{spreadsheetId}/values/{range}";
                @Key
                private String spreadsheetId;
                @Key
                private String range;
                @Key
                private String valueRenderOption;
                @Key
                private String dateTimeRenderOption;
                @Key
                private String majorDimension;
                
                protected Get(final String a1, final String a2) {
                    super(Values.this.this$1.this$0, "GET", "v4/spreadsheets/{spreadsheetId}/values/{range}", (Object)null, ValueRange.class);
                    this.spreadsheetId = Preconditions.<String>checkNotNull(a1, (Object)"Required parameter spreadsheetId must be specified.");
                    this.range = Preconditions.<String>checkNotNull(a2, (Object)"Required parameter range must be specified.");
                }
                
                public HttpResponse executeUsingHead() throws IOException {
                    /*SL:2493*/return super.executeUsingHead();
                }
                
                public HttpRequest buildHttpRequestUsingHead() throws IOException {
                    /*SL:2498*/return super.buildHttpRequestUsingHead();
                }
                
                public Get set$Xgafv(final String s) {
                    /*SL:2503*/return (Get)super.set$Xgafv(s);
                }
                
                public Get setAccessToken(final String accessToken) {
                    /*SL:2508*/return (Get)super.setAccessToken(accessToken);
                }
                
                public Get setAlt(final String alt) {
                    /*SL:2513*/return (Get)super.setAlt(alt);
                }
                
                public Get setCallback(final String callback) {
                    /*SL:2518*/return (Get)super.setCallback(callback);
                }
                
                public Get setFields(final String fields) {
                    /*SL:2523*/return (Get)super.setFields(fields);
                }
                
                public Get setKey(final String key) {
                    /*SL:2528*/return (Get)super.setKey(key);
                }
                
                public Get setOauthToken(final String oauthToken) {
                    /*SL:2533*/return (Get)super.setOauthToken(oauthToken);
                }
                
                public Get setPrettyPrint(final Boolean prettyPrint) {
                    /*SL:2538*/return (Get)super.setPrettyPrint(prettyPrint);
                }
                
                public Get setQuotaUser(final String quotaUser) {
                    /*SL:2543*/return (Get)super.setQuotaUser(quotaUser);
                }
                
                public Get setUploadType(final String uploadType) {
                    /*SL:2548*/return (Get)super.setUploadType(uploadType);
                }
                
                public Get setUploadProtocol(final String uploadProtocol) {
                    /*SL:2553*/return (Get)super.setUploadProtocol(uploadProtocol);
                }
                
                public String getSpreadsheetId() {
                    /*SL:2563*/return this.spreadsheetId;
                }
                
                public Get setSpreadsheetId(final String spreadsheetId) {
                    /*SL:2568*/this.spreadsheetId = spreadsheetId;
                    /*SL:2569*/return this;
                }
                
                public String getRange() {
                    /*SL:2579*/return this.range;
                }
                
                public Get setRange(final String range) {
                    /*SL:2584*/this.range = range;
                    /*SL:2585*/return this;
                }
                
                public String getValueRenderOption() {
                    /*SL:2599*/return this.valueRenderOption;
                }
                
                public Get setValueRenderOption(final String valueRenderOption) {
                    /*SL:2607*/this.valueRenderOption = valueRenderOption;
                    /*SL:2608*/return this;
                }
                
                public String getDateTimeRenderOption() {
                    /*SL:2624*/return this.dateTimeRenderOption;
                }
                
                public Get setDateTimeRenderOption(final String dateTimeRenderOption) {
                    /*SL:2633*/this.dateTimeRenderOption = dateTimeRenderOption;
                    /*SL:2634*/return this;
                }
                
                public String getMajorDimension() {
                    /*SL:2654*/return this.majorDimension;
                }
                
                public Get setMajorDimension(final String majorDimension) {
                    /*SL:2665*/this.majorDimension = majorDimension;
                    /*SL:2666*/return this;
                }
                
                public Get set(final String s, final Object o) {
                    /*SL:2671*/return (Get)super.set(s, o);
                }
            }
            
            public class Update extends SheetsRequest<UpdateValuesResponse>
            {
                private static final String REST_PATH = "v4/spreadsheets/{spreadsheetId}/values/{range}";
                @Key
                private String spreadsheetId;
                @Key
                private String range;
                @Key
                private String responseValueRenderOption;
                @Key
                private String valueInputOption;
                @Key
                private String responseDateTimeRenderOption;
                @Key
                private Boolean includeValuesInResponse;
                
                protected Update(final String a1, final String a2, final ValueRange valueRange) {
                    super(Values.this.this$1.this$0, "PUT", "v4/spreadsheets/{spreadsheetId}/values/{range}", valueRange, UpdateValuesResponse.class);
                    this.spreadsheetId = Preconditions.<String>checkNotNull(a1, (Object)"Required parameter spreadsheetId must be specified.");
                    this.range = Preconditions.<String>checkNotNull(a2, (Object)"Required parameter range must be specified.");
                }
                
                public Update set$Xgafv(final String s) {
                    /*SL:2722*/return (Update)super.set$Xgafv(s);
                }
                
                public Update setAccessToken(final String accessToken) {
                    /*SL:2727*/return (Update)super.setAccessToken(accessToken);
                }
                
                public Update setAlt(final String alt) {
                    /*SL:2732*/return (Update)super.setAlt(alt);
                }
                
                public Update setCallback(final String callback) {
                    /*SL:2737*/return (Update)super.setCallback(callback);
                }
                
                public Update setFields(final String fields) {
                    /*SL:2742*/return (Update)super.setFields(fields);
                }
                
                public Update setKey(final String key) {
                    /*SL:2747*/return (Update)super.setKey(key);
                }
                
                public Update setOauthToken(final String oauthToken) {
                    /*SL:2752*/return (Update)super.setOauthToken(oauthToken);
                }
                
                public Update setPrettyPrint(final Boolean prettyPrint) {
                    /*SL:2757*/return (Update)super.setPrettyPrint(prettyPrint);
                }
                
                public Update setQuotaUser(final String quotaUser) {
                    /*SL:2762*/return (Update)super.setQuotaUser(quotaUser);
                }
                
                public Update setUploadType(final String uploadType) {
                    /*SL:2767*/return (Update)super.setUploadType(uploadType);
                }
                
                public Update setUploadProtocol(final String uploadProtocol) {
                    /*SL:2772*/return (Update)super.setUploadProtocol(uploadProtocol);
                }
                
                public String getSpreadsheetId() {
                    /*SL:2782*/return this.spreadsheetId;
                }
                
                public Update setSpreadsheetId(final String spreadsheetId) {
                    /*SL:2787*/this.spreadsheetId = spreadsheetId;
                    /*SL:2788*/return this;
                }
                
                public String getRange() {
                    /*SL:2798*/return this.range;
                }
                
                public Update setRange(final String range) {
                    /*SL:2803*/this.range = range;
                    /*SL:2804*/return this;
                }
                
                public String getResponseValueRenderOption() {
                    /*SL:2818*/return this.responseValueRenderOption;
                }
                
                public Update setResponseValueRenderOption(final String responseValueRenderOption) {
                    /*SL:2826*/this.responseValueRenderOption = responseValueRenderOption;
                    /*SL:2827*/return this;
                }
                
                public String getValueInputOption() {
                    /*SL:2837*/return this.valueInputOption;
                }
                
                public Update setValueInputOption(final String valueInputOption) {
                    /*SL:2842*/this.valueInputOption = valueInputOption;
                    /*SL:2843*/return this;
                }
                
                public String getResponseDateTimeRenderOption() {
                    /*SL:2859*/return this.responseDateTimeRenderOption;
                }
                
                public Update setResponseDateTimeRenderOption(final String responseDateTimeRenderOption) {
                    /*SL:2868*/this.responseDateTimeRenderOption = responseDateTimeRenderOption;
                    /*SL:2869*/return this;
                }
                
                public Boolean getIncludeValuesInResponse() {
                    /*SL:2887*/return this.includeValuesInResponse;
                }
                
                public Update setIncludeValuesInResponse(final Boolean includeValuesInResponse) {
                    /*SL:2897*/this.includeValuesInResponse = includeValuesInResponse;
                    /*SL:2898*/return this;
                }
                
                public Update set(final String s, final Object o) {
                    /*SL:2903*/return (Update)super.set(s, o);
                }
            }
        }
    }
    
    public static final class Builder extends AbstractGoogleJsonClient.Builder
    {
        public Builder(final HttpTransport a1, final JsonFactory a2, final HttpRequestInitializer a3) {
            super(a1, a2, "https://sheets.googleapis.com/", "", a3, false);
            this.setBatchPath("batch");
        }
        
        public Sheets build() {
            /*SL:2958*/return new Sheets(this);
        }
        
        public Builder setRootUrl(final String rootUrl) {
            /*SL:2963*/return (Builder)super.setRootUrl(rootUrl);
        }
        
        public Builder setServicePath(final String servicePath) {
            /*SL:2968*/return (Builder)super.setServicePath(servicePath);
        }
        
        public Builder setBatchPath(final String batchPath) {
            /*SL:2973*/return (Builder)super.setBatchPath(batchPath);
        }
        
        public Builder setHttpRequestInitializer(final HttpRequestInitializer httpRequestInitializer) {
            /*SL:2978*/return (Builder)super.setHttpRequestInitializer(httpRequestInitializer);
        }
        
        public Builder setApplicationName(final String applicationName) {
            /*SL:2983*/return (Builder)super.setApplicationName(applicationName);
        }
        
        public Builder setSuppressPatternChecks(final boolean suppressPatternChecks) {
            /*SL:2988*/return (Builder)super.setSuppressPatternChecks(suppressPatternChecks);
        }
        
        public Builder setSuppressRequiredParameterChecks(final boolean suppressRequiredParameterChecks) {
            /*SL:2993*/return (Builder)super.setSuppressRequiredParameterChecks(suppressRequiredParameterChecks);
        }
        
        public Builder setSuppressAllChecks(final boolean suppressAllChecks) {
            /*SL:2998*/return (Builder)super.setSuppressAllChecks(suppressAllChecks);
        }
        
        public Builder setSheetsRequestInitializer(final SheetsRequestInitializer googleClientRequestInitializer) {
            /*SL:3008*/return (Builder)super.setGoogleClientRequestInitializer(googleClientRequestInitializer);
        }
        
        public Builder setGoogleClientRequestInitializer(final GoogleClientRequestInitializer googleClientRequestInitializer) {
            /*SL:3014*/return (Builder)super.setGoogleClientRequestInitializer(googleClientRequestInitializer);
        }
    }
}

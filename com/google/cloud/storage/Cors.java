package com.google.cloud.storage;

import java.net.URISyntaxException;
import java.net.URI;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Iterables;
import com.google.common.base.Functions;
import java.util.Objects;
import java.util.List;
import com.google.common.collect.ImmutableList;
import com.google.api.services.storage.model.Bucket;
import com.google.common.base.Function;
import java.io.Serializable;

public final class Cors implements Serializable
{
    private static final long serialVersionUID = -8637770919343335655L;
    static final Function<Bucket.Cors, Cors> FROM_PB_FUNCTION;
    static final Function<Cors, Bucket.Cors> TO_PB_FUNCTION;
    private final Integer maxAgeSeconds;
    private final ImmutableList<HttpMethod> methods;
    private final ImmutableList<Origin> origins;
    private final ImmutableList<String> responseHeaders;
    
    private Cors(final Builder a1) {
        this.maxAgeSeconds = a1.maxAgeSeconds;
        this.methods = a1.methods;
        this.origins = a1.origins;
        this.responseHeaders = a1.responseHeaders;
    }
    
    public Integer getMaxAgeSeconds() {
        /*SL:177*/return this.maxAgeSeconds;
    }
    
    public List<HttpMethod> getMethods() {
        /*SL:182*/return this.methods;
    }
    
    public List<Origin> getOrigins() {
        /*SL:187*/return this.origins;
    }
    
    public List<String> getResponseHeaders() {
        /*SL:192*/return this.responseHeaders;
    }
    
    public Builder toBuilder() {
        /*SL:197*/return newBuilder().setMaxAgeSeconds(this.maxAgeSeconds).setMethods(/*EL:198*/this.methods).setOrigins(/*EL:199*/this.origins).setResponseHeaders(/*EL:200*/this.responseHeaders);
    }
    
    @Override
    public int hashCode() {
        /*SL:206*/return Objects.hash(this.maxAgeSeconds, this.methods, this.origins, this.responseHeaders);
    }
    
    @Override
    public boolean equals(final Object a1) {
        /*SL:211*/if (!(a1 instanceof Cors)) {
            /*SL:212*/return false;
        }
        final Cors v1 = /*EL:214*/(Cors)a1;
        /*SL:215*/return Objects.equals(this.maxAgeSeconds, v1.maxAgeSeconds) && /*EL:216*/Objects.equals(this.methods, v1.methods) && /*EL:217*/Objects.equals(this.origins, v1.origins) && /*EL:218*/Objects.equals(this.responseHeaders, v1.responseHeaders);
    }
    
    public static Builder newBuilder() {
        /*SL:223*/return new Builder();
    }
    
    Bucket.Cors toPb() {
        final Bucket.Cors v1 = /*EL:227*/new Bucket.Cors();
        /*SL:228*/v1.setMaxAgeSeconds(this.maxAgeSeconds);
        /*SL:229*/v1.setResponseHeader((List)this.responseHeaders);
        /*SL:230*/if (this.methods != null) {
            /*SL:231*/v1.setMethod((List)Lists.<Object>newArrayList(Iterables.<HttpMethod, ?>transform((Iterable<HttpMethod>)this.methods, (Function<? super HttpMethod, ?>)Functions.toStringFunction())));
        }
        /*SL:233*/if (this.origins != null) {
            /*SL:234*/v1.setOrigin((List)Lists.<Object>newArrayList(Iterables.<Origin, ?>transform((Iterable<Origin>)this.origins, (Function<? super Origin, ?>)Functions.toStringFunction())));
        }
        /*SL:236*/return v1;
    }
    
    static Cors fromPb(final Bucket.Cors a1) {
        final Builder v1 = newBuilder().setMaxAgeSeconds(/*EL:240*/a1.getMaxAgeSeconds());
        /*SL:241*/if (a1.getMethod() != null) {
            /*SL:242*/v1.setMethods(/*EL:243*/Iterables.<Object, HttpMethod>transform((Iterable<Object>)a1.getMethod(), /*EL:244*/(Function<? super Object, ? extends HttpMethod>)new Function<String, HttpMethod>() {
                @Override
                public HttpMethod apply(final String a1) {
                    /*SL:248*/return HttpMethod.valueOf(a1.toUpperCase());
                }
            }));
        }
        /*SL:252*/if (a1.getOrigin() != null) {
            /*SL:253*/v1.setOrigins(/*EL:254*/Iterables.<Object, Origin>transform((Iterable<Object>)a1.getOrigin(), /*EL:255*/(Function<? super Object, ? extends Origin>)new Function<String, Origin>() {
                @Override
                public Origin apply(final String a1) {
                    /*SL:259*/return Origin.of(a1);
                }
            }));
        }
        /*SL:263*/v1.setResponseHeaders(a1.getResponseHeader());
        /*SL:264*/return v1.build();
    }
    
    static {
        FROM_PB_FUNCTION = new Function<Bucket.Cors, Cors>() {
            @Override
            public Cors apply(final Bucket.Cors a1) {
                /*SL:47*/return Cors.fromPb(a1);
            }
        };
        TO_PB_FUNCTION = new Function<Cors, Bucket.Cors>() {
            @Override
            public Bucket.Cors apply(final Cors a1) {
                /*SL:55*/return a1.toPb();
            }
        };
    }
    
    public static final class Origin implements Serializable
    {
        private static final long serialVersionUID = -4447958124895577993L;
        private static final String ANY_URI = "*";
        private final String value;
        private static final Origin ANY;
        
        private Origin(final String a1) {
            this.value = Preconditions.<String>checkNotNull(a1);
        }
        
        public static Origin any() {
            /*SL:79*/return Origin.ANY;
        }
        
        public static Origin of(final String a2, final String a3, final int v1) {
            try {
                /*SL:85*/return of(new URI(a2, null, a3, v1, null, null, null).toString());
            }
            catch (URISyntaxException a4) {
                /*SL:87*/throw new IllegalArgumentException(a4);
            }
        }
        
        public static Origin of(final String a1) {
            /*SL:93*/if ("*".equals(a1)) {
                /*SL:94*/return any();
            }
            /*SL:96*/return new Origin(a1);
        }
        
        @Override
        public int hashCode() {
            /*SL:101*/return this.value.hashCode();
        }
        
        @Override
        public boolean equals(final Object a1) {
            /*SL:106*/return a1 instanceof Origin && /*EL:109*/this.value.equals(((Origin)a1).value);
        }
        
        @Override
        public String toString() {
            /*SL:114*/return this.getValue();
        }
        
        public String getValue() {
            /*SL:118*/return this.value;
        }
        
        static {
            ANY = new Origin("*");
        }
    }
    
    public static final class Builder
    {
        private Integer maxAgeSeconds;
        private ImmutableList<HttpMethod> methods;
        private ImmutableList<Origin> origins;
        private ImmutableList<String> responseHeaders;
        
        public Builder setMaxAgeSeconds(final Integer a1) {
            /*SL:137*/this.maxAgeSeconds = a1;
            /*SL:138*/return this;
        }
        
        public Builder setMethods(final Iterable<HttpMethod> a1) {
            /*SL:143*/this.methods = ((a1 != null) ? ImmutableList.<HttpMethod>copyOf((Iterable<? extends HttpMethod>)a1) : null);
            /*SL:144*/return this;
        }
        
        public Builder setOrigins(final Iterable<Origin> a1) {
            /*SL:149*/this.origins = ((a1 != null) ? ImmutableList.<Origin>copyOf((Iterable<? extends Origin>)a1) : null);
            /*SL:150*/return this;
        }
        
        public Builder setResponseHeaders(final Iterable<String> a1) {
            /*SL:155*/this.responseHeaders = ((a1 != null) ? ImmutableList.<String>copyOf((Iterable<? extends String>)a1) : null);
            /*SL:156*/return this;
        }
        
        public Cors build() {
            /*SL:161*/return new Cors(this, null);
        }
    }
}

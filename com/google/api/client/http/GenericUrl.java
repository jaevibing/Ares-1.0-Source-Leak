package com.google.api.client.http;

import com.google.api.client.util.escape.PercentEscaper;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.Set;
import java.util.Collections;
import java.util.Iterator;
import java.net.MalformedURLException;
import com.google.api.client.util.Preconditions;
import java.util.Collection;
import java.util.ArrayList;
import com.google.api.client.util.escape.CharEscapers;
import java.util.Locale;
import java.net.URL;
import java.net.URI;
import java.util.List;
import com.google.api.client.util.escape.Escaper;
import com.google.api.client.util.GenericData;

public class GenericUrl extends GenericData
{
    private static final Escaper URI_FRAGMENT_ESCAPER;
    private String scheme;
    private String host;
    private String userInfo;
    private int port;
    private List<String> pathParts;
    private String fragment;
    
    public GenericUrl() {
        this.port = -1;
    }
    
    public GenericUrl(final String a1) {
        this(parseURL(a1));
    }
    
    public GenericUrl(final URI a1) {
        this(a1.getScheme(), a1.getHost(), a1.getPort(), a1.getRawPath(), a1.getRawFragment(), a1.getRawQuery(), a1.getRawUserInfo());
    }
    
    public GenericUrl(final URL a1) {
        this(a1.getProtocol(), a1.getHost(), a1.getPort(), a1.getPath(), a1.getRef(), a1.getQuery(), a1.getUserInfo());
    }
    
    private GenericUrl(final String a1, final String a2, final int a3, final String a4, final String a5, final String a6, final String a7) {
        this.port = -1;
        this.scheme = a1.toLowerCase(Locale.US);
        this.host = a2;
        this.port = a3;
        this.pathParts = toPathParts(a4);
        this.fragment = ((a5 != null) ? CharEscapers.decodeUri(a5) : null);
        if (a6 != null) {
            UrlEncodedParser.parse(a6, this);
        }
        this.userInfo = ((a7 != null) ? CharEscapers.decodeUri(a7) : null);
    }
    
    @Override
    public int hashCode() {
        /*SL:173*/return this.build().hashCode();
    }
    
    @Override
    public boolean equals(final Object a1) {
        /*SL:178*/if (this == a1) {
            /*SL:179*/return true;
        }
        /*SL:181*/if (!super.equals(a1) || !(a1 instanceof GenericUrl)) {
            /*SL:182*/return false;
        }
        final GenericUrl v1 = /*EL:184*/(GenericUrl)a1;
        /*SL:186*/return this.build().equals(v1.build());
    }
    
    @Override
    public String toString() {
        /*SL:191*/return this.build();
    }
    
    @Override
    public GenericUrl clone() {
        final GenericUrl v1 = /*EL:196*/(GenericUrl)super.clone();
        /*SL:197*/if (this.pathParts != null) {
            /*SL:198*/v1.pathParts = new ArrayList<String>(this.pathParts);
        }
        /*SL:200*/return v1;
    }
    
    @Override
    public GenericUrl set(final String a1, final Object a2) {
        /*SL:205*/return (GenericUrl)super.set(a1, a2);
    }
    
    public final String getScheme() {
        /*SL:214*/return this.scheme;
    }
    
    public final void setScheme(final String a1) {
        /*SL:223*/this.scheme = Preconditions.<String>checkNotNull(a1);
    }
    
    public String getHost() {
        /*SL:232*/return this.host;
    }
    
    public final void setHost(final String a1) {
        /*SL:241*/this.host = Preconditions.<String>checkNotNull(a1);
    }
    
    public final String getUserInfo() {
        /*SL:250*/return this.userInfo;
    }
    
    public final void setUserInfo(final String a1) {
        /*SL:259*/this.userInfo = a1;
    }
    
    public int getPort() {
        /*SL:268*/return this.port;
    }
    
    public final void setPort(final int a1) {
        /*SL:277*/Preconditions.checkArgument(a1 >= -1, (Object)"expected port >= -1");
        /*SL:278*/this.port = a1;
    }
    
    public List<String> getPathParts() {
        /*SL:288*/return this.pathParts;
    }
    
    public void setPathParts(final List<String> a1) {
        /*SL:308*/this.pathParts = a1;
    }
    
    public String getFragment() {
        /*SL:317*/return this.fragment;
    }
    
    public final void setFragment(final String a1) {
        /*SL:326*/this.fragment = a1;
    }
    
    public final String build() {
        /*SL:334*/return this.buildAuthority() + this.buildRelativeUrl();
    }
    
    public final String buildAuthority() {
        final StringBuilder v1 = /*EL:350*/new StringBuilder();
        /*SL:351*/v1.append(Preconditions.<String>checkNotNull(this.scheme));
        /*SL:352*/v1.append("://");
        /*SL:353*/if (this.userInfo != null) {
            /*SL:354*/v1.append(CharEscapers.escapeUriUserInfo(this.userInfo)).append('@');
        }
        /*SL:356*/v1.append(Preconditions.<String>checkNotNull(this.host));
        final int v2 = /*EL:357*/this.port;
        /*SL:358*/if (v2 != -1) {
            /*SL:359*/v1.append(':').append(v2);
        }
        /*SL:361*/return v1.toString();
    }
    
    public final String buildRelativeUrl() {
        final StringBuilder v1 = /*EL:376*/new StringBuilder();
        /*SL:377*/if (this.pathParts != null) {
            /*SL:378*/this.appendRawPathFromParts(v1);
        }
        addQueryParams(/*EL:380*/this.entrySet(), v1);
        final String v2 = /*EL:383*/this.fragment;
        /*SL:384*/if (v2 != null) {
            /*SL:385*/v1.append('#').append(GenericUrl.URI_FRAGMENT_ESCAPER.escape(v2));
        }
        /*SL:387*/return v1.toString();
    }
    
    public final URI toURI() {
        /*SL:402*/return toURI(this.build());
    }
    
    public final URL toURL() {
        /*SL:417*/return parseURL(this.build());
    }
    
    public final URL toURL(final String v0) {
        try {
            final URL a1 = /*EL:434*/this.toURL();
            /*SL:435*/return new URL(a1, v0);
        }
        catch (MalformedURLException v) {
            /*SL:437*/throw new IllegalArgumentException(v);
        }
    }
    
    public Object getFirst(final String v-2) {
        final Object value = /*EL:448*/this.get(v-2);
        /*SL:449*/if (value instanceof Collection) {
            final Collection<Object> a1 = /*EL:451*/(Collection<Object>)value;
            final Iterator<Object> v1 = /*EL:452*/a1.iterator();
            /*SL:453*/return v1.hasNext() ? v1.next() : null;
        }
        /*SL:455*/return value;
    }
    
    public Collection<Object> getAll(final String v2) {
        final Object v3 = /*EL:465*/this.get(v2);
        /*SL:466*/if (v3 == null) {
            /*SL:467*/return Collections.<Object>emptySet();
        }
        /*SL:469*/if (v3 instanceof Collection) {
            final Collection<Object> a1 = /*EL:471*/(Collection<Object>)v3;
            /*SL:472*/return Collections.<Object>unmodifiableCollection((Collection<?>)a1);
        }
        /*SL:474*/return Collections.<Object>singleton(v3);
    }
    
    public String getRawPath() {
        final List<String> v1 = /*EL:484*/this.pathParts;
        /*SL:485*/if (v1 == null) {
            /*SL:486*/return null;
        }
        final StringBuilder v2 = /*EL:488*/new StringBuilder();
        /*SL:489*/this.appendRawPathFromParts(v2);
        /*SL:490*/return v2.toString();
    }
    
    public void setRawPath(final String a1) {
        /*SL:499*/this.pathParts = toPathParts(a1);
    }
    
    public void appendRawPath(final String v0) {
        /*SL:514*/if (v0 != null && v0.length() != 0) {
            final List<String> v = toPathParts(/*EL:515*/v0);
            /*SL:516*/if (this.pathParts == null || this.pathParts.isEmpty()) {
                /*SL:517*/this.pathParts = v;
            }
            else {
                final int a1 = /*EL:519*/this.pathParts.size();
                /*SL:520*/this.pathParts.set(a1 - 1, this.pathParts.get(a1 - 1) + v.get(0));
                /*SL:521*/this.pathParts.addAll(v.subList(1, v.size()));
            }
        }
    }
    
    public static List<String> toPathParts(final String v-3) {
        /*SL:536*/if (v-3 == null || v-3.length() == 0) {
            /*SL:537*/return null;
        }
        final List<String> list = /*EL:539*/new ArrayList<String>();
        int n = /*EL:540*/0;
        boolean v0 = /*EL:541*/true;
        /*SL:542*/while (v0) {
            final int v = /*EL:543*/v-3.indexOf(47, n);
            /*SL:544*/v0 = (v != -1);
            final String v2;
            /*SL:546*/if (v0) {
                final String a1 = /*EL:547*/v-3.substring(n, v);
            }
            else {
                /*SL:549*/v2 = v-3.substring(n);
            }
            /*SL:551*/list.add(CharEscapers.decodeUri(v2));
            /*SL:552*/n = v + 1;
        }
        /*SL:554*/return list;
    }
    
    private void appendRawPathFromParts(final StringBuilder v-1) {
        /*SL:559*/for (int v0 = this.pathParts.size(), v = 0; v < v0; ++v) {
            final String a1 = /*EL:560*/this.pathParts.get(v);
            /*SL:561*/if (v != 0) {
                /*SL:562*/v-1.append('/');
            }
            /*SL:564*/if (a1.length() != 0) {
                /*SL:565*/v-1.append(CharEscapers.escapeUriPath(a1));
            }
        }
    }
    
    static void addQueryParams(final Set<Map.Entry<String, Object>> v-5, final StringBuilder v-4) {
        boolean b = /*EL:575*/true;
        /*SL:576*/for (final Map.Entry<String, Object> entry : v-5) {
            final Object v0 = /*EL:577*/entry.getValue();
            /*SL:578*/if (v0 != null) {
                final String v = /*EL:579*/CharEscapers.escapeUriQuery(entry.getKey());
                /*SL:580*/if (v0 instanceof Collection) {
                    Collection<?> a2 = /*EL:581*/(Collection<?>)v0;
                    final Iterator<?> iterator2 = /*EL:582*/a2.iterator();
                    while (iterator2.hasNext()) {
                        a2 = iterator2.next();
                        /*SL:583*/b = appendParam(b, v-4, v, a2);
                    }
                }
                else {
                    /*SL:586*/b = appendParam(b, v-4, v, v0);
                }
            }
        }
    }
    
    private static boolean appendParam(boolean a1, final StringBuilder a2, final String a3, final Object a4) {
        /*SL:593*/if (a1) {
            /*SL:594*/a1 = false;
            /*SL:595*/a2.append('?');
        }
        else {
            /*SL:597*/a2.append('&');
        }
        /*SL:599*/a2.append(a3);
        final String v1 = /*EL:600*/CharEscapers.escapeUriQuery(a4.toString());
        /*SL:601*/if (v1.length() != 0) {
            /*SL:602*/a2.append('=').append(v1);
        }
        /*SL:604*/return a1;
    }
    
    private static URI toURI(final String v1) {
        try {
            /*SL:619*/return new URI(v1);
        }
        catch (URISyntaxException a1) {
            /*SL:621*/throw new IllegalArgumentException(a1);
        }
    }
    
    private static URL parseURL(final String v1) {
        try {
            /*SL:637*/return new URL(v1);
        }
        catch (MalformedURLException a1) {
            /*SL:639*/throw new IllegalArgumentException(a1);
        }
    }
    
    static {
        URI_FRAGMENT_ESCAPER = new PercentEscaper("=&-_.!~*'()@:$,;/?:", false);
    }
}

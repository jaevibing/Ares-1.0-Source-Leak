package com.fasterxml.jackson.core.util;

import java.io.InputStream;
import java.io.Closeable;
import java.io.IOException;
import java.util.Properties;
import com.fasterxml.jackson.core.Versioned;
import com.fasterxml.jackson.core.Version;
import java.util.regex.Pattern;

public class VersionUtil
{
    private static final Pattern V_SEP;
    
    @Deprecated
    public Version version() {
        /*SL:39*/return Version.unknownVersion();
    }
    
    public static Version versionFor(final Class<?> a1) {
        final Version v1 = packageVersionFor(/*EL:58*/a1);
        /*SL:59*/return (v1 == null) ? Version.unknownVersion() : v1;
    }
    
    public static Version packageVersionFor(final Class<?> v-1) {
        Version v0 = /*EL:71*/null;
        try {
            final String v = /*EL:73*/v-1.getPackage().getName() + ".PackageVersion";
            final Class<?> v2 = /*EL:74*/Class.forName(v, true, v-1.getClassLoader());
            try {
                /*SL:77*/v0 = ((Versioned)v2.getDeclaredConstructor((Class<?>[])new Class[0]).newInstance(new Object[0])).version();
            }
            catch (Exception a1) {
                /*SL:79*/throw new IllegalArgumentException("Failed to get Versioned out of " + v2);
            }
        }
        catch (Exception ex) {}
        /*SL:84*/return (v0 == null) ? Version.unknownVersion() : v0;
    }
    
    @Deprecated
    public static Version mavenVersionFor(final ClassLoader v-6, final String v-5, final String v-4) {
        final InputStream resourceAsStream = /*EL:105*/v-6.getResourceAsStream("META-INF/maven/" + v-5.replaceAll("\\.", "/") + "/" + v-4 + "/pom.properties");
        /*SL:107*/if (resourceAsStream != null) {
            try {
                final Properties a1 = /*EL:109*/new Properties();
                /*SL:110*/a1.load(resourceAsStream);
                final String a2 = /*EL:111*/a1.getProperty("version");
                final String a3 = /*EL:112*/a1.getProperty("artifactId");
                final String v1 = /*EL:113*/a1.getProperty("groupId");
                /*SL:118*/return parseVersion(a2, v1, a3);
            }
            catch (IOException ex) {}
            finally {
                _close(resourceAsStream);
            }
        }
        /*SL:121*/return Version.unknownVersion();
    }
    
    public static Version parseVersion(String a2, final String a3, final String v1) {
        /*SL:129*/if (a2 != null && (a2 = a2.trim()).length() > 0) {
            final String[] a4 = VersionUtil.V_SEP.split(/*EL:130*/a2);
            /*SL:131*/return new Version(parseVersionPart(a4[0]), (a4.length > 1) ? parseVersionPart(a4[1]) : 0, (a4.length > 2) ? parseVersionPart(a4[2]) : 0, (a4.length > 3) ? a4[3] : null, a3, v1);
        }
        /*SL:137*/return Version.unknownVersion();
    }
    
    protected static int parseVersionPart(final String v-1) {
        int v0 = /*EL:141*/0;
        /*SL:142*/for (int v = 0, v2 = v-1.length(); v < v2; ++v) {
            final char a1 = /*EL:143*/v-1.charAt(v);
            /*SL:144*/if (a1 > '9') {
                break;
            }
            if (a1 < '0') {
                break;
            }
            /*SL:145*/v0 = v0 * 10 + (a1 - '0');
        }
        /*SL:147*/return v0;
    }
    
    private static final void _close(final Closeable v1) {
        try {
            /*SL:152*/v1.close();
        }
        catch (IOException ex) {}
    }
    
    public static final void throwInternal() {
        /*SL:163*/throw new RuntimeException("Internal error: this code path should never get executed");
    }
    
    static {
        V_SEP = Pattern.compile("[-_./;:]");
    }
}

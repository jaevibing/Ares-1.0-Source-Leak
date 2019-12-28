package org.reflections.vfs;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.reflections.ReflectionsException;
import java.net.MalformedURLException;
import java.io.IOException;
import org.reflections.Reflections;
import java.util.jar.JarFile;
import java.net.URL;
import java.io.File;
import com.google.common.base.Predicate;

public class UrlTypeVFS implements Vfs.UrlType
{
    public static final String[] REPLACE_EXTENSION;
    final String VFSZIP = "vfszip";
    final String VFSFILE = "vfsfile";
    Predicate<java.io.File> realFile;
    
    public UrlTypeVFS() {
        this.realFile = new Predicate<java.io.File>() {
            @Override
            public boolean apply(final java.io.File a1) {
                /*SL:90*/return a1.exists() && a1.isFile();
            }
        };
    }
    
    @Override
    public boolean matches(final URL a1) {
        /*SL:33*/return "vfszip".equals(a1.getProtocol()) || "vfsfile".equals(a1.getProtocol());
    }
    
    @Override
    public Vfs.Dir createDir(final URL v-1) {
        try {
            final URL a1 = /*EL:38*/this.adaptURL(v-1);
            /*SL:39*/return new ZipDir(new JarFile(a1.getFile()));
        }
        catch (Exception v0) {
            try {
                /*SL:42*/return new ZipDir(new JarFile(v-1.getFile()));
            }
            catch (IOException v) {
                /*SL:44*/if (Reflections.log != null) {
                    Reflections.log.warn(/*EL:45*/"Could not get URL", (Throwable)v0);
                    Reflections.log.warn(/*EL:46*/"Could not get URL", (Throwable)v);
                }
                /*SL:50*/return null;
            }
        }
    }
    
    public URL adaptURL(final URL a1) throws MalformedURLException {
        /*SL:54*/if ("vfszip".equals(a1.getProtocol())) {
            /*SL:55*/return this.replaceZipSeparators(a1.getPath(), this.realFile);
        }
        /*SL:56*/if ("vfsfile".equals(a1.getProtocol())) {
            /*SL:57*/return new URL(a1.toString().replace("vfsfile", "file"));
        }
        /*SL:59*/return a1;
    }
    
    URL replaceZipSeparators(final String v1, final Predicate<java.io.File> v2) throws MalformedURLException {
        int v3 = /*EL:65*/0;
        /*SL:66*/while (v3 != -1) {
            /*SL:67*/v3 = this.findFirstMatchOfDeployableExtention(v1, v3);
            /*SL:69*/if (v3 > 0) {
                final java.io.File a1 = /*EL:70*/new java.io.File(v1.substring(0, v3 - 1));
                /*SL:71*/if (v2.apply(a1)) {
                    return this.replaceZipSeparatorStartingFrom(v1, v3);
                }
                /*SL:72*/continue;
            }
        }
        /*SL:75*/throw new ReflectionsException("Unable to identify the real zip file in path '" + v1 + "'.");
    }
    
    int findFirstMatchOfDeployableExtention(final String a1, final int a2) {
        final Pattern v1 = /*EL:79*/Pattern.compile("\\.[ejprw]ar/");
        final Matcher v2 = /*EL:80*/v1.matcher(a1);
        /*SL:81*/if (v2.find(a2)) {
            /*SL:82*/return v2.end();
        }
        /*SL:84*/return -1;
    }
    
    URL replaceZipSeparatorStartingFrom(final String v2, final int v3) throws MalformedURLException {
        final String v4 = /*EL:96*/v2.substring(0, v3 - 1);
        String v5 = /*EL:97*/v2.substring(v3);
        int v6 = /*EL:99*/1;
        /*SL:100*/for (final String a1 : UrlTypeVFS.REPLACE_EXTENSION) {
            /*SL:101*/while (v5.contains(a1)) {
                /*SL:102*/v5 = v5.replace(a1, a1.substring(0, 4) + "!");
                /*SL:103*/++v6;
            }
        }
        String v7 = /*EL:107*/"";
        /*SL:108*/for (int a2 = 0; a2 < v6; ++a2) {
            /*SL:109*/v7 += "zip:";
        }
        /*SL:112*/if (v5.trim().length() == 0) {
            /*SL:113*/return new URL(v7 + "/" + v4);
        }
        /*SL:115*/return new URL(v7 + "/" + v4 + "!" + v5);
    }
    
    static {
        REPLACE_EXTENSION = new String[] { ".ear/", ".jar/", ".war/", ".sar/", ".har/", ".par/" };
    }
}

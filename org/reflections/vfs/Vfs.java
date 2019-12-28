package org.reflections.vfs;

import java.io.IOException;
import java.io.InputStream;
import org.reflections.util.ClasspathHelper;
import java.net.URLConnection;
import java.net.JarURLConnection;
import java.util.jar.JarFile;
import javax.annotation.Nullable;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URISyntaxException;
import java.io.File;
import com.google.common.collect.Iterables;
import java.util.ArrayList;
import org.reflections.util.Utils;
import com.google.common.base.Predicate;
import java.util.Collection;
import com.google.common.collect.Lists;
import java.util.Iterator;
import org.reflections.ReflectionsException;
import org.reflections.Reflections;
import java.net.URL;
import java.util.List;

public abstract class Vfs
{
    private static List<UrlType> defaultUrlTypes;
    
    public static List<UrlType> getDefaultUrlTypes() {
        /*SL:76*/return Vfs.defaultUrlTypes;
    }
    
    public static void setDefaultURLTypes(final List<UrlType> a1) {
        Vfs.defaultUrlTypes = /*EL:81*/a1;
    }
    
    public static void addDefaultURLTypes(final UrlType a1) {
        Vfs.defaultUrlTypes.add(/*EL:86*/0, a1);
    }
    
    public static Dir fromURL(final URL a1) {
        /*SL:91*/return fromURL(a1, Vfs.defaultUrlTypes);
    }
    
    public static Dir fromURL(final URL v-2, final List<UrlType> v-1) {
        /*SL:96*/for (final UrlType v1 : v-1) {
            try {
                /*SL:98*/if (!v1.matches(v-2)) {
                    continue;
                }
                final Dir a1 = /*EL:99*/v1.createDir(v-2);
                /*SL:100*/if (a1 != null) {
                    return a1;
                }
                continue;
            }
            catch (Throwable a2) {
                /*SL:103*/if (Reflections.log == null) {
                    continue;
                }
                Reflections.log.warn(/*EL:104*/"could not create Dir using " + v1 + " from url " + v-2.toExternalForm() + ". skipping.", a2);
            }
        }
        /*SL:109*/throw new ReflectionsException("could not create Vfs.Dir from url, no matching UrlType was found [" + v-2.toExternalForm() + "]\neither use fromURL(final URL url, final List<UrlType> urlTypes) or use the static setDefaultURLTypes(final List<UrlType> urlTypes) or addDefaultURLTypes(UrlType urlType) with your specialized UrlType.");
    }
    
    public static Dir fromURL(final URL a1, final UrlType... a2) {
        /*SL:117*/return fromURL(a1, Lists.<UrlType>newArrayList(a2));
    }
    
    public static Iterable<File> findFiles(final Collection<URL> a1, final String a2, final Predicate<String> a3) {
        final Predicate<File> v1 = /*EL:122*/new Predicate<File>() {
            @Override
            public boolean apply(final File v2) {
                final String v3 = /*EL:124*/v2.getRelativePath();
                /*SL:125*/if (v3.startsWith(a2)) {
                    final String a1 = /*EL:126*/v3.substring(v3.indexOf(a2) + a2.length());
                    /*SL:127*/return !Utils.isEmpty(a1) && a3.apply(a1.substring(1));
                }
                /*SL:129*/return false;
            }
        };
        /*SL:134*/return findFiles(a1, v1);
    }
    
    public static Iterable<File> findFiles(final Collection<URL> v1, final Predicate<File> v2) {
        Iterable<File> v3 = /*EL:139*/new ArrayList<File>();
        /*SL:141*/for (final Throwable a2 : v1) {
            try {
                /*SL:143*/v3 = Iterables.<File>concat((Iterable<? extends File>)v3, /*EL:144*/(Iterable<? extends File>)Iterables.<? extends T>filter((Iterable<? extends T>)new Iterable<File>() {
                    @Override
                    public Iterator<File> iterator() {
                        /*SL:146*/return Vfs.fromURL(a2).getFiles().iterator();
                    }
                }, (Predicate<? super T>)v2));
            }
            catch (Throwable a2) {
                /*SL:150*/if (Reflections.log == null) {
                    continue;
                }
                Reflections.log.error(/*EL:151*/"could not findFiles for url. continuing. [" + a2 + "]", a2);
            }
        }
        /*SL:156*/return v3;
    }
    
    @Nullable
    public static java.io.File getFile(final URL v-1) {
        try {
            final String v1 = /*EL:165*/v-1.toURI().getSchemeSpecificPart();
            final java.io.File a1;
            /*SL:166*/if ((a1 = new java.io.File(v1)).exists()) {
                return a1;
            }
        }
        catch (URISyntaxException ex) {}
        try {
            String v1 = /*EL:171*/URLDecoder.decode(v-1.getPath(), "UTF-8");
            /*SL:172*/if (v1.contains(".jar!")) {
                v1 = v1.substring(0, v1.lastIndexOf(".jar!") + ".jar".length());
            }
            final java.io.File v2;
            /*SL:173*/if ((v2 = new java.io.File(v1)).exists()) {
                return v2;
            }
        }
        catch (UnsupportedEncodingException ex2) {}
        try {
            String v1 = /*EL:179*/v-1.toExternalForm();
            /*SL:180*/if (v1.startsWith("jar:")) {
                v1 = v1.substring("jar:".length());
            }
            /*SL:181*/if (v1.startsWith("wsjar:")) {
                v1 = v1.substring("wsjar:".length());
            }
            /*SL:182*/if (v1.startsWith("file:")) {
                v1 = v1.substring("file:".length());
            }
            /*SL:183*/if (v1.contains(".jar!")) {
                v1 = v1.substring(0, v1.indexOf(".jar!") + ".jar".length());
            }
            java.io.File v2;
            /*SL:184*/if ((v2 = new java.io.File(v1)).exists()) {
                return v2;
            }
            /*SL:186*/v1 = v1.replace("%20", " ");
            /*SL:187*/if ((v2 = new java.io.File(v1)).exists()) {
                return v2;
            }
        }
        catch (Exception ex3) {}
        /*SL:192*/return null;
    }
    
    private static boolean hasJarFileInPath(final URL a1) {
        /*SL:196*/return a1.toExternalForm().matches(".*\\.jar(\\!.*|$)");
    }
    
    static {
        Vfs.defaultUrlTypes = (List<UrlType>)Lists.<DefaultUrlTypes>newArrayList(DefaultUrlTypes.values());
    }
    
    public enum DefaultUrlTypes implements UrlType
    {
        jarFile {
            @Override
            public boolean matches(final URL a1) {
                /*SL:212*/return a1.getProtocol().equals("file") && hasJarFileInPath(a1);
            }
            
            @Override
            public Dir createDir(final URL a1) throws Exception {
                /*SL:216*/return new ZipDir(new JarFile(Vfs.getFile(a1)));
            }
        }, 
        jarUrl {
            @Override
            public boolean matches(final URL a1) {
                /*SL:222*/return "jar".equals(a1.getProtocol()) || "zip".equals(a1.getProtocol()) || "wsjar".equals(a1.getProtocol());
            }
            
            @Override
            public Dir createDir(final URL v2) throws Exception {
                try {
                    final URLConnection a1 = /*EL:227*/v2.openConnection();
                    /*SL:228*/if (a1 instanceof JarURLConnection) {
                        /*SL:229*/return new ZipDir(((JarURLConnection)a1).getJarFile());
                    }
                }
                catch (Throwable t) {}
                final java.io.File v3 = /*EL:232*/Vfs.getFile(v2);
                /*SL:233*/if (v3 != null) {
                    /*SL:234*/return new ZipDir(new JarFile(v3));
                }
                /*SL:236*/return null;
            }
        }, 
        directory {
            @Override
            public boolean matches(final URL v2) {
                /*SL:242*/if (v2.getProtocol().equals("file") && !hasJarFileInPath(v2)) {
                    final java.io.File a1 = /*EL:243*/Vfs.getFile(v2);
                    /*SL:244*/return a1 != null && a1.isDirectory();
                }
                /*SL:245*/return false;
            }
            
            @Override
            public Dir createDir(final URL a1) throws Exception {
                /*SL:249*/return new SystemDir(Vfs.getFile(a1));
            }
        }, 
        jboss_vfs {
            @Override
            public boolean matches(final URL a1) {
                /*SL:255*/return a1.getProtocol().equals("vfs");
            }
            
            @Override
            public Dir createDir(final URL a1) throws Exception {
                final Object v1 = /*EL:259*/a1.openConnection().getContent();
                final Class<?> v2 = /*EL:260*/ClasspathHelper.contextClassLoader().loadClass("org.jboss.vfs.VirtualFile");
                final java.io.File v3 = /*EL:261*/(java.io.File)v2.getMethod("getPhysicalFile", (Class<?>[])new Class[0]).invoke(v1, new Object[0]);
                final String v4 = /*EL:262*/(String)v2.getMethod("getName", (Class<?>[])new Class[0]).invoke(v1, new Object[0]);
                java.io.File v5 = /*EL:263*/new java.io.File(v3.getParentFile(), v4);
                /*SL:264*/if (!v5.exists() || !v5.canRead()) {
                    v5 = v3;
                }
                /*SL:265*/return v5.isDirectory() ? new SystemDir(v5) : new ZipDir(new JarFile(v5));
            }
        }, 
        jboss_vfsfile {
            @Override
            public boolean matches(final URL a1) throws Exception {
                /*SL:271*/return "vfszip".equals(a1.getProtocol()) || "vfsfile".equals(a1.getProtocol());
            }
            
            @Override
            public Dir createDir(final URL a1) throws Exception {
                /*SL:275*/return new UrlTypeVFS().createDir(a1);
            }
        }, 
        bundle {
            @Override
            public boolean matches(final URL a1) throws Exception {
                /*SL:281*/return a1.getProtocol().startsWith("bundle");
            }
            
            @Override
            public Dir createDir(final URL a1) throws Exception {
                /*SL:285*/return Vfs.fromURL((URL)ClasspathHelper.contextClassLoader().loadClass("org.eclipse.core.runtime.FileLocator").getMethod(/*EL:286*/"resolve", URL.class).invoke(null, a1));
            }
        }, 
        jarInputStream {
            @Override
            public boolean matches(final URL a1) throws Exception {
                /*SL:292*/return a1.toExternalForm().contains(".jar");
            }
            
            @Override
            public Dir createDir(final URL a1) throws Exception {
                /*SL:296*/return new JarInputDir(a1);
            }
        };
    }
    
    public interface UrlType
    {
        boolean matches(URL p0) throws Exception;
        
        Dir createDir(URL p0) throws Exception;
    }
    
    public interface Dir
    {
        String getPath();
        
        Iterable<File> getFiles();
        
        void close();
    }
    
    public interface File
    {
        String getName();
        
        String getRelativePath();
        
        InputStream openInputStream() throws IOException;
    }
}

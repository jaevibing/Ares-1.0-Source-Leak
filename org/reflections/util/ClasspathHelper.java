package org.reflections.util;

import java.util.Map;
import java.util.LinkedHashMap;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.jar.Manifest;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.Iterator;
import java.util.Set;
import javax.servlet.ServletContext;
import java.io.File;
import java.util.Arrays;
import java.net.URLClassLoader;
import java.net.MalformedURLException;
import java.util.Enumeration;
import java.util.List;
import java.io.IOException;
import java.util.ArrayList;
import java.net.URL;
import java.util.Collection;
import org.reflections.Reflections;

public abstract class ClasspathHelper
{
    public static ClassLoader contextClassLoader() {
        /*SL:30*/return Thread.currentThread().getContextClassLoader();
    }
    
    public static ClassLoader staticClassLoader() {
        /*SL:40*/return Reflections.class.getClassLoader();
    }
    
    public static ClassLoader[] classLoaders(final ClassLoader... v-1) {
        /*SL:51*/if (v-1 != null && v-1.length != 0) {
            /*SL:52*/return v-1;
        }
        final ClassLoader a1 = contextClassLoader();
        final ClassLoader v1 = staticClassLoader();
        final ClassLoader[] array3;
        /*SL:55*/if (a1 != null) {
            if (v1 != null && a1 != v1) {
                final ClassLoader[] array2;
                final ClassLoader[] array = array2 = new ClassLoader[2];
                array[0] = a1;
                array[1] = v1;
            }
            else {
                array3 = new ClassLoader[] { a1 };
            }
        }
        else {
            final ClassLoader[] array2 = new ClassLoader[0];
        }
        return array3;
    }
    
    public static Collection<URL> forPackage(final String a1, final ClassLoader... a2) {
        /*SL:79*/return forResource(resourceName(a1), a2);
    }
    
    public static Collection<URL> forResource(final String v-7, final ClassLoader... v-6) {
        final List<URL> v3 = /*EL:97*/new ArrayList<URL>();
        final ClassLoader[] classLoaders;
        final ClassLoader[] array = /*EL:99*/classLoaders = classLoaders(v-6);
        for (final ClassLoader v0 : classLoaders) {
            try {
                final Enumeration<URL> v = /*EL:101*/v0.getResources(v-7);
                /*SL:102*/while (v.hasMoreElements()) {
                    final URL a1 = /*EL:103*/v.nextElement();
                    final int a2 = /*EL:104*/a1.toExternalForm().lastIndexOf(v-7);
                    /*SL:105*/if (a2 != -1) {
                        /*SL:107*/v3.add(new URL(a1, a1.toExternalForm().substring(0, a2)));
                    }
                    else {
                        /*SL:109*/v3.add(a1);
                    }
                }
            }
            catch (IOException v2) {
                /*SL:113*/if (Reflections.log != null) {
                    Reflections.log.error(/*EL:114*/"error getting resources for " + v-7, (Throwable)v2);
                }
            }
        }
        /*SL:118*/return distinctUrls(v3);
    }
    
    public static URL forClass(final Class<?> v-7, final ClassLoader... v-6) {
        final ClassLoader[] classLoaders = classLoaders(/*EL:132*/v-6);
        final String string = /*EL:133*/v-7.getName().replace(".", "/") + ".class";
        /*SL:134*/for (final ClassLoader v0 : classLoaders) {
            try {
                URL a2 = /*EL:136*/v0.getResource(string);
                /*SL:137*/if (a2 != null) {
                    /*SL:138*/a2 = a2.toExternalForm().substring(0, a2.toExternalForm().lastIndexOf(v-7.getPackage().getName().replace(".", "/")));
                    /*SL:139*/return new URL(a2);
                }
            }
            catch (MalformedURLException v) {
                /*SL:142*/if (Reflections.log != null) {
                    Reflections.log.warn(/*EL:143*/"Could not get URL", (Throwable)v);
                }
            }
        }
        /*SL:147*/return null;
    }
    
    public static Collection<URL> forClassLoader() {
        /*SL:161*/return forClassLoader(classLoaders(new ClassLoader[0]));
    }
    
    public static Collection<URL> forClassLoader(final ClassLoader... v-5) {
        final Collection<URL> v2 = /*EL:178*/new ArrayList<URL>();
        final ClassLoader[] classLoaders;
        final ClassLoader[] array = /*EL:180*/classLoaders = classLoaders(v-5);
        for (ClassLoader v1 : classLoaders) {
            /*SL:181*/while (v1 != null) {
                /*SL:182*/if (v1 instanceof URLClassLoader) {
                    final URL[] a1 = /*EL:183*/((URLClassLoader)v1).getURLs();
                    /*SL:184*/if (a1 != null) {
                        /*SL:185*/v2.addAll(Arrays.<URL>asList(a1));
                    }
                }
                /*SL:188*/v1 = v1.getParent();
            }
        }
        /*SL:191*/return distinctUrls(v2);
    }
    
    public static Collection<URL> forJavaClassPath() {
        final Collection<URL> v2 = /*EL:204*/new ArrayList<URL>();
        final String property = /*EL:205*/System.getProperty("java.class.path");
        /*SL:206*/if (property != null) {
            /*SL:207*/for (final String v0 : property.split(File.pathSeparator)) {
                try {
                    /*SL:209*/v2.add(new File(v0).toURI().toURL());
                }
                catch (Exception v) {
                    /*SL:211*/if (Reflections.log != null) {
                        Reflections.log.warn(/*EL:212*/"Could not get URL", (Throwable)v);
                    }
                }
            }
        }
        /*SL:217*/return distinctUrls(v2);
    }
    
    public static Collection<URL> forWebInfLib(final ServletContext v1) {
        final Collection<URL> v2 = /*EL:230*/new ArrayList<URL>();
        final Set<?> v3 = /*EL:231*/(Set<?>)v1.getResourcePaths("/WEB-INF/lib");
        /*SL:232*/if (v3 == null) {
            /*SL:233*/return v2;
        }
        /*SL:235*/for (final Object a1 : v3) {
            try {
                /*SL:237*/v2.add(v1.getResource((String)a1));
            }
            catch (MalformedURLException ex) {}
        }
        /*SL:240*/return distinctUrls(v2);
    }
    
    public static URL forWebInfClasses(final ServletContext v0) {
        try {
            final String v = /*EL:252*/v0.getRealPath("/WEB-INF/classes");
            /*SL:253*/if (v == null) {
                /*SL:258*/return v0.getResource("/WEB-INF/classes");
            }
            final File a1 = new File(v);
            if (a1.exists()) {
                return a1.toURL();
            }
        }
        catch (MalformedURLException ex) {}
        /*SL:261*/return null;
    }
    
    public static Collection<URL> forManifest() {
        /*SL:275*/return forManifest(forClassLoader());
    }
    
    public static Collection<URL> forManifest(final URL v-6) {
        final Collection<URL> v2 = /*EL:289*/new ArrayList<URL>();
        /*SL:290*/v2.add(v-6);
        try {
            final String cleanPath = cleanPath(/*EL:292*/v-6);
            final File file = /*EL:293*/new File(cleanPath);
            final JarFile jarFile = /*EL:294*/new JarFile(cleanPath);
            URL url = tryToGetValidUrl(/*EL:295*/file.getPath(), new File(cleanPath).getParent(), cleanPath);
            /*SL:296*/if (url != null) {
                v2.add(url);
            }
            final Manifest v0 = /*EL:297*/jarFile.getManifest();
            /*SL:298*/if (v0 != null) {
                final String v = /*EL:299*/v0.getMainAttributes().getValue(new Attributes.Name("Class-Path"));
                /*SL:300*/if (v != null) {
                    /*SL:301*/for (final String a1 : v.split(" ")) {
                        /*SL:302*/url = tryToGetValidUrl(file.getPath(), new File(cleanPath).getParent(), a1);
                        /*SL:303*/if (url != null) {
                            v2.add(url);
                        }
                    }
                }
            }
        }
        catch (IOException ex) {}
        /*SL:310*/return distinctUrls(v2);
    }
    
    public static Collection<URL> forManifest(final Iterable<URL> v1) {
        final Collection<URL> v2 = /*EL:326*/new ArrayList<URL>();
        /*SL:328*/for (final URL a1 : v1) {
            /*SL:329*/v2.addAll(forManifest(a1));
        }
        /*SL:331*/return distinctUrls(v2);
    }
    
    static URL tryToGetValidUrl(final String a1, final String a2, final String a3) {
        try {
            /*SL:337*/if (new File(a3).exists()) {
                /*SL:338*/return new File(a3).toURI().toURL();
            }
            /*SL:339*/if (new File(a2 + File.separator + a3).exists()) {
                /*SL:340*/return new File(a2 + File.separator + a3).toURI().toURL();
            }
            /*SL:341*/if (new File(a1 + File.separator + a3).exists()) {
                /*SL:342*/return new File(a1 + File.separator + a3).toURI().toURL();
            }
            /*SL:343*/if (new File(new URL(a3).getFile()).exists()) {
                /*SL:344*/return new File(new URL(a3).getFile()).toURI().toURL();
            }
        }
        catch (MalformedURLException ex) {}
        /*SL:348*/return null;
    }
    
    public static String cleanPath(final URL a1) {
        String v1 = /*EL:358*/a1.getPath();
        try {
            /*SL:360*/v1 = URLDecoder.decode(v1, "UTF-8");
        }
        catch (UnsupportedEncodingException ex) {}
        /*SL:362*/if (v1.startsWith("jar:")) {
            /*SL:363*/v1 = v1.substring("jar:".length());
        }
        /*SL:365*/if (v1.startsWith("file:")) {
            /*SL:366*/v1 = v1.substring("file:".length());
        }
        /*SL:368*/if (v1.endsWith("!/")) {
            /*SL:369*/v1 = v1.substring(0, v1.lastIndexOf("!/")) + "/";
        }
        /*SL:371*/return v1;
    }
    
    private static String resourceName(final String v1) {
        /*SL:375*/if (v1 != null) {
            String a1 = /*EL:376*/v1.replace(".", "/");
            /*SL:377*/a1 = a1.replace("\\", "/");
            /*SL:378*/if (a1.startsWith("/")) {
                /*SL:379*/a1 = a1.substring(1);
            }
            /*SL:381*/return a1;
        }
        /*SL:383*/return null;
    }
    
    private static Collection<URL> distinctUrls(final Collection<URL> v1) {
        final Map<String, URL> v2 = /*EL:388*/new LinkedHashMap<String, URL>(v1.size());
        /*SL:389*/for (final URL a1 : v1) {
            /*SL:390*/v2.put(a1.toExternalForm(), a1);
        }
        /*SL:392*/return v2.values();
    }
}

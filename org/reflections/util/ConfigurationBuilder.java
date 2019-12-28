package org.reflections.util;

import com.google.common.collect.ObjectArrays;
import org.reflections.serializers.XmlSerializer;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.Executors;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.reflections.adapters.JavaReflectionAdapter;
import org.reflections.adapters.JavassistAdapter;
import java.util.Iterator;
import java.util.List;
import java.util.Collection;
import org.reflections.ReflectionsException;
import org.reflections.Reflections;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import java.util.concurrent.ExecutorService;
import org.reflections.serializers.Serializer;
import javax.annotation.Nullable;
import com.google.common.base.Predicate;
import org.reflections.adapters.MetadataAdapter;
import java.net.URL;
import javax.annotation.Nonnull;
import org.reflections.scanners.Scanner;
import java.util.Set;
import org.reflections.Configuration;

public class ConfigurationBuilder implements Configuration
{
    @Nonnull
    private Set<Scanner> scanners;
    @Nonnull
    private Set<URL> urls;
    protected MetadataAdapter metadataAdapter;
    @Nullable
    private Predicate<String> inputsFilter;
    private Serializer serializer;
    @Nullable
    private ExecutorService executorService;
    @Nullable
    private ClassLoader[] classLoaders;
    private boolean expandSuperTypes;
    
    public ConfigurationBuilder() {
        this.expandSuperTypes = true;
        this.scanners = Sets.<Scanner>newHashSet(new TypeAnnotationsScanner(), new SubTypesScanner());
        this.urls = (Set<URL>)Sets.<Object>newHashSet();
    }
    
    public static ConfigurationBuilder build(@Nullable final Object... v-7) {
        final ConfigurationBuilder configurationBuilder = /*EL:76*/new ConfigurationBuilder();
        final List<Object> arrayList = /*EL:79*/Lists.<Object>newArrayList();
        /*SL:80*/if (v-7 != null) {
            /*SL:81*/for (final Object o : v-7) {
                /*SL:82*/if (o != null) {
                    /*SL:83*/if (o.getClass().isArray()) {
                        for (final Object a1 : (Object[])o) {
                            if (a1 != null) {
                                arrayList.add(a1);
                            }
                        }
                    }
                    else/*SL:84*/ if (o instanceof Iterable) {
                        for (final Object v1 : (Iterable)o) {
                            if (v1 != null) {
                                arrayList.add(v1);
                            }
                        }
                    }
                    else {
                        /*SL:85*/arrayList.add(o);
                    }
                }
            }
        }
        final List<ClassLoader> arrayList2 = /*EL:90*/(List<ClassLoader>)Lists.<Object>newArrayList();
        /*SL:91*/for (final Object next : arrayList) {
            if (next instanceof ClassLoader) {
                arrayList2.add((ClassLoader)next);
            }
        }
        final ClassLoader[] v-8 = /*EL:93*/(ClassLoader[])(arrayList2.isEmpty() ? null : ((ClassLoader[])arrayList2.<ClassLoader>toArray(new ClassLoader[arrayList2.size()])));
        final FilterBuilder a2 = /*EL:94*/new FilterBuilder();
        final List<Scanner> arrayList3 = /*EL:95*/(List<Scanner>)Lists.<Object>newArrayList();
        /*SL:97*/for (final Object v1 : arrayList) {
            /*SL:98*/if (v1 instanceof String) {
                /*SL:99*/configurationBuilder.addUrls(ClasspathHelper.forPackage((String)v1, v-8));
                /*SL:100*/a2.includePackage((String)v1);
            }
            else/*SL:102*/ if (v1 instanceof Class) {
                /*SL:103*/if (Scanner.class.isAssignableFrom((Class<?>)v1)) {
                    try {
                        /*SL:104*/configurationBuilder.addScanners(((Class)v1).newInstance());
                    }
                    catch (Exception ex) {}
                }
                /*SL:106*/configurationBuilder.addUrls(ClasspathHelper.forClass((Class<?>)v1, v-8));
                /*SL:107*/a2.includePackage((Class<?>)v1);
            }
            else/*SL:109*/ if (v1 instanceof Scanner) {
                arrayList3.add((Scanner)v1);
            }
            else/*SL:110*/ if (v1 instanceof URL) {
                configurationBuilder.addUrls((URL)v1);
            }
            else {
                /*SL:111*/if (v1 instanceof ClassLoader) {
                    continue;
                }
                /*SL:112*/if (v1 instanceof Predicate) {
                    a2.add((Predicate<String>)v1);
                }
                else/*SL:113*/ if (v1 instanceof ExecutorService) {
                    configurationBuilder.setExecutorService((ExecutorService)v1);
                }
                else {
                    /*SL:114*/if (Reflections.log != null) {
                        throw new ReflectionsException("could not use param " + v1);
                    }
                    continue;
                }
            }
        }
        /*SL:117*/if (configurationBuilder.getUrls().isEmpty()) {
            /*SL:118*/if (v-8 != null) {
                /*SL:119*/configurationBuilder.addUrls(ClasspathHelper.forClassLoader(v-8));
            }
            else {
                /*SL:121*/configurationBuilder.addUrls(ClasspathHelper.forClassLoader());
            }
        }
        /*SL:125*/configurationBuilder.filterInputsBy(a2);
        /*SL:126*/if (!arrayList3.isEmpty()) {
            configurationBuilder.setScanners((Scanner[])arrayList3.<Scanner>toArray(new Scanner[arrayList3.size()]));
        }
        /*SL:127*/if (!arrayList2.isEmpty()) {
            configurationBuilder.addClassLoaders(arrayList2);
        }
        /*SL:129*/return configurationBuilder;
    }
    
    public ConfigurationBuilder forPackages(final String... v2) {
        /*SL:133*/for (final String a1 : v2) {
            /*SL:134*/this.addUrls(ClasspathHelper.forPackage(a1, new ClassLoader[0]));
        }
        /*SL:136*/return this;
    }
    
    @Nonnull
    @Override
    public Set<Scanner> getScanners() {
        /*SL:141*/return this.scanners;
    }
    
    public ConfigurationBuilder setScanners(@Nonnull final Scanner... a1) {
        /*SL:146*/this.scanners.clear();
        /*SL:147*/return this.addScanners(a1);
    }
    
    public ConfigurationBuilder addScanners(final Scanner... a1) {
        /*SL:152*/this.scanners.addAll(Sets.<Scanner>newHashSet(a1));
        /*SL:153*/return this;
    }
    
    @Nonnull
    @Override
    public Set<URL> getUrls() {
        /*SL:158*/return this.urls;
    }
    
    public ConfigurationBuilder setUrls(@Nonnull final Collection<URL> a1) {
        /*SL:165*/this.urls = (Set<URL>)Sets.<Object>newHashSet((Iterable<?>)a1);
        /*SL:166*/return this;
    }
    
    public ConfigurationBuilder setUrls(final URL... a1) {
        /*SL:173*/this.urls = Sets.<URL>newHashSet(a1);
        /*SL:174*/return this;
    }
    
    public ConfigurationBuilder addUrls(final Collection<URL> a1) {
        /*SL:181*/this.urls.addAll(a1);
        /*SL:182*/return this;
    }
    
    public ConfigurationBuilder addUrls(final URL... a1) {
        /*SL:189*/this.urls.addAll(Sets.<URL>newHashSet(a1));
        /*SL:190*/return this;
    }
    
    @Override
    public MetadataAdapter getMetadataAdapter() {
        /*SL:197*/if (this.metadataAdapter != null) {
            return this.metadataAdapter;
        }
        try {
            /*SL:200*/return this.metadataAdapter = new JavassistAdapter();
        }
        catch (Throwable v1) {
            /*SL:202*/if (Reflections.log != null) {
                Reflections.log.warn(/*EL:203*/"could not create JavassistAdapter, using JavaReflectionAdapter", v1);
            }
            /*SL:204*/return this.metadataAdapter = new JavaReflectionAdapter();
        }
    }
    
    public ConfigurationBuilder setMetadataAdapter(final MetadataAdapter a1) {
        /*SL:211*/this.metadataAdapter = a1;
        /*SL:212*/return this;
    }
    
    @Nullable
    @Override
    public Predicate<String> getInputsFilter() {
        /*SL:217*/return this.inputsFilter;
    }
    
    public void setInputsFilter(@Nullable final Predicate<String> a1) {
        /*SL:223*/this.inputsFilter = a1;
    }
    
    public ConfigurationBuilder filterInputsBy(final Predicate<String> a1) {
        /*SL:229*/this.inputsFilter = a1;
        /*SL:230*/return this;
    }
    
    @Nullable
    @Override
    public ExecutorService getExecutorService() {
        /*SL:235*/return this.executorService;
    }
    
    public ConfigurationBuilder setExecutorService(@Nullable final ExecutorService a1) {
        /*SL:240*/this.executorService = a1;
        /*SL:241*/return this;
    }
    
    public ConfigurationBuilder useParallelExecutor() {
        /*SL:247*/return this.useParallelExecutor(Runtime.getRuntime().availableProcessors());
    }
    
    public ConfigurationBuilder useParallelExecutor(final int a1) {
        final ThreadFactory v1 = /*EL:254*/new ThreadFactoryBuilder().setDaemon(true).setNameFormat("org.reflections-scanner-%d").build();
        /*SL:255*/this.setExecutorService(Executors.newFixedThreadPool(a1, v1));
        /*SL:256*/return this;
    }
    
    @Override
    public Serializer getSerializer() {
        /*SL:260*/return (this.serializer != null) ? this.serializer : (this.serializer = new XmlSerializer());
    }
    
    public ConfigurationBuilder setSerializer(final Serializer a1) {
        /*SL:265*/this.serializer = a1;
        /*SL:266*/return this;
    }
    
    @Nullable
    @Override
    public ClassLoader[] getClassLoaders() {
        /*SL:272*/return this.classLoaders;
    }
    
    @Override
    public boolean shouldExpandSuperTypes() {
        /*SL:277*/return this.expandSuperTypes;
    }
    
    public ConfigurationBuilder setExpandSuperTypes(final boolean a1) {
        /*SL:285*/this.expandSuperTypes = a1;
        /*SL:286*/return this;
    }
    
    public void setClassLoaders(@Nullable final ClassLoader[] a1) {
        /*SL:291*/this.classLoaders = a1;
    }
    
    public ConfigurationBuilder addClassLoader(final ClassLoader a1) {
        /*SL:296*/return this.addClassLoaders(a1);
    }
    
    public ConfigurationBuilder addClassLoaders(final ClassLoader... a1) {
        /*SL:301*/this.classLoaders = ((this.classLoaders == null) ? a1 : ObjectArrays.<ClassLoader>concat(this.classLoaders, a1, ClassLoader.class));
        /*SL:302*/return this;
    }
    
    public ConfigurationBuilder addClassLoaders(final Collection<ClassLoader> a1) {
        /*SL:307*/return this.addClassLoaders((ClassLoader[])a1.<ClassLoader>toArray(new ClassLoader[a1.size()]));
    }
}

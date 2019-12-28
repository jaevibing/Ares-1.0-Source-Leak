package org.reflections;

import org.reflections.scanners.MemberUsageScanner;
import java.lang.reflect.Member;
import org.reflections.scanners.MethodParameterNamesScanner;
import java.util.regex.Pattern;
import org.reflections.scanners.ResourcesScanner;
import org.reflections.scanners.FieldAnnotationsScanner;
import java.lang.reflect.Field;
import java.lang.reflect.Constructor;
import org.reflections.scanners.MethodParameterScanner;
import org.reflections.scanners.MethodAnnotationsScanner;
import java.lang.reflect.Method;
import com.google.common.base.Predicates;
import com.google.common.collect.Iterables;
import java.lang.annotation.Inherited;
import org.reflections.scanners.TypeAnnotationsScanner;
import java.lang.annotation.Annotation;
import java.util.Arrays;
import com.google.common.collect.UnmodifiableIterator;
import com.google.common.collect.HashMultimap;
import java.util.Set;
import com.google.common.collect.Sets;
import org.reflections.scanners.SubTypesScanner;
import com.google.common.collect.Multimap;
import java.io.FileNotFoundException;
import java.io.FileInputStream;
import java.io.File;
import java.io.InputStream;
import java.util.Collection;
import org.reflections.util.Utils;
import java.io.IOException;
import org.reflections.util.ClasspathHelper;
import org.reflections.serializers.XmlSerializer;
import org.reflections.serializers.Serializer;
import org.reflections.util.FilterBuilder;
import com.google.common.base.Predicate;
import org.reflections.vfs.Vfs;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.Future;
import java.net.URL;
import com.google.common.collect.Lists;
import com.google.common.base.Joiner;
import org.reflections.util.ConfigurationBuilder;
import java.util.Iterator;
import org.reflections.scanners.Scanner;
import javax.annotation.Nullable;
import org.slf4j.Logger;

public class Reflections
{
    @Nullable
    public static Logger log;
    protected final transient Configuration configuration;
    protected Store store;
    
    public Reflections(final Configuration v2) {
        this.configuration = v2;
        this.store = new Store(v2);
        if (v2.getScanners() != null && !v2.getScanners().isEmpty()) {
            for (final Scanner a1 : v2.getScanners()) {
                a1.setConfiguration(v2);
                a1.setStore(this.store.getOrCreate(a1.getClass().getSimpleName()));
            }
            this.scan();
            if (v2.shouldExpandSuperTypes()) {
                this.expandSuperTypes();
            }
        }
    }
    
    public Reflections(final String a1, @Nullable final Scanner... a2) {
        this(new Object[] { a1, a2 });
    }
    
    public Reflections(final Object... a1) {
        this(ConfigurationBuilder.build(a1));
    }
    
    protected Reflections() {
        this.configuration = new ConfigurationBuilder();
        this.store = new Store(this.configuration);
    }
    
    protected void scan() {
        /*SL:178*/if (this.configuration.getUrls() == null || this.configuration.getUrls().isEmpty()) {
            /*SL:179*/if (Reflections.log != null) {
                Reflections.log.warn("given scan urls are empty. set urls in the configuration");
            }
            /*SL:180*/return;
        }
        /*SL:183*/if (Reflections.log != null && Reflections.log.isDebugEnabled()) {
            Reflections.log.debug(/*EL:184*/"going to scan these urls:\n" + Joiner.on("\n").join(this.configuration.getUrls()));
        }
        long currentTimeMillis = /*EL:187*/System.currentTimeMillis();
        int n = /*EL:188*/0;
        final ExecutorService executorService = /*EL:189*/this.configuration.getExecutorService();
        final List<Future<?>> arrayList = /*EL:190*/(List<Future<?>>)Lists.<Object>newArrayList();
        /*SL:192*/for (final URL v0 : this.configuration.getUrls()) {
            try {
                /*SL:194*/if (executorService != null) {
                    /*SL:195*/arrayList.add(executorService.submit(new Runnable() {
                        @Override
                        public void run() {
                            /*SL:197*/if (Reflections.log != null && Reflections.log.isDebugEnabled()) {
                                Reflections.log.debug("[" + Thread.currentThread().toString() + "] scanning " + /*EL:199*/v0);
                            }
                            Reflections.this.scan(v0);
                        }
                    }));
                }
                else {
                    /*SL:202*/this.scan(v0);
                }
                /*SL:204*/++n;
            }
            catch (ReflectionsException v) {
                /*SL:206*/if (Reflections.log == null || !Reflections.log.isWarnEnabled()) {
                    continue;
                }
                Reflections.log.warn("could not create Vfs.Dir from url. ignoring the exception and continuing", (Throwable)v);
            }
        }
        /*SL:211*/if (executorService != null) {
            /*SL:212*/for (final Future v2 : arrayList) {
                try {
                    /*SL:213*/v2.get();
                }
                catch (Exception v3) {
                    throw new RuntimeException(v3);
                }
            }
        }
        /*SL:217*/currentTimeMillis = System.currentTimeMillis() - currentTimeMillis;
        /*SL:220*/if (executorService != null) {
            /*SL:221*/executorService.shutdown();
        }
        /*SL:224*/if (Reflections.log != null) {
            int n2 = /*EL:225*/0;
            int v4 = /*EL:226*/0;
            /*SL:227*/for (final String v5 : this.store.keySet()) {
                /*SL:228*/n2 += this.store.get(v5).keySet().size();
                /*SL:229*/v4 += this.store.get(v5).size();
            }
            Reflections.log.info(/*EL:232*/String.format("Reflections took %d ms to scan %d urls, producing %d keys and %d values %s", currentTimeMillis, /*EL:233*/n, n2, v4, (executorService != null && executorService instanceof ThreadPoolExecutor) ? /*EL:235*/String.format("[using %d cores]", ((ThreadPoolExecutor)executorService).getMaximumPoolSize()) : ""));
        }
    }
    
    protected void scan(final URL v-8) {
        final Vfs.Dir fromURL = /*EL:240*/Vfs.fromURL(v-8);
        try {
            /*SL:243*/for (final Vfs.File file : fromURL.getFiles()) {
                final Predicate<String> inputsFilter = /*EL:245*/this.configuration.getInputsFilter();
                final String relativePath = /*EL:246*/file.getRelativePath();
                final String replace = /*EL:247*/relativePath.replace('/', '.');
                /*SL:248*/if (inputsFilter == null || inputsFilter.apply(relativePath) || inputsFilter.apply(replace)) {
                    Object scan = /*EL:249*/null;
                    /*SL:250*/for (final Scanner v1 : this.configuration.getScanners()) {
                        try {
                            /*SL:252*/if (!v1.acceptsInput(relativePath) && !v1.acceptResult(replace)) {
                                continue;
                            }
                            /*SL:253*/scan = v1.scan(file, scan);
                        }
                        catch (Exception a1) {
                            /*SL:256*/if (Reflections.log == null || !Reflections.log.isDebugEnabled()) {
                                continue;
                            }
                            Reflections.log.debug(/*EL:257*/"could not scan file " + file.getRelativePath() + " in url " + v-8.toExternalForm() + " with scanner " + v1.getClass().getSimpleName(), (Throwable)a1);
                        }
                    }
                }
            }
        }
        finally {
            /*SL:263*/fromURL.close();
        }
    }
    
    public static Reflections collect() {
        /*SL:272*/return collect("META-INF/reflections/", new FilterBuilder().include(".*-reflections.xml"), new Serializer[0]);
    }
    
    public static Reflections collect(final String v-12, final Predicate<String> v-11, @Nullable final Serializer... v-10) {
        final Serializer serializer = /*EL:284*/(v-10 != null && v-10.length == 1) ? v-10[0] : new XmlSerializer();
        final Collection<URL> forPackage = /*EL:286*/ClasspathHelper.forPackage(v-12, new ClassLoader[0]);
        /*SL:287*/if (forPackage.isEmpty()) {
            return null;
        }
        final long currentTimeMillis = /*EL:288*/System.currentTimeMillis();
        final Reflections reflections = /*EL:289*/new Reflections();
        final Iterable<Vfs.File> files = /*EL:290*/Vfs.findFiles(forPackage, v-12, v-11);
        /*SL:291*/for (final IOException a3 : files) {
            InputStream a2 = /*EL:292*/null;
            try {
                /*SL:294*/a2 = a3.openInputStream();
                /*SL:295*/reflections.merge(serializer.read(a2));
            }
            catch (IOException a3) {
                /*SL:297*/throw new ReflectionsException("could not merge " + a3, a3);
            }
            finally {
                /*SL:299*/Utils.close(a2);
            }
        }
        /*SL:303*/if (Reflections.log != null) {
            final Store store = /*EL:304*/reflections.getStore();
            int n = /*EL:305*/0;
            int n2 = /*EL:306*/0;
            /*SL:307*/for (final String v1 : store.keySet()) {
                /*SL:308*/n += store.get(v1).keySet().size();
                /*SL:309*/n2 += store.get(v1).size();
            }
            Reflections.log.info(/*EL:312*/String.format("Reflections took %d ms to collect %d url%s, producing %d keys and %d values [%s]", /*EL:313*/System.currentTimeMillis() - currentTimeMillis, forPackage.size(), (forPackage.size() > 1) ? "s" : "", n, n2, Joiner.on(", ").join(forPackage)));
        }
        /*SL:315*/return reflections;
    }
    
    public Reflections collect(final InputStream v2) {
        try {
            /*SL:323*/this.merge(this.configuration.getSerializer().read(v2));
            /*SL:324*/if (Reflections.log != null) {
                Reflections.log.info("Reflections collected metadata from input stream using serializer " + this.configuration.getSerializer().getClass().getName());
            }
        }
        catch (Exception a1) {
            /*SL:326*/throw new ReflectionsException("could not merge input stream", a1);
        }
        /*SL:329*/return this;
    }
    
    public Reflections collect(final File v2) {
        FileInputStream v3 = /*EL:336*/null;
        try {
            /*SL:338*/v3 = new FileInputStream(v2);
            /*SL:339*/return this.collect(v3);
        }
        catch (FileNotFoundException a1) {
            /*SL:341*/throw new ReflectionsException("could not obtain input stream from file " + v2, a1);
        }
        finally {
            /*SL:343*/Utils.close(v3);
        }
    }
    
    public Reflections merge(final Reflections v-4) {
        /*SL:351*/if (v-4.store != null) {
            /*SL:352*/for (final String s : v-4.store.keySet()) {
                final Multimap<String, String> value = /*EL:353*/v-4.store.get(s);
                /*SL:354*/for (final String v1 : value.keySet()) {
                    /*SL:355*/for (final String a1 : value.get(v1)) {
                        /*SL:356*/this.store.getOrCreate(s).put(v1, a1);
                    }
                }
            }
        }
        /*SL:361*/return this;
    }
    
    public void expandSuperTypes() {
        /*SL:376*/if (this.store.keySet().contains(index(SubTypesScanner.class))) {
            final Multimap<String, String> value = /*EL:377*/this.store.get(index(SubTypesScanner.class));
            final Sets.SetView<String> difference = /*EL:378*/Sets.<String>difference(value.keySet(), Sets.<Object>newHashSet((Iterable<?>)value.values()));
            final Multimap<String, String> create = /*EL:379*/(Multimap<String, String>)HashMultimap.<Object, Object>create();
            /*SL:380*/for (final String v0 : difference) {
                final Class<?> v = /*EL:381*/ReflectionUtils.forName(v0, new ClassLoader[0]);
                /*SL:382*/if (v != null) {
                    /*SL:383*/this.expandSupertypes(create, v0, v);
                }
            }
            /*SL:386*/value.putAll(create);
        }
    }
    
    private void expandSupertypes(final Multimap<String, String> a3, final String v1, final Class<?> v2) {
        /*SL:391*/for (final Class<?> a4 : ReflectionUtils.getSuperTypes(v2)) {
            /*SL:392*/if (a3.put(a4.getName(), v1)) {
                /*SL:393*/if (Reflections.log != null) {
                    Reflections.log.debug("expanded subtype {} -> {}", (Object)a4.getName(), (Object)v1);
                }
                /*SL:394*/this.expandSupertypes(a3, a4.getName(), a4);
            }
        }
    }
    
    public <T> Set<Class<? extends T>> getSubTypesOf(final Class<T> a1) {
        /*SL:405*/return (Set<Class<? extends T>>)Sets.<Object>newHashSet((Iterable<?>)ReflectionUtils.<Object>forNames(this.store.getAll(index(SubTypesScanner.class), /*EL:406*/Arrays.<String>asList(a1.getName())), this.loaders()));
    }
    
    public Set<Class<?>> getTypesAnnotatedWith(final Class<? extends Annotation> a1) {
        /*SL:418*/return this.getTypesAnnotatedWith(a1, false);
    }
    
    public Set<Class<?>> getTypesAnnotatedWith(final Class<? extends Annotation> a1, final boolean a2) {
        final Iterable<String> v1 = /*EL:431*/this.store.get(index(TypeAnnotationsScanner.class), a1.getName());
        final Iterable<String> v2 = /*EL:432*/this.getAllAnnotated(v1, a1.isAnnotationPresent(Inherited.class), a2);
        /*SL:433*/return (Set<Class<?>>)Sets.<Object>newHashSet(Iterables.<?>concat((Iterable<?>)ReflectionUtils.<Object>forNames(v1, this.loaders()), (Iterable<?>)ReflectionUtils.<Object>forNames(v2, this.loaders())));
    }
    
    public Set<Class<?>> getTypesAnnotatedWith(final Annotation a1) {
        /*SL:442*/return this.getTypesAnnotatedWith(a1, false);
    }
    
    public Set<Class<?>> getTypesAnnotatedWith(final Annotation a1, final boolean a2) {
        final Iterable<String> v1 = /*EL:451*/this.store.get(index(TypeAnnotationsScanner.class), a1.annotationType().getName());
        final Iterable<Class<?>> v2 = /*EL:452*/ReflectionUtils.<Class<?>>filter(ReflectionUtils.<Object>forNames(v1, this.loaders()), ReflectionUtils.<Object>withAnnotation(a1));
        final Iterable<String> v3 = /*EL:453*/this.getAllAnnotated(Utils.names(v2), a1.annotationType().isAnnotationPresent(Inherited.class), a2);
        /*SL:454*/return (Set<Class<?>>)Sets.<Object>newHashSet(Iterables.<?>concat((Iterable<?>)v2, (Iterable<?>)ReflectionUtils.<Object>forNames(ReflectionUtils.<String>filter(v3, Predicates.<Object>not(Predicates.<Object>in((Collection<?>)Sets.<Object>newHashSet((Iterable<?>)v1)))), this.loaders())));
    }
    
    protected Iterable<String> getAllAnnotated(final Iterable<String> v1, final boolean v2, final boolean v3) {
        /*SL:458*/if (!v3) {
            final Iterable<String> a1 = /*EL:471*/Iterables.<String>concat((Iterable<? extends String>)v1, (Iterable<? extends String>)this.store.getAll(index(TypeAnnotationsScanner.class), v1));
            /*SL:472*/return Iterables.<String>concat((Iterable<? extends String>)a1, (Iterable<? extends String>)this.store.getAll(index(SubTypesScanner.class), a1));
        }
        if (v2) {
            final Iterable<String> a1 = this.store.get(index(SubTypesScanner.class), ReflectionUtils.<String>filter(v1, new Predicate<String>() {
                @Override
                public boolean apply(@Nullable final String a1) {
                    final Class<?> v1 = /*EL:462*/ReflectionUtils.forName(a1, Reflections.this.loaders());
                    /*SL:463*/return v1 != null && !v1.isInterface();
                }
            }));
            return Iterables.<String>concat((Iterable<? extends String>)a1, (Iterable<? extends String>)this.store.getAll(index(SubTypesScanner.class), a1));
        }
        return v1;
    }
    
    public Set<Method> getMethodsAnnotatedWith(final Class<? extends Annotation> a1) {
        final Iterable<String> v1 = /*EL:481*/this.store.get(index(MethodAnnotationsScanner.class), a1.getName());
        /*SL:482*/return Utils.getMethodsFromDescriptors(v1, this.loaders());
    }
    
    public Set<Method> getMethodsAnnotatedWith(final Annotation a1) {
        /*SL:490*/return ReflectionUtils.<Method>filter(this.getMethodsAnnotatedWith(a1.annotationType()), ReflectionUtils.<Object>withAnnotation(a1));
    }
    
    public Set<Method> getMethodsMatchParams(final Class<?>... a1) {
        /*SL:495*/return Utils.getMethodsFromDescriptors(this.store.get(index(MethodParameterScanner.class), Utils.names(a1).toString()), this.loaders());
    }
    
    public Set<Method> getMethodsReturn(final Class a1) {
        /*SL:500*/return Utils.getMethodsFromDescriptors(this.store.get(index(MethodParameterScanner.class), Utils.names(a1)), this.loaders());
    }
    
    public Set<Method> getMethodsWithAnyParamAnnotated(final Class<? extends Annotation> a1) {
        /*SL:505*/return Utils.getMethodsFromDescriptors(this.store.get(index(MethodParameterScanner.class), a1.getName()), this.loaders());
    }
    
    public Set<Method> getMethodsWithAnyParamAnnotated(final Annotation a1) {
        /*SL:511*/return ReflectionUtils.<Method>filter(this.getMethodsWithAnyParamAnnotated(a1.annotationType()), ReflectionUtils.withAnyParameterAnnotation(a1));
    }
    
    public Set<Constructor> getConstructorsAnnotatedWith(final Class<? extends Annotation> a1) {
        final Iterable<String> v1 = /*EL:519*/this.store.get(index(MethodAnnotationsScanner.class), a1.getName());
        /*SL:520*/return Utils.getConstructorsFromDescriptors(v1, this.loaders());
    }
    
    public Set<Constructor> getConstructorsAnnotatedWith(final Annotation a1) {
        /*SL:528*/return (Set<Constructor>)ReflectionUtils.<Constructor>filter(this.getConstructorsAnnotatedWith(a1.annotationType()), ReflectionUtils.<Object>withAnnotation(a1));
    }
    
    public Set<Constructor> getConstructorsMatchParams(final Class<?>... a1) {
        /*SL:533*/return Utils.getConstructorsFromDescriptors(this.store.get(index(MethodParameterScanner.class), Utils.names(a1).toString()), this.loaders());
    }
    
    public Set<Constructor> getConstructorsWithAnyParamAnnotated(final Class<? extends Annotation> a1) {
        /*SL:538*/return Utils.getConstructorsFromDescriptors(this.store.get(index(MethodParameterScanner.class), a1.getName()), this.loaders());
    }
    
    public Set<Constructor> getConstructorsWithAnyParamAnnotated(final Annotation a1) {
        /*SL:543*/return (Set<Constructor>)ReflectionUtils.<Constructor>filter(this.getConstructorsWithAnyParamAnnotated(a1.annotationType()), ReflectionUtils.withAnyParameterAnnotation(a1));
    }
    
    public Set<Field> getFieldsAnnotatedWith(final Class<? extends Annotation> v2) {
        final Set<Field> v3 = /*EL:551*/(Set<Field>)Sets.<Object>newHashSet();
        /*SL:552*/for (final String a1 : this.store.get(index(FieldAnnotationsScanner.class), v2.getName())) {
            /*SL:553*/v3.add(Utils.getFieldFromString(a1, this.loaders()));
        }
        /*SL:555*/return v3;
    }
    
    public Set<Field> getFieldsAnnotatedWith(final Annotation a1) {
        /*SL:563*/return ReflectionUtils.<Field>filter(this.getFieldsAnnotatedWith(a1.annotationType()), ReflectionUtils.<Object>withAnnotation(a1));
    }
    
    public Set<String> getResources(final Predicate<String> a1) {
        final Iterable<String> v1 = /*EL:570*/Iterables.<String>filter(this.store.get(index(ResourcesScanner.class)).keySet(), a1);
        /*SL:571*/return (Set<String>)Sets.<Object>newHashSet((Iterable<?>)this.store.get(index(ResourcesScanner.class), v1));
    }
    
    public Set<String> getResources(final Pattern a1) {
        /*SL:579*/return this.getResources(new Predicate<String>() {
            @Override
            public boolean apply(final String a1) {
                /*SL:581*/return a1.matcher(a1).matches();
            }
        });
    }
    
    public List<String> getMethodParamNames(final Method a1) {
        final Iterable<String> v1 = /*EL:590*/this.store.get(index(MethodParameterNamesScanner.class), Utils.name(a1));
        /*SL:591*/return Iterables.isEmpty(v1) ? Arrays.<String>asList(new String[0]) : Arrays.<String>asList(Iterables.<String>getOnlyElement(v1).split(", "));
    }
    
    public List<String> getConstructorParamNames(final Constructor a1) {
        final Iterable<String> v1 = /*EL:598*/this.store.get(index(MethodParameterNamesScanner.class), Utils.name(a1));
        /*SL:599*/return Iterables.isEmpty(v1) ? Arrays.<String>asList(new String[0]) : Arrays.<String>asList(Iterables.<String>getOnlyElement(v1).split(", "));
    }
    
    public Set<Member> getFieldUsage(final Field a1) {
        /*SL:606*/return Utils.getMembersFromDescriptors(this.store.get(index(MemberUsageScanner.class), Utils.name(a1)), new ClassLoader[0]);
    }
    
    public Set<Member> getMethodUsage(final Method a1) {
        /*SL:613*/return Utils.getMembersFromDescriptors(this.store.get(index(MemberUsageScanner.class), Utils.name(a1)), new ClassLoader[0]);
    }
    
    public Set<Member> getConstructorUsage(final Constructor a1) {
        /*SL:620*/return Utils.getMembersFromDescriptors(this.store.get(index(MemberUsageScanner.class), Utils.name(a1)), new ClassLoader[0]);
    }
    
    public Set<String> getAllTypes() {
        final Set<String> v1 = /*EL:630*/(Set<String>)Sets.<Object>newHashSet((Iterable<?>)this.store.getAll(index(SubTypesScanner.class), Object.class.getName()));
        /*SL:631*/if (v1.isEmpty()) {
            /*SL:632*/throw new ReflectionsException("Couldn't find subtypes of Object. Make sure SubTypesScanner initialized to include Object class - new SubTypesScanner(false)");
        }
        /*SL:635*/return v1;
    }
    
    public Store getStore() {
        /*SL:640*/return this.store;
    }
    
    public Configuration getConfiguration() {
        /*SL:645*/return this.configuration;
    }
    
    public File save(final String a1) {
        /*SL:655*/return this.save(a1, this.configuration.getSerializer());
    }
    
    public File save(final String a1, final Serializer a2) {
        final File v1 = /*EL:664*/a2.save(this, a1);
        /*SL:665*/if (Reflections.log != null) {
            Reflections.log.info(/*EL:666*/"Reflections successfully saved in " + v1.getAbsolutePath() + " using " + a2.getClass().getSimpleName());
        }
        /*SL:667*/return v1;
    }
    
    private static String index(final Class<? extends Scanner> a1) {
        /*SL:670*/return a1.getSimpleName();
    }
    
    private ClassLoader[] loaders() {
        /*SL:672*/return this.configuration.getClassLoaders();
    }
    
    static {
        Reflections.log = Utils.findLogger(Reflections.class);
    }
}

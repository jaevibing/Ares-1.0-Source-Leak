package org.yaml.snakeyaml.introspector;

import java.util.Iterator;
import java.util.Collection;
import java.util.TreeSet;
import java.lang.reflect.InvocationTargetException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.beans.IntrospectionException;
import org.yaml.snakeyaml.error.YAMLException;
import java.beans.FeatureDescriptor;
import java.beans.Introspector;
import java.lang.reflect.Modifier;
import java.util.LinkedHashMap;
import java.util.HashMap;
import java.lang.reflect.Method;
import org.yaml.snakeyaml.util.PlatformFeatureDetector;
import java.util.Set;
import java.util.Map;
import java.util.logging.Logger;

public class PropertyUtils
{
    private static final Logger log;
    private final Map<Class<?>, Map<String, Property>> propertiesCache;
    private final Map<Class<?>, Set<Property>> readableProperties;
    private BeanAccess beanAccess;
    private boolean allowReadOnlyProperties;
    private boolean skipMissingProperties;
    private PlatformFeatureDetector platformFeatureDetector;
    private boolean transientMethodChecked;
    private Method isTransientMethod;
    
    public PropertyUtils() {
        this(new PlatformFeatureDetector());
    }
    
    PropertyUtils(final PlatformFeatureDetector a1) {
        this.propertiesCache = new HashMap<Class<?>, Map<String, Property>>();
        this.readableProperties = new HashMap<Class<?>, Set<Property>>();
        this.beanAccess = BeanAccess.DEFAULT;
        this.allowReadOnlyProperties = false;
        this.skipMissingProperties = false;
        this.platformFeatureDetector = a1;
        if (a1.isRunningOnAndroid()) {
            this.beanAccess = BeanAccess.FIELD;
        }
    }
    
    protected Map<String, Property> getPropertiesMap(final Class<?> v-3, final BeanAccess v-2) {
        /*SL:66*/if (this.propertiesCache.containsKey(v-3)) {
            /*SL:67*/return this.propertiesCache.get(v-3);
        }
        final Map<String, Property> map = /*EL:70*/new LinkedHashMap<String, Property>();
        boolean v0 = /*EL:71*/false;
        /*SL:72*/switch (v-2) {
            case FIELD: {
                /*SL:74*/for (Class<?> v = v-3; v != null; v = v.getSuperclass()) {
                    /*SL:75*/for (int a2 : v.getDeclaredFields()) {
                        /*SL:76*/a2 = a2.getModifiers();
                        /*SL:77*/if (!Modifier.isStatic(a2) && !Modifier.isTransient(a2) && !map.containsKey(a2.getName())) {
                            /*SL:79*/map.put(a2.getName(), new FieldProperty(a2));
                        }
                    }
                }
                /*SL:83*/break;
            }
            default: {
                try {
                    /*SL:87*/for (final PropertyDescriptor v2 : Introspector.getBeanInfo(v-3).getPropertyDescriptors()) {
                        final Method v3 = /*EL:89*/v2.getReadMethod();
                        /*SL:90*/if ((v3 == null || !v3.getName().equals("getClass")) && !this.isTransient(v2)) {
                            /*SL:92*/map.put(v2.getName(), new MethodProperty(v2));
                        }
                    }
                }
                catch (IntrospectionException v4) {
                    /*SL:96*/throw new YAMLException(v4);
                }
                /*SL:100*/for (Class<?> v = v-3; v != null; v = v.getSuperclass()) {
                    /*SL:101*/for (final Field v5 : v.getDeclaredFields()) {
                        final int v6 = /*EL:102*/v5.getModifiers();
                        /*SL:103*/if (!Modifier.isStatic(v6) && !Modifier.isTransient(v6)) {
                            /*SL:104*/if (Modifier.isPublic(v6)) {
                                /*SL:105*/map.put(v5.getName(), new FieldProperty(v5));
                            }
                            else {
                                /*SL:107*/v0 = true;
                            }
                        }
                    }
                }
                break;
            }
        }
        /*SL:114*/if (map.isEmpty() && v0) {
            /*SL:115*/throw new YAMLException("No JavaBean properties found in " + v-3.getName());
        }
        /*SL:117*/this.propertiesCache.put(v-3, map);
        /*SL:118*/return map;
    }
    
    private boolean isTransient(final FeatureDescriptor v0) {
        /*SL:125*/if (!this.transientMethodChecked) {
            /*SL:126*/this.transientMethodChecked = true;
            try {
                /*SL:128*/(this.isTransientMethod = FeatureDescriptor.class.getDeclaredMethod("isTransient", (Class<?>[])new Class[0])).setAccessible(/*EL:129*/true);
            }
            catch (NoSuchMethodException a1) {
                PropertyUtils.log.fine(/*EL:131*/"NoSuchMethod: FeatureDescriptor.isTransient(). Don't check it anymore.");
            }
            catch (SecurityException v) {
                /*SL:133*/v.printStackTrace();
                /*SL:134*/this.isTransientMethod = null;
            }
        }
        /*SL:138*/if (this.isTransientMethod != null) {
            try {
                /*SL:140*/return Boolean.TRUE.equals(this.isTransientMethod.invoke(v0, new Object[0]));
            }
            catch (IllegalAccessException v2) {
                /*SL:142*/v2.printStackTrace();
            }
            catch (IllegalArgumentException v3) {
                /*SL:144*/v3.printStackTrace();
            }
            catch (InvocationTargetException v4) {
                /*SL:146*/v4.printStackTrace();
            }
            /*SL:148*/this.isTransientMethod = null;
        }
        /*SL:150*/return false;
    }
    
    public Set<Property> getProperties(final Class<?> a1) {
        /*SL:154*/return this.getProperties(a1, this.beanAccess);
    }
    
    public Set<Property> getProperties(final Class<?> a1, final BeanAccess a2) {
        /*SL:158*/if (this.readableProperties.containsKey(a1)) {
            /*SL:159*/return this.readableProperties.get(a1);
        }
        final Set<Property> v1 = /*EL:161*/this.createPropertySet(a1, a2);
        /*SL:162*/this.readableProperties.put(a1, v1);
        /*SL:163*/return v1;
    }
    
    protected Set<Property> createPropertySet(final Class<?> v1, final BeanAccess v2) {
        final Set<Property> v3 = /*EL:167*/new TreeSet<Property>();
        final Collection<Property> v4 = /*EL:168*/this.getPropertiesMap(v1, v2).values();
        /*SL:169*/for (final Property a1 : v4) {
            /*SL:170*/if (a1.isReadable() && (this.allowReadOnlyProperties || a1.isWritable())) {
                /*SL:171*/v3.add(a1);
            }
        }
        /*SL:174*/return v3;
    }
    
    public Property getProperty(final Class<?> a1, final String a2) {
        /*SL:178*/return this.getProperty(a1, a2, this.beanAccess);
    }
    
    public Property getProperty(final Class<?> a1, final String a2, final BeanAccess a3) {
        final Map<String, Property> v1 = /*EL:182*/this.getPropertiesMap(a1, a3);
        Property v2 = /*EL:183*/v1.get(a2);
        /*SL:184*/if (v2 == null && this.skipMissingProperties) {
            /*SL:185*/v2 = new MissingProperty(a2);
        }
        /*SL:187*/if (v2 == null) {
            /*SL:188*/throw new YAMLException("Unable to find property '" + a2 + "' on class: " + a1.getName());
        }
        /*SL:191*/return v2;
    }
    
    public void setBeanAccess(final BeanAccess a1) {
        /*SL:195*/if (this.platformFeatureDetector.isRunningOnAndroid() && a1 != BeanAccess.FIELD) {
            /*SL:196*/throw new IllegalArgumentException("JVM is Android - only BeanAccess.FIELD is available");
        }
        /*SL:199*/if (this.beanAccess != a1) {
            /*SL:200*/this.beanAccess = a1;
            /*SL:201*/this.propertiesCache.clear();
            /*SL:202*/this.readableProperties.clear();
        }
    }
    
    public void setAllowReadOnlyProperties(final boolean a1) {
        /*SL:207*/if (this.allowReadOnlyProperties != a1) {
            /*SL:208*/this.allowReadOnlyProperties = a1;
            /*SL:209*/this.readableProperties.clear();
        }
    }
    
    public boolean isAllowReadOnlyProperties() {
        /*SL:214*/return this.allowReadOnlyProperties;
    }
    
    public void setSkipMissingProperties(final boolean a1) {
        /*SL:225*/if (this.skipMissingProperties != a1) {
            /*SL:226*/this.skipMissingProperties = a1;
            /*SL:227*/this.readableProperties.clear();
        }
    }
    
    public boolean isSkipMissingProperties() {
        /*SL:232*/return this.skipMissingProperties;
    }
    
    static {
        log = Logger.getLogger(PropertyUtils.class.getPackage().getName());
    }
}

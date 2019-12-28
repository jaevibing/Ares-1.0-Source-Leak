package org.yaml.snakeyaml;

import java.lang.reflect.Constructor;
import org.yaml.snakeyaml.nodes.Node;
import java.util.LinkedHashSet;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Iterator;
import java.util.Collection;
import org.yaml.snakeyaml.error.YAMLException;
import java.util.Collections;
import org.yaml.snakeyaml.introspector.BeanAccess;
import org.yaml.snakeyaml.introspector.PropertySubstitute;
import java.util.Map;
import org.yaml.snakeyaml.introspector.PropertyUtils;
import org.yaml.snakeyaml.introspector.Property;
import java.util.Set;
import org.yaml.snakeyaml.nodes.Tag;

public class TypeDescription
{
    private final Class<?> type;
    private Class<?> impl;
    private Tag tag;
    private transient Set<Property> dumpProperties;
    private transient PropertyUtils propertyUtils;
    private transient boolean delegatesChecked;
    private Map<String, PropertySubstitute> properties;
    protected Set<String> excludes;
    protected String[] includes;
    protected BeanAccess beanAccess;
    
    public TypeDescription(final Class<?> a1, final Tag a2) {
        this(a1, a2, null);
    }
    
    public TypeDescription(final Class<?> a1, final Tag a2, final Class<?> a3) {
        this.properties = Collections.<String, PropertySubstitute>emptyMap();
        this.excludes = Collections.<String>emptySet();
        this.includes = null;
        this.type = a1;
        this.tag = a2;
        this.impl = a3;
        this.beanAccess = null;
    }
    
    public TypeDescription(final Class<?> a1, final String a2) {
        this(a1, new Tag(a2), null);
    }
    
    public TypeDescription(final Class<?> a1) {
        this(a1, null, null);
    }
    
    public TypeDescription(final Class<?> a1, final Class<?> a2) {
        this(a1, null, a2);
    }
    
    public Tag getTag() {
        /*SL:93*/return this.tag;
    }
    
    public void setTag(final Tag a1) {
        /*SL:103*/this.tag = a1;
    }
    
    public void setTag(final String a1) {
        /*SL:107*/this.setTag(new Tag(a1));
    }
    
    public Class<?> getType() {
        /*SL:116*/return this.type;
    }
    
    @Deprecated
    public void putListPropertyType(final String a1, final Class<?> a2) {
        /*SL:129*/this.addPropertyParameters(a1, a2);
    }
    
    @Deprecated
    public Class<?> getListPropertyType(final String v2) {
        /*SL:141*/if (this.properties.containsKey(v2)) {
            final Class<?>[] a1 = /*EL:142*/this.properties.get(v2).getActualTypeArguments();
            /*SL:143*/if (a1 != null && a1.length > 0) {
                /*SL:144*/return a1[0];
            }
        }
        /*SL:147*/return null;
    }
    
    @Deprecated
    public void putMapPropertyType(final String a1, final Class<?> a2, final Class<?> a3) {
        /*SL:163*/this.addPropertyParameters(a1, a2, a3);
    }
    
    @Deprecated
    public Class<?> getMapKeyType(final String v2) {
        /*SL:175*/if (this.properties.containsKey(v2)) {
            final Class<?>[] a1 = /*EL:176*/this.properties.get(v2).getActualTypeArguments();
            /*SL:177*/if (a1 != null && a1.length > 0) {
                /*SL:178*/return a1[0];
            }
        }
        /*SL:181*/return null;
    }
    
    @Deprecated
    public Class<?> getMapValueType(final String v2) {
        /*SL:193*/if (this.properties.containsKey(v2)) {
            final Class<?>[] a1 = /*EL:194*/this.properties.get(v2).getActualTypeArguments();
            /*SL:195*/if (a1 != null && a1.length > 1) {
                /*SL:196*/return a1[1];
            }
        }
        /*SL:199*/return null;
    }
    
    public void addPropertyParameters(final String v1, final Class<?>... v2) {
        /*SL:212*/if (!this.properties.containsKey(v1)) {
            /*SL:213*/this.substituteProperty(v1, null, null, null, v2);
        }
        else {
            final PropertySubstitute a1 = /*EL:215*/this.properties.get(v1);
            /*SL:216*/a1.setActualTypeArguments(v2);
        }
    }
    
    @Override
    public String toString() {
        /*SL:223*/return "TypeDescription for " + this.getType() + " (tag='" + this.getTag() + "')";
    }
    
    private void checkDelegates() {
        final Collection<PropertySubstitute> values = /*EL:227*/this.properties.values();
        /*SL:228*/for (final PropertySubstitute v1 : values) {
            try {
                /*SL:230*/v1.setDelegate(this.discoverProperty(v1.getName()));
            }
            catch (YAMLException ex) {}
        }
        /*SL:234*/this.delegatesChecked = true;
    }
    
    private Property discoverProperty(final String a1) {
        /*SL:238*/if (this.propertyUtils == null) {
            /*SL:244*/return null;
        }
        if (this.beanAccess == null) {
            return this.propertyUtils.getProperty(this.type, a1);
        }
        return this.propertyUtils.getProperty(this.type, a1, this.beanAccess);
    }
    
    public Property getProperty(final String a1) {
        /*SL:248*/if (!this.delegatesChecked) {
            /*SL:249*/this.checkDelegates();
        }
        /*SL:251*/return this.properties.containsKey(a1) ? ((PropertySubstitute)this.properties.get(a1)) : this.discoverProperty(a1);
    }
    
    public void substituteProperty(final String a1, final Class<?> a2, final String a3, final String a4, final Class<?>... a5) {
        /*SL:270*/this.substituteProperty(new PropertySubstitute(a1, a2, a3, a4, a5));
    }
    
    public void substituteProperty(final PropertySubstitute a1) {
        /*SL:274*/if (Collections.EMPTY_MAP == this.properties) {
            /*SL:275*/this.properties = new LinkedHashMap<String, PropertySubstitute>();
        }
        /*SL:277*/a1.setTargetType(this.type);
        /*SL:278*/this.properties.put(a1.getName(), a1);
    }
    
    public void setPropertyUtils(final PropertyUtils a1) {
        /*SL:282*/this.propertyUtils = a1;
    }
    
    public void setIncludes(final String... a1) {
        /*SL:287*/this.includes = (String[])((a1 != null && a1.length > 0) ? a1 : null);
    }
    
    public void setExcludes(final String... v2) {
        /*SL:291*/if (v2 != null && v2.length > 0) {
            /*SL:292*/this.excludes = new HashSet<String>();
            /*SL:293*/for (final String a1 : v2) {
                /*SL:294*/this.excludes.add(a1);
            }
        }
        else {
            /*SL:297*/this.excludes = Collections.<String>emptySet();
        }
    }
    
    public Set<Property> getProperties() {
        /*SL:302*/if (this.dumpProperties != null) {
            /*SL:303*/return this.dumpProperties;
        }
        /*SL:306*/if (this.propertyUtils == null) {
            /*SL:353*/return null;
        }
        if (this.includes != null) {
            this.dumpProperties = new LinkedHashSet<Property>();
            for (final String v1 : this.includes) {
                if (!this.excludes.contains(v1)) {
                    this.dumpProperties.add(this.getProperty(v1));
                }
            }
            return this.dumpProperties;
        }
        final Set<Property> dumpProperties = (this.beanAccess == null) ? this.propertyUtils.getProperties(this.type) : this.propertyUtils.getProperties(this.type, this.beanAccess);
        if (!this.properties.isEmpty()) {
            if (!this.delegatesChecked) {
                this.checkDelegates();
            }
            this.dumpProperties = new LinkedHashSet<Property>();
            for (final Property v2 : this.properties.values()) {
                if (!this.excludes.contains(v2.getName()) && v2.isReadable()) {
                    this.dumpProperties.add(v2);
                }
            }
            for (final Property v2 : dumpProperties) {
                if (!this.excludes.contains(v2.getName())) {
                    this.dumpProperties.add(v2);
                }
            }
            return this.dumpProperties;
        }
        if (this.excludes.isEmpty()) {
            return this.dumpProperties = dumpProperties;
        }
        this.dumpProperties = new LinkedHashSet<Property>();
        for (final Property v2 : dumpProperties) {
            if (!this.excludes.contains(v2.getName())) {
                this.dumpProperties.add(v2);
            }
        }
        return this.dumpProperties;
    }
    
    public boolean setupPropertyType(final String a1, final Node a2) {
        /*SL:361*/return false;
    }
    
    public boolean setProperty(final Object a1, final String a2, final Object a3) throws Exception {
        /*SL:366*/return false;
    }
    
    public Object newInstance(final Node v0) {
        /*SL:379*/if (this.impl != null) {
            try {
                final Constructor<?> a1 = /*EL:381*/this.impl.getDeclaredConstructor((Class<?>[])new Class[0]);
                /*SL:382*/a1.setAccessible(true);
                /*SL:383*/return a1.newInstance(new Object[0]);
            }
            catch (Exception v) {
                /*SL:385*/v.printStackTrace();
                /*SL:386*/this.impl = null;
            }
        }
        /*SL:389*/return null;
    }
    
    public Object newInstance(final String a1, final Node a2) {
        /*SL:393*/return null;
    }
    
    public Object finalizeConstruction(final Object a1) {
        /*SL:402*/return a1;
    }
}

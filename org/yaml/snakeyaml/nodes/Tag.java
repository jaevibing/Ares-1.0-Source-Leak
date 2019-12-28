package org.yaml.snakeyaml.nodes;

import java.sql.Timestamp;
import java.util.Date;
import java.math.BigInteger;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.HashMap;
import org.yaml.snakeyaml.error.YAMLException;
import java.net.URI;
import org.yaml.snakeyaml.util.UriEncoder;
import java.util.Set;
import java.util.Map;

public final class Tag implements Comparable<Tag>
{
    public static final String PREFIX = "tag:yaml.org,2002:";
    public static final Tag YAML;
    public static final Tag MERGE;
    public static final Tag SET;
    public static final Tag PAIRS;
    public static final Tag OMAP;
    public static final Tag BINARY;
    public static final Tag INT;
    public static final Tag FLOAT;
    public static final Tag TIMESTAMP;
    public static final Tag BOOL;
    public static final Tag NULL;
    public static final Tag STR;
    public static final Tag SEQ;
    public static final Tag MAP;
    public static final Map<Tag, Set<Class<?>>> COMPATIBILITY_MAP;
    private final String value;
    private boolean secondary;
    
    public Tag(final String a1) {
        this.secondary = false;
        if (a1 == null) {
            throw new NullPointerException("Tag must be provided.");
        }
        if (a1.length() == 0) {
            throw new IllegalArgumentException("Tag must not be empty.");
        }
        if (a1.trim().length() != a1.length()) {
            throw new IllegalArgumentException("Tag must not contain leading or trailing spaces.");
        }
        this.value = UriEncoder.encode(a1);
        this.secondary = !a1.startsWith("tag:yaml.org,2002:");
    }
    
    public Tag(final Class<?> a1) {
        this.secondary = false;
        if (a1 == null) {
            throw new NullPointerException("Class for tag must be provided.");
        }
        this.value = "tag:yaml.org,2002:" + UriEncoder.encode(a1.getName());
    }
    
    public Tag(final URI a1) {
        this.secondary = false;
        if (a1 == null) {
            throw new NullPointerException("URI for tag must be provided.");
        }
        this.value = a1.toASCIIString();
    }
    
    public boolean isSecondary() {
        /*SL:99*/return this.secondary;
    }
    
    public String getValue() {
        /*SL:103*/return this.value;
    }
    
    public boolean startsWith(final String a1) {
        /*SL:107*/return this.value.startsWith(a1);
    }
    
    public String getClassName() {
        /*SL:111*/if (!this.value.startsWith("tag:yaml.org,2002:")) {
            /*SL:112*/throw new YAMLException("Invalid tag: " + this.value);
        }
        /*SL:114*/return UriEncoder.decode(this.value.substring("tag:yaml.org,2002:".length()));
    }
    
    public int getLength() {
        /*SL:118*/return this.value.length();
    }
    
    @Override
    public String toString() {
        /*SL:123*/return this.value;
    }
    
    @Override
    public boolean equals(final Object a1) {
        /*SL:128*/return a1 instanceof Tag && /*EL:129*/this.value.equals(((Tag)a1).getValue());
    }
    
    @Override
    public int hashCode() {
        /*SL:136*/return this.value.hashCode();
    }
    
    public boolean isCompatible(final Class<?> a1) {
        final Set<Class<?>> v1 = Tag.COMPATIBILITY_MAP.get(/*EL:149*/this);
        /*SL:150*/return v1 != null && /*EL:151*/v1.contains(a1);
    }
    
    public boolean matches(final Class<?> a1) {
        /*SL:165*/return this.value.equals("tag:yaml.org,2002:" + a1.getName());
    }
    
    @Override
    public int compareTo(final Tag a1) {
        /*SL:169*/return this.value.compareTo(a1.getValue());
    }
    
    static {
        YAML = new Tag("tag:yaml.org,2002:yaml");
        MERGE = new Tag("tag:yaml.org,2002:merge");
        SET = new Tag("tag:yaml.org,2002:set");
        PAIRS = new Tag("tag:yaml.org,2002:pairs");
        OMAP = new Tag("tag:yaml.org,2002:omap");
        BINARY = new Tag("tag:yaml.org,2002:binary");
        INT = new Tag("tag:yaml.org,2002:int");
        FLOAT = new Tag("tag:yaml.org,2002:float");
        TIMESTAMP = new Tag("tag:yaml.org,2002:timestamp");
        BOOL = new Tag("tag:yaml.org,2002:bool");
        NULL = new Tag("tag:yaml.org,2002:null");
        STR = new Tag("tag:yaml.org,2002:str");
        SEQ = new Tag("tag:yaml.org,2002:seq");
        MAP = new Tag("tag:yaml.org,2002:map");
        COMPATIBILITY_MAP = new HashMap<Tag, Set<Class<?>>>();
        final Set<Class<?>> v1 = new HashSet<Class<?>>();
        v1.add(Double.class);
        v1.add(Float.class);
        v1.add(BigDecimal.class);
        Tag.COMPATIBILITY_MAP.put(Tag.FLOAT, v1);
        final Set<Class<?>> v2 = new HashSet<Class<?>>();
        v2.add(Integer.class);
        v2.add(Long.class);
        v2.add(BigInteger.class);
        Tag.COMPATIBILITY_MAP.put(Tag.INT, v2);
        final Set<Class<?>> v3 = new HashSet<Class<?>>();
        v3.add(Date.class);
        v3.add(java.sql.Date.class);
        v3.add(Timestamp.class);
        Tag.COMPATIBILITY_MAP.put(Tag.TIMESTAMP, v3);
    }
}

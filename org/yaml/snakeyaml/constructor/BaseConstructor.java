package org.yaml.snakeyaml.constructor;

import org.yaml.snakeyaml.nodes.NodeTuple;
import java.util.Collection;
import org.yaml.snakeyaml.nodes.MappingNode;
import org.yaml.snakeyaml.nodes.SequenceNode;
import org.yaml.snakeyaml.nodes.CollectionNode;
import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import org.yaml.snakeyaml.error.YAMLException;
import java.lang.reflect.Array;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import org.yaml.snakeyaml.nodes.ScalarNode;
import org.yaml.snakeyaml.error.Mark;
import java.util.Iterator;
import java.util.TreeSet;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.SortedMap;
import java.util.HashSet;
import java.util.HashMap;
import java.util.EnumMap;
import org.yaml.snakeyaml.TypeDescription;
import org.yaml.snakeyaml.introspector.PropertyUtils;
import java.util.ArrayList;
import java.util.Set;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.composer.Composer;
import org.yaml.snakeyaml.nodes.Tag;
import org.yaml.snakeyaml.nodes.NodeId;
import java.util.Map;

public abstract class BaseConstructor
{
    protected final Map<NodeId, Construct> yamlClassConstructors;
    protected final Map<Tag, Construct> yamlConstructors;
    protected final Map<String, Construct> yamlMultiConstructors;
    protected Composer composer;
    final Map<Node, Object> constructedObjects;
    private final Set<Node> recursiveObjects;
    private final ArrayList<RecursiveTuple<Map<Object, Object>, RecursiveTuple<Object, Object>>> maps2fill;
    private final ArrayList<RecursiveTuple<Set<Object>, Object>> sets2fill;
    protected Tag rootTag;
    private PropertyUtils propertyUtils;
    private boolean explicitPropertyUtils;
    private boolean allowDuplicateKeys;
    protected final Map<Class<?>, TypeDescription> typeDefinitions;
    protected final Map<Tag, Class<?>> typeTags;
    
    public BaseConstructor() {
        this.yamlClassConstructors = new EnumMap<NodeId, Construct>(NodeId.class);
        this.yamlConstructors = new HashMap<Tag, Construct>();
        this.yamlMultiConstructors = new HashMap<String, Construct>();
        this.allowDuplicateKeys = true;
        this.constructedObjects = new HashMap<Node, Object>();
        this.recursiveObjects = new HashSet<Node>();
        this.maps2fill = new ArrayList<RecursiveTuple<Map<Object, Object>, RecursiveTuple<Object, Object>>>();
        this.sets2fill = new ArrayList<RecursiveTuple<Set<Object>, Object>>();
        this.typeDefinitions = new HashMap<Class<?>, TypeDescription>();
        this.typeTags = new HashMap<Tag, Class<?>>();
        this.rootTag = null;
        this.explicitPropertyUtils = false;
        this.typeDefinitions.put(SortedMap.class, new TypeDescription(SortedMap.class, Tag.OMAP, TreeMap.class));
        this.typeDefinitions.put(SortedSet.class, new TypeDescription(SortedSet.class, Tag.SET, TreeSet.class));
    }
    
    public void setComposer(final Composer a1) {
        /*SL:102*/this.composer = a1;
    }
    
    public boolean checkData() {
        /*SL:112*/return this.composer.checkNode();
    }
    
    public Object getData() {
        /*SL:122*/this.composer.checkNode();
        final Node v1 = /*EL:123*/this.composer.getNode();
        /*SL:124*/if (this.rootTag != null) {
            /*SL:125*/v1.setTag(this.rootTag);
        }
        /*SL:127*/return this.constructDocument(v1);
    }
    
    public Object getSingleData(final Class<?> a1) {
        final Node v1 = /*EL:140*/this.composer.getSingleNode();
        /*SL:141*/if (v1 != null && !Tag.NULL.equals(v1.getTag())) {
            /*SL:142*/if (Object.class != a1) {
                /*SL:143*/v1.setTag(new Tag(a1));
            }
            else/*SL:144*/ if (this.rootTag != null) {
                /*SL:145*/v1.setTag(this.rootTag);
            }
            /*SL:147*/return this.constructDocument(v1);
        }
        /*SL:149*/return null;
    }
    
    protected final Object constructDocument(final Node a1) {
        final Object v1 = /*EL:161*/this.constructObject(a1);
        /*SL:162*/this.fillRecursive();
        /*SL:163*/this.constructedObjects.clear();
        /*SL:164*/this.recursiveObjects.clear();
        /*SL:165*/return v1;
    }
    
    private void fillRecursive() {
        /*SL:169*/if (!this.maps2fill.isEmpty()) {
            /*SL:170*/for (final RecursiveTuple<Map<Object, Object>, RecursiveTuple<Object, Object>> v0 : this.maps2fill) {
                final RecursiveTuple<Object, Object> v = /*EL:171*/v0._2();
                /*SL:172*/v0._1().put(v._1(), v._2());
            }
            /*SL:174*/this.maps2fill.clear();
        }
        /*SL:176*/if (!this.sets2fill.isEmpty()) {
            /*SL:177*/for (final RecursiveTuple<Set<Object>, Object> v2 : this.sets2fill) {
                /*SL:178*/v2._1().add(v2._2());
            }
            /*SL:180*/this.sets2fill.clear();
        }
    }
    
    protected Object constructObject(final Node a1) {
        /*SL:193*/if (this.constructedObjects.containsKey(a1)) {
            /*SL:194*/return this.constructedObjects.get(a1);
        }
        /*SL:196*/return this.constructObjectNoCheck(a1);
    }
    
    protected Object constructObjectNoCheck(final Node a1) {
        /*SL:200*/if (this.recursiveObjects.contains(a1)) {
            /*SL:202*/throw new ConstructorException(null, null, "found unconstructable recursive node", a1.getStartMark());
        }
        /*SL:204*/this.recursiveObjects.add(a1);
        final Construct v1 = /*EL:205*/this.getConstructor(a1);
        final Object v2 = /*EL:206*/this.constructedObjects.containsKey(a1) ? this.constructedObjects.get(a1) : v1.construct(a1);
        /*SL:209*/this.finalizeConstruction(a1, v2);
        /*SL:210*/this.constructedObjects.put(a1, v2);
        /*SL:211*/this.recursiveObjects.remove(a1);
        /*SL:212*/if (a1.isTwoStepsConstruction()) {
            /*SL:213*/v1.construct2ndStep(a1, v2);
        }
        /*SL:215*/return v2;
    }
    
    protected Construct getConstructor(final Node v0) {
        /*SL:228*/if (v0.useClassConstructor()) {
            /*SL:229*/return this.yamlClassConstructors.get(v0.getNodeId());
        }
        final Construct v = /*EL:231*/this.yamlConstructors.get(v0.getTag());
        /*SL:232*/if (v == null) {
            /*SL:233*/for (final String a1 : this.yamlMultiConstructors.keySet()) {
                /*SL:234*/if (v0.getTag().startsWith(a1)) {
                    /*SL:235*/return this.yamlMultiConstructors.get(a1);
                }
            }
            /*SL:238*/return this.yamlConstructors.get(null);
        }
        /*SL:240*/return v;
    }
    
    protected Object constructScalar(final ScalarNode a1) {
        /*SL:245*/return a1.getValue();
    }
    
    protected List<Object> createDefaultList(final int a1) {
        /*SL:250*/return new ArrayList<Object>(a1);
    }
    
    protected Set<Object> createDefaultSet(final int a1) {
        /*SL:254*/return new LinkedHashSet<Object>(a1);
    }
    
    protected Map<Object, Object> createDefaultMap() {
        /*SL:259*/return new LinkedHashMap<Object, Object>();
    }
    
    protected Set<Object> createDefaultSet() {
        /*SL:264*/return new LinkedHashSet<Object>();
    }
    
    protected Object createArray(final Class<?> a1, final int a2) {
        /*SL:268*/return Array.newInstance(a1.getComponentType(), a2);
    }
    
    protected Object finalizeConstruction(final Node a1, final Object a2) {
        final Class<?> v1 = /*EL:274*/a1.getType();
        /*SL:275*/if (this.typeDefinitions.containsKey(v1)) {
            /*SL:276*/return this.typeDefinitions.get(v1).finalizeConstruction(a2);
        }
        /*SL:278*/return a2;
    }
    
    protected Object newInstance(final Node v2) {
        try {
            /*SL:284*/return this.newInstance(Object.class, v2);
        }
        catch (InstantiationException a1) {
            /*SL:286*/throw new YAMLException(a1);
        }
    }
    
    protected final Object newInstance(final Class<?> a1, final Node a2) throws InstantiationException {
        /*SL:291*/return this.newInstance(a1, a2, true);
    }
    
    protected Object newInstance(final Class<?> v-3, final Node v-2, final boolean v-1) throws InstantiationException {
        final Class<?> v0 = /*EL:296*/v-2.getType();
        /*SL:297*/if (this.typeDefinitions.containsKey(v0)) {
            final TypeDescription a1 = /*EL:298*/this.typeDefinitions.get(v0);
            final Object a2 = /*EL:299*/a1.newInstance(v-2);
            /*SL:300*/if (a2 != null) {
                /*SL:301*/return a2;
            }
        }
        /*SL:309*/if (v-1 && v-3.isAssignableFrom(v0) && !Modifier.isAbstract(v0.getModifiers())) {
            try {
                final Constructor<?> a3 = /*EL:311*/v0.getDeclaredConstructor((Class<?>[])new Class[0]);
                /*SL:312*/a3.setAccessible(true);
                /*SL:313*/return a3.newInstance(new Object[0]);
            }
            catch (NoSuchMethodException v) {
                /*SL:315*/throw new InstantiationException("NoSuchMethodException:" + v.getLocalizedMessage());
            }
            catch (Exception v2) {
                /*SL:318*/throw new YAMLException(v2);
            }
        }
        /*SL:322*/throw new InstantiationException();
    }
    
    protected Set<Object> newSet(final CollectionNode<?> v2) {
        try {
            /*SL:328*/return (Set<Object>)this.newInstance(Set.class, v2);
        }
        catch (InstantiationException a1) {
            /*SL:330*/return this.createDefaultSet(v2.getValue().size());
        }
    }
    
    protected List<Object> newList(final SequenceNode v2) {
        try {
            /*SL:337*/return (List<Object>)this.newInstance(List.class, v2);
        }
        catch (InstantiationException a1) {
            /*SL:339*/return this.createDefaultList(v2.getValue().size());
        }
    }
    
    protected Map<Object, Object> newMap(final MappingNode v2) {
        try {
            /*SL:346*/return (Map<Object, Object>)this.newInstance(Map.class, v2);
        }
        catch (InstantiationException a1) {
            /*SL:348*/return this.createDefaultMap();
        }
    }
    
    protected List<?> constructSequence(final SequenceNode a1) {
        final List<Object> v1 = /*EL:356*/this.newList(a1);
        /*SL:357*/this.constructSequenceStep2(a1, v1);
        /*SL:358*/return v1;
    }
    
    protected Set<?> constructSet(final SequenceNode a1) {
        final Set<Object> v1 = /*EL:362*/this.newSet(a1);
        /*SL:363*/this.constructSequenceStep2(a1, v1);
        /*SL:364*/return v1;
    }
    
    protected Object constructArray(final SequenceNode a1) {
        /*SL:368*/return this.constructArrayStep2(a1, this.createArray(a1.getType(), a1.getValue().size()));
    }
    
    protected void constructSequenceStep2(final SequenceNode v1, final Collection<Object> v2) {
        /*SL:372*/for (final Node a1 : v1.getValue()) {
            /*SL:373*/v2.add(this.constructObject(a1));
        }
    }
    
    protected Object constructArrayStep2(final SequenceNode v2, final Object v3) {
        final Class<?> v4 = /*EL:378*/v2.getType().getComponentType();
        int v5 = /*EL:380*/0;
        /*SL:381*/for (Object a2 : v2.getValue()) {
            /*SL:383*/if (a2.getType() == Object.class) {
                /*SL:384*/a2.setType(v4);
            }
            /*SL:387*/a2 = this.constructObject(a2);
            /*SL:389*/if (v4.isPrimitive()) {
                /*SL:391*/if (a2 == null) {
                    /*SL:392*/throw new NullPointerException("Unable to construct element value for " + a2);
                }
                /*SL:397*/if (Byte.TYPE.equals(v4)) {
                    /*SL:398*/Array.setByte(v3, v5, ((Number)a2).byteValue());
                }
                else/*SL:400*/ if (Short.TYPE.equals(v4)) {
                    /*SL:401*/Array.setShort(v3, v5, ((Number)a2).shortValue());
                }
                else/*SL:403*/ if (Integer.TYPE.equals(v4)) {
                    /*SL:404*/Array.setInt(v3, v5, ((Number)a2).intValue());
                }
                else/*SL:406*/ if (Long.TYPE.equals(v4)) {
                    /*SL:407*/Array.setLong(v3, v5, ((Number)a2).longValue());
                }
                else/*SL:409*/ if (Float.TYPE.equals(v4)) {
                    /*SL:410*/Array.setFloat(v3, v5, ((Number)a2).floatValue());
                }
                else/*SL:412*/ if (Double.TYPE.equals(v4)) {
                    /*SL:413*/Array.setDouble(v3, v5, ((Number)a2).doubleValue());
                }
                else/*SL:415*/ if (Character.TYPE.equals(v4)) {
                    /*SL:416*/Array.setChar(v3, v5, (char)a2);
                }
                else {
                    /*SL:418*/if (!Boolean.TYPE.equals(v4)) {
                        /*SL:422*/throw new YAMLException("unexpected primitive type");
                    }
                    Array.setBoolean(v3, v5, (boolean)a2);
                }
            }
            else {
                /*SL:427*/Array.set(v3, v5, a2);
            }
            /*SL:430*/++v5;
        }
        /*SL:432*/return v3;
    }
    
    protected Set<Object> constructSet(final MappingNode a1) {
        final Set<Object> v1 = /*EL:436*/this.newSet(a1);
        /*SL:437*/this.constructSet2ndStep(a1, v1);
        /*SL:438*/return v1;
    }
    
    protected Map<Object, Object> constructMapping(final MappingNode a1) {
        final Map<Object, Object> v1 = /*EL:442*/this.newMap(a1);
        /*SL:443*/this.constructMapping2ndStep(a1, v1);
        /*SL:444*/return v1;
    }
    
    protected void constructMapping2ndStep(final MappingNode v-5, final Map<Object, Object> v-4) {
        final List<NodeTuple> value = /*EL:448*/v-5.getValue();
        /*SL:449*/for (final NodeTuple nodeTuple : value) {
            final Node a2 = /*EL:450*/nodeTuple.getKeyNode();
            final Node v1 = /*EL:451*/nodeTuple.getValueNode();
            final Object v2 = /*EL:452*/this.constructObject(a2);
            /*SL:453*/if (v2 != null) {
                try {
                    /*SL:455*/v2.hashCode();
                }
                catch (Exception a2) {
                    /*SL:457*/throw new ConstructorException("while constructing a mapping", v-5.getStartMark(), /*EL:458*/"found unacceptable key " + v2, nodeTuple.getKeyNode().getStartMark(), /*EL:459*/a2);
                }
            }
            final Object v3 = /*EL:462*/this.constructObject(v1);
            /*SL:463*/if (a2.isTwoStepsConstruction()) {
                /*SL:470*/this.maps2fill.add(0, new RecursiveTuple<Map<Object, Object>, RecursiveTuple<Object, Object>>(v-4, new RecursiveTuple<Object, Object>(v2, v3)));
            }
            else {
                /*SL:474*/v-4.put(v2, v3);
            }
        }
    }
    
    protected void constructSet2ndStep(final MappingNode v-5, final Set<Object> v-4) {
        final List<NodeTuple> value = /*EL:480*/v-5.getValue();
        /*SL:481*/for (final NodeTuple nodeTuple : value) {
            final Node a2 = /*EL:482*/nodeTuple.getKeyNode();
            final Object v1 = /*EL:483*/this.constructObject(a2);
            /*SL:484*/if (v1 != null) {
                try {
                    /*SL:486*/v1.hashCode();
                }
                catch (Exception a2) {
                    /*SL:488*/throw new ConstructorException("while constructing a Set", v-5.getStartMark(), "found unacceptable key " + v1, nodeTuple.getKeyNode().getStartMark(), /*EL:489*/a2);
                }
            }
            /*SL:492*/if (a2.isTwoStepsConstruction()) {
                /*SL:499*/this.sets2fill.add(0, new RecursiveTuple<Set<Object>, Object>(v-4, v1));
            }
            else {
                /*SL:501*/v-4.add(v1);
            }
        }
    }
    
    public void setPropertyUtils(final PropertyUtils v2) {
        /*SL:521*/this.propertyUtils = v2;
        /*SL:522*/this.explicitPropertyUtils = true;
        final Collection<TypeDescription> v3 = /*EL:523*/this.typeDefinitions.values();
        /*SL:524*/for (final TypeDescription a1 : v3) {
            /*SL:525*/a1.setPropertyUtils(v2);
        }
    }
    
    public final PropertyUtils getPropertyUtils() {
        /*SL:530*/if (this.propertyUtils == null) {
            /*SL:531*/this.propertyUtils = new PropertyUtils();
        }
        /*SL:533*/return this.propertyUtils;
    }
    
    public TypeDescription addTypeDescription(final TypeDescription a1) {
        /*SL:547*/if (a1 == null) {
            /*SL:548*/throw new NullPointerException("TypeDescription is required.");
        }
        final Tag v1 = /*EL:550*/a1.getTag();
        /*SL:551*/this.typeTags.put(v1, a1.getType());
        /*SL:552*/a1.setPropertyUtils(this.getPropertyUtils());
        /*SL:553*/return this.typeDefinitions.put(a1.getType(), a1);
    }
    
    public final boolean isExplicitPropertyUtils() {
        /*SL:575*/return this.explicitPropertyUtils;
    }
    
    public boolean isAllowDuplicateKeys() {
        /*SL:579*/return this.allowDuplicateKeys;
    }
    
    public void setAllowDuplicateKeys(final boolean a1) {
        /*SL:583*/this.allowDuplicateKeys = a1;
    }
    
    private static class RecursiveTuple<T, K>
    {
        private final T _1;
        private final K _2;
        
        public RecursiveTuple(final T a1, final K a2) {
            this._1 = /*EL:584*/a1;
            this._2 = a2;
        }
        
        public K _2() {
            /*SL:566*/return this._2;
        }
        
        public T _1() {
            /*SL:570*/return this._1;
        }
    }
}

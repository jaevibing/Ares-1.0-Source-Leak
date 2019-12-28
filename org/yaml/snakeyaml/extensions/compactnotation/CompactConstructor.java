package org.yaml.snakeyaml.extensions.compactnotation;

import org.yaml.snakeyaml.nodes.SequenceNode;
import java.util.Set;
import java.util.List;
import org.yaml.snakeyaml.nodes.NodeTuple;
import org.yaml.snakeyaml.nodes.MappingNode;
import org.yaml.snakeyaml.nodes.Node;
import java.util.regex.Matcher;
import org.yaml.snakeyaml.introspector.Property;
import java.util.Iterator;
import org.yaml.snakeyaml.error.YAMLException;
import java.util.Map;
import java.util.HashMap;
import org.yaml.snakeyaml.nodes.ScalarNode;
import org.yaml.snakeyaml.constructor.Construct;
import java.util.regex.Pattern;
import org.yaml.snakeyaml.constructor.Constructor;

public class CompactConstructor extends Constructor
{
    private static final Pattern GUESS_COMPACT;
    private static final Pattern FIRST_PATTERN;
    private static final Pattern PROPERTY_NAME_PATTERN;
    private Construct compactConstruct;
    
    protected Object constructCompactFormat(final ScalarNode v-1, final CompactData v0) {
        try {
            final Object a1 = /*EL:49*/this.createInstance(v-1, v0);
            final Map<String, Object> a2 = /*EL:50*/new HashMap<String, Object>(v0.getProperties());
            /*SL:51*/this.setProperties(a1, a2);
            /*SL:52*/return a1;
        }
        catch (Exception v) {
            /*SL:54*/throw new YAMLException(v);
        }
    }
    
    protected Object createInstance(final ScalarNode v1, final CompactData v2) throws Exception {
        final Class<?> v3 = /*EL:59*/this.getClassForName(v2.getPrefix());
        final Class<?>[] v4 = /*EL:60*/(Class<?>[])new Class[v2.getArguments().size()];
        /*SL:61*/for (int a1 = 0; a1 < v4.length; ++a1) {
            /*SL:63*/v4[a1] = String.class;
        }
        final java.lang.reflect.Constructor<?> a2 = /*EL:65*/v3.getDeclaredConstructor(v4);
        /*SL:66*/a2.setAccessible(true);
        /*SL:67*/return a2.newInstance(v2.getArguments().toArray());
    }
    
    protected void setProperties(final Object v-4, final Map<String, Object> v-3) throws Exception {
        /*SL:72*/if (v-3 == null) {
            /*SL:73*/throw new NullPointerException("Data for Compact Object Notation cannot be null.");
        }
        /*SL:75*/for (final Map.Entry<String, Object> entry : v-3.entrySet()) {
            final String a2 = /*EL:76*/entry.getKey();
            final Property v1 = /*EL:77*/this.getPropertyUtils().getProperty(v-4.getClass(), a2);
            try {
                /*SL:79*/v1.set(v-4, entry.getValue());
            }
            catch (IllegalArgumentException a2) {
                /*SL:81*/throw new YAMLException("Cannot set property='" + a2 + "' with value='" + v-3.get(a2) + /*EL:82*/"' (" + v-3.get(a2).getClass() + ") in " + v-4);
            }
        }
    }
    
    public CompactData getCompactData(final String v-9) {
        /*SL:88*/if (!v-9.endsWith(")")) {
            /*SL:89*/return null;
        }
        /*SL:91*/if (v-9.indexOf(40) < 0) {
            /*SL:92*/return null;
        }
        final Matcher matcher = CompactConstructor.FIRST_PATTERN.matcher(/*EL:94*/v-9);
        /*SL:95*/if (!matcher.matches()) {
            /*SL:119*/return null;
        }
        final String trim = matcher.group(1).trim();
        final String group = matcher.group(3);
        final CompactData compactData = new CompactData(trim);
        if (group.length() == 0) {
            return compactData;
        }
        final String[] split = group.split("\\s*,\\s*");
        for (int i = 0; i < split.length; ++i) {
            final String s = split[i];
            if (s.indexOf(61) < 0) {
                compactData.getArguments().add(s);
            }
            else {
                final Matcher matcher2 = CompactConstructor.PROPERTY_NAME_PATTERN.matcher(s);
                if (!matcher2.matches()) {
                    return null;
                }
                final String a1 = matcher2.group(1);
                final String v1 = matcher2.group(2).trim();
                compactData.getProperties().put(a1, v1);
            }
        }
        return compactData;
    }
    
    private Construct getCompactConstruct() {
        /*SL:123*/if (this.compactConstruct == null) {
            /*SL:124*/this.compactConstruct = this.createCompactConstruct();
        }
        /*SL:126*/return this.compactConstruct;
    }
    
    protected Construct createCompactConstruct() {
        /*SL:130*/return new ConstructCompactObject();
    }
    
    @Override
    protected Construct getConstructor(final Node v-2) {
        /*SL:135*/if (v-2 instanceof MappingNode) {
            final MappingNode mappingNode = /*EL:136*/(MappingNode)v-2;
            final List<NodeTuple> v0 = /*EL:137*/mappingNode.getValue();
            /*SL:138*/if (v0.size() == 1) {
                final NodeTuple v = /*EL:139*/v0.get(0);
                final Node v2 = /*EL:140*/v.getKeyNode();
                /*SL:141*/if (v2 instanceof ScalarNode) {
                    final ScalarNode a1 = /*EL:142*/(ScalarNode)v2;
                    /*SL:143*/if (CompactConstructor.GUESS_COMPACT.matcher(a1.getValue()).matches()) {
                        /*SL:144*/return this.getCompactConstruct();
                    }
                }
            }
        }
        else/*SL:148*/ if (v-2 instanceof ScalarNode) {
            final ScalarNode scalarNode = /*EL:149*/(ScalarNode)v-2;
            /*SL:150*/if (CompactConstructor.GUESS_COMPACT.matcher(scalarNode.getValue()).matches()) {
                /*SL:151*/return this.getCompactConstruct();
            }
        }
        /*SL:154*/return super.getConstructor(v-2);
    }
    
    protected void applySequence(final Object v2, final List<?> v3) {
        try {
            final Property a1 = /*EL:203*/this.getPropertyUtils().getProperty(v2.getClass(), this.getSequencePropertyName(v2.getClass()));
            /*SL:205*/a1.set(v2, v3);
        }
        catch (Exception a2) {
            /*SL:207*/throw new YAMLException(a2);
        }
    }
    
    protected String getSequencePropertyName(final Class<?> v-1) {
        final Set<Property> v0 = /*EL:218*/this.getPropertyUtils().getProperties(v-1);
        final Iterator<Property> v = /*EL:219*/v0.iterator();
        while (v.hasNext()) {
            final Property a1 = /*EL:220*/v.next();
            /*SL:221*/if (!List.class.isAssignableFrom(a1.getType())) {
                /*SL:222*/v.remove();
            }
        }
        /*SL:225*/if (v0.size() == 0) {
            /*SL:226*/throw new YAMLException("No list property found in " + v-1);
        }
        /*SL:227*/if (v0.size() > 1) {
            /*SL:228*/throw new YAMLException("Many list properties found in " + v-1 + "; Please override getSequencePropertyName() to specify which property to use.");
        }
        /*SL:233*/return v0.iterator().next().getName();
    }
    
    static {
        GUESS_COMPACT = Pattern.compile("\\p{Alpha}.*\\s*\\((?:,?\\s*(?:(?:\\w*)|(?:\\p{Alpha}\\w*\\s*=.+))\\s*)+\\)");
        FIRST_PATTERN = Pattern.compile("(\\p{Alpha}.*)(\\s*)\\((.*?)\\)");
        PROPERTY_NAME_PATTERN = Pattern.compile("\\s*(\\p{Alpha}\\w*)\\s*=(.+)");
    }
    
    public class ConstructCompactObject extends ConstructMapping
    {
        @Override
        public void construct2ndStep(final Node a1, final Object a2) {
            final MappingNode v1 = /*EL:162*/(MappingNode)a1;
            final NodeTuple v2 = /*EL:163*/v1.getValue().iterator().next();
            final Node v3 = /*EL:165*/v2.getValueNode();
            /*SL:167*/if (v3 instanceof MappingNode) {
                /*SL:168*/v3.setType(a2.getClass());
                /*SL:169*/this.constructJavaBean2ndStep((MappingNode)v3, a2);
            }
            else {
                /*SL:172*/CompactConstructor.this.applySequence(a2, CompactConstructor.this.constructSequence((SequenceNode)v3));
            }
        }
        
        @Override
        public Object construct(final Node v-2) {
            ScalarNode scalarNode = /*EL:181*/null;
            /*SL:182*/if (v-2 instanceof MappingNode) {
                final MappingNode a1 = /*EL:184*/(MappingNode)v-2;
                final NodeTuple v1 = /*EL:185*/a1.getValue().iterator().next();
                /*SL:186*/v-2.setTwoStepsConstruction(true);
                /*SL:187*/scalarNode = (ScalarNode)v1.getKeyNode();
            }
            else {
                /*SL:190*/scalarNode = (ScalarNode)v-2;
            }
            final CompactData v2 = /*EL:193*/CompactConstructor.this.getCompactData(scalarNode.getValue());
            /*SL:194*/if (v2 == null) {
                /*SL:195*/return CompactConstructor.this.constructScalar(scalarNode);
            }
            /*SL:197*/return CompactConstructor.this.constructCompactFormat(scalarNode, v2);
        }
    }
}

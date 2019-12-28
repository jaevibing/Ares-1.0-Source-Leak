package org.yaml.snakeyaml.constructor;

import org.yaml.snakeyaml.error.Mark;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.regex.Matcher;
import org.yaml.snakeyaml.error.YAMLException;
import java.util.TimeZone;
import java.util.Calendar;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;
import org.yaml.snakeyaml.nodes.ScalarNode;
import java.math.BigInteger;
import java.util.Set;
import org.yaml.snakeyaml.nodes.SequenceNode;
import org.yaml.snakeyaml.nodes.Node;
import java.util.Iterator;
import java.util.TreeSet;
import java.util.List;
import org.yaml.snakeyaml.nodes.NodeTuple;
import java.util.ArrayList;
import java.util.HashMap;
import org.yaml.snakeyaml.nodes.MappingNode;
import org.yaml.snakeyaml.nodes.NodeId;
import org.yaml.snakeyaml.nodes.Tag;
import java.util.regex.Pattern;
import java.util.Map;

public class SafeConstructor extends BaseConstructor
{
    public static final ConstructUndefined undefinedConstructor;
    private static final Map<String, Boolean> BOOL_VALUES;
    private static final Pattern TIMESTAMP_REGEXP;
    private static final Pattern YMD_REGEXP;
    
    public SafeConstructor() {
        this.yamlConstructors.put(Tag.NULL, new ConstructYamlNull());
        this.yamlConstructors.put(Tag.BOOL, new ConstructYamlBool());
        this.yamlConstructors.put(Tag.INT, new ConstructYamlInt());
        this.yamlConstructors.put(Tag.FLOAT, new ConstructYamlFloat());
        this.yamlConstructors.put(Tag.BINARY, new ConstructYamlBinary());
        this.yamlConstructors.put(Tag.TIMESTAMP, new ConstructYamlTimestamp());
        this.yamlConstructors.put(Tag.OMAP, new ConstructYamlOmap());
        this.yamlConstructors.put(Tag.PAIRS, new ConstructYamlPairs());
        this.yamlConstructors.put(Tag.SET, new ConstructYamlSet());
        this.yamlConstructors.put(Tag.STR, new ConstructYamlStr());
        this.yamlConstructors.put(Tag.SEQ, new ConstructYamlSeq());
        this.yamlConstructors.put(Tag.MAP, new ConstructYamlMap());
        this.yamlConstructors.put(null, SafeConstructor.undefinedConstructor);
        this.yamlClassConstructors.put(NodeId.scalar, SafeConstructor.undefinedConstructor);
        this.yamlClassConstructors.put(NodeId.sequence, SafeConstructor.undefinedConstructor);
        this.yamlClassConstructors.put(NodeId.mapping, SafeConstructor.undefinedConstructor);
    }
    
    protected void flattenMapping(final MappingNode a1) {
        /*SL:70*/this.processDuplicateKeys(a1);
        /*SL:71*/if (a1.isMerged()) {
            /*SL:72*/a1.setValue(this.mergeNode(a1, true, new HashMap<Object, Integer>(), new ArrayList<NodeTuple>()));
        }
    }
    
    protected void processDuplicateKeys(final MappingNode v-7) {
        final List<NodeTuple> value = /*EL:78*/v-7.getValue();
        final Map<Object, Integer> map = /*EL:79*/new HashMap<Object, Integer>(value.size());
        final TreeSet<Integer> set = /*EL:80*/new TreeSet<Integer>();
        int n = /*EL:81*/0;
        /*SL:82*/for (final NodeTuple nodeTuple : value) {
            final Node v0 = /*EL:83*/nodeTuple.getKeyNode();
            /*SL:84*/if (!v0.getTag().equals(Tag.MERGE)) {
                final Object v = /*EL:85*/this.constructObject(v0);
                /*SL:86*/if (v != null) {
                    try {
                        /*SL:88*/v.hashCode();
                    }
                    catch (Exception a1) {
                        /*SL:90*/throw new ConstructorException("while constructing a mapping", v-7.getStartMark(), /*EL:91*/"found unacceptable key " + v, nodeTuple.getKeyNode().getStartMark(), /*EL:92*/a1);
                    }
                }
                final Integer v2 = /*EL:96*/map.put(v, n);
                /*SL:97*/if (v2 != null) {
                    /*SL:98*/if (!this.isAllowDuplicateKeys()) {
                        /*SL:99*/throw new IllegalStateException("duplicate key: " + v);
                    }
                    /*SL:101*/set.add(v2);
                }
            }
            /*SL:104*/++n;
        }
        final Iterator<Integer> descendingIterator = /*EL:107*/set.descendingIterator();
        /*SL:108*/while (descendingIterator.hasNext()) {
            /*SL:109*/value.remove((int)descendingIterator.next());
        }
    }
    
    private List<NodeTuple> mergeNode(final MappingNode v-9, final boolean v-8, final Map<Object, Integer> v-7, final List<NodeTuple> v-6) {
        final Iterator<NodeTuple> iterator = /*EL:129*/v-9.getValue().iterator();
        /*SL:130*/while (iterator.hasNext()) {
            final NodeTuple nodeTuple = /*EL:131*/iterator.next();
            final Node keyNode = /*EL:132*/nodeTuple.getKeyNode();
            final Node valueNode = /*EL:133*/nodeTuple.getValueNode();
            /*SL:134*/if (keyNode.getTag().equals(Tag.MERGE)) {
                /*SL:135*/iterator.remove();
                /*SL:136*/switch (valueNode.getNodeId()) {
                    case mapping: {
                        final MappingNode a1 = /*EL:138*/(MappingNode)valueNode;
                        /*SL:139*/this.mergeNode(a1, false, v-7, v-6);
                        /*SL:140*/continue;
                    }
                    case sequence: {
                        final SequenceNode a2 = /*EL:142*/(SequenceNode)valueNode;
                        final List<Node> v1 = /*EL:143*/a2.getValue();
                        /*SL:144*/for (final Node a3 : v1) {
                            /*SL:145*/if (!(a3 instanceof MappingNode)) {
                                /*SL:146*/throw new ConstructorException("while constructing a mapping", v-9.getStartMark(), /*EL:147*/"expected a mapping for merging, but found " + a3.getNodeId(), /*EL:149*/a3.getStartMark());
                            }
                            final MappingNode a4 = /*EL:152*/(MappingNode)a3;
                            /*SL:153*/this.mergeNode(a4, false, v-7, v-6);
                        }
                        /*SL:155*/continue;
                    }
                    default: {
                        /*SL:157*/throw new ConstructorException("while constructing a mapping", v-9.getStartMark(), /*EL:158*/"expected a mapping or list of mappings for merging, but found " + valueNode.getNodeId(), /*EL:160*/valueNode.getStartMark());
                    }
                }
            }
            else {
                final Object constructObject = /*EL:165*/this.constructObject(keyNode);
                /*SL:166*/if (!v-7.containsKey(constructObject)) {
                    /*SL:167*/v-6.add(nodeTuple);
                    /*SL:169*/v-7.put(constructObject, v-6.size() - 1);
                }
                else {
                    /*SL:170*/if (!v-8) {
                        continue;
                    }
                    /*SL:173*/v-6.set(v-7.get(constructObject), nodeTuple);
                }
            }
        }
        /*SL:177*/return v-6;
    }
    
    @Override
    protected void constructMapping2ndStep(final MappingNode a1, final Map<Object, Object> a2) {
        /*SL:182*/this.flattenMapping(a1);
        /*SL:183*/super.constructMapping2ndStep(a1, a2);
    }
    
    @Override
    protected void constructSet2ndStep(final MappingNode a1, final Set<Object> a2) {
        /*SL:188*/this.flattenMapping(a1);
        /*SL:189*/super.constructSet2ndStep(a1, a2);
    }
    
    private Number createNumber(final int v-3, String v-2, final int v-1) {
        /*SL:260*/if (v-3 < 0) {
            /*SL:261*/v-2 = "-" + v-2;
        }
        Number v0 = null;
        try {
            final Number a1 = /*EL:264*/Integer.valueOf(v-2, v-1);
        }
        catch (NumberFormatException v) {
            try {
                final Number a2 = /*EL:267*/Long.valueOf(v-2, v-1);
            }
            catch (NumberFormatException a3) {
                /*SL:269*/v0 = new BigInteger(v-2, v-1);
            }
        }
        /*SL:272*/return v0;
    }
    
    static {
        undefinedConstructor = new ConstructUndefined();
        (BOOL_VALUES = new HashMap<String, Boolean>()).put("yes", Boolean.TRUE);
        SafeConstructor.BOOL_VALUES.put("no", Boolean.FALSE);
        SafeConstructor.BOOL_VALUES.put("true", Boolean.TRUE);
        SafeConstructor.BOOL_VALUES.put("false", Boolean.FALSE);
        SafeConstructor.BOOL_VALUES.put("on", Boolean.TRUE);
        SafeConstructor.BOOL_VALUES.put("off", Boolean.FALSE);
        TIMESTAMP_REGEXP = Pattern.compile("^([0-9][0-9][0-9][0-9])-([0-9][0-9]?)-([0-9][0-9]?)(?:(?:[Tt]|[ \t]+)([0-9][0-9]?):([0-9][0-9]):([0-9][0-9])(?:\\.([0-9]*))?(?:[ \t]*(?:Z|([-+][0-9][0-9]?)(?::([0-9][0-9])?)?))?)?$");
        YMD_REGEXP = Pattern.compile("^([0-9][0-9][0-9][0-9])-([0-9][0-9]?)-([0-9][0-9]?)$");
    }
    
    public class ConstructYamlNull extends AbstractConstruct
    {
        @Override
        public Object construct(final Node a1) {
            /*SL:195*/SafeConstructor.this.constructScalar((ScalarNode)a1);
            /*SL:196*/return null;
        }
    }
    
    public class ConstructYamlBool extends AbstractConstruct
    {
        @Override
        public Object construct(final Node a1) {
            final String v1 = /*EL:213*/(String)SafeConstructor.this.constructScalar((ScalarNode)a1);
            /*SL:214*/return SafeConstructor.BOOL_VALUES.get(v1.toLowerCase());
        }
    }
    
    public class ConstructYamlInt extends AbstractConstruct
    {
        @Override
        public Object construct(final Node v-8) {
            String s = /*EL:221*/SafeConstructor.this.constructScalar((ScalarNode)v-8).toString().replaceAll("_", "");
            int a2 = /*EL:222*/1;
            final char char1 = /*EL:223*/s.charAt(0);
            /*SL:224*/if (char1 == '-') {
                /*SL:225*/a2 = -1;
                /*SL:226*/s = s.substring(1);
            }
            else/*SL:227*/ if (char1 == '+') {
                /*SL:228*/s = s.substring(1);
            }
            int a3 = /*EL:230*/10;
            /*SL:231*/if ("0".equals(s)) {
                /*SL:232*/return 0;
            }
            /*SL:233*/if (s.startsWith("0b")) {
                /*SL:234*/s = s.substring(2);
                /*SL:235*/a3 = 2;
            }
            else/*SL:236*/ if (s.startsWith("0x")) {
                /*SL:237*/s = s.substring(2);
                /*SL:238*/a3 = 16;
            }
            else/*SL:239*/ if (s.startsWith("0")) {
                /*SL:240*/s = s.substring(1);
                /*SL:241*/a3 = 8;
            }
            else {
                /*SL:242*/if (s.indexOf(58) != -1) {
                    final String[] split = /*EL:243*/s.split(":");
                    int n = /*EL:244*/1;
                    int n2 = /*EL:245*/0;
                    /*SL:246*/for (int a1 = 0, v1 = split.length; a1 < v1; ++a1) {
                        /*SL:247*/n2 += (int)(Long.parseLong(split[v1 - a1 - 1]) * n);
                        /*SL:248*/n *= 60;
                    }
                    /*SL:250*/return SafeConstructor.this.createNumber(a2, String.valueOf(n2), 10);
                }
                /*SL:252*/return SafeConstructor.this.createNumber(a2, s, 10);
            }
            /*SL:254*/return SafeConstructor.this.createNumber(a2, s, a3);
        }
    }
    
    public class ConstructYamlFloat extends AbstractConstruct
    {
        @Override
        public Object construct(final Node v-9) {
            String s = /*EL:278*/SafeConstructor.this.constructScalar((ScalarNode)v-9).toString().replaceAll("_", "");
            int n = /*EL:279*/1;
            final char char1 = /*EL:280*/s.charAt(0);
            /*SL:281*/if (char1 == '-') {
                /*SL:282*/n = -1;
                /*SL:283*/s = s.substring(1);
            }
            else/*SL:284*/ if (char1 == '+') {
                /*SL:285*/s = s.substring(1);
            }
            final String lowerCase = /*EL:287*/s.toLowerCase();
            /*SL:288*/if (".inf".equals(lowerCase)) {
                /*SL:289*/return new Double((n == -1) ? Double.NEGATIVE_INFINITY : Double.POSITIVE_INFINITY);
            }
            /*SL:290*/if (".nan".equals(lowerCase)) {
                /*SL:291*/return new Double(Double.NaN);
            }
            /*SL:292*/if (s.indexOf(58) != -1) {
                final String[] split = /*EL:293*/s.split(":");
                int n2 = /*EL:294*/1;
                double n3 = /*EL:295*/0.0;
                /*SL:296*/for (int a1 = 0, v1 = split.length; a1 < v1; ++a1) {
                    /*SL:297*/n3 += Double.parseDouble(split[v1 - a1 - 1]) * n2;
                    /*SL:298*/n2 *= 60;
                }
                /*SL:300*/return new Double(n * n3);
            }
            final Double value = /*EL:302*/Double.valueOf(s);
            /*SL:303*/return new Double(value * n);
        }
    }
    
    public class ConstructYamlBinary extends AbstractConstruct
    {
        @Override
        public Object construct(final Node a1) {
            final String v1 = /*EL:312*/SafeConstructor.this.constructScalar((ScalarNode)a1).toString().replaceAll("\\s", "");
            final byte[] v2 = /*EL:314*/Base64Coder.decode(v1.toCharArray());
            /*SL:315*/return v2;
        }
    }
    
    public static class ConstructYamlTimestamp extends AbstractConstruct
    {
        private Calendar calendar;
        
        public Calendar getCalendar() {
            /*SL:328*/return this.calendar;
        }
        
        @Override
        public Object construct(final Node v-4) {
            final ScalarNode scalarNode = /*EL:333*/(ScalarNode)v-4;
            final String value = /*EL:334*/scalarNode.getValue();
            Matcher matcher = /*EL:335*/SafeConstructor.YMD_REGEXP.matcher(value);
            /*SL:336*/if (matcher.matches()) {
                final String a1 = /*EL:337*/matcher.group(1);
                final String v1 = /*EL:338*/matcher.group(2);
                final String v2 = /*EL:339*/matcher.group(3);
                /*SL:340*/(this.calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))).clear();
                /*SL:342*/this.calendar.set(1, Integer.parseInt(a1));
                /*SL:344*/this.calendar.set(2, Integer.parseInt(v1) - 1);
                /*SL:345*/this.calendar.set(5, Integer.parseInt(v2));
                /*SL:346*/return this.calendar.getTime();
            }
            /*SL:348*/matcher = SafeConstructor.TIMESTAMP_REGEXP.matcher(value);
            /*SL:349*/if (!matcher.matches()) {
                /*SL:350*/throw new YAMLException("Unexpected timestamp: " + value);
            }
            final String v3 = /*EL:352*/matcher.group(1);
            final String v1 = /*EL:353*/matcher.group(2);
            final String v2 = /*EL:354*/matcher.group(3);
            final String v4 = /*EL:355*/matcher.group(4);
            final String v5 = /*EL:356*/matcher.group(5);
            String v6 = /*EL:358*/matcher.group(6);
            final String v7 = /*EL:359*/matcher.group(7);
            /*SL:360*/if (v7 != null) {
                /*SL:361*/v6 = v6 + "." + v7;
            }
            final double v8 = /*EL:363*/Double.parseDouble(v6);
            final int v9 = /*EL:364*/(int)Math.round(Math.floor(v8));
            final int v10 = /*EL:365*/(int)Math.round((v8 - v9) * 1000.0);
            final String v11 = /*EL:367*/matcher.group(8);
            final String v12 = /*EL:368*/matcher.group(9);
            TimeZone v14;
            /*SL:370*/if (v11 != null) {
                final String v13 = /*EL:371*/(v12 != null) ? (":" + v12) : "00";
                /*SL:372*/v14 = TimeZone.getTimeZone("GMT" + v11 + v13);
            }
            else {
                /*SL:375*/v14 = TimeZone.getTimeZone("UTC");
            }
            /*SL:377*/(this.calendar = Calendar.getInstance(v14)).set(/*EL:378*/1, Integer.parseInt(v3));
            /*SL:380*/this.calendar.set(2, Integer.parseInt(v1) - 1);
            /*SL:381*/this.calendar.set(5, Integer.parseInt(v2));
            /*SL:382*/this.calendar.set(11, Integer.parseInt(v4));
            /*SL:383*/this.calendar.set(12, Integer.parseInt(v5));
            /*SL:384*/this.calendar.set(13, v9);
            /*SL:385*/this.calendar.set(14, v10);
            /*SL:386*/return this.calendar.getTime();
        }
    }
    
    public class ConstructYamlOmap extends AbstractConstruct
    {
        @Override
        public Object construct(final Node v-5) {
            final Map<Object, Object> map = /*EL:396*/new LinkedHashMap<Object, Object>();
            /*SL:397*/if (!(v-5 instanceof SequenceNode)) {
                /*SL:398*/throw new ConstructorException("while constructing an ordered map", v-5.getStartMark(), /*EL:399*/"expected a sequence, but found " + v-5.getNodeId(), v-5.getStartMark());
            }
            final SequenceNode sequenceNode = /*EL:402*/(SequenceNode)v-5;
            /*SL:403*/for (final Node node : sequenceNode.getValue()) {
                /*SL:404*/if (!(node instanceof MappingNode)) {
                    /*SL:405*/throw new ConstructorException("while constructing an ordered map", v-5.getStartMark(), /*EL:406*/"expected a mapping of length 1, but found " + node.getNodeId(), /*EL:407*/node.getStartMark());
                }
                final MappingNode a1 = /*EL:410*/(MappingNode)node;
                /*SL:411*/if (a1.getValue().size() != 1) {
                    /*SL:412*/throw new ConstructorException("while constructing an ordered map", v-5.getStartMark(), /*EL:413*/"expected a single mapping item, but found " + a1.getValue().size() + /*EL:414*/" items", a1.getStartMark());
                }
                final Node v1 = /*EL:417*/a1.getValue().get(0).getKeyNode();
                final Node v2 = /*EL:418*/a1.getValue().get(0).getValueNode();
                final Object v3 = /*EL:419*/SafeConstructor.this.constructObject(v1);
                final Object v4 = /*EL:420*/SafeConstructor.this.constructObject(v2);
                /*SL:421*/map.put(v3, v4);
            }
            /*SL:423*/return map;
        }
    }
    
    public class ConstructYamlPairs extends AbstractConstruct
    {
        @Override
        public Object construct(final Node v-5) {
            /*SL:433*/if (!(v-5 instanceof SequenceNode)) {
                /*SL:434*/throw new ConstructorException("while constructing pairs", v-5.getStartMark(), "expected a sequence, but found " + v-5.getNodeId(), /*EL:435*/v-5.getStartMark());
            }
            final SequenceNode sequenceNode = /*EL:437*/(SequenceNode)v-5;
            final List<Object[]> list = /*EL:438*/new ArrayList<Object[]>(sequenceNode.getValue().size());
            /*SL:439*/for (final Node node : sequenceNode.getValue()) {
                /*SL:440*/if (!(node instanceof MappingNode)) {
                    /*SL:441*/throw new ConstructorException("while constructingpairs", v-5.getStartMark(), "expected a mapping of length 1, but found " + node.getNodeId(), /*EL:442*/node.getStartMark());
                }
                final MappingNode a1 = /*EL:445*/(MappingNode)node;
                /*SL:446*/if (a1.getValue().size() != 1) {
                    /*SL:447*/throw new ConstructorException("while constructing pairs", v-5.getStartMark(), "expected a single mapping item, but found " + a1.getValue().size() + /*EL:448*/" items", a1.getStartMark());
                }
                final Node v1 = /*EL:452*/a1.getValue().get(0).getKeyNode();
                final Node v2 = /*EL:453*/a1.getValue().get(0).getValueNode();
                final Object v3 = /*EL:454*/SafeConstructor.this.constructObject(v1);
                final Object v4 = /*EL:455*/SafeConstructor.this.constructObject(v2);
                /*SL:456*/list.add(new Object[] { v3, v4 });
            }
            /*SL:458*/return list;
        }
    }
    
    public class ConstructYamlSet implements Construct
    {
        @Override
        public Object construct(final Node a1) {
            /*SL:465*/if (a1.isTwoStepsConstruction()) {
                /*SL:466*/return SafeConstructor.this.constructedObjects.containsKey(a1) ? SafeConstructor.this.constructedObjects.get(a1) : SafeConstructor.this.createDefaultSet();
            }
            /*SL:469*/return SafeConstructor.this.constructSet((MappingNode)a1);
        }
        
        @Override
        public void construct2ndStep(final Node a1, final Object a2) {
            /*SL:476*/if (a1.isTwoStepsConstruction()) {
                /*SL:477*/SafeConstructor.this.constructSet2ndStep((MappingNode)a1, (Set<Object>)a2);
                /*SL:481*/return;
            }
            throw new YAMLException("Unexpected recursive set structure. Node: " + a1);
        }
    }
    
    public class ConstructYamlStr extends AbstractConstruct
    {
        @Override
        public Object construct(final Node a1) {
            /*SL:487*/return SafeConstructor.this.constructScalar((ScalarNode)a1);
        }
    }
    
    public class ConstructYamlSeq implements Construct
    {
        @Override
        public Object construct(final Node a1) {
            final SequenceNode v1 = /*EL:494*/(SequenceNode)a1;
            /*SL:495*/if (a1.isTwoStepsConstruction()) {
                /*SL:496*/return SafeConstructor.this.newList(v1);
            }
            /*SL:498*/return SafeConstructor.this.constructSequence(v1);
        }
        
        @Override
        public void construct2ndStep(final Node a1, final Object a2) {
            /*SL:505*/if (a1.isTwoStepsConstruction()) {
                /*SL:506*/SafeConstructor.this.constructSequenceStep2((SequenceNode)a1, (Collection<Object>)a2);
                /*SL:510*/return;
            }
            throw new YAMLException("Unexpected recursive sequence structure. Node: " + a1);
        }
    }
    
    public class ConstructYamlMap implements Construct
    {
        @Override
        public Object construct(final Node a1) {
            /*SL:516*/if (a1.isTwoStepsConstruction()) {
                /*SL:517*/return SafeConstructor.this.createDefaultMap();
            }
            /*SL:519*/return SafeConstructor.this.constructMapping((MappingNode)a1);
        }
        
        @Override
        public void construct2ndStep(final Node a1, final Object a2) {
            /*SL:526*/if (a1.isTwoStepsConstruction()) {
                /*SL:527*/SafeConstructor.this.constructMapping2ndStep((MappingNode)a1, (Map<Object, Object>)a2);
                /*SL:531*/return;
            }
            throw new YAMLException("Unexpected recursive mapping structure. Node: " + a1);
        }
    }
    
    public static final class ConstructUndefined extends AbstractConstruct
    {
        @Override
        public Object construct(final Node a1) {
            /*SL:537*/throw new ConstructorException(null, null, "could not determine a constructor for the tag " + a1.getTag(), /*EL:538*/a1.getStartMark());
        }
    }
}

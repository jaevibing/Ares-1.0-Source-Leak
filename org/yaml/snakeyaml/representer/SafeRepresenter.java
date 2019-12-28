package org.yaml.snakeyaml.representer;

import java.io.ObjectInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.math.BigInteger;
import java.io.UnsupportedEncodingException;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;
import org.yaml.snakeyaml.error.YAMLException;
import org.yaml.snakeyaml.reader.StreamReader;
import org.yaml.snakeyaml.nodes.Node;
import java.util.HashMap;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;
import java.util.TimeZone;
import org.yaml.snakeyaml.nodes.Tag;
import java.util.Map;

class SafeRepresenter extends BaseRepresenter
{
    protected Map<Class<?>, Tag> classTags;
    protected TimeZone timeZone;
    public static Pattern MULTILINE_PATTERN;
    
    public SafeRepresenter() {
        this.timeZone = null;
        this.nullRepresenter = new RepresentNull();
        this.representers.put(String.class, new RepresentString());
        this.representers.put(Boolean.class, new RepresentBoolean());
        this.representers.put(Character.class, new RepresentString());
        this.representers.put(UUID.class, new RepresentUuid());
        this.representers.put(byte[].class, new RepresentByteArray());
        final Represent v1 = new RepresentPrimitiveArray();
        this.representers.put(short[].class, v1);
        this.representers.put(int[].class, v1);
        this.representers.put(long[].class, v1);
        this.representers.put(float[].class, v1);
        this.representers.put(double[].class, v1);
        this.representers.put(char[].class, v1);
        this.representers.put(boolean[].class, v1);
        this.multiRepresenters.put(Number.class, new RepresentNumber());
        this.multiRepresenters.put(List.class, new RepresentList());
        this.multiRepresenters.put(Map.class, new RepresentMap());
        this.multiRepresenters.put(Set.class, new RepresentSet());
        this.multiRepresenters.put(Iterator.class, new RepresentIterator());
        this.multiRepresenters.put(new Object[0].getClass(), new RepresentArray());
        this.multiRepresenters.put(Date.class, new RepresentDate());
        this.multiRepresenters.put(Enum.class, new RepresentEnum());
        this.multiRepresenters.put(Calendar.class, new RepresentDate());
        this.classTags = new HashMap<Class<?>, Tag>();
    }
    
    protected Tag getTag(final Class<?> a1, final Tag a2) {
        /*SL:78*/if (this.classTags.containsKey(a1)) {
            /*SL:79*/return this.classTags.get(a1);
        }
        /*SL:81*/return a2;
    }
    
    public Tag addClassTag(final Class<?> a1, final Tag a2) {
        /*SL:96*/if (a2 == null) {
            /*SL:97*/throw new NullPointerException("Tag must be provided.");
        }
        /*SL:99*/return this.classTags.put(a1, a2);
    }
    
    public TimeZone getTimeZone() {
        /*SL:438*/return this.timeZone;
    }
    
    public void setTimeZone(final TimeZone a1) {
        /*SL:442*/this.timeZone = a1;
    }
    
    static {
        SafeRepresenter.MULTILINE_PATTERN = Pattern.compile("\n|\u0085|\u2028|\u2029");
    }
    
    protected class RepresentNull implements Represent
    {
        @Override
        public Node representData(final Object a1) {
            /*SL:104*/return SafeRepresenter.this.representScalar(Tag.NULL, "null");
        }
    }
    
    protected class RepresentString implements Represent
    {
        @Override
        public Node representData(final Object v-5) {
            Tag a2 = Tag.STR;
            Character a3 = /*EL:113*/null;
            String s = /*EL:114*/v-5.toString();
            /*SL:115*/if (!StreamReader.isPrintable(s)) {
                /*SL:116*/a2 = Tag.BINARY;
                char[] encode;
                try {
                    final byte[] a1 = /*EL:119*/s.getBytes("UTF-8");
                    final String v1 = /*EL:123*/new String(a1, "UTF-8");
                    /*SL:124*/if (!v1.equals(s)) {
                        /*SL:125*/throw new YAMLException("invalid string value has occurred");
                    }
                    /*SL:127*/encode = Base64Coder.encode(a1);
                }
                catch (UnsupportedEncodingException v2) {
                    /*SL:129*/throw new YAMLException(v2);
                }
                /*SL:131*/s = String.valueOf(encode);
                /*SL:132*/a3 = '|';
            }
            /*SL:136*/if (SafeRepresenter.this.defaultScalarStyle == null && SafeRepresenter.MULTILINE_PATTERN.matcher(s).find()) {
                /*SL:137*/a3 = '|';
            }
            /*SL:139*/return SafeRepresenter.this.representScalar(a2, s, a3);
        }
    }
    
    protected class RepresentBoolean implements Represent
    {
        @Override
        public Node representData(final Object v2) {
            final String v3;
            /*SL:146*/if (Boolean.TRUE.equals(v2)) {
                final String a1 = /*EL:147*/"true";
            }
            else {
                /*SL:149*/v3 = "false";
            }
            /*SL:151*/return SafeRepresenter.this.representScalar(Tag.BOOL, v3);
        }
    }
    
    protected class RepresentNumber implements Represent
    {
        @Override
        public Node representData(final Object v-1) {
            String v1;
            final Tag v3;
            /*SL:159*/if (v-1 instanceof Byte || v-1 instanceof Short || v-1 instanceof Integer || v-1 instanceof Long || v-1 instanceof BigInteger) {
                final Tag a1 = Tag.INT;
                /*SL:162*/v1 = v-1.toString();
            }
            else {
                final Number v2 = /*EL:164*/(Number)v-1;
                /*SL:165*/v3 = Tag.FLOAT;
                /*SL:166*/if (v2.equals(Double.NaN)) {
                    /*SL:167*/v1 = ".NaN";
                }
                else/*SL:168*/ if (v2.equals(Double.POSITIVE_INFINITY)) {
                    /*SL:169*/v1 = ".inf";
                }
                else/*SL:170*/ if (v2.equals(Double.NEGATIVE_INFINITY)) {
                    /*SL:171*/v1 = "-.inf";
                }
                else {
                    /*SL:173*/v1 = v2.toString();
                }
            }
            /*SL:176*/return SafeRepresenter.this.representScalar(SafeRepresenter.this.getTag(v-1.getClass(), v3), v1);
        }
    }
    
    protected class RepresentList implements Represent
    {
        @Override
        public Node representData(final Object a1) {
            /*SL:183*/return SafeRepresenter.this.representSequence(SafeRepresenter.this.getTag(a1.getClass(), Tag.SEQ), (Iterable<?>)a1, null);
        }
    }
    
    protected class RepresentIterator implements Represent
    {
        @Override
        public Node representData(final Object a1) {
            final Iterator<Object> v1 = /*EL:190*/(Iterator<Object>)a1;
            /*SL:191*/return SafeRepresenter.this.representSequence(SafeRepresenter.this.getTag(a1.getClass(), Tag.SEQ), new IteratorWrapper(v1), null);
        }
    }
    
    private static class IteratorWrapper implements Iterable<Object>
    {
        private Iterator<Object> iter;
        
        public IteratorWrapper(final Iterator<Object> a1) {
            this.iter = a1;
        }
        
        @Override
        public Iterator<Object> iterator() {
            /*SL:204*/return this.iter;
        }
    }
    
    protected class RepresentArray implements Represent
    {
        @Override
        public Node representData(final Object a1) {
            final Object[] v1 = /*EL:210*/(Object[])a1;
            final List<Object> v2 = /*EL:211*/Arrays.<Object>asList(v1);
            /*SL:212*/return SafeRepresenter.this.representSequence(Tag.SEQ, v2, null);
        }
    }
    
    protected class RepresentPrimitiveArray implements Represent
    {
        @Override
        public Node representData(final Object a1) {
            final Class<?> v1 = /*EL:223*/a1.getClass().getComponentType();
            /*SL:225*/if (Byte.TYPE == v1) {
                /*SL:226*/return SafeRepresenter.this.representSequence(Tag.SEQ, this.asByteList(a1), null);
            }
            /*SL:227*/if (Short.TYPE == v1) {
                /*SL:228*/return SafeRepresenter.this.representSequence(Tag.SEQ, this.asShortList(a1), null);
            }
            /*SL:229*/if (Integer.TYPE == v1) {
                /*SL:230*/return SafeRepresenter.this.representSequence(Tag.SEQ, this.asIntList(a1), null);
            }
            /*SL:231*/if (Long.TYPE == v1) {
                /*SL:232*/return SafeRepresenter.this.representSequence(Tag.SEQ, this.asLongList(a1), null);
            }
            /*SL:233*/if (Float.TYPE == v1) {
                /*SL:234*/return SafeRepresenter.this.representSequence(Tag.SEQ, this.asFloatList(a1), null);
            }
            /*SL:235*/if (Double.TYPE == v1) {
                /*SL:236*/return SafeRepresenter.this.representSequence(Tag.SEQ, this.asDoubleList(a1), null);
            }
            /*SL:237*/if (Character.TYPE == v1) {
                /*SL:238*/return SafeRepresenter.this.representSequence(Tag.SEQ, this.asCharList(a1), null);
            }
            /*SL:239*/if (Boolean.TYPE == v1) {
                /*SL:240*/return SafeRepresenter.this.representSequence(Tag.SEQ, this.asBooleanList(a1), null);
            }
            /*SL:243*/throw new YAMLException("Unexpected primitive '" + v1.getCanonicalName() + "'");
        }
        
        private List<Byte> asByteList(final Object v2) {
            final byte[] v3 = /*EL:247*/(byte[])v2;
            final List<Byte> v4 = /*EL:248*/new ArrayList<Byte>(v3.length);
            /*SL:249*/for (int a1 = 0; a1 < v3.length; ++a1) {
                /*SL:250*/v4.add(v3[a1]);
            }
            /*SL:251*/return v4;
        }
        
        private List<Short> asShortList(final Object v2) {
            final short[] v3 = /*EL:255*/(short[])v2;
            final List<Short> v4 = /*EL:256*/new ArrayList<Short>(v3.length);
            /*SL:257*/for (int a1 = 0; a1 < v3.length; ++a1) {
                /*SL:258*/v4.add(v3[a1]);
            }
            /*SL:259*/return v4;
        }
        
        private List<Integer> asIntList(final Object v2) {
            final int[] v3 = /*EL:263*/(int[])v2;
            final List<Integer> v4 = /*EL:264*/new ArrayList<Integer>(v3.length);
            /*SL:265*/for (int a1 = 0; a1 < v3.length; ++a1) {
                /*SL:266*/v4.add(v3[a1]);
            }
            /*SL:267*/return v4;
        }
        
        private List<Long> asLongList(final Object v2) {
            final long[] v3 = /*EL:271*/(long[])v2;
            final List<Long> v4 = /*EL:272*/new ArrayList<Long>(v3.length);
            /*SL:273*/for (int a1 = 0; a1 < v3.length; ++a1) {
                /*SL:274*/v4.add(v3[a1]);
            }
            /*SL:275*/return v4;
        }
        
        private List<Float> asFloatList(final Object v2) {
            final float[] v3 = /*EL:279*/(float[])v2;
            final List<Float> v4 = /*EL:280*/new ArrayList<Float>(v3.length);
            /*SL:281*/for (int a1 = 0; a1 < v3.length; ++a1) {
                /*SL:282*/v4.add(v3[a1]);
            }
            /*SL:283*/return v4;
        }
        
        private List<Double> asDoubleList(final Object v2) {
            final double[] v3 = /*EL:287*/(double[])v2;
            final List<Double> v4 = /*EL:288*/new ArrayList<Double>(v3.length);
            /*SL:289*/for (int a1 = 0; a1 < v3.length; ++a1) {
                /*SL:290*/v4.add(v3[a1]);
            }
            /*SL:291*/return v4;
        }
        
        private List<Character> asCharList(final Object v2) {
            final char[] v3 = /*EL:295*/(char[])v2;
            final List<Character> v4 = /*EL:296*/new ArrayList<Character>(v3.length);
            /*SL:297*/for (int a1 = 0; a1 < v3.length; ++a1) {
                /*SL:298*/v4.add(v3[a1]);
            }
            /*SL:299*/return v4;
        }
        
        private List<Boolean> asBooleanList(final Object v2) {
            final boolean[] v3 = /*EL:303*/(boolean[])v2;
            final List<Boolean> v4 = /*EL:304*/new ArrayList<Boolean>(v3.length);
            /*SL:305*/for (int a1 = 0; a1 < v3.length; ++a1) {
                /*SL:306*/v4.add(v3[a1]);
            }
            /*SL:307*/return v4;
        }
    }
    
    protected class RepresentMap implements Represent
    {
        @Override
        public Node representData(final Object a1) {
            /*SL:314*/return SafeRepresenter.this.representMapping(SafeRepresenter.this.getTag(a1.getClass(), Tag.MAP), (Map<?, ?>)a1, null);
        }
    }
    
    protected class RepresentSet implements Represent
    {
        @Override
        public Node representData(final Object v2) {
            final Map<Object, Object> v3 = /*EL:322*/new LinkedHashMap<Object, Object>();
            final Set<Object> v4 = /*EL:323*/(Set<Object>)v2;
            /*SL:324*/for (final Object a1 : v4) {
                /*SL:325*/v3.put(a1, null);
            }
            /*SL:327*/return SafeRepresenter.this.representMapping(SafeRepresenter.this.getTag(v2.getClass(), Tag.SET), v3, null);
        }
    }
    
    protected class RepresentDate implements Represent
    {
        @Override
        public Node representData(final Object v-10) {
            final Calendar instance;
            /*SL:335*/if (v-10 instanceof Calendar) {
                final Calendar a1 = /*EL:336*/(Calendar)v-10;
            }
            else {
                /*SL:338*/instance = Calendar.getInstance((SafeRepresenter.this.getTimeZone() == null) ? TimeZone.getTimeZone("UTC") : SafeRepresenter.this.timeZone);
                /*SL:340*/instance.setTime((Date)v-10);
            }
            final int value = /*EL:342*/instance.get(1);
            final int n = /*EL:343*/instance.get(2) + 1;
            final int value2 = /*EL:344*/instance.get(5);
            final int value3 = /*EL:345*/instance.get(11);
            final int value4 = /*EL:346*/instance.get(12);
            final int value5 = /*EL:347*/instance.get(13);
            final int value6 = /*EL:348*/instance.get(14);
            final StringBuilder sb = /*EL:349*/new StringBuilder(String.valueOf(value));
            /*SL:350*/while (sb.length() < 4) {
                /*SL:352*/sb.insert(0, "0");
            }
            /*SL:354*/sb.append("-");
            /*SL:355*/if (n < 10) {
                /*SL:356*/sb.append("0");
            }
            /*SL:358*/sb.append(String.valueOf(n));
            /*SL:359*/sb.append("-");
            /*SL:360*/if (value2 < 10) {
                /*SL:361*/sb.append("0");
            }
            /*SL:363*/sb.append(String.valueOf(value2));
            /*SL:364*/sb.append("T");
            /*SL:365*/if (value3 < 10) {
                /*SL:366*/sb.append("0");
            }
            /*SL:368*/sb.append(String.valueOf(value3));
            /*SL:369*/sb.append(":");
            /*SL:370*/if (value4 < 10) {
                /*SL:371*/sb.append("0");
            }
            /*SL:373*/sb.append(String.valueOf(value4));
            /*SL:374*/sb.append(":");
            /*SL:375*/if (value5 < 10) {
                /*SL:376*/sb.append("0");
            }
            /*SL:378*/sb.append(String.valueOf(value5));
            /*SL:379*/if (value6 > 0) {
                /*SL:380*/if (value6 < 10) {
                    /*SL:381*/sb.append(".00");
                }
                else/*SL:382*/ if (value6 < 100) {
                    /*SL:383*/sb.append(".0");
                }
                else {
                    /*SL:385*/sb.append(".");
                }
                /*SL:387*/sb.append(String.valueOf(value6));
            }
            int v0 = /*EL:391*/instance.getTimeZone().getOffset(instance.get(0), instance.get(1), /*EL:392*/instance.get(2), instance.get(5), /*EL:393*/instance.get(7), instance.get(14));
            /*SL:395*/if (v0 == 0) {
                /*SL:396*/sb.append('Z');
            }
            else {
                /*SL:398*/if (v0 < 0) {
                    /*SL:399*/sb.append('-');
                    /*SL:400*/v0 *= -1;
                }
                else {
                    /*SL:402*/sb.append('+');
                }
                final int v = /*EL:404*/v0 / 60000;
                final int v2 = /*EL:405*/v / 60;
                final int v3 = /*EL:406*/v % 60;
                /*SL:408*/if (v2 < 10) {
                    /*SL:409*/sb.append('0');
                }
                /*SL:411*/sb.append(v2);
                /*SL:412*/sb.append(':');
                /*SL:413*/if (v3 < 10) {
                    /*SL:414*/sb.append('0');
                }
                /*SL:416*/sb.append(v3);
            }
            /*SL:419*/return SafeRepresenter.this.representScalar(SafeRepresenter.this.getTag(v-10.getClass(), Tag.TIMESTAMP), sb.toString(), null);
        }
    }
    
    protected class RepresentEnum implements Represent
    {
        @Override
        public Node representData(final Object a1) {
            final Tag v1 = /*EL:425*/new Tag(a1.getClass());
            /*SL:426*/return SafeRepresenter.this.representScalar(SafeRepresenter.this.getTag(a1.getClass(), v1), ((Enum)a1).name());
        }
    }
    
    protected class RepresentByteArray implements Represent
    {
        @Override
        public Node representData(final Object a1) {
            final char[] v1 = /*EL:432*/Base64Coder.encode((byte[])a1);
            /*SL:433*/return SafeRepresenter.this.representScalar(Tag.BINARY, String.valueOf(v1), '|');
        }
    }
    
    protected class RepresentUuid implements Represent
    {
        @Override
        public Node representData(final Object a1) {
            /*SL:447*/return SafeRepresenter.this.representScalar(SafeRepresenter.this.getTag(a1.getClass(), new Tag(UUID.class)), a1.toString());
        }
    }
}

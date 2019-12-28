package org.json;

import java.io.Writer;
import java.io.StringWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;
import java.io.IOException;
import java.io.Closeable;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.Set;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Enumeration;
import java.util.ResourceBundle;
import java.util.Locale;
import java.util.Iterator;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class JSONObject
{
    static final Pattern NUMBER_PATTERN;
    private final Map<String, Object> map;
    public static final Object NULL;
    
    public JSONObject() {
        this.map = new HashMap<String, Object>();
    }
    
    public JSONObject(final JSONObject v1, final String[] v2) {
        this(v2.length);
        for (int a1 = 0; a1 < v2.length; ++a1) {
            try {
                this.putOnce(v2[a1], v1.opt(v2[a1]));
            }
            catch (Exception ex) {}
        }
    }
    
    public JSONObject(final JSONTokener v2) throws JSONException {
        this();
        if (v2.nextClean() != '{') {
            throw v2.syntaxError("A JSONObject text must begin with '{'");
        }
        while (true) {
            char v3 = v2.nextClean();
            switch (v3) {
                case '\0': {
                    throw v2.syntaxError("A JSONObject text must end with '}'");
                }
                case '}': {}
                default: {
                    v2.back();
                    final String v4 = v2.nextValue().toString();
                    v3 = v2.nextClean();
                    if (v3 != ':') {
                        throw v2.syntaxError("Expected a ':' after a key");
                    }
                    if (v4 != null) {
                        if (this.opt(v4) != null) {
                            throw v2.syntaxError("Duplicate key \"" + v4 + "\"");
                        }
                        final Object a1 = v2.nextValue();
                        if (a1 != null) {
                            this.put(v4, a1);
                        }
                    }
                    switch (v2.nextClean()) {
                        case ',':
                        case ';': {
                            if (v2.nextClean() == '}') {
                                return;
                            }
                            v2.back();
                            continue;
                        }
                        case '}': {
                            return;
                        }
                        default: {
                            throw v2.syntaxError("Expected a ',' or '}'");
                        }
                    }
                    break;
                }
            }
        }
    }
    
    public JSONObject(final Map<?, ?> v-1) {
        if (v-1 == null) {
            this.map = new HashMap<String, Object>();
        }
        else {
            this.map = new HashMap<String, Object>(v-1.size());
            for (final Map.Entry<?, ?> v1 : v-1.entrySet()) {
                if (v1.getKey() == null) {
                    throw new NullPointerException("Null key.");
                }
                final Object a1 = v1.getValue();
                if (a1 == null) {
                    continue;
                }
                this.map.put(String.valueOf(v1.getKey()), wrap(a1));
            }
        }
    }
    
    public JSONObject(final Object a1) {
        this();
        this.populateMap(a1);
    }
    
    public JSONObject(final Object v2, final String[] v3) {
        this(v3.length);
        final Class<?> v4 = v2.getClass();
        for (String a2 = (String)0; a2 < v3.length; ++a2) {
            a2 = v3[a2];
            try {
                this.putOpt(a2, v4.getField(a2).get(v2));
            }
            catch (Exception ex) {}
        }
    }
    
    public JSONObject(final String a1) throws JSONException {
        this(new JSONTokener(a1));
    }
    
    public JSONObject(final String v-7, final Locale v-6) throws JSONException {
        this();
        final ResourceBundle bundle = ResourceBundle.getBundle(v-7, v-6, Thread.currentThread().getContextClassLoader());
        final Enumeration<String> keys = bundle.getKeys();
        while (keys.hasMoreElements()) {
            final Object nextElement = keys.nextElement();
            if (nextElement != null) {
                final String[] split = ((String)nextElement).split("\\.");
                final int n = split.length - 1;
                JSONObject v0 = this;
                for (final String a1 : split) {
                    JSONObject a2 = v0.optJSONObject(a1);
                    if (a2 == null) {
                        a2 = new JSONObject();
                        v0.put(a1, a2);
                    }
                    v0 = a2;
                }
                v0.put(split[n], bundle.getString((String)nextElement));
            }
        }
    }
    
    protected JSONObject(final int a1) {
        this.map = new HashMap<String, Object>(a1);
    }
    
    public JSONObject accumulate(final String a1, final Object a2) throws JSONException {
        testValidity(/*EL:485*/a2);
        final Object v1 = /*EL:486*/this.opt(a1);
        /*SL:487*/if (v1 == null) {
            /*SL:488*/this.put(a1, /*EL:489*/(a2 instanceof JSONArray) ? new JSONArray().put(a2) : /*EL:490*/a2);
        }
        else/*SL:491*/ if (v1 instanceof JSONArray) {
            /*SL:492*/((JSONArray)v1).put(a2);
        }
        else {
            /*SL:494*/this.put(a1, new JSONArray().put(v1).put(a2));
        }
        /*SL:496*/return this;
    }
    
    public JSONObject append(final String a1, final Object a2) throws JSONException {
        testValidity(/*EL:517*/a2);
        final Object v1 = /*EL:518*/this.opt(a1);
        /*SL:519*/if (v1 == null) {
            /*SL:520*/this.put(a1, new JSONArray().put(a2));
        }
        else {
            /*SL:521*/if (!(v1 instanceof JSONArray)) {
                /*SL:524*/throw new JSONException("JSONObject[" + a1 + "] is not a JSONArray.");
            }
            this.put(a1, ((JSONArray)v1).put(a2));
        }
        /*SL:527*/return this;
    }
    
    public static String doubleToString(final double a1) {
        /*SL:539*/if (Double.isInfinite(a1) || Double.isNaN(a1)) {
            /*SL:540*/return "null";
        }
        String v1 = /*EL:545*/Double.toString(a1);
        /*SL:546*/if (v1.indexOf(46) > 0 && v1.indexOf(101) < 0 && v1.indexOf(69) < /*EL:547*/0) {
            /*SL:548*/while (v1.endsWith("0")) {
                /*SL:549*/v1 = v1.substring(0, v1.length() - 1);
            }
            /*SL:551*/if (v1.endsWith(".")) {
                /*SL:552*/v1 = v1.substring(0, v1.length() - 1);
            }
        }
        /*SL:555*/return v1;
    }
    
    public Object get(final String a1) throws JSONException {
        /*SL:568*/if (a1 == null) {
            /*SL:569*/throw new JSONException("Null key.");
        }
        final Object v1 = /*EL:571*/this.opt(a1);
        /*SL:572*/if (v1 == null) {
            /*SL:573*/throw new JSONException("JSONObject[" + quote(a1) + "] not found.");
        }
        /*SL:575*/return v1;
    }
    
    public <E extends Enum<E>> E getEnum(final Class<E> a1, final String a2) throws JSONException {
        final E v1 = /*EL:593*/(E)this.<Enum>optEnum((Class<Enum>)a1, a2);
        /*SL:594*/if (v1 == null) {
            /*SL:598*/throw new JSONException("JSONObject[" + quote(a2) + "] is not an enum of type " + quote(a1.getSimpleName()) + /*EL:599*/".");
        }
        /*SL:602*/return v1;
    }
    
    public boolean getBoolean(final String a1) throws JSONException {
        final Object v1 = /*EL:616*/this.get(a1);
        /*SL:617*/if (v1.equals(Boolean.FALSE) || (v1 instanceof String && ((String)v1).equalsIgnoreCase("false"))) {
            /*SL:620*/return false;
        }
        /*SL:621*/if (v1.equals(Boolean.TRUE) || (v1 instanceof String && ((String)v1).equalsIgnoreCase("true"))) {
            /*SL:624*/return true;
        }
        /*SL:626*/throw new JSONException("JSONObject[" + quote(a1) + "] is not a Boolean.");
    }
    
    public BigInteger getBigInteger(final String a1) throws JSONException {
        final Object v1 = /*EL:641*/this.get(a1);
        final BigInteger v2 = objectToBigInteger(/*EL:642*/v1, null);
        /*SL:643*/if (v2 != null) {
            /*SL:644*/return v2;
        }
        /*SL:646*/throw new JSONException("JSONObject[" + quote(a1) + "] could not be converted to BigInteger (" + v1 + ").");
    }
    
    public BigDecimal getBigDecimal(final String a1) throws JSONException {
        final Object v1 = /*EL:664*/this.get(a1);
        final BigDecimal v2 = objectToBigDecimal(/*EL:665*/v1, null);
        /*SL:666*/if (v2 != null) {
            /*SL:667*/return v2;
        }
        /*SL:669*/throw new JSONException("JSONObject[" + quote(a1) + "] could not be converted to BigDecimal (" + v1 + ").");
    }
    
    public double getDouble(final String a1) throws JSONException {
        /*SL:684*/return this.getNumber(a1).doubleValue();
    }
    
    public float getFloat(final String a1) throws JSONException {
        /*SL:698*/return this.getNumber(a1).floatValue();
    }
    
    public Number getNumber(final String v2) throws JSONException {
        final Object v3 = /*EL:712*/this.get(v2);
        try {
            /*SL:714*/if (v3 instanceof Number) {
                /*SL:715*/return (Number)v3;
            }
            /*SL:717*/return stringToNumber(v3.toString());
        }
        catch (Exception a1) {
            /*SL:719*/throw new JSONException("JSONObject[" + quote(v2) + "] is not a number.", a1);
        }
    }
    
    public int getInt(final String a1) throws JSONException {
        /*SL:735*/return this.getNumber(a1).intValue();
    }
    
    public JSONArray getJSONArray(final String a1) throws JSONException {
        final Object v1 = /*EL:748*/this.get(a1);
        /*SL:749*/if (v1 instanceof JSONArray) {
            /*SL:750*/return (JSONArray)v1;
        }
        /*SL:752*/throw new JSONException("JSONObject[" + quote(a1) + "] is not a JSONArray.");
    }
    
    public JSONObject getJSONObject(final String a1) throws JSONException {
        final Object v1 = /*EL:766*/this.get(a1);
        /*SL:767*/if (v1 instanceof JSONObject) {
            /*SL:768*/return (JSONObject)v1;
        }
        /*SL:770*/throw new JSONException("JSONObject[" + quote(a1) + "] is not a JSONObject.");
    }
    
    public long getLong(final String a1) throws JSONException {
        /*SL:785*/return this.getNumber(a1).longValue();
    }
    
    public static String[] getNames(final JSONObject a1) {
        /*SL:796*/if (a1.isEmpty()) {
            /*SL:797*/return null;
        }
        /*SL:799*/return a1.keySet().<String>toArray(new String[a1.length()]);
    }
    
    public static String[] getNames(final Object v1) {
        /*SL:810*/if (v1 == null) {
            /*SL:811*/return null;
        }
        final Class<?> v2 = /*EL:813*/v1.getClass();
        final Field[] v3 = /*EL:814*/v2.getFields();
        final int v4 = /*EL:815*/v3.length;
        /*SL:816*/if (v4 == 0) {
            /*SL:817*/return null;
        }
        final String[] v5 = /*EL:819*/new String[v4];
        /*SL:820*/for (int a1 = 0; a1 < v4; ++a1) {
            /*SL:821*/v5[a1] = v3[a1].getName();
        }
        /*SL:823*/return v5;
    }
    
    public String getString(final String a1) throws JSONException {
        final Object v1 = /*EL:836*/this.get(a1);
        /*SL:837*/if (v1 instanceof String) {
            /*SL:838*/return (String)v1;
        }
        /*SL:840*/throw new JSONException("JSONObject[" + quote(a1) + "] not a string.");
    }
    
    public boolean has(final String a1) {
        /*SL:851*/return this.map.containsKey(a1);
    }
    
    public JSONObject increment(final String a1) throws JSONException {
        final Object v1 = /*EL:867*/this.opt(a1);
        /*SL:868*/if (v1 == null) {
            /*SL:869*/this.put(a1, 1);
        }
        else/*SL:870*/ if (v1 instanceof BigInteger) {
            /*SL:871*/this.put(a1, ((BigInteger)v1).add(BigInteger.ONE));
        }
        else/*SL:872*/ if (v1 instanceof BigDecimal) {
            /*SL:873*/this.put(a1, ((BigDecimal)v1).add(BigDecimal.ONE));
        }
        else/*SL:874*/ if (v1 instanceof Integer) {
            /*SL:875*/this.put(a1, (int)v1 + 1);
        }
        else/*SL:876*/ if (v1 instanceof Long) {
            /*SL:877*/this.put(a1, (long)v1 + 1L);
        }
        else/*SL:878*/ if (v1 instanceof Double) {
            /*SL:879*/this.put(a1, (double)v1 + 1.0);
        }
        else {
            /*SL:880*/if (!(v1 instanceof Float)) {
                /*SL:883*/throw new JSONException("Unable to increment [" + quote(a1) + "].");
            }
            this.put(a1, (float)v1 + 1.0f);
        }
        /*SL:885*/return this;
    }
    
    public boolean isNull(final String a1) {
        /*SL:898*/return JSONObject.NULL.equals(this.opt(a1));
    }
    
    public Iterator<String> keys() {
        /*SL:910*/return this.keySet().iterator();
    }
    
    public Set<String> keySet() {
        /*SL:922*/return this.map.keySet();
    }
    
    protected Set<Map.Entry<String, Object>> entrySet() {
        /*SL:938*/return this.map.entrySet();
    }
    
    public int length() {
        /*SL:947*/return this.map.size();
    }
    
    public boolean isEmpty() {
        /*SL:956*/return this.map.isEmpty();
    }
    
    public JSONArray names() {
        /*SL:967*/if (this.map.isEmpty()) {
            /*SL:968*/return null;
        }
        /*SL:970*/return new JSONArray(this.map.keySet());
    }
    
    public static String numberToString(final Number a1) throws JSONException {
        /*SL:983*/if (a1 == null) {
            /*SL:984*/throw new JSONException("Null pointer");
        }
        testValidity(/*EL:986*/a1);
        String v1 = /*EL:990*/a1.toString();
        /*SL:991*/if (v1.indexOf(46) > 0 && v1.indexOf(101) < 0 && v1.indexOf(69) < /*EL:992*/0) {
            /*SL:993*/while (v1.endsWith("0")) {
                /*SL:994*/v1 = v1.substring(0, v1.length() - 1);
            }
            /*SL:996*/if (v1.endsWith(".")) {
                /*SL:997*/v1 = v1.substring(0, v1.length() - 1);
            }
        }
        /*SL:1000*/return v1;
    }
    
    public Object opt(final String a1) {
        /*SL:1011*/return (a1 == null) ? null : this.map.get(a1);
    }
    
    public <E extends Enum<E>> E optEnum(final Class<E> a1, final String a2) {
        /*SL:1026*/return this.<E>optEnum(a1, a2, (E)null);
    }
    
    public <E extends Enum<E>> E optEnum(final Class<E> v-2, final String v-1, final E v0) {
        try {
            Object a2 = /*EL:1045*/this.opt(v-1);
            /*SL:1046*/if (JSONObject.NULL.equals(a2)) {
                /*SL:1047*/return v0;
            }
            /*SL:1049*/if (v-2.isAssignableFrom(a2.getClass())) {
                /*SL:1052*/a2 = (E)a2;
                /*SL:1053*/return a2;
            }
            /*SL:1055*/return Enum.<E>valueOf(v-2, a2.toString());
        }
        catch (IllegalArgumentException a3) {
            /*SL:1057*/return v0;
        }
        catch (NullPointerException v) {
            /*SL:1059*/return v0;
        }
    }
    
    public boolean optBoolean(final String a1) {
        /*SL:1072*/return this.optBoolean(a1, false);
    }
    
    public boolean optBoolean(final String v1, final boolean v2) {
        final Object v3 = /*EL:1087*/this.opt(v1);
        /*SL:1088*/if (JSONObject.NULL.equals(v3)) {
            /*SL:1089*/return v2;
        }
        /*SL:1091*/if (v3 instanceof Boolean) {
            /*SL:1092*/return (boolean)v3;
        }
        try {
            /*SL:1096*/return this.getBoolean(v1);
        }
        catch (Exception a1) {
            /*SL:1098*/return v2;
        }
    }
    
    public BigDecimal optBigDecimal(final String a1, final BigDecimal a2) {
        final Object v1 = /*EL:1117*/this.opt(a1);
        /*SL:1118*/return objectToBigDecimal(v1, a2);
    }
    
    static BigDecimal objectToBigDecimal(final Object v1, final BigDecimal v2) {
        /*SL:1128*/if (JSONObject.NULL.equals(v1)) {
            /*SL:1129*/return v2;
        }
        /*SL:1131*/if (v1 instanceof BigDecimal) {
            /*SL:1132*/return (BigDecimal)v1;
        }
        /*SL:1134*/if (v1 instanceof BigInteger) {
            /*SL:1135*/return new BigDecimal((BigInteger)v1);
        }
        /*SL:1137*/if (v1 instanceof Double || v1 instanceof Float) {
            final double a1 = /*EL:1138*/((Number)v1).doubleValue();
            /*SL:1139*/if (Double.isNaN(a1)) {
                /*SL:1140*/return v2;
            }
            /*SL:1142*/return new BigDecimal(((Number)v1).doubleValue());
        }
        else {
            /*SL:1144*/if (v1 instanceof Long || v1 instanceof Integer || v1 instanceof Short || v1 instanceof Byte) {
                /*SL:1146*/return new BigDecimal(((Number)v1).longValue());
            }
            try {
                /*SL:1150*/return new BigDecimal(v1.toString());
            }
            catch (Exception a2) {
                /*SL:1152*/return v2;
            }
        }
    }
    
    public BigInteger optBigInteger(final String a1, final BigInteger a2) {
        final Object v1 = /*EL:1168*/this.opt(a1);
        /*SL:1169*/return objectToBigInteger(v1, a2);
    }
    
    static BigInteger objectToBigInteger(final Object v-1, final BigInteger v0) {
        /*SL:1179*/if (JSONObject.NULL.equals(v-1)) {
            /*SL:1180*/return v0;
        }
        /*SL:1182*/if (v-1 instanceof BigInteger) {
            /*SL:1183*/return (BigInteger)v-1;
        }
        /*SL:1185*/if (v-1 instanceof BigDecimal) {
            /*SL:1186*/return ((BigDecimal)v-1).toBigInteger();
        }
        /*SL:1188*/if (v-1 instanceof Double || v-1 instanceof Float) {
            final double a1 = /*EL:1189*/((Number)v-1).doubleValue();
            /*SL:1190*/if (Double.isNaN(a1)) {
                /*SL:1191*/return v0;
            }
            /*SL:1193*/return new BigDecimal(a1).toBigInteger();
        }
        else {
            /*SL:1195*/if (v-1 instanceof Long || v-1 instanceof Integer || v-1 instanceof Short || v-1 instanceof Byte) {
                /*SL:1197*/return BigInteger.valueOf(((Number)v-1).longValue());
            }
            try {
                final String a2 = /*EL:1206*/v-1.toString();
                /*SL:1207*/if (isDecimalNotation(a2)) {
                    /*SL:1208*/return new BigDecimal(a2).toBigInteger();
                }
                /*SL:1210*/return new BigInteger(a2);
            }
            catch (Exception v) {
                /*SL:1212*/return v0;
            }
        }
    }
    
    public double optDouble(final String a1) {
        /*SL:1226*/return this.optDouble(a1, Double.NaN);
    }
    
    public double optDouble(final String a1, final double a2) {
        final Number v1 = /*EL:1241*/this.optNumber(a1);
        /*SL:1242*/if (v1 == null) {
            /*SL:1243*/return a2;
        }
        final double v2 = /*EL:1245*/v1.doubleValue();
        /*SL:1249*/return v2;
    }
    
    public float optFloat(final String a1) {
        /*SL:1262*/return this.optFloat(a1, Float.NaN);
    }
    
    public float optFloat(final String a1, final float a2) {
        final Number v1 = /*EL:1277*/this.optNumber(a1);
        /*SL:1278*/if (v1 == null) {
            /*SL:1279*/return a2;
        }
        final float v2 = /*EL:1281*/v1.floatValue();
        /*SL:1285*/return v2;
    }
    
    public int optInt(final String a1) {
        /*SL:1298*/return this.optInt(a1, 0);
    }
    
    public int optInt(final String a1, final int a2) {
        final Number v1 = /*EL:1313*/this.optNumber(a1, null);
        /*SL:1314*/if (v1 == null) {
            /*SL:1315*/return a2;
        }
        /*SL:1317*/return v1.intValue();
    }
    
    public JSONArray optJSONArray(final String a1) {
        final Object v1 = /*EL:1329*/this.opt(a1);
        /*SL:1330*/return (v1 instanceof JSONArray) ? ((JSONArray)v1) : null;
    }
    
    public JSONObject optJSONObject(final String a1) {
        final Object v1 = /*EL:1342*/this.opt(a1);
        /*SL:1343*/return (v1 instanceof JSONObject) ? ((JSONObject)v1) : null;
    }
    
    public long optLong(final String a1) {
        /*SL:1356*/return this.optLong(a1, 0L);
    }
    
    public long optLong(final String a1, final long a2) {
        final Number v1 = /*EL:1371*/this.optNumber(a1, null);
        /*SL:1372*/if (v1 == null) {
            /*SL:1373*/return a2;
        }
        /*SL:1376*/return v1.longValue();
    }
    
    public Number optNumber(final String a1) {
        /*SL:1390*/return this.optNumber(a1, null);
    }
    
    public Number optNumber(final String v1, final Number v2) {
        final Object v3 = /*EL:1406*/this.opt(v1);
        /*SL:1407*/if (JSONObject.NULL.equals(v3)) {
            /*SL:1408*/return v2;
        }
        /*SL:1410*/if (v3 instanceof Number) {
            /*SL:1411*/return (Number)v3;
        }
        try {
            /*SL:1415*/return stringToNumber(v3.toString());
        }
        catch (Exception a1) {
            /*SL:1417*/return v2;
        }
    }
    
    public String optString(final String a1) {
        /*SL:1431*/return this.optString(a1, "");
    }
    
    public String optString(final String a1, final String a2) {
        final Object v1 = /*EL:1445*/this.opt(a1);
        /*SL:1446*/return JSONObject.NULL.equals(v1) ? a2 : v1.toString();
    }
    
    private void populateMap(final Object v-8) {
        final Class<?> class1 = /*EL:1459*/v-8.getClass();
        final boolean b = /*EL:1463*/class1.getClassLoader() != null;
        final Method[] array2;
        final Method[] array = /*EL:1466*/array2 = (b ? class1.getMethods() : class1.getDeclaredMethods());
        for (final Method v-9 : array2) {
            final int v0 = /*EL:1467*/v-9.getModifiers();
            /*SL:1472*/if (Modifier.isPublic(v0) && !Modifier.isStatic(v0) && v-9.getParameterTypes().length == 0 && !v-9.isBridge() && v-9.getReturnType() != Void.TYPE && this.isValidMethodName(v-9.getName())) {
                final String v = /*EL:1474*/this.getKeyNameFromMethod(v-9);
                /*SL:1475*/if (v != null && !v.isEmpty()) {
                    try {
                        final Object a1 = /*EL:1477*/v-9.invoke(v-8, new Object[0]);
                        /*SL:1478*/if (a1 != null) {
                            /*SL:1479*/this.map.put(v, wrap(a1));
                            /*SL:1483*/if (a1 instanceof Closeable) {
                                try {
                                    /*SL:1485*/((Closeable)a1).close();
                                }
                                catch (IOException ex) {}
                            }
                        }
                    }
                    catch (IllegalAccessException ex2) {}
                    catch (IllegalArgumentException ex3) {}
                    catch (InvocationTargetException ex4) {}
                }
            }
        }
    }
    
    private boolean isValidMethodName(final String a1) {
        /*SL:1500*/return !"getClass".equals(a1) && !"getDeclaringClass".equals(a1);
    }
    
    private String getKeyNameFromMethod(final Method v-2) {
        final int annotationDepth = getAnnotationDepth(/*EL:1504*/v-2, JSONPropertyIgnore.class);
        /*SL:1505*/if (annotationDepth > 0) {
            final int a1 = getAnnotationDepth(/*EL:1506*/v-2, JSONPropertyName.class);
            /*SL:1507*/if (a1 < 0 || annotationDepth <= a1) {
                /*SL:1510*/return null;
            }
        }
        final JSONPropertyName v0 = (JSONPropertyName)getAnnotation(/*EL:1513*/v-2, (Class)JSONPropertyName.class);
        /*SL:1514*/if (v0 != null && v0.value() != null && !v0.value().isEmpty()) {
            /*SL:1515*/return v0.value();
        }
        final String v = /*EL:1518*/v-2.getName();
        String v2;
        /*SL:1519*/if (v.startsWith("get") && v.length() > 3) {
            /*SL:1520*/v2 = v.substring(3);
        }
        else {
            /*SL:1521*/if (!v.startsWith("is") || v.length() <= 2) {
                /*SL:1524*/return null;
            }
            v2 = v.substring(2);
        }
        /*SL:1529*/if (Character.isLowerCase(v2.charAt(0))) {
            /*SL:1530*/return null;
        }
        /*SL:1532*/if (v2.length() == 1) {
            /*SL:1533*/v2 = v2.toLowerCase(Locale.ROOT);
        }
        else/*SL:1534*/ if (!Character.isUpperCase(v2.charAt(1))) {
            /*SL:1535*/v2 = v2.substring(0, 1).toLowerCase(Locale.ROOT) + v2.substring(1);
        }
        /*SL:1537*/return v2;
    }
    
    private static <A extends java.lang.Object> A getAnnotation(final Method v-6, final Class<A> v-5) {
        /*SL:1556*/if (v-6 == null || v-5 == null) {
            /*SL:1557*/return null;
        }
        /*SL:1560*/if (v-6.isAnnotationPresent((Class<? extends Annotation>)v-5)) {
            /*SL:1561*/return v-6.<A>getAnnotation(v-5);
        }
        final Class<?> declaringClass = /*EL:1565*/v-6.getDeclaringClass();
        /*SL:1566*/if (declaringClass.getSuperclass() == null) {
            /*SL:1567*/return null;
        }
        /*SL:1571*/for (final Class<?> v0 : declaringClass.getInterfaces()) {
            try {
                final Method a1 = /*EL:1573*/v0.getMethod(v-6.getName(), v-6.getParameterTypes());
                /*SL:1574*/return (A)getAnnotation(a1, (Class)v-5);
            }
            catch (SecurityException a2) {}
            catch (NoSuchMethodException v) {}
        }
        try {
            /*SL:1583*/return (A)getAnnotation(declaringClass.getSuperclass().getMethod(/*EL:1584*/v-6.getName(), v-6.getParameterTypes()), (Class)v-5);
        }
        catch (SecurityException ex) {
            /*SL:1587*/return null;
        }
        catch (NoSuchMethodException ex2) {
            /*SL:1589*/return null;
        }
    }
    
    private static int getAnnotationDepth(final Method v-6, final Class<? extends Annotation> v-5) {
        /*SL:1609*/if (v-6 == null || v-5 == null) {
            /*SL:1610*/return -1;
        }
        /*SL:1613*/if (v-6.isAnnotationPresent(v-5)) {
            /*SL:1614*/return 1;
        }
        final Class<?> declaringClass = /*EL:1618*/v-6.getDeclaringClass();
        /*SL:1619*/if (declaringClass.getSuperclass() == null) {
            /*SL:1620*/return -1;
        }
        /*SL:1624*/for (final Class<?> v0 : declaringClass.getInterfaces()) {
            try {
                final Method a1 = /*EL:1626*/v0.getMethod(v-6.getName(), v-6.getParameterTypes());
                final int a2 = getAnnotationDepth(/*EL:1627*/a1, v-5);
                /*SL:1628*/if (a2 > 0) {
                    /*SL:1630*/return a2 + 1;
                }
            }
            catch (SecurityException v) {}
            catch (NoSuchMethodException v2) {}
        }
        try {
            final int annotationDepth = getAnnotationDepth(/*EL:1640*/declaringClass.getSuperclass().getMethod(/*EL:1641*/v-6.getName(), v-6.getParameterTypes()), v-5);
            /*SL:1643*/if (annotationDepth > 0) {
                /*SL:1645*/return annotationDepth + 1;
            }
            /*SL:1647*/return -1;
        }
        catch (SecurityException ex) {
            /*SL:1649*/return -1;
        }
        catch (NoSuchMethodException ex2) {
            /*SL:1651*/return -1;
        }
    }
    
    public JSONObject put(final String a1, final boolean a2) throws JSONException {
        /*SL:1669*/return this.put(a1, a2 ? Boolean.TRUE : Boolean.FALSE);
    }
    
    public JSONObject put(final String a1, final Collection<?> a2) throws JSONException {
        /*SL:1687*/return this.put(a1, new JSONArray(a2));
    }
    
    public JSONObject put(final String a1, final double a2) throws JSONException {
        /*SL:1704*/return this.put(a1, (Object)a2);
    }
    
    public JSONObject put(final String a1, final float a2) throws JSONException {
        /*SL:1721*/return this.put(a1, (Object)a2);
    }
    
    public JSONObject put(final String a1, final int a2) throws JSONException {
        /*SL:1738*/return this.put(a1, (Object)a2);
    }
    
    public JSONObject put(final String a1, final long a2) throws JSONException {
        /*SL:1755*/return this.put(a1, (Object)a2);
    }
    
    public JSONObject put(final String a1, final Map<?, ?> a2) throws JSONException {
        /*SL:1773*/return this.put(a1, new JSONObject(a2));
    }
    
    public JSONObject put(final String a1, final Object a2) throws JSONException {
        /*SL:1793*/if (a1 == null) {
            /*SL:1794*/throw new NullPointerException("Null key.");
        }
        /*SL:1796*/if (a2 != null) {
            testValidity(/*EL:1797*/a2);
            /*SL:1798*/this.map.put(a1, a2);
        }
        else {
            /*SL:1800*/this.remove(a1);
        }
        /*SL:1802*/return this;
    }
    
    public JSONObject putOnce(final String a1, final Object a2) throws JSONException {
        /*SL:1819*/if (a1 == null || a2 == null) {
            /*SL:1825*/return this;
        }
        if (this.opt(a1) != null) {
            throw new JSONException("Duplicate key \"" + a1 + "\"");
        }
        return this.put(a1, a2);
    }
    
    public JSONObject putOpt(final String a1, final Object a2) throws JSONException {
        /*SL:1843*/if (a1 != null && a2 != null) {
            /*SL:1844*/return this.put(a1, a2);
        }
        /*SL:1846*/return this;
    }
    
    public Object query(final String a1) {
        /*SL:1869*/return this.query(new JSONPointer(a1));
    }
    
    public Object query(final JSONPointer a1) {
        /*SL:1891*/return a1.queryFrom(this);
    }
    
    public Object optQuery(final String a1) {
        /*SL:1903*/return this.optQuery(new JSONPointer(a1));
    }
    
    public Object optQuery(final JSONPointer v2) {
        try {
            /*SL:1916*/return v2.queryFrom(this);
        }
        catch (JSONPointerException a1) {
            /*SL:1918*/return null;
        }
    }
    
    public static String quote(final String v1) {
        final StringWriter v2 = /*EL:1934*/new StringWriter();
        /*SL:1935*/synchronized (v2.getBuffer()) {
            try {
                /*SL:1937*/return quote(v1, v2).toString();
            }
            catch (IOException a1) {
                /*SL:1940*/return "";
            }
        }
    }
    
    public static Writer quote(final String v1, final Writer v2) throws IOException {
        /*SL:1946*/if (v1 == null || v1.isEmpty()) {
            /*SL:1947*/v2.write("\"\"");
            /*SL:1948*/return v2;
        }
        char v3 = /*EL:1952*/'\0';
        final int v4 = /*EL:1955*/v1.length();
        /*SL:1957*/v2.write(34);
        /*SL:1958*/for (int v5 = 0; v5 < v4; ++v5) {
            char a2 = /*EL:1959*/v3;
            /*SL:1960*/v3 = v1.charAt(v5);
            /*SL:1961*/switch (v3) {
                case '\"':
                case '\\': {
                    /*SL:1964*/v2.write(92);
                    /*SL:1965*/v2.write(v3);
                    /*SL:1966*/break;
                }
                case '/': {
                    /*SL:1968*/if (a2 == '<') {
                        /*SL:1969*/v2.write(92);
                    }
                    /*SL:1971*/v2.write(v3);
                    /*SL:1972*/break;
                }
                case '\b': {
                    /*SL:1974*/v2.write("\\b");
                    /*SL:1975*/break;
                }
                case '\t': {
                    /*SL:1977*/v2.write("\\t");
                    /*SL:1978*/break;
                }
                case '\n': {
                    /*SL:1980*/v2.write("\\n");
                    /*SL:1981*/break;
                }
                case '\f': {
                    /*SL:1983*/v2.write("\\f");
                    /*SL:1984*/break;
                }
                case '\r': {
                    /*SL:1986*/v2.write("\\r");
                    /*SL:1987*/break;
                }
                default: {
                    /*SL:1989*/if (v3 < ' ' || (v3 >= '\u0080' && v3 < ' ') || (v3 >= '\u2000' && v3 < '\u2100')) {
                        /*SL:1991*/v2.write("\\u");
                        /*SL:1992*/a2 = Integer.toHexString(v3);
                        /*SL:1993*/v2.write("0000", 0, 4 - a2.length());
                        /*SL:1994*/v2.write(a2);
                        break;
                    }
                    /*SL:1996*/v2.write(v3);
                    break;
                }
            }
        }
        /*SL:2000*/v2.write(34);
        /*SL:2001*/return v2;
    }
    
    public Object remove(final String a1) {
        /*SL:2013*/return this.map.remove(a1);
    }
    
    public boolean similar(final Object v-3) {
        try {
            /*SL:2026*/if (!(v-3 instanceof JSONObject)) {
                /*SL:2027*/return false;
            }
            /*SL:2029*/if (!this.keySet().equals(((JSONObject)v-3).keySet())) {
                /*SL:2030*/return false;
            }
            /*SL:2032*/for (final Map.Entry<String, ?> entry : this.entrySet()) {
                final String a1 = /*EL:2033*/entry.getKey();
                final Object v1 = /*EL:2034*/entry.getValue();
                final Object v2 = /*EL:2035*/((JSONObject)v-3).get(a1);
                /*SL:2036*/if (v1 == v2) {
                    /*SL:2037*/continue;
                }
                /*SL:2039*/if (v1 == null) {
                    /*SL:2040*/return false;
                }
                /*SL:2042*/if (v1 instanceof JSONObject) {
                    /*SL:2043*/if (!((JSONObject)v1).similar(v2)) {
                        /*SL:2044*/return false;
                    }
                    continue;
                }
                else/*SL:2046*/ if (v1 instanceof JSONArray) {
                    /*SL:2047*/if (!((JSONArray)v1).similar(v2)) {
                        /*SL:2048*/return false;
                    }
                    continue;
                }
                else {
                    /*SL:2050*/if (!v1.equals(v2)) {
                        /*SL:2051*/return false;
                    }
                    continue;
                }
            }
            /*SL:2054*/return true;
        }
        catch (Throwable t) {
            /*SL:2056*/return false;
        }
    }
    
    protected static boolean isDecimalNotation(final String a1) {
        /*SL:2067*/return a1.indexOf(46) > -1 || a1.indexOf(101) > -1 || a1.indexOf(69) > /*EL:2068*/-1 || "-0".equals(a1);
    }
    
    protected static Number stringToNumber(final String v-1) throws NumberFormatException {
        final char v0 = /*EL:2082*/v-1.charAt(0);
        /*SL:2083*/if ((v0 < '0' || v0 > '9') && v0 != '-') {
            /*SL:2132*/throw new NumberFormatException("val [" + v-1 + "] is not a valid number.");
        }
        if (isDecimalNotation(v-1)) {
            if (v-1.length() > 14) {
                return new BigDecimal(v-1);
            }
            final Double a1 = Double.valueOf(v-1);
            if (a1.isInfinite() || a1.isNaN()) {
                return new BigDecimal(v-1);
            }
            return a1;
        }
        else {
            final BigInteger v = new BigInteger(v-1);
            if (v.bitLength() <= 31) {
                return v.intValue();
            }
            if (v.bitLength() <= 63) {
                return v.longValue();
            }
            return v;
        }
    }
    
    public static Object stringToValue(final String v-1) {
        /*SL:2148*/if ("".equals(v-1)) {
            /*SL:2149*/return v-1;
        }
        /*SL:2153*/if ("true".equalsIgnoreCase(v-1)) {
            /*SL:2154*/return Boolean.TRUE;
        }
        /*SL:2156*/if ("false".equalsIgnoreCase(v-1)) {
            /*SL:2157*/return Boolean.FALSE;
        }
        /*SL:2159*/if ("null".equalsIgnoreCase(v-1)) {
            /*SL:2160*/return JSONObject.NULL;
        }
        final char v0 = /*EL:2168*/v-1.charAt(0);
        /*SL:2169*/if (v0 < '0' || v0 > '9') {
            if (v0 != '-') {
                return /*EL:2191*/v-1;
            }
        }
        try {
            if (isDecimalNotation(v-1)) {
                final Double a1 = Double.valueOf(v-1);
                if (!a1.isInfinite() && !a1.isNaN()) {
                    return a1;
                }
            }
            else {
                final Long v = Long.valueOf(v-1);
                if (v-1.equals(v.toString())) {
                    if (v == (int)(Object)v) {
                        return (Object)v;
                    }
                    return v;
                }
            }
        }
        catch (Exception ex) {}
        return v-1;
    }
    
    public static void testValidity(final Object a1) throws JSONException {
        /*SL:2203*/if (a1 != null) {
            /*SL:2204*/if (a1 instanceof Double) {
                /*SL:2205*/if (((Double)a1).isInfinite() || ((Double)a1).isNaN()) {
                    /*SL:2206*/throw new JSONException("JSON does not allow non-finite numbers.");
                }
            }
            else/*SL:2209*/ if (a1 instanceof Float && /*EL:2210*/(((Float)a1).isInfinite() || ((Float)a1).isNaN())) {
                /*SL:2211*/throw new JSONException("JSON does not allow non-finite numbers.");
            }
        }
    }
    
    public JSONArray toJSONArray(final JSONArray v2) throws JSONException {
        /*SL:2230*/if (v2 == null || v2.isEmpty()) {
            /*SL:2231*/return null;
        }
        final JSONArray v3 = /*EL:2233*/new JSONArray();
        /*SL:2234*/for (int a1 = 0; a1 < v2.length(); ++a1) {
            /*SL:2235*/v3.put(this.opt(v2.getString(a1)));
        }
        /*SL:2237*/return v3;
    }
    
    @Override
    public String toString() {
        try {
            /*SL:2256*/return this.toString(0);
        }
        catch (Exception v1) {
            /*SL:2258*/return null;
        }
    }
    
    public String toString(final int a1) throws JSONException {
        final StringWriter v1 = /*EL:2289*/new StringWriter();
        /*SL:2290*/synchronized (v1.getBuffer()) {
            /*SL:2291*/return this.write(v1, a1, 0).toString();
        }
    }
    
    public static String valueToString(final Object a1) throws JSONException {
        /*SL:2324*/return JSONWriter.valueToString(a1);
    }
    
    public static Object wrap(final Object v0) {
        try {
            /*SL:2341*/if (v0 == null) {
                /*SL:2342*/return JSONObject.NULL;
            }
            /*SL:2345*/if (v0 instanceof JSONObject || v0 instanceof JSONArray || JSONObject.NULL.equals(v0) || v0 instanceof JSONString || v0 instanceof Byte || v0 instanceof Character || v0 instanceof Short || v0 instanceof Integer || v0 instanceof Long || v0 instanceof Boolean || v0 instanceof Float || v0 instanceof Double || v0 instanceof String || v0 instanceof BigInteger || v0 instanceof BigDecimal || v0 instanceof Enum) {
                /*SL:2352*/return v0;
            }
            /*SL:2355*/if (v0 instanceof Collection) {
                final Collection<?> a1 = /*EL:2356*/(Collection<?>)v0;
                /*SL:2357*/return new JSONArray(a1);
            }
            /*SL:2359*/if (v0.getClass().isArray()) {
                /*SL:2360*/return new JSONArray(v0);
            }
            /*SL:2362*/if (v0 instanceof Map) {
                final Map<?, ?> a2 = /*EL:2363*/(Map<?, ?>)v0;
                /*SL:2364*/return new JSONObject(a2);
            }
            final Package v = /*EL:2366*/v0.getClass().getPackage();
            final String v2 = /*EL:2367*/(v != null) ? /*EL:2368*/v.getName() : "";
            /*SL:2370*/if (v2.startsWith("java.") || v2.startsWith("javax.") || v0.getClass().getClassLoader() == /*EL:2371*/null) {
                /*SL:2372*/return v0.toString();
            }
            /*SL:2374*/return new JSONObject(v0);
        }
        catch (Exception v3) {
            /*SL:2376*/return null;
        }
    }
    
    public Writer write(final Writer a1) throws JSONException {
        /*SL:2391*/return this.write(a1, 0, 0);
    }
    
    static final Writer writeValue(final Writer v-3, final Object v-2, final int v-1, final int v0) throws JSONException, IOException {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     0: aload_1         /* v-2 */
        //     1: ifnull          12
        //     4: aload_1         /* v-2 */
        //     5: aconst_null    
        //     6: invokevirtual   java/lang/Object.equals:(Ljava/lang/Object;)Z
        //     9: ifeq            22
        //    12: aload_0         /* v-3 */
        //    13: ldc_w           "null"
        //    16: invokevirtual   java/io/Writer.write:(Ljava/lang/String;)V
        //    19: goto            316
        //    22: aload_1         /* v-2 */
        //    23: instanceof      Lorg/json/JSONString;
        //    26: ifeq            82
        //    29: aload_1         /* v-2 */
        //    30: checkcast       Lorg/json/JSONString;
        //    33: invokeinterface org/json/JSONString.toJSONString:()Ljava/lang/String;
        //    38: astore          a1
        //    40: goto            55
        //    43: astore          a2
        //    45: new             Lorg/json/JSONException;
        //    48: dup            
        //    49: aload           a2
        //    51: invokespecial   org/json/JSONException.<init>:(Ljava/lang/Throwable;)V
        //    54: athrow         
        //    55: aload_0         /* v-3 */
        //    56: aload           a3
        //    58: ifnull          69
        //    61: aload           a3
        //    63: invokevirtual   java/lang/Object.toString:()Ljava/lang/String;
        //    66: goto            76
        //    69: aload_1         /* v-2 */
        //    70: invokevirtual   java/lang/Object.toString:()Ljava/lang/String;
        //    73: invokestatic    org/json/JSONObject.quote:(Ljava/lang/String;)Ljava/lang/String;
        //    76: invokevirtual   java/io/Writer.write:(Ljava/lang/String;)V
        //    79: goto            316
        //    82: aload_1         /* v-2 */
        //    83: instanceof      Ljava/lang/Number;
        //    86: ifeq            131
        //    89: aload_1         /* v-2 */
        //    90: checkcast       Ljava/lang/Number;
        //    93: invokestatic    org/json/JSONObject.numberToString:(Ljava/lang/Number;)Ljava/lang/String;
        //    96: astore          a4
        //    98: getstatic       org/json/JSONObject.NUMBER_PATTERN:Ljava/util/regex/Pattern;
        //   101: aload           a4
        //   103: invokevirtual   java/util/regex/Pattern.matcher:(Ljava/lang/CharSequence;)Ljava/util/regex/Matcher;
        //   106: invokevirtual   java/util/regex/Matcher.matches:()Z
        //   109: ifeq            121
        //   112: aload_0         /* v-3 */
        //   113: aload           a4
        //   115: invokevirtual   java/io/Writer.write:(Ljava/lang/String;)V
        //   118: goto            128
        //   121: aload           a4
        //   123: aload_0         /* v-3 */
        //   124: invokestatic    org/json/JSONObject.quote:(Ljava/lang/String;Ljava/io/Writer;)Ljava/io/Writer;
        //   127: pop            
        //   128: goto            316
        //   131: aload_1         /* v-2 */
        //   132: instanceof      Ljava/lang/Boolean;
        //   135: ifeq            149
        //   138: aload_0         /* v-3 */
        //   139: aload_1         /* v-2 */
        //   140: invokevirtual   java/lang/Object.toString:()Ljava/lang/String;
        //   143: invokevirtual   java/io/Writer.write:(Ljava/lang/String;)V
        //   146: goto            316
        //   149: aload_1         /* v-2 */
        //   150: instanceof      Ljava/lang/Enum;
        //   153: ifeq            173
        //   156: aload_0         /* v-3 */
        //   157: aload_1         /* v-2 */
        //   158: checkcast       Ljava/lang/Enum;
        //   161: invokevirtual   java/lang/Enum.name:()Ljava/lang/String;
        //   164: invokestatic    org/json/JSONObject.quote:(Ljava/lang/String;)Ljava/lang/String;
        //   167: invokevirtual   java/io/Writer.write:(Ljava/lang/String;)V
        //   170: goto            316
        //   173: aload_1         /* v-2 */
        //   174: instanceof      Lorg/json/JSONObject;
        //   177: ifeq            194
        //   180: aload_1         /* v-2 */
        //   181: checkcast       Lorg/json/JSONObject;
        //   184: aload_0         /* v-3 */
        //   185: iload_2         /* v-1 */
        //   186: iload_3         /* v0 */
        //   187: invokevirtual   org/json/JSONObject.write:(Ljava/io/Writer;II)Ljava/io/Writer;
        //   190: pop            
        //   191: goto            316
        //   194: aload_1         /* v-2 */
        //   195: instanceof      Lorg/json/JSONArray;
        //   198: ifeq            215
        //   201: aload_1         /* v-2 */
        //   202: checkcast       Lorg/json/JSONArray;
        //   205: aload_0         /* v-3 */
        //   206: iload_2         /* v-1 */
        //   207: iload_3         /* v0 */
        //   208: invokevirtual   org/json/JSONArray.write:(Ljava/io/Writer;II)Ljava/io/Writer;
        //   211: pop            
        //   212: goto            316
        //   215: aload_1         /* v-2 */
        //   216: instanceof      Ljava/util/Map;
        //   219: ifeq            247
        //   222: aload_1         /* v-2 */
        //   223: checkcast       Ljava/util/Map;
        //   226: astore          a1
        //   228: new             Lorg/json/JSONObject;
        //   231: dup            
        //   232: aload           a1
        //   234: invokespecial   org/json/JSONObject.<init>:(Ljava/util/Map;)V
        //   237: aload_0         /* v-3 */
        //   238: iload_2         /* v-1 */
        //   239: iload_3         /* v0 */
        //   240: invokevirtual   org/json/JSONObject.write:(Ljava/io/Writer;II)Ljava/io/Writer;
        //   243: pop            
        //   244: goto            316
        //   247: aload_1         /* v-2 */
        //   248: instanceof      Ljava/util/Collection;
        //   251: ifeq            279
        //   254: aload_1         /* v-2 */
        //   255: checkcast       Ljava/util/Collection;
        //   258: astore          a1
        //   260: new             Lorg/json/JSONArray;
        //   263: dup            
        //   264: aload           a1
        //   266: invokespecial   org/json/JSONArray.<init>:(Ljava/util/Collection;)V
        //   269: aload_0         /* v-3 */
        //   270: iload_2         /* v-1 */
        //   271: iload_3         /* v0 */
        //   272: invokevirtual   org/json/JSONArray.write:(Ljava/io/Writer;II)Ljava/io/Writer;
        //   275: pop            
        //   276: goto            316
        //   279: aload_1         /* v-2 */
        //   280: invokevirtual   java/lang/Object.getClass:()Ljava/lang/Class;
        //   283: invokevirtual   java/lang/Class.isArray:()Z
        //   286: ifeq            307
        //   289: new             Lorg/json/JSONArray;
        //   292: dup            
        //   293: aload_1         /* v-2 */
        //   294: invokespecial   org/json/JSONArray.<init>:(Ljava/lang/Object;)V
        //   297: aload_0         /* v-3 */
        //   298: iload_2         /* v-1 */
        //   299: iload_3         /* v0 */
        //   300: invokevirtual   org/json/JSONArray.write:(Ljava/io/Writer;II)Ljava/io/Writer;
        //   303: pop            
        //   304: goto            316
        //   307: aload_1         /* v-2 */
        //   308: invokevirtual   java/lang/Object.toString:()Ljava/lang/String;
        //   311: aload_0         /* v-3 */
        //   312: invokestatic    org/json/JSONObject.quote:(Ljava/lang/String;Ljava/io/Writer;)Ljava/io/Writer;
        //   315: pop            
        //   316: aload_0         /* v-3 */
        //   317: areturn        
        //    Exceptions:
        //  throws org.json.JSONException
        //  throws java.io.IOException
        //    LocalVariableTable:
        //  Start  Length  Slot  Name  Signature
        //  -----  ------  ----  ----  ----------------------
        //  40     3       4     a1    Ljava/lang/Object;
        //  45     10      5     a2    Ljava/lang/Exception;
        //  55     24      4     a3    Ljava/lang/Object;
        //  98     30      4     a4    Ljava/lang/String;
        //  228    16      4     v1    Ljava/util/Map;
        //  260    16      4     v1    Ljava/util/Collection;
        //  0      318     0     v-3   Ljava/io/Writer;
        //  0      318     1     v-2   Ljava/lang/Object;
        //  0      318     2     v-1   I
        //  0      318     3     v0    I
        //    LocalVariableTypeTable:
        //  Start  Length  Slot  Name  Signature
        //  -----  ------  ----  ----  -------------------------
        //  228    16      4     a1    Ljava/util/Map<**>;
        //  260    16      4     a1    Ljava/util/Collection<*>;
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                 
        //  -----  -----  -----  -----  ---------------------
        //  29     40     43     55     Ljava/lang/Exception;
        // 
        // The error that occurred was:
        // 
        // java.lang.NullPointerException
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    static final void indent(final Writer a2, final int v1) throws IOException {
        /*SL:2439*/for (int a3 = 0; a3 < v1; ++a3) {
            /*SL:2440*/a2.write(32);
        }
    }
    
    public Writer write(final Writer v-8, final int v-7, final int v-6) throws JSONException {
        try {
            boolean b = /*EL:2473*/false;
            final int length = /*EL:2474*/this.length();
            /*SL:2475*/v-8.write(123);
            /*SL:2477*/if (length == 1) {
                Map.Entry<String, ?> a2 = /*EL:2478*/this.entrySet().iterator().next();
                /*SL:2479*/a2 = a2.getKey();
                /*SL:2480*/v-8.write(quote(a2));
                /*SL:2481*/v-8.write(58);
                /*SL:2482*/if (v-7 > 0) {
                    /*SL:2483*/v-8.write(32);
                }
                try {
                    writeValue(/*EL:2486*/v-8, a2.getValue(), v-7, v-6);
                }
                catch (Exception a3) {
                    /*SL:2488*/throw new JSONException("Unable to write JSONObject value for key: " + a2, a3);
                }
            }
            else/*SL:2490*/ if (length != 0) {
                final int n = /*EL:2491*/v-6 + v-7;
                /*SL:2492*/for (final Map.Entry<String, ?> a4 : this.entrySet()) {
                    /*SL:2493*/if (b) {
                        /*SL:2494*/v-8.write(44);
                    }
                    /*SL:2496*/if (v-7 > 0) {
                        /*SL:2497*/v-8.write(10);
                    }
                    indent(/*EL:2499*/v-8, n);
                    final String v0 = /*EL:2500*/a4.getKey();
                    /*SL:2501*/v-8.write(quote(v0));
                    /*SL:2502*/v-8.write(58);
                    /*SL:2503*/if (v-7 > 0) {
                        /*SL:2504*/v-8.write(32);
                    }
                    try {
                        writeValue(/*EL:2507*/v-8, a4.getValue(), v-7, n);
                    }
                    catch (Exception v) {
                        /*SL:2509*/throw new JSONException("Unable to write JSONObject value for key: " + v0, v);
                    }
                    /*SL:2511*/b = true;
                }
                /*SL:2513*/if (v-7 > 0) {
                    /*SL:2514*/v-8.write(10);
                }
                indent(/*EL:2516*/v-8, v-6);
            }
            /*SL:2518*/v-8.write(125);
            /*SL:2519*/return v-8;
        }
        catch (IOException a5) {
            /*SL:2521*/throw new JSONException(a5);
        }
    }
    
    public Map<String, Object> toMap() {
        final Map<String, Object> map = /*EL:2535*/new HashMap<String, Object>();
        /*SL:2536*/for (final Map.Entry<String, Object> v0 : this.entrySet()) {
            Object v;
            /*SL:2538*/if (v0.getValue() == null || JSONObject.NULL.equals(v0.getValue())) {
                /*SL:2539*/v = null;
            }
            else/*SL:2540*/ if (v0.getValue() instanceof JSONObject) {
                /*SL:2541*/v = v0.getValue().toMap();
            }
            else/*SL:2542*/ if (v0.getValue() instanceof JSONArray) {
                /*SL:2543*/v = v0.getValue().toList();
            }
            else {
                /*SL:2545*/v = v0.getValue();
            }
            /*SL:2547*/map.put(v0.getKey(), v);
        }
        /*SL:2549*/return map;
    }
    
    static {
        NUMBER_PATTERN = Pattern.compile("-?(?:0|[1-9]\\d*)(?:\\.\\d+)?(?:[eE][+-]?\\d+)?");
        NULL = new Null();
    }
    
    private static final class Null
    {
        @Override
        protected final Object clone() {
            /*SL:119*/return this;
        }
        
        @Override
        public boolean equals(final Object a1) {
            /*SL:132*/return a1 == null || a1 == this;
        }
        
        @Override
        public int hashCode() {
            /*SL:141*/return 0;
        }
        
        @Override
        public String toString() {
            /*SL:151*/return "null";
        }
    }
}

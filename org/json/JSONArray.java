package org.json;

import java.util.List;
import java.io.IOException;
import java.io.Writer;
import java.io.StringWriter;
import java.util.Map;
import java.math.BigInteger;
import java.math.BigDecimal;
import java.lang.reflect.Array;
import java.util.Iterator;
import java.util.Collection;
import java.util.ArrayList;

public class JSONArray implements Iterable<Object>
{
    private final ArrayList<Object> myArrayList;
    
    public JSONArray() {
        this.myArrayList = new ArrayList<Object>();
    }
    
    public JSONArray(final JSONTokener a1) throws JSONException {
        this();
        if (a1.nextClean() != '[') {
            throw a1.syntaxError("A JSONArray text must start with '['");
        }
        char v1 = a1.nextClean();
        if (v1 == '\0') {
            throw a1.syntaxError("Expected a ',' or ']'");
        }
        if (v1 == ']') {
            return;
        }
        a1.back();
        while (true) {
            if (a1.nextClean() == ',') {
                a1.back();
                this.myArrayList.add(JSONObject.NULL);
            }
            else {
                a1.back();
                this.myArrayList.add(a1.nextValue());
            }
            switch (a1.nextClean()) {
                case '\0': {
                    throw a1.syntaxError("Expected a ',' or ']'");
                }
                case ',': {
                    v1 = a1.nextClean();
                    if (v1 == '\0') {
                        throw a1.syntaxError("Expected a ',' or ']'");
                    }
                    if (v1 == ']') {
                        return;
                    }
                    a1.back();
                    continue;
                }
                case ']': {}
                default: {
                    throw a1.syntaxError("Expected a ',' or ']'");
                }
            }
        }
    }
    
    public JSONArray(final String a1) throws JSONException {
        this(new JSONTokener(a1));
    }
    
    public JSONArray(final Collection<?> v2) {
        if (v2 == null) {
            this.myArrayList = new ArrayList<Object>();
        }
        else {
            this.myArrayList = new ArrayList<Object>(v2.size());
            for (final Object a1 : v2) {
                this.myArrayList.add(JSONObject.wrap(a1));
            }
        }
    }
    
    public JSONArray(final Object v0) throws JSONException {
        this();
        if (v0.getClass().isArray()) {
            final int v = Array.getLength(v0);
            this.myArrayList.ensureCapacity(v);
            for (int a1 = 0; a1 < v; ++a1) {
                this.put(JSONObject.wrap(Array.get(v0, a1)));
            }
            return;
        }
        throw new JSONException("JSONArray initial value should be a string or collection or array.");
    }
    
    @Override
    public Iterator<Object> iterator() {
        /*SL:210*/return this.myArrayList.iterator();
    }
    
    public Object get(final int a1) throws JSONException {
        final Object v1 = /*EL:223*/this.opt(a1);
        /*SL:224*/if (v1 == null) {
            /*SL:225*/throw new JSONException("JSONArray[" + a1 + "] not found.");
        }
        /*SL:227*/return v1;
    }
    
    public boolean getBoolean(final int a1) throws JSONException {
        final Object v1 = /*EL:242*/this.get(a1);
        /*SL:243*/if (v1.equals(Boolean.FALSE) || (v1 instanceof String && ((String)v1).equalsIgnoreCase("false"))) {
            /*SL:246*/return false;
        }
        /*SL:247*/if (v1.equals(Boolean.TRUE) || (v1 instanceof String && ((String)v1).equalsIgnoreCase("true"))) {
            /*SL:250*/return true;
        }
        /*SL:252*/throw new JSONException("JSONArray[" + a1 + "] is not a boolean.");
    }
    
    public double getDouble(final int a1) throws JSONException {
        /*SL:266*/return this.getNumber(a1).doubleValue();
    }
    
    public float getFloat(final int a1) throws JSONException {
        /*SL:280*/return this.getNumber(a1).floatValue();
    }
    
    public Number getNumber(final int v2) throws JSONException {
        final Object v3 = /*EL:294*/this.get(v2);
        try {
            /*SL:296*/if (v3 instanceof Number) {
                /*SL:297*/return (Number)v3;
            }
            /*SL:299*/return JSONObject.stringToNumber(v3.toString());
        }
        catch (Exception a1) {
            /*SL:301*/throw new JSONException("JSONArray[" + v2 + "] is not a number.", a1);
        }
    }
    
    public <E extends Enum<E>> E getEnum(final Class<E> a1, final int a2) throws JSONException {
        final E v1 = /*EL:320*/(E)this.<Enum>optEnum((Class<Enum>)a1, a2);
        /*SL:321*/if (v1 == null) {
            /*SL:325*/throw new JSONException("JSONArray[" + a2 + "] is not an enum of type " + /*EL:326*/JSONObject.quote(a1.getSimpleName()) + ".");
        }
        /*SL:328*/return v1;
    }
    
    public BigDecimal getBigDecimal(final int a1) throws JSONException {
        final Object v1 = /*EL:345*/this.get(a1);
        final BigDecimal v2 = /*EL:346*/JSONObject.objectToBigDecimal(v1, null);
        /*SL:347*/if (v2 == null) {
            /*SL:348*/throw new JSONException("JSONArray[" + a1 + "] could not convert to BigDecimal (" + v1 + ").");
        }
        /*SL:351*/return v2;
    }
    
    public BigInteger getBigInteger(final int a1) throws JSONException {
        final Object v1 = /*EL:365*/this.get(a1);
        final BigInteger v2 = /*EL:366*/JSONObject.objectToBigInteger(v1, null);
        /*SL:367*/if (v2 == null) {
            /*SL:368*/throw new JSONException("JSONArray[" + a1 + "] could not convert to BigDecimal (" + v1 + ").");
        }
        /*SL:371*/return v2;
    }
    
    public int getInt(final int a1) throws JSONException {
        /*SL:384*/return this.getNumber(a1).intValue();
    }
    
    public JSONArray getJSONArray(final int a1) throws JSONException {
        final Object v1 = /*EL:398*/this.get(a1);
        /*SL:399*/if (v1 instanceof JSONArray) {
            /*SL:400*/return (JSONArray)v1;
        }
        /*SL:402*/throw new JSONException("JSONArray[" + a1 + "] is not a JSONArray.");
    }
    
    public JSONObject getJSONObject(final int a1) throws JSONException {
        final Object v1 = /*EL:416*/this.get(a1);
        /*SL:417*/if (v1 instanceof JSONObject) {
            /*SL:418*/return (JSONObject)v1;
        }
        /*SL:420*/throw new JSONException("JSONArray[" + a1 + "] is not a JSONObject.");
    }
    
    public long getLong(final int a1) throws JSONException {
        /*SL:434*/return this.getNumber(a1).longValue();
    }
    
    public String getString(final int a1) throws JSONException {
        final Object v1 = /*EL:447*/this.get(a1);
        /*SL:448*/if (v1 instanceof String) {
            /*SL:449*/return (String)v1;
        }
        /*SL:451*/throw new JSONException("JSONArray[" + a1 + "] not a string.");
    }
    
    public boolean isNull(final int a1) {
        /*SL:462*/return JSONObject.NULL.equals(this.opt(a1));
    }
    
    public String join(final String v2) throws JSONException {
        final int v3 = /*EL:477*/this.length();
        /*SL:478*/if (v3 == 0) {
            /*SL:479*/return "";
        }
        final StringBuilder v4 = /*EL:483*/new StringBuilder(JSONObject.valueToString(this.myArrayList.get(0)));
        /*SL:485*/for (int a1 = 1; a1 < v3; ++a1) {
            /*SL:486*/v4.append(v2).append(/*EL:487*/JSONObject.valueToString(this.myArrayList.get(a1)));
        }
        /*SL:489*/return v4.toString();
    }
    
    public int length() {
        /*SL:498*/return this.myArrayList.size();
    }
    
    public Object opt(final int a1) {
        /*SL:509*/return (a1 < 0 || a1 >= this.length()) ? null : /*EL:510*/this.myArrayList.get(a1);
    }
    
    public boolean optBoolean(final int a1) {
        /*SL:523*/return this.optBoolean(a1, false);
    }
    
    public boolean optBoolean(final int v1, final boolean v2) {
        try {
            /*SL:539*/return this.getBoolean(v1);
        }
        catch (Exception a1) {
            /*SL:541*/return v2;
        }
    }
    
    public double optDouble(final int a1) {
        /*SL:555*/return this.optDouble(a1, Double.NaN);
    }
    
    public double optDouble(final int a1, final double a2) {
        final Number v1 = /*EL:570*/this.optNumber(a1, null);
        /*SL:571*/if (v1 == null) {
            /*SL:572*/return a2;
        }
        final double v2 = /*EL:574*/v1.doubleValue();
        /*SL:578*/return v2;
    }
    
    public float optFloat(final int a1) {
        /*SL:591*/return this.optFloat(a1, Float.NaN);
    }
    
    public float optFloat(final int a1, final float a2) {
        final Number v1 = /*EL:606*/this.optNumber(a1, null);
        /*SL:607*/if (v1 == null) {
            /*SL:608*/return a2;
        }
        final float v2 = /*EL:610*/v1.floatValue();
        /*SL:614*/return v2;
    }
    
    public int optInt(final int a1) {
        /*SL:627*/return this.optInt(a1, 0);
    }
    
    public int optInt(final int a1, final int a2) {
        final Number v1 = /*EL:642*/this.optNumber(a1, null);
        /*SL:643*/if (v1 == null) {
            /*SL:644*/return a2;
        }
        /*SL:646*/return v1.intValue();
    }
    
    public <E extends Enum<E>> E optEnum(final Class<E> a1, final int a2) {
        /*SL:661*/return this.<E>optEnum(a1, a2, (E)null);
    }
    
    public <E extends Enum<E>> E optEnum(final Class<E> v-2, final int v-1, final E v0) {
        try {
            Object a2 = /*EL:680*/this.opt(v-1);
            /*SL:681*/if (JSONObject.NULL.equals(a2)) {
                /*SL:682*/return v0;
            }
            /*SL:684*/if (v-2.isAssignableFrom(a2.getClass())) {
                /*SL:687*/a2 = (E)a2;
                /*SL:688*/return a2;
            }
            /*SL:690*/return Enum.<E>valueOf(v-2, a2.toString());
        }
        catch (IllegalArgumentException a3) {
            /*SL:692*/return v0;
        }
        catch (NullPointerException v) {
            /*SL:694*/return v0;
        }
    }
    
    public BigInteger optBigInteger(final int a1, final BigInteger a2) {
        final Object v1 = /*EL:710*/this.opt(a1);
        /*SL:711*/return JSONObject.objectToBigInteger(v1, a2);
    }
    
    public BigDecimal optBigDecimal(final int a1, final BigDecimal a2) {
        final Object v1 = /*EL:729*/this.opt(a1);
        /*SL:730*/return JSONObject.objectToBigDecimal(v1, a2);
    }
    
    public JSONArray optJSONArray(final int a1) {
        final Object v1 = /*EL:742*/this.opt(a1);
        /*SL:743*/return (v1 instanceof JSONArray) ? ((JSONArray)v1) : null;
    }
    
    public JSONObject optJSONObject(final int a1) {
        final Object v1 = /*EL:756*/this.opt(a1);
        /*SL:757*/return (v1 instanceof JSONObject) ? ((JSONObject)v1) : null;
    }
    
    public long optLong(final int a1) {
        /*SL:770*/return this.optLong(a1, 0L);
    }
    
    public long optLong(final int a1, final long a2) {
        final Number v1 = /*EL:785*/this.optNumber(a1, null);
        /*SL:786*/if (v1 == null) {
            /*SL:787*/return a2;
        }
        /*SL:789*/return v1.longValue();
    }
    
    public Number optNumber(final int a1) {
        /*SL:803*/return this.optNumber(a1, null);
    }
    
    public Number optNumber(final int v1, final Number v2) {
        final Object v3 = /*EL:819*/this.opt(v1);
        /*SL:820*/if (JSONObject.NULL.equals(v3)) {
            /*SL:821*/return v2;
        }
        /*SL:823*/if (v3 instanceof Number) {
            /*SL:824*/return (Number)v3;
        }
        /*SL:827*/if (v3 instanceof String) {
            try {
                /*SL:829*/return JSONObject.stringToNumber((String)v3);
            }
            catch (Exception a1) {
                /*SL:831*/return v2;
            }
        }
        /*SL:834*/return v2;
    }
    
    public String optString(final int a1) {
        /*SL:847*/return this.optString(a1, "");
    }
    
    public String optString(final int a1, final String a2) {
        final Object v1 = /*EL:861*/this.opt(a1);
        /*SL:862*/return JSONObject.NULL.equals(v1) ? a2 : /*EL:863*/v1.toString();
    }
    
    public JSONArray put(final boolean a1) {
        /*SL:874*/return this.put(a1 ? Boolean.TRUE : Boolean.FALSE);
    }
    
    public JSONArray put(final Collection<?> a1) {
        /*SL:888*/return this.put(new JSONArray(a1));
    }
    
    public JSONArray put(final double a1) throws JSONException {
        /*SL:901*/return this.put((Object)a1);
    }
    
    public JSONArray put(final float a1) throws JSONException {
        /*SL:914*/return this.put((Object)a1);
    }
    
    public JSONArray put(final int a1) {
        /*SL:925*/return this.put((Object)a1);
    }
    
    public JSONArray put(final long a1) {
        /*SL:936*/return this.put((Object)a1);
    }
    
    public JSONArray put(final Map<?, ?> a1) {
        /*SL:952*/return this.put(new JSONObject(a1));
    }
    
    public JSONArray put(final Object a1) {
        /*SL:967*/JSONObject.testValidity(a1);
        /*SL:968*/this.myArrayList.add(a1);
        /*SL:969*/return this;
    }
    
    public JSONArray put(final int a1, final boolean a2) throws JSONException {
        /*SL:986*/return this.put(a1, a2 ? Boolean.TRUE : Boolean.FALSE);
    }
    
    public JSONArray put(final int a1, final Collection<?> a2) throws JSONException {
        /*SL:1002*/return this.put(a1, new JSONArray(a2));
    }
    
    public JSONArray put(final int a1, final double a2) throws JSONException {
        /*SL:1019*/return this.put(a1, (Object)a2);
    }
    
    public JSONArray put(final int a1, final float a2) throws JSONException {
        /*SL:1036*/return this.put(a1, (Object)a2);
    }
    
    public JSONArray put(final int a1, final int a2) throws JSONException {
        /*SL:1053*/return this.put(a1, (Object)a2);
    }
    
    public JSONArray put(final int a1, final long a2) throws JSONException {
        /*SL:1070*/return this.put(a1, (Object)a2);
    }
    
    public JSONArray put(final int a1, final Map<?, ?> a2) throws JSONException {
        /*SL:1089*/this.put(a1, new JSONObject(a2));
        /*SL:1090*/return this;
    }
    
    public JSONArray put(final int a1, final Object a2) throws JSONException {
        /*SL:1110*/if (a1 < 0) {
            /*SL:1111*/throw new JSONException("JSONArray[" + a1 + "] not found.");
        }
        /*SL:1113*/if (a1 < this.length()) {
            /*SL:1114*/JSONObject.testValidity(a2);
            /*SL:1115*/this.myArrayList.set(a1, a2);
            /*SL:1116*/return this;
        }
        /*SL:1118*/if (a1 == this.length()) {
            /*SL:1120*/return this.put(a2);
        }
        /*SL:1124*/this.myArrayList.ensureCapacity(a1 + 1);
        /*SL:1125*/while (a1 != this.length()) {
            /*SL:1127*/this.myArrayList.add(JSONObject.NULL);
        }
        /*SL:1129*/return this.put(a2);
    }
    
    public Object query(final String a1) {
        /*SL:1152*/return this.query(new JSONPointer(a1));
    }
    
    public Object query(final JSONPointer a1) {
        /*SL:1175*/return a1.queryFrom(this);
    }
    
    public Object optQuery(final String a1) {
        /*SL:1187*/return this.optQuery(new JSONPointer(a1));
    }
    
    public Object optQuery(final JSONPointer v2) {
        try {
            /*SL:1200*/return v2.queryFrom(this);
        }
        catch (JSONPointerException a1) {
            /*SL:1202*/return null;
        }
    }
    
    public Object remove(final int a1) {
        /*SL:1215*/return (a1 >= 0 && a1 < this.length()) ? /*EL:1216*/this.myArrayList.remove(a1) : /*EL:1217*/null;
    }
    
    public boolean similar(final Object v-3) {
        /*SL:1228*/if (!(v-3 instanceof JSONArray)) {
            /*SL:1229*/return false;
        }
        final int length = /*EL:1231*/this.length();
        /*SL:1232*/if (length != ((JSONArray)v-3).length()) {
            /*SL:1233*/return false;
        }
        /*SL:1235*/for (int i = 0; i < length; ++i) {
            final Object a1 = /*EL:1236*/this.myArrayList.get(i);
            final Object v1 = /*EL:1237*/((JSONArray)v-3).myArrayList.get(i);
            /*SL:1238*/if (a1 != v1) {
                /*SL:1241*/if (a1 == null) {
                    /*SL:1242*/return false;
                }
                /*SL:1244*/if (a1 instanceof JSONObject) {
                    /*SL:1245*/if (!((JSONObject)a1).similar(v1)) {
                        /*SL:1246*/return false;
                    }
                }
                else/*SL:1248*/ if (a1 instanceof JSONArray) {
                    /*SL:1249*/if (!((JSONArray)a1).similar(v1)) {
                        /*SL:1250*/return false;
                    }
                }
                else/*SL:1252*/ if (!a1.equals(v1)) {
                    /*SL:1253*/return false;
                }
            }
        }
        /*SL:1256*/return true;
    }
    
    public JSONObject toJSONObject(final JSONArray v2) throws JSONException {
        /*SL:1272*/if (v2 == null || v2.isEmpty() || this.isEmpty()) {
            /*SL:1273*/return null;
        }
        final JSONObject v3 = /*EL:1275*/new JSONObject(v2.length());
        /*SL:1276*/for (int a1 = 0; a1 < v2.length(); ++a1) {
            /*SL:1277*/v3.put(v2.getString(a1), this.opt(a1));
        }
        /*SL:1279*/return v3;
    }
    
    @Override
    public String toString() {
        try {
            /*SL:1297*/return this.toString(0);
        }
        catch (Exception v1) {
            /*SL:1299*/return null;
        }
    }
    
    public String toString(final int a1) throws JSONException {
        final StringWriter v1 = /*EL:1331*/new StringWriter();
        /*SL:1332*/synchronized (v1.getBuffer()) {
            /*SL:1333*/return this.write(v1, a1, 0).toString();
        }
    }
    
    public Writer write(final Writer a1) throws JSONException {
        /*SL:1348*/return this.write(a1, 0, 0);
    }
    
    public Writer write(final Writer v-4, final int v-3, final int v-2) throws JSONException {
        try {
            boolean b = /*EL:1382*/false;
            final int v0 = /*EL:1383*/this.length();
            /*SL:1384*/v-4.write(91);
            Label_0178: {
                /*SL:1386*/if (v0 == 1) {
                    try {
                        /*SL:1388*/JSONObject.writeValue(v-4, this.myArrayList.get(0), v-3, v-2);
                        break Label_0178;
                    }
                    catch (Exception a1) {
                        /*SL:1391*/throw new JSONException("Unable to write JSONArray value at index: 0", a1);
                    }
                }
                /*SL:1393*/if (v0 != 0) {
                    final int v = /*EL:1394*/v-2 + v-3;
                    /*SL:1396*/for (int a2 = 0; a2 < v0; ++a2) {
                        /*SL:1397*/if (b) {
                            /*SL:1398*/v-4.write(44);
                        }
                        /*SL:1400*/if (v-3 > 0) {
                            /*SL:1401*/v-4.write(10);
                        }
                        /*SL:1403*/JSONObject.indent(v-4, v);
                        try {
                            /*SL:1405*/JSONObject.writeValue(v-4, this.myArrayList.get(a2), v-3, v);
                        }
                        catch (Exception a3) {
                            /*SL:1408*/throw new JSONException("Unable to write JSONArray value at index: " + a2, a3);
                        }
                        /*SL:1410*/b = true;
                    }
                    /*SL:1412*/if (v-3 > 0) {
                        /*SL:1413*/v-4.write(10);
                    }
                    /*SL:1415*/JSONObject.indent(v-4, v-2);
                }
            }
            /*SL:1417*/v-4.write(93);
            /*SL:1418*/return v-4;
        }
        catch (IOException a4) {
            /*SL:1420*/throw new JSONException(a4);
        }
    }
    
    public List<Object> toList() {
        final List<Object> list = /*EL:1434*/new ArrayList<Object>(this.myArrayList.size());
        /*SL:1435*/for (final Object v1 : this.myArrayList) {
            /*SL:1436*/if (v1 == null || JSONObject.NULL.equals(v1)) {
                /*SL:1437*/list.add(null);
            }
            else/*SL:1438*/ if (v1 instanceof JSONArray) {
                /*SL:1439*/list.add(((JSONArray)v1).toList());
            }
            else/*SL:1440*/ if (v1 instanceof JSONObject) {
                /*SL:1441*/list.add(((JSONObject)v1).toMap());
            }
            else {
                /*SL:1443*/list.add(v1);
            }
        }
        /*SL:1446*/return list;
    }
    
    public boolean isEmpty() {
        /*SL:1455*/return this.myArrayList.isEmpty();
    }
}

package com.google.api.client.json;

import java.util.concurrent.locks.ReentrantLock;
import java.util.HashSet;
import java.util.Iterator;
import com.google.api.client.util.Sets;
import java.util.Locale;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import com.google.api.client.util.Data;
import java.util.Collection;
import com.google.api.client.util.FieldInfo;
import com.google.api.client.util.Types;
import java.util.Map;
import com.google.api.client.util.GenericData;
import com.google.api.client.util.ClassInfo;
import java.util.ArrayList;
import java.lang.reflect.Type;
import com.google.api.client.util.Preconditions;
import java.util.Set;
import java.util.Collections;
import com.google.api.client.util.Beta;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.io.IOException;
import java.util.concurrent.locks.Lock;
import java.lang.reflect.Field;
import java.util.WeakHashMap;

public abstract class JsonParser
{
    private static WeakHashMap<Class<?>, Field> cachedTypemapFields;
    private static final Lock lock;
    
    public abstract JsonFactory getFactory();
    
    public abstract void close() throws IOException;
    
    public abstract JsonToken nextToken() throws IOException;
    
    public abstract JsonToken getCurrentToken();
    
    public abstract String getCurrentName() throws IOException;
    
    public abstract JsonParser skipChildren() throws IOException;
    
    public abstract String getText() throws IOException;
    
    public abstract byte getByteValue() throws IOException;
    
    public abstract short getShortValue() throws IOException;
    
    public abstract int getIntValue() throws IOException;
    
    public abstract float getFloatValue() throws IOException;
    
    public abstract long getLongValue() throws IOException;
    
    public abstract double getDoubleValue() throws IOException;
    
    public abstract BigInteger getBigIntegerValue() throws IOException;
    
    public abstract BigDecimal getDecimalValue() throws IOException;
    
    public final <T> T parseAndClose(final Class<T> a1) throws IOException {
        /*SL:148*/return this.<T>parseAndClose(a1, null);
    }
    
    @Beta
    public final <T> T parseAndClose(final Class<T> a1, final CustomizeJsonParser a2) throws IOException {
        try {
            /*SL:166*/return (T)this.<Object>parse((Class<Object>)a1, a2);
        }
        finally {
            /*SL:168*/this.close();
        }
    }
    
    public final void skipToKey(final String a1) throws IOException {
        /*SL:185*/this.skipToKey(Collections.<String>singleton(a1));
    }
    
    public final String skipToKey(final Set<String> v2) throws IOException {
        /*SL:204*/for (JsonToken v3 = this.startParsingObjectOrArray(); v3 == JsonToken.FIELD_NAME; /*SL:211*/v3 = this.nextToken()) {
            final String a1 = this.getText();
            this.nextToken();
            if (v2.contains(a1)) {
                return a1;
            }
            this.skipChildren();
        }
        /*SL:213*/return null;
    }
    
    private JsonToken startParsing() throws IOException {
        JsonToken v1 = /*EL:218*/this.getCurrentToken();
        /*SL:220*/if (v1 == null) {
            /*SL:221*/v1 = this.nextToken();
        }
        /*SL:223*/Preconditions.checkArgument(v1 != null, (Object)"no JSON input found");
        /*SL:224*/return v1;
    }
    
    private JsonToken startParsingObjectOrArray() throws IOException {
        JsonToken v1 = /*EL:240*/this.startParsing();
        /*SL:241*/switch (v1) {
            case START_OBJECT: {
                /*SL:243*/v1 = this.nextToken();
                /*SL:244*/Preconditions.checkArgument(v1 == JsonToken.FIELD_NAME || v1 == JsonToken.END_OBJECT, v1);
                /*SL:247*/break;
            }
            case START_ARRAY: {
                /*SL:249*/v1 = this.nextToken();
                break;
            }
        }
        /*SL:254*/return v1;
    }
    
    public final void parseAndClose(final Object a1) throws IOException {
        /*SL:270*/this.parseAndClose(a1, null);
    }
    
    @Beta
    public final void parseAndClose(final Object a1, final CustomizeJsonParser a2) throws IOException {
        try {
            /*SL:290*/this.parse(a1, a2);
        }
        finally {
            /*SL:292*/this.close();
        }
    }
    
    public final <T> T parse(final Class<T> a1) throws IOException {
        /*SL:312*/return this.<T>parse(a1, null);
    }
    
    @Beta
    public final <T> T parse(final Class<T> a1, final CustomizeJsonParser a2) throws IOException {
        final T v1 = /*EL:336*/(T)this.parse(a1, false, a2);
        /*SL:337*/return v1;
    }
    
    public Object parse(final Type a1, final boolean a2) throws IOException {
        /*SL:355*/return this.parse(a1, a2, null);
    }
    
    @Beta
    public Object parse(final Type a1, final boolean a2, final CustomizeJsonParser a3) throws IOException {
        try {
            /*SL:379*/if (!Void.class.equals(a1)) {
                /*SL:380*/this.startParsing();
            }
            /*SL:382*/return this.parseValue(null, a1, new ArrayList<Type>(), null, a3, true);
        }
        finally {
            /*SL:384*/if (a2) {
                /*SL:385*/this.close();
            }
        }
    }
    
    public final void parse(final Object a1) throws IOException {
        /*SL:403*/this.parse(a1, null);
    }
    
    @Beta
    public final void parse(final Object a1, final CustomizeJsonParser a2) throws IOException {
        final ArrayList<Type> v1 = /*EL:423*/new ArrayList<Type>();
        /*SL:424*/v1.add(a1.getClass());
        /*SL:425*/this.parse(v1, a1, a2);
    }
    
    private void parse(final ArrayList<Type> v-10, final Object v-9, final CustomizeJsonParser v-8) throws IOException {
        /*SL:439*/if (v-9 instanceof GenericJson) {
            /*SL:440*/((GenericJson)v-9).setFactory(this.getFactory());
        }
        JsonToken jsonToken = /*EL:442*/this.startParsingObjectOrArray();
        final Class<?> class1 = /*EL:443*/v-9.getClass();
        final ClassInfo of = /*EL:444*/ClassInfo.of(class1);
        final boolean assignable = /*EL:445*/GenericData.class.isAssignableFrom(class1);
        /*SL:446*/if (!assignable && Map.class.isAssignableFrom(class1)) {
            final Map<String, Object> a1 = /*EL:450*/(Map<String, Object>)v-9;
            /*SL:451*/this.parseMap(null, a1, Types.getMapValueParameter(class1), v-10, v-8);
            /*SL:453*/return;
        }
        /*SL:455*/while (jsonToken == JsonToken.FIELD_NAME) {
            final String text = /*EL:456*/this.getText();
            /*SL:457*/this.nextToken();
            /*SL:459*/if (v-8 != null && v-8.stopAt(v-9, text)) {
                /*SL:460*/return;
            }
            final FieldInfo fieldInfo = /*EL:463*/of.getFieldInfo(text);
            /*SL:464*/if (fieldInfo != null) {
                /*SL:466*/if (fieldInfo.isFinal() && !fieldInfo.isPrimitive()) {
                    /*SL:467*/throw new IllegalArgumentException("final array/object fields are not supported");
                }
                final Field a2 = /*EL:469*/fieldInfo.getField();
                final int a3 = /*EL:470*/v-10.size();
                /*SL:471*/v-10.add(a2.getGenericType());
                final Object v1 = /*EL:472*/this.parseValue(a2, fieldInfo.getGenericType(), /*EL:473*/v-10, v-9, v-8, true);
                /*SL:478*/v-10.remove(a3);
                /*SL:479*/fieldInfo.setValue(v-9, v1);
            }
            else/*SL:480*/ if (assignable) {
                final GenericData genericData = /*EL:482*/(GenericData)v-9;
                /*SL:483*/genericData.set(text, this.parseValue(null, null, v-10, v-9, v-8, true));
            }
            else {
                /*SL:486*/if (v-8 != null) {
                    /*SL:487*/v-8.handleUnrecognizedKey(v-9, text);
                }
                /*SL:489*/this.skipChildren();
            }
            /*SL:491*/jsonToken = this.nextToken();
        }
    }
    
    public final <T> Collection<T> parseArrayAndClose(final Class<?> a1, final Class<T> a2) throws IOException {
        /*SL:507*/return this.<T>parseArrayAndClose(a1, a2, null);
    }
    
    @Beta
    public final <T> Collection<T> parseArrayAndClose(final Class<?> a1, final Class<T> a2, final CustomizeJsonParser a3) throws IOException {
        try {
            /*SL:525*/return (Collection<T>)this.<Object>parseArray(a1, (Class<Object>)a2, a3);
        }
        finally {
            /*SL:527*/this.close();
        }
    }
    
    public final <T> void parseArrayAndClose(final Collection<? super T> a1, final Class<T> a2) throws IOException {
        /*SL:543*/this.<T>parseArrayAndClose(a1, a2, null);
    }
    
    @Beta
    public final <T> void parseArrayAndClose(final Collection<? super T> a1, final Class<T> a2, final CustomizeJsonParser a3) throws IOException {
        try {
            /*SL:560*/this.<Object>parseArray((Collection<? super Object>)a1, (Class<Object>)a2, a3);
        }
        finally {
            /*SL:562*/this.close();
        }
    }
    
    public final <T> Collection<T> parseArray(final Class<?> a1, final Class<T> a2) throws IOException {
        /*SL:577*/return this.<T>parseArray(a1, a2, null);
    }
    
    @Beta
    public final <T> Collection<T> parseArray(final Class<?> a1, final Class<T> a2, final CustomizeJsonParser a3) throws IOException {
        final Collection<T> v1 = /*EL:596*/(Collection<T>)Data.newCollectionInstance(a1);
        /*SL:597*/this.<T>parseArray(v1, a2, a3);
        /*SL:598*/return v1;
    }
    
    public final <T> void parseArray(final Collection<? super T> a1, final Class<T> a2) throws IOException {
        /*SL:612*/this.<T>parseArray(a1, a2, null);
    }
    
    @Beta
    public final <T> void parseArray(final Collection<? super T> a1, final Class<T> a2, final CustomizeJsonParser a3) throws IOException {
        /*SL:628*/this.<? super T>parseArray(null, a1, a2, new ArrayList<Type>(), a3);
    }
    
    private <T> void parseArray(final Field a3, final Collection<T> a4, final Type a5, final ArrayList<Type> v1, final CustomizeJsonParser v2) throws IOException {
        /*SL:646*/for (JsonToken v3 = this.startParsingObjectOrArray(); v3 != JsonToken.END_ARRAY; /*SL:655*/v3 = this.nextToken()) {
            final T a6 = (T)this.parseValue(a3, a5, v1, a4, v2, true);
            a4.add(a6);
        }
    }
    
    private void parseMap(final Field a4, final Map<String, Object> a5, final Type v1, final ArrayList<Type> v2, final CustomizeJsonParser v3) throws IOException {
        /*SL:672*/for (JsonToken v4 = this.startParsingObjectOrArray(); v4 == JsonToken.FIELD_NAME; /*SL:682*/v4 = this.nextToken()) {
            final String a6 = this.getText();
            this.nextToken();
            if (v3 != null && v3.stopAt(a5, a6)) {
                return;
            }
            final Object a7 = this.parseValue(a4, v1, v2, a5, v3, true);
            a5.put(a6, a7);
        }
    }
    
    private final Object parseValue(final Field v-10, Type v-9, final ArrayList<Type> v-8, final Object v-7, final CustomizeJsonParser v-6, final boolean v-5) throws IOException {
        /*SL:705*/v-9 = Data.resolveWildcardTypeOrTypeVariable(v-8, v-9);
        Class<?> rawClass = /*EL:707*/(Class<?>)((v-9 instanceof Class) ? ((Class)v-9) : null);
        /*SL:708*/if (v-9 instanceof ParameterizedType) {
            /*SL:709*/rawClass = Types.getRawClass((ParameterizedType)v-9);
        }
        /*SL:712*/if (rawClass == Void.class) {
            /*SL:713*/this.skipChildren();
            /*SL:714*/return null;
        }
        final JsonToken currentToken = /*EL:717*/this.getCurrentToken();
        try {
            /*SL:719*/switch (this.getCurrentToken()) {
                case START_ARRAY:
                case END_ARRAY: {
                    final boolean a1 = /*EL:722*/Types.isArray(v-9);
                    /*SL:723*/Preconditions.checkArgument(v-9 == null || a1 || (rawClass != null && /*EL:724*/Types.isAssignableToOrFrom(rawClass, Collection.class)), "expected collection or array type but got %s", v-9);
                    Collection<Object> a2 = /*EL:726*/null;
                    /*SL:727*/if (v-6 != null && v-10 != null) {
                        /*SL:728*/a2 = v-6.newInstanceForArray(v-7, v-10);
                    }
                    /*SL:730*/if (a2 == null) {
                        /*SL:731*/a2 = Data.newCollectionInstance(v-9);
                    }
                    Type a3 = /*EL:733*/null;
                    /*SL:734*/if (a1) {
                        /*SL:735*/a3 = Types.getArrayComponentType(v-9);
                    }
                    else/*SL:736*/ if (rawClass != null && Iterable.class.isAssignableFrom(rawClass)) {
                        /*SL:737*/a3 = Types.getIterableParameter(v-9);
                    }
                    /*SL:739*/a3 = Data.resolveWildcardTypeOrTypeVariable(v-8, a3);
                    /*SL:740*/this.<Object>parseArray(v-10, a2, a3, v-8, v-6);
                    /*SL:741*/if (a1) {
                        /*SL:742*/return Types.toArray(a2, Types.getRawArrayComponentType(v-8, a3));
                    }
                    /*SL:744*/return a2;
                }
                case START_OBJECT:
                case FIELD_NAME:
                case END_OBJECT: {
                    /*SL:748*/Preconditions.checkArgument(/*EL:749*/!Types.isArray(v-9), "expected object or map type but got %s", v-9);
                    final Field v1 = /*EL:751*/v-5 ? getCachedTypemapFieldFor(rawClass) : null;
                    Object v2 = /*EL:752*/null;
                    /*SL:753*/if (rawClass != null && v-6 != null) {
                        /*SL:754*/v2 = v-6.newInstanceForObject(v-7, rawClass);
                    }
                    final boolean v3 = /*EL:756*/rawClass != null && Types.isAssignableToOrFrom(rawClass, Map.class);
                    /*SL:757*/if (v1 != null) {
                        /*SL:758*/v2 = new GenericJson();
                    }
                    else/*SL:759*/ if (v2 == null) {
                        /*SL:761*/if (v3 || rawClass == null) {
                            /*SL:762*/v2 = Data.newMapInstance(rawClass);
                        }
                        else {
                            /*SL:764*/v2 = Types.<Object>newInstance(rawClass);
                        }
                    }
                    final int v4 = /*EL:767*/v-8.size();
                    /*SL:768*/if (v-9 != null) {
                        /*SL:769*/v-8.add(v-9);
                    }
                    /*SL:771*/if (v3 && !GenericData.class.isAssignableFrom(rawClass)) {
                        final Type a4 = /*EL:772*/Map.class.isAssignableFrom(rawClass) ? /*EL:773*/Types.getMapValueParameter(v-9) : null;
                        /*SL:774*/if (a4 != null) {
                            final Map<String, Object> a5 = /*EL:776*/(Map<String, Object>)v2;
                            /*SL:777*/this.parseMap(v-10, a5, a4, v-8, v-6);
                            /*SL:778*/return v2;
                        }
                    }
                    /*SL:781*/this.parse(v-8, v2, v-6);
                    /*SL:782*/if (v-9 != null) {
                        /*SL:783*/v-8.remove(v4);
                    }
                    /*SL:785*/if (v1 == null) {
                        /*SL:786*/return v2;
                    }
                    final Object v5 = /*EL:790*/((GenericJson)v2).get(v1.getName());
                    /*SL:791*/Preconditions.checkArgument(v5 != null, (Object)"No value specified for @JsonPolymorphicTypeMap field");
                    final String v6 = /*EL:793*/v5.toString();
                    final JsonPolymorphicTypeMap v7 = /*EL:794*/v1.<JsonPolymorphicTypeMap>getAnnotation(JsonPolymorphicTypeMap.class);
                    Class<?> v8 = /*EL:795*/null;
                    /*SL:796*/for (final JsonPolymorphicTypeMap.TypeDef a6 : v7.typeDefinitions()) {
                        /*SL:797*/if (a6.key().equals(v6)) {
                            /*SL:798*/v8 = a6.ref();
                            /*SL:799*/break;
                        }
                    }
                    /*SL:802*/Preconditions.checkArgument(v8 != null, (Object)("No TypeDef annotation found with key: " + v6));
                    final JsonFactory v9 = /*EL:804*/this.getFactory();
                    final JsonParser v10 = /*EL:806*/v9.createJsonParser(v9.toString(v2));
                    /*SL:807*/v10.startParsing();
                    /*SL:808*/return v10.parseValue(v-10, v8, v-8, null, null, false);
                }
                case VALUE_TRUE:
                case VALUE_FALSE: {
                    /*SL:811*/Preconditions.checkArgument(v-9 == null || rawClass == Boolean.TYPE || (rawClass != null && rawClass.isAssignableFrom(Boolean.class)), /*EL:812*/"expected type Boolean or boolean but got %s", v-9);
                    /*SL:814*/return (currentToken == JsonToken.VALUE_TRUE) ? Boolean.TRUE : Boolean.FALSE;
                }
                case VALUE_NUMBER_FLOAT:
                case VALUE_NUMBER_INT: {
                    /*SL:817*/Preconditions.checkArgument(v-10 == null || v-10.<JsonString>getAnnotation(JsonString.class) == /*EL:818*/null, (Object)"number type formatted as a JSON number cannot use @JsonString annotation");
                    /*SL:820*/if (rawClass == null || rawClass.isAssignableFrom(BigDecimal.class)) {
                        /*SL:821*/return this.getDecimalValue();
                    }
                    /*SL:823*/if (rawClass == BigInteger.class) {
                        /*SL:824*/return this.getBigIntegerValue();
                    }
                    /*SL:826*/if (rawClass == Double.class || rawClass == Double.TYPE) {
                        /*SL:827*/return this.getDoubleValue();
                    }
                    /*SL:829*/if (rawClass == Long.class || rawClass == Long.TYPE) {
                        /*SL:830*/return this.getLongValue();
                    }
                    /*SL:832*/if (rawClass == Float.class || rawClass == Float.TYPE) {
                        /*SL:833*/return this.getFloatValue();
                    }
                    /*SL:835*/if (rawClass == Integer.class || rawClass == Integer.TYPE) {
                        /*SL:836*/return this.getIntValue();
                    }
                    /*SL:838*/if (rawClass == Short.class || rawClass == Short.TYPE) {
                        /*SL:839*/return this.getShortValue();
                    }
                    /*SL:841*/if (rawClass == Byte.class || rawClass == Byte.TYPE) {
                        /*SL:842*/return this.getByteValue();
                    }
                    /*SL:844*/throw new IllegalArgumentException("expected numeric type but got " + v-9);
                }
                case VALUE_STRING: {
                    final String v11 = /*EL:847*/this.getText().trim().toLowerCase(Locale.US);
                    /*SL:851*/if ((rawClass != Float.TYPE && rawClass != Float.class && rawClass != Double.TYPE && rawClass != Double.class) || (!v11.equals("nan") && /*EL:853*/!v11.equals("infinity") && !v11.equals("-infinity"))) {
                        /*SL:854*/Preconditions.checkArgument(rawClass == null || !Number.class.isAssignableFrom(rawClass) || /*EL:855*/(v-10 != null && v-10.<JsonString>getAnnotation(JsonString.class) != /*EL:856*/null), (Object)"number field formatted as a JSON string must use the @JsonString annotation");
                    }
                    /*SL:859*/return Data.parsePrimitiveValue(v-9, this.getText());
                }
                case VALUE_NULL: {
                    /*SL:861*/Preconditions.checkArgument(rawClass == null || !rawClass.isPrimitive(), (Object)"primitive number field but found a JSON null");
                    /*SL:863*/if (rawClass != null && 0x0 != (rawClass.getModifiers() & /*EL:864*/0x600)) {
                        /*SL:865*/if (Types.isAssignableToOrFrom(rawClass, Collection.class)) {
                            /*SL:866*/return Data.<Object>nullOf(Data.newCollectionInstance(v-9).getClass());
                        }
                        /*SL:868*/if (Types.isAssignableToOrFrom(rawClass, Map.class)) {
                            /*SL:869*/return Data.<Object>nullOf(Data.newMapInstance(rawClass).getClass());
                        }
                    }
                    /*SL:872*/return Data.<Object>nullOf(Types.getRawArrayComponentType(v-8, v-9));
                }
                default: {
                    /*SL:874*/throw new IllegalArgumentException("unexpected JSON node type: " + currentToken);
                }
            }
        }
        catch (IllegalArgumentException ex) {
            final StringBuilder sb = /*EL:878*/new StringBuilder();
            final String v12 = /*EL:879*/this.getCurrentName();
            /*SL:880*/if (v12 != null) {
                /*SL:881*/sb.append("key ").append(v12);
            }
            /*SL:883*/if (v-10 != null) {
                /*SL:884*/if (v12 != null) {
                    /*SL:885*/sb.append(", ");
                }
                /*SL:887*/sb.append("field ").append(v-10);
            }
            /*SL:889*/throw new IllegalArgumentException(sb.toString(), ex);
        }
    }
    
    private static Field getCachedTypemapFieldFor(final Class<?> v-6) {
        /*SL:906*/if (v-6 == null) {
            /*SL:907*/return null;
        }
        JsonParser.lock.lock();
        try {
            /*SL:913*/if (JsonParser.cachedTypemapFields.containsKey(v-6)) {
                /*SL:914*/return JsonParser.cachedTypemapFields.get(v-6);
            }
            Field field = /*EL:917*/null;
            final Collection<FieldInfo> fieldInfos = /*EL:918*/ClassInfo.of(v-6).getFieldInfos();
            /*SL:919*/for (final FieldInfo fieldInfo : fieldInfos) {
                final Field field2 = /*EL:920*/fieldInfo.getField();
                final JsonPolymorphicTypeMap v0 = /*EL:921*/field2.<JsonPolymorphicTypeMap>getAnnotation(JsonPolymorphicTypeMap.class);
                /*SL:923*/if (v0 != null) {
                    /*SL:924*/Preconditions.checkArgument(field == null, "Class contains more than one field with @JsonPolymorphicTypeMap annotation: %s", v-6);
                    /*SL:927*/Preconditions.checkArgument(Data.isPrimitive(field2.getType()), "Field which has the @JsonPolymorphicTypeMap, %s, is not a supported type: %s", v-6, field2.getType());
                    /*SL:930*/field = field2;
                    final JsonPolymorphicTypeMap.TypeDef[] v = /*EL:932*/v0.typeDefinitions();
                    final HashSet<String> v2 = /*EL:933*/Sets.<String>newHashSet();
                    /*SL:934*/Preconditions.checkArgument(v.length > 0, (Object)"@JsonPolymorphicTypeMap must have at least one @TypeDef");
                    /*SL:936*/for (final JsonPolymorphicTypeMap.TypeDef a1 : v) {
                        /*SL:937*/Preconditions.checkArgument(v2.add(a1.key()), "Class contains two @TypeDef annotations with identical key: %s", a1.key());
                    }
                }
            }
            JsonParser.cachedTypemapFields.put(/*EL:942*/v-6, field);
            /*SL:943*/return field;
        }
        finally {
            JsonParser.lock.unlock();
        }
    }
    
    static {
        JsonParser.cachedTypemapFields = new WeakHashMap<Class<?>, Field>();
        lock = new ReentrantLock();
    }
}

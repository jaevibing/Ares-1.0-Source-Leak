package com.google.api.client.http;

import com.google.api.client.util.Preconditions;
import java.util.HashMap;
import java.util.ListIterator;
import com.google.api.client.util.escape.CharEscapers;
import com.google.api.client.util.FieldInfo;
import com.google.api.client.util.Types;
import com.google.api.client.repackaged.com.google.common.base.Splitter;
import java.util.Iterator;
import com.google.api.client.util.Data;
import java.util.LinkedHashMap;
import java.util.Map;

public class UriTemplate
{
    static final Map<Character, CompositeOutput> COMPOSITE_PREFIXES;
    private static final String COMPOSITE_NON_EXPLODE_JOINER = ",";
    
    static CompositeOutput getCompositeOutput(final String a1) {
        final CompositeOutput v1 = UriTemplate.COMPOSITE_PREFIXES.get(/*EL:202*/a1.charAt(0));
        /*SL:203*/return (v1 == null) ? CompositeOutput.SIMPLE : v1;
    }
    
    private static Map<String, Object> getMap(final Object v-2) {
        final Map<String, Object> map = /*EL:216*/new LinkedHashMap<String, Object>();
        /*SL:217*/for (final Map.Entry<String, Object> v1 : Data.mapOf(v-2).entrySet()) {
            final Object a1 = /*EL:218*/v1.getValue();
            /*SL:219*/if (a1 != null && !Data.isNull(a1)) {
                /*SL:220*/map.put(v1.getKey(), a1);
            }
        }
        /*SL:223*/return map;
    }
    
    public static String expand(final String a4, final String v1, final Object v2, final boolean v3) {
        final String v4;
        /*SL:252*/if (v1.startsWith("/")) {
            final GenericUrl a5 = /*EL:254*/new GenericUrl(a4);
            /*SL:255*/a5.setRawPath(null);
            final String a6 = /*EL:256*/a5.build() + v1;
        }
        else/*SL:257*/ if (!v1.startsWith("http://") && !v1.startsWith("https://")) {
            /*SL:260*/v4 = a4 + v1;
        }
        /*SL:262*/return expand(v4, v2, v3);
    }
    
    public static String expand(final String v-18, final Object v-17, final boolean v-16) {
        final Map<String, Object> map = getMap(/*EL:284*/v-17);
        final StringBuilder v-19 = /*EL:285*/new StringBuilder();
        int i = /*EL:286*/0;
        final int length = /*EL:287*/v-18.length();
        /*SL:288*/while (i < length) {
            final int index = /*EL:289*/v-18.indexOf(123, i);
            /*SL:290*/if (index == -1) {
                /*SL:291*/if (i == 0 && !v-16) {
                    /*SL:293*/return v-18;
                }
                /*SL:295*/v-19.append(v-18.substring(i));
                /*SL:296*/break;
            }
            else {
                /*SL:298*/v-19.append(v-18.substring(i, index));
                final int index2 = /*EL:299*/v-18.indexOf(125, index + 2);
                /*SL:300*/i = index2 + 1;
                final String substring = /*EL:302*/v-18.substring(index + 1, index2);
                final CompositeOutput compositeOutput = getCompositeOutput(/*EL:303*/substring);
                final ListIterator<String> listIterator = /*EL:305*/Splitter.on(',').splitToList(substring).listIterator();
                boolean b = /*EL:306*/true;
                /*SL:307*/while (listIterator.hasNext()) {
                    final String s = /*EL:308*/listIterator.next();
                    final boolean endsWith = /*EL:309*/s.endsWith("*");
                    final int n = /*EL:311*/(listIterator.nextIndex() == 1) ? compositeOutput.getVarNameStartIndex() : /*EL:312*/0;
                    int length2 = /*EL:313*/s.length();
                    /*SL:314*/if (endsWith) {
                        /*SL:316*/--length2;
                    }
                    final String substring2 = /*EL:319*/s.substring(n, length2);
                    Object v0 = /*EL:321*/map.remove(substring2);
                    /*SL:322*/if (v0 == null) {
                        /*SL:324*/continue;
                    }
                    /*SL:326*/if (!b) {
                        /*SL:327*/v-19.append(compositeOutput.getExplodeJoiner());
                    }
                    else {
                        /*SL:329*/v-19.append(compositeOutput.getOutputPrefix());
                        /*SL:330*/b = false;
                    }
                    /*SL:332*/if (v0 instanceof Iterator) {
                        final Iterator<?> a1 = /*EL:334*/(Iterator<?>)v0;
                        /*SL:335*/v0 = getListPropertyValue(substring2, a1, endsWith, compositeOutput);
                    }
                    else/*SL:336*/ if (v0 instanceof Iterable || v0.getClass().isArray()) {
                        final Iterator<?> a1 = /*EL:338*/Types.<Object>iterableOf(v0).iterator();
                        /*SL:339*/v0 = getListPropertyValue(substring2, a1, endsWith, compositeOutput);
                    }
                    else/*SL:340*/ if (v0.getClass().isEnum()) {
                        final String a2 = /*EL:341*/FieldInfo.of((Enum<?>)v0).getName();
                        /*SL:342*/if (a2 != null) {
                            /*SL:343*/if (compositeOutput.requiresVarAssignment()) {
                                /*SL:344*/v0 = String.format("%s=%s", substring2, v0);
                            }
                            /*SL:346*/v0 = CharEscapers.escapeUriPath(v0.toString());
                        }
                    }
                    else/*SL:348*/ if (!Data.isValueOfPrimitiveType(v0)) {
                        final Map<String, Object> a3 = getMap(/*EL:350*/v0);
                        /*SL:351*/v0 = getMapPropertyValue(substring2, a3, endsWith, compositeOutput);
                    }
                    else {
                        /*SL:354*/if (compositeOutput.requiresVarAssignment()) {
                            /*SL:355*/v0 = String.format("%s=%s", substring2, v0);
                        }
                        /*SL:357*/if (compositeOutput.getReservedExpansion()) {
                            /*SL:358*/v0 = CharEscapers.escapeUriPathWithoutReserved(v0.toString());
                        }
                        else {
                            /*SL:360*/v0 = CharEscapers.escapeUriPath(v0.toString());
                        }
                    }
                    /*SL:363*/v-19.append(v0);
                }
            }
        }
        /*SL:366*/if (v-16) {
            /*SL:368*/GenericUrl.addQueryParams(map.entrySet(), v-19);
        }
        /*SL:370*/return v-19.toString();
    }
    
    private static String getListPropertyValue(final String a2, final Iterator<?> a3, final boolean a4, final CompositeOutput v1) {
        /*SL:388*/if (!a3.hasNext()) {
            /*SL:389*/return "";
        }
        final StringBuilder v2 = /*EL:391*/new StringBuilder();
        final String v3;
        /*SL:393*/if (a4) {
            final String a5 = /*EL:394*/v1.getExplodeJoiner();
        }
        else {
            /*SL:396*/v3 = ",";
            /*SL:397*/if (v1.requiresVarAssignment()) {
                /*SL:398*/v2.append(CharEscapers.escapeUriPath(a2));
                /*SL:399*/v2.append("=");
            }
        }
        /*SL:402*/while (a3.hasNext()) {
            /*SL:403*/if (a4 && v1.requiresVarAssignment()) {
                /*SL:404*/v2.append(CharEscapers.escapeUriPath(a2));
                /*SL:405*/v2.append("=");
            }
            /*SL:407*/v2.append(v1.getEncodedValue(a3.next().toString()));
            /*SL:408*/if (a3.hasNext()) {
                /*SL:409*/v2.append(v3);
            }
        }
        /*SL:412*/return v2.toString();
    }
    
    private static String getMapPropertyValue(final String v-9, final Map<String, Object> v-8, final boolean v-7, final CompositeOutput v-6) {
        /*SL:430*/if (v-8.isEmpty()) {
            /*SL:431*/return "";
        }
        final StringBuilder sb = /*EL:433*/new StringBuilder();
        final String s;
        final String s2;
        /*SL:436*/if (v-7) {
            final String a1 = /*EL:437*/v-6.getExplodeJoiner();
            final String a2 = /*EL:438*/"=";
        }
        else {
            /*SL:440*/s = ",";
            /*SL:441*/s2 = ",";
            /*SL:442*/if (v-6.requiresVarAssignment()) {
                /*SL:443*/sb.append(CharEscapers.escapeUriPath(v-9));
                /*SL:444*/sb.append("=");
            }
        }
        final Iterator<Map.Entry<String, Object>> iterator = /*EL:447*/v-8.entrySet().iterator();
        /*SL:448*/while (iterator.hasNext()) {
            final Map.Entry<String, Object> a3 = /*EL:449*/iterator.next();
            final String a4 = /*EL:450*/v-6.getEncodedValue(a3.getKey());
            final String v1 = /*EL:451*/v-6.getEncodedValue(a3.getValue().toString());
            /*SL:452*/sb.append(a4);
            /*SL:453*/sb.append(s2);
            /*SL:454*/sb.append(v1);
            /*SL:455*/if (iterator.hasNext()) {
                /*SL:456*/sb.append(s);
            }
        }
        /*SL:459*/return sb.toString();
    }
    
    static {
        COMPOSITE_PREFIXES = new HashMap<Character, CompositeOutput>();
        CompositeOutput.values();
    }
    
    private enum CompositeOutput
    {
        PLUS('+', "", ",", false, true), 
        HASH('#', "#", ",", false, true), 
        DOT('.', ".", ".", false, false), 
        FORWARD_SLASH('/', "/", "/", false, false), 
        SEMI_COLON(';', ";", ";", true, false), 
        QUERY('?', "?", "&", true, false), 
        AMP('&', "&", "&", true, false), 
        SIMPLE((Character)null, "", ",", false, false);
        
        private final Character propertyPrefix;
        private final String outputPrefix;
        private final String explodeJoiner;
        private final boolean requiresVarAssignment;
        private final boolean reservedExpansion;
        
        private CompositeOutput(final Character a1, final String a2, final String a3, final boolean a4, final boolean a5) {
            this.propertyPrefix = a1;
            this.outputPrefix = Preconditions.<String>checkNotNull(a2);
            this.explodeJoiner = Preconditions.<String>checkNotNull(a3);
            this.requiresVarAssignment = a4;
            this.reservedExpansion = a5;
            if (a1 != null) {
                UriTemplate.COMPOSITE_PREFIXES.put(a1, this);
            }
        }
        
        String getOutputPrefix() {
            /*SL:152*/return this.outputPrefix;
        }
        
        String getExplodeJoiner() {
            /*SL:159*/return this.explodeJoiner;
        }
        
        boolean requiresVarAssignment() {
            /*SL:166*/return this.requiresVarAssignment;
        }
        
        int getVarNameStartIndex() {
            /*SL:174*/return (this.propertyPrefix != null) ? 1 : 0;
        }
        
        String getEncodedValue(final String v2) {
            final String v3;
            /*SL:187*/if (this.reservedExpansion) {
                final String a1 = /*EL:189*/CharEscapers.escapeUriPath(v2);
            }
            else {
                /*SL:191*/v3 = CharEscapers.escapeUri(v2);
            }
            /*SL:193*/return v3;
        }
        
        boolean getReservedExpansion() {
            /*SL:197*/return this.reservedExpansion;
        }
    }
}

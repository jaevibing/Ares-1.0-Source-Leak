package org.reflections.util;

import java.util.regex.Pattern;
import org.reflections.ReflectionsException;
import java.util.ArrayList;
import java.util.Iterator;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import java.util.List;
import com.google.common.base.Predicate;

public class FilterBuilder implements Predicate<String>
{
    private final List<Predicate<String>> chain;
    
    public FilterBuilder() {
        this.chain = (List<Predicate<String>>)Lists.<Object>newArrayList();
    }
    
    private FilterBuilder(final Iterable<Predicate<String>> a1) {
        this.chain = (List<Predicate<String>>)Lists.<Object>newArrayList((Iterable<?>)a1);
    }
    
    public FilterBuilder include(final String a1) {
        /*SL:28*/return this.add(new Include(a1));
    }
    
    public FilterBuilder exclude(final String a1) {
        /*SL:31*/this.add(new Exclude(a1));
        return this;
    }
    
    public FilterBuilder add(final Predicate<String> a1) {
        /*SL:34*/this.chain.add(a1);
        return this;
    }
    
    public FilterBuilder includePackage(final Class<?> a1) {
        /*SL:37*/return this.add(new Include(packageNameRegex(a1)));
    }
    
    public FilterBuilder excludePackage(final Class<?> a1) {
        /*SL:40*/return this.add(new Exclude(packageNameRegex(a1)));
    }
    
    public FilterBuilder includePackage(final String... v2) {
        /*SL:44*/for (final String a1 : v2) {
            /*SL:45*/this.add(new Include(prefix(a1)));
        }
        /*SL:47*/return this;
    }
    
    public FilterBuilder excludePackage(final String a1) {
        /*SL:51*/return this.add(new Exclude(prefix(a1)));
    }
    
    private static String packageNameRegex(final Class<?> a1) {
        /*SL:53*/return prefix(a1.getPackage().getName() + ".");
    }
    
    public static String prefix(final String a1) {
        /*SL:55*/return a1.replace(".", "\\.") + ".*";
    }
    
    @Override
    public String toString() {
        /*SL:57*/return Joiner.on(", ").join(this.chain);
    }
    
    @Override
    public boolean apply(final String v2) {
        boolean v3 = /*EL:60*/this.chain == null || this.chain.isEmpty() || this.chain.get(0) instanceof Exclude;
        /*SL:62*/if (this.chain != null) {
            /*SL:63*/for (final Predicate<String> a1 : this.chain) {
                /*SL:64*/if (v3 && a1 instanceof Include) {
                    continue;
                }
                /*SL:65*/if (!v3 && a1 instanceof Exclude) {
                    continue;
                }
                /*SL:66*/v3 = a1.apply(v2);
                /*SL:67*/if (!v3 && a1 instanceof Exclude) {
                    break;
                }
            }
        }
        /*SL:70*/return v3;
    }
    
    public static FilterBuilder parse(final String v-8) {
        final List<Predicate<String>> a2 = /*EL:105*/new ArrayList<Predicate<String>>();
        /*SL:107*/if (!Utils.isEmpty(v-8)) {
            /*SL:108*/for (final String s : v-8.split(",")) {
                final String trim = /*EL:109*/s.trim();
                final char char1 = /*EL:110*/trim.charAt(0);
                final String v0 = /*EL:111*/trim.substring(1);
                Predicate<String> a1 = null;
                /*SL:114*/switch (char1) {
                    case '+': {
                        /*SL:116*/a1 = new Include(v0);
                        /*SL:117*/break;
                    }
                    case '-': {
                        /*SL:119*/a1 = new Exclude(v0);
                        /*SL:120*/break;
                    }
                    default: {
                        /*SL:122*/throw new ReflectionsException("includeExclude should start with either + or -");
                    }
                }
                /*SL:125*/a2.add(a1);
            }
            /*SL:128*/return new FilterBuilder(a2);
        }
        /*SL:130*/return new FilterBuilder();
    }
    
    public static FilterBuilder parsePackages(final String v-8) {
        final List<Predicate<String>> a2 = /*EL:146*/new ArrayList<Predicate<String>>();
        /*SL:148*/if (!Utils.isEmpty(v-8)) {
            /*SL:149*/for (final String s : v-8.split(",")) {
                final String trim = /*EL:150*/s.trim();
                final char char1 = /*EL:151*/trim.charAt(0);
                String v0 = /*EL:152*/trim.substring(1);
                /*SL:153*/if (!v0.endsWith(".")) {
                    /*SL:154*/v0 += ".";
                }
                /*SL:156*/v0 = prefix(v0);
                Predicate<String> a1 = null;
                /*SL:159*/switch (char1) {
                    case '+': {
                        /*SL:161*/a1 = new Include(v0);
                        /*SL:162*/break;
                    }
                    case '-': {
                        /*SL:164*/a1 = new Exclude(v0);
                        /*SL:165*/break;
                    }
                    default: {
                        /*SL:167*/throw new ReflectionsException("includeExclude should start with either + or -");
                    }
                }
                /*SL:170*/a2.add(a1);
            }
            /*SL:173*/return new FilterBuilder(a2);
        }
        /*SL:175*/return new FilterBuilder();
    }
    
    public abstract static class Matcher implements Predicate<String>
    {
        final Pattern pattern;
        
        public Matcher(final String a1) {
            this.pattern = Pattern.compile(a1);
        }
        
        @Override
        public abstract boolean apply(final String p0);
        
        @Override
        public String toString() {
            /*SL:77*/return this.pattern.pattern();
        }
    }
    
    public static class Include extends Matcher
    {
        public Include(final String a1) {
            super(a1);
        }
        
        @Override
        public boolean apply(final String a1) {
            /*SL:82*/return this.pattern.matcher(a1).matches();
        }
        
        @Override
        public String toString() {
            /*SL:83*/return "+" + super.toString();
        }
    }
    
    public static class Exclude extends Matcher
    {
        public Exclude(final String a1) {
            super(a1);
        }
        
        @Override
        public boolean apply(final String a1) {
            /*SL:88*/return !this.pattern.matcher(a1).matches();
        }
        
        @Override
        public String toString() {
            /*SL:89*/return "-" + super.toString();
        }
    }
}

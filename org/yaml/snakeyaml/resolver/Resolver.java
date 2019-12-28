package org.yaml.snakeyaml.resolver;

import java.util.Iterator;
import org.yaml.snakeyaml.nodes.NodeId;
import java.util.ArrayList;
import java.util.HashMap;
import org.yaml.snakeyaml.nodes.Tag;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class Resolver
{
    public static final Pattern BOOL;
    public static final Pattern FLOAT;
    public static final Pattern INT;
    public static final Pattern MERGE;
    public static final Pattern NULL;
    public static final Pattern EMPTY;
    public static final Pattern TIMESTAMP;
    public static final Pattern VALUE;
    public static final Pattern YAML;
    protected Map<Character, List<ResolverTuple>> yamlImplicitResolvers;
    
    protected void addImplicitResolvers() {
        /*SL:53*/this.addImplicitResolver(Tag.BOOL, Resolver.BOOL, "yYnNtTfFoO");
        /*SL:59*/this.addImplicitResolver(Tag.INT, Resolver.INT, "-+0123456789");
        /*SL:60*/this.addImplicitResolver(Tag.FLOAT, Resolver.FLOAT, "-+0123456789.");
        /*SL:61*/this.addImplicitResolver(Tag.MERGE, Resolver.MERGE, "<");
        /*SL:62*/this.addImplicitResolver(Tag.NULL, Resolver.NULL, "~nN\u0000");
        /*SL:63*/this.addImplicitResolver(Tag.NULL, Resolver.EMPTY, null);
        /*SL:64*/this.addImplicitResolver(Tag.TIMESTAMP, Resolver.TIMESTAMP, "0123456789");
        /*SL:69*/this.addImplicitResolver(Tag.YAML, Resolver.YAML, "!&*");
    }
    
    public Resolver() {
        this.yamlImplicitResolvers = new HashMap<Character, List<ResolverTuple>>();
        this.addImplicitResolvers();
    }
    
    public void addImplicitResolver(final Tag v-3, final Pattern v-2, final String v-1) {
        /*SL:77*/if (v-1 == null) {
            List<ResolverTuple> a1 = /*EL:78*/this.yamlImplicitResolvers.get(null);
            /*SL:79*/if (a1 == null) {
                /*SL:80*/a1 = new ArrayList<ResolverTuple>();
                /*SL:81*/this.yamlImplicitResolvers.put(null, a1);
            }
            /*SL:83*/a1.add(new ResolverTuple(v-3, v-2));
        }
        else {
            final char[] v0 = /*EL:85*/v-1.toCharArray();
            /*SL:86*/for (int v = 0, v2 = v0.length; v < v2; ++v) {
                Character a2 = /*EL:87*/v0[v];
                /*SL:88*/if (a2 == '\0') {
                    /*SL:90*/a2 = null;
                }
                List<ResolverTuple> a3 = /*EL:92*/this.yamlImplicitResolvers.get(a2);
                /*SL:93*/if (a3 == null) {
                    /*SL:94*/a3 = new ArrayList<ResolverTuple>();
                    /*SL:95*/this.yamlImplicitResolvers.put(a2, a3);
                }
                /*SL:97*/a3.add(new ResolverTuple(v-3, v-2));
            }
        }
    }
    
    public Tag resolve(final NodeId v-5, final String v-4, final boolean v-3) {
        /*SL:103*/if (v-5 == NodeId.scalar && v-3) {
            List<ResolverTuple> list = /*EL:104*/null;
            /*SL:105*/if (v-4.length() == 0) {
                /*SL:106*/list = this.yamlImplicitResolvers.get('\0');
            }
            else {
                /*SL:108*/list = this.yamlImplicitResolvers.get(v-4.charAt(0));
            }
            /*SL:110*/if (list != null) {
                /*SL:111*/for (Pattern a3 : list) {
                    final Tag a2 = /*EL:112*/a3.getTag();
                    /*SL:113*/a3 = a3.getRegexp();
                    /*SL:114*/if (a3.matcher(v-4).matches()) {
                        /*SL:115*/return a2;
                    }
                }
            }
            /*SL:119*/if (this.yamlImplicitResolvers.containsKey(null)) {
                /*SL:120*/for (final ResolverTuple v0 : this.yamlImplicitResolvers.get(null)) {
                    final Tag v = /*EL:121*/v0.getTag();
                    final Pattern v2 = /*EL:122*/v0.getRegexp();
                    /*SL:123*/if (v2.matcher(v-4).matches()) {
                        /*SL:124*/return v;
                    }
                }
            }
        }
        /*SL:129*/switch (v-5) {
            case scalar: {
                /*SL:131*/return Tag.STR;
            }
            case sequence: {
                /*SL:133*/return Tag.SEQ;
            }
            default: {
                /*SL:135*/return Tag.MAP;
            }
        }
    }
    
    static {
        BOOL = Pattern.compile("^(?:yes|Yes|YES|no|No|NO|true|True|TRUE|false|False|FALSE|on|On|ON|off|Off|OFF)$");
        FLOAT = Pattern.compile("^([-+]?(\\.[0-9]+|[0-9_]+(\\.[0-9_]*)?)([eE][-+]?[0-9]+)?|[-+]?[0-9][0-9_]*(?::[0-5]?[0-9])+\\.[0-9_]*|[-+]?\\.(?:inf|Inf|INF)|\\.(?:nan|NaN|NAN))$");
        INT = Pattern.compile("^(?:[-+]?0b[0-1_]+|[-+]?0[0-7_]+|[-+]?(?:0|[1-9][0-9_]*)|[-+]?0x[0-9a-fA-F_]+|[-+]?[1-9][0-9_]*(?::[0-5]?[0-9])+)$");
        MERGE = Pattern.compile("^(?:<<)$");
        NULL = Pattern.compile("^(?:~|null|Null|NULL| )$");
        EMPTY = Pattern.compile("^$");
        TIMESTAMP = Pattern.compile("^(?:[0-9][0-9][0-9][0-9]-[0-9][0-9]-[0-9][0-9]|[0-9][0-9][0-9][0-9]-[0-9][0-9]?-[0-9][0-9]?(?:[Tt]|[ \t]+)[0-9][0-9]?:[0-9][0-9]:[0-9][0-9](?:\\.[0-9]*)?(?:[ \t]*(?:Z|[-+][0-9][0-9]?(?::[0-9][0-9])?))?)$");
        VALUE = Pattern.compile("^(?:=)$");
        YAML = Pattern.compile("^(?:!|&|\\*)$");
    }
}

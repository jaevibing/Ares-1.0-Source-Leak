package org.yaml.snakeyaml.representer;

import org.yaml.snakeyaml.nodes.MappingNode;
import org.yaml.snakeyaml.nodes.NodeTuple;
import org.yaml.snakeyaml.nodes.SequenceNode;
import java.util.ArrayList;
import java.util.List;
import org.yaml.snakeyaml.error.Mark;
import org.yaml.snakeyaml.nodes.ScalarNode;
import org.yaml.snakeyaml.nodes.Tag;
import java.util.Iterator;
import org.yaml.snakeyaml.nodes.AnchorNode;
import java.util.IdentityHashMap;
import java.util.LinkedHashMap;
import java.util.HashMap;
import org.yaml.snakeyaml.introspector.PropertyUtils;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.DumperOptions;
import java.util.Map;

public abstract class BaseRepresenter
{
    protected final Map<Class<?>, Represent> representers;
    protected Represent nullRepresenter;
    protected final Map<Class<?>, Represent> multiRepresenters;
    protected Character defaultScalarStyle;
    protected DumperOptions.FlowStyle defaultFlowStyle;
    protected final Map<Object, Node> representedObjects;
    protected Object objectToRepresent;
    private PropertyUtils propertyUtils;
    private boolean explicitPropertyUtils;
    
    public BaseRepresenter() {
        this.representers = new HashMap<Class<?>, Represent>();
        this.multiRepresenters = new LinkedHashMap<Class<?>, Represent>();
        this.defaultFlowStyle = DumperOptions.FlowStyle.AUTO;
        this.representedObjects = new IdentityHashMap<Object, Node>() {
            private static final long serialVersionUID = -5576159264232131854L;
            
            @Override
            public Node put(final Object a1, final Node a2) {
                /*SL:55*/return super.put(a1, new AnchorNode(a2));
            }
        };
        this.explicitPropertyUtils = false;
    }
    
    public Node represent(final Object a1) {
        final Node v1 = /*EL:64*/this.representData(a1);
        /*SL:65*/this.representedObjects.clear();
        /*SL:66*/this.objectToRepresent = null;
        /*SL:67*/return v1;
    }
    
    protected final Node representData(final Object v0) {
        /*SL:71*/this.objectToRepresent = v0;
        /*SL:73*/if (this.representedObjects.containsKey(this.objectToRepresent)) {
            final Node a1 = /*EL:74*/this.representedObjects.get(this.objectToRepresent);
            /*SL:75*/return a1;
        }
        /*SL:79*/if (v0 == null) {
            final Node v = /*EL:80*/this.nullRepresenter.representData(null);
            /*SL:81*/return v;
        }
        final Class<?> v2 = /*EL:85*/v0.getClass();
        Node v;
        /*SL:86*/if (this.representers.containsKey(v2)) {
            final Represent v3 = /*EL:87*/this.representers.get(v2);
            /*SL:88*/v = v3.representData(v0);
        }
        else {
            /*SL:91*/for (final Class<?> v4 : this.multiRepresenters.keySet()) {
                /*SL:92*/if (v4 != null && v4.isInstance(v0)) {
                    final Represent v5 = /*EL:93*/this.multiRepresenters.get(v4);
                    /*SL:94*/v = v5.representData(v0);
                    /*SL:95*/return v;
                }
            }
            /*SL:100*/if (this.multiRepresenters.containsKey(null)) {
                final Represent v3 = /*EL:101*/this.multiRepresenters.get(null);
                /*SL:102*/v = v3.representData(v0);
            }
            else {
                final Represent v3 = /*EL:104*/this.representers.get(null);
                /*SL:105*/v = v3.representData(v0);
            }
        }
        /*SL:108*/return v;
    }
    
    protected Node representScalar(final Tag a1, final String a2, Character a3) {
        /*SL:112*/if (a3 == null) {
            /*SL:113*/a3 = this.defaultScalarStyle;
        }
        final Node v1 = /*EL:115*/new ScalarNode(a1, a2, null, null, a3);
        /*SL:116*/return v1;
    }
    
    protected Node representScalar(final Tag a1, final String a2) {
        /*SL:120*/return this.representScalar(a1, a2, null);
    }
    
    protected Node representSequence(final Tag v1, final Iterable<?> v2, final Boolean v3) {
        int v4 = /*EL:124*/10;
        /*SL:125*/if (v2 instanceof List) {
            /*SL:126*/v4 = ((List)v2).size();
        }
        final List<Node> v5 = /*EL:128*/new ArrayList<Node>(v4);
        final SequenceNode v6 = /*EL:129*/new SequenceNode(v1, v5, v3);
        /*SL:130*/this.representedObjects.put(this.objectToRepresent, v6);
        boolean v7 = /*EL:131*/true;
        /*SL:132*/for (Node a2 : v2) {
            /*SL:133*/a2 = this.representData(a2);
            /*SL:134*/if (!(a2 instanceof ScalarNode) || ((ScalarNode)a2).getStyle() != null) {
                /*SL:135*/v7 = false;
            }
            /*SL:137*/v5.add(a2);
        }
        /*SL:139*/if (v3 == null) {
            /*SL:140*/if (this.defaultFlowStyle != DumperOptions.FlowStyle.AUTO) {
                /*SL:141*/v6.setFlowStyle(this.defaultFlowStyle.getStyleBoolean());
            }
            else {
                /*SL:143*/v6.setFlowStyle(v7);
            }
        }
        /*SL:146*/return v6;
    }
    
    protected Node representMapping(final Tag v2, final Map<?, ?> v3, final Boolean v4) {
        final List<NodeTuple> v5 = /*EL:150*/new ArrayList<NodeTuple>(v3.size());
        final MappingNode v6 = /*EL:151*/new MappingNode(v2, v5, v4);
        /*SL:152*/this.representedObjects.put(this.objectToRepresent, v6);
        boolean v7 = /*EL:153*/true;
        /*SL:154*/for (Node a3 : v3.entrySet()) {
            final Node a2 = /*EL:155*/this.representData(a3.getKey());
            /*SL:156*/a3 = this.representData(a3.getValue());
            /*SL:157*/if (!(a2 instanceof ScalarNode) || ((ScalarNode)a2).getStyle() != null) {
                /*SL:158*/v7 = false;
            }
            /*SL:160*/if (!(a3 instanceof ScalarNode) || ((ScalarNode)a3).getStyle() != null) {
                /*SL:161*/v7 = false;
            }
            /*SL:163*/v5.add(new NodeTuple(a2, a3));
        }
        /*SL:165*/if (v4 == null) {
            /*SL:166*/if (this.defaultFlowStyle != DumperOptions.FlowStyle.AUTO) {
                /*SL:167*/v6.setFlowStyle(this.defaultFlowStyle.getStyleBoolean());
            }
            else {
                /*SL:169*/v6.setFlowStyle(v7);
            }
        }
        /*SL:172*/return v6;
    }
    
    public void setDefaultScalarStyle(final DumperOptions.ScalarStyle a1) {
        /*SL:176*/this.defaultScalarStyle = a1.getChar();
    }
    
    public DumperOptions.ScalarStyle getDefaultScalarStyle() {
        /*SL:180*/return DumperOptions.ScalarStyle.createStyle(this.defaultScalarStyle);
    }
    
    public void setDefaultFlowStyle(final DumperOptions.FlowStyle a1) {
        /*SL:184*/this.defaultFlowStyle = a1;
    }
    
    public DumperOptions.FlowStyle getDefaultFlowStyle() {
        /*SL:188*/return this.defaultFlowStyle;
    }
    
    public void setPropertyUtils(final PropertyUtils a1) {
        /*SL:192*/this.propertyUtils = a1;
        /*SL:193*/this.explicitPropertyUtils = true;
    }
    
    public final PropertyUtils getPropertyUtils() {
        /*SL:197*/if (this.propertyUtils == null) {
            /*SL:198*/this.propertyUtils = new PropertyUtils();
        }
        /*SL:200*/return this.propertyUtils;
    }
    
    public final boolean isExplicitPropertyUtils() {
        /*SL:204*/return this.explicitPropertyUtils;
    }
}

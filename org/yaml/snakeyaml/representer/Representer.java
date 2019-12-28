package org.yaml.snakeyaml.representer;

import java.util.TimeZone;
import java.util.Arrays;
import org.yaml.snakeyaml.nodes.SequenceNode;
import org.yaml.snakeyaml.nodes.NodeId;
import org.yaml.snakeyaml.nodes.Node;
import java.util.List;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.nodes.ScalarNode;
import org.yaml.snakeyaml.nodes.Tag;
import org.yaml.snakeyaml.nodes.NodeTuple;
import java.util.ArrayList;
import org.yaml.snakeyaml.nodes.MappingNode;
import org.yaml.snakeyaml.introspector.Property;
import java.util.Set;
import java.util.Iterator;
import java.util.Collection;
import org.yaml.snakeyaml.introspector.PropertyUtils;
import java.util.HashMap;
import java.util.Collections;
import org.yaml.snakeyaml.TypeDescription;
import java.util.Map;

public class Representer extends SafeRepresenter
{
    protected Map<Class<?>, TypeDescription> typeDefinitions;
    
    public Representer() {
        this.typeDefinitions = Collections.<Class<?>, TypeDescription>emptyMap();
        this.representers.put(null, new RepresentJavaBean());
    }
    
    public TypeDescription addTypeDescription(final TypeDescription a1) {
        /*SL:53*/if (Collections.EMPTY_MAP == this.typeDefinitions) {
            /*SL:54*/this.typeDefinitions = new HashMap<Class<?>, TypeDescription>();
        }
        /*SL:56*/if (a1.getTag() != null) {
            /*SL:57*/this.addClassTag(a1.getType(), a1.getTag());
        }
        /*SL:59*/a1.setPropertyUtils(this.getPropertyUtils());
        /*SL:60*/return this.typeDefinitions.put(a1.getType(), a1);
    }
    
    @Override
    public void setPropertyUtils(final PropertyUtils v2) {
        /*SL:65*/super.setPropertyUtils(v2);
        final Collection<TypeDescription> v3 = /*EL:66*/this.typeDefinitions.values();
        /*SL:67*/for (final TypeDescription a1 : v3) {
            /*SL:68*/a1.setPropertyUtils(v2);
        }
    }
    
    protected MappingNode representJavaBean(final Set<Property> v-10, final Object v-9) {
        final List<NodeTuple> a3 = /*EL:93*/new ArrayList<NodeTuple>(v-10.size());
        final Tag tag = /*EL:95*/this.classTags.get(v-9.getClass());
        final Tag a4 = /*EL:96*/(tag != null) ? tag : new Tag(v-9.getClass());
        final MappingNode mappingNode = /*EL:98*/new MappingNode(a4, a3, null);
        /*SL:99*/this.representedObjects.put(v-9, mappingNode);
        boolean b = /*EL:100*/true;
        /*SL:101*/for (final Property a5 : v-10) {
            final Object a1 = /*EL:102*/a5.get(v-9);
            final Tag a2 = /*EL:103*/(a1 == null) ? null : this.classTags.get(a1.getClass());
            final NodeTuple v1 = /*EL:105*/this.representJavaBeanProperty(v-9, a5, a1, a2);
            /*SL:107*/if (v1 == null) {
                /*SL:108*/continue;
            }
            /*SL:110*/if (((ScalarNode)v1.getKeyNode()).getStyle() != null) {
                /*SL:111*/b = false;
            }
            final Node v2 = /*EL:113*/v1.getValueNode();
            /*SL:114*/if (!(v2 instanceof ScalarNode) || ((ScalarNode)v2).getStyle() != null) {
                /*SL:115*/b = false;
            }
            /*SL:117*/a3.add(v1);
        }
        /*SL:119*/if (this.defaultFlowStyle != DumperOptions.FlowStyle.AUTO) {
            /*SL:120*/mappingNode.setFlowStyle(this.defaultFlowStyle.getStyleBoolean());
        }
        else {
            /*SL:122*/mappingNode.setFlowStyle(b);
        }
        /*SL:124*/return mappingNode;
    }
    
    protected NodeTuple representJavaBeanProperty(final Object a3, final Property a4, final Object v1, final Tag v2) {
        final ScalarNode v3 = /*EL:143*/(ScalarNode)this.representData(a4.getName());
        final boolean v4 = /*EL:145*/this.representedObjects.containsKey(v1);
        final Node v5 = /*EL:147*/this.representData(v1);
        /*SL:149*/if (v1 != null && !v4) {
            final NodeId a5 = /*EL:150*/v5.getNodeId();
            /*SL:151*/if (v2 == null) {
                /*SL:152*/if (a5 == NodeId.scalar) {
                    /*SL:153*/if (a4.getType() == v1.getClass() && /*EL:154*/v1 instanceof Enum) {
                        /*SL:155*/v5.setTag(Tag.STR);
                    }
                }
                else {
                    /*SL:161*/if (a5 == NodeId.mapping && a4.getType() == v1.getClass() && !(v1 instanceof Map) && /*EL:162*/!v5.getTag().equals(Tag.SET)) {
                        /*SL:163*/v5.setTag(Tag.MAP);
                    }
                    /*SL:168*/this.checkGlobalTag(a4, v5, v1);
                }
            }
        }
        /*SL:173*/return new NodeTuple(v3, v5);
    }
    
    protected void checkGlobalTag(final Property v-4, final Node v-3, final Object v-2) {
        /*SL:190*/if (v-2.getClass().isArray() && v-2.getClass().getComponentType().isPrimitive()) {
            /*SL:191*/return;
        }
        final Class<?>[] actualTypeArguments = /*EL:194*/v-4.getActualTypeArguments();
        /*SL:195*/if (actualTypeArguments != null) {
            /*SL:196*/if (v-3.getNodeId() == NodeId.sequence) {
                Class<?> a3 = /*EL:198*/actualTypeArguments[0];
                final SequenceNode v1 = /*EL:199*/(SequenceNode)v-3;
                Iterable<Object> v2 = (Iterable<Object>)Collections.EMPTY_LIST;
                /*SL:201*/if (v-2.getClass().isArray()) {
                    /*SL:202*/v2 = Arrays.<Object>asList((Object[])v-2);
                }
                else/*SL:203*/ if (v-2 instanceof Iterable) {
                    /*SL:205*/v2 = (Iterable<Object>)v-2;
                }
                final Iterator<Object> v3 = /*EL:207*/v2.iterator();
                /*SL:208*/if (v3.hasNext()) {
                    /*SL:209*/for (final Node a2 : v1.getValue()) {
                        /*SL:210*/a3 = v3.next();
                        /*SL:212*/if (a3 != null && a3.equals(a3.getClass()) && /*EL:213*/a2.getNodeId() == NodeId.mapping) {
                            /*SL:214*/a2.setTag(Tag.MAP);
                        }
                    }
                }
            }
            else/*SL:219*/ if (v-2 instanceof Set) {
                final Class<?> a3 = /*EL:220*/actualTypeArguments[0];
                final MappingNode v4 = /*EL:221*/(MappingNode)v-3;
                final Iterator<NodeTuple> v5 = /*EL:222*/v4.getValue().iterator();
                final Set<?> v6 = /*EL:223*/(Set<?>)v-2;
                /*SL:224*/for (final Object v7 : v6) {
                    final NodeTuple v8 = /*EL:225*/v5.next();
                    final Node v9 = /*EL:226*/v8.getKeyNode();
                    /*SL:227*/if (a3.equals(v7.getClass()) && /*EL:228*/v9.getNodeId() == NodeId.mapping) {
                        /*SL:229*/v9.setTag(Tag.MAP);
                    }
                }
            }
            else/*SL:233*/ if (v-2 instanceof Map) {
                final Class<?> a3 = /*EL:234*/actualTypeArguments[0];
                final Class<?> v10 = /*EL:235*/actualTypeArguments[1];
                final MappingNode v11 = /*EL:236*/(MappingNode)v-3;
                /*SL:237*/for (final NodeTuple v12 : v11.getValue()) {
                    /*SL:238*/this.resetTag(a3, v12.getKeyNode());
                    /*SL:239*/this.resetTag(v10, v12.getValueNode());
                }
            }
        }
    }
    
    private void resetTag(final Class<?> a1, final Node a2) {
        final Tag v1 = /*EL:249*/a2.getTag();
        /*SL:250*/if (v1.matches(a1)) {
            /*SL:251*/if (Enum.class.isAssignableFrom(a1)) {
                /*SL:252*/a2.setTag(Tag.STR);
            }
            else {
                /*SL:254*/a2.setTag(Tag.MAP);
            }
        }
    }
    
    protected Set<Property> getProperties(final Class<?> a1) {
        /*SL:268*/if (this.typeDefinitions.containsKey(a1)) {
            /*SL:269*/return this.typeDefinitions.get(a1).getProperties();
        }
        /*SL:271*/return this.getPropertyUtils().getProperties(a1);
    }
    
    protected class RepresentJavaBean implements Represent
    {
        @Override
        public Node representData(final Object a1) {
            /*SL:74*/return Representer.this.representJavaBean(Representer.this.getProperties(a1.getClass()), a1);
        }
    }
}

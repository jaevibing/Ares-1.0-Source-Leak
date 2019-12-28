package org.yaml.snakeyaml.serializer;

import org.yaml.snakeyaml.events.MappingEndEvent;
import org.yaml.snakeyaml.events.MappingStartEvent;
import org.yaml.snakeyaml.nodes.CollectionNode;
import org.yaml.snakeyaml.events.SequenceEndEvent;
import org.yaml.snakeyaml.events.SequenceStartEvent;
import org.yaml.snakeyaml.events.ScalarEvent;
import org.yaml.snakeyaml.events.ImplicitTuple;
import org.yaml.snakeyaml.nodes.ScalarNode;
import org.yaml.snakeyaml.events.AliasEvent;
import java.util.Iterator;
import java.util.List;
import org.yaml.snakeyaml.nodes.NodeTuple;
import org.yaml.snakeyaml.nodes.MappingNode;
import org.yaml.snakeyaml.nodes.SequenceNode;
import org.yaml.snakeyaml.nodes.AnchorNode;
import org.yaml.snakeyaml.nodes.NodeId;
import org.yaml.snakeyaml.events.DocumentEndEvent;
import org.yaml.snakeyaml.events.DocumentStartEvent;
import org.yaml.snakeyaml.events.StreamEndEvent;
import java.io.IOException;
import org.yaml.snakeyaml.events.Event;
import org.yaml.snakeyaml.error.Mark;
import org.yaml.snakeyaml.events.StreamStartEvent;
import java.util.HashMap;
import java.util.HashSet;
import org.yaml.snakeyaml.nodes.Tag;
import org.yaml.snakeyaml.nodes.Node;
import java.util.Set;
import java.util.Map;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.resolver.Resolver;
import org.yaml.snakeyaml.emitter.Emitable;

public final class Serializer
{
    private final Emitable emitter;
    private final Resolver resolver;
    private boolean explicitStart;
    private boolean explicitEnd;
    private DumperOptions.Version useVersion;
    private Map<String, String> useTags;
    private Set<Node> serializedNodes;
    private Map<Node, String> anchors;
    private AnchorGenerator anchorGenerator;
    private Boolean closed;
    private Tag explicitRoot;
    
    public Serializer(final Emitable a1, final Resolver a2, final DumperOptions a3, final Tag a4) {
        this.emitter = a1;
        this.resolver = a2;
        this.explicitStart = a3.isExplicitStart();
        this.explicitEnd = a3.isExplicitEnd();
        if (a3.getVersion() != null) {
            this.useVersion = a3.getVersion();
        }
        this.useTags = a3.getTags();
        this.serializedNodes = new HashSet<Node>();
        this.anchors = new HashMap<Node, String>();
        this.anchorGenerator = a3.getAnchorGenerator();
        this.closed = null;
        this.explicitRoot = a4;
    }
    
    public void open() throws IOException {
        /*SL:80*/if (this.closed == null) {
            /*SL:81*/this.emitter.emit(new StreamStartEvent(null, null));
            /*SL:82*/this.closed = Boolean.FALSE;
            /*SL:88*/return;
        }
        if (Boolean.TRUE.equals(this.closed)) {
            throw new SerializerException("serializer is closed");
        }
        throw new SerializerException("serializer is already opened");
    }
    
    public void close() throws IOException {
        /*SL:91*/if (this.closed == null) {
            /*SL:92*/throw new SerializerException("serializer is not opened");
        }
        /*SL:93*/if (!Boolean.TRUE.equals(this.closed)) {
            /*SL:94*/this.emitter.emit(new StreamEndEvent(null, null));
            /*SL:95*/this.closed = Boolean.TRUE;
        }
    }
    
    public void serialize(final Node a1) throws IOException {
        /*SL:100*/if (this.closed == null) {
            /*SL:101*/throw new SerializerException("serializer is not opened");
        }
        /*SL:102*/if (this.closed) {
            /*SL:103*/throw new SerializerException("serializer is closed");
        }
        /*SL:105*/this.emitter.emit(new DocumentStartEvent(null, null, this.explicitStart, this.useVersion, this.useTags));
        /*SL:107*/this.anchorNode(a1);
        /*SL:108*/if (this.explicitRoot != null) {
            /*SL:109*/a1.setTag(this.explicitRoot);
        }
        /*SL:111*/this.serializeNode(a1, null);
        /*SL:112*/this.emitter.emit(new DocumentEndEvent(null, null, this.explicitEnd));
        /*SL:113*/this.serializedNodes.clear();
        /*SL:114*/this.anchors.clear();
    }
    
    private void anchorNode(Node v-3) {
        /*SL:118*/if (v-3.getNodeId() == NodeId.anchor) {
            /*SL:119*/v-3 = ((AnchorNode)v-3).getRealNode();
        }
        /*SL:121*/if (this.anchors.containsKey(v-3)) {
            String a1 = /*EL:122*/this.anchors.get(v-3);
            /*SL:123*/if (null == a1) {
                /*SL:124*/a1 = this.anchorGenerator.nextAnchor(v-3);
                /*SL:125*/this.anchors.put(v-3, a1);
            }
        }
        else {
            /*SL:128*/this.anchors.put(v-3, null);
            /*SL:129*/switch (v-3.getNodeId()) {
                case sequence: {
                    final SequenceNode sequenceNode = /*EL:131*/(SequenceNode)v-3;
                    final List<Node> value = /*EL:132*/sequenceNode.getValue();
                    /*SL:133*/for (final Node v1 : value) {
                        /*SL:134*/this.anchorNode(v1);
                    }
                    /*SL:136*/break;
                }
                case mapping: {
                    final MappingNode v2 = /*EL:138*/(MappingNode)v-3;
                    final List<NodeTuple> v3 = /*EL:139*/v2.getValue();
                    /*SL:140*/for (final NodeTuple v4 : v3) {
                        final Node v5 = /*EL:141*/v4.getKeyNode();
                        final Node v6 = /*EL:142*/v4.getValueNode();
                        /*SL:143*/this.anchorNode(v5);
                        /*SL:144*/this.anchorNode(v6);
                    }
                    break;
                }
            }
        }
    }
    
    private void serializeNode(Node v-4, final Node v-3) throws IOException {
        /*SL:152*/if (v-4.getNodeId() == NodeId.anchor) {
            /*SL:153*/v-4 = ((AnchorNode)v-4).getRealNode();
        }
        final String s = /*EL:155*/this.anchors.get(v-4);
        /*SL:156*/if (this.serializedNodes.contains(v-4)) {
            /*SL:157*/this.emitter.emit(new AliasEvent(s, null, null));
        }
        else {
            /*SL:159*/this.serializedNodes.add(v-4);
            /*SL:160*/switch (v-4.getNodeId()) {
                case scalar: {
                    final ScalarNode a1 = /*EL:162*/(ScalarNode)v-4;
                    final Tag a2 = /*EL:163*/this.resolver.resolve(NodeId.scalar, a1.getValue(), true);
                    final Tag v1 = /*EL:164*/this.resolver.resolve(NodeId.scalar, a1.getValue(), false);
                    final ImplicitTuple v2 = /*EL:166*/new ImplicitTuple(v-4.getTag().equals(a2), v-4.getTag().equals(v1));
                    final ScalarEvent v3 = /*EL:168*/new ScalarEvent(s, v-4.getTag().getValue(), v2, a1.getValue(), null, null, a1.getStyle());
                    /*SL:169*/this.emitter.emit(v3);
                    /*SL:170*/break;
                }
                case sequence: {
                    final SequenceNode v4 = /*EL:172*/(SequenceNode)v-4;
                    final boolean v5 = /*EL:173*/v-4.getTag().equals(this.resolver.resolve(NodeId.sequence, null, true));
                    /*SL:175*/this.emitter.emit(/*EL:176*/new SequenceStartEvent(s, v-4.getTag().getValue(), v5, null, null, v4.getFlowStyle()));
                    final List<Node> v6 = /*EL:177*/v4.getValue();
                    /*SL:178*/for (final Node v7 : v6) {
                        /*SL:179*/this.serializeNode(v7, v-4);
                    }
                    /*SL:181*/this.emitter.emit(new SequenceEndEvent(null, null));
                    /*SL:182*/break;
                }
                default: {
                    final Tag v8 = /*EL:184*/this.resolver.resolve(NodeId.mapping, null, true);
                    final boolean v9 = /*EL:185*/v-4.getTag().equals(v8);
                    /*SL:186*/this.emitter.emit(/*EL:187*/new MappingStartEvent(s, v-4.getTag().getValue(), v9, null, null, ((CollectionNode)v-4).getFlowStyle()));
                    final MappingNode v10 = /*EL:188*/(MappingNode)v-4;
                    final List<NodeTuple> v11 = /*EL:189*/v10.getValue();
                    /*SL:190*/for (final NodeTuple v12 : v11) {
                        final Node v13 = /*EL:191*/v12.getKeyNode();
                        final Node v14 = /*EL:192*/v12.getValueNode();
                        /*SL:193*/this.serializeNode(v13, v10);
                        /*SL:194*/this.serializeNode(v14, v10);
                    }
                    /*SL:196*/this.emitter.emit(new MappingEndEvent(null, null));
                    break;
                }
            }
        }
    }
}

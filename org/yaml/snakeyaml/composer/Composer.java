package org.yaml.snakeyaml.composer;

import org.yaml.snakeyaml.nodes.MappingNode;
import org.yaml.snakeyaml.nodes.NodeTuple;
import org.yaml.snakeyaml.events.MappingStartEvent;
import java.util.List;
import org.yaml.snakeyaml.nodes.SequenceNode;
import java.util.ArrayList;
import org.yaml.snakeyaml.events.SequenceStartEvent;
import org.yaml.snakeyaml.nodes.ScalarNode;
import org.yaml.snakeyaml.nodes.Tag;
import org.yaml.snakeyaml.nodes.NodeId;
import org.yaml.snakeyaml.events.ScalarEvent;
import org.yaml.snakeyaml.events.NodeEvent;
import org.yaml.snakeyaml.error.Mark;
import org.yaml.snakeyaml.events.AliasEvent;
import org.yaml.snakeyaml.events.Event;
import java.util.HashSet;
import java.util.HashMap;
import java.util.Set;
import org.yaml.snakeyaml.nodes.Node;
import java.util.Map;
import org.yaml.snakeyaml.resolver.Resolver;
import org.yaml.snakeyaml.parser.Parser;

public class Composer
{
    protected final Parser parser;
    private final Resolver resolver;
    private final Map<String, Node> anchors;
    private final Set<Node> recursiveNodes;
    
    public Composer(final Parser a1, final Resolver a2) {
        this.parser = a1;
        this.resolver = a2;
        this.anchors = new HashMap<String, Node>();
        this.recursiveNodes = new HashSet<Node>();
    }
    
    public boolean checkNode() {
        /*SL:68*/if (this.parser.checkEvent(Event.ID.StreamStart)) {
            /*SL:69*/this.parser.getEvent();
        }
        /*SL:72*/return !this.parser.checkEvent(Event.ID.StreamEnd);
    }
    
    public Node getNode() {
        /*SL:83*/if (!this.parser.checkEvent(Event.ID.StreamEnd)) {
            /*SL:84*/return this.composeDocument();
        }
        /*SL:86*/return null;
    }
    
    public Node getSingleNode() {
        /*SL:101*/this.parser.getEvent();
        Node v0 = /*EL:103*/null;
        /*SL:104*/if (!this.parser.checkEvent(Event.ID.StreamEnd)) {
            /*SL:105*/v0 = this.composeDocument();
        }
        /*SL:108*/if (!this.parser.checkEvent(Event.ID.StreamEnd)) {
            final Event v = /*EL:109*/this.parser.getEvent();
            /*SL:111*/throw new ComposerException("expected a single document in the stream", v0.getStartMark(), "but found another document", v.getStartMark());
        }
        /*SL:114*/this.parser.getEvent();
        /*SL:115*/return v0;
    }
    
    private Node composeDocument() {
        /*SL:120*/this.parser.getEvent();
        final Node v1 = /*EL:122*/this.composeNode(null);
        /*SL:124*/this.parser.getEvent();
        /*SL:125*/this.anchors.clear();
        /*SL:126*/this.recursiveNodes.clear();
        /*SL:127*/return v1;
    }
    
    private Node composeNode(final Node v-2) {
        /*SL:131*/this.recursiveNodes.add(v-2);
        Node node = /*EL:132*/null;
        /*SL:133*/if (this.parser.checkEvent(Event.ID.Alias)) {
            final AliasEvent a1 = /*EL:134*/(AliasEvent)this.parser.getEvent();
            final String v1 = /*EL:135*/a1.getAnchor();
            /*SL:136*/if (!this.anchors.containsKey(v1)) {
                /*SL:137*/throw new ComposerException(null, null, "found undefined alias " + v1, a1.getStartMark());
            }
            /*SL:140*/node = this.anchors.get(v1);
            /*SL:141*/if (this.recursiveNodes.remove(node)) {
                /*SL:142*/node.setTwoStepsConstruction(true);
            }
        }
        else {
            final NodeEvent v2 = /*EL:145*/(NodeEvent)this.parser.peekEvent();
            String v1 = /*EL:146*/null;
            /*SL:147*/v1 = v2.getAnchor();
            /*SL:149*/if (this.parser.checkEvent(Event.ID.Scalar)) {
                /*SL:150*/node = this.composeScalarNode(v1);
            }
            else/*SL:151*/ if (this.parser.checkEvent(Event.ID.SequenceStart)) {
                /*SL:152*/node = this.composeSequenceNode(v1);
            }
            else {
                /*SL:154*/node = this.composeMappingNode(v1);
            }
        }
        /*SL:157*/this.recursiveNodes.remove(v-2);
        /*SL:158*/return node;
    }
    
    protected Node composeScalarNode(final String v2) {
        final ScalarEvent v3 = /*EL:162*/(ScalarEvent)this.parser.getEvent();
        final String v4 = /*EL:163*/v3.getTag();
        boolean v5 = /*EL:164*/false;
        final Tag v6;
        /*SL:166*/if (v4 == null || v4.equals("!")) {
            final Tag a1 = /*EL:167*/this.resolver.resolve(NodeId.scalar, v3.getValue(), v3.getImplicit().canOmitTagInPlainScalar());
            /*SL:169*/v5 = true;
        }
        else {
            /*SL:171*/v6 = new Tag(v4);
        }
        final Node v7 = /*EL:174*/new ScalarNode(v6, v5, v3.getValue(), v3.getStartMark(), v3.getEndMark(), v3.getStyle());
        /*SL:175*/if (v2 != null) {
            /*SL:176*/v7.setAnchor(v2);
            /*SL:177*/this.anchors.put(v2, v7);
        }
        /*SL:179*/return v7;
    }
    
    protected Node composeSequenceNode(final String v2) {
        final SequenceStartEvent v3 = /*EL:183*/(SequenceStartEvent)this.parser.getEvent();
        final String v4 = /*EL:184*/v3.getTag();
        boolean v5 = /*EL:186*/false;
        final Tag v6;
        /*SL:187*/if (v4 == null || v4.equals("!")) {
            final Tag a1 = /*EL:188*/this.resolver.resolve(NodeId.sequence, null, v3.getImplicit());
            /*SL:189*/v5 = true;
        }
        else {
            /*SL:191*/v6 = new Tag(v4);
        }
        final ArrayList<Node> v7 = /*EL:193*/new ArrayList<Node>();
        final SequenceNode v8 = /*EL:195*/new SequenceNode(v6, v5, v7, v3.getStartMark(), null, v3.getFlowStyle());
        /*SL:196*/if (v2 != null) {
            /*SL:197*/v8.setAnchor(v2);
            /*SL:198*/this.anchors.put(v2, v8);
        }
        /*SL:200*/while (!this.parser.checkEvent(Event.ID.SequenceEnd)) {
            /*SL:201*/v7.add(this.composeNode(v8));
        }
        final Event v9 = /*EL:203*/this.parser.getEvent();
        /*SL:204*/v8.setEndMark(v9.getEndMark());
        /*SL:205*/return v8;
    }
    
    protected Node composeMappingNode(final String v2) {
        final MappingStartEvent v3 = /*EL:209*/(MappingStartEvent)this.parser.getEvent();
        final String v4 = /*EL:210*/v3.getTag();
        boolean v5 = /*EL:212*/false;
        final Tag v6;
        /*SL:213*/if (v4 == null || v4.equals("!")) {
            final Tag a1 = /*EL:214*/this.resolver.resolve(NodeId.mapping, null, v3.getImplicit());
            /*SL:215*/v5 = true;
        }
        else {
            /*SL:217*/v6 = new Tag(v4);
        }
        final List<NodeTuple> v7 = /*EL:220*/new ArrayList<NodeTuple>();
        final MappingNode v8 = /*EL:222*/new MappingNode(v6, v5, v7, v3.getStartMark(), null, v3.getFlowStyle());
        /*SL:223*/if (v2 != null) {
            /*SL:224*/v8.setAnchor(v2);
            /*SL:225*/this.anchors.put(v2, v8);
        }
        /*SL:227*/while (!this.parser.checkEvent(Event.ID.MappingEnd)) {
            /*SL:228*/this.composeMappingChildren(v7, v8);
        }
        final Event v9 = /*EL:230*/this.parser.getEvent();
        /*SL:231*/v8.setEndMark(v9.getEndMark());
        /*SL:232*/return v8;
    }
    
    protected void composeMappingChildren(final List<NodeTuple> a1, final MappingNode a2) {
        final Node v1 = /*EL:236*/this.composeKeyNode(a2);
        /*SL:237*/if (v1.getTag().equals(Tag.MERGE)) {
            /*SL:238*/a2.setMerged(true);
        }
        final Node v2 = /*EL:240*/this.composeValueNode(a2);
        /*SL:241*/a1.add(new NodeTuple(v1, v2));
    }
    
    protected Node composeKeyNode(final MappingNode a1) {
        /*SL:245*/return this.composeNode(a1);
    }
    
    protected Node composeValueNode(final MappingNode a1) {
        /*SL:249*/return this.composeNode(a1);
    }
}

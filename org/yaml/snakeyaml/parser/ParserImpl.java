package org.yaml.snakeyaml.parser;

import org.yaml.snakeyaml.events.MappingEndEvent;
import org.yaml.snakeyaml.events.SequenceEndEvent;
import org.yaml.snakeyaml.tokens.BlockEntryToken;
import org.yaml.snakeyaml.events.DocumentEndEvent;
import org.yaml.snakeyaml.error.YAMLException;
import org.yaml.snakeyaml.events.StreamEndEvent;
import org.yaml.snakeyaml.tokens.StreamEndToken;
import org.yaml.snakeyaml.events.DocumentStartEvent;
import org.yaml.snakeyaml.events.StreamStartEvent;
import org.yaml.snakeyaml.tokens.StreamStartToken;
import org.yaml.snakeyaml.tokens.TagTuple;
import org.yaml.snakeyaml.events.MappingStartEvent;
import org.yaml.snakeyaml.events.ScalarEvent;
import org.yaml.snakeyaml.events.ImplicitTuple;
import org.yaml.snakeyaml.tokens.ScalarToken;
import org.yaml.snakeyaml.events.SequenceStartEvent;
import org.yaml.snakeyaml.tokens.TagToken;
import org.yaml.snakeyaml.tokens.AnchorToken;
import org.yaml.snakeyaml.events.AliasEvent;
import org.yaml.snakeyaml.tokens.AliasToken;
import java.util.Iterator;
import java.util.List;
import org.yaml.snakeyaml.tokens.DirectiveToken;
import org.yaml.snakeyaml.tokens.Token;
import org.yaml.snakeyaml.DumperOptions;
import java.util.HashMap;
import org.yaml.snakeyaml.scanner.ScannerImpl;
import org.yaml.snakeyaml.reader.StreamReader;
import org.yaml.snakeyaml.error.Mark;
import org.yaml.snakeyaml.util.ArrayStack;
import org.yaml.snakeyaml.events.Event;
import org.yaml.snakeyaml.scanner.Scanner;
import java.util.Map;

public class ParserImpl implements Parser
{
    private static final Map<String, String> DEFAULT_TAGS;
    protected final Scanner scanner;
    private Event currentEvent;
    private final ArrayStack<Production> states;
    private final ArrayStack<Mark> marks;
    private Production state;
    private VersionTagsTuple directives;
    
    public ParserImpl(final StreamReader a1) {
        this(new ScannerImpl(a1));
    }
    
    public ParserImpl(final Scanner a1) {
        this.scanner = a1;
        this.currentEvent = null;
        this.directives = new VersionTagsTuple(null, new HashMap<String, String>(ParserImpl.DEFAULT_TAGS));
        this.states = new ArrayStack<Production>(100);
        this.marks = new ArrayStack<Mark>(10);
        this.state = new ParseStreamStart();
    }
    
    @Override
    public boolean checkEvent(final Event.ID a1) {
        /*SL:147*/this.peekEvent();
        /*SL:148*/return this.currentEvent != null && this.currentEvent.is(a1);
    }
    
    @Override
    public Event peekEvent() {
        /*SL:155*/if (this.currentEvent == null && /*EL:156*/this.state != null) {
            /*SL:157*/this.currentEvent = this.state.produce();
        }
        /*SL:160*/return this.currentEvent;
    }
    
    @Override
    public Event getEvent() {
        /*SL:167*/this.peekEvent();
        final Event v1 = /*EL:168*/this.currentEvent;
        /*SL:169*/this.currentEvent = null;
        /*SL:170*/return v1;
    }
    
    private VersionTagsTuple processDirectives() {
        DumperOptions.Version a1 = /*EL:285*/null;
        final HashMap<String, String> a2 = /*EL:286*/new HashMap<String, String>();
        /*SL:287*/while (this.scanner.checkToken(Token.ID.Directive)) {
            final DirectiveToken v0 = /*EL:289*/(DirectiveToken)this.scanner.getToken();
            /*SL:290*/if (v0.getName().equals("YAML")) {
                /*SL:291*/if (a1 != null) {
                    /*SL:293*/throw new ParserException(null, null, "found duplicate YAML directive", v0.getStartMark());
                }
                final List<Integer> v = /*EL:295*/v0.getValue();
                final Integer v2 = /*EL:296*/v.get(0);
                /*SL:297*/if (v2 != 1) {
                    /*SL:300*/throw new ParserException(null, null, "found incompatible YAML document (version 1.* is required)", v0.getStartMark());
                }
                final Integer v3 = /*EL:302*/v.get(1);
                /*SL:303*/switch (v3) {
                    case 0: {
                        /*SL:305*/a1 = DumperOptions.Version.V1_0;
                        /*SL:306*/continue;
                    }
                    default: {
                        /*SL:309*/a1 = DumperOptions.Version.V1_1;
                        continue;
                    }
                }
            }
            else {
                /*SL:312*/if (!v0.getName().equals("TAG")) {
                    continue;
                }
                final List<String> v4 = /*EL:313*/v0.getValue();
                final String v5 = /*EL:314*/v4.get(0);
                final String v6 = /*EL:315*/v4.get(1);
                /*SL:316*/if (a2.containsKey(v5)) {
                    /*SL:317*/throw new ParserException(null, null, "duplicate tag handle " + v5, v0.getStartMark());
                }
                /*SL:320*/a2.put(v5, v6);
            }
        }
        /*SL:323*/if (a1 != null || !a2.isEmpty()) {
            /*SL:325*/for (final String v7 : ParserImpl.DEFAULT_TAGS.keySet()) {
                /*SL:327*/if (!a2.containsKey(v7)) {
                    /*SL:328*/a2.put(v7, ParserImpl.DEFAULT_TAGS.get(v7));
                }
            }
            /*SL:331*/this.directives = new VersionTagsTuple(a1, a2);
        }
        /*SL:333*/return this.directives;
    }
    
    private Event parseFlowNode() {
        /*SL:363*/return this.parseNode(false, false);
    }
    
    private Event parseBlockNodeOrIndentlessSequence() {
        /*SL:367*/return this.parseNode(true, true);
    }
    
    private Event parseNode(final boolean v-8, final boolean v-7) {
        Mark startMark = /*EL:372*/null;
        Mark a3 = /*EL:373*/null;
        Mark a4 = /*EL:374*/null;
        Event event = null;
        /*SL:375*/if (this.scanner.checkToken(Token.ID.Alias)) {
            final AliasToken a1 = /*EL:376*/(AliasToken)this.scanner.getToken();
            final Event a2 = /*EL:377*/new AliasEvent(a1.getValue(), a1.getStartMark(), a1.getEndMark());
            /*SL:378*/this.state = this.states.pop();
        }
        else {
            String a5 = /*EL:380*/null;
            TagTuple tagTuple = /*EL:381*/null;
            /*SL:382*/if (this.scanner.checkToken(Token.ID.Anchor)) {
                final AnchorToken v0 = /*EL:383*/(AnchorToken)this.scanner.getToken();
                /*SL:384*/startMark = v0.getStartMark();
                /*SL:385*/a3 = v0.getEndMark();
                /*SL:386*/a5 = v0.getValue();
                /*SL:387*/if (this.scanner.checkToken(Token.ID.Tag)) {
                    final TagToken v = /*EL:388*/(TagToken)this.scanner.getToken();
                    /*SL:389*/a4 = v.getStartMark();
                    /*SL:390*/a3 = v.getEndMark();
                    /*SL:391*/tagTuple = v.getValue();
                }
            }
            else/*SL:393*/ if (this.scanner.checkToken(Token.ID.Tag)) {
                final TagToken v2 = /*EL:394*/(TagToken)this.scanner.getToken();
                /*SL:395*/startMark = /*EL:396*/(a4 = v2.getStartMark());
                /*SL:397*/a3 = v2.getEndMark();
                /*SL:398*/tagTuple = v2.getValue();
                /*SL:399*/if (this.scanner.checkToken(Token.ID.Anchor)) {
                    final AnchorToken v3 = /*EL:400*/(AnchorToken)this.scanner.getToken();
                    /*SL:401*/a3 = v3.getEndMark();
                    /*SL:402*/a5 = v3.getValue();
                }
            }
            String v4 = /*EL:405*/null;
            /*SL:406*/if (tagTuple != null) {
                final String v5 = /*EL:407*/tagTuple.getHandle();
                final String v6 = /*EL:408*/tagTuple.getSuffix();
                /*SL:409*/if (v5 != null) {
                    /*SL:410*/if (!this.directives.getTags().containsKey(v5)) {
                        /*SL:411*/throw new ParserException("while parsing a node", startMark, "found undefined tag handle " + v5, a4);
                    }
                    /*SL:414*/v4 = this.directives.getTags().get(v5) + v6;
                }
                else {
                    /*SL:416*/v4 = v6;
                }
            }
            /*SL:419*/if (startMark == null) {
                /*SL:420*/startMark = /*EL:421*/(a3 = this.scanner.peekToken().getStartMark());
            }
            /*SL:423*/event = null;
            final boolean v7 = /*EL:424*/v4 == null || v4.equals("!");
            /*SL:425*/if (v-7 && this.scanner.checkToken(Token.ID.BlockEntry)) {
                /*SL:426*/a3 = this.scanner.peekToken().getEndMark();
                /*SL:427*/event = new SequenceStartEvent(a5, v4, v7, startMark, a3, Boolean.FALSE);
                /*SL:429*/this.state = new ParseIndentlessSequenceEntry();
            }
            else/*SL:431*/ if (this.scanner.checkToken(Token.ID.Scalar)) {
                final ScalarToken v8 = /*EL:432*/(ScalarToken)this.scanner.getToken();
                /*SL:433*/a3 = v8.getEndMark();
                ImplicitTuple v9;
                /*SL:435*/if ((v8.getPlain() && v4 == null) || "!".equals(v4)) {
                    /*SL:436*/v9 = new ImplicitTuple(true, false);
                }
                else/*SL:437*/ if (v4 == null) {
                    /*SL:438*/v9 = new ImplicitTuple(false, true);
                }
                else {
                    /*SL:440*/v9 = new ImplicitTuple(false, false);
                }
                /*SL:443*/event = new ScalarEvent(a5, v4, v9, v8.getValue(), startMark, a3, v8.getStyle());
                /*SL:444*/this.state = this.states.pop();
            }
            else/*SL:445*/ if (this.scanner.checkToken(Token.ID.FlowSequenceStart)) {
                /*SL:446*/a3 = this.scanner.peekToken().getEndMark();
                /*SL:447*/event = new SequenceStartEvent(a5, v4, v7, startMark, a3, Boolean.TRUE);
                /*SL:449*/this.state = new ParseFlowSequenceFirstEntry();
            }
            else/*SL:450*/ if (this.scanner.checkToken(Token.ID.FlowMappingStart)) {
                /*SL:451*/a3 = this.scanner.peekToken().getEndMark();
                /*SL:452*/event = new MappingStartEvent(a5, v4, v7, startMark, a3, Boolean.TRUE);
                /*SL:454*/this.state = new ParseFlowMappingFirstKey();
            }
            else/*SL:455*/ if (v-8 && this.scanner.checkToken(Token.ID.BlockSequenceStart)) {
                /*SL:456*/a3 = this.scanner.peekToken().getStartMark();
                /*SL:457*/event = new SequenceStartEvent(a5, v4, v7, startMark, a3, Boolean.FALSE);
                /*SL:459*/this.state = new ParseBlockSequenceFirstEntry();
            }
            else/*SL:460*/ if (v-8 && this.scanner.checkToken(Token.ID.BlockMappingStart)) {
                /*SL:461*/a3 = this.scanner.peekToken().getStartMark();
                /*SL:462*/event = new MappingStartEvent(a5, v4, v7, startMark, a3, Boolean.FALSE);
                /*SL:464*/this.state = new ParseBlockMappingFirstKey();
            }
            else {
                /*SL:465*/if (a5 == null && v4 == null) {
                    String v6;
                    /*SL:473*/if (v-8) {
                        /*SL:474*/v6 = "block";
                    }
                    else {
                        /*SL:476*/v6 = "flow";
                    }
                    final Token v10 = /*EL:478*/this.scanner.peekToken();
                    /*SL:479*/throw new ParserException("while parsing a " + v6 + " node", startMark, "expected the node content, but found " + v10.getTokenId(), /*EL:480*/v10.getStartMark());
                }
                event = new ScalarEvent(a5, v4, new ImplicitTuple(v7, false), "", startMark, a3, '\0');
                this.state = this.states.pop();
            }
        }
        /*SL:485*/return event;
    }
    
    private Event processEmptyScalar(final Mark a1) {
        /*SL:792*/return new ScalarEvent(null, null, new ImplicitTuple(true, false), "", a1, a1, '\0');
    }
    
    static {
        (DEFAULT_TAGS = new HashMap<String, String>()).put("!", "!");
        ParserImpl.DEFAULT_TAGS.put("!!", "tag:yaml.org,2002:");
    }
    
    private class ParseStreamStart implements Production
    {
        @Override
        public Event produce() {
            final StreamStartToken v1 = /*EL:183*/(StreamStartToken)ParserImpl.this.scanner.getToken();
            final Event v2 = /*EL:184*/new StreamStartEvent(v1.getStartMark(), v1.getEndMark());
            /*SL:186*/ParserImpl.this.state = new ParseImplicitDocumentStart();
            /*SL:187*/return v2;
        }
    }
    
    private class ParseImplicitDocumentStart implements Production
    {
        @Override
        public Event produce() {
            /*SL:194*/if (!ParserImpl.this.scanner.checkToken(Token.ID.Directive, Token.ID.DocumentStart, Token.ID.StreamEnd)) {
                /*SL:195*/ParserImpl.this.directives = new VersionTagsTuple(null, ParserImpl.DEFAULT_TAGS);
                final Token v1 = /*EL:196*/ParserImpl.this.scanner.peekToken();
                final Mark v3;
                final Mark v2 = /*EL:198*/v3 = v1.getStartMark();
                final Event v4 = /*EL:199*/new DocumentStartEvent(v2, v3, false, null, null);
                /*SL:201*/ParserImpl.this.states.push(new ParseDocumentEnd());
                /*SL:202*/ParserImpl.this.state = new ParseBlockNode();
                /*SL:203*/return v4;
            }
            final Production v5 = /*EL:205*/new ParseDocumentStart();
            /*SL:206*/return v5.produce();
        }
    }
    
    private class ParseDocumentStart implements Production
    {
        @Override
        public Event produce() {
            /*SL:214*/while (ParserImpl.this.scanner.checkToken(Token.ID.DocumentEnd)) {
                /*SL:215*/ParserImpl.this.scanner.getToken();
            }
            Event v5;
            /*SL:219*/if (!ParserImpl.this.scanner.checkToken(Token.ID.StreamEnd)) {
                Token v1 = /*EL:220*/ParserImpl.this.scanner.peekToken();
                final Mark v2 = /*EL:221*/v1.getStartMark();
                final VersionTagsTuple v3 = /*EL:222*/ParserImpl.this.processDirectives();
                /*SL:223*/if (!ParserImpl.this.scanner.checkToken(Token.ID.DocumentStart)) {
                    /*SL:224*/throw new ParserException(null, null, "expected '<document start>', but found " + ParserImpl.this.scanner.peekToken().getTokenId(), /*EL:225*/ParserImpl.this.scanner.peekToken().getStartMark());
                }
                /*SL:227*/v1 = ParserImpl.this.scanner.getToken();
                final Mark v4 = /*EL:228*/v1.getEndMark();
                /*SL:230*/v5 = new DocumentStartEvent(v2, v4, true, v3.getVersion(), v3.getTags());
                /*SL:231*/ParserImpl.this.states.push(new ParseDocumentEnd());
                /*SL:232*/ParserImpl.this.state = new ParseDocumentContent();
            }
            else {
                final StreamEndToken v6 = /*EL:235*/(StreamEndToken)ParserImpl.this.scanner.getToken();
                /*SL:236*/v5 = new StreamEndEvent(v6.getStartMark(), v6.getEndMark());
                /*SL:237*/if (!ParserImpl.this.states.isEmpty()) {
                    /*SL:238*/throw new YAMLException("Unexpected end of stream. States left: " + ParserImpl.this.states);
                }
                /*SL:240*/if (!ParserImpl.this.marks.isEmpty()) {
                    /*SL:241*/throw new YAMLException("Unexpected end of stream. Marks left: " + ParserImpl.this.marks);
                }
                /*SL:243*/ParserImpl.this.state = null;
            }
            /*SL:245*/return v5;
        }
    }
    
    private class ParseDocumentEnd implements Production
    {
        @Override
        public Event produce() {
            Token v1 = /*EL:252*/ParserImpl.this.scanner.peekToken();
            Mark v3;
            final Mark v2 = /*EL:254*/v3 = v1.getStartMark();
            boolean v4 = /*EL:255*/false;
            /*SL:256*/if (ParserImpl.this.scanner.checkToken(Token.ID.DocumentEnd)) {
                /*SL:257*/v1 = ParserImpl.this.scanner.getToken();
                /*SL:258*/v3 = v1.getEndMark();
                /*SL:259*/v4 = true;
            }
            final Event v5 = /*EL:261*/new DocumentEndEvent(v2, v3, v4);
            /*SL:263*/ParserImpl.this.state = new ParseDocumentStart();
            /*SL:264*/return v5;
        }
    }
    
    private class ParseDocumentContent implements Production
    {
        @Override
        public Event produce() {
            /*SL:271*/if (ParserImpl.this.scanner.checkToken(Token.ID.Directive, Token.ID.DocumentStart, Token.ID.DocumentEnd, Token.ID.StreamEnd)) {
                final Event v1 = /*EL:273*/ParserImpl.this.processEmptyScalar(ParserImpl.this.scanner.peekToken().getStartMark());
                /*SL:274*/ParserImpl.this.state = ParserImpl.this.states.pop();
                /*SL:275*/return v1;
            }
            final Production v2 = /*EL:277*/new ParseBlockNode();
            /*SL:278*/return v2.produce();
        }
    }
    
    private class ParseBlockNode implements Production
    {
        @Override
        public Event produce() {
            /*SL:358*/return ParserImpl.this.parseNode(true, false);
        }
    }
    
    private class ParseBlockSequenceFirstEntry implements Production
    {
        @Override
        public Event produce() {
            final Token v1 = /*EL:493*/ParserImpl.this.scanner.getToken();
            /*SL:494*/ParserImpl.this.marks.push(v1.getStartMark());
            /*SL:495*/return new ParseBlockSequenceEntry().produce();
        }
    }
    
    private class ParseBlockSequenceEntry implements Production
    {
        @Override
        public Event produce() {
            /*SL:501*/if (ParserImpl.this.scanner.checkToken(Token.ID.BlockEntry)) {
                final BlockEntryToken v1 = /*EL:502*/(BlockEntryToken)ParserImpl.this.scanner.getToken();
                /*SL:503*/if (!ParserImpl.this.scanner.checkToken(Token.ID.BlockEntry, Token.ID.BlockEnd)) {
                    /*SL:504*/ParserImpl.this.states.push(new ParseBlockSequenceEntry());
                    /*SL:505*/return new ParseBlockNode().produce();
                }
                /*SL:507*/ParserImpl.this.state = new ParseBlockSequenceEntry();
                /*SL:508*/return ParserImpl.this.processEmptyScalar(v1.getEndMark());
            }
            else {
                /*SL:511*/if (!ParserImpl.this.scanner.checkToken(Token.ID.BlockEnd)) {
                    final Token v2 = /*EL:512*/ParserImpl.this.scanner.peekToken();
                    /*SL:513*/throw new ParserException("while parsing a block collection", ParserImpl.this.marks.pop(), "expected <block end>, but found " + v2.getTokenId(), /*EL:514*/v2.getStartMark());
                }
                final Token v2 = /*EL:517*/ParserImpl.this.scanner.getToken();
                final Event v3 = /*EL:518*/new SequenceEndEvent(v2.getStartMark(), v2.getEndMark());
                /*SL:519*/ParserImpl.this.state = ParserImpl.this.states.pop();
                /*SL:520*/ParserImpl.this.marks.pop();
                /*SL:521*/return v3;
            }
        }
    }
    
    private class ParseIndentlessSequenceEntry implements Production
    {
        @Override
        public Event produce() {
            /*SL:529*/if (!ParserImpl.this.scanner.checkToken(Token.ID.BlockEntry)) {
                final Token v1 = /*EL:540*/ParserImpl.this.scanner.peekToken();
                final Event v2 = /*EL:541*/new SequenceEndEvent(v1.getStartMark(), v1.getEndMark());
                /*SL:542*/ParserImpl.this.state = ParserImpl.this.states.pop();
                /*SL:543*/return v2;
            }
            final Token v1 = ParserImpl.this.scanner.getToken();
            if (!ParserImpl.this.scanner.checkToken(Token.ID.BlockEntry, Token.ID.Key, Token.ID.Value, Token.ID.BlockEnd)) {
                ParserImpl.this.states.push(new ParseIndentlessSequenceEntry());
                return new ParseBlockNode().produce();
            }
            ParserImpl.this.state = new ParseIndentlessSequenceEntry();
            return ParserImpl.this.processEmptyScalar(v1.getEndMark());
        }
    }
    
    private class ParseBlockMappingFirstKey implements Production
    {
        @Override
        public Event produce() {
            final Token v1 = /*EL:549*/ParserImpl.this.scanner.getToken();
            /*SL:550*/ParserImpl.this.marks.push(v1.getStartMark());
            /*SL:551*/return new ParseBlockMappingKey().produce();
        }
    }
    
    private class ParseBlockMappingKey implements Production
    {
        @Override
        public Event produce() {
            /*SL:557*/if (ParserImpl.this.scanner.checkToken(Token.ID.Key)) {
                final Token v1 = /*EL:558*/ParserImpl.this.scanner.getToken();
                /*SL:559*/if (!ParserImpl.this.scanner.checkToken(Token.ID.Key, Token.ID.Value, Token.ID.BlockEnd)) {
                    /*SL:560*/ParserImpl.this.states.push(new ParseBlockMappingValue());
                    /*SL:561*/return ParserImpl.this.parseBlockNodeOrIndentlessSequence();
                }
                /*SL:563*/ParserImpl.this.state = new ParseBlockMappingValue();
                /*SL:564*/return ParserImpl.this.processEmptyScalar(v1.getEndMark());
            }
            else {
                /*SL:567*/if (!ParserImpl.this.scanner.checkToken(Token.ID.BlockEnd)) {
                    final Token v1 = /*EL:568*/ParserImpl.this.scanner.peekToken();
                    /*SL:569*/throw new ParserException("while parsing a block mapping", ParserImpl.this.marks.pop(), "expected <block end>, but found " + v1.getTokenId(), /*EL:570*/v1.getStartMark());
                }
                final Token v1 = /*EL:573*/ParserImpl.this.scanner.getToken();
                final Event v2 = /*EL:574*/new MappingEndEvent(v1.getStartMark(), v1.getEndMark());
                /*SL:575*/ParserImpl.this.state = ParserImpl.this.states.pop();
                /*SL:576*/ParserImpl.this.marks.pop();
                /*SL:577*/return v2;
            }
        }
    }
    
    private class ParseBlockMappingValue implements Production
    {
        @Override
        public Event produce() {
            /*SL:583*/if (!ParserImpl.this.scanner.checkToken(Token.ID.Value)) {
                /*SL:593*/ParserImpl.this.state = new ParseBlockMappingKey();
                final Token v1 = /*EL:594*/ParserImpl.this.scanner.peekToken();
                /*SL:595*/return ParserImpl.this.processEmptyScalar(v1.getStartMark());
            }
            final Token v1 = ParserImpl.this.scanner.getToken();
            if (!ParserImpl.this.scanner.checkToken(Token.ID.Key, Token.ID.Value, Token.ID.BlockEnd)) {
                ParserImpl.this.states.push(new ParseBlockMappingKey());
                return ParserImpl.this.parseBlockNodeOrIndentlessSequence();
            }
            ParserImpl.this.state = new ParseBlockMappingKey();
            return ParserImpl.this.processEmptyScalar(v1.getEndMark());
        }
    }
    
    private class ParseFlowSequenceFirstEntry implements Production
    {
        @Override
        public Event produce() {
            final Token v1 = /*EL:614*/ParserImpl.this.scanner.getToken();
            /*SL:615*/ParserImpl.this.marks.push(v1.getStartMark());
            /*SL:616*/return new ParseFlowSequenceEntry(true).produce();
        }
    }
    
    private class ParseFlowSequenceEntry implements Production
    {
        private boolean first;
        
        public ParseFlowSequenceEntry(final boolean a1) {
            this.first = false;
            this.first = a1;
        }
        
        @Override
        public Event produce() {
            /*SL:628*/if (!ParserImpl.this.scanner.checkToken(Token.ID.FlowSequenceEnd)) {
                /*SL:629*/if (!this.first) {
                    /*SL:630*/if (!ParserImpl.this.scanner.checkToken(Token.ID.FlowEntry)) {
                        final Token v1 = /*EL:633*/ParserImpl.this.scanner.peekToken();
                        /*SL:634*/throw new ParserException("while parsing a flow sequence", ParserImpl.this.marks.pop(), "expected ',' or ']', but got " + v1.getTokenId(), /*EL:635*/v1.getStartMark());
                    }
                    ParserImpl.this.scanner.getToken();
                }
                /*SL:639*/if (ParserImpl.this.scanner.checkToken(Token.ID.Key)) {
                    final Token v1 = /*EL:640*/ParserImpl.this.scanner.peekToken();
                    final Event v2 = /*EL:642*/new MappingStartEvent(null, null, true, v1.getStartMark(), v1.getEndMark(), Boolean.TRUE);
                    /*SL:643*/ParserImpl.this.state = new ParseFlowSequenceEntryMappingKey();
                    /*SL:644*/return v2;
                }
                /*SL:645*/if (!ParserImpl.this.scanner.checkToken(Token.ID.FlowSequenceEnd)) {
                    /*SL:646*/ParserImpl.this.states.push(new ParseFlowSequenceEntry(false));
                    /*SL:647*/return ParserImpl.this.parseFlowNode();
                }
            }
            final Token v1 = /*EL:650*/ParserImpl.this.scanner.getToken();
            final Event v2 = /*EL:651*/new SequenceEndEvent(v1.getStartMark(), v1.getEndMark());
            /*SL:652*/ParserImpl.this.state = ParserImpl.this.states.pop();
            /*SL:653*/ParserImpl.this.marks.pop();
            /*SL:654*/return v2;
        }
    }
    
    private class ParseFlowSequenceEntryMappingKey implements Production
    {
        @Override
        public Event produce() {
            final Token v1 = /*EL:660*/ParserImpl.this.scanner.getToken();
            /*SL:661*/if (!ParserImpl.this.scanner.checkToken(Token.ID.Value, Token.ID.FlowEntry, Token.ID.FlowSequenceEnd)) {
                /*SL:662*/ParserImpl.this.states.push(new ParseFlowSequenceEntryMappingValue());
                /*SL:663*/return ParserImpl.this.parseFlowNode();
            }
            /*SL:665*/ParserImpl.this.state = new ParseFlowSequenceEntryMappingValue();
            /*SL:666*/return ParserImpl.this.processEmptyScalar(v1.getEndMark());
        }
    }
    
    private class ParseFlowSequenceEntryMappingValue implements Production
    {
        @Override
        public Event produce() {
            /*SL:673*/if (!ParserImpl.this.scanner.checkToken(Token.ID.Value)) {
                /*SL:683*/ParserImpl.this.state = new ParseFlowSequenceEntryMappingEnd();
                final Token v1 = /*EL:684*/ParserImpl.this.scanner.peekToken();
                /*SL:685*/return ParserImpl.this.processEmptyScalar(v1.getStartMark());
            }
            final Token v1 = ParserImpl.this.scanner.getToken();
            if (!ParserImpl.this.scanner.checkToken(Token.ID.FlowEntry, Token.ID.FlowSequenceEnd)) {
                ParserImpl.this.states.push(new ParseFlowSequenceEntryMappingEnd());
                return ParserImpl.this.parseFlowNode();
            }
            ParserImpl.this.state = new ParseFlowSequenceEntryMappingEnd();
            return ParserImpl.this.processEmptyScalar(v1.getEndMark());
        }
    }
    
    private class ParseFlowSequenceEntryMappingEnd implements Production
    {
        @Override
        public Event produce() {
            /*SL:692*/ParserImpl.this.state = new ParseFlowSequenceEntry(false);
            final Token v1 = /*EL:693*/ParserImpl.this.scanner.peekToken();
            /*SL:694*/return new MappingEndEvent(v1.getStartMark(), v1.getEndMark());
        }
    }
    
    private class ParseFlowMappingFirstKey implements Production
    {
        @Override
        public Event produce() {
            final Token v1 = /*EL:709*/ParserImpl.this.scanner.getToken();
            /*SL:710*/ParserImpl.this.marks.push(v1.getStartMark());
            /*SL:711*/return new ParseFlowMappingKey(true).produce();
        }
    }
    
    private class ParseFlowMappingKey implements Production
    {
        private boolean first;
        
        public ParseFlowMappingKey(final boolean a1) {
            this.first = false;
            this.first = a1;
        }
        
        @Override
        public Event produce() {
            /*SL:723*/if (!ParserImpl.this.scanner.checkToken(Token.ID.FlowMappingEnd)) {
                /*SL:724*/if (!this.first) {
                    /*SL:725*/if (!ParserImpl.this.scanner.checkToken(Token.ID.FlowEntry)) {
                        final Token v1 = /*EL:728*/ParserImpl.this.scanner.peekToken();
                        /*SL:729*/throw new ParserException("while parsing a flow mapping", ParserImpl.this.marks.pop(), "expected ',' or '}', but got " + v1.getTokenId(), /*EL:730*/v1.getStartMark());
                    }
                    ParserImpl.this.scanner.getToken();
                }
                /*SL:734*/if (ParserImpl.this.scanner.checkToken(Token.ID.Key)) {
                    final Token v1 = /*EL:735*/ParserImpl.this.scanner.getToken();
                    /*SL:736*/if (!ParserImpl.this.scanner.checkToken(Token.ID.Value, Token.ID.FlowEntry, Token.ID.FlowMappingEnd)) {
                        /*SL:738*/ParserImpl.this.states.push(new ParseFlowMappingValue());
                        /*SL:739*/return ParserImpl.this.parseFlowNode();
                    }
                    /*SL:741*/ParserImpl.this.state = new ParseFlowMappingValue();
                    /*SL:742*/return ParserImpl.this.processEmptyScalar(v1.getEndMark());
                }
                else/*SL:744*/ if (!ParserImpl.this.scanner.checkToken(Token.ID.FlowMappingEnd)) {
                    /*SL:745*/ParserImpl.this.states.push(new ParseFlowMappingEmptyValue());
                    /*SL:746*/return ParserImpl.this.parseFlowNode();
                }
            }
            final Token v1 = /*EL:749*/ParserImpl.this.scanner.getToken();
            final Event v2 = /*EL:750*/new MappingEndEvent(v1.getStartMark(), v1.getEndMark());
            /*SL:751*/ParserImpl.this.state = ParserImpl.this.states.pop();
            /*SL:752*/ParserImpl.this.marks.pop();
            /*SL:753*/return v2;
        }
    }
    
    private class ParseFlowMappingValue implements Production
    {
        @Override
        public Event produce() {
            /*SL:759*/if (!ParserImpl.this.scanner.checkToken(Token.ID.Value)) {
                /*SL:769*/ParserImpl.this.state = new ParseFlowMappingKey(false);
                final Token v1 = /*EL:770*/ParserImpl.this.scanner.peekToken();
                /*SL:771*/return ParserImpl.this.processEmptyScalar(v1.getStartMark());
            }
            final Token v1 = ParserImpl.this.scanner.getToken();
            if (!ParserImpl.this.scanner.checkToken(Token.ID.FlowEntry, Token.ID.FlowMappingEnd)) {
                ParserImpl.this.states.push(new ParseFlowMappingKey(false));
                return ParserImpl.this.parseFlowNode();
            }
            ParserImpl.this.state = new ParseFlowMappingKey(false);
            return ParserImpl.this.processEmptyScalar(v1.getEndMark());
        }
    }
    
    private class ParseFlowMappingEmptyValue implements Production
    {
        @Override
        public Event produce() {
            /*SL:778*/ParserImpl.this.state = new ParseFlowMappingKey(false);
            /*SL:779*/return ParserImpl.this.processEmptyScalar(ParserImpl.this.scanner.peekToken().getStartMark());
        }
    }
}

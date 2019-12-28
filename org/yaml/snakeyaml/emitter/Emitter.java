package org.yaml.snakeyaml.emitter;

import java.util.Set;
import java.util.Collection;
import java.util.TreeSet;
import org.yaml.snakeyaml.events.StreamStartEvent;
import java.util.HashMap;
import org.yaml.snakeyaml.reader.StreamReader;
import org.yaml.snakeyaml.scanner.Constant;
import org.yaml.snakeyaml.error.YAMLException;
import org.yaml.snakeyaml.events.MappingEndEvent;
import org.yaml.snakeyaml.events.SequenceEndEvent;
import org.yaml.snakeyaml.events.NodeEvent;
import org.yaml.snakeyaml.events.ScalarEvent;
import org.yaml.snakeyaml.events.AliasEvent;
import java.util.Iterator;
import org.yaml.snakeyaml.events.StreamEndEvent;
import org.yaml.snakeyaml.events.CollectionEndEvent;
import org.yaml.snakeyaml.events.DocumentEndEvent;
import org.yaml.snakeyaml.events.CollectionStartEvent;
import org.yaml.snakeyaml.events.MappingStartEvent;
import org.yaml.snakeyaml.events.SequenceStartEvent;
import org.yaml.snakeyaml.events.DocumentStartEvent;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.concurrent.ArrayBlockingQueue;
import org.yaml.snakeyaml.DumperOptions;
import java.util.regex.Pattern;
import org.yaml.snakeyaml.events.Event;
import java.util.Queue;
import org.yaml.snakeyaml.util.ArrayStack;
import java.io.Writer;
import java.util.Map;

public final class Emitter implements Emitable
{
    private static final Map<Character, String> ESCAPE_REPLACEMENTS;
    public static final int MIN_INDENT = 1;
    public static final int MAX_INDENT = 10;
    private static final char[] SPACE;
    private static final Map<String, String> DEFAULT_TAG_PREFIXES;
    private final Writer stream;
    private final ArrayStack<EmitterState> states;
    private EmitterState state;
    private final Queue<Event> events;
    private Event event;
    private final ArrayStack<Integer> indents;
    private Integer indent;
    private int flowLevel;
    private boolean rootContext;
    private boolean mappingContext;
    private boolean simpleKeyContext;
    private int column;
    private boolean whitespace;
    private boolean indention;
    private boolean openEnded;
    private Boolean canonical;
    private Boolean prettyFlow;
    private boolean allowUnicode;
    private int bestIndent;
    private int indicatorIndent;
    private int bestWidth;
    private char[] bestLineBreak;
    private boolean splitLines;
    private Map<String, String> tagPrefixes;
    private String preparedAnchor;
    private String preparedTag;
    private ScalarAnalysis analysis;
    private Character style;
    private static final Pattern HANDLE_FORMAT;
    private static final Pattern ANCHOR_FORMAT;
    
    public Emitter(final Writer a1, final DumperOptions a2) {
        this.stream = a1;
        this.states = new ArrayStack<EmitterState>(100);
        this.state = new ExpectStreamStart();
        this.events = new ArrayBlockingQueue<Event>(100);
        this.event = null;
        this.indents = new ArrayStack<Integer>(10);
        this.indent = null;
        this.flowLevel = 0;
        this.mappingContext = false;
        this.simpleKeyContext = false;
        this.column = 0;
        this.whitespace = true;
        this.indention = true;
        this.openEnded = false;
        this.canonical = a2.isCanonical();
        this.prettyFlow = a2.isPrettyFlow();
        this.allowUnicode = a2.isAllowUnicode();
        this.bestIndent = 2;
        if (a2.getIndent() > 1 && a2.getIndent() < 10) {
            this.bestIndent = a2.getIndent();
        }
        this.indicatorIndent = a2.getIndicatorIndent();
        this.bestWidth = 80;
        if (a2.getWidth() > this.bestIndent * 2) {
            this.bestWidth = a2.getWidth();
        }
        this.bestLineBreak = a2.getLineBreak().getString().toCharArray();
        this.splitLines = a2.getSplitLines();
        this.tagPrefixes = new LinkedHashMap<String, String>();
        this.preparedAnchor = null;
        this.preparedTag = null;
        this.analysis = null;
        this.style = null;
    }
    
    @Override
    public void emit(final Event a1) throws IOException {
        /*SL:215*/this.events.add(a1);
        /*SL:216*/while (!this.needMoreEvents()) {
            /*SL:217*/this.event = this.events.poll();
            /*SL:218*/this.state.expect();
            /*SL:219*/this.event = null;
        }
    }
    
    private boolean needMoreEvents() {
        /*SL:226*/if (this.events.isEmpty()) {
            /*SL:227*/return true;
        }
        final Event v1 = /*EL:229*/this.events.peek();
        /*SL:230*/if (v1 instanceof DocumentStartEvent) {
            /*SL:231*/return this.needEvents(1);
        }
        /*SL:232*/if (v1 instanceof SequenceStartEvent) {
            /*SL:233*/return this.needEvents(2);
        }
        /*SL:234*/return v1 instanceof MappingStartEvent && /*EL:235*/this.needEvents(3);
    }
    
    private boolean needEvents(final int v2) {
        int v3 = /*EL:242*/0;
        final Iterator<Event> v4 = /*EL:243*/this.events.iterator();
        /*SL:244*/v4.next();
        /*SL:245*/while (v4.hasNext()) {
            final Event a1 = /*EL:246*/v4.next();
            /*SL:247*/if (a1 instanceof DocumentStartEvent || a1 instanceof CollectionStartEvent) {
                /*SL:248*/++v3;
            }
            else/*SL:249*/ if (a1 instanceof DocumentEndEvent || a1 instanceof CollectionEndEvent) {
                /*SL:250*/--v3;
            }
            else/*SL:251*/ if (a1 instanceof StreamEndEvent) {
                /*SL:252*/v3 = -1;
            }
            /*SL:254*/if (v3 < 0) {
                /*SL:255*/return false;
            }
        }
        /*SL:258*/return this.events.size() < v2 + 1;
    }
    
    private void increaseIndent(final boolean a1, final boolean a2) {
        /*SL:262*/this.indents.push(this.indent);
        /*SL:263*/if (this.indent == null) {
            /*SL:264*/if (a1) {
                /*SL:265*/this.indent = this.bestIndent;
            }
            else {
                /*SL:267*/this.indent = 0;
            }
        }
        else/*SL:269*/ if (!a2) {
            /*SL:270*/this.indent += this.bestIndent;
        }
    }
    
    private void expectNode(final boolean a1, final boolean a2, final boolean a3) throws IOException {
        /*SL:384*/this.rootContext = a1;
        /*SL:385*/this.mappingContext = a2;
        /*SL:386*/this.simpleKeyContext = a3;
        /*SL:387*/if (this.event instanceof AliasEvent) {
            /*SL:388*/this.expectAlias();
        }
        else {
            /*SL:389*/if (!(this.event instanceof ScalarEvent) && !(this.event instanceof CollectionStartEvent)) {
                /*SL:410*/throw new EmitterException("expected NodeEvent, but got " + this.event);
            }
            this.processAnchor("&");
            this.processTag();
            if (this.event instanceof ScalarEvent) {
                this.expectScalar();
            }
            else if (this.event instanceof SequenceStartEvent) {
                if (this.flowLevel != 0 || this.canonical || ((SequenceStartEvent)this.event).getFlowStyle() || this.checkEmptySequence()) {
                    this.expectFlowSequence();
                }
                else {
                    this.expectBlockSequence();
                }
            }
            else if (this.flowLevel != 0 || this.canonical || ((MappingStartEvent)this.event).getFlowStyle() || this.checkEmptyMapping()) {
                this.expectFlowMapping();
            }
            else {
                this.expectBlockMapping();
            }
        }
    }
    
    private void expectAlias() throws IOException {
        /*SL:415*/if (((NodeEvent)this.event).getAnchor() == null) {
            /*SL:416*/throw new EmitterException("anchor is not specified for alias");
        }
        /*SL:418*/this.processAnchor("*");
        /*SL:419*/this.state = this.states.pop();
    }
    
    private void expectScalar() throws IOException {
        /*SL:423*/this.increaseIndent(true, false);
        /*SL:424*/this.processScalar();
        /*SL:425*/this.indent = this.indents.pop();
        /*SL:426*/this.state = this.states.pop();
    }
    
    private void expectFlowSequence() throws IOException {
        /*SL:432*/this.writeIndicator("[", true, true, false);
        /*SL:433*/++this.flowLevel;
        /*SL:434*/this.increaseIndent(true, false);
        /*SL:435*/if (this.prettyFlow) {
            /*SL:436*/this.writeIndent();
        }
        /*SL:438*/this.state = new ExpectFirstFlowSequenceItem();
    }
    
    private void expectFlowMapping() throws IOException {
        /*SL:486*/this.writeIndicator("{", true, true, false);
        /*SL:487*/++this.flowLevel;
        /*SL:488*/this.increaseIndent(true, false);
        /*SL:489*/if (this.prettyFlow) {
            /*SL:490*/this.writeIndent();
        }
        /*SL:492*/this.state = new ExpectFirstFlowMappingKey();
    }
    
    private void expectBlockSequence() throws IOException {
        final boolean v1 = /*EL:571*/this.mappingContext && !this.indention;
        /*SL:572*/this.increaseIndent(false, v1);
        /*SL:573*/this.state = new ExpectFirstBlockSequenceItem();
    }
    
    private void expectBlockMapping() throws IOException {
        /*SL:605*/this.increaseIndent(false, false);
        /*SL:606*/this.state = new ExpectFirstBlockMappingKey();
    }
    
    private boolean checkEmptySequence() {
        /*SL:660*/return this.event instanceof SequenceStartEvent && !this.events.isEmpty() && this.events.peek() instanceof SequenceEndEvent;
    }
    
    private boolean checkEmptyMapping() {
        /*SL:664*/return this.event instanceof MappingStartEvent && !this.events.isEmpty() && this.events.peek() instanceof MappingEndEvent;
    }
    
    private boolean checkEmptyDocument() {
        /*SL:668*/if (!(this.event instanceof DocumentStartEvent) || this.events.isEmpty()) {
            /*SL:669*/return false;
        }
        final Event v0 = /*EL:671*/this.events.peek();
        /*SL:672*/if (v0 instanceof ScalarEvent) {
            final ScalarEvent v = /*EL:673*/(ScalarEvent)v0;
            /*SL:674*/return v.getAnchor() == null && v.getTag() == null && v.getImplicit() != null && v.getValue().length() == /*EL:675*/0;
        }
        /*SL:677*/return false;
    }
    
    private boolean checkSimpleKey() {
        int v1 = /*EL:681*/0;
        /*SL:682*/if (this.event instanceof NodeEvent && ((NodeEvent)this.event).getAnchor() != null) {
            /*SL:683*/if (this.preparedAnchor == null) {
                /*SL:684*/this.preparedAnchor = prepareAnchor(((NodeEvent)this.event).getAnchor());
            }
            /*SL:686*/v1 += this.preparedAnchor.length();
        }
        String v2 = /*EL:688*/null;
        /*SL:689*/if (this.event instanceof ScalarEvent) {
            /*SL:690*/v2 = ((ScalarEvent)this.event).getTag();
        }
        else/*SL:691*/ if (this.event instanceof CollectionStartEvent) {
            /*SL:692*/v2 = ((CollectionStartEvent)this.event).getTag();
        }
        /*SL:694*/if (v2 != null) {
            /*SL:695*/if (this.preparedTag == null) {
                /*SL:696*/this.preparedTag = this.prepareTag(v2);
            }
            /*SL:698*/v1 += this.preparedTag.length();
        }
        /*SL:700*/if (this.event instanceof ScalarEvent) {
            /*SL:701*/if (this.analysis == null) {
                /*SL:702*/this.analysis = this.analyzeScalar(((ScalarEvent)this.event).getValue());
            }
            /*SL:704*/v1 += this.analysis.scalar.length();
        }
        /*SL:706*/return v1 < 128 && (this.event instanceof AliasEvent || (this.event instanceof ScalarEvent && !this.analysis.empty && !this.analysis.multiline) || this.checkEmptySequence() || /*EL:708*/this.checkEmptyMapping());
    }
    
    private void processAnchor(final String a1) throws IOException {
        final NodeEvent v1 = /*EL:714*/(NodeEvent)this.event;
        /*SL:715*/if (v1.getAnchor() == null) {
            /*SL:716*/this.preparedAnchor = null;
            /*SL:717*/return;
        }
        /*SL:719*/if (this.preparedAnchor == null) {
            /*SL:720*/this.preparedAnchor = prepareAnchor(v1.getAnchor());
        }
        /*SL:722*/this.writeIndicator(a1 + this.preparedAnchor, true, false, false);
        /*SL:723*/this.preparedAnchor = null;
    }
    
    private void processTag() throws IOException {
        String v0 = /*EL:727*/null;
        /*SL:728*/if (this.event instanceof ScalarEvent) {
            final ScalarEvent v = /*EL:729*/(ScalarEvent)this.event;
            /*SL:730*/v0 = v.getTag();
            /*SL:731*/if (this.style == null) {
                /*SL:732*/this.style = this.chooseScalarStyle();
            }
            /*SL:734*/if ((!this.canonical || v0 == null) && ((this.style == null && v.getImplicit().canOmitTagInPlainScalar()) || /*EL:735*/(this.style != null && v.getImplicit().canOmitTagInNonPlainScalar()))) {
                /*SL:737*/this.preparedTag = null;
                /*SL:738*/return;
            }
            /*SL:740*/if (v.getImplicit().canOmitTagInPlainScalar() && v0 == null) {
                /*SL:741*/v0 = "!";
                /*SL:742*/this.preparedTag = null;
            }
        }
        else {
            final CollectionStartEvent v2 = /*EL:745*/(CollectionStartEvent)this.event;
            /*SL:746*/v0 = v2.getTag();
            /*SL:747*/if ((!this.canonical || v0 == null) && v2.getImplicit()) {
                /*SL:748*/this.preparedTag = null;
                /*SL:749*/return;
            }
        }
        /*SL:752*/if (v0 == null) {
            /*SL:753*/throw new EmitterException("tag is not specified");
        }
        /*SL:755*/if (this.preparedTag == null) {
            /*SL:756*/this.preparedTag = this.prepareTag(v0);
        }
        /*SL:758*/this.writeIndicator(this.preparedTag, true, false, false);
        /*SL:759*/this.preparedTag = null;
    }
    
    private Character chooseScalarStyle() {
        final ScalarEvent v1 = /*EL:763*/(ScalarEvent)this.event;
        /*SL:764*/if (this.analysis == null) {
            /*SL:765*/this.analysis = this.analyzeScalar(v1.getValue());
        }
        /*SL:767*/if ((v1.getStyle() != null && v1.getStyle() == '\"') || this.canonical) {
            /*SL:768*/return '\"';
        }
        /*SL:771*/if (v1.getStyle() == null && v1.getImplicit().canOmitTagInPlainScalar() && (!this.simpleKeyContext || (!this.analysis.empty && !this.analysis.multiline)) && ((this.flowLevel != 0 && this.analysis.allowFlowPlain) || (this.flowLevel == 0 && this.analysis.allowBlockPlain))) {
            /*SL:773*/return null;
        }
        /*SL:777*/if (v1.getStyle() != null && (v1.getStyle() == '|' || v1.getStyle() == '>') && this.flowLevel == 0 && !this.simpleKeyContext && this.analysis.allowBlock) {
            /*SL:778*/return v1.getStyle();
        }
        /*SL:782*/if ((v1.getStyle() == null || v1.getStyle() == '\'') && this.analysis.allowSingleQuoted && (!this.simpleKeyContext || !this.analysis.multiline)) {
            /*SL:783*/return '\'';
        }
        /*SL:786*/return '\"';
    }
    
    private void processScalar() throws IOException {
        final ScalarEvent v1 = /*EL:790*/(ScalarEvent)this.event;
        /*SL:791*/if (this.analysis == null) {
            /*SL:792*/this.analysis = this.analyzeScalar(v1.getValue());
        }
        /*SL:794*/if (this.style == null) {
            /*SL:795*/this.style = this.chooseScalarStyle();
        }
        final boolean v2 = /*EL:797*/!this.simpleKeyContext && this.splitLines;
        /*SL:798*/if (this.style == null) {
            /*SL:799*/this.writePlain(this.analysis.scalar, v2);
        }
        else {
            /*SL:801*/switch ((char)this.style) {
                case '\"': {
                    /*SL:803*/this.writeDoubleQuoted(this.analysis.scalar, v2);
                    /*SL:804*/break;
                }
                case '\'': {
                    /*SL:806*/this.writeSingleQuoted(this.analysis.scalar, v2);
                    /*SL:807*/break;
                }
                case '>': {
                    /*SL:809*/this.writeFolded(this.analysis.scalar, v2);
                    /*SL:810*/break;
                }
                case '|': {
                    /*SL:812*/this.writeLiteral(this.analysis.scalar);
                    /*SL:813*/break;
                }
                default: {
                    /*SL:815*/throw new YAMLException("Unexpected style: " + this.style);
                }
            }
        }
        /*SL:818*/this.analysis = null;
        /*SL:819*/this.style = null;
    }
    
    private String prepareVersion(final DumperOptions.Version a1) {
        /*SL:825*/if (a1.major() != 1) {
            /*SL:826*/throw new EmitterException("unsupported YAML version: " + a1);
        }
        /*SL:828*/return a1.getRepresentation();
    }
    
    private String prepareTagHandle(final String a1) {
        /*SL:834*/if (a1.length() == 0) {
            /*SL:835*/throw new EmitterException("tag handle must not be empty");
        }
        /*SL:836*/if (a1.charAt(0) != '!' || a1.charAt(a1.length() - 1) != '!') {
            /*SL:837*/throw new EmitterException("tag handle must start and end with '!': " + a1);
        }
        /*SL:838*/if (!"!".equals(a1) && !Emitter.HANDLE_FORMAT.matcher(a1).matches()) {
            /*SL:839*/throw new EmitterException("invalid character in the tag handle: " + a1);
        }
        /*SL:841*/return a1;
    }
    
    private String prepareTagPrefix(final String a1) {
        /*SL:845*/if (a1.length() == 0) {
            /*SL:846*/throw new EmitterException("tag prefix must not be empty");
        }
        final StringBuilder v1 = /*EL:848*/new StringBuilder();
        final int v2 = /*EL:849*/0;
        int v3 = /*EL:850*/0;
        /*SL:851*/if (a1.charAt(0) == '!') {
            /*SL:852*/v3 = 1;
        }
        /*SL:854*/while (v3 < a1.length()) {
            /*SL:855*/++v3;
        }
        /*SL:857*/if (v2 < v3) {
            /*SL:858*/v1.append(a1.substring(v2, v3));
        }
        /*SL:860*/return v1.toString();
    }
    
    private String prepareTag(final String v2) {
        /*SL:864*/if (v2.length() == 0) {
            /*SL:865*/throw new EmitterException("tag must not be empty");
        }
        /*SL:867*/if ("!".equals(v2)) {
            /*SL:868*/return v2;
        }
        String v3 = /*EL:870*/null;
        String v4 = /*EL:871*/v2;
        /*SL:873*/for (final String a1 : this.tagPrefixes.keySet()) {
            /*SL:874*/if (v2.startsWith(a1) && ("!".equals(a1) || a1.length() < v2.length())) {
                /*SL:875*/v3 = a1;
            }
        }
        /*SL:878*/if (v3 != null) {
            /*SL:879*/v4 = v2.substring(v3.length());
            /*SL:880*/v3 = this.tagPrefixes.get(v3);
        }
        final int v5 = /*EL:883*/v4.length();
        final String v6 = /*EL:884*/(v5 > 0) ? v4.substring(0, v5) : "";
        /*SL:886*/if (v3 != null) {
            /*SL:887*/return v3 + v6;
        }
        /*SL:889*/return "!<" + v6 + ">";
    }
    
    static String prepareAnchor(final String a1) {
        /*SL:895*/if (a1.length() == 0) {
            /*SL:896*/throw new EmitterException("anchor must not be empty");
        }
        /*SL:898*/if (!Emitter.ANCHOR_FORMAT.matcher(a1).matches()) {
            /*SL:899*/throw new EmitterException("invalid character in the anchor: " + a1);
        }
        /*SL:901*/return a1;
    }
    
    private ScalarAnalysis analyzeScalar(final String v-15) {
        /*SL:906*/if (v-15.length() == 0) {
            /*SL:907*/return new ScalarAnalysis(v-15, true, false, false, true, true, false);
        }
        boolean b = /*EL:910*/false;
        boolean b2 = /*EL:911*/false;
        boolean a2 = /*EL:912*/false;
        boolean b3 = /*EL:913*/false;
        boolean b4 = /*EL:916*/false;
        boolean b5 = /*EL:917*/false;
        boolean b6 = /*EL:918*/false;
        boolean b7 = /*EL:919*/false;
        boolean b8 = /*EL:920*/false;
        boolean b9 = /*EL:921*/false;
        /*SL:924*/if (v-15.startsWith("---") || v-15.startsWith("...")) {
            /*SL:925*/b = true;
            /*SL:926*/b2 = true;
        }
        boolean b10 = /*EL:929*/true;
        boolean b11 = /*EL:930*/v-15.length() == 1 || Constant.NULL_BL_T_LINEBR.has(v-15.codePointAt(1));
        boolean b12 = /*EL:932*/false;
        boolean b13 = /*EL:935*/false;
        int v0 = /*EL:937*/0;
        /*SL:939*/while (v0 < v-15.length()) {
            final int v = /*EL:940*/v-15.codePointAt(v0);
            /*SL:942*/if (v0 == 0) {
                /*SL:944*/if ("#,[]{}&*!|>'\"%@`".indexOf(v) != -1) {
                    /*SL:945*/b2 = true;
                    /*SL:946*/b = true;
                }
                /*SL:948*/if (v == 63 || v == 58) {
                    /*SL:949*/b2 = true;
                    /*SL:950*/if (b11) {
                        /*SL:951*/b = true;
                    }
                }
                /*SL:954*/if (v == 45 && b11) {
                    /*SL:955*/b2 = true;
                    /*SL:956*/b = true;
                }
            }
            else {
                /*SL:960*/if (",?[]{}".indexOf(v) != -1) {
                    /*SL:961*/b2 = true;
                }
                /*SL:963*/if (v == 58) {
                    /*SL:964*/b2 = true;
                    /*SL:965*/if (b11) {
                        /*SL:966*/b = true;
                    }
                }
                /*SL:969*/if (v == 35 && b10) {
                    /*SL:970*/b2 = true;
                    /*SL:971*/b = true;
                }
            }
            final boolean v2 = Constant.LINEBR.has(/*EL:975*/v);
            /*SL:976*/if (v2) {
                /*SL:977*/a2 = true;
            }
            /*SL:979*/if (v != 10 && (32 > v || v > 126)) {
                /*SL:980*/if (v == 133 || (v >= 160 && v <= 55295) || (v >= 57344 && v <= 65533) || (v >= 65536 && v <= 1114111)) {
                    /*SL:984*/if (!this.allowUnicode) {
                        /*SL:985*/b3 = true;
                    }
                }
                else {
                    /*SL:988*/b3 = true;
                }
            }
            /*SL:992*/if (v == 32) {
                /*SL:993*/if (v0 == 0) {
                    /*SL:994*/b4 = true;
                }
                /*SL:996*/if (v0 == v-15.length() - 1) {
                    /*SL:997*/b6 = true;
                }
                /*SL:999*/if (b13) {
                    /*SL:1000*/b8 = true;
                }
                /*SL:1002*/b12 = true;
                /*SL:1003*/b13 = false;
            }
            else/*SL:1004*/ if (v2) {
                /*SL:1005*/if (v0 == 0) {
                    /*SL:1006*/b5 = true;
                }
                /*SL:1008*/if (v0 == v-15.length() - 1) {
                    /*SL:1009*/b7 = true;
                }
                /*SL:1011*/if (b12) {
                    /*SL:1012*/b9 = true;
                }
                /*SL:1014*/b12 = false;
                /*SL:1015*/b13 = true;
            }
            else {
                /*SL:1017*/b12 = false;
                /*SL:1018*/b13 = false;
            }
            /*SL:1022*/v0 += Character.charCount(v);
            /*SL:1023*/b10 = (Constant.NULL_BL_T.has(v) || v2);
            /*SL:1024*/b11 = true;
            /*SL:1025*/if (v0 + 1 < v-15.length()) {
                final int a1 = /*EL:1026*/v0 + Character.charCount(v-15.codePointAt(v0));
                /*SL:1027*/if (a1 >= v-15.length()) {
                    continue;
                }
                /*SL:1028*/b11 = (Constant.NULL_BL_T.has(v-15.codePointAt(a1)) || v2);
            }
        }
        boolean v3 = /*EL:1033*/true;
        boolean v2 = /*EL:1034*/true;
        boolean v4 = /*EL:1035*/true;
        boolean v5 = /*EL:1036*/true;
        /*SL:1038*/if (b4 || b5 || b6 || b7) {
            /*SL:1039*/v2 = (v3 = false);
        }
        /*SL:1042*/if (b6) {
            /*SL:1043*/v5 = false;
        }
        /*SL:1047*/if (b8) {
            /*SL:1048*/v2 = (v3 = (v4 = false));
        }
        /*SL:1052*/if (b9 || b3) {
            /*SL:1053*/v2 = (v3 = (v4 = (v5 = false)));
        }
        /*SL:1057*/if (a2) {
            /*SL:1058*/v3 = false;
        }
        /*SL:1061*/if (b2) {
            /*SL:1062*/v3 = false;
        }
        /*SL:1065*/if (b) {
            /*SL:1066*/v2 = false;
        }
        /*SL:1069*/return new ScalarAnalysis(v-15, false, a2, v3, v2, v4, v5);
    }
    
    void flushStream() throws IOException {
        /*SL:1076*/this.stream.flush();
    }
    
    void writeStreamStart() {
    }
    
    void writeStreamEnd() throws IOException {
        /*SL:1084*/this.flushStream();
    }
    
    void writeIndicator(final String a1, final boolean a2, final boolean a3, final boolean a4) throws IOException {
        /*SL:1089*/if (!this.whitespace && a2) {
            /*SL:1090*/++this.column;
            /*SL:1091*/this.stream.write(Emitter.SPACE);
        }
        /*SL:1093*/this.whitespace = a3;
        /*SL:1094*/this.indention = (this.indention && a4);
        /*SL:1095*/this.column += a1.length();
        /*SL:1096*/this.openEnded = false;
        /*SL:1097*/this.stream.write(a1);
    }
    
    void writeIndent() throws IOException {
        int v1;
        /*SL:1102*/if (this.indent != null) {
            /*SL:1103*/v1 = this.indent;
        }
        else {
            /*SL:1105*/v1 = 0;
        }
        /*SL:1108*/if (!this.indention || this.column > v1 || (this.column == v1 && !this.whitespace)) {
            /*SL:1109*/this.writeLineBreak(null);
        }
        /*SL:1112*/this.writeWhitespace(v1 - this.column);
    }
    
    private void writeWhitespace(final int v2) throws IOException {
        /*SL:1116*/if (v2 <= 0) {
            /*SL:1117*/return;
        }
        /*SL:1119*/this.whitespace = true;
        final char[] v3 = /*EL:1120*/new char[v2];
        /*SL:1121*/for (int a1 = 0; a1 < v3.length; ++a1) {
            /*SL:1122*/v3[a1] = ' ';
        }
        /*SL:1124*/this.column += v2;
        /*SL:1125*/this.stream.write(v3);
    }
    
    private void writeLineBreak(final String a1) throws IOException {
        /*SL:1129*/this.whitespace = true;
        /*SL:1130*/this.indention = true;
        /*SL:1131*/this.column = 0;
        /*SL:1132*/if (a1 == null) {
            /*SL:1133*/this.stream.write(this.bestLineBreak);
        }
        else {
            /*SL:1135*/this.stream.write(a1);
        }
    }
    
    void writeVersionDirective(final String a1) throws IOException {
        /*SL:1140*/this.stream.write("%YAML ");
        /*SL:1141*/this.stream.write(a1);
        /*SL:1142*/this.writeLineBreak(null);
    }
    
    void writeTagDirective(final String a1, final String a2) throws IOException {
        /*SL:1148*/this.stream.write("%TAG ");
        /*SL:1149*/this.stream.write(a1);
        /*SL:1150*/this.stream.write(Emitter.SPACE);
        /*SL:1151*/this.stream.write(a2);
        /*SL:1152*/this.writeLineBreak(null);
    }
    
    private void writeSingleQuoted(final String v-6, final boolean v-5) throws IOException {
        /*SL:1157*/this.writeIndicator("'", true, false, false);
        boolean b = /*EL:1158*/false;
        boolean has = /*EL:1159*/false;
        int n = /*EL:1160*/0;
        /*SL:1162*/for (int i = 0; i <= v-6.length(); /*SL:1214*/++i) {
            char v0 = '\0';
            if (i < v-6.length()) {
                v0 = v-6.charAt(i);
            }
            if (b) {
                if (v0 == '\0' || v0 != ' ') {
                    if (n + 1 == i && this.column > this.bestWidth && v-5 && n != 0 && i != v-6.length()) {
                        this.writeIndent();
                    }
                    else {
                        final int a1 = i - n;
                        this.column += a1;
                        this.stream.write(v-6, n, a1);
                    }
                    n = i;
                }
            }
            else if (has) {
                if (v0 == '\0' || Constant.LINEBR.hasNo(v0)) {
                    if (v-6.charAt(n) == '\n') {
                        this.writeLineBreak(null);
                    }
                    final String v = v-6.substring(n, i);
                    for (final char a2 : v.toCharArray()) {
                        if (a2 == '\n') {
                            this.writeLineBreak(null);
                        }
                        else {
                            this.writeLineBreak(String.valueOf(a2));
                        }
                    }
                    this.writeIndent();
                    n = i;
                }
            }
            else if (Constant.LINEBR.has(v0, "\u0000 '") && n < i) {
                final int v2 = i - n;
                this.column += v2;
                this.stream.write(v-6, n, v2);
                n = i;
            }
            if (v0 == '\'') {
                this.column += 2;
                this.stream.write("''");
                n = i + 1;
            }
            if (v0 != '\0') {
                b = (v0 == ' ');
                has = Constant.LINEBR.has(v0);
            }
        }
        /*SL:1216*/this.writeIndicator("'", false, false, false);
    }
    
    private void writeDoubleQuoted(final String v-5, final boolean v-4) throws IOException {
        /*SL:1220*/this.writeIndicator("\"", true, false, false);
        int n = /*EL:1221*/0;
        /*SL:1223*/for (int i = 0; i <= v-5.length(); /*SL:1289*/++i) {
            Character value = null;
            if (i < v-5.length()) {
                value = v-5.charAt(i);
            }
            if (value == null || "\"\\\u0085\u2028\u2029\ufeff".indexOf(value) != -1 || ' ' > value || value > '~') {
                if (n < i) {
                    final int a1 = i - n;
                    this.column += a1;
                    this.stream.write(v-5, n, a1);
                    n = i;
                }
                if (value != null) {
                    String v2 = null;
                    if (Emitter.ESCAPE_REPLACEMENTS.containsKey(value)) {
                        final String a2 = "\\" + Emitter.ESCAPE_REPLACEMENTS.get(value);
                    }
                    else if (!this.allowUnicode || !StreamReader.isPrintable(value)) {
                        if (value <= '\u00ff') {
                            final String v1 = "0" + Integer.toString(value, 16);
                            v2 = "\\x" + v1.substring(v1.length() - 2);
                        }
                        else if (value >= '\ud800' && value <= '\udbff') {
                            if (i + 1 < v-5.length()) {
                                final Character v3 = v-5.charAt(++i);
                                final String v4 = "000" + Long.toHexString(Character.toCodePoint(value, v3));
                                v2 = "\\U" + v4.substring(v4.length() - 8);
                            }
                            else {
                                final String v1 = "000" + Integer.toString(value, 16);
                                v2 = "\\u" + v1.substring(v1.length() - 4);
                            }
                        }
                        else {
                            final String v1 = "000" + Integer.toString(value, 16);
                            v2 = "\\u" + v1.substring(v1.length() - 4);
                        }
                    }
                    else {
                        v2 = String.valueOf(value);
                    }
                    this.column += v2.length();
                    this.stream.write(v2);
                    n = i + 1;
                }
            }
            if (0 < i && i < v-5.length() - 1 && (value == ' ' || n >= i) && this.column + (i - n) > this.bestWidth && v-4) {
                String v2;
                if (n >= i) {
                    v2 = "\\";
                }
                else {
                    v2 = v-5.substring(n, i) + "\\";
                }
                if (n < i) {
                    n = i;
                }
                this.column += v2.length();
                this.stream.write(v2);
                this.writeIndent();
                this.whitespace = false;
                this.indention = false;
                if (v-5.charAt(n) == ' ') {
                    v2 = "\\";
                    this.column += v2.length();
                    this.stream.write(v2);
                }
            }
        }
        /*SL:1291*/this.writeIndicator("\"", false, false, false);
    }
    
    private String determineBlockHints(final String a1) {
        final StringBuilder v1 = /*EL:1295*/new StringBuilder();
        /*SL:1296*/if (Constant.LINEBR.has(a1.charAt(0), " ")) {
            /*SL:1297*/v1.append(this.bestIndent);
        }
        final char v2 = /*EL:1299*/a1.charAt(a1.length() - 1);
        /*SL:1300*/if (Constant.LINEBR.hasNo(v2)) {
            /*SL:1301*/v1.append("-");
        }
        else/*SL:1302*/ if (a1.length() == 1 || Constant.LINEBR.has(a1.charAt(a1.length() - 2))) {
            /*SL:1303*/v1.append("+");
        }
        /*SL:1305*/return v1.toString();
    }
    
    void writeFolded(final String v-8, final boolean v-7) throws IOException {
        final String determineBlockHints = /*EL:1309*/this.determineBlockHints(v-8);
        /*SL:1310*/this.writeIndicator(">" + determineBlockHints, true, false, false);
        /*SL:1311*/if (determineBlockHints.length() > 0 && determineBlockHints.charAt(determineBlockHints.length() - 1) == '+') {
            /*SL:1312*/this.openEnded = true;
        }
        /*SL:1314*/this.writeLineBreak(null);
        boolean b = /*EL:1315*/true;
        boolean b2 = /*EL:1316*/false;
        boolean has = /*EL:1317*/true;
        int n = /*EL:1318*/0;
        /*SL:1319*/for (int i = 0; i <= v-8.length(); /*SL:1369*/++i) {
            char v0 = '\0';
            if (i < v-8.length()) {
                v0 = v-8.charAt(i);
            }
            if (has) {
                if (v0 == '\0' || Constant.LINEBR.hasNo(v0)) {
                    if (!b && v0 != '\0' && v0 != ' ' && v-8.charAt(n) == '\n') {
                        this.writeLineBreak(null);
                    }
                    b = (v0 == ' ');
                    String a2 = v-8.substring(n, i);
                    final char[] charArray = a2.toCharArray();
                    for (int length = charArray.length, j = 0; j < length; ++j) {
                        a2 = charArray[j];
                        if (a2 == '\n') {
                            this.writeLineBreak(null);
                        }
                        else {
                            this.writeLineBreak(String.valueOf(a2));
                        }
                    }
                    if (v0 != '\0') {
                        this.writeIndent();
                    }
                    n = i;
                }
            }
            else if (b2) {
                if (v0 != ' ') {
                    if (n + 1 == i && this.column > this.bestWidth && v-7) {
                        this.writeIndent();
                    }
                    else {
                        final int v = i - n;
                        this.column += v;
                        this.stream.write(v-8, n, v);
                    }
                    n = i;
                }
            }
            else if (Constant.LINEBR.has(v0, "\u0000 ")) {
                final int v = i - n;
                this.column += v;
                this.stream.write(v-8, n, v);
                if (v0 == '\0') {
                    this.writeLineBreak(null);
                }
                n = i;
            }
            if (v0 != '\0') {
                has = Constant.LINEBR.has(v0);
                b2 = (v0 == ' ');
            }
        }
    }
    
    void writeLiteral(final String v-5) throws IOException {
        final String determineBlockHints = /*EL:1374*/this.determineBlockHints(v-5);
        /*SL:1375*/this.writeIndicator("|" + determineBlockHints, true, false, false);
        /*SL:1376*/if (determineBlockHints.length() > 0 && determineBlockHints.charAt(determineBlockHints.length() - 1) == '+') {
            /*SL:1377*/this.openEnded = true;
        }
        /*SL:1379*/this.writeLineBreak(null);
        boolean has = /*EL:1380*/true;
        int n = /*EL:1381*/0;
        /*SL:1382*/for (int i = 0; i <= v-5.length(); /*SL:1414*/++i) {
            char v0 = '\0';
            if (i < v-5.length()) {
                v0 = v-5.charAt(i);
            }
            if (has) {
                if (v0 == '\0' || Constant.LINEBR.hasNo(v0)) {
                    final String v = v-5.substring(n, i);
                    for (final char a1 : v.toCharArray()) {
                        if (a1 == '\n') {
                            this.writeLineBreak(null);
                        }
                        else {
                            this.writeLineBreak(String.valueOf(a1));
                        }
                    }
                    if (v0 != '\0') {
                        this.writeIndent();
                    }
                    n = i;
                }
            }
            else if (v0 == '\0' || Constant.LINEBR.has(v0)) {
                this.stream.write(v-5, n, i - n);
                if (v0 == '\0') {
                    this.writeLineBreak(null);
                }
                n = i;
            }
            if (v0 != '\0') {
                has = Constant.LINEBR.has(v0);
            }
        }
    }
    
    void writePlain(final String v-6, final boolean v-5) throws IOException {
        /*SL:1419*/if (this.rootContext) {
            /*SL:1420*/this.openEnded = true;
        }
        /*SL:1422*/if (v-6.length() == 0) {
            /*SL:1423*/return;
        }
        /*SL:1425*/if (!this.whitespace) {
            /*SL:1426*/++this.column;
            /*SL:1427*/this.stream.write(Emitter.SPACE);
        }
        /*SL:1429*/this.whitespace = false;
        /*SL:1430*/this.indention = false;
        boolean b = /*EL:1431*/false;
        boolean has = /*EL:1432*/false;
        int n = /*EL:1433*/0;
        /*SL:1434*/for (int i = 0; i <= v-6.length(); /*SL:1482*/++i) {
            char v0 = '\0';
            if (i < v-6.length()) {
                v0 = v-6.charAt(i);
            }
            if (b) {
                if (v0 != ' ') {
                    if (n + 1 == i && this.column > this.bestWidth && v-5) {
                        this.writeIndent();
                        this.whitespace = false;
                        this.indention = false;
                    }
                    else {
                        final int a1 = i - n;
                        this.column += a1;
                        this.stream.write(v-6, n, a1);
                    }
                    n = i;
                }
            }
            else if (has) {
                if (Constant.LINEBR.hasNo(v0)) {
                    if (v-6.charAt(n) == '\n') {
                        this.writeLineBreak(null);
                    }
                    final String v = v-6.substring(n, i);
                    for (final char a2 : v.toCharArray()) {
                        if (a2 == '\n') {
                            this.writeLineBreak(null);
                        }
                        else {
                            this.writeLineBreak(String.valueOf(a2));
                        }
                    }
                    this.writeIndent();
                    this.whitespace = false;
                    this.indention = false;
                    n = i;
                }
            }
            else if (Constant.LINEBR.has(v0, "\u0000 ")) {
                final int v2 = i - n;
                this.column += v2;
                this.stream.write(v-6, n, v2);
                n = i;
            }
            if (v0 != '\0') {
                b = (v0 == ' ');
                has = Constant.LINEBR.has(v0);
            }
        }
    }
    
    static {
        ESCAPE_REPLACEMENTS = new HashMap<Character, String>();
        SPACE = new char[] { ' ' };
        Emitter.ESCAPE_REPLACEMENTS.put('\0', "0");
        Emitter.ESCAPE_REPLACEMENTS.put('\u0007', "a");
        Emitter.ESCAPE_REPLACEMENTS.put('\b', "b");
        Emitter.ESCAPE_REPLACEMENTS.put('\t', "t");
        Emitter.ESCAPE_REPLACEMENTS.put('\n', "n");
        Emitter.ESCAPE_REPLACEMENTS.put('\u000b', "v");
        Emitter.ESCAPE_REPLACEMENTS.put('\f', "f");
        Emitter.ESCAPE_REPLACEMENTS.put('\r', "r");
        Emitter.ESCAPE_REPLACEMENTS.put('\u001b', "e");
        Emitter.ESCAPE_REPLACEMENTS.put('\"', "\"");
        Emitter.ESCAPE_REPLACEMENTS.put('\\', "\\");
        Emitter.ESCAPE_REPLACEMENTS.put('\u0085', "N");
        Emitter.ESCAPE_REPLACEMENTS.put(' ', "_");
        Emitter.ESCAPE_REPLACEMENTS.put('\u2028', "L");
        Emitter.ESCAPE_REPLACEMENTS.put('\u2029', "P");
        (DEFAULT_TAG_PREFIXES = new LinkedHashMap<String, String>()).put("!", "!");
        Emitter.DEFAULT_TAG_PREFIXES.put("tag:yaml.org,2002:", "!!");
        HANDLE_FORMAT = Pattern.compile("^![-_\\w]*!$");
        ANCHOR_FORMAT = Pattern.compile("^[-_\\w]*$");
    }
    
    private class ExpectStreamStart implements EmitterState
    {
        @Override
        public void expect() throws IOException {
            /*SL:280*/if (Emitter.this.event instanceof StreamStartEvent) {
                /*SL:281*/Emitter.this.writeStreamStart();
                /*SL:282*/Emitter.this.state = new ExpectFirstDocumentStart();
                /*SL:286*/return;
            }
            throw new EmitterException("expected StreamStartEvent, but got " + Emitter.this.event);
        }
    }
    
    private class ExpectNothing implements EmitterState
    {
        @Override
        public void expect() throws IOException {
            /*SL:291*/throw new EmitterException("expecting nothing, but got " + Emitter.this.event);
        }
    }
    
    private class ExpectFirstDocumentStart implements EmitterState
    {
        @Override
        public void expect() throws IOException {
            /*SL:299*/new ExpectDocumentStart(true).expect();
        }
    }
    
    private class ExpectDocumentStart implements EmitterState
    {
        private boolean first;
        
        public ExpectDocumentStart(final boolean a1) {
            this.first = a1;
        }
        
        @Override
        public void expect() throws IOException {
            /*SL:311*/if (Emitter.this.event instanceof DocumentStartEvent) {
                final DocumentStartEvent v0 = (DocumentStartEvent)/*EL:312*/Emitter.this.event;
                /*SL:313*/if ((v0.getVersion() != null || v0.getTags() != null) && Emitter.this.openEnded) {
                    /*SL:314*/Emitter.this.writeIndicator("...", true, false, false);
                    /*SL:315*/Emitter.this.writeIndent();
                }
                /*SL:317*/if (v0.getVersion() != null) {
                    final String v = /*EL:318*/Emitter.this.prepareVersion(v0.getVersion());
                    /*SL:319*/Emitter.this.writeVersionDirective(v);
                }
                /*SL:321*/Emitter.this.tagPrefixes = (Map<String, String>)new LinkedHashMap(Emitter.DEFAULT_TAG_PREFIXES);
                /*SL:322*/if (v0.getTags() != null) {
                    final Set<String> v2 = /*EL:323*/new TreeSet<String>(v0.getTags().keySet());
                    /*SL:324*/for (final String v3 : v2) {
                        final String v4 = /*EL:325*/v0.getTags().get(v3);
                        /*SL:326*/Emitter.this.tagPrefixes.put(v4, v3);
                        final String v5 = /*EL:327*/Emitter.this.prepareTagHandle(v3);
                        final String v6 = /*EL:328*/Emitter.this.prepareTagPrefix(v4);
                        /*SL:329*/Emitter.this.writeTagDirective(v5, v6);
                    }
                }
                final boolean v7 = /*EL:332*/this.first && !v0.getExplicit() && !Emitter.this.canonical && v0.getVersion() == /*EL:333*/null && (v0.getTags() == /*EL:334*/null || v0.getTags().isEmpty()) && /*EL:335*/!Emitter.this.checkEmptyDocument();
                /*SL:336*/if (!v7) {
                    /*SL:337*/Emitter.this.writeIndent();
                    /*SL:338*/Emitter.this.writeIndicator("---", true, false, false);
                    /*SL:339*/if (Emitter.this.canonical) {
                        /*SL:340*/Emitter.this.writeIndent();
                    }
                }
                /*SL:343*/Emitter.this.state = new ExpectDocumentRoot();
            }
            else {
                /*SL:344*/if (!(Emitter.this.event instanceof StreamEndEvent)) {
                    /*SL:353*/throw new EmitterException("expected DocumentStartEvent, but got " + Emitter.this.event);
                }
                Emitter.this.writeStreamEnd();
                Emitter.this.state = new ExpectNothing();
            }
        }
    }
    
    private class ExpectDocumentEnd implements EmitterState
    {
        @Override
        public void expect() throws IOException {
            /*SL:360*/if (Emitter.this.event instanceof DocumentEndEvent) {
                /*SL:361*/Emitter.this.writeIndent();
                /*SL:362*/if (((DocumentEndEvent)Emitter.this.event).getExplicit()) {
                    /*SL:363*/Emitter.this.writeIndicator("...", true, false, false);
                    /*SL:364*/Emitter.this.writeIndent();
                }
                /*SL:366*/Emitter.this.flushStream();
                /*SL:367*/Emitter.this.state = new ExpectDocumentStart(false);
                /*SL:371*/return;
            }
            throw new EmitterException("expected DocumentEndEvent, but got " + Emitter.this.event);
        }
    }
    
    private class ExpectDocumentRoot implements EmitterState
    {
        @Override
        public void expect() throws IOException {
            /*SL:376*/Emitter.this.states.push(new ExpectDocumentEnd());
            /*SL:377*/Emitter.this.expectNode(true, false, false);
        }
    }
    
    private class ExpectFirstFlowSequenceItem implements EmitterState
    {
        @Override
        public void expect() throws IOException {
            /*SL:443*/if (Emitter.this.event instanceof SequenceEndEvent) {
                /*SL:444*/Emitter.this.indent = Emitter.this.indents.pop();
                /*SL:445*/Emitter.this.flowLevel--;
                /*SL:446*/Emitter.this.writeIndicator("]", false, false, false);
                /*SL:447*/Emitter.this.state = Emitter.this.states.pop();
            }
            else {
                /*SL:449*/if (Emitter.this.canonical || (Emitter.this.column > Emitter.this.bestWidth && Emitter.this.splitLines) || Emitter.this.prettyFlow) {
                    /*SL:450*/Emitter.this.writeIndent();
                }
                /*SL:452*/Emitter.this.states.push(new ExpectFlowSequenceItem());
                /*SL:453*/Emitter.this.expectNode(false, false, false);
            }
        }
    }
    
    private class ExpectFlowSequenceItem implements EmitterState
    {
        @Override
        public void expect() throws IOException {
            /*SL:460*/if (Emitter.this.event instanceof SequenceEndEvent) {
                /*SL:461*/Emitter.this.indent = Emitter.this.indents.pop();
                /*SL:462*/Emitter.this.flowLevel--;
                /*SL:463*/if (Emitter.this.canonical) {
                    /*SL:464*/Emitter.this.writeIndicator(",", false, false, false);
                    /*SL:465*/Emitter.this.writeIndent();
                }
                /*SL:467*/Emitter.this.writeIndicator("]", false, false, false);
                /*SL:468*/if (Emitter.this.prettyFlow) {
                    /*SL:469*/Emitter.this.writeIndent();
                }
                /*SL:471*/Emitter.this.state = Emitter.this.states.pop();
            }
            else {
                /*SL:473*/Emitter.this.writeIndicator(",", false, false, false);
                /*SL:474*/if (Emitter.this.canonical || (Emitter.this.column > Emitter.this.bestWidth && Emitter.this.splitLines) || Emitter.this.prettyFlow) {
                    /*SL:475*/Emitter.this.writeIndent();
                }
                /*SL:477*/Emitter.this.states.push(new ExpectFlowSequenceItem());
                /*SL:478*/Emitter.this.expectNode(false, false, false);
            }
        }
    }
    
    private class ExpectFirstFlowMappingKey implements EmitterState
    {
        @Override
        public void expect() throws IOException {
            /*SL:497*/if (Emitter.this.event instanceof MappingEndEvent) {
                /*SL:498*/Emitter.this.indent = Emitter.this.indents.pop();
                /*SL:499*/Emitter.this.flowLevel--;
                /*SL:500*/Emitter.this.writeIndicator("}", false, false, false);
                /*SL:501*/Emitter.this.state = Emitter.this.states.pop();
            }
            else {
                /*SL:503*/if (Emitter.this.canonical || (Emitter.this.column > Emitter.this.bestWidth && Emitter.this.splitLines) || Emitter.this.prettyFlow) {
                    /*SL:504*/Emitter.this.writeIndent();
                }
                /*SL:506*/if (!Emitter.this.canonical && Emitter.this.checkSimpleKey()) {
                    /*SL:507*/Emitter.this.states.push(new ExpectFlowMappingSimpleValue());
                    /*SL:508*/Emitter.this.expectNode(false, true, true);
                }
                else {
                    /*SL:510*/Emitter.this.writeIndicator("?", true, false, false);
                    /*SL:511*/Emitter.this.states.push(new ExpectFlowMappingValue());
                    /*SL:512*/Emitter.this.expectNode(false, true, false);
                }
            }
        }
    }
    
    private class ExpectFlowMappingKey implements EmitterState
    {
        @Override
        public void expect() throws IOException {
            /*SL:520*/if (Emitter.this.event instanceof MappingEndEvent) {
                /*SL:521*/Emitter.this.indent = Emitter.this.indents.pop();
                /*SL:522*/Emitter.this.flowLevel--;
                /*SL:523*/if (Emitter.this.canonical) {
                    /*SL:524*/Emitter.this.writeIndicator(",", false, false, false);
                    /*SL:525*/Emitter.this.writeIndent();
                }
                /*SL:527*/if (Emitter.this.prettyFlow) {
                    /*SL:528*/Emitter.this.writeIndent();
                }
                /*SL:530*/Emitter.this.writeIndicator("}", false, false, false);
                /*SL:531*/Emitter.this.state = Emitter.this.states.pop();
            }
            else {
                /*SL:533*/Emitter.this.writeIndicator(",", false, false, false);
                /*SL:534*/if (Emitter.this.canonical || (Emitter.this.column > Emitter.this.bestWidth && Emitter.this.splitLines) || Emitter.this.prettyFlow) {
                    /*SL:535*/Emitter.this.writeIndent();
                }
                /*SL:537*/if (!Emitter.this.canonical && Emitter.this.checkSimpleKey()) {
                    /*SL:538*/Emitter.this.states.push(new ExpectFlowMappingSimpleValue());
                    /*SL:539*/Emitter.this.expectNode(false, true, true);
                }
                else {
                    /*SL:541*/Emitter.this.writeIndicator("?", true, false, false);
                    /*SL:542*/Emitter.this.states.push(new ExpectFlowMappingValue());
                    /*SL:543*/Emitter.this.expectNode(false, true, false);
                }
            }
        }
    }
    
    private class ExpectFlowMappingSimpleValue implements EmitterState
    {
        @Override
        public void expect() throws IOException {
            /*SL:551*/Emitter.this.writeIndicator(":", false, false, false);
            /*SL:552*/Emitter.this.states.push(new ExpectFlowMappingKey());
            /*SL:553*/Emitter.this.expectNode(false, true, false);
        }
    }
    
    private class ExpectFlowMappingValue implements EmitterState
    {
        @Override
        public void expect() throws IOException {
            /*SL:559*/if (Emitter.this.canonical || Emitter.this.column > Emitter.this.bestWidth || Emitter.this.prettyFlow) {
                /*SL:560*/Emitter.this.writeIndent();
            }
            /*SL:562*/Emitter.this.writeIndicator(":", true, false, false);
            /*SL:563*/Emitter.this.states.push(new ExpectFlowMappingKey());
            /*SL:564*/Emitter.this.expectNode(false, true, false);
        }
    }
    
    private class ExpectFirstBlockSequenceItem implements EmitterState
    {
        @Override
        public void expect() throws IOException {
            /*SL:578*/new ExpectBlockSequenceItem(true).expect();
        }
    }
    
    private class ExpectBlockSequenceItem implements EmitterState
    {
        private boolean first;
        
        public ExpectBlockSequenceItem(final boolean a1) {
            this.first = a1;
        }
        
        @Override
        public void expect() throws IOException {
            /*SL:590*/if (!this.first && Emitter.this.event instanceof SequenceEndEvent) {
                /*SL:591*/Emitter.this.indent = Emitter.this.indents.pop();
                /*SL:592*/Emitter.this.state = Emitter.this.states.pop();
            }
            else {
                /*SL:594*/Emitter.this.writeIndent();
                /*SL:595*/Emitter.this.writeWhitespace(Emitter.this.indicatorIndent);
                /*SL:596*/Emitter.this.writeIndicator("-", true, false, true);
                /*SL:597*/Emitter.this.states.push(new ExpectBlockSequenceItem(false));
                /*SL:598*/Emitter.this.expectNode(false, false, false);
            }
        }
    }
    
    private class ExpectFirstBlockMappingKey implements EmitterState
    {
        @Override
        public void expect() throws IOException {
            /*SL:611*/new ExpectBlockMappingKey(true).expect();
        }
    }
    
    private class ExpectBlockMappingKey implements EmitterState
    {
        private boolean first;
        
        public ExpectBlockMappingKey(final boolean a1) {
            this.first = a1;
        }
        
        @Override
        public void expect() throws IOException {
            /*SL:623*/if (!this.first && Emitter.this.event instanceof MappingEndEvent) {
                /*SL:624*/Emitter.this.indent = Emitter.this.indents.pop();
                /*SL:625*/Emitter.this.state = Emitter.this.states.pop();
            }
            else {
                /*SL:627*/Emitter.this.writeIndent();
                /*SL:628*/if (Emitter.this.checkSimpleKey()) {
                    /*SL:629*/Emitter.this.states.push(new ExpectBlockMappingSimpleValue());
                    /*SL:630*/Emitter.this.expectNode(false, true, true);
                }
                else {
                    /*SL:632*/Emitter.this.writeIndicator("?", true, false, true);
                    /*SL:633*/Emitter.this.states.push(new ExpectBlockMappingValue());
                    /*SL:634*/Emitter.this.expectNode(false, true, false);
                }
            }
        }
    }
    
    private class ExpectBlockMappingSimpleValue implements EmitterState
    {
        @Override
        public void expect() throws IOException {
            /*SL:642*/Emitter.this.writeIndicator(":", false, false, false);
            /*SL:643*/Emitter.this.states.push(new ExpectBlockMappingKey(false));
            /*SL:644*/Emitter.this.expectNode(false, true, false);
        }
    }
    
    private class ExpectBlockMappingValue implements EmitterState
    {
        @Override
        public void expect() throws IOException {
            /*SL:650*/Emitter.this.writeIndent();
            /*SL:651*/Emitter.this.writeIndicator(":", true, false, true);
            /*SL:652*/Emitter.this.states.push(new ExpectBlockMappingKey(false));
            /*SL:653*/Emitter.this.expectNode(false, true, false);
        }
    }
}

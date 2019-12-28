package org.yaml.snakeyaml.scanner;

import java.io.ObjectInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.nio.charset.CharacterCodingException;
import org.yaml.snakeyaml.util.UriEncoder;
import java.nio.ByteBuffer;
import org.yaml.snakeyaml.tokens.ScalarToken;
import org.yaml.snakeyaml.tokens.TagToken;
import org.yaml.snakeyaml.tokens.TagTuple;
import org.yaml.snakeyaml.tokens.AliasToken;
import org.yaml.snakeyaml.tokens.AnchorToken;
import org.yaml.snakeyaml.tokens.DirectiveToken;
import org.yaml.snakeyaml.tokens.ValueToken;
import org.yaml.snakeyaml.tokens.KeyToken;
import org.yaml.snakeyaml.tokens.BlockMappingStartToken;
import org.yaml.snakeyaml.tokens.BlockEntryToken;
import org.yaml.snakeyaml.tokens.BlockSequenceStartToken;
import org.yaml.snakeyaml.tokens.FlowEntryToken;
import org.yaml.snakeyaml.tokens.FlowSequenceEndToken;
import org.yaml.snakeyaml.tokens.FlowMappingEndToken;
import org.yaml.snakeyaml.tokens.FlowSequenceStartToken;
import org.yaml.snakeyaml.tokens.FlowMappingStartToken;
import org.yaml.snakeyaml.tokens.DocumentEndToken;
import org.yaml.snakeyaml.tokens.DocumentStartToken;
import org.yaml.snakeyaml.tokens.StreamEndToken;
import org.yaml.snakeyaml.tokens.StreamStartToken;
import org.yaml.snakeyaml.tokens.BlockEndToken;
import org.yaml.snakeyaml.error.YAMLException;
import java.util.Iterator;
import org.yaml.snakeyaml.error.Mark;
import java.util.LinkedHashMap;
import java.util.ArrayList;
import org.yaml.snakeyaml.util.ArrayStack;
import org.yaml.snakeyaml.tokens.Token;
import java.util.List;
import org.yaml.snakeyaml.reader.StreamReader;
import java.util.Map;
import java.util.regex.Pattern;

public final class ScannerImpl implements Scanner
{
    private static final Pattern NOT_HEXA;
    public static final Map<Character, String> ESCAPE_REPLACEMENTS;
    public static final Map<Character, Integer> ESCAPE_CODES;
    private final StreamReader reader;
    private boolean done;
    private int flowLevel;
    private List<Token> tokens;
    private int tokensTaken;
    private int indent;
    private ArrayStack<Integer> indents;
    private boolean allowSimpleKey;
    private Map<Integer, SimpleKey> possibleSimpleKeys;
    
    public ScannerImpl(final StreamReader a1) {
        this.done = false;
        this.flowLevel = 0;
        this.tokensTaken = 0;
        this.indent = -1;
        this.allowSimpleKey = true;
        this.reader = a1;
        this.tokens = new ArrayList<Token>(100);
        this.indents = new ArrayStack<Integer>(10);
        this.possibleSimpleKeys = new LinkedHashMap<Integer, SimpleKey>();
        this.fetchStreamStart();
    }
    
    @Override
    public boolean checkToken(final Token.ID... v0) {
        /*SL:225*/while (this.needMoreTokens()) {
            /*SL:226*/this.fetchMoreTokens();
        }
        /*SL:228*/if (!this.tokens.isEmpty()) {
            /*SL:229*/if (v0.length == 0) {
                /*SL:230*/return true;
            }
            final Token.ID v = /*EL:234*/this.tokens.get(0).getTokenId();
            /*SL:235*/for (int a1 = 0; a1 < v0.length; ++a1) {
                /*SL:236*/if (v == v0[a1]) {
                    /*SL:237*/return true;
                }
            }
        }
        /*SL:241*/return false;
    }
    
    @Override
    public Token peekToken() {
        /*SL:248*/while (this.needMoreTokens()) {
            /*SL:249*/this.fetchMoreTokens();
        }
        /*SL:251*/return this.tokens.get(0);
    }
    
    @Override
    public Token getToken() {
        /*SL:258*/if (!this.tokens.isEmpty()) {
            /*SL:259*/++this.tokensTaken;
            /*SL:260*/return this.tokens.remove(0);
        }
        /*SL:262*/return null;
    }
    
    private boolean needMoreTokens() {
        /*SL:271*/if (this.done) {
            /*SL:272*/return false;
        }
        /*SL:275*/if (this.tokens.isEmpty()) {
            /*SL:276*/return true;
        }
        /*SL:280*/this.stalePossibleSimpleKeys();
        /*SL:281*/return this.nextPossibleSimpleKey() == this.tokensTaken;
    }
    
    private void fetchMoreTokens() {
        /*SL:289*/this.scanToNextToken();
        /*SL:291*/this.stalePossibleSimpleKeys();
        /*SL:294*/this.unwindIndent(this.reader.getColumn());
        final int peek = /*EL:297*/this.reader.peek();
        /*SL:298*/switch (peek) {
            case 0: {
                /*SL:301*/this.fetchStreamEnd();
                /*SL:302*/return;
            }
            case 37: {
                /*SL:305*/if (this.checkDirective()) {
                    /*SL:306*/this.fetchDirective();
                    /*SL:307*/return;
                }
                break;
            }
            case 45: {
                /*SL:312*/if (this.checkDocumentStart()) {
                    /*SL:313*/this.fetchDocumentStart();
                    /*SL:314*/return;
                }
                /*SL:316*/if (this.checkBlockEntry()) {
                    /*SL:317*/this.fetchBlockEntry();
                    /*SL:318*/return;
                }
                break;
            }
            case 46: {
                /*SL:323*/if (this.checkDocumentEnd()) {
                    /*SL:324*/this.fetchDocumentEnd();
                    /*SL:325*/return;
                }
                break;
            }
            case 91: {
                /*SL:331*/this.fetchFlowSequenceStart();
                /*SL:332*/return;
            }
            case 123: {
                /*SL:335*/this.fetchFlowMappingStart();
                /*SL:336*/return;
            }
            case 93: {
                /*SL:339*/this.fetchFlowSequenceEnd();
                /*SL:340*/return;
            }
            case 125: {
                /*SL:343*/this.fetchFlowMappingEnd();
                /*SL:344*/return;
            }
            case 44: {
                /*SL:347*/this.fetchFlowEntry();
                /*SL:348*/return;
            }
            case 63: {
                /*SL:352*/if (this.checkKey()) {
                    /*SL:353*/this.fetchKey();
                    /*SL:354*/return;
                }
                break;
            }
            case 58: {
                /*SL:359*/if (this.checkValue()) {
                    /*SL:360*/this.fetchValue();
                    /*SL:361*/return;
                }
                break;
            }
            case 42: {
                /*SL:366*/this.fetchAlias();
                /*SL:367*/return;
            }
            case 38: {
                /*SL:370*/this.fetchAnchor();
                /*SL:371*/return;
            }
            case 33: {
                /*SL:374*/this.fetchTag();
                /*SL:375*/return;
            }
            case 124: {
                /*SL:378*/if (this.flowLevel == 0) {
                    /*SL:379*/this.fetchLiteral();
                    /*SL:380*/return;
                }
                break;
            }
            case 62: {
                /*SL:385*/if (this.flowLevel == 0) {
                    /*SL:386*/this.fetchFolded();
                    /*SL:387*/return;
                }
                break;
            }
            case 39: {
                /*SL:392*/this.fetchSingle();
                /*SL:393*/return;
            }
            case 34: {
                /*SL:396*/this.fetchDouble();
                /*SL:397*/return;
            }
        }
        /*SL:400*/if (this.checkPlain()) {
            /*SL:401*/this.fetchPlain();
            /*SL:402*/return;
        }
        String s = /*EL:407*/String.valueOf(Character.toChars(peek));
        /*SL:408*/for (final Character v0 : ScannerImpl.ESCAPE_REPLACEMENTS.keySet()) {
            final String v = ScannerImpl.ESCAPE_REPLACEMENTS.get(/*EL:409*/v0);
            /*SL:410*/if (v.equals(s)) {
                /*SL:411*/s = "\\" + v0;
                /*SL:412*/break;
            }
        }
        /*SL:415*/if (peek == 9) {
            /*SL:416*/s += "(TAB)";
        }
        final String format = /*EL:418*/String.format("found character '%s' that cannot start any token. (Do not use %s for indentation)", s, s);
        /*SL:421*/throw new ScannerException("while scanning for the next token", null, format, this.reader.getMark());
    }
    
    private int nextPossibleSimpleKey() {
        /*SL:435*/if (!this.possibleSimpleKeys.isEmpty()) {
            /*SL:436*/return this.possibleSimpleKeys.values().iterator().next().getTokenNumber();
        }
        /*SL:438*/return -1;
    }
    
    private void stalePossibleSimpleKeys() {
        /*SL:452*/if (!this.possibleSimpleKeys.isEmpty()) {
            final Iterator<SimpleKey> v0 = /*EL:453*/this.possibleSimpleKeys.values().iterator();
            /*SL:454*/while (v0.hasNext()) {
                final SimpleKey v = /*EL:455*/v0.next();
                /*SL:456*/if (v.getLine() != this.reader.getLine() || this.reader.getIndex() - /*EL:457*/v.getIndex() > 1024) {
                    /*SL:462*/if (v.isRequired()) {
                        /*SL:466*/throw new ScannerException("while scanning a simple key", v.getMark(), "could not find expected ':'", this.reader.getMark());
                    }
                    /*SL:468*/v0.remove();
                }
            }
        }
    }
    
    private void savePossibleSimpleKey() {
        final boolean v0 = /*EL:487*/this.flowLevel == 0 && this.indent == this.reader.getColumn();
        if (/*EL:489*/!this.allowSimpleKey && v0) {
            /*SL:493*/throw new YAMLException("A simple key is required only if it is the first token in the current line");
        }
        /*SL:499*/if (this.allowSimpleKey) {
            /*SL:500*/this.removePossibleSimpleKey();
            final int v = /*EL:501*/this.tokensTaken + this.tokens.size();
            final SimpleKey v2 = /*EL:503*/new SimpleKey(v, v0, this.reader.getIndex(), this.reader.getLine(), this.reader.getColumn(), this.reader.getMark());
            /*SL:504*/this.possibleSimpleKeys.put(this.flowLevel, v2);
        }
    }
    
    private void removePossibleSimpleKey() {
        final SimpleKey v1 = /*EL:512*/this.possibleSimpleKeys.remove(this.flowLevel);
        /*SL:513*/if (v1 != null && v1.isRequired()) {
            /*SL:515*/throw new ScannerException("while scanning a simple key", v1.getMark(), "could not find expected ':'", this.reader.getMark());
        }
    }
    
    private void unwindIndent(final int v2) {
        /*SL:544*/if (this.flowLevel != 0) {
            /*SL:545*/return;
        }
        /*SL:549*/while (this.indent > v2) {
            final Mark a1 = /*EL:550*/this.reader.getMark();
            /*SL:551*/this.indent = this.indents.pop();
            /*SL:552*/this.tokens.add(new BlockEndToken(a1, a1));
        }
    }
    
    private boolean addIndent(final int a1) {
        /*SL:560*/if (this.indent < a1) {
            /*SL:561*/this.indents.push(this.indent);
            /*SL:562*/this.indent = a1;
            /*SL:563*/return true;
        }
        /*SL:565*/return false;
    }
    
    private void fetchStreamStart() {
        final Mark v1 = /*EL:576*/this.reader.getMark();
        final Token v2 = /*EL:579*/new StreamStartToken(v1, v1);
        /*SL:580*/this.tokens.add(v2);
    }
    
    private void fetchStreamEnd() {
        /*SL:585*/this.unwindIndent(-1);
        /*SL:588*/this.removePossibleSimpleKey();
        /*SL:589*/this.allowSimpleKey = false;
        /*SL:590*/this.possibleSimpleKeys.clear();
        final Mark v1 = /*EL:593*/this.reader.getMark();
        final Token v2 = /*EL:596*/new StreamEndToken(v1, v1);
        /*SL:597*/this.tokens.add(v2);
        /*SL:600*/this.done = true;
    }
    
    private void fetchDirective() {
        /*SL:612*/this.unwindIndent(-1);
        /*SL:615*/this.removePossibleSimpleKey();
        /*SL:616*/this.allowSimpleKey = false;
        final Token v1 = /*EL:619*/this.scanDirective();
        /*SL:620*/this.tokens.add(v1);
    }
    
    private void fetchDocumentStart() {
        /*SL:627*/this.fetchDocumentIndicator(true);
    }
    
    private void fetchDocumentEnd() {
        /*SL:634*/this.fetchDocumentIndicator(false);
    }
    
    private void fetchDocumentIndicator(final boolean v2) {
        /*SL:643*/this.unwindIndent(-1);
        /*SL:647*/this.removePossibleSimpleKey();
        /*SL:648*/this.allowSimpleKey = false;
        final Mark v3 = /*EL:651*/this.reader.getMark();
        /*SL:652*/this.reader.forward(3);
        final Mark v4 = /*EL:653*/this.reader.getMark();
        final Token v5;
        /*SL:655*/if (v2) {
            final Token a1 = /*EL:656*/new DocumentStartToken(v3, v4);
        }
        else {
            /*SL:658*/v5 = new DocumentEndToken(v3, v4);
        }
        /*SL:660*/this.tokens.add(v5);
    }
    
    private void fetchFlowSequenceStart() {
        /*SL:664*/this.fetchFlowCollectionStart(false);
    }
    
    private void fetchFlowMappingStart() {
        /*SL:668*/this.fetchFlowCollectionStart(true);
    }
    
    private void fetchFlowCollectionStart(final boolean v2) {
        /*SL:685*/this.savePossibleSimpleKey();
        /*SL:688*/++this.flowLevel;
        /*SL:691*/this.allowSimpleKey = true;
        final Mark v3 = /*EL:694*/this.reader.getMark();
        /*SL:695*/this.reader.forward(1);
        final Mark v4 = /*EL:696*/this.reader.getMark();
        final Token v5;
        /*SL:698*/if (v2) {
            final Token a1 = /*EL:699*/new FlowMappingStartToken(v3, v4);
        }
        else {
            /*SL:701*/v5 = new FlowSequenceStartToken(v3, v4);
        }
        /*SL:703*/this.tokens.add(v5);
    }
    
    private void fetchFlowSequenceEnd() {
        /*SL:707*/this.fetchFlowCollectionEnd(false);
    }
    
    private void fetchFlowMappingEnd() {
        /*SL:711*/this.fetchFlowCollectionEnd(true);
    }
    
    private void fetchFlowCollectionEnd(final boolean v2) {
        /*SL:726*/this.removePossibleSimpleKey();
        /*SL:729*/--this.flowLevel;
        /*SL:732*/this.allowSimpleKey = false;
        final Mark v3 = /*EL:735*/this.reader.getMark();
        /*SL:736*/this.reader.forward();
        final Mark v4 = /*EL:737*/this.reader.getMark();
        final Token v5;
        /*SL:739*/if (v2) {
            final Token a1 = /*EL:740*/new FlowMappingEndToken(v3, v4);
        }
        else {
            /*SL:742*/v5 = new FlowSequenceEndToken(v3, v4);
        }
        /*SL:744*/this.tokens.add(v5);
    }
    
    private void fetchFlowEntry() {
        /*SL:755*/this.allowSimpleKey = true;
        /*SL:758*/this.removePossibleSimpleKey();
        final Mark v1 = /*EL:761*/this.reader.getMark();
        /*SL:762*/this.reader.forward();
        final Mark v2 = /*EL:763*/this.reader.getMark();
        final Token v3 = /*EL:764*/new FlowEntryToken(v1, v2);
        /*SL:765*/this.tokens.add(v3);
    }
    
    private void fetchBlockEntry() {
        /*SL:775*/if (this.flowLevel == 0) {
            /*SL:777*/if (!this.allowSimpleKey) {
                /*SL:779*/throw new ScannerException(null, null, "sequence entries are not allowed here", this.reader.getMark());
            }
            /*SL:783*/if (this.addIndent(this.reader.getColumn())) {
                final Mark v1 = /*EL:784*/this.reader.getMark();
                /*SL:785*/this.tokens.add(new BlockSequenceStartToken(v1, v1));
            }
        }
        /*SL:792*/this.allowSimpleKey = true;
        /*SL:795*/this.removePossibleSimpleKey();
        final Mark v1 = /*EL:798*/this.reader.getMark();
        /*SL:799*/this.reader.forward();
        final Mark v2 = /*EL:800*/this.reader.getMark();
        final Token v3 = /*EL:801*/new BlockEntryToken(v1, v2);
        /*SL:802*/this.tokens.add(v3);
    }
    
    private void fetchKey() {
        /*SL:812*/if (this.flowLevel == 0) {
            /*SL:814*/if (!this.allowSimpleKey) {
                /*SL:816*/throw new ScannerException(null, null, "mapping keys are not allowed here", this.reader.getMark());
            }
            /*SL:819*/if (this.addIndent(this.reader.getColumn())) {
                final Mark v1 = /*EL:820*/this.reader.getMark();
                /*SL:821*/this.tokens.add(new BlockMappingStartToken(v1, v1));
            }
        }
        /*SL:825*/this.allowSimpleKey = (this.flowLevel == 0);
        /*SL:828*/this.removePossibleSimpleKey();
        final Mark v1 = /*EL:831*/this.reader.getMark();
        /*SL:832*/this.reader.forward();
        final Mark v2 = /*EL:833*/this.reader.getMark();
        final Token v3 = /*EL:834*/new KeyToken(v1, v2);
        /*SL:835*/this.tokens.add(v3);
    }
    
    private void fetchValue() {
        final SimpleKey v0 = /*EL:845*/this.possibleSimpleKeys.remove(this.flowLevel);
        /*SL:846*/if (v0 != null) {
            /*SL:848*/this.tokens.add(v0.getTokenNumber() - this.tokensTaken, /*EL:849*/new KeyToken(v0.getMark(), v0.getMark()));
            /*SL:853*/if (this.flowLevel == 0 && /*EL:854*/this.addIndent(v0.getColumn())) {
                /*SL:855*/this.tokens.add(v0.getTokenNumber() - this.tokensTaken, /*EL:856*/new BlockMappingStartToken(v0.getMark(), v0.getMark()));
            }
            /*SL:860*/this.allowSimpleKey = false;
        }
        else {
            /*SL:866*/if (this.flowLevel == 0 && /*EL:870*/!this.allowSimpleKey) {
                /*SL:872*/throw new ScannerException(null, null, "mapping values are not allowed here", this.reader.getMark());
            }
            /*SL:879*/if (this.flowLevel == 0 && /*EL:880*/this.addIndent(this.reader.getColumn())) {
                final Mark v = /*EL:881*/this.reader.getMark();
                /*SL:882*/this.tokens.add(new BlockMappingStartToken(v, v));
            }
            /*SL:887*/this.allowSimpleKey = (this.flowLevel == 0);
            /*SL:890*/this.removePossibleSimpleKey();
        }
        final Mark v = /*EL:893*/this.reader.getMark();
        /*SL:894*/this.reader.forward();
        final Mark v2 = /*EL:895*/this.reader.getMark();
        final Token v3 = /*EL:896*/new ValueToken(v, v2);
        /*SL:897*/this.tokens.add(v3);
    }
    
    private void fetchAlias() {
        /*SL:912*/this.savePossibleSimpleKey();
        /*SL:915*/this.allowSimpleKey = false;
        final Token v1 = /*EL:918*/this.scanAnchor(false);
        /*SL:919*/this.tokens.add(v1);
    }
    
    private void fetchAnchor() {
        /*SL:933*/this.savePossibleSimpleKey();
        /*SL:936*/this.allowSimpleKey = false;
        final Token v1 = /*EL:939*/this.scanAnchor(true);
        /*SL:940*/this.tokens.add(v1);
    }
    
    private void fetchTag() {
        /*SL:950*/this.savePossibleSimpleKey();
        /*SL:953*/this.allowSimpleKey = false;
        final Token v1 = /*EL:956*/this.scanTag();
        /*SL:957*/this.tokens.add(v1);
    }
    
    private void fetchLiteral() {
        /*SL:968*/this.fetchBlockScalar('|');
    }
    
    private void fetchFolded() {
        /*SL:978*/this.fetchBlockScalar('>');
    }
    
    private void fetchBlockScalar(final char a1) {
        /*SL:990*/this.allowSimpleKey = true;
        /*SL:993*/this.removePossibleSimpleKey();
        final Token v1 = /*EL:996*/this.scanBlockScalar(a1);
        /*SL:997*/this.tokens.add(v1);
    }
    
    private void fetchSingle() {
        /*SL:1004*/this.fetchFlowScalar('\'');
    }
    
    private void fetchDouble() {
        /*SL:1011*/this.fetchFlowScalar('\"');
    }
    
    private void fetchFlowScalar(final char a1) {
        /*SL:1023*/this.savePossibleSimpleKey();
        /*SL:1026*/this.allowSimpleKey = false;
        final Token v1 = /*EL:1029*/this.scanFlowScalar(a1);
        /*SL:1030*/this.tokens.add(v1);
    }
    
    private void fetchPlain() {
        /*SL:1038*/this.savePossibleSimpleKey();
        /*SL:1043*/this.allowSimpleKey = false;
        final Token v1 = /*EL:1046*/this.scanPlain();
        /*SL:1047*/this.tokens.add(v1);
    }
    
    private boolean checkDirective() {
        /*SL:1060*/return this.reader.getColumn() == 0;
    }
    
    private boolean checkDocumentStart() {
        /*SL:1070*/return this.reader.getColumn() == 0 && "---".equals(this.reader.prefix(3)) && Constant.NULL_BL_T_LINEBR.has(this.reader.peek(3));
    }
    
    private boolean checkDocumentEnd() {
        /*SL:1084*/return this.reader.getColumn() == 0 && "...".equals(this.reader.prefix(3)) && Constant.NULL_BL_T_LINEBR.has(this.reader.peek(3));
    }
    
    private boolean checkBlockEntry() {
        /*SL:1096*/return Constant.NULL_BL_T_LINEBR.has(this.reader.peek(1));
    }
    
    private boolean checkKey() {
        /*SL:1104*/return this.flowLevel != 0 || Constant.NULL_BL_T_LINEBR.has(/*EL:1108*/this.reader.peek(1));
    }
    
    private boolean checkValue() {
        /*SL:1117*/return this.flowLevel != 0 || Constant.NULL_BL_T_LINEBR.has(/*EL:1121*/this.reader.peek(1));
    }
    
    private boolean checkPlain() {
        final int v1 = /*EL:1145*/this.reader.peek();
        /*SL:1148*/return Constant.NULL_BL_T_LINEBR.hasNo(v1, "-?:,[]{}#&*!|>'\"%@`") || (Constant.NULL_BL_T_LINEBR.hasNo(this.reader.peek(1)) && /*EL:1149*/(v1 == 45 || (this.flowLevel == 0 && "?:".indexOf(v1) != /*EL:1150*/-1)));
    }
    
    private void scanToNextToken() {
        /*SL:1179*/if (this.reader.getIndex() == 0 && this.reader.peek() == 65279) {
            /*SL:1180*/this.reader.forward();
        }
        boolean v0 = /*EL:1182*/false;
        /*SL:1183*/while (!v0) {
            int v;
            /*SL:1187*/for (v = 0; this.reader.peek(v) == 32; /*SL:1188*/++v) {}
            /*SL:1190*/if (v > 0) {
                /*SL:1191*/this.reader.forward(v);
            }
            /*SL:1197*/if (this.reader.peek() == 35) {
                /*SL:1199*/for (v = 0; Constant.NULL_OR_LINEBR.hasNo(this.reader.peek(v)); /*SL:1200*/++v) {}
                /*SL:1202*/if (v > 0) {
                    /*SL:1203*/this.reader.forward(v);
                }
            }
            /*SL:1208*/if (this.scanLineBreak().length() != 0) {
                /*SL:1209*/if (this.flowLevel != 0) {
                    continue;
                }
                /*SL:1212*/this.allowSimpleKey = true;
            }
            else {
                /*SL:1215*/v0 = true;
            }
        }
    }
    
    private Token scanDirective() {
        final Mark v0 = /*EL:1223*/this.reader.getMark();
        /*SL:1225*/this.reader.forward();
        final String v = /*EL:1226*/this.scanDirectiveName(v0);
        List<?> v2 = /*EL:1227*/null;
        Mark v3;
        /*SL:1228*/if ("YAML".equals(v)) {
            /*SL:1229*/v2 = this.scanYamlDirectiveValue(v0);
            /*SL:1230*/v3 = this.reader.getMark();
        }
        else/*SL:1231*/ if ("TAG".equals(v)) {
            /*SL:1232*/v2 = this.scanTagDirectiveValue(v0);
            /*SL:1233*/v3 = this.reader.getMark();
        }
        else {
            /*SL:1235*/v3 = this.reader.getMark();
            int v4;
            /*SL:1237*/for (v4 = 0; Constant.NULL_OR_LINEBR.hasNo(this.reader.peek(v4)); /*SL:1238*/++v4) {}
            /*SL:1240*/if (v4 > 0) {
                /*SL:1241*/this.reader.forward(v4);
            }
        }
        /*SL:1244*/this.scanDirectiveIgnoredLine(v0);
        /*SL:1245*/return new DirectiveToken<Object>(v, v2, v0, v3);
    }
    
    private String scanDirectiveName(final Mark v-3) {
        int a2;
        int n;
        /*SL:1261*/for (a2 = 0, n = this.reader.peek(a2); Constant.ALPHA.has(n); /*SL:1263*/n = this.reader.peek(a2)) {
            ++a2;
        }
        /*SL:1266*/if (a2 == 0) {
            final String a1 = /*EL:1267*/String.valueOf(Character.toChars(n));
            /*SL:1268*/throw new ScannerException("while scanning a directive", /*EL:1270*/v-3, "expected alphabetic or numeric character, but found " + a1 + "(" + n + ")", this.reader.getMark());
        }
        final String v0 = /*EL:1272*/this.reader.prefixForward(a2);
        /*SL:1273*/n = this.reader.peek();
        /*SL:1274*/if (Constant.NULL_BL_LINEBR.hasNo(n)) {
            final String v = /*EL:1275*/String.valueOf(Character.toChars(n));
            /*SL:1276*/throw new ScannerException("while scanning a directive", /*EL:1278*/v-3, "expected alphabetic or numeric character, but found " + v + "(" + n + ")", this.reader.getMark());
        }
        /*SL:1280*/return v0;
    }
    
    private List<Integer> scanYamlDirectiveValue(final Mark v-3) {
        /*SL:1285*/while (this.reader.peek() == 32) {
            /*SL:1286*/this.reader.forward();
        }
        final Integer scanYamlDirectiveNumber = /*EL:1288*/this.scanYamlDirectiveNumber(v-3);
        int a2 = /*EL:1289*/this.reader.peek();
        /*SL:1290*/if (a2 != 46) {
            final String a1 = /*EL:1291*/String.valueOf(Character.toChars(a2));
            /*SL:1292*/throw new ScannerException("while scanning a directive", /*EL:1294*/v-3, "expected a digit or '.', but found " + a1 + "(" + a2 + ")", this.reader.getMark());
        }
        /*SL:1296*/this.reader.forward();
        final Integer v0 = /*EL:1297*/this.scanYamlDirectiveNumber(v-3);
        /*SL:1298*/a2 = this.reader.peek();
        /*SL:1299*/if (Constant.NULL_BL_LINEBR.hasNo(a2)) {
            final String v = /*EL:1300*/String.valueOf(Character.toChars(a2));
            /*SL:1301*/throw new ScannerException("while scanning a directive", /*EL:1303*/v-3, "expected a digit or ' ', but found " + v + "(" + a2 + ")", this.reader.getMark());
        }
        final List<Integer> v2 = /*EL:1305*/new ArrayList<Integer>(2);
        /*SL:1306*/v2.add(scanYamlDirectiveNumber);
        /*SL:1307*/v2.add(v0);
        /*SL:1308*/return v2;
    }
    
    private Integer scanYamlDirectiveNumber(final Mark v2) {
        final int v3 = /*EL:1320*/this.reader.peek();
        /*SL:1321*/if (!Character.isDigit(v3)) {
            final String a1 = /*EL:1322*/String.valueOf(Character.toChars(v3));
            /*SL:1323*/throw new ScannerException("while scanning a directive", /*EL:1324*/v2, "expected a digit, but found " + a1 + "(" + v3 + ")", this.reader.getMark());
        }
        int v4;
        /*SL:1327*/for (v4 = 0; Character.isDigit(this.reader.peek(v4)); /*SL:1328*/++v4) {}
        final Integer v5 = /*EL:1330*/Integer.parseInt(this.reader.prefixForward(v4));
        /*SL:1331*/return v5;
    }
    
    private List<String> scanTagDirectiveValue(final Mark a1) {
        /*SL:1348*/while (this.reader.peek() == 32) {
            /*SL:1349*/this.reader.forward();
        }
        final String v1 = /*EL:1351*/this.scanTagDirectiveHandle(a1);
        /*SL:1352*/while (this.reader.peek() == 32) {
            /*SL:1353*/this.reader.forward();
        }
        final String v2 = /*EL:1355*/this.scanTagDirectivePrefix(a1);
        final List<String> v3 = /*EL:1356*/new ArrayList<String>(2);
        /*SL:1357*/v3.add(v1);
        /*SL:1358*/v3.add(v2);
        /*SL:1359*/return v3;
    }
    
    private String scanTagDirectiveHandle(final Mark v2) {
        final String v3 = /*EL:1371*/this.scanTagHandle("directive", v2);
        final int v4 = /*EL:1372*/this.reader.peek();
        /*SL:1373*/if (v4 != 32) {
            final String a1 = /*EL:1374*/String.valueOf(Character.toChars(v4));
            /*SL:1375*/throw new ScannerException("while scanning a directive", /*EL:1376*/v2, "expected ' ', but found " + a1 + "(" + v4 + ")", this.reader.getMark());
        }
        /*SL:1378*/return v3;
    }
    
    private String scanTagDirectivePrefix(final Mark v2) {
        final String v3 = /*EL:1388*/this.scanTagUri("directive", v2);
        final int v4 = /*EL:1389*/this.reader.peek();
        /*SL:1390*/if (Constant.NULL_BL_LINEBR.hasNo(v4)) {
            final String a1 = /*EL:1391*/String.valueOf(Character.toChars(v4));
            /*SL:1392*/throw new ScannerException("while scanning a directive", /*EL:1394*/v2, "expected ' ', but found " + a1 + "(" + v4 + ")", this.reader.getMark());
        }
        /*SL:1396*/return v3;
    }
    
    private String scanDirectiveIgnoredLine(final Mark v2) {
        /*SL:1401*/while (this.reader.peek() == 32) {
            /*SL:1402*/this.reader.forward();
        }
        /*SL:1404*/if (this.reader.peek() == 35) {
            /*SL:1405*/while (Constant.NULL_OR_LINEBR.hasNo(this.reader.peek())) {
                /*SL:1406*/this.reader.forward();
            }
        }
        final int v3 = /*EL:1409*/this.reader.peek();
        final String v4 = /*EL:1410*/this.scanLineBreak();
        /*SL:1411*/if (v4.length() == 0 && v3 != 0) {
            final String a1 = /*EL:1412*/String.valueOf(Character.toChars(v3));
            /*SL:1413*/throw new ScannerException("while scanning a directive", /*EL:1415*/v2, "expected a comment or a line break, but found " + a1 + "(" + v3 + ")", this.reader.getMark());
        }
        /*SL:1417*/return v4;
    }
    
    private Token scanAnchor(final boolean v-6) {
        final Mark mark = /*EL:1433*/this.reader.getMark();
        final int peek = /*EL:1434*/this.reader.peek();
        final String s = /*EL:1435*/(peek == 42) ? "alias" : "anchor";
        /*SL:1436*/this.reader.forward();
        int a2;
        int n;
        /*SL:1439*/for (a2 = 0, n = this.reader.peek(a2); Constant.ALPHA.has(n); /*SL:1441*/n = this.reader.peek(a2)) {
            ++a2;
        }
        /*SL:1443*/if (a2 == 0) {
            final String a1 = /*EL:1444*/String.valueOf(Character.toChars(n));
            /*SL:1445*/throw new ScannerException("while scanning an " + s, mark, "expected alphabetic or numeric character, but found " + a1 + "(" + n + ")", this.reader.getMark());
        }
        final String v0 = /*EL:1449*/this.reader.prefixForward(a2);
        /*SL:1450*/n = this.reader.peek();
        /*SL:1451*/if (Constant.NULL_BL_T_LINEBR.hasNo(n, "?:,]}%@`")) {
            final String v = /*EL:1452*/String.valueOf(Character.toChars(n));
            /*SL:1453*/throw new ScannerException("while scanning an " + s, mark, "expected alphabetic or numeric character, but found " + v + "(" + n + ")", this.reader.getMark());
        }
        final Mark v2 = /*EL:1457*/this.reader.getMark();
        Token v3;
        /*SL:1459*/if (v-6) {
            /*SL:1460*/v3 = new AnchorToken(v0, mark, v2);
        }
        else {
            /*SL:1462*/v3 = new AliasToken(v0, mark, v2);
        }
        /*SL:1464*/return v3;
    }
    
    private Token scanTag() {
        final Mark mark = /*EL:1502*/this.reader.getMark();
        int a1 = /*EL:1505*/this.reader.peek(1);
        String scanTagHandle = /*EL:1506*/null;
        String v0 = /*EL:1507*/null;
        /*SL:1509*/if (a1 == 60) {
            /*SL:1512*/this.reader.forward(2);
            /*SL:1513*/v0 = this.scanTagUri("tag", mark);
            /*SL:1514*/a1 = this.reader.peek();
            /*SL:1515*/if (a1 != 62) {
                final String v = /*EL:1518*/String.valueOf(Character.toChars(a1));
                /*SL:1519*/throw new ScannerException("while scanning a tag", mark, "expected '>', but found '" + v + "' (" + a1 + ")", this.reader.getMark());
            }
            /*SL:1523*/this.reader.forward();
        }
        else/*SL:1524*/ if (Constant.NULL_BL_T_LINEBR.has(a1)) {
            /*SL:1527*/v0 = "!";
            /*SL:1528*/this.reader.forward();
        }
        else {
            int v2 = /*EL:1534*/1;
            boolean v3 = /*EL:1535*/false;
            /*SL:1536*/while (Constant.NULL_BL_LINEBR.hasNo(a1)) {
                /*SL:1537*/if (a1 == 33) {
                    /*SL:1538*/v3 = true;
                    /*SL:1539*/break;
                }
                /*SL:1541*/++v2;
                /*SL:1542*/a1 = this.reader.peek(v2);
            }
            /*SL:1544*/scanTagHandle = "!";
            /*SL:1547*/if (v3) {
                /*SL:1548*/scanTagHandle = this.scanTagHandle("tag", mark);
            }
            else {
                /*SL:1550*/scanTagHandle = "!";
                /*SL:1551*/this.reader.forward();
            }
            /*SL:1553*/v0 = this.scanTagUri("tag", mark);
        }
        /*SL:1555*/a1 = this.reader.peek();
        /*SL:1558*/if (Constant.NULL_BL_LINEBR.hasNo(a1)) {
            final String v = /*EL:1559*/String.valueOf(Character.toChars(a1));
            /*SL:1560*/throw new ScannerException("while scanning a tag", mark, "expected ' ', but found '" + v + "' (" + a1 + ")", this.reader.getMark());
        }
        final TagTuple v4 = /*EL:1563*/new TagTuple(scanTagHandle, v0);
        final Mark v5 = /*EL:1564*/this.reader.getMark();
        /*SL:1565*/return new TagToken(v4, mark, v5);
    }
    
    private Token scanBlockScalar(final char v-10) {
        final boolean b;
        /*SL:1573*/if (v-10 == '>') {
            final boolean a1 = /*EL:1574*/true;
        }
        else {
            /*SL:1576*/b = false;
        }
        final StringBuilder sb = /*EL:1578*/new StringBuilder();
        final Mark mark = /*EL:1579*/this.reader.getMark();
        /*SL:1581*/this.reader.forward();
        final Chomping scanBlockScalarIndicators = /*EL:1582*/this.scanBlockScalarIndicators(mark);
        final int increment = /*EL:1583*/scanBlockScalarIndicators.getIncrement();
        /*SL:1584*/this.scanBlockScalarIgnoredLine(mark);
        int n = /*EL:1587*/this.indent + 1;
        /*SL:1588*/if (n < 1) {
            /*SL:1589*/n = 1;
        }
        String s = /*EL:1591*/null;
        int intValue = /*EL:1592*/0;
        int max = /*EL:1593*/0;
        Mark v2;
        /*SL:1595*/if (increment == -1) {
            final Object[] v1 = /*EL:1596*/this.scanBlockScalarIndentation();
            /*SL:1597*/s = (String)v1[0];
            /*SL:1598*/intValue = (int)v1[1];
            /*SL:1599*/v2 = (Mark)v1[2];
            /*SL:1600*/max = Math.max(n, intValue);
        }
        else {
            /*SL:1602*/max = n + increment - 1;
            final Object[] v1 = /*EL:1603*/this.scanBlockScalarBreaks(max);
            /*SL:1604*/s = (String)v1[0];
            /*SL:1605*/v2 = (Mark)v1[1];
        }
        String v3 = /*EL:1608*/"";
        /*SL:1611*/while (this.reader.getColumn() == max && this.reader.peek() != 0) {
            /*SL:1612*/sb.append(s);
            final boolean v4 = /*EL:1613*/" \t".indexOf(this.reader.peek()) == -1;
            int v5;
            /*SL:1615*/for (v5 = 0; Constant.NULL_OR_LINEBR.hasNo(this.reader.peek(v5)); /*SL:1616*/++v5) {}
            /*SL:1618*/sb.append(this.reader.prefixForward(v5));
            /*SL:1619*/v3 = this.scanLineBreak();
            final Object[] v6 = /*EL:1620*/this.scanBlockScalarBreaks(max);
            /*SL:1621*/s = (String)v6[0];
            /*SL:1622*/v2 = (Mark)v6[1];
            /*SL:1623*/if (this.reader.getColumn() != max || this.reader.peek() == 0) {
                break;
            }
            /*SL:1628*/if (b && "\n".equals(v3) && v4 && " \t".indexOf(this.reader.peek()) == /*EL:1629*/-1) {
                /*SL:1630*/if (s.length() != 0) {
                    continue;
                }
                /*SL:1631*/sb.append(" ");
            }
            else {
                /*SL:1634*/sb.append(v3);
            }
        }
        /*SL:1643*/if (scanBlockScalarIndicators.chompTailIsNotFalse()) {
            /*SL:1644*/sb.append(v3);
        }
        /*SL:1646*/if (scanBlockScalarIndicators.chompTailIsTrue()) {
            /*SL:1647*/sb.append(s);
        }
        /*SL:1650*/return new ScalarToken(sb.toString(), false, mark, v2, v-10);
    }
    
    private Chomping scanBlockScalarIndicators(final Mark v-3) {
        Boolean a2 = /*EL:1670*/null;
        int a3 = /*EL:1671*/-1;
        int v0 = /*EL:1672*/this.reader.peek();
        /*SL:1673*/if (v0 == 45 || v0 == 43) {
            /*SL:1674*/if (v0 == 43) {
                /*SL:1675*/a2 = Boolean.TRUE;
            }
            else {
                /*SL:1677*/a2 = Boolean.FALSE;
            }
            /*SL:1679*/this.reader.forward();
            /*SL:1680*/v0 = this.reader.peek();
            /*SL:1681*/if (Character.isDigit(v0)) {
                final String a1 = /*EL:1682*/String.valueOf(Character.toChars(v0));
                /*SL:1683*/a3 = Integer.parseInt(a1);
                /*SL:1684*/if (a3 == 0) {
                    /*SL:1687*/throw new ScannerException("while scanning a block scalar", v-3, "expected indentation indicator in the range 1-9, but found 0", this.reader.getMark());
                }
                /*SL:1689*/this.reader.forward();
            }
        }
        else/*SL:1691*/ if (Character.isDigit(v0)) {
            final String v = /*EL:1692*/String.valueOf(Character.toChars(v0));
            /*SL:1693*/a3 = Integer.parseInt(v);
            /*SL:1694*/if (a3 == 0) {
                /*SL:1697*/throw new ScannerException("while scanning a block scalar", v-3, "expected indentation indicator in the range 1-9, but found 0", this.reader.getMark());
            }
            /*SL:1699*/this.reader.forward();
            /*SL:1700*/v0 = this.reader.peek();
            /*SL:1701*/if (v0 == 45 || v0 == 43) {
                /*SL:1702*/if (v0 == 43) {
                    /*SL:1703*/a2 = Boolean.TRUE;
                }
                else {
                    /*SL:1705*/a2 = Boolean.FALSE;
                }
                /*SL:1707*/this.reader.forward();
            }
        }
        /*SL:1710*/v0 = this.reader.peek();
        /*SL:1711*/if (Constant.NULL_BL_LINEBR.hasNo(v0)) {
            final String v = /*EL:1712*/String.valueOf(Character.toChars(v0));
            /*SL:1713*/throw new ScannerException("while scanning a block scalar", /*EL:1715*/v-3, "expected chomping or indentation indicators, but found " + v + "(" + v0 + ")", this.reader.getMark());
        }
        /*SL:1717*/return new Chomping(a2, a3);
    }
    
    private String scanBlockScalarIgnoredLine(final Mark v2) {
        /*SL:1728*/while (this.reader.peek() == 32) {
            /*SL:1729*/this.reader.forward();
        }
        /*SL:1733*/if (this.reader.peek() == 35) {
            /*SL:1734*/while (Constant.NULL_OR_LINEBR.hasNo(this.reader.peek())) {
                /*SL:1735*/this.reader.forward();
            }
        }
        final int v3 = /*EL:1740*/this.reader.peek();
        final String v4 = /*EL:1741*/this.scanLineBreak();
        /*SL:1742*/if (v4.length() == 0 && v3 != 0) {
            final String a1 = /*EL:1743*/String.valueOf(Character.toChars(v3));
            /*SL:1744*/throw new ScannerException("while scanning a block scalar", /*EL:1746*/v2, "expected a comment or a line break, but found " + a1 + "(" + v3 + ")", this.reader.getMark());
        }
        /*SL:1748*/return v4;
    }
    
    private Object[] scanBlockScalarIndentation() {
        final StringBuilder v1 = /*EL:1760*/new StringBuilder();
        int v2 = /*EL:1761*/0;
        Mark v3 = /*EL:1762*/this.reader.getMark();
        /*SL:1766*/while (Constant.LINEBR.has(this.reader.peek(), " \r")) {
            /*SL:1767*/if (this.reader.peek() != 32) {
                /*SL:1770*/v1.append(this.scanLineBreak());
                /*SL:1771*/v3 = this.reader.getMark();
            }
            else {
                /*SL:1776*/this.reader.forward();
                /*SL:1777*/if (this.reader.getColumn() <= v2) {
                    continue;
                }
                /*SL:1778*/v2 = this.reader.getColumn();
            }
        }
        /*SL:1783*/return new Object[] { v1.toString(), v2, v3 };
    }
    
    private Object[] scanBlockScalarBreaks(final int a1) {
        final StringBuilder v1 = /*EL:1788*/new StringBuilder();
        Mark v2 = /*EL:1789*/this.reader.getMark();
        /*SL:1793*/for (int v3 = this.reader.getColumn(); v3 < a1 && this.reader.peek() == 32; /*SL:1795*/++v3) {
            this.reader.forward();
        }
        String v4 = /*EL:1800*/null;
        /*SL:1801*/while ((v4 = this.scanLineBreak()).length() != 0) {
            /*SL:1802*/v1.append(v4);
            /*SL:1803*/v2 = this.reader.getMark();
            /*SL:1807*/for (int v3 = this.reader.getColumn(); v3 < a1 && this.reader.peek() == 32; /*SL:1809*/++v3) {
                this.reader.forward();
            }
        }
        /*SL:1813*/return new Object[] { v1.toString(), v2 };
    }
    
    private Token scanFlowScalar(final char v2) {
        final boolean v3;
        /*SL:1836*/if (v2 == '\"') {
            final boolean a1 = /*EL:1837*/true;
        }
        else {
            /*SL:1839*/v3 = false;
        }
        final StringBuilder v4 = /*EL:1841*/new StringBuilder();
        final Mark v5 = /*EL:1842*/this.reader.getMark();
        final int v6 = /*EL:1843*/this.reader.peek();
        /*SL:1844*/this.reader.forward();
        /*SL:1845*/v4.append(this.scanFlowScalarNonSpaces(v3, v5));
        /*SL:1846*/while (this.reader.peek() != v6) {
            /*SL:1847*/v4.append(this.scanFlowScalarSpaces(v5));
            /*SL:1848*/v4.append(this.scanFlowScalarNonSpaces(v3, v5));
        }
        /*SL:1850*/this.reader.forward();
        final Mark v7 = /*EL:1851*/this.reader.getMark();
        /*SL:1852*/return new ScalarToken(v4.toString(), false, v5, v7, v2);
    }
    
    private String scanFlowScalarNonSpaces(final boolean v-6, final Mark v-5) {
        final StringBuilder sb = /*EL:1860*/new StringBuilder();
        while (true) {
            int intValue;
            /*SL:1865*/for (intValue = 0; Constant.NULL_BL_T_LINEBR.hasNo(this.reader.peek(intValue), "'\"\\"); /*SL:1866*/++intValue) {}
            /*SL:1868*/if (intValue != 0) {
                /*SL:1869*/sb.append(this.reader.prefixForward(intValue));
            }
            int n = /*EL:1873*/this.reader.peek();
            /*SL:1874*/if (!v-6 && n == 39 && this.reader.peek(1) == 39) {
                /*SL:1875*/sb.append("'");
                /*SL:1876*/this.reader.forward(2);
            }
            else/*SL:1877*/ if ((v-6 && n == 39) || (!v-6 && "\"\\".indexOf(n) != -1)) {
                /*SL:1878*/sb.appendCodePoint(n);
                /*SL:1879*/this.reader.forward();
            }
            else {
                /*SL:1880*/if (!v-6 || n != 92) {
                    /*SL:1914*/return sb.toString();
                }
                this.reader.forward();
                n = this.reader.peek();
                if (!Character.isSupplementaryCodePoint(n) && ScannerImpl.ESCAPE_REPLACEMENTS.containsKey((char)n)) {
                    sb.append(ScannerImpl.ESCAPE_REPLACEMENTS.get((char)n));
                    this.reader.forward();
                }
                else if (!Character.isSupplementaryCodePoint(n) && ScannerImpl.ESCAPE_CODES.containsKey((char)n)) {
                    intValue = ScannerImpl.ESCAPE_CODES.get((char)n);
                    this.reader.forward();
                    final String a1 = this.reader.prefix(intValue);
                    if (ScannerImpl.NOT_HEXA.matcher(a1).find()) {
                        throw new ScannerException("while scanning a double-quoted scalar", v-5, "expected escape sequence of " + intValue + " hexadecimal numbers, but found: " + a1, this.reader.getMark());
                    }
                    final int a2 = Integer.parseInt(a1, 16);
                    final String v1 = new String(Character.toChars(a2));
                    sb.append(v1);
                    this.reader.forward(intValue);
                }
                else {
                    if (this.scanLineBreak().length() == 0) {
                        final String value = String.valueOf(Character.toChars(n));
                        throw new ScannerException("while scanning a double-quoted scalar", v-5, "found unknown escape character " + value + "(" + n + ")", this.reader.getMark());
                    }
                    sb.append(this.scanFlowScalarBreaks(v-5));
                }
            }
        }
    }
    
    private String scanFlowScalarSpaces(final Mark v2) {
        final StringBuilder v3 = /*EL:1921*/new StringBuilder();
        int v4;
        /*SL:1925*/for (v4 = 0; " \t".indexOf(this.reader.peek(v4)) != -1; /*SL:1926*/++v4) {}
        final String v5 = /*EL:1928*/this.reader.prefixForward(v4);
        final int v6 = /*EL:1929*/this.reader.peek();
        /*SL:1930*/if (v6 == 0) {
            /*SL:1933*/throw new ScannerException("while scanning a quoted scalar", v2, "found unexpected end of stream", this.reader.getMark());
        }
        final String v7 = /*EL:1936*/this.scanLineBreak();
        /*SL:1937*/if (v7.length() != 0) {
            final String a1 = /*EL:1938*/this.scanFlowScalarBreaks(v2);
            /*SL:1939*/if (!"\n".equals(v7)) {
                /*SL:1940*/v3.append(v7);
            }
            else/*SL:1941*/ if (a1.length() == 0) {
                /*SL:1942*/v3.append(" ");
            }
            /*SL:1944*/v3.append(a1);
        }
        else {
            /*SL:1946*/v3.append(v5);
        }
        /*SL:1948*/return v3.toString();
    }
    
    private String scanFlowScalarBreaks(final Mark v-2) {
        final StringBuilder sb = /*EL:1953*/new StringBuilder();
        while (true) {
            final String a1 = /*EL:1957*/this.reader.prefix(3);
            /*SL:1958*/if (("---".equals(a1) || "...".equals(a1)) && Constant.NULL_BL_T_LINEBR.has(this.reader.peek(3))) {
                /*SL:1961*/throw new ScannerException("while scanning a quoted scalar", v-2, "found unexpected document separator", this.reader.getMark());
            }
            /*SL:1964*/while (" \t".indexOf(this.reader.peek()) != -1) {
                /*SL:1965*/this.reader.forward();
            }
            final String v1 = /*EL:1969*/this.scanLineBreak();
            /*SL:1970*/if (v1.length() == 0) {
                /*SL:1973*/return sb.toString();
            }
            sb.append(v1);
        }
    }
    
    private Token scanPlain() {
        final StringBuilder sb = /*EL:1990*/new StringBuilder();
        Mark a3;
        final Mark mark = /*EL:1992*/a3 = this.reader.getMark();
        final int n = /*EL:1993*/this.indent + 1;
        String v0 = /*EL:1994*/"";
        do {
            int v = /*EL:1997*/0;
            /*SL:1999*/if (this.reader.peek() == 35) {
                /*SL:2000*/break;
            }
            int v2;
            while (true) {
                /*SL:2003*/v2 = this.reader.peek(v);
                if (Constant.NULL_BL_T_LINEBR.has(/*EL:2004*/v2) || (this.flowLevel == 0 && v2 == 58 && Constant.NULL_BL_T_LINEBR.has(this.reader.peek(v + 1))) || /*EL:2006*/(this.flowLevel != 0 && ",:?[]{}".indexOf(v2) != /*EL:2007*/-1)) {
                    break;
                }
                /*SL:2010*/++v;
            }
            /*SL:2013*/if (this.flowLevel != 0 && v2 == 58 && Constant.NULL_BL_T_LINEBR.hasNo(this.reader.peek(v + 1), /*EL:2014*/",[]{}")) {
                /*SL:2015*/this.reader.forward(v);
                /*SL:2017*/throw new ScannerException("while scanning a plain scalar", mark, "found unexpected ':'", this.reader.getMark(), "Please check http://pyyaml.org/wiki/YAMLColonInFlowContext for details.");
            }
            /*SL:2020*/if (v == 0) {
                /*SL:2021*/break;
            }
            /*SL:2023*/this.allowSimpleKey = false;
            /*SL:2024*/sb.append(v0);
            /*SL:2025*/sb.append(this.reader.prefixForward(v));
            /*SL:2026*/a3 = this.reader.getMark();
            /*SL:2027*/v0 = this.scanPlainSpaces();
            /*SL:2029*/if (v0.length() == 0 || this.reader.peek() == 35) {
                break;
            }
        } while (this.flowLevel != 0 || this.reader.getColumn() >= /*EL:2030*/n);
        /*SL:2034*/return new ScalarToken(sb.toString(), mark, a3, true);
    }
    
    private String scanPlainSpaces() {
        int a1;
        /*SL:2043*/for (a1 = 0; this.reader.peek(a1) == 32 || this.reader.peek(a1) == 9; /*SL:2044*/++a1) {}
        final String prefixForward = /*EL:2046*/this.reader.prefixForward(a1);
        final String scanLineBreak = /*EL:2047*/this.scanLineBreak();
        /*SL:2048*/if (scanLineBreak.length() == 0) {
            /*SL:2080*/return prefixForward;
        }
        this.allowSimpleKey = true;
        String s = this.reader.prefix(3);
        if ("---".equals(s) || ("...".equals(s) && Constant.NULL_BL_T_LINEBR.has(this.reader.peek(3)))) {
            return "";
        }
        final StringBuilder v0 = new StringBuilder();
        while (true) {
            if (this.reader.peek() == 32) {
                this.reader.forward();
            }
            else {
                final String v = this.scanLineBreak();
                if (v.length() != 0) {
                    v0.append(v);
                    s = this.reader.prefix(3);
                    if ("---".equals(s) || ("...".equals(s) && Constant.NULL_BL_T_LINEBR.has(this.reader.peek(3)))) {
                        return "";
                    }
                    continue;
                }
                else {
                    if (!"\n".equals(scanLineBreak)) {
                        return scanLineBreak + (Object)v0;
                    }
                    if (v0.length() == 0) {
                        return " ";
                    }
                    return v0.toString();
                }
            }
        }
    }
    
    private String scanTagHandle(final String v2, final Mark v3) {
        int v4 = /*EL:2106*/this.reader.peek();
        /*SL:2107*/if (v4 != 33) {
            final String a1 = /*EL:2108*/String.valueOf(Character.toChars(v4));
            /*SL:2109*/throw new ScannerException("while scanning a " + v2, /*EL:2110*/v3, "expected '!', but found " + a1 + "(" + v4 + ")", this.reader.getMark());
        }
        int v5 = /*EL:2115*/1;
        /*SL:2116*/v4 = this.reader.peek(v5);
        /*SL:2117*/if (v4 != 32) {
            /*SL:2122*/while (Constant.ALPHA.has(v4)) {
                /*SL:2123*/++v5;
                /*SL:2124*/v4 = this.reader.peek(v5);
            }
            /*SL:2129*/if (v4 != 33) {
                /*SL:2130*/this.reader.forward(v5);
                final String a2 = /*EL:2131*/String.valueOf(Character.toChars(v4));
                /*SL:2132*/throw new ScannerException("while scanning a " + v2, /*EL:2133*/v3, "expected '!', but found " + a2 + "(" + v4 + ")", this.reader.getMark());
            }
            /*SL:2135*/++v5;
        }
        final String v6 = /*EL:2137*/this.reader.prefixForward(v5);
        /*SL:2138*/return v6;
    }
    
    private String scanTagUri(final String v1, final Mark v2) {
        final StringBuilder v3 = /*EL:2159*/new StringBuilder();
        int v4;
        int v5;
        /*SL:2165*/for (v4 = 0, v5 = this.reader.peek(v4); Constant.URI_CHARS.has(v5); /*SL:2173*/v5 = this.reader.peek(v4)) {
            if (v5 == 37) {
                v3.append(this.reader.prefixForward(v4));
                v4 = 0;
                v3.append(this.scanUriEscapes(v1, v2));
            }
            else {
                ++v4;
            }
        }
        /*SL:2177*/if (v4 != 0) {
            /*SL:2178*/v3.append(this.reader.prefixForward(v4));
            /*SL:2179*/v4 = 0;
        }
        /*SL:2181*/if (v3.length() == 0) {
            final String a1 = /*EL:2183*/String.valueOf(Character.toChars(v5));
            /*SL:2184*/throw new ScannerException("while scanning a " + v1, /*EL:2185*/v2, "expected URI, but found " + a1 + "(" + v5 + ")", this.reader.getMark());
        }
        /*SL:2187*/return v3.toString();
    }
    
    private String scanUriEscapes(final String v-6, final Mark v-5) {
        int n;
        /*SL:2205*/for (n = 1; this.reader.peek(n * 3) == 37; /*SL:2206*/++n) {}
        final Mark mark = /*EL:2212*/this.reader.getMark();
        final ByteBuffer allocate = /*EL:2213*/ByteBuffer.allocate(n);
        /*SL:2214*/while (this.reader.peek() == 37) {
            /*SL:2215*/this.reader.forward();
            try {
                final byte a1 = /*EL:2217*/(byte)Integer.parseInt(this.reader.prefix(2), 16);
                /*SL:2218*/allocate.put(a1);
            }
            catch (NumberFormatException ex2) {
                final int a2 = /*EL:2220*/this.reader.peek();
                final String v1 = /*EL:2221*/String.valueOf(Character.toChars(a2));
                final int v2 = /*EL:2222*/this.reader.peek(1);
                final String v3 = /*EL:2223*/String.valueOf(Character.toChars(v2));
                /*SL:2224*/throw new ScannerException("while scanning a " + v-6, /*EL:2228*/v-5, "expected URI escape sequence of 2 hexadecimal numbers, but found " + v1 + "(" + a2 + ") and " + v3 + "(" + v2 + ")", this.reader.getMark());
            }
            /*SL:2230*/this.reader.forward(2);
        }
        /*SL:2232*/allocate.flip();
        try {
            /*SL:2234*/return UriEncoder.decode(allocate);
        }
        catch (CharacterCodingException ex) {
            /*SL:2236*/throw new ScannerException("while scanning a " + v-6, /*EL:2237*/v-5, "expected URI in UTF-8: " + ex.getMessage(), mark);
        }
    }
    
    private String scanLineBreak() {
        final int v1 = /*EL:2259*/this.reader.peek();
        /*SL:2260*/if (v1 == 13 || v1 == 10 || v1 == 133) {
            /*SL:2261*/if (v1 == 13 && 10 == this.reader.peek(1)) {
                /*SL:2262*/this.reader.forward(2);
            }
            else {
                /*SL:2264*/this.reader.forward();
            }
            /*SL:2266*/return "\n";
        }
        /*SL:2267*/if (v1 == 8232 || v1 == 8233) {
            /*SL:2268*/this.reader.forward();
            /*SL:2269*/return String.valueOf(Character.toChars(v1));
        }
        /*SL:2271*/return "";
    }
    
    static {
        NOT_HEXA = Pattern.compile("[^0-9A-Fa-f]");
        ESCAPE_REPLACEMENTS = new HashMap<Character, String>();
        ESCAPE_CODES = new HashMap<Character, Integer>();
        ScannerImpl.ESCAPE_REPLACEMENTS.put('0', "\u0000");
        ScannerImpl.ESCAPE_REPLACEMENTS.put('a', "\u0007");
        ScannerImpl.ESCAPE_REPLACEMENTS.put('b', "\b");
        ScannerImpl.ESCAPE_REPLACEMENTS.put('t', "\t");
        ScannerImpl.ESCAPE_REPLACEMENTS.put('n', "\n");
        ScannerImpl.ESCAPE_REPLACEMENTS.put('v', "\u000b");
        ScannerImpl.ESCAPE_REPLACEMENTS.put('f', "\f");
        ScannerImpl.ESCAPE_REPLACEMENTS.put('r', "\r");
        ScannerImpl.ESCAPE_REPLACEMENTS.put('e', "\u001b");
        ScannerImpl.ESCAPE_REPLACEMENTS.put(' ', " ");
        ScannerImpl.ESCAPE_REPLACEMENTS.put('\"', "\"");
        ScannerImpl.ESCAPE_REPLACEMENTS.put('\\', "\\");
        ScannerImpl.ESCAPE_REPLACEMENTS.put('N', "\u0085");
        ScannerImpl.ESCAPE_REPLACEMENTS.put('_', " ");
        ScannerImpl.ESCAPE_REPLACEMENTS.put('L', "\u2028");
        ScannerImpl.ESCAPE_REPLACEMENTS.put('P', "\u2029");
        ScannerImpl.ESCAPE_CODES.put('x', 2);
        ScannerImpl.ESCAPE_CODES.put('u', 4);
        ScannerImpl.ESCAPE_CODES.put('U', 8);
    }
    
    private static class Chomping
    {
        private final Boolean value;
        private final int increment;
        
        public Chomping(final Boolean a1, final int a2) {
            this.value = a1;
            this.increment = a2;
        }
        
        public boolean chompTailIsNotFalse() {
            /*SL:2287*/return this.value == null || this.value;
        }
        
        public boolean chompTailIsTrue() {
            /*SL:2291*/return this.value != null && this.value;
        }
        
        public int getIncrement() {
            /*SL:2295*/return this.increment;
        }
    }
}

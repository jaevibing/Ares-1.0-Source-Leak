package com.fasterxml.jackson.core.filter;

import java.io.OutputStream;
import com.fasterxml.jackson.core.Base64Variant;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.io.IOException;
import com.fasterxml.jackson.core.JsonStreamContext;
import com.fasterxml.jackson.core.JsonLocation;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.util.JsonParserDelegate;

public class FilteringParserDelegate extends JsonParserDelegate
{
    protected TokenFilter rootFilter;
    protected boolean _allowMultipleMatches;
    protected boolean _includePath;
    @Deprecated
    protected boolean _includeImmediateParent;
    protected JsonToken _currToken;
    protected JsonToken _lastClearedToken;
    protected TokenFilterContext _headContext;
    protected TokenFilterContext _exposedContext;
    protected TokenFilter _itemFilter;
    protected int _matchCount;
    
    public FilteringParserDelegate(final JsonParser a1, final TokenFilter a2, final boolean a3, final boolean a4) {
        super(a1);
        this.rootFilter = a2;
        this._itemFilter = a2;
        this._headContext = TokenFilterContext.createRootContext(a2);
        this._includePath = a3;
        this._allowMultipleMatches = a4;
    }
    
    public TokenFilter getFilter() {
        /*SL:132*/return this.rootFilter;
    }
    
    public int getMatchCount() {
        /*SL:139*/return this._matchCount;
    }
    
    @Override
    public JsonToken getCurrentToken() {
        /*SL:148*/return this._currToken;
    }
    
    @Override
    public JsonToken currentToken() {
        /*SL:149*/return this._currToken;
    }
    
    @Override
    public final int getCurrentTokenId() {
        final JsonToken v1 = /*EL:152*/this._currToken;
        /*SL:153*/return (v1 == null) ? 0 : v1.id();
    }
    
    @Override
    public final int currentTokenId() {
        final JsonToken v1 = /*EL:156*/this._currToken;
        /*SL:157*/return (v1 == null) ? 0 : v1.id();
    }
    
    @Override
    public boolean hasCurrentToken() {
        /*SL:160*/return this._currToken != null;
    }
    
    @Override
    public boolean hasTokenId(final int a1) {
        final JsonToken v1 = /*EL:162*/this._currToken;
        /*SL:163*/if (v1 == null) {
            /*SL:164*/return 0 == a1;
        }
        /*SL:166*/return v1.id() == a1;
    }
    
    @Override
    public final boolean hasToken(final JsonToken a1) {
        /*SL:170*/return this._currToken == a1;
    }
    
    @Override
    public boolean isExpectedStartArrayToken() {
        /*SL:173*/return this._currToken == JsonToken.START_ARRAY;
    }
    
    @Override
    public boolean isExpectedStartObjectToken() {
        /*SL:174*/return this._currToken == JsonToken.START_OBJECT;
    }
    
    @Override
    public JsonLocation getCurrentLocation() {
        /*SL:176*/return this.delegate.getCurrentLocation();
    }
    
    @Override
    public JsonStreamContext getParsingContext() {
        /*SL:180*/return this._filterContext();
    }
    
    @Override
    public String getCurrentName() throws IOException {
        final JsonStreamContext v0 = /*EL:186*/this._filterContext();
        /*SL:187*/if (this._currToken == JsonToken.START_OBJECT || this._currToken == JsonToken.START_ARRAY) {
            final JsonStreamContext v = /*EL:188*/v0.getParent();
            /*SL:189*/return (v == null) ? null : v.getCurrentName();
        }
        /*SL:191*/return v0.getCurrentName();
    }
    
    @Override
    public void clearCurrentToken() {
        /*SL:202*/if (this._currToken != null) {
            /*SL:203*/this._lastClearedToken = this._currToken;
            /*SL:204*/this._currToken = null;
        }
    }
    
    @Override
    public JsonToken getLastClearedToken() {
        /*SL:209*/return this._lastClearedToken;
    }
    
    @Override
    public void overrideCurrentName(final String a1) {
        /*SL:217*/throw new UnsupportedOperationException("Can not currently override name during filtering read");
    }
    
    @Override
    public JsonToken nextToken() throws IOException {
        /*SL:240*/if (!this._allowMultipleMatches && this._currToken != null && this._exposedContext == null && this._currToken.isScalarValue() && !this._headContext.isStartHandled() && !this._includePath && this._itemFilter == TokenFilter.INCLUDE_ALL) {
            /*SL:242*/return this._currToken = null;
        }
        TokenFilterContext v0 = /*EL:246*/this._exposedContext;
        /*SL:248*/if (v0 != null) {
            while (true) {
                JsonToken v = /*EL:250*/v0.nextTokenToRead();
                /*SL:251*/if (v != null) {
                    /*SL:253*/return this._currToken = v;
                }
                /*SL:256*/if (v0 == this._headContext) {
                    /*SL:257*/this._exposedContext = null;
                    /*SL:258*/if (v0.inArray()) {
                        /*SL:259*/v = this.delegate.getCurrentToken();
                        /*SL:263*/return this._currToken = v;
                    }
                    break;
                }
                else {
                    /*SL:278*/v0 = this._headContext.findChildOf(v0);
                    /*SL:280*/if ((this._exposedContext = v0) == null) {
                        /*SL:281*/throw this._constructError("Unexpected problem: chain of filtered context broken");
                    }
                    /*SL:283*/continue;
                }
            }
        }
        JsonToken v = /*EL:287*/this.delegate.nextToken();
        /*SL:288*/if (v == null) {
            /*SL:291*/return this._currToken = v;
        }
        /*SL:297*/switch (v.id()) {
            case 3: {
                TokenFilter v2 = /*EL:299*/this._itemFilter;
                /*SL:300*/if (v2 == TokenFilter.INCLUDE_ALL) {
                    /*SL:301*/this._headContext = this._headContext.createChildArrayContext(v2, true);
                    /*SL:302*/return this._currToken = v;
                }
                /*SL:304*/if (v2 == null) {
                    /*SL:305*/this.delegate.skipChildren();
                    /*SL:306*/break;
                }
                /*SL:309*/v2 = this._headContext.checkValue(v2);
                /*SL:310*/if (v2 == null) {
                    /*SL:311*/this.delegate.skipChildren();
                    /*SL:312*/break;
                }
                /*SL:314*/if (v2 != TokenFilter.INCLUDE_ALL) {
                    /*SL:315*/v2 = v2.filterStartArray();
                }
                /*SL:318*/if ((this._itemFilter = v2) == TokenFilter.INCLUDE_ALL) {
                    /*SL:319*/this._headContext = this._headContext.createChildArrayContext(v2, true);
                    /*SL:320*/return this._currToken = v;
                }
                /*SL:322*/this._headContext = this._headContext.createChildArrayContext(v2, false);
                /*SL:325*/if (!this._includePath) {
                    break;
                }
                /*SL:326*/v = this._nextTokenWithBuffering(this._headContext);
                /*SL:327*/if (v != null) {
                    /*SL:329*/return this._currToken = v;
                }
                break;
            }
            case 1: {
                TokenFilter v2 = /*EL:335*/this._itemFilter;
                /*SL:336*/if (v2 == TokenFilter.INCLUDE_ALL) {
                    /*SL:337*/this._headContext = this._headContext.createChildObjectContext(v2, true);
                    /*SL:338*/return this._currToken = v;
                }
                /*SL:340*/if (v2 == null) {
                    /*SL:341*/this.delegate.skipChildren();
                    /*SL:342*/break;
                }
                /*SL:345*/v2 = this._headContext.checkValue(v2);
                /*SL:346*/if (v2 == null) {
                    /*SL:347*/this.delegate.skipChildren();
                    /*SL:348*/break;
                }
                /*SL:350*/if (v2 != TokenFilter.INCLUDE_ALL) {
                    /*SL:351*/v2 = v2.filterStartObject();
                }
                /*SL:354*/if ((this._itemFilter = v2) == TokenFilter.INCLUDE_ALL) {
                    /*SL:355*/this._headContext = this._headContext.createChildObjectContext(v2, true);
                    /*SL:356*/return this._currToken = v;
                }
                /*SL:358*/this._headContext = this._headContext.createChildObjectContext(v2, false);
                /*SL:360*/if (!this._includePath) {
                    break;
                }
                /*SL:361*/v = this._nextTokenWithBuffering(this._headContext);
                /*SL:362*/if (v != null) {
                    /*SL:364*/return this._currToken = v;
                }
                break;
            }
            case 2:
            case 4: {
                final boolean v3 = /*EL:374*/this._headContext.isStartHandled();
                final TokenFilter v2 = /*EL:375*/this._headContext.getFilter();
                /*SL:376*/if (v2 != null && v2 != TokenFilter.INCLUDE_ALL) {
                    /*SL:377*/v2.filterFinishArray();
                }
                /*SL:379*/this._headContext = this._headContext.getParent();
                /*SL:380*/this._itemFilter = this._headContext.getFilter();
                /*SL:381*/if (v3) {
                    /*SL:382*/return this._currToken = v;
                }
                /*SL:385*/break;
            }
            case 5: {
                final String v4 = /*EL:389*/this.delegate.getCurrentName();
                TokenFilter v2 = /*EL:391*/this._headContext.setFieldName(v4);
                /*SL:392*/if (v2 == TokenFilter.INCLUDE_ALL) {
                    /*SL:393*/this._itemFilter = v2;
                    /*SL:397*/if (!this._includePath && this._includeImmediateParent && !this._headContext.isStartHandled()) {
                        /*SL:398*/v = this._headContext.nextTokenToRead();
                        /*SL:399*/this._exposedContext = this._headContext;
                    }
                    /*SL:402*/return this._currToken = v;
                }
                /*SL:404*/if (v2 == null) {
                    /*SL:405*/this.delegate.nextToken();
                    /*SL:406*/this.delegate.skipChildren();
                    /*SL:407*/break;
                }
                /*SL:409*/v2 = v2.includeProperty(v4);
                /*SL:410*/if (v2 == null) {
                    /*SL:411*/this.delegate.nextToken();
                    /*SL:412*/this.delegate.skipChildren();
                    /*SL:413*/break;
                }
                /*SL:416*/if ((this._itemFilter = v2) == TokenFilter.INCLUDE_ALL) {
                    /*SL:417*/if (this._verifyAllowedMatches()) {
                        /*SL:418*/if (this._includePath) {
                            /*SL:419*/return this._currToken = v;
                        }
                    }
                    else {
                        /*SL:422*/this.delegate.nextToken();
                        /*SL:423*/this.delegate.skipChildren();
                    }
                }
                /*SL:426*/if (!this._includePath) {
                    break;
                }
                /*SL:427*/v = this._nextTokenWithBuffering(this._headContext);
                /*SL:428*/if (v != null) {
                    /*SL:430*/return this._currToken = v;
                }
                break;
            }
            default: {
                TokenFilter v2 = /*EL:437*/this._itemFilter;
                /*SL:438*/if (v2 == TokenFilter.INCLUDE_ALL) {
                    /*SL:439*/return this._currToken = v;
                }
                /*SL:441*/if (v2 == null) {
                    break;
                }
                /*SL:442*/v2 = this._headContext.checkValue(v2);
                /*SL:443*/if ((v2 == TokenFilter.INCLUDE_ALL || (v2 != null && v2.includeValue(this.delegate))) && /*EL:445*/this._verifyAllowedMatches()) {
                    /*SL:446*/return this._currToken = v;
                }
                break;
            }
        }
        /*SL:455*/return this._nextToken2();
    }
    
    protected final JsonToken _nextToken2() throws IOException {
        while (true) {
            JsonToken currToken = /*EL:468*/this.delegate.nextToken();
            /*SL:469*/if (currToken == null) {
                /*SL:471*/return this._currToken = currToken;
            }
            /*SL:475*/switch (currToken.id()) {
                case 3: {
                    TokenFilter v0 = /*EL:477*/this._itemFilter;
                    /*SL:478*/if (v0 == TokenFilter.INCLUDE_ALL) {
                        /*SL:479*/this._headContext = this._headContext.createChildArrayContext(v0, true);
                        /*SL:480*/return this._currToken = currToken;
                    }
                    /*SL:482*/if (v0 == null) {
                        /*SL:483*/this.delegate.skipChildren();
                        /*SL:484*/continue;
                    }
                    /*SL:487*/v0 = this._headContext.checkValue(v0);
                    /*SL:488*/if (v0 == null) {
                        /*SL:489*/this.delegate.skipChildren();
                        /*SL:490*/continue;
                    }
                    /*SL:492*/if (v0 != TokenFilter.INCLUDE_ALL) {
                        /*SL:493*/v0 = v0.filterStartArray();
                    }
                    /*SL:496*/if ((this._itemFilter = v0) == TokenFilter.INCLUDE_ALL) {
                        /*SL:497*/this._headContext = this._headContext.createChildArrayContext(v0, true);
                        /*SL:498*/return this._currToken = currToken;
                    }
                    /*SL:500*/this._headContext = this._headContext.createChildArrayContext(v0, false);
                    /*SL:502*/if (!this._includePath) {
                        continue;
                    }
                    /*SL:503*/currToken = this._nextTokenWithBuffering(this._headContext);
                    /*SL:504*/if (currToken != null) {
                        /*SL:506*/return this._currToken = currToken;
                    }
                    continue;
                }
                case 1: {
                    TokenFilter v0 = /*EL:512*/this._itemFilter;
                    /*SL:513*/if (v0 == TokenFilter.INCLUDE_ALL) {
                        /*SL:514*/this._headContext = this._headContext.createChildObjectContext(v0, true);
                        /*SL:515*/return this._currToken = currToken;
                    }
                    /*SL:517*/if (v0 == null) {
                        /*SL:518*/this.delegate.skipChildren();
                        /*SL:519*/continue;
                    }
                    /*SL:522*/v0 = this._headContext.checkValue(v0);
                    /*SL:523*/if (v0 == null) {
                        /*SL:524*/this.delegate.skipChildren();
                        /*SL:525*/continue;
                    }
                    /*SL:527*/if (v0 != TokenFilter.INCLUDE_ALL) {
                        /*SL:528*/v0 = v0.filterStartObject();
                    }
                    /*SL:531*/if ((this._itemFilter = v0) == TokenFilter.INCLUDE_ALL) {
                        /*SL:532*/this._headContext = this._headContext.createChildObjectContext(v0, true);
                        /*SL:533*/return this._currToken = currToken;
                    }
                    /*SL:535*/this._headContext = this._headContext.createChildObjectContext(v0, false);
                    /*SL:536*/if (!this._includePath) {
                        continue;
                    }
                    /*SL:537*/currToken = this._nextTokenWithBuffering(this._headContext);
                    /*SL:538*/if (currToken != null) {
                        /*SL:540*/return this._currToken = currToken;
                    }
                    continue;
                }
                case 2:
                case 4: {
                    final boolean v = /*EL:548*/this._headContext.isStartHandled();
                    final TokenFilter v0 = /*EL:549*/this._headContext.getFilter();
                    /*SL:550*/if (v0 != null && v0 != TokenFilter.INCLUDE_ALL) {
                        /*SL:551*/v0.filterFinishArray();
                    }
                    /*SL:553*/this._headContext = this._headContext.getParent();
                    /*SL:554*/this._itemFilter = this._headContext.getFilter();
                    /*SL:555*/if (v) {
                        /*SL:556*/return this._currToken = currToken;
                    }
                    /*SL:559*/continue;
                }
                case 5: {
                    final String v2 = /*EL:563*/this.delegate.getCurrentName();
                    TokenFilter v0 = /*EL:564*/this._headContext.setFieldName(v2);
                    /*SL:565*/if (v0 == TokenFilter.INCLUDE_ALL) {
                        /*SL:566*/this._itemFilter = v0;
                        /*SL:567*/return this._currToken = currToken;
                    }
                    /*SL:569*/if (v0 == null) {
                        /*SL:570*/this.delegate.nextToken();
                        /*SL:571*/this.delegate.skipChildren();
                        /*SL:572*/continue;
                    }
                    /*SL:574*/v0 = v0.includeProperty(v2);
                    /*SL:575*/if (v0 == null) {
                        /*SL:576*/this.delegate.nextToken();
                        /*SL:577*/this.delegate.skipChildren();
                        /*SL:578*/continue;
                    }
                    /*SL:581*/if ((this._itemFilter = v0) != TokenFilter.INCLUDE_ALL) {
                        /*SL:588*/if (!this._includePath) {
                            continue;
                        }
                        /*SL:589*/currToken = this._nextTokenWithBuffering(this._headContext);
                        /*SL:590*/if (currToken != null) {
                            /*SL:592*/return this._currToken = currToken;
                        }
                        continue;
                        /*SL:596*/continue;
                    }
                    if (this._verifyAllowedMatches() && this._includePath) {
                        return this._currToken = currToken;
                    }
                    continue;
                }
                default: {
                    TokenFilter v0 = /*EL:599*/this._itemFilter;
                    /*SL:600*/if (v0 == TokenFilter.INCLUDE_ALL) {
                        /*SL:601*/return this._currToken = currToken;
                    }
                    /*SL:603*/if (v0 == null) {
                        continue;
                    }
                    /*SL:604*/v0 = this._headContext.checkValue(v0);
                    /*SL:605*/if ((v0 == TokenFilter.INCLUDE_ALL || (v0 != null && v0.includeValue(this.delegate))) && /*EL:607*/this._verifyAllowedMatches()) {
                        /*SL:608*/return this._currToken = currToken;
                    }
                    continue;
                    /*SL:615*/continue;
                }
            }
        }
    }
    
    protected final JsonToken _nextTokenWithBuffering(final TokenFilterContext v-3) throws IOException {
        while (true) {
            final JsonToken nextToken = /*EL:626*/this.delegate.nextToken();
            /*SL:627*/if (nextToken == null) {
                /*SL:628*/return nextToken;
            }
            /*SL:636*/switch (nextToken.id()) {
                case 3: {
                    TokenFilter a2 = /*EL:638*/this._headContext.checkValue(this._itemFilter);
                    /*SL:639*/if (a2 == null) {
                        /*SL:640*/this.delegate.skipChildren();
                        /*SL:641*/continue;
                    }
                    /*SL:643*/if (a2 != TokenFilter.INCLUDE_ALL) {
                        /*SL:644*/a2 = a2.filterStartArray();
                    }
                    /*SL:647*/if ((this._itemFilter = a2) == TokenFilter.INCLUDE_ALL) {
                        /*SL:648*/this._headContext = this._headContext.createChildArrayContext(a2, true);
                        /*SL:649*/return this._nextBuffered(v-3);
                    }
                    /*SL:651*/this._headContext = this._headContext.createChildArrayContext(a2, false);
                    /*SL:652*/continue;
                }
                case 1: {
                    TokenFilter a2 = /*EL:655*/this._itemFilter;
                    /*SL:656*/if (a2 == TokenFilter.INCLUDE_ALL) {
                        /*SL:657*/this._headContext = this._headContext.createChildObjectContext(a2, true);
                        /*SL:658*/return nextToken;
                    }
                    /*SL:660*/if (a2 == null) {
                        /*SL:661*/this.delegate.skipChildren();
                        /*SL:662*/continue;
                    }
                    /*SL:665*/a2 = this._headContext.checkValue(a2);
                    /*SL:666*/if (a2 == null) {
                        /*SL:667*/this.delegate.skipChildren();
                        /*SL:668*/continue;
                    }
                    /*SL:670*/if (a2 != TokenFilter.INCLUDE_ALL) {
                        /*SL:671*/a2 = a2.filterStartObject();
                    }
                    /*SL:674*/if ((this._itemFilter = a2) == TokenFilter.INCLUDE_ALL) {
                        /*SL:675*/this._headContext = this._headContext.createChildObjectContext(a2, true);
                        /*SL:676*/return this._nextBuffered(v-3);
                    }
                    /*SL:678*/this._headContext = this._headContext.createChildObjectContext(a2, false);
                    /*SL:679*/continue;
                }
                case 2:
                case 4: {
                    final TokenFilter a2 = /*EL:686*/this._headContext.getFilter();
                    /*SL:687*/if (a2 != null && a2 != TokenFilter.INCLUDE_ALL) {
                        /*SL:688*/a2.filterFinishArray();
                    }
                    final boolean a1 = /*EL:690*/this._headContext == v-3;
                    final boolean v1 = /*EL:691*/a1 && this._headContext.isStartHandled();
                    /*SL:693*/this._headContext = this._headContext.getParent();
                    /*SL:694*/this._itemFilter = this._headContext.getFilter();
                    /*SL:696*/if (v1) {
                        /*SL:697*/return nextToken;
                    }
                    /*SL:700*/continue;
                }
                case 5: {
                    final String v2 = /*EL:704*/this.delegate.getCurrentName();
                    TokenFilter a2 = /*EL:705*/this._headContext.setFieldName(v2);
                    /*SL:706*/if (a2 == TokenFilter.INCLUDE_ALL) {
                        /*SL:707*/this._itemFilter = a2;
                        /*SL:708*/return this._nextBuffered(v-3);
                    }
                    /*SL:710*/if (a2 == null) {
                        /*SL:711*/this.delegate.nextToken();
                        /*SL:712*/this.delegate.skipChildren();
                        /*SL:713*/continue;
                    }
                    /*SL:715*/a2 = a2.includeProperty(v2);
                    /*SL:716*/if (a2 == null) {
                        /*SL:717*/this.delegate.nextToken();
                        /*SL:718*/this.delegate.skipChildren();
                        /*SL:719*/continue;
                    }
                    /*SL:722*/if ((this._itemFilter = a2) != TokenFilter.INCLUDE_ALL) {
                        continue;
                    }
                    /*SL:723*/if (this._verifyAllowedMatches()) {
                        /*SL:724*/return this._nextBuffered(v-3);
                    }
                    /*SL:728*/this._itemFilter = this._headContext.setFieldName(v2);
                    /*SL:732*/continue;
                }
                default: {
                    TokenFilter a2 = /*EL:735*/this._itemFilter;
                    /*SL:736*/if (a2 == TokenFilter.INCLUDE_ALL) {
                        /*SL:737*/return this._nextBuffered(v-3);
                    }
                    /*SL:739*/if (a2 == null) {
                        continue;
                    }
                    /*SL:740*/a2 = this._headContext.checkValue(a2);
                    /*SL:741*/if ((a2 == TokenFilter.INCLUDE_ALL || (a2 != null && a2.includeValue(this.delegate))) && /*EL:743*/this._verifyAllowedMatches()) {
                        /*SL:744*/return this._nextBuffered(v-3);
                    }
                    continue;
                }
            }
        }
    }
    
    private JsonToken _nextBuffered(final TokenFilterContext a1) throws IOException {
        /*SL:756*/this._exposedContext = a1;
        TokenFilterContext v1 = /*EL:757*/a1;
        JsonToken v2 = /*EL:758*/v1.nextTokenToRead();
        /*SL:759*/if (v2 != null) {
            /*SL:760*/return v2;
        }
        /*SL:764*/while (v1 != this._headContext) {
            /*SL:772*/v1 = this._exposedContext.findChildOf(v1);
            /*SL:774*/if ((this._exposedContext = v1) == null) {
                /*SL:775*/throw this._constructError("Unexpected problem: chain of filtered context broken");
            }
            /*SL:777*/v2 = this._exposedContext.nextTokenToRead();
            /*SL:778*/if (v2 != null) {
                /*SL:779*/return v2;
            }
        }
        throw this._constructError("Internal error: failed to locate expected buffered tokens");
    }
    
    private final boolean _verifyAllowedMatches() throws IOException {
        /*SL:785*/if (this._matchCount == 0 || this._allowMultipleMatches) {
            /*SL:786*/++this._matchCount;
            /*SL:787*/return true;
        }
        /*SL:789*/return false;
    }
    
    @Override
    public JsonToken nextValue() throws IOException {
        JsonToken v1 = /*EL:795*/this.nextToken();
        /*SL:796*/if (v1 == JsonToken.FIELD_NAME) {
            /*SL:797*/v1 = this.nextToken();
        }
        /*SL:799*/return v1;
    }
    
    @Override
    public JsonParser skipChildren() throws IOException {
        /*SL:810*/if (this._currToken != JsonToken.START_OBJECT && this._currToken != JsonToken.START_ARRAY) {
            /*SL:812*/return this;
        }
        int v0 = /*EL:814*/1;
        while (true) {
            final JsonToken v = /*EL:819*/this.nextToken();
            /*SL:820*/if (v == null) {
                /*SL:821*/return this;
            }
            /*SL:823*/if (v.isStructStart()) {
                /*SL:824*/++v0;
            }
            else {
                /*SL:825*/if (v.isStructEnd() && /*EL:826*/--v0 == 0) {
                    /*SL:827*/return this;
                }
                continue;
            }
        }
    }
    
    @Override
    public String getText() throws IOException {
        /*SL:839*/return this.delegate.getText();
    }
    
    @Override
    public boolean hasTextCharacters() {
        /*SL:840*/return this.delegate.hasTextCharacters();
    }
    
    @Override
    public char[] getTextCharacters() throws IOException {
        /*SL:841*/return this.delegate.getTextCharacters();
    }
    
    @Override
    public int getTextLength() throws IOException {
        /*SL:842*/return this.delegate.getTextLength();
    }
    
    @Override
    public int getTextOffset() throws IOException {
        /*SL:843*/return this.delegate.getTextOffset();
    }
    
    @Override
    public BigInteger getBigIntegerValue() throws IOException {
        /*SL:852*/return this.delegate.getBigIntegerValue();
    }
    
    @Override
    public boolean getBooleanValue() throws IOException {
        /*SL:855*/return this.delegate.getBooleanValue();
    }
    
    @Override
    public byte getByteValue() throws IOException {
        /*SL:858*/return this.delegate.getByteValue();
    }
    
    @Override
    public short getShortValue() throws IOException {
        /*SL:861*/return this.delegate.getShortValue();
    }
    
    @Override
    public BigDecimal getDecimalValue() throws IOException {
        /*SL:864*/return this.delegate.getDecimalValue();
    }
    
    @Override
    public double getDoubleValue() throws IOException {
        /*SL:867*/return this.delegate.getDoubleValue();
    }
    
    @Override
    public float getFloatValue() throws IOException {
        /*SL:870*/return this.delegate.getFloatValue();
    }
    
    @Override
    public int getIntValue() throws IOException {
        /*SL:873*/return this.delegate.getIntValue();
    }
    
    @Override
    public long getLongValue() throws IOException {
        /*SL:876*/return this.delegate.getLongValue();
    }
    
    @Override
    public NumberType getNumberType() throws IOException {
        /*SL:879*/return this.delegate.getNumberType();
    }
    
    @Override
    public Number getNumberValue() throws IOException {
        /*SL:882*/return this.delegate.getNumberValue();
    }
    
    @Override
    public int getValueAsInt() throws IOException {
        /*SL:890*/return this.delegate.getValueAsInt();
    }
    
    @Override
    public int getValueAsInt(final int a1) throws IOException {
        /*SL:891*/return this.delegate.getValueAsInt(a1);
    }
    
    @Override
    public long getValueAsLong() throws IOException {
        /*SL:892*/return this.delegate.getValueAsLong();
    }
    
    @Override
    public long getValueAsLong(final long a1) throws IOException {
        /*SL:893*/return this.delegate.getValueAsLong(a1);
    }
    
    @Override
    public double getValueAsDouble() throws IOException {
        /*SL:894*/return this.delegate.getValueAsDouble();
    }
    
    @Override
    public double getValueAsDouble(final double a1) throws IOException {
        /*SL:895*/return this.delegate.getValueAsDouble(a1);
    }
    
    @Override
    public boolean getValueAsBoolean() throws IOException {
        /*SL:896*/return this.delegate.getValueAsBoolean();
    }
    
    @Override
    public boolean getValueAsBoolean(final boolean a1) throws IOException {
        /*SL:897*/return this.delegate.getValueAsBoolean(a1);
    }
    
    @Override
    public String getValueAsString() throws IOException {
        /*SL:898*/return this.delegate.getValueAsString();
    }
    
    @Override
    public String getValueAsString(final String a1) throws IOException {
        /*SL:899*/return this.delegate.getValueAsString(a1);
    }
    
    @Override
    public Object getEmbeddedObject() throws IOException {
        /*SL:907*/return this.delegate.getEmbeddedObject();
    }
    
    @Override
    public byte[] getBinaryValue(final Base64Variant a1) throws IOException {
        /*SL:908*/return this.delegate.getBinaryValue(a1);
    }
    
    @Override
    public int readBinaryValue(final Base64Variant a1, final OutputStream a2) throws IOException {
        /*SL:909*/return this.delegate.readBinaryValue(a1, a2);
    }
    
    @Override
    public JsonLocation getTokenLocation() {
        /*SL:910*/return this.delegate.getTokenLocation();
    }
    
    protected JsonStreamContext _filterContext() {
        /*SL:919*/if (this._exposedContext != null) {
            /*SL:920*/return this._exposedContext;
        }
        /*SL:922*/return this._headContext;
    }
}

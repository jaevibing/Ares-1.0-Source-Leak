package com.fasterxml.jackson.core.filter;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.io.InputStream;
import com.fasterxml.jackson.core.Base64Variant;
import com.fasterxml.jackson.core.SerializableString;
import java.io.IOException;
import com.fasterxml.jackson.core.JsonStreamContext;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.util.JsonGeneratorDelegate;

public class FilteringGeneratorDelegate extends JsonGeneratorDelegate
{
    protected TokenFilter rootFilter;
    protected boolean _allowMultipleMatches;
    protected boolean _includePath;
    @Deprecated
    protected boolean _includeImmediateParent;
    protected TokenFilterContext _filterContext;
    protected TokenFilter _itemFilter;
    protected int _matchCount;
    
    public FilteringGeneratorDelegate(final JsonGenerator a1, final TokenFilter a2, final boolean a3, final boolean a4) {
        super(a1, false);
        this.rootFilter = a2;
        this._itemFilter = a2;
        this._filterContext = TokenFilterContext.createRootContext(a2);
        this._includePath = a3;
        this._allowMultipleMatches = a4;
    }
    
    public TokenFilter getFilter() {
        /*SL:111*/return this.rootFilter;
    }
    
    public JsonStreamContext getFilterContext() {
        /*SL:114*/return this._filterContext;
    }
    
    public int getMatchCount() {
        /*SL:122*/return this._matchCount;
    }
    
    @Override
    public JsonStreamContext getOutputContext() {
        /*SL:137*/return this._filterContext;
    }
    
    @Override
    public void writeStartArray() throws IOException {
        /*SL:150*/if (this._itemFilter == null) {
            /*SL:151*/this._filterContext = this._filterContext.createChildArrayContext(null, false);
            /*SL:152*/return;
        }
        /*SL:154*/if (this._itemFilter == TokenFilter.INCLUDE_ALL) {
            /*SL:155*/this._filterContext = this._filterContext.createChildArrayContext(this._itemFilter, true);
            /*SL:156*/this.delegate.writeStartArray();
            /*SL:157*/return;
        }
        /*SL:160*/this._itemFilter = this._filterContext.checkValue(this._itemFilter);
        /*SL:161*/if (this._itemFilter == null) {
            /*SL:162*/this._filterContext = this._filterContext.createChildArrayContext(null, false);
            /*SL:163*/return;
        }
        /*SL:165*/if (this._itemFilter != TokenFilter.INCLUDE_ALL) {
            /*SL:166*/this._itemFilter = this._itemFilter.filterStartArray();
        }
        /*SL:168*/if (this._itemFilter == TokenFilter.INCLUDE_ALL) {
            /*SL:169*/this._checkParentPath();
            /*SL:170*/this._filterContext = this._filterContext.createChildArrayContext(this._itemFilter, true);
            /*SL:171*/this.delegate.writeStartArray();
        }
        else {
            /*SL:173*/this._filterContext = this._filterContext.createChildArrayContext(this._itemFilter, false);
        }
    }
    
    @Override
    public void writeStartArray(final int a1) throws IOException {
        /*SL:180*/if (this._itemFilter == null) {
            /*SL:181*/this._filterContext = this._filterContext.createChildArrayContext(null, false);
            /*SL:182*/return;
        }
        /*SL:184*/if (this._itemFilter == TokenFilter.INCLUDE_ALL) {
            /*SL:185*/this._filterContext = this._filterContext.createChildArrayContext(this._itemFilter, true);
            /*SL:186*/this.delegate.writeStartArray(a1);
            /*SL:187*/return;
        }
        /*SL:189*/this._itemFilter = this._filterContext.checkValue(this._itemFilter);
        /*SL:190*/if (this._itemFilter == null) {
            /*SL:191*/this._filterContext = this._filterContext.createChildArrayContext(null, false);
            /*SL:192*/return;
        }
        /*SL:194*/if (this._itemFilter != TokenFilter.INCLUDE_ALL) {
            /*SL:195*/this._itemFilter = this._itemFilter.filterStartArray();
        }
        /*SL:197*/if (this._itemFilter == TokenFilter.INCLUDE_ALL) {
            /*SL:198*/this._checkParentPath();
            /*SL:199*/this._filterContext = this._filterContext.createChildArrayContext(this._itemFilter, true);
            /*SL:200*/this.delegate.writeStartArray(a1);
        }
        else {
            /*SL:202*/this._filterContext = this._filterContext.createChildArrayContext(this._itemFilter, false);
        }
    }
    
    @Override
    public void writeEndArray() throws IOException {
        /*SL:209*/this._filterContext = this._filterContext.closeArray(this.delegate);
        /*SL:211*/if (this._filterContext != null) {
            /*SL:212*/this._itemFilter = this._filterContext.getFilter();
        }
    }
    
    @Override
    public void writeStartObject() throws IOException {
        /*SL:219*/if (this._itemFilter == null) {
            /*SL:220*/this._filterContext = this._filterContext.createChildObjectContext(this._itemFilter, false);
            /*SL:221*/return;
        }
        /*SL:223*/if (this._itemFilter == TokenFilter.INCLUDE_ALL) {
            /*SL:224*/this._filterContext = this._filterContext.createChildObjectContext(this._itemFilter, true);
            /*SL:225*/this.delegate.writeStartObject();
            /*SL:226*/return;
        }
        TokenFilter v1 = /*EL:229*/this._filterContext.checkValue(this._itemFilter);
        /*SL:230*/if (v1 == null) {
            /*SL:231*/return;
        }
        /*SL:234*/if (v1 != TokenFilter.INCLUDE_ALL) {
            /*SL:235*/v1 = v1.filterStartObject();
        }
        /*SL:237*/if (v1 == TokenFilter.INCLUDE_ALL) {
            /*SL:238*/this._checkParentPath();
            /*SL:239*/this._filterContext = this._filterContext.createChildObjectContext(v1, true);
            /*SL:240*/this.delegate.writeStartObject();
        }
        else {
            /*SL:242*/this._filterContext = this._filterContext.createChildObjectContext(v1, false);
        }
    }
    
    @Override
    public void writeStartObject(final Object a1) throws IOException {
        /*SL:249*/if (this._itemFilter == null) {
            /*SL:250*/this._filterContext = this._filterContext.createChildObjectContext(this._itemFilter, false);
            /*SL:251*/return;
        }
        /*SL:253*/if (this._itemFilter == TokenFilter.INCLUDE_ALL) {
            /*SL:254*/this._filterContext = this._filterContext.createChildObjectContext(this._itemFilter, true);
            /*SL:255*/this.delegate.writeStartObject(a1);
            /*SL:256*/return;
        }
        TokenFilter v1 = /*EL:259*/this._filterContext.checkValue(this._itemFilter);
        /*SL:260*/if (v1 == null) {
            /*SL:261*/return;
        }
        /*SL:264*/if (v1 != TokenFilter.INCLUDE_ALL) {
            /*SL:265*/v1 = v1.filterStartObject();
        }
        /*SL:267*/if (v1 == TokenFilter.INCLUDE_ALL) {
            /*SL:268*/this._checkParentPath();
            /*SL:269*/this._filterContext = this._filterContext.createChildObjectContext(v1, true);
            /*SL:270*/this.delegate.writeStartObject(a1);
        }
        else {
            /*SL:272*/this._filterContext = this._filterContext.createChildObjectContext(v1, false);
        }
    }
    
    @Override
    public void writeEndObject() throws IOException {
        /*SL:279*/this._filterContext = this._filterContext.closeObject(this.delegate);
        /*SL:280*/if (this._filterContext != null) {
            /*SL:281*/this._itemFilter = this._filterContext.getFilter();
        }
    }
    
    @Override
    public void writeFieldName(final String a1) throws IOException {
        TokenFilter v1 = /*EL:288*/this._filterContext.setFieldName(a1);
        /*SL:289*/if (v1 == null) {
            /*SL:290*/this._itemFilter = null;
            /*SL:291*/return;
        }
        /*SL:293*/if (v1 == TokenFilter.INCLUDE_ALL) {
            /*SL:294*/this._itemFilter = v1;
            /*SL:295*/this.delegate.writeFieldName(a1);
            /*SL:296*/return;
        }
        /*SL:298*/v1 = v1.includeProperty(a1);
        /*SL:300*/if ((this._itemFilter = v1) == TokenFilter.INCLUDE_ALL) {
            /*SL:301*/this._checkPropertyParentPath();
        }
    }
    
    @Override
    public void writeFieldName(final SerializableString a1) throws IOException {
        TokenFilter v1 = /*EL:308*/this._filterContext.setFieldName(a1.getValue());
        /*SL:309*/if (v1 == null) {
            /*SL:310*/this._itemFilter = null;
            /*SL:311*/return;
        }
        /*SL:313*/if (v1 == TokenFilter.INCLUDE_ALL) {
            /*SL:314*/this._itemFilter = v1;
            /*SL:315*/this.delegate.writeFieldName(a1);
            /*SL:316*/return;
        }
        /*SL:318*/v1 = v1.includeProperty(a1.getValue());
        /*SL:320*/if ((this._itemFilter = v1) == TokenFilter.INCLUDE_ALL) {
            /*SL:321*/this._checkPropertyParentPath();
        }
    }
    
    @Override
    public void writeString(final String v2) throws IOException {
        /*SL:334*/if (this._itemFilter == null) {
            /*SL:335*/return;
        }
        /*SL:337*/if (this._itemFilter != TokenFilter.INCLUDE_ALL) {
            final TokenFilter a1 = /*EL:338*/this._filterContext.checkValue(this._itemFilter);
            /*SL:339*/if (a1 == null) {
                /*SL:340*/return;
            }
            /*SL:342*/if (a1 != TokenFilter.INCLUDE_ALL && /*EL:343*/!a1.includeString(v2)) {
                /*SL:344*/return;
            }
            /*SL:347*/this._checkParentPath();
        }
        /*SL:349*/this.delegate.writeString(v2);
    }
    
    @Override
    public void writeString(final char[] v1, final int v2, final int v3) throws IOException {
        /*SL:355*/if (this._itemFilter == null) {
            /*SL:356*/return;
        }
        /*SL:358*/if (this._itemFilter != TokenFilter.INCLUDE_ALL) {
            final String a1 = /*EL:359*/new String(v1, v2, v3);
            final TokenFilter a2 = /*EL:360*/this._filterContext.checkValue(this._itemFilter);
            /*SL:361*/if (a2 == null) {
                /*SL:362*/return;
            }
            /*SL:364*/if (a2 != TokenFilter.INCLUDE_ALL && /*EL:365*/!a2.includeString(a1)) {
                /*SL:366*/return;
            }
            /*SL:369*/this._checkParentPath();
        }
        /*SL:371*/this.delegate.writeString(v1, v2, v3);
    }
    
    @Override
    public void writeString(final SerializableString v2) throws IOException {
        /*SL:377*/if (this._itemFilter == null) {
            /*SL:378*/return;
        }
        /*SL:380*/if (this._itemFilter != TokenFilter.INCLUDE_ALL) {
            final TokenFilter a1 = /*EL:381*/this._filterContext.checkValue(this._itemFilter);
            /*SL:382*/if (a1 == null) {
                /*SL:383*/return;
            }
            /*SL:385*/if (a1 != TokenFilter.INCLUDE_ALL && /*EL:386*/!a1.includeString(v2.getValue())) {
                /*SL:387*/return;
            }
            /*SL:390*/this._checkParentPath();
        }
        /*SL:392*/this.delegate.writeString(v2);
    }
    
    @Override
    public void writeRawUTF8String(final byte[] a1, final int a2, final int a3) throws IOException {
        /*SL:398*/if (this._checkRawValueWrite()) {
            /*SL:399*/this.delegate.writeRawUTF8String(a1, a2, a3);
        }
    }
    
    @Override
    public void writeUTF8String(final byte[] a1, final int a2, final int a3) throws IOException {
        /*SL:407*/if (this._checkRawValueWrite()) {
            /*SL:408*/this.delegate.writeUTF8String(a1, a2, a3);
        }
    }
    
    @Override
    public void writeRaw(final String a1) throws IOException {
        /*SL:421*/if (this._checkRawValueWrite()) {
            /*SL:422*/this.delegate.writeRaw(a1);
        }
    }
    
    @Override
    public void writeRaw(final String a1, final int a2, final int a3) throws IOException {
        /*SL:429*/if (this._checkRawValueWrite()) {
            /*SL:430*/this.delegate.writeRaw(a1);
        }
    }
    
    @Override
    public void writeRaw(final SerializableString a1) throws IOException {
        /*SL:437*/if (this._checkRawValueWrite()) {
            /*SL:438*/this.delegate.writeRaw(a1);
        }
    }
    
    @Override
    public void writeRaw(final char[] a1, final int a2, final int a3) throws IOException {
        /*SL:445*/if (this._checkRawValueWrite()) {
            /*SL:446*/this.delegate.writeRaw(a1, a2, a3);
        }
    }
    
    @Override
    public void writeRaw(final char a1) throws IOException {
        /*SL:453*/if (this._checkRawValueWrite()) {
            /*SL:454*/this.delegate.writeRaw(a1);
        }
    }
    
    @Override
    public void writeRawValue(final String a1) throws IOException {
        /*SL:461*/if (this._checkRawValueWrite()) {
            /*SL:462*/this.delegate.writeRaw(a1);
        }
    }
    
    @Override
    public void writeRawValue(final String a1, final int a2, final int a3) throws IOException {
        /*SL:469*/if (this._checkRawValueWrite()) {
            /*SL:470*/this.delegate.writeRaw(a1, a2, a3);
        }
    }
    
    @Override
    public void writeRawValue(final char[] a1, final int a2, final int a3) throws IOException {
        /*SL:477*/if (this._checkRawValueWrite()) {
            /*SL:478*/this.delegate.writeRaw(a1, a2, a3);
        }
    }
    
    @Override
    public void writeBinary(final Base64Variant a1, final byte[] a2, final int a3, final int a4) throws IOException {
        /*SL:485*/if (this._checkBinaryWrite()) {
            /*SL:486*/this.delegate.writeBinary(a1, a2, a3, a4);
        }
    }
    
    @Override
    public int writeBinary(final Base64Variant a1, final InputStream a2, final int a3) throws IOException {
        /*SL:493*/if (this._checkBinaryWrite()) {
            /*SL:494*/return this.delegate.writeBinary(a1, a2, a3);
        }
        /*SL:496*/return -1;
    }
    
    @Override
    public void writeNumber(final short v2) throws IOException {
        /*SL:508*/if (this._itemFilter == null) {
            /*SL:509*/return;
        }
        /*SL:511*/if (this._itemFilter != TokenFilter.INCLUDE_ALL) {
            final TokenFilter a1 = /*EL:512*/this._filterContext.checkValue(this._itemFilter);
            /*SL:513*/if (a1 == null) {
                /*SL:514*/return;
            }
            /*SL:516*/if (a1 != TokenFilter.INCLUDE_ALL && /*EL:517*/!a1.includeNumber(v2)) {
                /*SL:518*/return;
            }
            /*SL:521*/this._checkParentPath();
        }
        /*SL:523*/this.delegate.writeNumber(v2);
    }
    
    @Override
    public void writeNumber(final int v2) throws IOException {
        /*SL:529*/if (this._itemFilter == null) {
            /*SL:530*/return;
        }
        /*SL:532*/if (this._itemFilter != TokenFilter.INCLUDE_ALL) {
            final TokenFilter a1 = /*EL:533*/this._filterContext.checkValue(this._itemFilter);
            /*SL:534*/if (a1 == null) {
                /*SL:535*/return;
            }
            /*SL:537*/if (a1 != TokenFilter.INCLUDE_ALL && /*EL:538*/!a1.includeNumber(v2)) {
                /*SL:539*/return;
            }
            /*SL:542*/this._checkParentPath();
        }
        /*SL:544*/this.delegate.writeNumber(v2);
    }
    
    @Override
    public void writeNumber(final long v2) throws IOException {
        /*SL:550*/if (this._itemFilter == null) {
            /*SL:551*/return;
        }
        /*SL:553*/if (this._itemFilter != TokenFilter.INCLUDE_ALL) {
            final TokenFilter a1 = /*EL:554*/this._filterContext.checkValue(this._itemFilter);
            /*SL:555*/if (a1 == null) {
                /*SL:556*/return;
            }
            /*SL:558*/if (a1 != TokenFilter.INCLUDE_ALL && /*EL:559*/!a1.includeNumber(v2)) {
                /*SL:560*/return;
            }
            /*SL:563*/this._checkParentPath();
        }
        /*SL:565*/this.delegate.writeNumber(v2);
    }
    
    @Override
    public void writeNumber(final BigInteger v2) throws IOException {
        /*SL:571*/if (this._itemFilter == null) {
            /*SL:572*/return;
        }
        /*SL:574*/if (this._itemFilter != TokenFilter.INCLUDE_ALL) {
            final TokenFilter a1 = /*EL:575*/this._filterContext.checkValue(this._itemFilter);
            /*SL:576*/if (a1 == null) {
                /*SL:577*/return;
            }
            /*SL:579*/if (a1 != TokenFilter.INCLUDE_ALL && /*EL:580*/!a1.includeNumber(v2)) {
                /*SL:581*/return;
            }
            /*SL:584*/this._checkParentPath();
        }
        /*SL:586*/this.delegate.writeNumber(v2);
    }
    
    @Override
    public void writeNumber(final double v2) throws IOException {
        /*SL:592*/if (this._itemFilter == null) {
            /*SL:593*/return;
        }
        /*SL:595*/if (this._itemFilter != TokenFilter.INCLUDE_ALL) {
            final TokenFilter a1 = /*EL:596*/this._filterContext.checkValue(this._itemFilter);
            /*SL:597*/if (a1 == null) {
                /*SL:598*/return;
            }
            /*SL:600*/if (a1 != TokenFilter.INCLUDE_ALL && /*EL:601*/!a1.includeNumber(v2)) {
                /*SL:602*/return;
            }
            /*SL:605*/this._checkParentPath();
        }
        /*SL:607*/this.delegate.writeNumber(v2);
    }
    
    @Override
    public void writeNumber(final float v2) throws IOException {
        /*SL:613*/if (this._itemFilter == null) {
            /*SL:614*/return;
        }
        /*SL:616*/if (this._itemFilter != TokenFilter.INCLUDE_ALL) {
            final TokenFilter a1 = /*EL:617*/this._filterContext.checkValue(this._itemFilter);
            /*SL:618*/if (a1 == null) {
                /*SL:619*/return;
            }
            /*SL:621*/if (a1 != TokenFilter.INCLUDE_ALL && /*EL:622*/!a1.includeNumber(v2)) {
                /*SL:623*/return;
            }
            /*SL:626*/this._checkParentPath();
        }
        /*SL:628*/this.delegate.writeNumber(v2);
    }
    
    @Override
    public void writeNumber(final BigDecimal v2) throws IOException {
        /*SL:634*/if (this._itemFilter == null) {
            /*SL:635*/return;
        }
        /*SL:637*/if (this._itemFilter != TokenFilter.INCLUDE_ALL) {
            final TokenFilter a1 = /*EL:638*/this._filterContext.checkValue(this._itemFilter);
            /*SL:639*/if (a1 == null) {
                /*SL:640*/return;
            }
            /*SL:642*/if (a1 != TokenFilter.INCLUDE_ALL && /*EL:643*/!a1.includeNumber(v2)) {
                /*SL:644*/return;
            }
            /*SL:647*/this._checkParentPath();
        }
        /*SL:649*/this.delegate.writeNumber(v2);
    }
    
    @Override
    public void writeNumber(final String v2) throws IOException, UnsupportedOperationException {
        /*SL:655*/if (this._itemFilter == null) {
            /*SL:656*/return;
        }
        /*SL:658*/if (this._itemFilter != TokenFilter.INCLUDE_ALL) {
            final TokenFilter a1 = /*EL:659*/this._filterContext.checkValue(this._itemFilter);
            /*SL:660*/if (a1 == null) {
                /*SL:661*/return;
            }
            /*SL:663*/if (a1 != TokenFilter.INCLUDE_ALL && /*EL:664*/!a1.includeRawValue()) {
                /*SL:665*/return;
            }
            /*SL:668*/this._checkParentPath();
        }
        /*SL:670*/this.delegate.writeNumber(v2);
    }
    
    @Override
    public void writeBoolean(final boolean v2) throws IOException {
        /*SL:676*/if (this._itemFilter == null) {
            /*SL:677*/return;
        }
        /*SL:679*/if (this._itemFilter != TokenFilter.INCLUDE_ALL) {
            final TokenFilter a1 = /*EL:680*/this._filterContext.checkValue(this._itemFilter);
            /*SL:681*/if (a1 == null) {
                /*SL:682*/return;
            }
            /*SL:684*/if (a1 != TokenFilter.INCLUDE_ALL && /*EL:685*/!a1.includeBoolean(v2)) {
                /*SL:686*/return;
            }
            /*SL:689*/this._checkParentPath();
        }
        /*SL:691*/this.delegate.writeBoolean(v2);
    }
    
    @Override
    public void writeNull() throws IOException {
        /*SL:697*/if (this._itemFilter == null) {
            /*SL:698*/return;
        }
        /*SL:700*/if (this._itemFilter != TokenFilter.INCLUDE_ALL) {
            final TokenFilter v1 = /*EL:701*/this._filterContext.checkValue(this._itemFilter);
            /*SL:702*/if (v1 == null) {
                /*SL:703*/return;
            }
            /*SL:705*/if (v1 != TokenFilter.INCLUDE_ALL && /*EL:706*/!v1.includeNull()) {
                /*SL:707*/return;
            }
            /*SL:710*/this._checkParentPath();
        }
        /*SL:712*/this.delegate.writeNull();
    }
    
    @Override
    public void writeOmittedField(final String a1) throws IOException {
        /*SL:724*/if (this._itemFilter != null) {
            /*SL:725*/this.delegate.writeOmittedField(a1);
        }
    }
    
    @Override
    public void writeObjectId(final Object a1) throws IOException {
        /*SL:740*/if (this._itemFilter != null) {
            /*SL:741*/this.delegate.writeObjectId(a1);
        }
    }
    
    @Override
    public void writeObjectRef(final Object a1) throws IOException {
        /*SL:747*/if (this._itemFilter != null) {
            /*SL:748*/this.delegate.writeObjectRef(a1);
        }
    }
    
    @Override
    public void writeTypeId(final Object a1) throws IOException {
        /*SL:754*/if (this._itemFilter != null) {
            /*SL:755*/this.delegate.writeTypeId(a1);
        }
    }
    
    protected void _checkParentPath() throws IOException {
        /*SL:834*/++this._matchCount;
        /*SL:836*/if (this._includePath) {
            /*SL:837*/this._filterContext.writePath(this.delegate);
        }
        /*SL:840*/if (!this._allowMultipleMatches) {
            /*SL:842*/this._filterContext.skipParentChecks();
        }
    }
    
    protected void _checkPropertyParentPath() throws IOException {
        /*SL:853*/++this._matchCount;
        /*SL:854*/if (this._includePath) {
            /*SL:855*/this._filterContext.writePath(this.delegate);
        }
        else/*SL:856*/ if (this._includeImmediateParent) {
            /*SL:859*/this._filterContext.writeImmediatePath(this.delegate);
        }
        /*SL:863*/if (!this._allowMultipleMatches) {
            /*SL:865*/this._filterContext.skipParentChecks();
        }
    }
    
    protected boolean _checkBinaryWrite() throws IOException {
        /*SL:871*/if (this._itemFilter == null) {
            /*SL:872*/return false;
        }
        /*SL:874*/if (this._itemFilter == TokenFilter.INCLUDE_ALL) {
            /*SL:875*/return true;
        }
        /*SL:877*/if (this._itemFilter.includeBinary()) {
            /*SL:878*/this._checkParentPath();
            /*SL:879*/return true;
        }
        /*SL:881*/return false;
    }
    
    protected boolean _checkRawValueWrite() throws IOException {
        /*SL:886*/if (this._itemFilter == null) {
            /*SL:887*/return false;
        }
        /*SL:889*/if (this._itemFilter == TokenFilter.INCLUDE_ALL) {
            /*SL:890*/return true;
        }
        /*SL:892*/if (this._itemFilter.includeRawValue()) {
            /*SL:893*/this._checkParentPath();
            /*SL:894*/return true;
        }
        /*SL:896*/return false;
    }
}

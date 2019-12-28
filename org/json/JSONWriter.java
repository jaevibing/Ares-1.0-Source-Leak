package org.json;

import java.io.IOException;

public class JSONWriter
{
    private static final int maxdepth = 200;
    private boolean comma;
    protected char mode;
    private final JSONObject[] stack;
    private int top;
    protected Appendable writer;
    
    public JSONWriter(final Appendable a1) {
        this.comma = false;
        this.mode = 'i';
        this.stack = new JSONObject[200];
        this.top = 0;
        this.writer = a1;
    }
    
    private JSONWriter append(final String v2) throws JSONException {
        /*SL:112*/if (v2 == null) {
            /*SL:113*/throw new JSONException("Null pointer");
        }
        /*SL:115*/if (this.mode != 'o') {
            if (this.mode != 'a') {
                /*SL:133*/throw new JSONException("Value out of sequence.");
            }
        }
        try {
            if (this.comma && this.mode == 'a') {
                this.writer.append(',');
            }
            this.writer.append(v2);
        }
        catch (IOException a1) {
            throw new JSONException(a1);
        }
        if (this.mode == 'o') {
            this.mode = 'k';
        }
        this.comma = true;
        return this;
    }
    
    public JSONWriter array() throws JSONException {
        /*SL:146*/if (this.mode == 'i' || this.mode == 'o' || this.mode == 'a') {
            /*SL:147*/this.push(null);
            /*SL:148*/this.append("[");
            /*SL:149*/this.comma = false;
            /*SL:150*/return this;
        }
        /*SL:152*/throw new JSONException("Misplaced array.");
    }
    
    private JSONWriter end(final char v1, final char v2) throws JSONException {
        /*SL:163*/if (this.mode != v1) {
            /*SL:164*/throw new JSONException((v1 == 'a') ? /*EL:165*/"Misplaced endArray." : /*EL:166*/"Misplaced endObject.");
        }
        /*SL:168*/this.pop(v1);
        try {
            /*SL:170*/this.writer.append(v2);
        }
        catch (IOException a1) {
            /*SL:175*/throw new JSONException(a1);
        }
        /*SL:177*/this.comma = true;
        /*SL:178*/return this;
    }
    
    public JSONWriter endArray() throws JSONException {
        /*SL:188*/return this.end('a', ']');
    }
    
    public JSONWriter endObject() throws JSONException {
        /*SL:198*/return this.end('k', '}');
    }
    
    public JSONWriter key(final String v0) throws JSONException {
        /*SL:210*/if (v0 == null) {
            /*SL:211*/throw new JSONException("Null key.");
        }
        /*SL:213*/if (this.mode == 'k') {
            try {
                final JSONObject a1 = /*EL:215*/this.stack[this.top - 1];
                /*SL:217*/if (a1.has(v0)) {
                    /*SL:218*/throw new JSONException("Duplicate key \"" + v0 + "\"");
                }
                /*SL:220*/a1.put(v0, true);
                /*SL:221*/if (this.comma) {
                    /*SL:222*/this.writer.append(',');
                }
                /*SL:224*/this.writer.append(JSONObject.quote(v0));
                /*SL:225*/this.writer.append(':');
                /*SL:226*/this.comma = false;
                /*SL:227*/this.mode = 'o';
                /*SL:228*/return this;
            }
            catch (IOException v) {
                /*SL:233*/throw new JSONException(v);
            }
        }
        /*SL:236*/throw new JSONException("Misplaced key.");
    }
    
    public JSONWriter object() throws JSONException {
        /*SL:250*/if (this.mode == 'i') {
            /*SL:251*/this.mode = 'o';
        }
        /*SL:253*/if (this.mode == 'o' || this.mode == 'a') {
            /*SL:254*/this.append("{");
            /*SL:255*/this.push(new JSONObject());
            /*SL:256*/this.comma = false;
            /*SL:257*/return this;
        }
        /*SL:259*/throw new JSONException("Misplaced object.");
    }
    
    private void pop(final char a1) throws JSONException {
        /*SL:270*/if (this.top <= 0) {
            /*SL:271*/throw new JSONException("Nesting error.");
        }
        final char v1 = /*EL:273*/(this.stack[this.top - 1] == null) ? 'a' : 'k';
        /*SL:274*/if (v1 != a1) {
            /*SL:275*/throw new JSONException("Nesting error.");
        }
        /*SL:277*/--this.top;
        /*SL:282*/this.mode = ((this.top == 0) ? 'd' : ((this.stack[this.top - 1] == null) ? 'a' : 'k'));
    }
    
    private void push(final JSONObject a1) throws JSONException {
        /*SL:291*/if (this.top >= 200) {
            /*SL:292*/throw new JSONException("Nesting too deep.");
        }
        /*SL:294*/this.stack[this.top] = a1;
        /*SL:295*/this.mode = ((a1 == null) ? 'a' : 'k');
        /*SL:296*/++this.top;
    }
    
    public static String valueToString(final Object v-1) throws JSONException {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     0: aload_0         /* v-1 */
        //     1: ifnull          12
        //     4: aload_0         /* v-1 */
        //     5: aconst_null    
        //     6: invokevirtual   java/lang/Object.equals:(Ljava/lang/Object;)Z
        //     9: ifeq            15
        //    12: ldc             "null"
        //    14: areturn        
        //    15: aload_0         /* v-1 */
        //    16: instanceof      Lorg/json/JSONString;
        //    19: ifeq            78
        //    22: aload_0         /* v-1 */
        //    23: checkcast       Lorg/json/JSONString;
        //    26: invokeinterface org/json/JSONString.toJSONString:()Ljava/lang/String;
        //    31: astore_1        /* a1 */
        //    32: goto            45
        //    35: astore_2        /* v1 */
        //    36: new             Lorg/json/JSONException;
        //    39: dup            
        //    40: aload_2         /* v1 */
        //    41: invokespecial   org/json/JSONException.<init>:(Ljava/lang/Throwable;)V
        //    44: athrow         
        //    45: aload_1         /* v0 */
        //    46: ifnull          51
        //    49: aload_1         /* v0 */
        //    50: areturn        
        //    51: new             Lorg/json/JSONException;
        //    54: dup            
        //    55: new             Ljava/lang/StringBuilder;
        //    58: dup            
        //    59: invokespecial   java/lang/StringBuilder.<init>:()V
        //    62: ldc             "Bad value from toJSONString: "
        //    64: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //    67: aload_1         /* v0 */
        //    68: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //    71: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //    74: invokespecial   org/json/JSONException.<init>:(Ljava/lang/String;)V
        //    77: athrow         
        //    78: aload_0         /* v-1 */
        //    79: instanceof      Ljava/lang/Number;
        //    82: ifeq            113
        //    85: aload_0         /* v-1 */
        //    86: checkcast       Ljava/lang/Number;
        //    89: invokestatic    org/json/JSONObject.numberToString:(Ljava/lang/Number;)Ljava/lang/String;
        //    92: astore_1        /* v0 */
        //    93: getstatic       org/json/JSONObject.NUMBER_PATTERN:Ljava/util/regex/Pattern;
        //    96: aload_1         /* v0 */
        //    97: invokevirtual   java/util/regex/Pattern.matcher:(Ljava/lang/CharSequence;)Ljava/util/regex/Matcher;
        //   100: invokevirtual   java/util/regex/Matcher.matches:()Z
        //   103: ifeq            108
        //   106: aload_1         /* v0 */
        //   107: areturn        
        //   108: aload_1         /* v0 */
        //   109: invokestatic    org/json/JSONObject.quote:(Ljava/lang/String;)Ljava/lang/String;
        //   112: areturn        
        //   113: aload_0         /* v-1 */
        //   114: instanceof      Ljava/lang/Boolean;
        //   117: ifne            134
        //   120: aload_0         /* v-1 */
        //   121: instanceof      Lorg/json/JSONObject;
        //   124: ifne            134
        //   127: aload_0         /* v-1 */
        //   128: instanceof      Lorg/json/JSONArray;
        //   131: ifeq            139
        //   134: aload_0         /* v-1 */
        //   135: invokevirtual   java/lang/Object.toString:()Ljava/lang/String;
        //   138: areturn        
        //   139: aload_0         /* v-1 */
        //   140: instanceof      Ljava/util/Map;
        //   143: ifeq            163
        //   146: aload_0         /* v-1 */
        //   147: checkcast       Ljava/util/Map;
        //   150: astore_1        /* a1 */
        //   151: new             Lorg/json/JSONObject;
        //   154: dup            
        //   155: aload_1         /* a1 */
        //   156: invokespecial   org/json/JSONObject.<init>:(Ljava/util/Map;)V
        //   159: invokevirtual   org/json/JSONObject.toString:()Ljava/lang/String;
        //   162: areturn        
        //   163: aload_0         /* v-1 */
        //   164: instanceof      Ljava/util/Collection;
        //   167: ifeq            187
        //   170: aload_0         /* v-1 */
        //   171: checkcast       Ljava/util/Collection;
        //   174: astore_1        /* a1 */
        //   175: new             Lorg/json/JSONArray;
        //   178: dup            
        //   179: aload_1         /* a1 */
        //   180: invokespecial   org/json/JSONArray.<init>:(Ljava/util/Collection;)V
        //   183: invokevirtual   org/json/JSONArray.toString:()Ljava/lang/String;
        //   186: areturn        
        //   187: aload_0         /* v-1 */
        //   188: invokevirtual   java/lang/Object.getClass:()Ljava/lang/Class;
        //   191: invokevirtual   java/lang/Class.isArray:()Z
        //   194: ifeq            209
        //   197: new             Lorg/json/JSONArray;
        //   200: dup            
        //   201: aload_0         /* v-1 */
        //   202: invokespecial   org/json/JSONArray.<init>:(Ljava/lang/Object;)V
        //   205: invokevirtual   org/json/JSONArray.toString:()Ljava/lang/String;
        //   208: areturn        
        //   209: aload_0         /* v-1 */
        //   210: instanceof      Ljava/lang/Enum;
        //   213: ifeq            227
        //   216: aload_0         /* v-1 */
        //   217: checkcast       Ljava/lang/Enum;
        //   220: invokevirtual   java/lang/Enum.name:()Ljava/lang/String;
        //   223: invokestatic    org/json/JSONObject.quote:(Ljava/lang/String;)Ljava/lang/String;
        //   226: areturn        
        //   227: aload_0         /* v-1 */
        //   228: invokevirtual   java/lang/Object.toString:()Ljava/lang/String;
        //   231: invokestatic    org/json/JSONObject.quote:(Ljava/lang/String;)Ljava/lang/String;
        //   234: areturn        
        //    Exceptions:
        //  throws org.json.JSONException
        //    LocalVariableTable:
        //  Start  Length  Slot  Name  Signature
        //  -----  ------  ----  ----  ----------------------
        //  32     3       1     a1    Ljava/lang/String;
        //  36     9       2     v1    Ljava/lang/Exception;
        //  45     33      1     v0    Ljava/lang/String;
        //  93     20      1     v0    Ljava/lang/String;
        //  151    12      1     v0    Ljava/util/Map;
        //  175    12      1     v0    Ljava/util/Collection;
        //  0      235     0     v-1   Ljava/lang/Object;
        //    LocalVariableTypeTable:
        //  Start  Length  Slot  Name  Signature
        //  -----  ------  ----  ----  -------------------------
        //  151    12      1     a1    Ljava/util/Map<**>;
        //  175    12      1     a1    Ljava/util/Collection<*>;
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                 
        //  -----  -----  -----  -----  ---------------------
        //  22     32     35     45     Ljava/lang/Exception;
        // 
        // The error that occurred was:
        // 
        // java.lang.NullPointerException
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    public JSONWriter value(final boolean a1) throws JSONException {
        /*SL:379*/return this.append(a1 ? "true" : "false");
    }
    
    public JSONWriter value(final double a1) throws JSONException {
        /*SL:389*/return this.value((Object)a1);
    }
    
    public JSONWriter value(final long a1) throws JSONException {
        /*SL:399*/return this.append(Long.toString(a1));
    }
    
    public JSONWriter value(final Object a1) throws JSONException {
        /*SL:411*/return this.append(valueToString(a1));
    }
}

package com.google.api.client.util;

import java.util.WeakHashMap;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.TreeSet;
import java.util.Comparator;
import java.util.Collection;
import java.lang.reflect.Field;
import java.util.Locale;
import java.util.List;
import java.util.IdentityHashMap;
import java.util.Map;

public final class ClassInfo
{
    private static final Map<Class<?>, ClassInfo> CACHE;
    private static final Map<Class<?>, ClassInfo> CACHE_IGNORE_CASE;
    private final Class<?> clazz;
    private final boolean ignoreCase;
    private final IdentityHashMap<String, FieldInfo> nameToFieldInfoMap;
    final List<String> names;
    
    public static ClassInfo of(final Class<?> a1) {
        /*SL:71*/return of(a1, false);
    }
    
    public static ClassInfo of(final Class<?> a2, final boolean v1) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     0: aload_0         /* a2 */
        //     1: ifnonnull       6
        //     4: aconst_null    
        //     5: areturn        
        //     6: iload_1         /* v1 */
        //     7: ifeq            16
        //    10: getstatic       com/google/api/client/util/ClassInfo.CACHE_IGNORE_CASE:Ljava/util/Map;
        //    13: goto            19
        //    16: getstatic       com/google/api/client/util/ClassInfo.CACHE:Ljava/util/Map;
        //    19: astore_2        /* v2 */
        //    20: aload_2         /* v2 */
        //    21: dup            
        //    22: astore_3       
        //    23: monitorenter   
        //    24: aload_2         /* v2 */
        //    25: aload_0         /* a2 */
        //    26: invokeinterface java/util/Map.get:(Ljava/lang/Object;)Ljava/lang/Object;
        //    31: checkcast       Lcom/google/api/client/util/ClassInfo;
        //    34: astore          a1
        //    36: aload           a1
        //    38: ifnonnull       62
        //    41: new             Lcom/google/api/client/util/ClassInfo;
        //    44: dup            
        //    45: aload_0         /* a2 */
        //    46: iload_1         /* v1 */
        //    47: invokespecial   com/google/api/client/util/ClassInfo.<init>:(Ljava/lang/Class;Z)V
        //    50: astore          a1
        //    52: aload_2         /* v2 */
        //    53: aload_0         /* a2 */
        //    54: aload           a1
        //    56: invokeinterface java/util/Map.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //    61: pop            
        //    62: aload_3        
        //    63: monitorexit    
        //    64: goto            74
        //    67: astore          5
        //    69: aload_3        
        //    70: monitorexit    
        //    71: aload           5
        //    73: athrow         
        //    74: aload           v4
        //    76: areturn        
        //    Signature:
        //  (Ljava/lang/Class<*>;Z)Lcom/google/api/client/util/ClassInfo;
        //    LocalVariableTable:
        //  Start  Length  Slot  Name  Signature
        //  -----  ------  ----  ----  --------------------------------------
        //  36     31      4     a1    Lcom/google/api/client/util/ClassInfo;
        //  0      77      0     a2    Ljava/lang/Class;
        //  0      77      1     v1    Z
        //  20     57      2     v2    Ljava/util/Map;
        //  74     3       4     v4    Lcom/google/api/client/util/ClassInfo;
        //    LocalVariableTypeTable:
        //  Start  Length  Slot  Name  Signature
        //  -----  ------  ----  ----  ---------------------------------------------------------------------------
        //  0      77      0     a2    Ljava/lang/Class<*>;
        //  20     57      2     v2    Ljava/util/Map<Ljava/lang/Class<*>;Lcom/google/api/client/util/ClassInfo;>;
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type
        //  -----  -----  -----  -----  ----
        //  24     64     67     74     Any
        //  67     71     67     74     Any
        // 
        // The error that occurred was:
        // 
        // java.lang.IndexOutOfBoundsException: Index: 0, Size: 0
        //     at java.util.ArrayList.rangeCheck(ArrayList.java:657)
        //     at java.util.ArrayList.get(ArrayList.java:433)
        //     at com.strobel.decompiler.ast.AstBuilder.convertLocalVariables(AstBuilder.java:3037)
        //     at com.strobel.decompiler.ast.AstBuilder.performStackAnalysis(AstBuilder.java:2446)
        //     at com.strobel.decompiler.ast.AstBuilder.build(AstBuilder.java:109)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:210)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:757)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:655)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:532)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:499)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:141)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:130)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:105)
        //     at cuchaz.enigma.Deobfuscator.getSourceTree(Deobfuscator.java:224)
        //     at cuchaz.enigma.Deobfuscator.writeSources(Deobfuscator.java:306)
        //     at cuchaz.enigma.gui.GuiController$1.run(GuiController.java:110)
        //     at cuchaz.enigma.gui.ProgressDialog$1.run(ProgressDialog.java:98)
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    public Class<?> getUnderlyingClass() {
        /*SL:104*/return this.clazz;
    }
    
    public final boolean getIgnoreCase() {
        /*SL:113*/return this.ignoreCase;
    }
    
    public FieldInfo getFieldInfo(String a1) {
        /*SL:123*/if (a1 != null) {
            /*SL:124*/if (this.ignoreCase) {
                /*SL:125*/a1 = a1.toLowerCase(Locale.US);
            }
            /*SL:127*/a1 = a1.intern();
        }
        /*SL:129*/return this.nameToFieldInfoMap.get(a1);
    }
    
    public Field getField(final String a1) {
        final FieldInfo v1 = /*EL:139*/this.getFieldInfo(a1);
        /*SL:140*/return (v1 == null) ? null : v1.getField();
    }
    
    public boolean isEnum() {
        /*SL:149*/return this.clazz.isEnum();
    }
    
    public Collection<String> getNames() {
        /*SL:157*/return this.names;
    }
    
    private ClassInfo(final Class<?> v-8, final boolean v-7) {
        this.nameToFieldInfoMap = new IdentityHashMap<String, FieldInfo>();
        this.clazz = v-8;
        this.ignoreCase = v-7;
        Preconditions.checkArgument(!v-7 || !v-8.isEnum(), (Object)("cannot ignore case on an enum: " + v-8));
        final TreeSet<String> set = new TreeSet<String>(new Comparator<String>() {
            @Override
            public int compare(final String a1, final String a2) {
                /*SL:168*/return Objects.equal(a1, a2) ? 0 : ((a1 == null) ? -1 : ((a2 == null) ? 1 : a1.compareTo(a2)));
            }
        });
        for (final Field v-9 : v-8.getDeclaredFields()) {
            final FieldInfo a1 = FieldInfo.of(v-9);
            if (a1 != null) {
                String a2 = a1.getName();
                if (v-7) {
                    a2 = a2.toLowerCase(Locale.US).intern();
                }
                final FieldInfo v1 = this.nameToFieldInfoMap.get(a2);
                Preconditions.checkArgument(v1 == null, "two fields have the same %sname <%s>: %s and %s", v-7 ? "case-insensitive " : "", a2, v-9, (v1 == null) ? null : v1.getField());
                this.nameToFieldInfoMap.put(a2, a1);
                set.add(a2);
            }
        }
        final Class<?> superclass = v-8.getSuperclass();
        if (superclass != null) {
            final ClassInfo of = of(superclass, v-7);
            set.addAll(of.names);
            for (final Map.Entry<String, FieldInfo> entry : of.nameToFieldInfoMap.entrySet()) {
                final String s = entry.getKey();
                if (!this.nameToFieldInfoMap.containsKey(s)) {
                    this.nameToFieldInfoMap.put(s, entry.getValue());
                }
            }
        }
        this.names = (set.isEmpty() ? Collections.<String>emptyList() : Collections.<String>unmodifiableList((List<? extends String>)new ArrayList<String>(set)));
    }
    
    public Collection<FieldInfo> getFieldInfos() {
        /*SL:218*/return Collections.<FieldInfo>unmodifiableCollection((Collection<? extends FieldInfo>)this.nameToFieldInfoMap.values());
    }
    
    static {
        CACHE = new WeakHashMap<Class<?>, ClassInfo>();
        CACHE_IGNORE_CASE = new WeakHashMap<Class<?>, ClassInfo>();
    }
}

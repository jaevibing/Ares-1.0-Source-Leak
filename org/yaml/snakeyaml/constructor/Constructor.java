package org.yaml.snakeyaml.constructor;

import java.util.List;
import java.util.ArrayList;
import org.yaml.snakeyaml.nodes.SequenceNode;
import java.lang.reflect.Constructor;
import java.util.UUID;
import java.util.Calendar;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import org.yaml.snakeyaml.nodes.ScalarNode;
import org.yaml.snakeyaml.error.Mark;
import org.yaml.snakeyaml.introspector.Property;
import java.util.Set;
import org.yaml.snakeyaml.nodes.CollectionNode;
import java.util.Map;
import org.yaml.snakeyaml.nodes.MappingNode;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.error.YAMLException;
import java.util.Iterator;
import org.yaml.snakeyaml.nodes.NodeId;
import org.yaml.snakeyaml.nodes.Tag;
import java.util.Collection;
import org.yaml.snakeyaml.TypeDescription;

public class Constructor extends SafeConstructor
{
    public Constructor() {
        this(Object.class);
    }
    
    public Constructor(final Class<?> a1) {
        this(new TypeDescription(checkRoot(a1)));
    }
    
    private static Class<?> checkRoot(final Class<?> a1) {
        /*SL:63*/if (a1 == null) {
            /*SL:64*/throw new NullPointerException("Root class must be provided.");
        }
        /*SL:66*/return a1;
    }
    
    public Constructor(final TypeDescription a1) {
        this(a1, null);
    }
    
    public Constructor(final TypeDescription v1, final Collection<TypeDescription> v2) {
        if (v1 == null) {
            throw new NullPointerException("Root type must be provided.");
        }
        this.yamlConstructors.put(null, new ConstructYamlObject());
        if (!Object.class.equals(v1.getType())) {
            this.rootTag = new Tag(v1.getType());
        }
        this.yamlClassConstructors.put(NodeId.scalar, new ConstructScalar());
        this.yamlClassConstructors.put(NodeId.mapping, new ConstructMapping());
        this.yamlClassConstructors.put(NodeId.sequence, new ConstructSequence());
        this.addTypeDescription(v1);
        if (v2 != null) {
            for (final TypeDescription a1 : v2) {
                this.addTypeDescription(a1);
            }
        }
    }
    
    public Constructor(final String a1) throws ClassNotFoundException {
        this(Class.forName(check(a1)));
    }
    
    private static final String check(final String a1) {
        /*SL:106*/if (a1 == null) {
            /*SL:107*/throw new NullPointerException("Root type must be provided.");
        }
        /*SL:109*/if (a1.trim().length() == 0) {
            /*SL:110*/throw new YAMLException("Root type must be provided.");
        }
        /*SL:112*/return a1;
    }
    
    protected Class<?> getClassForNode(final Node v-3) {
        final Class<?> clazz = /*EL:635*/this.typeTags.get(v-3.getTag());
        /*SL:636*/if (clazz == null) {
            final String className = /*EL:637*/v-3.getTag().getClassName();
            Class<?> a1;
            try {
                /*SL:640*/a1 = this.getClassForName(className);
            }
            catch (ClassNotFoundException v1) {
                /*SL:642*/throw new YAMLException("Class not found: " + className);
            }
            /*SL:644*/this.typeTags.put(v-3.getTag(), a1);
            /*SL:645*/return a1;
        }
        /*SL:647*/return clazz;
    }
    
    protected Class<?> getClassForName(final String v2) throws ClassNotFoundException {
        try {
            /*SL:653*/return Class.forName(v2, true, Thread.currentThread().getContextClassLoader());
        }
        catch (ClassNotFoundException a1) {
            /*SL:655*/return Class.forName(v2);
        }
    }
    
    protected class ConstructMapping implements Construct
    {
        @Override
        public Object construct(final Node v2) {
            final MappingNode v3 = /*EL:131*/(MappingNode)v2;
            /*SL:132*/if (Map.class.isAssignableFrom(v2.getType())) {
                /*SL:133*/if (v2.isTwoStepsConstruction()) {
                    /*SL:134*/return Constructor.this.newMap(v3);
                }
                /*SL:136*/return Constructor.this.constructMapping(v3);
            }
            else/*SL:138*/ if (Collection.class.isAssignableFrom(v2.getType())) {
                /*SL:139*/if (v2.isTwoStepsConstruction()) {
                    /*SL:140*/return Constructor.this.newSet(v3);
                }
                /*SL:142*/return Constructor.this.constructSet(v3);
            }
            else {
                final Object a1 = /*EL:145*/Constructor.this.newInstance(v3);
                /*SL:146*/if (v2.isTwoStepsConstruction()) {
                    /*SL:147*/return a1;
                }
                /*SL:149*/return this.constructJavaBean2ndStep(v3, a1);
            }
        }
        
        @Override
        public void construct2ndStep(final Node a1, final Object a2) {
            /*SL:156*/if (Map.class.isAssignableFrom(a1.getType())) {
                /*SL:157*/Constructor.this.constructMapping2ndStep((MappingNode)a1, (Map<Object, Object>)a2);
            }
            else/*SL:158*/ if (Set.class.isAssignableFrom(a1.getType())) {
                /*SL:159*/Constructor.this.constructSet2ndStep((MappingNode)a1, (Set<Object>)a2);
            }
            else {
                /*SL:161*/this.constructJavaBean2ndStep((MappingNode)a1, a2);
            }
        }
        
        protected Object constructJavaBean2ndStep(final MappingNode v-13, final Object v-12) {
            // 
            // This method could not be decompiled.
            // 
            // Original Bytecode:
            // 
            //     0: aload_0         /* v-14 */
            //     1: getfield        org/yaml/snakeyaml/constructor/Constructor$ConstructMapping.this$0:Lorg/yaml/snakeyaml/constructor/Constructor;
            //     4: aload_1         /* v-13 */
            //     5: invokevirtual   org/yaml/snakeyaml/constructor/Constructor.flattenMapping:(Lorg/yaml/snakeyaml/nodes/MappingNode;)V
            //     8: aload_1         /* v-13 */
            //     9: invokevirtual   org/yaml/snakeyaml/nodes/MappingNode.getType:()Ljava/lang/Class;
            //    12: astore_3        /* v-11 */
            //    13: aload_1         /* v-13 */
            //    14: invokevirtual   org/yaml/snakeyaml/nodes/MappingNode.getValue:()Ljava/util/List;
            //    17: astore          v-10
            //    19: aload           v-10
            //    21: invokeinterface java/util/List.iterator:()Ljava/util/Iterator;
            //    26: astore          5
            //    28: aload           5
            //    30: invokeinterface java/util/Iterator.hasNext:()Z
            //    35: ifeq            625
            //    38: aload           5
            //    40: invokeinterface java/util/Iterator.next:()Ljava/lang/Object;
            //    45: checkcast       Lorg/yaml/snakeyaml/nodes/NodeTuple;
            //    48: astore          v-8
            //    50: aload           v-8
            //    52: invokevirtual   org/yaml/snakeyaml/nodes/NodeTuple.getKeyNode:()Lorg/yaml/snakeyaml/nodes/Node;
            //    55: instanceof      Lorg/yaml/snakeyaml/nodes/ScalarNode;
            //    58: ifeq            74
            //    61: aload           v-8
            //    63: invokevirtual   org/yaml/snakeyaml/nodes/NodeTuple.getKeyNode:()Lorg/yaml/snakeyaml/nodes/Node;
            //    66: checkcast       Lorg/yaml/snakeyaml/nodes/ScalarNode;
            //    69: astore          a1
            //    71: goto            105
            //    74: new             Lorg/yaml/snakeyaml/error/YAMLException;
            //    77: dup            
            //    78: new             Ljava/lang/StringBuilder;
            //    81: dup            
            //    82: invokespecial   java/lang/StringBuilder.<init>:()V
            //    85: ldc             "Keys must be scalars but found: "
            //    87: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
            //    90: aload           v-8
            //    92: invokevirtual   org/yaml/snakeyaml/nodes/NodeTuple.getKeyNode:()Lorg/yaml/snakeyaml/nodes/Node;
            //    95: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/Object;)Ljava/lang/StringBuilder;
            //    98: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
            //   101: invokespecial   org/yaml/snakeyaml/error/YAMLException.<init>:(Ljava/lang/String;)V
            //   104: athrow         
            //   105: aload           v-8
            //   107: invokevirtual   org/yaml/snakeyaml/nodes/NodeTuple.getValueNode:()Lorg/yaml/snakeyaml/nodes/Node;
            //   110: astore          v-6
            //   112: aload           v-7
            //   114: ldc             Ljava/lang/String;.class
            //   116: invokevirtual   org/yaml/snakeyaml/nodes/ScalarNode.setType:(Ljava/lang/Class;)V
            //   119: aload_0         /* v-14 */
            //   120: getfield        org/yaml/snakeyaml/constructor/Constructor$ConstructMapping.this$0:Lorg/yaml/snakeyaml/constructor/Constructor;
            //   123: aload           v-7
            //   125: invokevirtual   org/yaml/snakeyaml/constructor/Constructor.constructObject:(Lorg/yaml/snakeyaml/nodes/Node;)Ljava/lang/Object;
            //   128: checkcast       Ljava/lang/String;
            //   131: astore          v-5
            //   133: aload_0         /* v-14 */
            //   134: getfield        org/yaml/snakeyaml/constructor/Constructor$ConstructMapping.this$0:Lorg/yaml/snakeyaml/constructor/Constructor;
            //   137: getfield        org/yaml/snakeyaml/constructor/Constructor.typeDefinitions:Ljava/util/Map;
            //   140: aload_3         /* v-11 */
            //   141: invokeinterface java/util/Map.get:(Ljava/lang/Object;)Ljava/lang/Object;
            //   146: checkcast       Lorg/yaml/snakeyaml/TypeDescription;
            //   149: astore          v-4
            //   151: aload           v-4
            //   153: ifnonnull       166
            //   156: aload_0         /* v-14 */
            //   157: aload_3         /* v-11 */
            //   158: aload           v-5
            //   160: invokevirtual   org/yaml/snakeyaml/constructor/Constructor$ConstructMapping.getProperty:(Ljava/lang/Class;Ljava/lang/String;)Lorg/yaml/snakeyaml/introspector/Property;
            //   163: goto            173
            //   166: aload           v-4
            //   168: aload           v-5
            //   170: invokevirtual   org/yaml/snakeyaml/TypeDescription.getProperty:(Ljava/lang/String;)Lorg/yaml/snakeyaml/introspector/Property;
            //   173: astore          v-3
            //   175: aload           v-3
            //   177: invokevirtual   org/yaml/snakeyaml/introspector/Property.isWritable:()Z
            //   180: ifne            223
            //   183: new             Lorg/yaml/snakeyaml/error/YAMLException;
            //   186: dup            
            //   187: new             Ljava/lang/StringBuilder;
            //   190: dup            
            //   191: invokespecial   java/lang/StringBuilder.<init>:()V
            //   194: ldc             "No writable property '"
            //   196: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
            //   199: aload           v-5
            //   201: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
            //   204: ldc             "' on class: "
            //   206: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
            //   209: aload_3         /* v-11 */
            //   210: invokevirtual   java/lang/Class.getName:()Ljava/lang/String;
            //   213: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
            //   216: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
            //   219: invokespecial   org/yaml/snakeyaml/error/YAMLException.<init>:(Ljava/lang/String;)V
            //   222: athrow         
            //   223: aload           v-6
            //   225: aload           v-3
            //   227: invokevirtual   org/yaml/snakeyaml/introspector/Property.getType:()Ljava/lang/Class;
            //   230: invokevirtual   org/yaml/snakeyaml/nodes/Node.setType:(Ljava/lang/Class;)V
            //   233: aload           v-4
            //   235: ifnull          250
            //   238: aload           v-4
            //   240: aload           v-5
            //   242: aload           v-6
            //   244: invokevirtual   org/yaml/snakeyaml/TypeDescription.setupPropertyType:(Ljava/lang/String;Lorg/yaml/snakeyaml/nodes/Node;)Z
            //   247: goto            251
            //   250: iconst_0       
            //   251: istore          v-2
            //   253: iload           v-2
            //   255: ifne            416
            //   258: aload           v-6
            //   260: invokevirtual   org/yaml/snakeyaml/nodes/Node.getNodeId:()Lorg/yaml/snakeyaml/nodes/NodeId;
            //   263: getstatic       org/yaml/snakeyaml/nodes/NodeId.scalar:Lorg/yaml/snakeyaml/nodes/NodeId;
            //   266: if_acmpeq       416
            //   269: aload           v-3
            //   271: invokevirtual   org/yaml/snakeyaml/introspector/Property.getActualTypeArguments:()[Ljava/lang/Class;
            //   274: astore          v-1
            //   276: aload           v-1
            //   278: ifnull          416
            //   281: aload           v-1
            //   283: arraylength    
            //   284: ifle            416
            //   287: aload           v-6
            //   289: invokevirtual   org/yaml/snakeyaml/nodes/Node.getNodeId:()Lorg/yaml/snakeyaml/nodes/NodeId;
            //   292: getstatic       org/yaml/snakeyaml/nodes/NodeId.sequence:Lorg/yaml/snakeyaml/nodes/NodeId;
            //   295: if_acmpne       321
            //   298: aload           v-1
            //   300: iconst_0       
            //   301: aaload         
            //   302: astore          a2
            //   304: aload           v-6
            //   306: checkcast       Lorg/yaml/snakeyaml/nodes/SequenceNode;
            //   309: astore          v1
            //   311: aload           v1
            //   313: aload           a2
            //   315: invokevirtual   org/yaml/snakeyaml/nodes/SequenceNode.setListType:(Ljava/lang/Class;)V
            //   318: goto            416
            //   321: ldc             Ljava/util/Set;.class
            //   323: aload           v-6
            //   325: invokevirtual   org/yaml/snakeyaml/nodes/Node.getType:()Ljava/lang/Class;
            //   328: invokevirtual   java/lang/Class.isAssignableFrom:(Ljava/lang/Class;)Z
            //   331: ifeq            366
            //   334: aload           v-1
            //   336: iconst_0       
            //   337: aaload         
            //   338: astore          a2
            //   340: aload           v-6
            //   342: checkcast       Lorg/yaml/snakeyaml/nodes/MappingNode;
            //   345: astore          v1
            //   347: aload           v1
            //   349: aload           a2
            //   351: invokevirtual   org/yaml/snakeyaml/nodes/MappingNode.setOnlyKeyType:(Ljava/lang/Class;)V
            //   354: aload           v1
            //   356: iconst_1       
            //   357: invokestatic    java/lang/Boolean.valueOf:(Z)Ljava/lang/Boolean;
            //   360: invokevirtual   org/yaml/snakeyaml/nodes/MappingNode.setUseClassConstructor:(Ljava/lang/Boolean;)V
            //   363: goto            416
            //   366: ldc             Ljava/util/Map;.class
            //   368: aload           v-6
            //   370: invokevirtual   org/yaml/snakeyaml/nodes/Node.getType:()Ljava/lang/Class;
            //   373: invokevirtual   java/lang/Class.isAssignableFrom:(Ljava/lang/Class;)Z
            //   376: ifeq            416
            //   379: aload           v-1
            //   381: iconst_0       
            //   382: aaload         
            //   383: astore          a2
            //   385: aload           v-1
            //   387: iconst_1       
            //   388: aaload         
            //   389: astore          v1
            //   391: aload           v-6
            //   393: checkcast       Lorg/yaml/snakeyaml/nodes/MappingNode;
            //   396: astore          v2
            //   398: aload           v2
            //   400: aload           a2
            //   402: aload           v1
            //   404: invokevirtual   org/yaml/snakeyaml/nodes/MappingNode.setTypes:(Ljava/lang/Class;Ljava/lang/Class;)V
            //   407: aload           v2
            //   409: iconst_1       
            //   410: invokestatic    java/lang/Boolean.valueOf:(Z)Ljava/lang/Boolean;
            //   413: invokevirtual   org/yaml/snakeyaml/nodes/MappingNode.setUseClassConstructor:(Ljava/lang/Boolean;)V
            //   416: aload           v-4
            //   418: ifnull          434
            //   421: aload_0         /* v-14 */
            //   422: aload           v-4
            //   424: aload           v-5
            //   426: aload           v-6
            //   428: invokespecial   org/yaml/snakeyaml/constructor/Constructor$ConstructMapping.newInstance:(Lorg/yaml/snakeyaml/TypeDescription;Ljava/lang/String;Lorg/yaml/snakeyaml/nodes/Node;)Ljava/lang/Object;
            //   431: goto            443
            //   434: aload_0         /* v-14 */
            //   435: getfield        org/yaml/snakeyaml/constructor/Constructor$ConstructMapping.this$0:Lorg/yaml/snakeyaml/constructor/Constructor;
            //   438: aload           v-6
            //   440: invokevirtual   org/yaml/snakeyaml/constructor/Constructor.constructObject:(Lorg/yaml/snakeyaml/nodes/Node;)Ljava/lang/Object;
            //   443: astore          v-1
            //   445: aload           v-3
            //   447: invokevirtual   org/yaml/snakeyaml/introspector/Property.getType:()Ljava/lang/Class;
            //   450: getstatic       java/lang/Float.TYPE:Ljava/lang/Class;
            //   453: if_acmpeq       466
            //   456: aload           v-3
            //   458: invokevirtual   org/yaml/snakeyaml/introspector/Property.getType:()Ljava/lang/Class;
            //   461: ldc             Ljava/lang/Float;.class
            //   463: if_acmpne       487
            //   466: aload           v-1
            //   468: instanceof      Ljava/lang/Double;
            //   471: ifeq            487
            //   474: aload           v-1
            //   476: checkcast       Ljava/lang/Double;
            //   479: invokevirtual   java/lang/Double.floatValue:()F
            //   482: invokestatic    java/lang/Float.valueOf:(F)Ljava/lang/Float;
            //   485: astore          v-1
            //   487: aload           v-3
            //   489: invokevirtual   org/yaml/snakeyaml/introspector/Property.getType:()Ljava/lang/Class;
            //   492: ldc             Ljava/lang/String;.class
            //   494: if_acmpne       536
            //   497: getstatic       org/yaml/snakeyaml/nodes/Tag.BINARY:Lorg/yaml/snakeyaml/nodes/Tag;
            //   500: aload           v-6
            //   502: invokevirtual   org/yaml/snakeyaml/nodes/Node.getTag:()Lorg/yaml/snakeyaml/nodes/Tag;
            //   505: invokevirtual   org/yaml/snakeyaml/nodes/Tag.equals:(Ljava/lang/Object;)Z
            //   508: ifeq            536
            //   511: aload           v-1
            //   513: instanceof      [B
            //   516: ifeq            536
            //   519: new             Ljava/lang/String;
            //   522: dup            
            //   523: aload           v-1
            //   525: checkcast       [B
            //   528: checkcast       [B
            //   531: invokespecial   java/lang/String.<init>:([B)V
            //   534: astore          v-1
            //   536: aload           v-4
            //   538: ifnull          554
            //   541: aload           v-4
            //   543: aload_2         /* v-12 */
            //   544: aload           v-5
            //   546: aload           v-1
            //   548: invokevirtual   org/yaml/snakeyaml/TypeDescription.setProperty:(Ljava/lang/Object;Ljava/lang/String;Ljava/lang/Object;)Z
            //   551: ifne            562
            //   554: aload           v-3
            //   556: aload_2         /* v-12 */
            //   557: aload           v-1
            //   559: invokevirtual   org/yaml/snakeyaml/introspector/Property.set:(Ljava/lang/Object;Ljava/lang/Object;)V
            //   562: goto            622
            //   565: astore          v-4
            //   567: new             Lorg/yaml/snakeyaml/constructor/ConstructorException;
            //   570: dup            
            //   571: new             Ljava/lang/StringBuilder;
            //   574: dup            
            //   575: invokespecial   java/lang/StringBuilder.<init>:()V
            //   578: ldc_w           "Cannot create property="
            //   581: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
            //   584: aload           v-5
            //   586: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
            //   589: ldc_w           " for JavaBean="
            //   592: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
            //   595: aload_2         /* v-12 */
            //   596: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/Object;)Ljava/lang/StringBuilder;
            //   599: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
            //   602: aload_1         /* v-13 */
            //   603: invokevirtual   org/yaml/snakeyaml/nodes/MappingNode.getStartMark:()Lorg/yaml/snakeyaml/error/Mark;
            //   606: aload           v-4
            //   608: invokevirtual   java/lang/Exception.getMessage:()Ljava/lang/String;
            //   611: aload           v-6
            //   613: invokevirtual   org/yaml/snakeyaml/nodes/Node.getStartMark:()Lorg/yaml/snakeyaml/error/Mark;
            //   616: aload           v-4
            //   618: invokespecial   org/yaml/snakeyaml/constructor/ConstructorException.<init>:(Ljava/lang/String;Lorg/yaml/snakeyaml/error/Mark;Ljava/lang/String;Lorg/yaml/snakeyaml/error/Mark;Ljava/lang/Throwable;)V
            //   621: athrow         
            //   622: goto            28
            //   625: aload_2         /* v-12 */
            //   626: areturn        
            //    LocalVariableTable:
            //  Start  Length  Slot  Name  Signature
            //  -----  ------  ----  ----  -------------------------------------------------------------
            //  71     3       7     a1    Lorg/yaml/snakeyaml/nodes/ScalarNode;
            //  304    14      14    a2    Ljava/lang/Class;
            //  311    7       15    v1    Lorg/yaml/snakeyaml/nodes/SequenceNode;
            //  340    23      14    v0    Ljava/lang/Class;
            //  347    16      15    v1    Lorg/yaml/snakeyaml/nodes/MappingNode;
            //  385    31      14    v0    Ljava/lang/Class;
            //  391    25      15    v1    Ljava/lang/Class;
            //  398    18      16    v2    Lorg/yaml/snakeyaml/nodes/MappingNode;
            //  276    140     13    v-1   [Ljava/lang/Class;
            //  151    411     10    v-4   Lorg/yaml/snakeyaml/TypeDescription;
            //  175    387     11    v-3   Lorg/yaml/snakeyaml/introspector/Property;
            //  253    309     12    v-2   Z
            //  445    117     13    v-1   Ljava/lang/Object;
            //  567    55      10    v-4   Ljava/lang/Exception;
            //  105    517     7     v-7   Lorg/yaml/snakeyaml/nodes/ScalarNode;
            //  112    510     8     v-6   Lorg/yaml/snakeyaml/nodes/Node;
            //  133    489     9     v-5   Ljava/lang/String;
            //  50     572     6     v-8   Lorg/yaml/snakeyaml/nodes/NodeTuple;
            //  0      627     0     v-14  Lorg/yaml/snakeyaml/constructor/Constructor$ConstructMapping;
            //  0      627     1     v-13  Lorg/yaml/snakeyaml/nodes/MappingNode;
            //  0      627     2     v-12  Ljava/lang/Object;
            //  13     614     3     v-11  Ljava/lang/Class;
            //  19     608     4     v-10  Ljava/util/List;
            //    LocalVariableTypeTable:
            //  Start  Length  Slot  Name  Signature
            //  -----  ------  ----  ----  ------------------------------------------------------
            //  304    14      14    a2    Ljava/lang/Class<*>;
            //  340    23      14    a2    Ljava/lang/Class<*>;
            //  385    31      14    a2    Ljava/lang/Class<*>;
            //  391    25      15    v1    Ljava/lang/Class<*>;
            //  276    140     13    v-1   [Ljava/lang/Class<*>;
            //  13     614     3     v-11  Ljava/lang/Class<*>;
            //  19     608     4     v-10  Ljava/util/List<Lorg/yaml/snakeyaml/nodes/NodeTuple;>;
            //    Exceptions:
            //  Try           Handler
            //  Start  End    Start  End    Type                 
            //  -----  -----  -----  -----  ---------------------
            //  133    562    565    622    Ljava/lang/Exception;
            // 
            // The error that occurred was:
            // 
            // java.lang.NullPointerException
            // 
            throw new IllegalStateException("An error occurred while decompiling this method.");
        }
        
        private Object newInstance(final TypeDescription a1, final String a2, final Node a3) {
            final Object v1 = /*EL:276*/a1.newInstance(a2, a3);
            /*SL:277*/if (v1 != null) {
                /*SL:278*/Constructor.this.constructedObjects.put(a3, v1);
                /*SL:279*/return Constructor.this.constructObjectNoCheck(a3);
            }
            /*SL:281*/return Constructor.this.constructObject(a3);
        }
        
        protected Property getProperty(final Class<?> a1, final String a2) {
            /*SL:285*/return Constructor.this.getPropertyUtils().getProperty(a1, a2);
        }
    }
    
    protected class ConstructYamlObject implements Construct
    {
        private Construct getConstructor(final Node a1) {
            final Class<?> v1 = /*EL:298*/Constructor.this.getClassForNode(a1);
            /*SL:299*/a1.setType(v1);
            final Construct v2 = /*EL:301*/Constructor.this.yamlClassConstructors.get(a1.getNodeId());
            /*SL:302*/return v2;
        }
        
        @Override
        public Object construct(final Node v-1) {
            Object v0 = /*EL:306*/null;
            try {
                /*SL:308*/v0 = this.getConstructor(v-1).construct(v-1);
            }
            catch (ConstructorException a1) {
                /*SL:310*/throw a1;
            }
            catch (Exception v) {
                /*SL:312*/throw new ConstructorException(null, null, "Can't construct a java object for " + v-1.getTag() + /*EL:313*/"; exception=" + v.getMessage(), v-1.getStartMark(), v);
            }
            /*SL:315*/return v0;
        }
        
        @Override
        public void construct2ndStep(final Node v1, final Object v2) {
            try {
                /*SL:320*/this.getConstructor(v1).construct2ndStep(v1, v2);
            }
            catch (Exception a1) {
                /*SL:322*/throw new ConstructorException(null, null, "Can't construct a second step for a java object for " + v1.getTag() + /*EL:324*/"; exception=" + a1.getMessage(), v1.getStartMark(), /*EL:325*/a1);
            }
        }
    }
    
    protected class ConstructScalar extends AbstractConstruct
    {
        @Override
        public Object construct(final Node v-9) {
            final ScalarNode a2 = /*EL:336*/(ScalarNode)v-9;
            final Class<?> type = /*EL:337*/a2.getType();
            try {
                /*SL:340*/return Constructor.this.newInstance(type, a2, false);
            }
            catch (InstantiationException ex3) {
                Object instance = null;
                /*SL:349*/if (type.isPrimitive() || type == String.class || Number.class.isAssignableFrom(type) || type == Boolean.class || Date.class.isAssignableFrom(type) || type == Character.class || type == BigInteger.class || type == BigDecimal.class || Enum.class.isAssignableFrom(type) || Tag.BINARY.equals(a2.getTag()) || Calendar.class.isAssignableFrom(type) || type == UUID.class) {
                    final Object a1 = /*EL:352*/this.constructStandardJavaInstance(type, a2);
                }
                else {
                    final java.lang.reflect.Constructor<?>[] declaredConstructors = /*EL:355*/type.getDeclaredConstructors();
                    int n = /*EL:357*/0;
                    java.lang.reflect.Constructor<?> declaredConstructor = /*EL:358*/null;
                    /*SL:359*/for (final java.lang.reflect.Constructor<?> v1 : declaredConstructors) {
                        /*SL:360*/if (v1.getParameterTypes().length == 1) {
                            /*SL:361*/++n;
                            /*SL:362*/declaredConstructor = v1;
                        }
                    }
                    /*SL:366*/if (declaredConstructor == null) {
                        try {
                            /*SL:368*/return Constructor.this.newInstance(type, a2, false);
                        }
                        catch (InstantiationException ex) {
                            /*SL:370*/throw new YAMLException("No single argument constructor found for " + type + " : " + ex.getMessage());
                        }
                    }
                    Object o;
                    /*SL:373*/if (n == 1) {
                        /*SL:374*/o = this.constructStandardJavaInstance(declaredConstructor.getParameterTypes()[0], a2);
                    }
                    else {
                        /*SL:383*/o = Constructor.this.constructScalar(a2);
                        try {
                            /*SL:385*/declaredConstructor = type.getDeclaredConstructor(String.class);
                        }
                        catch (Exception ex2) {
                            /*SL:387*/throw new YAMLException("Can't construct a java object for scalar " + a2.getTag() + /*EL:388*/"; No String constructor found. Exception=" + ex2.getMessage(), /*EL:389*/ex2);
                        }
                    }
                    try {
                        /*SL:393*/declaredConstructor.setAccessible(true);
                        /*SL:394*/instance = declaredConstructor.newInstance(o);
                    }
                    catch (Exception ex2) {
                        /*SL:396*/throw new ConstructorException(null, null, "Can't construct a java object for scalar " + a2.getTag() + /*EL:397*/"; exception=" + ex2.getMessage(), /*EL:398*/a2.getStartMark(), /*EL:399*/ex2);
                    }
                }
                /*SL:402*/return instance;
            }
        }
        
        private Object constructStandardJavaInstance(final Class v-2, final ScalarNode v-1) {
            Object v2 = null;
            /*SL:409*/if (v-2 == String.class) {
                final Construct a1 = /*EL:410*/Constructor.this.yamlConstructors.get(Tag.STR);
                final Object a2 = /*EL:411*/a1.construct(v-1);
            }
            else/*SL:412*/ if (v-2 == Boolean.class || v-2 == Boolean.TYPE) {
                final Construct v1 = /*EL:413*/Constructor.this.yamlConstructors.get(Tag.BOOL);
                /*SL:414*/v2 = v1.construct(v-1);
            }
            else/*SL:415*/ if (v-2 == Character.class || v-2 == Character.TYPE) {
                final Construct v1 = /*EL:416*/Constructor.this.yamlConstructors.get(Tag.STR);
                final String v3 = /*EL:417*/(String)v1.construct(v-1);
                /*SL:418*/if (v3.length() == 0) {
                    /*SL:419*/v2 = null;
                }
                else {
                    /*SL:420*/if (v3.length() != 1) {
                        /*SL:421*/throw new YAMLException("Invalid node Character: '" + v3 + "'; length: " + v3.length());
                    }
                    /*SL:424*/v2 = v3.charAt(0);
                }
            }
            else/*SL:426*/ if (Date.class.isAssignableFrom(v-2)) {
                final Construct v1 = /*EL:427*/Constructor.this.yamlConstructors.get(Tag.TIMESTAMP);
                final Date v4 = /*EL:428*/(Date)v1.construct(v-1);
                /*SL:429*/if (v-2 == Date.class) {
                    /*SL:430*/v2 = v4;
                }
                else {
                    try {
                        final java.lang.reflect.Constructor<?> v5 = /*EL:433*/v-2.getConstructor(Long.TYPE);
                        /*SL:434*/v2 = v5.newInstance(v4.getTime());
                    }
                    catch (RuntimeException v6) {
                        /*SL:436*/throw v6;
                    }
                    catch (Exception v10) {
                        /*SL:438*/throw new YAMLException("Cannot construct: '" + v-2 + "'");
                    }
                }
            }
            else/*SL:441*/ if (v-2 == Float.class || v-2 == Double.class || v-2 == Float.TYPE || v-2 == Double.TYPE || v-2 == BigDecimal.class) {
                /*SL:443*/if (v-2 == BigDecimal.class) {
                    /*SL:444*/v2 = new BigDecimal(v-1.getValue());
                }
                else {
                    final Construct v1 = /*EL:446*/Constructor.this.yamlConstructors.get(Tag.FLOAT);
                    /*SL:447*/v2 = v1.construct(v-1);
                    /*SL:448*/if (v-2 == Float.class || v-2 == Float.TYPE) {
                        /*SL:449*/v2 = new Float((double)v2);
                    }
                }
            }
            else/*SL:452*/ if (v-2 == Byte.class || v-2 == Short.class || v-2 == Integer.class || v-2 == Long.class || v-2 == BigInteger.class || v-2 == Byte.TYPE || v-2 == Short.TYPE || v-2 == Integer.TYPE || v-2 == Long.TYPE) {
                final Construct v1 = /*EL:455*/Constructor.this.yamlConstructors.get(Tag.INT);
                /*SL:456*/v2 = v1.construct(v-1);
                /*SL:457*/if (v-2 == Byte.class || v-2 == Byte.TYPE) {
                    /*SL:458*/v2 = Byte.valueOf(v2.toString());
                }
                else/*SL:459*/ if (v-2 == Short.class || v-2 == Short.TYPE) {
                    /*SL:460*/v2 = Short.valueOf(v2.toString());
                }
                else/*SL:461*/ if (v-2 == Integer.class || v-2 == Integer.TYPE) {
                    /*SL:462*/v2 = Integer.parseInt(v2.toString());
                }
                else/*SL:463*/ if (v-2 == Long.class || v-2 == Long.TYPE) {
                    /*SL:464*/v2 = Long.valueOf(v2.toString());
                }
                else {
                    /*SL:467*/v2 = new BigInteger(v2.toString());
                }
            }
            else/*SL:469*/ if (Enum.class.isAssignableFrom(v-2)) {
                final String v7 = /*EL:470*/v-1.getValue();
                try {
                    /*SL:472*/v2 = Enum.<Object>valueOf((Class<Object>)v-2, v7);
                }
                catch (Exception v11) {
                    /*SL:474*/throw new YAMLException("Unable to find enum value '" + v7 + "' for enum class: " + v-2.getName());
                }
            }
            else/*SL:477*/ if (Calendar.class.isAssignableFrom(v-2)) {
                final ConstructYamlTimestamp v8 = /*EL:478*/new ConstructYamlTimestamp();
                /*SL:479*/v8.construct(v-1);
                /*SL:480*/v2 = v8.getCalendar();
            }
            else/*SL:481*/ if (Number.class.isAssignableFrom(v-2)) {
                final ConstructYamlFloat v9 = /*EL:483*/new ConstructYamlFloat(Constructor.this);
                /*SL:484*/v2 = v9.construct(v-1);
            }
            else/*SL:485*/ if (UUID.class == v-2) {
                /*SL:486*/v2 = UUID.fromString(v-1.getValue());
            }
            else {
                /*SL:488*/if (!Constructor.this.yamlConstructors.containsKey(v-1.getTag())) {
                    /*SL:491*/throw new YAMLException("Unsupported class: " + v-2);
                }
                v2 = Constructor.this.yamlConstructors.get(v-1.getTag()).construct(v-1);
            }
            /*SL:494*/return v2;
        }
    }
    
    protected class ConstructSequence implements Construct
    {
        @Override
        public Object construct(final Node v-7) {
            final SequenceNode a2 = /*EL:505*/(SequenceNode)v-7;
            /*SL:506*/if (Set.class.isAssignableFrom(v-7.getType())) {
                /*SL:507*/if (v-7.isTwoStepsConstruction()) {
                    /*SL:508*/throw new YAMLException("Set cannot be recursive.");
                }
                /*SL:510*/return Constructor.this.constructSet(a2);
            }
            else/*SL:512*/ if (Collection.class.isAssignableFrom(v-7.getType())) {
                /*SL:513*/if (v-7.isTwoStepsConstruction()) {
                    /*SL:514*/return Constructor.this.newList(a2);
                }
                /*SL:516*/return Constructor.this.constructSequence(a2);
            }
            else {
                /*SL:518*/if (!v-7.getType().isArray()) {
                    final List<java.lang.reflect.Constructor<?>> list = /*EL:527*/new ArrayList<java.lang.reflect.Constructor<?>>(a2.getValue().size());
                    /*SL:528*/for (final java.lang.reflect.Constructor<?> a1 : v-7.getType().getDeclaredConstructors()) {
                        /*SL:530*/if (a2.getValue().size() == a1.getParameterTypes().length) {
                            /*SL:531*/list.add(a1);
                        }
                    }
                    /*SL:534*/if (!list.isEmpty()) {
                        /*SL:535*/if (list.size() == 1) {
                            final Object[] array = /*EL:536*/new Object[a2.getValue().size()];
                            final java.lang.reflect.Constructor<?> constructor = /*EL:537*/list.get(0);
                            int n = /*EL:538*/0;
                            /*SL:539*/for (final Node v0 : a2.getValue()) {
                                final Class<?> v = /*EL:540*/constructor.getParameterTypes()[n];
                                /*SL:542*/v0.setType(v);
                                /*SL:543*/array[n++] = Constructor.this.constructObject(v0);
                            }
                            try {
                                /*SL:547*/constructor.setAccessible(true);
                                /*SL:548*/return constructor.newInstance(array);
                            }
                            catch (Exception a3) {
                                /*SL:550*/throw new YAMLException(a3);
                            }
                        }
                        final List<Object> constructSequence = /*EL:555*/(List<Object>)Constructor.this.constructSequence(a2);
                        final Class<?>[] array2 = /*EL:556*/(Class<?>[])new Class[constructSequence.size()];
                        int n = /*EL:557*/0;
                        /*SL:558*/for (final Object v2 : constructSequence) {
                            /*SL:559*/array2[n] = v2.getClass();
                            /*SL:560*/++n;
                        }
                        /*SL:563*/for (final java.lang.reflect.Constructor<?> v3 : list) {
                            final Class<?>[] v4 = /*EL:564*/v3.getParameterTypes();
                            boolean v5 = /*EL:565*/true;
                            /*SL:566*/for (int v6 = 0; v6 < v4.length; ++v6) {
                                /*SL:567*/if (!this.wrapIfPrimitive(v4[v6]).isAssignableFrom(array2[v6])) {
                                    /*SL:568*/v5 = false;
                                    /*SL:569*/break;
                                }
                            }
                            /*SL:572*/if (v5) {
                                try {
                                    /*SL:574*/v3.setAccessible(true);
                                    /*SL:575*/return v3.newInstance(constructSequence.toArray());
                                }
                                catch (Exception v7) {
                                    /*SL:577*/throw new YAMLException(v7);
                                }
                            }
                        }
                    }
                    /*SL:582*/throw new YAMLException("No suitable constructor with " + /*EL:583*/String.valueOf(a2.getValue().size()) + " arguments found for " + v-7.getType());
                }
                if (v-7.isTwoStepsConstruction()) {
                    return Constructor.this.createArray(v-7.getType(), a2.getValue().size());
                }
                return Constructor.this.constructArray(a2);
            }
        }
        
        private final Class<?> wrapIfPrimitive(final Class<?> a1) {
            /*SL:590*/if (!a1.isPrimitive()) {
                /*SL:591*/return a1;
            }
            /*SL:593*/if (a1 == Integer.TYPE) {
                /*SL:594*/return Integer.class;
            }
            /*SL:596*/if (a1 == Float.TYPE) {
                /*SL:597*/return Float.class;
            }
            /*SL:599*/if (a1 == Double.TYPE) {
                /*SL:600*/return Double.class;
            }
            /*SL:602*/if (a1 == Boolean.TYPE) {
                /*SL:603*/return Boolean.class;
            }
            /*SL:605*/if (a1 == Long.TYPE) {
                /*SL:606*/return Long.class;
            }
            /*SL:608*/if (a1 == Character.TYPE) {
                /*SL:609*/return Character.class;
            }
            /*SL:611*/if (a1 == Short.TYPE) {
                /*SL:612*/return Short.class;
            }
            /*SL:614*/if (a1 == Byte.TYPE) {
                /*SL:615*/return Byte.class;
            }
            /*SL:617*/throw new YAMLException("Unexpected primitive " + a1);
        }
        
        @Override
        public void construct2ndStep(final Node v1, final Object v2) {
            final SequenceNode v3 = /*EL:622*/(SequenceNode)v1;
            /*SL:623*/if (List.class.isAssignableFrom(v1.getType())) {
                final List<Object> a1 = /*EL:624*/(List<Object>)v2;
                /*SL:625*/Constructor.this.constructSequenceStep2(v3, a1);
            }
            else {
                /*SL:626*/if (!v1.getType().isArray()) {
                    /*SL:629*/throw new YAMLException("Immutable objects cannot be recursive.");
                }
                Constructor.this.constructArrayStep2(v3, v2);
            }
        }
    }
}

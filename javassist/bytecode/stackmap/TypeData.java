package javassist.bytecode.stackmap;

import java.util.Iterator;
import javassist.bytecode.Descriptor;
import javassist.CtClass;
import java.util.HashSet;
import java.util.ArrayList;
import javassist.NotFoundException;
import javassist.bytecode.ConstPool;
import javassist.bytecode.BadBytecode;
import javassist.ClassPool;

public abstract class TypeData
{
    public static TypeData[] make(final int v1) {
        final TypeData[] v2 = /*EL:36*/new TypeData[v1];
        /*SL:37*/for (int a1 = 0; a1 < v1; ++a1) {
            /*SL:38*/v2[a1] = TypeTag.TOP;
        }
        /*SL:40*/return v2;
    }
    
    private static void setType(final TypeData a1, final String a2, final ClassPool a3) throws BadBytecode {
        /*SL:53*/a1.setType(a2, a3);
    }
    
    public abstract int getTypeTag();
    
    public abstract int getTypeData(final ConstPool p0);
    
    public TypeData join() {
        /*SL:59*/return new TypeVar(this);
    }
    
    public abstract BasicType isBasicType();
    
    public abstract boolean is2WordType();
    
    public boolean isNullType() {
        /*SL:72*/return false;
    }
    
    public boolean isUninit() {
        /*SL:74*/return false;
    }
    
    public abstract boolean eq(final TypeData p0);
    
    public abstract String getName();
    
    public abstract void setType(final String p0, final ClassPool p1) throws BadBytecode;
    
    public abstract TypeData getArrayType(final int p0) throws NotFoundException;
    
    public int dfs(final ArrayList a1, final int a2, final ClassPool a3) throws NotFoundException {
        /*SL:95*/return a2;
    }
    
    protected TypeVar toTypeVar(final int a1) {
        /*SL:105*/return null;
    }
    
    public void constructorCalled(final int a1) {
    }
    
    @Override
    public String toString() {
        /*SL:111*/return super.toString() + "(" + this.toString2(new HashSet()) + ")";
    }
    
    abstract String toString2(final HashSet p0);
    
    public static CtClass commonSuperClassEx(final CtClass v-3, final CtClass v-2) throws NotFoundException {
        /*SL:481*/if (v-3 == v-2) {
            /*SL:482*/return v-3;
        }
        /*SL:483*/if (v-3.isArray() && v-2.isArray()) {
            final CtClass a1 = /*EL:484*/v-3.getComponentType();
            final CtClass a2 = /*EL:485*/v-2.getComponentType();
            final CtClass v1 = commonSuperClassEx(/*EL:486*/a1, a2);
            /*SL:487*/if (v1 == a1) {
                /*SL:488*/return v-3;
            }
            /*SL:489*/if (v1 == a2) {
                /*SL:490*/return v-2;
            }
            /*SL:492*/return v-3.getClassPool().get((v1 == null) ? "java.lang.Object" : (v1.getName() + /*EL:493*/"[]"));
        }
        else {
            /*SL:495*/if (v-3.isPrimitive() || v-2.isPrimitive()) {
                /*SL:496*/return null;
            }
            /*SL:497*/if (v-3.isArray() || v-2.isArray()) {
                /*SL:498*/return v-3.getClassPool().get("java.lang.Object");
            }
            /*SL:500*/return commonSuperClass(v-3, v-2);
        }
    }
    
    public static CtClass commonSuperClass(final CtClass v1, final CtClass v2) throws NotFoundException {
        CtClass v3 = /*EL:508*/v1;
        CtClass v5;
        CtClass v4 = /*EL:510*/v5 = v2;
        CtClass v6 = /*EL:511*/v3;
        /*SL:516*/while (!eq(v3, v4) || v3.getSuperclass() == null) {
            final CtClass a1 = /*EL:519*/v3.getSuperclass();
            final CtClass a2 = /*EL:520*/v4.getSuperclass();
            /*SL:522*/if (a2 == null) {
                /*SL:524*/v4 = v5;
            }
            else {
                /*SL:528*/if (a1 != null) {
                    /*SL:539*/v3 = a1;
                    /*SL:540*/v4 = a2;
                    /*SL:541*/continue;
                }
                v3 = v6;
                v6 = v5;
                v5 = v3;
                v3 = v4;
                v4 = v5;
            }
            while (true) {
                /*SL:545*/v3 = v3.getSuperclass();
                /*SL:546*/if (v3 == null) {
                    break;
                }
                /*SL:549*/v6 = v6.getSuperclass();
            }
            /*SL:556*/for (v3 = v6; !eq(v3, v4); /*SL:557*/v3 = v3.getSuperclass(), /*SL:558*/v4 = v4.getSuperclass()) {}
            /*SL:561*/return v3;
        }
        return v3;
    }
    
    static boolean eq(final CtClass a1, final CtClass a2) {
        /*SL:565*/return a1 == a2 || (a1 != null && a2 != null && a1.getName().equals(a2.getName()));
    }
    
    public static void aastore(final TypeData a2, final TypeData a3, final ClassPool v1) throws BadBytecode {
        /*SL:569*/if (a2 instanceof AbsTypeVar && /*EL:570*/!a3.isNullType()) {
            /*SL:571*/((AbsTypeVar)a2).merge(ArrayType.make(a3));
        }
        /*SL:573*/if (a3 instanceof AbsTypeVar) {
            /*SL:574*/if (a2 instanceof AbsTypeVar) {
                /*SL:575*/ArrayElement.make(a2);
            }
            else {
                /*SL:576*/if (!(a2 instanceof ClassName)) {
                    /*SL:583*/throw new BadBytecode("bad AASTORE: " + a2);
                }
                if (!a2.isNullType()) {
                    final String a4 = typeName(a2.getName());
                    a3.setType(a4, v1);
                }
            }
        }
    }
    
    protected static class BasicType extends TypeData
    {
        private String name;
        private int typeTag;
        private char decodedName;
        
        public BasicType(final String a1, final int a2, final char a3) {
            this.name = a1;
            this.typeTag = a2;
            this.decodedName = a3;
        }
        
        @Override
        public int getTypeTag() {
            /*SL:130*/return this.typeTag;
        }
        
        @Override
        public int getTypeData(final ConstPool a1) {
            /*SL:131*/return 0;
        }
        
        @Override
        public TypeData join() {
            /*SL:134*/if (this == TypeTag.TOP) {
                /*SL:135*/return this;
            }
            /*SL:137*/return super.join();
        }
        
        @Override
        public BasicType isBasicType() {
            /*SL:140*/return this;
        }
        
        @Override
        public boolean is2WordType() {
            /*SL:143*/return this.typeTag == 4 || this.typeTag == 3;
        }
        
        @Override
        public boolean eq(final TypeData a1) {
            /*SL:147*/return this == a1;
        }
        
        @Override
        public String getName() {
            /*SL:150*/return this.name;
        }
        
        public char getDecodedName() {
            /*SL:153*/return this.decodedName;
        }
        
        @Override
        public void setType(final String a1, final ClassPool a2) throws BadBytecode {
            /*SL:156*/throw new BadBytecode("conflict: " + this.name + " and " + a1);
        }
        
        @Override
        public TypeData getArrayType(final int v0) throws NotFoundException {
            /*SL:163*/if (this == TypeTag.TOP) {
                /*SL:164*/return this;
            }
            /*SL:165*/if (v0 < 0) {
                /*SL:166*/throw new NotFoundException("no element type: " + this.name);
            }
            /*SL:167*/if (v0 == 0) {
                /*SL:168*/return this;
            }
            final char[] v = /*EL:170*/new char[v0 + 1];
            /*SL:171*/for (int a1 = 0; a1 < v0; ++a1) {
                /*SL:172*/v[a1] = '[';
            }
            /*SL:174*/v[v0] = this.decodedName;
            /*SL:175*/return new ClassName(new String(v));
        }
        
        @Override
        String toString2(final HashSet a1) {
            /*SL:179*/return this.name;
        }
    }
    
    public abstract static class AbsTypeVar extends TypeData
    {
        public abstract void merge(final TypeData p0);
        
        @Override
        public int getTypeTag() {
            /*SL:186*/return 7;
        }
        
        @Override
        public int getTypeData(final ConstPool a1) {
            /*SL:189*/return a1.addClassInfo(this.getName());
        }
        
        @Override
        public boolean eq(final TypeData a1) {
            /*SL:192*/return this.getName().equals(a1.getName());
        }
    }
    
    public static class TypeVar extends AbsTypeVar
    {
        protected ArrayList lowers;
        protected ArrayList usedBy;
        protected ArrayList uppers;
        protected String fixedType;
        private boolean is2WordType;
        private int visited;
        private int smallest;
        private boolean inList;
        private int dimension;
        
        public TypeVar(final TypeData a1) {
            this.visited = 0;
            this.smallest = 0;
            this.inList = false;
            this.dimension = 0;
            this.uppers = null;
            this.lowers = new ArrayList(2);
            this.usedBy = new ArrayList(2);
            this.merge(a1);
            this.fixedType = null;
            this.is2WordType = a1.is2WordType();
        }
        
        @Override
        public String getName() {
            /*SL:214*/if (this.fixedType == null) {
                /*SL:215*/return this.lowers.get(0).getName();
            }
            /*SL:217*/return this.fixedType;
        }
        
        @Override
        public BasicType isBasicType() {
            /*SL:221*/if (this.fixedType == null) {
                /*SL:222*/return this.lowers.get(0).isBasicType();
            }
            /*SL:224*/return null;
        }
        
        @Override
        public boolean is2WordType() {
            /*SL:228*/return this.fixedType == null && /*EL:229*/this.is2WordType;
        }
        
        @Override
        public boolean isNullType() {
            /*SL:237*/return this.fixedType == null && /*EL:238*/this.lowers.get(0).isNullType();
        }
        
        @Override
        public boolean isUninit() {
            /*SL:244*/return this.fixedType == null && /*EL:245*/this.lowers.get(0).isUninit();
        }
        
        @Override
        public void merge(final TypeData a1) {
            /*SL:251*/this.lowers.add(a1);
            /*SL:252*/if (a1 instanceof TypeVar) {
                /*SL:253*/((TypeVar)a1).usedBy.add(this);
            }
        }
        
        @Override
        public int getTypeTag() {
            /*SL:260*/if (this.fixedType == null) {
                /*SL:261*/return this.lowers.get(0).getTypeTag();
            }
            /*SL:263*/return super.getTypeTag();
        }
        
        @Override
        public int getTypeData(final ConstPool a1) {
            /*SL:267*/if (this.fixedType == null) {
                /*SL:268*/return this.lowers.get(0).getTypeData(a1);
            }
            /*SL:270*/return super.getTypeData(a1);
        }
        
        @Override
        public void setType(final String a1, final ClassPool a2) throws BadBytecode {
            /*SL:274*/if (this.uppers == null) {
                /*SL:275*/this.uppers = new ArrayList();
            }
            /*SL:277*/this.uppers.add(a1);
        }
        
        @Override
        protected TypeVar toTypeVar(final int a1) {
            /*SL:286*/this.dimension = a1;
            /*SL:287*/return this;
        }
        
        @Override
        public TypeData getArrayType(final int v2) throws NotFoundException {
            /*SL:294*/if (v2 == 0) {
                /*SL:295*/return this;
            }
            final BasicType a1 = /*EL:297*/this.isBasicType();
            /*SL:298*/if (a1 != null) {
                /*SL:304*/return a1.getArrayType(v2);
            }
            if (this.isNullType()) {
                return new NullType();
            }
            return new ClassName(this.getName()).getArrayType(v2);
        }
        
        @Override
        public int dfs(final ArrayList v-4, int v-3, final ClassPool v-2) throws NotFoundException {
            /*SL:310*/if (this.visited > 0) {
                /*SL:311*/return v-3;
            }
            final int n = /*EL:313*/++v-3;
            this.smallest = n;
            this.visited = n;
            /*SL:314*/v-4.add(this);
            /*SL:315*/this.inList = true;
            final int size = /*EL:316*/this.lowers.size();
            /*SL:317*/for (TypeVar a2 = (TypeVar)0; a2 < size; ++a2) {
                /*SL:318*/a2 = this.lowers.get(a2).toTypeVar(this.dimension);
                /*SL:319*/if (a2 != null) {
                    /*SL:320*/if (a2.visited == 0) {
                        /*SL:321*/v-3 = a2.dfs(v-4, v-3, v-2);
                        /*SL:322*/if (a2.smallest < this.smallest) {
                            /*SL:323*/this.smallest = a2.smallest;
                        }
                    }
                    else/*SL:325*/ if (a2.inList && /*EL:326*/a2.visited < this.smallest) {
                        /*SL:327*/this.smallest = a2.visited;
                    }
                }
            }
            /*SL:330*/if (this.visited == this.smallest) {
                final ArrayList a3 = /*EL:331*/new ArrayList();
                TypeVar v1;
                /*SL:337*/do {
                    v1 = v-4.remove(v-4.size() - 1);
                    v1.inList = false;
                    a3.add(v1);
                } while (v1 != this);
                /*SL:338*/this.fixTypes(a3, v-2);
            }
            /*SL:341*/return v-3;
        }
        
        private void fixTypes(final ArrayList v-12, final ClassPool v-11) throws NotFoundException {
            final HashSet v2 = /*EL:345*/new HashSet();
            boolean b = /*EL:346*/false;
            TypeData top = /*EL:347*/null;
            /*SL:349*/for (int size = v-12.size(), i = 0; i < size; ++i) {
                final TypeVar typeVar = /*EL:350*/v-12.get(i);
                final ArrayList lowers = /*EL:351*/typeVar.lowers;
                /*SL:353*/for (int size2 = lowers.size(), j = 0; j < size2; ++j) {
                    final TypeData a1 = /*EL:354*/lowers.get(j);
                    final TypeData a2 = /*EL:355*/a1.getArrayType(typeVar.dimension);
                    final BasicType v1 = /*EL:356*/a2.isBasicType();
                    /*SL:357*/if (top == null) {
                        /*SL:358*/if (v1 == null) {
                            /*SL:359*/b = false;
                            /*SL:360*/top = a2;
                            /*SL:365*/if (a2.isUninit()) {
                                /*SL:366*/break;
                            }
                        }
                        else {
                            /*SL:369*/b = true;
                            /*SL:370*/top = v1;
                        }
                    }
                    else/*SL:374*/ if ((v1 == null && b) || (v1 != null && top != v1)) {
                        /*SL:375*/b = true;
                        /*SL:376*/top = TypeTag.TOP;
                        /*SL:377*/break;
                    }
                    /*SL:381*/if (v1 == null && !a2.isNullType()) {
                        /*SL:382*/v2.add(a2.getName());
                    }
                }
            }
            /*SL:386*/if (b) {
                /*SL:387*/this.is2WordType = top.is2WordType();
                /*SL:388*/this.fixTypes1(v-12, top);
            }
            else {
                final String fixTypes2 = /*EL:391*/this.fixTypes2(v-12, v2, v-11);
                /*SL:392*/this.fixTypes1(v-12, new ClassName(fixTypes2));
            }
        }
        
        private void fixTypes1(final ArrayList v-2, final TypeData v-1) throws NotFoundException {
            /*SL:398*/for (int v0 = v-2.size(), v = 0; v < v0; ++v) {
                final TypeVar a1 = /*EL:399*/v-2.get(v);
                final TypeData a2 = /*EL:400*/v-1.getArrayType(-a1.dimension);
                /*SL:401*/if (a2.isBasicType() == null) {
                    /*SL:402*/a1.fixedType = a2.getName();
                }
                else {
                    /*SL:404*/a1.lowers.clear();
                    /*SL:405*/a1.lowers.add(a2);
                    /*SL:406*/a1.is2WordType = a2.is2WordType();
                }
            }
        }
        
        private String fixTypes2(final ArrayList a3, final HashSet v1, final ClassPool v2) throws NotFoundException {
            final Iterator v3 = /*EL:412*/v1.iterator();
            /*SL:413*/if (v1.size() == 0) {
                /*SL:414*/return null;
            }
            /*SL:415*/if (v1.size() == 1) {
                /*SL:416*/return v3.next();
            }
            CtClass a4 = /*EL:418*/v2.get(v3.next());
            /*SL:419*/while (v3.hasNext()) {
                /*SL:420*/a4 = TypeData.commonSuperClassEx(a4, v2.get(v3.next()));
            }
            /*SL:422*/if (a4.getSuperclass() == null || isObjectArray(a4)) {
                /*SL:423*/a4 = this.fixByUppers(a3, v2, new HashSet(), a4);
            }
            /*SL:425*/if (a4.isArray()) {
                /*SL:426*/return Descriptor.toJvmName(a4);
            }
            /*SL:428*/return a4.getName();
        }
        
        private static boolean isObjectArray(final CtClass a1) throws NotFoundException {
            /*SL:433*/return a1.isArray() && a1.getComponentType().getSuperclass() == null;
        }
        
        private CtClass fixByUppers(final ArrayList v-4, final ClassPool v-3, final HashSet v-2, CtClass v-1) throws NotFoundException {
            /*SL:439*/if (v-4 == null) {
                /*SL:440*/return v-1;
            }
            /*SL:443*/for (int v0 = v-4.size(), v = 0; v < v0; ++v) {
                TypeVar a4 = /*EL:444*/v-4.get(v);
                /*SL:445*/if (!v-2.add(a4)) {
                    /*SL:446*/return v-1;
                }
                /*SL:448*/if (a4.uppers != null) {
                    /*SL:450*/for (int a2 = a4.uppers.size(), a3 = 0; a3 < a2; ++a3) {
                        /*SL:451*/a4 = v-3.get(a4.uppers.get(a3));
                        /*SL:452*/if (a4.subtypeOf(v-1)) {
                            /*SL:453*/v-1 = a4;
                        }
                    }
                }
                /*SL:457*/v-1 = this.fixByUppers(a4.usedBy, v-3, v-2, v-1);
            }
            /*SL:460*/return v-1;
        }
        
        @Override
        String toString2(final HashSet v2) {
            /*SL:464*/v2.add(this);
            /*SL:465*/if (this.lowers.size() > 0) {
                final TypeData a1 = /*EL:466*/this.lowers.get(0);
                /*SL:467*/if (a1 != null && !v2.contains(a1)) {
                    /*SL:468*/return a1.toString2(v2);
                }
            }
            /*SL:472*/return "?";
        }
    }
    
    public static class ArrayType extends AbsTypeVar
    {
        private AbsTypeVar element;
        
        private ArrayType(final AbsTypeVar a1) {
            this.element = a1;
        }
        
        static TypeData make(final TypeData a1) throws BadBytecode {
            /*SL:597*/if (a1 instanceof ArrayElement) {
                /*SL:598*/return ((ArrayElement)a1).arrayType();
            }
            /*SL:599*/if (a1 instanceof AbsTypeVar) {
                /*SL:600*/return new ArrayType((AbsTypeVar)a1);
            }
            /*SL:601*/if (a1 instanceof ClassName && /*EL:602*/!a1.isNullType()) {
                /*SL:603*/return new ClassName(typeName(a1.getName()));
            }
            /*SL:605*/throw new BadBytecode("bad AASTORE: " + a1);
        }
        
        @Override
        public void merge(final TypeData v2) {
            try {
                /*SL:610*/if (!v2.isNullType()) {
                    /*SL:611*/this.element.merge(ArrayElement.make(v2));
                }
            }
            catch (BadBytecode a1) {
                /*SL:615*/throw new RuntimeException("fatal: " + a1);
            }
        }
        
        @Override
        public String getName() {
            /*SL:620*/return typeName(this.element.getName());
        }
        
        public AbsTypeVar elementType() {
            /*SL:623*/return this.element;
        }
        
        @Override
        public BasicType isBasicType() {
            /*SL:625*/return null;
        }
        
        @Override
        public boolean is2WordType() {
            /*SL:626*/return false;
        }
        
        public static String typeName(final String a1) {
            /*SL:632*/if (a1.charAt(0) == '[') {
                /*SL:633*/return "[" + a1;
            }
            /*SL:635*/return "[L" + a1.replace('.', '/') + ";";
        }
        
        @Override
        public void setType(final String a1, final ClassPool a2) throws BadBytecode {
            /*SL:639*/this.element.setType(typeName(a1), a2);
        }
        
        @Override
        protected TypeVar toTypeVar(final int a1) {
            /*SL:642*/return this.element.toTypeVar(a1 + 1);
        }
        
        @Override
        public TypeData getArrayType(final int a1) throws NotFoundException {
            /*SL:645*/return this.element.getArrayType(a1 + 1);
        }
        
        @Override
        public int dfs(final ArrayList a1, final int a2, final ClassPool a3) throws NotFoundException {
            /*SL:649*/return this.element.dfs(a1, a2, a3);
        }
        
        @Override
        String toString2(final HashSet a1) {
            /*SL:653*/return "[" + this.element.toString2(a1);
        }
    }
    
    public static class ArrayElement extends AbsTypeVar
    {
        private AbsTypeVar array;
        
        private ArrayElement(final AbsTypeVar a1) {
            this.array = a1;
        }
        
        public static TypeData make(final TypeData a1) throws BadBytecode {
            /*SL:668*/if (a1 instanceof ArrayType) {
                /*SL:669*/return ((ArrayType)a1).elementType();
            }
            /*SL:670*/if (a1 instanceof AbsTypeVar) {
                /*SL:671*/return new ArrayElement((AbsTypeVar)a1);
            }
            /*SL:672*/if (a1 instanceof ClassName && /*EL:673*/!a1.isNullType()) {
                /*SL:674*/return new ClassName(typeName(a1.getName()));
            }
            /*SL:676*/throw new BadBytecode("bad AASTORE: " + a1);
        }
        
        @Override
        public void merge(final TypeData v2) {
            try {
                /*SL:681*/if (!v2.isNullType()) {
                    /*SL:682*/this.array.merge(ArrayType.make(v2));
                }
            }
            catch (BadBytecode a1) {
                /*SL:686*/throw new RuntimeException("fatal: " + a1);
            }
        }
        
        @Override
        public String getName() {
            /*SL:691*/return typeName(this.array.getName());
        }
        
        public AbsTypeVar arrayType() {
            /*SL:694*/return this.array;
        }
        
        @Override
        public BasicType isBasicType() {
            /*SL:700*/return null;
        }
        
        @Override
        public boolean is2WordType() {
            /*SL:702*/return false;
        }
        
        private static String typeName(final String v1) {
            /*SL:705*/if (v1.length() > 1 && v1.charAt(0) == '[') {
                final char a1 = /*EL:706*/v1.charAt(1);
                /*SL:707*/if (a1 == 'L') {
                    /*SL:708*/return v1.substring(2, v1.length() - 1).replace('/', '.');
                }
                /*SL:709*/if (a1 == '[') {
                    /*SL:710*/return v1.substring(1);
                }
            }
            /*SL:713*/return "java.lang.Object";
        }
        
        @Override
        public void setType(final String a1, final ClassPool a2) throws BadBytecode {
            /*SL:717*/this.array.setType(ArrayType.typeName(a1), a2);
        }
        
        @Override
        protected TypeVar toTypeVar(final int a1) {
            /*SL:720*/return this.array.toTypeVar(a1 - 1);
        }
        
        @Override
        public TypeData getArrayType(final int a1) throws NotFoundException {
            /*SL:723*/return this.array.getArrayType(a1 - 1);
        }
        
        @Override
        public int dfs(final ArrayList a1, final int a2, final ClassPool a3) throws NotFoundException {
            /*SL:727*/return this.array.dfs(a1, a2, a3);
        }
        
        @Override
        String toString2(final HashSet a1) {
            /*SL:731*/return "*" + this.array.toString2(a1);
        }
    }
    
    public static class UninitTypeVar extends AbsTypeVar
    {
        protected TypeData type;
        
        public UninitTypeVar(final UninitData a1) {
            this.type = a1;
        }
        
        @Override
        public int getTypeTag() {
            /*SL:739*/return this.type.getTypeTag();
        }
        
        @Override
        public int getTypeData(final ConstPool a1) {
            /*SL:740*/return this.type.getTypeData(a1);
        }
        
        @Override
        public BasicType isBasicType() {
            /*SL:741*/return this.type.isBasicType();
        }
        
        @Override
        public boolean is2WordType() {
            /*SL:742*/return this.type.is2WordType();
        }
        
        @Override
        public boolean isUninit() {
            /*SL:743*/return this.type.isUninit();
        }
        
        @Override
        public boolean eq(final TypeData a1) {
            /*SL:744*/return this.type.eq(a1);
        }
        
        @Override
        public String getName() {
            /*SL:745*/return this.type.getName();
        }
        
        @Override
        protected TypeVar toTypeVar(final int a1) {
            /*SL:747*/return null;
        }
        
        @Override
        public TypeData join() {
            /*SL:748*/return this.type.join();
        }
        
        @Override
        public void setType(final String a1, final ClassPool a2) throws BadBytecode {
            /*SL:751*/this.type.setType(a1, a2);
        }
        
        @Override
        public void merge(final TypeData a1) {
            /*SL:755*/if (!a1.eq(this.type)) {
                /*SL:756*/this.type = TypeTag.TOP;
            }
        }
        
        @Override
        public void constructorCalled(final int a1) {
            /*SL:760*/this.type.constructorCalled(a1);
        }
        
        public int offset() {
            /*SL:764*/if (this.type instanceof UninitData) {
                /*SL:765*/return ((UninitData)this.type).offset;
            }
            /*SL:767*/throw new RuntimeException("not available");
        }
        
        @Override
        public TypeData getArrayType(final int a1) throws NotFoundException {
            /*SL:771*/return this.type.getArrayType(a1);
        }
        
        @Override
        String toString2(final HashSet a1) {
            /*SL:774*/return "";
        }
    }
    
    public static class ClassName extends TypeData
    {
        private String name;
        
        public ClassName(final String a1) {
            this.name = a1;
        }
        
        @Override
        public String getName() {
            /*SL:788*/return this.name;
        }
        
        @Override
        public BasicType isBasicType() {
            /*SL:791*/return null;
        }
        
        @Override
        public boolean is2WordType() {
            /*SL:793*/return false;
        }
        
        @Override
        public int getTypeTag() {
            /*SL:795*/return 7;
        }
        
        @Override
        public int getTypeData(final ConstPool a1) {
            /*SL:798*/return a1.addClassInfo(this.getName());
        }
        
        @Override
        public boolean eq(final TypeData a1) {
            /*SL:801*/return this.name.equals(a1.getName());
        }
        
        @Override
        public void setType(final String a1, final ClassPool a2) throws BadBytecode {
        }
        
        @Override
        public TypeData getArrayType(final int v0) throws NotFoundException {
            /*SL:806*/if (v0 == 0) {
                /*SL:807*/return this;
            }
            /*SL:808*/if (v0 > 0) {
                final char[] v = /*EL:809*/new char[v0];
                /*SL:810*/for (int a1 = 0; a1 < v0; ++a1) {
                    /*SL:811*/v[a1] = '[';
                }
                String v2 = /*EL:813*/this.getName();
                /*SL:814*/if (v2.charAt(0) != '[') {
                    /*SL:815*/v2 = "L" + v2.replace('.', '/') + ";";
                }
                /*SL:817*/return new ClassName(new String(v) + v2);
            }
            /*SL:820*/for (int v3 = 0; v3 < -v0; ++v3) {
                /*SL:821*/if (this.name.charAt(v3) != '[') {
                    /*SL:822*/throw new NotFoundException("no " + v0 + " dimensional array type: " + this.getName());
                }
            }
            final char v4 = /*EL:824*/this.name.charAt(-v0);
            /*SL:825*/if (v4 == '[') {
                /*SL:826*/return new ClassName(this.name.substring(-v0));
            }
            /*SL:827*/if (v4 == 'L') {
                /*SL:828*/return new ClassName(this.name.substring(-v0 + 1, this.name.length() - 1).replace('/', '.'));
            }
            /*SL:829*/if (v4 == TypeTag.DOUBLE.decodedName) {
                /*SL:830*/return TypeTag.DOUBLE;
            }
            /*SL:831*/if (v4 == TypeTag.FLOAT.decodedName) {
                /*SL:832*/return TypeTag.FLOAT;
            }
            /*SL:833*/if (v4 == TypeTag.LONG.decodedName) {
                /*SL:834*/return TypeTag.LONG;
            }
            /*SL:836*/return TypeTag.INTEGER;
        }
        
        @Override
        String toString2(final HashSet a1) {
            /*SL:841*/return this.name;
        }
    }
    
    public static class NullType extends ClassName
    {
        public NullType() {
            super("null-type");
        }
        
        @Override
        public int getTypeTag() {
            /*SL:856*/return 5;
        }
        
        @Override
        public boolean isNullType() {
            /*SL:859*/return true;
        }
        
        @Override
        public int getTypeData(final ConstPool a1) {
            /*SL:860*/return 0;
        }
        
        @Override
        public TypeData getArrayType(final int a1) {
            /*SL:862*/return this;
        }
    }
    
    public static class UninitData extends ClassName
    {
        int offset;
        boolean initialized;
        
        UninitData(final int a1, final String a2) {
            super(a2);
            this.offset = a1;
            this.initialized = false;
        }
        
        public UninitData copy() {
            /*SL:878*/return new UninitData(this.offset, this.getName());
        }
        
        @Override
        public int getTypeTag() {
            /*SL:881*/return 8;
        }
        
        @Override
        public int getTypeData(final ConstPool a1) {
            /*SL:885*/return this.offset;
        }
        
        @Override
        public TypeData join() {
            /*SL:889*/if (this.initialized) {
                /*SL:890*/return new TypeVar(new ClassName(this.getName()));
            }
            /*SL:892*/return new UninitTypeVar(this.copy());
        }
        
        @Override
        public boolean isUninit() {
            /*SL:895*/return true;
        }
        
        @Override
        public boolean eq(final TypeData v2) {
            /*SL:898*/if (v2 instanceof UninitData) {
                final UninitData a1 = /*EL:899*/(UninitData)v2;
                /*SL:900*/return this.offset == a1.offset && this.getName().equals(a1.getName());
            }
            /*SL:903*/return false;
        }
        
        public int offset() {
            /*SL:906*/return this.offset;
        }
        
        @Override
        public void constructorCalled(final int a1) {
            /*SL:909*/if (a1 == this.offset) {
                /*SL:910*/this.initialized = true;
            }
        }
        
        @Override
        String toString2(final HashSet a1) {
            /*SL:913*/return this.getName() + "," + this.offset;
        }
    }
    
    public static class UninitThis extends UninitData
    {
        UninitThis(final String a1) {
            super(-1, a1);
        }
        
        @Override
        public UninitData copy() {
            /*SL:921*/return new UninitThis(this.getName());
        }
        
        @Override
        public int getTypeTag() {
            /*SL:924*/return 6;
        }
        
        @Override
        public int getTypeData(final ConstPool a1) {
            /*SL:928*/return 0;
        }
        
        @Override
        String toString2(final HashSet a1) {
            /*SL:931*/return "uninit:this";
        }
    }
}

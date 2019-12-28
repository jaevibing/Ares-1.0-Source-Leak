package org.spongepowered.asm.util;

import org.spongepowered.asm.lib.signature.SignatureWriter;
import org.spongepowered.asm.lib.tree.ClassNode;
import java.util.Set;
import java.util.HashSet;
import java.util.ListIterator;
import java.util.Iterator;
import org.spongepowered.asm.lib.signature.SignatureVisitor;
import org.spongepowered.asm.lib.signature.SignatureReader;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Deque;
import java.util.List;
import java.util.Map;

public class ClassSignature
{
    protected static final String OBJECT = "java/lang/Object";
    private final Map<TypeVar, TokenHandle> types;
    private Token superClass;
    private final List<Token> interfaces;
    private final Deque<String> rawInterfaces;
    
    ClassSignature() {
        this.types = new LinkedHashMap<TypeVar, TokenHandle>();
        this.superClass = new Token("java/lang/Object");
        this.interfaces = new ArrayList<Token>();
        this.rawInterfaces = new LinkedList<String>();
    }
    
    private ClassSignature read(final String v2) {
        /*SL:990*/if (v2 != null) {
            try {
                /*SL:992*/new SignatureReader(v2).accept(new SignatureParser());
            }
            catch (Exception a1) {
                /*SL:994*/a1.printStackTrace();
            }
        }
        /*SL:997*/return this;
    }
    
    protected TypeVar getTypeVar(final String v2) {
        /*SL:1007*/for (final TypeVar a1 : this.types.keySet()) {
            /*SL:1008*/if (a1.matches(v2)) {
                /*SL:1009*/return a1;
            }
        }
        /*SL:1012*/return null;
    }
    
    protected TokenHandle getType(final String v2) {
        /*SL:1022*/for (final TypeVar a1 : this.types.keySet()) {
            /*SL:1023*/if (a1.matches(v2)) {
                /*SL:1024*/return this.types.get(a1);
            }
        }
        final TokenHandle v3 = /*EL:1028*/new TokenHandle();
        /*SL:1029*/this.types.put(new TypeVar(v2), v3);
        /*SL:1030*/return v3;
    }
    
    protected String getTypeVar(final TokenHandle v-3) {
        /*SL:1041*/for (final Map.Entry<TypeVar, TokenHandle> entry : this.types.entrySet()) {
            final TypeVar a1 = /*EL:1042*/entry.getKey();
            final TokenHandle v1 = /*EL:1043*/entry.getValue();
            /*SL:1044*/if (v-3 == v1 || v-3.asToken() == v1.asToken()) {
                /*SL:1045*/return "T" + a1 + ";";
            }
        }
        /*SL:1048*/return v-3.token.asType();
    }
    
    protected void addTypeVar(final TypeVar a1, final TokenHandle a2) throws IllegalArgumentException {
        /*SL:1059*/if (this.types.containsKey(a1)) {
            /*SL:1060*/throw new IllegalArgumentException("TypeVar " + a1 + " is already present on " + this);
        }
        /*SL:1063*/this.types.put(a1, a2);
    }
    
    protected void setSuperClass(final Token a1) {
        /*SL:1072*/this.superClass = a1;
    }
    
    public String getSuperClass() {
        /*SL:1081*/return this.superClass.asType(true);
    }
    
    protected void addInterface(final Token v-1) {
        /*SL:1090*/if (!v-1.isRaw()) {
            final String v0 = /*EL:1091*/v-1.asType(true);
            final ListIterator<Token> v = /*EL:1092*/this.interfaces.listIterator();
            while (v.hasNext()) {
                final Token a1 = /*EL:1093*/v.next();
                /*SL:1094*/if (a1.isRaw() && a1.asType(true).equals(v0)) {
                    /*SL:1095*/v.set(v-1);
                    /*SL:1096*/return;
                }
            }
        }
        /*SL:1101*/this.interfaces.add(v-1);
    }
    
    public void addInterface(final String a1) {
        /*SL:1110*/this.rawInterfaces.add(a1);
    }
    
    protected void addRawInterface(final String v2) {
        final Token v3 = /*EL:1119*/new Token(v2);
        final String v4 = /*EL:1120*/v3.asType(true);
        /*SL:1121*/for (final Token a1 : this.interfaces) {
            /*SL:1122*/if (a1.asType(true).equals(v4)) {
                /*SL:1123*/return;
            }
        }
        /*SL:1126*/this.interfaces.add(v3);
    }
    
    public void merge(final ClassSignature v0) {
        try {
            final Set<String> v = /*EL:1140*/new HashSet<String>();
            /*SL:1141*/for (final TypeVar a1 : this.types.keySet()) {
                /*SL:1142*/v.add(a1.toString());
            }
            /*SL:1145*/v0.conform(v);
        }
        catch (IllegalStateException v2) {
            /*SL:1148*/v2.printStackTrace();
            /*SL:1149*/return;
        }
        /*SL:1152*/for (final Map.Entry<TypeVar, TokenHandle> v3 : v0.types.entrySet()) {
            /*SL:1153*/this.addTypeVar(v3.getKey(), v3.getValue());
        }
        /*SL:1156*/for (final Token v4 : v0.interfaces) {
            /*SL:1157*/this.addInterface(v4);
        }
    }
    
    private void conform(final Set<String> v-1) {
        /*SL:1162*/for (final TypeVar v1 : this.types.keySet()) {
            final String a1 = /*EL:1163*/this.findUniqueName(v1.getOriginalName(), v-1);
            /*SL:1164*/v1.rename(a1);
            /*SL:1165*/v-1.add(a1);
        }
    }
    
    private String findUniqueName(final String v1, final Set<String> v2) {
        /*SL:1178*/if (!v2.contains(v1)) {
            /*SL:1179*/return v1;
        }
        /*SL:1182*/if (v1.length() == 1) {
            final String a1 = /*EL:1183*/this.findOffsetName(v1.charAt(0), v2);
            /*SL:1184*/if (a1 != null) {
                /*SL:1185*/return a1;
            }
        }
        String v3 = /*EL:1189*/this.findOffsetName('T', v2, "", v1);
        /*SL:1190*/if (v3 != null) {
            /*SL:1191*/return v3;
        }
        /*SL:1194*/v3 = this.findOffsetName('T', v2, v1, "");
        /*SL:1195*/if (v3 != null) {
            /*SL:1196*/return v3;
        }
        /*SL:1199*/v3 = this.findOffsetName('T', v2, "T", v1);
        /*SL:1200*/if (v3 != null) {
            /*SL:1201*/return v3;
        }
        /*SL:1204*/v3 = this.findOffsetName('T', v2, "", v1 + "Type");
        /*SL:1205*/if (v3 != null) {
            /*SL:1206*/return v3;
        }
        /*SL:1209*/throw new IllegalStateException("Failed to conform type var: " + v1);
    }
    
    private String findOffsetName(final char a1, final Set<String> a2) {
        /*SL:1220*/return this.findOffsetName(a1, a2, "", "");
    }
    
    private String findOffsetName(final char a3, final Set<String> a4, final String v1, final String v2) {
        String v3 = /*EL:1233*/String.format("%s%s%s", v1, a3, v2);
        /*SL:1234*/if (!a4.contains(v3)) {
            /*SL:1235*/return v3;
        }
        /*SL:1238*/if (a3 > '@' && a3 < '[') {
            /*SL:1239*/for (int a5 = a3 - '@'; a5 + 65 != a3; a5 = ++a5 % 26) {
                /*SL:1240*/v3 = String.format("%s%s%s", v1, (char)(a5 + 65), v2);
                /*SL:1241*/if (!a4.contains(v3)) {
                    /*SL:1242*/return v3;
                }
            }
        }
        /*SL:1247*/return null;
    }
    
    public SignatureVisitor getRemapper() {
        /*SL:1256*/return new SignatureRemapper();
    }
    
    @Override
    public String toString() {
        /*SL:1267*/while (this.rawInterfaces.size() > 0) {
            /*SL:1268*/this.addRawInterface(this.rawInterfaces.remove());
        }
        final StringBuilder sb = /*EL:1271*/new StringBuilder();
        /*SL:1273*/if (this.types.size() > 0) {
            boolean b = /*EL:1274*/false;
            final StringBuilder sb2 = /*EL:1275*/new StringBuilder();
            /*SL:1276*/for (final Map.Entry<TypeVar, TokenHandle> v0 : this.types.entrySet()) {
                final String v = /*EL:1277*/v0.getValue().asBound();
                /*SL:1278*/if (!v.isEmpty()) {
                    /*SL:1279*/sb2.append(v0.getKey()).append(':').append(v);
                    /*SL:1280*/b = true;
                }
            }
            /*SL:1284*/if (b) {
                /*SL:1285*/sb.append('<').append((CharSequence)sb2).append('>');
            }
        }
        /*SL:1289*/sb.append(this.superClass.asType());
        /*SL:1291*/for (final Token token : this.interfaces) {
            /*SL:1292*/sb.append(token.asType());
        }
        /*SL:1295*/return sb.toString();
    }
    
    public ClassSignature wake() {
        /*SL:1302*/return this;
    }
    
    public static ClassSignature of(final String a1) {
        /*SL:1312*/return new ClassSignature().read(a1);
    }
    
    public static ClassSignature of(final ClassNode a1) {
        /*SL:1324*/if (a1.signature != null) {
            /*SL:1325*/return of(a1.signature);
        }
        /*SL:1328*/return generate(a1);
    }
    
    public static ClassSignature ofLazy(final ClassNode a1) {
        /*SL:1341*/if (a1.signature != null) {
            /*SL:1342*/return new Lazy(a1.signature);
        }
        /*SL:1345*/return generate(a1);
    }
    
    private static ClassSignature generate(final ClassNode v1) {
        final ClassSignature v2 = /*EL:1355*/new ClassSignature();
        v2.setSuperClass(/*EL:1356*/new Token((v1.superName != null) ? v1.superName : "java/lang/Object"));
        /*SL:1357*/for (final String a1 : v1.interfaces) {
            /*SL:1358*/v2.addInterface(new Token(a1));
        }
        /*SL:1360*/return v2;
    }
    
    static class Lazy extends ClassSignature
    {
        private final String sig;
        private ClassSignature generated;
        
        Lazy(final String a1) {
            this.sig = a1;
        }
        
        @Override
        public ClassSignature wake() {
            /*SL:69*/if (this.generated == null) {
                /*SL:70*/this.generated = ClassSignature.of(this.sig);
            }
            /*SL:72*/return this.generated;
        }
    }
    
    static class TypeVar implements Comparable<TypeVar>
    {
        private final String originalName;
        private String currentName;
        
        TypeVar(final String a1) {
            this.originalName = a1;
            this.currentName = a1;
        }
        
        @Override
        public int compareTo(final TypeVar a1) {
            /*SL:98*/return this.currentName.compareTo(a1.currentName);
        }
        
        @Override
        public String toString() {
            /*SL:103*/return this.currentName;
        }
        
        String getOriginalName() {
            /*SL:107*/return this.originalName;
        }
        
        void rename(final String a1) {
            /*SL:111*/this.currentName = a1;
        }
        
        public boolean matches(final String a1) {
            /*SL:115*/return this.originalName.equals(a1);
        }
        
        @Override
        public boolean equals(final Object a1) {
            /*SL:120*/return this.currentName.equals(a1);
        }
        
        @Override
        public int hashCode() {
            /*SL:125*/return this.currentName.hashCode();
        }
    }
    
    static class Token implements IToken
    {
        static final String SYMBOLS = "+-*";
        private final boolean inner;
        private boolean array;
        private char symbol;
        private String type;
        private List<Token> classBound;
        private List<Token> ifaceBound;
        private List<IToken> signature;
        private List<IToken> suffix;
        private Token tail;
        
        Token() {
            this(false);
        }
        
        Token(final String a1) {
            this(a1, false);
        }
        
        Token(final char a1) {
            this();
            this.symbol = a1;
        }
        
        Token(final boolean a1) {
            this(null, a1);
        }
        
        Token(final String a1, final boolean a2) {
            this.symbol = '\0';
            this.inner = a2;
            this.type = a1;
        }
        
        Token setSymbol(final char a1) {
            /*SL:260*/if (this.symbol == '\0' && "+-*".indexOf(a1) > -1) {
                /*SL:261*/this.symbol = a1;
            }
            /*SL:263*/return this;
        }
        
        Token setType(final String a1) {
            /*SL:267*/if (this.type == null) {
                /*SL:268*/this.type = a1;
            }
            /*SL:270*/return this;
        }
        
        boolean hasClassBound() {
            /*SL:274*/return this.classBound != null;
        }
        
        boolean hasInterfaceBound() {
            /*SL:278*/return this.ifaceBound != null;
        }
        
        @Override
        public IToken setArray(final boolean a1) {
            /*SL:283*/this.array |= a1;
            /*SL:284*/return this;
        }
        
        @Override
        public IToken setWildcard(final char a1) {
            /*SL:289*/if ("+-".indexOf(a1) == -1) {
                /*SL:290*/return this;
            }
            /*SL:292*/return this.setSymbol(a1);
        }
        
        private List<Token> getClassBound() {
            /*SL:296*/if (this.classBound == null) {
                /*SL:297*/this.classBound = new ArrayList<Token>();
            }
            /*SL:299*/return this.classBound;
        }
        
        private List<Token> getIfaceBound() {
            /*SL:303*/if (this.ifaceBound == null) {
                /*SL:304*/this.ifaceBound = new ArrayList<Token>();
            }
            /*SL:306*/return this.ifaceBound;
        }
        
        private List<IToken> getSignature() {
            /*SL:310*/if (this.signature == null) {
                /*SL:311*/this.signature = new ArrayList<IToken>();
            }
            /*SL:313*/return this.signature;
        }
        
        private List<IToken> getSuffix() {
            /*SL:317*/if (this.suffix == null) {
                /*SL:318*/this.suffix = new ArrayList<IToken>();
            }
            /*SL:320*/return this.suffix;
        }
        
        IToken addTypeArgument(final char a1) {
            /*SL:330*/if (this.tail != null) {
                /*SL:331*/return this.tail.addTypeArgument(a1);
            }
            final Token v1 = /*EL:334*/new Token(a1);
            /*SL:335*/this.getSignature().add(v1);
            /*SL:336*/return v1;
        }
        
        IToken addTypeArgument(final String a1) {
            /*SL:346*/if (this.tail != null) {
                /*SL:347*/return this.tail.addTypeArgument(a1);
            }
            final Token v1 = /*EL:350*/new Token(a1);
            /*SL:351*/this.getSignature().add(v1);
            /*SL:352*/return v1;
        }
        
        IToken addTypeArgument(final Token a1) {
            /*SL:362*/if (this.tail != null) {
                /*SL:363*/return this.tail.addTypeArgument(a1);
            }
            /*SL:366*/this.getSignature().add(a1);
            /*SL:367*/return a1;
        }
        
        IToken addTypeArgument(final TokenHandle a1) {
            /*SL:377*/if (this.tail != null) {
                /*SL:378*/return this.tail.addTypeArgument(a1);
            }
            final TokenHandle v1 = /*EL:381*/a1.clone();
            /*SL:382*/this.getSignature().add(v1);
            /*SL:383*/return v1;
        }
        
        Token addBound(final String a1, final boolean a2) {
            /*SL:395*/if (a2) {
                /*SL:396*/return this.addClassBound(a1);
            }
            /*SL:399*/return this.addInterfaceBound(a1);
        }
        
        Token addClassBound(final String a1) {
            final Token v1 = /*EL:409*/new Token(a1);
            /*SL:410*/this.getClassBound().add(v1);
            /*SL:411*/return v1;
        }
        
        Token addInterfaceBound(final String a1) {
            final Token v1 = /*EL:421*/new Token(a1);
            /*SL:422*/this.getIfaceBound().add(v1);
            /*SL:423*/return v1;
        }
        
        Token addInnerClass(final String a1) {
            /*SL:433*/this.tail = new Token(a1, true);
            /*SL:434*/this.getSuffix().add(this.tail);
            /*SL:435*/return this.tail;
        }
        
        @Override
        public String toString() {
            /*SL:443*/return this.asType();
        }
        
        @Override
        public String asBound() {
            final StringBuilder sb = /*EL:451*/new StringBuilder();
            /*SL:453*/if (this.type != null) {
                /*SL:454*/sb.append(this.type);
            }
            /*SL:457*/if (this.classBound != null) {
                /*SL:458*/for (final Token v1 : this.classBound) {
                    /*SL:459*/sb.append(v1.asType());
                }
            }
            /*SL:463*/if (this.ifaceBound != null) {
                /*SL:464*/for (final Token v1 : this.ifaceBound) {
                    /*SL:465*/sb.append(':').append(v1.asType());
                }
            }
            /*SL:469*/return sb.toString();
        }
        
        @Override
        public String asType() {
            /*SL:477*/return this.asType(false);
        }
        
        public String asType(final boolean v-2) {
            final StringBuilder sb = /*EL:488*/new StringBuilder();
            /*SL:490*/if (this.array) {
                /*SL:491*/sb.append('[');
            }
            /*SL:494*/if (this.symbol != '\0') {
                /*SL:495*/sb.append(this.symbol);
            }
            /*SL:498*/if (this.type == null) {
                /*SL:499*/return sb.toString();
            }
            /*SL:502*/if (!this.inner) {
                /*SL:503*/sb.append('L');
            }
            /*SL:506*/sb.append(this.type);
            /*SL:508*/if (!v-2) {
                /*SL:509*/if (this.signature != null) {
                    /*SL:510*/sb.append('<');
                    /*SL:511*/for (final IToken a1 : this.signature) {
                        /*SL:512*/sb.append(a1.asType());
                    }
                    /*SL:514*/sb.append('>');
                }
                /*SL:517*/if (this.suffix != null) {
                    /*SL:518*/for (final IToken v1 : this.suffix) {
                        /*SL:519*/sb.append('.').append(v1.asType());
                    }
                }
            }
            /*SL:524*/if (!this.inner) {
                /*SL:525*/sb.append(';');
            }
            /*SL:528*/return sb.toString();
        }
        
        boolean isRaw() {
            /*SL:532*/return this.signature == null;
        }
        
        String getClassType() {
            /*SL:536*/return (this.type != null) ? this.type : "java/lang/Object";
        }
        
        @Override
        public Token asToken() {
            /*SL:541*/return this;
        }
    }
    
    class TokenHandle implements IToken
    {
        final Token token;
        boolean array;
        char wildcard;
        
        TokenHandle(final ClassSignature a1) {
            this(a1, new Token());
        }
        
        TokenHandle(final Token a2) {
            this.token = a2;
        }
        
        @Override
        public IToken setArray(final boolean a1) {
            /*SL:583*/this.array |= a1;
            /*SL:584*/return this;
        }
        
        @Override
        public IToken setWildcard(final char a1) {
            /*SL:593*/if ("+-".indexOf(a1) > -1) {
                /*SL:594*/this.wildcard = a1;
            }
            /*SL:596*/return this;
        }
        
        @Override
        public String asBound() {
            /*SL:604*/return this.token.asBound();
        }
        
        @Override
        public String asType() {
            final StringBuilder v1 = /*EL:612*/new StringBuilder();
            /*SL:614*/if (this.wildcard > '\0') {
                /*SL:615*/v1.append(this.wildcard);
            }
            /*SL:618*/if (this.array) {
                /*SL:619*/v1.append('[');
            }
            /*SL:622*/return v1.append(ClassSignature.this.getTypeVar(this)).toString();
        }
        
        @Override
        public Token asToken() {
            /*SL:630*/return this.token;
        }
        
        @Override
        public String toString() {
            /*SL:638*/return this.token.toString();
        }
        
        public TokenHandle clone() {
            /*SL:646*/return new TokenHandle(this.token);
        }
    }
    
    class SignatureParser extends SignatureVisitor
    {
        private FormalParamElement param;
        final /* synthetic */ ClassSignature this$0;
        
        SignatureParser() {
            super(327680);
        }
        
        @Override
        public void visitFormalTypeParameter(final String a1) {
            /*SL:906*/this.param = new FormalParamElement(a1);
        }
        
        @Override
        public SignatureVisitor visitClassBound() {
            /*SL:911*/return this.param.visitClassBound();
        }
        
        @Override
        public SignatureVisitor visitInterfaceBound() {
            /*SL:916*/return this.param.visitInterfaceBound();
        }
        
        @Override
        public SignatureVisitor visitSuperclass() {
            /*SL:921*/return new SuperClassElement();
        }
        
        @Override
        public SignatureVisitor visitInterface() {
            /*SL:926*/return new InterfaceElement();
        }
        
        abstract class SignatureElement extends SignatureVisitor
        {
            public SignatureElement() {
                super(327680);
            }
        }
        
        abstract class TokenElement extends SignatureElement
        {
            protected Token token;
            private boolean array;
            
            public Token getToken() {
                /*SL:686*/if (this.token == null) {
                    /*SL:687*/this.token = new Token();
                }
                /*SL:689*/return this.token;
            }
            
            protected void setArray() {
                /*SL:693*/this.array = true;
            }
            
            private boolean getArray() {
                final boolean v1 = /*EL:697*/this.array;
                /*SL:698*/this.array = false;
                /*SL:699*/return v1;
            }
            
            @Override
            public void visitClassType(final String a1) {
                /*SL:704*/this.getToken().setType(a1);
            }
            
            @Override
            public SignatureVisitor visitClassBound() {
                /*SL:709*/this.getToken();
                /*SL:710*/return new BoundElement(this, true);
            }
            
            @Override
            public SignatureVisitor visitInterfaceBound() {
                /*SL:715*/this.getToken();
                /*SL:716*/return new BoundElement(this, false);
            }
            
            @Override
            public void visitInnerClassType(final String a1) {
                /*SL:721*/this.token.addInnerClass(a1);
            }
            
            @Override
            public SignatureVisitor visitArrayType() {
                /*SL:726*/this.setArray();
                /*SL:727*/return this;
            }
            
            @Override
            public SignatureVisitor visitTypeArgument(final char a1) {
                /*SL:732*/return new TypeArgElement(this, a1);
            }
            
            Token addTypeArgument() {
                /*SL:736*/return this.token.addTypeArgument('*').asToken();
            }
            
            IToken addTypeArgument(final char a1) {
                /*SL:740*/return this.token.addTypeArgument(a1).setArray(this.getArray());
            }
            
            IToken addTypeArgument(final String a1) {
                /*SL:744*/return this.token.addTypeArgument(a1).setArray(this.getArray());
            }
            
            IToken addTypeArgument(final Token a1) {
                /*SL:748*/return this.token.addTypeArgument(a1).setArray(this.getArray());
            }
            
            IToken addTypeArgument(final TokenHandle a1) {
                /*SL:752*/return this.token.addTypeArgument(a1).setArray(this.getArray());
            }
        }
        
        class FormalParamElement extends TokenElement
        {
            private final TokenHandle handle;
            
            FormalParamElement(final String a2) {
                this.handle = SignatureParser.this.this$0.getType(a2);
                this.token = this.handle.asToken();
            }
        }
        
        class TypeArgElement extends TokenElement
        {
            private final TokenElement type;
            private final char wildcard;
            
            TypeArgElement(final TokenElement a2, final char a3) {
                this.type = a2;
                this.wildcard = a3;
            }
            
            @Override
            public SignatureVisitor visitArrayType() {
                /*SL:796*/this.type.setArray();
                /*SL:797*/return this;
            }
            
            @Override
            public void visitBaseType(final char a1) {
                /*SL:802*/this.token = this.type.addTypeArgument(a1).asToken();
            }
            
            @Override
            public void visitTypeVariable(final String a1) {
                final TokenHandle v1 = /*EL:807*/ClassSignature.this.getType(a1);
                /*SL:808*/this.token = this.type.addTypeArgument(v1).setWildcard(this.wildcard).asToken();
            }
            
            @Override
            public void visitClassType(final String a1) {
                /*SL:813*/this.token = this.type.addTypeArgument(a1).setWildcard(this.wildcard).asToken();
            }
            
            @Override
            public void visitTypeArgument() {
                /*SL:818*/this.token.addTypeArgument('*');
            }
            
            @Override
            public SignatureVisitor visitTypeArgument(final char a1) {
                /*SL:823*/return new TypeArgElement(this, a1);
            }
            
            @Override
            public void visitEnd() {
            }
        }
        
        class BoundElement extends TokenElement
        {
            private final TokenElement type;
            private final boolean classBound;
            
            BoundElement(final TokenElement a2, final boolean a3) {
                /*SL:828*/this.type = a2;
                this.classBound = a3;
            }
            
            @Override
            public void visitClassType(final String a1) {
                /*SL:854*/this.token = this.type.token.addBound(a1, this.classBound);
            }
            
            @Override
            public void visitTypeArgument() {
                /*SL:859*/this.token.addTypeArgument('*');
            }
            
            @Override
            public SignatureVisitor visitTypeArgument(final char a1) {
                /*SL:864*/return new TypeArgElement(this, a1);
            }
        }
        
        class SuperClassElement extends TokenElement
        {
            @Override
            public void visitEnd() {
                /*SL:876*/ClassSignature.this.setSuperClass(this.token);
            }
        }
        
        class InterfaceElement extends TokenElement
        {
            @Override
            public void visitEnd() {
                /*SL:888*/ClassSignature.this.addInterface(this.token);
            }
        }
    }
    
    class SignatureRemapper extends SignatureWriter
    {
        private final Set<String> localTypeVars;
        
        SignatureRemapper() {
            /*SL:889*/this.localTypeVars = new HashSet<String>();
        }
        
        @Override
        public void visitFormalTypeParameter(final String a1) {
            /*SL:940*/this.localTypeVars.add(a1);
            /*SL:941*/super.visitFormalTypeParameter(a1);
        }
        
        @Override
        public void visitTypeVariable(final String v2) {
            /*SL:946*/if (!this.localTypeVars.contains(v2)) {
                final TypeVar a1 = /*EL:947*/ClassSignature.this.getTypeVar(v2);
                /*SL:948*/if (a1 != null) {
                    /*SL:949*/super.visitTypeVariable(a1.toString());
                    /*SL:950*/return;
                }
            }
            /*SL:953*/super.visitTypeVariable(v2);
        }
    }
    
    interface IToken
    {
        public static final String WILDCARDS = "+-";
        
        String asType();
        
        String asBound();
        
        Token asToken();
        
        IToken setArray(boolean p0);
        
        IToken setWildcard(char p0);
    }
}

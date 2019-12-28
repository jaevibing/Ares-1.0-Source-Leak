package org.spongepowered.asm.mixin.injection.modify;

import org.spongepowered.asm.util.SignaturePrinter;
import java.util.Map;
import java.util.HashMap;
import org.spongepowered.asm.lib.tree.LocalVariableNode;
import org.spongepowered.asm.util.Locals;
import org.spongepowered.asm.util.Bytecode;
import org.spongepowered.asm.util.PrettyPrinter;
import java.util.Collection;
import java.util.List;
import java.util.HashSet;
import org.spongepowered.asm.util.Annotations;
import org.spongepowered.asm.lib.tree.AnnotationNode;
import org.spongepowered.asm.lib.tree.AbstractInsnNode;
import org.spongepowered.asm.mixin.injection.struct.Target;
import org.spongepowered.asm.lib.Type;
import java.util.Collections;
import java.util.Set;

public class LocalVariableDiscriminator
{
    private final boolean argsOnly;
    private final int ordinal;
    private final int index;
    private final Set<String> names;
    private final boolean print;
    
    public LocalVariableDiscriminator(final boolean a1, final int a2, final int a3, final Set<String> a4, final boolean a5) {
        this.argsOnly = a1;
        this.ordinal = a2;
        this.index = a3;
        this.names = Collections.<String>unmodifiableSet((Set<? extends String>)a4);
        this.print = a5;
    }
    
    public boolean isArgsOnly() {
        /*SL:238*/return this.argsOnly;
    }
    
    public int getOrdinal() {
        /*SL:245*/return this.ordinal;
    }
    
    public int getIndex() {
        /*SL:252*/return this.index;
    }
    
    public Set<String> getNames() {
        /*SL:259*/return this.names;
    }
    
    public boolean hasNames() {
        /*SL:266*/return !this.names.isEmpty();
    }
    
    public boolean printLVT() {
        /*SL:273*/return this.print;
    }
    
    protected boolean isImplicit(final Context a1) {
        /*SL:286*/return this.ordinal < 0 && this.index < a1.baseArgIndex && this.names.isEmpty();
    }
    
    public int findLocal(final Type a3, final boolean a4, final Target v1, final AbstractInsnNode v2) {
        try {
            /*SL:300*/return this.findLocal(new Context(a3, a4, v1, v2));
        }
        catch (InvalidImplicitDiscriminatorException a5) {
            /*SL:302*/return -2;
        }
    }
    
    public int findLocal(final Context a1) {
        /*SL:313*/if (this.isImplicit(a1)) {
            /*SL:314*/return this.findImplicitLocal(a1);
        }
        /*SL:316*/return this.findExplicitLocal(a1);
    }
    
    private int findImplicitLocal(final Context v-2) {
        int n = /*EL:328*/0;
        int v0 = /*EL:329*/0;
        /*SL:330*/for (int v = v-2.baseArgIndex; v < v-2.locals.length; ++v) {
            final Context.Local a1 = /*EL:331*/v-2.locals[v];
            /*SL:332*/if (a1 != null) {
                if (a1.type.equals(v-2.returnType)) {
                    /*SL:335*/++v0;
                    /*SL:336*/n = v;
                }
            }
        }
        /*SL:339*/if (v0 == 1) {
            /*SL:340*/return n;
        }
        /*SL:343*/throw new InvalidImplicitDiscriminatorException("Found " + v0 + " candidate variables but exactly 1 is required.");
    }
    
    private int findExplicitLocal(final Context v0) {
        /*SL:354*/for (int v = v0.baseArgIndex; v < v0.locals.length; ++v) {
            final Context.Local a1 = /*EL:355*/v0.locals[v];
            /*SL:356*/if (a1 != null) {
                if (a1.type.equals(v0.returnType)) {
                    /*SL:359*/if (this.ordinal > -1) {
                        /*SL:360*/if (this.ordinal == a1.ord) {
                            /*SL:361*/return v;
                        }
                    }
                    else/*SL:365*/ if (this.index >= v0.baseArgIndex) {
                        /*SL:366*/if (this.index == v) {
                            /*SL:367*/return v;
                        }
                    }
                    else/*SL:371*/ if (this.names.contains(a1.name)) {
                        /*SL:372*/return v;
                    }
                }
            }
        }
        /*SL:376*/return -1;
    }
    
    public static LocalVariableDiscriminator parse(final AnnotationNode a1) {
        final boolean v1 = /*EL:386*/Annotations.<Boolean>getValue(a1, "argsOnly", Boolean.FALSE);
        final int v2 = /*EL:387*/Annotations.<Integer>getValue(a1, "ordinal", Integer.valueOf(-1));
        final int v3 = /*EL:388*/Annotations.<Integer>getValue(a1, "index", Integer.valueOf(-1));
        final boolean v4 = /*EL:389*/Annotations.<Boolean>getValue(a1, "print", Boolean.FALSE);
        final Set<String> v5 = /*EL:391*/new HashSet<String>();
        final List<String> v6 = /*EL:392*/Annotations.<List<String>>getValue(a1, "name", (List<String>)null);
        /*SL:393*/if (v6 != null) {
            /*SL:394*/v5.addAll(v6);
        }
        /*SL:397*/return new LocalVariableDiscriminator(v1, v2, v3, v5, v4);
    }
    
    public static class Context implements PrettyPrinter.IPrettyPrintable
    {
        final Target target;
        final Type returnType;
        final AbstractInsnNode node;
        final int baseArgIndex;
        final Local[] locals;
        private final boolean isStatic;
        
        public Context(final Type a1, final boolean a2, final Target a3, final AbstractInsnNode a4) {
            this.isStatic = Bytecode.methodIsStatic(a3.method);
            this.returnType = a1;
            this.target = a3;
            this.node = a4;
            this.baseArgIndex = (this.isStatic ? 0 : 1);
            this.locals = this.initLocals(a3, a2, a4);
            this.initOrdinals();
        }
        
        private Local[] initLocals(final Target v-4, final boolean v-3, final AbstractInsnNode v-2) {
            /*SL:135*/if (!v-3) {
                LocalVariableNode[] a3 = /*EL:136*/Locals.getLocalsAt(v-4.classNode, v-4.method, v-2);
                /*SL:137*/if (a3 != null) {
                    final Local[] a2 = /*EL:138*/new Local[a3.length];
                    /*SL:139*/for (a3 = 0; a3 < a3.length; ++a3) {
                        /*SL:140*/if (a3[a3] != null) {
                            /*SL:141*/a2[a3] = new Local(a3[a3].name, Type.getType(a3[a3].desc));
                        }
                    }
                    /*SL:144*/return a2;
                }
            }
            final Local[] array = /*EL:148*/new Local[this.baseArgIndex + v-4.arguments.length];
            /*SL:149*/if (!this.isStatic) {
                /*SL:150*/array[0] = new Local("this", Type.getType(v-4.classNode.name));
            }
            /*SL:152*/for (int v0 = this.baseArgIndex; v0 < array.length; ++v0) {
                final Type v = /*EL:153*/v-4.arguments[v0 - this.baseArgIndex];
                /*SL:154*/array[v0] = new Local("arg" + v0, v);
            }
            /*SL:156*/return array;
        }
        
        private void initOrdinals() {
            final Map<Type, Integer> map = /*EL:160*/new HashMap<Type, Integer>();
            /*SL:161*/for (int v0 = 0; v0 < this.locals.length; ++v0) {
                Integer v = /*EL:162*/0;
                /*SL:163*/if (this.locals[v0] != null) {
                    /*SL:164*/v = map.get(this.locals[v0].type);
                    /*SL:165*/map.put(this.locals[v0].type, v = ((v == null) ? 0 : (v + 1)));
                    /*SL:166*/this.locals[v0].ord = v;
                }
            }
        }
        
        @Override
        public void print(final PrettyPrinter v-3) {
            /*SL:173*/v-3.add("%5s  %7s  %30s  %-50s  %s", "INDEX", "ORDINAL", "TYPE", "NAME", "CANDIDATE");
            /*SL:174*/for (int i = this.baseArgIndex; i < this.locals.length; ++i) {
                final Local local = /*EL:175*/this.locals[i];
                /*SL:176*/if (local != null) {
                    final Type a1 = /*EL:177*/local.type;
                    final String v1 = /*EL:178*/local.name;
                    final int v2 = /*EL:179*/local.ord;
                    final String v3 = /*EL:180*/this.returnType.equals(a1) ? "YES" : "-";
                    /*SL:181*/v-3.add("[%3d]    [%3d]  %30s  %-50s  %s", i, v2, SignaturePrinter.getTypeName(a1, false), v1, v3);
                }
                else/*SL:182*/ if (i > 0) {
                    final Local v4 = /*EL:183*/this.locals[i - 1];
                    final boolean v5 = /*EL:184*/v4 != null && v4.type != null && v4.type.getSize() > 1;
                    /*SL:185*/v-3.add("[%3d]           %30s", i, v5 ? "<top>" : "-");
                }
            }
        }
        
        public class Local
        {
            int ord;
            String name;
            Type type;
            
            public Local(final String a2, final Type a3) {
                this.ord = 0;
                this.name = a2;
                this.type = a3;
            }
            
            @Override
            public String toString() {
                /*SL:87*/return String.format("Local[ordinal=%d, name=%s, type=%s]", this.ord, this.name, this.type);
            }
        }
    }
}

package org.spongepowered.asm.mixin.gen;

import com.google.common.collect.ImmutableSet;
import java.util.Set;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;
import org.spongepowered.asm.util.Bytecode;
import java.util.regex.Matcher;
import org.apache.logging.log4j.LogManager;
import org.spongepowered.asm.mixin.MixinEnvironment;
import com.google.common.base.Strings;
import org.spongepowered.asm.mixin.refmap.IMixinContext;
import org.spongepowered.asm.mixin.gen.throwables.InvalidAccessorException;
import org.spongepowered.asm.util.Annotations;
import java.lang.annotation.Annotation;
import org.spongepowered.asm.mixin.transformer.MixinTargetContext;
import org.spongepowered.asm.lib.tree.MethodNode;
import org.spongepowered.asm.lib.tree.FieldNode;
import org.spongepowered.asm.mixin.injection.struct.MemberInfo;
import org.spongepowered.asm.lib.Type;
import java.util.regex.Pattern;
import org.spongepowered.asm.mixin.struct.SpecialMethodInfo;

public class AccessorInfo extends SpecialMethodInfo
{
    protected static final Pattern PATTERN_ACCESSOR;
    protected final Type[] argTypes;
    protected final Type returnType;
    protected final AccessorType type;
    private final Type targetFieldType;
    protected final MemberInfo target;
    protected FieldNode targetField;
    protected MethodNode targetMethod;
    
    public AccessorInfo(final MixinTargetContext a1, final MethodNode a2) {
        this(a1, a2, Accessor.class);
    }
    
    protected AccessorInfo(final MixinTargetContext a1, final MethodNode a2, final Class<? extends Annotation> a3) {
        super(a1, a2, Annotations.getVisible(a2, a3));
        this.argTypes = Type.getArgumentTypes(a2.desc);
        this.returnType = Type.getReturnType(a2.desc);
        this.type = this.initType();
        this.targetFieldType = this.initTargetFieldType();
        this.target = this.initTarget();
    }
    
    protected AccessorType initType() {
        /*SL:178*/if (this.returnType.equals(Type.VOID_TYPE)) {
            /*SL:179*/return AccessorType.FIELD_SETTER;
        }
        /*SL:181*/return AccessorType.FIELD_GETTER;
    }
    
    protected Type initTargetFieldType() {
        /*SL:185*/switch (this.type) {
            case FIELD_GETTER: {
                /*SL:187*/if (this.argTypes.length > 0) {
                    /*SL:188*/throw new InvalidAccessorException(this.mixin, this + " must take exactly 0 arguments, found " + this.argTypes.length);
                }
                /*SL:190*/return this.returnType;
            }
            case FIELD_SETTER: {
                /*SL:193*/if (this.argTypes.length != 1) {
                    /*SL:194*/throw new InvalidAccessorException(this.mixin, this + " must take exactly 1 argument, found " + this.argTypes.length);
                }
                /*SL:196*/return this.argTypes[0];
            }
            default: {
                /*SL:199*/throw new InvalidAccessorException(this.mixin, "Computed unsupported accessor type " + this.type + " for " + this);
            }
        }
    }
    
    protected MemberInfo initTarget() {
        final MemberInfo v1 = /*EL:204*/new MemberInfo(this.getTargetName(), null, this.targetFieldType.getDescriptor());
        /*SL:205*/this.annotation.visit("target", v1.toString());
        /*SL:206*/return v1;
    }
    
    protected String getTargetName() {
        final String v0 = /*EL:210*/Annotations.<String>getValue(this.annotation);
        /*SL:211*/if (!Strings.isNullOrEmpty(v0)) {
            /*SL:218*/return MemberInfo.parse(v0, this.mixin).name;
        }
        final String v = this.inflectTarget();
        if (v == null) {
            throw new InvalidAccessorException(this.mixin, "Failed to inflect target name for " + this + ", supported prefixes: [get, set, is]");
        }
        return v;
    }
    
    protected String inflectTarget() {
        /*SL:228*/return inflectTarget(this.method.name, this.type, this.toString(), this.mixin, this.mixin.getEnvironment().getOption(MixinEnvironment.Option.DEBUG_VERBOSE));
    }
    
    public static String inflectTarget(final String a5, final AccessorType v1, final String v2, final IMixinContext v3, final boolean v4) {
        final Matcher v5 = AccessorInfo.PATTERN_ACCESSOR.matcher(/*EL:251*/a5);
        /*SL:252*/if (v5.matches()) {
            final String a6 = /*EL:253*/v5.group(1);
            final String a7 = /*EL:254*/v5.group(3);
            final String a8 = /*EL:255*/v5.group(4);
            final String a9 = /*EL:257*/String.format("%s%s", toLowerCase(a7, !isUpperCase(a8)), a8);
            /*SL:258*/if (!v1.isExpectedPrefix(a6) && v4) {
                /*SL:259*/LogManager.getLogger("mixin").warn("Unexpected prefix for {}, found [{}] expecting {}", new Object[] { v2, a6, v1.getExpectedPrefixes() });
            }
            /*SL:262*/return MemberInfo.parse(a9, v3).name;
        }
        /*SL:264*/return null;
    }
    
    public final MemberInfo getTarget() {
        /*SL:271*/return this.target;
    }
    
    public final Type getTargetFieldType() {
        /*SL:278*/return this.targetFieldType;
    }
    
    public final FieldNode getTargetField() {
        /*SL:285*/return this.targetField;
    }
    
    public final MethodNode getTargetMethod() {
        /*SL:292*/return this.targetMethod;
    }
    
    public final Type getReturnType() {
        /*SL:299*/return this.returnType;
    }
    
    public final Type[] getArgTypes() {
        /*SL:306*/return this.argTypes;
    }
    
    @Override
    public String toString() {
        /*SL:311*/return String.format("%s->@%s[%s]::%s%s", this.mixin.toString(), Bytecode.getSimpleName(this.annotation), this.type.toString(), this.method.name, this.method.desc);
    }
    
    public void locate() {
        /*SL:321*/this.targetField = this.findTargetField();
    }
    
    public MethodNode generate() {
        final MethodNode v1 = /*EL:332*/this.type.getGenerator(this).generate();
        /*SL:333*/Bytecode.mergeAnnotations(this.method, v1);
        /*SL:334*/return v1;
    }
    
    private FieldNode findTargetField() {
        /*SL:338*/return this.<FieldNode>findTarget(this.classNode.fields);
    }
    
    protected <TNode> TNode findTarget(final List<TNode> v-5) {
        TNode tNode = /*EL:350*/null;
        final List<TNode> list = /*EL:351*/new ArrayList<TNode>();
        /*SL:353*/for (final TNode next : v-5) {
            final String a1 = /*EL:354*/AccessorInfo.<TNode>getNodeDesc(next);
            /*SL:355*/if (a1 != null) {
                if (!a1.equals(this.target.desc)) {
                    /*SL:356*/continue;
                }
                final String v1 = /*EL:359*/AccessorInfo.<TNode>getNodeName(next);
                /*SL:360*/if (v1 == null) {
                    continue;
                }
                /*SL:361*/if (v1.equals(this.target.name)) {
                    /*SL:362*/tNode = next;
                }
                /*SL:364*/if (!v1.equalsIgnoreCase(this.target.name)) {
                    continue;
                }
                /*SL:365*/list.add(next);
            }
        }
        /*SL:370*/if (tNode != null) {
            /*SL:371*/if (list.size() > 1) {
                /*SL:372*/LogManager.getLogger("mixin").debug("{} found an exact match for {} but other candidates were found!", new Object[] { this, this.target });
            }
            /*SL:374*/return tNode;
        }
        /*SL:377*/if (list.size() == 1) {
            /*SL:378*/return list.get(0);
        }
        final String s = /*EL:381*/(list.size() == 0) ? "No" : "Multiple";
        /*SL:382*/throw new InvalidAccessorException(this, s + " candidates were found matching " + this.target + " in " + this.classNode.name + " for " + this);
    }
    
    private static <TNode> String getNodeDesc(final TNode a1) {
        /*SL:387*/return (a1 instanceof MethodNode) ? ((MethodNode)a1).desc : ((a1 instanceof FieldNode) ? ((FieldNode)a1).desc : null);
    }
    
    private static <TNode> String getNodeName(final TNode a1) {
        /*SL:391*/return (a1 instanceof MethodNode) ? ((MethodNode)a1).name : ((a1 instanceof FieldNode) ? ((FieldNode)a1).name : null);
    }
    
    public static AccessorInfo of(final MixinTargetContext a1, final MethodNode a2, final Class<? extends Annotation> a3) {
        /*SL:404*/if (a3 == Accessor.class) {
            /*SL:405*/return new AccessorInfo(a1, a2);
        }
        /*SL:406*/if (a3 == Invoker.class) {
            /*SL:407*/return new InvokerInfo(a1, a2);
        }
        /*SL:409*/throw new InvalidAccessorException(a1, "Could not parse accessor for unknown type " + a3.getName());
    }
    
    private static String toLowerCase(final String a1, final boolean a2) {
        /*SL:413*/return a2 ? a1.toLowerCase() : a1;
    }
    
    private static boolean isUpperCase(final String a1) {
        /*SL:417*/return a1.toUpperCase().equals(a1);
    }
    
    static {
        PATTERN_ACCESSOR = Pattern.compile("^(get|set|is|invoke|call)(([A-Z])(.*?))(_\\$md.*)?$");
    }
    
    public enum AccessorType
    {
        FIELD_GETTER((Set)ImmutableSet.<String>of("get", "is")) {
            @Override
            AccessorGenerator getGenerator(final AccessorInfo a1) {
                /*SL:66*/return new AccessorGeneratorFieldGetter(a1);
            }
        }, 
        FIELD_SETTER((Set)ImmutableSet.<String>of("set")) {
            @Override
            AccessorGenerator getGenerator(final AccessorInfo a1) {
                /*SL:77*/return new AccessorGeneratorFieldSetter(a1);
            }
        }, 
        METHOD_PROXY((Set)ImmutableSet.<String>of("call", "invoke")) {
            @Override
            AccessorGenerator getGenerator(final AccessorInfo a1) {
                /*SL:87*/return new AccessorGeneratorMethodProxy(a1);
            }
        };
        
        private final Set<String> expectedPrefixes;
        
        private AccessorType(final Set<String> a1) {
            this.expectedPrefixes = a1;
        }
        
        public boolean isExpectedPrefix(final String a1) {
            /*SL:105*/return this.expectedPrefixes.contains(a1);
        }
        
        public String getExpectedPrefixes() {
            /*SL:116*/return this.expectedPrefixes.toString();
        }
        
        abstract AccessorGenerator getGenerator(final AccessorInfo p0);
    }
}

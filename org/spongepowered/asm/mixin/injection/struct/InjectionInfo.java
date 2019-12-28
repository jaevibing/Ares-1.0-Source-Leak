package org.spongepowered.asm.mixin.injection.struct;

import org.spongepowered.asm.mixin.transformer.throwables.InvalidMixinException;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.lib.Type;
import com.google.common.base.Strings;
import org.spongepowered.asm.mixin.Dynamic;
import org.spongepowered.asm.mixin.Final;
import java.lang.annotation.Annotation;
import org.spongepowered.asm.mixin.transformer.meta.MixinMerged;
import org.spongepowered.asm.mixin.injection.code.MethodSlice;
import org.spongepowered.asm.mixin.injection.throwables.InjectionError;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.mixin.injection.code.InjectorTarget;
import java.util.Collection;
import java.util.Iterator;
import org.spongepowered.asm.mixin.refmap.IMixinContext;
import java.util.LinkedHashSet;
import org.spongepowered.asm.mixin.injection.throwables.InvalidInjectionException;
import org.spongepowered.asm.util.Annotations;
import java.util.Set;
import org.spongepowered.asm.util.Bytecode;
import java.util.LinkedHashMap;
import java.util.ArrayList;
import java.util.ArrayDeque;
import org.spongepowered.asm.lib.tree.AnnotationNode;
import org.spongepowered.asm.mixin.transformer.MixinTargetContext;
import org.spongepowered.asm.mixin.injection.code.Injector;
import java.util.Map;
import org.spongepowered.asm.mixin.injection.InjectionPoint;
import java.util.List;
import org.spongepowered.asm.mixin.injection.code.MethodSlices;
import org.spongepowered.asm.lib.tree.MethodNode;
import java.util.Deque;
import org.spongepowered.asm.mixin.injection.code.ISliceContext;
import org.spongepowered.asm.mixin.struct.SpecialMethodInfo;

public abstract class InjectionInfo extends SpecialMethodInfo implements ISliceContext
{
    protected final boolean isStatic;
    protected final Deque<MethodNode> targets;
    protected final MethodSlices slices;
    protected final String atKey;
    protected final List<InjectionPoint> injectionPoints;
    protected final Map<Target, List<InjectionNodes.InjectionNode>> targetNodes;
    protected Injector injector;
    protected InjectorGroupInfo group;
    private final List<MethodNode> injectedMethods;
    private int expectedCallbackCount;
    private int requiredCallbackCount;
    private int maxCallbackCount;
    private int injectedCallbackCount;
    
    protected InjectionInfo(final MixinTargetContext a1, final MethodNode a2, final AnnotationNode a3) {
        this(a1, a2, a3, "at");
    }
    
    protected InjectionInfo(final MixinTargetContext a1, final MethodNode a2, final AnnotationNode a3, final String a4) {
        super(a1, a2, a3);
        this.targets = new ArrayDeque<MethodNode>();
        this.injectionPoints = new ArrayList<InjectionPoint>();
        this.targetNodes = new LinkedHashMap<Target, List<InjectionNodes.InjectionNode>>();
        this.injectedMethods = new ArrayList<MethodNode>(0);
        this.expectedCallbackCount = 1;
        this.requiredCallbackCount = 0;
        this.maxCallbackCount = Integer.MAX_VALUE;
        this.injectedCallbackCount = 0;
        this.isStatic = Bytecode.methodIsStatic(a2);
        this.slices = MethodSlices.parse(this);
        this.atKey = a4;
        this.readAnnotation();
    }
    
    protected void readAnnotation() {
        /*SL:166*/if (this.annotation == null) {
            /*SL:167*/return;
        }
        final String v1 = /*EL:170*/"@" + Bytecode.getSimpleName(this.annotation);
        final List<AnnotationNode> v2 = /*EL:171*/this.readInjectionPoints(v1);
        /*SL:172*/this.findMethods(this.parseTargets(v1), v1);
        /*SL:173*/this.parseInjectionPoints(v2);
        /*SL:174*/this.parseRequirements();
        /*SL:175*/this.injector = this.parseInjector(this.annotation);
    }
    
    protected Set<MemberInfo> parseTargets(final String v-4) {
        final List<String> value = /*EL:179*/Annotations.<String>getValue(this.annotation, "method", false);
        /*SL:180*/if (value == null) {
            /*SL:181*/throw new InvalidInjectionException(this, String.format("%s annotation on %s is missing method name", v-4, this.method.name));
        }
        final Set<MemberInfo> set = /*EL:184*/new LinkedHashSet<MemberInfo>();
        /*SL:185*/for (final String v0 : value) {
            try {
                final MemberInfo a1 = /*EL:187*/MemberInfo.parseAndValidate(v0, this.mixin);
                /*SL:188*/if (a1.owner != null && !a1.owner.equals(this.mixin.getTargetClassRef())) {
                    /*SL:189*/throw new InvalidInjectionException(/*EL:190*/this, String.format("%s annotation on %s specifies a target class '%s', which is not supported", v-4, this.method.name, a1.owner));
                }
                /*SL:193*/set.add(a1);
            }
            catch (InvalidMemberDescriptorException v) {
                /*SL:195*/throw new InvalidInjectionException(this, String.format("%s annotation on %s, has invalid target descriptor: \"%s\". %s", v-4, this.method.name, v0, this.mixin.getReferenceMapper().getStatus()));
            }
        }
        /*SL:199*/return set;
    }
    
    protected List<AnnotationNode> readInjectionPoints(final String a1) {
        final List<AnnotationNode> v1 = /*EL:203*/Annotations.<AnnotationNode>getValue(this.annotation, this.atKey, false);
        /*SL:204*/if (v1 == null) {
            /*SL:205*/throw new InvalidInjectionException(this, String.format("%s annotation on %s is missing '%s' value(s)", a1, this.method.name, this.atKey));
        }
        /*SL:208*/return v1;
    }
    
    protected void parseInjectionPoints(final List<AnnotationNode> a1) {
        /*SL:212*/this.injectionPoints.addAll(InjectionPoint.parse(this.mixin, this.method, this.annotation, a1));
    }
    
    protected void parseRequirements() {
        /*SL:216*/this.group = this.mixin.getInjectorGroups().parseGroup(this.method, this.mixin.getDefaultInjectorGroup()).add(this);
        final Integer v1 = /*EL:218*/Annotations.<Integer>getValue(this.annotation, "expect");
        /*SL:219*/if (v1 != null) {
            /*SL:220*/this.expectedCallbackCount = v1;
        }
        final Integer v2 = /*EL:223*/Annotations.<Integer>getValue(this.annotation, "require");
        /*SL:224*/if (v2 != null && v2 > -1) {
            /*SL:225*/this.requiredCallbackCount = v2;
        }
        else/*SL:226*/ if (this.group.isDefault()) {
            /*SL:227*/this.requiredCallbackCount = this.mixin.getDefaultRequiredInjections();
        }
        final Integer v3 = /*EL:230*/Annotations.<Integer>getValue(this.annotation, "allow");
        /*SL:231*/if (v3 != null) {
            /*SL:232*/this.maxCallbackCount = Math.max(Math.max(this.requiredCallbackCount, 1), v3);
        }
    }
    
    protected abstract Injector parseInjector(final AnnotationNode p0);
    
    public boolean isValid() {
        /*SL:246*/return this.targets.size() > 0 && this.injectionPoints.size() > 0;
    }
    
    public void prepare() {
        /*SL:253*/this.targetNodes.clear();
        /*SL:254*/for (final MethodNode v0 : this.targets) {
            final Target v = /*EL:255*/this.mixin.getTargetMethod(v0);
            final InjectorTarget v2 = /*EL:256*/new InjectorTarget(this, v);
            /*SL:257*/this.targetNodes.put(v, this.injector.find(v2, this.injectionPoints));
            /*SL:258*/v2.dispose();
        }
    }
    
    public void inject() {
        /*SL:266*/for (final Map.Entry<Target, List<InjectionNodes.InjectionNode>> v1 : this.targetNodes.entrySet()) {
            /*SL:267*/this.injector.inject(v1.getKey(), v1.getValue());
        }
        /*SL:269*/this.targets.clear();
    }
    
    public void postInject() {
        /*SL:276*/for (final MethodNode v1 : this.injectedMethods) {
            /*SL:277*/this.classNode.methods.add(v1);
        }
        final String v2 = /*EL:280*/this.getDescription();
        final String v3 = /*EL:281*/this.mixin.getReferenceMapper().getStatus();
        final String v4 = /*EL:282*/this.getDynamicInfo();
        /*SL:283*/if (this.mixin.getEnvironment().getOption(MixinEnvironment.Option.DEBUG_INJECTORS) && this.injectedCallbackCount < this.expectedCallbackCount) {
            /*SL:284*/throw new InvalidInjectionException(/*EL:285*/this, String.format("Injection validation failed: %s %s%s in %s expected %d invocation(s) but %d succeeded. %s%s", v2, this.method.name, this.method.desc, this.mixin, this.expectedCallbackCount, /*EL:286*/this.injectedCallbackCount, v3, v4));
        }
        /*SL:288*/if (this.injectedCallbackCount < this.requiredCallbackCount) {
            /*SL:289*/throw new InjectionError(/*EL:290*/String.format("Critical injection failure: %s %s%s in %s failed injection check, (%d/%d) succeeded. %s%s", v2, this.method.name, this.method.desc, this.mixin, this.injectedCallbackCount, /*EL:291*/this.requiredCallbackCount, v3, v4));
        }
        /*SL:293*/if (this.injectedCallbackCount > this.maxCallbackCount) {
            /*SL:294*/throw new InjectionError(/*EL:295*/String.format("Critical injection failure: %s %s%s in %s failed injection check, %d succeeded of %d allowed.%s", v2, this.method.name, this.method.desc, this.mixin, this.injectedCallbackCount, /*EL:296*/this.maxCallbackCount, v4));
        }
    }
    
    public void notifyInjected(final Target a1) {
    }
    
    protected String getDescription() {
        /*SL:312*/return "Callback method";
    }
    
    @Override
    public String toString() {
        /*SL:317*/return describeInjector(this.mixin, this.annotation, this.method);
    }
    
    public Collection<MethodNode> getTargets() {
        /*SL:326*/return this.targets;
    }
    
    @Override
    public MethodSlice getSlice(final String a1) {
        /*SL:334*/return this.slices.get(this.getSliceId(a1));
    }
    
    public String getSliceId(final String a1) {
        /*SL:346*/return "";
    }
    
    public int getInjectedCallbackCount() {
        /*SL:355*/return this.injectedCallbackCount;
    }
    
    public MethodNode addMethod(final int a1, final String a2, final String a3) {
        final MethodNode v1 = /*EL:368*/new MethodNode(327680, a1 | 0x1000, a2, a3, null, null);
        /*SL:369*/this.injectedMethods.add(v1);
        /*SL:370*/return v1;
    }
    
    public void addCallbackInvocation(final MethodNode a1) {
        /*SL:379*/++this.injectedCallbackCount;
    }
    
    private void findMethods(final Set<MemberInfo> v-6, final String v-5) {
        /*SL:389*/this.targets.clear();
        final int n = /*EL:395*/this.mixin.getEnvironment().getOption(MixinEnvironment.Option.REFMAP_REMAP) ? 2 : 1;
        /*SL:397*/for (MemberInfo transform : v-6) {
            /*SL:398*/for (int n2 = 0, v0 = 0; v0 < n && n2 < 1; ++v0) {
                int v = /*EL:399*/0;
                /*SL:400*/for (boolean a2 : this.classNode.methods) {
                    /*SL:401*/if (transform.matches(a2.name, a2.desc, v)) {
                        /*SL:402*/a2 = (Annotations.getVisible(a2, MixinMerged.class) != null);
                        /*SL:403*/if (transform.matchAll) {
                            if (Bytecode.methodIsStatic(a2) != this.isStatic || a2 == this.method) {
                                continue;
                            }
                            if (a2) {
                                /*SL:404*/continue;
                            }
                        }
                        /*SL:407*/this.checkTarget(a2);
                        /*SL:408*/this.targets.add(a2);
                        /*SL:409*/++v;
                        /*SL:410*/++n2;
                    }
                }
                /*SL:415*/transform = transform.transform(null);
            }
        }
        /*SL:419*/if (this.targets.size() == 0) {
            /*SL:420*/throw new InvalidInjectionException(/*EL:421*/this, String.format("%s annotation on %s could not find any targets matching %s in the target class %s. %s%s", v-5, this.method.name, namesOf(v-6), /*EL:422*/this.mixin.getTarget(), this.mixin.getReferenceMapper().getStatus(), /*EL:423*/this.getDynamicInfo()));
        }
    }
    
    private void checkTarget(final MethodNode a1) {
        final AnnotationNode v1 = /*EL:428*/Annotations.getVisible(a1, MixinMerged.class);
        /*SL:429*/if (v1 == null) {
            /*SL:430*/return;
        }
        /*SL:433*/if (Annotations.getVisible(a1, Final.class) != null) {
            /*SL:434*/throw new InvalidInjectionException(this, String.format("%s cannot inject into @Final method %s::%s%s merged by %s", this, this.classNode.name, a1.name, a1.desc, /*EL:435*/Annotations.<Object>getValue(v1, "mixin")));
        }
    }
    
    protected String getDynamicInfo() {
        final AnnotationNode v1 = /*EL:446*/Annotations.getInvisible(this.method, Dynamic.class);
        String v2 = /*EL:447*/Strings.nullToEmpty(Annotations.<String>getValue(v1));
        final Type v3 = /*EL:448*/Annotations.<Type>getValue(v1, "mixin");
        /*SL:449*/if (v3 != null) {
            /*SL:450*/v2 = String.format("{%s} %s", v3.getClassName(), v2).trim();
        }
        /*SL:452*/return (v2.length() > 0) ? String.format(" Method is @Dynamic(%s)", v2) : "";
    }
    
    public static InjectionInfo parse(final MixinTargetContext a1, final MethodNode a2) {
        final AnnotationNode v1 = getInjectorAnnotation(/*EL:465*/a1.getMixin(), a2);
        /*SL:467*/if (v1 == null) {
            /*SL:468*/return null;
        }
        /*SL:471*/if (v1.desc.endsWith(Inject.class.getSimpleName() + ";")) {
            /*SL:472*/return new CallbackInjectionInfo(a1, a2, v1);
        }
        /*SL:473*/if (v1.desc.endsWith(ModifyArg.class.getSimpleName() + ";")) {
            /*SL:474*/return new ModifyArgInjectionInfo(a1, a2, v1);
        }
        /*SL:475*/if (v1.desc.endsWith(ModifyArgs.class.getSimpleName() + ";")) {
            /*SL:476*/return new ModifyArgsInjectionInfo(a1, a2, v1);
        }
        /*SL:477*/if (v1.desc.endsWith(Redirect.class.getSimpleName() + ";")) {
            /*SL:478*/return new RedirectInjectionInfo(a1, a2, v1);
        }
        /*SL:479*/if (v1.desc.endsWith(ModifyVariable.class.getSimpleName() + ";")) {
            /*SL:480*/return new ModifyVariableInjectionInfo(a1, a2, v1);
        }
        /*SL:481*/if (v1.desc.endsWith(ModifyConstant.class.getSimpleName() + ";")) {
            /*SL:482*/return new ModifyConstantInjectionInfo(a1, a2, v1);
        }
        /*SL:485*/return null;
    }
    
    public static AnnotationNode getInjectorAnnotation(final IMixinInfo a2, final MethodNode v1) {
        AnnotationNode v2 = /*EL:499*/null;
        try {
            /*SL:501*/v2 = Annotations.getSingleVisible(v1, Inject.class, ModifyArg.class, ModifyArgs.class, Redirect.class, ModifyVariable.class, ModifyConstant.class);
        }
        catch (IllegalArgumentException a3) {
            /*SL:510*/throw new InvalidMixinException(a2, String.format("Error parsing annotations on %s in %s: %s", v1.name, a2.getClassName(), a3.getMessage()));
        }
        /*SL:513*/return v2;
    }
    
    public static String getInjectorPrefix(final AnnotationNode a1) {
        /*SL:523*/if (a1 != null) {
            /*SL:524*/if (a1.desc.endsWith(ModifyArg.class.getSimpleName() + ";")) {
                /*SL:525*/return "modify";
            }
            /*SL:526*/if (a1.desc.endsWith(ModifyArgs.class.getSimpleName() + ";")) {
                /*SL:527*/return "args";
            }
            /*SL:528*/if (a1.desc.endsWith(Redirect.class.getSimpleName() + ";")) {
                /*SL:529*/return "redirect";
            }
            /*SL:530*/if (a1.desc.endsWith(ModifyVariable.class.getSimpleName() + ";")) {
                /*SL:531*/return "localvar";
            }
            /*SL:532*/if (a1.desc.endsWith(ModifyConstant.class.getSimpleName() + ";")) {
                /*SL:533*/return "constant";
            }
        }
        /*SL:536*/return "handler";
    }
    
    static String describeInjector(final IMixinContext a1, final AnnotationNode a2, final MethodNode a3) {
        /*SL:540*/return String.format("%s->@%s::%s%s", a1.toString(), Bytecode.getSimpleName(a2), a3.name, a3.desc);
    }
    
    private static String namesOf(final Collection<MemberInfo> v1) {
        int v2 = /*EL:550*/0;
        final int v3 = v1.size();
        final StringBuilder v4 = /*EL:551*/new StringBuilder();
        /*SL:552*/for (final MemberInfo a1 : v1) {
            /*SL:553*/if (v2 > 0) {
                /*SL:554*/if (v2 == v3 - 1) {
                    /*SL:555*/v4.append(" or ");
                }
                else {
                    /*SL:557*/v4.append(", ");
                }
            }
            /*SL:560*/v4.append('\'').append(a1.name).append('\'');
            /*SL:561*/++v2;
        }
        /*SL:563*/return v4.toString();
    }
}

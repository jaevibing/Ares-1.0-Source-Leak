package org.spongepowered.asm.mixin.injection.struct;

import com.google.common.base.Joiner;
import org.spongepowered.asm.mixin.injection.throwables.InvalidInjectionPointException;
import org.spongepowered.asm.mixin.injection.modify.LocalVariableDiscriminator;
import org.spongepowered.asm.lib.Type;
import java.util.Iterator;
import java.util.regex.Matcher;
import com.google.common.base.Strings;
import java.util.HashMap;
import java.util.List;
import org.spongepowered.asm.mixin.injection.InjectionPoint;
import org.spongepowered.asm.lib.tree.AnnotationNode;
import org.spongepowered.asm.lib.tree.MethodNode;
import org.spongepowered.asm.mixin.refmap.IMixinContext;
import java.util.Map;
import java.util.regex.Pattern;

public class InjectionPointData
{
    private static final Pattern AT_PATTERN;
    private final Map<String, String> args;
    private final IMixinContext context;
    private final MethodNode method;
    private final AnnotationNode parent;
    private final String at;
    private final String type;
    private final InjectionPoint.Selector selector;
    private final String target;
    private final String slice;
    private final int ordinal;
    private final int opcode;
    private final String id;
    
    public InjectionPointData(final IMixinContext a1, final MethodNode a2, final AnnotationNode a3, final String a4, final List<String> a5, final String a6, final String a7, final int a8, final int a9, final String a10) {
        this.args = new HashMap<String, String>();
        this.context = a1;
        this.method = a2;
        this.parent = a3;
        this.at = a4;
        this.target = a6;
        this.slice = Strings.nullToEmpty(a7);
        this.ordinal = Math.max(-1, a8);
        this.opcode = a9;
        this.id = a10;
        this.parseArgs(a5);
        this.args.put("target", a6);
        this.args.put("ordinal", String.valueOf(a8));
        this.args.put("opcode", String.valueOf(a9));
        final Matcher v1 = InjectionPointData.AT_PATTERN.matcher(a4);
        this.type = parseType(v1, a4);
        this.selector = parseSelector(v1);
    }
    
    private void parseArgs(final List<String> v-1) {
        /*SL:141*/if (v-1 == null) {
            /*SL:142*/return;
        }
        /*SL:144*/for (final String v1 : v-1) {
            /*SL:145*/if (v1 != null) {
                final int a1 = /*EL:146*/v1.indexOf(61);
                /*SL:147*/if (a1 > -1) {
                    /*SL:148*/this.args.put(v1.substring(0, a1), v1.substring(a1 + 1));
                }
                else {
                    /*SL:150*/this.args.put(v1, "");
                }
            }
        }
    }
    
    public String getAt() {
        /*SL:160*/return this.at;
    }
    
    public String getType() {
        /*SL:167*/return this.type;
    }
    
    public InjectionPoint.Selector getSelector() {
        /*SL:174*/return this.selector;
    }
    
    public IMixinContext getContext() {
        /*SL:181*/return this.context;
    }
    
    public MethodNode getMethod() {
        /*SL:188*/return this.method;
    }
    
    public Type getMethodReturnType() {
        /*SL:195*/return Type.getReturnType(this.method.desc);
    }
    
    public AnnotationNode getParent() {
        /*SL:202*/return this.parent;
    }
    
    public String getSlice() {
        /*SL:209*/return this.slice;
    }
    
    public LocalVariableDiscriminator getLocalVariableDiscriminator() {
        /*SL:213*/return LocalVariableDiscriminator.parse(this.parent);
    }
    
    public String get(final String a1, final String a2) {
        final String v1 = /*EL:225*/this.args.get(a1);
        /*SL:226*/return (v1 != null) ? v1 : a2;
    }
    
    public int get(final String a1, final int a2) {
        /*SL:238*/return parseInt(this.get(a1, String.valueOf(a2)), a2);
    }
    
    public boolean get(final String a1, final boolean a2) {
        /*SL:250*/return parseBoolean(this.get(a1, String.valueOf(a2)), a2);
    }
    
    public MemberInfo get(final String v2) {
        try {
            /*SL:262*/return MemberInfo.parseAndValidate(this.get(v2, ""), this.context);
        }
        catch (InvalidMemberDescriptorException a1) {
            /*SL:264*/throw new InvalidInjectionPointException(this.context, "Failed parsing @At(\"%s\").%s descriptor \"%s\" on %s", new Object[] { this.at, v2, this.target, /*EL:265*/InjectionInfo.describeInjector(this.context, this.parent, this.method) });
        }
    }
    
    public MemberInfo getTarget() {
        try {
            /*SL:274*/return MemberInfo.parseAndValidate(this.target, this.context);
        }
        catch (InvalidMemberDescriptorException v1) {
            /*SL:276*/throw new InvalidInjectionPointException(this.context, "Failed parsing @At(\"%s\") descriptor \"%s\" on %s", new Object[] { this.at, this.target, /*EL:277*/InjectionInfo.describeInjector(this.context, this.parent, this.method) });
        }
    }
    
    public int getOrdinal() {
        /*SL:285*/return this.ordinal;
    }
    
    public int getOpcode() {
        /*SL:292*/return this.opcode;
    }
    
    public int getOpcode(final int a1) {
        /*SL:303*/return (this.opcode > 0) ? this.opcode : a1;
    }
    
    public int getOpcode(final int v1, final int... v2) {
        /*SL:316*/for (final int a1 : v2) {
            /*SL:317*/if (this.opcode == a1) {
                /*SL:318*/return this.opcode;
            }
        }
        /*SL:321*/return v1;
    }
    
    public String getId() {
        /*SL:328*/return this.id;
    }
    
    @Override
    public String toString() {
        /*SL:333*/return this.type;
    }
    
    private static Pattern createPattern() {
        /*SL:337*/return Pattern.compile(String.format("^([^:]+):?(%s)?$", Joiner.on('|').join(InjectionPoint.Selector.values())));
    }
    
    public static String parseType(final String a1) {
        final Matcher v1 = InjectionPointData.AT_PATTERN.matcher(/*EL:347*/a1);
        /*SL:348*/return parseType(v1, a1);
    }
    
    private static String parseType(final Matcher a1, final String a2) {
        /*SL:352*/return a1.matches() ? a1.group(1) : a2;
    }
    
    private static InjectionPoint.Selector parseSelector(final Matcher a1) {
        /*SL:356*/return (a1.matches() && a1.group(2) != null) ? InjectionPoint.Selector.valueOf(a1.group(2)) : InjectionPoint.Selector.DEFAULT;
    }
    
    private static int parseInt(final String a2, final int v1) {
        try {
            /*SL:361*/return Integer.parseInt(a2);
        }
        catch (Exception a3) {
            /*SL:363*/return v1;
        }
    }
    
    private static boolean parseBoolean(final String a2, final boolean v1) {
        try {
            /*SL:369*/return Boolean.parseBoolean(a2);
        }
        catch (Exception a3) {
            /*SL:371*/return v1;
        }
    }
    
    static {
        AT_PATTERN = createPattern();
    }
}

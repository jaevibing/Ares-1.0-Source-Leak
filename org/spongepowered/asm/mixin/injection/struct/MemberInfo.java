package org.spongepowered.asm.mixin.injection.struct;

import com.google.common.base.Strings;
import org.spongepowered.asm.mixin.refmap.IMixinContext;
import org.spongepowered.asm.mixin.refmap.IReferenceMapper;
import com.google.common.base.Objects;
import org.spongepowered.asm.lib.Type;
import org.spongepowered.asm.obfuscation.mapping.common.MappingField;
import org.spongepowered.asm.util.SignaturePrinter;
import org.spongepowered.asm.mixin.throwables.MixinException;
import org.spongepowered.asm.obfuscation.mapping.common.MappingMethod;
import org.spongepowered.asm.obfuscation.mapping.IMapping;
import org.spongepowered.asm.lib.tree.FieldInsnNode;
import org.spongepowered.asm.lib.tree.MethodInsnNode;
import org.spongepowered.asm.lib.tree.AbstractInsnNode;

public final class MemberInfo
{
    public final String owner;
    public final String name;
    public final String desc;
    public final boolean matchAll;
    private final boolean forceField;
    private final String unparsed;
    
    public MemberInfo(final String a1, final boolean a2) {
        this(a1, null, null, a2);
    }
    
    public MemberInfo(final String a1, final String a2, final boolean a3) {
        this(a1, a2, null, a3);
    }
    
    public MemberInfo(final String a1, final String a2, final String a3) {
        this(a1, a2, a3, false);
    }
    
    public MemberInfo(final String a1, final String a2, final String a3, final boolean a4) {
        this(a1, a2, a3, a4, null);
    }
    
    public MemberInfo(final String a1, final String a2, final String a3, final boolean a4, final String a5) {
        if (a2 != null && a2.contains(".")) {
            throw new IllegalArgumentException("Attempt to instance a MemberInfo with an invalid owner format");
        }
        this.owner = a2;
        this.name = a1;
        this.desc = a3;
        this.matchAll = a4;
        this.forceField = false;
        this.unparsed = a5;
    }
    
    public MemberInfo(final AbstractInsnNode v0) {
        this.matchAll = false;
        this.forceField = false;
        this.unparsed = null;
        if (v0 instanceof MethodInsnNode) {
            final MethodInsnNode a1 = (MethodInsnNode)v0;
            this.owner = a1.owner;
            this.name = a1.name;
            this.desc = a1.desc;
        }
        else {
            if (!(v0 instanceof FieldInsnNode)) {
                throw new IllegalArgumentException("insn must be an instance of MethodInsnNode or FieldInsnNode");
            }
            final FieldInsnNode v = (FieldInsnNode)v0;
            this.owner = v.owner;
            this.name = v.name;
            this.desc = v.desc;
        }
    }
    
    public MemberInfo(final IMapping<?> a1) {
        this.owner = a1.getOwner();
        this.name = a1.getSimpleName();
        this.desc = a1.getDesc();
        this.matchAll = false;
        this.forceField = (a1.getType() == IMapping.Type.FIELD);
        this.unparsed = null;
    }
    
    private MemberInfo(final MemberInfo a1, final MappingMethod a2, final boolean a3) {
        this.owner = (a3 ? a2.getOwner() : a1.owner);
        this.name = a2.getSimpleName();
        this.desc = a2.getDesc();
        this.matchAll = a1.matchAll;
        this.forceField = false;
        this.unparsed = null;
    }
    
    private MemberInfo(final MemberInfo a1, final String a2) {
        this.owner = a2;
        this.name = a1.name;
        this.desc = a1.desc;
        this.matchAll = a1.matchAll;
        this.forceField = a1.forceField;
        this.unparsed = null;
    }
    
    @Override
    public String toString() {
        final String v1 = /*EL:260*/(this.owner != null) ? ("L" + this.owner + ";") : "";
        final String v2 = /*EL:261*/(this.name != null) ? this.name : "";
        final String v3 = /*EL:262*/this.matchAll ? "*" : "";
        final String v4 = /*EL:263*/(this.desc != null) ? this.desc : "";
        final String v5 = /*EL:264*/v4.startsWith("(") ? "" : ((this.desc != null) ? ":" : "");
        /*SL:265*/return v1 + v2 + v3 + v5 + v4;
    }
    
    @Deprecated
    public String toSrg() {
        /*SL:276*/if (!this.isFullyQualified()) {
            /*SL:277*/throw new MixinException("Cannot convert unqualified reference to SRG mapping");
        }
        /*SL:280*/if (this.desc.startsWith("(")) {
            /*SL:281*/return this.owner + "/" + this.name + " " + this.desc;
        }
        /*SL:284*/return this.owner + "/" + this.name;
    }
    
    public String toDescriptor() {
        /*SL:291*/if (this.desc == null) {
            /*SL:292*/return "";
        }
        /*SL:295*/return new SignaturePrinter(this).setFullyQualified(true).toDescriptor();
    }
    
    public String toCtorType() {
        /*SL:302*/if (this.unparsed == null) {
            /*SL:303*/return null;
        }
        final String v1 = /*EL:306*/this.getReturnType();
        /*SL:307*/if (v1 != null) {
            /*SL:308*/return v1;
        }
        /*SL:311*/if (this.owner != null) {
            /*SL:312*/return this.owner;
        }
        /*SL:315*/if (this.name != null && this.desc == null) {
            /*SL:316*/return this.name;
        }
        /*SL:319*/return (this.desc != null) ? this.desc : this.unparsed;
    }
    
    public String toCtorDesc() {
        /*SL:327*/if (this.desc != null && this.desc.startsWith("(") && this.desc.indexOf(41) > -1) {
            /*SL:328*/return this.desc.substring(0, this.desc.indexOf(41) + 1) + "V";
        }
        /*SL:331*/return null;
    }
    
    public String getReturnType() {
        /*SL:340*/if (this.desc == null || this.desc.indexOf(41) == -1 || this.desc.indexOf(40) != 0) {
            /*SL:341*/return null;
        }
        final String v1 = /*EL:344*/this.desc.substring(this.desc.indexOf(41) + 1);
        /*SL:345*/if (v1.startsWith("L") && v1.endsWith(";")) {
            /*SL:346*/return v1.substring(1, v1.length() - 1);
        }
        /*SL:348*/return v1;
    }
    
    public IMapping<?> asMapping() {
        /*SL:356*/return (IMapping<?>)(this.isField() ? this.asFieldMapping() : this.asMethodMapping());
    }
    
    public MappingMethod asMethodMapping() {
        /*SL:363*/if (!this.isFullyQualified()) {
            /*SL:364*/throw new MixinException("Cannot convert unqualified reference " + this + " to MethodMapping");
        }
        /*SL:367*/if (this.isField()) {
            /*SL:368*/throw new MixinException("Cannot convert a non-method reference " + this + " to MethodMapping");
        }
        /*SL:371*/return new MappingMethod(this.owner, this.name, this.desc);
    }
    
    public MappingField asFieldMapping() {
        /*SL:378*/if (!this.isField()) {
            /*SL:379*/throw new MixinException("Cannot convert non-field reference " + this + " to FieldMapping");
        }
        /*SL:382*/return new MappingField(this.owner, this.name, this.desc);
    }
    
    public boolean isFullyQualified() {
        /*SL:391*/return this.owner != null && this.name != null && this.desc != null;
    }
    
    public boolean isField() {
        /*SL:401*/return this.forceField || (this.desc != null && !this.desc.startsWith("("));
    }
    
    public boolean isConstructor() {
        /*SL:410*/return "<init>".equals(this.name);
    }
    
    public boolean isClassInitialiser() {
        /*SL:419*/return "<clinit>".equals(this.name);
    }
    
    public boolean isInitialiser() {
        /*SL:429*/return this.isConstructor() || this.isClassInitialiser();
    }
    
    public MemberInfo validate() throws InvalidMemberDescriptorException {
        /*SL:442*/if (this.owner != null) {
            /*SL:443*/if (!this.owner.matches("(?i)^[\\w\\p{Sc}/]+$")) {
                /*SL:444*/throw new InvalidMemberDescriptorException("Invalid owner: " + this.owner);
            }
            /*SL:449*/if (this.unparsed != null && this.unparsed.lastIndexOf(46) > 0 && this.owner.startsWith("L")) {
                /*SL:450*/throw new InvalidMemberDescriptorException("Malformed owner: " + this.owner + " If you are seeing this message unexpectedly and the owner appears to be correct, replace the owner descriptor with formal type L" + this.owner + "; to suppress this error");
            }
        }
        /*SL:456*/if (this.name != null && !this.name.matches("(?i)^<?[\\w\\p{Sc}]+>?$")) {
            /*SL:457*/throw new InvalidMemberDescriptorException("Invalid name: " + this.name);
        }
        /*SL:460*/if (this.desc != null) {
            /*SL:461*/if (!this.desc.matches("^(\\([\\w\\p{Sc}\\[/;]*\\))?\\[*[\\w\\p{Sc}/;]+$")) {
                /*SL:462*/throw new InvalidMemberDescriptorException("Invalid descriptor: " + this.desc);
            }
            /*SL:464*/if (this.isField()) {
                /*SL:465*/if (!this.desc.equals(Type.getType(this.desc).getDescriptor())) {
                    /*SL:466*/throw new InvalidMemberDescriptorException("Invalid field type in descriptor: " + this.desc);
                }
            }
            else {
                try {
                    /*SL:470*/Type.getArgumentTypes(this.desc);
                }
                catch (Exception v3) {
                    /*SL:472*/throw new InvalidMemberDescriptorException("Invalid descriptor: " + this.desc);
                }
                final String v1 = /*EL:475*/this.desc.substring(this.desc.indexOf(41) + 1);
                try {
                    final Type v2 = /*EL:477*/Type.getType(v1);
                    /*SL:478*/if (!v1.equals(v2.getDescriptor())) {
                        /*SL:479*/throw new InvalidMemberDescriptorException("Invalid return type \"" + v1 + "\" in descriptor: " + this.desc);
                    }
                }
                catch (Exception v4) {
                    /*SL:482*/throw new InvalidMemberDescriptorException("Invalid return type \"" + v1 + "\" in descriptor: " + this.desc);
                }
            }
        }
        /*SL:487*/return this;
    }
    
    public boolean matches(final String a1, final String a2, final String a3) {
        /*SL:501*/return this.matches(a1, a2, a3, 0);
    }
    
    public boolean matches(final String a1, final String a2, final String a3, final int a4) {
        /*SL:523*/return (this.desc == null || a3 == null || this.desc.equals(a3)) && (this.name == null || a2 == null || this.name.equals(a2)) && (this.owner == null || a1 == null || this.owner.equals(a1)) && /*EL:526*/(a4 == 0 || this.matchAll);
    }
    
    public boolean matches(final String a1, final String a2) {
        /*SL:539*/return this.matches(a1, a2, 0);
    }
    
    public boolean matches(final String a1, final String a2, final int a3) {
        /*SL:554*/return (this.name == null || this.name.equals(a1)) && (this.desc == null || (a2 != null && a2.equals(this.desc))) && /*EL:555*/(a3 == 0 || this.matchAll);
    }
    
    @Override
    public boolean equals(final Object a1) {
        /*SL:564*/if (a1 == null || a1.getClass() != MemberInfo.class) {
            /*SL:565*/return false;
        }
        final MemberInfo v1 = /*EL:568*/(MemberInfo)a1;
        /*SL:569*/return this.matchAll == v1.matchAll && this.forceField == v1.forceField && /*EL:570*/Objects.equal(this.owner, v1.owner) && /*EL:571*/Objects.equal(this.name, v1.name) && /*EL:572*/Objects.equal(this.desc, v1.desc);
    }
    
    @Override
    public int hashCode() {
        /*SL:580*/return Objects.hashCode(this.matchAll, this.owner, this.name, this.desc);
    }
    
    public MemberInfo move(final String a1) {
        /*SL:589*/if ((a1 == null && this.owner == null) || (a1 != null && a1.equals(this.owner))) {
            /*SL:590*/return this;
        }
        /*SL:592*/return new MemberInfo(this, a1);
    }
    
    public MemberInfo transform(final String a1) {
        /*SL:601*/if ((a1 == null && this.desc == null) || (a1 != null && a1.equals(this.desc))) {
            /*SL:602*/return this;
        }
        /*SL:604*/return new MemberInfo(this.name, this.owner, a1, this.matchAll);
    }
    
    public MemberInfo remapUsing(final MappingMethod a1, final boolean a2) {
        /*SL:615*/return new MemberInfo(this, a1, a2);
    }
    
    public static MemberInfo parseAndValidate(final String a1) throws InvalidMemberDescriptorException {
        /*SL:625*/return parse(a1, null, null).validate();
    }
    
    public static MemberInfo parseAndValidate(final String a1, final IMixinContext a2) throws InvalidMemberDescriptorException {
        /*SL:636*/return parse(a1, a2.getReferenceMapper(), a2.getClassRef()).validate();
    }
    
    public static MemberInfo parse(final String a1) {
        /*SL:646*/return parse(a1, null, null);
    }
    
    public static MemberInfo parse(final String a1, final IMixinContext a2) {
        /*SL:657*/return parse(a1, a2.getReferenceMapper(), a2.getClassRef());
    }
    
    private static MemberInfo parse(final String a1, final IReferenceMapper a2, final String a3) {
        String v1 = /*EL:669*/null;
        String v2 = /*EL:670*/null;
        String v3 = /*EL:671*/Strings.nullToEmpty(a1).replaceAll("\\s", "");
        /*SL:673*/if (a2 != null) {
            /*SL:674*/v3 = a2.remap(a3, v3);
        }
        final int v4 = /*EL:677*/v3.lastIndexOf(46);
        final int v5 = /*EL:678*/v3.indexOf(59);
        /*SL:679*/if (v4 > -1) {
            /*SL:680*/v2 = v3.substring(0, v4).replace('.', '/');
            /*SL:681*/v3 = v3.substring(v4 + 1);
        }
        else/*SL:682*/ if (v5 > -1 && v3.startsWith("L")) {
            /*SL:683*/v2 = v3.substring(1, v5).replace('.', '/');
            /*SL:684*/v3 = v3.substring(v5 + 1);
        }
        final int v6 = /*EL:687*/v3.indexOf(40);
        final int v7 = /*EL:688*/v3.indexOf(58);
        /*SL:689*/if (v6 > -1) {
            /*SL:690*/v1 = v3.substring(v6);
            /*SL:691*/v3 = v3.substring(0, v6);
        }
        else/*SL:692*/ if (v7 > -1) {
            /*SL:693*/v1 = v3.substring(v7 + 1);
            /*SL:694*/v3 = v3.substring(0, v7);
        }
        /*SL:697*/if ((v3.indexOf(47) > -1 || v3.indexOf(46) > -1) && v2 == null) {
            /*SL:698*/v2 = v3;
            /*SL:699*/v3 = "";
        }
        final boolean v8 = /*EL:702*/v3.endsWith("*");
        /*SL:703*/if (v8) {
            /*SL:704*/v3 = v3.substring(0, v3.length() - 1);
        }
        /*SL:707*/if (v3.isEmpty()) {
            /*SL:708*/v3 = null;
        }
        /*SL:711*/return new MemberInfo(v3, v2, v1, v8, a1);
    }
    
    public static MemberInfo fromMapping(final IMapping<?> a1) {
        /*SL:721*/return new MemberInfo(a1);
    }
}

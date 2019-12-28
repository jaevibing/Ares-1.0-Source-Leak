package org.spongepowered.asm.mixin.injection.points;

import org.apache.logging.log4j.LogManager;
import java.util.ListIterator;
import org.spongepowered.asm.lib.tree.FrameNode;
import org.spongepowered.asm.lib.tree.LabelNode;
import org.spongepowered.asm.util.Bytecode;
import org.spongepowered.asm.lib.tree.AbstractInsnNode;
import org.spongepowered.asm.lib.tree.InsnList;
import java.util.Iterator;
import java.util.Set;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;
import org.spongepowered.asm.mixin.injection.throwables.InvalidInjectionException;
import com.google.common.primitives.Doubles;
import com.google.common.primitives.Longs;
import com.google.common.primitives.Floats;
import com.google.common.primitives.Ints;
import org.spongepowered.asm.mixin.injection.struct.InjectionPointData;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.util.Annotations;
import org.spongepowered.asm.lib.tree.AnnotationNode;
import org.spongepowered.asm.mixin.refmap.IMixinContext;
import org.spongepowered.asm.lib.Type;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.injection.InjectionPoint;

@AtCode("CONSTANT")
public class BeforeConstant extends InjectionPoint
{
    private static final Logger logger;
    private final int ordinal;
    private final boolean nullValue;
    private final Integer intValue;
    private final Float floatValue;
    private final Long longValue;
    private final Double doubleValue;
    private final String stringValue;
    private final Type typeValue;
    private final int[] expandOpcodes;
    private final boolean expand;
    private final String matchByType;
    private final boolean log;
    
    public BeforeConstant(final IMixinContext a1, final AnnotationNode a2, final String a3) {
        super(Annotations.<String>getValue(a2, "slice", ""), Selector.DEFAULT, null);
        final Boolean v1 = Annotations.<Boolean>getValue(a2, "nullValue", (Boolean)null);
        this.ordinal = Annotations.<Integer>getValue(a2, "ordinal", Integer.valueOf(-1));
        this.nullValue = (v1 != null && v1);
        this.intValue = Annotations.<Integer>getValue(a2, "intValue", (Integer)null);
        this.floatValue = Annotations.<Float>getValue(a2, "floatValue", (Float)null);
        this.longValue = Annotations.<Long>getValue(a2, "longValue", (Long)null);
        this.doubleValue = Annotations.<Double>getValue(a2, "doubleValue", (Double)null);
        this.stringValue = Annotations.<String>getValue(a2, "stringValue", (String)null);
        this.typeValue = Annotations.<Type>getValue(a2, "classValue", (Type)null);
        this.matchByType = this.validateDiscriminator(a1, a3, v1, "on @Constant annotation");
        this.expandOpcodes = this.parseExpandOpcodes(Annotations.<Constant.Condition>getValue(a2, "expandZeroConditions", true, Constant.Condition.class));
        this.expand = (this.expandOpcodes.length > 0);
        this.log = Annotations.<Boolean>getValue(a2, "log", Boolean.FALSE);
    }
    
    public BeforeConstant(final InjectionPointData v2) {
        super(v2);
        final String v3 = v2.get("nullValue", null);
        final Boolean v4 = (v3 != null) ? Boolean.parseBoolean(v3) : null;
        this.ordinal = v2.getOrdinal();
        this.nullValue = (v4 != null && v4);
        this.intValue = Ints.tryParse(v2.get("intValue", ""));
        this.floatValue = Floats.tryParse(v2.get("floatValue", ""));
        this.longValue = Longs.tryParse(v2.get("longValue", ""));
        this.doubleValue = Doubles.tryParse(v2.get("doubleValue", ""));
        this.stringValue = v2.get("stringValue", null);
        final String v5 = v2.get("classValue", null);
        this.typeValue = ((v5 != null) ? Type.getObjectType(v5.replace('.', '/')) : null);
        this.matchByType = this.validateDiscriminator(v2.getContext(), "V", v4, "in @At(\"CONSTANT\") args");
        if ("V".equals(this.matchByType)) {
            throw new InvalidInjectionException(v2.getContext(), "No constant discriminator could be parsed in @At(\"CONSTANT\") args");
        }
        final List<Constant.Condition> v6 = new ArrayList<Constant.Condition>();
        final String v7 = v2.get("expandZeroConditions", "").toLowerCase();
        for (final Constant.Condition a1 : Constant.Condition.values()) {
            if (v7.contains(a1.name().toLowerCase())) {
                v6.add(a1);
            }
        }
        this.expandOpcodes = this.parseExpandOpcodes(v6);
        this.expand = (this.expandOpcodes.length > 0);
        this.log = v2.get("log", false);
    }
    
    private String validateDiscriminator(final IMixinContext a1, String a2, final Boolean a3, final String a4) {
        final int v1 = count(/*EL:204*/a3, this.intValue, this.floatValue, this.longValue, this.doubleValue, this.stringValue, this.typeValue);
        /*SL:205*/if (v1 == 1) {
            /*SL:206*/a2 = null;
        }
        else/*SL:207*/ if (v1 > 1) {
            /*SL:208*/throw new InvalidInjectionException(a1, "Conflicting constant discriminators specified " + a4 + " for " + a1);
        }
        /*SL:210*/return a2;
    }
    
    private int[] parseExpandOpcodes(final List<Constant.Condition> v-3) {
        final Set<Integer> collection = /*EL:214*/new HashSet<Integer>();
        /*SL:215*/for (final Constant.Condition v0 : v-3) {
            final Constant.Condition v = /*EL:216*/v0.getEquivalentCondition();
            /*SL:217*/for (final int a1 : v.getOpcodes()) {
                /*SL:218*/collection.add(a1);
            }
        }
        /*SL:221*/return Ints.toArray(collection);
    }
    
    @Override
    public boolean find(final String v-5, final InsnList v-4, final Collection<AbstractInsnNode> v-3) {
        boolean b = /*EL:226*/false;
        /*SL:228*/this.log("BeforeConstant is searching for constants in method with descriptor {}", v-5);
        final ListIterator<AbstractInsnNode> iterator = /*EL:230*/v-4.iterator();
        int a3 = /*EL:231*/0;
        int v1 = 0;
        while (iterator.hasNext()) {
            final AbstractInsnNode a2 = /*EL:232*/iterator.next();
            /*SL:234*/a3 = (this.expand ? this.matchesConditionalInsn(v1, a2) : this.matchesConstantInsn(a2));
            /*SL:235*/if (a3) {
                /*SL:236*/this.log("    BeforeConstant found a matching constant{} at ordinal {}", (this.matchByType != null) ? " TYPE" : " value", a3);
                /*SL:237*/if (this.ordinal == -1 || this.ordinal == a3) {
                    /*SL:238*/this.log("      BeforeConstant found {}", Bytecode.describeNode(a2).trim());
                    /*SL:239*/v-3.add(a2);
                    /*SL:240*/b = true;
                }
                /*SL:242*/++a3;
            }
            /*SL:245*/if (!(a2 instanceof LabelNode) && !(a2 instanceof FrameNode)) {
                /*SL:246*/v1 = a2.getOpcode();
            }
        }
        /*SL:250*/return b;
    }
    
    private boolean matchesConditionalInsn(final int v-1, final AbstractInsnNode v0) {
        final int[] expandOpcodes = /*EL:254*/this.expandOpcodes;
        final int length = expandOpcodes.length;
        int i = 0;
        while (i < length) {
            int a2 = expandOpcodes[i];
            /*SL:255*/a2 = v0.getOpcode();
            /*SL:256*/if (a2 == a2) {
                /*SL:257*/if (v-1 == 148 || v-1 == 149 || v-1 == 150 || v-1 == 151 || v-1 == 152) {
                    /*SL:258*/this.log("  BeforeConstant is ignoring {} following {}", Bytecode.getOpcodeName(a2), Bytecode.getOpcodeName(v-1));
                    /*SL:259*/return false;
                }
                /*SL:262*/this.log("  BeforeConstant found {} instruction", Bytecode.getOpcodeName(a2));
                /*SL:263*/return true;
            }
            else {
                ++i;
            }
        }
        /*SL:267*/if (this.intValue != null && this.intValue == 0 && Bytecode.isConstant(v0)) {
            final Object v = /*EL:268*/Bytecode.getConstant(v0);
            /*SL:269*/this.log("  BeforeConstant found INTEGER constant: value = {}", v);
            /*SL:270*/return v instanceof Integer && (int)v == 0;
        }
        /*SL:273*/return false;
    }
    
    private boolean matchesConstantInsn(final AbstractInsnNode a1) {
        /*SL:277*/if (!Bytecode.isConstant(a1)) {
            /*SL:278*/return false;
        }
        final Object v1 = /*EL:281*/Bytecode.getConstant(a1);
        /*SL:282*/if (v1 == null) {
            /*SL:283*/this.log("  BeforeConstant found NULL constant: nullValue = {}", this.nullValue);
            /*SL:284*/return this.nullValue || "Ljava/lang/Object;".equals(this.matchByType);
        }
        /*SL:285*/if (v1 instanceof Integer) {
            /*SL:286*/this.log("  BeforeConstant found INTEGER constant: value = {}, intValue = {}", v1, this.intValue);
            /*SL:287*/return v1.equals(this.intValue) || "I".equals(this.matchByType);
        }
        /*SL:288*/if (v1 instanceof Float) {
            /*SL:289*/this.log("  BeforeConstant found FLOAT constant: value = {}, floatValue = {}", v1, this.floatValue);
            /*SL:290*/return v1.equals(this.floatValue) || "F".equals(this.matchByType);
        }
        /*SL:291*/if (v1 instanceof Long) {
            /*SL:292*/this.log("  BeforeConstant found LONG constant: value = {}, longValue = {}", v1, this.longValue);
            /*SL:293*/return v1.equals(this.longValue) || "J".equals(this.matchByType);
        }
        /*SL:294*/if (v1 instanceof Double) {
            /*SL:295*/this.log("  BeforeConstant found DOUBLE constant: value = {}, doubleValue = {}", v1, this.doubleValue);
            /*SL:296*/return v1.equals(this.doubleValue) || "D".equals(this.matchByType);
        }
        /*SL:297*/if (v1 instanceof String) {
            /*SL:298*/this.log("  BeforeConstant found STRING constant: value = {}, stringValue = {}", v1, this.stringValue);
            /*SL:299*/return v1.equals(this.stringValue) || "Ljava/lang/String;".equals(this.matchByType);
        }
        /*SL:300*/if (v1 instanceof Type) {
            /*SL:301*/this.log("  BeforeConstant found CLASS constant: value = {}, typeValue = {}", v1, this.typeValue);
            /*SL:302*/return v1.equals(this.typeValue) || "Ljava/lang/Class;".equals(this.matchByType);
        }
        /*SL:305*/return false;
    }
    
    protected void log(final String a1, final Object... a2) {
        /*SL:309*/if (this.log) {
            BeforeConstant.logger.info(/*EL:310*/a1, a2);
        }
    }
    
    private static int count(final Object... v1) {
        int v2 = /*EL:315*/0;
        /*SL:316*/for (final Object a1 : v1) {
            /*SL:317*/if (a1 != null) {
                /*SL:318*/++v2;
            }
        }
        /*SL:321*/return v2;
    }
    
    static {
        logger = LogManager.getLogger("mixin");
    }
}

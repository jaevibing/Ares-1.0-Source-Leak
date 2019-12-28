package org.spongepowered.asm.mixin.injection;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;
import java.lang.annotation.Annotation;
import java.util.LinkedHashSet;
import java.lang.reflect.Array;
import java.util.ArrayList;
import com.google.common.base.Joiner;
import org.spongepowered.asm.mixin.injection.points.BeforeConstant;
import org.spongepowered.asm.mixin.injection.points.BeforeFinalReturn;
import org.spongepowered.asm.mixin.injection.modify.AfterStoreLocal;
import org.spongepowered.asm.mixin.injection.modify.BeforeLoadLocal;
import org.spongepowered.asm.mixin.injection.points.AfterInvoke;
import org.spongepowered.asm.mixin.injection.points.MethodHead;
import org.spongepowered.asm.mixin.injection.points.JumpInsnPoint;
import org.spongepowered.asm.mixin.injection.points.BeforeStringInvoke;
import org.spongepowered.asm.mixin.injection.points.BeforeReturn;
import org.spongepowered.asm.mixin.injection.points.BeforeNew;
import org.spongepowered.asm.mixin.injection.points.BeforeInvoke;
import org.spongepowered.asm.mixin.injection.points.BeforeFieldAccess;
import java.util.HashMap;
import org.apache.logging.log4j.LogManager;
import org.spongepowered.asm.util.Bytecode;
import org.spongepowered.asm.mixin.transformer.MixinTargetContext;
import org.spongepowered.asm.mixin.MixinEnvironment;
import java.lang.reflect.Constructor;
import org.spongepowered.asm.mixin.injection.throwables.InvalidInjectionException;
import org.spongepowered.asm.util.Annotations;
import java.util.Arrays;
import java.util.Iterator;
import com.google.common.collect.ImmutableList;
import org.spongepowered.asm.lib.tree.MethodNode;
import org.spongepowered.asm.mixin.refmap.IMixinContext;
import org.spongepowered.asm.lib.tree.AnnotationNode;
import java.util.List;
import org.spongepowered.asm.lib.tree.AbstractInsnNode;
import java.util.Collection;
import org.spongepowered.asm.lib.tree.InsnList;
import org.spongepowered.asm.mixin.injection.struct.InjectionPointData;
import java.util.Map;

public abstract class InjectionPoint
{
    public static final int DEFAULT_ALLOWED_SHIFT_BY = 0;
    public static final int MAX_ALLOWED_SHIFT_BY = 5;
    private static Map<String, Class<? extends InjectionPoint>> types;
    private final String slice;
    private final Selector selector;
    private final String id;
    
    protected InjectionPoint() {
        this("", Selector.DEFAULT, null);
    }
    
    protected InjectionPoint(final InjectionPointData a1) {
        this(a1.getSlice(), a1.getSelector(), a1.getId());
    }
    
    public InjectionPoint(final String a1, final Selector a2, final String a3) {
        this.slice = a1;
        this.selector = a2;
        this.id = a3;
    }
    
    public String getSlice() {
        /*SL:207*/return this.slice;
    }
    
    public Selector getSelector() {
        /*SL:211*/return this.selector;
    }
    
    public String getId() {
        /*SL:215*/return this.id;
    }
    
    public boolean checkPriority(final int a1, final int a2) {
        /*SL:231*/return a1 < a2;
    }
    
    public abstract boolean find(final String p0, final InsnList p1, final Collection<AbstractInsnNode> p2);
    
    @Override
    public String toString() {
        /*SL:253*/return String.format("@At(\"%s\")", this.getAtCode());
    }
    
    protected static AbstractInsnNode nextNode(final InsnList a1, final AbstractInsnNode a2) {
        final int v1 = /*EL:265*/a1.indexOf(a2) + 1;
        /*SL:266*/if (v1 > 0 && v1 < a1.size()) {
            /*SL:267*/return a1.get(v1);
        }
        /*SL:269*/return a2;
    }
    
    public static InjectionPoint and(final InjectionPoint... a1) {
        /*SL:418*/return new Intersection(a1);
    }
    
    public static InjectionPoint or(final InjectionPoint... a1) {
        /*SL:429*/return new Union(a1);
    }
    
    public static InjectionPoint after(final InjectionPoint a1) {
        /*SL:440*/return new Shift(a1, 1);
    }
    
    public static InjectionPoint before(final InjectionPoint a1) {
        /*SL:451*/return new Shift(a1, -1);
    }
    
    public static InjectionPoint shift(final InjectionPoint a1, final int a2) {
        /*SL:463*/return new Shift(a1, a2);
    }
    
    public static List<InjectionPoint> parse(final IInjectionPointContext a1, final List<AnnotationNode> a2) {
        /*SL:477*/return parse(a1.getContext(), a1.getMethod(), a1.getAnnotation(), a2);
    }
    
    public static List<InjectionPoint> parse(final IMixinContext a3, final MethodNode a4, final AnnotationNode v1, final List<AnnotationNode> v2) {
        final ImmutableList.Builder<InjectionPoint> v3 = /*EL:493*/ImmutableList.<InjectionPoint>builder();
        /*SL:494*/for (final AnnotationNode a5 : v2) {
            final InjectionPoint a6 = parse(/*EL:495*/a3, a4, v1, a5);
            /*SL:496*/if (a6 != null) {
                /*SL:497*/v3.add(a6);
            }
        }
        /*SL:500*/return v3.build();
    }
    
    public static InjectionPoint parse(final IInjectionPointContext a1, final At a2) {
        /*SL:513*/return parse(a1.getContext(), a1.getMethod(), a1.getAnnotation(), a2.value(), a2.shift(), a2.by(), /*EL:514*/Arrays.<String>asList(a2.args()), a2.target(), a2.slice(), a2.ordinal(), a2.opcode(), a2.id());
    }
    
    public static InjectionPoint parse(final IMixinContext a1, final MethodNode a2, final AnnotationNode a3, final At a4) {
        /*SL:529*/return parse(a1, a2, a3, a4.value(), a4.shift(), a4.by(), Arrays.<String>asList(a4.args()), a4.target(), a4.slice(), a4.ordinal(), /*EL:530*/a4.opcode(), a4.id());
    }
    
    public static InjectionPoint parse(final IInjectionPointContext a1, final AnnotationNode a2) {
        /*SL:544*/return parse(a1.getContext(), a1.getMethod(), a1.getAnnotation(), a2);
    }
    
    public static InjectionPoint parse(final IMixinContext a1, final MethodNode a2, final AnnotationNode a3, final AnnotationNode a4) {
        final String v1 = /*EL:560*/Annotations.<String>getValue(a4, "value");
        List<String> v2 = /*EL:561*/Annotations.<List<String>>getValue(a4, "args");
        final String v3 = /*EL:562*/Annotations.<String>getValue(a4, "target", "");
        final String v4 = /*EL:563*/Annotations.<String>getValue(a4, "slice", "");
        final At.Shift v5 = /*EL:564*/Annotations.<At.Shift>getValue(a4, "shift", At.Shift.class, At.Shift.NONE);
        final int v6 = /*EL:565*/Annotations.<Integer>getValue(a4, "by", Integer.valueOf(0));
        final int v7 = /*EL:566*/Annotations.<Integer>getValue(a4, "ordinal", Integer.valueOf(-1));
        final int v8 = /*EL:567*/Annotations.<Integer>getValue(a4, "opcode", Integer.valueOf(0));
        final String v9 = /*EL:568*/Annotations.<String>getValue(a4, "id");
        /*SL:570*/if (v2 == null) {
            /*SL:571*/v2 = (List<String>)ImmutableList.<Object>of();
        }
        /*SL:574*/return parse(a1, a2, a3, v1, v5, v6, v2, v3, v4, v7, v8, v9);
    }
    
    public static InjectionPoint parse(final IMixinContext a1, final MethodNode a2, final AnnotationNode a3, final String a4, final At.Shift a5, final int a6, final List<String> a7, final String a8, final String a9, final int a10, final int a11, final String a12) {
        final InjectionPointData v1 = /*EL:599*/new InjectionPointData(a1, a2, a3, a4, a7, a8, a9, a10, a11, a12);
        final Class<? extends InjectionPoint> v2 = findClass(/*EL:600*/a1, v1);
        final InjectionPoint v3 = create(/*EL:601*/a1, v1, v2);
        /*SL:602*/return shift(a1, a2, a3, v3, a5, a6);
    }
    
    private static Class<? extends InjectionPoint> findClass(final IMixinContext a2, final InjectionPointData v1) {
        final String v2 = /*EL:607*/v1.getType();
        Class<? extends InjectionPoint> v3 = InjectionPoint.types.get(/*EL:608*/v2);
        /*SL:609*/if (v3 == null) {
            /*SL:610*/if (v2.matches("^([A-Za-z_][A-Za-z0-9_]*\\.)+[A-Za-z_][A-Za-z0-9_]*$")) {
                try {
                    /*SL:612*/v3 = (Class<? extends InjectionPoint>)Class.forName(v2);
                    InjectionPoint.types.put(/*EL:613*/v2, v3);
                    /*SL:616*/return /*EL:621*/v3;
                }
                catch (Exception a3) {
                    throw new InvalidInjectionException(a2, v1 + " could not be loaded or is not a valid InjectionPoint", a3);
                }
            }
            throw new InvalidInjectionException(a2, v1 + " is not a valid injection point specifier");
        }
        return v3;
    }
    
    private static InjectionPoint create(final IMixinContext a3, final InjectionPointData v1, final Class<? extends InjectionPoint> v2) {
        Constructor<? extends InjectionPoint> v3 = /*EL:625*/null;
        try {
            /*SL:627*/v3 = v2.getDeclaredConstructor(InjectionPointData.class);
            /*SL:628*/v3.setAccessible(true);
        }
        catch (NoSuchMethodException a4) {
            /*SL:630*/throw new InvalidInjectionException(a3, v2.getName() + " must contain a constructor which accepts an InjectionPointData", a4);
        }
        InjectionPoint v4 = /*EL:633*/null;
        try {
            /*SL:635*/v4 = (InjectionPoint)v3.newInstance(v1);
        }
        catch (Exception a5) {
            /*SL:637*/throw new InvalidInjectionException(a3, "Error whilst instancing injection point " + v2.getName() + " for " + v1.getAt(), a5);
        }
        /*SL:640*/return v4;
    }
    
    private static InjectionPoint shift(final IMixinContext a1, final MethodNode a2, final AnnotationNode a3, final InjectionPoint a4, final At.Shift a5, final int a6) {
        /*SL:646*/if (a4 != null) {
            /*SL:647*/if (a5 == At.Shift.BEFORE) {
                /*SL:648*/return before(a4);
            }
            /*SL:649*/if (a5 == At.Shift.AFTER) {
                /*SL:650*/return after(a4);
            }
            /*SL:651*/if (a5 == At.Shift.BY) {
                validateByValue(/*EL:652*/a1, a2, a3, a4, a6);
                /*SL:653*/return shift(a4, a6);
            }
        }
        /*SL:657*/return a4;
    }
    
    private static void validateByValue(final IMixinContext a1, final MethodNode a2, final AnnotationNode a3, final InjectionPoint a4, final int a5) {
        final MixinEnvironment v1 = /*EL:661*/a1.getMixin().getConfig().getEnvironment();
        final ShiftByViolationBehaviour v2 = /*EL:662*/v1.<ShiftByViolationBehaviour>getOption(MixinEnvironment.Option.SHIFT_BY_VIOLATION_BEHAVIOUR, ShiftByViolationBehaviour.WARN);
        /*SL:663*/if (v2 == ShiftByViolationBehaviour.IGNORE) {
            /*SL:664*/return;
        }
        String v3 = /*EL:667*/"the maximum allowed value: ";
        String v4 = /*EL:668*/"Increase the value of maxShiftBy to suppress this warning.";
        int v5 = /*EL:669*/0;
        /*SL:670*/if (a1 instanceof MixinTargetContext) {
            /*SL:671*/v5 = ((MixinTargetContext)a1).getMaxShiftByValue();
        }
        /*SL:674*/if (a5 <= v5) {
            /*SL:675*/return;
        }
        /*SL:678*/if (a5 > 5) {
            /*SL:679*/v3 = "MAX_ALLOWED_SHIFT_BY=";
            /*SL:680*/v4 = "You must use an alternate query or a custom injection point.";
            /*SL:681*/v5 = 5;
        }
        final String v6 = /*EL:684*/String.format("@%s(%s) Shift.BY=%d on %s::%s exceeds %s%d. %s", Bytecode.getSimpleName(a3), a4, a5, /*EL:685*/a1, a2.name, v3, v5, v4);
        /*SL:687*/if (v2 == ShiftByViolationBehaviour.WARN && v5 < 5) {
            /*SL:688*/LogManager.getLogger("mixin").warn(v6);
            /*SL:689*/return;
        }
        /*SL:692*/throw new InvalidInjectionException(a1, v6);
    }
    
    protected String getAtCode() {
        final AtCode v1 = /*EL:696*/this.getClass().<AtCode>getAnnotation(AtCode.class);
        /*SL:697*/return (v1 == null) ? this.getClass().getName() : v1.value();
    }
    
    public static void register(final Class<? extends InjectionPoint> a1) {
        final AtCode v1 = /*EL:707*/a1.<AtCode>getAnnotation(AtCode.class);
        /*SL:708*/if (v1 == null) {
            /*SL:709*/throw new IllegalArgumentException("Injection point class " + a1 + " is not annotated with @AtCode");
        }
        final Class<? extends InjectionPoint> v2 = InjectionPoint.types.get(/*EL:712*/v1.value());
        /*SL:713*/if (v2 != null && !v2.equals(a1)) {
            /*SL:714*/LogManager.getLogger("mixin").debug("Overriding InjectionPoint {} with {} (previously {})", new Object[] { v1.value(), a1.getName(), v2.getName() });
        }
        InjectionPoint.types.put(/*EL:718*/v1.value(), a1);
    }
    
    static {
        InjectionPoint.types = new HashMap<String, Class<? extends InjectionPoint>>();
        register(BeforeFieldAccess.class);
        register(BeforeInvoke.class);
        register(BeforeNew.class);
        register(BeforeReturn.class);
        register(BeforeStringInvoke.class);
        register(JumpInsnPoint.class);
        register(MethodHead.class);
        register(AfterInvoke.class);
        register(BeforeLoadLocal.class);
        register(AfterStoreLocal.class);
        register(BeforeFinalReturn.class);
        register(BeforeConstant.class);
    }
    
    public enum Selector
    {
        FIRST, 
        LAST, 
        ONE;
        
        public static final Selector DEFAULT;
        
        static {
            DEFAULT = Selector.FIRST;
        }
    }
    
    enum ShiftByViolationBehaviour
    {
        IGNORE, 
        WARN, 
        ERROR;
    }
    
    abstract static class CompositeInjectionPoint extends InjectionPoint
    {
        protected final InjectionPoint[] components;
        
        protected CompositeInjectionPoint(final InjectionPoint... a1) {
            if (a1 == null || a1.length < 2) {
                throw new IllegalArgumentException("Must supply two or more component injection points for composite point!");
            }
            this.components = a1;
        }
        
        @Override
        public String toString() {
            /*SL:292*/return "CompositeInjectionPoint(" + this.getClass().getSimpleName() + ")[" + Joiner.on(',').join(this.components) + "]";
        }
    }
    
    static final class Intersection extends CompositeInjectionPoint
    {
        public Intersection(final InjectionPoint... a1) {
            super(a1);
        }
        
        @Override
        public boolean find(final String v-7, final InsnList v-6, final Collection<AbstractInsnNode> v-5) {
            boolean b = /*EL:309*/false;
            final ArrayList<AbstractInsnNode>[] array = /*EL:311*/(ArrayList<AbstractInsnNode>[])Array.newInstance(ArrayList.class, this.components.length);
            /*SL:313*/for (int a1 = 0; a1 < this.components.length; ++a1) {
                /*SL:314*/array[a1] = new ArrayList<AbstractInsnNode>();
                /*SL:315*/this.components[a1].find(v-7, v-6, array[a1]);
            }
            final ArrayList<AbstractInsnNode> a2 = /*EL:318*/array[0];
            /*SL:319*/for (int i = 0; i < a2.size(); ++i) {
                final AbstractInsnNode a3 = /*EL:320*/a2.get(i);
                final boolean v1 = /*EL:321*/true;
                for (int a4 = /*EL:323*/1; a4 < array.length && /*EL:324*/array[a4].contains(a3); ++a4) {}
                /*SL:329*/if (v1) {
                    /*SL:333*/v-5.add(a3);
                    /*SL:334*/b = true;
                }
            }
            /*SL:337*/return b;
        }
    }
    
    static final class Union extends CompositeInjectionPoint
    {
        public Union(final InjectionPoint... a1) {
            super(a1);
        }
        
        @Override
        public boolean find(final String a3, final InsnList v1, final Collection<AbstractInsnNode> v2) {
            final LinkedHashSet<AbstractInsnNode> v3 = /*EL:353*/new LinkedHashSet<AbstractInsnNode>();
            /*SL:355*/for (int a4 = 0; a4 < this.components.length; ++a4) {
                /*SL:356*/this.components[a4].find(a3, v1, v3);
            }
            /*SL:359*/v2.addAll(v3);
            /*SL:361*/return v3.size() > 0;
        }
    }
    
    static final class Shift extends InjectionPoint
    {
        private final InjectionPoint input;
        private final int shift;
        
        public Shift(final InjectionPoint a1, final int a2) {
            if (a1 == null) {
                throw new IllegalArgumentException("Must supply an input injection point for SHIFT");
            }
            this.input = a1;
            this.shift = a2;
        }
        
        @Override
        public String toString() {
            /*SL:388*/return "InjectionPoint(" + this.getClass().getSimpleName() + ")[" + this.input + "]";
        }
        
        @Override
        public boolean find(final String a3, final InsnList v1, final Collection<AbstractInsnNode> v2) {
            final List<AbstractInsnNode> v3 = /*EL:393*/(v2 instanceof List) ? ((List)v2) : new ArrayList<AbstractInsnNode>(v2);
            /*SL:395*/this.input.find(a3, v1, v2);
            /*SL:397*/for (int a4 = 0; a4 < v3.size(); ++a4) {
                /*SL:398*/v3.set(a4, v1.get(v1.indexOf(v3.get(a4)) + this.shift));
            }
            /*SL:401*/if (v2 != v3) {
                /*SL:402*/v2.clear();
                /*SL:403*/v2.addAll(v3);
            }
            /*SL:406*/return v2.size() > 0;
        }
    }
    
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ ElementType.TYPE })
    public @interface AtCode {
        String value();
    }
}

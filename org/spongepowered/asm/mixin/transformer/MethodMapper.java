package org.spongepowered.asm.mixin.transformer;

import java.util.Iterator;
import org.spongepowered.asm.lib.tree.FrameNode;
import org.spongepowered.asm.lib.tree.AbstractInsnNode;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.gen.Accessor;
import java.lang.annotation.Annotation;
import org.spongepowered.asm.util.Annotations;
import org.spongepowered.asm.mixin.Unique;
import java.util.HashMap;
import java.util.ArrayList;
import org.apache.logging.log4j.LogManager;
import com.google.common.base.Strings;
import org.spongepowered.asm.mixin.injection.struct.InjectionInfo;
import org.spongepowered.asm.lib.tree.MethodNode;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.util.Counter;
import java.util.Map;
import java.util.List;
import org.apache.logging.log4j.Logger;

public class MethodMapper
{
    private static final Logger logger;
    private static final List<String> classes;
    private static final Map<String, Counter> methods;
    private final ClassInfo info;
    
    public MethodMapper(final MixinEnvironment a1, final ClassInfo a2) {
        this.info = a2;
    }
    
    public ClassInfo getClassInfo() {
        /*SL:68*/return this.info;
    }
    
    public void remapHandlerMethod(final MixinInfo a1, final MethodNode a2, final ClassInfo.Method a3) {
        /*SL:79*/if (!(a2 instanceof MixinInfo.MixinMethodNode) || !((MixinInfo.MixinMethodNode)a2).isInjector()) {
            /*SL:80*/return;
        }
        /*SL:83*/if (a3.isUnique()) {
            MethodMapper.logger.warn(/*EL:84*/"Redundant @Unique on injector method {} in {}. Injectors are implicitly unique", new Object[] { a3, a1 });
        }
        /*SL:87*/if (a3.isRenamed()) {
            /*SL:88*/a2.name = a3.getName();
            /*SL:89*/return;
        }
        final String v1 = /*EL:92*/this.getHandlerName((MixinInfo.MixinMethodNode)a2);
        /*SL:93*/a2.name = a3.renameTo(v1);
    }
    
    public String getHandlerName(final MixinInfo.MixinMethodNode a1) {
        final String v1 = /*EL:103*/InjectionInfo.getInjectorPrefix(a1.getInjectorAnnotation());
        final String v2 = getClassUID(/*EL:104*/a1.getOwner().getClassRef());
        final String v3 = getMethodUID(/*EL:105*/a1.name, a1.desc, !a1.isSurrogate());
        /*SL:106*/return String.format("%s$%s$%s%s", v1, a1.name, v2, v3);
    }
    
    private static String getClassUID(final String a1) {
        int v1 = MethodMapper.classes.indexOf(/*EL:116*/a1);
        /*SL:117*/if (v1 < 0) {
            /*SL:118*/v1 = MethodMapper.classes.size();
            MethodMapper.classes.add(/*EL:119*/a1);
        }
        /*SL:121*/return finagle(v1);
    }
    
    private static String getMethodUID(final String a1, final String a2, final boolean a3) {
        final String v1 = /*EL:133*/String.format("%s%s", a1, a2);
        Counter v2 = MethodMapper.methods.get(/*EL:134*/v1);
        /*SL:135*/if (v2 == null) {
            /*SL:136*/v2 = new Counter();
            MethodMapper.methods.put(/*EL:137*/v1, v2);
        }
        else/*SL:138*/ if (a3) {
            final Counter counter = /*EL:139*/v2;
            ++counter.value;
        }
        /*SL:141*/return String.format("%03x", v2.value);
    }
    
    private static String finagle(final int v-2) {
        final String hexString = /*EL:151*/Integer.toHexString(v-2);
        final StringBuilder v0 = /*EL:152*/new StringBuilder();
        /*SL:153*/for (int v = 0; v < hexString.length(); ++v) {
            char a1 = /*EL:154*/hexString.charAt(v);
            /*SL:155*/v0.append(a1 += ((a1 < ':') ? '1' : '\n'));
        }
        /*SL:157*/return Strings.padStart(v0.toString(), 3, 'z');
    }
    
    static {
        logger = LogManager.getLogger("mixin");
        classes = new ArrayList<String>();
        methods = new HashMap<String, Counter>();
    }
}

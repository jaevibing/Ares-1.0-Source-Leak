package org.spongepowered.asm.mixin.transformer;

import org.spongepowered.asm.mixin.injection.throwables.InvalidInjectionException;
import org.spongepowered.asm.mixin.refmap.IMixinContext;
import org.spongepowered.asm.mixin.transformer.throwables.InvalidInterfaceMixinException;
import org.spongepowered.asm.mixin.injection.struct.InjectionInfo;
import org.spongepowered.asm.lib.tree.MethodNode;
import org.spongepowered.asm.lib.tree.FieldNode;
import java.util.Map;
import java.util.Iterator;

class MixinApplicatorInterface extends MixinApplicatorStandard
{
    MixinApplicatorInterface(final TargetClassContext a1) {
        super(a1);
    }
    
    @Override
    protected void applyInterfaces(final MixinTargetContext v2) {
        /*SL:58*/for (final String a1 : v2.getInterfaces()) {
            /*SL:59*/if (!this.targetClass.name.equals(a1) && !this.targetClass.interfaces.contains(a1)) {
                /*SL:60*/this.targetClass.interfaces.add(a1);
                /*SL:61*/v2.getTargetClassInfo().addInterface(a1);
            }
        }
    }
    
    @Override
    protected void applyFields(final MixinTargetContext v-1) {
        /*SL:74*/for (final Map.Entry<FieldNode, ClassInfo.Field> v1 : v-1.getShadowFields()) {
            final FieldNode a1 = /*EL:75*/v1.getKey();
            /*SL:76*/this.logger.error("Ignoring redundant @Shadow field {}:{} in {}", new Object[] { a1.name, a1.desc, v-1 });
        }
        /*SL:79*/this.mergeNewFields(v-1);
    }
    
    @Override
    protected void applyInitialisers(final MixinTargetContext a1) {
    }
    
    @Override
    protected void prepareInjections(final MixinTargetContext v-3) {
        /*SL:100*/for (final MethodNode a2 : this.targetClass.methods) {
            try {
                final InjectionInfo a1 = /*EL:102*/InjectionInfo.parse(v-3, a2);
                /*SL:103*/if (a1 != null) {
                    /*SL:104*/throw new InvalidInterfaceMixinException(v-3, a1 + " is not supported on interface mixin method " + a2.name);
                }
                /*SL:109*/continue;
            }
            catch (InvalidInjectionException v2) {
                final String v1 = (v2.getInjectionInfo() != null) ? v2.getInjectionInfo().toString() : "Injection";
                throw new InvalidInterfaceMixinException(v-3, v1 + " is not supported in interface mixin");
            }
        }
    }
    
    @Override
    protected void applyInjections(final MixinTargetContext a1) {
    }
}

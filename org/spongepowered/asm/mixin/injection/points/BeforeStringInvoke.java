package org.spongepowered.asm.mixin.injection.points;

import org.spongepowered.asm.mixin.injection.struct.MemberInfo;
import org.spongepowered.asm.lib.tree.LdcInsnNode;
import org.spongepowered.asm.lib.tree.AbstractInsnNode;
import java.util.Collection;
import org.spongepowered.asm.lib.tree.InsnList;
import org.spongepowered.asm.mixin.injection.struct.InjectionPointData;
import org.spongepowered.asm.mixin.injection.InjectionPoint;

@AtCode("INVOKE_STRING")
public class BeforeStringInvoke extends BeforeInvoke
{
    private static final String STRING_VOID_SIG = "(Ljava/lang/String;)V";
    private final String ldcValue;
    private boolean foundLdc;
    
    public BeforeStringInvoke(final InjectionPointData a1) {
        super(a1);
        this.ldcValue = a1.get("ldc", null);
        if (this.ldcValue == null) {
            throw new IllegalArgumentException(this.getClass().getSimpleName() + " requires named argument \"ldc\" to specify the desired target");
        }
        if (!"(Ljava/lang/String;)V".equals(this.target.desc)) {
            throw new IllegalArgumentException(this.getClass().getSimpleName() + " requires target method with with signature " + "(Ljava/lang/String;)V");
        }
    }
    
    @Override
    public boolean find(final String a1, final InsnList a2, final Collection<AbstractInsnNode> a3) {
        /*SL:114*/this.foundLdc = false;
        /*SL:116*/return super.find(a1, a2, a3);
    }
    
    @Override
    protected void inspectInsn(final String a3, final InsnList v1, final AbstractInsnNode v2) {
        /*SL:121*/if (v2 instanceof LdcInsnNode) {
            final LdcInsnNode a4 = /*EL:122*/(LdcInsnNode)v2;
            /*SL:123*/if (a4.cst instanceof String && this.ldcValue.equals(a4.cst)) {
                /*SL:124*/this.log("{} > found a matching LDC with value {}", this.className, a4.cst);
                /*SL:125*/this.foundLdc = true;
                /*SL:126*/return;
            }
        }
        /*SL:130*/this.foundLdc = false;
    }
    
    @Override
    protected boolean matchesInsn(final MemberInfo a1, final int a2) {
        /*SL:135*/this.log("{} > > found LDC \"{}\" = {}", this.className, this.ldcValue, this.foundLdc);
        /*SL:136*/return this.foundLdc && super.matchesInsn(a1, a2);
    }
}

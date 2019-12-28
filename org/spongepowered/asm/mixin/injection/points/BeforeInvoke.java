package org.spongepowered.asm.mixin.injection.points;

import org.spongepowered.asm.lib.tree.MethodInsnNode;
import java.util.ListIterator;
import org.spongepowered.asm.lib.tree.AbstractInsnNode;
import java.util.Collection;
import org.spongepowered.asm.lib.tree.InsnList;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.apache.logging.log4j.LogManager;
import org.spongepowered.asm.mixin.injection.struct.InjectionPointData;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.refmap.IMixinContext;
import org.spongepowered.asm.mixin.injection.struct.MemberInfo;
import org.spongepowered.asm.mixin.injection.InjectionPoint;

@AtCode("INVOKE")
public class BeforeInvoke extends InjectionPoint
{
    protected final MemberInfo target;
    protected final boolean allowPermissive;
    protected final int ordinal;
    protected final String className;
    protected final IMixinContext context;
    protected final Logger logger;
    private boolean log;
    
    public BeforeInvoke(final InjectionPointData a1) {
        super(a1);
        this.logger = LogManager.getLogger("mixin");
        this.log = false;
        this.target = a1.getTarget();
        this.ordinal = a1.getOrdinal();
        this.log = a1.get("log", false);
        this.className = this.getClassName();
        this.context = a1.getContext();
        this.allowPermissive = (this.context.getOption(MixinEnvironment.Option.REFMAP_REMAP) && this.context.getOption(MixinEnvironment.Option.REFMAP_REMAP_ALLOW_PERMISSIVE) && !this.context.getReferenceMapper().isDefault());
    }
    
    private String getClassName() {
        final AtCode v1 = /*EL:132*/this.getClass().<AtCode>getAnnotation(AtCode.class);
        /*SL:133*/return String.format("@At(%s)", (v1 != null) ? v1.value() : this.getClass().getSimpleName().toUpperCase());
    }
    
    public BeforeInvoke setLogging(final boolean a1) {
        /*SL:143*/this.log = a1;
        /*SL:144*/return this;
    }
    
    @Override
    public boolean find(final String a1, final InsnList a2, final Collection<AbstractInsnNode> a3) {
        /*SL:154*/this.log("{} is searching for an injection point in method with descriptor {}", this.className, a1);
        /*SL:156*/if (!this.find(a1, a2, a3, this.target, SearchType.STRICT) && this.target.desc != null && this.allowPermissive) {
            /*SL:157*/this.logger.warn("STRICT match for {} using \"{}\" in {} returned 0 results, attempting permissive search. To inhibit permissive search set mixin.env.allowPermissiveMatch=false", new Object[] { this.className, this.target, this.context });
            /*SL:159*/return this.find(a1, a2, a3, this.target, SearchType.PERMISSIVE);
        }
        /*SL:161*/return true;
    }
    
    protected boolean find(final String a4, final InsnList a5, final Collection<AbstractInsnNode> v1, final MemberInfo v2, final SearchType v3) {
        /*SL:165*/if (v2 == null) {
            /*SL:166*/return false;
        }
        final MemberInfo v4 = /*EL:169*/(v3 == SearchType.PERMISSIVE) ? v2.transform(null) : v2;
        int v5 = /*EL:171*/0;
        int v6 = /*EL:172*/0;
        /*SL:174*/for (final AbstractInsnNode a6 : a5) {
            /*SL:178*/if (this.matchesInsn(a6)) {
                final MemberInfo a7 = /*EL:179*/new MemberInfo(a6);
                /*SL:180*/this.log("{} is considering insn {}", this.className, a7);
                /*SL:182*/if (v4.matches(a7.owner, a7.name, a7.desc)) {
                    /*SL:183*/this.log("{} > found a matching insn, checking preconditions...", this.className);
                    /*SL:185*/if (this.matchesInsn(a7, v5)) {
                        /*SL:186*/this.log("{} > > > found a matching insn at ordinal {}", this.className, v5);
                        /*SL:188*/if (this.addInsn(a5, v1, a6)) {
                            /*SL:189*/++v6;
                        }
                        /*SL:192*/if (this.ordinal == v5) {
                            /*SL:193*/break;
                        }
                    }
                    /*SL:197*/++v5;
                }
            }
            /*SL:201*/this.inspectInsn(a4, a5, a6);
        }
        /*SL:204*/if (v3 == SearchType.PERMISSIVE && v6 > 1) {
            /*SL:205*/this.logger.warn("A permissive match for {} using \"{}\" in {} matched {} instructions, this may cause unexpected behaviour. To inhibit permissive search set mixin.env.allowPermissiveMatch=false", new Object[] { this.className, v2, this.context, v6 });
        }
        /*SL:209*/return v6 > 0;
    }
    
    protected boolean addInsn(final InsnList a1, final Collection<AbstractInsnNode> a2, final AbstractInsnNode a3) {
        /*SL:213*/a2.add(a3);
        /*SL:214*/return true;
    }
    
    protected boolean matchesInsn(final AbstractInsnNode a1) {
        /*SL:218*/return a1 instanceof MethodInsnNode;
    }
    
    protected void inspectInsn(final String a1, final InsnList a2, final AbstractInsnNode a3) {
    }
    
    protected boolean matchesInsn(final MemberInfo a1, final int a2) {
        /*SL:226*/this.log("{} > > comparing target ordinal {} with current ordinal {}", this.className, this.ordinal, a2);
        /*SL:227*/return this.ordinal == -1 || this.ordinal == a2;
    }
    
    protected void log(final String a1, final Object... a2) {
        /*SL:231*/if (this.log) {
            /*SL:232*/this.logger.info(a1, a2);
        }
    }
    
    public enum SearchType
    {
        STRICT, 
        PERMISSIVE;
    }
}

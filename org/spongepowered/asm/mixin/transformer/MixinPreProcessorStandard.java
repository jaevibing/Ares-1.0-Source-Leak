package org.spongepowered.asm.mixin.transformer;

import org.spongepowered.asm.lib.tree.FrameNode;
import java.util.ArrayList;
import java.util.List;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.Overwrite;
import org.apache.logging.log4j.LogManager;
import org.spongepowered.asm.lib.Type;
import com.google.common.base.Strings;
import org.spongepowered.asm.mixin.Dynamic;
import org.spongepowered.asm.lib.tree.FieldInsnNode;
import org.spongepowered.asm.lib.tree.MethodInsnNode;
import org.spongepowered.asm.lib.tree.AbstractInsnNode;
import org.spongepowered.asm.util.throwables.SyntheticBridgeException;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.util.Bytecode;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;
import org.spongepowered.asm.mixin.transformer.throwables.InvalidMixinException;
import org.spongepowered.asm.mixin.refmap.IMixinContext;
import org.spongepowered.asm.mixin.gen.throwables.InvalidAccessorException;
import org.spongepowered.asm.lib.tree.ClassNode;
import org.spongepowered.asm.lib.tree.AnnotationNode;
import org.spongepowered.asm.mixin.transformer.meta.MixinRenamed;
import java.lang.annotation.Annotation;
import org.spongepowered.asm.util.Annotations;
import org.spongepowered.asm.mixin.Shadow;
import java.util.Iterator;
import org.spongepowered.asm.lib.tree.FieldNode;
import org.spongepowered.asm.lib.tree.MethodNode;
import org.spongepowered.asm.util.perf.Profiler;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.apache.logging.log4j.Logger;

class MixinPreProcessorStandard
{
    private static final Logger logger;
    protected final MixinInfo mixin;
    protected final MixinInfo.MixinClassNode classNode;
    protected final MixinEnvironment env;
    protected final Profiler profiler;
    private final boolean verboseLogging;
    private final boolean strictUnique;
    private boolean prepared;
    private boolean attached;
    
    MixinPreProcessorStandard(final MixinInfo a1, final MixinInfo.MixinClassNode a2) {
        this.profiler = MixinEnvironment.getProfiler();
        this.mixin = a1;
        this.classNode = a2;
        this.env = a1.getParent().getEnvironment();
        this.verboseLogging = this.env.getOption(MixinEnvironment.Option.DEBUG_VERBOSE);
        this.strictUnique = this.env.getOption(MixinEnvironment.Option.DEBUG_UNIQUE);
    }
    
    final MixinPreProcessorStandard prepare() {
        /*SL:163*/if (this.prepared) {
            /*SL:164*/return this;
        }
        /*SL:167*/this.prepared = true;
        final Profiler.Section begin = /*EL:169*/this.profiler.begin("prepare");
        /*SL:171*/for (final MixinInfo.MixinMethodNode v0 : this.classNode.mixinMethods) {
            final ClassInfo.Method v = /*EL:172*/this.mixin.getClassInfo().findMethod(v0);
            /*SL:173*/this.prepareMethod(v0, v);
        }
        /*SL:176*/for (final FieldNode v2 : this.classNode.fields) {
            /*SL:177*/this.prepareField(v2);
        }
        /*SL:180*/begin.end();
        /*SL:181*/return this;
    }
    
    protected void prepareMethod(final MixinInfo.MixinMethodNode a1, final ClassInfo.Method a2) {
        /*SL:185*/this.prepareShadow(a1, a2);
        /*SL:186*/this.prepareSoftImplements(a1, a2);
    }
    
    protected void prepareShadow(final MixinInfo.MixinMethodNode v1, final ClassInfo.Method v2) {
        final AnnotationNode v3 = /*EL:190*/Annotations.getVisible(v1, Shadow.class);
        /*SL:191*/if (v3 == null) {
            /*SL:192*/return;
        }
        final String v4 = /*EL:195*/Annotations.<String>getValue(v3, "prefix", (Class<?>)Shadow.class);
        /*SL:196*/if (v1.name.startsWith(v4)) {
            /*SL:197*/Annotations.setVisible(v1, MixinRenamed.class, "originalName", v1.name);
            final String a1 = /*EL:198*/v1.name.substring(v4.length());
            /*SL:199*/v1.name = v2.renameTo(a1);
        }
    }
    
    protected void prepareSoftImplements(final MixinInfo.MixinMethodNode v1, final ClassInfo.Method v2) {
        /*SL:204*/for (final InterfaceInfo a1 : this.mixin.getSoftImplements()) {
            /*SL:205*/if (a1.renameMethod(v1)) {
                /*SL:206*/v2.renameTo(v1.name);
            }
        }
    }
    
    protected void prepareField(final FieldNode a1) {
    }
    
    final MixinPreProcessorStandard conform(final TargetClassContext a1) {
        /*SL:216*/return this.conform(a1.getClassInfo());
    }
    
    final MixinPreProcessorStandard conform(final ClassInfo v-2) {
        final Profiler.Section begin = /*EL:220*/this.profiler.begin("conform");
        /*SL:222*/for (final MixinInfo.MixinMethodNode v1 : this.classNode.mixinMethods) {
            /*SL:223*/if (v1.isInjector()) {
                final ClassInfo.Method a1 = /*EL:224*/this.mixin.getClassInfo().findMethod(v1, 10);
                /*SL:225*/this.conformInjector(v-2, v1, a1);
            }
        }
        /*SL:229*/begin.end();
        /*SL:230*/return this;
    }
    
    private void conformInjector(final ClassInfo a1, final MixinInfo.MixinMethodNode a2, final ClassInfo.Method a3) {
        final MethodMapper v1 = /*EL:234*/a1.getMethodMapper();
        /*SL:235*/v1.remapHandlerMethod(this.mixin, a2, a3);
    }
    
    MixinTargetContext createContextFor(final TargetClassContext a1) {
        final MixinTargetContext v1 = /*EL:239*/new MixinTargetContext(this.mixin, this.classNode, a1);
        /*SL:240*/this.conform(a1);
        /*SL:241*/this.attach(v1);
        /*SL:242*/return v1;
    }
    
    final MixinPreProcessorStandard attach(final MixinTargetContext a1) {
        /*SL:251*/if (this.attached) {
            /*SL:252*/throw new IllegalStateException("Preprocessor was already attached");
        }
        /*SL:255*/this.attached = true;
        final Profiler.Section v1 = /*EL:257*/this.profiler.begin("attach");
        Profiler.Section v2 = /*EL:260*/this.profiler.begin("methods");
        /*SL:261*/this.attachMethods(a1);
        /*SL:262*/v2 = v2.next("fields");
        /*SL:263*/this.attachFields(a1);
        /*SL:266*/v2 = v2.next("transform");
        /*SL:267*/this.transform(a1);
        /*SL:268*/v2.end();
        /*SL:270*/v1.end();
        /*SL:271*/return this;
    }
    
    protected void attachMethods(final MixinTargetContext v0) {
        final Iterator<MixinInfo.MixinMethodNode> v = /*EL:275*/this.classNode.mixinMethods.iterator();
        while (v.hasNext()) {
            final MixinInfo.MixinMethodNode a1 = /*EL:276*/v.next();
            /*SL:278*/if (!this.validateMethod(v0, a1)) {
                /*SL:279*/v.remove();
            }
            else/*SL:283*/ if (this.attachInjectorMethod(v0, a1)) {
                /*SL:284*/v0.addMixinMethod(a1);
            }
            else/*SL:288*/ if (this.attachAccessorMethod(v0, a1)) {
                /*SL:289*/v.remove();
            }
            else/*SL:293*/ if (this.attachShadowMethod(v0, a1)) {
                /*SL:294*/v0.addShadowMethod(a1);
                /*SL:295*/v.remove();
            }
            else/*SL:299*/ if (this.attachOverwriteMethod(v0, a1)) {
                /*SL:300*/v0.addMixinMethod(a1);
            }
            else/*SL:304*/ if (this.attachUniqueMethod(v0, a1)) {
                /*SL:305*/v.remove();
            }
            else {
                /*SL:309*/this.attachMethod(v0, a1);
                /*SL:310*/v0.addMixinMethod(a1);
            }
        }
    }
    
    protected boolean validateMethod(final MixinTargetContext a1, final MixinInfo.MixinMethodNode a2) {
        /*SL:315*/return true;
    }
    
    protected boolean attachInjectorMethod(final MixinTargetContext a1, final MixinInfo.MixinMethodNode a2) {
        /*SL:319*/return a2.isInjector();
    }
    
    protected boolean attachAccessorMethod(final MixinTargetContext a1, final MixinInfo.MixinMethodNode a2) {
        /*SL:323*/return this.attachAccessorMethod(a1, a2, SpecialMethod.ACCESSOR) || this.attachAccessorMethod(a1, a2, SpecialMethod.INVOKER);
    }
    
    protected boolean attachAccessorMethod(final MixinTargetContext a3, final MixinInfo.MixinMethodNode v1, final SpecialMethod v2) {
        final AnnotationNode v3 = /*EL:328*/v1.getVisibleAnnotation(v2.annotation);
        /*SL:329*/if (v3 == null) {
            /*SL:330*/return false;
        }
        final String v4 = /*EL:333*/v2 + " method " + v1.name;
        final ClassInfo.Method v5 = /*EL:334*/this.getSpecialMethod(v1, v2);
        /*SL:335*/if (MixinEnvironment.getCompatibilityLevel().isAtLeast(MixinEnvironment.CompatibilityLevel.JAVA_8) && v5.isStatic()) {
            /*SL:336*/if (this.mixin.getTargets().size() > 1) {
                /*SL:337*/throw new InvalidAccessorException(a3, v4 + " in multi-target mixin is invalid. Mixin must have exactly 1 target.");
            }
            final String a4 = /*EL:340*/a3.getUniqueName(v1, true);
            MixinPreProcessorStandard.logger.log(/*EL:341*/this.mixin.getLoggingLevel(), "Renaming @Unique method {}{} to {} in {}", new Object[] { v1.name, v1.desc, a4, this.mixin });
            /*SL:343*/v1.name = v5.renameTo(a4);
        }
        else {
            /*SL:346*/if (!v5.isAbstract()) {
                /*SL:347*/throw new InvalidAccessorException(a3, v4 + " is not abstract");
            }
            /*SL:350*/if (v5.isStatic()) {
                /*SL:351*/throw new InvalidAccessorException(a3, v4 + " cannot be static");
            }
        }
        /*SL:355*/a3.addAccessorMethod(v1, v2.annotation);
        /*SL:356*/return true;
    }
    
    protected boolean attachShadowMethod(final MixinTargetContext a1, final MixinInfo.MixinMethodNode a2) {
        /*SL:360*/return this.attachSpecialMethod(a1, a2, SpecialMethod.SHADOW);
    }
    
    protected boolean attachOverwriteMethod(final MixinTargetContext a1, final MixinInfo.MixinMethodNode a2) {
        /*SL:364*/return this.attachSpecialMethod(a1, a2, SpecialMethod.OVERWRITE);
    }
    
    protected boolean attachSpecialMethod(final MixinTargetContext a1, final MixinInfo.MixinMethodNode a2, final SpecialMethod a3) {
        final AnnotationNode v1 = /*EL:369*/a2.getVisibleAnnotation(a3.annotation);
        /*SL:370*/if (v1 == null) {
            /*SL:371*/return false;
        }
        /*SL:374*/if (a3.isOverwrite) {
            /*SL:375*/this.checkMixinNotUnique(a2, a3);
        }
        final ClassInfo.Method v2 = /*EL:378*/this.getSpecialMethod(a2, a3);
        MethodNode v3 = /*EL:379*/a1.findMethod(a2, v1);
        /*SL:380*/if (v3 == null) {
            /*SL:381*/if (a3.isOverwrite) {
                /*SL:382*/return false;
            }
            /*SL:384*/v3 = a1.findRemappedMethod(a2);
            /*SL:385*/if (v3 == null) {
                /*SL:386*/throw new InvalidMixinException(this.mixin, /*EL:387*/String.format("%s method %s in %s was not located in the target class %s. %s%s", a3, a2.name, this.mixin, a1.getTarget(), /*EL:388*/a1.getReferenceMapper().getStatus(), getDynamicInfo(a2)));
            }
            /*SL:391*/a2.name = v2.renameTo(v3.name);
        }
        /*SL:394*/if ("<init>".equals(v3.name)) {
            /*SL:395*/throw new InvalidMixinException(this.mixin, String.format("Nice try! %s in %s cannot alias a constructor", a2.name, this.mixin));
        }
        /*SL:398*/if (!Bytecode.compareFlags(a2, v3, 8)) {
            /*SL:399*/throw new InvalidMixinException(this.mixin, String.format("STATIC modifier of %s method %s in %s does not match the target", a3, a2.name, this.mixin));
        }
        /*SL:403*/this.conformVisibility(a1, a2, a3, v3);
        /*SL:405*/if (!v3.name.equals(a2.name)) {
            /*SL:406*/if (a3.isOverwrite && (v3.access & 0x2) == 0x0) {
                /*SL:407*/throw new InvalidMixinException(this.mixin, "Non-private method cannot be aliased. Found " + v3.name);
            }
            /*SL:410*/a2.name = v2.renameTo(v3.name);
        }
        /*SL:413*/return true;
    }
    
    private void conformVisibility(final MixinTargetContext a1, final MixinInfo.MixinMethodNode a2, final SpecialMethod a3, final MethodNode a4) {
        final Bytecode.Visibility v1 = /*EL:417*/Bytecode.getVisibility(a4);
        final Bytecode.Visibility v2 = /*EL:418*/Bytecode.getVisibility(a2);
        /*SL:419*/if (v2.ordinal() >= v1.ordinal()) {
            /*SL:420*/if (v1 == Bytecode.Visibility.PRIVATE && v2.ordinal() > Bytecode.Visibility.PRIVATE.ordinal()) {
                /*SL:421*/a1.getTarget().addUpgradedMethod(a4);
            }
            /*SL:423*/return;
        }
        final String v3 = /*EL:426*/String.format("%s %s method %s in %s cannot reduce visibiliy of %s target method", v2, a3, a2.name, this.mixin, v1);
        /*SL:429*/if (a3.isOverwrite && !this.mixin.getParent().conformOverwriteVisibility()) {
            /*SL:430*/throw new InvalidMixinException(this.mixin, v3);
        }
        /*SL:433*/if (v2 == Bytecode.Visibility.PRIVATE) {
            /*SL:434*/if (a3.isOverwrite) {
                MixinPreProcessorStandard.logger.warn(/*EL:435*/"Static binding violation: {}, visibility will be upgraded.", new Object[] { v3 });
            }
            /*SL:437*/a1.addUpgradedMethod(a2);
            /*SL:438*/Bytecode.setVisibility(a2, v1);
        }
    }
    
    protected ClassInfo.Method getSpecialMethod(final MixinInfo.MixinMethodNode a1, final SpecialMethod a2) {
        final ClassInfo.Method v1 = /*EL:443*/this.mixin.getClassInfo().findMethod(a1, 10);
        /*SL:444*/this.checkMethodNotUnique(v1, a2);
        /*SL:445*/return v1;
    }
    
    protected void checkMethodNotUnique(final ClassInfo.Method a1, final SpecialMethod a2) {
        /*SL:449*/if (a1.isUnique()) {
            /*SL:450*/throw new InvalidMixinException(this.mixin, String.format("%s method %s in %s cannot be @Unique", a2, a1.getName(), this.mixin));
        }
    }
    
    protected void checkMixinNotUnique(final MixinInfo.MixinMethodNode a1, final SpecialMethod a2) {
        /*SL:455*/if (this.mixin.isUnique()) {
            /*SL:456*/throw new InvalidMixinException(this.mixin, String.format("%s method %s found in a @Unique mixin %s", a2, a1.name, this.mixin));
        }
    }
    
    protected boolean attachUniqueMethod(final MixinTargetContext v2, final MixinInfo.MixinMethodNode v3) {
        final ClassInfo.Method v4 = /*EL:462*/this.mixin.getClassInfo().findMethod(v3, 10);
        /*SL:463*/if (v4 == null || (!v4.isUnique() && !this.mixin.isUnique() && !v4.isSynthetic())) {
            /*SL:464*/return false;
        }
        /*SL:467*/if (v4.isSynthetic()) {
            /*SL:468*/v2.transformDescriptor(v3);
            /*SL:469*/v4.remapTo(v3.desc);
        }
        final MethodNode v5 = /*EL:472*/v2.findMethod(v3, null);
        /*SL:473*/if (v5 == null) {
            /*SL:474*/return false;
        }
        final String v6 = /*EL:477*/v4.isSynthetic() ? "synthetic" : "@Unique";
        /*SL:479*/if (Bytecode.getVisibility(v3).ordinal() < Bytecode.Visibility.PUBLIC.ordinal()) {
            final String a1 = /*EL:480*/v2.getUniqueName(v3, false);
            MixinPreProcessorStandard.logger.log(/*EL:481*/this.mixin.getLoggingLevel(), "Renaming {} method {}{} to {} in {}", new Object[] { v6, v3.name, v3.desc, a1, this.mixin });
            /*SL:483*/v3.name = v4.renameTo(a1);
            /*SL:484*/return false;
        }
        /*SL:487*/if (this.strictUnique) {
            /*SL:488*/throw new InvalidMixinException(this.mixin, String.format("Method conflict, %s method %s in %s cannot overwrite %s%s in %s", v6, v3.name, this.mixin, v5.name, v5.desc, v2.getTarget()));
        }
        final AnnotationNode v7 = /*EL:492*/Annotations.getVisible(v3, Unique.class);
        /*SL:493*/if (v7 == null || !Annotations.<Boolean>getValue(v7, "silent", Boolean.FALSE)) {
            /*SL:494*/if (Bytecode.hasFlag(v3, 64)) {
                try {
                    /*SL:497*/Bytecode.compareBridgeMethods(v5, v3);
                    MixinPreProcessorStandard.logger.debug(/*EL:498*/"Discarding sythetic bridge method {} in {} because existing method in {} is compatible", new Object[] { v6, v3.name, this.mixin, v2.getTarget() });
                    /*SL:500*/return true;
                }
                catch (SyntheticBridgeException a2) {
                    /*SL:502*/if (this.verboseLogging || this.env.getOption(MixinEnvironment.Option.DEBUG_VERIFY)) {
                        /*SL:504*/a2.printAnalysis(v2, v5, v3);
                    }
                    /*SL:506*/throw new InvalidMixinException(this.mixin, a2.getMessage());
                }
            }
            MixinPreProcessorStandard.logger.warn(/*EL:510*/"Discarding {} public method {} in {} because it already exists in {}", new Object[] { v6, v3.name, this.mixin, v2.getTarget() });
            /*SL:512*/return true;
        }
        /*SL:515*/v2.addMixinMethod(v3);
        /*SL:516*/return true;
    }
    
    protected void attachMethod(final MixinTargetContext a1, final MixinInfo.MixinMethodNode a2) {
        final ClassInfo.Method v1 = /*EL:520*/this.mixin.getClassInfo().findMethod(a2);
        /*SL:521*/if (v1 == null) {
            /*SL:522*/return;
        }
        final ClassInfo.Method v2 = /*EL:525*/this.mixin.getClassInfo().findMethodInHierarchy(a2, ClassInfo.SearchType.SUPER_CLASSES_ONLY);
        /*SL:526*/if (v2 != null && v2.isRenamed()) {
            /*SL:527*/a2.name = v1.renameTo(v2.getName());
        }
        final MethodNode v3 = /*EL:530*/a1.findMethod(a2, null);
        /*SL:531*/if (v3 != null) {
            /*SL:532*/this.conformVisibility(a1, a2, SpecialMethod.MERGE, v3);
        }
    }
    
    protected void attachFields(final MixinTargetContext v-7) {
        final Iterator<FieldNode> iterator = /*EL:537*/this.classNode.fields.iterator();
        while (iterator.hasNext()) {
            final FieldNode fieldNode = /*EL:538*/iterator.next();
            final AnnotationNode visible = /*EL:539*/Annotations.getVisible(fieldNode, Shadow.class);
            final boolean b = /*EL:540*/visible != null;
            /*SL:542*/if (!this.validateField(v-7, fieldNode, visible)) {
                /*SL:543*/iterator.remove();
            }
            else {
                final ClassInfo.Field field = /*EL:547*/this.mixin.getClassInfo().findField(fieldNode);
                /*SL:548*/v-7.transformDescriptor(fieldNode);
                /*SL:549*/field.remapTo(fieldNode.desc);
                /*SL:551*/if (field.isUnique() && b) {
                    /*SL:552*/throw new InvalidMixinException(this.mixin, String.format("@Shadow field %s cannot be @Unique", fieldNode.name));
                }
                FieldNode fieldNode2 = /*EL:555*/v-7.findField(fieldNode, visible);
                /*SL:556*/if (fieldNode2 == null) {
                    /*SL:557*/if (visible == null) {
                        /*SL:558*/continue;
                    }
                    /*SL:560*/fieldNode2 = v-7.findRemappedField(fieldNode);
                    /*SL:561*/if (fieldNode2 == null) {
                        /*SL:563*/throw new InvalidMixinException(this.mixin, String.format("Shadow field %s was not located in the target class %s. %s%s", fieldNode.name, v-7.getTarget(), /*EL:564*/v-7.getReferenceMapper().getStatus(), getDynamicInfo(fieldNode)));
                    }
                    /*SL:567*/fieldNode.name = field.renameTo(fieldNode2.name);
                }
                /*SL:570*/if (!Bytecode.compareFlags(fieldNode, fieldNode2, 8)) {
                    /*SL:571*/throw new InvalidMixinException(this.mixin, String.format("STATIC modifier of @Shadow field %s in %s does not match the target", fieldNode.name, this.mixin));
                }
                /*SL:575*/if (field.isUnique()) {
                    /*SL:576*/if ((fieldNode.access & 0x6) != 0x0) {
                        final String a1 = /*EL:577*/v-7.getUniqueName(fieldNode);
                        MixinPreProcessorStandard.logger.log(/*EL:578*/this.mixin.getLoggingLevel(), "Renaming @Unique field {}{} to {} in {}", new Object[] { fieldNode.name, fieldNode.desc, a1, this.mixin });
                        /*SL:580*/fieldNode.name = field.renameTo(a1);
                    }
                    else {
                        /*SL:584*/if (this.strictUnique) {
                            /*SL:585*/throw new InvalidMixinException(this.mixin, String.format("Field conflict, @Unique field %s in %s cannot overwrite %s%s in %s", fieldNode.name, this.mixin, fieldNode2.name, fieldNode2.desc, v-7.getTarget()));
                        }
                        MixinPreProcessorStandard.logger.warn(/*EL:589*/"Discarding @Unique public field {} in {} because it already exists in {}. Note that declared FIELD INITIALISERS will NOT be removed!", new Object[] { fieldNode.name, this.mixin, v-7.getTarget() });
                        /*SL:592*/iterator.remove();
                    }
                }
                else {
                    /*SL:597*/if (!fieldNode2.desc.equals(fieldNode.desc)) {
                        /*SL:598*/throw new InvalidMixinException(this.mixin, String.format("The field %s in the target class has a conflicting signature", fieldNode.name));
                    }
                    /*SL:602*/if (!fieldNode2.name.equals(fieldNode.name)) {
                        /*SL:603*/if ((fieldNode2.access & 0x2) == 0x0 && (fieldNode2.access & 0x1000) == 0x0) {
                            /*SL:604*/throw new InvalidMixinException(this.mixin, "Non-private field cannot be aliased. Found " + fieldNode2.name);
                        }
                        /*SL:607*/fieldNode.name = field.renameTo(fieldNode2.name);
                    }
                    /*SL:611*/iterator.remove();
                    /*SL:613*/if (!b) {
                        continue;
                    }
                    final boolean v0 = /*EL:614*/field.isDecoratedFinal();
                    /*SL:615*/if (this.verboseLogging && Bytecode.hasFlag(fieldNode2, 16) != v0) {
                        final String v = /*EL:616*/v0 ? "@Shadow field {}::{} is decorated with @Final but target is not final" : "@Shadow target {}::{} is final but shadow is not decorated with @Final";
                        MixinPreProcessorStandard.logger.warn(/*EL:619*/v, new Object[] { this.mixin, fieldNode.name });
                    }
                    /*SL:622*/v-7.addShadowField(fieldNode, field);
                }
            }
        }
    }
    
    protected boolean validateField(final MixinTargetContext a1, final FieldNode a2, final AnnotationNode a3) {
        /*SL:631*/if (Bytecode.hasFlag(a2, 8) && !Bytecode.hasFlag(a2, 2) && !Bytecode.hasFlag(a2, 4096) && a3 == null) {
            /*SL:633*/throw new InvalidMixinException(a1, String.format("Mixin %s contains non-private static field %s:%s", a1, a2.name, a2.desc));
        }
        final String v1 = /*EL:638*/Annotations.<String>getValue(a3, "prefix", (Class<?>)Shadow.class);
        /*SL:639*/if (a2.name.startsWith(v1)) {
            /*SL:640*/throw new InvalidMixinException(a1, String.format("@Shadow field %s.%s has a shadow prefix. This is not allowed.", a1, a2.name));
        }
        /*SL:645*/if (!"super$".equals(a2.name)) {
            /*SL:658*/return true;
        }
        if (a2.access != 2) {
            throw new InvalidMixinException(this.mixin, String.format("Imaginary super field %s.%s must be private and non-final", a1, a2.name));
        }
        if (!a2.desc.equals("L" + this.mixin.getClassRef() + ";")) {
            throw new InvalidMixinException(this.mixin, String.format("Imaginary super field %s.%s must have the same type as the parent mixin (%s)", a1, a2.name, this.mixin.getClassName()));
        }
        return false;
    }
    
    protected void transform(final MixinTargetContext v-2) {
        /*SL:666*/for (final MethodNode v0 : this.classNode.methods) {
            /*SL:667*/for (final AbstractInsnNode a1 : v0.instructions) {
                /*SL:669*/if (a1 instanceof MethodInsnNode) {
                    /*SL:670*/this.transformMethod((MethodInsnNode)a1);
                }
                else {
                    /*SL:671*/if (!(a1 instanceof FieldInsnNode)) {
                        continue;
                    }
                    /*SL:672*/this.transformField((FieldInsnNode)a1);
                }
            }
        }
    }
    
    protected void transformMethod(final MethodInsnNode a1) {
        final Profiler.Section v1 = /*EL:679*/this.profiler.begin("meta");
        final ClassInfo v2 = /*EL:680*/ClassInfo.forName(a1.owner);
        /*SL:681*/if (v2 == null) {
            /*SL:682*/throw new RuntimeException(new ClassNotFoundException(a1.owner.replace('/', '.')));
        }
        final ClassInfo.Method v3 = /*EL:685*/v2.findMethodInHierarchy(a1, ClassInfo.SearchType.ALL_CLASSES, 2);
        /*SL:686*/v1.end();
        /*SL:688*/if (v3 != null && v3.isRenamed()) {
            /*SL:689*/a1.name = v3.getName();
        }
    }
    
    protected void transformField(final FieldInsnNode a1) {
        final Profiler.Section v1 = /*EL:694*/this.profiler.begin("meta");
        final ClassInfo v2 = /*EL:695*/ClassInfo.forName(a1.owner);
        /*SL:696*/if (v2 == null) {
            /*SL:697*/throw new RuntimeException(new ClassNotFoundException(a1.owner.replace('/', '.')));
        }
        final ClassInfo.Field v3 = /*EL:700*/v2.findField(a1, 2);
        /*SL:701*/v1.end();
        /*SL:703*/if (v3 != null && v3.isRenamed()) {
            /*SL:704*/a1.name = v3.getName();
        }
    }
    
    protected static String getDynamicInfo(final MethodNode a1) {
        /*SL:718*/return getDynamicInfo("Method", Annotations.getInvisible(a1, Dynamic.class));
    }
    
    protected static String getDynamicInfo(final FieldNode a1) {
        /*SL:731*/return getDynamicInfo("Field", Annotations.getInvisible(a1, Dynamic.class));
    }
    
    private static String getDynamicInfo(final String a1, final AnnotationNode a2) {
        String v1 = /*EL:735*/Strings.nullToEmpty(Annotations.<String>getValue(a2));
        final Type v2 = /*EL:736*/Annotations.<Type>getValue(a2, "mixin");
        /*SL:737*/if (v2 != null) {
            /*SL:738*/v1 = String.format("{%s} %s", v2.getClassName(), v1).trim();
        }
        /*SL:740*/return (v1.length() > 0) ? String.format(" %s is @Dynamic(%s)", a1, v1) : "";
    }
    
    static {
        logger = LogManager.getLogger("mixin");
    }
    
    enum SpecialMethod
    {
        MERGE(true), 
        OVERWRITE(true, (Class<? extends Annotation>)Overwrite.class), 
        SHADOW(false, (Class<? extends Annotation>)Shadow.class), 
        ACCESSOR(false, (Class<? extends Annotation>)Accessor.class), 
        INVOKER(false, (Class<? extends Annotation>)Invoker.class);
        
        final boolean isOverwrite;
        final Class<? extends Annotation> annotation;
        final String description;
        
        private SpecialMethod(final boolean a1, final Class<? extends Annotation> a2) {
            this.isOverwrite = a1;
            this.annotation = a2;
            this.description = "@" + Bytecode.getSimpleName(a2);
        }
        
        private SpecialMethod(final boolean a1) {
            this.isOverwrite = a1;
            this.annotation = null;
            this.description = "overwrite";
        }
        
        @Override
        public String toString() {
            /*SL:121*/return this.description;
        }
    }
}

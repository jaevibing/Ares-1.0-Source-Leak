package org.spongepowered.asm.mixin.transformer;

import org.apache.logging.log4j.LogManager;
import org.spongepowered.asm.lib.tree.AnnotationNode;
import java.io.OutputStream;
import org.spongepowered.asm.util.Bytecode;
import java.lang.annotation.Annotation;
import org.spongepowered.asm.util.Annotations;
import org.spongepowered.asm.mixin.Debug;
import java.util.Iterator;
import java.util.Deque;
import org.spongepowered.asm.lib.tree.FieldNode;
import java.util.List;
import java.util.HashSet;
import java.util.HashMap;
import org.spongepowered.asm.lib.tree.MethodNode;
import java.util.Set;
import org.spongepowered.asm.mixin.injection.struct.Target;
import java.util.Map;
import java.util.SortedSet;
import org.spongepowered.asm.util.ClassSignature;
import org.spongepowered.asm.mixin.struct.SourceMap;
import org.spongepowered.asm.lib.tree.ClassNode;
import org.spongepowered.asm.mixin.transformer.ext.Extensions;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.transformer.ext.ITargetClassContext;

class TargetClassContext extends ClassContext implements ITargetClassContext
{
    private static final Logger logger;
    private final MixinEnvironment env;
    private final Extensions extensions;
    private final String sessionId;
    private final String className;
    private final ClassNode classNode;
    private final ClassInfo classInfo;
    private final SourceMap sourceMap;
    private final ClassSignature signature;
    private final SortedSet<MixinInfo> mixins;
    private final Map<String, Target> targetMethods;
    private final Set<MethodNode> mixinMethods;
    private int nextUniqueMethodIndex;
    private int nextUniqueFieldIndex;
    private boolean applied;
    private boolean forceExport;
    
    TargetClassContext(final MixinEnvironment a1, final Extensions a2, final String a3, final String a4, final ClassNode a5, final SortedSet<MixinInfo> a6) {
        this.targetMethods = new HashMap<String, Target>();
        this.mixinMethods = new HashSet<MethodNode>();
        this.env = a1;
        this.extensions = a2;
        this.sessionId = a3;
        this.className = a4;
        this.classNode = a5;
        this.classInfo = ClassInfo.fromClassNode(a5);
        this.signature = this.classInfo.getSignature();
        this.mixins = a6;
        (this.sourceMap = new SourceMap(a5.sourceFile)).addFile(this.classNode);
    }
    
    @Override
    public String toString() {
        /*SL:151*/return this.className;
    }
    
    boolean isApplied() {
        /*SL:155*/return this.applied;
    }
    
    boolean isExportForced() {
        /*SL:159*/return this.forceExport;
    }
    
    Extensions getExtensions() {
        /*SL:166*/return this.extensions;
    }
    
    String getSessionId() {
        /*SL:173*/return this.sessionId;
    }
    
    @Override
    String getClassRef() {
        /*SL:181*/return this.classNode.name;
    }
    
    String getClassName() {
        /*SL:188*/return this.className;
    }
    
    @Override
    public ClassNode getClassNode() {
        /*SL:196*/return this.classNode;
    }
    
    List<MethodNode> getMethods() {
        /*SL:203*/return this.classNode.methods;
    }
    
    List<FieldNode> getFields() {
        /*SL:210*/return this.classNode.fields;
    }
    
    @Override
    public ClassInfo getClassInfo() {
        /*SL:218*/return this.classInfo;
    }
    
    SortedSet<MixinInfo> getMixins() {
        /*SL:225*/return this.mixins;
    }
    
    SourceMap getSourceMap() {
        /*SL:232*/return this.sourceMap;
    }
    
    void mergeSignature(final ClassSignature a1) {
        /*SL:241*/this.signature.merge(a1);
    }
    
    void addMixinMethod(final MethodNode a1) {
        /*SL:250*/this.mixinMethods.add(a1);
    }
    
    void methodMerged(final MethodNode a1) {
        /*SL:254*/if (!this.mixinMethods.remove(a1)) {
            TargetClassContext.logger.debug(/*EL:255*/"Unexpected: Merged unregistered method {}{} in {}", new Object[] { a1.name, a1.desc, this });
        }
    }
    
    MethodNode findMethod(final Deque<String> a1, final String a2) {
        /*SL:260*/return this.findAliasedMethod(a1, a2, true);
    }
    
    MethodNode findAliasedMethod(final Deque<String> a1, final String a2) {
        /*SL:264*/return this.findAliasedMethod(a1, a2, false);
    }
    
    private MethodNode findAliasedMethod(final Deque<String> v1, final String v2, final boolean v3) {
        final String v4 = /*EL:268*/v1.poll();
        /*SL:269*/if (v4 == null) {
            /*SL:270*/return null;
        }
        /*SL:273*/for (final MethodNode a1 : this.classNode.methods) {
            /*SL:274*/if (a1.name.equals(v4) && a1.desc.equals(v2)) {
                /*SL:275*/return a1;
            }
        }
        /*SL:279*/if (v3) {
            /*SL:280*/for (final MethodNode a2 : this.mixinMethods) {
                /*SL:281*/if (a2.name.equals(v4) && a2.desc.equals(v2)) {
                    /*SL:282*/return a2;
                }
            }
        }
        /*SL:287*/return this.findAliasedMethod(v1, v2);
    }
    
    FieldNode findAliasedField(final Deque<String> v1, final String v2) {
        final String v3 = /*EL:298*/v1.poll();
        /*SL:299*/if (v3 == null) {
            /*SL:300*/return null;
        }
        /*SL:303*/for (final FieldNode a1 : this.classNode.fields) {
            /*SL:304*/if (a1.name.equals(v3) && a1.desc.equals(v2)) {
                /*SL:305*/return a1;
            }
        }
        /*SL:309*/return this.findAliasedField(v1, v2);
    }
    
    Target getTargetMethod(final MethodNode a1) {
        /*SL:319*/if (!this.classNode.methods.contains(a1)) {
            /*SL:320*/throw new IllegalArgumentException("Invalid target method supplied to getTargetMethod()");
        }
        final String v1 = /*EL:323*/a1.name + a1.desc;
        Target v2 = /*EL:324*/this.targetMethods.get(v1);
        /*SL:325*/if (v2 == null) {
            /*SL:326*/v2 = new Target(this.classNode, a1);
            /*SL:327*/this.targetMethods.put(v1, v2);
        }
        /*SL:329*/return v2;
    }
    
    String getUniqueName(final MethodNode a1, final boolean a2) {
        final String v1 = /*EL:333*/Integer.toHexString(this.nextUniqueMethodIndex++);
        final String v2 = /*EL:334*/a2 ? "%2$s_$md$%1$s$%3$s" : "md%s$%s$%s";
        /*SL:335*/return String.format(v2, this.sessionId.substring(30), a1.name, v1);
    }
    
    String getUniqueName(final FieldNode a1) {
        final String v1 = /*EL:339*/Integer.toHexString(this.nextUniqueFieldIndex++);
        /*SL:340*/return String.format("fd%s$%s$%s", this.sessionId.substring(30), a1.name, v1);
    }
    
    void applyMixins() {
        /*SL:347*/if (this.applied) {
            /*SL:348*/throw new IllegalStateException("Mixins already applied to target class " + this.className);
        }
        /*SL:350*/this.applied = true;
        final MixinApplicatorStandard v1 = /*EL:352*/this.createApplicator();
        /*SL:353*/v1.apply(this.mixins);
        /*SL:354*/this.applySignature();
        /*SL:355*/this.upgradeMethods();
        /*SL:356*/this.checkMerges();
    }
    
    private MixinApplicatorStandard createApplicator() {
        /*SL:360*/if (this.classInfo.isInterface()) {
            /*SL:361*/return new MixinApplicatorInterface(this);
        }
        /*SL:363*/return new MixinApplicatorStandard(this);
    }
    
    private void applySignature() {
        /*SL:367*/this.getClassNode().signature = this.signature.toString();
    }
    
    private void checkMerges() {
        /*SL:371*/for (final MethodNode v1 : this.mixinMethods) {
            /*SL:372*/if (!v1.name.startsWith("<")) {
                TargetClassContext.logger.debug(/*EL:373*/"Unexpected: Registered method {}{} in {} was not merged", new Object[] { v1.name, v1.desc, this });
            }
        }
    }
    
    void processDebugTasks() {
        /*SL:382*/if (!this.env.getOption(MixinEnvironment.Option.DEBUG_VERBOSE)) {
            /*SL:383*/return;
        }
        final AnnotationNode visible = /*EL:386*/Annotations.getVisible(this.classNode, Debug.class);
        /*SL:387*/if (visible != null) {
            /*SL:388*/this.forceExport = Boolean.TRUE.equals(Annotations.<Object>getValue(visible, "export"));
            /*SL:389*/if (Boolean.TRUE.equals(Annotations.<Object>getValue(visible, "print"))) {
                /*SL:390*/Bytecode.textify(this.classNode, System.err);
            }
        }
        /*SL:394*/for (final MethodNode v0 : this.classNode.methods) {
            final AnnotationNode v = /*EL:395*/Annotations.getVisible(v0, Debug.class);
            /*SL:396*/if (v != null && Boolean.TRUE.equals(Annotations.<Object>getValue(v, "print"))) {
                /*SL:397*/Bytecode.textify(v0, System.err);
            }
        }
    }
    
    static {
        logger = LogManager.getLogger("mixin");
    }
}

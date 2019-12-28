package org.spongepowered.asm.mixin.transformer;

import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.lib.tree.AbstractInsnNode;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.gen.Accessor;
import java.lang.annotation.Annotation;
import org.spongepowered.asm.util.Annotations;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.lib.tree.FrameNode;
import org.apache.logging.log4j.LogManager;
import org.spongepowered.asm.lib.Type;
import org.apache.logging.log4j.Level;
import org.spongepowered.asm.service.MixinService;
import org.spongepowered.asm.lib.tree.FieldInsnNode;
import org.spongepowered.asm.lib.tree.MethodInsnNode;
import com.google.common.collect.ImmutableList;
import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.lib.tree.FieldNode;
import org.spongepowered.asm.lib.tree.MethodNode;
import java.util.Collection;
import org.spongepowered.asm.lib.tree.ClassNode;
import java.util.Collections;
import com.google.common.collect.ImmutableSet;
import java.util.HashMap;
import java.util.HashSet;
import org.spongepowered.asm.util.ClassSignature;
import java.util.Set;
import java.util.Map;
import org.spongepowered.asm.util.perf.Profiler;
import org.apache.logging.log4j.Logger;

public final class ClassInfo
{
    public static final int INCLUDE_PRIVATE = 2;
    public static final int INCLUDE_STATIC = 8;
    public static final int INCLUDE_ALL = 10;
    private static final Logger logger;
    private static final Profiler profiler;
    private static final String JAVA_LANG_OBJECT = "java/lang/Object";
    private static final Map<String, ClassInfo> cache;
    private static final ClassInfo OBJECT;
    private final String name;
    private final String superName;
    private final String outerName;
    private final boolean isProbablyStatic;
    private final Set<String> interfaces;
    private final Set<Method> methods;
    private final Set<Field> fields;
    private final Set<MixinInfo> mixins;
    private final Map<ClassInfo, ClassInfo> correspondingTypes;
    private final MixinInfo mixin;
    private final MethodMapper methodMapper;
    private final boolean isMixin;
    private final boolean isInterface;
    private final int access;
    private ClassInfo superClass;
    private ClassInfo outerClass;
    private ClassSignature signature;
    
    private ClassInfo() {
        this.mixins = new HashSet<MixinInfo>();
        this.correspondingTypes = new HashMap<ClassInfo, ClassInfo>();
        this.name = "java/lang/Object";
        this.superName = null;
        this.outerName = null;
        this.isProbablyStatic = true;
        this.methods = ImmutableSet.<Method>of(new Method("getClass", "()Ljava/lang/Class;"), new Method("hashCode", "()I"), new Method("equals", "(Ljava/lang/Object;)Z"), new Method("clone", "()Ljava/lang/Object;"), new Method("toString", "()Ljava/lang/String;"), new Method("notify", "()V"), new Method("notifyAll", "()V"), new Method("wait", "(J)V"), new Method("wait", "(JI)V"), new Method("wait", "()V"), new Method("finalize", "()V"));
        this.fields = Collections.<Field>emptySet();
        this.isInterface = false;
        this.interfaces = Collections.<String>emptySet();
        this.access = 1;
        this.isMixin = false;
        this.mixin = null;
        this.methodMapper = null;
    }
    
    private ClassInfo(final ClassNode v-4) {
        this.mixins = new HashSet<MixinInfo>();
        this.correspondingTypes = new HashMap<ClassInfo, ClassInfo>();
        final Profiler.Section begin = ClassInfo.profiler.begin(1, "class.meta");
        try {
            this.name = v-4.name;
            this.superName = ((v-4.superName != null) ? v-4.superName : "java/lang/Object");
            this.methods = new HashSet<Method>();
            this.fields = new HashSet<Field>();
            this.isInterface = ((v-4.access & 0x200) != 0x0);
            this.interfaces = new HashSet<String>();
            this.access = v-4.access;
            this.isMixin = (v-4 instanceof MixinInfo.MixinClassNode);
            this.mixin = (this.isMixin ? ((MixinInfo.MixinClassNode)v-4).getMixin() : null);
            this.interfaces.addAll(v-4.interfaces);
            for (final MethodNode a1 : v-4.methods) {
                this.addMethod(a1, this.isMixin);
            }
            boolean isProbablyStatic = true;
            String outerName = v-4.outerClass;
            for (final FieldNode v1 : v-4.fields) {
                if ((v1.access & 0x1000) != 0x0 && v1.name.startsWith("this$")) {
                    isProbablyStatic = false;
                    if (outerName == null) {
                        outerName = v1.desc;
                        if (outerName != null && outerName.startsWith("L")) {
                            outerName = outerName.substring(1, outerName.length() - 1);
                        }
                    }
                }
                this.fields.add(new Field(v1, this.isMixin));
            }
            this.isProbablyStatic = isProbablyStatic;
            this.outerName = outerName;
            this.methodMapper = new MethodMapper(MixinEnvironment.getCurrentEnvironment(), this);
            this.signature = ClassSignature.ofLazy(v-4);
        }
        finally {
            begin.end();
        }
    }
    
    void addInterface(final String a1) {
        /*SL:766*/this.interfaces.add(a1);
        /*SL:767*/this.getSignature().addInterface(a1);
    }
    
    void addMethod(final MethodNode a1) {
        /*SL:771*/this.addMethod(a1, true);
    }
    
    private void addMethod(final MethodNode a1, final boolean a2) {
        /*SL:775*/if (!a1.name.startsWith("<")) {
            /*SL:776*/this.methods.add(new Method(a1, a2));
        }
    }
    
    void addMixin(final MixinInfo a1) {
        /*SL:784*/if (this.isMixin) {
            /*SL:785*/throw new IllegalArgumentException("Cannot add target " + this.name + " for " + a1.getClassName() + " because the target is a mixin");
        }
        /*SL:787*/this.mixins.add(a1);
    }
    
    public Set<MixinInfo> getMixins() {
        /*SL:794*/return Collections.<MixinInfo>unmodifiableSet((Set<? extends MixinInfo>)this.mixins);
    }
    
    public boolean isMixin() {
        /*SL:801*/return this.isMixin;
    }
    
    public boolean isPublic() {
        /*SL:808*/return (this.access & 0x1) != 0x0;
    }
    
    public boolean isAbstract() {
        /*SL:815*/return (this.access & 0x400) != 0x0;
    }
    
    public boolean isSynthetic() {
        /*SL:822*/return (this.access & 0x1000) != 0x0;
    }
    
    public boolean isProbablyStatic() {
        /*SL:829*/return this.isProbablyStatic;
    }
    
    public boolean isInner() {
        /*SL:836*/return this.outerName != null;
    }
    
    public boolean isInterface() {
        /*SL:843*/return this.isInterface;
    }
    
    public Set<String> getInterfaces() {
        /*SL:850*/return Collections.<String>unmodifiableSet((Set<? extends String>)this.interfaces);
    }
    
    @Override
    public String toString() {
        /*SL:855*/return this.name;
    }
    
    public MethodMapper getMethodMapper() {
        /*SL:859*/return this.methodMapper;
    }
    
    public int getAccess() {
        /*SL:863*/return this.access;
    }
    
    public String getName() {
        /*SL:870*/return this.name;
    }
    
    public String getClassName() {
        /*SL:877*/return this.name.replace('/', '.');
    }
    
    public String getSuperName() {
        /*SL:884*/return this.superName;
    }
    
    public ClassInfo getSuperClass() {
        /*SL:892*/if (this.superClass == null && this.superName != null) {
            /*SL:893*/this.superClass = forName(this.superName);
        }
        /*SL:896*/return this.superClass;
    }
    
    public String getOuterName() {
        /*SL:903*/return this.outerName;
    }
    
    public ClassInfo getOuterClass() {
        /*SL:911*/if (this.outerClass == null && this.outerName != null) {
            /*SL:912*/this.outerClass = forName(this.outerName);
        }
        /*SL:915*/return this.outerClass;
    }
    
    public ClassSignature getSignature() {
        /*SL:924*/return this.signature.wake();
    }
    
    List<ClassInfo> getTargets() {
        /*SL:931*/if (this.mixin != null) {
            final List<ClassInfo> v1 = /*EL:932*/new ArrayList<ClassInfo>();
            /*SL:933*/v1.add(this);
            /*SL:934*/v1.addAll(this.mixin.getTargets());
            /*SL:935*/return v1;
        }
        /*SL:938*/return ImmutableList.<ClassInfo>of(this);
    }
    
    public Set<Method> getMethods() {
        /*SL:947*/return Collections.<Method>unmodifiableSet((Set<? extends Method>)this.methods);
    }
    
    public Set<Method> getInterfaceMethods(final boolean v2) {
        final Set<Method> v3 = /*EL:961*/new HashSet<Method>();
        ClassInfo v4 = /*EL:963*/this.addMethodsRecursive(v3, v2);
        /*SL:964*/if (!this.isInterface) {
            /*SL:965*/while (v4 != null && v4 != ClassInfo.OBJECT) {
                /*SL:966*/v4 = v4.addMethodsRecursive(v3, v2);
            }
        }
        final Iterator<Method> a1 = /*EL:971*/v3.iterator();
        while (a1.hasNext()) {
            /*SL:972*/if (!a1.next().isAbstract()) {
                /*SL:973*/a1.remove();
            }
        }
        /*SL:977*/return Collections.<Method>unmodifiableSet((Set<? extends Method>)v3);
    }
    
    private ClassInfo addMethodsRecursive(final Set<Method> v-2, final boolean v-1) {
        /*SL:990*/if (this.isInterface) {
            /*SL:991*/for (final Method a1 : this.methods) {
                /*SL:993*/if (!a1.isAbstract()) {
                    /*SL:995*/v-2.remove(a1);
                }
                /*SL:997*/v-2.add(a1);
            }
        }
        else/*SL:999*/ if (!this.isMixin && v-1) {
            /*SL:1000*/for (final MixinInfo a2 : this.mixins) {
                /*SL:1001*/a2.getClassInfo().addMethodsRecursive(v-2, v-1);
            }
        }
        /*SL:1005*/for (final String v1 : this.interfaces) {
            forName(/*EL:1006*/v1).addMethodsRecursive(v-2, v-1);
        }
        /*SL:1009*/return this.getSuperClass();
    }
    
    public boolean hasSuperClass(final String a1) {
        /*SL:1020*/return this.hasSuperClass(a1, Traversal.NONE);
    }
    
    public boolean hasSuperClass(final String a1, final Traversal a2) {
        /*SL:1032*/return "java/lang/Object".equals(a1) || /*EL:1036*/this.findSuperClass(a1, a2) != null;
    }
    
    public boolean hasSuperClass(final ClassInfo a1) {
        /*SL:1047*/return this.hasSuperClass(a1, Traversal.NONE, false);
    }
    
    public boolean hasSuperClass(final ClassInfo a1, final Traversal a2) {
        /*SL:1059*/return this.hasSuperClass(a1, a2, false);
    }
    
    public boolean hasSuperClass(final ClassInfo a1, final Traversal a2, final boolean a3) {
        /*SL:1072*/return ClassInfo.OBJECT == a1 || /*EL:1076*/this.findSuperClass(a1.name, a2, a3) != null;
    }
    
    public ClassInfo findSuperClass(final String a1) {
        /*SL:1087*/return this.findSuperClass(a1, Traversal.NONE);
    }
    
    public ClassInfo findSuperClass(final String a1, final Traversal a2) {
        /*SL:1099*/return this.findSuperClass(a1, a2, false, new HashSet<String>());
    }
    
    public ClassInfo findSuperClass(final String a1, final Traversal a2, final boolean a3) {
        /*SL:1112*/if (ClassInfo.OBJECT.name.equals(a1)) {
            /*SL:1113*/return null;
        }
        /*SL:1116*/return this.findSuperClass(a1, a2, a3, new HashSet<String>());
    }
    
    private ClassInfo findSuperClass(final String v-7, final Traversal v-6, final boolean v-5, final Set<String> v-4) {
        final ClassInfo superClass = /*EL:1120*/this.getSuperClass();
        /*SL:1121*/if (superClass != null) {
            /*SL:1122*/for (ClassInfo a2 : superClass.getTargets()) {
                /*SL:1123*/if (v-7.equals(a2.getName())) {
                    /*SL:1124*/return superClass;
                }
                /*SL:1127*/a2 = a2.findSuperClass(v-7, v-6.next(), v-5, v-4);
                /*SL:1128*/if (a2 != null) {
                    /*SL:1129*/return a2;
                }
            }
        }
        /*SL:1134*/if (v-5) {
            final ClassInfo a3 = /*EL:1135*/this.findInterface(v-7);
            /*SL:1136*/if (a3 != null) {
                /*SL:1137*/return a3;
            }
        }
        /*SL:1141*/if (v-6.canTraverse()) {
            /*SL:1142*/for (final MixinInfo mixinInfo : this.mixins) {
                final String a4 = /*EL:1143*/mixinInfo.getClassName();
                /*SL:1144*/if (v-4.contains(a4)) {
                    /*SL:1145*/continue;
                }
                /*SL:1147*/v-4.add(a4);
                final ClassInfo v1 = /*EL:1148*/mixinInfo.getClassInfo();
                /*SL:1149*/if (v-7.equals(v1.getName())) {
                    /*SL:1150*/return v1;
                }
                final ClassInfo v2 = /*EL:1152*/v1.findSuperClass(v-7, Traversal.ALL, v-5, v-4);
                /*SL:1153*/if (v2 != null) {
                    /*SL:1154*/return v2;
                }
            }
        }
        /*SL:1159*/return null;
    }
    
    private ClassInfo findInterface(final String v-3) {
        /*SL:1163*/for (final String v-4 : this.getInterfaces()) {
            final ClassInfo a1 = forName(/*EL:1164*/v-4);
            /*SL:1165*/if (v-3.equals(v-4)) {
                /*SL:1166*/return a1;
            }
            final ClassInfo v1 = /*EL:1168*/a1.findInterface(v-3);
            /*SL:1169*/if (v1 != null) {
                /*SL:1170*/return v1;
            }
        }
        /*SL:1173*/return null;
    }
    
    ClassInfo findCorrespondingType(final ClassInfo a1) {
        /*SL:1187*/if (a1 == null || !a1.isMixin || this.isMixin) {
            /*SL:1188*/return null;
        }
        ClassInfo v1 = /*EL:1191*/this.correspondingTypes.get(a1);
        /*SL:1192*/if (v1 == null) {
            /*SL:1193*/v1 = this.findSuperTypeForMixin(a1);
            /*SL:1194*/this.correspondingTypes.put(a1, v1);
        }
        /*SL:1196*/return v1;
    }
    
    private ClassInfo findSuperTypeForMixin(final ClassInfo v2) {
        /*SL:1206*/for (ClassInfo v3 = this; v3 != null && v3 != ClassInfo.OBJECT; /*SL:1213*/v3 = v3.getSuperClass()) {
            for (final MixinInfo a1 : v3.mixins) {
                if (a1.getClassInfo().equals(v2)) {
                    return v3;
                }
            }
        }
        /*SL:1216*/return null;
    }
    
    public boolean hasMixinInHierarchy() {
        /*SL:1227*/if (!this.isMixin) {
            /*SL:1228*/return false;
        }
        /*SL:1233*/for (ClassInfo v1 = this.getSuperClass(); v1 != null && v1 != ClassInfo.OBJECT; /*SL:1237*/v1 = v1.getSuperClass()) {
            if (v1.isMixin) {
                return true;
            }
        }
        /*SL:1240*/return false;
    }
    
    public boolean hasMixinTargetInHierarchy() {
        /*SL:1252*/if (this.isMixin) {
            /*SL:1253*/return false;
        }
        /*SL:1258*/for (ClassInfo v1 = this.getSuperClass(); v1 != null && v1 != ClassInfo.OBJECT; /*SL:1262*/v1 = v1.getSuperClass()) {
            if (v1.mixins.size() > 0) {
                return true;
            }
        }
        /*SL:1265*/return false;
    }
    
    public Method findMethodInHierarchy(final MethodNode a1, final SearchType a2) {
        /*SL:1276*/return this.findMethodInHierarchy(a1.name, a1.desc, a2, Traversal.NONE);
    }
    
    public Method findMethodInHierarchy(final MethodNode a1, final SearchType a2, final int a3) {
        /*SL:1288*/return this.findMethodInHierarchy(a1.name, a1.desc, a2, Traversal.NONE, a3);
    }
    
    public Method findMethodInHierarchy(final MethodInsnNode a1, final SearchType a2) {
        /*SL:1299*/return this.findMethodInHierarchy(a1.name, a1.desc, a2, Traversal.NONE);
    }
    
    public Method findMethodInHierarchy(final MethodInsnNode a1, final SearchType a2, final int a3) {
        /*SL:1311*/return this.findMethodInHierarchy(a1.name, a1.desc, a2, Traversal.NONE, a3);
    }
    
    public Method findMethodInHierarchy(final String a1, final String a2, final SearchType a3) {
        /*SL:1323*/return this.findMethodInHierarchy(a1, a2, a3, Traversal.NONE);
    }
    
    public Method findMethodInHierarchy(final String a1, final String a2, final SearchType a3, final Traversal a4) {
        /*SL:1336*/return this.findMethodInHierarchy(a1, a2, a3, a4, 0);
    }
    
    public Method findMethodInHierarchy(final String a1, final String a2, final SearchType a3, final Traversal a4, final int a5) {
        /*SL:1350*/return this.<Method>findInHierarchy(a1, a2, a3, a4, a5, Member.Type.METHOD);
    }
    
    public Field findFieldInHierarchy(final FieldNode a1, final SearchType a2) {
        /*SL:1361*/return this.findFieldInHierarchy(a1.name, a1.desc, a2, Traversal.NONE);
    }
    
    public Field findFieldInHierarchy(final FieldNode a1, final SearchType a2, final int a3) {
        /*SL:1373*/return this.findFieldInHierarchy(a1.name, a1.desc, a2, Traversal.NONE, a3);
    }
    
    public Field findFieldInHierarchy(final FieldInsnNode a1, final SearchType a2) {
        /*SL:1384*/return this.findFieldInHierarchy(a1.name, a1.desc, a2, Traversal.NONE);
    }
    
    public Field findFieldInHierarchy(final FieldInsnNode a1, final SearchType a2, final int a3) {
        /*SL:1396*/return this.findFieldInHierarchy(a1.name, a1.desc, a2, Traversal.NONE, a3);
    }
    
    public Field findFieldInHierarchy(final String a1, final String a2, final SearchType a3) {
        /*SL:1408*/return this.findFieldInHierarchy(a1, a2, a3, Traversal.NONE);
    }
    
    public Field findFieldInHierarchy(final String a1, final String a2, final SearchType a3, final Traversal a4) {
        /*SL:1421*/return this.findFieldInHierarchy(a1, a2, a3, a4, 0);
    }
    
    public Field findFieldInHierarchy(final String a1, final String a2, final SearchType a3, final Traversal a4, final int a5) {
        /*SL:1435*/return this.<Field>findInHierarchy(a1, a2, a3, a4, a5, Member.Type.FIELD);
    }
    
    private <M extends Member> M findInHierarchy(final String v-9, final String v-8, final SearchType v-7, final Traversal v-6, final int v-5, final Member.Type v-4) {
        /*SL:1452*/if (v-7 == SearchType.ALL_CLASSES) {
            M a3 = /*EL:1453*/this.<M>findMember(v-9, v-8, v-5, v-4);
            /*SL:1454*/if (a3 != null) {
                /*SL:1455*/return a3;
            }
            /*SL:1458*/if (v-6.canTraverse()) {
                /*SL:1459*/for (final MixinInfo a2 : this.mixins) {
                    /*SL:1460*/a3 = a2.getClassInfo().<M>findMember(v-9, v-8, v-5, v-4);
                    /*SL:1461*/if (a3 != null) {
                        /*SL:1462*/return this.<M>cloneMember(a3);
                    }
                }
            }
        }
        final ClassInfo superClass = /*EL:1468*/this.getSuperClass();
        /*SL:1469*/if (superClass != null) {
            /*SL:1470*/for (final ClassInfo a4 : superClass.getTargets()) {
                final M a3 = /*EL:1471*/(M)a4.<Member>findInHierarchy(v-9, v-8, SearchType.ALL_CLASSES, v-6.next(), v-5 & 0xFFFFFFFD, v-4);
                /*SL:1473*/if (a3 != null) {
                    /*SL:1474*/return a3;
                }
            }
        }
        /*SL:1479*/if (v-4 == Member.Type.METHOD && (this.isInterface || MixinEnvironment.getCompatibilityLevel().supportsMethodsInInterfaces())) {
            /*SL:1480*/for (final String v-10 : this.interfaces) {
                final ClassInfo a5 = forName(/*EL:1481*/v-10);
                /*SL:1482*/if (a5 == null) {
                    ClassInfo.logger.debug(/*EL:1483*/"Failed to resolve declared interface {} on {}", new Object[] { v-10, this.name });
                }
                else {
                    final M v1 = /*EL:1487*/(M)a5.<Member>findInHierarchy(v-9, v-8, SearchType.ALL_CLASSES, v-6.next(), v-5 & 0xFFFFFFFD, v-4);
                    /*SL:1488*/if (v1 != null) {
                        /*SL:1489*/return (M)(this.isInterface ? v1 : new InterfaceMethod(v1));
                    }
                    /*SL:1491*/continue;
                }
            }
        }
        /*SL:1494*/return null;
    }
    
    private <M extends Member> M cloneMember(final M a1) {
        /*SL:1508*/if (a1 instanceof Method) {
            /*SL:1509*/return (M)new Method(a1);
        }
        /*SL:1512*/return (M)new Field(a1);
    }
    
    public Method findMethod(final MethodNode a1) {
        /*SL:1522*/return this.findMethod(a1.name, a1.desc, a1.access);
    }
    
    public Method findMethod(final MethodNode a1, final int a2) {
        /*SL:1533*/return this.findMethod(a1.name, a1.desc, a2);
    }
    
    public Method findMethod(final MethodInsnNode a1) {
        /*SL:1543*/return this.findMethod(a1.name, a1.desc, 0);
    }
    
    public Method findMethod(final MethodInsnNode a1, final int a2) {
        /*SL:1554*/return this.findMethod(a1.name, a1.desc, a2);
    }
    
    public Method findMethod(final String a1, final String a2, final int a3) {
        /*SL:1566*/return this.<Method>findMember(a1, a2, a3, Member.Type.METHOD);
    }
    
    public Field findField(final FieldNode a1) {
        /*SL:1576*/return this.findField(a1.name, a1.desc, a1.access);
    }
    
    public Field findField(final FieldInsnNode a1, final int a2) {
        /*SL:1587*/return this.findField(a1.name, a1.desc, a2);
    }
    
    public Field findField(final String a1, final String a2, final int a3) {
        /*SL:1599*/return this.<Field>findMember(a1, a2, a3, Member.Type.FIELD);
    }
    
    private <M extends Member> M findMember(final String a3, final String a4, final int v1, final Member.Type v2) {
        final Set<M> v3 = /*EL:1613*/(Set<M>)((v2 == Member.Type.METHOD) ? this.methods : this.fields);
        /*SL:1615*/for (final M a5 : v3) {
            /*SL:1616*/if (a5.equals(a3, a4) && a5.matchesFlags(v1)) {
                /*SL:1617*/return a5;
            }
        }
        /*SL:1621*/return null;
    }
    
    @Override
    public boolean equals(final Object a1) {
        /*SL:1629*/return a1 instanceof ClassInfo && /*EL:1632*/((ClassInfo)a1).name.equals(this.name);
    }
    
    @Override
    public int hashCode() {
        /*SL:1640*/return this.name.hashCode();
    }
    
    static ClassInfo fromClassNode(final ClassNode a1) {
        ClassInfo v1 = ClassInfo.cache.get(/*EL:1653*/a1.name);
        /*SL:1654*/if (v1 == null) {
            /*SL:1655*/v1 = new ClassInfo(a1);
            ClassInfo.cache.put(/*EL:1656*/a1.name, v1);
        }
        /*SL:1659*/return v1;
    }
    
    public static ClassInfo forName(String v-1) {
        /*SL:1671*/v-1 = v-1.replace('.', '/');
        ClassInfo v0 = ClassInfo.cache.get(/*EL:1673*/v-1);
        /*SL:1674*/if (v0 == null) {
            try {
                final ClassNode a1 = /*EL:1676*/MixinService.getService().getBytecodeProvider().getClassNode(v-1);
                /*SL:1677*/v0 = new ClassInfo(a1);
            }
            catch (Exception v) {
                ClassInfo.logger.catching(Level.TRACE, /*EL:1679*/(Throwable)v);
                ClassInfo.logger.warn(/*EL:1680*/"Error loading class: {} ({}: {})", new Object[] { v-1, v.getClass().getName(), v.getMessage() });
            }
            ClassInfo.cache.put(/*EL:1685*/v-1, v0);
            ClassInfo.logger.trace(/*EL:1686*/"Added class metadata for {} to metadata cache", new Object[] { v-1 });
        }
        /*SL:1689*/return v0;
    }
    
    public static ClassInfo forType(final Type a1) {
        /*SL:1701*/if (a1.getSort() == 9) {
            /*SL:1702*/return forType(a1.getElementType());
        }
        /*SL:1703*/if (a1.getSort() < 9) {
            /*SL:1704*/return null;
        }
        /*SL:1706*/return forName(a1.getClassName().replace('.', '/'));
    }
    
    public static ClassInfo getCommonSuperClass(final String a1, final String a2) {
        /*SL:1718*/if (a1 == null || a2 == null) {
            /*SL:1719*/return ClassInfo.OBJECT;
        }
        /*SL:1721*/return getCommonSuperClass(forName(a1), forName(a2));
    }
    
    public static ClassInfo getCommonSuperClass(final Type a1, final Type a2) {
        /*SL:1734*/if (a1 == null || a2 == null || a1.getSort() != 10 || a2.getSort() != 10) {
            /*SL:1735*/return ClassInfo.OBJECT;
        }
        /*SL:1737*/return getCommonSuperClass(forType(a1), forType(a2));
    }
    
    private static ClassInfo getCommonSuperClass(final ClassInfo a1, final ClassInfo a2) {
        /*SL:1749*/return getCommonSuperClass(a1, a2, false);
    }
    
    public static ClassInfo getCommonSuperClassOrInterface(final String a1, final String a2) {
        /*SL:1761*/if (a1 == null || a2 == null) {
            /*SL:1762*/return ClassInfo.OBJECT;
        }
        /*SL:1764*/return getCommonSuperClassOrInterface(forName(a1), forName(a2));
    }
    
    public static ClassInfo getCommonSuperClassOrInterface(final Type a1, final Type a2) {
        /*SL:1777*/if (a1 == null || a2 == null || a1.getSort() != 10 || a2.getSort() != 10) {
            /*SL:1778*/return ClassInfo.OBJECT;
        }
        /*SL:1780*/return getCommonSuperClassOrInterface(forType(a1), forType(a2));
    }
    
    public static ClassInfo getCommonSuperClassOrInterface(final ClassInfo a1, final ClassInfo a2) {
        /*SL:1792*/return getCommonSuperClass(a1, a2, true);
    }
    
    private static ClassInfo getCommonSuperClass(ClassInfo a1, final ClassInfo a2, final boolean a3) {
        /*SL:1796*/if (a1.hasSuperClass(a2, Traversal.NONE, a3)) {
            /*SL:1797*/return a2;
        }
        /*SL:1798*/if (a2.hasSuperClass(a1, Traversal.NONE, a3)) {
            /*SL:1799*/return a1;
        }
        /*SL:1800*/if (a1.isInterface() || a2.isInterface()) {
            /*SL:1801*/return ClassInfo.OBJECT;
        }
        /*SL:1809*/do {
            a1 = a1.getSuperClass();
            if (a1 == null) {
                return ClassInfo.OBJECT;
            }
        } while (!a2.hasSuperClass(a1, Traversal.NONE, a3));
        /*SL:1811*/return a1;
    }
    
    static {
        logger = LogManager.getLogger("mixin");
        profiler = MixinEnvironment.getProfiler();
        cache = new HashMap<String, ClassInfo>();
        OBJECT = new ClassInfo();
        ClassInfo.cache.put("java/lang/Object", ClassInfo.OBJECT);
    }
    
    public enum SearchType
    {
        ALL_CLASSES, 
        SUPER_CLASSES_ONLY;
    }
    
    public enum Traversal
    {
        NONE((Traversal)null, false, SearchType.SUPER_CLASSES_ONLY), 
        ALL((Traversal)null, true, SearchType.ALL_CLASSES), 
        IMMEDIATE(Traversal.NONE, true, SearchType.SUPER_CLASSES_ONLY), 
        SUPER(Traversal.ALL, false, SearchType.SUPER_CLASSES_ONLY);
        
        private final Traversal next;
        private final boolean traverse;
        private final SearchType searchType;
        
        private Traversal(final Traversal a1, final boolean a2, final SearchType a3) {
            this.next = ((a1 != null) ? a1 : this);
            this.traverse = a2;
            this.searchType = a3;
        }
        
        public Traversal next() {
            /*SL:148*/return this.next;
        }
        
        public boolean canTraverse() {
            /*SL:155*/return this.traverse;
        }
        
        public SearchType getSearchType() {
            /*SL:159*/return this.searchType;
        }
    }
    
    public static class FrameData
    {
        private static final String[] FRAMETYPES;
        public final int index;
        public final int type;
        public final int locals;
        
        FrameData(final int a1, final int a2, final int a3) {
            this.index = a1;
            this.type = a2;
            this.locals = a3;
        }
        
        FrameData(final int a1, final FrameNode a2) {
            this.index = a1;
            this.type = a2.type;
            this.locals = ((a2.local != null) ? a2.local.size() : 0);
        }
        
        @Override
        public String toString() {
            /*SL:203*/return String.format("FrameData[index=%d, type=%s, locals=%d]", this.index, FrameData.FRAMETYPES[this.type + 1], this.locals);
        }
        
        static {
            FRAMETYPES = new String[] { "NEW", "FULL", "APPEND", "CHOP", "SAME", "SAME1" };
        }
    }
    
    abstract static class Member
    {
        private final Type type;
        private final String memberName;
        private final String memberDesc;
        private final boolean isInjected;
        private final int modifiers;
        private String currentName;
        private String currentDesc;
        private boolean decoratedFinal;
        private boolean decoratedMutable;
        private boolean unique;
        
        protected Member(final Member a1) {
            this(a1.type, a1.memberName, a1.memberDesc, a1.modifiers, a1.isInjected);
            this.currentName = a1.currentName;
            this.currentDesc = a1.currentDesc;
            this.unique = a1.unique;
        }
        
        protected Member(final Type a1, final String a2, final String a3, final int a4) {
            this(a1, a2, a3, a4, false);
        }
        
        protected Member(final Type a1, final String a2, final String a3, final int a4, final boolean a5) {
            this.type = a1;
            this.memberName = a2;
            this.memberDesc = a3;
            this.isInjected = a5;
            this.currentName = a2;
            this.currentDesc = a3;
            this.modifiers = a4;
        }
        
        public String getOriginalName() {
            /*SL:295*/return this.memberName;
        }
        
        public String getName() {
            /*SL:299*/return this.currentName;
        }
        
        public String getOriginalDesc() {
            /*SL:303*/return this.memberDesc;
        }
        
        public String getDesc() {
            /*SL:307*/return this.currentDesc;
        }
        
        public boolean isInjected() {
            /*SL:311*/return this.isInjected;
        }
        
        public boolean isRenamed() {
            /*SL:315*/return !this.currentName.equals(this.memberName);
        }
        
        public boolean isRemapped() {
            /*SL:319*/return !this.currentDesc.equals(this.memberDesc);
        }
        
        public boolean isPrivate() {
            /*SL:323*/return (this.modifiers & 0x2) != 0x0;
        }
        
        public boolean isStatic() {
            /*SL:327*/return (this.modifiers & 0x8) != 0x0;
        }
        
        public boolean isAbstract() {
            /*SL:331*/return (this.modifiers & 0x400) != 0x0;
        }
        
        public boolean isFinal() {
            /*SL:335*/return (this.modifiers & 0x10) != 0x0;
        }
        
        public boolean isSynthetic() {
            /*SL:339*/return (this.modifiers & 0x1000) != 0x0;
        }
        
        public boolean isUnique() {
            /*SL:343*/return this.unique;
        }
        
        public void setUnique(final boolean a1) {
            /*SL:347*/this.unique = a1;
        }
        
        public boolean isDecoratedFinal() {
            /*SL:351*/return this.decoratedFinal;
        }
        
        public boolean isDecoratedMutable() {
            /*SL:355*/return this.decoratedMutable;
        }
        
        public void setDecoratedFinal(final boolean a1, final boolean a2) {
            /*SL:359*/this.decoratedFinal = a1;
            /*SL:360*/this.decoratedMutable = a2;
        }
        
        public boolean matchesFlags(final int a1) {
            /*SL:364*/return ((~this.modifiers | (a1 & 0x2)) & 0x2) != 0x0 && ((~this.modifiers | (a1 & 0x8)) & 0x8) != 0x0;
        }
        
        public abstract ClassInfo getOwner();
        
        public ClassInfo getImplementor() {
            /*SL:372*/return this.getOwner();
        }
        
        public int getAccess() {
            /*SL:376*/return this.modifiers;
        }
        
        public String renameTo(final String a1) {
            /*SL:385*/return this.currentName = a1;
        }
        
        public String remapTo(final String a1) {
            /*SL:390*/return this.currentDesc = a1;
        }
        
        public boolean equals(final String a1, final String a2) {
            /*SL:394*/return (this.memberName.equals(a1) || this.currentName.equals(a1)) && (this.memberDesc.equals(a2) || /*EL:395*/this.currentDesc.equals(a2));
        }
        
        @Override
        public boolean equals(final Object a1) {
            /*SL:400*/if (!(a1 instanceof Member)) {
                /*SL:401*/return false;
            }
            final Member v1 = /*EL:404*/(Member)a1;
            /*SL:405*/return (v1.memberName.equals(this.memberName) || v1.currentName.equals(this.currentName)) && (v1.memberDesc.equals(this.memberDesc) || /*EL:406*/v1.currentDesc.equals(this.currentDesc));
        }
        
        @Override
        public int hashCode() {
            /*SL:411*/return this.toString().hashCode();
        }
        
        @Override
        public String toString() {
            /*SL:416*/return String.format(this.getDisplayFormat(), this.memberName, this.memberDesc);
        }
        
        protected String getDisplayFormat() {
            /*SL:420*/return "%s%s";
        }
        
        enum Type
        {
            METHOD, 
            FIELD;
        }
    }
    
    public class Method extends Member
    {
        private final List<FrameData> frames;
        private boolean isAccessor;
        
        public Method(final Member a2) {
            super(a2);
            this.frames = ((a2 instanceof Method) ? ((Method)a2).frames : null);
        }
        
        public Method(final ClassInfo a1, final MethodNode a2) {
            this(a1, a2, false);
            this.setUnique(Annotations.getVisible(a2, Unique.class) != null);
            this.isAccessor = (Annotations.getSingleVisible(a2, Accessor.class, Invoker.class) != null);
        }
        
        public Method(final MethodNode a2, final boolean a3) {
            super(Type.METHOD, a2.name, a2.desc, a2.access, a3);
            this.frames = this.gatherFrames(a2);
            this.setUnique(Annotations.getVisible(a2, Unique.class) != null);
            this.isAccessor = (Annotations.getSingleVisible(a2, Accessor.class, Invoker.class) != null);
        }
        
        public Method(final String a2, final String a3) {
            super(Type.METHOD, a2, a3, 1, false);
            this.frames = null;
        }
        
        public Method(final String a2, final String a3, final int a4) {
            super(Type.METHOD, a2, a3, a4, false);
            this.frames = null;
        }
        
        public Method(final String a2, final String a3, final int a4, final boolean a5) {
            super(Type.METHOD, a2, a3, a4, a5);
            this.frames = null;
        }
        
        private List<FrameData> gatherFrames(final MethodNode v-1) {
            final List<FrameData> v0 = /*EL:470*/new ArrayList<FrameData>();
            /*SL:471*/for (final AbstractInsnNode a1 : v-1.instructions) {
                /*SL:473*/if (a1 instanceof FrameNode) {
                    /*SL:474*/v0.add(new FrameData(v-1.instructions.indexOf(a1), (FrameNode)a1));
                }
            }
            /*SL:477*/return v0;
        }
        
        public List<FrameData> getFrames() {
            /*SL:481*/return this.frames;
        }
        
        @Override
        public ClassInfo getOwner() {
            /*SL:486*/return ClassInfo.this;
        }
        
        public boolean isAccessor() {
            /*SL:490*/return this.isAccessor;
        }
        
        @Override
        public boolean equals(final Object a1) {
            /*SL:495*/return a1 instanceof Method && /*EL:499*/super.equals(a1);
        }
    }
    
    public class InterfaceMethod extends Method
    {
        private final ClassInfo owner;
        
        public InterfaceMethod(final Member a2) {
            super(a2);
            this.owner = a2.getOwner();
        }
        
        @Override
        public ClassInfo getOwner() {
            /*SL:519*/return this.owner;
        }
        
        @Override
        public ClassInfo getImplementor() {
            /*SL:524*/return ClassInfo.this;
        }
    }
    
    class Field extends Member
    {
        public Field(final Member a2) {
            super(a2);
        }
        
        public Field(final ClassInfo a1, final FieldNode a2) {
            this(a1, a2, false);
        }
        
        public Field(final FieldNode v2, final boolean v3) {
            super(Type.FIELD, v2.name, v2.desc, v2.access, v3);
            this.setUnique(Annotations.getVisible(v2, Unique.class) != null);
            if (Annotations.getVisible(v2, Shadow.class) != null) {
                final boolean a1 = Annotations.getVisible(v2, Final.class) != null;
                final boolean a2 = Annotations.getVisible(v2, Mutable.class) != null;
                this.setDecoratedFinal(a1, a2);
            }
        }
        
        public Field(final String a2, final String a3, final int a4) {
            super(Type.FIELD, a2, a3, a4, false);
        }
        
        public Field(final String a2, final String a3, final int a4, final boolean a5) {
            super(Type.FIELD, a2, a3, a4, a5);
        }
        
        @Override
        public ClassInfo getOwner() {
            /*SL:564*/return ClassInfo.this;
        }
        
        @Override
        public boolean equals(final Object a1) {
            /*SL:569*/return a1 instanceof Field && /*EL:573*/super.equals(a1);
        }
        
        @Override
        protected String getDisplayFormat() {
            /*SL:578*/return "%s:%s";
        }
    }
}

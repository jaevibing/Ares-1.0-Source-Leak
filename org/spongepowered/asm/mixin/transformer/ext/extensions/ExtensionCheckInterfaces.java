package org.spongepowered.asm.mixin.transformer.ext.extensions;

import org.spongepowered.asm.lib.tree.FrameNode;
import org.spongepowered.asm.lib.tree.AbstractInsnNode;
import java.util.ArrayList;
import org.spongepowered.asm.mixin.transformer.ClassInfo.Member;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.gen.Accessor;
import java.lang.annotation.Annotation;
import org.spongepowered.asm.util.Annotations;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.lib.tree.MethodNode;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.commons.io.IOUtils;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.FileOutputStream;
import java.util.Set;
import org.spongepowered.asm.util.SignaturePrinter;
import java.util.Collection;
import java.util.HashSet;
import org.spongepowered.asm.util.PrettyPrinter;
import java.util.Iterator;
import org.spongepowered.asm.mixin.transformer.ext.ITargetClassContext;
import org.spongepowered.asm.mixin.MixinEnvironment;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.io.IOException;
import com.google.common.io.Files;
import com.google.common.base.Charsets;
import org.spongepowered.asm.util.Constants;
import com.google.common.collect.HashMultimap;
import org.spongepowered.asm.mixin.transformer.ClassInfo;
import com.google.common.collect.Multimap;
import java.io.File;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.transformer.ext.IExtension;

public class ExtensionCheckInterfaces implements IExtension
{
    private static final String AUDIT_DIR = "audit";
    private static final String IMPL_REPORT_FILENAME = "mixin_implementation_report";
    private static final String IMPL_REPORT_CSV_FILENAME = "mixin_implementation_report.csv";
    private static final String IMPL_REPORT_TXT_FILENAME = "mixin_implementation_report.txt";
    private static final Logger logger;
    private final File csv;
    private final File report;
    private final Multimap<ClassInfo, ClassInfo.Method> interfaceMethods;
    private boolean strict;
    
    public ExtensionCheckInterfaces() {
        this.interfaceMethods = (Multimap<ClassInfo, ClassInfo.Method>)HashMultimap.<Object, Object>create();
        final File v0 = new File(Constants.DEBUG_OUTPUT_DIR, "audit");
        v0.mkdirs();
        this.csv = new File(v0, "mixin_implementation_report.csv");
        this.report = new File(v0, "mixin_implementation_report.txt");
        try {
            Files.write("Class,Method,Signature,Interface\n", this.csv, Charsets.ISO_8859_1);
        }
        catch (IOException ex) {}
        try {
            final String v = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            Files.write("Mixin Implementation Report generated on " + v + "\n", this.report, Charsets.ISO_8859_1);
        }
        catch (IOException ex2) {}
    }
    
    @Override
    public boolean checkActive(final MixinEnvironment a1) {
        /*SL:116*/this.strict = a1.getOption(MixinEnvironment.Option.CHECK_IMPLEMENTS_STRICT);
        /*SL:117*/return a1.getOption(MixinEnvironment.Option.CHECK_IMPLEMENTS);
    }
    
    @Override
    public void preApply(final ITargetClassContext v2) {
        final ClassInfo v3 = /*EL:126*/v2.getClassInfo();
        /*SL:127*/for (final ClassInfo.Method a1 : v3.getInterfaceMethods(false)) {
            /*SL:128*/this.interfaceMethods.put(v3, a1);
        }
    }
    
    @Override
    public void postApply(final ITargetClassContext v-9) {
        final ClassInfo classInfo = /*EL:138*/v-9.getClassInfo();
        /*SL:141*/if (classInfo.isAbstract() && !this.strict) {
            ExtensionCheckInterfaces.logger.info(/*EL:142*/"{} is skipping abstract target {}", new Object[] { this.getClass().getSimpleName(), v-9 });
            /*SL:143*/return;
        }
        final String replace = /*EL:146*/classInfo.getName().replace('/', '.');
        int n = /*EL:147*/0;
        final PrettyPrinter v3 = /*EL:148*/new PrettyPrinter();
        /*SL:150*/v3.add("Class: %s", replace).hr();
        /*SL:151*/v3.add("%-32s %-47s  %s", "Return Type", "Missing Method", "From Interface").hr();
        final Set<ClassInfo.Method> interfaceMethods = /*EL:153*/classInfo.getInterfaceMethods(true);
        final Set<ClassInfo.Method> set = /*EL:154*/new HashSet<ClassInfo.Method>(classInfo.getSuperClass().getInterfaceMethods(true));
        /*SL:155*/set.addAll(this.interfaceMethods.removeAll(classInfo));
        /*SL:157*/for (final ClassInfo.Method a2 : interfaceMethods) {
            final ClassInfo.Method a1 = /*EL:158*/classInfo.findMethodInHierarchy(a2.getName(), a2.getDesc(), ClassInfo.SearchType.ALL_CLASSES, ClassInfo.Traversal.ALL);
            /*SL:161*/if (a1 != null && !a1.isAbstract()) {
                /*SL:162*/continue;
            }
            /*SL:166*/if (set.contains(a2)) {
                /*SL:167*/continue;
            }
            /*SL:170*/if (n > 0) {
                /*SL:171*/v3.add();
            }
            final SignaturePrinter v1 = /*EL:174*/new SignaturePrinter(a2.getName(), a2.getDesc()).setModifiers("");
            final String v2 = /*EL:175*/a2.getOwner().getName().replace('/', '.');
            /*SL:176*/++n;
            /*SL:177*/v3.add("%-32s%s", v1.getReturnType(), v1);
            /*SL:178*/v3.add("%-80s  %s", "", v2);
            /*SL:180*/this.appendToCSVReport(replace, a2, v2);
        }
        /*SL:183*/if (n > 0) {
            /*SL:184*/v3.hr().add("%82s%s: %d", "", "Total unimplemented", n);
            /*SL:185*/v3.print(System.err);
            /*SL:186*/this.appendToTextReport(v3);
        }
    }
    
    @Override
    public void export(final MixinEnvironment a1, final String a2, final boolean a3, final byte[] a4) {
    }
    
    private void appendToCSVReport(final String a1, final ClassInfo.Method a2, final String a3) {
        try {
            /*SL:201*/Files.append(String.format("%s,%s,%s,%s\n", a1, a2.getName(), a2.getDesc(), a3), this.csv, Charsets.ISO_8859_1);
        }
        catch (IOException ex) {}
    }
    
    private void appendToTextReport(final PrettyPrinter v2) {
        FileOutputStream v3 = /*EL:208*/null;
        try {
            /*SL:211*/v3 = new FileOutputStream(this.report, true);
            final PrintStream a1 = /*EL:212*/new PrintStream(v3);
            /*SL:213*/a1.print("\n");
            /*SL:214*/v2.print(a1);
        }
        catch (Exception ex) {}
        finally {
            /*SL:218*/IOUtils.closeQuietly((OutputStream)v3);
        }
    }
    
    static {
        logger = LogManager.getLogger("mixin");
    }
}

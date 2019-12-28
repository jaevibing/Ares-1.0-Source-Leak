package org.spongepowered.asm.mixin.transformer.ext.extensions;

import org.apache.logging.log4j.LogManager;
import org.spongepowered.asm.util.perf.Profiler;
import org.spongepowered.asm.mixin.transformer.ext.ITargetClassContext;
import java.util.regex.Pattern;
import java.lang.reflect.Constructor;
import java.io.IOException;
import org.apache.commons.io.FileUtils;
import org.spongepowered.asm.util.Constants;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.mixin.transformer.ext.IDecompiler;
import java.io.File;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.transformer.ext.IExtension;

public class ExtensionClassExporter implements IExtension
{
    private static final String DECOMPILER_CLASS = "org.spongepowered.asm.mixin.transformer.debug.RuntimeDecompiler";
    private static final String EXPORT_CLASS_DIR = "class";
    private static final String EXPORT_JAVA_DIR = "java";
    private static final Logger logger;
    private final File classExportDir;
    private final IDecompiler decompiler;
    
    public ExtensionClassExporter(final MixinEnvironment v2) {
        this.classExportDir = new File(Constants.DEBUG_OUTPUT_DIR, "class");
        this.decompiler = this.initDecompiler(v2, new File(Constants.DEBUG_OUTPUT_DIR, "java"));
        try {
            FileUtils.deleteDirectory(this.classExportDir);
        }
        catch (IOException a1) {
            ExtensionClassExporter.logger.warn("Error cleaning class output directory: {}", new Object[] { a1.getMessage() });
        }
    }
    
    public boolean isDecompilerActive() {
        /*SL:79*/return this.decompiler != null;
    }
    
    private IDecompiler initDecompiler(final MixinEnvironment v-3, final File v-2) {
        /*SL:83*/if (!v-3.getOption(MixinEnvironment.Option.DEBUG_EXPORT_DECOMPILE)) {
            /*SL:84*/return null;
        }
        try {
            final boolean a1 = /*EL:88*/v-3.getOption(MixinEnvironment.Option.DEBUG_EXPORT_DECOMPILE_THREADED);
            ExtensionClassExporter.logger.info(/*EL:89*/"Attempting to load Fernflower decompiler{}", new Object[] { a1 ? " (Threaded mode)" : "" });
            final String a2 = /*EL:90*/"org.spongepowered.asm.mixin.transformer.debug.RuntimeDecompiler" + (a1 ? "Async" : "");
            final Class<? extends IDecompiler> v1 = /*EL:92*/(Class<? extends IDecompiler>)Class.forName(a2);
            final Constructor<? extends IDecompiler> v2 = /*EL:93*/v1.getDeclaredConstructor(File.class);
            final IDecompiler v3 = /*EL:94*/(IDecompiler)v2.newInstance(v-2);
            ExtensionClassExporter.logger.info(/*EL:95*/"Fernflower decompiler was successfully initialised, exported classes will be decompiled{}", new Object[] { a1 ? " in a separate thread" : "" });
            /*SL:97*/return v3;
        }
        catch (Throwable t) {
            ExtensionClassExporter.logger.info(/*EL:99*/"Fernflower could not be loaded, exported classes will not be decompiled. {}: {}", new Object[] { t.getClass().getSimpleName(), /*EL:100*/t.getMessage() });
            /*SL:102*/return null;
        }
    }
    
    private String prepareFilter(String a1) {
        /*SL:106*/a1 = "^\\Q" + a1.replace("**", "\u0081").replace("*", "\u0082").replace("?", "\u0083") + "\\E$";
        /*SL:107*/return a1.replace("\u0081", "\\E.*\\Q").replace("\u0082", "\\E[^\\.]+\\Q").replace("\u0083", "\\E.\\Q").replace("\\Q\\E", "");
    }
    
    private boolean applyFilter(final String a1, final String a2) {
        /*SL:111*/return Pattern.compile(this.prepareFilter(a1), 2).matcher(a2).matches();
    }
    
    @Override
    public boolean checkActive(final MixinEnvironment a1) {
        /*SL:116*/return true;
    }
    
    @Override
    public void preApply(final ITargetClassContext a1) {
    }
    
    @Override
    public void postApply(final ITargetClassContext a1) {
    }
    
    @Override
    public void export(final MixinEnvironment v1, final String v2, final boolean v3, final byte[] v4) {
        /*SL:130*/if (v3 || v1.getOption(MixinEnvironment.Option.DEBUG_EXPORT)) {
            String a3 = /*EL:131*/v1.getOptionValue(MixinEnvironment.Option.DEBUG_EXPORT_FILTER);
            /*SL:132*/if (v3 || a3 == null || this.applyFilter(a3, v2)) {
                final Profiler.Section a2 = /*EL:133*/MixinEnvironment.getProfiler().begin("debug.export");
                /*SL:134*/a3 = this.dumpClass(v2.replace('.', '/'), v4);
                /*SL:135*/if (this.decompiler != null) {
                    /*SL:136*/this.decompiler.decompile(a3);
                }
                /*SL:138*/a2.end();
            }
        }
    }
    
    public File dumpClass(final String a1, final byte[] a2) {
        final File v1 = /*EL:151*/new File(this.classExportDir, a1 + ".class");
        try {
            /*SL:153*/FileUtils.writeByteArrayToFile(v1, a2);
        }
        catch (IOException ex) {}
        /*SL:157*/return v1;
    }
    
    static {
        logger = LogManager.getLogger("mixin");
    }
}

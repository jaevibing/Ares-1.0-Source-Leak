package org.spongepowered.asm.mixin.transformer.debug;

import java.util.jar.Manifest;
import com.google.common.io.Files;
import com.google.common.base.Charsets;
import org.jetbrains.java.decompiler.main.Fernflower;
import org.jetbrains.java.decompiler.util.InterpreterUtil;
import org.jetbrains.java.decompiler.main.extern.IBytecodeProvider;
import java.io.IOException;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import com.google.common.collect.ImmutableMap;
import org.apache.logging.log4j.Logger;
import java.io.File;
import java.util.Map;
import org.apache.logging.log4j.Level;
import org.jetbrains.java.decompiler.main.extern.IResultSaver;
import org.spongepowered.asm.mixin.transformer.ext.IDecompiler;
import org.jetbrains.java.decompiler.main.extern.IFernflowerLogger;

public class RuntimeDecompiler extends IFernflowerLogger implements IDecompiler, IResultSaver
{
    private static final Level[] SEVERITY_LEVELS;
    private final Map<String, Object> options;
    private final File outputPath;
    protected final Logger logger;
    
    public RuntimeDecompiler(final File v2) {
        this.options = (Map<String, Object>)ImmutableMap.<String, String>builder().put("din", "0").put("rbr", "0").put("dgs", "1").put("asc", "1").put("den", "1").put("hdc", "1").put("ind", "    ").build();
        this.logger = LogManager.getLogger("fernflower");
        this.outputPath = v2;
        if (this.outputPath.exists()) {
            try {
                FileUtils.deleteDirectory(this.outputPath);
            }
            catch (IOException a1) {
                this.logger.warn("Error cleaning output directory: {}", new Object[] { a1.getMessage() });
            }
        }
    }
    
    public void decompile(final File v0) {
        try {
            final Fernflower a1 = /*EL:76*/new Fernflower((IBytecodeProvider)new IBytecodeProvider() {
                private byte[] byteCode;
                
                public byte[] getBytecode(final String a1, final String a2) throws IOException {
                    /*SL:82*/if (this.byteCode == null) {
                        /*SL:83*/this.byteCode = InterpreterUtil.getBytes(new File(a1));
                    }
                    /*SL:85*/return this.byteCode;
                }
            }, (IResultSaver)this, (Map)this.options, (IFernflowerLogger)this);
            /*SL:90*/a1.getStructContext().addSpace(v0, true);
            /*SL:91*/a1.decompileContext();
        }
        catch (Throwable v) {
            /*SL:93*/this.logger.warn("Decompilation error while processing {}", new Object[] { v0.getName() });
        }
    }
    
    public void saveFolder(final String a1) {
    }
    
    public void saveClassFile(final String a3, final String a4, final String a5, final String v1, final int[] v2) {
        final File v3 = /*EL:103*/new File(this.outputPath, a4 + ".java");
        /*SL:104*/v3.getParentFile().mkdirs();
        try {
            /*SL:106*/this.logger.info("Writing {}", new Object[] { v3.getAbsolutePath() });
            /*SL:107*/Files.write(v1, v3, Charsets.UTF_8);
        }
        catch (IOException a6) {
            /*SL:109*/this.writeMessage("Cannot write source file " + v3, a6);
        }
    }
    
    public void startReadingClass(final String a1) {
        /*SL:115*/this.logger.info("Decompiling {}", new Object[] { a1 });
    }
    
    public void writeMessage(final String a1, final IFernflowerLogger.Severity a2) {
        /*SL:120*/this.logger.log(RuntimeDecompiler.SEVERITY_LEVELS[a2.ordinal()], a1);
    }
    
    public void writeMessage(final String a1, final Throwable a2) {
        /*SL:125*/this.logger.warn("{} {}: {}", new Object[] { a1, a2.getClass().getSimpleName(), a2.getMessage() });
    }
    
    public void writeMessage(final String a1, final IFernflowerLogger.Severity a2, final Throwable a3) {
        /*SL:130*/this.logger.log(RuntimeDecompiler.SEVERITY_LEVELS[a2.ordinal()], a1, a3);
    }
    
    public void copyFile(final String a1, final String a2, final String a3) {
    }
    
    public void createArchive(final String a1, final String a2, final Manifest a3) {
    }
    
    public void saveDirEntry(final String a1, final String a2, final String a3) {
    }
    
    public void copyEntry(final String a1, final String a2, final String a3, final String a4) {
    }
    
    public void saveClassEntry(final String a1, final String a2, final String a3, final String a4, final String a5) {
    }
    
    public void closeArchive(final String a1, final String a2) {
    }
    
    static {
        SEVERITY_LEVELS = new Level[] { Level.TRACE, Level.INFO, Level.WARN, Level.ERROR };
    }
}

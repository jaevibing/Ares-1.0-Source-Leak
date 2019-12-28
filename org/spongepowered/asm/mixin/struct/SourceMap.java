package org.spongepowered.asm.mixin.struct;

import org.spongepowered.asm.lib.tree.LineNumberNode;
import org.spongepowered.asm.lib.tree.AbstractInsnNode;
import org.spongepowered.asm.lib.tree.MethodNode;
import java.util.Iterator;
import org.spongepowered.asm.util.Bytecode;
import org.spongepowered.asm.lib.tree.ClassNode;
import java.util.LinkedHashMap;
import java.util.Map;

public class SourceMap
{
    private static final String DEFAULT_STRATUM = "Mixin";
    private static final String NEWLINE = "\n";
    private final String sourceFile;
    private final Map<String, Stratum> strata;
    private int nextLineOffset;
    private String defaultStratum;
    
    public SourceMap(final String a1) {
        this.strata = new LinkedHashMap<String, Stratum>();
        this.nextLineOffset = 1;
        this.defaultStratum = "Mixin";
        this.sourceFile = a1;
    }
    
    public String getSourceFile() {
        /*SL:220*/return this.sourceFile;
    }
    
    public String getPseudoGeneratedSourceFile() {
        /*SL:227*/return this.sourceFile.replace(".java", "$mixin.java");
    }
    
    public File addFile(final ClassNode a1) {
        /*SL:237*/return this.addFile(this.defaultStratum, a1);
    }
    
    public File addFile(final String a1, final ClassNode a2) {
        /*SL:248*/return this.addFile(a1, a2.sourceFile, a2.name + ".java", Bytecode.getMaxLineNumber(a2, 500, 50));
    }
    
    public File addFile(final String a1, final String a2, final int a3) {
        /*SL:260*/return this.addFile(this.defaultStratum, a1, a2, a3);
    }
    
    public File addFile(final String a1, final String a2, final String a3, final int a4) {
        Stratum v1 = /*EL:273*/this.strata.get(a1);
        /*SL:274*/if (v1 == null) {
            /*SL:275*/v1 = new Stratum(a1);
            /*SL:276*/this.strata.put(a1, v1);
        }
        final File v2 = /*EL:279*/v1.addFile(this.nextLineOffset, a4, a2, a3);
        /*SL:280*/this.nextLineOffset += a4;
        /*SL:281*/return v2;
    }
    
    @Override
    public String toString() {
        final StringBuilder v1 = /*EL:286*/new StringBuilder();
        /*SL:287*/this.appendTo(v1);
        /*SL:288*/return v1.toString();
    }
    
    private void appendTo(final StringBuilder v2) {
        /*SL:293*/v2.append("SMAP").append("\n");
        /*SL:296*/v2.append(this.getSourceFile()).append("\n");
        /*SL:299*/v2.append(this.defaultStratum).append("\n");
        /*SL:300*/for (final Stratum a1 : this.strata.values()) {
            /*SL:301*/a1.appendTo(v2);
        }
        /*SL:305*/v2.append("*E").append("\n");
    }
    
    public static class File
    {
        public final int id;
        public final int lineOffset;
        public final int size;
        public final String sourceFileName;
        public final String sourceFilePath;
        
        public File(final int a1, final int a2, final int a3, final String a4) {
            this(a1, a2, a3, a4, null);
        }
        
        public File(final int a1, final int a2, final int a3, final String a4, final String a5) {
            this.id = a1;
            this.lineOffset = a2;
            this.size = a3;
            this.sourceFileName = a4;
            this.sourceFilePath = a5;
        }
        
        public void applyOffset(final ClassNode v2) {
            /*SL:110*/for (final MethodNode a1 : v2.methods) {
                /*SL:111*/this.applyOffset(a1);
            }
        }
        
        public void applyOffset(final MethodNode v0) {
            /*SL:122*/for (final AbstractInsnNode a1 : v0.instructions) {
                /*SL:124*/if (a1 instanceof LineNumberNode) {
                    final LineNumberNode lineNumberNode = /*EL:125*/(LineNumberNode)a1;
                    lineNumberNode.line += this.lineOffset - 1;
                }
            }
        }
        
        void appendFile(final StringBuilder a1) {
            /*SL:131*/if (this.sourceFilePath != null) {
                /*SL:132*/a1.append("+ ").append(this.id).append(" ").append(this.sourceFileName).append("\n");
                /*SL:133*/a1.append(this.sourceFilePath).append("\n");
            }
            else {
                /*SL:135*/a1.append(this.id).append(" ").append(this.sourceFileName).append("\n");
            }
        }
        
        public void appendLines(final StringBuilder a1) {
            /*SL:145*/a1.append("1#").append(this.id).append(",").append(/*EL:146*/this.size).append(":").append(/*EL:147*/this.lineOffset).append("\n");
        }
    }
    
    static class Stratum
    {
        private static final String STRATUM_MARK = "*S";
        private static final String FILE_MARK = "*F";
        private static final String LINES_MARK = "*L";
        public final String name;
        private final Map<String, File> files;
        
        public Stratum(final String a1) {
            this.files = new LinkedHashMap<String, File>();
            this.name = a1;
        }
        
        public File addFile(final int a1, final int a2, final String a3, final String a4) {
            File v1 = /*EL:174*/this.files.get(a4);
            /*SL:175*/if (v1 == null) {
                /*SL:176*/v1 = new File(this.files.size() + 1, a1, a2, a3, a4);
                /*SL:177*/this.files.put(a4, v1);
            }
            /*SL:179*/return v1;
        }
        
        void appendTo(final StringBuilder v-1) {
            /*SL:183*/v-1.append("*S").append(" ").append(this.name).append("\n");
            /*SL:185*/v-1.append("*F").append("\n");
            /*SL:186*/for (final File a1 : this.files.values()) {
                /*SL:187*/a1.appendFile(v-1);
            }
            /*SL:190*/v-1.append("*L").append("\n");
            /*SL:191*/for (final File v1 : this.files.values()) {
                /*SL:192*/v1.appendLines(v-1);
            }
        }
    }
}

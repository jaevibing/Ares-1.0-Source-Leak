package org.reflections.vfs;

import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;

public class JarInputFile implements Vfs.File
{
    private final ZipEntry entry;
    private final JarInputDir jarInputDir;
    private final long fromIndex;
    private final long endIndex;
    
    public JarInputFile(final ZipEntry a1, final JarInputDir a2, final long a3, final long a4) {
        this.entry = a1;
        this.jarInputDir = a2;
        this.fromIndex = a3;
        this.endIndex = a4;
    }
    
    @Override
    public String getName() {
        final String v1 = /*EL:24*/this.entry.getName();
        /*SL:25*/return v1.substring(v1.lastIndexOf("/") + 1);
    }
    
    @Override
    public String getRelativePath() {
        /*SL:29*/return this.entry.getName();
    }
    
    @Override
    public InputStream openInputStream() throws IOException {
        /*SL:33*/return new InputStream() {
            @Override
            public int read() throws IOException {
                /*SL:36*/if (JarInputFile.this.jarInputDir.cursor >= JarInputFile.this.fromIndex && JarInputFile.this.jarInputDir.cursor <= JarInputFile.this.endIndex) {
                    final int v1 = /*EL:37*/JarInputFile.this.jarInputDir.jarInputStream.read();
                    final JarInputDir access$000 = /*EL:38*/JarInputFile.this.jarInputDir;
                    ++access$000.cursor;
                    /*SL:39*/return v1;
                }
                /*SL:41*/return -1;
            }
        };
    }
}

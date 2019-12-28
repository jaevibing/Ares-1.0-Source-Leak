package org.reflections.vfs;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;

public class ZipFile implements Vfs.File
{
    private final ZipDir root;
    private final ZipEntry entry;
    
    public ZipFile(final ZipDir a1, final ZipEntry a2) {
        this.root = a1;
        this.entry = a2;
    }
    
    @Override
    public String getName() {
        final String v1 = /*EL:18*/this.entry.getName();
        /*SL:19*/return v1.substring(v1.lastIndexOf("/") + 1);
    }
    
    @Override
    public String getRelativePath() {
        /*SL:23*/return this.entry.getName();
    }
    
    @Override
    public InputStream openInputStream() throws IOException {
        /*SL:27*/return this.root.jarFile.getInputStream(this.entry);
    }
    
    @Override
    public String toString() {
        /*SL:32*/return this.root.getPath() + "!" + java.io.File.separatorChar + this.entry.toString();
    }
}

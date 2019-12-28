package org.reflections.vfs;

import java.io.FileNotFoundException;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.File;

public class SystemFile implements Vfs.File
{
    private final SystemDir root;
    private final java.io.File file;
    
    public SystemFile(final SystemDir a1, final java.io.File a2) {
        this.root = a1;
        this.file = a2;
    }
    
    @Override
    public String getName() {
        /*SL:18*/return this.file.getName();
    }
    
    @Override
    public String getRelativePath() {
        final String v1 = /*EL:22*/this.file.getPath().replace("\\", "/");
        /*SL:23*/if (v1.startsWith(this.root.getPath())) {
            /*SL:24*/return v1.substring(this.root.getPath().length() + 1);
        }
        /*SL:27*/return null;
    }
    
    @Override
    public InputStream openInputStream() {
        try {
            /*SL:32*/return new FileInputStream(this.file);
        }
        catch (FileNotFoundException v1) {
            /*SL:34*/throw new RuntimeException(v1);
        }
    }
    
    @Override
    public String toString() {
        /*SL:40*/return this.file.toString();
    }
}

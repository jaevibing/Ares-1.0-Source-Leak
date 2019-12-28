package org.reflections.vfs;

import java.io.IOException;
import org.reflections.Reflections;
import java.util.zip.ZipEntry;
import java.util.Enumeration;
import com.google.common.collect.AbstractIterator;
import java.util.Iterator;
import java.util.jar.JarFile;
import java.util.zip.ZipFile;

public class ZipDir implements Vfs.Dir
{
    final ZipFile jarFile;
    
    public ZipDir(final JarFile a1) {
        this.jarFile = a1;
    }
    
    @Override
    public String getPath() {
        /*SL:20*/return this.jarFile.getName();
    }
    
    @Override
    public Iterable<Vfs.File> getFiles() {
        /*SL:24*/return new Iterable<Vfs.File>() {
            @Override
            public Iterator<Vfs.File> iterator() {
                /*SL:26*/return new AbstractIterator<Vfs.File>() {
                    final Enumeration<? extends ZipEntry> entries = ZipDir.this.jarFile.entries();
                    
                    @Override
                    protected Vfs.File computeNext() {
                        /*SL:30*/while (this.entries.hasMoreElements()) {
                            final ZipEntry v1 = /*EL:31*/(ZipEntry)this.entries.nextElement();
                            /*SL:32*/if (!v1.isDirectory()) {
                                /*SL:33*/return new org.reflections.vfs.ZipFile(ZipDir.this, v1);
                            }
                        }
                        /*SL:37*/return this.endOfData();
                    }
                };
            }
        };
    }
    
    @Override
    public void close() {
        try {
            /*SL:45*/this.jarFile.close();
        }
        catch (IOException v1) {
            /*SL:46*/if (Reflections.log != null) {
                Reflections.log.warn(/*EL:47*/"Could not close JarFile", (Throwable)v1);
            }
        }
    }
    
    @Override
    public String toString() {
        /*SL:54*/return this.jarFile.getName();
    }
}

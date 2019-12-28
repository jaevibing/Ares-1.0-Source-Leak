package org.reflections.vfs;

import java.io.InputStream;
import org.reflections.util.Utils;
import java.util.zip.ZipEntry;
import java.io.IOException;
import org.reflections.ReflectionsException;
import com.google.common.collect.AbstractIterator;
import java.util.Iterator;
import java.util.jar.JarInputStream;
import java.net.URL;

public class JarInputDir implements Vfs.Dir
{
    private final URL url;
    JarInputStream jarInputStream;
    long cursor;
    long nextCursor;
    
    public JarInputDir(final URL a1) {
        this.cursor = 0L;
        this.nextCursor = 0L;
        this.url = a1;
    }
    
    @Override
    public String getPath() {
        /*SL:27*/return this.url.getPath();
    }
    
    @Override
    public Iterable<Vfs.File> getFiles() {
        /*SL:31*/return new Iterable<Vfs.File>() {
            @Override
            public Iterator<Vfs.File> iterator() {
                /*SL:33*/return new AbstractIterator<Vfs.File>() {
                    {
                        try {
                            JarInputDir.this.jarInputStream = new JarInputStream(JarInputDir.this.url.openConnection().getInputStream());
                        }
                        catch (Exception a1) {
                            throw new ReflectionsException("Could not open url connection", a1);
                        }
                    }
                    
                    @Override
                    protected Vfs.File computeNext() {
                        try {
                            while (true) {
                                final ZipEntry v1 = /*EL:43*/JarInputDir.this.jarInputStream.getNextJarEntry();
                                /*SL:44*/if (v1 == null) {
                                    /*SL:45*/return this.endOfData();
                                }
                                long v2 = /*EL:48*/v1.getSize();
                                /*SL:49*/if (v2 < 0L) {
                                    v2 += 4294967295L;
                                }
                                final JarInputDir this$0 = /*EL:50*/JarInputDir.this;
                                this$0.nextCursor += v2;
                                /*SL:51*/if (!v1.isDirectory()) {
                                    /*SL:52*/return new JarInputFile(v1, JarInputDir.this, JarInputDir.this.cursor, JarInputDir.this.nextCursor);
                                }
                            }
                        }
                        catch (IOException v3) {
                            /*SL:55*/throw new ReflectionsException("could not get next zip entry", v3);
                        }
                    }
                };
            }
        };
    }
    
    @Override
    public void close() {
        /*SL:65*/Utils.close(this.jarInputStream);
    }
}

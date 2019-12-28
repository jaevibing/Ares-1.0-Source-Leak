package org.reflections.vfs;

import com.google.common.collect.Lists;
import java.util.List;
import java.util.Collection;
import java.util.Stack;
import com.google.common.collect.AbstractIterator;
import java.util.Iterator;
import java.util.Collections;
import java.io.File;

public class SystemDir implements Vfs.Dir
{
    private final java.io.File file;
    
    public SystemDir(final java.io.File a1) {
        if (a1 != null && (!a1.isDirectory() || !a1.canRead())) {
            throw new RuntimeException("cannot use dir " + a1);
        }
        this.file = a1;
    }
    
    @Override
    public String getPath() {
        /*SL:27*/if (this.file == null) {
            /*SL:28*/return "/NO-SUCH-DIRECTORY/";
        }
        /*SL:30*/return this.file.getPath().replace("\\", "/");
    }
    
    @Override
    public Iterable<Vfs.File> getFiles() {
        /*SL:34*/if (this.file == null || !this.file.exists()) {
            /*SL:35*/return (Iterable<Vfs.File>)Collections.<Object>emptyList();
        }
        /*SL:37*/return new Iterable<Vfs.File>() {
            @Override
            public Iterator<Vfs.File> iterator() {
                /*SL:39*/return new AbstractIterator<Vfs.File>() {
                    final Stack<java.io.File> stack;
                    
                    {
                        (this.stack = new Stack<java.io.File>()).addAll((Collection<?>)listFiles(SystemDir.this.file));
                    }
                    
                    @Override
                    protected Vfs.File computeNext() {
                        /*SL:44*/while (!this.stack.isEmpty()) {
                            final java.io.File v1 = /*EL:45*/this.stack.pop();
                            /*SL:46*/if (!v1.isDirectory()) {
                                /*SL:49*/return new SystemFile(SystemDir.this, v1);
                            }
                            this.stack.addAll((Collection<?>)listFiles(v1));
                        }
                        /*SL:53*/return this.endOfData();
                    }
                };
            }
        };
    }
    
    private static List<java.io.File> listFiles(final java.io.File a1) {
        final java.io.File[] v1 = /*EL:61*/a1.listFiles();
        /*SL:63*/if (v1 != null) {
            /*SL:64*/return Lists.<java.io.File>newArrayList(v1);
        }
        /*SL:66*/return (List<java.io.File>)Lists.<Object>newArrayList();
    }
    
    @Override
    public void close() {
    }
    
    @Override
    public String toString() {
        /*SL:74*/return this.getPath();
    }
}

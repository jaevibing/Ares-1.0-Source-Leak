package org.spongepowered.asm.mixin.transformer.debug;

import java.util.concurrent.LinkedBlockingQueue;
import java.io.File;
import java.util.concurrent.BlockingQueue;

public class RuntimeDecompilerAsync extends RuntimeDecompiler implements Runnable, Thread.UncaughtExceptionHandler
{
    private final BlockingQueue<File> queue;
    private final Thread thread;
    private boolean run;
    
    public RuntimeDecompilerAsync(final File a1) {
        super(a1);
        this.queue = new LinkedBlockingQueue<File>();
        this.run = true;
        (this.thread = new Thread(this, "Decompiler thread")).setDaemon(true);
        this.thread.setPriority(1);
        this.thread.setUncaughtExceptionHandler(this);
        this.thread.start();
    }
    
    @Override
    public void decompile(final File a1) {
        /*SL:55*/if (this.run) {
            /*SL:56*/this.queue.offer(a1);
        }
        else {
            /*SL:58*/super.decompile(a1);
        }
    }
    
    @Override
    public void run() {
        /*SL:64*/while (this.run) {
            try {
                final File v1 = /*EL:66*/this.queue.take();
                /*SL:67*/super.decompile(v1);
            }
            catch (InterruptedException v3) {
                /*SL:69*/this.run = false;
            }
            catch (Exception v2) {
                /*SL:71*/v2.printStackTrace();
            }
        }
    }
    
    @Override
    public void uncaughtException(final Thread a1, final Throwable a2) {
        /*SL:78*/this.logger.error("Async decompiler encountered an error and will terminate. Further decompile requests will be handled synchronously. {} {}", new Object[] { a2.getClass().getName(), /*EL:79*/a2.getMessage() });
        /*SL:80*/this.flush();
    }
    
    private void flush() {
        /*SL:84*/this.run = false;
        File v1;
        /*SL:85*/while ((v1 = this.queue.poll()) != null) {
            /*SL:86*/this.decompile(v1);
        }
    }
}

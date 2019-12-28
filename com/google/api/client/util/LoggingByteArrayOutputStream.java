package com.google.api.client.util;

import java.text.NumberFormat;
import java.io.IOException;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.io.ByteArrayOutputStream;

public class LoggingByteArrayOutputStream extends ByteArrayOutputStream
{
    private int bytesWritten;
    private final int maximumBytesToLog;
    private boolean closed;
    private final Level loggingLevel;
    private final Logger logger;
    
    public LoggingByteArrayOutputStream(final Logger a1, final Level a2, final int a3) {
        this.logger = Preconditions.<Logger>checkNotNull(a1);
        this.loggingLevel = Preconditions.<Level>checkNotNull(a2);
        Preconditions.checkArgument(a3 >= 0);
        this.maximumBytesToLog = a3;
    }
    
    @Override
    public synchronized void write(final int a1) {
        /*SL:71*/Preconditions.checkArgument(!this.closed);
        /*SL:72*/++this.bytesWritten;
        /*SL:73*/if (this.count < this.maximumBytesToLog) {
            /*SL:74*/super.write(a1);
        }
    }
    
    @Override
    public synchronized void write(final byte[] a3, final int v1, int v2) {
        /*SL:80*/Preconditions.checkArgument(!this.closed);
        /*SL:81*/this.bytesWritten += v2;
        /*SL:82*/if (this.count < this.maximumBytesToLog) {
            final int a4 = /*EL:83*/this.count + v2;
            /*SL:84*/if (a4 > this.maximumBytesToLog) {
                /*SL:85*/v2 += this.maximumBytesToLog - a4;
            }
            /*SL:87*/super.write(a3, v1, v2);
        }
    }
    
    @Override
    public synchronized void close() throws IOException {
        /*SL:94*/if (!this.closed) {
            /*SL:96*/if (this.bytesWritten != 0) {
                final StringBuilder v1 = /*EL:98*/new StringBuilder().append("Total: ");
                appendBytes(/*EL:99*/v1, this.bytesWritten);
                /*SL:100*/if (this.count != 0 && this.count < this.bytesWritten) {
                    /*SL:101*/v1.append(" (logging first ");
                    appendBytes(/*EL:102*/v1, this.count);
                    /*SL:103*/v1.append(")");
                }
                /*SL:105*/this.logger.config(v1.toString());
                /*SL:107*/if (this.count != 0) {
                    /*SL:109*/this.logger.log(this.loggingLevel, this.toString("UTF-8").replaceAll(/*EL:110*/"[\\x00-\\x09\\x0B\\x0C\\x0E-\\x1F\\x7F]", " "));
                }
            }
            /*SL:113*/this.closed = true;
        }
    }
    
    public final int getMaximumBytesToLog() {
        /*SL:119*/return this.maximumBytesToLog;
    }
    
    public final synchronized int getBytesWritten() {
        /*SL:124*/return this.bytesWritten;
    }
    
    private static void appendBytes(final StringBuilder a1, final int a2) {
        /*SL:128*/if (a2 == 1) {
            /*SL:129*/a1.append("1 byte");
        }
        else {
            /*SL:131*/a1.append(NumberFormat.getInstance().format(a2)).append(" bytes");
        }
    }
}

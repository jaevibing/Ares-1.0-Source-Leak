package com.google.api.client.util;

import java.io.IOException;
import java.io.OutputStream;
import java.util.logging.Logger;
import java.util.logging.Level;

public final class LoggingStreamingContent implements StreamingContent
{
    private final StreamingContent content;
    private final int contentLoggingLimit;
    private final Level loggingLevel;
    private final Logger logger;
    
    public LoggingStreamingContent(final StreamingContent a1, final Logger a2, final Level a3, final int a4) {
        this.content = a1;
        this.logger = a2;
        this.loggingLevel = a3;
        this.contentLoggingLimit = a4;
    }
    
    @Override
    public void writeTo(final OutputStream a1) throws IOException {
        final LoggingOutputStream v1 = /*EL:62*/new LoggingOutputStream(a1, this.logger, this.loggingLevel, this.contentLoggingLimit);
        try {
            /*SL:65*/this.content.writeTo(v1);
        }
        finally {
            /*SL:68*/v1.getLogStream().close();
        }
        /*SL:70*/a1.flush();
    }
}

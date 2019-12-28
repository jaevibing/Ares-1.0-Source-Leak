package com.google.api.client.testing.util;

import java.util.Iterator;
import com.google.api.client.util.Lists;
import java.util.logging.LogRecord;
import java.util.List;
import com.google.api.client.util.Beta;
import java.util.logging.Handler;

@Beta
public class LogRecordingHandler extends Handler
{
    private final List<LogRecord> records;
    
    public LogRecordingHandler() {
        this.records = (List<LogRecord>)Lists.<Object>newArrayList();
    }
    
    @Override
    public void publish(final LogRecord a1) {
        /*SL:39*/this.records.add(a1);
    }
    
    @Override
    public void flush() {
    }
    
    @Override
    public void close() throws SecurityException {
    }
    
    public List<String> messages() {
        final List<String> arrayList = /*EL:52*/(List<String>)Lists.<Object>newArrayList();
        /*SL:53*/for (final LogRecord v1 : this.records) {
            /*SL:54*/arrayList.add(v1.getMessage());
        }
        /*SL:56*/return arrayList;
    }
}

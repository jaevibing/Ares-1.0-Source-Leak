package com.google.api.client.http;

import java.io.FileNotFoundException;
import java.io.FileInputStream;
import java.io.InputStream;
import com.google.api.client.util.Preconditions;
import java.io.File;

public final class FileContent extends AbstractInputStreamContent
{
    private final File file;
    
    public FileContent(final String a1, final File a2) {
        super(a1);
        this.file = Preconditions.<File>checkNotNull(a2);
    }
    
    @Override
    public long getLength() {
        /*SL:62*/return this.file.length();
    }
    
    @Override
    public boolean retrySupported() {
        /*SL:66*/return true;
    }
    
    @Override
    public InputStream getInputStream() throws FileNotFoundException {
        /*SL:71*/return new FileInputStream(this.file);
    }
    
    public File getFile() {
        /*SL:80*/return this.file;
    }
    
    @Override
    public FileContent setType(final String a1) {
        /*SL:85*/return (FileContent)super.setType(a1);
    }
    
    @Override
    public FileContent setCloseInputStream(final boolean a1) {
        /*SL:90*/return (FileContent)super.setCloseInputStream(a1);
    }
}

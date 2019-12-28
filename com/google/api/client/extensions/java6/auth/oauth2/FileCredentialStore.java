package com.google.api.client.extensions.java6.auth.oauth2;

import com.google.api.client.util.store.DataStore;
import com.google.api.client.util.store.DataStoreFactory;
import com.google.api.client.auth.oauth2.StoredCredential;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.client.json.JsonGenerator;
import java.io.OutputStream;
import com.google.api.client.util.Charsets;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.FileInputStream;
import com.google.api.client.auth.oauth2.Credential;
import java.io.IOException;
import com.google.api.client.util.Preconditions;
import java.util.concurrent.locks.ReentrantLock;
import java.io.File;
import java.util.concurrent.locks.Lock;
import com.google.api.client.json.JsonFactory;
import java.util.logging.Logger;
import com.google.api.client.util.Beta;
import com.google.api.client.auth.oauth2.CredentialStore;

@Deprecated
@Beta
public class FileCredentialStore implements CredentialStore
{
    private static final Logger LOGGER;
    private final JsonFactory jsonFactory;
    private final Lock lock;
    private FilePersistedCredentials credentials;
    private final File file;
    private static final boolean IS_WINDOWS;
    
    public FileCredentialStore(final File a1, final JsonFactory a2) throws IOException {
        this.lock = new ReentrantLock();
        this.credentials = new FilePersistedCredentials();
        this.file = Preconditions.<File>checkNotNull(a1);
        this.jsonFactory = Preconditions.<JsonFactory>checkNotNull(a2);
        final File v1 = a1.getCanonicalFile().getParentFile();
        if (v1 != null && !v1.exists() && !v1.mkdirs()) {
            final String value = String.valueOf(String.valueOf(v1));
            throw new IOException(new StringBuilder(35 + value.length()).append("unable to create parent directory: ").append(value).toString());
        }
        if (this.isSymbolicLink(a1)) {
            final String value2 = String.valueOf(String.valueOf(a1));
            throw new IOException(new StringBuilder(31 + value2.length()).append("unable to use a symbolic link: ").append(value2).toString());
        }
        if (!a1.createNewFile()) {
            this.loadCredentials(a1);
        }
        else {
            if (!a1.setReadable(false, false) || !a1.setWritable(false, false) || !a1.setExecutable(false, false)) {
                final Logger logger = FileCredentialStore.LOGGER;
                final String value3 = String.valueOf(String.valueOf(a1));
                logger.warning(new StringBuilder(49 + value3.length()).append("unable to change file permissions for everybody: ").append(value3).toString());
            }
            if (!a1.setReadable(true) || !a1.setWritable(true)) {
                final String value4 = String.valueOf(String.valueOf(a1));
                throw new IOException(new StringBuilder(32 + value4.length()).append("unable to set file permissions: ").append(value4).toString());
            }
            this.save();
        }
    }
    
    protected boolean isSymbolicLink(final File a1) throws IOException {
        /*SL:108*/if (FileCredentialStore.IS_WINDOWS) {
            /*SL:109*/return false;
        }
        File v1 = /*EL:112*/a1;
        /*SL:113*/if (a1.getParent() != null) {
            /*SL:114*/v1 = new File(a1.getParentFile().getCanonicalFile(), a1.getName());
        }
        /*SL:116*/return !v1.getCanonicalFile().equals(v1.getAbsoluteFile());
    }
    
    public void store(final String a1, final Credential a2) throws IOException {
        /*SL:121*/this.lock.lock();
        try {
            /*SL:123*/this.credentials.store(a1, a2);
            /*SL:124*/this.save();
        }
        finally {
            /*SL:126*/this.lock.unlock();
        }
    }
    
    public void delete(final String a1, final Credential a2) throws IOException {
        /*SL:132*/this.lock.lock();
        try {
            /*SL:134*/this.credentials.delete(a1);
            /*SL:135*/this.save();
        }
        finally {
            /*SL:137*/this.lock.unlock();
        }
    }
    
    public boolean load(final String a1, final Credential a2) {
        /*SL:143*/this.lock.lock();
        try {
            /*SL:147*/return this.credentials.load(a1, a2);
        }
        finally {
            this.lock.unlock();
        }
    }
    
    private void loadCredentials(final File a1) throws IOException {
        final FileInputStream v1 = /*EL:152*/new FileInputStream(a1);
        try {
            /*SL:154*/this.credentials = this.jsonFactory.<FilePersistedCredentials>fromInputStream(v1, FilePersistedCredentials.class);
        }
        finally {
            /*SL:156*/v1.close();
        }
    }
    
    private void save() throws IOException {
        final FileOutputStream v0 = /*EL:161*/new FileOutputStream(this.file);
        try {
            final JsonGenerator v = /*EL:163*/this.jsonFactory.createJsonGenerator(v0, Charsets.UTF_8);
            /*SL:164*/v.serialize(this.credentials);
            /*SL:165*/v.close();
        }
        finally {
            /*SL:167*/v0.close();
        }
    }
    
    public final void migrateTo(final FileDataStoreFactory a1) throws IOException {
        /*SL:190*/this.migrateTo(StoredCredential.getDefaultDataStore(a1));
    }
    
    public final void migrateTo(final DataStore<StoredCredential> a1) throws IOException {
        /*SL:200*/this.credentials.migrateTo(a1);
    }
    
    static {
        LOGGER = Logger.getLogger(FileCredentialStore.class.getName());
        IS_WINDOWS = (File.separatorChar == '\\');
    }
}

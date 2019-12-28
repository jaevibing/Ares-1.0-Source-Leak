package com.google.api.client.util.store;

import java.io.OutputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.FileInputStream;
import java.util.HashMap;
import com.google.api.client.util.Maps;
import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;
import com.google.api.client.util.Throwables;
import java.io.IOException;
import com.google.api.client.util.IOUtils;
import java.io.File;
import java.util.logging.Logger;

public class FileDataStoreFactory extends AbstractDataStoreFactory
{
    private static final Logger LOGGER;
    private final File dataDirectory;
    
    public FileDataStoreFactory(File a1) throws IOException {
        a1 = a1.getCanonicalFile();
        this.dataDirectory = a1;
        if (IOUtils.isSymbolicLink(a1)) {
            throw new IOException("unable to use a symbolic link: " + a1);
        }
        if (!a1.exists() && !a1.mkdirs()) {
            throw new IOException("unable to create directory: " + a1);
        }
        setPermissionsToOwnerOnly(a1);
    }
    
    public final File getDataDirectory() {
        /*SL:68*/return this.dataDirectory;
    }
    
    @Override
    protected <V extends java.lang.Object> DataStore<V> createDataStore(final String a1) throws IOException {
        /*SL:73*/return new FileDataStore<V>(this, this.dataDirectory, a1);
    }
    
    static void setPermissionsToOwnerOnly(final File v-1) throws IOException {
        try {
            final Method a1 = /*EL:128*/File.class.getMethod("setReadable", Boolean.TYPE, Boolean.TYPE);
            final Method v1 = /*EL:129*/File.class.getMethod("setWritable", Boolean.TYPE, Boolean.TYPE);
            final Method v2 = /*EL:130*/File.class.getMethod("setExecutable", Boolean.TYPE, Boolean.TYPE);
            /*SL:132*/if (!(boolean)a1.invoke(v-1, false, false) || !(boolean)v1.invoke(v-1, false, false) || !(boolean)v2.invoke(/*EL:133*/v-1, false, false)) {
                FileDataStoreFactory.LOGGER.warning(/*EL:134*/"unable to change permissions for everybody: " + v-1);
            }
            /*SL:137*/if (!(boolean)a1.invoke(v-1, true, true) || !(boolean)v1.invoke(v-1, true, true) || !(boolean)v2.invoke(/*EL:138*/v-1, true, true)) {
                FileDataStoreFactory.LOGGER.warning(/*EL:139*/"unable to change permissions for owner: " + v-1);
            }
        }
        catch (InvocationTargetException v4) {
            final Throwable v3 = /*EL:142*/v4.getCause();
            /*SL:143*/Throwables.<IOException>propagateIfPossible(v3, IOException.class);
            /*SL:145*/throw new RuntimeException(v3);
        }
        catch (NoSuchMethodException v5) {
            FileDataStoreFactory.LOGGER.warning(/*EL:147*/"Unable to set permissions for " + v-1 + ", likely because you are running a version of Java prior to 1.6");
        }
        catch (SecurityException ex) {}
        catch (IllegalAccessException ex2) {}
        catch (IllegalArgumentException ex3) {}
    }
    
    static {
        LOGGER = Logger.getLogger(FileDataStoreFactory.class.getName());
    }
    
    static class FileDataStore<V extends java.lang.Object> extends AbstractMemoryDataStore<V>
    {
        private final File dataFile;
        
        FileDataStore(final FileDataStoreFactory a1, final File a2, final String a3) throws IOException {
            super(a1, a3);
            this.dataFile = new File(a2, a3);
            if (IOUtils.isSymbolicLink(this.dataFile)) {
                throw new IOException("unable to use a symbolic link: " + this.dataFile);
            }
            if (this.dataFile.createNewFile()) {
                this.keyValueMap = Maps.<String, byte[]>newHashMap();
                this.save();
            }
            else {
                this.keyValueMap = (HashMap<String, byte[]>)IOUtils.deserialize((InputStream)new FileInputStream(this.dataFile));
            }
        }
        
        @Override
        public void save() throws IOException {
            /*SL:108*/IOUtils.serialize(this.keyValueMap, new FileOutputStream(this.dataFile));
        }
        
        @Override
        public FileDataStoreFactory getDataStoreFactory() {
            /*SL:113*/return (FileDataStoreFactory)super.getDataStoreFactory();
        }
    }
}

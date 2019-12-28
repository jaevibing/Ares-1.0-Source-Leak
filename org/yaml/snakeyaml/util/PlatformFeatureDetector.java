package org.yaml.snakeyaml.util;

public class PlatformFeatureDetector
{
    private Boolean isRunningOnAndroid;
    
    public PlatformFeatureDetector() {
        this.isRunningOnAndroid = null;
    }
    
    public boolean isRunningOnAndroid() {
        /*SL:23*/if (this.isRunningOnAndroid == null) {
            /*SL:25*/this.isRunningOnAndroid = System.getProperty("java.runtime.name").startsWith("Android Runtime");
        }
        /*SL:27*/return this.isRunningOnAndroid;
    }
}

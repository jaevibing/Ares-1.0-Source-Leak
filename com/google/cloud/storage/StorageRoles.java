package com.google.cloud.storage;

import com.google.cloud.Role;

public class StorageRoles
{
    public static Role admin() {
        /*SL:38*/return Role.of("roles/storage.admin");
    }
    
    public static Role objectViewer() {
        /*SL:50*/return Role.of("roles/storage.objectViewer");
    }
    
    public static Role objectCreator() {
        /*SL:61*/return Role.of("roles/storage.objectCreator");
    }
    
    public static Role objectAdmin() {
        /*SL:72*/return Role.of("roles/storage.objectAdmin");
    }
    
    public static Role legacyBucketOwner() {
        /*SL:89*/return Role.of("roles/storage.legacyBucketOwner");
    }
    
    public static Role legacyBucketWriter() {
        /*SL:103*/return Role.of("roles/storage.legacyBucketWriter");
    }
    
    public static Role legacyBucketReader() {
        /*SL:115*/return Role.of("roles/storage.legacyBucketReader");
    }
    
    public static Role legacyObjectOwner() {
        /*SL:129*/return Role.of("roles/storage.legacyObjectOwner");
    }
    
    public static Role legacyObjectReader() {
        /*SL:140*/return Role.of("roles/storage.legacyObjectReader");
    }
}

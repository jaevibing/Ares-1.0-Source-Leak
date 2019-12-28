package com.google.api.services.sheets.v4;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class SheetsScopes
{
    public static final String DRIVE = "https://www.googleapis.com/auth/drive";
    public static final String DRIVE_FILE = "https://www.googleapis.com/auth/drive.file";
    public static final String DRIVE_READONLY = "https://www.googleapis.com/auth/drive.readonly";
    public static final String SPREADSHEETS = "https://www.googleapis.com/auth/spreadsheets";
    public static final String SPREADSHEETS_READONLY = "https://www.googleapis.com/auth/spreadsheets.readonly";
    
    public static Set<String> all() {
        final HashSet<String> set = /*EL:51*/new HashSet<String>();
        /*SL:52*/set.add("https://www.googleapis.com/auth/drive");
        /*SL:53*/set.add("https://www.googleapis.com/auth/drive.file");
        /*SL:54*/set.add("https://www.googleapis.com/auth/drive.readonly");
        /*SL:55*/set.add("https://www.googleapis.com/auth/spreadsheets");
        /*SL:56*/set.add("https://www.googleapis.com/auth/spreadsheets.readonly");
        /*SL:57*/return (Set<String>)Collections.<Object>unmodifiableSet((Set<?>)set);
    }
}

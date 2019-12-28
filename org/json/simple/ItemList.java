package org.json.simple;

import java.util.Collection;
import java.util.StringTokenizer;
import java.util.ArrayList;
import java.util.List;

public class ItemList
{
    private String sp;
    List items;
    
    public ItemList() {
        this.sp = ",";
        this.items = new ArrayList();
    }
    
    public ItemList(final String a1) {
        this.sp = ",";
        this.items = new ArrayList();
        this.split(a1, this.sp, this.items);
    }
    
    public ItemList(final String a1, final String a2) {
        this.sp = ",";
        this.items = new ArrayList();
        this.split(this.sp = a1, a2, this.items);
    }
    
    public ItemList(final String a1, final String a2, final boolean a3) {
        this.sp = ",";
        this.split(a1, a2, this.items = new ArrayList(), a3);
    }
    
    public List getItems() {
        /*SL:39*/return this.items;
    }
    
    public String[] getArray() {
        /*SL:43*/return (String[])this.items.toArray();
    }
    
    public void split(final String a3, final String a4, final List v1, final boolean v2) {
        /*SL:47*/if (a3 == null || a4 == null) {
            /*SL:48*/return;
        }
        /*SL:49*/if (v2) {
            final StringTokenizer a5 = /*EL:50*/new StringTokenizer(a3, a4);
            /*SL:51*/while (a5.hasMoreTokens()) {
                /*SL:52*/v1.add(a5.nextToken().trim());
            }
        }
        else {
            /*SL:56*/this.split(a3, a4, v1);
        }
    }
    
    public void split(final String a1, final String a2, final List a3) {
        /*SL:61*/if (a1 == null || a2 == null) {
            /*SL:62*/return;
        }
        int v1 = /*EL:63*/0;
        int v2 = /*EL:64*/0;
        /*SL:72*/do {
            v2 = v1;
            v1 = a1.indexOf(a2, v1);
            if (v1 == -1) {
                break;
            }
            a3.add(a1.substring(v2, v1).trim());
            v1 += a2.length();
        } while (v1 != -1);
        /*SL:73*/a3.add(a1.substring(v2).trim());
    }
    
    public void setSP(final String a1) {
        /*SL:77*/this.sp = a1;
    }
    
    public void add(final int a1, final String a2) {
        /*SL:81*/if (a2 == null) {
            /*SL:82*/return;
        }
        /*SL:83*/this.items.add(a1, a2.trim());
    }
    
    public void add(final String a1) {
        /*SL:87*/if (a1 == null) {
            /*SL:88*/return;
        }
        /*SL:89*/this.items.add(a1.trim());
    }
    
    public void addAll(final ItemList a1) {
        /*SL:93*/this.items.addAll(a1.items);
    }
    
    public void addAll(final String a1) {
        /*SL:97*/this.split(a1, this.sp, this.items);
    }
    
    public void addAll(final String a1, final String a2) {
        /*SL:101*/this.split(a1, a2, this.items);
    }
    
    public void addAll(final String a1, final String a2, final boolean a3) {
        /*SL:105*/this.split(a1, a2, this.items, a3);
    }
    
    public String get(final int a1) {
        /*SL:113*/return this.items.get(a1);
    }
    
    public int size() {
        /*SL:117*/return this.items.size();
    }
    
    public String toString() {
        /*SL:121*/return this.toString(this.sp);
    }
    
    public String toString(final String v2) {
        final StringBuffer v3 = /*EL:125*/new StringBuffer();
        /*SL:127*/for (int a1 = 0; a1 < this.items.size(); ++a1) {
            /*SL:128*/if (a1 == 0) {
                /*SL:129*/v3.append(this.items.get(a1));
            }
            else {
                /*SL:131*/v3.append(v2);
                /*SL:132*/v3.append(this.items.get(a1));
            }
        }
        /*SL:135*/return v3.toString();
    }
    
    public void clear() {
        /*SL:140*/this.items.clear();
    }
    
    public void reset() {
        /*SL:144*/this.sp = ",";
        /*SL:145*/this.items.clear();
    }
}

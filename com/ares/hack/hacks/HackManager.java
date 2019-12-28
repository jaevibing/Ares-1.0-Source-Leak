package com.ares.hack.hacks;

import java.util.Comparator;
import java.util.Collection;
import java.util.ArrayList;
import java.util.Arrays;
import com.ares.hack.categories.Category;
import java.util.HashMap;
import java.util.Iterator;
import com.ares.natives.EncryptedStringPool;
import java.util.Map;
import java.util.List;

public class HackManager
{
    private static List<BaseHack> hacks;
    static Map<String, BaseHack> hacksMap;
    private static Boolean areHacksAlphabetical;
    public static StrLengthComparator strLengthComparator;
    public static AlphabeticComparator alphabeticComparator;
    
    public static List<BaseHack> getAll() {
        /*SL:18*/return HackManager.hacks;
    }
    
    public static BaseHack getHack(final String v1) {
        /*SL:23*/for (final BaseHack a1 : HackManager.hacks) {
            /*SL:25*/if (a1.name.equalsIgnoreCase(v1)) {
                /*SL:27*/return a1;
            }
        }
        /*SL:30*/throw new RuntimeException(EncryptedStringPool.poolGet(10) + v1 + EncryptedStringPool.poolGet(11));
    }
    
    public static BaseHack getHack(final Class<? extends BaseHack> v1) {
        /*SL:35*/for (final BaseHack a1 : HackManager.hacks) {
            /*SL:37*/if (a1.getClass() == v1) {
                /*SL:39*/return a1;
            }
        }
        /*SL:42*/throw new RuntimeException(EncryptedStringPool.poolGet(12) + v1.getName() + EncryptedStringPool.poolGet(11));
    }
    
    public static HashMap<Category, List<BaseHack>> getCategoryMap() {
        final HashMap<Category, List<BaseHack>> hashMap = /*EL:47*/new HashMap<Category, List<BaseHack>>();
        /*SL:49*/for (final BaseHack v1 : HackManager.hacks) {
            /*SL:51*/if (hashMap.containsKey(v1.category)) {
                /*SL:52*/hashMap.get(v1.category).add(v1);
            }
            else {
                /*SL:54*/hashMap.put(v1.category, new ArrayList<BaseHack>(Arrays.<BaseHack>asList(v1)));
            }
        }
        /*SL:56*/return hashMap;
    }
    
    public static void sortHacks(final boolean a1) {
        /*SL:61*/if (a1) {
            /*SL:63*/if (HackManager.areHacksAlphabetical == null || !HackManager.areHacksAlphabetical) {
                HackManager.hacks.sort(HackManager.alphabeticComparator);
                HackManager.areHacksAlphabetical = /*EL:66*/true;
            }
        }
        else/*SL:71*/ if (HackManager.areHacksAlphabetical == null || HackManager.areHacksAlphabetical) {
            HackManager.hacks.sort(HackManager.strLengthComparator);
            HackManager.areHacksAlphabetical = /*EL:74*/false;
        }
    }
    
    static {
        HackManager.hacks = new ArrayList<BaseHack>() {};
        HackManager.hacksMap = new HashMap<String, BaseHack>();
        HackManager.areHacksAlphabetical = null;
        HackManager.strLengthComparator = new StrLengthComparator();
        HackManager.alphabeticComparator = new AlphabeticComparator();
    }
}

package com.ares.hack.categories;

import com.ares.hack.hacks.BaseHack;
import java.util.ArrayList;

public class Category
{
    private static ArrayList<Category> categories;
    private ArrayList<BaseHack> hacks;
    private String name;
    
    Category(final String a1) {
        this.hacks = new ArrayList<BaseHack>();
        this.name = a1;
        Category.categories.add(this);
    }
    
    public String getName() {
        /*SL:24*/return this.name;
    }
    
    public ArrayList<BaseHack> getHacks() {
        /*SL:29*/return this.hacks;
    }
    
    @Override
    public String toString() {
        /*SL:35*/return this.getName();
    }
    
    public static ArrayList<Category> getAll() {
        System.out.println(/*EL:42*/"Categories: " + Category.categories.toString());
        /*SL:44*/return Category.categories;
    }
    
    static {
        Category.categories = new ArrayList<Category>();
    }
}

package com.ares.gui.guis.clickgui.buttons;

import com.ares.hack.categories.Category;
import java.util.ArrayList;

public class CategoryButton extends Button
{
    private static ArrayList<CategoryButton> categoryButtons;
    private ArrayList<HackButton> hackButtons;
    public static final int width = 100;
    public static final int height = 20;
    private static final int startX = 25;
    private static int startY;
    private Category category;
    int listPlaces;
    public boolean isDragging;
    public int StartMouseX;
    public int StartMouseY;
    
    public CategoryButton(final Category a1) {
        super(25, CategoryButton.startY, 100, 20, a1.getName(), true, new float[] { 1.0f, 0.0f, 0.0f, 1.0f });
        this.hackButtons = new ArrayList<HackButton>();
        this.listPlaces = 1;
        this.isDragging = false;
        this.StartMouseX = 0;
        this.StartMouseY = 0;
        this.category = a1;
        CategoryButton.startY += 21;
        CategoryButton.categoryButtons.add(this);
    }
    
    public Category getCategory() {
        /*SL:61*/return this.category;
    }
    
    public ArrayList<HackButton> getHackButtons() {
        /*SL:66*/return this.hackButtons;
    }
    
    public static ArrayList<CategoryButton> getAll() {
        /*SL:71*/return CategoryButton.categoryButtons;
    }
    
    public static ArrayList<CategoryButton> getAllInverted() {
        final ArrayList<CategoryButton> v0 = /*EL:76*/new ArrayList<CategoryButton>();
        /*SL:78*/for (int v = getAll().size() - 1; v >= 0; --v) {
            /*SL:80*/v0.add(getAll().get(v));
        }
        /*SL:83*/return v0;
    }
    
    static {
        CategoryButton.categoryButtons = new ArrayList<CategoryButton>();
        CategoryButton.startY = 25;
    }
}

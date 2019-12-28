package com.ares.gui.guis.containergui.buttons;

import com.ares.hack.categories.Category;
import com.ares.gui.guis.AresGui;
import com.ares.hack.categories.EnumCategory;
import com.ares.gui.buttons.TextButtonCentered;

public class CategoryButton extends TextButtonCentered
{
    private EnumCategory category;
    
    public CategoryButton(final AresGui a1, final int a2, final int a3, final int a4, final int a5, final EnumCategory a6) {
        super(a1, a2, a3, a4, a5, a6.category.getName(), 1.0);
        this.category = a6;
    }
    
    public Category getCategory() {
        /*SL:21*/return this.category.category;
    }
}

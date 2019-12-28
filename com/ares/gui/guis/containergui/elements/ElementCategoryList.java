package com.ares.gui.guis.containergui.elements;

import java.util.Iterator;
import com.ares.utils.ColourUtils;
import net.minecraft.client.gui.GuiScreen;
import com.ares.hack.categories.EnumCategory;
import java.util.HashMap;
import com.ares.gui.guis.AresGui;
import com.ares.gui.guis.containergui.buttons.CategoryButton;
import com.ares.hack.categories.Category;
import java.util.Map;
import com.ares.gui.elements.Element;

public class ElementCategoryList extends Element
{
    private static final int SPACE_BETWEEN_CATS = 5;
    private static final int SPACE_BETWEEN_RECTS = 1;
    private Map<Category, CategoryButton> categoryButtonMap;
    
    public ElementCategoryList(final AresGui a4, final int v1, final int v2, final int v3) {
        super(a4, v1, v2);
        this.categoryButtonMap = new HashMap<Category, CategoryButton>();
        int v4 = 0;
        for (final EnumCategory a5 : EnumCategory.values()) {
            final CategoryButton a6 = this.<CategoryButton>addButton(new CategoryButton(a4, v1 + v3 / 2, v2 + (ElementCategoryList.mc.field_71466_p.field_78288_b + 5) * v4, ElementCategoryList.mc.field_71466_p.func_78256_a(a5.category.getName()), ElementCategoryList.mc.field_71466_p.field_78288_b + 1, a5));
            this.categoryButtonMap.put(a5.category, a6);
            ++v4;
        }
    }
    
    public void onDraw(final GuiScreen v2) {
        /*SL:38*/for (final CategoryButton a1 : this.categoryButtonMap.values()) {
            /*SL:40*/if (a1.getCategory() == ElementHackList.currentCategory) {
                /*SL:42*/a1.textColor = ColourUtils.getAresRedString();
            }
            else {
                /*SL:46*/a1.textColor = "FFFFFF";
            }
        }
    }
}

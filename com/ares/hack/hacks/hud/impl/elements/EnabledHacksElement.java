package com.ares.hack.hacks.hud.impl.elements;

import java.util.Iterator;
import com.ares.utils.data.Vec2i;
import com.ares.utils.ColourUtils;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import com.ares.hack.hacks.HackManager;
import java.util.List;
import com.ares.hack.categories.EnumCategory;
import com.ares.hack.hacks.BaseHack;
import com.ares.hack.hacks.hud.api.MoveableHudElement;

@Hack(name = "Module List", description = "Lists enabled hacks", category = EnumCategory.HUD, defaultIsVisible = false)
public class EnabledHacksElement extends MoveableHudElement
{
    public EnabledHacksElement() {
        super("Enabled Hacks");
    }
    
    @Override
    public void render(final int v1, final int v2, final float v3) {
        /*SL:24*/super.render(v1, v2, v3);
        /*SL:25*/if (!this.shouldRender()) {
            return;
        }
        final List<BaseHack> v4 = /*EL:27*/HackManager.getAll().stream().filter(a1 -> a1.getEnabled() && a1.isVisible.getValue()).<List<BaseHack>, ?>collect((Collector<? super Object, ?, List<BaseHack>>)Collectors.<? super Object>toList());
        int v5 = /*EL:29*/0;
        int v6 = /*EL:30*/0;
        /*SL:31*/for (final BaseHack a2 : v4) {
            /*SL:33*/ColourUtils.drawRainbowString(a2.name, this.getPos().x, this.getPos().y + v6);
            /*SL:34*/v6 += EnabledHacksElement.mc.field_71466_p.field_78288_b + 2;
            final int a3 = EnabledHacksElement.mc.field_71466_p.func_78256_a(/*EL:36*/a2.name);
            /*SL:37*/if (a3 > v5) {
                /*SL:38*/v5 = a3;
            }
        }
        int v7 = /*EL:41*/v4.size() * (EnabledHacksElement.mc.field_71466_p.field_78288_b + 2);
        /*SL:42*/v7 -= 2;
        /*SL:44*/this.setSize(new Vec2i(v5, v7));
    }
}

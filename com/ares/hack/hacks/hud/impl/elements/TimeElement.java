package com.ares.hack.hacks.hud.impl.elements;

import com.ares.utils.ColourUtils;
import java.time.temporal.TemporalAccessor;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import com.ares.hack.categories.EnumCategory;
import com.ares.hack.hacks.BaseHack;
import com.ares.hack.hacks.hud.api.MoveableHudElement;

@Hack(name = "Time", description = "Displays current time", category = EnumCategory.HUD, defaultIsVisible = false)
public class TimeElement extends MoveableHudElement
{
    public TimeElement() {
        super("Time");
    }
    
    @Override
    public void render(final int a1, final int a2, final float a3) {
        /*SL:22*/super.render(a1, a2, a3);
        /*SL:23*/if (!this.shouldRender()) {
            return;
        }
        final String v1 = /*EL:25*/DateTimeFormatter.ofPattern("HH:mm:ss").format(LocalDateTime.now());
        /*SL:26*/ColourUtils.drawRainbowString(v1, this.getPos().x, this.getPos().y);
        /*SL:28*/this.getSize().x = TimeElement.mc.field_71466_p.func_78256_a(v1);
        /*SL:29*/this.getSize().y = TimeElement.mc.field_71466_p.field_78288_b;
    }
}

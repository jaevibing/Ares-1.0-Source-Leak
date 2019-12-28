package com.ares.hack.hacks.client;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.entity.player.EntityPlayer;
import com.ares.extensions.RenderGlobalWrapper;
import net.minecraft.world.World;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;
import com.ares.utils.render.RenderUtils;
import net.minecraft.block.material.Material;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;
import com.ares.hack.settings.settings.number.DoubleSetting;
import com.ares.hack.settings.settings.number.IntegerSetting;
import com.ares.hack.settings.settings.number.FloatSetting;
import com.ares.hack.settings.settings.BooleanSetting;
import com.ares.hack.settings.Setting;
import com.ares.hack.categories.EnumCategory;
import com.ares.hack.hacks.BaseHack;

@Hack(name = "HighlightBox", description = "Better Highlight Box", category = EnumCategory.CLIENT)
public class BetterHighlightBox extends BaseHack
{
    private Setting<Boolean> surround;
    private Setting<Float> width;
    private Setting<Integer> redS;
    private Setting<Integer> greenS;
    private Setting<Integer> blueS;
    private Setting<Double> alphaS;
    
    public BetterHighlightBox() {
        this.surround = new BooleanSetting("Surround", this, true);
        this.width = new FloatSetting("Width", this, 5.0f, 0.0f, 10.0f);
        this.redS = new IntegerSetting("Red", this, 0, 0, 255);
        this.greenS = new IntegerSetting("Green", this, 0, 0, 255);
        this.blueS = new IntegerSetting("Blue", this, 0, 0, 255);
        this.alphaS = new DoubleSetting("Alpha", this, 0.4, 0.0, 1.0);
    }
    
    @SubscribeEvent
    public void onDrawBlockHighlight(final DrawBlockHighlightEvent v-5) {
        /*SL:53*/if (this.getEnabled()) {
            final float partialTicks = /*EL:56*/v-5.getPartialTicks();
            final EntityPlayer player = /*EL:57*/v-5.getPlayer();
            final RayTraceResult target = /*EL:59*/v-5.getTarget();
            /*SL:61*/if (target.field_72313_a == RayTraceResult.Type.BLOCK) {
                final BlockPos func_178782_a = /*EL:63*/target.func_178782_a();
                final IBlockState v0 = BetterHighlightBox.mc.field_71441_e.func_180495_p(/*EL:64*/func_178782_a);
                /*SL:66*/if (v0.func_185904_a() != Material.field_151579_a && BetterHighlightBox.mc.field_71441_e.func_175723_af().func_177746_a(func_178782_a)) {
                    /*SL:68*/if (this.surround.getValue()) {
                        /*SL:70*/RenderUtils.glStart();
                        /*SL:71*/GL11.glColor4f((float)this.redS.getValue(), /*EL:72*/(float)this.greenS.getValue(), /*EL:73*/(float)this.blueS.getValue(), /*EL:74*/(float)(Object)this.alphaS.getValue());
                        /*SL:76*/GL11.glLineWidth((float)this.width.getValue());
                        final AxisAlignedBB a1 = /*EL:78*/RenderUtils.getBoundingBox(func_178782_a);
                        /*SL:79*/RenderUtils.drawOutlinedBox(a1);
                        /*SL:81*/RenderUtils.glEnd();
                    }
                    else {
                        /*SL:85*/GlStateManager.func_179147_l();
                        /*SL:86*/GlStateManager.func_187428_a(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
                        /*SL:87*/GlStateManager.func_187441_d((float)this.width.getValue());
                        /*SL:88*/GlStateManager.func_179090_x();
                        /*SL:89*/GlStateManager.func_179132_a(false);
                        final double v = /*EL:91*/player.field_70142_S + (player.field_70165_t - player.field_70142_S) * partialTicks;
                        final double v2 = /*EL:92*/player.field_70137_T + (player.field_70163_u - player.field_70137_T) * partialTicks;
                        final double v3 = /*EL:93*/player.field_70136_U + (player.field_70161_v - player.field_70136_U) * partialTicks;
                        /*SL:95*/RenderGlobalWrapper.func_189697_a(v0.func_185918_c((World)BetterHighlightBox.mc.field_71441_e, func_178782_a).func_186662_g(/*EL:96*/0.0020000000949949026).func_72317_d(/*EL:98*/-v, -v2, -v3), /*EL:99*/(float)Math.min(Math.abs(this.redS.getValue() - 255), 244), /*EL:100*/(float)Math.min(Math.abs(this.greenS.getValue() - 255), 244), /*EL:101*/(float)Math.min(Math.abs(this.blueS.getValue() - 255), 244), (float)(Object)this.alphaS.getValue());
                        /*SL:105*/GlStateManager.func_179132_a(true);
                        /*SL:106*/GlStateManager.func_179098_w();
                        /*SL:107*/GlStateManager.func_179084_k();
                    }
                }
            }
            /*SL:112*/v-5.setCanceled(true);
        }
    }
}

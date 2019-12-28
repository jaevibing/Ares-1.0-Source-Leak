package com.ares.hack.hacks.client;

import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.Arrays;
import java.util.Objects;
import java.util.function.Function;
import net.minecraftforge.registries.IForgeRegistryEntry;
import com.google.common.collect.ImmutableSet;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import com.ares.extensions.Wrapper;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemBlock;
import net.minecraft.block.BlockShulkerBox;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.nbt.NBTTagCompound;
import java.util.Collection;
import java.util.ArrayList;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraft.world.World;
import net.minecraft.tileentity.TileEntity;
import com.ares.utils.ItemNBTUtil;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import java.util.List;
import net.minecraft.util.ResourceLocation;
import com.ares.hack.categories.EnumCategory;
import com.ares.hack.hacks.BaseHack;

@Hack(name = "Shulker Preview", description = "Preview Shulkers via tooltip", category = EnumCategory.CLIENT)
public class ShulkerPreview extends BaseHack
{
    static String[] shulkerArr;
    private static final ResourceLocation SHULKER_ICON;
    private static List<ResourceLocation> shulkerBoxes;
    private static final int[][] TARGET_RATIOS;
    private static final int CORNER = 5;
    private static final int BUFFER = 1;
    private static final int EDGE = 18;
    
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void makeTooltip(final ItemTooltipEvent v-4) {
        /*SL:72*/if (isShulkerBox(v-4.getItemStack(), ShulkerPreview.shulkerBoxes) && v-4.getItemStack().func_77942_o()) {
            NBTTagCompound nbtTagCompound = /*EL:74*/ItemNBTUtil.getCompound(v-4.getItemStack(), "BlockEntityTag", true);
            /*SL:75*/if (nbtTagCompound != null) {
                /*SL:77*/if (!nbtTagCompound.func_150297_b("id", 8)) {
                    /*SL:79*/nbtTagCompound = nbtTagCompound.func_74737_b();
                    /*SL:80*/nbtTagCompound.func_74778_a("id", "minecraft:shulker_box");
                }
                final TileEntity func_190200_a = /*EL:82*/TileEntity.func_190200_a((World)ShulkerPreview.mc.field_71441_e, nbtTagCompound);
                /*SL:83*/if (func_190200_a != null && func_190200_a.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, (EnumFacing)null)) {
                    final List<String> toolTip = /*EL:85*/(List<String>)v-4.getToolTip();
                    final List<String> v0 = /*EL:86*/new ArrayList<String>(toolTip);
                    /*SL:88*/for (int v = 1; v < v0.size(); ++v) {
                        final String a1 = /*EL:90*/v0.get(v);
                        /*SL:91*/if (!a1.startsWith("§") || a1.startsWith("§o")) {
                            /*SL:92*/toolTip.remove(a1);
                        }
                    }
                }
            }
        }
    }
    
    @SubscribeEvent
    public void renderTooltip(final RenderTooltipEvent.PostText v-12) {
        /*SL:102*/if (!this.getEnabled()) {
            return;
        }
        /*SL:104*/if (isShulkerBox(v-12.getStack(), ShulkerPreview.shulkerBoxes) && v-12.getStack().func_77942_o()) {
            NBTTagCompound nbtTagCompound = /*EL:106*/ItemNBTUtil.getCompound(v-12.getStack(), "BlockEntityTag", true);
            /*SL:107*/if (nbtTagCompound != null) {
                /*SL:109*/if (!nbtTagCompound.func_150297_b("id", 8)) {
                    /*SL:111*/nbtTagCompound = nbtTagCompound.func_74737_b();
                    /*SL:112*/nbtTagCompound.func_74778_a("id", "minecraft:shulker_box");
                }
                final TileEntity func_190200_a = /*EL:114*/TileEntity.func_190200_a((World)ShulkerPreview.mc.field_71441_e, nbtTagCompound);
                /*SL:115*/if (func_190200_a != null && func_190200_a.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, (EnumFacing)null)) {
                    final ItemStack stack = /*EL:117*/v-12.getStack();
                    int a2 = /*EL:118*/v-12.getX() - 5;
                    int a3 = /*EL:119*/v-12.getY() - 70;
                    final IItemHandler itemHandler = /*EL:121*/(IItemHandler)func_190200_a.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, (EnumFacing)null);
                    /*SL:122*/assert itemHandler != null;
                    final int slots = /*EL:124*/itemHandler.getSlots();
                    int[] array = /*EL:125*/{ Math.min(slots, 9), Math.max(slots / 9, 1) };
                    /*SL:126*/for (final int[] a1 : ShulkerPreview.TARGET_RATIOS) {
                        /*SL:128*/if (a1[0] * a1[1] == slots) {
                            /*SL:130*/array = a1;
                            /*SL:131*/break;
                        }
                    }
                    final int n = /*EL:135*/10 + 18 * array[0];
                    /*SL:137*/if (a3 < 0) {
                        /*SL:138*/a3 = v-12.getY() + v-12.getLines().size() * 10 + 5;
                    }
                    final ScaledResolution scaledResolution = /*EL:140*/new ScaledResolution(ShulkerPreview.mc);
                    final int n2 = /*EL:141*/a2 + n;
                    /*SL:142*/if (n2 > scaledResolution.func_78326_a()) {
                        /*SL:143*/a2 -= n2 - scaledResolution.func_78326_a();
                    }
                    /*SL:145*/GlStateManager.func_179094_E();
                    /*SL:146*/RenderHelper.func_74519_b();
                    /*SL:147*/GlStateManager.func_179091_B();
                    /*SL:148*/GlStateManager.func_179124_c(1.0f, 1.0f, 1.0f);
                    /*SL:149*/GlStateManager.func_179109_b(0.0f, 0.0f, 700.0f);
                    ShulkerPreview.mc.func_110434_K().func_110577_a(ShulkerPreview.SHULKER_ICON);
                    /*SL:152*/RenderHelper.func_74518_a();
                    int v0 = /*EL:154*/-1;
                    /*SL:156*/if (((ItemBlock)stack.func_77973_b()).func_179223_d() instanceof BlockShulkerBox) {
                        final EnumDyeColor v = /*EL:157*/((BlockShulkerBox)((ItemBlock)stack.func_77973_b()).func_179223_d()).func_190956_e();
                        /*SL:158*/v0 = ItemDye.field_150922_c[v.func_176767_b()];
                    }
                    renderTooltipBackground(/*EL:161*/a2, a3, array[0], array[1], v0);
                    final RenderItem v2 = ShulkerPreview.mc.func_175599_af();
                    /*SL:165*/RenderHelper.func_74520_c();
                    /*SL:166*/GlStateManager.func_179126_j();
                    /*SL:167*/for (int v3 = 0; v3 < slots; ++v3) {
                        final ItemStack v4 = /*EL:169*/itemHandler.getStackInSlot(v3);
                        final int v5 = /*EL:170*/a2 + 6 + v3 % 9 * 18;
                        final int v6 = /*EL:171*/a3 + 6 + v3 / 9 * 18;
                        /*SL:173*/if (!v4.func_190926_b()) {
                            /*SL:175*/v2.func_180450_b(v4, v5, v6);
                            /*SL:176*/v2.func_175030_a(Wrapper.fontRenderer, v4, v5, v6);
                        }
                    }
                    /*SL:180*/GlStateManager.func_179097_i();
                    /*SL:181*/GlStateManager.func_179101_C();
                    /*SL:182*/GlStateManager.func_179121_F();
                }
            }
        }
    }
    
    private static void renderTooltipBackground(final int a3, final int a4, final int a5, final int v1, final int v2) {
        ShulkerPreview.mc.func_110434_K().func_110577_a(ShulkerPreview.SHULKER_ICON);
        /*SL:205*/GlStateManager.func_179124_c(((v2 & 0xFF0000) >> 16) / 255.0f, ((v2 & 0xFF00) >> 8) / 255.0f, (v2 & 0xFF) / 255.0f);
        /*SL:209*/RenderHelper.func_74518_a();
        /*SL:211*/Gui.func_146110_a(a3, a4, 0.0f, 0.0f, 5, 5, 256.0f, 256.0f);
        /*SL:214*/Gui.func_146110_a(a3 + 5 + 18 * a5, a4 + 5 + 18 * v1, 25.0f, 25.0f, 5, 5, 256.0f, 256.0f);
        /*SL:217*/Gui.func_146110_a(a3 + 5 + 18 * a5, a4, 25.0f, 0.0f, 5, 5, 256.0f, 256.0f);
        /*SL:220*/Gui.func_146110_a(a3, a4 + 5 + 18 * v1, 0.0f, 25.0f, 5, 5, 256.0f, 256.0f);
        /*SL:223*/for (int a6 = 0; a6 < v1; ++a6) {
            /*SL:224*/Gui.func_146110_a(a3, a4 + 5 + 18 * a6, 0.0f, 6.0f, 5, 18, 256.0f, 256.0f);
            /*SL:227*/Gui.func_146110_a(a3 + 5 + 18 * a5, a4 + 5 + 18 * a6, 25.0f, 6.0f, 5, 18, 256.0f, 256.0f);
            /*SL:230*/for (int a7 = 0; a7 < a5; ++a7) {
                /*SL:231*/if (a6 == 0) {
                    /*SL:232*/Gui.func_146110_a(a3 + 5 + 18 * a7, a4, 6.0f, 0.0f, 18, 5, 256.0f, 256.0f);
                    /*SL:235*/Gui.func_146110_a(a3 + 5 + 18 * a7, a4 + 5 + 18 * v1, 6.0f, 25.0f, 18, 5, 256.0f, 256.0f);
                }
                /*SL:240*/Gui.func_146110_a(a3 + 5 + 18 * a7, a4 + 5 + 18 * a6, 6.0f, 6.0f, 18, 18, 256.0f, 256.0f);
            }
        }
        /*SL:246*/GlStateManager.func_179124_c(1.0f, 1.0f, 1.0f);
    }
    
    private static boolean isShulkerBox(final ItemStack a1, final List<ResourceLocation> a2) {
        /*SL:251*/return !a1.func_190926_b() && isShulkerBox(a1.func_77973_b().getRegistryName(), a2);
    }
    
    private static boolean isShulkerBox(final ResourceLocation a1, final List<ResourceLocation> a2) {
        /*SL:255*/return a1 != null && /*EL:258*/a2.contains(a1);
    }
    
    static {
        ShulkerPreview.shulkerArr = ImmutableSet.<Block>of(Blocks.field_190977_dl, Blocks.field_190978_dm, Blocks.field_190979_dn, Blocks.field_190980_do, Blocks.field_190981_dp, Blocks.field_190982_dq, Blocks.field_190983_dr, Blocks.field_190984_ds, Blocks.field_190985_dt, Blocks.field_190986_du, Blocks.field_190987_dv, Blocks.field_190988_dw, Blocks.field_190989_dx, Blocks.field_190990_dy, Blocks.field_190991_dz, Blocks.field_190975_dA).stream().<Object>map((Function<? super Object, ?>)IForgeRegistryEntry.Impl::getRegistryName).<Object>map((Function<? super Object, ?>)Objects::toString).<String>toArray(String[]::new);
        SHULKER_ICON = new ResourceLocation("ares", "inv_slot.png");
        ShulkerPreview.shulkerBoxes = Arrays.<String>stream(ShulkerPreview.shulkerArr).<Object>map((Function<? super String, ?>)ResourceLocation::new).<List<ResourceLocation>, ?>collect((Collector<? super Object, ?, List<ResourceLocation>>)Collectors.<? super Object>toList());
        TARGET_RATIOS = new int[][] { { 1, 1 }, { 9, 3 }, { 9, 5 }, { 9, 6 }, { 9, 8 }, { 9, 9 }, { 12, 9 } };
    }
}

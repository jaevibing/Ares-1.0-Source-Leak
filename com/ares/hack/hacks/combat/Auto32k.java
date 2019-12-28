package com.ares.hack.hacks.combat;

import net.minecraft.init.Blocks;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.Vec3i;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.ContainerHopper;
import net.minecraft.util.math.MathHelper;
import com.ares.utils.WorldUtils;
import net.minecraft.item.Item;
import com.ares.utils.chat.ChatUtils;
import net.minecraft.util.math.Vec3d;
import com.ares.hack.settings.settings.BooleanSetting;
import com.ares.hack.settings.settings.number.IntegerSetting;
import com.ares.hack.settings.settings.EnumSetting;
import net.minecraft.util.math.BlockPos;
import com.ares.hack.settings.Setting;
import com.ares.hack.categories.EnumCategory;
import com.ares.hack.hacks.BaseHack;

@Hack(name = "Auto32k", description = "Instantly places shulker and hopper and grabs a 32k sword", category = EnumCategory.COMBAT)
public class Auto32k extends BaseHack
{
    private static final String COLOR = "blue";
    private Setting<PlaceMode> mode;
    private Setting<Integer> placeDelay;
    private Setting<Boolean> SecrectClose;
    private BlockPos basePos;
    private int direction;
    private int tickCount;
    private int hopper;
    private int shulker;
    private int solidBlock;
    private int dispenser;
    private int redstone;
    
    public Auto32k() {
        this.mode = new EnumSetting<PlaceMode>("Mode", this, PlaceMode.DISPENSER);
        this.placeDelay = new IntegerSetting("Disp.-Delay", this, 15, 0, 20);
        this.SecrectClose = new BooleanSetting("SecretClose", this, false);
        this.tickCount = 0;
    }
    
    public void onEnabled() {
        /*SL:71*/if (Auto32k.mc.field_71476_x == null || Auto32k.mc.field_71476_x.field_178784_b == null) {
            return;
        }
        /*SL:73*/if (!this.run()) {
            /*SL:75*/this.setEnabled(false);
        }
    }
    
    private boolean run() {
        /*SL:81*/this.basePos = null;
        /*SL:82*/this.tickCount = 0;
        /*SL:86*/if (Auto32k.mc.field_71476_x == null || Auto32k.mc.field_71476_x.field_178784_b == null) {
            /*SL:88*/return false;
        }
        /*SL:92*/this.basePos = Auto32k.mc.field_71476_x.func_178782_a().func_177972_a(Auto32k.mc.field_71476_x.field_178784_b);
        final Vec3d v0 = /*EL:97*/new Vec3d(Auto32k.mc.field_71439_g.field_70165_t, Auto32k.mc.field_71439_g.field_70163_u + Auto32k.mc.field_71439_g.func_70047_e(), Auto32k.mc.field_71439_g.field_70161_v);
        /*SL:100*/if (v0.func_72436_e(new Vec3d((double)this.basePos.func_177958_n(), (double)this.basePos.func_177956_o(), (double)this.basePos.func_177952_p())) > 16.0) {
            /*SL:102*/ChatUtils.printMessage("Location too far away!", "blue");
            /*SL:103*/return false;
        }
        /*SL:106*/this.hopper = WorldUtils.findItem(Item.func_150899_d(154));
        /*SL:107*/if (WorldUtils.findItem(Item.func_150899_d(154)) == -1) {
            /*SL:109*/ChatUtils.printMessage("A hopper was not found in your hotbar!", "blue");
            /*SL:110*/return false;
        }
        /*SL:113*/for (int v = 219; v <= 234; ++v) {
            /*SL:115*/this.shulker = WorldUtils.findItem(Item.func_150899_d(v));
            /*SL:116*/if (this.shulker != -1) {
                /*SL:118*/break;
            }
            /*SL:121*/if (v == 234) {
                /*SL:123*/ChatUtils.printMessage("A shulker was not found in your hotbar!", "blue");
                /*SL:124*/return false;
            }
        }
        /*SL:128*/if (this.mode.getValue() == PlaceMode.DISPENSER) {
            /*SL:131*/this.solidBlock = getBlockNotRedstone();
            /*SL:132*/if (this.solidBlock == -1) {
                /*SL:134*/ChatUtils.printMessage("No blocks found in hotbar!", "blue");
                /*SL:135*/return false;
            }
            /*SL:139*/this.dispenser = WorldUtils.findItem(Item.func_150899_d(23));
            /*SL:140*/if (this.dispenser == -1) {
                /*SL:142*/ChatUtils.printMessage("No dispenser found in hotbar!", "blue");
                /*SL:143*/return false;
            }
            /*SL:147*/this.redstone = WorldUtils.findItem(Item.func_150899_d(152));
            /*SL:148*/if (this.redstone == -1) {
                /*SL:150*/ChatUtils.printMessage("No redstone block found in hotbar!", "blue");
                /*SL:151*/return false;
            }
        }
        /*SL:156*/this.direction = (MathHelper.func_76128_c(Auto32k.mc.field_71439_g.field_70177_z * 4.0f / 360.0f + 0.5) & 0x3);
        /*SL:158*/return true;
    }
    
    public void onLogic() {
        /*SL:165*/if (Auto32k.mc.field_71439_g.field_71070_bA instanceof ContainerHopper) {
            /*SL:167*/for (int v1 = 0; v1 < Auto32k.mc.field_71439_g.field_71070_bA.field_75151_b.size(); ++v1) {
                /*SL:169*/if (isSuperWeapon(Auto32k.mc.field_71439_g.field_71070_bA.field_75151_b.get(v1).func_75211_c()) && !isSuperWeapon(Auto32k.mc.field_71439_g.field_71069_bz.field_75151_b.get(Auto32k.mc.field_71439_g.field_71071_by.field_70461_c).func_75211_c())) {
                    Auto32k.mc.field_71442_b.func_187098_a(Auto32k.mc.field_71439_g.field_71070_bA.field_75152_c, /*EL:171*/v1, Auto32k.mc.field_71439_g.field_71071_by.field_70461_c, ClickType.SWAP, (EntityPlayer)Auto32k.mc.field_71439_g);
                    /*SL:173*/if (this.SecrectClose.getValue()) {
                        Auto32k.mc.field_71439_g.func_71053_j();
                    }
                    /*SL:175*/return;
                }
            }
        }
        /*SL:180*/if (!this.getEnabled() || this.tickCount++ == 0) {
            return;
        }
        /*SL:182*/if (this.mode.getValue() == PlaceMode.HOPPER_ONLY) {
            Auto32k.mc.field_71439_g.field_71071_by.field_70461_c = /*EL:185*/this.hopper;
            /*SL:186*/WorldUtils.placeBlockMainHand(this.basePos);
            Auto32k.mc.field_71439_g.field_71071_by.field_70461_c = /*EL:189*/this.shulker;
            /*SL:190*/WorldUtils.placeBlockMainHand(new BlockPos(this.basePos.func_177958_n(), this.basePos.func_177956_o() + 1, this.basePos.func_177952_p()));
            /*SL:192*/this.endSequence();
        }
        else/*SL:194*/ if (this.mode.getValue() == PlaceMode.DISPENSER) {
            /*SL:196*/if (this.tickCount % this.placeDelay.getValue() != 0) {
                /*SL:198*/++this.tickCount;
                /*SL:199*/return;
            }
            BlockPos v2 = null;
            /*SL:204*/switch (this.direction) {
                case 0: {
                    /*SL:208*/v2 = new BlockPos((Vec3i)this.basePos.func_177982_a(0, 0, 1));
                    /*SL:209*/break;
                }
                case 1: {
                    /*SL:212*/v2 = new BlockPos((Vec3i)this.basePos.func_177982_a(-1, 0, 0));
                    /*SL:213*/break;
                }
                case 2: {
                    /*SL:216*/v2 = new BlockPos((Vec3i)this.basePos.func_177982_a(0, 0, -1));
                    /*SL:217*/break;
                }
                default: {
                    /*SL:220*/v2 = new BlockPos((Vec3i)this.basePos.func_177982_a(1, 0, 0));
                    break;
                }
            }
            /*SL:222*/if (Auto32k.mc.field_71441_e.func_180495_p(v2).func_185904_a().func_76222_j()) {
                Auto32k.mc.field_71439_g.field_71071_by.field_70461_c = /*EL:224*/this.solidBlock;
                /*SL:225*/WorldUtils.placeBlockMainHand(v2);
            }
            /*SL:230*/switch (this.direction) {
                case 0: {
                    /*SL:234*/v2 = new BlockPos((Vec3i)this.basePos.func_177982_a(0, 1, 1));
                    /*SL:235*/break;
                }
                case 1: {
                    /*SL:238*/v2 = new BlockPos((Vec3i)this.basePos.func_177982_a(-1, 1, 0));
                    /*SL:239*/break;
                }
                case 2: {
                    /*SL:242*/v2 = new BlockPos((Vec3i)this.basePos.func_177982_a(0, 1, -1));
                    /*SL:243*/break;
                }
                default: {
                    /*SL:246*/v2 = new BlockPos((Vec3i)this.basePos.func_177982_a(1, 1, 0));
                    break;
                }
            }
            /*SL:248*/if (Auto32k.mc.field_71441_e.func_180495_p(v2).func_185904_a().func_76222_j()) {
                Auto32k.mc.field_71439_g.field_71071_by.field_70461_c = /*EL:250*/this.dispenser;
                /*SL:251*/WorldUtils.placeBlockMainHand(v2);
                Auto32k.mc.field_71439_g.field_71071_by.field_70461_c = /*EL:252*/this.shulker;
                Auto32k.mc.field_71442_b.func_187099_a(Auto32k.mc.field_71439_g, Auto32k.mc.field_71441_e, /*EL:253*/v2, EnumFacing.UP, new Vec3d((double)v2.func_177958_n(), (double)v2.func_177956_o(), (double)v2.func_177952_p()), EnumHand.MAIN_HAND);
                /*SL:254*/++this.tickCount;
                /*SL:255*/return;
            }
            /*SL:258*/if (Auto32k.mc.field_71439_g.field_71070_bA.field_75151_b.get(1).func_75211_c().func_190926_b()) {
                Auto32k.mc.field_71442_b.func_187098_a(Auto32k.mc.field_71439_g.field_71070_bA.field_75152_c, Auto32k.mc.field_71439_g.field_71070_bA.field_75151_b.get(/*EL:260*/1).field_75222_d, Auto32k.mc.field_71439_g.field_71071_by.field_70461_c, ClickType.SWAP, (EntityPlayer)Auto32k.mc.field_71439_g);
                Auto32k.mc.field_71439_g.func_71053_j();
            }
            /*SL:265*/switch (this.direction) {
                case 0: {
                    /*SL:269*/v2 = new BlockPos((Vec3i)this.basePos.func_177982_a(0, 2, 1));
                    /*SL:270*/break;
                }
                case 1: {
                    /*SL:273*/v2 = new BlockPos((Vec3i)this.basePos.func_177982_a(-1, 2, 0));
                    /*SL:274*/break;
                }
                case 2: {
                    /*SL:277*/v2 = new BlockPos((Vec3i)this.basePos.func_177982_a(0, 2, -1));
                    /*SL:278*/break;
                }
                default: {
                    /*SL:281*/v2 = new BlockPos((Vec3i)this.basePos.func_177982_a(1, 2, 0));
                    break;
                }
            }
            /*SL:283*/if (Auto32k.mc.field_71441_e.func_180495_p(v2).func_185904_a().func_76222_j()) {
                Auto32k.mc.field_71439_g.field_71071_by.field_70461_c = /*EL:285*/WorldUtils.findItem(Item.func_150899_d(152));
                /*SL:286*/WorldUtils.placeBlockMainHand(v2);
                /*SL:287*/++this.tickCount;
                /*SL:288*/return;
            }
            Auto32k.mc.field_71439_g.field_71071_by.field_70461_c = /*EL:293*/this.hopper;
            /*SL:294*/WorldUtils.placeBlockMainHand(this.basePos);
            Auto32k.mc.field_71439_g.field_71071_by.field_70461_c = /*EL:296*/this.shulker;
            /*SL:298*/this.endSequence();
            /*SL:299*/this.setEnabled(false);
        }
    }
    
    private void endSequence() {
        /*SL:306*/if (this.SecrectClose.getValue()) {
            /*SL:308*/BaseHack.setEnabled("Secret Close", false);
            /*SL:309*/BaseHack.setEnabled("Secret Close", true);
        }
        Auto32k.mc.field_71442_b.func_187099_a(Auto32k.mc.field_71439_g, Auto32k.mc.field_71441_e, /*EL:313*/this.basePos, EnumFacing.UP, new Vec3d((double)this.basePos.func_177958_n(), (double)this.basePos.func_177956_o(), (double)this.basePos.func_177952_p()), EnumHand.MAIN_HAND);
    }
    
    static boolean isSuperWeapon(final ItemStack v-2) {
        /*SL:318*/if (v-2 == null) {
            /*SL:319*/return false;
        }
        /*SL:322*/if (v-2.func_77978_p() == null) {
            /*SL:323*/return false;
        }
        /*SL:326*/if (v-2.func_77986_q().func_150303_d() == 0) {
            /*SL:327*/return false;
        }
        final NBTTagList list = /*EL:330*/(NBTTagList)v-2.func_77978_p().func_74781_a("ench");
        int v0 = /*EL:332*/0;
        while (v0 < list.func_74745_c()) {
            final NBTTagCompound v = /*EL:333*/list.func_150305_b(v0);
            /*SL:334*/if (v.func_74762_e("id") == 16) {
                final int a1 = /*EL:335*/v.func_74762_e("lvl");
                /*SL:336*/if (a1 >= 16) {
                    /*SL:337*/return true;
                }
                break;
            }
            else {
                ++v0;
            }
        }
        /*SL:343*/return false;
    }
    
    static int getBlockNotRedstone() {
        /*SL:349*/for (int v1 = 0; v1 < 9; ++v1) {
            if (Auto32k.mc.field_71439_g.field_71071_by.func_70301_a(/*EL:351*/v1) != ItemStack.field_190927_a && Auto32k.mc.field_71439_g.field_71071_by.func_70301_a(v1).func_77973_b() instanceof ItemBlock && Block.func_149634_a(Auto32k.mc.field_71439_g.field_71071_by.func_70301_a(v1).func_77973_b()).func_176223_P().func_185913_b() && !Block.func_149634_a(Auto32k.mc.field_71439_g.field_71071_by.func_70301_a(v1).func_77973_b()).equals(Blocks.field_150451_bX) && !Block.func_149634_a(Auto32k.mc.field_71439_g.field_71071_by.func_70301_a(v1).func_77973_b()).equals(Blocks.field_150367_z)) {
                /*SL:356*/return v1;
            }
        }
        /*SL:359*/return -1;
    }
    
    enum PlaceMode
    {
        HOPPER_ONLY, 
        DISPENSER;
    }
}

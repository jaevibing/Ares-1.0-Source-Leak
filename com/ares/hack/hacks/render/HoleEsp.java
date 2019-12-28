package com.ares.hack.hacks.render;

import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.util.math.AxisAlignedBB;
import org.lwjgl.opengl.GL11;
import java.util.Map;
import com.ares.utils.render.RenderUtils;
import net.minecraft.init.Blocks;
import net.minecraft.block.state.IBlockState;
import java.util.Iterator;
import com.ares.hack.settings.settings.number.FloatSetting;
import com.ares.hack.settings.settings.BooleanSetting;
import com.ares.hack.settings.settings.number.IntegerSetting;
import net.minecraft.util.math.BlockPos;
import java.util.HashMap;
import com.ares.hack.settings.Setting;
import com.ares.hack.categories.EnumCategory;
import com.ares.hack.hacks.BaseHack;

@Hack(name = "Hole ESP", description = "See holes to camp in during pvp", category = EnumCategory.RENDER)
public class HoleEsp extends BaseHack
{
    private final Setting<Integer> holeRadius;
    private final Setting<Boolean> holeHeight;
    private final Setting<Boolean> renderBtm;
    private final Setting<Integer> maxY;
    private final Setting<Integer> maxHoles;
    private final Setting<Float> delay;
    private HashMap<BlockPos, HoleType> holes;
    
    public HoleEsp() {
        this.holeRadius = new IntegerSetting("Hole Radius", this, 10, 1, 50);
        this.holeHeight = new BooleanSetting("Hole height 3", this, false);
        this.renderBtm = new BooleanSetting("Render Bottom", this, false);
        this.maxY = new IntegerSetting("Max Y", this, 125, 0, 125);
        this.maxHoles = new IntegerSetting("Max Holes", this, 20, 1, 50);
        this.delay = new FloatSetting("Refresh Delay", this, 1.0f, 0.0f, 4.0f);
        this.holes = new HashMap<BlockPos, HoleType>();
    }
    
    public void onLogic() {
        /*SL:58*/if (!this.getEnabled()) {
            return;
        }
        /*SL:60*/this.holes.clear();
        final Iterable<BlockPos> func_177980_a = /*EL:62*/(Iterable<BlockPos>)BlockPos.func_177980_a(HoleEsp.mc.field_71439_g.func_180425_c().func_177982_a(/*EL:63*/-this.holeRadius.getValue(), -this.holeRadius.getValue(), -this.holeRadius.getValue()), HoleEsp.mc.field_71439_g.func_180425_c().func_177982_a(/*EL:64*/(int)this.holeRadius.getValue(), (int)this.holeRadius.getValue(), (int)this.holeRadius.getValue()));
        int n = /*EL:67*/0;
        /*SL:69*/for (final BlockPos v0 : func_177980_a) {
            /*SL:71*/if (n > this.maxHoles.getValue()) {
                return;
            }
            final HoleType v = /*EL:73*/this.isHole(v0, this.holeHeight.getValue());
            /*SL:75*/if (v == HoleType.NONE) {
                continue;
            }
            /*SL:77*/this.holes.put(v0, v);
            /*SL:78*/++n;
        }
    }
    
    public HoleType isHole(final BlockPos a1) {
        /*SL:85*/return this.isHole(a1, false);
    }
    
    public HoleType isHole(final BlockPos a1, final boolean a2) {
        /*SL:90*/if (a1.func_177956_o() > this.maxY.getValue()) {
            return HoleType.NONE;
        }
        final IBlockState[] v1 = /*EL:92*/{ HoleEsp.mc.field_71441_e.func_180495_p(a1), HoleEsp.mc.field_71441_e.func_180495_p(/*EL:93*/a1.func_177982_a(0, 1, 0)), HoleEsp.mc.field_71441_e.func_180495_p(/*EL:94*/a1.func_177982_a(0, 2, 0)), HoleEsp.mc.field_71441_e.func_180495_p(/*EL:95*/a1.func_177982_a(0, -1, 0)), HoleEsp.mc.field_71441_e.func_180495_p(/*EL:96*/a1.func_177982_a(1, 0, 0)), HoleEsp.mc.field_71441_e.func_180495_p(/*EL:97*/a1.func_177982_a(0, 0, 1)), HoleEsp.mc.field_71441_e.func_180495_p(/*EL:98*/a1.func_177982_a(-1, 0, 0)), HoleEsp.mc.field_71441_e.func_180495_p(/*EL:99*/a1.func_177982_a(0, 0, -1)) };
        final boolean v2 = /*EL:103*/!v1[0].func_185904_a().func_76230_c() && /*EL:104*/!v1[1].func_185904_a().func_76230_c() && /*EL:106*/(!v1[2].func_185904_a().func_76230_c() || /*EL:108*/!a2) && v1[3].func_177230_c().equals(Blocks.field_150357_h) && /*EL:110*/v1[4].func_177230_c().equals(Blocks.field_150357_h) && /*EL:112*/v1[5].func_177230_c().equals(Blocks.field_150357_h) && /*EL:114*/v1[6].func_177230_c().equals(Blocks.field_150357_h) && /*EL:116*/v1[7].func_177230_c().equals(Blocks.field_150357_h) && /*EL:118*/(!v1[2].func_185904_a().func_76230_c() || /*EL:120*/!a2);
        /*SL:123*/if (v2) {
            /*SL:124*/return HoleType.BEDROCK;
        }
        final boolean v3 = /*EL:126*/!v1[0].func_185904_a().func_76230_c() && /*EL:127*/!v1[1].func_185904_a().func_76230_c() && /*EL:129*/(!v1[2].func_185904_a().func_76230_c() || /*EL:131*/!a2) && (v1[3].func_177230_c().equals(Blocks.field_150357_h) || /*EL:133*/v1[3].func_177230_c().equals(Blocks.field_150343_Z)) && (v1[4].func_177230_c().equals(Blocks.field_150357_h) || /*EL:135*/v1[4].func_177230_c().equals(Blocks.field_150343_Z)) && (v1[5].func_177230_c().equals(Blocks.field_150357_h) || /*EL:137*/v1[5].func_177230_c().equals(Blocks.field_150343_Z)) && (v1[6].func_177230_c().equals(Blocks.field_150357_h) || /*EL:139*/v1[6].func_177230_c().equals(Blocks.field_150343_Z)) && (v1[7].func_177230_c().equals(Blocks.field_150357_h) || /*EL:141*/v1[7].func_177230_c().equals(Blocks.field_150343_Z));
        /*SL:144*/if (v3) {
            /*SL:145*/return HoleType.OBBY;
        }
        final boolean v4 = /*EL:147*/!v1[0].func_185904_a().func_76230_c() && /*EL:148*/!v1[1].func_185904_a().func_76230_c() && /*EL:150*/(!v1[2].func_185904_a().func_76230_c() || /*EL:152*/!a2) && v1[3].func_185904_a().func_76220_a() && /*EL:155*/v1[4].func_185904_a().func_76220_a() && /*EL:157*/v1[5].func_185904_a().func_76220_a() && /*EL:159*/v1[6].func_185904_a().func_76220_a() && /*EL:161*/v1[7].func_185904_a().func_76220_a();
        /*SL:170*/return HoleType.NONE;
    }
    
    public void onRender3d() {
        /*SL:176*/if (!this.getEnabled()) {
            return;
        }
        /*SL:178*/RenderUtils.glStart(255.0f, 0.0f, 255.0f, 0.2f);
        /*SL:180*/if (this.holes != null) {
            int n = /*EL:182*/0;
            int n2 = /*EL:183*/0;
            int n3 = /*EL:184*/0;
            /*SL:185*/for (final Map.Entry<BlockPos, HoleType> v0 : this.holes.entrySet()) {
                final HoleType v = /*EL:187*/v0.getValue();
                /*SL:189*/GL11.glLineWidth(2.0f);
                AxisAlignedBB v2 = /*EL:191*/RenderUtils.getBoundingBox(v0.getKey());
                /*SL:193*/if (this.renderBtm.getValue()) {
                    /*SL:195*/v2 = new AxisAlignedBB(v2.field_72340_a, v2.field_72338_b, v2.field_72339_c, v2.field_72336_d, v2.field_72338_b + 0.001, v2.field_72334_f);
                }
                /*SL:198*/if (v == HoleType.BEDROCK) {
                    /*SL:201*/RenderGlobal.func_189696_b(v2, 1.0f, 0.0f, 0.0f, 0.2f);
                    /*SL:202*/RenderGlobal.func_189697_a(v2, 1.0f, 0.0f, 0.0f, 0.2f);
                    /*SL:203*/++n;
                }
                /*SL:206*/if (v == HoleType.OBBY) {
                    /*SL:209*/RenderGlobal.func_189696_b(v2, 1.0f, 0.498f, 0.3137f, 0.2f);
                    /*SL:210*/RenderGlobal.func_189697_a(v2, 1.0f, 0.498f, 0.3137f, 0.2f);
                    /*SL:211*/++n2;
                }
                /*SL:214*/if (v == HoleType.OTHER) {
                    /*SL:217*/RenderGlobal.func_189696_b(v2, 255.0f, 255.0f, 255.0f, 0.2f);
                    /*SL:218*/RenderGlobal.func_189697_a(v2, 255.0f, 255.0f, 255.0f, 0.2f);
                    /*SL:219*/++n3;
                }
            }
        }
        /*SL:224*/RenderUtils.glEnd();
    }
    
    enum HoleType
    {
        BEDROCK, 
        OBBY, 
        OTHER, 
        NONE;
    }
}

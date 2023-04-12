package com.mgen256.al;

import java.util.function.Supplier;

import com.mgen256.al.blocks.ALLamp;
import com.mgen256.al.blocks.ALTorch;
import com.mgen256.al.blocks.ALTorch_Wall;
import com.mgen256.al.blocks.Fire;
import com.mgen256.al.blocks.FirePit_L;
import com.mgen256.al.blocks.FirePit_S;
import com.mgen256.al.blocks.Fire_Soul;
import com.mgen256.al.blocks.IModBlock;
import com.mgen256.al.blocks.StandingTorch_L;
import com.mgen256.al.blocks.StandingTorch_S;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.core.Direction;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.StandingAndWallBlockItem;

public enum ModBlockList {

    ALLamp_Acacia("al_lamp_acacia_planks", () -> new ALLamp(Blocks.ACACIA_PLANKS) ),
    ALLamp_Birch("al_lamp_birch_planks", () -> new ALLamp(Blocks.BIRCH_PLANKS) ),
    ALLamp_Oak("al_lamp_oak_planks", () -> new ALLamp(Blocks.OAK_PLANKS) ),
    ALLamp_Dark_Oak("al_lamp_dark_oak_planks", () -> new ALLamp(Blocks.DARK_OAK_PLANKS) ),
    ALLamp_Spruce("al_lamp_spruce_planks", () -> new ALLamp(Blocks.SPRUCE_PLANKS) ),
    ALLamp_Jungle("al_lamp_jungle_planks", () -> new ALLamp(Blocks.JUNGLE_PLANKS) ),
    ALLamp_Stone("al_lamp_stone", () -> new ALLamp(Blocks.STONE) ),
    ALLamp_CobbleStone("al_lamp_cobblestone", () -> new ALLamp(Blocks.COBBLESTONE) ),
    ALLamp_Mossy_CobbleStone("al_lamp_mossy_cobblestone", () -> new ALLamp(Blocks.MOSSY_COBBLESTONE) ),
    ALLamp_End_Stone("al_lamp_end_stone", () -> new ALLamp(Blocks.END_STONE) ),
    ALLamp_Sand_Stone("al_lamp_sandstone", () -> new ALLamp(Blocks.SANDSTONE) ),
    ALLamp_Glass("al_lamp_glass", () -> new ALLamp(Blocks.GLASS) ),
    ALLamp_Iron("al_lamp_iron_block", () -> new ALLamp(Blocks.IRON_BLOCK) ),
    ALLamp_Gold("al_lamp_gold_block", () -> new ALLamp(Blocks.GOLD_BLOCK) ),
    ALLamp_Diamond("al_lamp_diamond_block", () -> new ALLamp(Blocks.DIAMOND_BLOCK) ),
    ALLamp_Ice("al_lamp_packed_ice", () -> new ALLamp(Blocks.PACKED_ICE) ),
    ALLamp_Pink_Wool("al_lamp_pink_wool", () -> new ALLamp(Blocks.PINK_WOOL) ),
    ALLamp_Magenta_Wool("al_lamp_magenta_wool", () -> new ALLamp(Blocks.MAGENTA_WOOL) ),
    ALLamp_Nether_Bricks("al_lamp_nether_bricks", () -> new ALLamp(Blocks.NETHER_BRICKS) ),
    ALLamp_Red_Nether_Bricks("al_lamp_red_nether_bricks", () -> new ALLamp(Blocks.RED_NETHER_BRICKS) ),
    ALLamp_BlackStone("al_lamp_blackstone", () -> new ALLamp(Blocks.BLACKSTONE) ),
    ALLamp_CrimsonPlanks("al_lamp_crimson_planks", () -> new ALLamp(Blocks.CRIMSON_PLANKS) ),
    ALLamp_WarpedPlanks("al_lamp_warped_planks", () -> new ALLamp(Blocks.WARPED_PLANKS) ),

    ALTorch_Acacia("al_torch_acacia_planks", () -> new ALTorch( Blocks.ACACIA_PLANKS ) ),
    ALTorch_Birch("al_torch_birch_planks", () -> new ALTorch( Blocks.BIRCH_PLANKS ) ),
    ALTorch_Oak("al_torch_oak_planks", () -> new ALTorch( Blocks.OAK_PLANKS ) ),
    ALTorch_Dark_Oak("al_torch_dark_oak_planks", () -> new ALTorch( Blocks.DARK_OAK_PLANKS ) ),
    ALTorch_Jungle("al_torch_jungle_planks", () -> new ALTorch( Blocks.JUNGLE_PLANKS ) ),
    ALTorch_Spruce("al_torch_spruce_planks", () -> new ALTorch( Blocks.SPRUCE_PLANKS ) ),
    ALTorch_Stone("al_torch_stone", () -> new ALTorch( Blocks.STONE ) ),
    ALTorch_CobbleStone("al_torch_cobblestone", () -> new ALTorch( Blocks.COBBLESTONE ) ),
    ALTorch_Mossy_CobbleStone("al_torch_mossy_cobblestone", () -> new ALTorch( Blocks.MOSSY_COBBLESTONE ) ),
    ALTorch_End_Stone("al_torch_end_stone", () -> new ALTorch( Blocks.END_STONE ) ),
    ALTorch_Sand_Stone("al_torch_sandstone", () -> new ALTorch( Blocks.SANDSTONE ) ),
    ALTorch_Stone_Bricks("al_torch_stone_bricks", () -> new ALTorch( Blocks.STONE_BRICKS ) ),
    ALTorch_Mossy_Stone_Bricks("al_torch_mossy_stone_bricks", () -> new ALTorch( Blocks.MOSSY_STONE_BRICKS ) ),
    ALTorch_End_Stone_Bricks("al_torch_end_stone_bricks", () -> new ALTorch( Blocks.END_STONE_BRICKS ) ),
    ALTorch_Nether_Bricks("al_torch_nether_bricks", () -> new ALTorch( Blocks.NETHER_BRICKS ) ),
    ALTorch_Red_Nether_Bricks("al_torch_red_nether_bricks", () -> new ALTorch( Blocks.RED_NETHER_BRICKS ) ),
    ALTorch_Smooth_Stone("al_torch_smooth_stone", () -> new ALTorch( Blocks.SMOOTH_STONE ) ),
    ALTorch_Glass("al_torch_glass", () -> new ALTorch( Blocks.GLASS )),
    ALTorch_Iron("al_torch_iron_block", () -> new ALTorch( Blocks.IRON_BLOCK )),
    ALTorch_Gold("al_torch_gold_block", () -> new ALTorch( Blocks.GOLD_BLOCK )),
    ALTorch_Diamond("al_torch_diamond_block", () -> new ALTorch( Blocks.DIAMOND_BLOCK )),
    ALTorch_Ice("al_torch_packed_ice", () -> new ALTorch( Blocks.PACKED_ICE )),
    ALTorch_Pink_Wool("al_torch_pink_wool", () -> new ALTorch( Blocks.PINK_WOOL )),
    ALTorch_Magenta_Wool("al_torch_magenta_wool", () -> new ALTorch( Blocks.MAGENTA_WOOL )),
    ALTorch_Crimson("al_torch_crimson_planks", () -> new ALTorch( Blocks.CRIMSON_PLANKS )),
    ALTorch_Warped("al_torch_warped_planks", () -> new ALTorch( Blocks.WARPED_PLANKS )),
    ALTorch_BlackStone("al_torch_blackstone", () -> new ALTorch( Blocks.BLACKSTONE )),
    
    ALTorch_Wall_Acacia("al_wall_torch_acacia_planks", () -> new ALTorch_Wall( Blocks.ACACIA_PLANKS, ALTorch_Acacia ) ),
    ALTorch_Wall_Birch("al_wall_torch_birch_planks", () -> new ALTorch_Wall( Blocks.BIRCH_PLANKS, ALTorch_Birch ) ),
    ALTorch_Wall_Oak("al_wall_torch_oak_planks", () -> new ALTorch_Wall( Blocks.OAK_PLANKS, ALTorch_Oak ) ),
    ALTorch_Wall_Dark_Oak("al_wall_torch_dark_oak_planks", () -> new ALTorch_Wall( Blocks.DARK_OAK_PLANKS, ALTorch_Dark_Oak ) ),
    ALTorch_Wall_Jungle("al_wall_torch_jungle_planks", () -> new ALTorch_Wall( Blocks.JUNGLE_PLANKS, ALTorch_Jungle ) ),
    ALTorch_Wall_Spruce("al_wall_torch_spruce_planks", () -> new ALTorch_Wall( Blocks.SPRUCE_PLANKS, ALTorch_Spruce ) ),
    ALTorch_Wall_Stone("al_wall_torch_stone", () -> new ALTorch_Wall( Blocks.STONE, ALTorch_Stone ) ),
    ALTorch_Wall_CobbleStone("al_wall_torch_cobblestone", () -> new ALTorch_Wall( Blocks.COBBLESTONE, ALTorch_CobbleStone ) ),
    ALTorch_Wall_Mossy_CobbleStone("al_wall_torch_mossy_cobblestone", () -> new ALTorch_Wall( Blocks.MOSSY_COBBLESTONE, ALTorch_Mossy_CobbleStone ) ),
    ALTorch_Wall_End_Stone("al_wall_torch_end_stone", () -> new ALTorch_Wall( Blocks.END_STONE, ALTorch_End_Stone ) ),
    ALTorch_Wall_Sand_Stone("al_wall_torch_sandstone", () -> new ALTorch_Wall( Blocks.SANDSTONE, ALTorch_Sand_Stone ) ),
    ALTorch_Wall_Stone_Bricks("al_wall_torch_stone_bricks", () -> new ALTorch_Wall( Blocks.STONE_BRICKS, ALTorch_Stone_Bricks ) ),
    ALTorch_Wall_Mossy_Stone_Bricks("al_wall_torch_mossy_stone_bricks", () -> new ALTorch_Wall( Blocks.MOSSY_STONE_BRICKS, ALTorch_Mossy_Stone_Bricks ) ),
    ALTorch_Wall_End_Stone_Bricks("al_wall_torch_end_stone_bricks", () -> new ALTorch_Wall( Blocks.END_STONE_BRICKS, ALTorch_End_Stone_Bricks ) ),
    ALTorch_Wall_Nether_Bricks("al_wall_torch_nether_bricks", () -> new ALTorch_Wall( Blocks.NETHER_BRICKS, ALTorch_Nether_Bricks ) ),
    ALTorch_Wall_Red_Nether_Bricks("al_wall_torch_red_nether_bricks", () -> new ALTorch_Wall( Blocks.RED_NETHER_BRICKS, ALTorch_Red_Nether_Bricks ) ),
    ALTorch_Wall_Smooth_Stone("al_wall_torch_smooth_stone", () -> new ALTorch_Wall( Blocks.SMOOTH_STONE, ALTorch_Smooth_Stone ) ),
    ALTorch_Wall_Glass("al_wall_torch_glass", () -> new ALTorch_Wall( Blocks.GLASS, ALTorch_Glass ) ),
    ALTorch_Wall_Iron("al_wall_torch_iron_block", () -> new ALTorch_Wall( Blocks.IRON_BLOCK, ALTorch_Iron ) ),
    ALTorch_Wall_Gold("al_wall_torch_gold_block", () -> new ALTorch_Wall( Blocks.GOLD_BLOCK, ALTorch_Gold ) ),
    ALTorch_Wall_Diamond("al_wall_torch_diamond_block", () -> new ALTorch_Wall( Blocks.DIAMOND_BLOCK, ALTorch_Diamond ) ),
    ALTorch_Wall_Ice("al_wall_torch_packed_ice", () -> new ALTorch_Wall( Blocks.PACKED_ICE, ALTorch_Ice ) ),
    ALTorch_Wall_Pink_Wool("al_wall_torch_pink_wool", () -> new ALTorch_Wall( Blocks.PINK_WOOL, ALTorch_Pink_Wool ) ),
    ALTorch_Wall_Magenta_Wool("al_wall_torch_magenta_wool", () -> new ALTorch_Wall( Blocks.MAGENTA_WOOL, ALTorch_Magenta_Wool ) ),
    ALTorch_Wall_Crimson("al_wall_torch_crimson_planks", () -> new ALTorch_Wall( Blocks.CRIMSON_PLANKS, ALTorch_Crimson ) ),
    ALTorch_Wall_Warped("al_wall_torch_warped_planks", () -> new ALTorch_Wall( Blocks.WARPED_PLANKS, ALTorch_Warped ) ),
    ALTorch_Wall_BlackStone("al_wall_torch_blackstone", () -> new ALTorch_Wall( Blocks.BLACKSTONE, ALTorch_BlackStone ) ),

    StandingTorch_S_Stone_Bricks("standing_torch_s_stone_bricks", () -> new StandingTorch_S( Blocks.STONE_BRICKS ) ),
    StandingTorch_S_Mossy_Stone_Bricks("standing_torch_s_mossy_stone_bricks", () -> new StandingTorch_S( Blocks.MOSSY_STONE_BRICKS ) ),
    StandingTorch_S_End_Stone_Bricks("standing_torch_s_end_stone_bricks", () -> new StandingTorch_S( Blocks.END_STONE_BRICKS ) ),
    StandingTorch_S_Nether_Bricks("standing_torch_s_nether_bricks", () -> new StandingTorch_S( Blocks.NETHER_BRICKS ) ),
    StandingTorch_S_Red_Nether_Bricks("standing_torch_s_red_nether_bricks", () -> new StandingTorch_S( Blocks.RED_NETHER_BRICKS ) ),
    StandingTorch_S_Smooth_Stone("standing_torch_s_smooth_stone", () -> new StandingTorch_S( Blocks.SMOOTH_STONE ) ),
    StandingTorch_S_Polished_Andesite("standing_torch_s_polished_andesite", () -> new StandingTorch_S( Blocks.POLISHED_ANDESITE ) ),
    StandingTorch_S_Polished_Diorite("standing_torch_s_polished_diorite", () -> new StandingTorch_S( Blocks.POLISHED_DIORITE ) ),
    StandingTorch_S_Polished_Granite("standing_torch_s_polished_granite", () -> new StandingTorch_S( Blocks.POLISHED_GRANITE ) ),
    StandingTorch_S_Stone("standing_torch_s_stone", () -> new StandingTorch_S( Blocks.STONE ) ),
    StandingTorch_S_CobbleStone("standing_torch_s_cobblestone", () -> new StandingTorch_S( Blocks.COBBLESTONE ) ),
    StandingTorch_S_Mossy_CobbleStone("standing_torch_s_mossy_cobblestone", () -> new StandingTorch_S( Blocks.MOSSY_COBBLESTONE ) ),
    StandingTorch_S_Sand_Stone("standing_torch_s_sandstone", () -> new StandingTorch_S( Blocks.SANDSTONE ) ),
    StandingTorch_S_Cut_Sand_Stone("standing_torch_s_cut_sandstone", () -> new StandingTorch_S( Blocks.CUT_SANDSTONE ) ),
    StandingTorch_S_End_Stone("standing_torch_s_end_stone", () -> new StandingTorch_S( Blocks.END_STONE ) ),
    StandingTorch_S_Iron("standing_torch_s_iron_block", () -> new StandingTorch_S( Blocks.IRON_BLOCK ) ),
    StandingTorch_S_Gold("standing_torch_s_gold_block", () -> new StandingTorch_S( Blocks.GOLD_BLOCK ) ),
    StandingTorch_S_Diamond("standing_torch_s_diamond_block", () -> new StandingTorch_S( Blocks.DIAMOND_BLOCK ) ),
    StandingTorch_S_Packed_Ice("standing_torch_s_packed_ice", () -> new StandingTorch_S( Blocks.PACKED_ICE ) ),
    StandingTorch_S_Pink_Wool("standing_torch_s_pink_wool", () -> new StandingTorch_S( Blocks.PINK_WOOL ) ),
    StandingTorch_S_Magenta_Wool("standing_torch_s_magenta_wool", () -> new StandingTorch_S( Blocks.MAGENTA_WOOL ) ),
    StandingTorch_S_Polished_BlackStone("standing_torch_s_polished_blackstone", () -> new StandingTorch_S( Blocks.POLISHED_BLACKSTONE ) ),
    
    StandingTorch_L_Stone_Bricks("standing_torch_l_stone_bricks", () -> new StandingTorch_L( Blocks.STONE_BRICKS ) ),
    StandingTorch_L_Mossy_Stone_Bricks("standing_torch_l_mossy_stone_bricks", () -> new StandingTorch_L( Blocks.MOSSY_STONE_BRICKS ) ),
    StandingTorch_L_End_Stone_Bricks("standing_torch_l_end_stone_bricks", () -> new StandingTorch_L( Blocks.END_STONE_BRICKS ) ),
    StandingTorch_L_Nether_Bricks("standing_torch_l_nether_bricks", () -> new StandingTorch_L( Blocks.NETHER_BRICKS ) ),
    StandingTorch_L_Red_Nether_Bricks("standing_torch_l_red_nether_bricks", () -> new StandingTorch_L( Blocks.RED_NETHER_BRICKS ) ),
    StandingTorch_L_Smooth_Stone("standing_torch_l_smooth_stone", () -> new StandingTorch_L( Blocks.SMOOTH_STONE ) ),
    StandingTorch_L_Polished_Andesite("standing_torch_l_polished_andesite", () -> new StandingTorch_L( Blocks.POLISHED_ANDESITE ) ),
    StandingTorch_L_Polished_Diorite("standing_torch_l_polished_diorite", () -> new StandingTorch_L( Blocks.POLISHED_DIORITE ) ),
    StandingTorch_L_Polished_Granite("standing_torch_l_polished_granite", () -> new StandingTorch_L( Blocks.POLISHED_GRANITE ) ),
    StandingTorch_L_Stone("standing_torch_l_stone", () -> new StandingTorch_L( Blocks.STONE ) ),
    StandingTorch_L_CobbleStone("standing_torch_l_cobblestone", () -> new StandingTorch_L( Blocks.COBBLESTONE ) ),
    StandingTorch_L_Mossy_CobbleStone("standing_torch_l_mossy_cobblestone", () -> new StandingTorch_L( Blocks.MOSSY_COBBLESTONE ) ),
    StandingTorch_L_Sand_Stone("standing_torch_l_sandstone", () -> new StandingTorch_L( Blocks.SANDSTONE ) ),
    StandingTorch_L_Cut_Sand_Stone("standing_torch_l_cut_sandstone", () -> new StandingTorch_L( Blocks.CUT_SANDSTONE ) ),
    StandingTorch_L_End_Stone("standing_torch_l_end_stone", () -> new StandingTorch_L( Blocks.END_STONE ) ),
    StandingTorch_L_Iron("standing_torch_l_iron_block", () -> new StandingTorch_L( Blocks.IRON_BLOCK ) ),
    StandingTorch_L_Gold("standing_torch_l_gold_block", () -> new StandingTorch_L( Blocks.GOLD_BLOCK ) ),
    StandingTorch_L_Diamond("standing_torch_l_diamond_block", () -> new StandingTorch_L( Blocks.DIAMOND_BLOCK ) ),
    StandingTorch_L_Packed_Ice("standing_torch_l_packed_ice", () -> new StandingTorch_L( Blocks.PACKED_ICE ) ),
    StandingTorch_L_Pink_Wool("standing_torch_l_pink_wool", () -> new StandingTorch_L( Blocks.PINK_WOOL ) ),
    StandingTorch_L_Magenta_Wool("standing_torch_l_magenta_wool", () -> new StandingTorch_L( Blocks.MAGENTA_WOOL ) ),
    StandingTorch_L_Polished_BlackStone("standing_torch_l_polished_blackstone", () -> new StandingTorch_L( Blocks.POLISHED_BLACKSTONE ) ),

    FirePit_S_Stone_Bricks("fire_pit_s_stone_bricks", () -> new FirePit_S( Blocks.STONE_BRICKS ) ),
    FirePit_S_Mossy_Stone_Bricks("fire_pit_s_mossy_stone_bricks", () -> new FirePit_S( Blocks.MOSSY_STONE_BRICKS ) ),
    FirePit_S_End_Stone_Bricks("fire_pit_s_end_stone_bricks", () -> new FirePit_S( Blocks.END_STONE_BRICKS ) ),
    FirePit_S_Nether_Bricks("fire_pit_s_nether_bricks", () -> new FirePit_S( Blocks.NETHER_BRICKS ) ),
    FirePit_S_Red_Nether_Bricks("fire_pit_s_red_nether_bricks", () -> new FirePit_S( Blocks.RED_NETHER_BRICKS ) ),
    FirePit_S_Smooth_Stone("fire_pit_s_smooth_stone", () -> new FirePit_S( Blocks.SMOOTH_STONE ) ),
    FirePit_S_Polished_Andesite("fire_pit_s_polished_andesite", () -> new FirePit_S( Blocks.POLISHED_ANDESITE ) ),
    FirePit_S_Polished_Diorite("fire_pit_s_polished_diorite", () -> new FirePit_S( Blocks.POLISHED_DIORITE ) ),
    FirePit_S_Polished_Granite("fire_pit_s_polished_granite", () -> new FirePit_S( Blocks.POLISHED_GRANITE ) ),
    FirePit_S_Stone("fire_pit_s_stone", () -> new FirePit_S( Blocks.STONE ) ),
    FirePit_S_CobbleStone("fire_pit_s_cobblestone", () -> new FirePit_S( Blocks.COBBLESTONE ) ),
    FirePit_S_Mossy_CobbleStone("fire_pit_s_mossy_cobblestone", () -> new FirePit_S( Blocks.MOSSY_COBBLESTONE ) ),
    FirePit_S_Sand_Stone("fire_pit_s_sandstone", () -> new FirePit_S( Blocks.SANDSTONE ) ),
    FirePit_S_Cut_Sand_Stone("fire_pit_s_cut_sandstone", () -> new FirePit_S( Blocks.CUT_SANDSTONE ) ),
    FirePit_S_End_Stone("fire_pit_s_end_stone", () -> new FirePit_S( Blocks.END_STONE ) ),
    FirePit_S_Iron("fire_pit_s_iron_block", () -> new FirePit_S( Blocks.IRON_BLOCK ) ),
    FirePit_S_Gold("fire_pit_s_gold_block", () -> new FirePit_S( Blocks.GOLD_BLOCK ) ),
    FirePit_S_Diamond("fire_pit_s_diamond_block", () -> new FirePit_S( Blocks.DIAMOND_BLOCK ) ),
    FirePit_S_Ice("fire_pit_s_packed_ice", () -> new FirePit_S( Blocks.PACKED_ICE ) ),
    FirePit_S_Pink_Wool("fire_pit_s_pink_wool", () -> new FirePit_S( Blocks.PINK_WOOL ) ),
    FirePit_S_Magenta_Wool("fire_pit_s_magenta_wool", () -> new FirePit_S( Blocks.MAGENTA_WOOL ) ),
    FirePit_S_Polished_BlackStone("fire_pit_s_polished_blackstone", () -> new FirePit_S( Blocks.POLISHED_BLACKSTONE ) ),

    FirePit_L_Stone_Bricks("fire_pit_l_stone_bricks", () -> new FirePit_L( Blocks.STONE_BRICKS ) ),
    FirePit_L_Mossy_Stone_Bricks("fire_pit_l_mossy_stone_bricks", () -> new FirePit_L( Blocks.MOSSY_STONE_BRICKS ) ),
    FirePit_L_End_Stone_Bricks("fire_pit_l_end_stone_bricks", () -> new FirePit_L( Blocks.END_STONE_BRICKS ) ),
    FirePit_L_Nether_Bricks("fire_pit_l_nether_bricks", () -> new FirePit_L( Blocks.NETHER_BRICKS ) ),
    FirePit_L_Red_Nether_Bricks("fire_pit_l_red_nether_bricks", () -> new FirePit_L( Blocks.RED_NETHER_BRICKS ) ),
    FirePit_L_Smooth_Stone("fire_pit_l_smooth_stone", () -> new FirePit_L( Blocks.SMOOTH_STONE ) ),
    FirePit_L_Polished_Andesite("fire_pit_l_polished_andesite", () -> new FirePit_L( Blocks.POLISHED_ANDESITE ) ),
    FirePit_L_Polished_Diorite("fire_pit_l_polished_diorite", () -> new FirePit_L( Blocks.POLISHED_DIORITE ) ),
    FirePit_L_Polished_Granite("fire_pit_l_polished_granite", () -> new FirePit_L( Blocks.POLISHED_GRANITE ) ),
    FirePit_L_Stone("fire_pit_l_stone", () -> new FirePit_L( Blocks.STONE ) ),
    FirePit_L_CobbleStone("fire_pit_l_cobblestone", () -> new FirePit_L( Blocks.COBBLESTONE ) ),
    FirePit_L_Mossy_CobbleStone("fire_pit_l_mossy_cobblestone", () -> new FirePit_L( Blocks.MOSSY_COBBLESTONE ) ),
    FirePit_L_Sand_Stone("fire_pit_l_sandstone", () -> new FirePit_L( Blocks.SANDSTONE ) ),
    FirePit_L_Cut_Sand_Stone("fire_pit_l_cut_sandstone", () -> new FirePit_L( Blocks.CUT_SANDSTONE ) ),
    FirePit_L_End_Stone("fire_pit_l_end_stone", () -> new FirePit_L( Blocks.END_STONE ) ),
    FirePit_L_Iron("fire_pit_l_iron_block", () -> new FirePit_L( Blocks.IRON_BLOCK ) ),
    FirePit_L_Gold("fire_pit_l_gold_block", () -> new FirePit_L( Blocks.GOLD_BLOCK ) ),
    FirePit_L_Diamond("fire_pit_l_diamond_block", () -> new FirePit_L( Blocks.DIAMOND_BLOCK ) ),
    FirePit_L_Ice("fire_pit_l_packed_ice", () -> new FirePit_L( Blocks.PACKED_ICE ) ),
    FirePit_L_Pink_Wool("fire_pit_l_pink_wool", () -> new FirePit_L( Blocks.PINK_WOOL ) ),
    FirePit_L_Magenta_Wool("fire_pit_l_magenta_wool", () -> new FirePit_L( Blocks.MAGENTA_WOOL ) ),
    FirePit_L_Polished_BlackStone("fire_pit_l_polished_blackstone", () -> new FirePit_L( Blocks.POLISHED_BLACKSTONE ) ),

    Fire_For_StandingTorch_S("fire_for_standing_torch_s", () -> new Fire( PedestalTypes.standing_torch_s ) ),
    Fire_For_StandingTorch_L( "fire_for_standing_torch_l", () -> new Fire( PedestalTypes.standing_torch_l ) ),
    Fire_For_FirePit_S("fire_for_fire_pit_s", () -> new Fire( PedestalTypes.fire_pit_s ) ),
    Fire_For_FirePit_L("fire_for_fire_pit_l", () -> new Fire( PedestalTypes.fire_pit_l )),
    
    SoulFire_For_StandingTorch_S("soul_fire_for_standing_torch_s", () -> new Fire_Soul( PedestalTypes.standing_torch_s )),
    SoulFire_For_StandingTorch_L("soul_fire_for_standing_torch_l", () -> new Fire_Soul( PedestalTypes.standing_torch_l ) ),
    SoulFire_For_FirePit_S("soul_fire_for_fire_pit_s", () -> new Fire_Soul( PedestalTypes.fire_pit_s )),
    SoulFire_For_FirePit_L("soul_fire_for_fire_pit_l", () -> new Fire_Soul( PedestalTypes.fire_pit_l ));

    private final String name;
    private Supplier<? extends Block> sup;

    <I> ModBlockList(String name, Supplier<? extends Block> sup) {
        this.name = name;
        this.sup = sup;
    }

    public String getRegName() {
        return name;
    }

    public Block getBlock() {
        return AdditionalLights.getBlock( this );
    }

    public Item getBlockItem() {
        return AdditionalLights.getBlockItem( this );
    }

    public void init() {
        ((IModBlock)getBlock()).setMyKey(this) ;
    }

    public void register(){
        AdditionalLights.modBlocks.put( this, AdditionalLights.BLOCKS.register( getRegName(), () -> sup.get() ) );

        if( name.contains("al_wall_torch") )
            return;

        if( name.contains("al_torch") )
        {
            var wallkey = ModBlockList.valueOf( this.name().replace("ALTorch", "ALTorch_Wall") );
            AdditionalLights.modBlockItems.put( this, AdditionalLights.ITEMS.register( getRegName(), () -> new StandingAndWallBlockItem( getBlock(), wallkey.getBlock() , new Item.Properties(), Direction.DOWN )));
        }
        else
            AdditionalLights.modBlockItems.put( this, AdditionalLights.ITEMS.register( getRegName(), () -> new BlockItem( getBlock(), new Item.Properties())));
    }
}
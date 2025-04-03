package com.mgen256.al;

import java.util.function.Function;

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
import com.mgen256.al.items.PedestalBlockItem;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.core.Direction;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.StandingAndWallBlockItem;

public enum ModBlockList {

    ALLamp_Acacia(
        "al_lamp_acacia_planks",
        regName -> new ALLamp(Blocks.ACACIA_PLANKS, regName)
    ),
    ALLamp_Birch(
        "al_lamp_birch_planks",
        regName -> new ALLamp(Blocks.BIRCH_PLANKS, regName)
    ),
    ALLamp_Oak(
        "al_lamp_oak_planks",
        regName -> new ALLamp(Blocks.OAK_PLANKS, regName)
    ),
    ALLamp_Dark_Oak(
        "al_lamp_dark_oak_planks",
        regName -> new ALLamp(Blocks.DARK_OAK_PLANKS, regName)
    ),
    ALLamp_Spruce(
        "al_lamp_spruce_planks",
        regName -> new ALLamp(Blocks.SPRUCE_PLANKS, regName)
    ),
    ALLamp_Jungle(
        "al_lamp_jungle_planks",
        regName -> new ALLamp(Blocks.JUNGLE_PLANKS, regName)
    ),
    ALLamp_Stone(
        "al_lamp_stone",
        regName -> new ALLamp(Blocks.STONE, regName)
    ),
    ALLamp_CobbleStone(
        "al_lamp_cobblestone",
        regName -> new ALLamp(Blocks.COBBLESTONE, regName)
    ),
    ALLamp_Mossy_CobbleStone(
        "al_lamp_mossy_cobblestone",
        regName -> new ALLamp(Blocks.MOSSY_COBBLESTONE, regName)
    ),
    ALLamp_End_Stone(
        "al_lamp_end_stone",
        regName -> new ALLamp(Blocks.END_STONE, regName)
    ),
    ALLamp_Sand_Stone(
        "al_lamp_sandstone",
        regName -> new ALLamp(Blocks.SANDSTONE, regName)
    ),
    ALLamp_Glass(
        "al_lamp_glass",
        regName -> new ALLamp(Blocks.GLASS, regName)
    ),
    ALLamp_Iron(
        "al_lamp_iron_block",
        regName -> new ALLamp(Blocks.IRON_BLOCK, regName)
    ),
    ALLamp_Gold(
        "al_lamp_gold_block",
        regName -> new ALLamp(Blocks.GOLD_BLOCK, regName)
    ),
    ALLamp_Diamond(
        "al_lamp_diamond_block",
        regName -> new ALLamp(Blocks.DIAMOND_BLOCK, regName)
    ),
    ALLamp_Ice(
        "al_lamp_packed_ice",
        regName -> new ALLamp(Blocks.PACKED_ICE, regName)
    ),
    ALLamp_Pink_Wool(
        "al_lamp_pink_wool",
        regName -> new ALLamp(Blocks.PINK_WOOL, regName)
    ),
    ALLamp_Magenta_Wool(
        "al_lamp_magenta_wool",
        regName -> new ALLamp(Blocks.MAGENTA_WOOL, regName)
    ),
    ALLamp_Nether_Bricks(
        "al_lamp_nether_bricks",
        regName -> new ALLamp(Blocks.NETHER_BRICKS, regName)
    ),
    ALLamp_Red_Nether_Bricks(
        "al_lamp_red_nether_bricks",
        regName -> new ALLamp(Blocks.RED_NETHER_BRICKS, regName)
    ),
    ALLamp_BlackStone(
        "al_lamp_blackstone",
        regName -> new ALLamp(Blocks.BLACKSTONE, regName)
    ),
    ALLamp_CrimsonPlanks(
        "al_lamp_crimson_planks",
        regName -> new ALLamp(Blocks.CRIMSON_PLANKS, regName)
    ),
    ALLamp_WarpedPlanks(
        "al_lamp_warped_planks",
        regName -> new ALLamp(Blocks.WARPED_PLANKS, regName)
    ),

    ALTorch_Acacia(
        "al_torch_acacia_planks",
        regName -> new ALTorch(Blocks.ACACIA_PLANKS, regName)
    ),
    ALTorch_Birch(
        "al_torch_birch_planks",
        regName -> new ALTorch(Blocks.BIRCH_PLANKS, regName)
    ),
    ALTorch_Oak(
        "al_torch_oak_planks",
        regName -> new ALTorch(Blocks.OAK_PLANKS, regName)
    ),
    ALTorch_Dark_Oak(
        "al_torch_dark_oak_planks",
        regName -> new ALTorch(Blocks.DARK_OAK_PLANKS, regName)
    ),
    ALTorch_Jungle(
        "al_torch_jungle_planks",
        regName -> new ALTorch(Blocks.JUNGLE_PLANKS, regName)
    ),
    ALTorch_Spruce(
        "al_torch_spruce_planks",
        regName -> new ALTorch(Blocks.SPRUCE_PLANKS, regName)
    ),
    ALTorch_Stone(
        "al_torch_stone",
        regName -> new ALTorch(Blocks.STONE, regName)
    ),
    ALTorch_CobbleStone(
        "al_torch_cobblestone",
        regName -> new ALTorch(Blocks.COBBLESTONE, regName)
    ),
    ALTorch_Mossy_CobbleStone(
        "al_torch_mossy_cobblestone",
        regName -> new ALTorch(Blocks.MOSSY_COBBLESTONE, regName)
    ),
    ALTorch_End_Stone(
        "al_torch_end_stone",
        regName -> new ALTorch(Blocks.END_STONE, regName)
    ),
    ALTorch_Sand_Stone(
        "al_torch_sandstone",
        regName -> new ALTorch(Blocks.SANDSTONE, regName)
    ),
    ALTorch_Stone_Bricks(
        "al_torch_stone_bricks",
        regName -> new ALTorch(Blocks.STONE_BRICKS, regName)
    ),
    ALTorch_Mossy_Stone_Bricks(
        "al_torch_mossy_stone_bricks",
        regName -> new ALTorch(Blocks.MOSSY_STONE_BRICKS, regName)
    ),
    ALTorch_End_Stone_Bricks(
        "al_torch_end_stone_bricks",
        regName -> new ALTorch(Blocks.END_STONE_BRICKS, regName)
    ),
    ALTorch_Nether_Bricks(
        "al_torch_nether_bricks",
        regName -> new ALTorch(Blocks.NETHER_BRICKS, regName)
    ),
    ALTorch_Red_Nether_Bricks(
        "al_torch_red_nether_bricks",
        regName -> new ALTorch(Blocks.RED_NETHER_BRICKS, regName)
    ),
    ALTorch_Smooth_Stone(
        "al_torch_smooth_stone",
        regName -> new ALTorch(Blocks.SMOOTH_STONE, regName)
    ),
    ALTorch_Glass(
        "al_torch_glass",
        regName -> new ALTorch(Blocks.GLASS, regName)
    ),
    ALTorch_Iron(
        "al_torch_iron_block",
        regName -> new ALTorch(Blocks.IRON_BLOCK, regName)
    ),
    ALTorch_Gold(
        "al_torch_gold_block",
        regName -> new ALTorch(Blocks.GOLD_BLOCK, regName)
    ),
    ALTorch_Diamond(
        "al_torch_diamond_block",
        regName -> new ALTorch(Blocks.DIAMOND_BLOCK, regName)
    ),
    ALTorch_Ice(
        "al_torch_packed_ice",
        regName -> new ALTorch(Blocks.PACKED_ICE, regName)
    ),
    ALTorch_Pink_Wool(
        "al_torch_pink_wool",
        regName -> new ALTorch(Blocks.PINK_WOOL, regName)
    ),
    ALTorch_Magenta_Wool(
        "al_torch_magenta_wool",
        regName -> new ALTorch(Blocks.MAGENTA_WOOL, regName)
    ),
    ALTorch_Crimson(
        "al_torch_crimson_planks",
        regName -> new ALTorch(Blocks.CRIMSON_PLANKS, regName)
    ),
    ALTorch_Warped(
        "al_torch_warped_planks",
        regName -> new ALTorch(Blocks.WARPED_PLANKS, regName)
    ),
    ALTorch_BlackStone(
        "al_torch_blackstone",
        regName -> new ALTorch(Blocks.BLACKSTONE, regName)
    ),

    ALTorch_Wall_Acacia(
        "al_wall_torch_acacia_planks",
        regName -> new ALTorch_Wall(Blocks.ACACIA_PLANKS, ALTorch_Acacia, regName)
    ),
    ALTorch_Wall_Birch(
        "al_wall_torch_birch_planks",
        regName -> new ALTorch_Wall(Blocks.BIRCH_PLANKS, ALTorch_Birch, regName)
    ),
    ALTorch_Wall_Oak(
        "al_wall_torch_oak_planks",
        regName -> new ALTorch_Wall(Blocks.OAK_PLANKS, ALTorch_Oak, regName)
    ),
    ALTorch_Wall_Dark_Oak(
        "al_wall_torch_dark_oak_planks",
        regName -> new ALTorch_Wall(Blocks.DARK_OAK_PLANKS, ALTorch_Dark_Oak, regName)
    ),
    ALTorch_Wall_Jungle(
        "al_wall_torch_jungle_planks",
        regName -> new ALTorch_Wall(Blocks.JUNGLE_PLANKS, ALTorch_Jungle, regName)
    ),
    ALTorch_Wall_Spruce(
        "al_wall_torch_spruce_planks",
        regName -> new ALTorch_Wall(Blocks.SPRUCE_PLANKS, ALTorch_Spruce, regName)
    ),
    ALTorch_Wall_Stone(
        "al_wall_torch_stone",
        regName -> new ALTorch_Wall(Blocks.STONE, ALTorch_Stone, regName)
    ),
    ALTorch_Wall_CobbleStone(
        "al_wall_torch_cobblestone",
        regName -> new ALTorch_Wall(Blocks.COBBLESTONE, ALTorch_CobbleStone, regName)
    ),
    ALTorch_Wall_Mossy_CobbleStone(
        "al_wall_torch_mossy_cobblestone",
        regName -> new ALTorch_Wall(Blocks.MOSSY_COBBLESTONE, ALTorch_Mossy_CobbleStone, regName)
    ),
    ALTorch_Wall_End_Stone(
        "al_wall_torch_end_stone",
        regName -> new ALTorch_Wall(Blocks.END_STONE, ALTorch_End_Stone, regName)
    ),
    ALTorch_Wall_Sand_Stone(
        "al_wall_torch_sandstone",
        regName -> new ALTorch_Wall(Blocks.SANDSTONE, ALTorch_Sand_Stone, regName)
    ),
    ALTorch_Wall_Stone_Bricks(
        "al_wall_torch_stone_bricks",
        regName -> new ALTorch_Wall(Blocks.STONE_BRICKS, ALTorch_Stone_Bricks, regName)
    ),
    ALTorch_Wall_Mossy_Stone_Bricks(
        "al_wall_torch_mossy_stone_bricks",
        regName -> new ALTorch_Wall(Blocks.MOSSY_STONE_BRICKS, ALTorch_Mossy_Stone_Bricks, regName)
    ),
    ALTorch_Wall_End_Stone_Bricks(
        "al_wall_torch_end_stone_bricks",
        regName -> new ALTorch_Wall(Blocks.END_STONE_BRICKS, ALTorch_End_Stone_Bricks, regName)
    ),
    ALTorch_Wall_Nether_Bricks(
        "al_wall_torch_nether_bricks",
        regName -> new ALTorch_Wall(Blocks.NETHER_BRICKS, ALTorch_Nether_Bricks, regName)
    ),
    ALTorch_Wall_Red_Nether_Bricks(
        "al_wall_torch_red_nether_bricks",
        regName -> new ALTorch_Wall(Blocks.RED_NETHER_BRICKS, ALTorch_Red_Nether_Bricks, regName)
    ),
    ALTorch_Wall_Smooth_Stone(
        "al_wall_torch_smooth_stone",
        regName -> new ALTorch_Wall(Blocks.SMOOTH_STONE, ALTorch_Smooth_Stone, regName)
    ),
    ALTorch_Wall_Glass(
        "al_wall_torch_glass",
        regName -> new ALTorch_Wall(Blocks.GLASS, ALTorch_Glass, regName)
    ),
    ALTorch_Wall_Iron(
        "al_wall_torch_iron_block",
        regName -> new ALTorch_Wall(Blocks.IRON_BLOCK, ALTorch_Iron, regName)
    ),
    ALTorch_Wall_Gold(
        "al_wall_torch_gold_block",
        regName -> new ALTorch_Wall(Blocks.GOLD_BLOCK, ALTorch_Gold, regName)
    ),
    ALTorch_Wall_Diamond(
        "al_wall_torch_diamond_block",
        regName -> new ALTorch_Wall(Blocks.DIAMOND_BLOCK, ALTorch_Diamond, regName)
    ),
    ALTorch_Wall_Ice(
        "al_wall_torch_packed_ice",
        regName -> new ALTorch_Wall(Blocks.PACKED_ICE, ALTorch_Ice, regName)
    ),
    ALTorch_Wall_Pink_Wool(
        "al_wall_torch_pink_wool",
        regName -> new ALTorch_Wall(Blocks.PINK_WOOL, ALTorch_Pink_Wool, regName)
    ),
    ALTorch_Wall_Magenta_Wool(
        "al_wall_torch_magenta_wool",
        regName -> new ALTorch_Wall(Blocks.MAGENTA_WOOL, ALTorch_Magenta_Wool, regName)
    ),
    ALTorch_Wall_Crimson(
        "al_wall_torch_crimson_planks",
        regName -> new ALTorch_Wall(Blocks.CRIMSON_PLANKS, ALTorch_Crimson, regName)
    ),
    ALTorch_Wall_Warped(
        "al_wall_torch_warped_planks",
        regName -> new ALTorch_Wall(Blocks.WARPED_PLANKS, ALTorch_Warped, regName)
    ),
    ALTorch_Wall_BlackStone(
        "al_wall_torch_blackstone",
        regName -> new ALTorch_Wall(Blocks.BLACKSTONE, ALTorch_BlackStone, regName)
    ),

    StandingTorch_S_Stone_Bricks(
        "standing_torch_s_stone_bricks",
        regName -> new StandingTorch_S(Blocks.STONE_BRICKS, regName)
    ),
    StandingTorch_S_Mossy_Stone_Bricks(
        "standing_torch_s_mossy_stone_bricks",
        regName -> new StandingTorch_S(Blocks.MOSSY_STONE_BRICKS, regName)
    ),
    StandingTorch_S_End_Stone_Bricks(
        "standing_torch_s_end_stone_bricks",
        regName -> new StandingTorch_S(Blocks.END_STONE_BRICKS, regName)
    ),
    StandingTorch_S_Nether_Bricks(
        "standing_torch_s_nether_bricks",
        regName -> new StandingTorch_S(Blocks.NETHER_BRICKS, regName)
    ),
    StandingTorch_S_Red_Nether_Bricks(
        "standing_torch_s_red_nether_bricks",
        regName -> new StandingTorch_S(Blocks.RED_NETHER_BRICKS, regName)
    ),
    StandingTorch_S_Smooth_Stone(
        "standing_torch_s_smooth_stone",
        regName -> new StandingTorch_S(Blocks.SMOOTH_STONE, regName)
    ),
    StandingTorch_S_Polished_Andesite(
        "standing_torch_s_polished_andesite",
        regName -> new StandingTorch_S(Blocks.POLISHED_ANDESITE, regName)
    ),
    StandingTorch_S_Polished_Diorite(
        "standing_torch_s_polished_diorite",
        regName -> new StandingTorch_S(Blocks.POLISHED_DIORITE, regName)
    ),
    StandingTorch_S_Polished_Granite(
        "standing_torch_s_polished_granite",
        regName -> new StandingTorch_S(Blocks.POLISHED_GRANITE, regName)
    ),
    StandingTorch_S_Stone(
        "standing_torch_s_stone",
        regName -> new StandingTorch_S(Blocks.STONE, regName)
    ),
    StandingTorch_S_CobbleStone(
        "standing_torch_s_cobblestone",
        regName -> new StandingTorch_S(Blocks.COBBLESTONE, regName)
    ),
    StandingTorch_S_Mossy_CobbleStone(
        "standing_torch_s_mossy_cobblestone",
        regName -> new StandingTorch_S(Blocks.MOSSY_COBBLESTONE, regName)
    ),
    StandingTorch_S_Sand_Stone(
        "standing_torch_s_sandstone",
        regName -> new StandingTorch_S(Blocks.SANDSTONE, regName)
    ),
    StandingTorch_S_Cut_Sand_Stone(
        "standing_torch_s_cut_sandstone",
        regName -> new StandingTorch_S(Blocks.CUT_SANDSTONE, regName)
    ),
    StandingTorch_S_End_Stone(
        "standing_torch_s_end_stone",
        regName -> new StandingTorch_S(Blocks.END_STONE, regName)
    ),
    StandingTorch_S_Iron(
        "standing_torch_s_iron_block",
        regName -> new StandingTorch_S(Blocks.IRON_BLOCK, regName)
    ),
    StandingTorch_S_Gold(
        "standing_torch_s_gold_block",
        regName -> new StandingTorch_S(Blocks.GOLD_BLOCK, regName)
    ),
    StandingTorch_S_Diamond(
        "standing_torch_s_diamond_block",
        regName -> new StandingTorch_S(Blocks.DIAMOND_BLOCK, regName)
    ),
    StandingTorch_S_Packed_Ice(
        "standing_torch_s_packed_ice",
        regName -> new StandingTorch_S(Blocks.PACKED_ICE, regName)
    ),
    StandingTorch_S_Pink_Wool(
        "standing_torch_s_pink_wool",
        regName -> new StandingTorch_S(Blocks.PINK_WOOL, regName)
    ),
    StandingTorch_S_Magenta_Wool(
        "standing_torch_s_magenta_wool",
        regName -> new StandingTorch_S(Blocks.MAGENTA_WOOL, regName)
    ),
    StandingTorch_S_Polished_BlackStone(
        "standing_torch_s_polished_blackstone",
        regName -> new StandingTorch_S(Blocks.POLISHED_BLACKSTONE, regName)
    ),

    StandingTorch_L_Stone_Bricks(
        "standing_torch_l_stone_bricks",
        regName -> new StandingTorch_L(Blocks.STONE_BRICKS, regName)
    ),
    StandingTorch_L_Mossy_Stone_Bricks(
        "standing_torch_l_mossy_stone_bricks",
        regName -> new StandingTorch_L(Blocks.MOSSY_STONE_BRICKS, regName)
    ),
    StandingTorch_L_End_Stone_Bricks(
        "standing_torch_l_end_stone_bricks",
        regName -> new StandingTorch_L(Blocks.END_STONE_BRICKS, regName)
    ),
    StandingTorch_L_Nether_Bricks(
        "standing_torch_l_nether_bricks",
        regName -> new StandingTorch_L(Blocks.NETHER_BRICKS, regName)
    ),
    StandingTorch_L_Red_Nether_Bricks(
        "standing_torch_l_red_nether_bricks",
        regName -> new StandingTorch_L(Blocks.RED_NETHER_BRICKS, regName)
    ),
    StandingTorch_L_Smooth_Stone(
        "standing_torch_l_smooth_stone",
        regName -> new StandingTorch_L(Blocks.SMOOTH_STONE, regName)
    ),
    StandingTorch_L_Polished_Andesite(
        "standing_torch_l_polished_andesite",
        regName -> new StandingTorch_L(Blocks.POLISHED_ANDESITE, regName)
    ),
    StandingTorch_L_Polished_Diorite(
        "standing_torch_l_polished_diorite",
        regName -> new StandingTorch_L(Blocks.POLISHED_DIORITE, regName)
    ),
    StandingTorch_L_Polished_Granite(
        "standing_torch_l_polished_granite",
        regName -> new StandingTorch_L(Blocks.POLISHED_GRANITE, regName)
    ),
    StandingTorch_L_Stone(
        "standing_torch_l_stone",
        regName -> new StandingTorch_L(Blocks.STONE, regName)
    ),
    StandingTorch_L_CobbleStone(
        "standing_torch_l_cobblestone",
        regName -> new StandingTorch_L(Blocks.COBBLESTONE, regName)
    ),
    StandingTorch_L_Mossy_CobbleStone(
        "standing_torch_l_mossy_cobblestone",
        regName -> new StandingTorch_L(Blocks.MOSSY_COBBLESTONE, regName)
    ),
    StandingTorch_L_Sand_Stone(
        "standing_torch_l_sandstone",
        regName -> new StandingTorch_L(Blocks.SANDSTONE, regName)
    ),
    StandingTorch_L_Cut_Sand_Stone(
        "standing_torch_l_cut_sandstone",
        regName -> new StandingTorch_L(Blocks.CUT_SANDSTONE, regName)
    ),
    StandingTorch_L_End_Stone(
        "standing_torch_l_end_stone",
        regName -> new StandingTorch_L(Blocks.END_STONE, regName)
    ),
    StandingTorch_L_Iron(
        "standing_torch_l_iron_block",
        regName -> new StandingTorch_L(Blocks.IRON_BLOCK, regName)
    ),
    StandingTorch_L_Gold(
        "standing_torch_l_gold_block",
        regName -> new StandingTorch_L(Blocks.GOLD_BLOCK, regName)
    ),
    StandingTorch_L_Diamond(
        "standing_torch_l_diamond_block",
        regName -> new StandingTorch_L(Blocks.DIAMOND_BLOCK, regName)
    ),
    StandingTorch_L_Packed_Ice(
        "standing_torch_l_packed_ice",
        regName -> new StandingTorch_L(Blocks.PACKED_ICE, regName)
    ),
    StandingTorch_L_Pink_Wool(
        "standing_torch_l_pink_wool",
        regName -> new StandingTorch_L(Blocks.PINK_WOOL, regName)
    ),
    StandingTorch_L_Magenta_Wool(
        "standing_torch_l_magenta_wool",
        regName -> new StandingTorch_L(Blocks.MAGENTA_WOOL, regName)
    ),
    StandingTorch_L_Polished_BlackStone(
        "standing_torch_l_polished_blackstone",
        regName -> new StandingTorch_L(Blocks.POLISHED_BLACKSTONE, regName)
    ),

    FirePit_S_Stone_Bricks(
        "fire_pit_s_stone_bricks",
        regName -> new FirePit_S(Blocks.STONE_BRICKS, regName)
    ),
    FirePit_S_Mossy_Stone_Bricks(
        "fire_pit_s_mossy_stone_bricks",
        regName -> new FirePit_S(Blocks.MOSSY_STONE_BRICKS, regName)
    ),
    FirePit_S_End_Stone_Bricks(
        "fire_pit_s_end_stone_bricks",
        regName -> new FirePit_S(Blocks.END_STONE_BRICKS, regName)
    ),
    FirePit_S_Nether_Bricks(
        "fire_pit_s_nether_bricks",
        regName -> new FirePit_S(Blocks.NETHER_BRICKS, regName)
    ),
    FirePit_S_Red_Nether_Bricks(
        "fire_pit_s_red_nether_bricks",
        regName -> new FirePit_S(Blocks.RED_NETHER_BRICKS, regName)
    ),
    FirePit_S_Smooth_Stone(
        "fire_pit_s_smooth_stone",
        regName -> new FirePit_S(Blocks.SMOOTH_STONE, regName)
    ),
    FirePit_S_Polished_Andesite(
        "fire_pit_s_polished_andesite",
        regName -> new FirePit_S(Blocks.POLISHED_ANDESITE, regName)
    ),
    FirePit_S_Polished_Diorite(
        "fire_pit_s_polished_diorite",
        regName -> new FirePit_S(Blocks.POLISHED_DIORITE, regName)
    ),
    FirePit_S_Polished_Granite(
        "fire_pit_s_polished_granite",
        regName -> new FirePit_S(Blocks.POLISHED_GRANITE, regName)
    ),
    FirePit_S_Stone(
        "fire_pit_s_stone",
        regName -> new FirePit_S(Blocks.STONE, regName)
    ),
    FirePit_S_CobbleStone(
        "fire_pit_s_cobblestone",
        regName -> new FirePit_S(Blocks.COBBLESTONE, regName)
    ),
    FirePit_S_Mossy_CobbleStone(
        "fire_pit_s_mossy_cobblestone",
        regName -> new FirePit_S(Blocks.MOSSY_COBBLESTONE, regName)
    ),
    FirePit_S_Sand_Stone(
        "fire_pit_s_sandstone",
        regName -> new FirePit_S(Blocks.SANDSTONE, regName)
    ),
    FirePit_S_Cut_Sand_Stone(
        "fire_pit_s_cut_sandstone",
        regName -> new FirePit_S(Blocks.CUT_SANDSTONE, regName)
    ),
    FirePit_S_End_Stone(
        "fire_pit_s_end_stone",
        regName -> new FirePit_S(Blocks.END_STONE, regName)
    ),
    FirePit_S_Iron(
        "fire_pit_s_iron_block",
        regName -> new FirePit_S(Blocks.IRON_BLOCK, regName)
    ),
    FirePit_S_Gold(
        "fire_pit_s_gold_block",
        regName -> new FirePit_S(Blocks.GOLD_BLOCK, regName)
    ),
    FirePit_S_Diamond(
        "fire_pit_s_diamond_block",
        regName -> new FirePit_S(Blocks.DIAMOND_BLOCK, regName)
    ),
    FirePit_S_Ice(
        "fire_pit_s_packed_ice",
        regName -> new FirePit_S(Blocks.PACKED_ICE, regName)
    ),
    FirePit_S_Pink_Wool(
        "fire_pit_s_pink_wool",
        regName -> new FirePit_S(Blocks.PINK_WOOL, regName)
    ),
    FirePit_S_Magenta_Wool(
        "fire_pit_s_magenta_wool",
        regName -> new FirePit_S(Blocks.MAGENTA_WOOL, regName)
    ),
    FirePit_S_Polished_BlackStone(
        "fire_pit_s_polished_blackstone",
        regName -> new FirePit_S(Blocks.POLISHED_BLACKSTONE, regName)
    ),

    FirePit_L_Stone_Bricks(
        "fire_pit_l_stone_bricks",
        regName -> new FirePit_L(Blocks.STONE_BRICKS, regName)
    ),
    FirePit_L_Mossy_Stone_Bricks(
        "fire_pit_l_mossy_stone_bricks",
        regName -> new FirePit_L(Blocks.MOSSY_STONE_BRICKS, regName)
    ),
    FirePit_L_End_Stone_Bricks(
        "fire_pit_l_end_stone_bricks",
        regName -> new FirePit_L(Blocks.END_STONE_BRICKS, regName)
    ),
    FirePit_L_Nether_Bricks(
        "fire_pit_l_nether_bricks",
        regName -> new FirePit_L(Blocks.NETHER_BRICKS, regName)
    ),
    FirePit_L_Red_Nether_Bricks(
        "fire_pit_l_red_nether_bricks",
        regName -> new FirePit_L(Blocks.RED_NETHER_BRICKS, regName)
    ),
    FirePit_L_Smooth_Stone(
        "fire_pit_l_smooth_stone",
        regName -> new FirePit_L(Blocks.SMOOTH_STONE, regName)
    ),
    FirePit_L_Polished_Andesite(
        "fire_pit_l_polished_andesite",
        regName -> new FirePit_L(Blocks.POLISHED_ANDESITE, regName)
    ),
    FirePit_L_Polished_Diorite(
        "fire_pit_l_polished_diorite",
        regName -> new FirePit_L(Blocks.POLISHED_DIORITE, regName)
    ),
    FirePit_L_Polished_Granite(
        "fire_pit_l_polished_granite",
        regName -> new FirePit_L(Blocks.POLISHED_GRANITE, regName)
    ),
    FirePit_L_Stone(
        "fire_pit_l_stone",
        regName -> new FirePit_L(Blocks.STONE, regName)
    ),
    FirePit_L_CobbleStone(
        "fire_pit_l_cobblestone",
        regName -> new FirePit_L(Blocks.COBBLESTONE, regName)
    ),
    FirePit_L_Mossy_CobbleStone(
        "fire_pit_l_mossy_cobblestone",
        regName -> new FirePit_L(Blocks.MOSSY_COBBLESTONE, regName)
    ),
    FirePit_L_Sand_Stone(
        "fire_pit_l_sandstone",
        regName -> new FirePit_L(Blocks.SANDSTONE, regName)
    ),
    FirePit_L_Cut_Sand_Stone(
        "fire_pit_l_cut_sandstone",
        regName -> new FirePit_L(Blocks.CUT_SANDSTONE, regName)
    ),
    FirePit_L_End_Stone(
        "fire_pit_l_end_stone",
        regName -> new FirePit_L(Blocks.END_STONE, regName)
    ),
    FirePit_L_Iron(
        "fire_pit_l_iron_block",
        regName -> new FirePit_L(Blocks.IRON_BLOCK, regName)
    ),
    FirePit_L_Gold(
        "fire_pit_l_gold_block",
        regName -> new FirePit_L(Blocks.GOLD_BLOCK, regName)
    ),
    FirePit_L_Diamond(
        "fire_pit_l_diamond_block",
        regName -> new FirePit_L(Blocks.DIAMOND_BLOCK, regName)
    ),
    FirePit_L_Ice(
        "fire_pit_l_packed_ice",
        regName -> new FirePit_L(Blocks.PACKED_ICE, regName)
    ),
    FirePit_L_Pink_Wool(
        "fire_pit_l_pink_wool",
        regName -> new FirePit_L(Blocks.PINK_WOOL, regName)
    ),
    FirePit_L_Magenta_Wool(
        "fire_pit_l_magenta_wool",
        regName -> new FirePit_L(Blocks.MAGENTA_WOOL, regName)
    ),
    FirePit_L_Polished_BlackStone(
        "fire_pit_l_polished_blackstone",
        regName -> new FirePit_L(Blocks.POLISHED_BLACKSTONE, regName)
    ),

    Fire_For_StandingTorch_S(
        "fire_for_standing_torch_s",
        regName -> new Fire(PedestalTypes.standing_torch_s, regName)
    ),
    Fire_For_StandingTorch_L(
        "fire_for_standing_torch_l",
        regName -> new Fire(PedestalTypes.standing_torch_l, regName)
    ),
    Fire_For_FirePit_S(
        "fire_for_fire_pit_s",
        regName -> new Fire(PedestalTypes.fire_pit_s, regName)
    ),
    Fire_For_FirePit_L( 
        "fire_for_fire_pit_l",
        regName -> new Fire(PedestalTypes.fire_pit_l, regName)
    ),
    SoulFire_For_StandingTorch_S(
        "soul_fire_for_standing_torch_s",
        regName -> new Fire_Soul(PedestalTypes.standing_torch_s, regName)
    ),
    SoulFire_For_StandingTorch_L(
        "soul_fire_for_standing_torch_l",
        regName -> new Fire_Soul(PedestalTypes.standing_torch_l, regName)
    ),
    SoulFire_For_FirePit_S(
        "soul_fire_for_fire_pit_s",
        regName -> new Fire_Soul(PedestalTypes.fire_pit_s, regName)
    ),
    SoulFire_For_FirePit_L(
        "soul_fire_for_fire_pit_l",
        regName -> new Fire_Soul(PedestalTypes.fire_pit_l, regName)
    );

    private final String regName;
    private final Function<String, Block> blockFactory;

    ModBlockList(String regName, Function<String, Block> blockFactory) {
        this.regName = regName;
        this.blockFactory = blockFactory;
    }

    public void init() {
        ((IModBlock)getBlock()).setMyKey(this) ;
    }

    public String getRegName() {
        return regName;
    }

    public Block getBlock() {
        return AdditionalLights.getBlock( this );
    }

    public Item getBlockItem() {
        return AdditionalLights.getBlockItem( this );
    }

    private Block createBlock() {
        return blockFactory.apply(regName);
    }

    public void register(){
        AdditionalLights.modBlocks.put( this, AdditionalLights.BLOCKS.register( regName, this::createBlock ) );

        if( getRegName().contains("al_wall_torch") )
            return;


        if (getRegName().contains("al_torch")) {
            var wallKey = ModBlockList.valueOf(this.name().replace("ALTorch", "ALTorch_Wall"));
            AdditionalLights.modBlockItems.put(this, AdditionalLights.ITEMS.register(getRegName(), () ->
                new StandingAndWallBlockItem(
                    getBlock(),
                    wallKey.getBlock(),
                    Direction.DOWN,
                    new Item.Properties().setId(AdditionalLights.createItemResourceKey(regName))
                )
            ));
        } else {
            AdditionalLights.modBlockItems.put(this, AdditionalLights.ITEMS.register(getRegName(), () -> {
                Block block = AdditionalLights.modBlocks.get(this).get();
                if (block instanceof com.mgen256.al.blocks.Pedestal) {
                    return new PedestalBlockItem(block, new Item.Properties().setId(AdditionalLights.createItemResourceKey(regName)));
                } else {
                    return new BlockItem(block, new Item.Properties().setId(AdditionalLights.createItemResourceKey(regName)));
                }
            }));
        }
    }

}
package com.mgen256.al;

import java.util.function.Function;

import com.mgen256.al.blocks.*;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.BlockItem;
import net.minecraft.item.VerticallyAttachableBlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;

public enum ModBlockList {

    ALLamp_Acacia("al_lamp_acacia_planks", (key) -> new ALLamp(Blocks.ACACIA_PLANKS, key) ),
    ALLamp_Birch("al_lamp_birch_planks", (key) -> new ALLamp(Blocks.BIRCH_PLANKS, key) ),
    ALLamp_Oak("al_lamp_oak_planks", (key) -> new ALLamp(Blocks.OAK_PLANKS, key) ),
    ALLamp_Dark_Oak("al_lamp_dark_oak_planks", (key) -> new ALLamp(Blocks.DARK_OAK_PLANKS, key) ),
    ALLamp_Spruce("al_lamp_spruce_planks", (key) -> new ALLamp(Blocks.SPRUCE_PLANKS, key) ),
    ALLamp_Jungle("al_lamp_jungle_planks", (key) -> new ALLamp(Blocks.JUNGLE_PLANKS, key) ),
    ALLamp_Stone("al_lamp_stone", (key) -> new ALLamp(Blocks.STONE, key) ),
    ALLamp_CobbleStone("al_lamp_cobblestone", (key) -> new ALLamp(Blocks.COBBLESTONE, key) ),
    ALLamp_Mossy_CobbleStone("al_lamp_mossy_cobblestone", (key) -> new ALLamp(Blocks.MOSSY_COBBLESTONE, key) ),
    ALLamp_End_Stone("al_lamp_end_stone", (key) -> new ALLamp(Blocks.END_STONE, key) ),
    ALLamp_Sand_Stone("al_lamp_sandstone", (key) -> new ALLamp(Blocks.SANDSTONE, key) ),
    ALLamp_Glass("al_lamp_glass", (key) -> new ALLamp(Blocks.GLASS, key) ),
    ALLamp_Iron("al_lamp_iron_block", (key) -> new ALLamp(Blocks.IRON_BLOCK, key) ),
    ALLamp_Gold("al_lamp_gold_block", (key) -> new ALLamp(Blocks.GOLD_BLOCK, key) ),
    ALLamp_Diamond("al_lamp_diamond_block", (key) -> new ALLamp(Blocks.DIAMOND_BLOCK, key) ),
    ALLamp_Ice("al_lamp_packed_ice", (key) -> new ALLamp(Blocks.PACKED_ICE, key) ),
    ALLamp_Pink_Wool("al_lamp_pink_wool", (key) -> new ALLamp(Blocks.PINK_WOOL, key) ),
    ALLamp_Magenta_Wool("al_lamp_magenta_wool", (key) -> new ALLamp(Blocks.MAGENTA_WOOL, key) ),
    ALLamp_Nether_Bricks("al_lamp_nether_bricks", (key) -> new ALLamp(Blocks.NETHER_BRICKS, key) ),
    ALLamp_Red_Nether_Bricks("al_lamp_red_nether_bricks", (key) -> new ALLamp(Blocks.RED_NETHER_BRICKS, key) ),
    ALLamp_BlackStone("al_lamp_blackstone", (key) -> new ALLamp(Blocks.BLACKSTONE, key) ),
    ALLamp_CrimsonPlanks("al_lamp_crimson_planks", (key) -> new ALLamp(Blocks.CRIMSON_PLANKS, key) ),
    ALLamp_WarpedPlanks("al_lamp_warped_planks", (key) -> new ALLamp(Blocks.WARPED_PLANKS, key) ),
    
    ALTorch_Wall_Acacia("al_wall_torch_acacia_planks", (key) -> new ALTorch_Wall( Blocks.ACACIA_PLANKS, key ) ),
    ALTorch_Wall_Birch("al_wall_torch_birch_planks", (key) -> new ALTorch_Wall( Blocks.BIRCH_PLANKS, key ) ),
    ALTorch_Wall_Oak("al_wall_torch_oak_planks", (key) -> new ALTorch_Wall( Blocks.OAK_PLANKS, key ) ),
    ALTorch_Wall_Dark_Oak("al_wall_torch_dark_oak_planks", (key) -> new ALTorch_Wall( Blocks.DARK_OAK_PLANKS, key ) ),
    ALTorch_Wall_Jungle("al_wall_torch_jungle_planks", (key) -> new ALTorch_Wall( Blocks.JUNGLE_PLANKS, key ) ),
    ALTorch_Wall_Spruce("al_wall_torch_spruce_planks", (key) -> new ALTorch_Wall( Blocks.SPRUCE_PLANKS, key ) ),
    ALTorch_Wall_Stone("al_wall_torch_stone", (key) -> new ALTorch_Wall( Blocks.STONE, key ) ),
    ALTorch_Wall_CobbleStone("al_wall_torch_cobblestone", (key) -> new ALTorch_Wall( Blocks.COBBLESTONE, key ) ),
    ALTorch_Wall_Mossy_CobbleStone("al_wall_torch_mossy_cobblestone", (key) -> new ALTorch_Wall( Blocks.MOSSY_COBBLESTONE, key ) ),
    ALTorch_Wall_End_Stone("al_wall_torch_end_stone", (key) -> new ALTorch_Wall( Blocks.END_STONE, key ) ),
    ALTorch_Wall_Sand_Stone("al_wall_torch_sandstone", (key) -> new ALTorch_Wall( Blocks.SANDSTONE, key ) ),
    ALTorch_Wall_Stone_Bricks("al_wall_torch_stone_bricks", (key) -> new ALTorch_Wall( Blocks.STONE_BRICKS, key ) ),
    ALTorch_Wall_Mossy_Stone_Bricks("al_wall_torch_mossy_stone_bricks", (key) -> new ALTorch_Wall( Blocks.MOSSY_STONE_BRICKS, key ) ),
    ALTorch_Wall_End_Stone_Bricks("al_wall_torch_end_stone_bricks", (key) -> new ALTorch_Wall( Blocks.END_STONE_BRICKS, key ) ),
    ALTorch_Wall_Nether_Bricks("al_wall_torch_nether_bricks", (key) -> new ALTorch_Wall( Blocks.NETHER_BRICKS, key ) ),
    ALTorch_Wall_Red_Nether_Bricks("al_wall_torch_red_nether_bricks", (key) -> new ALTorch_Wall( Blocks.RED_NETHER_BRICKS, key ) ),
    ALTorch_Wall_Smooth_Stone("al_wall_torch_smooth_stone", (key) -> new ALTorch_Wall( Blocks.SMOOTH_STONE, key ) ),
    ALTorch_Wall_Glass("al_wall_torch_glass", (key) -> new ALTorch_Wall( Blocks.GLASS, key ) ),
    ALTorch_Wall_Iron("al_wall_torch_iron_block", (key) -> new ALTorch_Wall( Blocks.IRON_BLOCK, key ) ),
    ALTorch_Wall_Gold("al_wall_torch_gold_block", (key) -> new ALTorch_Wall( Blocks.GOLD_BLOCK, key ) ),
    ALTorch_Wall_Diamond("al_wall_torch_diamond_block", (key) -> new ALTorch_Wall( Blocks.DIAMOND_BLOCK, key ) ),
    ALTorch_Wall_Ice("al_wall_torch_packed_ice", (key) -> new ALTorch_Wall( Blocks.PACKED_ICE, key ) ),
    ALTorch_Wall_Pink_Wool("al_wall_torch_pink_wool", (key) -> new ALTorch_Wall( Blocks.PINK_WOOL, key ) ),
    ALTorch_Wall_Magenta_Wool("al_wall_torch_magenta_wool", (key) -> new ALTorch_Wall( Blocks.MAGENTA_WOOL, key ) ),
    ALTorch_Wall_Crimson("al_wall_torch_crimson_planks", (key) -> new ALTorch_Wall( Blocks.CRIMSON_PLANKS, key ) ),
    ALTorch_Wall_Warped("al_wall_torch_warped_planks", (key) -> new ALTorch_Wall( Blocks.WARPED_PLANKS, key ) ),
    ALTorch_Wall_BlackStone("al_wall_torch_blackstone", (key) -> new ALTorch_Wall( Blocks.BLACKSTONE, key ) ),

    ALTorch_Acacia("al_torch_acacia_planks", (key) -> new ALTorch( Blocks.ACACIA_PLANKS, key ) ),
    ALTorch_Birch("al_torch_birch_planks", (key) -> new ALTorch( Blocks.BIRCH_PLANKS, key ) ),
    ALTorch_Oak("al_torch_oak_planks", (key) -> new ALTorch( Blocks.OAK_PLANKS, key ) ),
    ALTorch_Dark_Oak("al_torch_dark_oak_planks", (key) -> new ALTorch( Blocks.DARK_OAK_PLANKS, key ) ),
    ALTorch_Jungle("al_torch_jungle_planks", (key) -> new ALTorch( Blocks.JUNGLE_PLANKS, key ) ),
    ALTorch_Spruce("al_torch_spruce_planks", (key) -> new ALTorch( Blocks.SPRUCE_PLANKS, key ) ),
    ALTorch_Stone("al_torch_stone", (key) -> new ALTorch( Blocks.STONE, key ) ),
    ALTorch_CobbleStone("al_torch_cobblestone", (key) -> new ALTorch( Blocks.COBBLESTONE, key ) ),
    ALTorch_Mossy_CobbleStone("al_torch_mossy_cobblestone", (key) -> new ALTorch( Blocks.MOSSY_COBBLESTONE, key ) ),
    ALTorch_End_Stone("al_torch_end_stone", (key) -> new ALTorch( Blocks.END_STONE, key ) ),
    ALTorch_Sand_Stone("al_torch_sandstone", (key) -> new ALTorch( Blocks.SANDSTONE, key ) ),
    ALTorch_Stone_Bricks("al_torch_stone_bricks", (key) -> new ALTorch( Blocks.STONE_BRICKS, key ) ),
    ALTorch_Mossy_Stone_Bricks("al_torch_mossy_stone_bricks", (key) -> new ALTorch( Blocks.MOSSY_STONE_BRICKS, key ) ),
    ALTorch_End_Stone_Bricks("al_torch_end_stone_bricks", (key) -> new ALTorch( Blocks.END_STONE_BRICKS, key ) ),
    ALTorch_Nether_Bricks("al_torch_nether_bricks", (key) -> new ALTorch( Blocks.NETHER_BRICKS, key ) ),
    ALTorch_Red_Nether_Bricks("al_torch_red_nether_bricks", (key) -> new ALTorch( Blocks.RED_NETHER_BRICKS, key ) ),
    ALTorch_Smooth_Stone("al_torch_smooth_stone", (key) -> new ALTorch( Blocks.SMOOTH_STONE, key ) ),
    ALTorch_Glass("al_torch_glass", (key) -> new ALTorch( Blocks.GLASS, key) ),
    ALTorch_Iron("al_torch_iron_block", (key) -> new ALTorch( Blocks.IRON_BLOCK, key) ),
    ALTorch_Gold("al_torch_gold_block", (key) -> new ALTorch( Blocks.GOLD_BLOCK, key) ),
    ALTorch_Diamond("al_torch_diamond_block", (key) -> new ALTorch( Blocks.DIAMOND_BLOCK, key) ),
    ALTorch_Ice("al_torch_packed_ice", (key) -> new ALTorch( Blocks.PACKED_ICE, key) ),
    ALTorch_Pink_Wool("al_torch_pink_wool", (key) -> new ALTorch( Blocks.PINK_WOOL, key) ),
    ALTorch_Magenta_Wool("al_torch_magenta_wool", (key) -> new ALTorch( Blocks.MAGENTA_WOOL, key) ),
    ALTorch_Crimson("al_torch_crimson_planks", (key) -> new ALTorch( Blocks.CRIMSON_PLANKS, key) ),
    ALTorch_Warped("al_torch_warped_planks", (key) -> new ALTorch( Blocks.WARPED_PLANKS, key) ),
    ALTorch_BlackStone("al_torch_blackstone", (key) -> new ALTorch( Blocks.BLACKSTONE, key) ),

    StandingTorch_S_Stone_Bricks("standing_torch_s_stone_bricks", (key) -> new StandingTorch_S( Blocks.STONE_BRICKS, key ) ),
    StandingTorch_S_Mossy_Stone_Bricks("standing_torch_s_mossy_stone_bricks", (key) -> new StandingTorch_S( Blocks.MOSSY_STONE_BRICKS, key ) ),
    StandingTorch_S_End_Stone_Bricks("standing_torch_s_end_stone_bricks", (key) -> new StandingTorch_S( Blocks.END_STONE_BRICKS, key ) ),
    StandingTorch_S_Nether_Bricks("standing_torch_s_nether_bricks", (key) -> new StandingTorch_S( Blocks.NETHER_BRICKS, key ) ),
    StandingTorch_S_Red_Nether_Bricks("standing_torch_s_red_nether_bricks", (key) -> new StandingTorch_S( Blocks.RED_NETHER_BRICKS, key ) ),
    StandingTorch_S_Smooth_Stone("standing_torch_s_smooth_stone", (key) -> new StandingTorch_S( Blocks.SMOOTH_STONE, key ) ),
    StandingTorch_S_Polished_Andesite("standing_torch_s_polished_andesite", (key) -> new StandingTorch_S( Blocks.POLISHED_ANDESITE, key ) ),
    StandingTorch_S_Polished_Diorite("standing_torch_s_polished_diorite", (key) -> new StandingTorch_S( Blocks.POLISHED_DIORITE, key ) ),
    StandingTorch_S_Polished_Granite("standing_torch_s_polished_granite", (key) -> new StandingTorch_S( Blocks.POLISHED_GRANITE, key ) ),
    StandingTorch_S_Stone("standing_torch_s_stone", (key) -> new StandingTorch_S( Blocks.STONE, key ) ),
    StandingTorch_S_CobbleStone("standing_torch_s_cobblestone", (key) -> new StandingTorch_S( Blocks.COBBLESTONE, key ) ),
    StandingTorch_S_Mossy_CobbleStone("standing_torch_s_mossy_cobblestone", (key) -> new StandingTorch_S( Blocks.MOSSY_COBBLESTONE, key ) ),
    StandingTorch_S_Sand_Stone("standing_torch_s_sandstone", (key) -> new StandingTorch_S( Blocks.SANDSTONE, key ) ),
    StandingTorch_S_Cut_Sand_Stone("standing_torch_s_cut_sandstone", (key) -> new StandingTorch_S( Blocks.CUT_SANDSTONE, key ) ),
    StandingTorch_S_End_Stone("standing_torch_s_end_stone", (key) -> new StandingTorch_S( Blocks.END_STONE, key ) ),
    StandingTorch_S_Iron("standing_torch_s_iron_block", (key) -> new StandingTorch_S( Blocks.IRON_BLOCK, key ) ),
    StandingTorch_S_Gold("standing_torch_s_gold_block", (key) -> new StandingTorch_S( Blocks.GOLD_BLOCK, key ) ),
    StandingTorch_S_Diamond("standing_torch_s_diamond_block", (key) -> new StandingTorch_S( Blocks.DIAMOND_BLOCK, key ) ),
    StandingTorch_S_Packed_Ice("standing_torch_s_packed_ice", (key) -> new StandingTorch_S( Blocks.PACKED_ICE, key ) ),
    StandingTorch_S_Pink_Wool("standing_torch_s_pink_wool", (key) -> new StandingTorch_S( Blocks.PINK_WOOL, key ) ),
    StandingTorch_S_Magenta_Wool("standing_torch_s_magenta_wool", (key) -> new StandingTorch_S( Blocks.MAGENTA_WOOL, key ) ),
    StandingTorch_S_Polished_BlackStone("standing_torch_s_polished_blackstone", (key) -> new StandingTorch_S( Blocks.POLISHED_BLACKSTONE, key ) ),
    
    StandingTorch_L_Stone_Bricks("standing_torch_l_stone_bricks", (key) -> new StandingTorch_L( Blocks.STONE_BRICKS, key ) ),
    StandingTorch_L_Mossy_Stone_Bricks("standing_torch_l_mossy_stone_bricks", (key) -> new StandingTorch_L( Blocks.MOSSY_STONE_BRICKS, key ) ),
    StandingTorch_L_End_Stone_Bricks("standing_torch_l_end_stone_bricks", (key) -> new StandingTorch_L( Blocks.END_STONE_BRICKS, key ) ),
    StandingTorch_L_Nether_Bricks("standing_torch_l_nether_bricks", (key) -> new StandingTorch_L( Blocks.NETHER_BRICKS, key ) ),
    StandingTorch_L_Red_Nether_Bricks("standing_torch_l_red_nether_bricks", (key) -> new StandingTorch_L( Blocks.RED_NETHER_BRICKS, key ) ),
    StandingTorch_L_Smooth_Stone("standing_torch_l_smooth_stone", (key) -> new StandingTorch_L( Blocks.SMOOTH_STONE, key ) ),
    StandingTorch_L_Polished_Andesite("standing_torch_l_polished_andesite", (key) -> new StandingTorch_L( Blocks.POLISHED_ANDESITE, key ) ),
    StandingTorch_L_Polished_Diorite("standing_torch_l_polished_diorite", (key) -> new StandingTorch_L( Blocks.POLISHED_DIORITE, key ) ),
    StandingTorch_L_Polished_Granite("standing_torch_l_polished_granite", (key) -> new StandingTorch_L( Blocks.POLISHED_GRANITE, key ) ),
    StandingTorch_L_Stone("standing_torch_l_stone", (key) -> new StandingTorch_L( Blocks.STONE, key ) ),
    StandingTorch_L_CobbleStone("standing_torch_l_cobblestone", (key) -> new StandingTorch_L( Blocks.COBBLESTONE, key ) ),
    StandingTorch_L_Mossy_CobbleStone("standing_torch_l_mossy_cobblestone", (key) -> new StandingTorch_L( Blocks.MOSSY_COBBLESTONE, key ) ),
    StandingTorch_L_Sand_Stone("standing_torch_l_sandstone", (key) -> new StandingTorch_L( Blocks.SANDSTONE, key ) ),
    StandingTorch_L_Cut_Sand_Stone("standing_torch_l_cut_sandstone", (key) -> new StandingTorch_L( Blocks.CUT_SANDSTONE, key ) ),
    StandingTorch_L_End_Stone("standing_torch_l_end_stone", (key) -> new StandingTorch_L( Blocks.END_STONE, key ) ),
    StandingTorch_L_Iron("standing_torch_l_iron_block", (key) -> new StandingTorch_L( Blocks.IRON_BLOCK, key ) ),
    StandingTorch_L_Gold("standing_torch_l_gold_block", (key) -> new StandingTorch_L( Blocks.GOLD_BLOCK, key ) ),
    StandingTorch_L_Diamond("standing_torch_l_diamond_block", (key) -> new StandingTorch_L( Blocks.DIAMOND_BLOCK, key ) ),
    StandingTorch_L_Packed_Ice("standing_torch_l_packed_ice", (key) -> new StandingTorch_L( Blocks.PACKED_ICE, key ) ),
    StandingTorch_L_Pink_Wool("standing_torch_l_pink_wool", (key) -> new StandingTorch_L( Blocks.PINK_WOOL, key ) ),
    StandingTorch_L_Magenta_Wool("standing_torch_l_magenta_wool", (key) -> new StandingTorch_L( Blocks.MAGENTA_WOOL, key ) ),
    StandingTorch_L_Polished_BlackStone("standing_torch_l_polished_blackstone", (key) -> new StandingTorch_L( Blocks.POLISHED_BLACKSTONE, key ) ),

    FirePit_S_Stone_Bricks("fire_pit_s_stone_bricks", (key) -> new FirePit_S( Blocks.STONE_BRICKS, key ) ),
    FirePit_S_Mossy_Stone_Bricks("fire_pit_s_mossy_stone_bricks", (key) -> new FirePit_S( Blocks.MOSSY_STONE_BRICKS, key ) ),
    FirePit_S_End_Stone_Bricks("fire_pit_s_end_stone_bricks", (key) -> new FirePit_S( Blocks.END_STONE_BRICKS, key ) ),
    FirePit_S_Nether_Bricks("fire_pit_s_nether_bricks", (key) -> new FirePit_S( Blocks.NETHER_BRICKS, key ) ),
    FirePit_S_Red_Nether_Bricks("fire_pit_s_red_nether_bricks", (key) -> new FirePit_S( Blocks.RED_NETHER_BRICKS, key ) ),
    FirePit_S_Smooth_Stone("fire_pit_s_smooth_stone", (key) -> new FirePit_S( Blocks.SMOOTH_STONE, key ) ),
    FirePit_S_Polished_Andesite("fire_pit_s_polished_andesite", (key) -> new FirePit_S( Blocks.POLISHED_ANDESITE, key ) ),
    FirePit_S_Polished_Diorite("fire_pit_s_polished_diorite", (key) -> new FirePit_S( Blocks.POLISHED_DIORITE, key ) ),
    FirePit_S_Polished_Granite("fire_pit_s_polished_granite", (key) -> new FirePit_S( Blocks.POLISHED_GRANITE, key ) ),
    FirePit_S_Stone("fire_pit_s_stone", (key) -> new FirePit_S( Blocks.STONE, key ) ),
    FirePit_S_CobbleStone("fire_pit_s_cobblestone", (key) -> new FirePit_S( Blocks.COBBLESTONE, key ) ),
    FirePit_S_Mossy_CobbleStone("fire_pit_s_mossy_cobblestone", (key) -> new FirePit_S( Blocks.MOSSY_COBBLESTONE, key ) ),
    FirePit_S_Sand_Stone("fire_pit_s_sandstone", (key) -> new FirePit_S( Blocks.SANDSTONE, key ) ),
    FirePit_S_Cut_Sand_Stone("fire_pit_s_cut_sandstone", (key) -> new FirePit_S( Blocks.CUT_SANDSTONE, key ) ),
    FirePit_S_End_Stone("fire_pit_s_end_stone", (key) -> new FirePit_S( Blocks.END_STONE, key ) ),
    FirePit_S_Iron("fire_pit_s_iron_block", (key) -> new FirePit_S( Blocks.IRON_BLOCK, key ) ),
    FirePit_S_Gold("fire_pit_s_gold_block", (key) -> new FirePit_S( Blocks.GOLD_BLOCK, key ) ),
    FirePit_S_Diamond("fire_pit_s_diamond_block", (key) -> new FirePit_S( Blocks.DIAMOND_BLOCK, key ) ),
    FirePit_S_Ice("fire_pit_s_packed_ice", (key) -> new FirePit_S( Blocks.PACKED_ICE, key ) ),
    FirePit_S_Pink_Wool("fire_pit_s_pink_wool", (key) -> new FirePit_S( Blocks.PINK_WOOL, key ) ),
    FirePit_S_Magenta_Wool("fire_pit_s_magenta_wool", (key) -> new FirePit_S( Blocks.MAGENTA_WOOL, key ) ),
    FirePit_S_Polished_BlackStone("fire_pit_s_polished_blackstone", (key) -> new FirePit_S( Blocks.POLISHED_BLACKSTONE, key ) ),

    FirePit_L_Stone_Bricks("fire_pit_l_stone_bricks", (key) -> new FirePit_L( Blocks.STONE_BRICKS, key ) ),
    FirePit_L_Mossy_Stone_Bricks("fire_pit_l_mossy_stone_bricks", (key) -> new FirePit_L( Blocks.MOSSY_STONE_BRICKS, key ) ),
    FirePit_L_End_Stone_Bricks("fire_pit_l_end_stone_bricks", (key) -> new FirePit_L( Blocks.END_STONE_BRICKS, key ) ),
    FirePit_L_Nether_Bricks("fire_pit_l_nether_bricks", (key) -> new FirePit_L( Blocks.NETHER_BRICKS, key ) ),
    FirePit_L_Red_Nether_Bricks("fire_pit_l_red_nether_bricks", (key) -> new FirePit_L( Blocks.RED_NETHER_BRICKS, key ) ),
    FirePit_L_Smooth_Stone("fire_pit_l_smooth_stone", (key) -> new FirePit_L( Blocks.SMOOTH_STONE, key ) ),
    FirePit_L_Polished_Andesite("fire_pit_l_polished_andesite", (key) -> new FirePit_L( Blocks.POLISHED_ANDESITE, key ) ),
    FirePit_L_Polished_Diorite("fire_pit_l_polished_diorite", (key) -> new FirePit_L( Blocks.POLISHED_DIORITE, key ) ),
    FirePit_L_Polished_Granite("fire_pit_l_polished_granite", (key) -> new FirePit_L( Blocks.POLISHED_GRANITE, key ) ),
    FirePit_L_Stone("fire_pit_l_stone", (key) -> new FirePit_L( Blocks.STONE, key ) ),
    FirePit_L_CobbleStone("fire_pit_l_cobblestone", (key) -> new FirePit_L( Blocks.COBBLESTONE, key ) ),
    FirePit_L_Mossy_CobbleStone("fire_pit_l_mossy_cobblestone", (key) -> new FirePit_L( Blocks.MOSSY_COBBLESTONE, key ) ),
    FirePit_L_Sand_Stone("fire_pit_l_sandstone", (key) -> new FirePit_L( Blocks.SANDSTONE, key ) ),
    FirePit_L_Cut_Sand_Stone("fire_pit_l_cut_sandstone", (key) -> new FirePit_L( Blocks.CUT_SANDSTONE, key ) ),
    FirePit_L_End_Stone("fire_pit_l_end_stone", (key) -> new FirePit_L( Blocks.END_STONE, key ) ),
    FirePit_L_Iron("fire_pit_l_iron_block", (key) -> new FirePit_L( Blocks.IRON_BLOCK, key ) ),
    FirePit_L_Gold("fire_pit_l_gold_block", (key) -> new FirePit_L( Blocks.GOLD_BLOCK, key ) ),
    FirePit_L_Diamond("fire_pit_l_diamond_block", (key) -> new FirePit_L( Blocks.DIAMOND_BLOCK, key ) ),
    FirePit_L_Ice("fire_pit_l_packed_ice", (key) -> new FirePit_L( Blocks.PACKED_ICE, key ) ),
    FirePit_L_Pink_Wool("fire_pit_l_pink_wool", (key) -> new FirePit_L( Blocks.PINK_WOOL, key ) ),
    FirePit_L_Magenta_Wool("fire_pit_l_magenta_wool", (key) -> new FirePit_L( Blocks.MAGENTA_WOOL, key ) ),
    FirePit_L_Polished_BlackStone("fire_pit_l_polished_blackstone", (key) -> new FirePit_L( Blocks.POLISHED_BLACKSTONE, key ) ),

    Fire_For_StandingTorch_S("fire_for_standing_torch_s", (key) -> new Fire( PedestalTypes.standing_torch_s, key ) ),
    Fire_For_StandingTorch_L( "fire_for_standing_torch_l", (key) -> new Fire( PedestalTypes.standing_torch_l, key ) ),
    Fire_For_FirePit_S("fire_for_fire_pit_s", (key) -> new Fire( PedestalTypes.fire_pit_s, key ) ),
    Fire_For_FirePit_L("fire_for_fire_pit_l", (key) -> new Fire( PedestalTypes.fire_pit_l, key) ),
    
    SoulFire_For_StandingTorch_S("soul_fire_for_standing_torch_s", (key) -> new Fire_Soul( PedestalTypes.standing_torch_s, key ) ),
    SoulFire_For_StandingTorch_L("soul_fire_for_standing_torch_l", (key) -> new Fire_Soul( PedestalTypes.standing_torch_l, key ) ),
    SoulFire_For_FirePit_S("soul_fire_for_fire_pit_s", (key) -> new Fire_Soul( PedestalTypes.fire_pit_s, key) ),
    SoulFire_For_FirePit_L("soul_fire_for_fire_pit_l", (key) -> new Fire_Soul( PedestalTypes.fire_pit_l, key) );


    private final String name;
    private Block block;
    private Function<RegistryKey<Block>, Block> blockFactory;
    private BlockItem blockItem;

    <I> ModBlockList(String name, Function<RegistryKey<Block>, Block> blockFactory) {
        this.name = name;
        this.blockFactory = blockFactory;
    }

    public String getRegName() {
        return name;
    }

    public Block get() {
        return get(null);
    }

    public Block get(RegistryKey<Block> key) {
        if (block == null) {
            assert key != null : "RegistryKey is null"; 
            block = blockFactory.apply(key);
        }
        return block;
    }

    public BlockItem getBlockItem() {
        return blockItem;
    }

    public void register(){
        var id = Identifier.of(AdditionalLights.MOD_ID, name);
        var key = RegistryKey.of(RegistryKeys.BLOCK, id);
        Registry.register(Registries.BLOCK, key, get(key));

        if( name.contains("al_wall_torch") )
            return;

        RegistryKey<Item> itemkey = RegistryKey.of(RegistryKeys.ITEM, id);
        Item.Settings settings = new Item.Settings()
            // If your item is based on a block
            .useBlockPrefixedTranslationKey()
            .registryKey(itemkey);

        if( name.contains("al_torch") )
        {
            var wallblock = ModBlockList.valueOf( this.name().replace("ALTorch", "ALTorch_Wall") );
            blockItem = new VerticallyAttachableBlockItem(get(), wallblock.get(), Direction.DOWN, settings);
            Registry.register(Registries.ITEM, id, blockItem);
        }
        else
        {
            blockItem = new BlockItem(get(), settings);
            Registry.register(Registries.ITEM, id, blockItem );
        }
    }
}
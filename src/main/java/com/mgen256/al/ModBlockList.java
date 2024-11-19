package com.mgen256.al;

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
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.StandingAndWallBlockItem;

public enum ModBlockList {

    ALLamp_Acacia("al_lamp_acacia_planks") {
        @Override
        public Block createBlock() {
            return new ALLamp(Blocks.ACACIA_PLANKS, getRegName());
        }
    },
    ALLamp_Birch("al_lamp_birch_planks") {
        @Override
        public Block createBlock() {
            return new ALLamp(Blocks.BIRCH_PLANKS, getRegName());
        }
    },
    ALLamp_Oak("al_lamp_oak_planks") {
        @Override
        public Block createBlock() {
            return new ALLamp(Blocks.OAK_PLANKS, getRegName());
        }
    },
    ALLamp_Dark_Oak("al_lamp_dark_oak_planks") {
        @Override
        public Block createBlock() {
            return new ALLamp(Blocks.DARK_OAK_PLANKS, getRegName());
        }
    },
    ALLamp_Spruce("al_lamp_spruce_planks") {
        @Override
        public Block createBlock() {
            return new ALLamp(Blocks.SPRUCE_PLANKS, getRegName());
        }
    },
    ALLamp_Jungle("al_lamp_jungle_planks") {
        @Override
        public Block createBlock() {
            return new ALLamp(Blocks.JUNGLE_PLANKS, getRegName());
        }
    },
    ALLamp_Stone("al_lamp_stone") {
        @Override
        public Block createBlock() {
            return new ALLamp(Blocks.STONE, getRegName());
        }
    },
    ALLamp_CobbleStone("al_lamp_cobblestone") {
        @Override
        public Block createBlock() {
            return new ALLamp(Blocks.COBBLESTONE, getRegName());
        }
    },
    ALLamp_Mossy_CobbleStone("al_lamp_mossy_cobblestone") {
        @Override
        public Block createBlock() {
            return new ALLamp(Blocks.MOSSY_COBBLESTONE, getRegName());
        }
    },
    ALLamp_End_Stone("al_lamp_end_stone") {
        @Override
        public Block createBlock() {
            return new ALLamp(Blocks.END_STONE, getRegName());
        }
    },
    ALLamp_Sand_Stone("al_lamp_sandstone") {
        @Override
        public Block createBlock() {
            return new ALLamp(Blocks.SANDSTONE, getRegName());
        }
    },
    ALLamp_Glass("al_lamp_glass") {
        @Override
        public Block createBlock() {
            return new ALLamp(Blocks.GLASS, getRegName());
        }
    },
    ALLamp_Iron("al_lamp_iron_block") {
        @Override
        public Block createBlock() {
            return new ALLamp(Blocks.IRON_BLOCK, getRegName());
        }
    },
    ALLamp_Gold("al_lamp_gold_block") {
        @Override
        public Block createBlock() {
            return new ALLamp(Blocks.GOLD_BLOCK, getRegName());
        }
    },
    ALLamp_Diamond("al_lamp_diamond_block") {
        @Override
        public Block createBlock() {
            return new ALLamp(Blocks.DIAMOND_BLOCK, getRegName());
        }
    },
    ALLamp_Ice("al_lamp_packed_ice") {
        @Override
        public Block createBlock() {
            return new ALLamp(Blocks.PACKED_ICE, getRegName());
        }
    },
    ALLamp_Pink_Wool("al_lamp_pink_wool") {
        @Override
        public Block createBlock() {
            return new ALLamp(Blocks.PINK_WOOL, getRegName());
        }
    },
    ALLamp_Magenta_Wool("al_lamp_magenta_wool") {
        @Override
        public Block createBlock() {
            return new ALLamp(Blocks.MAGENTA_WOOL, getRegName());
        }
    },
    ALLamp_Nether_Bricks("al_lamp_nether_bricks") {
        @Override
        public Block createBlock() {
            return new ALLamp(Blocks.NETHER_BRICKS, getRegName());
        }
    },
    ALLamp_Red_Nether_Bricks("al_lamp_red_nether_bricks") {
        @Override
        public Block createBlock() {
            return new ALLamp(Blocks.RED_NETHER_BRICKS, getRegName());
        }
    },
    ALLamp_BlackStone("al_lamp_blackstone") {
        @Override
        public Block createBlock() {
            return new ALLamp(Blocks.BLACKSTONE, getRegName());
        }
    },
    ALLamp_CrimsonPlanks("al_lamp_crimson_planks") {
        @Override
        public Block createBlock() {
            return new ALLamp(Blocks.CRIMSON_PLANKS, getRegName());
        }
    },
    ALLamp_WarpedPlanks("al_lamp_warped_planks") {
        @Override
        public Block createBlock() {
            return new ALLamp(Blocks.WARPED_PLANKS, getRegName());
        }
    },

    ALTorch_Acacia("al_torch_acacia_planks") {
        @Override
        public Block createBlock() {
            return new ALTorch(Blocks.ACACIA_PLANKS, getRegName());
        }
    },
    ALTorch_Birch("al_torch_birch_planks") {
        @Override
        public Block createBlock() {
            return new ALTorch(Blocks.BIRCH_PLANKS, getRegName());
        }
    },
    ALTorch_Oak("al_torch_oak_planks") {
        @Override
        public Block createBlock() {
            return new ALTorch(Blocks.OAK_PLANKS, getRegName());
        }
    },
    ALTorch_Dark_Oak("al_torch_dark_oak_planks") {
        @Override
        public Block createBlock() {
            return new ALTorch(Blocks.DARK_OAK_PLANKS, getRegName());
        }
    },
    ALTorch_Jungle("al_torch_jungle_planks") {
        @Override
        public Block createBlock() {
            return new ALTorch(Blocks.JUNGLE_PLANKS, getRegName());
        }
    },
    ALTorch_Spruce("al_torch_spruce_planks") {
        @Override
        public Block createBlock() {
            return new ALTorch(Blocks.SPRUCE_PLANKS, getRegName());
        }
    },
    ALTorch_Stone("al_torch_stone") {
        @Override
        public Block createBlock() {
            return new ALTorch(Blocks.STONE, getRegName());
        }
    },
    ALTorch_CobbleStone("al_torch_cobblestone") {
        @Override
        public Block createBlock() {
            return new ALTorch(Blocks.COBBLESTONE, getRegName());
        }
    },
    ALTorch_Mossy_CobbleStone("al_torch_mossy_cobblestone") {
        @Override
        public Block createBlock() {
            return new ALTorch(Blocks.MOSSY_COBBLESTONE, getRegName());
        }
    },
    ALTorch_End_Stone("al_torch_end_stone") {
        @Override
        public Block createBlock() {
            return new ALTorch(Blocks.END_STONE, getRegName());
        }
    },
    ALTorch_Sand_Stone("al_torch_sandstone") {
        @Override
        public Block createBlock() {
            return new ALTorch(Blocks.SANDSTONE, getRegName());
        }
    },
    ALTorch_Stone_Bricks("al_torch_stone_bricks") {
        @Override
        public Block createBlock() {
            return new ALTorch(Blocks.STONE_BRICKS, getRegName());
        }
    },
    ALTorch_Mossy_Stone_Bricks("al_torch_mossy_stone_bricks") {
        @Override
        public Block createBlock() {
            return new ALTorch(Blocks.MOSSY_STONE_BRICKS, getRegName());
        }
    },
    ALTorch_End_Stone_Bricks("al_torch_end_stone_bricks") {
        @Override
        public Block createBlock() {
            return new ALTorch(Blocks.END_STONE_BRICKS, getRegName());
        }
    },
    ALTorch_Nether_Bricks("al_torch_nether_bricks") {
        @Override
        public Block createBlock() {
            return new ALTorch(Blocks.NETHER_BRICKS, getRegName());
        }
    },
    ALTorch_Red_Nether_Bricks("al_torch_red_nether_bricks") {
        @Override
        public Block createBlock() {
            return new ALTorch(Blocks.RED_NETHER_BRICKS, getRegName());
        }
    },
    ALTorch_Smooth_Stone("al_torch_smooth_stone") {
        @Override
        public Block createBlock() {
            return new ALTorch(Blocks.SMOOTH_STONE, getRegName());
        }
    },
    ALTorch_Glass("al_torch_glass") {
        @Override
        public Block createBlock() {
            return new ALTorch(Blocks.GLASS, getRegName());
        }
    },
    ALTorch_Iron("al_torch_iron_block") {
        @Override
        public Block createBlock() {
            return new ALTorch(Blocks.IRON_BLOCK, getRegName());
        }
    },
    ALTorch_Gold("al_torch_gold_block") {
        @Override
        public Block createBlock() {
            return new ALTorch(Blocks.GOLD_BLOCK, getRegName());
        }
    },
    ALTorch_Diamond("al_torch_diamond_block") {
        @Override
        public Block createBlock() {
            return new ALTorch(Blocks.DIAMOND_BLOCK, getRegName());
        }
    },
    ALTorch_Ice("al_torch_packed_ice") {
        @Override
        public Block createBlock() {
            return new ALTorch(Blocks.PACKED_ICE, getRegName());
        }
    },
    ALTorch_Pink_Wool("al_torch_pink_wool") {
        @Override
        public Block createBlock() {
            return new ALTorch(Blocks.PINK_WOOL, getRegName());
        }
    },
    ALTorch_Magenta_Wool("al_torch_magenta_wool") {
        @Override
        public Block createBlock() {
            return new ALTorch(Blocks.MAGENTA_WOOL, getRegName());
        }
    },
    ALTorch_Crimson("al_torch_crimson_planks") {
        @Override
        public Block createBlock() {
            return new ALTorch(Blocks.CRIMSON_PLANKS, getRegName());
        }
    },
    ALTorch_Warped("al_torch_warped_planks") {
        @Override
        public Block createBlock() {
            return new ALTorch(Blocks.WARPED_PLANKS, getRegName());
        }
    },
    ALTorch_BlackStone("al_torch_blackstone") {
        @Override
        public Block createBlock() {
            return new ALTorch(Blocks.BLACKSTONE, getRegName());
        }
    },

    ALTorch_Wall_Acacia("al_wall_torch_acacia_planks") {
        @Override
        public Block createBlock() {
            return new ALTorch_Wall(Blocks.ACACIA_PLANKS, ALTorch_Acacia, getRegName());
        }
    },
    ALTorch_Wall_Birch("al_wall_torch_birch_planks") {
        @Override
        public Block createBlock() {
            return new ALTorch_Wall(Blocks.BIRCH_PLANKS, ALTorch_Birch, getRegName());
        }
    },
    ALTorch_Wall_Oak("al_wall_torch_oak_planks") {
        @Override
        public Block createBlock() {
            return new ALTorch_Wall(Blocks.OAK_PLANKS, ALTorch_Oak, getRegName());
        }
    },
    ALTorch_Wall_Dark_Oak("al_wall_torch_dark_oak_planks") {
        @Override
        public Block createBlock() {
            return new ALTorch_Wall(Blocks.DARK_OAK_PLANKS, ALTorch_Dark_Oak, getRegName());
        }
    },
    ALTorch_Wall_Jungle("al_wall_torch_jungle_planks") {
        @Override
        public Block createBlock() {
            return new ALTorch_Wall(Blocks.JUNGLE_PLANKS, ALTorch_Jungle, getRegName());
        }
    },
    ALTorch_Wall_Spruce("al_wall_torch_spruce_planks") {
        @Override
        public Block createBlock() {
            return new ALTorch_Wall(Blocks.SPRUCE_PLANKS, ALTorch_Spruce, getRegName());
        }
    },
    ALTorch_Wall_Stone("al_wall_torch_stone") {
        @Override
        public Block createBlock() {
            return new ALTorch_Wall(Blocks.STONE, ALTorch_Stone, getRegName());
        }
    },
    ALTorch_Wall_CobbleStone("al_wall_torch_cobblestone") {
        @Override
        public Block createBlock() {
            return new ALTorch_Wall(Blocks.COBBLESTONE, ALTorch_CobbleStone, getRegName());
        }
    },
    ALTorch_Wall_Mossy_CobbleStone("al_wall_torch_mossy_cobblestone") {
        @Override
        public Block createBlock() {
            return new ALTorch_Wall(Blocks.MOSSY_COBBLESTONE, ALTorch_Mossy_CobbleStone, getRegName());
        }
    },
    ALTorch_Wall_End_Stone("al_wall_torch_end_stone") {
        @Override
        public Block createBlock() {
            return new ALTorch_Wall(Blocks.END_STONE, ALTorch_End_Stone, getRegName());
        }
    },
    ALTorch_Wall_Sand_Stone("al_wall_torch_sandstone") {
        @Override
        public Block createBlock() {
            return new ALTorch_Wall(Blocks.SANDSTONE, ALTorch_Sand_Stone, getRegName());
        }
    },
    ALTorch_Wall_Stone_Bricks("al_wall_torch_stone_bricks") {
        @Override
        public Block createBlock() {
            return new ALTorch_Wall(Blocks.STONE_BRICKS, ALTorch_Stone_Bricks, getRegName());
        }
    },
    ALTorch_Wall_Mossy_Stone_Bricks("al_wall_torch_mossy_stone_bricks") {
        @Override
        public Block createBlock() {
            return new ALTorch_Wall(Blocks.MOSSY_STONE_BRICKS, ALTorch_Mossy_Stone_Bricks, getRegName());
        }
    },
    ALTorch_Wall_End_Stone_Bricks("al_wall_torch_end_stone_bricks") {
        @Override
        public Block createBlock() {
            return new ALTorch_Wall(Blocks.END_STONE_BRICKS, ALTorch_End_Stone_Bricks, getRegName());
        }
    },
    ALTorch_Wall_Nether_Bricks("al_wall_torch_nether_bricks") {
        @Override
        public Block createBlock() {
            return new ALTorch_Wall(Blocks.NETHER_BRICKS, ALTorch_Nether_Bricks, getRegName());
        }
    },
    ALTorch_Wall_Red_Nether_Bricks("al_wall_torch_red_nether_bricks") {
        @Override
        public Block createBlock() {
            return new ALTorch_Wall(Blocks.RED_NETHER_BRICKS, ALTorch_Red_Nether_Bricks, getRegName());
        }
    },
    ALTorch_Wall_Smooth_Stone("al_wall_torch_smooth_stone") {
        @Override
        public Block createBlock() {
            return new ALTorch_Wall(Blocks.SMOOTH_STONE, ALTorch_Smooth_Stone, getRegName());
        }
    },
    ALTorch_Wall_Glass("al_wall_torch_glass") {
        @Override
        public Block createBlock() {
            return new ALTorch_Wall(Blocks.GLASS, ALTorch_Glass, getRegName());
        }
    },
    ALTorch_Wall_Iron("al_wall_torch_iron_block") {
        @Override
        public Block createBlock() {
            return new ALTorch_Wall(Blocks.IRON_BLOCK, ALTorch_Iron, getRegName());
        }
    },
    ALTorch_Wall_Gold("al_wall_torch_gold_block") {
        @Override
        public Block createBlock() {
            return new ALTorch_Wall(Blocks.GOLD_BLOCK, ALTorch_Gold, getRegName());
        }
    },
    ALTorch_Wall_Diamond("al_wall_torch_diamond_block") {
        @Override
        public Block createBlock() {
            return new ALTorch_Wall(Blocks.DIAMOND_BLOCK, ALTorch_Diamond, getRegName());
        }
    },
    ALTorch_Wall_Ice("al_wall_torch_packed_ice") {
        @Override
        public Block createBlock() {
            return new ALTorch_Wall(Blocks.PACKED_ICE, ALTorch_Ice, getRegName());
        }
    },
    ALTorch_Wall_Pink_Wool("al_wall_torch_pink_wool") {
        @Override
        public Block createBlock() {
            return new ALTorch_Wall(Blocks.PINK_WOOL, ALTorch_Pink_Wool, getRegName());
        }
    },
    ALTorch_Wall_Magenta_Wool("al_wall_torch_magenta_wool") {
        @Override
        public Block createBlock() {
            return new ALTorch_Wall(Blocks.MAGENTA_WOOL, ALTorch_Magenta_Wool, getRegName());
        }
    },
    ALTorch_Wall_Crimson("al_wall_torch_crimson_planks") {
        @Override
        public Block createBlock() {
            return new ALTorch_Wall(Blocks.CRIMSON_PLANKS, ALTorch_Crimson, getRegName());
        }
    },
    ALTorch_Wall_Warped("al_wall_torch_warped_planks") {
        @Override
        public Block createBlock() {
            return new ALTorch_Wall(Blocks.WARPED_PLANKS, ALTorch_Warped, getRegName());
        }
    },
    ALTorch_Wall_BlackStone("al_wall_torch_blackstone") {
        @Override
        public Block createBlock() {
            return new ALTorch_Wall(Blocks.BLACKSTONE, ALTorch_BlackStone, getRegName());
        }
    },

    StandingTorch_S_Stone_Bricks("standing_torch_s_stone_bricks") {
        @Override
        public Block createBlock() {
            return new StandingTorch_S(Blocks.STONE_BRICKS, getRegName());
        }
    },
    StandingTorch_S_Mossy_Stone_Bricks("standing_torch_s_mossy_stone_bricks") {
        @Override
        public Block createBlock() {
            return new StandingTorch_S(Blocks.MOSSY_STONE_BRICKS, getRegName());
        }
    },
    StandingTorch_S_End_Stone_Bricks("standing_torch_s_end_stone_bricks") {
        @Override
        public Block createBlock() {
            return new StandingTorch_S(Blocks.END_STONE_BRICKS, getRegName());
        }
    },
    StandingTorch_S_Nether_Bricks("standing_torch_s_nether_bricks") {
        @Override
        public Block createBlock() {
            return new StandingTorch_S(Blocks.NETHER_BRICKS, getRegName());
        }
    },
    StandingTorch_S_Red_Nether_Bricks("standing_torch_s_red_nether_bricks") {
        @Override
        public Block createBlock() {
            return new StandingTorch_S(Blocks.RED_NETHER_BRICKS, getRegName());
        }
    },
    StandingTorch_S_Smooth_Stone("standing_torch_s_smooth_stone") {
        @Override
        public Block createBlock() {
            return new StandingTorch_S(Blocks.SMOOTH_STONE, getRegName());
        }
    },
    StandingTorch_S_Polished_Andesite("standing_torch_s_polished_andesite") {
        @Override
        public Block createBlock() {
            return new StandingTorch_S(Blocks.POLISHED_ANDESITE, getRegName());
        }
    },
    StandingTorch_S_Polished_Diorite("standing_torch_s_polished_diorite") {
        @Override
        public Block createBlock() {
            return new StandingTorch_S(Blocks.POLISHED_DIORITE, getRegName());
        }
    },
    StandingTorch_S_Polished_Granite("standing_torch_s_polished_granite") {
        @Override
        public Block createBlock() {
            return new StandingTorch_S(Blocks.POLISHED_GRANITE, getRegName());
        }
    },
    StandingTorch_S_Stone("standing_torch_s_stone") {
        @Override
        public Block createBlock() {
            return new StandingTorch_S(Blocks.STONE, getRegName());
        }
    },
    StandingTorch_S_CobbleStone("standing_torch_s_cobblestone") {
        @Override
        public Block createBlock() {
            return new StandingTorch_S(Blocks.COBBLESTONE, getRegName());
        }
    },
    StandingTorch_S_Mossy_CobbleStone("standing_torch_s_mossy_cobblestone") {
        @Override
        public Block createBlock() {
            return new StandingTorch_S(Blocks.MOSSY_COBBLESTONE, getRegName());
        }
    },
    StandingTorch_S_Sand_Stone("standing_torch_s_sandstone") {
        @Override
        public Block createBlock() {
            return new StandingTorch_S(Blocks.SANDSTONE, getRegName());
        }
    },
    StandingTorch_S_Cut_Sand_Stone("standing_torch_s_cut_sandstone") {
        @Override
        public Block createBlock() {
            return new StandingTorch_S(Blocks.CUT_SANDSTONE, getRegName());
        }
    },
    StandingTorch_S_End_Stone("standing_torch_s_end_stone") {
        @Override
        public Block createBlock() {
            return new StandingTorch_S(Blocks.END_STONE, getRegName());
        }
    },
    StandingTorch_S_Iron("standing_torch_s_iron_block") {
        @Override
        public Block createBlock() {
            return new StandingTorch_S(Blocks.IRON_BLOCK, getRegName());
        }
    },
    StandingTorch_S_Gold("standing_torch_s_gold_block") {
        @Override
        public Block createBlock() {
            return new StandingTorch_S(Blocks.GOLD_BLOCK, getRegName());
        }
    },
    StandingTorch_S_Diamond("standing_torch_s_diamond_block") {
        @Override
        public Block createBlock() {
            return new StandingTorch_S(Blocks.DIAMOND_BLOCK, getRegName());
        }
    },
    StandingTorch_S_Packed_Ice("standing_torch_s_packed_ice") {
        @Override
        public Block createBlock() {
            return new StandingTorch_S(Blocks.PACKED_ICE, getRegName());
        }
    },
    StandingTorch_S_Pink_Wool("standing_torch_s_pink_wool") {
        @Override
        public Block createBlock() {
            return new StandingTorch_S(Blocks.PINK_WOOL, getRegName());
        }
    },
    StandingTorch_S_Magenta_Wool("standing_torch_s_magenta_wool") {
        @Override
        public Block createBlock() {
            return new StandingTorch_S(Blocks.MAGENTA_WOOL, getRegName());
        }
    },
    StandingTorch_S_Polished_BlackStone("standing_torch_s_polished_blackstone") {
        @Override
        public Block createBlock() {
            return new StandingTorch_S(Blocks.POLISHED_BLACKSTONE, getRegName());
        }
    },

    StandingTorch_L_Stone_Bricks("standing_torch_l_stone_bricks") {
        @Override
        public Block createBlock() {
            return new StandingTorch_L(Blocks.STONE_BRICKS, getRegName());
        }
    },
    StandingTorch_L_Mossy_Stone_Bricks("standing_torch_l_mossy_stone_bricks") {
        @Override
        public Block createBlock() {
            return new StandingTorch_L(Blocks.MOSSY_STONE_BRICKS, getRegName());
        }
    },
    StandingTorch_L_End_Stone_Bricks("standing_torch_l_end_stone_bricks") {
        @Override
        public Block createBlock() {
            return new StandingTorch_L(Blocks.END_STONE_BRICKS, getRegName());
        }
    },
    StandingTorch_L_Nether_Bricks("standing_torch_l_nether_bricks") {
        @Override
        public Block createBlock() {
            return new StandingTorch_L(Blocks.NETHER_BRICKS, getRegName());
        }
    },
    StandingTorch_L_Red_Nether_Bricks("standing_torch_l_red_nether_bricks") {
        @Override
        public Block createBlock() {
            return new StandingTorch_L(Blocks.RED_NETHER_BRICKS, getRegName());
        }
    },
    StandingTorch_L_Smooth_Stone("standing_torch_l_smooth_stone") {
        @Override
        public Block createBlock() {
            return new StandingTorch_L(Blocks.SMOOTH_STONE, getRegName());
        }
    },
    StandingTorch_L_Polished_Andesite("standing_torch_l_polished_andesite") {
        @Override
        public Block createBlock() {
            return new StandingTorch_L(Blocks.POLISHED_ANDESITE, getRegName());
        }
    },
    StandingTorch_L_Polished_Diorite("standing_torch_l_polished_diorite") {
        @Override
        public Block createBlock() {
            return new StandingTorch_L(Blocks.POLISHED_DIORITE, getRegName());
        }
    },
    StandingTorch_L_Polished_Granite("standing_torch_l_polished_granite") {
        @Override
        public Block createBlock() {
            return new StandingTorch_L(Blocks.POLISHED_GRANITE, getRegName());
        }
    },
    StandingTorch_L_Stone("standing_torch_l_stone") {
        @Override
        public Block createBlock() {
            return new StandingTorch_L(Blocks.STONE, getRegName());
        }
    },
    StandingTorch_L_CobbleStone("standing_torch_l_cobblestone") {
        @Override
        public Block createBlock() {
            return new StandingTorch_L(Blocks.COBBLESTONE, getRegName());
        }
    },
    StandingTorch_L_Mossy_CobbleStone("standing_torch_l_mossy_cobblestone") {
        @Override
        public Block createBlock() {
            return new StandingTorch_L(Blocks.MOSSY_COBBLESTONE, getRegName());
        }
    },
    StandingTorch_L_Sand_Stone("standing_torch_l_sandstone") {
        @Override
        public Block createBlock() {
            return new StandingTorch_L(Blocks.SANDSTONE, getRegName());
        }
    },
    StandingTorch_L_Cut_Sand_Stone("standing_torch_l_cut_sandstone") {
        @Override
        public Block createBlock() {
            return new StandingTorch_L(Blocks.CUT_SANDSTONE, getRegName());
        }
    },
    StandingTorch_L_End_Stone("standing_torch_l_end_stone") {
        @Override
        public Block createBlock() {
            return new StandingTorch_L(Blocks.END_STONE, getRegName());
        }
    },
    StandingTorch_L_Iron("standing_torch_l_iron_block") {
        @Override
        public Block createBlock() {
            return new StandingTorch_L(Blocks.IRON_BLOCK, getRegName());
        }
    },
    StandingTorch_L_Gold("standing_torch_l_gold_block") {
        @Override
        public Block createBlock() {
            return new StandingTorch_L(Blocks.GOLD_BLOCK, getRegName());
        }
    },
    StandingTorch_L_Diamond("standing_torch_l_diamond_block") {
        @Override
        public Block createBlock() {
            return new StandingTorch_L(Blocks.DIAMOND_BLOCK, getRegName());
        }
    },
    StandingTorch_L_Packed_Ice("standing_torch_l_packed_ice") {
        @Override
        public Block createBlock() {
            return new StandingTorch_L(Blocks.PACKED_ICE, getRegName());
        }
    },
    StandingTorch_L_Pink_Wool("standing_torch_l_pink_wool") {
        @Override
        public Block createBlock() {
            return new StandingTorch_L(Blocks.PINK_WOOL, getRegName());
        }
    },
    StandingTorch_L_Magenta_Wool("standing_torch_l_magenta_wool") {
        @Override
        public Block createBlock() {
            return new StandingTorch_L(Blocks.MAGENTA_WOOL, getRegName());
        }
    },
    StandingTorch_L_Polished_BlackStone("standing_torch_l_polished_blackstone") {
        @Override
        public Block createBlock() {
            return new StandingTorch_L(Blocks.POLISHED_BLACKSTONE, getRegName());
        }
    },

    FirePit_S_Stone_Bricks("fire_pit_s_stone_bricks") {
        @Override
        public Block createBlock() {
            return new FirePit_S(Blocks.STONE_BRICKS, getRegName());
        }
    },
    FirePit_S_Mossy_Stone_Bricks("fire_pit_s_mossy_stone_bricks") {
        @Override
        public Block createBlock() {
            return new FirePit_S(Blocks.MOSSY_STONE_BRICKS, getRegName());
        }
    },
    FirePit_S_End_Stone_Bricks("fire_pit_s_end_stone_bricks") {
        @Override
        public Block createBlock() {
            return new FirePit_S(Blocks.END_STONE_BRICKS, getRegName());
        }
    },
    FirePit_S_Nether_Bricks("fire_pit_s_nether_bricks") {
        @Override
        public Block createBlock() {
            return new FirePit_S(Blocks.NETHER_BRICKS, getRegName());
        }
    },
    FirePit_S_Red_Nether_Bricks("fire_pit_s_red_nether_bricks") {
        @Override
        public Block createBlock() {
            return new FirePit_S(Blocks.RED_NETHER_BRICKS, getRegName());
        }
    },
    FirePit_S_Smooth_Stone("fire_pit_s_smooth_stone") {
        @Override
        public Block createBlock() {
            return new FirePit_S(Blocks.SMOOTH_STONE, getRegName());
        }
    },
    FirePit_S_Polished_Andesite("fire_pit_s_polished_andesite") {
        @Override
        public Block createBlock() {
            return new FirePit_S(Blocks.POLISHED_ANDESITE, getRegName());
        }
    },
    FirePit_S_Polished_Diorite("fire_pit_s_polished_diorite") {
        @Override
        public Block createBlock() {
            return new FirePit_S(Blocks.POLISHED_DIORITE, getRegName());
        }
    },
    FirePit_S_Polished_Granite("fire_pit_s_polished_granite") {
        @Override
        public Block createBlock() {
            return new FirePit_S(Blocks.POLISHED_GRANITE, getRegName());
        }
    },
    FirePit_S_Stone("fire_pit_s_stone") {
        @Override
        public Block createBlock() {
            return new FirePit_S(Blocks.STONE, getRegName());
        }
    },
    FirePit_S_CobbleStone("fire_pit_s_cobblestone") {
        @Override
        public Block createBlock() {
            return new FirePit_S(Blocks.COBBLESTONE, getRegName());
        }
    },
    FirePit_S_Mossy_CobbleStone("fire_pit_s_mossy_cobblestone") {
        @Override
        public Block createBlock() {
            return new FirePit_S(Blocks.MOSSY_COBBLESTONE, getRegName());
        }
    },
    FirePit_S_Sand_Stone("fire_pit_s_sandstone") {
        @Override
        public Block createBlock() {
            return new FirePit_S(Blocks.SANDSTONE, getRegName());
        }
    },
    FirePit_S_Cut_Sand_Stone("fire_pit_s_cut_sandstone") {
        @Override
        public Block createBlock() {
            return new FirePit_S(Blocks.CUT_SANDSTONE, getRegName());
        }
    },
    FirePit_S_End_Stone("fire_pit_s_end_stone") {
        @Override
        public Block createBlock() {
            return new FirePit_S(Blocks.END_STONE, getRegName());
        }
    },
    FirePit_S_Iron("fire_pit_s_iron_block") {
        @Override
        public Block createBlock() {
            return new FirePit_S(Blocks.IRON_BLOCK, getRegName());
        }
    },
    FirePit_S_Gold("fire_pit_s_gold_block") {
        @Override
        public Block createBlock() {
            return new FirePit_S(Blocks.GOLD_BLOCK, getRegName());
        }
    },
    FirePit_S_Diamond("fire_pit_s_diamond_block") {
        @Override
        public Block createBlock() {
            return new FirePit_S(Blocks.DIAMOND_BLOCK, getRegName());
        }
    },
    FirePit_S_Ice("fire_pit_s_packed_ice") {
        @Override
        public Block createBlock() {
            return new FirePit_S(Blocks.PACKED_ICE, getRegName());
        }
    },
    FirePit_S_Pink_Wool("fire_pit_s_pink_wool") {
        @Override
        public Block createBlock() {
            return new FirePit_S(Blocks.PINK_WOOL, getRegName());
        }
    },
    FirePit_S_Magenta_Wool("fire_pit_s_magenta_wool") {
        @Override
        public Block createBlock() {
            return new FirePit_S(Blocks.MAGENTA_WOOL, getRegName());
        }
    },
    FirePit_S_Polished_BlackStone("fire_pit_s_polished_blackstone") {
        @Override
        public Block createBlock() {
            return new FirePit_S(Blocks.POLISHED_BLACKSTONE, getRegName());
        }
    },

    FirePit_L_Stone_Bricks("fire_pit_l_stone_bricks") {
        @Override
        public Block createBlock() {
            return new FirePit_L(Blocks.STONE_BRICKS, getRegName());
        }
    },
    FirePit_L_Mossy_Stone_Bricks("fire_pit_l_mossy_stone_bricks") {
        @Override
        public Block createBlock() {
            return new FirePit_L(Blocks.MOSSY_STONE_BRICKS, getRegName());
        }
    },
    FirePit_L_End_Stone_Bricks("fire_pit_l_end_stone_bricks") {
        @Override
        public Block createBlock() {
            return new FirePit_L(Blocks.END_STONE_BRICKS, getRegName());
        }
    },
    FirePit_L_Nether_Bricks("fire_pit_l_nether_bricks") {
        @Override
        public Block createBlock() {
            return new FirePit_L(Blocks.NETHER_BRICKS, getRegName());
        }
    },
    FirePit_L_Red_Nether_Bricks("fire_pit_l_red_nether_bricks") {
        @Override
        public Block createBlock() {
            return new FirePit_L(Blocks.RED_NETHER_BRICKS, getRegName());
        }
    },
    FirePit_L_Smooth_Stone("fire_pit_l_smooth_stone") {
        @Override
        public Block createBlock() {
            return new FirePit_L(Blocks.SMOOTH_STONE, getRegName());
        }
    },
    FirePit_L_Polished_Andesite("fire_pit_l_polished_andesite") {
        @Override
        public Block createBlock() {
            return new FirePit_L(Blocks.POLISHED_ANDESITE, getRegName());
        }
    },
    FirePit_L_Polished_Diorite("fire_pit_l_polished_diorite") {
        @Override
        public Block createBlock() {
            return new FirePit_L(Blocks.POLISHED_DIORITE, getRegName());
        }
    },
    FirePit_L_Polished_Granite("fire_pit_l_polished_granite") {
        @Override
        public Block createBlock() {
            return new FirePit_L(Blocks.POLISHED_GRANITE, getRegName());
        }
    },
    FirePit_L_Stone("fire_pit_l_stone") {
        @Override
        public Block createBlock() {
            return new FirePit_L(Blocks.STONE, getRegName());
        }
    },
    FirePit_L_CobbleStone("fire_pit_l_cobblestone") {
        @Override
        public Block createBlock() {
            return new FirePit_L(Blocks.COBBLESTONE, getRegName());
        }
    },
    FirePit_L_Mossy_CobbleStone("fire_pit_l_mossy_cobblestone") {
        @Override
        public Block createBlock() {
            return new FirePit_L(Blocks.MOSSY_COBBLESTONE, getRegName());
        }
    },
    FirePit_L_Sand_Stone("fire_pit_l_sandstone") {
        @Override
        public Block createBlock() {
            return new FirePit_L(Blocks.SANDSTONE, getRegName());
        }
    },
    FirePit_L_Cut_Sand_Stone("fire_pit_l_cut_sandstone") {
        @Override
        public Block createBlock() {
            return new FirePit_L(Blocks.CUT_SANDSTONE, getRegName());
        }
    },
    FirePit_L_End_Stone("fire_pit_l_end_stone") {
        @Override
        public Block createBlock() {
            return new FirePit_L(Blocks.END_STONE, getRegName());
        }
    },
    FirePit_L_Iron("fire_pit_l_iron_block") {
        @Override
        public Block createBlock() {
            return new FirePit_L(Blocks.IRON_BLOCK, getRegName());
        }
    },
    FirePit_L_Gold("fire_pit_l_gold_block") {
        @Override
        public Block createBlock() {
            return new FirePit_L(Blocks.GOLD_BLOCK, getRegName());
        }
    },
    FirePit_L_Diamond("fire_pit_l_diamond_block") {
        @Override
        public Block createBlock() {
            return new FirePit_L(Blocks.DIAMOND_BLOCK, getRegName());
        }
    },
    FirePit_L_Ice("fire_pit_l_packed_ice") {
        @Override
        public Block createBlock() {
            return new FirePit_L(Blocks.PACKED_ICE, getRegName());
        }
    },
    FirePit_L_Pink_Wool("fire_pit_l_pink_wool") {
        @Override
        public Block createBlock() {
            return new FirePit_L(Blocks.PINK_WOOL, getRegName());
        }
    },
    FirePit_L_Magenta_Wool("fire_pit_l_magenta_wool") {
        @Override
        public Block createBlock() {
            return new FirePit_L(Blocks.MAGENTA_WOOL, getRegName());
        }
    },
    FirePit_L_Polished_BlackStone("fire_pit_l_polished_blackstone") {
        @Override
        public Block createBlock() {
            return new FirePit_L(Blocks.POLISHED_BLACKSTONE, getRegName());
        }
    },

    Fire_For_StandingTorch_S("fire_for_standing_torch_s") {
        @Override
        public Block createBlock() {
            return new Fire(PedestalTypes.standing_torch_s, getRegName());
        }
    },
    Fire_For_StandingTorch_L("fire_for_standing_torch_l") {
        @Override
        public Block createBlock() {
            return new Fire(PedestalTypes.standing_torch_l, getRegName());
        }
    },
    Fire_For_FirePit_S("fire_for_fire_pit_s") {
        @Override
        public Block createBlock() {
            return new Fire(PedestalTypes.fire_pit_s, getRegName());
        }
    },
    Fire_For_FirePit_L("fire_for_fire_pit_l") {
        @Override
        public Block createBlock() {
            return new Fire(PedestalTypes.fire_pit_l, getRegName());
        }
    },

    SoulFire_For_StandingTorch_S("soul_fire_for_standing_torch_s") {
        @Override
        public Block createBlock() {
            return new Fire_Soul(PedestalTypes.standing_torch_s, getRegName());
        }
    },
    SoulFire_For_StandingTorch_L("soul_fire_for_standing_torch_l") {
        @Override
        public Block createBlock() {
            return new Fire_Soul(PedestalTypes.standing_torch_l, getRegName());
        }
    },
    SoulFire_For_FirePit_S("soul_fire_for_fire_pit_s") {
        @Override
        public Block createBlock() {
            return new Fire_Soul(PedestalTypes.fire_pit_s, getRegName());
        }
    },
    SoulFire_For_FirePit_L("soul_fire_for_fire_pit_l") {
        @Override
        public Block createBlock() {
            return new Fire_Soul(PedestalTypes.fire_pit_l, getRegName());
        }
    };

    private final String regName;

    <I> ModBlockList(String regName) {
        this.regName = regName;
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

    public abstract Block createBlock();

    public void register(){
        AdditionalLights.modBlocks.put( this, AdditionalLights.BLOCKS.register( getRegName(), () -> createBlock() ) );

        if( getRegName().contains("al_wall_torch") )
            return;

        if( getRegName().contains("al_torch") )
        {
            var wallkey = ModBlockList.valueOf( this.name().replace("ALTorch", "ALTorch_Wall") );
            AdditionalLights.modBlockItems.put( this, AdditionalLights.ITEMS.register( getRegName(), () -> new StandingAndWallBlockItem( getBlock(), wallkey.getBlock(), Direction.DOWN, new Item.Properties().setId( AdditionalLights.createItemResourceKey(regName)) )));
        }
        else
            AdditionalLights.modBlockItems.put( this, AdditionalLights.ITEMS.register( getRegName(), () -> new BlockItem( getBlock(), new Item.Properties().setId( AdditionalLights.createItemResourceKey(regName)))));
    }
}
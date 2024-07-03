package com.mgen256.al.blocks;

import net.minecraft.world.BlockView;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.item.BlockItem;
import net.minecraft.util.shape.VoxelShape;


public abstract class ModBlock extends Block {

    protected ModBlock(Settings settings, VoxelShape shape) {
        super(settings);
        this.voxelShape = shape;
    }

    protected BlockItem blockItem;
    private VoxelShape voxelShape;

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return voxelShape;
    }
    
    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }
}
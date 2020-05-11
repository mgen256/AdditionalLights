
package com.mgen256.al.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.IWaterLoggable;
import net.minecraft.fluid.Fluids;
import net.minecraft.fluid.IFluidState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.fluid.Fluid;
import net.minecraft.state.StateContainer;
import javax.annotation.Nullable;


public abstract class StandingTorchBase extends ModBlock implements IWaterLoggable {


    private static Properties createProps(Block mainblock){
        Properties p = createBasicProps(mainblock);
        p.lightValue(0);
        return p;
    }
    
    public StandingTorchBase(String basename, Block mainblock, VoxelShape shape) {
        super(basename, mainblock, createProps(mainblock), shape);
    }

    
    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext p_196258_1_) {
        return getDefaultState().with(BlockStateProperties.AXIS, p_196258_1_.getFace().getAxis())
                .with(BlockStateProperties.WATERLOGGED, false);
    }

    @Override
    public boolean canContainFluid(IBlockReader p_204510_1_, BlockPos p_204510_2_, BlockState p_204510_3_,
            Fluid p_204510_4_) {
        return true;
    }

    @Override
    public IFluidState getFluidState(BlockState p_204507_1_) {
        return p_204507_1_.get(BlockStateProperties.WATERLOGGED) ? Fluids.WATER.getStillFluidState(false)
                : super.getFluidState(p_204507_1_);
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> p_206840_1_) {
        p_206840_1_.add(BlockStateProperties.AXIS, BlockStateProperties.WATERLOGGED);
    }
}
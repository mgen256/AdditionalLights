package com.mgen256.al.blocks;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.IWaterLoggable;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.fluid.IFluidState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;


public abstract class FirePitBase extends FireBlock implements IWaterLoggable  {

    private static Properties createProps(Block mainblock){
        Properties p = createBasicProps(mainblock);
        p.lightValue(15);
        return p;
    }

    public FirePitBase( String basename, Block mainblock, VoxelShape shape ) {
        super(basename, mainblock, createProps(mainblock), shape, ParticleTypes.LARGE_SMOKE);
    }

    @Override
    protected float getFireDamageAmount() {
        return 1.0F;
    }

    
    @Nullable
    @Override
    public BlockState getStateForPlacement(final BlockItemUseContext p_196258_1_) {
        return getDefaultState().with(BlockStateProperties.AXIS, p_196258_1_.getFace().getAxis())
                .with(BlockStateProperties.WATERLOGGED, false);
    }

    @Override
    public boolean canContainFluid(final IBlockReader p_204510_1_, final BlockPos p_204510_2_, final BlockState p_204510_3_,
            final Fluid p_204510_4_) {
        return true;
    }

    @Override
    public IFluidState getFluidState(final BlockState p_204507_1_) {
        return p_204507_1_.get(BlockStateProperties.WATERLOGGED) ? Fluids.WATER.getStillFluidState(false)
                : super.getFluidState(p_204507_1_);
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> p_206840_1_) {
        p_206840_1_.add(BlockStateProperties.AXIS, BlockStateProperties.WATERLOGGED);
    }
}

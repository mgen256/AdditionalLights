package com.mgen256.al.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.IWaterLoggable;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BlockItem;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.IFluidState;
import javax.annotation.Nullable;

import com.mgen256.al.AdditionalLights;



public class ALLamp extends ModBlock implements IWaterLoggable{

    // D-U-N-S-W-E
    private static VoxelShape[] SHAPES = {
        Block.makeCuboidShape( 5.0, 15.0, 5.0, 11.0, 16.0, 11.0), // down
        Block.makeCuboidShape( 5.0, 0.0, 5.0, 11.0, 1.0, 11.0), // up

        Block.makeCuboidShape(6.0, 7.0, 12.0, 10.0, 13.0, 16.0), // north
        Block.makeCuboidShape(6.0, 7.0, 0.0, 10.0, 13.0, 4.0), // south
        Block.makeCuboidShape(12.0, 7.0, 6.0, 16.0, 13.0, 10.0), // west
        Block.makeCuboidShape(0.0, 7.0, 6.0, 4.0, 13.0, 10.0), // east
    }; 


    private static Properties createProps(Block mainblock){
        Properties p = createBasicProps(mainblock);
        p.lightValue(15);
        p.hardnessAndResistance(0.0f);
        p.doesNotBlockMovement();
        return p;
    }

    public ALLamp(Block mainblock ) {
        super( "al_lamp_", mainblock, createProps(mainblock), VoxelShapes.empty());
      }

    @Override
    public void init() {
        setRegistryName(name);
        blockItem = new BlockItem(this, AdditionalLights.ItemProps);
        blockItem.setRegistryName(getRegistryName());
    }
      
    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(BlockStateProperties.FACING, BlockStateProperties.WATERLOGGED);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        boolean waterlogged = context.getWorld().getFluidState(context.getPos()).getFluid() == Fluids.WATER;
        return getDefaultState().with(BlockStateProperties.FACING, context.getFace()).with(BlockStateProperties.WATERLOGGED, waterlogged);
    }

    
    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        Direction facing = state.get(BlockStateProperties.FACING);
        return SHAPES[facing.getIndex()];
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
    public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
        return facing.getOpposite() == stateIn.get(BlockStateProperties.FACING) 
            && !stateIn.isValidPosition(worldIn, currentPos) ? Blocks.AIR.getDefaultState() : stateIn;
     }
     
     public boolean isValidPosition(BlockState state, IWorldReader worldIn, BlockPos pos) {
        Direction direction = state.get(BlockStateProperties.FACING);
        BlockPos blockpos = pos.offset(direction.getOpposite());
        BlockState blockstate = worldIn.getBlockState(blockpos);
        return blockstate.func_224755_d(worldIn, blockpos, direction);
    }
}
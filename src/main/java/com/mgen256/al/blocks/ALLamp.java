package com.mgen256.al.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.IWaterLoggable;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.block.material.PushReaction;
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
        Block.makeCuboidShape( 5.0, 14.0, 5.0, 11.0, 16.0, 11.0), // down
        Block.makeCuboidShape( 5.0, 0.0, 5.0, 11.0, 2.0, 11.0), // up

        Block.makeCuboidShape(6.0, 7.0, 12.0, 10.0, 13.0, 16.0), // north
        Block.makeCuboidShape(6.0, 7.0, 0.0, 10.0, 13.0, 4.0), // south
        Block.makeCuboidShape(12.0, 7.0, 6.0, 16.0, 13.0, 10.0), // west
        Block.makeCuboidShape(0.0, 7.0, 6.0, 4.0, 13.0, 10.0), // east
    }; 


    private static Properties createProps(Block mainblock){
       Material mbm = mainblock.getMaterial(null);

       Material material = new Material(
        MaterialColor.AIR,
        false, //isLiquid
        true,  //isSolid
        true, //Blocks Movement
        mbm.isOpaque(), //isOpaque
        true, //requires no tool
        false, //isFlammable
        false, //isReplaceable
        PushReaction.NORMAL
        );

        Properties p = Block.Properties.create(material);
        p.lightValue(15);
        p.hardnessAndResistance(0.0f);
        p.doesNotBlockMovement();
        p.sound(mainblock.getSoundType(null));
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

        Direction direction;
        direction = GrassIsPlaced( context.getWorld(), context.getPos() ) ? Direction.UP : context.getFace();
        return getDefaultState().with(BlockStateProperties.FACING, direction).with(BlockStateProperties.WATERLOGGED, waterlogged);
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        Direction facing = state.get(BlockStateProperties.FACING);
        return SHAPES[facing.getIndex()];
    }

    @Override
    public BlockRenderType getRenderType(final BlockState state) {
        return BlockRenderType.MODEL;
    }
    
    @Override
    public boolean canContainFluid(IBlockReader worldIn, BlockPos pos, BlockState state, Fluid fluidIn) {
        return true;
    }

    @Override
    public IFluidState getFluidState(final BlockState state) {
        return state.get(BlockStateProperties.WATERLOGGED) ? Fluids.WATER.getStillFluidState(false)
                : super.getFluidState(state);
    }

    @Override
    public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState
        , IWorld worldIn, BlockPos currentPos, BlockPos facingPos) 
    {
        return facing == stateIn.get(BlockStateProperties.FACING).getOpposite() 
            && !stateIn.isValidPosition(worldIn, currentPos) ? Blocks.AIR.getDefaultState() : stateIn;
    }

    @Override
    public boolean isValidPosition(BlockState state, IWorldReader worldIn, BlockPos pos) {

        if( GrassIsPlaced( worldIn, pos ) )
            return !worldIn.isAirBlock(pos.down());

        Direction direction = state.get(BlockStateProperties.FACING);
        BlockPos blockpos = pos.offset(direction.getOpposite());

        return !worldIn.isAirBlock( blockpos );
    }

    private boolean GrassIsPlaced( IWorldReader worldIn, BlockPos pos )
    {
        Material material = worldIn.getBlockState(pos).getMaterial();
        return ( material == Material.TALL_PLANTS || material == Material.PLANTS || material == Material.SEA_GRASS );
    }
}
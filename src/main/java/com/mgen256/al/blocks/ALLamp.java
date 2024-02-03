package com.mgen256.al.blocks;

import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;



public class ALLamp extends ModBlock implements SimpleWaterloggedBlock{

    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    
    // D-U-N-S-W-E
    private static VoxelShape[] SHAPES = {
        Block.box( 5.0, 14.0, 5.0, 11.0, 16.0, 11.0), // down
        Block.box( 5.0, 0.0, 5.0, 11.0, 2.0, 11.0), // up

        Block.box(6.0, 7.0, 12.0, 10.0, 13.0, 16.0), // north
        Block.box(6.0, 7.0, 0.0, 10.0, 13.0, 4.0), // south
        Block.box(12.0, 7.0, 6.0, 16.0, 13.0, 10.0), // west
        Block.box(0.0, 7.0, 6.0, 4.0, 13.0, 10.0), // east
    }; 

    private static Properties createProps( Block mainblock ){
        return BlockBehaviour.Properties.of()
            .sound( mainblock.defaultBlockState().getSoundType() )
            .mapColor( MapColor.NONE )
            .pushReaction( PushReaction.NORMAL )
            .instabreak()
            .lightLevel( lightLevel -> 15 )
            .noCollission();
    }

    public ALLamp(Block mainblock ) {
        super( mainblock, createProps(mainblock), Shapes.empty());
      }
      
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(BlockStateProperties.FACING, BlockStateProperties.WATERLOGGED);
    }
    
    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        var waterlogged = context.getLevel().getFluidState(context.getClickedPos()).getType() == Fluids.WATER;
        var direction = willBeReplaced( context.getLevel(), context.getClickedPos() ) ? Direction.UP : context.getClickedFace();

        return defaultBlockState().setValue(BlockStateProperties.FACING, direction).setValue(BlockStateProperties.WATERLOGGED, waterlogged);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter blockgetter, BlockPos pos, CollisionContext context) {
        Direction facing = state.getValue(BlockStateProperties.FACING);
        return SHAPES[facing.get3DDataValue()];
    }

    @Override
    public RenderShape getRenderShape( BlockState state ) {
        return RenderShape.MODEL;
    }
    
    @Override
    public boolean canPlaceLiquid(@Nullable Player player, BlockGetter blockgetter, BlockPos pos, BlockState state, Fluid fluidIn) {
        return true;
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.getValue(BlockStateProperties.WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Override
    public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState
        , LevelAccessor level, BlockPos currentPos, BlockPos facingPos) 
    {
        return facing == stateIn.getValue(BlockStateProperties.FACING).getOpposite() 
            && !stateIn.canSurvive(level, currentPos) ? Blocks.AIR.defaultBlockState() : stateIn;
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {

        if( willBeReplaced( level, pos ) ) 
            return !level.isEmptyBlock(pos.below());

        var direction = state.getValue(BlockStateProperties.FACING);
        var blockpos = pos.relative(direction.getOpposite());

        return !level.isEmptyBlock( blockpos ) && !( level.getBlockState(blockpos).getBlock() == this );
    }

    private boolean willBeReplaced( LevelReader level, BlockPos pos ) {
        var blockstate = level.getBlockState(pos);
        return blockstate.canBeReplaced() && ( blockstate.isAir() == false && blockstate.getBlock() != Blocks.WATER );
    }
}
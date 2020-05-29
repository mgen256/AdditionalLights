package com.mgen256.al.blocks;

import net.minecraft.block.Block;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.EnumFacing.Plane;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

import javax.annotation.Nullable;

import com.mgen256.al.AdditionalLights;


// implements IWaterLoggable
public class ALLamp extends ModBlock {
    static PropertyDirection FACING = PropertyDirection.create("facing");
    //private static List<EnumFacing> FACINGS = Arrays.asList( EnumFacing.NORTH, EnumFacing.EAST, EnumFacing.SOUTH, EnumFacing.WEST, EnumFacing.UP, EnumFacing.DOWN ); 
   // private static final EnumFacing[] FACING = new EnumFacing[] {EnumFacing.NORTH, EnumFacing.EAST, EnumFacing.SOUTH, EnumFacing.WEST, EnumFacing.UP, EnumFacing.DOWN };
    //public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);

    /*
    AxisAlignedBB AABB_UPDOWN = new AxisAlignedBB(0.4375,0.0,0.4375,0.5625,1.0,0.5625);
    AxisAlignedBB AABB_EASTWEST = new AxisAlignedBB(0.0,0.4375,0.4375,1.0,0.5625,0.5625);
    AxisAlignedBB AABB_NORTHSOUTH = new AxisAlignedBB(0.4375,0.4375,0.0,0.5625,0.5625,1.0);
    private static PropertyEnum<EnumFacing.Axis> AXIS = PropertyEnum.create("axis", EnumFacing.Axis.class);
*/

    // D-U-N-S-W-E
    private static AxisAlignedBB[] SHAPES = {
        new AxisAlignedBB( 0.3125, 0.875, 0.3125, 0.6875, 1, 0.6875), // down
        new AxisAlignedBB( 0.3125, 0.0, 0.3125, 0.6875, 0.125, 0.6875), // up
        
        

        new AxisAlignedBB(0.375, 0.4375, 0.75, 0.625, 0.8125, 1), // north
        new AxisAlignedBB(0.375, 0.4375, 0.0, 0.625, 0.8125, 0.25), // south
        new AxisAlignedBB(0.75, 0.4375, 0.375, 1, 0.8125, 0.625), // west
        new AxisAlignedBB(0.0, 0.4375, 0.375, 0.25, 0.8125, 0.625), // east

  /*      new AxisAlignedBB( 5.0, 14.0, 5.0, 11.0, 16.0, 11.0), // down
        new AxisAlignedBB( 5.0, 0.0, 5.0, 11.0, 2.0, 11.0), // up

        new AxisAlignedBB(6.0, 7.0, 12.0, 10.0, 13.0, 16.0), // north
        new AxisAlignedBB(6.0, 7.0, 0.0, 10.0, 13.0, 4.0), // south
        new AxisAlignedBB(12.0, 7.0, 6.0, 16.0, 13.0, 10.0), // west
        new AxisAlignedBB(0.0, 7.0, 6.0, 4.0, 13.0, 10.0), // east
        */
    };

/*
    private static Properties createProps(Block mainblock){
       Material mbm = mainblock.getMaterial(null);
       Material material = new Material(
        mbm.getColor(),
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
*/

    public ALLamp(final Block mainblock ) {
        super( "al_lamp_", mainblock);
      }

            
    @Override
    @SideOnly(Side.CLIENT)
    public boolean isOpaqueCube( IBlockState state ){
        return false;
    }

    @Override
    public boolean isFullCube( IBlockState state ){
        return false;
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, FACING);
        //return new BlockStateContainer(this, new IProperty[] {FACING});
	}

    @Override
    public BlockFaceShape getBlockFaceShape( IBlockAccess access, IBlockState state, BlockPos pos, EnumFacing facing) {
        return facing.getOpposite() == state.getValue(FACING) ? BlockFaceShape.SOLID : BlockFaceShape.UNDEFINED;
    }

    @Override
    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing
    , float hitX, float hitY, float hitZ, int meta,EntityLivingBase placer) {
        return getDefaultState().withProperty(FACING, facing);//
    }

    @Override
    public boolean canPlaceBlockOnSide( World worldIn, BlockPos pos, EnumFacing facing ){
        return this.canPlaceBlockAt(worldIn, pos);
    }

    @Override
    public int getMetaFromState( IBlockState state ) {
        return state.getValue(FACING).getIndex();
    }

    @Override
    public IBlockState getStateFromMeta( int meta ) {
        EnumFacing facing;
        switch( meta ) {
            case 0: facing = EnumFacing.UP; break;
            case 1: facing = EnumFacing.DOWN; break;
            case 2: facing = EnumFacing.EAST; break;
            case 3: facing = EnumFacing.WEST; break;
            case 4: facing = EnumFacing.NORTH; break;
            case 5: facing = EnumFacing.SOUTH; break;
            default: facing = EnumFacing.UP; break;
        }
        return getDefaultState().withProperty( FACING, facing );
        //return getDefaultState().withProperty( FACING, EnumFacing.values()[meta] );
    }



    @Override
    public IBlockState withRotation( IBlockState state, Rotation rot ) {
        return state.withProperty(FACING, rot.rotate(state.getValue(FACING)));
    }

    @Override
    public IBlockState withMirror( IBlockState state, Mirror mirrorIn ) {
        return state.withRotation(mirrorIn.toRotation(state.getValue(FACING)));
    }


      /*


    @Override
    public BlockFaceShape getBlockFaceShape( IBlockAccess access, IBlockState state, BlockPos pos, EnumFacing facing ) {
        return facing.getAxis() == state.getValue(AXIS) ? BlockFaceShape.CENTER : BlockFaceShape.UNDEFINED;
    }
    
    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing
         , float hitX, float hitY, float hitZ, int meta,EntityLivingBase placer) {
        return this.getDefaultState().withProperty( AXIS, facing.getAxis() );
    }

    @Override
    public boolean canPlaceBlockOnSide( World worldIn, BlockPos pos, EnumFacing facing ){
        BlockFaceShape shape = worldIn.getBlockState(pos.offset(facing.getOpposite())).getBlockFaceShape(worldIn,pos,facing);
        return shape == BlockFaceShape.SOLID || shape == BlockFaceShape.CENTER || shape == BlockFaceShape.CENTER_BIG;
    }



    @Override
    public BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, AXIS);
    }

    @Override
    public int getMetaFromState( IBlockState state ) {
        Axis axis = state.getValue(AXIS);
        if( axis.isHorizontal() )
            return 0;
        if( axis.isVertical() )
            return 1;
        return 2;
    }
    
    @Override
    public IBlockState getStateFromMeta( int meta ) {
        Axis axis;
        switch(meta)
        {
            case 0: axis=EnumFacing.Axis.X; break;
            case 1: axis=EnumFacing.Axis.Y; break;
            default: axis=EnumFacing.Axis.Z;break;
        }
        return getDefaultState().withProperty(AXIS, axis);
    }



*/












/*
    @Override
    public AxisAlignedBB getBoundingBox(final IBlockState state, final IBlockAccess source, final BlockPos pos)  {
        final EnumFacing enumfacing = (EnumFacing)state.getValue(FACING);
        return SHAPES[enumfacing.getIndex()];
    }

/*
    
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
 */
/*  hokan

    @Override
    public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
    return facing == stateIn.get(BlockStateProperties.FACING).getOpposite() 
        && !stateIn.isValidPosition(worldIn, currentPos) ? Blocks.AIR.getDefaultState() : stateIn;
     }

    @Override
    public boolean isValidPosition(BlockState state, IWorldReader worldIn, BlockPos pos) {
        Direction direction = state.get(BlockStateProperties.FACING);
        BlockPos blockpos = pos.offset(direction.getOpposite());
        
        BlockPos p = null;
        int d = direction.getIndex();
        // D-U-N-S-W-E
        switch( direction.getOpposite().getIndex() )
        {
        case 0: p = pos.down(); break;
        case 1: p = pos.up();   break;
        case 2: p = pos.north(); break;
        case 3: p = pos.south(); break;
        case 4: p = pos.west(); break;
        case 5: p = pos.east(); break;
        }

        return hasEnoughSolidSide(worldIn, p, direction );;

    }

  */
}
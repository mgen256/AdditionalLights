
package com.mgen256.al.blocks;

import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.entity.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.*;
import net.minecraft.item.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.*;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;

import javax.annotation.Nullable;

import com.mgen256.al.*;
import com.mgen256.al.items.*;


public abstract class Pedestal extends ModBlock implements IWaterLoggable{

    public static EnumProperty<FireTypes> FIRE_TYPE = EnumProperty.create( "firetype", FireTypes.class );
    public static BooleanProperty ISPOWERED = BooleanProperty.create("ispowered");
    public static BooleanProperty ACTIVATED = BooleanProperty.create("activated");

    public Pedestal( String basename, Block mainblock, Properties props, VoxelShape shape ) {
        super(basename, mainblock, props, shape);

        setDefaultState( getDefaultState()
            .with( BlockStateProperties.WATERLOGGED, false ) 
            .with( FIRE_TYPE, FireTypes.NORMAL )
            .with( ISPOWERED, false ) 
            .with( ACTIVATED, false )
            );

        ignitionSound = basename.contains("_l_") ? AdditionalLights.modSounds.get(ModSoundList.Fire_Ignition_L) 
            : AdditionalLights.modSounds.get(ModSoundList.Fire_Ignition_S);
    }


    private SoundEvent ignitionSound;
    protected abstract ModBlockList getFireKey(BlockState state);
    public abstract PedestalTypes getType( );

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add( BlockStateProperties.WATERLOGGED );
        builder.add( FIRE_TYPE );
        builder.add( ISPOWERED );
        builder.add( ACTIVATED );
    }

    public BlockState setFireType( World worldIn, BlockPos pos, BlockState state, FireTypes fireType ) {
        
        BlockState newState = state.with( FIRE_TYPE, fireType );
        if( worldIn.setBlockState( pos, newState ) )
            return newState;
        
        return state;
    }

    @Override
    public boolean canContainFluid(IBlockReader worldIn, BlockPos pos, BlockState state, Fluid fluidIn) {
        return true;
    }

    @Override
    public IFluidState getFluidState(final BlockState state) {
        return state.get(BlockStateProperties.WATERLOGGED) ? Fluids.WATER.getStillFluidState(false) : super.getFluidState(state);
    }

    private Block getFireBlock(BlockState state){
        return AdditionalLights.modBlocks.get(getFireKey(state));
    }

    public boolean setFire( World worldIn, BlockPos pos, BlockState state, boolean replaceOnly ) {
        BlockPos upperpos = pos.up();
        BlockState upperBlockState = worldIn.getBlockState(upperpos);
        Block upperblock = upperBlockState.getBlock();

        FireBase firebase = null;
        if( upperblock instanceof FireBase )
            firebase = (FireBase)upperblock;

        if( replaceOnly )
        {
            if( firebase == null )
                return false;
        }
        else
        {
            if( ( upperBlockState.isAir() || upperBlockState.getMaterial() == Material.WATER || firebase != null ) == false )
                return false;
        }
        
        if( firebase != null && worldIn.getBlockState( upperpos ).getBlockState().get(FireBase.SUMMONED) == false )
            worldIn.destroyBlock( upperpos, true );

        return worldIn.setBlockState( upperpos, getFireBlock(state).getDefaultState()
            .with(FireBase.SET, true)
            .with(FireBase.SUMMONED, true) );
    }

    public void RemoveFire(World worldIn, BlockPos pos, BlockState state )
    {
        BlockState upperBlockState = worldIn.getBlockState(pos.up());

        if( upperBlockState.getBlock() instanceof FireBase == false )
            return;

        worldIn.setBlockState(pos.up(), Blocks.AIR.getDefaultState() );
    }


   @Override
   public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos
        , PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
    
        if( handIn == Hand.OFF_HAND )
            return ActionResultType.PASS;
    
        ItemStack stack = player.getHeldItem(handIn);
        if( stack.getItem() instanceof Wand )
            return ActionResultType.FAIL;
            
        if( setFire( worldIn, pos, state, false ) == false )
            return ActionResultType.PASS;
        
        playIgnitionSound( worldIn, pos );
        return ActionResultType.SUCCESS;
    }

    private void playIgnitionSound(World worldIn, BlockPos pos)
    {
        //worldIn.playSound( pos.getX(), pos.getY(), pos.getZ(), ignitionSound, SoundCategory.BLOCKS, 1.5f, 1.0f, false );

        worldIn.playSound( null, pos, ignitionSound, SoundCategory.BLOCKS, 1.5f, 1.0f );
    }

    
    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        if( placer.isSneaking() )
            return;
        setFire( worldIn, pos, state, false );
    }

    
    @Override
    public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
        super.neighborChanged(state, worldIn, pos, blockIn, fromPos, isMoving);
        if( worldIn.isBlockPowered(pos) )
        {
            if( worldIn.getBlockState(pos).get(ACTIVATED) )
                return;

            if( setFire( worldIn, pos, state, false ) )
                playIgnitionSound( worldIn, pos );

            worldIn.setBlockState( pos, state.with( ACTIVATED, true ) );
            worldIn.setBlockState( pos, worldIn.getBlockState(pos).with( ISPOWERED, true ) );
        }
        else if( state.get( ISPOWERED ) && state.get( ACTIVATED ) )
        {
            RemoveFire( worldIn, pos, state );
            worldIn.setBlockState( pos, worldIn.getBlockState(pos).with( ACTIVATED, false ) );
            worldIn.setBlockState( pos, worldIn.getBlockState(pos).with( ISPOWERED, false)  );
        }
    }
}
package com.mgen256.al.blocks;

import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.util.Constants;
import net.minecraft.world.*;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.pathfinder.PathComputationType;

import static java.lang.Boolean.TRUE;

import java.util.List;

import javax.annotation.Nullable;

import com.mgen256.al.*;
import com.mgen256.al.items.*;


public abstract class Pedestal extends ModBlock implements SimpleWaterloggedBlock, IHasFire {

    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    public static final BooleanProperty ACCEPT_POWER = BooleanProperty.create("accept_power");
    public static final BooleanProperty ISPOWERED = BooleanProperty.create("ispowered");
    public static final BooleanProperty ACTIVATED = BooleanProperty.create("activated");

    private static Component txt_shift;
    private static Component txt_tips;
    private static Component txt_rightclick;
    private static Component txt_sneaking;
    private static Component txt_signals;
    
    private static Properties createProps( Block mainblock ){
        BlockState mainblockState = mainblock.defaultBlockState();
        Material mbm = mainblockState.getMaterial();

        var prop = BlockBehaviour.Properties.of( mbm, mainblockState.getMapColor(null, null) )
            .destroyTime( mainblockState.getDestroySpeed( null, null ) )
            .explosionResistance( mainblockState.getExplosionResistance( null, null, null ) )
            .sound( mainblockState.getSoundType( null, null, null ) );

        if( mainblockState.requiresCorrectToolForDrops() )
            return prop.requiresCorrectToolForDrops();
            
        return prop;
    }

    protected Pedestal( String basename, Block mainblock, VoxelShape shape ) {
        super(basename, mainblock, createProps(mainblock), shape);

        registerDefaultState( stateDefinition.any()
            .setValue( BlockStateProperties.WATERLOGGED, false ) 
            .setValue( FIRE_TYPE, FireTypes.NORMAL )
            .setValue( PREVIOUS_FIRE_TYPE, FireTypes.NORMAL )
            .setValue( ACCEPT_POWER, true )
            .setValue( ISPOWERED, false )
            .setValue( ACTIVATED, false )
            );

        if( ignitionSound == null )
        {
            ignitionSound = basename.contains("_l_") ? AdditionalLights.modSounds.get(ModSoundList.Fire_Ignition_L) 
                : AdditionalLights.modSounds.get(ModSoundList.Fire_Ignition_S);
        }
    }


    private static SoundEvent ignitionSound;
    protected abstract ModBlockList getFireKey(BlockState state);
    public abstract PedestalTypes getType( );

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add( BlockStateProperties.WATERLOGGED );
        builder.add( FIRE_TYPE );
        builder.add( PREVIOUS_FIRE_TYPE );
        builder.add( ACCEPT_POWER );
        builder.add( ISPOWERED );
        builder.add( ACTIVATED );
    }

    @Override
    public boolean canPlaceLiquid(BlockGetter blockgetter, BlockPos pos, BlockState state, Fluid fluidIn) {
        return true;
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        
        return state.getValue(BlockStateProperties.WATERLOGGED)  == Boolean.TRUE 
            ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    private Block getFireBlock(BlockState state){
        return AdditionalLights.getBlock( getFireKey(state) );
    }
    
    public boolean setFire( Level level, BlockPos pos, BlockState state, boolean replaceOnly ) {
        BlockPos upperpos = pos.above();
        BlockState upperBlockState = level.getBlockState(upperpos);
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
        
        if( firebase != null && level.getBlockState( upperpos ).getValue(FireBase.SUMMONED) == false )
            level.destroyBlock( upperpos, true );

        return level.setBlock( upperpos, getFireBlock(state).defaultBlockState()
            .setValue(FireBase.SET, true)
            .setValue(FireBase.SUMMONED, true)
            , Constants.BlockFlags.DEFAULT );
    }

    public void removeFire(Level level, BlockPos pos, BlockState state )
    {
        if( level.getBlockState(pos.above()).getBlock() instanceof FireBase == false )
            return;

        level.setBlock(pos.above(), Blocks.AIR.defaultBlockState(), Constants.BlockFlags.DEFAULT );
    }


   @Override
   public InteractionResult use(BlockState state, Level level, BlockPos pos
        , Player player, InteractionHand handIn, BlockHitResult hit) {
    
        if( handIn == InteractionHand.OFF_HAND )
            return InteractionResult.PASS;
    
        ItemStack stack = player.getItemInHand(handIn);
        if( stack.getItem() instanceof Wand )
            return InteractionResult.FAIL;
            
        if( setFire( level, pos, state, false ) == false )
            return InteractionResult.PASS;
        
        playIgnitionSound( level, player, state.getBlock(), pos );
        return InteractionResult.SUCCESS;
    }

    private static void playIgnitionSound(Level level, Player player, Block block, BlockPos pos)
    {
        float volume = block instanceof FirePitBase ? 2.0f : 1.5f;
        level.playSound( player, pos, ignitionSound, SoundSource.BLOCKS, volume, 1.0f );
    }

    
    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        if( placer.getOffhandItem().getItem() instanceof SoulWand )
            state = state.setValue( FIRE_TYPE, FireTypes.SOUL );

        if( placer.isSuppressingSlidingDownLadder() )
            level.setBlock( pos, state.setValue( ACCEPT_POWER, false ), Constants.BlockFlags.DEFAULT );
        else
        {
            level.setBlock( pos, state, Constants.BlockFlags.DEFAULT );
            setFire( level, pos, state, false );
        }
    }

    
    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
        state = level.getBlockState(pos);
        if( state.getValue(ACCEPT_POWER) != TRUE )
            return;

        if( level.hasNeighborSignal(pos) )
        {
            if( state.getValue(ACTIVATED) == TRUE )
                return;

            if( setFire( level, pos, state, false ) )
                playIgnitionSound( level, null, state.getBlock(),pos );

            level.setBlock( pos, state.setValue( ISPOWERED, true ).setValue( ACTIVATED, true ), Constants.BlockFlags.DEFAULT );
        }
        else if( state.getValue( ISPOWERED ) && state.getValue( ACTIVATED ) )
        {
            removeFire( level, pos, state );
            level.setBlock( pos, state.setValue( ISPOWERED, false ).setValue( ACTIVATED, false ), Constants.BlockFlags.DEFAULT );
        }
        super.neighborChanged(state, level, pos, blockIn, fromPos, isMoving);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable BlockGetter blockgetter, List<Component> tooltip, TooltipFlag flagIn) {
        if( txt_shift == null )
        {
            if( I18n.exists("additional_lights.txt.shift") != TRUE )
                return;

            txt_shift = new TranslatableComponent( "additional_lights.txt.shift" );
            txt_tips = new TranslatableComponent( "additional_lights.txt.tips" );
            txt_rightclick = new TranslatableComponent( "additional_lights.txt.block.pedestal.rightclick" );
            txt_sneaking = new TranslatableComponent( "additional_lights.txt.block.pedestal.sneaking" );
            txt_signals = new TranslatableComponent( "additional_lights.txt.block.pedestal.signals" );
        }

        if ( Screen.hasShiftDown() )
        {
            tooltip.add( txt_tips );
            tooltip.add( txt_rightclick );
            tooltip.add( txt_sneaking );
            tooltip.add( txt_signals );
        }
        else
        {
            tooltip.add( txt_shift );
        }
    }
    
    @Override
    public boolean isPathfindable(BlockState state, BlockGetter blockgetter, BlockPos pos, PathComputationType type) {
        return false;
    }
}
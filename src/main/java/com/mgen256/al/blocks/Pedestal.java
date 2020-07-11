
package com.mgen256.al.blocks;

import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.entity.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.*;
import net.minecraft.item.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.*;
import net.minecraft.state.*;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.*;

import java.util.List;

import javax.annotation.Nullable;

import com.mgen256.al.*;
import com.mgen256.al.items.*;


public abstract class Pedestal extends ModBlock implements IWaterLoggable, IHasFire {

    public static BooleanProperty ACCEPT_POWER = BooleanProperty.create("accept_power");
    public static BooleanProperty ISPOWERED = BooleanProperty.create("ispowered");
    public static BooleanProperty ACTIVATED = BooleanProperty.create("activated");

    private static StringTextComponent txt_shift;
    private static StringTextComponent txt_tips;
    private static StringTextComponent txt_rightclick;
    private static StringTextComponent txt_sneaking;
    private static StringTextComponent txt_signals;
    
    private static Properties createProps( Block mainblock ){
        BlockState state = mainblock.getDefaultState();
        Material mbm = state.getMaterial();
        
        return Block.Properties.create( mbm, state.getMaterialColor(null, null) )
            .harvestTool( state.getHarvestTool() )
            .harvestLevel( state.getHarvestLevel() )
            .hardnessAndResistance( state.getBlockHardness( null, null ), state.getExplosionResistance( null, null, null ) )
            .sound( state.getSoundType( null, null, null ) );
    }

    public Pedestal( String basename, Block mainblock, VoxelShape shape ) {
        super(basename, mainblock, createProps(mainblock), shape);

        setDefaultState( getDefaultState()
            .with( BlockStateProperties.WATERLOGGED, false ) 
            .with( FIRE_TYPE, FireTypes.NORMAL )
            .with( PREVIOUS_FIRE_TYPE, FireTypes.NORMAL )
            .with( ACCEPT_POWER, true )
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
        builder.add( PREVIOUS_FIRE_TYPE );
        builder.add( ACCEPT_POWER );
        builder.add( ISPOWERED );
        builder.add( ACTIVATED );
    }

    @Override
    public boolean canContainFluid(IBlockReader worldIn, BlockPos pos, BlockState state, Fluid fluidIn) {
        return true;
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.get(BlockStateProperties.WATERLOGGED) ? Fluids.WATER.getStillFluidState(false) : super.getFluidState(state);
    }

    private Block getFireBlock(BlockState state){
        return AdditionalLights.getBlock( getFireKey(state) );
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
        
        playIgnitionSound( worldIn, player, pos );
        return ActionResultType.SUCCESS;
    }

    private void playIgnitionSound(World worldIn, PlayerEntity player, BlockPos pos)
    {
        worldIn.playSound( player, pos, ignitionSound, SoundCategory.BLOCKS, 1.5f, 1.0f );
    }

    
    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        if( placer.getHeldItemOffhand().getItem() instanceof SoulWand )
            state = state.with( FIRE_TYPE, FireTypes.SOUL );

        if( placer.isSneaking() )
            worldIn.setBlockState( pos, state.with( ACCEPT_POWER, false ) );
        else
        {
            worldIn.setBlockState( pos, state );
            setFire( worldIn, pos, state, false );
        }
    }

    
    @Override
    public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
        super.neighborChanged(state, worldIn, pos, blockIn, fromPos, isMoving);

        state = worldIn.getBlockState(pos);
        if( state.get(ACCEPT_POWER) == false )
            return;

        if( worldIn.isBlockPowered(pos) )
        {
            if( state.get(ACTIVATED) )
                return;

            if( setFire( worldIn, pos, state, false ) )
                playIgnitionSound( worldIn, null, pos );

            worldIn.setBlockState( pos, state.with( ISPOWERED, true ).with( ACTIVATED, true ) );
        }
        else if( state.get( ISPOWERED ) && state.get( ACTIVATED ) )
        {
            RemoveFire( worldIn, pos, state );
            worldIn.setBlockState( pos, state.with( ISPOWERED, false ).with( ACTIVATED, false ) );
        }
    }

    
    @OnlyIn(Dist.CLIENT)
    @Override
    public void addInformation(ItemStack stack, @Nullable IBlockReader worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        if( txt_shift == null )
        {
            if( I18n.hasKey("additional_lights.txt.shift") == false )
                return;

            txt_shift = new StringTextComponent( I18n.format( "additional_lights.txt.shift" ) );
            txt_tips = new StringTextComponent( I18n.format( "additional_lights.txt.tips" ) );
            txt_rightclick = new StringTextComponent( I18n.format( "additional_lights.txt.block.pedestal.rightclick" ) );
            txt_sneaking = new StringTextComponent( I18n.format( "additional_lights.txt.block.pedestal.sneaking" ) ) ;
            txt_signals = new StringTextComponent( I18n.format( "additional_lights.txt.block.pedestal.signals" ) );
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
}
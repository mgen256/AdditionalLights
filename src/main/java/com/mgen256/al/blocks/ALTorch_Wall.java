package com.mgen256.al.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.WallTorchBlock;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.annotation.Nullable;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.mgen256.al.*;
import com.mgen256.al.items.SoulWand;

public class ALTorch_Wall extends WallTorchBlock implements IModBlock, IHasFire {
    
    private static final Map<Direction, VoxelShape> SHAPES = Maps.newEnumMap( ImmutableMap.of( 
        Direction.NORTH, Block.makeCuboidShape(5.5D, 2.0D, 11.0D, 10.5D, 13.0D, 16.0D), 
        Direction.SOUTH, Block.makeCuboidShape(5.5D, 2.0D, 0.0D, 10.5D, 13.0D, 5.0D), 
        Direction.WEST, Block.makeCuboidShape(11.0D, 2.0D, 5.5D, 16.0D, 13.0D, 10.5D), 
        Direction.EAST, Block.makeCuboidShape(0.0D, 2.0D, 5.5D, 5.0D, 13.0D, 10.5D)) );

         
    public ALTorch_Wall(Block mainblock, ModBlockList _floorKey ) {
        super(ALTorch.createProps(mainblock));
        name = "al_wall_torch_" + mainblock.getRegistryName().getPath();
        floorKey = _floorKey;
        setDefaultState( stateContainer.getBaseState()
            .with( HORIZONTAL_FACING, Direction.NORTH )
            .with( FIRE_TYPE, FireTypes.NORMAL ) 
            .with( PREVIOUS_FIRE_TYPE, FireTypes.NORMAL ) );
    }

    private ModBlockList floorKey;
    private String name;

    @Override
    public void init() {
        setRegistryName(name);
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        super.fillStateContainer(builder);
        builder.add( FIRE_TYPE );
        builder.add( PREVIOUS_FIRE_TYPE );
    }

    @Override
    public String getName(){
        return name;
    }

    @Override
    public BlockItem getBlockItem() {
        return null;
    }

    @Override
    public int getLightValue(BlockState state) {
        return state.get( FIRE_TYPE ) == FireTypes.SOUL ? 10 : 14;
     }

    @Override
    public boolean notRequireItemRegistration(){
        return true;
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return func_220289_j(state);
    }

    public static VoxelShape func_220289_j(BlockState p_220289_0_) {
        return SHAPES.get(p_220289_0_.get(HORIZONTAL_FACING));
    }
 
    @Override
    public void setRenderLayer() {
        RenderTypeLookup.setRenderLayer(this, name.contains("glass") ? RenderType.getCutout() : RenderType.getSolid() );
    }
    
    @Override
    public void animateTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand) {
        Direction direction = stateIn.get(HORIZONTAL_FACING);
        double dx = (double)pos.getX() + 0.5D;
        double dy = (double)pos.getY() + 0.9D;
        double dz = (double)pos.getZ() + 0.5D;
  
        Direction direction1 = direction.getOpposite();
        double d3 = 0.38D;
        worldIn.addParticle(ParticleTypes.SMOKE, dx + d3 * (double)direction1.getXOffset(), dy, dz + d3 * (double)direction1.getZOffset(), 0.0D, 0.0D, 0.0D);

        IParticleData particleType;
        if( stateIn.get( FIRE_TYPE ) == FireTypes.SOUL )
            particleType = AdditionalLights.getParticle( ModParticleList.SoulFire_Flame );
        else
            particleType = ParticleTypes.FLAME;
            
        worldIn.addParticle(particleType, dx + d3 * (double)direction1.getXOffset(), dy, dz + d3 * (double)direction1.getZOffset(), 0.0D, 0.0D, 0.0D);
       }

    
    @Override
    public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
    
        List<ItemStack> list = new ArrayList<>();
        list.add( new ItemStack( AdditionalLights.getBlockItem( floorKey ) ) );

        return list;
    }
    
    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        if( placer.getHeldItemOffhand().getItem() instanceof SoulWand )
            worldIn.setBlockState( pos, state.with( FIRE_TYPE, FireTypes.SOUL ) );
    }
}
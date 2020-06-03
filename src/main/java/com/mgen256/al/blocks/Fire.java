package com.mgen256.al.blocks;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.mgen256.al.*;

import net.minecraft.block.*;
import net.minecraft.block.material.*;
import net.minecraft.client.renderer.*;
import net.minecraft.item.*;
import net.minecraft.particles.*;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.*;
import net.minecraft.world.*;
import net.minecraft.world.storage.loot.LootContext;

public class Fire extends ModBlock{

    private static Map<FireBlockList, VoxelShape> SHAPES;
    private static Map<FireBlockList, BasicParticleType> PARTICLE_TYPES;
    private static Map<FireBlockList, Double> SMOKE_POS;

    static {
        SHAPES = new LinkedHashMap<FireBlockList, VoxelShape>();
        SHAPES.put( FireBlockList.standing_torch_s, Block.makeCuboidShape(4.0D, -6.0D, 4.0D, 12.0D, 2.0D, 12.0D) );
        SHAPES.put( FireBlockList.standing_torch_l, Block.makeCuboidShape(4.0D, -2.0D, 4.0D, 12.0D, 6.0D, 12.0D) );
        SHAPES.put( FireBlockList.fire_pit_s, Block.makeCuboidShape(0.0D, -10.0D, 0.0D, 16.0D, -1.0D, 16.0D) );
        SHAPES.put( FireBlockList.fire_pit_l, Block.makeCuboidShape(0.0D, -2.0D, 0.0D, 16.0D, 7.0D, 16.0D) );

        PARTICLE_TYPES = new LinkedHashMap<FireBlockList, BasicParticleType>();
        PARTICLE_TYPES.put( FireBlockList.standing_torch_s, ParticleTypes.SMOKE );
        PARTICLE_TYPES.put( FireBlockList.standing_torch_l, ParticleTypes.SMOKE );
        PARTICLE_TYPES.put( FireBlockList.fire_pit_s, ParticleTypes.LARGE_SMOKE );
        PARTICLE_TYPES.put( FireBlockList.fire_pit_l, ParticleTypes.LARGE_SMOKE );

        SMOKE_POS = new LinkedHashMap<FireBlockList, Double>();
        SMOKE_POS.put( FireBlockList.standing_torch_s, 0.2 );
        SMOKE_POS.put( FireBlockList.standing_torch_l, 0.7 );
        SMOKE_POS.put( FireBlockList.fire_pit_s, 0.0 );
        SMOKE_POS.put( FireBlockList.fire_pit_l, 0.8 );
    }

    private static Properties createProps(){
       Material material = new Material(
        MaterialColor.AIR,
        false, //isLiquid
        false,  //isSolid
        true, //Blocks Movement
        true, //isOpaque
        true, //requires no tool
        false, //isFlammable
        false, //isReplaceable
        PushReaction.NORMAL
        );

        Properties p = Block.Properties.create(material);
        p.lightValue(15);
        p.hardnessAndResistance(0.0f);
        p.doesNotBlockMovement();
        //SoundEvents.BLOCK_FIRE_EXTINGUISH
        p.sound(new SoundType(0.5F, 2.0F, SoundEvents.BLOCK_WOOL_BREAK, SoundEvents.BLOCK_WOOL_STEP
            , SoundEvents.BLOCK_STONE_PLACE, SoundEvents.BLOCK_WOOL_HIT, SoundEvents.BLOCK_WOOL_FALL) );
            
        return p;
    }

    public Fire( FireBlockList _baseFireBlock ) {
        super( "fire_for_" + _baseFireBlock, null, createProps(), SHAPES.get(_baseFireBlock));

        baseFireBlock = _baseFireBlock;
      }

      
    private FireBlockList baseFireBlock;
    

    @Override
    public void init() {
        setRegistryName(name);
        blockItem = new BlockItem(this, AdditionalLights.ItemProps);
        blockItem.setRegistryName(getRegistryName());
    }

    protected float getFireDamageAmount() {
        return 0.0F;
    }

    @Override
    public void setRenderLayer() {
        RenderTypeLookup.setRenderLayer(this, RenderType.getCutout());
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return SHAPES.get(baseFireBlock);
    }
    
    @Override
    public void animateTick(final BlockState stateIn, final World worldIn, final BlockPos pos, final Random rand) {
        final double d0 = (double) pos.getX() + 0.5D;
        final double d1 = (double) pos.getY() + SMOKE_POS.get(baseFireBlock);
        final double d2 = (double) pos.getZ() + 0.5D;
        worldIn.addParticle(PARTICLE_TYPES.get(baseFireBlock), d0, d1, d2, 0.0D, 0.0D, 0.0D);
    }

    @Override
    public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState
        , IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
        return facing == Direction.DOWN && !isValidPosition(stateIn, worldIn, currentPos) 
        ? Blocks.AIR.getDefaultState() : stateIn;
    }

    public boolean isValidPosition(BlockState state, IWorldReader worldIn, BlockPos pos) {
        BlockState blockstate = worldIn.getBlockState(pos.down());
        return !worldIn.isAirBlock(pos.down()) && !blockstate.getMaterial().isLiquid();
     }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
        List<ItemStack> list = new ArrayList<>();
        return list;   
     }

/*
    public void onEntityWalk(World worldIn, BlockPos pos, Entity entityIn) {
        if (!entityIn.isImmuneToFire() && entityIn instanceof LivingEntity
                && !EnchantmentHelper.hasFrostWalker((LivingEntity) entityIn)) {
            entityIn.attackEntityFrom(DamageSource.IN_FIRE, getFireDamageAmount());
        }

        super.onEntityWalk(worldIn, pos, entityIn);
    }
*/
}
package com.mgen256.al.blocks;
import com.mgen256.al.BlockSpec;

import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import com.mgen256.al.AdditionalLightsNeoForge;
import com.mgen256.al.ModBlockList;
import com.mgen256.al.FireTypes;
import com.mgen256.al.NeoForgeFireTypesAdapter;
import com.mgen256.al.items.FireTypeWandTrait;
import com.mgen256.al.blocks.NeoForgeFireTrait;
import com.mgen256.al.blocks.ALTorchSpec;
import com.mgen256.al.blocks.ALTorchCore;

public class ALTorch extends TorchBlock
    implements ModBlockSpec, NeoForgeFireTrait, ALTorchSpec {

    protected static final VoxelShape SHAPE = Block.box(
        SHAPE_BOX[0], SHAPE_BOX[1], SHAPE_BOX[2], SHAPE_BOX[3], SHAPE_BOX[4], SHAPE_BOX[5]
    );

    private final ALTorchCore<VoxelShape> core;
    
    public static Properties createProps( Block mainblock, String name ){
        return BlockBehaviour.Properties.of()
            .noCollision()
            .instabreak()
            .lightLevel(state -> state.getValue(FIRE_TYPE).toCore().getLuminance())
            .sound(mainblock.defaultBlockState().getSoundType())
            .setId(AdditionalLightsNeoForge.createResourceKey(name));
    }
    
    public ALTorch( Block mainblock, String name ) {
        super(ParticleTypes.FLAME, ALTorch.createProps(mainblock, name) );

        this.name = name;
        this.core = new ALTorchCore<>(name, SHAPE);

        registerDefaultState(stateDefinition.any()
            .setValue(FIRE_TYPE, NeoForgeFireTypesAdapter.fromCore(FireTypes.NORMAL))
            .setValue(PREVIOUS_FIRE_TYPE, NeoForgeFireTypesAdapter.fromCore(FireTypes.NORMAL)));
    }
        
    private BlockSpec myKey;
    private String name;
    
    @Override
    public void setMyKey(BlockSpec key) {
        myKey = key;
    }
    
    @Override
    public String getRegName() {
        return name;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(FIRE_TYPE);
        builder.add(PREVIOUS_FIRE_TYPE);
    }


    @Override
    public int getLightEmission(BlockState state, BlockGetter world, BlockPos pos) {
        return state.getValue(FIRE_TYPE).toCore().getLuminance();
    }

    @Override
    public VoxelShape getShape(BlockState p_57510_, BlockGetter p_57511_, BlockPos p_57512_, CollisionContext p_57513_) {
        return core.getShape();
    }

    @Override
    public void animateTick(BlockState stateIn, Level level, BlockPos pos, RandomSource rand) {
        var fireType = stateIn.getValue(FIRE_TYPE).toCore();
        if (fireType == FireTypes.LIGHT) {
            return;
        }

        double d0 = pos.getX() + 0.5D;
        double d1 = pos.getY() + 0.7D;
        double d2 = pos.getZ() + 0.5D;
        level.addParticle(ParticleTypes.SMOKE, d0, d1, d2, 0.0D, 0.0D, 0.0D);
        
        ParticleOptions particleOption;
        particleOption = fireType == FireTypes.SOUL ? ParticleTypes.SOUL_FIRE_FLAME : ParticleTypes.FLAME;

        level.addParticle(particleOption, d0, d1, d2, 0.0D, 0.0D, 0.0D);
     }

        
    @Override
    public List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {

        List<ItemStack> list = new ArrayList<>();
        list.add(new ItemStack(AdditionalLightsNeoForge.getBlockItem(myKey)));

        return list;
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        if( placer == null ) 
            return;

        if (placer.getOffhandItem().getItem() instanceof FireTypeWandTrait wand && wand.supportsAutomaticPlacementOnTorch()) {
            level.setBlockAndUpdate(pos, state.setValue(FIRE_TYPE, NeoForgeFireTypesAdapter.fromCore(wand.getTargetFireType())));
        }
    }
}

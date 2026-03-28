package com.mgen256.al.blocks;
import com.mgen256.al.BlockSpec;

import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.ArrayList;
import java.util.List;

import com.mgen256.al.AdditionalLightsNeoForge;
import com.mgen256.al.ModBlockList;
import com.mgen256.al.blocks.ModBlockCore;

public abstract class ModBlock extends Block implements ModBlockSpec {

    private final ModBlockCore<VoxelShape> core;
    private BlockSpec myKey;

    protected ModBlock(Block mainblock, String name, Properties props, VoxelShape shape) {
        super(props);
        this.core = new ModBlockCore<>(name, shape);
    }

    protected BlockItem blockItem;

    @Override
    public void setMyKey(BlockSpec key) {
        core.setMyKey(key);
        this.myKey = key;
    }

    @Override
    public String getRegName() {
        return core.getRegName();
    }
    
    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return core.getShape();
    }
    
    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {

        List<ItemStack> list = new ArrayList<>();
        list.add(new ItemStack(AdditionalLightsNeoForge.getBlockItem(myKey)));

        return list;
    }

    protected static void log(String string)
    {
        AdditionalLightsNeoForge.log(string);
    }
}
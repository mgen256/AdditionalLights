package com.mgen256.al.blocks;

import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.ArrayList;
import java.util.List;

import com.mgen256.al.AdditionalLights;

public abstract class ModBlock extends Block implements IModBlock {

    protected ModBlock(String basename, Block mainblock, Properties props, VoxelShape shape) {
        super(props);
        if( mainblock == null )
            name = basename;
        else
            name = basename + Registry.BLOCK.getKey(mainblock).getPath();
        voxelShape = shape;
    }

    protected BlockItem blockItem;
    protected String name;
    private VoxelShape voxelShape;

    @Override
    public void init() {
        blockItem = new BlockItem(this, AdditionalLights.ItemProps);
    }

    @Override
    public String getModRegistryName(){
        return name;
    }

    @Override
    public BlockItem getBlockItem() {
        return blockItem;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return voxelShape;
    }
    
    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public void setRenderLayer() {
        ItemBlockRenderTypes.setRenderLayer(this, name.contains("glass") ? RenderType.cutout() : RenderType.solid() );
    }
    
    @Override
    public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {

        List<ItemStack> list = new ArrayList<>();
        list.add( new ItemStack(blockItem) );

        return list;
    }

    protected static void Log( String string )
    {
        AdditionalLights.Log(string);
    }
}
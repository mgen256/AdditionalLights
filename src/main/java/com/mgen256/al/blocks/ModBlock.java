package com.mgen256.al.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;

import java.util.ArrayList;
import java.util.List;

import com.mgen256.al.AdditionalLights;

public abstract class ModBlock extends Block implements IModBlock {

    public ModBlock(String basename, Block mainblock, Properties props, VoxelShape shape) {
        super(props);
        if( mainblock == null )
            name = basename;
        else
            name = basename + mainblock.getRegistryName().getPath();
        voxelShape = shape;
    }

    protected BlockItem blockItem;
    protected String name;
    private VoxelShape voxelShape;

    @Override
    public void init() {
        setRegistryName(name);
        blockItem = new BlockItem(this, AdditionalLights.ItemProps);
        blockItem.setRegistryName(getRegistryName());
    }

    @Override
    public String getName(){
        return name;
    }

    @Override
    public BlockItem getBlockItem() {
        return blockItem;
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return voxelShape;
    }

    @Override
    public BlockRenderType getRenderType(final BlockState state) {
        return BlockRenderType.MODEL;
    }
    
    @Override
    public void setRenderLayer() {
        RenderTypeLookup.setRenderLayer(this, name.contains("glass") ? RenderType.getCutout() : RenderType.getSolid() );
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
package com.mgen256.al.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;

import com.mgen256.al.AdditionalLights;
import com.mgen256.al.blocks.IModBlock;

public abstract class ModBlock extends Block implements IModBlock {

    public ModBlock(String basename, Block mainblock, Material material ) {
        super(material);
        
        name = mainblock == null ? basename : basename + mainblock.getRegistryName().getPath();
        blockRenderLayer = name.contains("glass") ? BlockRenderLayer.CUTOUT : BlockRenderLayer.SOLID;

        setSoundType(mainblock.getSoundType());
        setTranslationKey(AdditionalLights.MOD_ID + "." + name );
        setCreativeTab(CreativeTabs.MATERIALS);
        setRegistryName(name);
        createItem();
    }

    protected String name;
    private BlockRenderLayer blockRenderLayer;
    private Item item;

    private void createItem(){
        item = new ItemBlock(this).setRegistryName(name);
    }

    @Override
    public String getName(){
        return name;
    }
    
    @Override
    public Item getItem() {
        return item;
    }
    
    @Override
    public boolean isOpaqueCube( final IBlockState state ){
        return false;
    }

    @Override
    public boolean isFullCube( final IBlockState state ){
        return false;
    }

    abstract AxisAlignedBB getShapes( final IBlockState state );
    abstract PropertyDirection getFacing();

    
    @Override
    public AxisAlignedBB getBoundingBox( IBlockState state, IBlockAccess source, BlockPos pos ) {
        return getShapes(state);
    }

    @Override
    public int getMetaFromState( IBlockState state ) {
        return state.getValue(getFacing()).getIndex();
    }
}
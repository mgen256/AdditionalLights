package com.mgen256.al.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import com.mgen256.al.AdditionalLights;
import com.mgen256.al.blocks.IModBlock;

public abstract class ModBlock extends Block implements IModBlock {

    public ModBlock(String basename, Block mainblock, String mainBlockName, Material material ) {
        super(material);
        
        if( mainblock == null )
            name = basename;
        else {
            name = basename + ( mainBlockName == null ? mainblock.getRegistryName().getPath() : mainBlockName );
            setSoundType(mainblock.getSoundType());
        }
        
        blockRenderLayer = name.contains("glass") ? BlockRenderLayer.CUTOUT : BlockRenderLayer.SOLID;

        setTranslationKey(AdditionalLights.MOD_ID + "." + name );
        setCreativeTab( AdditionalLights.MYTAB );
        setRegistryName(name);
        createItem();
    }

    protected String name;
    private BlockRenderLayer blockRenderLayer;
    private Item item;

    abstract AxisAlignedBB getShapes( final IBlockState state );

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

    @Override
    public BlockRenderLayer getRenderLayer() {
        return blockRenderLayer;
    }

    @Override
    public AxisAlignedBB getBoundingBox( IBlockState state, IBlockAccess source, BlockPos pos ) {
        return getShapes(state);
    }

}
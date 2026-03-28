package com.mgen256.al.blocks;
import com.mgen256.al.BlockSpec;
import com.mgen256.al.blocks.ModBlockCore;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;


public abstract class ModBlock extends Block implements ModBlockSpec {

    private final ModBlockCore<VoxelShape> core;

    protected ModBlock(Properties settings, VoxelShape shape, ResourceKey<Block> key) {
        super(settings);
        this.core = new ModBlockCore<>(key.identifier().getPath(), shape);
    }

    protected BlockItem blockItem;

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return core.getShape();
    }
    
    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public void setMyKey(BlockSpec key) {
        core.setMyKey(key);
    }

    @Override
    public String getRegName() {
        return core.getRegName();
    }
}

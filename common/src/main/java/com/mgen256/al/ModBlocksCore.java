package com.mgen256.al;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.BiFunction;

import com.mgen256.al.BlockSpec;
import com.mgen256.al.blocks.BlockFactory;
import com.mgen256.al.blocks.ModBlockSpec;

public class ModBlocksCore<B, I> {

    private final Map<BlockSpec, B> blocks = new LinkedHashMap<>();
    private final Map<BlockSpec, I> items = new LinkedHashMap<>();

    public ModBlocksCore() {
    }

    public <K> void register(
            BlockSpec key,
            K registryKey,
            BlockFactory<? extends B, K> blockFactory,
            BiFunction<BlockSpec, ? super B, ? extends I> itemFactory) {

        B block = blockFactory.create(registryKey);
        if (block instanceof ModBlockSpec mod) {
            mod.setMyKey(key);
        }
        blocks.put(key, block);

        if (itemFactory != null) {
            I item = itemFactory.apply(key, block);
            if (item != null) {
                items.put(key, item);
            }
        }
    }

    public B getBlock(BlockSpec key) {
        return blocks.get(key);
    }

    public I getBlockItem(BlockSpec key) {
        return items.get(key);
    }
}


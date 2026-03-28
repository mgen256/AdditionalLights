package com.mgen256.al;

import java.util.LinkedHashMap;
import java.util.Map;
import com.mgen256.al.BlockSpec;

public class AdditionalLightsCore<B, I, S> {
    private final Map<BlockSpec, B> blocks = new LinkedHashMap<>();
    private final Map<BlockSpec, I> blockItems = new LinkedHashMap<>();
    private final Map<ModSoundList, S> sounds = new LinkedHashMap<>();

    public void putBlock(BlockSpec key, B block) {
        blocks.put(key, block);
    }

    public void putBlockItem(BlockSpec key, I item) {
        if (item != null) {
            blockItems.put(key, item);
        }
    }

    public void putSound(ModSoundList key, S sound) {
        sounds.put(key, sound);
    }

    public B getBlock(BlockSpec key) {
        return blocks.get(key);
    }

    public I getBlockItem(BlockSpec key) {
        return blockItems.get(key);
    }

    public S getSound(ModSoundList key) {
        return sounds.get(key);
    }
}

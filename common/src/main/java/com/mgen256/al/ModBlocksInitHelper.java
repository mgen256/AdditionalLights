package com.mgen256.al;

import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;
import com.mgen256.al.BlockSpec;
import com.mgen256.al.LampSpec;
import com.mgen256.al.FloorTorchSpec;
import com.mgen256.al.WallTorchSpec;
import com.mgen256.al.StandingTorchSSpec;
import com.mgen256.al.StandingTorchLSpec;
import com.mgen256.al.FirePitSSpec;
import com.mgen256.al.FirePitLSpec;
import com.mgen256.al.VanillaBlockMap;
import com.mgen256.al.blocks.PedestalFireSpec;

public final class ModBlocksInitHelper {

    private ModBlocksInitHelper() {
    }

    public static <B, K> void initFactories(
            Map<BlockSpec, Function<K, B>> factories,
            Function<String, B> baseResolver,
            BiFunction<B, K, B> lampFactory,
            BiFunction<B, BlockSpec, Function<K, B>> wallTorchFactory,
            BiFunction<B, K, B> torchFactory,
            BiFunction<B, K, B> standingSFactory,
            BiFunction<B, K, B> standingLFactory,
            BiFunction<B, K, B> firePitSFactory,
            BiFunction<B, K, B> firePitLFactory,
            BiFunction<PedestalTypes, K, B> fireFactory,
            BiFunction<PedestalTypes, K, B> soulFireFactory,
            BiFunction<PedestalTypes, K, B> lightFireFactory) {

        for (var key : ModBlockList.values()) {
            String baseName = VanillaBlockMap.REG_TO_BLOCK_FIELD.get(key.regName());
            if (baseName == null) {
                continue;
            }
            B base = baseResolver.apply(baseName);
            if (key instanceof LampSpec) {
                factories.put(key, k -> lampFactory.apply(base, k));
            } else if (key instanceof WallTorchSpec) {
                factories.put(key, wallTorchFactory.apply(base, key.getFloorTorchKey()));
            } else if (key instanceof FloorTorchSpec) {
                factories.put(key, k -> torchFactory.apply(base, k));
            } else if (key instanceof StandingTorchSSpec) {
                factories.put(key, k -> standingSFactory.apply(base, k));
            } else if (key instanceof StandingTorchLSpec) {
                factories.put(key, k -> standingLFactory.apply(base, k));
            } else if (key instanceof FirePitSSpec) {
                factories.put(key, k -> firePitSFactory.apply(base, k));
            } else if (key instanceof FirePitLSpec) {
                factories.put(key, k -> firePitLFactory.apply(base, k));
            }
        }
        registerPedestalFireFactories(
                factories,
                fireFactory,
                soulFireFactory,
                lightFireFactory);
    }

    private static <B, K> void registerPedestalFireFactories(
            final Map<BlockSpec, Function<K, B>> factories,
            final BiFunction<PedestalTypes, K, B> fireFactory,
            final BiFunction<PedestalTypes, K, B> soulFireFactory,
            final BiFunction<PedestalTypes, K, B> lightFactory) {
        for (final PedestalTypes pedestalType : PedestalTypes.values()) {
            factories.put(
                    PedestalFireSpec.resolve(pedestalType, FireTypes.NORMAL),
                    key -> fireFactory.apply(pedestalType, key));
            factories.put(
                    PedestalFireSpec.resolve(pedestalType, FireTypes.SOUL),
                    key -> soulFireFactory.apply(pedestalType, key));
            factories.put(
                    PedestalFireSpec.resolve(pedestalType, FireTypes.LIGHT),
                    key -> lightFactory.apply(pedestalType, key));
        }
    }
}

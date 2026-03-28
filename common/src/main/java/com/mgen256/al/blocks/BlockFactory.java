package com.mgen256.al.blocks;

@FunctionalInterface
public interface BlockFactory<B, K> {
    B create(K key);
}

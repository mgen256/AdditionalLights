package com.mgen256.al.blocks;

import com.mgen256.al.BlockSpec;

public class ModBlockCore<S> implements ModBlockSpec {
    private final String regName;
    private final S shape;
    private BlockSpec myKey;

    public ModBlockCore(String regName, S shape) {
        this.regName = regName;
        this.shape = shape;
    }

    public S getShape() { return shape; }

    @Override
    public void setMyKey(BlockSpec key) { myKey = key; }

    @Override
    public String getRegName() { return regName; }
}

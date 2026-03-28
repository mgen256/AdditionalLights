package com.mgen256.al.blocks;

import com.mgen256.al.PedestalSize;

public abstract class StandingTorchBaseCore<S> extends PedestalCore<S> {
    public StandingTorchBaseCore(String regName, S shape, PedestalSize size) {
        super(regName, shape, size);
    }
}

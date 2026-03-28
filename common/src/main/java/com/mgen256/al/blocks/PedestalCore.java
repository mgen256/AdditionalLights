package com.mgen256.al.blocks;

import com.mgen256.al.PedestalSize;

public class PedestalCore<S> extends ModBlockCore<S> {
    private final PedestalSize size;

    public PedestalCore(String regName, S shape, PedestalSize size) {
        super(regName, shape);
        this.size = size;
    }

    public PedestalSize getSize() { return size; }
}

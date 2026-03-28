package com.mgen256.al.blocks;

public class ALTorchWallCore<S, D> extends ModBlockCore<S> implements ALTorchWallSpec {
    private final java.util.Map<D, S> shapes;

    @FunctionalInterface
    public interface ShapeFactory<S> {
        S create(double[] box);
    }

    @SafeVarargs
    public ALTorchWallCore(String regName, ShapeFactory<S> factory, D... dirs) {
        super(regName, factory.create(NORTH_BOX));
        this.shapes = java.util.Map.of(
            dirs[0], factory.create(NORTH_BOX),
            dirs[1], factory.create(SOUTH_BOX),
            dirs[2], factory.create(WEST_BOX),
            dirs[3], factory.create(EAST_BOX)
        );
    }

    public S getShape(D dir) { return shapes.get(dir); }
}

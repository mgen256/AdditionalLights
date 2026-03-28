package com.mgen256.al.blocks;

import java.util.Map;
import java.util.function.Function;

public abstract class ALLampCore<S, W, P, B, D, F>
    extends ModBlockCore<S> implements ALLampSpec {

    private final Map<D, S> shapes;

    @FunctionalInterface
    public interface ShapeFactory<S> {
        S create(double[] box);
    }

    @SafeVarargs
    protected ALLampCore(String regName, ShapeFactory<S> factory, D... dirs) {
        super(regName, factory.create(DOWN_BOX));
        this.shapes = Map.of(
            dirs[0], factory.create(DOWN_BOX),
            dirs[1], factory.create(UP_BOX),
            dirs[2], factory.create(NORTH_BOX),
            dirs[3], factory.create(SOUTH_BOX),
            dirs[4], factory.create(WEST_BOX),
            dirs[5], factory.create(EAST_BOX)
        );
    }

    public S getShape(D dir) { return shapes.get(dir); }

    protected abstract B getBlockState(W world, P pos);
    protected abstract F getFluidState(W world, P pos);
    protected abstract boolean isWaterFluid(F state);
    protected abstract boolean canBeReplaced(B state);
    protected abstract boolean isAir(B state);
    protected abstract boolean isWaterBlock(B state);
    protected abstract P offset(P pos, D dir);
    protected abstract D opposite(D dir);
    protected abstract D up();
    protected abstract D down();
    protected abstract B defaultState();
    protected abstract B withFacing(B state, D dir);
    protected abstract B withWaterlogged(B state, boolean value);
    protected abstract D getFacing(B state);
    protected abstract boolean isSameBlock(B state);

    protected boolean willBeReplaced(W world, P pos) {
        B state = getBlockState(world, pos);
        return canBeReplaced(state) && !(isAir(state) || isWaterBlock(state));
    }

    public B getPlacementState(W world, P pos, D side) {
        boolean water = isWaterFluid(getFluidState(world, pos));
        D dir = willBeReplaced(world, pos) ? up() : side;
        return withWaterlogged(withFacing(defaultState(), dir), water);
    }

    public boolean canSurvive(B state, W world, P pos) {
        if (willBeReplaced(world, pos)) {
            return !isAir(getBlockState(world, offset(pos, down())));
        }
        D dir = opposite(getFacing(state));
        B neighbor = getBlockState(world, offset(pos, dir));
        return !(isAir(neighbor) || isSameBlock(neighbor));
    }
}

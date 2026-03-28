package com.mgen256.al.blocks;

public interface PedestalTrait<W, P, S> {
    P offsetUp(P pos);

    S getBlockState(W world, P pos);

    boolean setBlockState(W world, P pos, S state);

    void breakBlock(W world, P pos);

    boolean isAir(S state);

    boolean isWater(S state);

    boolean hasRedstonePower(W world, P pos);

    boolean isFireBlock(S state);

    boolean isFireSummoned(S state);

    S createFireState(S pedestalState);

    default boolean shouldPlaceFire(S state) {
        return true;
    }

    boolean getAcceptPower(S state);

    boolean getIsPowered(S state);

    boolean getActivated(S state);

    boolean isLightFireType(S state);

    boolean getLit(S state);

    S setIsPowered(S state, boolean value);

    S setActivated(S state, boolean value);

    S setLit(S state, boolean value);

    default S withLightLit(S state, boolean value) {
        if (!isLightFireType(state) || getLit(state) == value) {
            return state;
        }
        return setLit(state, value);
    }

    default S updateLightLit(W world, P pos, S state, boolean value) {
        if (!isLightFireType(state) || getLit(state) == value) {
            return state;
        }

        S updatedState = setLit(state, value);
        setBlockState(world, pos, updatedState);
        return updatedState;
    }

    default boolean igniteByInteraction(W world, P pos, S state) {
        boolean litChanged = isLightFireType(state) && !getLit(state);
        if (litChanged) {
            state = updateLightLit(world, pos, state, true);
        }

        P upperPos = offsetUp(pos);
        S upperState = getBlockState(world, upperPos);
        boolean fire = isFireBlock(upperState);

        if (!shouldPlaceFire(state)) {
            if (fire) {
                breakBlock(world, upperPos);
                return true;
            }
            return litChanged;
        }

        if (!(isAir(upperState) || isWater(upperState) || fire)) {
            return litChanged;
        }

        if (fire && isFireSummoned(upperState)) {
            return litChanged;
        }

        if (fire) {
            breakBlock(world, upperPos);
        }

        return setBlockState(world, upperPos, createFireState(state)) || litChanged;
    }

    default void onIgnited(W world, P pos) {}

    default boolean igniteFire(W world, P pos, S state, boolean replaceOnly) {
        P upperPos = offsetUp(pos);
        S upperState = getBlockState(world, upperPos);
        boolean fire = isFireBlock(upperState);

        if (!shouldPlaceFire(state)) {
            if (fire) {
                breakBlock(world, upperPos);
            }
            return true;
        }

        if (replaceOnly) {
            if (!fire) {
                return false;
            }
        } else if (!(isAir(upperState) || isWater(upperState) || fire)) {
            return false;
        }

        if (fire && !isFireSummoned(upperState)) {
            breakBlock(world, upperPos);
        }

        return setBlockState(world, upperPos, createFireState(state));
    }

    default void removeFire(W world, P pos, S state) {
        P upperPos = offsetUp(pos);
        if (isFireBlock(getBlockState(world, upperPos))) {
            breakBlock(world, upperPos);
        }
    }

    default void handleNeighborUpdate(S state, W world, P pos) {
        state = getBlockState(world, pos);
        if (!getAcceptPower(state)) {
            return;
        }

        if (hasRedstonePower(world, pos)) {
            if (getActivated(state)) {
                return;
            }

            if (igniteFire(world, pos, state, false)) {
                onIgnited(world, pos);
            }

            S updatedState = withLightLit(setIsPowered(setActivated(state, true), true), true);
            setBlockState(world, pos, updatedState);
        } else if (getIsPowered(state) && getActivated(state)) {
            removeFire(world, pos, state);
            S updatedState = withLightLit(setIsPowered(setActivated(state, false), false), false);
            setBlockState(world, pos, updatedState);
        }
    }
}

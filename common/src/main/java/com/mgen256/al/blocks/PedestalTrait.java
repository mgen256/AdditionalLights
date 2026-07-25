package com.mgen256.al.blocks;

public interface PedestalTrait<W, P, S> extends LightWandTargetSpec {
    P offsetUp(P pos);

    S getBlockState(W world, P pos);

    boolean isClientSide(W world);

    boolean setBlockState(W world, P pos, S state);

    void breakBlock(W world, P pos);

    boolean isAir(S state);

    boolean isWater(S state);

    boolean hasRedstonePower(W world, P pos);

    boolean isFireBlock(S state);

    boolean isFireSummoned(S state);

    boolean isLightFireBlock(S state);

    boolean getLightFireLit(S state);

    S setLightFireLit(S state, boolean value);

    S createFireState(S pedestalState);

    boolean getAcceptPower(S state);

    boolean getIsPowered(S state);

    boolean getActivated(S state);

    boolean isLightFireType(S state);

    boolean getLit(S state);

    int getActiveComparatorOutput(S state);

    S setIsPowered(S state, boolean value);

    S setActivated(S state, boolean value);

    S setLit(S state, boolean value);

    void updateComparatorOutput(W world, P pos);

    default int getComparatorOutput(W world, P pos, S state) {
        if (isLightFireType(state) && !getLit(state)) {
            return 0;
        }

        S upperState = getBlockState(world, offsetUp(pos));
        return isFireBlock(upperState) && isFireSummoned(upperState) ? getActiveComparatorOutput(state) : 0;
    }

    default void updateComparatorOutputIfChanged(W world, P pos, int previousOutput) {
        if (getComparatorOutput(world, pos, getBlockState(world, pos)) != previousOutput) {
            updateComparatorOutput(world, pos);
        }
    }

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

        boolean lightFireChanged = ensureLightFire(world, pos, state);
        P upperPos = offsetUp(pos);
        S upperState = getBlockState(world, upperPos);
        boolean fire = isFireBlock(upperState);

        if (!(isAir(upperState) || isWater(upperState) || fire)) {
            return litChanged || lightFireChanged;
        }

        if (fire && isFireSummoned(upperState)) {
            return litChanged || lightFireChanged;
        }

        if (fire) {
            breakBlock(world, upperPos);
        }

        return setBlockState(world, upperPos, createFireState(state))
                || litChanged
                || lightFireChanged;
    }

    default void onIgnited(W world, P pos) {}

    default boolean igniteFire(W world, P pos, S state, boolean replaceOnly) {
        P upperPos = offsetUp(pos);
        S upperState = getBlockState(world, upperPos);
        boolean fire = isFireBlock(upperState);

        if (replaceOnly && !fire && !isLightFireType(state)) {
            return false;
        }
        if (!(isAir(upperState) || isWater(upperState) || fire)) {
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

    default boolean ensureLightFire(W world, P pos, S state) {
        if (!isLightFireType(state)) {
            return false;
        }

        P upperPos = offsetUp(pos);
        S upperState = getBlockState(world, upperPos);
        if (!isLightFireBlock(upperState) || !isFireSummoned(upperState)) {
            return igniteFire(world, pos, state, false);
        }

        boolean expectedLit = getLit(state);
        if (getLightFireLit(upperState) == expectedLit) {
            return false;
        }
        return setBlockState(world, upperPos, setLightFireLit(upperState, expectedLit));
    }

    default void handleNeighborUpdate(S state, W world, P pos) {
        state = getBlockState(world, pos);
        if (isClientSide(world) && isLightFireType(state)) {
            return;
        }

        boolean hasPower = hasRedstonePower(world, pos);
        if (!getAcceptPower(state)) {
            ensureLightFire(world, pos, state);
            return;
        }

        if (hasPower) {
            if (getActivated(state)) {
                ensureLightFire(world, pos, state);
                return;
            }

            S updatedState = withLightLit(setIsPowered(setActivated(state, true), true), true);
            setBlockState(world, pos, updatedState);

            if (isLightFireType(updatedState)) {
                ensureLightFire(world, pos, updatedState);
            } else if (igniteFire(world, pos, updatedState, false)) {
                onIgnited(world, pos);
            }
        } else if (getIsPowered(state) && getActivated(state)) {
            S updatedState = withLightLit(setIsPowered(setActivated(state, false), false), false);
            if (!isLightFireType(state)) {
                removeFire(world, pos, state);
            }
            setBlockState(world, pos, updatedState);
            ensureLightFire(world, pos, updatedState);
        } else {
            ensureLightFire(world, pos, state);
        }
    }
}

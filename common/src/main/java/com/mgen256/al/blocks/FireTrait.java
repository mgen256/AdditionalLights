package com.mgen256.al.blocks;

import com.mgen256.al.FireTypes;
import com.mgen256.al.FireTypesAdapter;

public interface FireTrait<W, P, S> {
    FireTypesAdapter getFireType(S state);

    FireTypesAdapter getPreviousFireType(S state);
    S withFireType(S state, FireTypesAdapter type);

    S withPreviousFireType(S state, FireTypesAdapter type);

    boolean setBlockState(W world, P pos, S state);

    default S setFireType(
        W world,
        P pos,
        S state,
        FireTypes newFireType,
        FireTypes prevFireType
    ) {
        S newState = withPreviousFireType(
            withFireType(state, FireTypesAdapter.fromCore(newFireType)),
            FireTypesAdapter.fromCore(prevFireType)
        );
        if (setBlockState(world, pos, newState)) {
            return newState;
        }
        return state;
    }
}

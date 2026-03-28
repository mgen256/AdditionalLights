package com.mgen256.al.blocks;

import java.util.Map;
import java.util.function.Function;

import com.mgen256.al.PedestalTypes;

public interface FireBaseCore<S, P, W, B> {
    Map<PedestalTypes, Double> SMOKE_POS = FireBaseSpec.createSmokePosMap();

    static <S> Map<PedestalTypes, S> createShapes(Function<double[], S> creator, double scale) {
        return FireBaseSpec.createShapeMap(creator, scale);
    }

    Map<PedestalTypes, S> getShapes();
    Map<PedestalTypes, P> getParticleTypes();
    PedestalTypes getPedestalKey();
    S getCollisionShapeImpl();

    double getX(B pos);
    double getY(B pos);
    double getZ(B pos);
    void spawnParticle(W world, P particle, double x, double y, double z);

    default S getCollisionShape(Object state, Object world, B pos, Object context) {
        return getCollisionShapeImpl();
    }

    default S getOutlineShape(Object state, Object world, B pos, Object context) {
        return getShapes().get(getPedestalKey());
    }

    default void randomDisplayTick(Object state, W world, B pos, java.util.Random random) {
        double d0 = getX(pos) + 0.5D;
        double d1 = getY(pos) + SMOKE_POS.get(getPedestalKey());
        double d2 = getZ(pos) + 0.5D;
        spawnParticle(world, getParticleTypes().get(getPedestalKey()), d0, d1, d2);
    }
}

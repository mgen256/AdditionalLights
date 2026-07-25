package com.mgen256.al.blocks;

import java.util.EnumMap;
import java.util.Map;
import java.util.function.Function;

import com.mgen256.al.PedestalTypes;

public final class LightFireShapeSpec {

    private static final Map<PedestalTypes, double[]> SHAPES = createShapes();

    private LightFireShapeSpec() {
    }

    public static <S> Map<PedestalTypes, S> createShapeMap(Function<double[], S> creator) {
        Map<PedestalTypes, S> result = new EnumMap<>(PedestalTypes.class);
        SHAPES.forEach((type, coordinates) -> result.put(type, creator.apply(coordinates.clone())));
        return Map.copyOf(result);
    }

    private static Map<PedestalTypes, double[]> createShapes() {
        Map<PedestalTypes, double[]> result = new EnumMap<>(PedestalTypes.class);
        result.put(PedestalTypes.standing_torch_s,
                new double[] {5.0D, -4.0D, 5.0D, 11.0D, -2.0D, 11.0D});
        result.put(PedestalTypes.standing_torch_l,
                new double[] {5.0D, 0.0D, 5.0D, 11.0D, 3.0D, 11.0D});
        result.put(PedestalTypes.fire_pit_s,
                new double[] {2.0D, -10.0D, 2.0D, 14.0D, -4.75D, 14.0D});
        result.put(PedestalTypes.fire_pit_l,
                new double[] {1.0D, -2.0D, 1.0D, 15.0D, 4.5D, 15.0D});
        return Map.copyOf(result);
    }
}

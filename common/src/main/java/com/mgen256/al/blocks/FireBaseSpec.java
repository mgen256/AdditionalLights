package com.mgen256.al.blocks;

import java.util.EnumMap;
import java.util.Map;
import java.util.function.Function;

import com.mgen256.al.PedestalTypes;

public interface FireBaseSpec {
    Map<PedestalTypes, double[]> SHAPES_COORDS = new EnumMap<>(PedestalTypes.class) {
        {
            put(PedestalTypes.standing_torch_s, new double[] {4.0D, -6.0D, 4.0D, 12.0D, 2.0D, 12.0D});
            put(PedestalTypes.standing_torch_l, new double[] {4.0D, -2.0D, 4.0D, 12.0D, 6.0D, 12.0D});
            put(PedestalTypes.fire_pit_s, new double[] {0.0D, -10.0D, 0.0D, 16.0D, 2.0D, 16.0D});
            put(PedestalTypes.fire_pit_l, new double[] {0.0D, -2.0D, 0.0D, 16.0D, 7.0D, 16.0D});
        }
    };

    Map<PedestalTypes, Double> SMOKE_POS = new EnumMap<>(PedestalTypes.class) {
        {
            put(PedestalTypes.standing_torch_s, 0.2D);
            put(PedestalTypes.standing_torch_l, 0.7D);
            put(PedestalTypes.fire_pit_s, 0.0D);
            put(PedestalTypes.fire_pit_l, 0.8D);
        }
    };

    static <S> Map<PedestalTypes, S> createShapeMap(Function<double[], S> creator, double scale) {
        Map<PedestalTypes, S> map = new EnumMap<>(PedestalTypes.class);
        for (Map.Entry<PedestalTypes, double[]> entry : SHAPES_COORDS.entrySet()) {
            double[] raw = entry.getValue();
            double[] coords = new double[raw.length];
            for (int i = 0; i < raw.length; i++) {
                coords[i] = raw[i] / scale;
            }
            map.put(entry.getKey(), creator.apply(coords));
        }
        return map;
    }

    static Map<PedestalTypes, Double> createSmokePosMap() {
        Map<PedestalTypes, Double> map = new EnumMap<>(PedestalTypes.class);
        for (Map.Entry<PedestalTypes, Double> entry : SMOKE_POS.entrySet()) {
            map.put(entry.getKey(), entry.getValue());
        }
        return map;
    }
}

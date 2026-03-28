package com.mgen256.al.blocks;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import com.mgen256.al.PedestalTypes;

public final class PedestalLightShapeSpec {

    public static final Map<PedestalTypes, List<ShapePartSpec>> SHAPE_PARTS = createShapeParts();

    private PedestalLightShapeSpec() {
    }

    public record ShapePartSpec(
        double minX,
        double minY,
        double minZ,
        double maxX,
        double maxY,
        double maxZ
    ) {
    }

    public static <S> Map<PedestalTypes, List<S>> createShapePartMap(Function<ShapePartSpec, S> creator) {
        Map<PedestalTypes, List<S>> map = new EnumMap<>(PedestalTypes.class);
        for (Map.Entry<PedestalTypes, List<ShapePartSpec>> entry : SHAPE_PARTS.entrySet()) {
            map.put(entry.getKey(), entry.getValue().stream().map(creator).toList());
        }
        return Map.copyOf(map);
    }

    private static Map<PedestalTypes, List<ShapePartSpec>> createShapeParts() {
        Map<PedestalTypes, List<ShapePartSpec>> map = new EnumMap<>(PedestalTypes.class);
        map.put(PedestalTypes.standing_torch_s, List.of(
            part(5.0D, 12.0D, 5.0D, 11.0D, 14.0D, 11.0D),
            part(5.25D, 12.0D, 5.25D, 10.75D, 13.75D, 10.75D)
        ));
        map.put(PedestalTypes.standing_torch_l, List.of(
            part(5.0D, 16.0D, 5.0D, 11.0D, 19.0D, 11.0D),
            part(5.25D, 16.0D, 5.25D, 10.75D, 18.75D, 10.75D)
        ));
        map.put(PedestalTypes.fire_pit_s, List.of(
            part(2.0D, 0.0D, 2.0D, 14.0D, 6.0D, 14.0D),
            part(2.5D, 6.0D, 2.5D, 13.5D, 10.75D, 13.5D),
            part(2.0D, 8.0D, 2.0D, 14.0D, 11.25D, 14.0D)
        ));
        map.put(PedestalTypes.fire_pit_l, List.of(
            part(1.5D, 14.0D, 1.5D, 14.5D, 20.0D, 14.5D),
            part(1.0D, 16.0D, 1.0D, 15.0D, 20.5D, 15.0D)
        ));
        return Map.copyOf(map);
    }

    private static ShapePartSpec part(
        double minX,
        double minY,
        double minZ,
        double maxX,
        double maxY,
        double maxZ
    ) {
        return new ShapePartSpec(minX, minY, minZ, maxX, maxY, maxZ);
    }
}

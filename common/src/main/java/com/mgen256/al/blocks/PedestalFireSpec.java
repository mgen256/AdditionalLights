package com.mgen256.al.blocks;

import com.mgen256.al.BlockSpec;
import com.mgen256.al.FireForSpec;
import com.mgen256.al.FireTypes;
import com.mgen256.al.LightFireForSpec;
import com.mgen256.al.PedestalTypes;
import com.mgen256.al.SoulFireForSpec;

public interface PedestalFireSpec extends LightWandTargetSpec {

    PedestalTypes getType();

    default BlockSpec getFireFromType(final FireTypes fireType) {
        return resolve(getType(), fireType);
    }

    static BlockSpec resolve(
            final PedestalTypes pedestalType,
            final FireTypes fireType) {
        return switch (fireType) {
            case NORMAL -> switch (pedestalType) {
                case standing_torch_s -> FireForSpec.Fire_For_StandingTorch_S;
                case standing_torch_l -> FireForSpec.Fire_For_StandingTorch_L;
                case fire_pit_s -> FireForSpec.Fire_For_FirePit_S;
                case fire_pit_l -> FireForSpec.Fire_For_FirePit_L;
            };
            case SOUL -> switch (pedestalType) {
                case standing_torch_s -> SoulFireForSpec.SoulFire_For_StandingTorch_S;
                case standing_torch_l -> SoulFireForSpec.SoulFire_For_StandingTorch_L;
                case fire_pit_s -> SoulFireForSpec.SoulFire_For_FirePit_S;
                case fire_pit_l -> SoulFireForSpec.SoulFire_For_FirePit_L;
            };
            case LIGHT -> switch (pedestalType) {
                case standing_torch_s -> LightFireForSpec.LightFire_For_StandingTorch_S;
                case standing_torch_l -> LightFireForSpec.LightFire_For_StandingTorch_L;
                case fire_pit_s -> LightFireForSpec.LightFire_For_FirePit_S;
                case fire_pit_l -> LightFireForSpec.LightFire_For_FirePit_L;
            };
        };
    }
}

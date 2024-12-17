package com.mgen256.al;

import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public enum ModSoundList {
    
    Change("change"),
    Undo("undo"),
    Fire_Ignition_S("fire_ignition_s"),
    Fire_Ignition_L("fire_ignition_l"),
    Fire_Extinguish("fire_extinguish");

    private Identifier soundId;
    private SoundEvent sound;
    

    ModSoundList(String sound_name)
    {
        this.soundId = Identifier.of(AdditionalLights.MOD_ID, sound_name);
        this.sound = SoundEvent.of(soundId);
    }


    public SoundEvent get(){
        return sound;
    }

    
    public void register(){
        Registry.register(Registries.SOUND_EVENT, soundId, sound);
    }

}
package net.acetheeldritchking.aces_spell_utils.entity.mobs;

import net.minecraft.sounds.SoundEvent;

import java.util.UUID;

public interface IPhaseMusicEntity extends IPhaseEntity {
    // Used for boss music; set it in the child class to the music you want to have play
    public default SoundEvent getBossMusic() {
        return null;
    }

    default UUID getEntityUUID()
    {
        return null;
    }

    default boolean isEntityDeadOrDying()
    {
        return false;
    }

    default boolean isEntityRemoved()
    {
        return false;
    }

    // This is for changing music based on phase changing
    // Set hasCustomMusic to be true if you want to use the music manager given by the API
    // As there is no dedicated transition phase, set the transition phase using the following methods
    // Set this to true if you want the music to change
    //public boolean hasCustomMusic;
    //public boolean changeMusicOnPhaseChange;
    //public boolean hasTransitionPhase;
    //public int usePhaseAsTransition;
    //public int usePhaseForMusicChange;

    // Methods for above values
    public default boolean hasCustomMusic()
    {
        return false;
    }

    public default boolean changeMusicOnPhaseChange()
    {
        return false;
    }

    public default boolean hasTransitionPhase()
    {
        return false;
    }

    // Input which phase you want to have transition music
    // Put in an integer between 1-11 to denote the phase, this lines up with the enum values for the phases
    public default int usePhaseAsTransition()
    {
        return 0;
    }

    // Input which phase you want to have alt music
    // Put in an integer between 1-11 to denote the phase, this lines up with the enum values for the phases
    public default int usePhaseForMusicChange()
    {
        return 0;
    }

    // Used for transition music
    public default SoundEvent getTransitionMusic()
    {
        return null;
    }

    // Used for music to get for other phases
    public default SoundEvent getOtherPhaseMusic()
    {
        return null;
    }
}

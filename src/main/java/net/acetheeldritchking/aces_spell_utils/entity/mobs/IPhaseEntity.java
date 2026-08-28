package net.acetheeldritchking.aces_spell_utils.entity.mobs;

public interface IPhaseEntity {
    public enum Phase
    {
        FirstPhase(0),
        SecondPhase(1),
        ThirdPhase(2),
        FourthPhase(3),
        FifthPhase(4),
        SixthPhase(5),
        SeventhPhase(6),
        EighthPhase(7),
        NinethPhase(8),
        TenthPhase(9),
        EleventhPhase(10),
        TwelfthPhase(11);

        final public int value;

        Phase(int value)
        {
            this.value = value;
        }
    }

    public default void setPhase(int phase)
    {
    }

    public default void setPhase(Phase phase)
    {
    }

    public default int getPhase()
    {
        return 0;
    }

    public default boolean isPhase(Phase phase)
    {
        return phase.value == getPhase();
    }
}

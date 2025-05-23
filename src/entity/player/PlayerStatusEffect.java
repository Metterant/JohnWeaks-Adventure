package entity.player;

public class PlayerStatusEffect {
    // Timers
    public static final int SPEED_BOOST = 0;
    public static final int SHOTGUN     = 1;
    public static final int MACHINE_GUN = 2;
    public static final int OCTOSHOT    = 3;

    /** Number of status effects available */
    private static final int NUMBER_OF_EFFECTS = 4;

    private int[] effectTimerFrames = new int[4];

    /** Decrease Timer */
    public void decreaseTimer() {
        for (int i = 0; i < NUMBER_OF_EFFECTS; i++) {
            if (effectTimerFrames[i] > 0)
                effectTimerFrames[i]--; 
        }
    }

    /** 
     * Returns the duration of a given status effect in frames
     * @param effect The effect ID
     * @return duration in frames of the specified effect
     */
    public int getStatusEffectDuration(int effect) {
        return effectTimerFrames[effect];
    }

    /**
     * Set duration in frames for specified effect
     * @param effect The effect ID
     * @param duration New duration of the effect (in game frames) 
     */
    public void setStatusEffectDuration(int effect, int duration) {
        effectTimerFrames[effect] = duration;
    }

}

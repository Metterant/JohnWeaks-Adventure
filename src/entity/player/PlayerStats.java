package entity.player;

import util.GameConstants;

public class PlayerStats {
    private static int lives;
    private static int damage;
    private static double currentBaseSpeed;

    // Static fields init
    static {
        lives            = GameConstants.Game.PLAYER_LIVES;
        damage           = GameConstants.Player.BASE_DAMAGE;
        currentBaseSpeed = GameConstants.Player.BASE_SPEED; 
    }

    //#region LIVES

    /**
     * Returns the amount of lives left <p>
     * (-1) means the game is over
     * @return the amount of lives left
     */
    public static int getLivesLeft() {
        return lives;
    }

    /**
     * Removes 1 life from Player's amount of lives left
     * @return The amount of lives after the modification
     */
    public static int removeLife() {
        if (lives >= 0) 
            lives--;
        return lives;
    }

    /**
     * Adds 1 life to Player's amount of lives left
     * @return The amount of lives after the modification
     */
    public static int addLife() {
        lives++;
        return lives;
    }
    
    //#endregion

    //#region DAMAGE

    public static int getDamage() { return damage; }

    public static void setDamage(int damage) {
        if (damage <= 0)
            throw new IllegalArgumentException("Damage must not be less than 1."); 
        PlayerStats.damage = damage;
    }

    //#endregion

    //#region MOVEMENT SPEED
    
    /**
     * Returns base movement speed of the Player
     * @return base movement speed multiplier
     */
    public static double getCurrentBaseSpeed() { return currentBaseSpeed; }

    /**
     * Sets a new base movement speed multiplier
     * @param newBaseSpeed new base movement speed multiplier
     */
    public static void setCurrentBaseSpeed(double newBaseSpeed) {
        if (newBaseSpeed <= 0)
            throw new IllegalArgumentException("Speed must be a positive value."); 
        currentBaseSpeed = newBaseSpeed;
    }

    private PlayerStats() { }
}

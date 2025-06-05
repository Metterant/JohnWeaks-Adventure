package entity.player;

import util.GameConstants;

public class PlayerStats {
    private static int coins;
    private static int lives;
    private static int damage;
    private static double currentBaseSpeed;
    private static int gunLevel;
    private static boolean hasRunnerBoots;
    private static boolean hasBurstShot;

    // Static fields init
    static {
        lives            = GameConstants.Game.PLAYER_LIVES;
        damage           = GameConstants.Player.DAMAGE_BASE;
        currentBaseSpeed = GameConstants.Player.BASE_SPEED; 
        coins            = 0;
        gunLevel         = 1;
        hasRunnerBoots   = false;
        hasBurstShot     = false;
    }

    private PlayerStats() { }
    
    //#region COINS

    /** Returns the amount of coins the Plaeyr is having
     * @return the amount of coins the Player is having
     */
    public static int getCoins() { return coins; }

    /**
     * Set amonut of coins of the Player
     * @param coins new amount of coins
     */
    public static void setCoins(int coins) {
        if (coins <= 0)
            throw new IllegalArgumentException("The player can't go into debt."); 
        PlayerStats.coins = coins;
    }

    /**
     * Takes away an amount of coins of the Player
     * @param amount The amount of coins to be taken away
     * @return a booleans that indicates the amount of coins is sufficient to be taken away
     */
    public static boolean removeCoins(int amount) {
        if (amount <= 0)
            throw new IllegalArgumentException("Can't take non-positive amount of coins.");
        if (amount > coins)
            return false;

        // Successfully remove coins
        coins -= amount;

        return true; 
    }

    /**
     * Gives an amount of coins of the Player
     * @param amount The amount of coins we wanna add
     */
    public static void addCoins(int amount) {
        if (amount <= 0)
            throw new IllegalArgumentException("Can't give non-positive amount of coins.");
        coins += amount; 
    }

    //#region LIVES

    /**
     * Returns the amount of lives left <p>
     * (-1) means has ran out of lives
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

    //#region GUN LEVEL

    /**
     * Returns the Level of Player's currently used Gun in the interval from 1 to 3 
     * @return the current level of the Gun the Player is using
     */
    public static int getGunLevel() { return gunLevel; }
    
    /**
     * Set Player's Gun Level to the specified gunLevel value
     * @param gunLevel new Gun Level
     * @throws IllegalArgumentException when gunLevel is not in the interval from 1 to 3
     */
    public static void setGunLevel(int gunLevel) throws IllegalArgumentException {
        if (gunLevel <= 0 || gunLevel >= 4)
            throw new IllegalArgumentException("test");
        PlayerStats.gunLevel = gunLevel;
    }

    //#endregion

    //#region RUNNER BOOTS
    public static boolean hasRunnerBoots() { return hasRunnerBoots; }
 
    public static void setHasRunnerBoots(boolean hasRunnerBoots) {
        PlayerStats.hasRunnerBoots = hasRunnerBoots; 
    }
    //#endregion

    //#region BURST SHOT
    public static boolean hasBurstShot() { return hasRunnerBoots; }
 
    public static void setHasBurstShot(boolean hasBurstShot) {
        PlayerStats.hasBurstShot = hasBurstShot; 
    }
    //#endregion

    /** Reset Player's Stats */
    public static void resetStats() {
        coins            = 0;
        lives            = GameConstants.Game.PLAYER_LIVES;
        damage           = GameConstants.Player.DAMAGE_BASE;
        currentBaseSpeed = GameConstants.Player.BASE_SPEED; 
        gunLevel         = 1;
        hasRunnerBoots   = false;
        hasBurstShot     = false;
    }
}

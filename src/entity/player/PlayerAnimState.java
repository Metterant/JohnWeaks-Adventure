package entity.player;

/**
 * Enum for determining the state of an animation of a Player.
 */
public enum PlayerAnimState {
    IDLE_DOWN,
    IDLE_UP,
    WALKING_DOWN,
    WALKING_RIGHT,
    WALKING_LEFT,
    WALKING_UP,
    SHOOTING_DOWN_WALKING,
    SHOOTING_UP_WALKING,
    SHOOTING_DOWN_STILL,
    SHOOTING_RIGHT_STILL,
    SHOOTING_LEFT_STILL,
    SHOOTING_UP_STILL;
}

package input;

import entity.Enemy;
import util.GameConstants;
import util.pathfinding.TileCoords;

public class AIController extends InputController {

    Enemy enemy;

    /**
     * Create an AIController instance
     * @param enemy : The enemy that is going to use this AI Controller
     */
    public AIController(Enemy enemy) {
        this.enemy = enemy;
    }

    @Override
    public void calculateInput() {
        double currentX = enemy.getPositionX();
        double currentY = enemy.getPositionY();

        // Next TileCoords the enemy is heading to
        TileCoords nextCoords = enemy.getNextTileCoords();
        if (nextCoords == null) return;

        double directionX = nextCoords.getCol() * GameConstants.TILE_SIZE - currentX;
        double directionY = - (nextCoords.getRow() * GameConstants.TILE_SIZE - currentY);
        
        // Normalize the vector
        double magnitude = Math.sqrt(directionX * directionX + directionY * directionY);
        if (magnitude != 0) {
            directionX /= magnitude;
            directionY /= magnitude;
        }

        inputMoveX = (float)directionX;
        inputMoveY = (float)directionY;
    }
}

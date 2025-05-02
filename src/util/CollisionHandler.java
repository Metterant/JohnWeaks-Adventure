package util;

import entity.Entity;
import tile.TileManager;

/**
 * Handle collision for entities that have collision boxes 
 */
public class CollisionHandler {
    /**
     * Check if an entity going to be displaced will collide with a Collidable Tile. <p>
     * After that, properly adjust entity's position
     * @param entity : The Object of the entity that needs the collision handled
     * @param desiredPosX : The horizontal position in the world to which the entity wants to move
     * @param desiredPosY : The vertical position in the world to which the entity wants to move
     * @param desiredAxialDisplacement : The speed/displacement that the entity is going to move
     */
    public void checkTile(Entity entity, double desiredPosX, double desiredPosY, double desiredAxialDisplacement) {
        // Calculate collision box world coordinates
        double leftX   = desiredPosX + entity.collisionBox.x;
        double rightX  = leftX + entity.collisionBox.width;
        double topY    = desiredPosY + entity.collisionBox.y;
        double bottomY = topY + entity.collisionBox.height;

        // Sides of the collision box
        int leftCol   = (int) leftX   / GameConstants.TILE_SIZE;
        int rightCol  = (int) rightX  / GameConstants.TILE_SIZE;
        int topRow    = (int) topY    / GameConstants.TILE_SIZE;
        int bottomRow = (int) bottomY / GameConstants.TILE_SIZE;

        // Player movement direction
        int moveX = entity.keyHandler.getInputMoveX();
        int moveY = entity.keyHandler.getInputMoveY();

        // columns and rows of the sides of unmoved collision box
        int leftOriginCol   = (int) (leftX   - moveX * desiredAxialDisplacement) / GameConstants.TILE_SIZE;
        int rightOriginCol  = (int) (rightX  - moveX * desiredAxialDisplacement) / GameConstants.TILE_SIZE;
        int topOriginRow    = (int) (topY    + moveY * desiredAxialDisplacement) / GameConstants.TILE_SIZE;
        int bottomOriginRow = (int) (bottomY + moveY * desiredAxialDisplacement) / GameConstants.TILE_SIZE;

        // Tile Manager
        TileManager tm = TileManager.getInstance();

        // Helper lambda to check tile collision to make it less verbose
        java.util.function.BiPredicate<Integer, Integer> isCollidable = (col, row) ->
            tm.tile[tm.tileMapNum[col][row]].isCollidable;

        // Horizontal collision
        if (isCollidable.test(leftCol, topOriginRow) || isCollidable.test(leftCol, bottomOriginRow)) {
            entity.setPositionX(desiredPosX - moveX * desiredAxialDisplacement);
        }
        if (isCollidable.test(rightCol, topOriginRow) || isCollidable.test(rightCol, bottomOriginRow)) {
            entity.setPositionX(desiredPosX - moveX * desiredAxialDisplacement);
        }

        // Vertical collision
        if (isCollidable.test(leftOriginCol, bottomRow) || isCollidable.test(rightOriginCol, bottomRow)) {
            entity.setPositionY(desiredPosY + moveY * desiredAxialDisplacement);
        }
        if (isCollidable.test(leftOriginCol, topRow) || isCollidable.test(rightOriginCol, topRow)) {
            entity.setPositionY(desiredPosY + moveY * desiredAxialDisplacement);
        }
    }
}

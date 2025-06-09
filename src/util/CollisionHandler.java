package util;

import entity.*;
import entity.player.Player;
import main.GameManager;
import tile.TileConstants;
import tile.TileManager;
import util.pathfinding.TileCoords;

/**
 * Handle collision for entities that have collision boxes 
 */
public class CollisionHandler {

    private TileCoords collidedTile;

    /**
     * Check if an entity going to be displaced will collide with a Collidable Tile. <p>
     * After that, properly adjust entity's position
     * @param entity : The Object of the entity that needs the collision handled
     * @param desiredPosX : The horizontal position in the world to which the entity wants to move
     * @param desiredPosY : The vertical position in the world to which the entity wants to move
     * @param desiredAxialDisplacement : The speed/displacement that the entity is going to move
     * @param moveX : Direction on the X axis
     * @param moveY : Direction on the Y axis
     * 
     * @return: a TileCoords that points the location of collided tile
     */
    public TileCoords checkTile(Entity entity, double desiredPosX, double desiredPosY, double desiredAxialDisplacement, double moveX, double moveY) {
        // Calculate collision box world coordinates
        double leftX   = desiredPosX + entity.offsetX;
        double rightX  = leftX + entity.collisionBox.width;
        double topY    = desiredPosY + entity.offsetY;
        double bottomY = topY + entity.collisionBox.height;

        // Sides of the collision box
        int leftCol   = (int) leftX   / GameConstants.TILE_SIZE;
        int rightCol  = (int) rightX  / GameConstants.TILE_SIZE;
        int topRow    = (int) topY    / GameConstants.TILE_SIZE;
        int bottomRow = (int) bottomY / GameConstants.TILE_SIZE;

        // columns and rows of the sides of unmoved collision box
        int leftOriginCol   = (int) (leftX   - moveX * desiredAxialDisplacement) / GameConstants.TILE_SIZE;
        int rightOriginCol  = (int) (rightX  - moveX * desiredAxialDisplacement) / GameConstants.TILE_SIZE;
        int topOriginRow    = (int) (topY    + moveY * desiredAxialDisplacement) / GameConstants.TILE_SIZE;
        int bottomOriginRow = (int) (bottomY + moveY * desiredAxialDisplacement) / GameConstants.TILE_SIZE;

        // Tile Manager
        TileManager tm = TileManager.getInstance();

        // Helper lambda to check tile collision to make it less verbose
        java.util.function.BiPredicate<Integer, Integer> isCollidable = (col, row) ->
            tm.tiles[tm.tileMapNum[col][row]].isCollidable;        

        // Use helper to check for collisions
        int[][] leftCols = { {leftCol, topOriginRow}, {leftCol, bottomOriginRow} };
        int[][] rightCols = { {rightCol, topOriginRow}, {rightCol, bottomOriginRow} };
        int[][] vertBottom = { {leftOriginCol, bottomRow}, {rightOriginCol, bottomRow} };
        int[][] vertTop = { {leftOriginCol, topRow}, {rightOriginCol, topRow} };

        /**
         * This method checks "tilemap collsion" by dividing displacement into 2 cases
         * horizontal and vertical.
         * This method is executed after the player has moved, so to get the old position, it will subtract
         * current player's world position with the displacement the player has just moved. As a result, this
         * method gets player's old position and checks if the position to which the player intended to move
         * will have 2 boxes (tile box and player's collision box) overlap each other. If overlap will occur,
         * this method will prevent the player from moving into the wrong position.
         */

        collidedTile = null;

        // Horizontal collision
        if (anyCollidable(isCollidable, leftCols)) {
            entity.setPositionX(desiredPosX - moveX * desiredAxialDisplacement);
            // Prevent diagonal case
            if (anyCollidable(isCollidable, vertBottom))
                entity.setPositionY(desiredPosY + moveY * desiredAxialDisplacement);
            if (anyCollidable(isCollidable, vertTop))
                entity.setPositionY(desiredPosY + moveY * desiredAxialDisplacement);
        }
        if (anyCollidable(isCollidable, rightCols)) {
            entity.setPositionX(desiredPosX - moveX * desiredAxialDisplacement);
            // Prevent diagonal case
            if (anyCollidable(isCollidable, vertBottom))
                entity.setPositionY(desiredPosY + moveY * desiredAxialDisplacement);
            if (anyCollidable(isCollidable, vertTop))
                entity.setPositionY(desiredPosY + moveY * desiredAxialDisplacement);
        }
        // Vertical collision
        if (anyCollidable(isCollidable, vertBottom)) {
            entity.setPositionY(desiredPosY + moveY * desiredAxialDisplacement);
        }
        if (anyCollidable(isCollidable, vertTop)) {
            entity.setPositionY(desiredPosY + moveY * desiredAxialDisplacement);
        }

        return collidedTile;
    }

    /**
     * Check if an entity is colliding with any colliable object
     * 
     * @param player : entity that needs to check if it is colliding with any Pickable objects
     */
    public void checkPickable(Player player) {
        // Get entities
        var entities = EntityManager.getInstance().instantiatedEntities;
        // Iterate through the list
        for (int i = 0; i < entities.size(); i++) {
            Entity nearbyEntity = entities.get(i);
            if (nearbyEntity == null || nearbyEntity == player) continue;

            if (nearbyEntity instanceof Pickable pickable && isColliding(nearbyEntity, player) 
                && pickable.checkPickupConditions()) {
                pickable.getPickedUp(player);

                EntityManager.getInstance().destroyEntity(nearbyEntity);
            }
        }
    }

    /**
     * Ultility Method used to check collision between two Entities
     * @param entityA first entity
     * @param entityB second entity
     * @return a boolean that indicates whether two objects' collisionBoxes are colliding 
     */
    public boolean isColliding(Entity entityA, Entity entityB) {
        return entityA.collisionBox.intersects(entityB.collisionBox);
    }

    /**
     * Helper to check if any of the given (col, row) pairs are collidable
     */
    private boolean anyCollidable(java.util.function.BiPredicate<Integer, Integer> isCollidable, int[][] positions) {
        for (int[] pos : positions) {
            if (isCollidable.test(pos[0], pos[1])) { 
                // pos[0] is col and pos[1] is row
                collidedTile = new TileCoords(pos[1], pos[0]);
                
                // Collided
                return true;
            }
        }
        // Not collided
        return false;
    }
}

package util;

import entity.*;
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
    public void checkTile(ControllableEntity entity, double desiredPosX, double desiredPosY, double desiredAxialDisplacement) {
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

        // Player movement direction
        int moveX = entity.keyHandler.getInputMoveX();
        int moveY = entity.keyHandler.getInputMoveY();

        // System.out.printf("%d, %d\n", moveX, moveY);
        // System.out.printf("%f\n", desiredAxialDisplacement);

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

        /**
         * This method checks "tilemap collsion" by dividing displacement into 2 cases
         * horizontal and vertical.
         * This method is executed after the player has moved, so to get the old position, it will subtract
         * current player's world position with the displacement the player has just moved. As a result, this
         * method gets player's old position and checks if the position to which the player intended to move
         * will have 2 boxes (tile box and player's collision box) overlap each other. If overlap will occur,
         * this method will prevent the player from moving into the wrong position.
         */
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

    /**
     * Check if an entity is colliding with any colliable object
     * 
     * @param entity : entity that needs to check if it is colliding with any Pickable objects
     */
    public void checkPickable(ControllableEntity entity) {
        // Get entities
        var entities = EntityManager.getInstance().instantiatedEntities;
        // Iterate through the list
        for (int i = 0; i < entities.size(); i++) {
            Entity nearbyEntity = entities.get(i);
            if (nearbyEntity == null || nearbyEntity == entity) continue;

            if (nearbyEntity instanceof Pickable pickable && isColliding(nearbyEntity, entity)) {
                pickable.getPickedUp(entity);

                EntityManager.getInstance().destroyEntity(nearbyEntity);
            }
        }
    }

    private boolean isColliding(Entity entityA, Entity entityB) {
        return entityA.collisionBox.intersects(entityB.collisionBox);
    }
}

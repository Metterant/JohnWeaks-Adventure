package util;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import entity.Enemy;
import entity.Entity;
import entity.Pickable;

/** Manage all Entity instances in the world */
public class EntityManager implements RenewableSingleton, GameComponent {
    public List<Entity> instantiatedEntities = new ArrayList<>();
    private int enemyCount = 0;

    private static EntityManager instance = new EntityManager();
    private EntityManager() {
        instantiatedEntities = new ArrayList<>();
    }

    /* Eager Initialized Method */
    public static synchronized EntityManager getInstance() {
        return instance;
    }

    /**
     * Returns the current count of enemies
     * @return the amount of enemies currently roaming
     */
    public int getEnemyCount() { return enemyCount; }

    /** Remove all null references in the list */
    public void removeNull() {
        Iterator<Entity> itr = instantiatedEntities.iterator();

        while (itr.hasNext()) {
            if (itr.next() == null)
                itr.remove();
        }
    }

    /** Run all update methods from all entities in the world */
    private void entitiesUpdate() { 
        List<Entity> entitiesCopy = new ArrayList<>(instantiatedEntities);
        int currentEntityCount = 0; // Enemy Count

        for (Entity entity : entitiesCopy) {
            if (entity == null) 
                continue;

            // Add enemy count
            if (entity instanceof Enemy)
                currentEntityCount++;

            entity.update();
        }
        
        enemyCount = currentEntityCount;
    }

    /** Run all draw methods from all entities in the world */
    public void entitiesDraw(Graphics2D g2) {
        List<Entity> entitiesCopy = new ArrayList<>(instantiatedEntities);

        for (Entity entity : entitiesCopy) {
            if (entity == null) 
                continue;

            entity.draw(g2);
        }
    }

    /** Add instance */
    public void addInstance(Entity entity) {
        instantiatedEntities.add(entity);
        entity.start();
    }
    
    /** Reset fields of the Singleton */
    public void resetSingleton() {
        instantiatedEntities = new ArrayList<>();
    }

    /** Call an entity's dispose method and remove it from the list */
    public void destroyEntity(Entity entity) {
        entity.dispose();

        // Remove the entity
        // instantiatedEntities.remove(entity);
        for (int i = 0; i < instantiatedEntities.size(); i++) {
            if (instantiatedEntities.get(i) == entity)
                instantiatedEntities.set(i, null);
        }
    }

    @Override
    public void start() { }

    @Override
    public void update() {
        entitiesUpdate();
        removeNull();
    }

    /** Remove all current Enemy instances */
    public void removeAllEnemies() {
        for (int i = 0; i < instantiatedEntities.size(); i++) {
            if (instantiatedEntities.get(i) instanceof Enemy) {
                destroyEntity(instantiatedEntities.get(i));
            }
        }
    }

    /** Remove all current Pickable instances */
    public void removeAllPickables() {
        for (int i = 0; i < instantiatedEntities.size(); i++) {
            if (instantiatedEntities.get(i) instanceof Pickable) {
                destroyEntity(instantiatedEntities.get(i));
            }
        }
    }
}

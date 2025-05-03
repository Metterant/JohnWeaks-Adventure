package util;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import entity.Entity;

/** Manage all Entity instances in the world */
public class EntityManager {
    public List<Entity> instantiatedEntities = new ArrayList<>();

    private static EntityManager instance = new EntityManager();

    private EntityManager() { }

    /* Eager Initialized Method */
    public static synchronized EntityManager getInstance() {
        return instance;
    }

    /** Remove all null references in the list */
    public void removeNull() {
        Iterator<Entity> itr = instantiatedEntities.iterator();

        while (itr.hasNext()) {
            if (itr.next() == null)
                itr.remove();
        }
    }

    /** Run all start methods from all entities in the world */
    public void entitiesStart() {
        for (Entity entity : instantiatedEntities) {
            if (entity == null) 
                continue;

            entity.start();
        }
    }

    /** Run all update methods from all entities in the world */
    public void entitiesUpdate() { 
        for (Entity entity : instantiatedEntities) {
            if (entity == null) 
                continue;

            entity.update();

            // System.out.println(entity.getClass());
        }
    }

    /** Run all draw methods from all entities in the world */
    public void entitiesDraw(Graphics2D g2) {
        for (Entity entity : instantiatedEntities) {
            if (entity == null) 
                continue;

            entity.draw(g2);
        }
    }

    /** d instance */
    public void addInstance(Entity entity) {
        instantiatedEntities.add(entity);
    }
}

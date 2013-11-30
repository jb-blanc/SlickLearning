package fr.angemaster.slick.rpg.game.models;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.tiled.TiledMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class WorldMap {
    private int x;
    private int y;
    private int backgroundLayer;
    private int behindPlayerLayer;
    private int frontOfPlayerLayer;
    private int collisionGroup;
    private int actionGroup;
    private int zoneGroup;
    private TiledMap map;
    private HashMap<String,WorldObject> collisionObjects;
    private List<ActionObject> actionObjects;

    public WorldMap(int x, int y) throws SlickException {
        super();
        this.x = x;
        this.y = y;
        this.collisionObjects = new HashMap<String, WorldObject>();
        this.actionObjects = new ArrayList<ActionObject>();
        this.map = new TiledMap("res/map/map_"+x+"_"+y+".tmx");
        this.backgroundLayer = map.getLayerIndex("background");
        this.behindPlayerLayer = map.getLayerIndex("behindof");
        this.frontOfPlayerLayer = map.getLayerIndex("frontof");
        this.collisionGroup = 0;
        this.actionGroup = 1;
        this.zoneGroup = 2;

        this.loadCollisionObjects();
        this.loadActionObjects();
    }

    public TiledMap getMap(){
        return this.map;
    }

    public void drawBackground(Graphics g){
        this.map.render(0,0,backgroundLayer);
    }

    public void drawBehindPlayer(Graphics g){
        this.map.render(0,0,behindPlayerLayer);
        for(ActionObject o : actionObjects){
            o.draw(g);
        }
    }

    public void drawFrontOfPlayer(Graphics g){
        this.map.render(0,0,frontOfPlayerLayer);
    }

    public void drawObjectsHitbox(Graphics g){
        g.setColor(Color.red);
        for(WorldObject o : collisionObjects.values()){
            o.drawHitbox(g);
        }
        for(ActionObject o : actionObjects){
            o.drawHitbox(g);
        }
    }

    /**
     * Load all objects contained on map and store a Rectangle corresponding to it.
     */
    private void loadCollisionObjects(){
        int objectsCount = map.getObjectCount(collisionGroup);

        for(int i = 0; i < objectsCount; i++){
            String name = map.getObjectName(collisionGroup,i);
            int x = map.getObjectX(collisionGroup, i);
            int y = map.getObjectY(collisionGroup, i);
            int w = map.getObjectWidth(collisionGroup, i);
            int h = map.getObjectHeight(collisionGroup,i);

            collisionObjects.put(name, new WorldObject(x, y, w, h));
        }
    }

    private void loadActionObjects(){
        int objectsCount = map.getObjectCount(actionGroup);

        for(int i = 0; i < objectsCount; i++){
            int x = map.getObjectX(actionGroup, i);
            int y = map.getObjectY(actionGroup, i);
            int w = map.getObjectWidth(actionGroup, i);
            int h = map.getObjectHeight(actionGroup,i);

            actionObjects.add(new ActionObject(x, y, w, h));
        }
    }

    public void addGroundObject(PickableObject object){
        if(!this.actionObjects.contains(object)){
            this.actionObjects.add(object);
        }
    }

    public void removeGroundObject(PickableObject object){
        if(this.actionObjects.contains(object)){
            this.actionObjects.remove(object);
        }
    }


    /**
     * Check if the player can go to a given location
     * @param newX the new x position of the player
     * @param newY the new y position of the player
     * @param collisionShape the shape to test collision with
     * @return true if no obstacle, false otherwise.
     */
    public boolean isFreeSpace(float newX, float newY, Shape collisionShape){
        Rectangle r = new Rectangle(newX+10,newY+22,collisionShape.getWidth(),collisionShape.getHeight());
        for(WorldObject obj: collisionObjects.values()){
            if(obj.getHitbox().intersects(r)){
                return false;
            }
        }
        return true;
    }

    public List<ActionObject> getObjectsInteract(float newX, float newY, Shape collisionShape){
        List<ActionObject> list = new ArrayList<ActionObject>();
        Rectangle r = new Rectangle(newX+10,newY+22,collisionShape.getWidth(),collisionShape.getHeight());
        for(ActionObject obj: actionObjects){
            if(obj.getHitbox().intersects(r)){
                list.add(obj);
            }
        }
        return list;
    }

    public ActionObject getObjectInteract(float newX, float newY, Shape collisionShape){
        Rectangle r = new Rectangle(newX+10,newY+22,collisionShape.getWidth(),collisionShape.getHeight());
        for(ActionObject obj: actionObjects){
            if(obj.isAvailable() && obj.getHitbox().intersects(r)){
                return obj;
            }
        }
        return null;
    }

    public void clearAllGroundObjects(){
        ArrayList<ActionObject> toRemove = new ArrayList<ActionObject>();
        for(ActionObject obj : actionObjects){
            if(obj instanceof PickableObject){
                toRemove.add(obj);
            }
        }
        actionObjects.removeAll(toRemove);
    }
}

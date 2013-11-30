package fr.angemaster.slick.rpg.game.models;

import fr.angemaster.slick.rpg.game.constants.ObjectType;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;

import java.util.Collection;

public class WorldObject {
    private Shape hitbox;
    private float x;
    private float y;
    private float width;
    private float height;
    private int type;

    public WorldObject(float x, float y, float width, float height){
        super();
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.hitbox = new Rectangle(x,y,width,height);
        this.type = ObjectType.STATIC;
    }

    protected void setType(int type){
        this.type = type;
    }

    public int getType(){
        return this.type;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public void draw(Graphics g){

    }

    public void drawHitbox(Graphics g){
        g.setColor(Color.red);
        g.drawRect(this.x, this.y, this.width, this.height);
    }

    public Shape getHitbox(){
        return this.hitbox;
    }
}

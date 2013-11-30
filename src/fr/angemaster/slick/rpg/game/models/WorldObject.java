package fr.angemaster.slick.rpg.game.models;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;

public class WorldObject {
    private Shape hitbox;
    private float x;
    private float y;
    private float width;
    private float height;
    private int type;

    public WorldObject(float x, float y, float width, float height, int type){
        super();
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.hitbox = new Rectangle(x,y,width,height);
    }

    public void drawHitbox(Graphics g){
        g.setColor(Color.red);
        g.drawRect(this.x, this.y, this.width, this.height);
    }

    public Shape getHitbox(){
        return this.hitbox;
    }
}

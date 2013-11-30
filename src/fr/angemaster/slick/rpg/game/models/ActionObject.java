package fr.angemaster.slick.rpg.game.models;

import fr.angemaster.slick.rpg.game.utils.ObjectConstant;
import org.newdawn.slick.Graphics;

public class ActionObject extends WorldObject{

    public ActionObject(float x, float y, float width, float height){
        super(x,y,width,height,1);
    }

    public void drawAction(Graphics g){
        String inter = "Open";
        g.setFont(ObjectConstant.FONT);
        float width = g.getFont().getWidth(inter);
        float height = g.getFont().getHeight(inter);
        float x = (this.getHitbox().getX()+this.getHitbox().getWidth()/2)-(ObjectConstant.PADDING+width/2);
        float y = (this.getHitbox().getY())-(ObjectConstant.PADDING*2 + height);
        g.setColor(ObjectConstant.BACKGROUND_COLOR);
        g.fillRect(x, y, width+ObjectConstant.PADDING*2, height+ObjectConstant.PADDING*2);
        g.setColor(ObjectConstant.FONT_COLOR);
        g.drawString(inter, x+ObjectConstant.PADDING, y+ObjectConstant.PADDING);
    }

}

package fr.angemaster.slick.rpg.game.models;

import fr.angemaster.slick.rpg.game.constants.ObjectType;
import fr.angemaster.slick.rpg.game.exception.GameException;
import fr.angemaster.slick.rpg.game.models.player.Player;
import fr.angemaster.slick.rpg.game.constants.ObjectConstant;
import org.newdawn.slick.Graphics;

public class ActionObject extends WorldObject{

    private String actionName;
    private boolean available;

    public ActionObject(float x, float y, float width, float height){
        super(x,y,width,height);
        this.setType(ObjectType.INTERACTABLE);
        this.setActionName("Default action");
        this.available = true;
    }

    protected void setAvailable(boolean available){
        this.available = available;
    }

    public boolean isAvailable(){
        return this.available;
    }

    protected void setActionName(String name){
        this.actionName = name;
    }

    public void draw(Graphics g){

    }

    public void drawAction(Graphics g){
        g.setFont(ObjectConstant.FONT);
        float width = g.getFont().getWidth(actionName);
        float height = g.getFont().getHeight(actionName);
        float x = (this.getHitbox().getX()+this.getHitbox().getWidth()/2)-(ObjectConstant.PADDING+width/2);
        float y = (this.getHitbox().getY())-(ObjectConstant.PADDING*2 + height);
        g.setColor(ObjectConstant.BACKGROUND_COLOR);
        g.fillRect(x, y, width+ObjectConstant.PADDING*2, height+ObjectConstant.PADDING*2);
        g.setColor(ObjectConstant.FONT_COLOR);
        g.drawString(actionName, x+ObjectConstant.PADDING, y+ObjectConstant.PADDING);
    }

    public void doAction(Player p) throws GameException {

    }

}

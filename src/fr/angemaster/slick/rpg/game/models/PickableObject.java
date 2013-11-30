package fr.angemaster.slick.rpg.game.models;

import fr.angemaster.slick.rpg.game.constants.ObjectType;
import fr.angemaster.slick.rpg.game.exception.GameException;
import fr.angemaster.slick.rpg.game.models.player.Item;
import fr.angemaster.slick.rpg.game.models.player.Player;
import org.newdawn.slick.Graphics;

public class PickableObject extends ActionObject{

    private Item item;
    private boolean picked;

    public PickableObject(Item item, float x, float y) {
        super(x, y, item.getFloorImage().getWidth(), item.getFloorImage().getHeight());
        this.picked = false;
        this.item = item;
        this.setType(ObjectType.LOOTABLE);
        this.setActionName("Ramasser "+item.getDisplayName());
    }

    public void setItem(Item item){
        if(!picked) this.item = item;
    }

    public void draw(Graphics g){
        if(!picked) this.item.renderOnFloor(g, getX(),getY());
    }

    @Override
    public void doAction(Player p) throws GameException {
        if(!picked){
            this.setAvailable(true);
            p.getInventory().addItem(this.item);
            System.out.println(p.getName() + " a rammassé "+item.getDisplayName());
            this.setAvailable(false);
            this.picked = true;
        }
    }
}

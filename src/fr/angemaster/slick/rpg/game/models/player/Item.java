package fr.angemaster.slick.rpg.game.models.player;

import fr.angemaster.slick.rpg.game.utils.GUIConstants;
import fr.angemaster.slick.rpg.game.utils.ObjectConstant;
import org.newdawn.slick.Graphics;

public class Item {
    private String name;
    private double weight;

    public Item(String name, double weight){
        super();
        this.name = name;
        this.weight = weight;
    }

    public String getName(){
        return this.name;
    }

    public double getWeight(){
        return this.weight;
    }

    public void renderInInventory(Graphics g, float x, float y){
        g.setColor(GUIConstants.Item.BACKGROUND);
        g.fillRect(x, y, GUIConstants.Inventory.ITEM_WIDTH, GUIConstants.Inventory.ITEM_HEIGHT);
        g.fillRect(x, y, GUIConstants.Inventory.ITEM_WIDTH, GUIConstants.Inventory.ITEM_HEIGHT);
        g.setFont(GUIConstants.Item.FONT);
        g.setColor(GUIConstants.Item.FONT_COLOR);
        g.drawString(this.name, x, y);
    }
}

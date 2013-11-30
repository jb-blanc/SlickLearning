package fr.angemaster.slick.rpg.game.models.player;

import fr.angemaster.slick.rpg.game.constants.GUIConstants;
import fr.angemaster.slick.rpg.game.constants.ItemNames;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import java.util.HashMap;

public class Item {
    private String name;
    private double weight;
    private Image floorImage;
    private Image inventoryImage;

    public Item(String name, double weight) throws SlickException {
        super();
        this.name = name;
        this.weight = weight;
        this.floorImage = new Image("res/images/items/"+name+"_floor.png");
        this.inventoryImage = new Image("res/images/items/"+name+"_inventory.png");
    }

    public Image getFloorImage() {
        return floorImage;
    }

    public Image getInventoryImage() {
        return inventoryImage;
    }

    public String getName(){
        return this.name;
    }

    public double getWeight(){
        return this.weight;
    }

    public String getDisplayName(){
        return ItemNames.get(this.name);
    }

    public void renderInInventory(Graphics g, float x, float y){
        g.setColor(GUIConstants.Item.BACKGROUND);
        g.drawImage(inventoryImage, x, y);
        g.setFont(GUIConstants.Item.FONT);
        g.setColor(GUIConstants.Item.FONT_COLOR);
        g.drawString(ItemNames.get(this.name), x, y);
    }

    public void renderOnFloor(Graphics g, float x, float y) {
        g.drawImage(floorImage, x, y);
    }

    public static Item createItem(String name) throws SlickException {
        HashMap<String, Double> items = new HashMap<String, Double>();
        items.put("arrow_01",0.2);
        items.put("flute_01",1.2);
        items.put("mace_01",5.4);
        items.put("potion_01",0.2);
        items.put("potion_02",0.2);
        items.put("ring_01",0.6);
        items.put("shield_01",2.5);
        items.put("shield_02",3.8);
        items.put("sword_01",4.2);
        items.put("wand_01",2.5);

        if(items.containsKey(name))
            return new Item(name,items.get(name).doubleValue());
        return null;
    }

}

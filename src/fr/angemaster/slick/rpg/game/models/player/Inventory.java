package fr.angemaster.slick.rpg.game.models.player;

import fr.angemaster.slick.rpg.game.exception.GameException;
import fr.angemaster.slick.rpg.game.utils.GUIConstants;
import fr.angemaster.slick.rpg.game.utils.PlayerConstants;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Inventory {

    private double weight;
    private int size;
    private HashMap<String, List<Item>> items;
    private String title;

    public Inventory(String title){
        super();
        this.title = title;
        this.size = 0;
        this.weight = 0;
        this.items = new HashMap<String, List<Item>>();
    }

    public void checkAdd(Item i) throws GameException {
        if(weight+i.getWeight() > PlayerConstants.Inventory.MAX_WEIGHT){
            throw new GameException("Vous avez trop de poids dans votre inventaire.");
        }
        else if(!items.containsKey(i.getName()) && size + 1 > PlayerConstants.Inventory.SLOTS){
            throw new GameException("Vous n'avez plus de place disponible dans votre inventaire.");
        }
    }

    public void checkRemove(Item i) throws GameException {
        if(!items.containsKey(i.getName())){
            throw new GameException("Cet objet n'est pas dans votre inventaire.");
        }
    }

    public void addItem(Item i) throws GameException {
        checkAdd(i);
        if(items.containsKey(i.getName())){
            items.get(i.getName()).add(i);
        }
        else{
            ArrayList<Item> slot = new ArrayList<Item>();
            slot.add(i);
            items.put(i.getName(),slot);
            this.size += 1;
        }
        this.weight += i.getWeight();
    }

    public void removeItem(Item i) throws GameException{
        checkRemove(i);
        items.get(i.getName()).remove(i);
        if(items.get(i.getName()).size() <= 0){
            items.remove(i.getName());
            this.size -= 1;
        }
        this.weight -= i.getWeight();
    }

    public void render(Graphics g){
        g.setFont(GUIConstants.Inventory.TITLE_FONT);

        float titleStrHeight = g.getFont().getHeight(this.title);
        float titleStrWidth = g.getFont().getWidth(this.title);
        float titleHeight = titleStrHeight + (2 * GUIConstants.Inventory.TITLE_SPACING);
        float oneItemWidth = (GUIConstants.Inventory.ITEM_WIDTH + GUIConstants.Inventory.COL_SPACING);
        float oneItemHeight = (GUIConstants.Inventory.ITEM_HEIGHT + GUIConstants.Inventory.ROW_SPACING);
        float inventoryWidth = (GUIConstants.Inventory.COLS * oneItemWidth) - GUIConstants.Inventory.COL_SPACING + (GUIConstants.Inventory.PADDING * 2);
        float rowHeight = GUIConstants.Inventory.ITEM_HEIGHT + GUIConstants.Inventory.ROW_SPACING;
        float inventoryHeight = (GUIConstants.Inventory.ROWS * rowHeight) - GUIConstants.Inventory.ROW_SPACING + (GUIConstants.Inventory.PADDING * 2);
        float totalHeight = inventoryHeight+titleHeight;


        float x = (GUIConstants.WIDTH/2) - (inventoryWidth/2);
        float y = (GUIConstants.HEIGHT/2) - (totalHeight/2);

        //Paint background
        g.setColor(GUIConstants.Inventory.BACKGROUND);
        g.fillRoundRect(x, y, inventoryWidth, totalHeight, 10);

        //Paint title
        g.setColor(GUIConstants.Inventory.TITLE_FONT_COLOR);
        g.drawString(this.title,x + GUIConstants.Inventory.PADDING , y + GUIConstants.Inventory.TITLE_SPACING);

        String totalWeight = String.format("%.2fKg/%.2fKg", this.weight, PlayerConstants.Inventory.MAX_WEIGHT);
        g.setColor(Color.black);
        g.drawString(
                totalWeight,
                x+inventoryWidth-g.getFont().getWidth(totalWeight)-GUIConstants.Inventory.PADDING,
                y + GUIConstants.Inventory.TITLE_SPACING
        );

        float xInventory = x;
        float yInventory = y + titleHeight;


        //Render background inventory
        for(int row = 0; row < GUIConstants.Inventory.ROWS; row++){
            for(int col = 0; col < GUIConstants.Inventory.COLS; col++){
                float xCase = xInventory + (col * oneItemWidth) + GUIConstants.Inventory.PADDING;
                float yCase = yInventory + (row * oneItemHeight) + GUIConstants.Inventory.PADDING;

                g.setColor(GUIConstants.Inventory.BORDER);
                g.fillRect(xCase-3,yCase-3,GUIConstants.Inventory.ITEM_WIDTH+6,GUIConstants.Inventory.ITEM_HEIGHT+6);
            }
        }

        //Paint each slot
        int cur_col = 0;
        int cur_row = 0;
        for(String name : items.keySet()){

            int count = items.get(name).size();
            float xItem = xInventory + (cur_col * oneItemWidth) + GUIConstants.Inventory.PADDING;
            float yItem = yInventory + (cur_row * oneItemHeight) + GUIConstants.Inventory.PADDING;

            items.get(name).get(0).renderInInventory(g,xItem,yItem);
            double oneItemWeight = items.get(name).get(0).getWeight();

            //Render number of items stacked
            g.setFont(GUIConstants.Inventory.COUNT_FONT);
            String countStr = "x "+count;
            float strWidth = g.getFont().getWidth(countStr);
            float strHeight = g.getFont().getHeight(countStr);
            float xCount = xItem + GUIConstants.Inventory.ITEM_WIDTH - strWidth;
            float yCount = yItem + GUIConstants.Inventory.ITEM_HEIGHT - strHeight;
            g.setColor(GUIConstants.Inventory.COUNT_BACKGROUND);
            g.fillRect(
                    xCount-(2*GUIConstants.Inventory.COUNT_PADDING),
                    yCount-(2*GUIConstants.Inventory.COUNT_PADDING),
                    strWidth+(2*GUIConstants.Inventory.COUNT_PADDING),
                    strHeight+(2*GUIConstants.Inventory.COUNT_PADDING)
            );
            g.setColor(GUIConstants.Inventory.COUNT_FONT_COLOR);
            g.drawString(
                    countStr,
                    xCount - GUIConstants.Inventory.COUNT_PADDING,
                    yCount - GUIConstants.Inventory.COUNT_PADDING
            );

            //Render weight of items stacked
            g.setFont(GUIConstants.Inventory.WEIGHT_FONT);
            String weightStr = String.format("%.2fKg",(oneItemWeight*count));
            float weightWidth = g.getFont().getWidth(weightStr);
            float weightHeight = g.getFont().getHeight(weightStr);
            float xWeight = xItem;
            float yWeight = yItem + GUIConstants.Inventory.ITEM_HEIGHT - weightHeight;
            g.setColor(GUIConstants.Inventory.WEIGHT_BACKGROUND);
            g.fillRect(
                    xWeight,
                    yWeight-(2*GUIConstants.Inventory.WEIGHT_PADDING),
                    weightWidth+(2*GUIConstants.Inventory.WEIGHT_PADDING),
                    weightHeight+(2*GUIConstants.Inventory.WEIGHT_PADDING)
            );
            g.setColor(GUIConstants.Inventory.WEIGHT_FONT_COLOR);
            g.drawString(
                    weightStr,
                    xWeight + GUIConstants.Inventory.WEIGHT_PADDING,
                    yWeight - GUIConstants.Inventory.WEIGHT_PADDING
            );

            if(cur_col > 0 && cur_col % (GUIConstants.Inventory.COLS-1) == 0){
                cur_col = 0;
                cur_row += 1;
            }
            else{
                cur_col += 1;
            }
        }
    }

    public void reset() {
        this.weight = 0;
        this.size = 0;
        this.items = new HashMap<String, List<Item>>();
    }
}

package fr.angemaster.slick.rpg.game.view.gui;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

import java.util.logging.Logger;

public class MenuButton {
    private final static Logger LOG = Logger.getLogger(MenuButton.class.getName());

    private int x;
    private int y;
    private int width;
    private int height;
    private String text;

    private int padding;
    private int borderSize;

    private Color colorNormal;
    private Color colorBorderNormal;
    private Color colorTextNormal;

    private Color colorHover;
    private Color colorBorderHover;
    private Color colorTextHover;

    public MenuButton(String text, int x, int y, int width, int height) {
        this.text = text;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

        this.padding = 10;
        this.borderSize = 5;

        this.colorNormal = Color.white;
        this.colorBorderNormal = Color.lightGray;
        this.colorTextNormal = Color.black;

        this.colorHover = Color.orange;
        this.colorBorderHover = Color.white;
        this.colorTextHover = Color.white;
    }

    public void render(GameContainer gc, Graphics g){
        int mousex = gc.getInput().getMouseX();
        int mousey = gc.getInput().getMouseY();

        float lwidth = g.getLineWidth();
        g.setLineWidth(borderSize);
        if(isIn(mousex,mousey)){
            g.setColor(this.colorBorderHover);
            g.drawRect(this.x, this.y, this.width, this.height);
            g.setColor(this.colorHover);
            g.fillRect(this.x+borderSize, this.y+borderSize, this.width-(2*borderSize), this.height-(2*borderSize));
            g.setColor(this.colorTextHover);
            g.drawString(this.text,this.x+borderSize+padding, this.y+borderSize+padding);
        }
        else{
            g.setColor(this.colorBorderNormal);
            g.drawRect(this.x, this.y, this.width, this.height);
            g.setColor(this.colorNormal);
            g.fillRect(this.x+borderSize, this.y+borderSize, this.width-(2*borderSize), this.height-(2*borderSize));
            g.setColor(this.colorTextNormal);
            g.drawString(this.text,this.x+borderSize+padding, this.y+borderSize+padding);
        }
        g.setLineWidth(lwidth);
    }

    public boolean isIn(int x, int y){
        return (x >= this.x && x <= this.x+this.width && y >= this.y && y <= this.y+this.height);
    }
}

package fr.angemaster.slick.rpg.game.exception;

import fr.angemaster.slick.rpg.game.utils.GUIConstants;
import org.newdawn.slick.Graphics;

public class GameException extends Exception{

    private long startDisplay;

    public GameException(String message){
        super(message);
        this.startDisplay = System.currentTimeMillis();
    }

    public void render(Graphics g){
        if(startDisplay + GUIConstants.Exceptions.HINT_DURATION > System.currentTimeMillis()){
            g.setFont(GUIConstants.Exceptions.FONT);
            float w = g.getFont().getWidth(this.getMessage());
            float h = g.getFont().getHeight(this.getMessage());
            float worldWidth = GUIConstants.WIDTH;

            g.setColor(GUIConstants.Exceptions.BACKGROUND);
            g.fillRect(
                    (worldWidth/2) - (w/2) - GUIConstants.Exceptions.PADDING,
                    GUIConstants.Exceptions.TOP,
                    w+(2*GUIConstants.Exceptions.PADDING),
                    h+(2*GUIConstants.Exceptions.PADDING)
            );
            g.setColor(GUIConstants.Exceptions.FONT_COLOR);
            g.drawString(this.getMessage(), (worldWidth/2) - (w/2), GUIConstants.Exceptions.TOP+GUIConstants.Exceptions.PADDING);
        }
    }

}

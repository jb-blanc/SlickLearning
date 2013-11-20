package fr.angemaster.slick.rpg.game.states;

import fr.angemaster.slick.rpg.game.RPG;
import fr.angemaster.slick.rpg.game.view.models.MenuItem;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import java.util.logging.Logger;

public class Menu extends BasicGameState {
    private final static Logger LOG = Logger.getLogger(Menu.class.getName());
    private int id;
    private MenuItem mPlay,mHigh,mQuit;

    public Menu(int id){
        super();
        this.id = id;
    }

    @Override
    public int getID() {
        return this.id;
    }

    @Override
    public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
        mPlay = new MenuItem("Jouer !",gc.getWidth()/2-100,40,200,50);
        mHigh = new MenuItem("Meilleurs scores",gc.getWidth()/2-100,100,200,50);
        mQuit = new MenuItem("Quitter",gc.getWidth()/2-100,160,200,50);
    }

    @Override
    public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
        mPlay.render(gc,g);
        mHigh.render(gc,g);
        mQuit.render(gc,g);
    }

    @Override
    public void update(GameContainer gc, StateBasedGame sbg, int i) throws SlickException {
        int mousex = gc.getInput().getMouseX();
        int mousey = gc.getInput().getMouseY();

        if(gc.getInput().isMousePressed(Input.MOUSE_LEFT_BUTTON)){
            if(mPlay.isIn(mousex,mousey)){
                LOG.info("Enter game state");
                sbg.enterState(RPG.STATE_GAME);
            }
            else if(mHigh.isIn(mousex,mousey)){
                LOG.info("Enter highscores state");
            }
            else if(mQuit.isIn(mousex,mousey)){
                LOG.info("Exit the game");
                System.exit(0);
            }
        }
    }
}

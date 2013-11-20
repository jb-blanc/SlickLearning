package fr.angemaster.slick.rpg.game.states;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import java.util.logging.Logger;

public class Highscores extends BasicGameState {
    private final static Logger LOG = Logger.getLogger(Highscores.class.getName());
    private int id;

    public Highscores(int id) {
        super();
        this.id = id;
    }

    @Override
    public int getID() {
        return this.id;
    }

    @Override
    public void init(GameContainer gameContainer, StateBasedGame stateBasedGame) throws SlickException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void render(GameContainer gameContainer, StateBasedGame stateBasedGame, Graphics graphics) throws SlickException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void update(GameContainer gameContainer, StateBasedGame stateBasedGame, int i) throws SlickException {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}

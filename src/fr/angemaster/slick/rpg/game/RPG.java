package fr.angemaster.slick.rpg.game;

import fr.angemaster.slick.rpg.game.states.*;
import fr.angemaster.slick.rpg.game.states.Game;
import net.java.games.input.Controller;
import net.java.games.input.ControllerEnvironment;
import org.lwjgl.LWJGLUtil;
import org.newdawn.slick.*;
import org.newdawn.slick.state.StateBasedGame;

import java.io.File;
import java.util.logging.Logger;

public class RPG extends StateBasedGame{
    private final static Logger LOG = Logger.getLogger(RPG.class.getName());
    public static final String GAME_NAME = "RPG!";
    public static final int STATE_MENU = 0;
    public static final int STATE_GAME = 1;

    public RPG(String gameName){
        super(gameName);
        this.addState(new Menu(STATE_MENU));
        this.addState(new Game(STATE_GAME));
    }

    @Override
    public void initStatesList(GameContainer gc) throws SlickException {
        this.getState(STATE_MENU).init(gc,this);
        this.getState(STATE_GAME).init(gc,this);
        this.enterState(STATE_GAME);
    }


    public static void main(String[] args){
        settingEnv();
        LOG.info("Starting game");
        RPG rpg = new RPG("RPG !");
        try {
            AppGameContainer container = new AppGameContainer(rpg);
            container.setDisplayMode(960,960,false);
            container.setShowFPS(true);
            container.start();

            Controller[] ca = ControllerEnvironment.getDefaultEnvironment().getControllers();
            for(Controller c : ca){
                LOG.info("Controller : "+c.getName());
            }
        } catch (SlickException e) {
            LOG.severe(e.getMessage());
        }
    }

    private static void settingEnv(){
        String libPath = new File(new File("lib", "natives"), LWJGLUtil.getPlatformName()).getAbsolutePath();
        String javaLibPath = System.getProperty("java.library.path");
        String javaPathAdd = File.pathSeparatorChar+libPath;
        System.setProperty("org.lwjgl.librarypath", libPath);
        if(!javaLibPath.contains(javaPathAdd)){
            System.setProperty("java.library.path", javaLibPath+javaPathAdd);
        }
        javaLibPath = System.getProperty("java.library.path");
        LOG.info("Setting org.lwjgl.librarypath = "+libPath);
        LOG.info("Setting java.library.path = "+javaLibPath);
    }
}

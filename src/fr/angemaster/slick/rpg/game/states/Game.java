package fr.angemaster.slick.rpg.game.states;

import fr.angemaster.slick.rpg.game.RPG;
import fr.angemaster.slick.rpg.game.exception.GameException;
import fr.angemaster.slick.rpg.game.models.ActionObject;
import fr.angemaster.slick.rpg.game.models.player.Item;
import fr.angemaster.slick.rpg.game.models.player.Player;
import fr.angemaster.slick.rpg.game.models.WorldMap;
import fr.angemaster.slick.rpg.game.utils.ConfigConstants;
import fr.angemaster.slick.rpg.game.utils.WorldConstants;
import org.newdawn.slick.*;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class Game extends BasicGameState{
    private final static Logger LOG = Logger.getLogger(Game.class.getName());
    private int id;

    private Player player;
    private WorldMap map;
    private float startX = 100;
    private float startY = 500;
    private int borderTop = 0;
    private int borderBot = 0;
    private int borderLeft = 0;
    private int borderRight = 0;
    private float dayCycleTransitionAlpha;
    private Image alphaMap;
    private Color lightColor = new Color(1f, 1f, 1f, .1f);
    private Color nightBlack = new Color(0, 0, 0, .95f);
    private int transitionTick;
    private int nightTick;
    private int dayTick;
    private boolean inInventory = false;
    private boolean isNight = false;
    private boolean isDay = true;
    private GameException ex;
    private List<Item> playerItems;

    public Game(int id){
        super();
        this.id = id;
        this.dayCycleTransitionAlpha = WorldConstants.TRANSITION_MIN_ALPHA;
        this.dayTick = WorldConstants.DAY_TICK;
        this.transitionTick = WorldConstants.TRANSITION_TICK;
        this.nightTick= WorldConstants.NIGHT_TICK;
        //ex = new GameException("Votre inventaire est plein.");
        this.playerItems = new ArrayList<Item>();
    }

    @Override
    public int getID() {
        return this.id;
    }

    @Override
    public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
        borderTop = 10;
        borderBot = gc.getHeight()-10;
        borderLeft = 10;
        borderRight = gc.getWidth()-10;

        player = new Player("Angemaster",startX,startY);
        map = new WorldMap(0,0);
        alphaMap = new ImageBuffer(gc.getWidth(),gc.getHeight()).getImage();
    }

    @Override
    public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
        map.drawBackground();
        map.drawBehindPlayer();
        player.render(g);
        map.drawFrontOfPlayer();
        renderNightLayout(gc,g);
        player.renderInfo(g);
        if(WorldConstants.DEBUG){
            map.drawObjectsHitbox(g);
            player.drawHitbox(g);
            renderTime(gc,g);
        }

        ActionObject action = map.getObjectInteract(player.getX(), player.getY(), player.getCollideShape());
        if(action != null){
            action.drawAction(g);
        }

        if(inInventory){
            player.getInventory().render(g);
        }

        if(ex != null){
            ex.render(g);
        }
    }

    @Override
    public void update(GameContainer gc, StateBasedGame sbg, int i) throws SlickException {
        updateTime();
        Input ip = gc.getInput();

        boolean moveup = ip.isKeyDown(ConfigConstants.Keyboard.MOVE_UP);
        boolean movedown = ip.isKeyDown(ConfigConstants.Keyboard.MOVE_DOWN);
        boolean moveright = ip.isKeyDown(ConfigConstants.Keyboard.MOVE_RIGHT);
        boolean moveleft = ip.isKeyDown(ConfigConstants.Keyboard.MOVE_LEFT);
        boolean hasStop = !moveleft && !movedown && !moveright && !moveup;

        float speed = player.getSpeed();

        float playerPosX = player.getX();
        float playerPosY = player.getY();
        int playerH = player.getHeight();
        int playerW = player.getWidth();

        float mov = i * speed;
        float movForTest = mov * WorldConstants.ACCELERATION;

        float newPlayerPosY = moveup ? playerPosY - movForTest : movedown ? playerPosY + movForTest : playerPosY;
        float newPlayerPosX = moveleft ? playerPosX - movForTest : moveright ? playerPosX + movForTest : playerPosX;

        boolean cango = map.isFreeSpace(newPlayerPosX,newPlayerPosY, player.getCollideShape());

        if(moveup && playerPosY > borderTop && cango)
            player.moveUp(mov);
        else if(movedown && playerPosY < borderBot-playerH && cango)
            player.moveDown(mov);
        else if(moveleft && playerPosX > borderLeft && cango)
            player.moveLeft(mov);
        else if(moveright && playerPosX < borderRight-playerW && cango)
            player.moveRight(mov);
        else if(hasStop)
            player.playerStop();

        if(ip.isKeyDown(ConfigConstants.Keyboard.ADD_HEALTH))
            player.addHealth(1);
        if(ip.isKeyDown(ConfigConstants.Keyboard.REM_HEALTH))
            player.removeHealth(1);

        if(ip.isKeyPressed(ConfigConstants.Keyboard.SWITCH_TORCH))
            player.switchTorch();

        if(ip.isKeyDown(ConfigConstants.Keyboard.ADD_MAX_HEALTH))
            player.addMaxHealth(1);
        if(ip.isKeyDown(ConfigConstants.Keyboard.REM_MAX_HEALTH))
            player.removeMaxHealth(1);

        if(ip.isKeyPressed(ConfigConstants.Keyboard.MENU))
            sbg.enterState(RPG.STATE_MENU);

        if(ip.isKeyPressed(ConfigConstants.Keyboard.INVENTORY)){
            inInventory = !inInventory;
        }

        if(ip.isKeyPressed(ConfigConstants.Keyboard.ACCELERATION_LESS)){
            if(WorldConstants.ACCELERATION > 1)
                WorldConstants.ACCELERATION -= 1;
        }

        if(ip.isKeyPressed(ConfigConstants.Keyboard.SWITCH_DEBUG)){
            WorldConstants.DEBUG = !WorldConstants.DEBUG;
        }

        if(ip.isKeyPressed(ConfigConstants.Keyboard.ADD_ITEM)){
            this.addItem();
        }
        if(ip.isKeyPressed(ConfigConstants.Keyboard.REM_ITEM)){
            this.removeItem();
        }
        if(ip.isKeyPressed(ConfigConstants.Keyboard.CLEAR_INVENTORY)){
            this.resetInventory();
        }

        if(ip.isKeyPressed(ConfigConstants.Keyboard.ACCELERATION_PLUS)){
            if(WorldConstants.ACCELERATION < 1000)
                WorldConstants.ACCELERATION += 1;
        }

        player.update(i);
    }

    /**
     * Update the current time and alpha on map if needed.
     */
    private void updateTime(){
        if(isNight && !isDay && nightTick > 0){
            nightTick -= WorldConstants.ACCELERATION;
            if(nightTick <= 0){
                isNight = false;
            }
        }
        else if(isDay && !isNight && dayTick > 0){
            dayTick -= WorldConstants.ACCELERATION;
            if(dayTick <= 0){
                isDay = false;
            }
        }
        else if(!isNight && !isDay && transitionTick > 0){
            transitionTick -= WorldConstants.ACCELERATION;
            double modulo = (WorldConstants.TRANSITION_UPDATE/WorldConstants.ACCELERATION);
            if(modulo > 0){
                if(transitionTick%modulo == 0){
                    if(dayTick <= 0)
                        dayCycleTransitionAlpha += WorldConstants.TRANSITION_ALPHA_INC;
                    else if(nightTick <= 0)
                        dayCycleTransitionAlpha -= WorldConstants.TRANSITION_ALPHA_INC;
                }
            }
        }

        if(!isDay && dayTick <= 0 && transitionTick <= 0){
            isNight = true;
            dayTick = WorldConstants.DAY_TICK;
            transitionTick = WorldConstants.TRANSITION_TICK;
            dayCycleTransitionAlpha = WorldConstants.TRANSITION_MAX_ALPHA;
        }

        if(!isNight && nightTick <= 0 && transitionTick <= 0){
            isDay = true;
            nightTick = WorldConstants.NIGHT_TICK;
            transitionTick = WorldConstants.TRANSITION_TICK;
            dayCycleTransitionAlpha = WorldConstants.TRANSITION_MIN_ALPHA;
        }
    }

    /**
     * Render a String on top of screen displaying time informations such as acceleration, day tick, night tick, transition tick, alpha value
     * @param gc the game container
     * @param g the graphics
     */
    public void renderTime(GameContainer gc, Graphics g){
        String strSpeed = "Acceleration: x"+WorldConstants.ACCELERATION+" | Day: "+ dayTick+" | Night: "+nightTick+" | Transition: "+ transitionTick+ " | Alpha: "+ dayCycleTransitionAlpha;
        g.setColor(Color.green);
        int strWidth = g.getFont().getWidth(strSpeed);
        int width = gc.getWidth();
        g.drawString(strSpeed, (width/2)-(strWidth/2), 0);
    }

    /**
     * Draw a black rectangle (with a given alpha) to simulate night in game.
     * This method although render the player flashlight.
     * @param gc the game container
     * @param g the graphics
     * @throws SlickException
     */
    private void renderNightLayout(GameContainer gc, Graphics g) throws SlickException {
        int size = 200;
        int maxSize = 600;
        int dir = player.getDirection();
        dir = dir >= 0 ? dir : 0;
        int halfAngle = 5;
        int maxHalfAngle = 20;
        int angle[] = {0,90,180,270};

        nightBlack.a = dayCycleTransitionAlpha;

        alphaMap.getGraphics().clear();
        alphaMap.getGraphics().setColor(nightBlack);
        alphaMap.getGraphics().fillRect(0, 0, gc.getWidth(), gc.getHeight());

        if(player.isTorchOn()){
            alphaMap.getGraphics().setColor(lightColor);
            for(int i=size,j=0 ; i<=maxSize;i+=15){
                alphaMap.getGraphics().fillArc(
                        player.getX()-(i/2)+(player.getWidth()/2),
                        player.getY()-(i/2)+(player.getHeight()/2),
                        i,
                        i,
                        angle[dir]-halfAngle-j,
                        angle[dir]+halfAngle+j
                );
                j += j>=maxHalfAngle ? 0 : 1;
            }
        }

        g.drawImage(alphaMap,0,0);
    }


    public void addItem(){
        String[] names = {
                "Test",
                "Epée",
                "Potion de vie",
                "Bouclier",
                "Guitare",
                "Eau",
                "Chips",
                "Poulet",
                "Flèches",
                "Arc",
                "Bidon",
                "Cuillère",
                "Bol"
        };

        double[] weight = {
                0.2,
                3.5,
                0.1,
                4.8,
                2.2,
                0.5,
                0.2,
                0.8,
                0.02,
                6.3,
                1.5,
                0.1,
                0.2
        };

        int random = (int)Math.round(Math.random()*(names.length-1));
        Item i = new Item(names[random], weight[random]);
        try {
            this.player.getInventory().addItem(i);
            this.playerItems.add(i);
            System.out.println("Random = " + random + "\nAdding item "+i.getName()+" ("+i.getWeight()+" kg)");
        } catch (GameException e) {
            this.ex = e;
            System.out.println(e.getMessage());
        }
    }

    public void resetInventory(){
        this.playerItems = new ArrayList<Item>();
        this.player.getInventory().reset();
    }

    public void removeItem(){
        if(this.playerItems.size() > 0){
            int random = (int)Math.round(Math.random()*(this.playerItems.size()-1));
            Item i = this.playerItems.get(random);
            try {
                this.player.getInventory().removeItem(i);
                this.playerItems.remove(i);
            } catch (GameException e) {
                this.ex = e;
            }
        }
    }
}

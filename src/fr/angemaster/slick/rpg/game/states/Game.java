package fr.angemaster.slick.rpg.game.states;

import fr.angemaster.slick.rpg.game.RPG;
import fr.angemaster.slick.rpg.game.exception.GameException;
import fr.angemaster.slick.rpg.game.models.ActionObject;
import fr.angemaster.slick.rpg.game.models.PickableObject;
import fr.angemaster.slick.rpg.game.models.npc.NPC;
import fr.angemaster.slick.rpg.game.models.player.Item;
import fr.angemaster.slick.rpg.game.models.player.Player;
import fr.angemaster.slick.rpg.game.models.WorldMap;
import fr.angemaster.slick.rpg.game.constants.ConfigConstants;
import fr.angemaster.slick.rpg.game.constants.WorldConstants;
import fr.angemaster.slick.utils.MathUtils;
import org.newdawn.slick.*;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import java.io.IOException;
import java.util.logging.Logger;

public class Game extends BasicGameState {
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
    private ActionObject currentActionObject;
    private NPC npc;

    public Game(int id){
        super();
        this.id = id;
        this.dayCycleTransitionAlpha = WorldConstants.TRANSITION_MIN_ALPHA;
        this.dayTick = WorldConstants.DAY_TICK;
        this.transitionTick = WorldConstants.TRANSITION_TICK;
        this.nightTick= WorldConstants.NIGHT_TICK;
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

        try {
            player = new Player("Angemaster",startX,startY);
        } catch (IOException e) {
            e.printStackTrace();
        }
        map = new WorldMap(0,0);
        alphaMap = new ImageBuffer(gc.getWidth(),gc.getHeight()).getImage();
        this.npc = new NPC("Monster",400,200);
    }

    @Override
    public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
        map.drawBackground(g);
        map.drawBehindPlayer(g);
        npc.render(g);
        player.render(g);
        map.drawFrontOfPlayer(g);
        renderNightLayout(gc,g);
        player.renderInfo(g);
        if(WorldConstants.DEBUG){
            map.drawObjectsHitbox(g);
            npc.drawHitbox(g);
            npc.renderDetection(g);
            player.drawHitbox(g);
            renderTime(gc,g);
        }

        currentActionObject = map.getObjectInteract(player.getX(), player.getY(), player.getCollisionShape());
        if(currentActionObject != null){
            currentActionObject.drawAction(g);
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
        boolean isRunning = !hasStop && ip.isKeyDown(ConfigConstants.Keyboard.MOVE_RUN);

        float speed = player.getSpeed();

        float playerPosX = player.getX();
        float playerPosY = player.getY();
        int playerH = player.getHeight();
        int playerW = player.getWidth();

        float mov = i * speed * (isRunning ? 1.5f : 1f);
        float movForTest = mov * WorldConstants.ACCELERATION;

        float newPlayerPosY = moveup ? playerPosY - movForTest : movedown ? playerPosY + movForTest : playerPosY;
        float newPlayerPosX = moveleft ? playerPosX - movForTest : moveright ? playerPosX + movForTest : playerPosX;

        boolean cango = map.isFreeSpace(newPlayerPosX,newPlayerPosY, player.getCollisionShape());

        /* MOVEMENT */
        if(moveup && playerPosY > borderTop && cango)
            player.moveUp(mov);
        if(movedown && playerPosY < borderBot-playerH && cango)
            player.moveDown(mov);
        if(moveleft && playerPosX > borderLeft && cango)
            player.moveLeft(mov);
        if(moveright && playerPosX < borderRight-playerW && cango)
            player.moveRight(mov);
        if(hasStop)
            player.playerStop();

        /* PLAYER UTILITY */
        if(ip.isKeyPressed(ConfigConstants.Keyboard.SWITCH_TORCH))
            player.switchTorch();
        if(ip.isKeyPressed(ConfigConstants.Keyboard.INVENTORY)){
            inInventory = !inInventory;
        }
        if(ip.isKeyPressed(ConfigConstants.Keyboard.ACTION)){
            if(currentActionObject != null){
                try {
                    currentActionObject.doAction(this.player);
                } catch (GameException e) {
                    this.ex = e;
                }
            }
        }
        //Si le joueur est dans l'inventaire on écoute les clics
        if(inInventory && ip.isMousePressed(Input.MOUSE_LEFT_BUTTON)){
            player.getInventory().getItemAt(gc.getGraphics(), ip.getMouseX(), ip.getMouseY());
        }

        /* GAME COMMAND*/
        if(ip.isKeyPressed(ConfigConstants.Keyboard.MENU))
            sbg.enterState(RPG.STATE_MENU);


        /* DEBUG MODE */
        if(ip.isKeyPressed(ConfigConstants.Keyboard.SWITCH_DEBUG)){
            WorldConstants.DEBUG = !WorldConstants.DEBUG;
        }
        if(WorldConstants.DEBUG){
            if(ip.isKeyDown(ConfigConstants.Keyboard.ADD_HEALTH))
                player.addHealth(1);
            if(ip.isKeyDown(ConfigConstants.Keyboard.REM_HEALTH))
                player.removeHealth(1);
            if(ip.isKeyDown(ConfigConstants.Keyboard.ADD_MAX_HEALTH))
                player.addMaxHealth(1);
            if(ip.isKeyDown(ConfigConstants.Keyboard.REM_MAX_HEALTH))
                player.removeMaxHealth(1);
            if(ip.isKeyPressed(ConfigConstants.Keyboard.CLEAR_INVENTORY)){
                this.resetInventory();
            }
            if(ip.isKeyPressed(ConfigConstants.Keyboard.SPAWN_ITEM)){
                this.spawnRandomItem();
            }
            if(ip.isKeyPressed(ConfigConstants.Keyboard.CLEAR_MAP)){
                this.map.clearAllGroundObjects();
            }
            if(ip.isKeyPressed(ConfigConstants.Keyboard.ACCELERATION_PLUS)){
                if(WorldConstants.ACCELERATION < 1000)
                    WorldConstants.ACCELERATION += 1;
            }
            if(ip.isKeyPressed(ConfigConstants.Keyboard.ACCELERATION_LESS)){
                if(WorldConstants.ACCELERATION > 1)
                    WorldConstants.ACCELERATION -= 1;
            }
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
        g.drawString(strSpeed, (width / 2) - (strWidth / 2), 0);
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
        //int angle[] = {0,90,180,270};

        nightBlack.a = dayCycleTransitionAlpha;

        alphaMap.getGraphics().clear();
        alphaMap.getGraphics().setColor(nightBlack);
        alphaMap.getGraphics().fillRect(0, 0, gc.getWidth(), gc.getHeight());

        if(player.isTorchOn()){
            alphaMap.getGraphics().setColor(lightColor);
            for(int i=size,j=0 ; i<=maxSize;i+=15){
                float cx = player.getX()-(i/2)+(player.getWidth()/2);
                float cy = player.getY()-(i/2)+(player.getHeight()/2);
                float mouseRot = MathUtils.getRotation(
                        player.getX()+(player.getWidth()/2),
                        player.getY()+(player.getWidth()/2),
                        gc.getInput().getMouseX(),
                        gc.getInput().getMouseY()
                );
                alphaMap.getGraphics().fillArc(
                        cx,
                        cy,
                        i,
                        i,
                        mouseRot-halfAngle-j,
                        mouseRot+halfAngle+j
                        //angle[dir]-halfAngle-j,
                        //angle[dir]+halfAngle+j
                );
                j += j>=maxHalfAngle ? 0 : 1;
            }
        }

        g.drawImage(alphaMap,0,0);
    }

    public void spawnRandomItem(){
        String[] names = {
                "arrow_01",
                "flute_01",
                "mace_01",
                "potion_01",
                "potion_02",
                "ring_01",
                "shield_01",
                "shield_02",
                "sword_01",
                "wand_01"
        };
        int random = (int)Math.round(Math.random()*(names.length-1));
        try {
            Item i = Item.createItem(names[random]);
            PickableObject o = new PickableObject(i,player.getX(),player.getY());
            this.map.addGroundObject(o);
        } catch (SlickException e) {
            e.printStackTrace();
        }
    }

    public void resetInventory(){
        this.player.getInventory().reset();
    }

}

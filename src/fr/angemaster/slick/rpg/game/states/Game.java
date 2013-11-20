package fr.angemaster.slick.rpg.game.states;

import fr.angemaster.slick.rpg.game.RPG;
import fr.angemaster.slick.rpg.game.view.models.Player;
import org.newdawn.slick.*;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.tiled.TiledMap;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class Game extends BasicGameState{
    private final static Logger LOG = Logger.getLogger(Game.class.getName());
    private final static int DAY_TICK = 60000;
    private final static int NIGHT_TICK = 30000;
    private final static int TRANSITION_TICK = 10000;
    private final static float TRANSITION_INCREMENT = .01f;
    private final static int TRANSITION_UPDATE = Math.round(TRANSITION_INCREMENT*TRANSITION_TICK);
    private int id;
    private Player player;
    private TiledMap map;
    private float startX = 100;
    private float startY = 100;
    private float maxSpeed = 4;
    private float minSpeed = .1f;
    private int borderTop = 0;
    private int borderBot = 0;
    private int borderLeft = 0;
    private int borderRight = 0;
    private Map<String,Rectangle> mapObjects;
    private float transitionTime = 0;
    private Image alphaMap;
    private Color lightColor = new Color(1f, 1f, 1f, .1f);
    private Color nightBlack = new Color(0, 0, 0, .95f);
    private int transitionTick = 0;
    private int nightTick = 0;
    private int dayTick = DAY_TICK;
    private boolean isNight = false;
    private boolean isDay = true;
    private int timeAcceleration = 1;

    public Game(int id){
        super();
        this.id = id;
        this.mapObjects = new HashMap<String, Rectangle>();
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
        map = new TiledMap("res/map/level1.tmx");
        loadMapObjects();
        alphaMap = new ImageBuffer(gc.getWidth(),gc.getHeight()).getImage();
    }

    @Override
    public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
        map.render(0, 0);
        renderNightLayout(gc,g);
        player.render(g);
        renderTime(gc,g);
    }

    @Override
    public void update(GameContainer gc, StateBasedGame sbg, int i) throws SlickException {
        updateTime();
        Input ip = gc.getInput();

        boolean moveup = ip.isKeyDown(Input.KEY_Z) || ip.isKeyDown(Input.KEY_UP);
        boolean movedown = ip.isKeyDown(Input.KEY_S) || ip.isKeyDown(Input.KEY_DOWN);
        boolean moveright = ip.isKeyDown(Input.KEY_D) || ip.isKeyDown(Input.KEY_RIGHT);
        boolean moveleft = ip.isKeyDown(Input.KEY_Q) || ip.isKeyDown(Input.KEY_LEFT);
        boolean hasStop = !moveleft && !movedown && !moveright && !moveup;

        float speed = player.getSpeed();

        float playerPosX = player.getX();
        float playerPosY = player.getY();
        int playerH = player.getHeight();
        int playerW = player.getWidth();

        float mov = i * speed;

        float newPlayerPosY = moveup ? playerPosY - mov : movedown ? playerPosY + mov : playerPosY;
        float newPlayerPosX = moveleft ? playerPosX - mov : moveright ? playerPosX + mov : playerPosX;

        boolean cango = canGo(newPlayerPosX,newPlayerPosY);

        if(moveup && playerPosY > borderTop && cango)
            player.moveUp(mov);
        else if(movedown && playerPosY < borderBot-playerH && cango)
            player.moveDown(i*speed);
        else if(moveleft && playerPosX > borderLeft && cango)
            player.moveLeft(i*speed);
        else if(moveright && playerPosX < borderRight-playerW && cango)
            player.moveRight(i*speed);
        else if(hasStop)
            player.playerStop();

        if(ip.isKeyDown(Input.KEY_P))
            player.addHealth(1);
        if(ip.isKeyDown(Input.KEY_O))
            player.removeHealth(1);

        if(ip.isKeyPressed(Input.KEY_F))
            player.switchTorch();

        if(ip.isKeyDown(Input.KEY_ADD))
            player.addMaxHealth(1);
        if(ip.isKeyDown(Input.KEY_SUBTRACT))
            player.removeMaxHealth(1);

        if(ip.isKeyPressed(Input.KEY_ESCAPE))
            sbg.enterState(RPG.STATE_MENU);

        if(ip.isKeyPressed(Input.KEY_1)){
            if(timeAcceleration > 1)
                timeAcceleration -= 1;
        }
        if(ip.isKeyPressed(Input.KEY_2)){
            if(timeAcceleration < 11)
                timeAcceleration += 1;
        }

        player.update(i);
    }

    private void updateTime(){
        if(isNight && !isDay && nightTick > 0){
            nightTick -= 1*timeAcceleration;
            if(nightTick <= 0){
                isNight = false;
            }
        }
        else if(isDay && !isNight && dayTick > 0){
            dayTick -= 1*timeAcceleration;
            if(dayTick <= 0){
                isDay = false;
            }
        }
        else if(!isNight && !isDay && transitionTick > 0){
            transitionTick -= 1*timeAcceleration;
            if(transitionTick%TRANSITION_UPDATE == 0){
                if(dayTick <= 0)
                    transitionTime += TRANSITION_INCREMENT;
                else if(nightTick <= 0)
                    transitionTime -= TRANSITION_INCREMENT;
            }
        }

        if(!isDay && dayTick <= 0 && transitionTick <= 0){
            isNight = true;
            dayTick = DAY_TICK;
            transitionTick = TRANSITION_TICK;
            transitionTime = 1;
        }
        if(!isNight && nightTick <= 0 && transitionTick <= 0){
            isDay = true;
            nightTick = NIGHT_TICK;
            transitionTick = TRANSITION_TICK;
            transitionTime = 0;
        }
    }

    private void loadMapObjects(){
        int groupId = 0;
        int objectsCount = map.getObjectCount(groupId);

        for(int i = 0; i < objectsCount; i++){
            String name = map.getObjectName(groupId,i);
            int x = map.getObjectX(groupId, i);
            int y = map.getObjectY(groupId, i);
            int w = map.getObjectWidth(groupId, i);
            int h = map.getObjectHeight(groupId,i);

            mapObjects.put(name, new Rectangle(x,y,w,h));
        }
    }

    private boolean canGo(float newX, float newY){
        Rectangle r = new Rectangle(newX+10,newY+22,player.getCollideShape().getWidth(),player.getCollideShape().getHeight());
        for(Rectangle obj: mapObjects.values()){
            if(obj.intersects(r))
                return false;
        }

        return true;
    }

    public void renderTime(GameContainer gc, Graphics g){
        String strSpeed = "Acceleration: x"+timeAcceleration+" | Day: "+ dayTick+" | Night: "+nightTick+" | Transition: "+ transitionTick+ " | Alpha: "+transitionTime;
        g.setColor(Color.green);
        int strWidth = g.getFont().getWidth(strSpeed);
        int width = gc.getWidth();
        g.drawString(strSpeed, (width/2)-(strWidth/2), 0);
    }

    private void renderNightLayout(GameContainer gc, Graphics g) throws SlickException {
        int size = 200;
        int maxSize = 600;
        int dir = player.getDirection();
        dir = dir >= 0 ? dir : 0;
        int halfAngle = 5;
        int maxHalfAngle = 20;
        int angle[] = {0,90,180,270};

        nightBlack.a = transitionTime;

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
}

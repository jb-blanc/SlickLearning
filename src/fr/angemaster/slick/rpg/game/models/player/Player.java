package fr.angemaster.slick.rpg.game.models.player;


import fr.angemaster.slick.rpg.game.constants.PlayerConstants;
import fr.angemaster.slick.rpg.game.constants.WorldConstants;
import fr.angemaster.slick.rpg.game.models.WorldCharacter;
import org.newdawn.slick.*;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.openal.Audio;
import org.newdawn.slick.openal.AudioLoader;
import org.newdawn.slick.util.ResourceLoader;

import java.io.IOException;

public class Player implements WorldCharacter {
    private float nameX;
    private float nameY;
    private float healthBarX;
    private float healthBarY;

    //walk animation
    private Animation walkLeft, walkRight, walkUp, walkDown;
    //idle animation
    private Animation idleLeft, idleRight, idleUp, idleDown;

    private SpriteSheet character;
    private Animation currentAnimation;
    private float x;
    private float y;
    private int width;
    private int height;
    private float speed;
    private Rectangle collisionShape;
    private Rectangle hitbox;
    private Rectangle interactionShape;
    private String name;
    private int health;
    private int maxHealth;
    private boolean torchOn;
    private boolean moving;
    private Inventory inventory;
    private Audio audioStep;

    /**
     * Create a new player
     * @param name the player name
     * @param x the starting x coordinate
     * @param y the starting y coordinate
     * @throws SlickException
     */
    public Player(String name, float x, float y) throws SlickException, IOException {
        super();
        this.name = name;
        this.x = x;
        this.y = y;
        this.health = PlayerConstants.Stats.HEALTH;
        this.maxHealth = PlayerConstants.Stats.HEALTH;
        this.speed = PlayerConstants.Stats.SPEED;
        this.width = 32;
        this.height = 32;
        this.torchOn = false;
        this.moving = false;
        this.character = new SpriteSheet("res/images/characters/player.png",width,height);
        this.buildAnimations();
        this.currentAnimation = idleDown;
        this.collisionShape = new Rectangle(this.x+10,this.y+22,10,10);
        this.hitbox = new Rectangle(this.x+5,this.y,this.width - 10,this.height);
        this.interactionShape = new Rectangle(this.x-10,this.y-10,this.width + 20, this.height + 20);
        this.inventory = new Inventory("Inventaire de "+this.name);
        audioStep = AudioLoader.getAudio("OGG", ResourceLoader.getResourceAsStream("res/audio/sfx/step.ogg"));
        this.initStaticValues();
    }

    public String getName(){
        return this.name;
    }

    public Inventory getInventory(){
        return this.inventory;
    }

    /**
     * Switch torch state
     */
    public void switchTorch(){
        this.torchOn = !this.torchOn;
    }

    /**
     * @return true if torch is on, false otherwise
     */
    public boolean isTorchOn(){
        return this.torchOn;
    }

    public boolean isMoving()
    {
        return this.moving;
    }

    public void setMoving(boolean moving)
    {
        this.moving = moving;
    }

    /**
     * Get the player moving direction : <br/>
     * <ul>
     *     <li>0 : Right</li>
     *     <li>1 : Down</li>
     *     <li>2 : Left</li>
     *     <li>3 : Up</li>
     * </ul>
     * @return the int value for the direction
     */
    public int getDirection(){
        if(currentAnimation.equals(idleRight) || currentAnimation.equals(walkRight)) return 0;
        else if(currentAnimation.equals(idleDown) || currentAnimation.equals(walkDown)) return 1;
        else if(currentAnimation.equals(idleLeft) || currentAnimation.equals(walkLeft)) return 2;
        else if(currentAnimation.equals(idleUp) || currentAnimation.equals(walkUp)) return 3;
        else return -1;
    }

    /**
     * Render the player on the given graphics
     * @param g the graphics on which the player must be paint
     */
    public void render(Graphics g){
        g.drawAnimation(currentAnimation, x, y);
    }

    /**
     * Render player info on the given graphics
     * @param g current graphics
     */
    public void renderInfo(Graphics g){
        renderName(g);
        renderHealth(g);
    }

    /**
     * Draw the player hitbox
     * @param g current graphics
     */
    public void drawHitbox(Graphics g){
        g.setColor(Color.red);
        g.drawRect(this.getCollisionShape().getX(), this.getCollisionShape().getY(), this.getCollisionShape().getWidth(), this.getCollisionShape().getHeight());
        g.setColor(Color.green);
        g.drawRect(this.getHitbox().getX(), this.getHitbox().getY(), this.getHitbox().getWidth(), this.getHitbox().getHeight());
        g.setColor(Color.blue);
        g.drawRect(this.getInteractionShape().getX(), this.getInteractionShape().getY(), this.getInteractionShape().getWidth(), this.getInteractionShape().getHeight());
    }

    /**
     * Render of the player name
     * @param g graphics to paint on
     */
    private void renderName(Graphics g){
        g.setColor(PlayerConstants.NAME_COLOR);
        g.setFont(PlayerConstants.NAME_FONT);
        g.drawString(name, x + nameX, y + nameY);
    }

    /**
     * Render the player health bar
     * @param g the graphics to paint on
     */
    private void renderHealth(Graphics g){
        String healthDisplay = health+"/"+maxHealth;
        float currentLifeBarW = (health * PlayerConstants.HealthBar.WIDTH) / maxHealth;
        //Draw border
        g.setColor(PlayerConstants.HealthBar.BORDER_COLOR);
        g.fillRect(
                x + healthBarX - PlayerConstants.HealthBar.BORDER_SIZE,
                y + healthBarY - (2 * PlayerConstants.HealthBar.BORDER_SIZE),
                PlayerConstants.HealthBar.WIDTH + 2 * PlayerConstants.HealthBar.BORDER_SIZE,
                PlayerConstants.HealthBar.HEIGHT + 2 * PlayerConstants.HealthBar.BORDER_SIZE
        );
        //Draw background (max health)
        g.setColor(PlayerConstants.HealthBar.BACKGROUND);
        g.fillRect(
                x + healthBarX,
                y + healthBarY - PlayerConstants.HealthBar.BORDER_SIZE,
                PlayerConstants.HealthBar.WIDTH,
                PlayerConstants.HealthBar.HEIGHT
        );
        //Draw current health bar
        g.setColor(PlayerConstants.HealthBar.FOREGROUND);
        g.fillRect(
                x + healthBarX,
                y + healthBarY - PlayerConstants.HealthBar.BORDER_SIZE,
                currentLifeBarW,
                PlayerConstants.HealthBar.HEIGHT
        );
        //Draw string representation of health
        g.setColor(PlayerConstants.HealthBar.FONT_COLOR);
        g.setFont(PlayerConstants.HealthBar.FONT);
        g.drawString(
                healthDisplay,
                x + healthBarX + (PlayerConstants.HealthBar.WIDTH/2) - (PlayerConstants.HealthBar.FONT.getWidth(healthDisplay)/2),
                y + healthBarY - PlayerConstants.HealthBar.BORDER_SIZE
        );
    }

    /**
     * Initialize some value that will not change (like player name position)
     */
    private void initStaticValues(){
        this.nameX = (width/2) - (PlayerConstants.NAME_FONT.getWidth(name) / 2);
        this.nameY = 0 - PlayerConstants.NAME_FONT.getHeight(name) - PlayerConstants.HealthBar.HEIGHT - PlayerConstants.HealthBar.MARGIN_BOTTOM - PlayerConstants.HealthBar.MARGIN_TOP;
        this.healthBarX = (width/2) - (PlayerConstants.HealthBar.WIDTH/2);
        this.healthBarY = 0 - PlayerConstants.HealthBar.MARGIN_BOTTOM - PlayerConstants.HealthBar.HEIGHT;
    }

    /**
     * Get the collision shape of the player
     * @return
     */
    public Rectangle getCollisionShape(){
        return this.collisionShape;
    }

    /**
     * @return player speed
     */
    public float getSpeed(){
        return this.speed;
    }

    /**
     * Add some current health to the player
     * @param amount amount of life to give to the player
     */
    public void addHealth(int amount){
        if(health+amount <= maxHealth) health += amount;
    }

    /**
     * Remove some current health to the player
     * @param amount amount of life to remove
     */
    public void removeHealth(int amount){
        if(health-amount < 0) health = 0;
        else this.health -= amount;
    }

    /**
     * Add some max health to the player
     * @param amount health to add to the max health
     */
    public void addMaxHealth(int amount){
        this.maxHealth += amount;
    }

    /**
     * Remove some max health to the player
     * @param amount health to remove to the max health
     */
    public void removeMaxHealth(int amount){
        if(maxHealth-amount < 1) maxHealth = 1;
        else maxHealth -= amount;

        if(health > maxHealth) health = maxHealth;
    }

    /**
     * Change player speed
     * @param speed new speed value
     */
    public void setSpeed(float speed){
        this.speed = speed;
        this.walkUp.setSpeed(1+speed);
        this.walkDown.setSpeed(1+speed);
        this.walkLeft.setSpeed(1+speed);
        this.walkRight.setSpeed(1+speed);
    }

    /**
     * Get x coordinates of the player
     * @return x coordinate
     */
    public float getX(){
        return this.x;
    }

    /**
     * Get y coordinates of the player
     * @return y coordinate
     */
    public float getY(){
        return this.y;
    }

    /**
     * Get width of the player sprite
     * @return sprite width
     */
    public int getWidth(){
        return this.width;
    }

    /**
     * Get height of the player sprite
     * @return sprite height
     */
    public int getHeight(){
        return this.height;
    }

    /**
     * Update animation according to the delta of the update thread
     * @param delta
     */
    public void update(int delta){
        if(this.currentAnimation != null){
            this.currentAnimation.update(delta);
        }
    }

    /**
     * Stop current player animation
     */
    private void stopAnimation(){
        if(currentAnimation != null){
            currentAnimation.stop();
            if(!this.isMoving()) {
                audioStep.stop();
            }
        }
    }

    /**
     * Start current player animation
     */
    private void startAnimation(){
        if(currentAnimation != null){
            currentAnimation.start();
        }
    }

    /**
     * Move the player to the left
     * @param inc the moving increment
     */
    public void moveLeft(float inc){
        if(!walkLeft.equals(currentAnimation)){
            stopAnimation();
            currentAnimation = walkLeft;
            this.playStepSound();
            this.setMoving(true);
            startAnimation();
        }
        this.removeX(inc);
    }

    /**
     * Move the player to the right
     * @param inc the moving increment
     */
    public void moveRight(float inc){
        if(!walkRight.equals(currentAnimation)){
            stopAnimation();
            currentAnimation = walkRight;
            this.playStepSound();
            this.setMoving(true);
            startAnimation();
        }
        this.addX(inc);
    }

    /**
     * Move the player to the top
     * @param inc the moving increment
     */
    public void moveUp(float inc){
        if(!walkUp.equals(currentAnimation)){
            stopAnimation();
            currentAnimation = walkUp;
            this.playStepSound();
            this.setMoving(true);
            startAnimation();
        }
        this.removeY(inc);
    }

    /**
     * Move the player to the bottom
     * @param inc the moving increment
     */
    public void moveDown(float inc){
        if(!walkDown.equals(currentAnimation)){
            stopAnimation();
            currentAnimation = walkDown;
            this.playStepSound();
            this.setMoving(true);
            startAnimation();
        }
        this.addY(inc);
    }

    public void playStepSound()
    {
        if(!this.isMoving()) {
            audioStep.playAsSoundEffect(1.0f, .1f, true);
        }
    }

    /**
     * Stop the player movement
     */
    public void playerStop(){
        this.setMoving(false);
        if(walkLeft.equals(currentAnimation)){
            stopAnimation();
            currentAnimation = idleLeft;
            startAnimation();
        }
        else if(walkRight.equals(currentAnimation)){
            stopAnimation();
            currentAnimation = idleRight;
            startAnimation();
        }
        if(walkUp.equals(currentAnimation)){
            stopAnimation();
            currentAnimation = idleUp;
            startAnimation();
        }
        if(walkDown.equals(currentAnimation)){
            stopAnimation();
            currentAnimation = idleDown;
            startAnimation();
        }
    }

    /**
     * Generate animations for the caracter
     */
    public void buildAnimations(){
        //Settings walk animations
        walkDown = new Animation(this.character,0,0,3,0,false,100,false);
        walkDown.setLooping(true);
        walkUp = new Animation(this.character,0,1,3,1,false,100,false);
        walkUp.setLooping(true);
        walkLeft = new Animation(this.character,0,2,3,2,false,100,false);
        walkLeft.setLooping(true);
        walkRight = new Animation(this.character,0,3,3,3,false,100,false);
        walkRight.setLooping(true);

        //Settings idle animations
        idleDown = new Animation(this.character,1,0,1,0,false,1000,false);
        idleUp = new Animation(this.character,1,1,1,1,false,1000,false);
        idleLeft = new Animation(this.character,1,2,1,2,false,1000,false);
        idleRight = new Animation(this.character,1,3,1,3,false,1000,false);
    }


    private void addX(float inc){
        this.x += inc * WorldConstants.ACCELERATION;
        this.collisionShape.setX(this.x + 10);
        this.hitbox.setX(this.x + 5);
        this.interactionShape.setX(this.x - 10);
    }
    private void removeX(float inc){
        this.x -= inc * WorldConstants.ACCELERATION;
        this.collisionShape.setX(this.x + 10);
        this.hitbox.setX(this.x + 5);
        this.interactionShape.setX(this.x - 10);
    }
    private void addY(float inc){
        this.y += inc * WorldConstants.ACCELERATION;
        this.collisionShape.setY(this.y + 22);
        this.hitbox.setY(this.y);
        this.interactionShape.setY(this.y - 10);
    }
    private void removeY(float inc){
        this.y -= inc * WorldConstants.ACCELERATION;
        this.collisionShape.setY(this.y + 22);
        this.hitbox.setY(this.y);
        this.interactionShape.setY(this.y - 10);
    }

    @Override
    public Shape getHitbox() {
        return this.hitbox;
    }

    @Override
    public Shape getInteractionShape() {
        return this.interactionShape;
    }
}

package fr.angemaster.slick.rpg.game.view.models;


import fr.angemaster.slick.rpg.game.view.utils.PlayerConstants;
import org.newdawn.slick.*;
import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Transform;
import org.newdawn.slick.geom.Vector2f;

import java.awt.*;

public class Player {


    private float nameX;
    private float nameY;
    private float healthbarX;
    private float healthbarY;

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
    private Rectangle collideShape;
    private String name;
    private int health = 300;
    private int maxHealth = 300;
    private boolean torchOn = false;

    public Player(String name, float x, float y) throws SlickException {
        super();
        this.name = name;
        this.x = x;
        this.y = y;
        this.speed = .2f;
        this.width = 32;
        this.height = 32;
        this.character = new SpriteSheet("res/characters/player.png",width,height);
        this.buildAnimations();
        this.currentAnimation = idleDown;
        this.collideShape = new Rectangle(this.x+10,this.y+22,20,10);
        this.initStaticValues();
    }

    public void switchTorch(){
        this.torchOn = !this.torchOn;
    }

    public boolean isTorchOn(){
        return this.torchOn;
    }

    public int getDirection(){
        if(currentAnimation.equals(idleRight) || currentAnimation.equals(walkRight)) return 0;
        else if(currentAnimation.equals(idleDown) || currentAnimation.equals(walkDown)) return 1;
        else if(currentAnimation.equals(idleLeft) || currentAnimation.equals(walkLeft)) return 2;
        else if(currentAnimation.equals(idleUp) || currentAnimation.equals(walkUp)) return 3;
        else return -1;
    }

    public void render(Graphics g){
        g.drawAnimation(currentAnimation, x, y);
        renderName(g);
        renderHealth(g);
    }

    private void renderName(Graphics g){
        g.setColor(PlayerConstants.NAME_COLOR);
        g.setFont(PlayerConstants.NAME_FONT);
        g.drawString(name, x + nameX, y + nameY);
    }

    private void renderHealth(Graphics g){
        String healthDisplay = health+"/"+maxHealth;
        float currentLifeBarW = (health * PlayerConstants.HealthBar.WIDTH) / maxHealth;

        g.setColor(PlayerConstants.HealthBar.BORDER_COLOR);
        g.fillRect(
                x + healthbarX - PlayerConstants.HealthBar.BORDER_SIZE,
                y + healthbarY - (2 * PlayerConstants.HealthBar.BORDER_SIZE),
                PlayerConstants.HealthBar.WIDTH + 2 * PlayerConstants.HealthBar.BORDER_SIZE,
                PlayerConstants.HealthBar.HEIGHT + 2 * PlayerConstants.HealthBar.BORDER_SIZE
        );
        g.setColor(PlayerConstants.HealthBar.BACKGROUND);
        g.fillRect(
                x + healthbarX,
                y + healthbarY - PlayerConstants.HealthBar.BORDER_SIZE,
                PlayerConstants.HealthBar.WIDTH,
                PlayerConstants.HealthBar.HEIGHT
        );
        g.setColor(PlayerConstants.HealthBar.FOREGROUND);
        g.fillRect(
                x + healthbarX,
                y + healthbarY - PlayerConstants.HealthBar.BORDER_SIZE,
                currentLifeBarW,
                PlayerConstants.HealthBar.HEIGHT
        );
        g.setColor(PlayerConstants.HealthBar.FONT_COLOR);
        g.setFont(PlayerConstants.HealthBar.FONT);
        g.drawString(
                healthDisplay,
                x + healthbarX + (PlayerConstants.HealthBar.WIDTH/2) - (PlayerConstants.HealthBar.FONT.getWidth(healthDisplay)/2),
                y + healthbarY - PlayerConstants.HealthBar.BORDER_SIZE
        );
    }

    private void initStaticValues(){
        this.nameX = (width/2) - (PlayerConstants.NAME_FONT.getWidth(name) / 2);
        this.nameY = 0 - PlayerConstants.NAME_FONT.getHeight(name) - PlayerConstants.HealthBar.HEIGHT - PlayerConstants.HealthBar.MARGIN_BOTTOM - PlayerConstants.HealthBar.MARGIN_TOP;
        this.healthbarX = (width/2) - (PlayerConstants.HealthBar.WIDTH/2);
        this.healthbarY = 0 - PlayerConstants.HealthBar.MARGIN_BOTTOM - PlayerConstants.HealthBar.HEIGHT;
    }

    public Rectangle getCollideShape(){
        return this.collideShape;
    }

    public float getSpeed(){
        return this.speed;
    }

    public void addHealth(int amount){
        if(health+amount > maxHealth) health = maxHealth;
        else health += amount;
    }
    public void removeHealth(int amount){
        if(health-amount < 0) health = 0;
        else this.health -= amount;
    }

    public void addMaxHealth(int amount){
        this.maxHealth += amount;
    }

    public void removeMaxHealth(int amount){
        if(maxHealth-amount < 1) maxHealth = 1;
        else maxHealth -= amount;

        if(health > maxHealth) health = maxHealth;
    }

    public void setSpeed(float speed){
        if(speed <= 0) speed = .1f;
        else if(speed > 4f) speed = 4f;
        this.speed = speed;
        this.walkUp.setSpeed(1+speed);
        this.walkDown.setSpeed(1+speed);
        this.walkLeft.setSpeed(1+speed);
        this.walkRight.setSpeed(1+speed);
    }

    public float getX(){
        return this.x;
    }

    public float getY(){
        return this.y;
    }

    public int getWidth(){
        return this.width;
    }

    public int getHeight(){
        return this.height;
    }

    public void update(int delta){
        if(this.currentAnimation != null){
            this.currentAnimation.update(delta);
        }
    }

    private void stopAnimation(){
        if(currentAnimation != null){
            currentAnimation.stop();
        }
    }

    private void startAnimation(){
        if(currentAnimation != null){
            currentAnimation.start();
        }
    }

    public void moveLeft(float inc){
        if(!walkLeft.equals(currentAnimation)){
            stopAnimation();
           currentAnimation = walkLeft;
            startAnimation();
        }
        this.x -= inc;
        this.collideShape.setX(this.x+10);
    }

    public void moveRight(float inc){
        if(!walkRight.equals(currentAnimation)){
            stopAnimation();
            currentAnimation = walkRight;
            startAnimation();
        }
        this.x += inc;
        this.collideShape.setX(this.x+10);
    }

    public void moveUp(float inc){
        if(!walkUp.equals(currentAnimation)){
            stopAnimation();
            currentAnimation = walkUp;
            startAnimation();
        }
        this.y -= inc;
        this.collideShape.setY(this.y+22);
    }

    public void moveDown(float inc){
        if(!walkDown.equals(currentAnimation)){
            stopAnimation();
            currentAnimation = walkDown;
            startAnimation();
        }
        this.y += inc;
        this.collideShape.setY(this.y+22);
    }

    public void playerStop(){
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

}

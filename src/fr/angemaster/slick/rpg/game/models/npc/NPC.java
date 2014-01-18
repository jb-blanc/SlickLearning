package fr.angemaster.slick.rpg.game.models.npc;

import fr.angemaster.slick.rpg.game.constants.NPCConstants;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.*;

public class NPC{
    private float nameX;
    private float nameY;
    private float healthBarX;
    private float healthBarY;

    private String name;
    private float x;
    private float y;
    private int width;
    private int height;
    private Shape hitbox;
    private Shape collisionShape;
    private Shape interactionShape;
    private Shape detectionRange;
    private float detectionRadius;
    private int health;
    private int maxHealth;
    private float speed;

    public NPC(String name, float x, float y){
        super();
        this.name = name;
        this.x = x;
        this.y = y;
        this.width = 32;
        this.height = 32;
        this.health = 500;
        this.maxHealth = 500;
        this.speed = .1f;
        this.detectionRadius = 200;

        initStaticValues();
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public float getDetectionRadius() {
        return detectionRadius;
    }

    public void setDetectionRadius(float detectionRadius) {
        this.detectionRadius = detectionRadius;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public Shape getHitbox() {
        return this.hitbox;
    }

    public Shape getCollisionShape() {
        return this.collisionShape;
    }

    public Shape getInteractionShape() {
        return this.interactionShape;
    }

    public Shape getDetectionRange(){
        return this.detectionRange;
    }

    public void render(Graphics g){
        g.setColor(Color.orange);
        g.fillRect(this.x, this.y, this.width, this.height);
    }


    /**
     * Initialize some value that will not change (like player name position)
     */
    private void initStaticValues(){
        this.nameX = (width/2) - (NPCConstants.NAME_FONT.getWidth(name) / 2);
        this.nameY = 0 - NPCConstants.NAME_FONT.getHeight(name) - NPCConstants.HealthBar.HEIGHT - NPCConstants.HealthBar.MARGIN_BOTTOM - NPCConstants.HealthBar.MARGIN_TOP;
        this.healthBarX = (width/2) - (NPCConstants.HealthBar.WIDTH/2);
        this.healthBarY = 0 - NPCConstants.HealthBar.MARGIN_BOTTOM - NPCConstants.HealthBar.HEIGHT;

        this.hitbox = new Rectangle(this.x+10, this.y+10, this.width-20, this.height-20);
        this.collisionShape = new Rectangle(this.x+10, this.y+10, this.width-20, this.height-20);
        this.interactionShape = new Rectangle(this.x+10, this.y+10, this.width-20, this.height-20);
        this.detectionRange = new Ellipse(this.x + (this.width / 2), this.y+(this.height/2), this.detectionRadius, this.detectionRadius);
    }
    /**
     * Draw the player hitbox
     * @param g current graphics
     */
    public void drawHitbox(Graphics g){
        g.setColor(Color.red);
        g.draw(this.getCollisionShape());
        g.setColor(Color.green);
        g.draw(this.getHitbox());
        g.setColor(Color.blue);
        g.draw(this.getInteractionShape());
        g.setColor(Color.blue);
        g.draw(this.getInteractionShape());
    }

    /**
     * Render of the player name
     * @param g graphics to paint on
     */
    private void renderName(Graphics g){
        g.setColor(NPCConstants.NAME_COLOR);
        g.setFont(NPCConstants.NAME_FONT);
        g.drawString(name, x + nameX, y + nameY);
    }

    public void renderDetection(Graphics g){
        Color col = Color.yellow;
        col.a = .2f;
        g.setColor(col);
        g.fill(this.getDetectionRange());
    }

    /**
     * Render the player health bar
     * @param g the graphics to paint on
     */
    private void renderHealth(Graphics g){
        String healthDisplay = health+"/"+maxHealth;
        float currentLifeBarW = (health * NPCConstants.HealthBar.WIDTH) / maxHealth;
        //Draw border
        g.setColor(NPCConstants.HealthBar.BORDER_COLOR);
        g.fillRect(
                x + healthBarX - NPCConstants.HealthBar.BORDER_SIZE,
                y + healthBarY - (2 * NPCConstants.HealthBar.BORDER_SIZE),
                NPCConstants.HealthBar.WIDTH + 2 * NPCConstants.HealthBar.BORDER_SIZE,
                NPCConstants.HealthBar.HEIGHT + 2 * NPCConstants.HealthBar.BORDER_SIZE
        );
        //Draw background (max health)
        g.setColor(NPCConstants.HealthBar.BACKGROUND);
        g.fillRect(
                x + healthBarX,
                y + healthBarY - NPCConstants.HealthBar.BORDER_SIZE,
                NPCConstants.HealthBar.WIDTH,
                NPCConstants.HealthBar.HEIGHT
        );
        //Draw current health bar
        g.setColor(NPCConstants.HealthBar.FOREGROUND);
        g.fillRect(
                x + healthBarX,
                y + healthBarY - NPCConstants.HealthBar.BORDER_SIZE,
                currentLifeBarW,
                NPCConstants.HealthBar.HEIGHT
        );
        //Draw string representation of health
        g.setColor(NPCConstants.HealthBar.FONT_COLOR);
        g.setFont(NPCConstants.HealthBar.FONT);
        g.drawString(
                healthDisplay,
                x + healthBarX + (NPCConstants.HealthBar.WIDTH/2) - (NPCConstants.HealthBar.FONT.getWidth(healthDisplay)/2),
                y + healthBarY - NPCConstants.HealthBar.BORDER_SIZE
        );
    }
}

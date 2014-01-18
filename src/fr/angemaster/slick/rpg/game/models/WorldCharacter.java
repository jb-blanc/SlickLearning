package fr.angemaster.slick.rpg.game.models;

import org.newdawn.slick.geom.Shape;

public interface WorldCharacter {

    public Shape getHitbox();
    public Shape getCollisionShape();
    public Shape getInteractionShape();

}

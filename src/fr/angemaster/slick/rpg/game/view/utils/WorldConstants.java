package fr.angemaster.slick.rpg.game.view.utils;

public class WorldConstants {
    public static int ACCELERATION = 1;
    public final static int DAY_TICK = 120000;
    public final static int NIGHT_TICK = 60000;
    public final static int TRANSITION_TICK = 20000;
    public final static float TRANSITION_ALPHA_INC = .001f;
    public final static float TRANSITION_MAX_ALPHA = .96f;
    public final static float TRANSITION_MIN_ALPHA = 0f;
    public final static int TRANSITION_UPDATE = Math.round((TRANSITION_ALPHA_INC *TRANSITION_TICK)/TRANSITION_MAX_ALPHA);
}

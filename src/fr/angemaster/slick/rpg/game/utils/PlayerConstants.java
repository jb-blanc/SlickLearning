package fr.angemaster.slick.rpg.game.utils;

import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.TrueTypeFont;

public class PlayerConstants {

    public static final Font NAME_FONT = new TrueTypeFont(new java.awt.Font("Trebuchet",java.awt.Font.BOLD,10),true);
    public static final Color NAME_COLOR = Color.green;

    public static class HealthBar{
        public static final Font FONT = new TrueTypeFont(new java.awt.Font("Trebuchet",java.awt.Font.BOLD,9),true);
        public static final Color BACKGROUND = Color.black;
        public static final Color FOREGROUND = Color.red;
        public static final Color FONT_COLOR = Color.white;
        public static final Color BORDER_COLOR = Color.black;
        public static final int MARGIN_TOP = 5;
        public static final int MARGIN_BOTTOM = 5;
        public static final int BORDER_SIZE = 2;
        public static final int WIDTH = 60;
        public static final int HEIGHT = 11;
    }

    public static class Stats{
        public static final int EXPERIENCE = 0;
        public static final int LEVEL = 1;
        public static final int HEALTH = 300;
        public static final int STAMINA = 100;
        public static final int STRENGTH = 5;
        public static final int DEXTERITY = 5;
        public static final int INTELLIGENCE = 5;
        public static final float SPEED = .1f;
    }

    public static class Inventory{
        public static int SLOTS = GUIConstants.Inventory.COLS * GUIConstants.Inventory.ROWS;
        public static double MAX_WEIGHT = 50;
    }
}

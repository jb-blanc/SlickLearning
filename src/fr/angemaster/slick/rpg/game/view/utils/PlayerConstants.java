package fr.angemaster.slick.rpg.game.view.utils;

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
}

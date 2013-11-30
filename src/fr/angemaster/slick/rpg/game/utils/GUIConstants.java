package fr.angemaster.slick.rpg.game.utils;

import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.TrueTypeFont;

public class GUIConstants {

    public static final int WIDTH = 960;
    public static final int HEIGHT = 960;

    public static class Exceptions{
        public static final Font FONT = new TrueTypeFont(new java.awt.Font("Trebuchet",java.awt.Font.BOLD,16),true);
        public final static Color FONT_COLOR = Color.white;
        public final static Color BACKGROUND = Color.red;
        public final static int HINT_DURATION = 3000;
        public final static int PADDING = 20;
        public final static int TOP = 100;
        public final static int RADIUS = 20;
    }

    public static class Inventory {
        public static final Font TITLE_FONT = new TrueTypeFont(new java.awt.Font("Trebuchet",java.awt.Font.BOLD,18),true);
        public static final Color BACKGROUND = Color.lightGray;
        public static final Color TITLE_FONT_COLOR = Color.orange;
        public static final Color BORDER = Color.black;
        public static final float TITLE_SPACING = 5;
        public static final float ITEM_WIDTH = 64;
        public static final float ITEM_HEIGHT = 64;
        public static final float COL_SPACING = 10;
        public static final float ROW_SPACING = 20;
        public static final float PADDING = 20;
        public static int ROWS = 4;
        public static int COLS = 10;

        public static final Font COUNT_FONT = new TrueTypeFont(new java.awt.Font("Trebuchet",java.awt.Font.BOLD,8),true);
        public static final Color COUNT_FONT_COLOR = Color.red;
        public static final Color COUNT_BACKGROUND = Color.black;
        public static final float COUNT_PADDING = 5;

        public static final Font WEIGHT_FONT = new TrueTypeFont(new java.awt.Font("Trebuchet",java.awt.Font.BOLD,8),true);
        public static final Color WEIGHT_FONT_COLOR = Color.red;
        public static final Color WEIGHT_BACKGROUND = Color.black;
        public static final float WEIGHT_PADDING = 5;
    }

    public static class Item {
        public static final Font FONT = new TrueTypeFont(new java.awt.Font("Trebuchet",java.awt.Font.BOLD,9),true);
        public static final Color BACKGROUND = Color.orange;
        public static final Color FONT_COLOR = Color.white;
    }
}

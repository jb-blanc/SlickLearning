package fr.angemaster.slick.rpg.game.view.utils;


import org.newdawn.slick.Input;

public class ConfigConstants {

    public static class Keyboard{
        /* PLAYER CONTROLS */
        public static int MOVE_UP = Input.KEY_Z;
        public static int MOVE_DOWN = Input.KEY_S;
        public static int MOVE_LEFT = Input.KEY_Q;
        public static int MOVE_RIGHT = Input.KEY_D;
        public static int SWITCH_TORCH = Input.KEY_F;
        public static int ACTION = Input.KEY_E;

        /* INTERFACE */
        public static int MENU = Input.KEY_ESCAPE;


        /* ONLY FOR DEBUG/ DEV */
        public static int ADD_HEALTH = Input.KEY_P;
        public static int REM_HEALTH = Input.KEY_O;
        public static int ADD_MAX_HEALTH = Input.KEY_M;
        public static int REM_MAX_HEALTH = Input.KEY_L;
        public static int ACCELERATION_PLUS = Input.KEY_ADD;
        public static int ACCELERATION_LESS = Input.KEY_SUBTRACT;
    }

}

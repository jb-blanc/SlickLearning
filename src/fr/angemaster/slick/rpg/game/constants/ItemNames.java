package fr.angemaster.slick.rpg.game.constants;

import java.util.HashMap;

public class ItemNames {

    public static HashMap<String, String> equivalence = new HashMap<String, String>();

    static{
        equivalence.put("arrow_01","Flèche");
        equivalence.put("flute_01","Flûte");
        equivalence.put("mace_01","Masse");
        equivalence.put("potion_01","Potion de vie");
        equivalence.put("potion_02","Potion de mana");
        equivalence.put("ring_01","Bague");
        equivalence.put("shield_01","Bouclier léger");
        equivalence.put("shield_02","Bouclier lourd");
        equivalence.put("sword_01","Epée");
        equivalence.put("wand_01","Bâton magique");
    }

    public static String get(String name){
        if(equivalence.containsKey(name))
            return equivalence.get(name);
        return "?!"+name+"!?";
    }

}

#SlickLearning

SlickLearning is a Java based game created with Slick2D allowing me to learn how to use this library.
Actually, I don't really know what i'm going to implements into the game. But this is what I have in mind for the game type and basics.

##Story
You are one of the last survivor of the Human race. Everything began when strange animals were invading some cities.
First, everybody was thinking it was a genetic mutation, but nah, that would have been to easy. In fact, it was an Alien race who sent his units to the Earth to exterminate us.
Today, there is few people left, and some communities has formed. Create one or join one to get stronger and maybe one day some places on Earth will be safe.

##Technical/Gameplay desire
This game is more of a technical exercice, so this is the things that I really want for this game :
 - *Technical* :
  - Internationalization : at least French and English
  - Reusable components : like Gui components or things like that.
  - Multiplayer : I don't really know where to start, I'll probably use Kryonet librairie but not sure, i have to read some papers before.
  - Day / night cycle : I've allready added one to the game, but not sure the best way to do it.
  - Big map : the idea is to cut the map in many small map and switch form one to another without loading the entire map since the beginning.
 - *Gameplay* :
  - Player health, stamina, hunger, thirst ...
  - Possibility to create/join a community
  - Finding resources for community or himself
  - Abilities and skills :
    - Engineering
    - Cooking
    - Weapons
    - Tools
    - Building
    - ...
  - Player/Community place :
    - Place built by players
    - Possibility to add buildings :
      - Utility :
        - Garden : give some food each day
        - Hangar : building to increase place's stock space
        - Bedroom : add a bed to allow community to host more people
        - Training camp : place to train your player (increase strength ...)
        - Library : place to learn some new abilities
        - ...
      - Defense :
        - Walls :
          - Wood -> can be transform on stone
          - Stone -> can be transform on metal
          - Metal
        - Minefield : inserting a minefield where you want around your place
        - Auto turret : turret having a certain range and firing bullets to enemies (NPC or Players)
        - ...
      - Decorations :
        - Roads
        - Flowers
        - ...

For every building, you'll need some resources to be able to build them.
Every building will have some kind of health bar, and they can be destroyed.
When a building is damaged, you can repair it with some resources.
The Player/Community place will be the respawn point for every member.


##Any ideas ?
If you have any good ideas, you can send me an email or a message on github.
If you want, you can use the bug tracker to give me some ideas with "enhancement" tickets.

#Notes
Until I find a good way to package the game into a runnable jar file and having good dependencies, this is the directories tree you should have at the project root (next to src directory) :
 - lib
  - natives
   - linux
   - mac
   - windows


Into lib, put jars available in [Slick2D] (http://slick.ninjacave.com/) lib directory.
Into natives, put into the right folder the appropriate libs availables into xxx-natives.jar files where xxx is the OS.

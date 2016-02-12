package com.gamecodeschool.platformgame;

import java.util.ArrayList;

/**
 * Created by Stacy on 9.2.2016 г..
 */
public class LevelCave extends LevelData {

    //the position of the player p is arbitrary
    //the actual spawn location of the player is determined by hte call of loadLevel()
    //here p it's put as the first element on the first line of the map, not to be forgotten

    //tile types
    // . = no tile (empty space), 1 = grass, p = player

    LevelCave(){
        tiles = new ArrayList<String>();
        tiles.add("p.............................................");
        tiles.add("..............................................");
        tiles.add(".....................111111...................");
        tiles.add("..............................................");
        tiles.add("............111111............................");
        tiles.add("..............................................");
        tiles.add(".........1111111..............................");
        tiles.add("..............................................");
        tiles.add("..............................................");
        tiles.add("..............................................");
        tiles.add("..............................11111111........");
        tiles.add("..............................................");
    }
}

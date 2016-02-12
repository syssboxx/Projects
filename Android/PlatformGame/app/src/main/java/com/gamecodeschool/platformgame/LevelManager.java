package com.gamecodeschool.platformgame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;

import java.util.ArrayList;

/**
 * Created by Stacy on 9.2.2016 г..
 */
public class LevelManager {

    //the name of the level
    private String level;

    int mapWidth;
    int mapHeight;
    //gravity will be used to different levels have different gravity value in each level
    float gravity;

    private boolean isPlaying;

    Player player;
    int playerIndex;

    LevelData levelData;
    ArrayList<GameObject> gameObjects;

    //the player's control buttons
    ArrayList<Rect> currentButtons;
    //the bitmap of the objects we need
    Bitmap[] bitmapsArray;

    public LevelManager(Context context,int pixelsPerMeter,int screenWidth,InputController ic,
                        String level,int px, int py){
        //px,py - starting coordinates of the player
        this.level = level;

        switch (level){

            //first level
            case "LevelCave" :
                levelData = new LevelCave();
                break;

            //extra levels

        }

        //initilize the list to hold all game objects
        gameObjects = new ArrayList<GameObject>();

        //initialize the list to hold 1 of every bitmap
        bitmapsArray = new Bitmap[25];

        //load all the game objects and bitmaps
        loadMapData(context, pixelsPerMeter,px,py);

        isPlaying = true;
    }


    //getters and setters

    public boolean isPlaying(){
        return isPlaying;
    }

    //methods

    //get the bitmap needed based on its index, each index correspond to different bitmap
    public Bitmap getBitmap(char objectType){
        int index;

        switch (objectType){

            case '.': index = 0; break;
            case '1': index = 1; break;
            case 'p': index = 2; break;
            default:  index = 0; break;
        }

        return bitmapsArray[index];
    }

    //get the index of the bitmap, so we can call getBitmap()
    //allows to each game object type to get the correct index to its bitmap in the bitmap array
    public int getBitmapIndex(char objectType){
        int index;
        switch (objectType){
            case '.': index = 0; break;
            case '1': index = 1; break;
            case 'p': index = 2; break;
            default:  index = 0; break;
        }
        return index;
    }

    private void loadMapData(Context context, int pixelsPerMeter,int px,int py){
        //load all the grass tiles
        //and the player

        char c;

        //keep track of where we load our game objects
        int currentIndex = -1;

        //calculate the map's dimensions
        mapHeight = levelData.tiles.size();
        mapWidth = levelData.tiles.get(0).length();

        //iterate over the map to see if the object is empty space or a grass
        //if it's other than '.' - a switch is used to create the object at this location
        //if it's '1'- a new Grass object it's added to the arraylist
        //if it's 'p' the player it's initialized at the location

        for (int i = 0; i < levelData.tiles.size(); i++) {
            for (int j = 0; j < levelData.tiles.get(i).length(); j++) {

                c = levelData.tiles.get(i).charAt(j);
                //for empty spaces nothing to load
                if(c != '.'){
                    currentIndex ++;
                    switch (c){
                        case '1' :
                            //add grass object
                            gameObjects.add(new Grass(j,i,c));
                            break;

                        case 'p':
                            //add the player object
                            gameObjects.add(new Player(context,px,py,pixelsPerMeter));
                            playerIndex = currentIndex;
                            //create a reference to the player
                            player = (Player)gameObjects.get(playerIndex);
                            break;
                    }

                    //check if a bitmap is prepared for the object that we just added in the gameobjects list
                    //if not (the bitmap is still null) - it's have to be prepared
                    if(bitmapsArray[getBitmapIndex(c)] == null){
                        //prepare it and put in the bitmap array
                        bitmapsArray[getBitmapIndex(c)] =
                                gameObjects.get(currentIndex).prepareBitmap(context,
                                                              gameObjects.get(currentIndex).getBitmapName(),
                                                              pixelsPerMeter);
                    }

                }

            }

        }
    }



}

package com.bmhs.gametitle.game.assets.worlds;

import com.badlogic.gdx.Gdx;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.bmhs.gametitle.gfx.assets.tiles.statictiles.WorldTile;
import com.bmhs.gametitle.gfx.utils.TileHandler;


public class WorldGenerator {

    private int worldMapRows, worldMapColumns;

    private int[][] worldIntMap;

    private int seedColor, lightGreen, Green;

    public WorldGenerator (int worldMapRows, int worldMapColumns) {
        this.worldMapRows = worldMapRows;
        this.worldMapColumns = worldMapColumns;

        worldIntMap = new int[worldMapRows][worldMapColumns];
        seedColor =10;
        lightGreen = 17;
        //call methods to build 2D array


        Vector2 mapSeed = new Vector2(MathUtils.random(worldIntMap[0].length), MathUtils.random(worldIntMap.length));
        Vector3 mapSeed = new Vector3(MathUtils.random(worldIntMap[0].length), MathUtils.random(worldIntMap.length));
        System.out.println(mapSeed.y + "" + mapSeed.x);

        worldIntMap[(int)mapSeed.y][(int)mapSeed.x] = 4;

        for(int r = 0; r < worldIntMap.length; r++) {
            for(int c = 0; c < worldIntMap[r].length; c++) {
                Vector2 tempVector = new Vector2(c,r);
                if(tempVector.dst(mapSeed) < 10) {
                    worldIntMap[r][c] = 0;

                }
            }
        }

        randomize();
        //leftCoast();
        //centralSea();
        water();
        seedMap();
        seedIslands(5);
        searchAndExpand(10,seedColor,lightGreen,0.25);
        searchAndExpand(8,seedColor,16,0.75);
        searchAndExpand(6,seedColor,15,0.65);
        searchAndExpand(5,seedColor,14,0.55);
        searchAndExpand(4,seedColor,13,0.45);
        searchAndExpand(3,seedColor,12,0.35);


        generateWorldTextFile();

        Gdx.app.error("WorldGenerator", "WorldGenerator(WorldTile[][][])");
    }

    private void seedIslands(int num) {
        for(int i = 0; i < num; i++) {
            int rSeed = MathUtils.random(worldIntMap.length-1);
            int cSeed = MathUtils.random(worldIntMap[0].length-1);
            worldIntMap[rSeed][cSeed] = seedColor;
        }
    }
    /*
    private void searchAndExpand(int radius) {
        for(int r = 0; r < worldIntMap.length; r++) {
            for(int c = 0; c < worldIntMap[r].length; c++) {

                if (worldIntMap[r][c]== seedColor) {

                    for(int subRow = r - radius; subRow <= r+radius; subRow++) {
                        for(int subCol = c - radius; subCol <= c+radius; subCol++)  {

                            if(subRow >= 0 && subCol >= 0 && subRow <= worldIntMap.length-1 && subCol <= worldIntMap[0].length-1 && worldIntMap[subRow][subCol] !=seedColor) {
                                worldIntMap[subRow][subCol] = 3;
                            }
                        }
                    }
                }


            }



        }
    }

     */

    private void searchAndExpand(int radius, int numToFind, int numToWrite, double probability) {
        for (int r = 0; r < worldIntMap.length; r++) {
            for (int c = 0; c < worldIntMap[r].length; c++) {

                if (worldIntMap[r][c] == numToFind) {

                    for (int subRow = r - radius; subRow <= r + radius; subRow++) {
                        for (int subCol = c - radius; subCol <= c + radius; subCol++) {

                            if (subRow >= 0 && subCol >= 0 && subRow <= worldIntMap.length - 1 && subCol <= worldIntMap[0].length - 1 && worldIntMap[subRow][subCol] != numToFind) {
                                if (Math.random() < probability) {
                                    worldIntMap[subRow][subCol] = numToWrite;


                                }


                            }
                        }
                    }
                }
            }
        }
    }
    private void multipleIslands(int num) {
        Vector3 mapSeed = new Vector3(MathUtils.random(worldIntMap[0].length), MathUtils.random(worldIntMap.length))
        for(int r = 0; r<worldIntMap.length; r++) {
            for(int c = 0; c < worldIntMap[r].length; c+=) {

            }
        }

    }


    public String getWorld3DArrayToString() {
        String returnString = "";

        for(int r = 0; r < worldIntMap.length; r++) {
            for(int c = 0; c < worldIntMap[r].length; c++) {
                returnString += worldIntMap[r][c] + " ";
            }
            returnString += "\n";
        }

        return returnString;
    }

    public void water(){
        for(int r = 0; r < worldIntMap.length; r++) {
            for(int c = 0; c < worldIntMap[r].length; c++) {
                worldIntMap[r][c] = 20;
            }
        }
    }

    public void seedMap() {
        Vector2 mapSeed = new Vector2(MathUtils.random(worldIntMap[0].length), MathUtils.random(worldIntMap.length));
        for(int r = 0; r < worldIntMap.length; r++) {
            for(int c = 0; c < worldIntMap[r].length; c++) {
                Vector2 tempVector = new Vector2(c,r);
                if(tempVector.dst(mapSeed) < 10) {
                    worldIntMap[r][c] = seedColor;

                }
            }
        }
    }







    public void randomize() {
        for(int r = 0; r < worldIntMap.length; r++) {
            for(int c = 0; c < worldIntMap[r].length; c++) {
                worldIntMap[r][c] = MathUtils.random(TileHandler.getTileHandler().getWorldTileArray().size-1);
            }
        }
    }

    public WorldTile[][] generateWorld() {
        WorldTile[][] worldTileMap = new WorldTile[worldMapRows][worldMapColumns];
        for(int r = 0; r < worldIntMap.length; r++) {
            for(int c = 0; c < worldIntMap[r].length; c++) {
                worldTileMap[r][c] = TileHandler.getTileHandler().getWorldTileArray().get(worldIntMap[r][c]);
            }
        }
        return worldTileMap;
    }

    private void generateWorldTextFile() {
        FileHandle file = Gdx.files.local("assets/worlds/world.text");
        file.writeString(getWorld3DArrayToString(), false);
    }

}

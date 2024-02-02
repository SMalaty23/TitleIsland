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


        //leftCoast();
        //centralSea();
        water();
        setBigIslandTiles(70,80,10000);
        generateRandomBigIslands(7);




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

    public void generateRandomIslands(int numIslands) {
        for (int i = 0; i < numIslands; i++) {

            int islandRow = (int) (Math.random() * worldMapRows);
            int islandCol = (int) (Math.random() * worldMapColumns);


            setIslandTiles(islandRow, islandCol);
        }
    }

    private void setIslandTiles(int row, int col) {
        worldIntMap[row][col] = 1;


        setTileIfValid(row - 1, col); // Up
        setTileIfValid(row + 1, col); // Down
        setTileIfValid(row, col - 1); // Left
        setTileIfValid(row, col + 1); // Right
    }

    private void setTileIfValid(int row, int col) {
        if (row >= 0 && row < worldMapRows && col >= 0 && col < worldMapColumns) {
            worldIntMap[row][col] = 1; // Example: 1 represents an island tile
        }
    }


    public void generateRandomBigIslands(int numIslands) {
        for (int i = 0; i < numIslands; i++) {
            // Randomly select coordinates for the center of the island
            int islandRow = (int) (Math.random() * worldMapRows);
            int islandCol = (int) (Math.random() * worldMapColumns);

            // Randomly determine the size of the island
            int islandSize = (int) (Math.random() * 50) + 50; // Adjust the range as needed

            // Set the tiles to represent the island
            setBigIslandTiles(islandRow, islandCol, islandSize);
        }
    }


    private void setBigIslandTiles(int row, int col, int size) {
        int halfSize = size / 2;
        worldIntMap[row][col] = 1;

        int numPoints = size * 100;

        for (int i = 0; i < numPoints; i++) {
            double angle = Math.random() * 2 * Math.PI;
            double distance = Math.sqrt(Math.random()) * halfSize;

            int xOffset = (int) (Math.cos(angle) * distance);
            int yOffset = (int) (Math.sin(angle) * distance);

            int islandX = col + xOffset;
            int islandY = row + yOffset;

            if (islandX >= 0 && islandX < worldMapColumns && islandY >= 0 && islandY < worldMapRows) {
                worldIntMap[islandY][islandX] = 1;
            }
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

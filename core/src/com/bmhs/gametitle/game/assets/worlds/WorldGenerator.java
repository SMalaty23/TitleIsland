package com.bmhs.gametitle.game.assets.worlds;

import com.badlogic.gdx.Gdx;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.bmhs.gametitle.gfx.assets.tiles.statictiles.WorldTile;
import com.bmhs.gametitle.gfx.utils.TileHandler;

import static com.badlogic.gdx.math.MathUtils.random;


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


        Vector2 mapSeed = new Vector2(random(worldIntMap[0].length), random(worldIntMap.length));

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
      //seedMap(6);
      // createIsland(20);
        //createIsland(20);
        //makeIrregularIsland(worldIntMap, 70,70);
        generateIsland(worldIntMap, 70,50);
        addHillFeature(worldIntMap,40,40,60);






        generateWorldTextFile();

        Gdx.app.error("WorldGenerator", "WorldGenerator(WorldTile[][][])");
    }

    private void seedIslands(int num) {
        for(int i = 0; i < num; i++) {
            int rSeed = random(worldIntMap.length-1);
            int cSeed = random(worldIntMap[0].length-1);
            worldIntMap[rSeed][cSeed] = seedColor;
        }
    }

    private void createIsland(int islandSize) {
        // Find the center of the world map
        int centerRow = worldMapRows / 2;
        int centerCol = worldMapColumns / 2;

        // Set the center tile of the island
        worldIntMap[centerRow][centerCol] = 17; // Assuming 17 represents the coastline

        // Randomize island size and shape
        double maxDist = islandSize * 1.5; // Maximum distance from the center of the island
        double maxIslandDist = Math.pow(islandSize * 0.5, 2); // Maximum distance for island influence

        // Iterate over the entire map
        for (int r = 0; r < worldMapRows; r++) {
            for (int c = 0; c < worldMapColumns; c++) {
                // Calculate the squared distance from the current tile to the center of the island
                double distSquared = Math.pow(r - centerRow, 2) + Math.pow(c - centerCol, 2);

                // Check if the tile is within the island's influence
                if (distSquared <= maxIslandDist) {
                    // Calculate the distance from the current tile to the center of the island
                    double distance = Math.sqrt(distSquared);

                    // Adjust the transition probability based on the distance from the center
                    double transitionProbability = 2.0 - (distance / maxDist);

                    // Apply randomness to the transition probability
                    double randomFactor = 0.5; // Adjust this factor to control randomness
                    transitionProbability += (Math.random() - 0.5) * randomFactor;

                    // Check if the current tile is not already a land tile
                    if (worldIntMap[r][c] != 17) {
                        // Determine the type of land tile based on the transition probability
                        if (Math.random() < transitionProbability) {
                            worldIntMap[r][c] = 16; // Green land tile
                        } else {
                            worldIntMap[r][c] = 20; // Water tile
                        }
                    }
                }
            }
        }
    }


    private void makeIrregularIsland(int[][] islandMap, int islandSize, int numClusters) {
        int centerX = islandMap.length / 2;
        int centerY = islandMap[0].length / 2;
        int maxDist = islandSize / 2; // Maximum distance from the center of the island
        double irregularityFactor = 0.20; // Adjust this factor to control irregularity

        // Generate multiple clusters of land tiles
        for (int cluster = 0; cluster < numClusters; cluster++) {
            // Determine a random center for the cluster within the island area
            int clusterCenterX = centerX + (int) (Math.random() * maxDist * 2) - maxDist;
            int clusterCenterY = centerY + (int) (Math.random() * maxDist * 2) - maxDist;

            // Generate land tiles for the cluster
            for (int x = 0; x < islandMap.length; x++) {
                for (int y = 0; y < islandMap[0].length; y++) {
                    // Calculate the distance from the cluster center
                    double distance = Math.sqrt(Math.pow(x - clusterCenterX, 2) + Math.pow(y - clusterCenterY, 2));

                    // Add or remove land tiles based on irregularity factor and randomness
                    if (distance < maxDist) {
                        double randomness = Math.random();

                        if (randomness < irregularityFactor) {
                            islandMap[x][y] = 16; // Green land tile
                        }
                    }
                }
            }
        }
    }


    private void generateIsland(int[][] islandMap, int islandSize, int numClusters) {
        int islandWidth = islandMap.length;
        int islandHeight = islandMap[0].length;
        double irregularityFactor = 0.2; // Adjust this factor to control irregularity

        // Generate multiple clusters of land tiles
        for (int cluster = 0; cluster < numClusters; cluster++) {
            int centerX = (int) (Math.random() * islandWidth);
            int centerY = (int) (Math.random() * islandHeight);
            int maxDist = islandSize / 2; // Maximum distance from the center of the cluster

            // Generate land tiles for the cluster
            for (int x = 0; x < islandWidth; x++) {
                for (int y = 0; y < islandHeight; y++) {
                    // Calculate the distance from the cluster center
                    double distance = Math.sqrt(Math.pow(x - centerX, 2) + Math.pow(y - centerY, 2));

                    // Adjust the irregularity factor based on distance
                    double adjustedFactor = irregularityFactor * (1 - distance / maxDist);

                    // Add or remove land tiles based on irregularity factor and randomness
                    if (distance < maxDist) {
                        double randomness = Math.random();

                        if (randomness < adjustedFactor) {
                            islandMap[x][y] = 16; // Green land tile
                        }
                    }
                }
            }
        }
    }

    private void addHillFeature(int[][] islandMap, int centerX, int centerY, int hillSize) {
        for (int x = centerX - hillSize; x <= centerX + hillSize; x++) {
            for (int y = centerY - hillSize; y <= centerY + hillSize; y++) {
                // Check if the current position is within the island map bounds
                if (x >= 0 && x < islandMap.length && y >= 0 && y < islandMap[0].length) {
                    double distanceToCenter = Math.sqrt(Math.pow(x - centerX, 2) + Math.pow(y - centerY, 2));
                    // Adjust the height based on the distance to the center
                    double heightFactor = 1 - distanceToCenter / hillSize;
                    // Add the hill feature to the island map
                    islandMap[x][y] += (int) (10 * heightFactor);
                }
            }
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


    public void seedMap(int num) {
        for (int i = 0; i < num; i++) {
            Vector2 mapSeed = new Vector2(random(worldIntMap[0].length), random(worldIntMap.length));
            for (int r = 0; r < worldIntMap.length; r++) {
                for (int c = 0; c < worldIntMap[r].length; c++) {
                    Vector2 tempVector = new Vector2(c, r);
                    if (tempVector.dst(mapSeed) < 10) {
                        worldIntMap[r][c] = 56;
                    }
                }
            }
        }
    }

/*
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

*/








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
/*
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
*/


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
        Vector2 mapSeed = new Vector2(random(worldIntMap[0].length), random(worldIntMap.length));
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
                worldIntMap[r][c] = random(TileHandler.getTileHandler().getWorldTileArray().size-1);
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

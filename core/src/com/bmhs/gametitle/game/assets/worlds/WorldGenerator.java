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
    private int seedColor,grass, lake, forestGrass, mountainDirt, dirt, ShallowWater, tree;


    public WorldGenerator(int worldMapRows, int worldMapColumns) {
        this.worldMapRows = worldMapRows;
        this.worldMapColumns = worldMapColumns;

        worldIntMap = new int[worldMapRows][worldMapColumns];
        //call methods to build 2D array

        seedColor = 17;
        lake = 18;
        grass = 57;
        forestGrass =72;
        mountainDirt =44;
        dirt = 45;
        ShallowWater = 19;
        tree = 39;



        Vector2 mapSeed = new Vector2(random(worldIntMap[0].length), random(worldIntMap.length));

        System.out.println(mapSeed.y + "" + mapSeed.x);

        worldIntMap[(int) mapSeed.y][(int) mapSeed.x] = 4;

        for (int r = 0; r < worldIntMap.length; r++) {
            for (int c = 0; c < worldIntMap[r].length; c++) {
                Vector2 tempVector = new Vector2(c, r);
                if (tempVector.dst(mapSeed) < 10) {
                    worldIntMap[r][c] = 0;

                }
            }
        }

        water();
        generateIslands(10,40);
        generateWorldTextFile();

        Gdx.app.error("WorldGenerator", "WorldGenerator(WorldTile[][][])");
    }


    public void generateIslands(int numberOfIslands, int islandSize) {
        for (int i = 0; i < numberOfIslands; i++) {
            int startRow = MathUtils.random(worldIntMap.length - 1);
            int startColumn = MathUtils.random(worldIntMap[0].length - 1);

            worldIntMap[startRow][startColumn] = seedColor;
            randomIslandExpansion(startRow, startColumn, islandSize);
        }


        connectIslands();
    }


    private void randomIslandExpansion(int row, int column, int islandSize) {
        for (int i = 0; i < islandSize; i++) {
            int expandRow = MathUtils.random(-1, 1);
            int expandColumn = MathUtils.random(-1, 1);

            int newRow = row + expandRow;
            int newColumn = column + expandColumn;

            if (newRow >= 0 && newRow < worldIntMap.length && newColumn >= 0 && newColumn < worldIntMap[0].length) {
                worldIntMap[newRow][newColumn] = seedColor;
                row = newRow;
                column = newColumn;
                int expansionRadius = random(4, 7);
                for (int tr = 0; tr < islandSize * 2; tr++) {
                    int treeRow = MathUtils.random(row - islandSize, row + islandSize);
                    int treeCol = MathUtils.random(column - islandSize, column + islandSize);


                    if (treeRow >= 0 && treeRow < worldIntMap.length && treeCol >= 0 && treeCol < worldIntMap[0].length) {

                        if (worldIntMap[treeRow][treeCol] == seedColor) {

                            worldIntMap[treeRow][treeCol] = tree;

                            for (int r = 0; r < worldIntMap.length; r++) {
                                for (int c = 0; c < worldIntMap[r].length; c++) {
                                    if (worldIntMap[r][c] == seedColor) {
                                        for (int g = 0; g < expansionRadius; g++) {
                                            int directions = random(5, 7);
                                            for (int j = 0; j < directions; j++) {
                                                int direction = random(0, 8);
                                                switch (direction) {
                                                    case 0:
                                                        row--;
                                                        firstLayerGrass(row - 1, column);
                                                        secondLayerForestGrass(row - 2, column);
                                                        thridLayerDirt(row - 3, column);
                                                        FourthLayerMoutainDirt(row - 4, column);
                                                        fifthLayerShallowWater(row - 5, column);
                                                        expandIsland(row - 1, column);
                                                        break;
                                                    case 1:
                                                        column++;
                                                        ;
                                                        firstLayerGrass(row, column + 1);
                                                        secondLayerForestGrass(row, column + 2);
                                                        thridLayerDirt(row, column + 3);
                                                        FourthLayerMoutainDirt(row, column + 4);
                                                        fifthLayerShallowWater(row, column + 5);
                                                        expandIsland(row, column + 1);
                                                        break;
                                                    case 2:
                                                        row++;
                                                        firstLayerGrass(row + 1, column);
                                                        secondLayerForestGrass(row + 2, column);
                                                        thridLayerDirt(row + 3, column);
                                                        FourthLayerMoutainDirt(row + 4, column);
                                                        fifthLayerShallowWater(row + 5, column);
                                                        expandIsland(row + 1, column);
                                                        break;
                                                    case 3:
                                                        column--;
                                                        firstLayerGrass(row, column - 1);
                                                        secondLayerForestGrass(row, column - 2);
                                                        thridLayerDirt(row, column - 3);
                                                        FourthLayerMoutainDirt(row, column - 4);
                                                        fifthLayerShallowWater(row, column - 5);
                                                        expandIsland(row, column - 1);
                                                        break;
                                                    case 4:
                                                        row--;
                                                        column--;
                                                        firstLayerGrass(row - 1, column - 1);
                                                        secondLayerForestGrass(row - 2, column - 2);
                                                        thridLayerDirt(row - 3, column - 3);
                                                        FourthLayerMoutainDirt(row - 4, column - 4);
                                                        fifthLayerShallowWater(row - 4, column - 4);
                                                        expandIsland(row - 1, column - 1);
                                                        break;
                                                    case 5:
                                                        row--;
                                                        column++;
                                                        firstLayerGrass(row - 1, column + 1);
                                                        secondLayerForestGrass(row - 1, column + 1);
                                                        thridLayerDirt(row - 2, column + 2);
                                                        FourthLayerMoutainDirt(row - 3, column + 3);
                                                        fifthLayerShallowWater(row - 4, column + 4);
                                                        expandIsland(row - 1, column + 1);
                                                        break;
                                                    case 6:
                                                        row++;
                                                        column--;
                                                        firstLayerGrass(row + 1, column - 1);
                                                        secondLayerForestGrass(row + 2, column - 2);
                                                        thridLayerDirt(row + 3, column - 3);
                                                        FourthLayerMoutainDirt(row + 4, column - 4);
                                                        fifthLayerShallowWater(row + 5, column - 5);
                                                        expandIsland(row + 1, column - 1);
                                                        break;
                                                    case 7:
                                                        row++;
                                                        column++;
                                                        firstLayerGrass(row + 1, column + 1);
                                                        secondLayerForestGrass(row + 2, column + 2);
                                                        thridLayerDirt(row + 3, column + 3);
                                                        FourthLayerMoutainDirt(row + 4, column + 4);
                                                        fifthLayerShallowWater(row + 5, column + 5);
                                                        expandIsland(row + 1, column + 1);
                                                        break;
                                                    case 8:
                                                        setLake(r, c);
                                                        break;
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }



    private void connectIslands() {

        for (int r = 0; r < worldIntMap.length; r++) {
            for (int c = 0; c < worldIntMap[r].length; c++) {

                if (worldIntMap[r][c] == seedColor) {

                    connectLandTile(r, c);
                }
            }
        }
    }


    private void connectLandTile(int row, int col) {
        for (int dr = -1; dr <= 1; dr++) {
            for (int dc = -1; dc <= 1; dc++) {
                int newRow = row + dr;
                int newColumn = col + dc;
                if (newRow >= 0 && newRow < worldIntMap.length && newColumn >= 0
                        && newColumn < worldIntMap[row].length && !(dr == 0 && dc == 0)) {
                    if (worldIntMap[newRow][newColumn] == seedColor) {

                        connectTiles(row, col, newRow, newColumn);
                    }
                }
            }
        }
    }



    private void connectTiles(int row1, int col1, int row2, int col2) {

        int dx = Math.abs(col2 - col1);
        int dy = Math.abs(row2 - row1);
        int sx = col1 < col2 ? 1 : -1;
        int sy = row1 < row2 ? 1 : -1;
        int err = dx - dy;

        while (row1 != row2 || col1 != col2) {
            worldIntMap[row1][col1] = seedColor;

            int e2 = 2 * err;
            if (e2 > -dy) {
                err -= dy;
                col1 += sx;
            }
            if (e2 < dx) {
                err += dx;
                row1 += sy;
            }
        }
    }





    private boolean isValidLocation(int row, int column) {
        return row >= 0 && row < worldIntMap.length && column >= 0 && column < worldIntMap[0].length;
    }
    private void setLake(int centerRow, int centerCol) {

        for (int r = centerRow - 1; r <= centerRow + 1; r++) {
            for (int c = centerCol - 1; c <= centerCol + 1; c++) {
                if (isValidLocation(r, c)) {
                    worldIntMap[r][c] = lake;
                }
            }
        }
    }

    private void firstLayerGrass(int centerRow, int centerCol) {
        applyTerrain(centerRow, centerCol, grass, new int[]{seedColor, lake,grass});
    }

    private void secondLayerForestGrass(int centerRow, int centerCol) {
        applyTerrain(centerRow, centerCol, forestGrass, new int[]{seedColor, lake, grass,forestGrass});
    }

    private void thridLayerDirt(int centerRow, int centerCol) {
        applyTerrain(centerRow, centerCol, dirt, new int[]{seedColor, grass, lake, forestGrass,dirt});
    }

    private void FourthLayerMoutainDirt(int centerRow, int centerCol) {
        applyTerrain(centerRow, centerCol, mountainDirt , new int[]{seedColor, grass, lake, forestGrass, dirt,mountainDirt});
    }

    private void fifthLayerShallowWater(int centerRow, int centerCol) {
        applyTerrain(centerRow, centerCol, ShallowWater, new int[]{seedColor, grass, lake, forestGrass, dirt, mountainDirt});
    }


    private void expandIsland(int row, int column) {
        if (row >= 0 && row < worldIntMap.length && column >= 0 && column < worldIntMap[row].length && worldIntMap[row][column] != seedColor) {
            worldIntMap[row][column] = grass;
        }
    }



    private void applyTerrain(int centerRow, int centerCol, int terrainType, int[] excludeTerrain) {
        for (int r = centerRow - 1; r <= centerRow + 1; r++) {
            for (int c = centerCol - 1; c <= centerCol + 1; c++) {

                if (r >= 0 && r < worldIntMap.length && c >= 0 && c < worldIntMap[r].length) {

                    boolean canApply = true;
                    for (int exclude : excludeTerrain) {
                        if (worldIntMap[r][c] == exclude) {
                            canApply = false;
                            break;
                        }
                    }

                    if (canApply) {
                        worldIntMap[r][c] = terrainType;
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

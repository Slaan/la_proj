package com.brianstempin.vindiniumclient.bot;

import com.brianstempin.vindiniumclient.dto.GameState;

import java.util.ArrayList;

/**
 * Created by Daniel Hofmeister on 18.10.2015.
 */
public class Map {

    private TileType[][] currentMap;
    private DirectionType nearestMineDirection;
    private DirectionType nearestTavernDirection;

    private ArrayList<GameState.Position> mines;
    private ArrayList<GameState.Position> taverns;

    public Map(GameState gameState) {
        currentMap = parseMap(gameState);
        calculateNearestMineAndTavern(gameState.getHero().getPos());
    }

    private TileType[][] parseMap(GameState gameState) {

        int size = gameState.getGame().getBoard().getSize();
        String tiles = gameState.getGame().getBoard().getTiles();
        TileType[][] result = new TileType[size][size];
        int heroNumber = gameState.getHero().getId();
        int positionInTiles = 0;
        taverns = new ArrayList<>();
        mines = new ArrayList<>();

        for(int y=0; y<size; y++) {
            // y-axis
            for(int x=0; x<size; x++) {
                //x-axis
                String tileString = tiles.substring(positionInTiles, positionInTiles+2);
                if(tileString.equals("  ")) {
                    result[x][y] = TileType.FREE;
                } else if(tileString.equals("[]")) {
                    result[x][y] = TileType.TAVERN;
                    GameState.Position tavernpos = new GameState.Position(x,y);
                    taverns.add(tavernpos);
                } else if (tileString.substring(0,1).equals("$")) {
                    if (tileString.substring(1,2).equals(""+heroNumber)) {
                        result[x][y] = TileType.BLOCKED;
                    } else {
                        result[x][y] = TileType.MINE;
                        GameState.Position minepos = new GameState.Position(x,y);
                        mines.add(minepos);
                    }
                } else {
                    result[x][y] = TileType.BLOCKED;
                }
                positionInTiles+=2;
            }
        }
        return result;
    }

    private void calculateNearestMineAndTavern(GameState.Position heropos) {
        GameState.Position closestMinePos = null;
        int curClosestMineDist = Integer.MAX_VALUE;
        GameState.Position closestTavernPos = null;
        int curClosestTavernDist = Integer.MAX_VALUE;

        for(GameState.Position minePos : mines) {
            System.out.println("x = " + minePos.getX() + ", y = " + minePos.getY());
            int mineDist = distanceBetweenPositions(minePos, heropos);
            if (mineDist<curClosestMineDist) {
                closestMinePos = minePos;
                curClosestMineDist = mineDist;
            };
        }

        for(GameState.Position tavernPos : taverns) {
            System.out.println("x = " + tavernPos.getX() + ", y = " + tavernPos.getY());
            int tavernDist = distanceBetweenPositions(tavernPos, heropos);
            if (tavernDist<curClosestTavernDist) {
                closestTavernPos = tavernPos;
                curClosestTavernDist = tavernDist;
            }
        }
        
        nearestMineDirection = calcDirection(heropos, closestMinePos);
        nearestTavernDirection = calcDirection(heropos, closestTavernPos);

    }

    public DirectionType calcDirection(GameState.Position heroPos, GameState.Position objPos) {
        int xDist = heroPos.getX()-objPos.getX();
        int yDist = heroPos.getY()-objPos.getY();
        DirectionType result = null;


        if (yDist>=0) {
            if (xDist<=yDist && xDist>=-yDist) {
                result = DirectionType.NORTH;
            } else if (xDist>yDist) {
                result = DirectionType.WEST;
            } else if (xDist<-yDist) {
                result = DirectionType.EAST;
            }
        }

        if (yDist<0) {
            if (xDist>=yDist && xDist<=-yDist) {
                result = DirectionType.SOUTH;
            } else if (xDist>yDist) {
                result = DirectionType.WEST;
            } else if (xDist<-yDist) {
                result = DirectionType.EAST;
            }
        }

        return result;
    }

    public int distanceBetweenPositions(GameState.Position pos1, GameState.Position pos2) {
        return (Math.abs(pos1.getX()-pos2.getX()) + Math.abs(pos1.getY()-pos2.getY()));
    }

    public TileType[][] getCurrentMap() {
        return currentMap;
    }

    public DirectionType getNearestMineDirection() {
        return nearestMineDirection;
    }

    public DirectionType getNearestTavernDirection() {
        return nearestTavernDirection;
    }
}
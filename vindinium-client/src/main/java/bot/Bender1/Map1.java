package bot.Bender1;

import bot.Bender.DirectionType;
import bot.Bender.TileType;
import bot.Bender1.SimpleHero;
import bot.Bender1.SimpleMine;
import bot.Bender1.SimpleTavern;
import bot.dto.GameState;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Daniel Hofmeister on 18.10.2015.
 */
public class Map1 {

    private TileType[][] currentMap;
    private DirectionType nearestMineDirection;
    private DirectionType nearestTavernDirection;

    private List<GameState.Position> mines;
    private List<GameState.Position> taverns;
    private List<GameState.Position> heroes;

    public Map1(GameState gameState) {
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
        heroes = new ArrayList<>();

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
                    if (tileString.substring(1, 2).equals("" + heroNumber)) {
                        result[x][y] = TileType.BLOCKED;
                    } else {
                        result[x][y] = TileType.MINE;
                        GameState.Position minepos = new GameState.Position(x, y);
                        mines.add(minepos);
                    }
                } else if (tileString.substring(0,1).equals("@")) {
                    if (tileString.substring(1,2).equals("1")) {
                        result[x][y] = TileType.HERO1;
                    } else if (tileString.substring(1,2).equals("2")) {
                        result[x][y] = TileType.HERO2;
                    } else if (tileString.substring(1,2).equals("3")) {
                        result[x][y] = TileType.HERO3;
                    } else if (tileString.substring(1,2).equals("4")) {
                        result[x][y] = TileType.HERO4;
                    } else {
                        throw new IllegalStateException("Hero unknown: " + tileString.substring(1,2));
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
            int mineDist = distanceBetweenPositions(minePos, heropos);
            if (mineDist<curClosestMineDist) {
                closestMinePos = minePos;
                curClosestMineDist = mineDist;
            };
        }

        for(GameState.Position tavernPos : taverns) {
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

    public TileType getTileFromDirection(GameState.Position heroPosition, DirectionType dir) {
        switch (dir) {
            case NORTH:
                if (heroPosition.getY() < 1) { return TileType.BLOCKED; }
                return currentMap[heroPosition.getX()    ][heroPosition.getY() - 1];
            case EAST:
                if (heroPosition.getX() >= currentMap.length - 1) { return TileType.BLOCKED; }
                return currentMap[heroPosition.getX() + 1][heroPosition.getY()    ];
            case SOUTH:
                if (heroPosition.getY() >= currentMap[0].length - 1) { return TileType.BLOCKED; }
                return currentMap[heroPosition.getX()    ][heroPosition.getY() + 1];
            case WEST:
                if (heroPosition.getX() < 1) { return TileType.BLOCKED; }
                return currentMap[heroPosition.getX() - 1][heroPosition.getY()    ];
            default:
                throw new RuntimeException("received a non supported direction.");
        }
    }

    public GameState.Position getPositionFromDirection(GameState.Position currentPosition, DirectionType dir){
        switch (dir) {
            case NORTH:
                if (currentPosition.getY() < 1) { throw new RuntimeException("out of map."); }
                return new GameState.Position(currentPosition.getX()   ,currentPosition.getY() - 1);
            case EAST:
                if (currentPosition.getX() >= currentMap.length - 1) { throw new RuntimeException("out of map."); }
                return new GameState.Position(currentPosition.getX() + 1,currentPosition.getY()   );
            case SOUTH:
                if (currentPosition.getY() >= currentMap[0].length - 1) { throw new RuntimeException("out of map."); }
                return new GameState.Position(currentPosition.getX()    ,currentPosition.getY() + 1);
            case WEST:
                if (currentPosition.getX() < 1) { throw new RuntimeException("out of map."); }
                return new GameState.Position(currentPosition.getX() - 1,currentPosition.getY()    );
            default:
                throw new RuntimeException("received a non supported direction.");
        }
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

    public List<GameState.Position> getMines() { return mines; }

    public List<GameState.Position> getTaverns() { return taverns; }
}

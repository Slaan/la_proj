package bot.bender;

import bot.dto.GameState;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Daniel Hofmeister on 18.10.2015.
 */
public class GameMap {

    private TileType[][] currentMap;
    private DirectionType nearestMineDirection;
    private DirectionType nearestTavernDirection;
    //private GameState.Position heroPosition;

    private List<GameState.Position> mines;
    private List<GameState.Position> taverns;
    private List<GameState.Position> heroes;

    private int totalMines;

    /** IMPORTANT: Map is updated to include a ring of "blocked" tiles. That means that all GameState Positions
     * are off by 1 on both x- and y-axis. All attributes of this class are adjusted to the new Positions.
     *
     * @param gameState
     */
    public GameMap(GameState gameState) {
        currentMap = parseMap(gameState);
/*        heroPosition = new GameState.Position(gameState.getHero().getPos().getX()+1,
                gameState.getHero().getPos().getY()+1);*/
        calculateNearestMineAndTavern(new GameState.Position(gameState.getHero().getPos().getX()+1,
                gameState.getHero().getPos().getY()+1));
    }

    private TileType[][] parseMap(GameState gameState) {

        int size = gameState.getGame().getBoard().getSize();
        String tiles = gameState.getGame().getBoard().getTiles();
        TileType[][] result = new TileType[size+2][size+2];
        int heroNumber = gameState.getHero().getId();
        int positionInTiles = 0;
        taverns = new ArrayList<>();
        mines = new ArrayList<>();
        heroes = new ArrayList<>();
        totalMines = 0;

        for(int z=0; z<size+2; z++) {
            result[0][z] = TileType.BLOCKED;
            result[z][0] = TileType.BLOCKED;
            result[size+1][z] = TileType.BLOCKED;
            result[z][size+1] = TileType.BLOCKED;
        }

        for(int y=1; y<size+1; y++) {
            // y-axis
            for(int x=1; x<size+1; x++) {
                //x-axis
                String tileString = tiles.substring(positionInTiles, positionInTiles+2);
                if(tileString.equals("  ")) {
                    result[x][y] = TileType.FREE;
                } else if(tileString.equals("[]")) {
                    result[x][y] = TileType.TAVERN;
                    GameState.Position tavernpos = new GameState.Position(x,y);
                    taverns.add(tavernpos);
                } else if (tileString.substring(0,1).equals("$")) {
                    totalMines++;
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

    /** Requires new Map Coordinates from hero.
     */
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
            }
        }

        for(GameState.Position tavernPos : taverns) {
            int tavernDist = distanceBetweenPositions(tavernPos, heropos);
            if (tavernDist<curClosestTavernDist) {
                closestTavernPos = tavernPos;
                curClosestTavernDist = tavernDist;
            }
        }

        nearestTavernDirection = calcDirection(heropos, closestTavernPos);
        if (closestMinePos == null) {
            // if we own all mines nearestMine shows to nearestTavern.
            nearestMineDirection = nearestTavernDirection;
        } else {
            nearestMineDirection = calcDirection(heropos, closestMinePos);
        }
    }

    /** Requires new Map Coordinates
     *
     * @param heroPos actual hero Position with new Map Coordinates
     * @param objPos target Position with new Map coordinates
     * @return
     */
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

    private int distanceBetweenPositions(GameState.Position pos1, GameState.Position pos2) {
        return (Math.abs(pos1.getX()-pos2.getX()) + Math.abs(pos1.getY()-pos2.getY()));
    }

    /** Requires new MapCoordinates
     *
     * @param heroPosition actual HeroPosition in Map
     * @param dir
     * @return
     */
    public TileType getTileFromDirection(GameState.Position heroPosition, DirectionType dir) {
        try {
            switch (dir) {
                case NORTH:
                    return currentMap[heroPosition.getX()   ][heroPosition.getY()-1 ];
                case EAST:
                    return currentMap[heroPosition.getX()+1 ][heroPosition.getY()   ];
                case SOUTH:
                    return currentMap[heroPosition.getX()   ][heroPosition.getY()+1 ];
                case WEST:
                    return currentMap[heroPosition.getX()-1 ][heroPosition.getY()   ];
                default:
                    throw new RuntimeException("received a non supported direction.");
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            return TileType.BLOCKED;
        }
    }

    /** returns TileType at Position position
     *
     * @param position Actual Position in Map
     * @return
     */
    public TileType getTile(GameState.Position position) {
        return currentMap[position.getX()][position.getY()];
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

    public int getTotalMineCount() { return totalMines; }
/*    public GameState.Position getHeroPosition() {
        return heroPosition;
    }*/

}

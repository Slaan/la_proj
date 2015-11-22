package algorithms.dijkstra;


import bot.bender.DirectionType;
import bot.bender.GameMap;
import bot.bender.TileType;
import bot.bender1.SimpleHero;
import bot.bender1.SimpleMine;
import bot.bender1.SimpleTavern;
import bot.Config;
import bot.dto.GameState;
import com.google.api.client.util.ArrayMap;

import java.util.*;

/**
 * Created by beckf on 04.11.2015.
 */
public class Dijkstra {

    private Map<GameState.Position, GameState.Position> previousFrom;
    private Map<GameState.Position, Integer> costFrom;
    private Map<GameState.Position, Boolean> visited;
    private Map<GameState.Position, DirectionType> direction;
    private List<GameState.Position> mines;
    private List<GameState.Position> tavern;
    private List<GameState.Position> heroes;
    private GameMap gameMap;
    private GameState.Position playerPosition;

    public Dijkstra(GameMap gameMap, GameState.Position playerPosition){
        previousFrom = new HashMap<>();
        costFrom = new HashMap<>();
        visited = new HashMap<>();
        direction = new ArrayMap<>();
        mines = new ArrayList<>();
        tavern = new ArrayList<>();
        heroes = new ArrayList<>();
        this.gameMap = gameMap;
        this.playerPosition = playerPosition;
    }

    public void runDijkstra() {
        long start = System.currentTimeMillis();
        _runDijkstra();
        long ende = System.currentTimeMillis();
        if (ende - start > 700) {
            throw new RuntimeException(String.format("runDijkstra() brauchte zu lange (%d ms).", (ende - start)));
        }
    }

    private void _runDijkstra(){
        Queue<GameState.Position> queue = new LinkedList<>();

        previousFrom.put(playerPosition, playerPosition);
        costFrom.put(playerPosition, 0);
        queue.add(playerPosition);

        for(int steps = 0; (steps < Config.getStepsToLook()) && ((mines.size() < Config.getNumberOfMinesToLook()) || (tavern.size() < Config.getNumberOfTavernsToLook()) || (heroes.size() < Config.getNumberOfHerosToLook())); steps++){
            Queue<GameState.Position> newQueue = new LinkedList<>();
            while((queue.size() > 0) && ((mines.size() < Config.getNumberOfMinesToLook()) || (tavern.size() < Config.getNumberOfTavernsToLook()) || (heroes.size() < Config.getNumberOfHerosToLook()))) {
                GameState.Position currentPosition = queue.remove();
                visited.put(currentPosition, true);
                for (GameState.Position position : neighborOf(currentPosition)) {
                    newQueue.add(position);
                    Integer newValue = costFrom.get(currentPosition) + 1;
                    Integer oldValue = Integer.MAX_VALUE;
                    if (costFrom.containsKey(position)) {
                        oldValue = costFrom.get(position);
                    }
                    if (newValue < oldValue) {
                        costFrom.put(position, newValue);
                        previousFrom.put(position, currentPosition);
                        addPositionToDirection(currentPosition, position);
                    }
                }
            }
            queue = newQueue;
        }
    }

    /**
     * Checks which Positions in the Directions of the currently processed Direction are Free.
     *
     * @param currentPosition the Position that is processed right now
     * @return the Positions are Free and the neighbors of the currentPosition
     */
    private List<GameState.Position> neighborOf(GameState.Position currentPosition){
        List<GameState.Position> positions = new ArrayList<>();
        for(DirectionType directionType : DirectionType.values()){
            if(isWalkableTile(currentPosition, directionType)) {
                GameState.Position neighborPosition = gameMap
                    .getPositionFromDirection(currentPosition, directionType);
                if(!visited.containsKey(neighborPosition)){
                    positions.add(neighborPosition);
                }
            }
        }
        return positions;
    }

    /**
     * Checks the TileType of the Position in the given Direction.
     * If it is not walkable it will be cheked if is anything else then BLOCKED.
     * Then it will be looked if the Position is not already visited.
     * If not the Position will be added to the appropriate List add it is added to all Maps.
     *
     * @param currentPosition the Position that is processed right now
     * @param directionType in which Direction should be checked
     * @return fals if not a walkable Tile
     */
    private boolean isWalkableTile(GameState.Position currentPosition , DirectionType directionType){
        TileType tileType = gameMap.getTileFromDirection(currentPosition, directionType);
        if(tileType.equals(TileType.BLOCKED)){
            return false;
        }
        if(tileType.equals(TileType.MINE)){
            GameState.Position position = gameMap
                .getPositionFromDirection(currentPosition, directionType);
            if(!visited.containsKey(position)) {
                mines.add(position);
                addPostionToMaps(currentPosition, position);
            }
            return false;
        }
        if(tileType.equals(TileType.TAVERN)) {
            GameState.Position position = gameMap
                .getPositionFromDirection(currentPosition, directionType);
            if(!visited.containsKey(position)) {
                tavern.add(position);
                addPostionToMaps(currentPosition, position);
            }
            return false;
        }
        if (tileType.isHero()) {
            GameState.Position position = gameMap
                .getPositionFromDirection(currentPosition, directionType);
            if(!visited.containsKey(position)) {
                heroes.add(position);
                addPostionToMaps(currentPosition, position);
            }
            return false;
        }
        return true;
    }

    /**
     * Puts the Position and the Direction for the shortest way to the Position as an Entry in the direction Map.
     * If the currentPosition is the same as the Player Position the Direction will be calculated.
     * If not the Direction will be taken from the currentPosition.
     *
     * @param currentPosition the Position that is processed right now
     * @param position the key to put
     */
    private void addPositionToDirection(GameState.Position currentPosition, GameState.Position position){
        if(currentPosition.equals(playerPosition)){
            direction.put(position, gameMap.calcDirection(playerPosition, position));
        } else {
            direction.put(position, direction.get(currentPosition));
        }
    }

    /**
     * Puts the Position in all the needed Maps as Key.
     * The Values are calculated out of the currentPosition.
     *
     * @param currentPosition the Position that is processed right now
     * @param position the key to put
     */
    private void addPostionToMaps(GameState.Position currentPosition, GameState.Position position){
        previousFrom.put(position, currentPosition);
        costFrom.put(position, costFrom.get(currentPosition) + 1);
        visited.put(position, true);
        addPositionToDirection(currentPosition,position);
    }

    /**
     * It will be looked in the direction Map with the position as Key.
     * Gives the Direction to go for the shortest Path to the given Position.
     *
     * @param position the Position to look up
     * @return the DirectionTyp dedicated to the Position
     */
    public DirectionType getDirectionToPosition(GameState.Position position) {
        return direction.get(position);
    }

    public SimpleMine getSimpleMine(GameState.Position minePosition){
        return new SimpleMine(getDirectionToPosition(minePosition), costFrom.get(minePosition));
    }

    /**
     * If Dijkstra found no Mine it will return a mook SimpleMine that says it is out of Sight.
     * If Dijkstra found Mines it will return the nearest Mine as Simple Mine.
     *
     * @return the nearestMine as SimpleMine
     */
    public SimpleMine getNearestMine(){
        if(mines.size() > 0){
            return getSimpleMine(mines.get(0));
        }
        return new SimpleMine();
    }

    public List<SimpleMine> getNeededSimpleMines () {
        List<SimpleMine> simpleMines = new ArrayList<>();
        for(int neededMines = 0; neededMines < Config.getNumberOfMinesToLook(); neededMines++){
            if(mines.size() > neededMines) {
                simpleMines.add(getSimpleMine(mines.get(neededMines)));
            } else {
                simpleMines.add(new SimpleMine());
            }
        }
        return simpleMines;
    }

    public List<SimpleMine> getSimpleMines () {
        List<SimpleMine> simpleMines = new ArrayList<>();
        for(GameState.Position position : mines){
            simpleMines.add(getSimpleMine(position));
        }
        return simpleMines;
    }

    public SimpleTavern getSimpleTavern(GameState.Position tavernPosition){
        return new SimpleTavern(getDirectionToPosition(tavernPosition), costFrom.get(tavernPosition));
    }

    public SimpleTavern getNearestTavern(){
        if(tavern.size() > 0){
            return getSimpleTavern(tavern.get(0));
        }
        return new SimpleTavern();
    }

    public List<SimpleTavern> getNeededSimpleTaverns () {
        List<SimpleTavern> simpleTaverns = new ArrayList<>();
        for(int neededTaverns = 0; neededTaverns < Config.getNumberOfTavernsToLook(); neededTaverns++){
            if(mines.size() > neededTaverns) {
                simpleTaverns.add(getSimpleTavern(tavern.get(neededTaverns)));
            } else {
                simpleTaverns.add(new SimpleTavern());
            }
        }
        return simpleTaverns;
    }

    public List<SimpleTavern> getSimpleTaverns () {
        List<SimpleTavern> simpleTaverns = new ArrayList<>();
        for(GameState.Position position : tavern){
            simpleTaverns.add(getSimpleTavern(position));
        }
        return simpleTaverns;
    }

    public SimpleHero getSimpleHero(GameState.Position heroPosition){
        DirectionType dir = getDirectionToPosition(heroPosition);
        return new SimpleHero(gameMap.getTile(heroPosition).getHero(), dir, costFrom.get(heroPosition));
    }

    public SimpleHero getNearesHero(){
        if(heroes.size() > 0){
            return getSimpleHero(heroes.get(0));
        }
        return new SimpleHero();
    }

    public List<SimpleHero> getNeededSimpleHeros () {
        List<SimpleHero> simpleHeros = new ArrayList<>();
        for(int neededHeroes = 0; neededHeroes < Config.getNumberOfHerosToLook(); neededHeroes++){
            if(heroes.size() > neededHeroes) {
                simpleHeros.add(getSimpleHero(heroes.get(neededHeroes)));
            } else {
                simpleHeros.add(new SimpleHero());
            }
        }
        return simpleHeros;
    }

    public List<SimpleHero> getSimpleHeros(){
        List<SimpleHero> simpleHeros = new ArrayList<>();
        for(GameState.Position position : heroes){
            simpleHeros.add(getSimpleHero(position));
        }
        return simpleHeros;
    }


}

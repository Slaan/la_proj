package algorithms.dijkstra;


import bot.Bender.DirectionType;
import bot.Bender.GameMap;
import bot.Bender.TileType;
import bot.Bender1.SimpleHero;
import bot.Bender1.SimpleMine;
import bot.Bender1.SimpleTavern;
import bot.Config;
import bot.dto.GameState;

import java.util.*;

/**
 * Created by beckf on 04.11.2015.
 */
public class Dijkstra {

    private Map<GameState.Position, GameState.Position> previousFrom;
    private Map<GameState.Position, Integer> costFrom;
    private Map<GameState.Position, Boolean> visited;
    private List<GameState.Position> mines;
    private List<GameState.Position> tavern;
    private List<GameState.Position> heroes;
    private GameMap gameMap;
    private GameState.Position playerPosition;

    public Dijkstra(GameMap gameMap, GameState.Position playerPosition){
        previousFrom = new HashMap<>();
        costFrom = new HashMap<>();
        visited = new HashMap<>();
        mines = new ArrayList<>();
        tavern = new ArrayList<>();
        heroes = new ArrayList<>();
        this.gameMap = gameMap;
        this.playerPosition = playerPosition;
    }


    public void runDijkstra(){
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
                    }
                }
            }
            queue = newQueue;
        }
    }

    public List<GameState.Position> neighborOf(GameState.Position currentPosition){
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
                previousFrom.put(position, currentPosition);
                costFrom.put(position, costFrom.get(currentPosition) + 1);
                visited.put(position, true);
            }
            return false;
        }
        if(tileType.equals(TileType.TAVERN)) {
            GameState.Position position = gameMap
                .getPositionFromDirection(currentPosition, directionType);
            if(!visited.containsKey(position)) {
                tavern.add(position);
                previousFrom.put(position, currentPosition);
                costFrom.put(position, costFrom.get(currentPosition) + 1);
                visited.put(position, true);
            }
            return false;
        }
        if (tileType.isHero()) {
            GameState.Position position = gameMap
                .getPositionFromDirection(currentPosition, directionType);
            if(!visited.containsKey(position)) {
                heroes.add(position);
                previousFrom.put(position, currentPosition);
                costFrom.put(position, costFrom.get(currentPosition) + 1);
                visited.put(position, true);
            }
            return false;
        }
        return true;
    }

    public DirectionType getDirectionToPosition(GameState.Position position) {

        GameState.Position prev = position;
        while(!previousFrom.get(previousFrom.get(prev)).equals(previousFrom.get(prev))){
            prev = previousFrom.get(prev);
        }
        return gameMap.calcDirection(playerPosition, prev);
    }

    public SimpleMine getSimpleMine(GameState.Position minePosition){
        return new SimpleMine(getDirectionToPosition(minePosition), costFrom.get(minePosition));
    }

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

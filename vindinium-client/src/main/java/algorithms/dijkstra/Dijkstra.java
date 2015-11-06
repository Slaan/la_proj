package algorithms.dijkstra;


import bot.Bender.DirectionType;
import bot.Bender.TileType;
import bot.Bender1.SimpleMine;
import bot.Bender1.SimpleTavern;
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
    private bot.Bender.Map map;
    private GameState.Position playerPosition;

    public Dijkstra(bot.Bender.Map map, GameState.Position playerPosition){
        previousFrom = new HashMap<>();
        costFrom = new HashMap<>();
        visited = new HashMap<>();
        mines = new ArrayList<>();
        tavern = new ArrayList<>();
        this.map = map;
        this.playerPosition = playerPosition;
    }


    public void runDijkstra(){
        Queue<GameState.Position> queue = new LinkedList<>();

        previousFrom.put(playerPosition, playerPosition);
        costFrom.put(playerPosition, 0);
        queue.add(playerPosition);

        while(queue.size() > 0){
            GameState.Position currentPosition = queue.remove();
            visited.put(currentPosition, true);
            for(GameState.Position position : neighborOf(currentPosition)){
                queue.add(position);
                Integer newValue = costFrom.get(currentPosition) + 1;
                Integer oldValue = Integer.MAX_VALUE;
                if(costFrom.containsKey(position)){
                    oldValue = costFrom.get(position);
                }
                if(newValue < oldValue){
                    costFrom.put(position, newValue);
                    previousFrom.put(position, currentPosition);
                }
            }

        }
    }

    public List<GameState.Position> neighborOf(GameState.Position currentPosition){
        List<GameState.Position> positions = new ArrayList<>();
        for(DirectionType directionType : DirectionType.values()){
            if(isWalkableTile(currentPosition, directionType)) {
                GameState.Position neighborPosition = map.getPositionFromDirection(currentPosition, directionType);
                if(!visited.containsKey(neighborPosition)){
                    positions.add(neighborPosition);
                }
            }
        }
        return positions;
    }

    private boolean isWalkableTile(GameState.Position currentPosition , DirectionType directionType){
        TileType tileType = map.getTileFromDirection(currentPosition, directionType);
        if(tileType.equals(TileType.BLOCKED)){
            return false;
        }
        if(tileType.equals(TileType.MINE)){
            GameState.Position position = map.getPositionFromDirection(currentPosition, directionType);
            if(!visited.containsKey(position)) {
                mines.add(position);
                previousFrom.put(position, currentPosition);
                costFrom.put(position, costFrom.get(currentPosition) + 1);
                visited.put(position, true);
            }
            return false;
        }
        if(tileType.equals(TileType.TAVERN)) {
            GameState.Position position = map.getPositionFromDirection(currentPosition, directionType);
            if(!visited.containsKey(position)) {
                tavern.add(position);
                previousFrom.put(position, currentPosition);
                costFrom.put(position, costFrom.get(currentPosition) + 1);
                visited.put(position, true);
            }
            return false;
        }
        return true;
    }

    public DirectionType getDirectionToPosition(GameState.Position minePosition) {

        GameState.Position prev = minePosition;
        while(!previousFrom.get(previousFrom.get(prev)).equals(previousFrom.get(prev))){
            prev = previousFrom.get(prev);
        }
        return map.calcDirection(playerPosition, prev);
    }

    public SimpleMine getSimpleMine(GameState.Position minePosition){
        return new SimpleMine(getDirectionToPosition(minePosition), costFrom.get(minePosition));
    }

    public SimpleMine getNearestMine(){
        return getSimpleMine(mines.get(0));
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
        return getSimpleTavern(tavern.get(0));
    }

    public List<SimpleTavern> getSimpleTaverns () {
        List<SimpleTavern> simpleTaverns = new ArrayList<>();
        for(GameState.Position position : tavern){
            simpleTaverns.add(getSimpleTavern(position));
        }
        return simpleTaverns;
    }
}

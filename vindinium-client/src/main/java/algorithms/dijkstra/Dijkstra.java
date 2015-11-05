package algorithms.dijkstra;


import bot.Bender.DirectionType;
import bot.Bender.TileType;
import bot.dto.GameState;

import java.util.*;

/**
 * Created by beckf on 04.11.2015.
 */
public class Dijkstra {

    private Map<GameState.Position, GameState.Position> previousFrom;
    private Map<GameState.Position, Integer> costFrom;
    private Map<GameState.Position, Boolean> visited;
    List<GameState.Position> mines;
    List<GameState.Position> tavern;
    private bot.Bender.Map map;

    public Dijkstra(bot.Bender.Map map){
        previousFrom = new HashMap<>();
        costFrom = new HashMap<>();
        visited = new HashMap<>();
        mines = new ArrayList<>();
        tavern = new ArrayList<>();
        this.map = map;
    }


    public void runDijkstraFrom(GameState.Position playerPosition){


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
        for(int x = -1; x <= 1; x =  x + 2){
            if(isWalkableTile(currentPosition, x, 0)){
                GameState.Position neighborPosition = new GameState.Position(currentPosition.getX() + x, currentPosition.getY() + 0);
                if(!visited.containsKey(neighborPosition)){
                    positions.add(neighborPosition);
                }
            }
        }
            for(int y = -1; y <= 1; y = y + 2){
                if(isWalkableTile(currentPosition, 0, y)){
                    GameState.Position neighborPosition = new GameState.Position(currentPosition.getX() + 0, currentPosition.getY() + y);
                    if(!visited.containsKey(neighborPosition)){
                        positions.add(neighborPosition);
                    }
                }
            }


        return positions;
    }

    private boolean isWalkableTile(GameState.Position currentPosition ,int additionX, int additionY){
        int x = currentPosition.getX() + additionX;
        int y = currentPosition.getY() + additionY;
        if((0 > x) || ( x >= map.getCurrentMap().length)) return false;
        if((0 > y) || ( y >= map.getCurrentMap()[0].length)) return false;
        if(map.getCurrentMap()[x][y].equals(TileType.BLOCKED)){
            return false;
        }
        if(map.getCurrentMap()[x][y].equals(TileType.MINE)){
            GameState.Position position = new GameState.Position(x, y);
            if(!visited.containsKey(position)) {
                mines.add(position);
                previousFrom.put(position, currentPosition);
                costFrom.put(position, costFrom.get(currentPosition) + 1);
                visited.put(position, true);
            }
            return false;
        }
        if(map.getCurrentMap()[x][y].equals(TileType.TAVERN)){
            GameState.Position position = new GameState.Position(x, y);
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
}

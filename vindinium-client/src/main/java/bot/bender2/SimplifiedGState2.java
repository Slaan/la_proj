package bot.bender2;

import algorithms.IdShifter;
import algorithms.dijkstra.Dijkstra;
import bot.bender.*;
import bot.bender0.RewardConfig;
import bot.bender1.*;
import bot.dto.GameState;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by fabi on 09.11.15.
 */
public class SimplifiedGState2 extends SimplifiedGState1 {

    public SimplifiedGState2() {super();} // Trouper

    public Set<BotMove> getPossibleMoves() {
        Set<BotMove> possibleMoves = new HashSet<>();
        if(!getClosestHero().getDistance().equals(Distance.OUTOFSIGHT)){
            possibleMoves.add(getClosestHero().getDirection().toBotMove());
        }
        if(!getClosestMine().getDistance().equals(Distance.OUTOFSIGHT)){
            possibleMoves.add(getClosestMine().getDirection().toBotMove());
        }
        if(!getClosestTavern().getDistance().equals(Distance.OUTOFSIGHT)){
            possibleMoves.add(getClosestTavern().getDirection().toBotMove());
        }
        if(possibleMoves.isEmpty()){
            for(DirectionType directionType : DirectionType.values()){
                if(!getGameMap().getTileFromDirection(getCurrentPos(), directionType).equals(TileType.BLOCKED)){
                    possibleMoves.add(directionType.toBotMove());
                }
            }
        }
        return possibleMoves;

    }
}

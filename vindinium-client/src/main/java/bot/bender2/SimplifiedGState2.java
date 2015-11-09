package bot.bender2;

import bot.Bender.BotMove;
import bot.Bender1.Distance;
import bot.Bender1.SimplifiedGState1;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by fabi on 09.11.15.
 */
public class SimplifiedGState2 extends SimplifiedGState1{

    public SimplifiedGState2(){
        super();
    }

    @Override
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
        if(!possibleMoves.isEmpty()){
            return possibleMoves;
        } else {
            return super.getPossibleMoves();
        }
    }
}

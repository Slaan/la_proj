package bot.Bender1;

import bot.Bender.*;
import org.omg.SendingContext.RunTime;
import persistence.GameLog;

/**
 * Created by Daniel Hofmeister on 06.11.2015.
 */
public class RewarderBender1 implements IRewarder {


    private final GameLog gameLog;
    private SimplifiedGState1 formerState;
    private BotMove move;
    private SimplifiedGState1 currentState;
    private int reward;

    public RewarderBender1(GameLog gameLog){
        this.gameLog = gameLog;
    }

    public void setLastMove(BotMove move){
        this.move = move;
    }

    public int calculateReward(ISimplifiedGState state) {
        if (formerState == null) {
            formerState = (SimplifiedGState1)state;
            return 0;
        }

        reward = RewardConfigBender1.getTurnReward();
        currentState = (SimplifiedGState1)state;

        if (checkForDeath()) {
            gameLog.addDeath();
            calcDeathReward();
        }

        if (checkForTavern()) {
            gameLog.addTavern();
            calcTavernReward();
        }

        if (checkForMine()) {
            gameLog.addMine();
            calcMineReward();
        }

        if (checkForKill()) {
            gameLog.addKill();
            calcKillReward();
        }

        formerState = currentState;
        return reward;
    }

     private boolean checkForKill() {
         boolean result = false;
         if (attackedHero()) {
            if(!heroDead())
                result = true;
         }
         return result;
    }

    private boolean attackedHero() {
        boolean result = false;
        if (formerState.getClosestHero().getDistance().equals((Distance.BESIDE))
                && formerState.getCurrentPos().equals(currentState.getCurrentPos())) {
            if (move.equals(BotMove.EAST) && formerState.getClosestHero().getDirection().equals(DirectionType.EAST)) {
                result = true;
            } else if (move.equals(BotMove.SOUTH)
                    && formerState.getClosestHero().getDirection().equals(DirectionType.SOUTH)) {
                result = true;
            } else if (move.equals(BotMove.WEST)
                    && formerState.getClosestHero().getDirection().equals(DirectionType.WEST)) {
                result = true;
            } else if (move.equals(BotMove.NORTH)
                    && formerState.getClosestHero().getDirection().equals(DirectionType.NORTH)) {
                result = true;
            } else {throw new RuntimeException("Enemy hero was next to us but not in a Direction? " +
                    "#RewarderBender1.attackHero() ");
            }
        }
        return result;
    }

    private boolean heroDead() {
        boolean result = false;

        if (formerState.getMineCount()>currentState.getMineCount()) {
            result = true;
        }

        return result;
    }

    private void calcKillReward() {

    }

    public boolean checkForDeath() {

        return (formerState.getLifeHP()<30 && currentState.getLifeHP()>98 &&
                currentState.getCurrentPos().equals(currentState.getSpawn()));
    }

    private void calcDeathReward() {
        reward += RewardConfigBender1.getDeathDefaultReward() +
                (RewardConfigBender1.getDeathperMineReward() *(formerState.getMineCount() + 1)); }

    public boolean checkForTavern() {
        boolean result=false;
        int lifechange = currentState.getLifeHP()-formerState.getLifeHP();
        if (formerState.getCurrentPos().equals(currentState.getCurrentPos()) && tavernNear()
                && (0<lifechange) && (lifechange<=50)) {
            result = true;
        }
        return result;
    }

    private boolean tavernNear() {
        int x = formerState.getCurrentPos().getX();
        int y = formerState.getCurrentPos().getY();
        GameMap gameMap = formerState.getGameMap();
        if ((gameMap.getTileFromDirection(formerState.getCurrentPos(), DirectionType.EAST))== TileType.TAVERN ||
                (gameMap.getTileFromDirection(formerState.getCurrentPos(),DirectionType.NORTH))==TileType.TAVERN ||
                (gameMap.getTileFromDirection(formerState.getCurrentPos(),DirectionType.WEST))==TileType.TAVERN ||
                (gameMap.getTileFromDirection(formerState.getCurrentPos(),DirectionType.SOUTH))==TileType.TAVERN) {
            return true;
        }
        return false;
    }

    private void calcTavernReward() {

        if (formerState.getLife().equals(Quantity.FEW)) {
            reward+=RewardConfigBender1.getTavernRewardLowHP();
        } else if (formerState.getLife().equals(Quantity.MIDDLE)) {
            reward+=RewardConfigBender1.getTavernRewardMiddleHP();
        } else if (formerState.getLife().equals(Quantity.LOTS)) {
            reward+=RewardConfigBender1.getTavernRewardHighHP();
        } else {
            throw new IllegalStateException("former State get Life had no State");
        }
    }

    private boolean checkForMine() {
        return ((currentState.getMineCount())>(formerState.getMineCount()));
    }

    private void calcMineReward() {
        reward+=RewardConfigBender1.getGetMineReward();
    }

}

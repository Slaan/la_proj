package bot.Bender1;

import bot.Bender.DirectionType;
import bot.Bender.ISimplifiedGState;
import bot.Bender.Map;
import bot.Bender.TileType;
import bot.Bender0.SimplifiedGState;
import persistence.GameLog;

/**
 * Created by Daniel Hofmeister on 06.11.2015.
 */
public class RewarderBender1 {


    private final GameLog gameLog;
    private SimplifiedGState1 formerState;
    private SimplifiedGState1 currentState;
    private int reward;

    public RewarderBender1(GameLog gameLog){
        this.gameLog = gameLog;
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
        return false;
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
        Map map = formerState.getMap();
        if ((map.getTileFromDirection(formerState.getCurrentPos(), DirectionType.EAST))== TileType.TAVERN ||
                (map.getTileFromDirection(formerState.getCurrentPos(),DirectionType.NORTH))==TileType.TAVERN ||
                (map.getTileFromDirection(formerState.getCurrentPos(),DirectionType.WEST))==TileType.TAVERN ||
                (map.getTileFromDirection(formerState.getCurrentPos(),DirectionType.SOUTH))==TileType.TAVERN) {
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
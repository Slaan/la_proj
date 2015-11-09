package bot.Bender0;


import bot.Bender.*;
import persistence.GameLog;

/**
 * Created by octavian on 19.10.15.
 */
public class Rewarder implements IRewarder {

    private final GameLog gameLog;
    private SimplifiedGState formerState;
    private SimplifiedGState currentState;
    private final int TURNREWARD = -1;
    private final int DEATHREWARDPERMINE = -15;
    private final int TAVERNREWARDSTART = -30;
    private final int TAVERNHEALPERLIFE = 2;
    private final int MINEREWARD = 50;
    private final int BLOCKEDOBJREWARD = -10;
    private int reward=TURNREWARD;

    public Rewarder(GameLog gameLog){
        this.gameLog = gameLog;
    }

    public int calculateReward(ISimplifiedGState state) {
        if (formerState == null) {
            formerState = (SimplifiedGState)state;
            return 0;
        }

        reward = TURNREWARD;

        currentState = (SimplifiedGState)state;

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

        if (checkForBlockedMove()) {
            gameLog.addBlockedWay();
            calcBlockedReward();
        }

        formerState = currentState;
        return reward;
    }

    public boolean checkForDeath() {
        return (formerState.getLife()<40 && currentState.getLife()>98 &&
                currentState.getCurrentPos().equals(currentState.getSpawn()));
    }

    private void calcDeathReward() { reward += DEATHREWARDPERMINE*(formerState.getNoOfOurMines() + 1); }

    public boolean checkForTavern() {
        boolean result=false;
        int lifechange = currentState.getLife()-formerState.getLife();
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
        reward += TAVERNREWARDSTART+((currentState.getLife()-formerState.getLife())*TAVERNHEALPERLIFE);
    }

    private boolean checkForMine() {
        return ((currentState.getNoOfOurMines())>(formerState.getNoOfOurMines()));
    }

    private void calcMineReward() {
        reward+=MINEREWARD;
    }

    private void calcBlockedReward() {
        reward += BLOCKEDOBJREWARD;
    }

    public boolean checkForBlockedMove() {
        return ((!checkForTavern())&&(!checkForMine())&&(formerState.getCurrentPos().equals(currentState.getCurrentPos())));
    }
}

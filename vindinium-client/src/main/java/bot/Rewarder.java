package bot;


/**
 * Created by octavian on 19.10.15.
 */
public class Rewarder {

    private SimplifiedGState formerState;
    private SimplifiedGState currentState;
    private final int TURNREWARD = -1;
    private final int DEATHREWARDPERMINE = -15;
    private final int TAVERNREWARDSTART = -30;
    private final int TAVERNHEALPERLIFE = 2;
    private final int MINEREWARD = 50;
    private final int BLOCKEDOBJREWARD = -10;
    private int reward=TURNREWARD;

    public int calculateReward(SimplifiedGState state) {
        if (formerState == null) {
            formerState = state;
            return 0;
        }

        reward = TURNREWARD;

        currentState = state;

        if (checkForDeath()) {
            calcDeathReward();
        }

        if (checkForTavern()) {
            calcTavernReward();
        }

        if (checkForMine()) {
            calcMineReward();
        }

        if (checkForBlockedMove()) {
            calcBlockedReward();
        }

        formerState = state;
        return reward;
    }

    private boolean checkForDeath() {
        return (formerState.getLife()<40 && currentState.getLife()>98 &&
                currentState.getCurrentPos().equals(currentState.getSpawn()));
    }

    private void calcDeathReward() { reward += DEATHREWARDPERMINE*formerState.getNoOfOurMines(); }

    private boolean checkForTavern() {
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
        Map map = formerState.getMap();
        if ((map.getTileFromDirection(formerState.getCurrentPos(),DirectionType.EAST))==TileType.TAVERN ||
                (map.getTileFromDirection(formerState.getCurrentPos(),DirectionType.NORTH))==TileType.TAVERN ||
                (map.getTileFromDirection(formerState.getCurrentPos(),DirectionType.WEST))==TileType.TAVERN ||
                (map.getTileFromDirection(formerState.getCurrentPos(),DirectionType.SOUTH))==TileType.TAVERN) {
            return true;
        }
        return false;
    }

    private void calcTavernReward() {
        reward += TAVERNREWARDSTART+((formerState.getLife()-currentState.getLife())*TAVERNREWARDSTART);
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

    private boolean checkForBlockedMove() {
        return ((!checkForTavern())&&(!checkForMine())&&(formerState.getCurrentPos().equals(currentState.getCurrentPos())));
    }
}

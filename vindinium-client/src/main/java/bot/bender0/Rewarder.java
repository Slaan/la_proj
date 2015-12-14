package bot.bender0;


import bot.bender.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import persistence.GameLog;

/**
 * Created by octavian on 19.10.15.
 */
public class Rewarder implements IRewarder {

  private static final Logger logger = LogManager.getLogger(Rewarder.class);

  private final GameLog gameLog;
  private SimplifiedGState formerState;
  private SimplifiedGState currentState;
  private final int TURNREWARD = -1;
  private final int DEATHREWARDPERMINE = -15;
  private final int TAVERNREWARDSTART = -30;
  private final int TAVERNHEALPERLIFE = 2;
  private final int MINEREWARD = 50;
  private final int BLOCKEDOBJREWARD = -10;
  private int reward = TURNREWARD;

  public Rewarder(GameLog gameLog) {
    this.gameLog = gameLog;
  }

  public void setLastMove(BotMove move) {

  }

  public int calculateReward(ISimplifiedGState state) {
    if (formerState == null) {
      formerState = (SimplifiedGState) state;
      return 0;
    }

    reward = TURNREWARD;

    currentState = (SimplifiedGState) state;

    double deathReward = 0;
    if (checkForDeath()) {
      gameLog.addDeatbByMine();
      deathReward = calcDeathReward();
    }

    double tavernReward = 0;
    if (checkForTavern()) {
      gameLog.addTavern();
      tavernReward = calcTavernReward();
    }

    double mineReward = 0;
    if (checkForMine()) {
      gameLog.addMine();
      mineReward = calcMineReward();
    }

    double blockedReward = 0;
    if (checkForBlockedMove()) {
      gameLog.addBlockedWay();
      blockedReward = calcBlockedReward();
    }

    reward += deathReward + tavernReward + mineReward + blockedReward;

    gameLog.addReward(reward);
    if (reward>gameLog.getBigestReward()) {
      gameLog.setBigestReward(reward);
      gameLog.setBigestRewardRound(formerState.getGame().getGame().getTurn());
    }
    if (reward<gameLog.getSmalestReward()) {
      gameLog.setSmalestReward(reward);
      gameLog.setSmalestRewardRound(formerState.getGame().getGame().getTurn());
    }
    formerState = currentState;
    return reward;
  }

  public boolean checkForDeath() {
    return (formerState.getLife() < 40 && currentState.getLife() > 98 &&
        currentState.getCurrentPos().equals(currentState.getSpawn()));
  }

  private double calcDeathReward() {
    return DEATHREWARDPERMINE * (formerState.getNoOfOurMines() + 1);
  }

  public boolean checkForTavern() {
    boolean result = false;
    int lifechange = currentState.getLife() - formerState.getLife();
    if (formerState.getCurrentPos().equals(currentState.getCurrentPos()) && tavernNear() && (0
        < lifechange) && (lifechange <= 50)) {
      result = true;
    }
    return result;
  }

  private boolean tavernNear() {
    int x = formerState.getCurrentPos().getX();
    int y = formerState.getCurrentPos().getY();
    GameMap gameMap = formerState.getGameMap();
    if ((gameMap.getTileFromDirection(formerState.getCurrentPos(), DirectionType.EAST))
        == TileType.TAVERN ||
        (gameMap.getTileFromDirection(formerState.getCurrentPos(), DirectionType.NORTH))
            == TileType.TAVERN ||
        (gameMap.getTileFromDirection(formerState.getCurrentPos(), DirectionType.WEST))
            == TileType.TAVERN ||
        (gameMap.getTileFromDirection(formerState.getCurrentPos(), DirectionType.SOUTH))
            == TileType.TAVERN) {
      return true;
    }
    return false;
  }

  private double calcTavernReward() {
    return TAVERNREWARDSTART +
        ((currentState.getLife() - formerState.getLife()) * TAVERNHEALPERLIFE);
  }

  private boolean checkForMine() {
    return ((currentState.getNoOfOurMines()) > (formerState.getNoOfOurMines()));
  }

  private double calcMineReward() {
    return MINEREWARD;
  }

  private double calcBlockedReward() {
    return BLOCKEDOBJREWARD;
  }

  public boolean checkForBlockedMove() {
    return ((!checkForTavern()) && (!checkForMine()) && (formerState.getCurrentPos()
        .equals(currentState.getCurrentPos())));
  }
}

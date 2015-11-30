package bot.bender1;

import bot.bender.*;
import bot.dto.GameState;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import persistence.GameLog;
import util.BotmoveDirectionConverter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Daniel Hofmeister on 06.11.2015.
 */
public class RewarderBender1 implements IRewarder {
    private static final Logger logger = LogManager.getLogger(RewarderBender1.class);

    private final GameLog gameLog;
    private SimplifiedGState1 formerState;
    private BotMove move;
    private SimplifiedGState1 currentState;
    private int reward;
    private List<GameState.Hero> killedHeroes=new ArrayList<>();

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

        double deathReward = 0;
        if (checkForDeath()) {
            gameLog.addDeath();
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

        double killReward = 0;
        if (checkForKill()) {
            gameLog.addKill();
            killReward = calcKillReward();
        }
        formerState = currentState;
        reward += deathReward + tavernReward + mineReward + killReward;

        if (reward > 250) {
            logger.warn(String.format("Reward: %d. <death: %d, tavern: %d, mine: %d, kill: %d>",
                reward,
                (int)deathReward,
                (int)tavernReward,
                (int)mineReward,
                (int)killReward));
        }

        return reward;
    }

    /** Checks if we killed a Hero last turn
     *
     * @return true for kill, false for no kill
     */
    private boolean checkForKill() {
        boolean result = false;

        // endPos = Position at the end of last turn
        GameState.Position endPos;
        DirectionType dir = BotmoveDirectionConverter.getDirectionTypeFromBotMove(move);
        if (!formerState.getGameMap().getTileFromDirection(formerState.getCurrentPos(),dir).equals(TileType.FREE)) {
            endPos = formerState.getCurrentPos();
        } else if (move.equals(BotMove.EAST)) {
            endPos = new GameState.Position(formerState.getCurrentPos().getX()+1,formerState.getCurrentPos().getY());
        } else if (move.equals(BotMove.SOUTH)) {
            endPos = new GameState.Position(formerState.getCurrentPos().getX(),formerState.getCurrentPos().getY()+1);
        } else if (move.equals(BotMove.WEST)) {
            endPos = new GameState.Position(formerState.getCurrentPos().getX()-1,formerState.getCurrentPos().getY());
        } else if (move.equals(BotMove.NORTH)) {
            endPos = new GameState.Position(formerState.getCurrentPos().getX(),formerState.getCurrentPos().getY()-1);
        } else {
            throw new IllegalArgumentException("Bot moved in unknown direction");
        }

        List<GameState.Hero> nearHeroes = getAllHeroesNextToUs(endPos);

        // A Hero is killed when we are next to him at the end of our turn and his Life is below 20
        killedHeroes = new ArrayList<>();
        for(GameState.Hero h : nearHeroes) {
            if (h.getLife()<=20) {
                killedHeroes.add(h);
                result = true;
                 //System.out.println("Killed Hero ID: " + h.getId() + " URL: " + formerState.getGame().getViewUrl() +
                 //        " Turn: " + formerState.getGame().getGame().getTurn());
            }
        }
        return result;
    }

    private double calcKillReward() {
        double result = 0;
        for (GameState.Hero h : killedHeroes) {
            result+=RewardConfigBender1.getKillDefault();
            for(int i=0; i<h.getMineCount(); i++) {
                result += RewardConfigBender1.getKillPerMine() *
                        Math.pow(RewardConfigBender1.getKillPerMineDiscount(),i);
            }
        }
        logger.debug("Kill! gameURL: " + formerState.getGame().getViewUrl() + "\n" +
                "Reward: " + reward + ", " +
                "Turn: " + formerState.getGame().getGame().getTurn());
        return result;
    }


    private List<GameState.Hero> getAllHeroesNextToUs(GameState.Position position) {
        List<GameState.Hero> result = new ArrayList<>();
        GameMap map = formerState.getGameMap();
        TileType tile = map.getTileFromDirection(position, DirectionType.EAST);
        if (isEnemyOnTile(tile)) {
            result.add(getActualHeroFromTile(tile));
        }
        tile = map.getTileFromDirection(position, DirectionType.SOUTH);
        if (isEnemyOnTile(tile)) {
            result.add(getActualHeroFromTile(tile));
        }
        tile = map.getTileFromDirection(position, DirectionType.WEST);
        if (isEnemyOnTile(tile)) {
            result.add(getActualHeroFromTile(tile));
        }
        tile = map.getTileFromDirection(position, DirectionType.NORTH);
        if (isEnemyOnTile(tile)) {
            result.add(getActualHeroFromTile(tile));
        }
        return result;
    }

    private boolean isEnemyOnTile(TileType tile) {
        int ourId = currentState.getGame().getHero().getId();
        boolean result = false;
        if (tile.isHero() && getActualHeroFromTile(tile).getId() != ourId) {
            result = true;
        }
        return result;
    }

    private GameState.Hero getActualHeroFromTile(TileType tile) {
        GameState.Hero result = null;
        if (tile.equals(TileType.HERO1)) {
            result = formerState.getGame().getGame().getHeroes().get(0);
        } else if (tile.equals(TileType.HERO2)) {
            result = formerState.getGame().getGame().getHeroes().get(1);
        } else if (tile.equals(TileType.HERO3)) {
            result = formerState.getGame().getGame().getHeroes().get(2);
        } else if (tile.equals(TileType.HERO4)) {
            result = formerState.getGame().getGame().getHeroes().get(3);
        } else {
            new RuntimeException("Hero is supposed to be on this tile but cant be found" +
                    "#RewarderBender1.getActualHeroFromTile TileType: " + tile);
        }
        return result;
    }

    public boolean checkForDeath() {

        return (formerState.getLifeHP()<30 && currentState.getLifeHP()>98 &&
                currentState.getCurrentPos().equals(currentState.getSpawn()));
    }

    private double calcDeathReward() {
        return (RewardConfigBender1.getDeathDefaultReward() +
                (RewardConfigBender1.getDeathperMineReward() *(formerState.getMineCount() + 1)));
    }

    public boolean checkForTavern() {
        boolean result=false;
        int lifechange = currentState.getLifeHP()-formerState.getLifeHP();
        if (formerState.getCurrentPos().equals(currentState.getCurrentPos()) && tavernNear()
                && (0<=lifechange) && (lifechange<=50)) {
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

    private double calcTavernReward() {

        if (formerState.getLife().equals(Quantity.FEW)) {
            return RewardConfigBender1.getTavernRewardLowHP();
        } else if (formerState.getLife().equals(Quantity.MIDDLE)) {
            return RewardConfigBender1.getTavernRewardMiddleHP();
        } else if (formerState.getLife().equals(Quantity.LOTS)) {
            return RewardConfigBender1.getTavernRewardHighHP();
        } else {
            throw new IllegalStateException("former State get Life had no State");
        }
    }

    /** Checks if we got a mine last turn
     *
     * @return
     */
    private boolean checkForMine() {
        boolean result = false;
        if (grabMine() && (formerState.getMineCount()<currentState.getMineCount())) {
            result = true;
        }
        return result;
    }

    /** Checks if we tried to get a mine last turn
     *
     * @return
     */
    private boolean grabMine() {
        boolean result = false;
        // Enemy hero has to be next to us and we havent moved between rounds
        if (formerState.getClosestMine().getDistance().equals((Distance.BESIDE))
                && formerState.getCurrentPos().equals(currentState.getCurrentPos())) {
            // our last action has to be in the direction of the enemy hero
            if (move.equals(BotMove.EAST) && formerState.getClosestMine().getDirection().equals(DirectionType.EAST)) {
                result = true;
            } else if (move.equals(BotMove.SOUTH)
                    && formerState.getClosestMine().getDirection().equals(DirectionType.SOUTH)) {
                result = true;
            } else if (move.equals(BotMove.WEST)
                    && formerState.getClosestMine().getDirection().equals(DirectionType.WEST)) {
                result = true;
            } else if (move.equals(BotMove.NORTH)
                    && formerState.getClosestMine().getDirection().equals(DirectionType.NORTH)) {
                result = true;
            }
        }
        return result;
    }

    private double calcMineReward() {
        return RewardConfigBender1.getGetMineReward();
    }

}

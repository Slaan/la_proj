package bot.bender2;

import bot.Bender.*;
import bot.Bender1.Quantity;
import bot.Bender1.RewardConfigBender1;
import bot.dto.GameState;
import persistence.GameLog;
import util.BotmoveDirectionConverter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by beckf on 09.11.2015.
 */
public class RewarderBender2 implements IRewarder{

    private final GameLog gameLog;
    private SimplifiedGState2 formerState;
    private BotMove move;
    private SimplifiedGState2 currentState;
    private int reward;
    private List<GameState.Hero> killedHeroes=new ArrayList<>();

    public RewarderBender2(GameLog gameLog){
        this.gameLog = gameLog;
    }

    public void setLastMove(BotMove move){
        this.move = move;
    }

    public int calculateReward(ISimplifiedGState state) {
        if (formerState == null) {
            formerState = (SimplifiedGState2)state;
            return 0;
        }

        reward = RewardConfigBender1.getTurnReward();
        currentState = (SimplifiedGState2)state;

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

    /** Checks if we killed a Hero last turn
     *
     * @return true for kill, false for no kill
     */
    private boolean checkForKill() {
        boolean result = false;

        // endPos = Position at the end of last turn
        GameState.Position endPos;
        DirectionType dir = BotmoveDirectionConverter.getDirectionTypeFromBotMove(move);
        if (formerState.getGameMap().getTileFromDirection(formerState.getCurrentPos(),dir).equals(TileType.BLOCKED)) {
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

    private void calcKillReward() {
        for (GameState.Hero h : killedHeroes) {
            reward+=RewardConfigBender1.getKillDefault();
            for(int i=0; i<h.getMineCount(); i++) {
                reward += RewardConfigBender1.getKillPerMine() *
                        Math.pow(RewardConfigBender1.getKillPerMineDiscount(),i);
            }
        }
        System.out.println("Kill! gameURL: " + formerState.getGame().getViewUrl() + "\n" +
                "Reward: " + reward + ", " +
                "Turn: " + formerState.getGame().getGame().getTurn());
    }


    private List<GameState.Hero> getAllHeroesNextToUs(GameState.Position position) {
        List<GameState.Hero> result = new ArrayList<>();
        GameMap map = formerState.getGameMap();
        TileType tile = map.getTileFromDirection(position, DirectionType.EAST);
        if (isHeroOnTile(tile)) {
            result.add(getActualHeroFromTile(tile));
        }
        tile = map.getTileFromDirection(position, DirectionType.SOUTH);
        if (isHeroOnTile(tile)) {
            result.add(getActualHeroFromTile(tile));
        }
        tile = map.getTileFromDirection(position, DirectionType.WEST);
        if (isHeroOnTile(tile)) {
            result.add(getActualHeroFromTile(tile));
        }
        tile = map.getTileFromDirection(position, DirectionType.NORTH);
        if (isHeroOnTile(tile)) {
            result.add(getActualHeroFromTile(tile));
        }
        return result;
    }

    private boolean isHeroOnTile(TileType tile) {
        boolean result = false;
        //   if (tile.equals(TileType.HERO1)) {
        //       result = true;
        //   } else
        if (tile.equals(TileType.HERO2)) {
            result = true;
        } else if (tile.equals(TileType.HERO3)) {
            result = true;
        } else if (tile.equals(TileType.HERO4)) {
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

    private void calcDeathReward() {
        reward += RewardConfigBender1.getDeathDefaultReward() +
                (RewardConfigBender1.getDeathperMineReward() *(formerState.getMineCount() + 1));
        System.out.println("Death! gameURL: " + formerState.getGame().getViewUrl() + "\n" +
                "Reward: " + reward + ", " +
                "Turn: " + formerState.getGame().getGame().getTurn());
    }

    public boolean checkForTavern() {
        boolean result=false;
        int lifechange = currentState.getLifeHP()-formerState.getLifeHP();
        if (formerState.getCurrentPos().equals(currentState.getCurrentPos()) && tavernNear()
             //   && (0<lifechange) && (lifechange<=50)) {
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

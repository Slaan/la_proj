package bot.Bender1;

import bot.Bender.*;
import bot.dto.GameState;
import persistence.GameLog;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Daniel Hofmeister on 06.11.2015.
 */
public class RewarderBender1 implements IRewarder {


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
     * @return
     */
    //TODO: if we stand still the botmove is still "executed" in the mind of the Rewarder, this has to be fixec
    private boolean checkForKill() {
        boolean result = false;

        // endPos = Position at the end of last turn
        GameState.Position endPos=null;
        // our last action has to be in the direction of the enemy hero
        if (move.equals(BotMove.EAST)) {
            endPos = new GameState.Position(formerState.getCurrentPos().getX()+1,formerState.getCurrentPos().getY());
        } else if (move.equals(BotMove.SOUTH)) {
            endPos = new GameState.Position(formerState.getCurrentPos().getX(),formerState.getCurrentPos().getY()+1);
        } else if (move.equals(BotMove.WEST)) {
            endPos = new GameState.Position(formerState.getCurrentPos().getX()-1,formerState.getCurrentPos().getY());
        } else if (move.equals(BotMove.NORTH)) {
            endPos = new GameState.Position(formerState.getCurrentPos().getX()+1,formerState.getCurrentPos().getY()-1);
        }

        List<GameState.Hero> nearHeroes = getAllHeroesNextToUs(endPos);

        // A Hero is killed when we are next to him at the end of our turn and his Life are below 20
        for(GameState.Hero h : nearHeroes) {
            if (h.getLife()<=20) {
                killedHeroes.add(h);
                result = true;
               // System.out.println("Killed Hero ID: " + h.getId() + " URL: " + formerState.getGame().getViewUrl() +
               //         " Turn: " + formerState.getGame().getGame().getTurn());
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
       // System.out.println("Got a reward <3");
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

    private void calcMineReward() {
        reward+=RewardConfigBender1.getGetMineReward();
    }

}

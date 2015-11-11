package bot.Bender1;

import bot.Bender.BotMove;
import bot.Bender.DirectionType;
import bot.Bender0.RewardConfig;
import bot.dto.GameState;

/**
 * Created by slaan on 02.11.15.
 */
public class SimpleHero {

    private int heroID;
    private Quantity lifeDifference;
    private Distance distance;
    private Quantity enemyMines;
    private DirectionType direction;

    /**
     * Create a Hero that is out of sight.
     */
    public SimpleHero() {
        heroID = -1;
        lifeDifference = Quantity.LOTS;
        distance = Distance.OUTOFSIGHT;
        enemyMines = Quantity.FEW;
        direction = DirectionType.NORTH;
    }

    public SimpleHero(int heroID, DirectionType direction, int distance) {
        this.heroID = heroID;
        this.distance = SimplifiedGState1.calcDistance(distance);
        this.direction = direction;
    }

    public SimpleHero init(GameState gameState) {
        for (GameState.Hero enemy : gameState.getGame().getHeroes()) {
            if (enemy.getId() == getHeroID()) {
                this.lifeDifference = calcLifeDif(gameState.getHero().getLife(), enemy.getLife());
                this.enemyMines = calcMineDif(gameState.getHero().getMineCount(), enemy.getMineCount());
                break;
            }
        }
        return this;
    }

    public int getHeroID() {
        return heroID;
    }

    public Quantity getLifeDifference() {
        return lifeDifference;
    }

    public Distance getDistance() {
        return distance;
    }

    public Quantity getEnemyMines() {
        return enemyMines;
    }

    public DirectionType getDirection() {
        return direction;
    }

    /**
     *
     * @param benderLife
     * @param enemyLife
     * @return Quantity.LOTS = don't attack, .MIDDLE = maybe attack, .FEW = good to attak
     *
     */
    private Quantity calcLifeDif(int benderLife, int enemyLife) {
        if (benderLife<(enemyLife + RewardConfig.getEnemyLifeMore())) {
            return Quantity.LOTS;
        } else if (benderLife<(enemyLife + RewardConfig.getEnemyLifeMore())) {
            return Quantity.MIDDLE;
        } else {
            return Quantity.FEW;
        }
    }

    /**
     * Maps the absolute distance to a relative one configured by the RewardConfig.
     */
    private Quantity calcDistance(int distance) {
        if (distance < RewardConfig.getDistantClose())
            return Quantity.FEW;
        else if (distance > RewardConfig.getDistantFar())
            return  Quantity.LOTS;
        return Quantity.MIDDLE;
    }

    private Quantity calcMineDif(int benderMineCount, int enemyMineCount) {
        int diff = enemyMineCount - benderMineCount;
        if (diff < RewardConfig.getLowerMineBoundryTotal())
            return Quantity.FEW;
        else if (diff > RewardConfig.getUpperMineBoundryTotal())
            return Quantity.LOTS;
        return Quantity.MIDDLE;
    }
}

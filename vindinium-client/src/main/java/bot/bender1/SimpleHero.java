package bot.bender1;

import bot.bender.DirectionType;
import bot.bender0.RewardConfig;
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
                this.lifeDifference = calcLife(enemy.getLife());
                this.enemyMines = calcMine(enemy.getMineCount());
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
     *
     * @param enemyLife
     * @return Quantity.LOTS = don't attack, .MIDDLE = maybe attack, .FEW = good to attak
     */
    private Quantity calcLife(int enemyLife){
        if (enemyLife>=RewardConfig.getUpperLifeboundry()) {
            return Quantity.LOTS;
        } else if (enemyLife>=RewardConfig.getLowerLifeBoundry()){
            return Quantity.MIDDLE;
        } else {return Quantity.FEW; }
    }

    private Quantity calcMineDif(int benderMineCount, int enemyMineCount) {
        int diff = enemyMineCount - benderMineCount;
        if (diff < RewardConfig.getLowerMineBoundryTotal())
            return Quantity.FEW;
        else if (diff > RewardConfig.getUpperMineBoundryTotal())
            return Quantity.LOTS;
        return Quantity.MIDDLE;
    }

    /**
     *
     * @param mineCount
     * @return Quantity.LOTS = good to attak, .MIDDLE = maybe attack, .FEW = don't attack
     */
    private Quantity calcMine(int mineCount){
        if (mineCount < RewardConfig.getLowerMineBoundryTotal())
            return Quantity.FEW;
        else if (mineCount > RewardConfig.getUpperMineBoundryTotal())
            return Quantity.LOTS;
        return Quantity.MIDDLE;
    }
}

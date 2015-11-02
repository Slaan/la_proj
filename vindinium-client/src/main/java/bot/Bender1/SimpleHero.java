package bot.Bender1;

import bot.Bender.BotMove;
import bot.RewardConfig;

/**
 * Created by slaan on 02.11.15.
 */
public class SimpleHero {

    private Quantity lifeDifference;
    private Quantity distance;
    private Quantity enemyMines;
    private BotMove direction;

    public SimpleHero(int benderLife, int enemyLife, BotMove direction, Quantity enemyMines, Quantity distance) {
        this.direction = direction;
        this.lifeDifference = calcLifeDif(benderLife,enemyLife);
        this.distance = distance;
        this.enemyMines = enemyMines;
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
}

package bot.Bender0;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by Daniel Hofmeister on 30.10.2015.
 */
public class RewardConfig {

    static InputStream inputstream;

    static int turnReward;
    static int getMineReward;
    static int blockedReward;
    static int deathDefaultReward;
    static int deathperMineReward;
    static int deathForAllMinesReward;
    static Double tavernPerHP;
    static int tavernDefault;
    static int killPerMine;
    static int killPerMineDiscount;

    static int lowerLifeBoundry;
    static int upperLifeboundry;
    static int lowerMineBoundryTotal;
    static int upperMineBoundryTotal;
    static int lowerMineBoundryPercentage;
    static int upperMineBoundryPercentage;
    static int enemyLifeMore;
    static int enemyLifeLess;
    static int distantClose;
    static int distantFar;


    public static void init() {
        Properties prop = new Properties();
        String propFileName = "res/mainConfig.properties";

        try {
            inputstream = new FileInputStream(propFileName);

            prop.load(inputstream);

            turnReward = Integer.parseInt(prop.getProperty("turn"));
            getMineReward = Integer.parseInt(prop.getProperty("getMine"));
            blockedReward = Integer.parseInt(prop.getProperty("blocked"));
            deathDefaultReward = Integer.parseInt(prop.getProperty("deathDefault"));
            deathperMineReward = Integer.parseInt(prop.getProperty("deathPerMine"));
            deathForAllMinesReward = Integer.parseInt(prop.getProperty("deathForAllMines"));
            tavernPerHP = Double.parseDouble(prop.getProperty("tavernPerHP"));
            tavernDefault = Integer.parseInt(prop.getProperty("tavernDefault"));
            killPerMine = Integer.parseInt(prop.getProperty("killPerMine"));
            killPerMineDiscount = Integer.parseInt(prop.getProperty("killPerMineDiscount"));

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            if (inputstream!=null) {
                try {
                    inputstream.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }

    }

    public static int getTurnReward() {
        return turnReward;
    }

    public static int getGetMineReward() {
        return getMineReward;
    }

    public static int getBlockedReward() {
        return blockedReward;
    }

    public static int getDeathDefaultReward() {
        return deathDefaultReward;
    }

    public static int getDeathperMineReward() {
        return deathperMineReward;
    }

    public static int getDeathForAllMinesReward() {
        return deathForAllMinesReward;
    }

    public static Double getTavernPerHP() {
        return tavernPerHP;
    }

    public static int getTarvernDefault() {
        return tavernDefault;
    }

    public static int getKillPerMine() {
        return killPerMine;
    }

    public static int getKillPerMineDiscount() {
        return killPerMineDiscount;
    }

    public static InputStream getInputstream() {
        return inputstream;
    }

    public static int getTavernDefault() {
        return tavernDefault;
    }

    public static int getLowerLifeBoundry() {
        return lowerLifeBoundry;
    }

    public static int getUpperLifeboundry() {
        return upperLifeboundry;
    }

    public static int getLowerMineBoundryTotal() {
        return lowerMineBoundryTotal;
    }

    public static int getUpperMineBoundryTotal() {
        return upperMineBoundryTotal;
    }

    public static int getLowerMineBoundryPercentage() {
        return lowerMineBoundryPercentage;
    }

    public static int getUpperMineBoundryPercentage() {
        return upperMineBoundryPercentage;
    }

    public static int getEnemyLifeMore() {
        return enemyLifeMore;
    }

    public static int getEnemyLifeLess() {
        return enemyLifeLess;
    }

    public static int getDistantClose() {
        return distantClose;
    }

    public static int getDistantFar() {
        return distantFar;
    }
}

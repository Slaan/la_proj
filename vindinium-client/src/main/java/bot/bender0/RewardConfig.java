package bot.bender0;

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
    static double tavernPerHP;
    static int tavernDefault;
    static int killPerMine;
    static double killPerMineDiscount;

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
        String propFileName = "res/rewardConfigBender0.properties";

        try {
            inputstream = new FileInputStream(propFileName);

            prop.load(inputstream);

            turnReward = Integer.parseInt(prop.getProperty("turn","-1"));
            getMineReward = Integer.parseInt(prop.getProperty("getMine","50"));
            blockedReward = Integer.parseInt(prop.getProperty("blocked","-10"));
            deathDefaultReward = Integer.parseInt(prop.getProperty("deathDefault","0"));
            deathperMineReward = Integer.parseInt(prop.getProperty("deathPerMine","-60"));
            deathForAllMinesReward = Integer.parseInt(prop.getProperty("deathForAllMines","0"));
            tavernPerHP = Double.parseDouble(prop.getProperty("tavernPerHP","0.5"));
            tavernDefault = Integer.parseInt(prop.getProperty("tavernDefault","-20"));
            killPerMine = Integer.parseInt(prop.getProperty("killPerMine","40"));
            killPerMineDiscount = Double.parseDouble(prop.getProperty("killPerMineDiscount","0.9"));

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

    public static double getTavernPerHP() {
        return tavernPerHP;
    }

    public static int getTarvernDefault() {
        return tavernDefault;
    }

    public static int getKillPerMine() {
        return killPerMine;
    }

    public static double getKillPerMineDiscount() {
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

package bot.bender1;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Created by Daniel Hofmeister on 06.11.2015.
 */
public class RewardConfigBender1 {

    static int turnReward;
    static int getMineReward;
    static int deathDefaultReward;
    static int deathperMineReward;
    static int deathForAllMinesReward;
    static int tavernRewardHighHP;
    static int tavernRewardMiddleHP;
    static int tavernRewardLowHP;
    static int killDefault;
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
        String propFileName = "res/rewardConfigBender1.properties";
        FileInputStream inputStream=null;

        try {
            inputStream = new FileInputStream(propFileName);
            if (inputStream == null || inputStream.available() == 0)
                throw new RuntimeException("konnte " + propFileName + " nicht öffnen.");
            prop.load(inputStream);
            if (!prop.propertyNames().hasMoreElements())
                throw new RuntimeException("konnte " + propFileName + " nicht parsen.");

            turnReward = Integer.parseInt(prop.getProperty("turn"));
            getMineReward = Integer.parseInt(prop.getProperty("getMine"));
            deathDefaultReward = Integer.parseInt(prop.getProperty("deathDefault"));
            deathperMineReward = Integer.parseInt(prop.getProperty("deathPerMine"));
            deathForAllMinesReward = Integer.parseInt(prop.getProperty("deathForAllMines"));
            tavernRewardHighHP = Integer.parseInt(prop.getProperty("tavernRewardHighHP"));
            tavernRewardMiddleHP = Integer.parseInt(prop.getProperty("tavernRewardMiddleHP"));
            tavernRewardLowHP = Integer.parseInt(prop.getProperty("tavernRewardLowHP"));
            killDefault = Integer.parseInt(prop.getProperty("killDefault"));
            killPerMine = Integer.parseInt(prop.getProperty("killPerMine"));
            killPerMineDiscount = Double.parseDouble(prop.getProperty("killPerMineDiscount"));

            lowerLifeBoundry = Integer.parseInt(prop.getProperty("lowerLifeBoundry"));
            upperLifeboundry = Integer.parseInt(prop.getProperty("upperLifeBoundry"));
            lowerMineBoundryTotal = Integer.parseInt(prop.getProperty("lowerMineBoundryTotal"));
            upperMineBoundryTotal = Integer.parseInt(prop.getProperty("upperMineBoundryTotal"));
            lowerMineBoundryPercentage = Integer.parseInt(prop.getProperty("lowerMineBoundryPercentage"));
            upperMineBoundryPercentage = Integer.parseInt(prop.getProperty("upperMineBoundryPercentage"));
            enemyLifeMore = Integer.parseInt(prop.getProperty("enemyLifeMore"));
            enemyLifeLess = Integer.parseInt(prop.getProperty("enemyLifeLess"));
            distantClose = Integer.parseInt(prop.getProperty("distantClose"));
            distantFar = Integer.parseInt(prop.getProperty("distantFar"));

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            if (inputStream!=null) {
                try {
                    inputStream.close();
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

    public static int getDeathDefaultReward() {
        return deathDefaultReward;
    }

    public static int getDeathperMineReward() {
        return deathperMineReward;
    }

    public static int getDeathForAllMinesReward() {
        return deathForAllMinesReward;
    }

    public static int getKillPerMine() {
        return killPerMine;
    }

    public static double getKillPerMineDiscount() {
        return killPerMineDiscount;
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

    public static int getTavernRewardHighHP() {
        return tavernRewardHighHP;
    }

    public static int getTavernRewardMiddleHP() {
        return tavernRewardMiddleHP;
    }

    public static int getTavernRewardLowHP() {
        return tavernRewardLowHP;
    }

    public static int getKillDefault() {
        return killDefault;
    }
}

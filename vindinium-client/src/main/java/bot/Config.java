package bot;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by slaan on 26.10.15.
 */
public class Config {

    static String name;
    static int NoOfRounds;
    static int NoOfThreads;
    static String APIKey;
    static String SlackULR;
    static String DBUser;
    static String DBPassword;
    static String Mode;
    static Double LearningRate;
    static Double ExplorationRate;
    static Double DiscountFactor;
    static Double Lamda;
    static int QueueLength;
    static InputStream inputstream=null;


    private Config() {}

    public static void init() {
        Properties prop = new Properties();
        String propFileName = "res/mainconfig.properties";

        try {
            inputstream = new FileInputStream(propFileName);

            prop.load(inputstream);

            name = prop.getProperty("name");
            NoOfRounds = Integer.parseInt(prop.getProperty("rounds"));
            NoOfThreads = Integer.parseInt(prop.getProperty("threads"));
            APIKey = prop.getProperty("apikey");
            SlackULR = prop.getProperty("slackurl");
            DBUser = prop.getProperty("dbuser");
            DBPassword = prop.getProperty("dbpassword");
            Mode = prop.getProperty("modus");
            LearningRate = Double.parseDouble(prop.getProperty("learningrate"));
            ExplorationRate = Double.parseDouble(prop.getProperty("explorationrate"));
            DiscountFactor = Double.parseDouble(prop.getProperty("discountfactor"));
            Lamda = Double.parseDouble(prop.getProperty("lamda"));
            QueueLength = Integer.parseInt(prop.getProperty("queuelength"));

        } catch (Exception e) {
            e.printStackTrace();
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

    public static String getName() {return name;}

    public static int getNoOfRounds() {return NoOfRounds;}

    public static int getNoOfThreads() {return NoOfThreads;}

    public static String getAPIKey() {return APIKey;}

    public static String getSlackULR() {return SlackULR;}

    public static String getDBUser() {return DBUser;}

    public static String getDBPassword() {return DBPassword;}

    public static String getMode() {return Mode;}

    public static double getLearningRate() {return LearningRate;}

    public static double getExplorationRate() {return ExplorationRate;}

    public static double getDiscountFactor() {return DiscountFactor;}

    public static double getLambda() {return Lamda;}

    public static int getQueueLenght() {return QueueLength;}

}

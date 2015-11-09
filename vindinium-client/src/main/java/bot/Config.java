package bot;

import bot.dto.ApiKey;
import bot.dto.TurnApiKey;
import com.google.api.client.http.GenericUrl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by slaan on 26.10.15.
 */
public class Config {
    private static final Logger logger = LogManager.getLogger(Config.class);

    static String name;
    static int NoOfRounds;
    static int NoOfThreads;
    static ApiKey APIKey;
    static GenericUrl SlackURL;
    static GenericUrl GameURL;
    static String DBUser;
    static String DBPassword;
    static String Mode;
    static Double LearningRate;
    static Double ExplorationRate;
    static Double DiscountFactor;
    static Double Lamda;
    static int QueueLength;
    static boolean sarsaStateActionLogs;
    static String Bender;
    static String serverURL;

    private final static String TRAINING_URL = "/api/training";
    private final static String COMPETITION_URL = "/api/arena";

    private Config() {}

    public static void init() {
        Properties prop = new Properties();
        String propFileName = "res/mainConfig.properties";

        InputStream inputstream = null;

        try {
            inputstream = new FileInputStream(propFileName);

            prop.load(inputstream);

            name = prop.getProperty("name");
            NoOfRounds = Integer.parseInt(prop.getProperty("rounds"));
            if (NoOfRounds > 600) {
                logger.warn("rounds is greater than 600 (" + NoOfRounds + ") it will be set to 600.");
                NoOfRounds = 600;
            }
            NoOfThreads = Integer.parseInt(prop.getProperty("threads"));
            SlackURL = new GenericUrl(prop.getProperty("slackurl"));
            DBUser = prop.getProperty("dbuser");
            DBPassword = prop.getProperty("dbpassword");
            serverURL = prop.getProperty("serverURL");
            Mode = prop.getProperty("modus");
            if ("COMPETITION".equals(Mode)) {
                GameURL = new GenericUrl(serverURL+COMPETITION_URL);
                APIKey = new ApiKey(prop.getProperty("apikey"));
            }
            else{
                GameURL = new GenericUrl(serverURL+TRAINING_URL);
                APIKey = new TurnApiKey(prop.getProperty("apikey"), NoOfRounds);
            }
            LearningRate = Double.parseDouble(prop.getProperty("learningrate"));
            ExplorationRate = Double.parseDouble(prop.getProperty("explorationrate"));
            DiscountFactor = Double.parseDouble(prop.getProperty("discountfactor"));
            Lamda = Double.parseDouble(prop.getProperty("lamda"));
            QueueLength = Integer.parseInt(prop.getProperty("queuelength"));
            sarsaStateActionLogs = Boolean.parseBoolean(prop.getProperty("sarsaStateActionLogs"));
            Bender = prop.getProperty("Bender");

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

    public static String getName() {return name;}

    public static int getNoOfRounds() {return NoOfRounds;}

    public static int getNoOfThreads() {return NoOfThreads;}

    public static ApiKey getAPIKey() {return APIKey;}

    public static GenericUrl getSlackULR() {return SlackURL;}

    public static GenericUrl getGameURL() {return GameURL;}

    public static String getDBUser() {return DBUser;}

    public static String getDBPassword() {return DBPassword;}

    public static String getMode() {return Mode;}

    public static double getLearningRate() {return LearningRate;}

    public static double getExplorationRate() {return ExplorationRate;}

    public static double getDiscountFactor() {return DiscountFactor;}

    public static double getLambda() {return Lamda;}

    public static int getQueueLenght() {return QueueLength;}

    public static boolean getSarsaStateActionLogs() { return sarsaStateActionLogs; }

    public static String getBender() { return Bender; }
}

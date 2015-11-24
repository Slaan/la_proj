package bot;

import bot.dto.ApiKey;
import bot.dto.TurnApiKey;
import com.google.api.client.http.GenericUrl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Created by slaan on 26.10.15.
 */
public class Config {
    private static final Logger logger = LogManager.getLogger(Config.class);

    static String name;
    static int noOfRounds;
    static int noOfThreads;
    static Map<String, ApiKey> aPIKeyMap;
    static GenericUrl slackURL;
    static int slackWait;
    static GenericUrl gameURL;
    static String DBUser;
    static String DBPassword;
    static String mode;

    //Sarsa Lambda
    static Double learningRate;
    static Double explorationRate;
    static Double discountFactor;
    static Double lamda;
    static int queueLength;
    static boolean stateActionLogs;

    //Dijkstra
    static int stepsToLook;
    static int numberOfHerosToLook;
    static int numberOfMinesToLook;
    static int numberOfTavernsToLook;

    static String[] bender;
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
            noOfRounds = Integer.parseInt(prop.getProperty("rounds"));
            if (noOfRounds > 600) {
                logger.warn("rounds is greater than 600 (" + noOfRounds + ") it will be set to 600.");
                noOfRounds = 600;
            }
            noOfThreads = Integer.parseInt(prop.getProperty("threads"));
            slackURL = new GenericUrl(prop.getProperty("slackurl"));
            slackWait = Integer.parseInt(prop.getProperty("slackwait", "1"));
            DBUser = prop.getProperty("dbuser");
            DBPassword = prop.getProperty("dbpassword");
            serverURL = prop.getProperty("serverURL");
            mode = prop.getProperty("modus");

            learningRate = Double.parseDouble(prop.getProperty("learningrate"));
            explorationRate = Double.parseDouble(prop.getProperty("explorationrate"));
            discountFactor = Double.parseDouble(prop.getProperty("discountfactor"));
            lamda = Double.parseDouble(prop.getProperty("lamda"));
            queueLength = Integer.parseInt(prop.getProperty("queuelength"));
            stateActionLogs = Boolean.parseBoolean(prop.getProperty("sarsaStateActionLogs"));
            String benderString = prop.getProperty("Bender");
            bender = benderString.split(",");

            String[] apikeys = prop.getProperty("apikey").split(",");
            aPIKeyMap = new HashMap<>();
            if ("ARENA".equals(mode)) {
                gameURL = new GenericUrl(serverURL+COMPETITION_URL);
                for(int i = 0; i < bender.length; i++){
                    if(apikeys.length == 1){
                        aPIKeyMap.put(bender[i], new ApiKey(apikeys[0]));
                    } else {
                        aPIKeyMap.put(bender[i], new ApiKey(apikeys[i]));
                    }
                }
            }
            else{
                gameURL = new GenericUrl(serverURL+TRAINING_URL);
                for(int i = 0; i < bender.length; i++){
                    if(apikeys.length == 1){
                        aPIKeyMap.put(bender[i], new TurnApiKey(apikeys[0], noOfRounds));
                    } else {
                        aPIKeyMap.put(bender[i], new TurnApiKey(apikeys[i], noOfRounds));
                    }
                }
            }


            stepsToLook = Integer.parseInt(prop.getProperty("stepsToLook"));
            numberOfHerosToLook = Integer.parseInt(prop.getProperty("numberOfHerosToLook"));
            numberOfMinesToLook = Integer.parseInt(prop.getProperty("numberOfMinesToLook"));
            numberOfTavernsToLook = Integer.parseInt(prop.getProperty("numberOfTavernsToLook"));

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

    public static int getNoOfRounds() {return noOfRounds;}

    public static int getNoOfThreads() {return noOfThreads;}

    public static ApiKey getAPIKey(String bender) {return aPIKeyMap.get(bender);}

    public static GenericUrl getSlackULR() {return slackURL;}

    public static GenericUrl getGameURL() {return gameURL;}

    public static String getDBUser() {return DBUser;}

    public static String getDBPassword() {return DBPassword;}

    public static String getMode() {return mode;}

    public static double getLearningRate() {return learningRate;}

    public static double getExplorationRate() {return explorationRate;}

    public static double getDiscountFactor() {return discountFactor;}

    public static double getLambda() {return lamda;}

    public static int getQueueLenght() {return queueLength;}

    public static boolean getStateActionLogs() { return stateActionLogs; }

    public static String[] getBender() { return bender; }

    public static int getStepsToLook() { return stepsToLook; }

    public static int getNumberOfHerosToLook() { return numberOfHerosToLook; }

    public static int getNumberOfMinesToLook() { return numberOfMinesToLook; }

    public static int getNumberOfTavernsToLook() { return numberOfTavernsToLook; }

    public static int getSlackWait() { return slackWait; }
}

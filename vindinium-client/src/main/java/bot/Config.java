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
    static String mode;

    //DB
    static String DBUser;
    static String DBPassword;
    static Map<String, String> DB;

    //Learning Algorithm
    static Double learningRate;
    static Double explorationRate;
    static Double discountFactor;
    static Double lamda;
    static int queueLength;
    static boolean stateActionLogs;
    static String learningAlgorithm;

    //Dijkstra
    static long timeToRunInMS;
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

            name = prop.getProperty("name","default");
            noOfRounds = Integer.parseInt(prop.getProperty("rounds","400"));
            if (noOfRounds > 600) {
                logger.warn("rounds is greater than 600 (" + noOfRounds + ") it will be set to 600.");
                noOfRounds = 600;
            }
            noOfThreads = Integer.parseInt(prop.getProperty("threads","1"));
            slackURL = new GenericUrl(prop.getProperty("slackurl",null));
            slackWait = Integer.parseInt(prop.getProperty("slackwait", "1"));
            DBUser = prop.getProperty("dbuser","root");
            DBPassword = prop.getProperty("dbpassword","root");

            String[] dBString = prop.getProperty("DB","bender2").split(",");


            serverURL = prop.getProperty("serverURL","http://vindinium.org");
            mode = prop.getProperty("modus","TRAINING");

            learningRate = Double.parseDouble(prop.getProperty("learningrate","0.1"));
            explorationRate = Double.parseDouble(prop.getProperty("explorationrate","0.1"));
            discountFactor = Double.parseDouble(prop.getProperty("discountfactor","0.9"));
            lamda = Double.parseDouble(prop.getProperty("lamda","0.9"));
            queueLength = Integer.parseInt(prop.getProperty("queuelength","10"));
            stateActionLogs = Boolean.parseBoolean(prop.getProperty("sarsaStateActionLogs","false"));
            learningAlgorithm = prop.getProperty("learningAlgorithm","Qlearning");
            String benderString = prop.getProperty("Bender","bender2");
            bender = benderString.split(",");

            if(dBString.length != bender.length){
                throw new RuntimeException("you need the right quantity of DBs");
            }
            DB = new HashMap<>();
            for (int i = 0; i < dBString.length; i++){
                DB.put(bender[i], dBString[i]);
            }

            String[] apikeys = prop.getProperty("apikey","sn52twky").split(",");
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

            timeToRunInMS = Long.parseLong(prop.getProperty("timeToRunInMS","700"));
            stepsToLook = Integer.parseInt(prop.getProperty("stepsToLook","10"));
            numberOfHerosToLook = Integer.parseInt(prop.getProperty("numberOfHerosToLook","1"));
            numberOfMinesToLook = Integer.parseInt(prop.getProperty("numberOfMinesToLook","1"));
            numberOfTavernsToLook = Integer.parseInt(prop.getProperty("numberOfTavernsToLook","1"));

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

    public static String getLearningAlgorithm() {
        return learningAlgorithm;
    }

    public static boolean getStateActionLogs() { return stateActionLogs; }

    public static String[] getBender() { return bender; }

    public static long getTimeToRunInMS() { return timeToRunInMS; }

    public static int getStepsToLook() { return stepsToLook; }

    public static int getNumberOfHerosToLook() { return numberOfHerosToLook; }

    public static int getNumberOfMinesToLook() { return numberOfMinesToLook; }

    public static int getNumberOfTavernsToLook() { return numberOfTavernsToLook; }

    public static int getSlackWait() { return slackWait; }

    public static String getDBForBender(String bender) { return DB.get(bender); }
}

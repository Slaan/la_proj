package bot;

import bot.dto.TurnApiKey;
import bot.simple.Bender;
import bot.simple.SimpleBot;
import bot.simple.SimpleBotRunner;
import bot.dto.ApiKey;
import com.google.api.client.http.*;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.gson.Gson;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import persistence.ManageSarsaState;
import persistence.ManageSarsaStateAction;
import persistence.SessionBuilder;

/**
 * CLI program for launching a bot
 */
public class Main {
    private static final int TURNS_DEFAULT = 20;
    private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
    private static final JsonFactory JSON_FACTORY = new GsonFactory();
    private static final Gson gson = new Gson();
    private static final Logger logger = LogManager.getLogger(Main.class);
    private static final Logger gameStateLogger = LogManager.getLogger("gameStateLogger");

    public static void main(String args[]) throws Exception {

        final String user = args[0];
        final GenericUrl slackUrl = new GenericUrl(args[1]);
        final String key = args[2];
        final String dBUser = args[3];
        final String dBPassword = args[4];
        final String arena = args[5];
        final int turns;
        if (args.length >= 7) {
            turns = Integer.parseInt(args[6]);
        } else {
            turns = TURNS_DEFAULT;
        }

        final GenericUrl gameUrl;
        final ApiKey apiKey;

        if ("TRAINING".equals(arena)) {
            gameUrl = VindiniumUrl.getTrainingUrl();
            apiKey = new TurnApiKey(key, turns);
        } else if ("COMPETITION".equals(arena)) {
            gameUrl = VindiniumUrl.getCompetitionUrl();
            apiKey = new ApiKey(key);
        } else
            throw new RuntimeException("You have to set arena to TRAINING or COMPETITION.");

        SessionBuilder sessionBuilder = new SessionBuilder(dBUser, dBPassword);
        ManageSarsaStateAction manageSarsaStateAction = new ManageSarsaStateAction(sessionBuilder.getFactory());
        ManageSarsaState manageSarsaState = new ManageSarsaState(sessionBuilder.getFactory(), manageSarsaStateAction);

        SimpleBot bot = new Bender(manageSarsaState);
        SimpleBotRunner runner = new SimpleBotRunner(slackUrl, apiKey, gameUrl, bot, user);
        runner.call();
    }

    /**
     * Represents the endpoint URL
     */
    public static class VindiniumUrl extends GenericUrl {
        private final static String TRAINING_URL = "http://vindinium.org/api/training";
        private final static String COMPETITION_URL = "http://vindinium.org/api/arena";

        public VindiniumUrl(String encodedUrl) {
            super(encodedUrl);
        }

        public static VindiniumUrl getCompetitionUrl() {
            return new VindiniumUrl(COMPETITION_URL);
        }

        public static VindiniumUrl getTrainingUrl() {
            return new VindiniumUrl(TRAINING_URL);
        }
    }
}

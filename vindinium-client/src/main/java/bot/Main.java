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

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

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

    private final String user;
    private final GenericUrl slackUrl;
    private final String key;
    private final String dBUser;
    private final String dBPassword;
    private final String arena;
    private final int turns;
    private final GenericUrl gameUrl;
    private final ApiKey apiKey;
    private final ManageSarsaState manageSarsaState;

    public static void main(String args[]) throws Exception {
        new Main(args);
    }

    private Main(String args[]) throws Exception {
        user = args[0];
        slackUrl = new GenericUrl(args[1]);
        key = args[2];
        dBUser = args[3];
        dBPassword = args[4];
        arena = args[5];
        if (args.length > 6) {
            turns = Integer.parseInt(args[6]);
        } else {
            turns = TURNS_DEFAULT;
        }
        int threadNumber = 0;
        if (args.length > 7) {
            threadNumber = Integer.parseInt(args[7]);
        }

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
        manageSarsaState = new ManageSarsaState(sessionBuilder.getFactory(), manageSarsaStateAction);

        if (threadNumber == 0) {
            // For debug-purpuses only.
            new MainThreads(this).run();
        } else {
            // Start the specified number of threads.
            List<Thread> threads = new LinkedList<>();
            for (int threadCount = 0; threadCount < threadNumber; threadCount++) {
                Thread thread = new MainThreads(this);
                thread.start();
                threads.add(thread);
                Thread.sleep(125);
            }
            // Wait for everyone to end.
            for (Thread thread : threads) {
                thread.join();
            }
        }

        manageSarsaState.updateSarsaStates();
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

    private static class MainThreads extends Thread {
        private Main main;

        public MainThreads(Main main) {
            this.main = main;
        }

        @Override public void run() {
            try {
                SimpleBot bot = new Bender(main.manageSarsaState);
                SimpleBotRunner runner = new SimpleBotRunner(main.slackUrl, main.apiKey, main.gameUrl, bot, main.user);
                runner.call();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

package bot;

import bot.dto.GameState;
import bot.dto.TurnApiKey;
import bot.simple.Bender;
import bot.simple.SimpleBotRunner;
import bot.dto.ApiKey;
import com.google.api.client.http.*;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.gson.Gson;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import persistence.*;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * CLI program for launching a bot
 */
public class Main extends Thread{
    private static  int TURNS_DEFAULT = 20;
    private static  HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
    private static  JsonFactory JSON_FACTORY = new GsonFactory();
    private static  Gson gson = new Gson();
    private static  Logger logger = LogManager.getLogger(Main.class);
    private static  Logger gameStateLogger = LogManager.getLogger("gameStateLogger");

    private String user;
    private GenericUrl slackUrl;
    private String key;
    private String dBUser;
    private String dBPassword;
    private String arena;
    private int turns;
    private int threadNumber;
    private GenericUrl gameUrl;
    private ApiKey apiKey;
    private ManageSarsaState manageSarsaState;

    private Main(String args[]){
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
        threadNumber = 0;
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
    }


    public static void main(String args[]){
        Main main = new Main(args);
        main.run();
    }


    @Override
    public void run(){
        try {
            SharedBuffer<String> slackBuffer= new SharedBuffer<>();
            List<SimpleBotRunner> runners = new ArrayList<>();
            SharedBuffer<GameLog> gameLogBuffer = new SharedBuffer<>();
            for(int i = 0; i<threadNumber; i++) {
                runners.add(new SimpleBotRunner(slackUrl, apiKey, gameUrl, user, dBUser, dBPassword, slackBuffer, gameLogBuffer));
            }

            for(SimpleBotRunner runner: runners){
                runner.start();
                sleep(1250);
            }

            DataInputStream dataInputStream = new DataInputStream(System.in);
            while (dataInputStream.available() == 0) {            }

            for(SimpleBotRunner runner: runners){
                runner.interrupt();
            }

            for(SimpleBotRunner runner: runners){
                runner.join();
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

 /*   private Main() throws Exception {






        int winCount = 0;
        int loseCount = 0;
        int crashCount = 0;
        if (threadNumber == 0) {
            // For debug-purpuses only.
            MainThreads thread = new MainThreads(this);
            thread.run();
            if (thread.isCrashed())
                crashCount++;
            else if (thread.isWinner())
                winCount++;
            else
                loseCount++;
        } else {
            // Start the specified number of threads.
            List<MainThreads> threads = new LinkedList<>();
            for (int threadCount = 0; threadCount < threadNumber; threadCount++) {
                MainThreads thread = new MainThreads(this);
                thread.start();
                threads.add(thread);
                Thread.sleep(1250);
            }


            // Learning... restart every Thread till you press a key.
            DataInputStream dataInputStream = new DataInputStream(System.in);
            while (dataInputStream.available() == 0) {
                for (int i = 0; i < threads.size(); i++) {
                    MainThreads thread = threads.get(i);
                    if (!thread.isAlive()) {
                        if (thread.isCrashed())
                            crashCount++;
                        else if (thread.isWinner())
                            winCount++;
                        else
                            loseCount++;
                        threads.remove(i);
                        i--;

                        thread = new MainThreads(this);
                        thread.start();
                        threads.add(thread);
                    }
                }
            }
            dataInputStream.close();
            System.out.println("Waiting for threads to join.");
            // Wait for everyone to end.
            for (MainThreads thread : threads) {
                thread.join();
                if (thread.isCrashed())
                    crashCount++;
                else if (thread.isWinner())
                    winCount++;
                else
                    loseCount++;
            }
        }

        manageSarsaState.updateSarsaStates();
        System.out.println(String.format(""
                + "Main ende.\n"
                + "Gewonnen:\t%d\n"
                + "Verloren:\t%d\n"
                + "Gecrasht:\t%d\n"
                + "WinRate:\t%d%% (ohne Crashes)\n",
            winCount,
            loseCount,
            crashCount,
            (int)(((double)winCount / (winCount + loseCount)) * 100)));
    }
*/
    /**
     * Represents the endpoint URL
     */
    public static class VindiniumUrl extends GenericUrl {
        private final static String TRAINING_URL = "http://vindinium.org/api/training";
        private final static String COMPETITION_URL = "http://vindinium.org/api/arena";//*/
        /*private final static String TRAINING_URL = "http://localhost:9000/api/training";
        private final static String COMPETITION_URL = "http://localhost:9000/api/arena";//*/

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

 /*   private static class MainThreads extends Thread {
        private Main main;
        private boolean isWinner = false;
        private boolean isCrashed = false;

        public MainThreads(Main main) {
            this.main = main;
        }

        @Override public void run() {
            try {
                Bender bot = new Bender(main.manageSarsaState);

                GameState gs = runner.call();
                isWinner = runner.isWinner(gs);
                isCrashed = gs == null || gs.getHero().isCrashed() || !gs.getGame().isFinished();
            } catch (Exception e) {
                e.printStackTrace();
                isCrashed = true;
            }
        }

        public boolean isWinner() {
            return isWinner;
        }

        public boolean isCrashed() {
            return isCrashed;
        }
    } */
}

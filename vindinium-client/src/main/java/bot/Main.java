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
import org.hibernate.SessionFactory;
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

    private static  Logger logger = LogManager.getLogger(Main.class);
    private static  Logger gameStateLogger = LogManager.getLogger("gameStateLogger");


    private ManageSarsaState manageSarsaState;

    private Main(){}

    public static void main(String args[]){
        Config.init();
        SessionBuilder.generateSessionFactory();
        Main main = new Main();
        main.run();
    }


    @Override
    public void run(){
        try {
            SharedBuffer<String> slackBuffer= new SharedBuffer<>();
            List<SimpleBotRunner> runners = new ArrayList<>();
            SharedBuffer<GameLog> gameLogBuffer = new SharedBuffer<>();
            SessionFactory factory = SessionBuilder.generateSessionFactory();
            ManageGameLog manageGameLog = new ManageGameLog(factory);
            ManageSarsaStateAction manageSarsaStateAction = new ManageSarsaStateAction(factory);
            ManageSarsaState manageSarsaState = new ManageSarsaState(factory, manageSarsaStateAction);
            for(int i = 0; i<Config.getNoOfThreads(); i++) {
                runners.add(new SimpleBotRunner(manageSarsaState, manageGameLog, slackBuffer, gameLogBuffer));
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

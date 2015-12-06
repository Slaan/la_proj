package bot;

import bot.bender.BenderRunner;
import bot.bender0.RewardConfig;
import bot.bender1.RewardConfigBender1;
import org.hibernate.SessionFactory;
import persistence.*;

import java.io.DataInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * CLI program for launching a bot
 */
public class Main extends Thread{
    private Main(){}

    public static void main(String args[]){
        Config.init();
        RewardConfig.init();
        RewardConfigBender1.init();
        Main main = new Main();
        main.run();
    }


    @Override
    public void run(){
        try {
            List<BenderRunner> runners = new ArrayList<>();
            Map<String, SharedBuffer<GameLog>> gameLogBuffers = new HashMap<>();



            for(String bender : Config.getBender()) {
                SharedBuffer<GameLog> gameLogBuffer = new SharedBuffer<>();
                SessionFactory factory = SessionBuilder.generateSessionFactory(bender);
                ManageGameLog manageGameLog = new ManageGameLog(factory);
                ManageStateActionLog manageStateActionLog = new ManageStateActionLog(factory);
                ManageStateAction manageStateAction = new ManageStateAction(factory, manageStateActionLog);
                ManageState manageState = new ManageState(factory, manageStateAction);
                for (int i = 0; i < Config.getNoOfThreads(); i++) {
                    runners.add(new BenderRunner(bender, manageState, manageGameLog, gameLogBuffer));
                }
                gameLogBuffers.put(bender, gameLogBuffer);
            }

            for (BenderRunner runner : runners) {
                runner.start();
            }
            // if no slackURL is present in config no SlackThread will be started
            SlackThread slackThread = null;
            if (!Config.getSlackULR().equals(null)) {
                slackThread = new SlackThread(gameLogBuffers);
                slackThread.start();
            }
            DataInputStream dataInputStream = new DataInputStream(System.in);
            while (dataInputStream.available() == 0) {
                Thread.sleep(250);
            }

            for(BenderRunner runner: runners){
                runner.end();
            }
            for(BenderRunner runner: runners){
                runner.join();
            }
            if (!slackThread.equals(null)) {
                slackThread.interrupt();
                slackThread.join();
            }

            System.out.println("Ihr habt Bender getötet, ihr Schweine!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

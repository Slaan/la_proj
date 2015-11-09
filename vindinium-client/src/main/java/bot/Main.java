package bot;

import bot.Bender.BenderRunner;
import bot.Bender0.RewardConfig;
import bot.Bender1.RewardConfigBender1;
import org.hibernate.SessionFactory;
import persistence.*;

import java.io.DataInputStream;
import java.util.ArrayList;
import java.util.List;

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
            SharedBuffer<GameLog> gameLogBuffer = new SharedBuffer<>();
            SlackThread slackThread = new SlackThread(gameLogBuffer);
            SessionFactory factory = SessionBuilder.generateSessionFactory();
            ManageGameLog manageGameLog = new ManageGameLog(factory);
            ManageSarsaStateActionLog manageSarsaStateActionLog = new ManageSarsaStateActionLog(factory);
            ManageSarsaStateAction manageSarsaStateAction = new ManageSarsaStateAction(factory, manageSarsaStateActionLog);
            ManageSarsaState manageSarsaState = new ManageSarsaState(factory, manageSarsaStateAction);
            for(int i = 0; i<Config.getNoOfThreads(); i++) {
                runners.add(new BenderRunner(manageSarsaState, manageGameLog, gameLogBuffer));
            }

            slackThread.start();
            for(BenderRunner runner: runners){
                runner.start();
                sleep(1250);
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
            slackThread.interrupt();
            slackThread.join();

            System.out.println("Ihr habt Bender getötet, ihr Schweine!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

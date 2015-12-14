package bot.bender;

import bot.bender0.Bender0;
import bot.bender1.Bender1;
import bot.Config;
import bot.bender2.Bender2;
import bot.bender3.Bender3;
import bot.dto.ApiKey;
import bot.dto.GameState;
import bot.dto.Move;
import com.google.api.client.http.*;
import com.google.api.client.http.apache.ApacheHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.JsonObjectParser;
import com.google.api.client.json.gson.GsonFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import persistence.GameLog;
import persistence.ManageGameLog;
import persistence.ManageState;
import persistence.SharedBuffer;

import java.io.IOException;

public class BenderRunner extends Thread {
    private static final HttpTransport HTTP_TRANSPORT = new ApacheHttpTransport();
    private static final JsonFactory JSON_FACTORY = new GsonFactory();
    private static final HttpRequestFactory REQUEST_FACTORY =
            HTTP_TRANSPORT.createRequestFactory(new HttpRequestInitializer() {
                @Override
                public void initialize(HttpRequest request) {
                    request.setParser(new JsonObjectParser(JSON_FACTORY));
                }
            });
    private static final Logger logger = LogManager.getLogger(BenderRunner.class);
    private static final UncaughtExceptionHandler exceptionHandler =
        new UncaughtExceptionHandler() {
            @Override public void uncaughtException(Thread thread, Throwable throwable) {
                String msg = "Thread" + thread.getId() + " DIED: " + thread.getName();
                logger.fatal(msg, throwable);
                System.err.println(msg);
                throwable.printStackTrace();
            }
        };

    private final ApiKey apiKey;
    private final GenericUrl gameUrl;
    private final String user;

    private final ManageGameLog manageGameLog;
    private final SharedBuffer<GameLog> gameLogBuffer;
    private final ManageState manageState;

    private final String bender;

    private boolean run = true;

    public BenderRunner(String bender, ManageState manageState, ManageGameLog manageGameLog, SharedBuffer<GameLog> gameLogBuffer) {
        this.setUncaughtExceptionHandler(exceptionHandler);
        this.bender = bender;
        this.apiKey = Config.getAPIKey(bender);
        this.gameUrl = Config.getGameURL();
        this.user = Config.getName();
        this.manageGameLog = manageGameLog;
        this.gameLogBuffer = gameLogBuffer;
        this.manageState = manageState;
        this.setName("Thread[" + this.getName() + "]<" + bender + "," + Config.getLearningAlgorithm() + ">");
    }

    public void end() {
        logger.info("Will stop Thread " + this.toString());
        run = false;
    }

    @Override
    public void run() {
        HttpContent content;
        HttpRequest request;
        HttpResponse response;
        GameState gameState = null;

        try {
            sleep((int)(Math.random() * 20000));
        } catch (InterruptedException e) {}

        while(run) {


            GameLog gameLog = manageGameLog.getGameLog();
            Bender bender = getBender(manageState, gameLog);


            try {

                // Initial request
                logger.debug("Sending initial request...");
                content = new UrlEncodedContent(apiKey);
                request = REQUEST_FACTORY.buildPostRequest(gameUrl, content);
                request.setReadTimeout(1260 * 1000); // Wait 21min to be assigned to a game
                response = request.execute();
                gameState = response.parseAs(GameState.class);

                // URL console output.
                logger.info("Game URL: {}, wir sind Player: {}",
                    gameState.getViewUrl(),
                    gameState.getHero().getId());
                gameLog.setGameURL(gameState.getViewUrl());
                gameLog.setWhoAmI(gameState.getHero().getId());

                // Game loop
                while (!gameState.getGame().isFinished() && !gameState.getHero().isCrashed()) {
                    logger.debug("Taking turn " + gameState.getGame().getTurn());
                    long startTime = System.currentTimeMillis();
                    BotMove direction = bender.move(gameState);
                    if (System.currentTimeMillis()-startTime>800) {
                        logger.info(String.format("Time needed for move: %d", System.currentTimeMillis()-startTime));
                    }
                    Move move = new Move(apiKey.getKey(), direction.toString());


                    HttpContent turn = new UrlEncodedContent(move);
                    HttpRequest turnRequest = REQUEST_FACTORY.buildPostRequest(new GenericUrl(gameState.getPlayUrl()), turn);
                    HttpResponse turnResponse = turnRequest.execute();

                    gameState = turnResponse.parseAs(GameState.class);
                    gameLog.addRound();
                }
            } catch (Exception e) {
                logger.error("Error during game play", e);
                if (gameState != null && gameState.getGame() != null && gameState.getGame().getBoard() != null)
                    logger.error(String.format("The game board (size %d) was:\n%s",
                        gameState.getGame().getBoard().getSize(),
                        gameState.getGame().getBoard().getTiles()));
                gameLog.setCrashed(true);
            }

            bender.finishGame(isWinner(gameState), gameLog.isCrashed());

            GameMap gameMap = new GameMap(gameState);
            gameLog.setTotalMineCount(gameMap.getTotalMineCount());
            gameLog.setWin(isWinner(gameState));
            setRanking(gameState, gameLog);
            manageGameLog.updateGameLog(gameLog);
            if (gameLogBuffer != null)
                gameLogBuffer.addEntity(gameLog);
            logger.debug("Game over");
        }
        logger.info("Stopped Thread " + this.toString());
    }

    public Bender getBender(ManageState manageState, GameLog gameLog){
        if(bender.equals("bender0")){
            return new Bender0(manageState, gameLog);
        } else if (bender.equals("bender1")){
            return new Bender1(manageState, gameLog);
        } else if (bender.equals("bender2")) {
            return new Bender2(manageState, gameLog);
        } else if (bender.equals("bender3")) {
            return new Bender3(manageState, gameLog);
        }
        throw new RuntimeException("Bender " + bender + " is not instantiable.");
    }

    public boolean isWinner(GameState gs) {
        // Check if the game is finished.
        if (gs == null || !gs.getGame().isFinished())
            return false;
        boolean isWinner = true;
        GameState.Hero benderHero = gs.getHero();
        for (GameState.Hero hero : gs.getGame().getHeroes()) {
            if (benderHero.getUserId().equals(hero.getUserId()))
                continue;
            if (hero.getGold() >= benderHero.getGold())
                isWinner = false;
        }
        return isWinner;
    }

    private void setRanking(GameState gs, GameLog gl) {
        for(GameState.Hero hero : gs.getGame().getHeroes()) {
            gl.setHeroForPlace(hero.getId(), hero.getUserId(), hero.getGold());
        }
    }
}

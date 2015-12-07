package bot.bender;

import bot.bender0.Bender0;
import bot.bender1.Bender1;
import bot.Config;
import bot.bender2.Bender2;
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

    private final ApiKey apiKey;
    private final GenericUrl gameUrl;
    private final String user;

    private final ManageGameLog manageGameLog;
    private final SharedBuffer<GameLog> gameLogBuffer;
    private final ManageState manageState;

    private final String bender;

    private boolean run = true;

    public BenderRunner(String bender, ManageState manageState, ManageGameLog manageGameLog, SharedBuffer<GameLog> gameLogBuffer) {
        this.bender = bender;
        this.apiKey = Config.getAPIKey(bender);
        this.gameUrl = Config.getGameURL();
        this.user = Config.getName();
        this.manageGameLog = manageGameLog;
        this.gameLogBuffer = gameLogBuffer;
        this.manageState = manageState;
    }

    public void end() { run = false; }

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
                request.setReadTimeout(0); // Wait forever to be assigned to a game
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
                        System.out.println(System.currentTimeMillis()-startTime);
                    }
                    Move move = new Move(apiKey.getKey(), direction.toString());


                    HttpContent turn = new UrlEncodedContent(move);
                    HttpRequest turnRequest = REQUEST_FACTORY.buildPostRequest(new GenericUrl(gameState.getPlayUrl()), turn);
                    HttpResponse turnResponse = turnRequest.execute();

                    gameState = turnResponse.parseAs(GameState.class);
                    gameLog.addRound();
                }
            } catch (IOException e) {
                logger.error("Error during game play", e);
                if (gameState != null && gameState.getGame() != null && gameState.getGame().getBoard() != null)
                    logger.error(String.format("The game board (size %d) was:\n%s",
                        gameState.getGame().getBoard().getSize(),
                        gameState.getGame().getBoard().getTiles()));
                gameLog.setCrashed(true);
            }


            gameLog.setWin(isWinner(gameState));
            manageGameLog.updateGameLog(gameLog);
            if (gameLogBuffer != null)
                gameLogBuffer.addEntity(gameLog);
            logger.debug("Game over");
        }
    }

    public Bender getBender(ManageState manageState, GameLog gameLog){
        if(bender.equals("bender0")){
            return new Bender0(manageState, gameLog);
        } else if (bender.equals("bender1")){
            return new Bender1(manageState, gameLog);
        } else if (bender.equals("bender2")) {
            return new Bender2(manageState, gameLog);
        }
        return null;
    }

    public boolean isWinner(GameState gs) {
        if (gs == null || !gs.getGame().isFinished())
            return false;
        boolean isWinner = true;
        GameState.Hero benderHero = gs.getHero();
        for (GameState.Hero hero : gs.getGame().getHeroes()) {
            if (hero.getId() == benderHero.getId())
                continue;
            if (hero.getGold() >= benderHero.getGold())
                isWinner = false;
        }
        return isWinner;
    }
}

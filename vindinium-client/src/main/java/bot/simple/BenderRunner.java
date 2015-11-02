package bot.simple;

import bot.BotMove;
import bot.Config;
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
import persistence.*;

import java.io.IOException;
import java.net.URLEncoder;

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
    private final SharedBuffer<String> slackBuffer;
    private final SharedBuffer<GameLog> gameLogBuffer;
    private final ManageSarsaState manageSarsaState;

    public BenderRunner(ManageSarsaState manageSarsaState, ManageGameLog manageGameLog,
        SharedBuffer<String> slackBuffer, SharedBuffer<GameLog> gameLogBuffer) {
        this.apiKey = Config.getAPIKey();
        this.gameUrl = Config.getGameURL();
        this.user = Config.getName();
        this.manageGameLog = manageGameLog;
        this.slackBuffer = slackBuffer;
        this.gameLogBuffer = gameLogBuffer;
        this.manageSarsaState = manageSarsaState;
    }

    @Override
    public void run() {
        HttpContent content;
        HttpRequest request;
        HttpResponse response;
        GameState gameState = null;

        while(!interrupted()) {


            GameLog gameLog = manageGameLog.getGameLog();
            Bender bender = new Bender(manageSarsaState, gameLog);


            try {

                // Initial request
                logger.debug("Sending initial request...");
                content = new UrlEncodedContent(apiKey);
                request = REQUEST_FACTORY.buildPostRequest(gameUrl, content);
                request.setReadTimeout(0); // Wait forever to be assigned to a game
                response = request.execute();
                gameState = response.parseAs(GameState.class);

                // URL console output.
                logger.info("Game URL: {}", gameState.getViewUrl());
                gameLog.setGameURL(gameState.getViewUrl());

                // Game loop
                while (!gameState.getGame().isFinished() && !gameState.getHero().isCrashed()) {
                    logger.debug("Taking turn " + gameState.getGame().getTurn());
                    BotMove direction = bender.move(gameState);
                    Move move = new Move(apiKey.getKey(), direction.toString());


                    HttpContent turn = new UrlEncodedContent(move);
                    HttpRequest turnRequest = REQUEST_FACTORY.buildPostRequest(new GenericUrl(gameState.getPlayUrl()), turn);
                    HttpResponse turnResponse = turnRequest.execute();

                    gameState = turnResponse.parseAs(GameState.class);
                }
                String url = URLEncoder.encode(gameState.getViewUrl(), "UTF-8");
                String msg = String.format(
                        "payload={\"text\": \"<%s> - %s (gestartet von: %s)\"}",
                        url,
                        isWinner(gameState) ? "Gewonnen. War ja klar." : "Verloren, die anderen cheaten. Ganz klar!",
                        user);
                slackBuffer.addEntity(msg);
            } catch (IOException e) {
                logger.error("Error during game play", e);
            }
/*
                // Slack integration.
                String url = URLEncoder.encode(gameState.getViewUrl(), "UTF-8");
                String msg = String.format(
                        "payload={\"text\": \"<%s> - %s (gestartet von: %s)\"}",
                        url,
                        isWinner(gameState) ? "Gewonnen. War ja klar." : "Verloren, die anderen cheaten. Ganz klar!",
                        user);
                content = new ByteArrayContent("application/x-www-form-urlencoded", msg.getBytes());
                logger.debug("Sending to Slack with URL: " + slackUrl);
                request = REQUEST_FACTORY.buildPostRequest(slackUrl, content);
                request.setReadTimeout(120000);
                request.setConnectTimeout(120000);
                request.executeAsync();
*/



            gameLog.setWin(isWinner(gameState));
            gameLogBuffer.addEntity(gameLog);
            manageSarsaState.updateSarsaStates();
            manageGameLog.addGameLog(gameLog);
            logger.debug("Game over");
        }
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

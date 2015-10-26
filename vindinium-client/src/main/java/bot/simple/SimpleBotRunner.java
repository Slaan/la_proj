package bot.simple;

import bot.BotMove;
import bot.dto.ApiKey;
import bot.dto.GameState;
import bot.dto.Move;
import bot.dto.TurnApiKey;
import com.google.api.client.http.*;
import com.google.api.client.http.apache.ApacheHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.JsonObjectParser;
import com.google.api.client.json.gson.GsonFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import persistence.ManageSarsaState;
import persistence.ManageSarsaStateAction;
import persistence.SessionBuilder;

import java.net.URLEncoder;
import java.util.concurrent.Callable;

public class SimpleBotRunner implements Callable<GameState> {
    private static final HttpTransport HTTP_TRANSPORT = new ApacheHttpTransport();
    private static final JsonFactory JSON_FACTORY = new GsonFactory();
    private static final HttpRequestFactory REQUEST_FACTORY =
            HTTP_TRANSPORT.createRequestFactory(new HttpRequestInitializer() {
                @Override
                public void initialize(HttpRequest request) {
                    request.setParser(new JsonObjectParser(JSON_FACTORY));
                }
            });
    private static final Logger logger = LogManager.getLogger(SimpleBotRunner.class);

    private final ApiKey apiKey;
    private final GenericUrl gameUrl;
    private final Bender bot;
    private final GenericUrl slackUrl;
    private final String user;

    public SimpleBotRunner(GenericUrl slackUrl, ApiKey apiKey, GenericUrl gameUrl, Bender bot, String user) {
        this.apiKey = apiKey;
        this.gameUrl = gameUrl;
        this.bot = bot;
        this.slackUrl = slackUrl;
        this.user = user;
    }

    @Override
    public GameState call() throws Exception {
        HttpContent content;
        HttpRequest request;
        HttpResponse response;
        GameState gameState = null;

        try {

            // Initial request
            logger.info("Sending initial request...");
            content = new UrlEncodedContent(apiKey);
            request = REQUEST_FACTORY.buildPostRequest(gameUrl, content);
            request.setReadTimeout(0); // Wait forever to be assigned to a game
            response = request.execute();
            gameState = response.parseAs(GameState.class);

            // URL console output.
            logger.info("Game URL: {}", gameState.getViewUrl());

            // Game loop
            while (!gameState.getGame().isFinished() && !gameState.getHero().isCrashed()) {
                logger.info("Taking turn " + gameState.getGame().getTurn());
                BotMove direction = bot.move(gameState);
                Move move = new Move(apiKey.getKey(), direction.toString());


                HttpContent turn = new UrlEncodedContent(move);
                HttpRequest turnRequest = REQUEST_FACTORY.buildPostRequest(new GenericUrl(gameState.getPlayUrl()), turn);
                HttpResponse turnResponse = turnRequest.execute();

                gameState = turnResponse.parseAs(GameState.class);
            }



            // Slack integration.
            String url = URLEncoder.encode(gameState.getViewUrl(), "UTF-8");
            String msg = String.format(
                "payload={\"text\": \"<%s> - %s (gestartet von: %s)\"}",
                url,
                isWinner(gameState) ? "Gewonnen. War ja klar." : "Verloren, die anderen cheaten. Ganz klar!",
                user);
            content = new ByteArrayContent("application/x-www-form-urlencoded", msg.getBytes());
            logger.info("Sending to Slack with URL: " + slackUrl);
            request = REQUEST_FACTORY.buildPostRequest(slackUrl, content);
            request.setReadTimeout(120000);
            request.setConnectTimeout(120000);
            request.executeAsync();

        } catch (Exception e) {
            logger.error("Error during game play", e);
        }

        logger.info("Game over");
        return gameState;
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

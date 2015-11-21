package bot;

import com.google.api.client.http.*;
import com.google.api.client.http.apache.ApacheHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.JsonObjectParser;
import com.google.api.client.json.gson.GsonFactory;
import persistence.GameLog;
import persistence.SharedBuffer;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Collections;
import java.util.List;

/**
 * Created by octavian on 02.11.15.
 */
public class SlackThread extends Thread {
    private static final HttpTransport HTTP_TRANSPORT = new ApacheHttpTransport();
    private static final JsonFactory JSON_FACTORY = new GsonFactory();
    private static final HttpRequestFactory REQUEST_FACTORY =
        HTTP_TRANSPORT.createRequestFactory(new HttpRequestInitializer() {
            @Override
            public void initialize(HttpRequest request) {
                request.setParser(new JsonObjectParser(JSON_FACTORY));
            }
        });

    private SharedBuffer<GameLog> gameLogBuffer;

    public SlackThread(SharedBuffer<GameLog> gameLogBuffer) {
        this.gameLogBuffer = gameLogBuffer;
    }

    @Override public void run() {
        int gameLogSendCount = Config.getNoOfThreads();

        try {
            while (!isInterrupted()) {
                sleep(1440 * 1000);
                List<GameLog> gameLogs = Collections.emptyList();
                gameLogs = gameLogBuffer.getEntityWhen(gameLogSendCount);

                sendList(gameLogs);
            }
        } catch (InterruptedException e) {}

        sendAll();
    }

    private void sendAll() {
        sendList(gameLogBuffer.getEntities());
    }

    private void sendList(List<GameLog> gameLogs) {
        if (gameLogs.isEmpty())
            return;

        int maxRounds = Config.getNoOfRounds();

        int wins = 0, looses = 0, crashes = 0;
        StringBuilder urlsWin = new StringBuilder();
        StringBuilder urlsLoose = new StringBuilder();
        StringBuilder urlsCrash = new StringBuilder();

        for (GameLog gameLog : gameLogs) {
            String url = null;
            try {
                url = String.format(" <%s>", URLEncoder.encode(gameLog.getGameURL(), "UTF-8"));
            } catch (UnsupportedEncodingException e) {}
            if (gameLog.isWin()) {
                wins++;
                urlsWin.append(url);
            } else if (gameLog.getRounds() >= maxRounds) {
                looses++;
                urlsLoose.append(url);
            } else {
                crashes++;
                urlsCrash.append(url);
            }
        }

        String msg = String.format(
            "payload={\"text\": \"Gestartet von: %s - Win-Rate: %d%% (Win: %d, Loose: %d, Crash: %d):\nGewonnen:%s\nVerloren:%s\nGecrasht:%s\"}",
            Config.getName(),
            (int)(((double)wins) / (wins + looses + crashes) * 100),
            wins, looses, crashes,
            urlsWin, urlsLoose, urlsCrash
        );

        try {
            ByteArrayContent content = new ByteArrayContent("application/x-www-form-urlencoded", msg.getBytes());
            HttpRequest request = REQUEST_FACTORY.buildPostRequest(Config.getSlackULR(), content);
            request.setReadTimeout(1200);
            request.setConnectTimeout(1200);
            request.execute();
        } catch (IOException e) {}
    }
}

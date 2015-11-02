package bot.Bender1;

import bot.Bender.BotMove;

/**
 * Created by slaan on 02.11.15.
 */
public class SimpleMine {

    private BotMove direction;
    private Quantity distance;

    public SimpleMine(BotMove direction, Quantity distance) {
        this.direction = direction;
        this.distance = distance;
    }

    public BotMove getDirection() {
        return direction;
    }

    public Quantity getDistance() {
        return distance;
    }
}

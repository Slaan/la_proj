package bot.Bender1;

import bot.Bender.BotMove;
import bot.Bender.DirectionType;

/**
 * Created by slaan on 02.11.15.
 */
public class SimpleMine {

    private DirectionType direction;
    private Distance distance;

    /**
     * Create a Mine that is out of sight.
     */
    public SimpleMine() {
        direction = DirectionType.NORTH;
        distance = Distance.OUTOFSIGHT;
    }

    public SimpleMine(DirectionType direction, int distance) {
        this.direction = direction;
        this.distance = SimplifiedGState1.calcDistance(distance);
    }

    public DirectionType getDirection() {
        return direction;
    }

    public Distance getDistance() {
        return distance;
    }
}

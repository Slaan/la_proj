package bot.Bender1;

import bot.Bender.DirectionType;

/**
 * Created by slaan on 02.11.15.
 */
public class SimpleTavern {

    private DirectionType direction;
    private Distance distance;

    public SimpleTavern(DirectionType direction, int distance) {
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

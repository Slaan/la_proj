package bot;

/**
 * Created by Daniel Hofmeister on 18.10.2015.
 */
public enum DirectionType {
    NORTH(0), SOUTH(1), WEST(2), EAST(3);

    private int value;
    private DirectionType(int value) { this.value = value; }
    public int getValue() { return value; }
}

package bot.Bender;

/**
 * Created by Daniel Hofmeister on 18.10.2015.
 */
public enum DirectionType {
    NORTH(0), SOUTH(1), WEST(2), EAST(3);

    private int value;
    private DirectionType(int value) { this.value = value; }
    /**
     * "Casts" this DirectionType to an integer.
     * @return
     */
    public int getValue() { return value; }

    /**
     * "Casts" an integer to a DirectionType.
     * @param value Value retrieved by "getValue".
     */
    public static DirectionType fromValue(int value) {
        switch (value) {
            case 0: return NORTH;
            case 1: return SOUTH;
            case 2: return WEST;
            case 3: return EAST;
            default: throw new RuntimeException("DirectionType " + value + " does not exist.");
        }
    }
}

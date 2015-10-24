package bot;

/**
 * Created by Daniel Hofmeister on 18.10.2015.
 */
public enum TileType {

    BLOCKED(0), MINE(1), TAVERN(2), FREE(3);

    private int value;
    private TileType(int value) { this.value = value; }

    /**
     * "Casts" this TileType to an integer.
     * @return
     */
    public int getValue() { return value; }

    /**
     * "Casts" an integer to a TileType.
     * @param value Value retrieved by "getValue".
     */
    public static TileType fromValue(int value) {
        switch (value) {
            case 0: return BLOCKED;
            case 1: return MINE;
            case 2: return TAVERN;
            case 3: return FREE;
            default: throw new RuntimeException("TileType " + value + " does not exist.");
        }
    }

    /**
     * Gets a one character long abbreviation for this TileType.
     */
    public char getAbbreviation() {
        switch (this) {
            case BLOCKED: return 'B';
            case MINE: return 'M';
            case TAVERN: return 'T';
            case FREE: return ' ';
            default: throw new RuntimeException("TileType has no Abbreviation: " + this);
        }
    }
}

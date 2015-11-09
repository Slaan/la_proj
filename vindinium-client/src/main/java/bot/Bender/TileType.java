package bot.Bender;

import bot.dto.GameState;

/**
 * Created by Daniel Hofmeister on 18.10.2015.
 */
public enum TileType {

    BLOCKED(0), MINE(1), TAVERN(2), FREE(3), HERO1(4), HERO2(5), HERO3(6), HERO4(7);

    private int value;
    private TileType(int value) { this.value = value; }

    /**
     * "Casts" this TileType to an integer.
     * @return
     */
    public int getValue() { return value; }

    /**
     * "Casts" this TileType to an integer compatible with Bender0.
     * @return
     */
    public int getValueB0() {
        if (isHero())
            return BLOCKED.getValueB0();
        return this.getValue();
    }

    public boolean isHero() {
        return (this == HERO1 || this == HERO2 || this == HERO3 || this == HERO4);
    }

    /**
     * Returns the Hero ID.
     * @return
     */
    public int getHero() {
        switch (this) {
            case HERO1: return 1;
            case HERO2: return 2;
            case HERO3: return 3;
            case HERO4: return 4;
            default: throw new RuntimeException("Can't access getHero if the entity isn't a Hero.");
        }
    }

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
            case 4: return HERO1;
            case 5: return HERO2;
            case 6: return HERO3;
            case 7: return HERO4;

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
            case HERO1: return '1';
            case HERO2: return '2';
            case HERO3: return '3';
            case HERO4: return '4';

            default: throw new RuntimeException("TileType has no Abbreviation: " + this);
        }
    }
}

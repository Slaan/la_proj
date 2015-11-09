package bot.Bender1;

import bot.Bender0.RewardConfig;

/**
 * Created by beckf on 06.11.2015.
 */
public enum Distance {
    BESIDE(0), CLOSE(1), MEDIUM(2), FAR(3), OUTOFSIGHT(4);

    private int value;
    Distance(int value) { this.value = value; }
    public int getValue() { return value; }

    public static Distance fromValue(int value) {
        switch (value) {
            case 0: return BESIDE;
            case 1: return CLOSE;
            case 2: return MEDIUM;
            case 3: return FAR;
            case 4: return OUTOFSIGHT;
            default: throw new RuntimeException("Tried to get Distance from not supported value: " + value);
        }
    }
}

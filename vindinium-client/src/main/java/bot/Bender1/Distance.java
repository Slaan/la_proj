package bot.Bender1;

import bot.Bender0.RewardConfig;

/**
 * Created by beckf on 06.11.2015.
 */
public enum Distance {
    BESIDE(0), CLOSE(1), MEDIUM(2), FAR(3);

    private int value;
    Distance(int value) { this.value = value; }
    public int getValue() { return value; }

    public static Distance fromValue(int value) {
        switch (value) {
            case 0: return BESIDE;
            case 1: return CLOSE;
            case 2: return MEDIUM;
            case 3: return FAR;
            default: throw new RuntimeException("Tried to get Distance from not supported value: " + value);
        }
    }

    public static Distance calcDistance(int distance){
        if(distance == 1){
            return BESIDE;
        } else if (distance < RewardConfig.getDistantClose()){
            return CLOSE;
        } else if (distance < RewardConfig.getDistantFar()){
            return MEDIUM;
        } else {
            return FAR;
        }
    }
}

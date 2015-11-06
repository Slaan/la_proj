package bot.Bender1;

import bot.RewardConfig;

/**
 * Created by beckf on 06.11.2015.
 */
public enum Distance {
    BESIDE, CLOSE, MEDIUM, FAR;

    public static Distance calcDistance(int dinstance){
        if(dinstance == 1){
            return BESIDE;
        } else if (dinstance < RewardConfig.getDistantClose()){
            return CLOSE;
        } else if (dinstance < RewardConfig.getDistantFar()){
            return MEDIUM;
        } else {
            return FAR;
        }
    }
}

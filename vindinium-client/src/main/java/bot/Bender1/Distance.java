package bot.Bender1;

import bot.Bender0.RewardConfig;

/**
 * Created by beckf on 06.11.2015.
 */
public enum Distance {
    BESIDE, CLOSE, MEDIUM, FAR;

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

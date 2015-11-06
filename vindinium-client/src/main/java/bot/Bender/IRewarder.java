package bot.Bender;

import bot.Bender0.SimplifiedGState;

/**
 * Created by slaan on 02.11.15.
 */
public interface IRewarder {


    /** This function is awesome
     *
     * @param state New GameState
     * @return Reward the bot receives
     */
    public int calculateReward(ISimplifiedGState state);
}

package bot.bender;

/**
 * Created by slaan on 02.11.15.
 */


/**
 * The classes implementing this Interface will be used to calculate rewards given to the bot
 * based on Simplified States.
 */
public interface IRewarder {


    /** This function is awesome
     *
     * @param state New GameState
     * @return Reward the bot receives
     */
    public int calculateReward(ISimplifiedGState state);

    public void setLastMove(BotMove move);
}

package bot.Bender;

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

    public void setLastMove(BotMove move);
}

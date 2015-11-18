package bot.Bender0;

import bot.Bender.Bender;
import bot.Bender.IRewarder;
import bot.Bender.ISimplifiedGState;
import persistence.GameLog;
import persistence.ManageState;

/**
 * Created by octavian on 19.10.15.
 */
public class Bender0 extends Bender {

    public Bender0(ManageState manageState, GameLog gameLog) {
        super(manageState, gameLog);
    }

    @Override
    protected ISimplifiedGState getSimplifiedGState() {
        return new SimplifiedGState();
    }

    @Override
    protected IRewarder getRewarder(GameLog gameLog) {
        return new Rewarder(gameLog);
    }
}

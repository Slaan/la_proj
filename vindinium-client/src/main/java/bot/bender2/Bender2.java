package bot.bender2;

import bot.Bender.Bender;
import bot.Bender.IRewarder;
import bot.Bender.ISimplifiedGState;
import bot.Bender0.Rewarder;
import bot.Bender0.SimplifiedGState;
import persistence.GameLog;
import persistence.ManageState;

/**
 * Created by octavian on 19.10.15.
 */
public class Bender2 extends Bender {

    public Bender2(ManageState manageState, GameLog gameLog) {
        super(manageState, gameLog);
    }

    @Override
    protected ISimplifiedGState getSimplifiedGState() {
        return new SimplifiedGState2();
    }

    @Override
    protected IRewarder getRewarder(GameLog gameLog) {
        return new RewarderBender2(gameLog);
    }
}

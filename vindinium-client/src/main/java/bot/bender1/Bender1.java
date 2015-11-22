package bot.bender1;

import bot.bender.Bender;
import bot.bender.IRewarder;
import bot.bender.ISimplifiedGState;
import persistence.GameLog;
import persistence.ManageState;

/**
 * Created by octavian on 19.10.15.
 */
public class Bender1 extends Bender {

    public Bender1(ManageState manageState, GameLog gameLog) {
        super(manageState, gameLog);
    }

    @Override
    protected ISimplifiedGState getSimplifiedGState() {
        return new SimplifiedGState1();
    }

    @Override
    protected IRewarder getRewarder(GameLog gameLog) {
        return new RewarderBender1(gameLog);
    }
}

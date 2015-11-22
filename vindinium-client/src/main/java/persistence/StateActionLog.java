package persistence;

import bot.bender.BotMove;

import javax.persistence.*;

/**
 * Created by beckf on 05.11.2015.
 */


@Entity
@Table(name = "StateActionLog")
public class StateActionLog {
    @Id @GeneratedValue
    @Column(name = "stateActionIDLog")
    private int stateActionIDLog;
    @Column(name = "stateActionID")
    private int stateActionID;
    @JoinColumn(name = "state")
    private int state;
    @Column(name = "description")
    private String description;
    @Column(name = "action")
    private BotMove action;
    @Column(name = "qValue")
    private double qValue;
    @Column(name = "used")
    private int used;

    private StateActionLog(){}
    protected StateActionLog(StateAction stateAction){
        this.stateActionID = stateAction.getStateActionID();
        this.state = stateAction.getState().getStateId();
        this.description = stateAction.getDescription();
        this.action = stateAction.getAction();
        this.qValue = stateAction.getQValue();
        this.used = stateAction.getUsed();
    }

    public int getStateActionIDLog() {
        return stateActionIDLog;
    }

    public void setStateActionIDLog(int stateActionIDLog) {
        this.stateActionIDLog = stateActionIDLog;
    }

    public int getStateActionID() {
        return stateActionID;
    }

    public void setStateActionID(int stateActionID) {
        this.stateActionID = stateActionID;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BotMove getAction() {
        return action;
    }

    public void setAction(BotMove action) {
        this.action = action;
    }

    public double getqValue() {
        return qValue;
    }

    public void setqValue(double qValue) {
        this.qValue = qValue;
    }

    public int getUsed() {
        return used;
    }

    public void setUsed(int used) {
        this.used = used;
    }
}

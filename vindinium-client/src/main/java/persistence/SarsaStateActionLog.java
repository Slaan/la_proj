package persistence;

import bot.Bender.BotMove;

import javax.persistence.*;

/**
 * Created by beckf on 05.11.2015.
 */


@Entity
@Table(name = "SarsaStateActionLog")
public class SarsaStateActionLog {
    @Id @GeneratedValue
    @Column(name = "sarsaStateActionIDLog")
    private int sarsaStateActionIDLog;
    @Column(name = "sarsaStateActionID")
    private int sarsaStateActionID;
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

    private SarsaStateActionLog(){}
    protected SarsaStateActionLog(SarsaStateAction sarsaStateAction){
        this.sarsaStateActionID = sarsaStateAction.getSarsaStateActionID();
        this.state = sarsaStateAction.getState().getgStateId();
        this.description = sarsaStateAction.getDescription();
        this.action = sarsaStateAction.getAction();
        this.qValue = sarsaStateAction.getQValue();
        this.used = sarsaStateAction.getUsed();
    }

    public int getSarsaStateActionIDLog() {
        return sarsaStateActionIDLog;
    }

    public void setSarsaStateActionIDLog(int sarsaStateActionIDLog) {
        this.sarsaStateActionIDLog = sarsaStateActionIDLog;
    }

    public int getSarsaStateActionID() {
        return sarsaStateActionID;
    }

    public void setSarsaStateActionID(int sarsaStateActionID) {
        this.sarsaStateActionID = sarsaStateActionID;
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

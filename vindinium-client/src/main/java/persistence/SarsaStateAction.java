package persistence;

import bot.BotMove;

import javax.persistence.*;

/**
 * Created by beckf on 17.10.2015.
 */
@Entity
@Table(name = "SarsaStateAction")
public class SarsaStateAction {
    @Id @GeneratedValue
    @Column(name = "sarsaStateActionID")
    private int sarsaStateActionID;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "state")
    private SarsaState state;
    @Column(name = "description")
    private String description;
    @Column(name = "action")
    private BotMove action;
    @Column(name = "qValue")
    private double qValue;

    private SarsaStateAction(){}
    protected SarsaStateAction(SarsaState state, String description, BotMove action, double qValue){
        this.state = state;
        this.description = description;
        this.action = action;
        this.qValue = qValue;
    }

    public int getSarsaStateActionID() {
        return sarsaStateActionID;
    }
    public SarsaState getState() {
        return state;
    }
    public String getDescription() {
        return description;
    }
    public BotMove getAction() { return action; }
    public double getqValue() {
        return qValue;
    }

    public void setSarsaStateActionID(int sarsaStateActionID) {
        this.sarsaStateActionID = sarsaStateActionID;
    }
    public void setState(SarsaState state) {
        this.state = state;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public void setAction(BotMove action) {
        this.action = action;
    }
    public void setqValue(double qValue) {
        this.qValue = qValue;
    }

    public double getQValue(){
        return qValue;
    }

    public void updateQValue(double additionQValue){ qValue += additionQValue; }

    @Override public String toString() {
        return this.getClass().getName() + "<Feld ist:\"" + description + "\" - " + action.toString() + ": " + qValue + ">";
    }
}

package persistence;

import bot.Bender.BotMove;

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
    @Column(name = "used")
    private int used;

    private SarsaStateAction(){}
    protected SarsaStateAction(SarsaState state, String description, BotMove action){
        this.state = state;
        this.description = description;
        this.action = action;
        this.qValue = 0;
        this.used = 0;
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
    public double getQValue(){
        return qValue;
    }
    public int getUsed() { return used; }

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
    public void setUsed(int used) {this.used = used; }

    public synchronized void updateQValue(double additionQValue){ qValue += additionQValue; }

    public synchronized void addUse() { used++; }
    public void updateUsed(int additonUsed) { used += additonUsed; }

    @Override public String toString() {
        return this.getClass().getName() + "<Feld ist:\"" + description + "\" - " + action.toString() + ": " + qValue + ">";
    }
}

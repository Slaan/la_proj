package persistence;

import bot.bender.BotMove;

import javax.persistence.*;

/**
 * Created by beckf on 17.10.2015.
 */
@Entity
@Table(name = "StateAction")
public class StateAction {
    @Id @GeneratedValue
    @Column(name = "stateActionID")
    private int stateActionID;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "state")
    private State state;
    @Column(name = "description")
    private String description;
    @Column(name = "action")
    private BotMove action;
    @Column(name = "qValue")
    private double qValue;
    @Column(name = "used")
    private int used;
    @Column(name = "explored")
    private int exploread;
    @Column(name = "bestAktion")
    private int bestAktion;

    private StateAction(){}
    protected StateAction(State state, String description, BotMove action){
        this.state = state;
        this.description = description;
        this.action = action;
        this.qValue = 0;
        this.used = 0;
        this.exploread = 0;
        this.bestAktion = 0;
    }

    public int getStateActionID() {
        return stateActionID;
    }
    public State getState() {
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
    public int getBestAktion() {
        return bestAktion;
    }
    public int getExploread() {
        return exploread;
    }

    public void setStateActionID(int stateActionID) {
        this.stateActionID = stateActionID;
    }
    public void setState(State state) {
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

    private synchronized void addUse() { used++; }
    public synchronized void addExplored() {
        exploread++;
        addUse();
    }
    public synchronized void addBestAction() {
        bestAktion++;
        addUse();
    }
    public void updateUsed(int additonUsed) { used += additonUsed; }

    @Override public String toString() {
        return this.getClass().getName() + "<Feld ist:\"" + description + "\" - " + action.toString() + ": " + qValue + ">";
    }
}

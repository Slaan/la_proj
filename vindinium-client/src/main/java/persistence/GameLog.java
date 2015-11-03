package persistence;

import javax.persistence.*;

/**
 * Created by slaan on 26.10.15.
 */
@Entity
@Table(name = "GameLog")
public class GameLog {
    @Id @GeneratedValue
    @Column(name = "gameID")
    private int gameID;
    @Column(name = "gameURL")
    private String gameURL;
    @Column(name = "win")
    private boolean win;
    @Column(name = "rounds")
    private int rounds;
    @Column(name = "tavern")
    private int tavern;
    @Column(name = "mine")
    private int mine;
    @Column(name = "deaths")
    private int deaths;
    @Column(name = "kills")
    private int kills;
    @Column(name = "blockedWay")
    private int blockedWay;

    protected GameLog(){
        this.gameURL ="";
        this.win = false;
        this.rounds = 0;
        this.tavern = 0;
        this.mine = 0;
        this.deaths = 0;
        this.kills = 0;
        this.blockedWay = 0;
    }

    public int getGameID() {
        return gameID;
    }

    public void setGameID(int gameID) {
        this.gameID = gameID;
    }

    public String getGameURL() { return gameURL; }

    public void setGameURL(String gameURL){ this.gameURL = gameURL; }

    public boolean isWin() {
        return win;
    }

    public void setWin(boolean win) {
        this.win = win;
    }

    public int getRounds() {
        return rounds;
    }

    public void setRounds(int rounds) {
        this.rounds = rounds;
    }

    public void addRound() { this.rounds++; }

    public int getTavern() {
        return tavern;
    }

    public void setTavern(int tavern) {
        this.tavern = tavern;
    }

    public void addTavern() { tavern++; }

    public int getMine() {
        return mine;
    }

    public void setMine(int mine) {
        this.mine = mine;
    }

    public void addMine() { mine++; }

    public int getDeaths() {
        return deaths;
    }

    public void setDeaths(int deaths) {
        this.deaths = deaths;
    }

    public void addDeath() { deaths++; }

    public int getKills() {
        return kills;
    }

    public void setKills(int kills) {
        this.kills = kills;
    }

    public void addKill() { kills++; }

    public int getBlockedWay() {
        return blockedWay;
    }

    public void setBlockedWay(int blockedWay) {
        this.blockedWay = blockedWay;
    }

    public void addBlockedWay() { blockedWay++; }
}

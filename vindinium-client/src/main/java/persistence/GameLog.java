package persistence;

/**
 * Created by slaan on 26.10.15.
 */
public class GameLog {

    public int gameID;

    public boolean win;

    public int rounds;

    public int tavern;

    public int mine;

    public int deaths;

    public int kills;

    public int blockedWay;

    public GameLog(){
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

    public int getTavern() {
        return tavern;
    }

    public void setTavern(int tavern) {
        this.tavern = tavern;
    }

    public int getMine() {
        return mine;
    }

    public void setMine(int mine) {
        this.mine = mine;
    }

    public int getDeaths() {
        return deaths;
    }

    public void setDeaths(int deaths) {
        this.deaths = deaths;
    }

    public int getKills() {
        return kills;
    }

    public void setKills(int kills) {
        this.kills = kills;
    }

    public int getBlockedWay() {
        return blockedWay;
    }

    public void setBlockedWay(int blockedWay) {
        this.blockedWay = blockedWay;
    }
}

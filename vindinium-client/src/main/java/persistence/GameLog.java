package persistence;

import javax.persistence.*;
import java.util.Date;

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
    @Column(name = "WhoAmI")
    private int whoAmI;
    @Column(name = "win")
    private boolean win;
    @Column(name = "rounds")
    private int rounds;
    @Column(name = "tavern")
    private int tavern;
    @Column(name = "mine")
    private int mine;
    @Column(name = "deathByEnemy")
    private int deathByEnemy;
    @Column(name = "deatbByMine")
    private int deatbByMine;
    @Column(name = "kills")
    private int kills;
    @Column(name = "blockedWay")
    private int blockedWay;
    @Column(name = "startingTime")
    private Date startingTime;
    @Column(name = "crashed")
    private boolean crashed;
    @Column(name = "hero1")
    private String hero1;
    @Column(name = "goldHero1")
    private int goldHero1;
    @Column(name = "hero2")
    private String hero2;
    @Column(name = "goldHero2")
    private int goldHero2;
    @Column(name = "hero3")
    private String hero3;
    @Column(name = "goldHero3")
    private int goldHero3;
    @Column(name = "hero4")
    private String hero4;
    @Column(name = "goldHero4")
    private int goldHero4;
    @Column(name = "reward")
    private int reward;
    @Column(name = "bigestReward")
    private int bigestReward;
    @Column(name = "bigestRewardRound")
    private int bigestRewardRound;
    @Column(name = "smalestReward")
    private int smalestReward;
    @Column(name = "smalestRewardRound")
    private int smalestRewardRound;


    protected GameLog(){
        this.gameURL ="";
        this.win = false;
        this.crashed = false;
        this.whoAmI = 5;
        this.rounds = 0;
        this.tavern = 0;
        this.mine = 0;
        this.deatbByMine = 0;
        this.deathByEnemy = 0;
        this.kills = 0;
        this.blockedWay = 0;
        this.startingTime = new Date();
        this.hero1 = "";
        this.goldHero1 = 0;
        this.hero2 = "";
        this.goldHero2 = 0;
        this.hero3 = "";
        this.goldHero3 = 0;
        this.hero4 = "";
        this.goldHero4 = 0;
        this.reward = 0;
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

    public int getDeatbByMine() {
        return deatbByMine;
    }

    public void setDeatbByMine(int deatbByMine) {
        this.deatbByMine = deatbByMine;
    }

    public int getDeathByEnemy() {
        return deathByEnemy;
    }

    public void setDeathByEnemy(int deathByEnemy) {
        this.deathByEnemy = deathByEnemy;
    }

    public void addDeathByEnemy() { deathByEnemy++; }

    public void addDeatbByMine() { deatbByMine++; }

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

    public boolean isCrashed() {
        return crashed;
    }

    public void setCrashed(boolean crashed) {
        this.crashed = crashed;
    }

    public int getWhoAmI() {
        return whoAmI;
    }

    public void setWhoAmI(int whoAmI) {
        this.whoAmI = whoAmI;
    }

    public void setHeroForPlace(int place, String hero, int gold){
        if(place == 1){
            setHero1(hero);
            setGoldHero1(gold);
        } else if (place == 2) {
            setHero2(hero);
            setGoldHero2(gold);
        } else if (place == 3) {
            setHero3(hero);
            setGoldHero3(gold);
        } else if (place == 4){
            setHero4(hero);
            setGoldHero4(gold);
        }
    }

    public void setHero1(String hero1) {
        this.hero1 = hero1;
    }

    public void setGoldHero1(int goldHero1) {
        this.goldHero1 = goldHero1;
    }

    public void setHero2(String hero2) {
        this.hero2 = hero2;
    }

    public void setGoldHero2(int goldHero2) {
        this.goldHero2 = goldHero2;
    }

    public void setHero3(String hero3) {
        this.hero3 = hero3;
    }

    public void setGoldHero3(int goldHero3) {
        this.goldHero3 = goldHero3;
    }

    public void setHero4(String hero4) {
        this.hero4 = hero4;
    }

    public void setGoldHero4(int goldHero4) {
        this.goldHero4 = goldHero4;
    }

    public void setReward(int reward) {
        this.reward = reward;
    }

    public void addReward(int reward) {
        this.reward += reward;
    }

    public void setBigestReward(int bigestReward) {
        this.bigestReward = bigestReward;
    }

    public void setBigestRewardRound(int bigestRewardRound) {
        this.bigestRewardRound = bigestRewardRound;
    }

    public void setSmalestReward(int smalestReward) {
        this.smalestReward = smalestReward;
    }

    public void setSmalestRewardRound(int smalestRewardRound) {
        this.smalestRewardRound = smalestRewardRound;
    }

    public int getBigestReward() {
        return bigestReward;
    }

    public int getSmalestReward() {
        return smalestReward;
    }
}

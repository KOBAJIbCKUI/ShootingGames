package com.KOBAJIbCKUI.ShootingBattles.battle;

import com.KOBAJIbCKUI.ShootingBattles.lobby.LobbyStatus;
import com.KOBAJIbCKUI.ShootingBattles.events.*;
import com.KOBAJIbCKUI.ShootingBattles.lobby.Lobby;
import com.KOBAJIbCKUI.ShootingBattles.tasks.*;
import com.KOBAJIbCKUI.ShootingBattles.lobby.ShootingMap;
import com.KOBAJIbCKUI.ShootingBattles.ShootingGames;
import com.KOBAJIbCKUI.ShootingBattles.tasks.GlowingTask;
import org.bukkit.*;

import java.util.List;
import java.util.UUID;

public class ShootingBattle {
    private final ShootingGames shootingGames;

    private final Lobby lobby;
    private final ShootingMap shootingMap;
    private final ShootingMap gulagMap;
    private final BattlePlayerData battlePlayerData;
    private final BattleResultData battleResultData;

    private boolean isInGulag;

    private GlowingTask glowerTask;
    private CountdownTask countdownTask;
    private TimerTask timerTask;

    public ShootingBattle(ShootingGames shootingGames, Lobby lobby, ShootingMap shootingMap) {
        this.shootingGames = shootingGames;
        this.lobby = lobby;
        this.battlePlayerData = new BattlePlayerData(this);
        this.battleResultData = new BattleResultData(this);
        this.shootingMap = shootingMap;
        this.gulagMap = lobby.getGulagMap();
        this.isInGulag = false;
    }

    public BattlePlayerData getBattlePlayerData() {
        return battlePlayerData;
    }

    public BattleResultData getBattleResultData() {
        return battleResultData;
    }

    public ShootingMap getShootingMap() {
        return shootingMap;
    }

    public ShootingMap getGulagMap() {
        return gulagMap;
    }

    public Lobby getLobby() {
        return lobby;
    }

    public boolean isInGulag() {
        return isInGulag;
    }

    public void setIsInGulag(boolean isInGulag) {
        this.isInGulag = isInGulag;
    }

    public GlowingTask getGlowerTask() {
        return this.glowerTask;
    }

    public CountdownTask getCountdownTask() {
        return this.countdownTask;
    }

    public TimerTask getTimerTask() {
        return this.timerTask;
    }

    public void startCountdown() {
        Bukkit.getPluginManager().callEvent(new StartCountdownEvent(this));

        lobby.setStatus(LobbyStatus.COUNTDOWN);
        lobby.setCurrentBattle(this);
        lobby.getBarData().prepareForCountdown();
        lobby.getBoard().createBattleTeams();
        lobby.getBoard().hideTagsForBattleTeams();
        countdownTask = new CountdownTask(this);
    }

    public void startBattle() {
        Bukkit.getPluginManager().callEvent(new StartBattleEvent(this));
        lobby.setStatus(LobbyStatus.RUNNING);
        lobby.getBarData().prepareForBattle();
        timerTask = new TimerTask(this, lobby.getTimeToEnd(), lobby.getTimeToTags());
        if (lobby.isGlowCampers()) {
            glowerTask = new GlowingTask(this);
        }
    }

    public void startGulag() {
        Bukkit.getPluginManager().callEvent(new StartGulagEvent(this));
        lobby.setStatus(LobbyStatus.GULAG);
        lobby.getBarData().prepareForGulag(true);
    }

    public void endBattle() {
        stopTasks();
        Bukkit.getPluginManager().callEvent(new EndBattleEvent(this));
        lobby.getBoard().revealTagsForBattleTeams();
        lobby.getBoard().removeBattleTeams();
        lobby.getBarData().resetBar();
        lobby.setCurrentBattle(null);
    }

    public void stopBattle() {
        stopTasks();
        Bukkit.getPluginManager().callEvent(new StopBattleEvent(this));
        lobby.getBoard().revealTagsForBattleTeams();
        lobby.getBoard().removeBattleTeams();
        lobby.getBarData().resetBar();
        lobby.setCurrentBattle(null);
    }

    public void checkEndConditions() {

        List<UUID> players = battlePlayerData.getPlayers();

        if (players.size() == 0) {
            endBattle();
            return;
        }

        if (!lobby.getBoard().areInOneBattleTeam(players)) {
            return;
        }
        battleResultData.setWinners(lobby.getBoard().findBattleTeam(players.get(0)).getEntries());
        endBattle();
    }

    public void stopGlowerTask() {
        if (glowerTask != null) {
            glowerTask.stop();
        }
    }


    public void stopTasks() {

        stopGlowerTask();

        if (countdownTask != null) {
            countdownTask.stop();
        }

        if (timerTask != null) {
            timerTask.stop();
        }
    }

}

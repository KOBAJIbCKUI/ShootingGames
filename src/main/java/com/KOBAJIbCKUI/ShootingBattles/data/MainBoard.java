package com.KOBAJIbCKUI.ShootingBattles.data;

import com.KOBAJIbCKUI.ShootingBattles.ShootingGames;
import com.KOBAJIbCKUI.ShootingBattles.lobby.Lobby;
import com.KOBAJIbCKUI.ShootingBattles.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import java.util.List;

public class MainBoard {
    private final ShootingGames shootingGames;
    private final Scoreboard scoreboard;
    private final List<Lobby> lobbies;

    private Objective lobbiesObjective;
    private boolean lobbiesChanged;

    public MainBoard(ShootingGames shootingGames) {
        this.shootingGames = shootingGames;
        this.lobbies = shootingGames.getLobbiesManager().getLobbies();
        this.scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
        Util.log("Main board created");
        updateLobbies();
    }

    public void updateLobbies() {
        lobbiesChanged = true;
        updateVisual();
    }

    public void updatePlayers() {
        updateVisual();
    }

    public void updateVisual() {
        if (lobbiesChanged) {
            if ((this.lobbiesObjective = this.scoreboard.getObjective("Lobbies")) != null) {
                this.lobbiesObjective.unregister();
                Util.log("Objective lobbies unregistered");
            }
            this.lobbiesObjective = this.scoreboard.registerNewObjective("Lobbies", "dummy");
            this.lobbiesObjective.setDisplaySlot(DisplaySlot.SIDEBAR);
            this.lobbiesChanged = false;
            Util.log("Objective lobbies successfully re-registered");
        }
        for (Lobby lobby : lobbies) {
            lobbiesObjective.getScore("" + ChatColor.GREEN + lobby.getName()).setScore(lobby.playersQuantity());
            Util.log("Score for " + lobby.getName() + " set to " + lobby.playersQuantity());
        }
    }
}

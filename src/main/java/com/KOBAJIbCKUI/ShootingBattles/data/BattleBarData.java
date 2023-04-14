package com.KOBAJIbCKUI.ShootingBattles.data;

import com.KOBAJIbCKUI.ShootingBattles.lobby.Lobby;
import com.KOBAJIbCKUI.ShootingBattles.lobby.LobbyStatus;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;

import java.util.UUID;

public class BattleBarData {
    private final Lobby lobby;
    private final BossBar bar;
    private final String title;

    public BattleBarData(Lobby lobby) {
        this.lobby = lobby;
        this.title = "" +ChatColor.WHITE  + ChatColor.BOLD + "Lobby - "
                + ChatColor.GREEN + ChatColor.BOLD + lobby.getName()
                + ChatColor.DARK_PURPLE + ChatColor.BOLD +  " | "
                + ChatColor.WHITE + ChatColor.BOLD + "Status - ";
        this.bar = Bukkit.createBossBar(title + lobby.getStatus().getName(), BarColor.PURPLE, BarStyle.SOLID);
        this.bar.setProgress(1.0);
    }

    public void prepareForCountdown() {
        setBarStatus(LobbyStatus.COUNTDOWN);
        setBarColor(BarColor.GREEN);
    }

    public void prepareForBattle() {
        setBarStatus(LobbyStatus.RUNNING);
    }

    public void prepareForTags() {
        setBarColor(BarColor.YELLOW);
    }

    public void prepareForGulag(boolean isTeleportToGulag) {
        if (isTeleportToGulag) {
            setBarStatus(LobbyStatus.GULAG);
        }
        setBarColor(BarColor.RED);
    }

    public void resetBar() {
        setBarStatus(LobbyStatus.READY);
        setBarColor(BarColor.PURPLE);
        bar.setProgress(1.0);
    }

    public void setBarStatus(LobbyStatus status) {
        bar.setTitle(title + status.getName());
    }

    public void setBarColor(BarColor barColor) {
        bar.setColor(barColor);
    }

    public void addPlayerToBar(UUID uuid) {
        addPlayerToBar(Bukkit.getPlayer(uuid));
    }

    public void addPlayerToBar(Player player) {
        bar.addPlayer(player);
    }

    public void removePlayerFromBar(UUID uuid) {
        removePlayerFromBar(Bukkit.getPlayer(uuid));
    }

    public void removePlayerFromBar(Player player) {
        bar.removePlayer(player);
    }

    public void updateBar(int remaining) {
        if (lobby.getCurrentBattle() != null) {
            bar.setProgress(((double) remaining) / ((double) lobby.getTimeToEnd() * 60));
            if (remaining == (lobby.getTimeToEnd() - lobby.getTimeToTags()) * 60) {
                prepareForTags();
            }
        }
    }
}

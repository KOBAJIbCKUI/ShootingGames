package com.KOBAJIbCKUI.ShootingBattles.lobby;

import com.KOBAJIbCKUI.ShootingBattles.Coordinates;
import com.KOBAJIbCKUI.ShootingBattles.ShootingGames;
import com.KOBAJIbCKUI.ShootingBattles.buffers.Cube;
import com.KOBAJIbCKUI.ShootingBattles.buffers.PlayerGlower;
import com.KOBAJIbCKUI.ShootingBattles.events.EndBattleEvent;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.Team;

import java.util.*;

public class ShootingBattle {
    ShootingGames shootingGames;
    private final List<Player> playersList;
    private final Set<Player> spectators = new HashSet<>();
    public final ShootingMap shootingMap;
    public final Lobby lobby;

    private Integer taskGlowerId = null;

    private BukkitTask taskFor10Minutes;
    private BukkitTask taskFor5Minutes;
    private BukkitTask taskFor1Minute;
    private BukkitTask taskForOver;

    private ShootingBattle(ShootingGames shootingGames, Lobby lobby, List<Player> playersList, ShootingMap shootingMap) {
        this.shootingGames = shootingGames;
        this.playersList = playersList;
        this.shootingMap = shootingMap;
        this.lobby = lobby;
    }

    public static void createBattle(ShootingGames shootingGames, Lobby lobby, List<Player> players, ShootingMap shootingMap) {
        lobby.isInBattle = true;
        lobby.currentBattle = new ShootingBattle(shootingGames, lobby, players, shootingMap);
        lobby.currentBattle.startTimers();

        ShootingGames.DEATHMATCH_TEAM.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.FOR_OTHER_TEAMS);
        for (Player player : players) {
            player.setHealth(20.0);
            player.setFoodLevel(20);
            player.setGameMode(GameMode.SURVIVAL);

            ShootingGames.playersInBattles.put(player, lobby.currentBattle);
            ShootingGames.NEUTRAL_TEAM.removeEntry(player.getName());
            ShootingGames.DEATHMATCH_TEAM.addEntry(player.getName());

            if (ShootingGames.CAMPERS_GLOWING) {
                PlayerGlower.cubesList.add(new Cube(player));
            }

            player.sendTitle(ShootingGames.BATTLE_HAS_BEGUN, ShootingGames.MINUTES_TO_GULAG, 20, 60, 20);
        }

        PlayerBattleSpawner.spawnPlayers(players, shootingMap.getSpawnPoints());
    }

    public List<Player> getPlayersList() {
        return playersList;
    }

    public Set<Player> getSpectators() {
        return spectators;
    }

    public void startTimers () {
        if (ShootingGames.CAMPERS_GLOWING) {
            taskGlowerId = Bukkit.getScheduler().scheduleSyncRepeatingTask(shootingGames, new PlayerGlower(), 100, 100);
        }
        taskFor10Minutes = Bukkit.getScheduler().runTaskLater(shootingGames, new BattleOversIn10Minutes(playersList, spectators), 6000L);
        taskFor5Minutes = Bukkit.getScheduler().runTaskLater(shootingGames, new BattleOversIn5Minutes(playersList, spectators), 12000L);
        taskFor1Minute = Bukkit.getScheduler().runTaskLater(shootingGames, new BattleOversIn1Minute(playersList, spectators, lobby.getGulagMap().getSpawnPoints()), 16800L);
        taskForOver = Bukkit.getScheduler().runTaskLater(shootingGames, new BattleOver(this), 18000L);
    }

    public void stopTimers () {
        if (taskGlowerId != null) {
            Bukkit.getScheduler().cancelTask(taskGlowerId);
        }
        taskFor10Minutes.cancel();
        taskFor5Minutes.cancel();
        taskFor1Minute.cancel();
        taskForOver.cancel();
    }

    private static class BattleOversIn10Minutes implements Runnable {

        private List<Player> players;
        private Set<Player> spectators;

        public BattleOversIn10Minutes(List<Player> players, Set<Player> spectators) {
            this.players = players;
            this.spectators = spectators;
        }

        @Override
        public void run() {
            if (!Thread.currentThread().isInterrupted()) {
                for (Player player : players) {
                    player.sendMessage(ShootingGames.BATTLE_ENDS_IN_10);
                }
                for (Player player : spectators) {
                    player.sendMessage(ShootingGames.BATTLE_ENDS_IN_10);
                }
            }
        }
    }

    private class BattleOversIn5Minutes implements Runnable {

        private List<Player> players;
        private Set<Player> spectators;

        public BattleOversIn5Minutes(List<Player> players, Set<Player> spectators) {
            this.players = players;
            this.spectators = spectators;
        }

        @Override
        public void run() {
            if (!Thread.currentThread().isInterrupted()) {
                ShootingGames.DEATHMATCH_TEAM.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.ALWAYS);
                Bukkit.getScheduler().cancelTask(taskGlowerId);
                for (Player player : players) {
                    player.sendMessage(ShootingGames.BATTLE_ENDS_IN_5);
                    player.sendMessage(ShootingGames.NAME_TAG_VISIBLE);
                    player.setGlowing(true);
                }
                for (Player player : spectators) {
                    player.sendMessage(ShootingGames.BATTLE_ENDS_IN_5);
                    player.sendMessage(ShootingGames.NAME_TAG_VISIBLE);
                }
            }
        }
    }

    private static class BattleOversIn1Minute implements Runnable {

        private List<Player> players;
        private Set<Player> spectators;
        List<Coordinates> spawnPoints;

        public BattleOversIn1Minute(List<Player> players, Set<Player> spectators, List<Coordinates> spawnPoints) {
            this.players = players;
            this.spectators = spectators;
            this.spawnPoints = spawnPoints;
        }

        @Override
        public void run() {
            if (!Thread.currentThread().isInterrupted()) {
                PlayerBattleSpawner.spawnPlayers(players, spawnPoints);
                PlayerBattleSpawner.spawnSpectator(spectators, spawnPoints);
                for (Player player : players) {
                    player.sendMessage(ShootingGames.BATTLE_ENDS_IN_1);
                    player.sendTitle(ShootingGames.WELCOME_TO_GULAG, ShootingGames.TRAITORS, 20, 60, 20);
                }
                for (Player player : spectators) {
                    player.sendMessage(ShootingGames.BATTLE_ENDS_IN_1);
                }
            }
        }
    }

    private static class BattleOver implements Runnable {
        ShootingBattle shootingBattle;

        public BattleOver(ShootingBattle shootingBattle) {
            this.shootingBattle = shootingBattle;
        }

        @Override
        public void run() {
            Bukkit.getPluginManager().callEvent(new EndBattleEvent(shootingBattle));
            shootingBattle.stopTimers();
        }
    }

}

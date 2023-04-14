package com.KOBAJIbCKUI.ShootingBattles.data;

import com.KOBAJIbCKUI.ShootingBattles.ShootingGames;
import com.KOBAJIbCKUI.ShootingBattles.lobby.Lobby;
import com.KOBAJIbCKUI.ShootingBattles.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

import java.util.*;

public class Board {
    private final ShootingGames shootingGames;
    private final Lobby lobby;
    private final Scoreboard lobbyScoreboard;
    private final Scoreboard battleScoreboard;

    private final Map<ChatColor, Boolean> colors = new HashMap<>();
    private final Map<UUID, Integer> wins = new HashMap<>();
    private final Map<UUID, Integer> kills = new HashMap<>();
    private final Map<UUID, Integer> killstreaks = new HashMap<>();

    private boolean isWinsChanged;
    private boolean isKillsChanged;
    private boolean isKillstreaksChanged;

    //Objectives declaration
    private final Objective winsObjective;
    private final Objective killsObjective;

    public Board(Lobby lobby) {
        this.shootingGames = ShootingGames.getPlugin();
        this.lobby = lobby;
        this.lobbyScoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        this.battleScoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        this.winsObjective = lobbyScoreboard.registerNewObjective("Wins", "dummy");
        this.killsObjective = lobbyScoreboard.registerNewObjective("Kills", "dummy");

        this.winsObjective.setDisplaySlot(DisplaySlot.SIDEBAR);
        this.killsObjective.setDisplaySlot(DisplaySlot.PLAYER_LIST);

        colors.put(ChatColor.RED, false);
        colors.put(ChatColor.DARK_GREEN, false);
        colors.put(ChatColor.YELLOW, false);
        colors.put(ChatColor.DARK_BLUE, false);
        colors.put(ChatColor.GOLD, false);
        colors.put(ChatColor.AQUA, false);
        colors.put(ChatColor.DARK_RED, false);
        colors.put(ChatColor.GRAY, false);
        colors.put(ChatColor.DARK_PURPLE, false);
        colors.put(ChatColor.LIGHT_PURPLE, false);
    }

    public Team createLobbyTeam(String name) {
        Team team = lobbyScoreboard.registerNewTeam(name);
        setTeamColor(team);
        Util.log("Team " + name + " created");
        return team;
    }

    public Set<Team> getLobbyTeams() {
        return lobbyScoreboard.getTeams();
    }

    public boolean joinLobbyTeam(UUID uuid, String teamName) {
        return joinLobbyTeam(Bukkit.getPlayer(uuid), teamName);
    }

    public boolean joinLobbyTeam(Player player, String teamName) {
        try {
            Team lobbyTeam = findLobbyTeam(teamName);
            sendTeamMessage(lobbyTeam, player.getName() + " joined " + teamName + "'s team");
            lobbyTeam.addEntry(player.getName());
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public boolean leaveLobbyTeam(UUID uuid, String teamName) {
        return leaveLobbyTeam(Bukkit.getPlayer(uuid), teamName);
    }

    public boolean leaveLobbyTeam(Player player, String teamName) {
        try {
            Team lobbyTeam = findLobbyTeam(teamName);
            lobbyTeam.removeEntry(player.getName());
            if (teamName.equals(player.getName())) {
                dismissLobbyTeam(lobbyTeam);
            }
            sendTeamMessage(lobbyTeam, player.getName() + " left " + teamName + "'s team");
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public void setTeamColor(Team team) {
        for (Map.Entry<ChatColor, Boolean> entry : colors.entrySet()) {
            if (!entry.getValue()) {
                team.setPrefix(entry.getKey().toString());
                colors.put(entry.getKey(), true);
                Util.log("Color for team " + team.getName() + " set");
                break;
            }
        }
        for (Map.Entry<ChatColor, Boolean> entry : colors.entrySet()) {
            Util.log(entry.getKey().getChar() + entry.getValue().toString());
        }
    }

    public void dismissLobbyTeam(Team lobbyTeam) {
        colors.put(ChatColor.getByChar(lobbyTeam.getPrefix()), false);
        Team newLobbyTeam;
        for (String entryName : lobbyTeam.getEntries()) {
            newLobbyTeam = createLobbyTeam(entryName);
            newLobbyTeam.addEntry(entryName);
            Util.sendMessage(Bukkit.getPlayer(entryName), lobbyTeam.getName() + "'s team has been dismissed");
        }
        Util.log("Team " + lobbyTeam.getName() + " dismissed");
        lobbyTeam.unregister();
    }

    public Team findLobbyTeam(UUID uuid) {
        return findLobbyTeam(Bukkit.getPlayer(uuid));
    }

    public Team findLobbyTeam(Player player) {
        return lobbyScoreboard.getEntryTeam(player.getName());
    }

    public Team findLobbyTeam(String teamName) {
        return lobbyScoreboard.getTeam(teamName);
    }

    public void createBattleTeams() {
        for (Team lobbyTeam : lobbyScoreboard.getTeams()) {
            Team team = battleScoreboard.registerNewTeam(lobbyTeam.getName());
            team.setAllowFriendlyFire(false);
            team.setPrefix(lobbyTeam.getPrefix());
            for (String entryName : lobbyTeam.getEntries()) {
                team.addEntry(entryName);
            }
        }
    }

    public void removeBattleTeams() {
        for (Team battleTeam : battleScoreboard.getTeams()) {
            for (String entryName : battleTeam.getEntries()) {
                battleTeam.removeEntry(entryName);
            }
            battleTeam.unregister();
        }
    }

    public void setLobbyBoard(UUID uuid) {
        setLobbyBoard(Bukkit.getPlayer(uuid));
    }

    public void setLobbyBoard(Player player) {
        player.setScoreboard(lobbyScoreboard);
    }

    public void setBattleBoard(UUID uuid) {
        setBattleBoard(Bukkit.getPlayer(uuid));
    }

    public void setBattleBoard(Player player) {
        player.setScoreboard(battleScoreboard);
    }

    public Set<Team> getBattleTeams() {
        return battleScoreboard.getTeams();
    }

    public Team findBattleTeam(UUID uuid) {
        return findBattleTeam(Bukkit.getPlayer(uuid));
    }

    public Team findBattleTeam(Player player) {
        return battleScoreboard.getEntryTeam(player.getName());
    }

    public void hideTagsForBattleTeams() {
        for (Team team : battleScoreboard.getTeams()) {
            team.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.FOR_OTHER_TEAMS);
        }
    }

    public void revealTagsForBattleTeams() {
        for (Team team : battleScoreboard.getTeams()) {
            team.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.ALWAYS);
        }
    }

    public void sendTeamMessage(Team team, String message) {
        for (String entryName : team.getEntries()) {
            Util.sendMessage(Bukkit.getPlayer(entryName), message);
        }
    }

    public boolean areInOneBattleTeam(Collection<UUID> uuids) {

        Iterator<UUID> iterator = uuids.iterator();
        String prevTeamName = findBattleTeam(iterator.next()).getName();
        String currentTeamName;
        while (iterator.hasNext()) {
            currentTeamName = findBattleTeam(iterator.next()).getName();
            if (!prevTeamName.equals(currentTeamName)) {
                return false;
            }
            prevTeamName = currentTeamName;
        }
        return true;
    }

    public boolean areInOneLobbyBattleTeam(Collection<UUID> uuids) {
        Iterator<UUID> iterator = uuids.iterator();
        String prevTeamName = findLobbyTeam(iterator.next()).getName();
        String currentTeamName;
        while (iterator.hasNext()) {
            currentTeamName = findLobbyTeam(iterator.next()).getName();
            if (!prevTeamName.equals(currentTeamName)) {
                return false;
            }
            prevTeamName = currentTeamName;
        }
        return true;
    }

    //LOBBY BOARD OBJECTIVES

    public void createKillsEntry(UUID uuid) {
        kills.put(uuid, 0);
        isKillsChanged = true;
        updateVisual();
    }

    public void addKill(UUID uuid) {
        if (kills.containsKey(uuid)) {
            kills.put(uuid, kills.get(uuid) + 1);
            isKillsChanged = true;
            updateVisual();
        }
    }

    public void removeKillsEntry(UUID uuid) {
        kills.remove(uuid);
        isKillsChanged = true;
        updateVisual();
    }

    public void createWinsEntry(UUID uuid) {
        wins.put(uuid, 0);
        isWinsChanged = true;
        updateVisual();
    }

    public void addWin(UUID uuid) {
        if (wins.containsKey(uuid)) {
            wins.put(uuid, wins.get(uuid) + 1);
            isWinsChanged = true;
            updateVisual();
        }
    }

    public void removeWinsEntry(UUID uuid) {
        wins.remove(uuid);
        isWinsChanged = true;
        updateVisual();
    }

    public void createKillstreaksEntry(UUID uuid) {
        killstreaks.put(uuid, 0);
        isKillstreaksChanged = true;
        updateVisual();
    }

    public void incrementKillstreak(UUID uuid) {
        if (killstreaks.containsKey(uuid)) {
            killstreaks.put(uuid, killstreaks.get(uuid) + 1);
            isKillstreaksChanged = true;
            updateVisual();
        }
    }

    public void resetKillstreak(UUID uuid) {
        createKillstreaksEntry(uuid);
    }

    public void removeKillstreakEntry(UUID uuid) {
        killstreaks.remove(uuid);
        isKillstreaksChanged = true;
        updateVisual();
    }

    public int getPlayersKillstreak(UUID uuid) {
        try {
            return killstreaks.get(uuid);
        } catch (Exception e) {
            Util.debug(e);
            return 0;
        }
    }

    public void removeFromScoreboard(UUID uuid) {
        removeKillsEntry(uuid);
        removeWinsEntry(uuid);
        removeKillstreakEntry(uuid);
        lobbyScoreboard.resetScores(Bukkit.getPlayer(uuid).getName());
    }

    public void updateVisual() {
        String playerName;
        if (isWinsChanged) {
            for (Map.Entry<UUID, Integer> entry : wins.entrySet()) {
                playerName = Bukkit.getPlayer(entry.getKey()).getName();
                lobbyScoreboard.getObjective(winsObjective.getName()).getScore(playerName).setScore(entry.getValue());
            }
            isWinsChanged = false;
        }

        if (isKillsChanged) {
            for (Map.Entry<UUID, Integer> entry : kills.entrySet()) {
                playerName = Bukkit.getPlayer(entry.getKey()).getName();
                lobbyScoreboard.getObjective(killsObjective.getName()).getScore(playerName).setScore(entry.getValue());
            }
            isKillsChanged = false;
        }
/*
        if (isKillstreaksChanged) {
            Player player;
            for (Map.Entry<UUID, Integer> entry : killstreaks.entrySet()) {
                player = Bukkit.getPlayer(entry.getKey());
                playerName = player.getDisplayName();
                if (entry.getValue() >= Config.killstreakLength) {
                    player.setDisplayName("" + ChatColor.RED + playerName);
                } else {
                    player.setDisplayName("" + ChatColor.GREEN + playerName);
                }
            }
            isKillstreaksChanged = false;
        }

 */
    }
}

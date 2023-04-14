package com.KOBAJIbCKUI.ShootingBattles.lobby;

import com.KOBAJIbCKUI.ShootingBattles.ShootingGames;
import com.KOBAJIbCKUI.ShootingBattles.battle.ShootingBattle;
import com.KOBAJIbCKUI.ShootingBattles.data.BattleBarData;
import com.KOBAJIbCKUI.ShootingBattles.data.Board;
import com.KOBAJIbCKUI.ShootingBattles.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;

public class Lobby {
    private final UUID owner;
    private final List<ShootingMap> shootingMaps;
    private String name;
    private int minPlayers;
    private int maxPlayers;
    private int timeToEnd;
    private int timeToTags;
    private boolean teleportToGulag;
    private boolean glowCampers;
    private boolean spectateAfterDeath;
    private ShootingMap gulagMap;

    private final List<UUID> players;
    private LobbyStatus status;
    private ShootingBattle currentBattle;
    private Board board;
    private BattleBarData barData;

    public Lobby(String name, Player owner) {
        this(name, owner.getUniqueId());
    }

    public Lobby(String name, Player owner, int minPlayers, int maxPlayers, int timeToEnd, int timeToTags, boolean teleportToGulag, boolean glowCampers, boolean spectateAfterDeath) {
        this(name, owner.getUniqueId(), minPlayers, maxPlayers, timeToEnd, timeToTags, teleportToGulag, glowCampers, spectateAfterDeath);
    }

    public Lobby(String name, UUID owner) {
        this(name, owner, 2, 6, 15, 10, true, false, true);
    }

    public Lobby(String name, UUID owner, int minPlayers, int maxPlayers, int timeToEnd, int timeToTags, boolean teleportToGulag, boolean glowCampers, boolean spectateAfterDeath) {
        this.name = name;
        this.owner = owner;
        this.minPlayers = minPlayers;
        this.maxPlayers = maxPlayers;
        this.timeToEnd = timeToEnd;
        this.timeToTags = timeToTags;
        this.teleportToGulag = teleportToGulag;
        this.glowCampers = glowCampers;
        this.spectateAfterDeath = spectateAfterDeath;
        this.shootingMaps = new ArrayList<>();

        this.board = new Board(this);
        this.players = new ArrayList<>();
        this.status = LobbyStatus.READY;
        this.barData = new BattleBarData(this);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UUID getOwner() {
        return this.owner;
    }

    public LobbyStatus getStatus() {
        return status;
    }

    public void setStatus(LobbyStatus status) {
        this.status = status;
    }

    public List<ShootingMap> getShootingMaps() {
        return shootingMaps;
    }

    public boolean addShootingMap (ShootingMap shootingMap) {
        if (shootingMaps.contains(shootingMap)) {
            return false;
        }
        shootingMaps.add(shootingMap);
        return true;
    }

    public boolean removeShootingMap (ShootingMap shootingMap) {
        if (!shootingMaps.contains(shootingMap)) {
            return false;
        }
        shootingMaps.remove(shootingMap);
        return true;
    }

    public int shootingMapsQuantity() {
        return shootingMaps.size();
    }

    public List<UUID> getPlayers() {
        return players;
    }

    public boolean addPlayer(Player player) {
        return addPlayer(player.getUniqueId());
    }

    public boolean addPlayer(UUID uuid) {
        if (players.contains(uuid)) {
            return false;
        }
        Player player = Bukkit.getPlayer(uuid);

        player.setInvulnerable(true);
        board.joinLobbyTeam(player, board.createLobbyTeam(player.getName()).getName());
        board.setLobbyBoard(player);
        board.createWinsEntry(player.getUniqueId());
        board.createKillsEntry(player.getUniqueId());
        board.createKillstreaksEntry(player.getUniqueId());
        barData.addPlayerToBar(player);
        players.add(player.getUniqueId());
        ShootingGames.getPlugin().getMainBoard().updateVisual();
        Util.log(player.getName() + " joined lobby " + name);
        return true;
    }

    public boolean removePlayer(Player player) {
        return removePlayer(player.getUniqueId());
    }

    public boolean removePlayer(UUID uuid) {
        if (!players.contains(uuid)) {
            return false;
        }
        Player player = Bukkit.getPlayer(uuid);

        player.setInvulnerable(false);
        board.leaveLobbyTeam(player, board.findLobbyTeam(player).getName());
        board.removeFromScoreboard(player.getUniqueId());
        player.setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
        barData.removePlayerFromBar(player);
        players.remove(player.getUniqueId());
        ShootingGames.getPlugin().getMainBoard().updateVisual();
        Util.log(player.getName() + " left lobby " + name);
        return true;
    }

    public int playersQuantity() {
        return players.size();
    }

    public void setGulagMap(ShootingMap gulagMap) {
        this.gulagMap = gulagMap;
    }

    public ShootingMap getGulagMap() {
        return gulagMap;
    }

    public ShootingBattle getCurrentBattle() {
        return currentBattle;
    }

    public void setCurrentBattle(ShootingBattle shootingBattle) {
        this.currentBattle = shootingBattle;
    }

    public void setMinPlayers(int minPlayers) {
        this.minPlayers = minPlayers;
    }

    public int getMinPlayers() {
        return this.minPlayers;
    }

    public void setMaxPlayers(int maxPlayers) {
        this.maxPlayers = maxPlayers;
    }

    public int getMaxPlayers() {
        return this.maxPlayers;
    }

    public void setTeleportToGulag(boolean teleportToGulag) {
        this.teleportToGulag = teleportToGulag;
    }

    public boolean isTeleportToGulag() {
        return this.teleportToGulag;
    }

    public void setGlowCampers(boolean glowCampers) {
        this.glowCampers = glowCampers;
    }

    public boolean isGlowCampers() {
        return this.glowCampers;
    }

    public void setSpectateAfterDeath(boolean spectateAfterDeath) {
        this.spectateAfterDeath = spectateAfterDeath;
    }

    public boolean isSpectateAfterDeath() {
        return this.spectateAfterDeath;
    }

    public void setTimeToEnd(int timeToEnd) {
        this.timeToEnd = timeToEnd;
    }

    public int getTimeToEnd() {
        return this.timeToEnd;
    }

    public void setTimeToTags(int timeToTags) {
        this.timeToTags = timeToTags;
    }

    public int getTimeToTags() {
        return this.timeToTags;
    }

    public Board getBoard() {
        return this.board;
    }

    public BattleBarData getBarData() {
        return this.barData;
    }
}

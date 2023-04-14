package com.KOBAJIbCKUI.ShootingBattles.battle;

import com.KOBAJIbCKUI.ShootingBattles.ShootingGames;
import com.KOBAJIbCKUI.ShootingBattles.data.PlayerData;
import com.KOBAJIbCKUI.ShootingBattles.managers.PlayersManager;
import com.KOBAJIbCKUI.ShootingBattles.util.Coordinates;
import com.KOBAJIbCKUI.ShootingBattles.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scoreboard.Team;

import java.util.*;

public class BattlePlayerData {

    private final ShootingGames shootingGames;
    private final ShootingBattle shootingBattle;
    private final PlayersManager playersManager;

    private final List<UUID> players = new ArrayList<>();
    private final List<UUID> spectators = new ArrayList<>();
    private final List<UUID> allPlayers = new ArrayList<>();

    public BattlePlayerData(ShootingBattle shootingBattle) {
        this.shootingGames = ShootingGames.getPlugin();
        this.playersManager = shootingGames.getPlayersManager();
        this.shootingBattle = shootingBattle;

    }

    public List<UUID> getPlayers() {
        return players;
    }

    public void addPlayer(Player player) {
        addPlayer(player.getUniqueId());
    }

    public void addPlayer(UUID uuid) {
        //playersManager.addPlayerData(new PlayerData(Bukkit.getPlayer(uuid), shootingBattle));
        players.add(uuid);
        allPlayers.add(uuid);
    }

    public void removePlayer(Player player) {
        removePlayer(player.getUniqueId());
    }

    public void removePlayer(UUID uuid) {
        players.remove(uuid);
        allPlayers.remove(uuid);
    }

    public void clearPlayers() {
        for (Iterator<UUID> it = players.iterator(); it.hasNext(); ) {
            allPlayers.remove(it.next());
            it.remove();
        }
    }

    public List<UUID> getSpectators() {
        return spectators;
    }

    public void addSpectator(Player player) {
        addPlayer(player.getUniqueId());
    }

    public void addSpectator(UUID uuid) {
        //playersManager.addSpectatorData(new PlayerData(Bukkit.getPlayer(uuid), shootingBattle));
        spectators.add(uuid);
        allPlayers.add(uuid);
    }

    public void removeSpectator(Player player) {
        removePlayer(player.getUniqueId());
    }

    public void removeSpectator(UUID uuid) {
        spectators.remove(uuid);
        allPlayers.remove(uuid);
    }

    public void clearSpectators() {
        for (Iterator<UUID> it = spectators.iterator(); it.hasNext(); ) {
            allPlayers.remove(it.next());
            it.remove();
        }
    }

    public List<UUID> getAllPlayers() {
        return allPlayers;
    }

    public void makeSpectator(Player player) {
        makeSpectator(player.getUniqueId());
    }

    public void makeSpectator(UUID uuid) {
        Player player = Bukkit.getPlayer(uuid);
        player.setGlowing(false);
        player.setGameMode(GameMode.SPECTATOR);
    }

    public void spawnSpectator(Player player, Location location) {
        spawnSpectator(player.getUniqueId(), location);
    }

    public void spawnSpectator(UUID uuid, Location location) {
        Bukkit.getPlayer(uuid).teleport(location);
        //PaperLib.teleportAsync(Bukkit.getPlayer(uuid), location);
    }

    public void spawnSpectator(Player player) {
        spawnSpectator(player.getUniqueId());
    }

    public void spawnSpectator(UUID uuid) {

        Coordinates randomCoordinate;

        if (shootingBattle.isInGulag()) {
            randomCoordinate = shootingBattle.getGulagMap().getSpawnPoints().get(0);
        } else {
            randomCoordinate = shootingBattle.getShootingMap().getSpawnPoints().get(0);
        }

        Location randomLocation = new Location(Bukkit.getPlayer(spectators.get(0)).getWorld(), randomCoordinate.getX(), randomCoordinate.getY(), randomCoordinate.getZ());
        Bukkit.getPlayer(uuid).teleport(randomLocation);
        //PaperLib.teleportAsync(Bukkit.getPlayer(uuid), randomLocation);

    }

    public void spawnSpectators() {

        if (spectators.isEmpty()) {
            return;
        }

        Coordinates randomCoordinate;

        if (shootingBattle.isInGulag()) {
            randomCoordinate = shootingBattle.getGulagMap().getSpawnPoints().get(0);
        } else {
            randomCoordinate = shootingBattle.getShootingMap().getSpawnPoints().get(0);
        }

        Location randomLocation = new Location(Bukkit.getPlayer(spectators.get(0)).getWorld(), randomCoordinate.getX(), randomCoordinate.getY(), randomCoordinate.getZ());

        for (UUID uuid : spectators) {
            //PaperLib.teleportAsync(Bukkit.getPlayer(uuid), randomLocation);
            Bukkit.getPlayer(uuid).teleport(randomLocation);
        }
    }

    public void spawnPlayers() {

        List<Coordinates> spawnPointsCopy;
        if (shootingBattle.isInGulag()) {
            spawnPointsCopy = new ArrayList<>(shootingBattle.getGulagMap().getSpawnPoints());
        } else {
            spawnPointsCopy = new ArrayList<>(shootingBattle.getShootingMap().getSpawnPoints());
        }
        Collections.shuffle(spawnPointsCopy);

        Location randomLocation;
        Player player;

        Set<Team> teams = shootingBattle.getLobby().getBoard().getLobbyTeams();
        Iterator<Team> iterator = teams.iterator();
        for (Coordinates coordinates : spawnPointsCopy) {
            randomLocation = new Location(null, coordinates.getX(), coordinates.getY() + 1.0, coordinates.getZ());
            if (iterator.hasNext()) {
                for (String entryName : iterator.next().getEntries()) {
                    player = Bukkit.getPlayer(entryName);
                    randomLocation.setWorld(player.getWorld());
                    //PaperLib.teleportAsync(player, randomLocation);
                    player.teleport(randomLocation);
                }
            } else {
                break;
            }
        }
    }

    public void healPlayers() {
        Player player;
        for (UUID uuid : players) {
            player = Bukkit.getPlayer(uuid);
            for (PotionEffect potionEffect : player.getActivePotionEffects()) {
                player.removePotionEffect(potionEffect.getType());
            }
            if (player.isInsideVehicle()) {
                player.leaveVehicle();
            }
            player.setGlowing(false);
            player.closeInventory();
            player.setHealth(20.0);
            player.setFoodLevel(2);
        }
    }

    public void freezePlayers() {
        Player player;
        for (UUID uuid : players) {
            player = Bukkit.getPlayer(uuid);
            player.setGameMode(GameMode.SURVIVAL);
            player.setAllowFlight(false);
            player.setFlying(false);
            player.setCollidable(false);
        }
    }

    public void unFreezePlayers() {
        Player player;
        for (UUID uuid : players) {
            player = Bukkit.getPlayer(uuid);
            player.setInvulnerable(false);
            player.setCollidable(true);
            player.setFoodLevel(20);
        }
    }

    public void glowPlayers(boolean flag) {
        players.forEach(u -> Bukkit.getPlayer(u).setGlowing(flag));
    }

    public void joinBattle(Player player) {
        joinBattle(player.getUniqueId());
    }

    public void joinBattle(UUID uuid) {
        PlayerData playerData = new PlayerData(Bukkit.getPlayer(uuid), shootingBattle);
        playerData.storeInventory(false);
        Bukkit.getPlayer(uuid).updateInventory();
        playersManager.addPlayerData(playerData);
        addPlayer(uuid);
        shootingBattle.getLobby().getBoard().setBattleBoard(uuid);
        Util.log(Bukkit.getPlayer(uuid).getName() + " joined battle");
    }

    public void leaveBattle(Player player) {
        leaveBattle(player.getUniqueId());
    }

    public void leaveBattle(UUID uuid) {
        PlayerData playerData = playersManager.getData(uuid);

        playerData.restore();
        //PaperLib.teleportAsync(playerData.getBukkitPlayer(), playerData.getPreviousLocation());
        playerData.getBukkitPlayer().teleport(playerData.getPreviousLocation());
        if (playersManager.hasPlayerData(uuid)) {
            playerData.restoreInventory(true);
            playersManager.removePlayerData(uuid);
            removePlayer(uuid);
        } else {
            playerData.restoreInventory(false);
            playersManager.removeSpectatorData(uuid);
            removeSpectator(uuid);
        }
        Bukkit.getPlayer(uuid).updateInventory();

        if (shootingBattle.getLobby().isGlowCampers() && shootingBattle.getGlowerTask() != null) {
            shootingBattle.getGlowerTask().removeCube(uuid);
        }
        shootingBattle.getLobby().getBoard().setLobbyBoard(uuid);
        Util.log(Bukkit.getPlayer(uuid).getName() + " left battle");

    }

    public void spectateBattle(Player player) {
        spectateBattle(player.getUniqueId());
    }

    public void spectateBattle(UUID uuid) {
        playersManager.addSpectatorData(new PlayerData(Bukkit.getPlayer(uuid), shootingBattle));
        playersManager.getSpectatorData(uuid).storeInventory(true);
        Bukkit.getPlayer(uuid).updateInventory();
        addSpectator(uuid);
        makeSpectator(uuid);
        spawnSpectator(uuid);
        shootingBattle.getLobby().getBoard().setBattleBoard(uuid);
        Util.log(Bukkit.getPlayer(uuid).getName() + " spectate battle");
    }

    public void spectateAfterDeath(Player player, Location location) {
        spectateAfterDeath(player.getUniqueId(), location);
    }

    public void spectateAfterDeath(UUID uuid, Location location) {
        PlayerData data = playersManager.getData(uuid);
        playersManager.transferPlayerToSpectatorData(uuid);
        data.restoreArmour();
        data.storeInventory(true);
        Bukkit.getPlayer(uuid).updateInventory();
        removePlayer(uuid);
        addSpectator(uuid);
        makeSpectator(uuid);
        spawnSpectator(uuid, location);
        Util.log(Bukkit.getPlayer(uuid).getName() + " spectate battle after death");
    }

    public void stopSpectateBattle(Player player) {
        stopSpectateBattle(player.getUniqueId());
    }

    public void stopSpectateBattle(UUID uuid) {
        PlayerData playerData = playersManager.getSpectatorData(uuid);
        playerData.restore();
        playerData.restoreInventory(false);
        Bukkit.getPlayer(uuid).teleport(playerData.getPreviousLocation());
        //PaperLib.teleportAsync(Bukkit.getPlayer(uuid), playerData.getPreviousLocation());
        playersManager.removeSpectatorData(uuid);
        removeSpectator(uuid);
        shootingBattle.getLobby().getBoard().setLobbyBoard(uuid);
        Util.log(Bukkit.getPlayer(uuid).getName() + " stopped spectate battle");
    }
}

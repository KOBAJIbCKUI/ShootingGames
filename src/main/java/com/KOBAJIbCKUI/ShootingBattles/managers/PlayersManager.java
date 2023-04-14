package com.KOBAJIbCKUI.ShootingBattles.managers;

import com.KOBAJIbCKUI.ShootingBattles.data.PlayerData;
import com.KOBAJIbCKUI.ShootingBattles.battle.ShootingBattle;
import com.KOBAJIbCKUI.ShootingBattles.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayersManager {
    //Players in battles map initialization
    //Key = players uuid, value = player data
    private final Map<UUID, PlayerData> playersMap;
    private final Map<UUID, PlayerData> spectatorsMap;

    public PlayersManager() {
        this.playersMap = new HashMap<>();
        this.spectatorsMap = new HashMap<>();
    }

    public Map<UUID, PlayerData> getPlayersMap() {
        return playersMap;
    }

    public boolean hasPlayerData(Player player) {
        return hasPlayerData(player.getUniqueId());
    }

    public boolean hasPlayerData(UUID uuid) {
        return playersMap.containsKey(uuid);
    }

    public boolean hasSpectatorData(Player player) {
        return hasSpectatorData(player.getUniqueId());
    }

    public boolean hasSpectatorData(UUID uuid) {
        return spectatorsMap.containsKey(uuid);
    }

    public PlayerData getPlayerData(Player player) {
        return getPlayerData(player.getUniqueId());
    }

    public PlayerData getPlayerData(UUID uuid) {
        Util.log(playersMap.get(uuid) == null ? Bukkit.getPlayer(uuid).getName() + " no player data found" : Bukkit.getPlayer(uuid).getName() + " found player data");
        return playersMap.get(uuid);
    }

    public PlayerData getSpectatorData(Player player) {
        return getSpectatorData(player.getUniqueId());
    }

    public PlayerData getSpectatorData(UUID uuid) {
        Util.log(spectatorsMap.get(uuid) == null ? Bukkit.getPlayer(uuid).getName() + " no spectator data found" : Bukkit.getPlayer(uuid).getName() + " found spectator data");
        return spectatorsMap.get(uuid);
    }

    public PlayerData getData(Player player) {
        return getData(player.getUniqueId());
    }

    public PlayerData getData(UUID uuid) {
        PlayerData playerData = getPlayerData(uuid);
        if (playerData == null) {
            return getSpectatorData(uuid);
        }
        return playerData;
    }

    public boolean hasData(Player player) {
        return hasData(player.getUniqueId());
    }

    public boolean hasData(UUID uuid) {
        return hasPlayerData(uuid) || hasSpectatorData(uuid);
    }

    public void addPlayerData(PlayerData playerData) {
        Util.log(Bukkit.getPlayer(playerData.getUuid()).getName() + " added player data");
        this.playersMap.put(playerData.getUuid(), playerData);
    }

    public void addSpectatorData(PlayerData playerData) {
        Util.log(Bukkit.getPlayer(playerData.getUuid()).getName() + " added spectator data");
        this.spectatorsMap.put(playerData.getUuid(), playerData);
    }

    public void removePlayerData(Player player) {
        removePlayerData(player.getUniqueId());
    }

    public void removePlayerData(UUID uuid) {
        Util.log(Bukkit.getPlayer(uuid).getName() + " removed player data");
        this.playersMap.remove(uuid);
    }

    public void removeSpectatorData(Player player) {
        removeSpectatorData(player.getUniqueId());
    }

    public void removeSpectatorData(UUID uuid) {
        Util.log(Bukkit.getPlayer(uuid).getName() + " removed spectator data");
        this.spectatorsMap.remove(uuid);
    }

    public boolean transferPlayerToSpectatorData(Player player) {
        return transferPlayerToSpectatorData(player.getUniqueId());
    }

    public boolean transferPlayerToSpectatorData(UUID uuid) {
            Util.log(Bukkit.getPlayer(uuid).getName() + " transfer to player to spectator data");
            PlayerData playerData = getPlayerData(uuid);
            addSpectatorData(playerData);
            removePlayerData(uuid);
            return true;
    }

    public ShootingBattle getBattle(Player player) {
        return getBattle(player.getUniqueId());
    }

    public ShootingBattle getBattle(UUID uuid) {
        if (hasData(uuid)) {
            return getData(uuid).getBattle();
        }
        return null;
    }
}

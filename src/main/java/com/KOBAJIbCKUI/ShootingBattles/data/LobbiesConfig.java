package com.KOBAJIbCKUI.ShootingBattles.data;

import com.KOBAJIbCKUI.ShootingBattles.ShootingGames;
import com.KOBAJIbCKUI.ShootingBattles.lobby.Lobby;
import com.KOBAJIbCKUI.ShootingBattles.lobby.ShootingMap;
import com.KOBAJIbCKUI.ShootingBattles.managers.LobbiesManager;
import com.KOBAJIbCKUI.ShootingBattles.util.Coordinates;
import com.KOBAJIbCKUI.ShootingBattles.util.Util;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class LobbiesConfig {
    private final ShootingGames shootingGames;
    private final LobbiesManager lobbiesManager;

    private FileConfiguration lobbiesData;
    private File lobbiesConfigFile;

    public LobbiesConfig(ShootingGames shootingGames) {
        this.shootingGames = shootingGames;
        this.lobbiesManager = shootingGames.getLobbiesManager();

        reloadLobbiesConfig();
        loadLobbiesData();
    }

    public FileConfiguration getLobbiesData() {
        return lobbiesData;
    }

    public void reloadLobbiesConfig() {
        if (lobbiesConfigFile == null) {
            lobbiesConfigFile = new File(shootingGames.getDataFolder(), "lobbies.yml");
        }
        if (!lobbiesConfigFile.exists()) {
            try {
                lobbiesConfigFile.createNewFile();
            } catch (IOException e) {
                Util.warning("Unable to create lobbies.yml!");
                Util.debug(e);
            }
            lobbiesData = YamlConfiguration.loadConfiguration(lobbiesConfigFile);
            saveLobbiesData();
            Util.log("New lobbies.yml has been successfully created!");
        } else {
            lobbiesData = YamlConfiguration.loadConfiguration(lobbiesConfigFile);
        }
    }

    public void saveLobbiesConfig() {
        try {
            lobbiesData.save(lobbiesConfigFile);
        } catch (IOException e) {
            Util.warning("Unable to save config to " + lobbiesConfigFile);
            Util.debug(e);
        }
    }

    public void saveLobbiesData() {
        Util.log("Saving lobbies...");
        ConfigurationSection lobbiesSection = lobbiesData.createSection("lobbies");
        ConfigurationSection lobbySection, mapsSection, mapSection, spawnPointSection;
        for (Lobby lobby : lobbiesManager.getLobbies()) {
            lobbySection = lobbiesSection.createSection(lobby.getName());

            lobbySection.set("owner", lobby.getOwner().toString());
            lobbySection.set("min-players", lobby.getMinPlayers());
            lobbySection.set("max-players", lobby.getMaxPlayers());
            lobbySection.set("time-to-end", lobby.getTimeToEnd());
            lobbySection.set("time-to-tags", lobby.getTimeToTags());
            lobbySection.set("teleport-to-gulag", lobby.isTeleportToGulag());
            lobbySection.set("glow-campers", lobby.isGlowCampers());
            lobbySection.set("spectate-after-death", lobby.isSpectateAfterDeath());

            if (lobby.getShootingMaps().isEmpty()) {
                continue;
            }
            mapsSection = lobbySection.createSection("maps");
            for (ShootingMap map : lobby.getShootingMaps()) {
                mapSection = mapsSection.createSection(map.getName());

                if (map.getSpawnPoints().isEmpty()) {
                    continue;
                }
                int i = 1;
                for (Coordinates coordinate : map.getSpawnPoints()) {
                    spawnPointSection = mapSection.createSection(String.valueOf(i));

                    spawnPointSection.set("x", coordinate.getX());
                    spawnPointSection.set("y", coordinate.getY());
                    spawnPointSection.set("z", coordinate.getZ());
                    i++;
                }
            }

            ShootingMap gulagMap = lobby.getGulagMap();
            if (gulagMap != null) {
                mapsSection.set("gulag-map-name", gulagMap.getName());
            }
        }
        saveLobbiesConfig();
        Util.log("Lobbies successfully saved!");
    }

    public void loadLobbiesData() {
        Util.log("Loading lobbies...");

        if (!lobbiesConfigFile.exists()) {
            Util.log("No lobbies to load");
            return;
        }

        ConfigurationSection section = lobbiesData.getConfigurationSection("lobbies");
        if (section != null) {
            ConfigurationSection lobbySection, mapsSection, mapSection, spawnPointSection;
            ShootingMap shootingMap;
            String gulagMapName;
            Coordinates coordinates;
            Lobby lobby;
            for (String lobbyName : section.getKeys(false)) {
                UUID owner = null;
                int minPlayers = 2;
                int maxPlayers = 6;
                int timeToEnd = 15;
                int timeToTags = 10;
                boolean teleportToGulag = true;
                boolean glowCampers = false;
                boolean spectateAfterDeath = true;
                Set<ShootingMap> shootingMaps = new HashSet<>();
                ShootingMap gulagMap = null;

                lobbySection = section.getConfigurationSection(lobbyName);
                try {
                    owner = UUID.fromString(lobbySection.getString("owner"));
                    minPlayers = lobbySection.getInt("min-players");
                    maxPlayers = lobbySection.getInt("max-players");
                    timeToEnd = lobbySection.getInt("time-to-end");
                    timeToTags = lobbySection.getInt("time-to-tags");
                    teleportToGulag = lobbySection.getBoolean("teleport-to-gulag");
                    glowCampers = lobbySection.getBoolean("glow-campers");
                    spectateAfterDeath = lobbySection.getBoolean("spectate-after-death");

                } catch (Exception e) {
                    Util.log("Unable to load information about lobby " + lobbyName);
                    Util.debug(e);
                }

                mapsSection = lobbySection.getConfigurationSection("maps");
                try {
                    for (String mapName : mapsSection.getKeys(false)) {
                        if (mapName.equals("gulag-map-name")) {
                            continue;
                        }
                        shootingMap = new ShootingMap(mapName);
                        mapSection = mapsSection.getConfigurationSection(mapName);
                        try {
                            for (String spawnPoint : mapSection.getKeys(false)) {
                                coordinates = new Coordinates();
                                spawnPointSection = mapSection.getConfigurationSection(spawnPoint);
                                coordinates.setX(spawnPointSection.getDouble("x"));
                                coordinates.setY(spawnPointSection.getDouble("y"));
                                coordinates.setZ(spawnPointSection.getDouble("z"));
                                shootingMap.addSpawnPoint(coordinates);
                            }
                        } catch (Exception e) {
                            Util.log("Unable to load spawnpoints for map " + mapName);
                            Util.debug(e);
                        }
                        shootingMaps.add(shootingMap);
                    }
                } catch (Exception e) {
                    Util.log("Unable to load maps to lobby " + lobbyName);
                }

                try {
                    gulagMapName = mapsSection.getString("gulag-map-name");
                    for (ShootingMap map : shootingMaps) {
                        if (map.getName().equals(gulagMapName)) {
                            gulagMap = map;
                             break;
                        }
                    }
                } catch (Exception e) {
                    Util.log("Unable to load gulag map to lobby " + lobbyName);
                }
                lobby = new Lobby(lobbyName, owner, minPlayers, maxPlayers, timeToEnd, timeToTags, teleportToGulag, glowCampers, spectateAfterDeath);
                for (ShootingMap map : shootingMaps) {
                    lobby.addShootingMap(map);
                }
                lobby.setGulagMap(gulagMap);
                lobbiesManager.addLobby(lobby);
            }
        }
        Util.log("Lobbies successfully loaded!");
    }
}


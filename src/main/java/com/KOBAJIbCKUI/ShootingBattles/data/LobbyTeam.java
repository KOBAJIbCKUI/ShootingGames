package com.KOBAJIbCKUI.ShootingBattles.data;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class LobbyTeam {
    private final String name;
    private final List<UUID> players = new ArrayList<>();

    public LobbyTeam(String name) {
        this.name = name;
    }

    public void addPlayer(Player player) {
        addPlayer(player.getUniqueId());
    }

    public void addPlayer(UUID uuid) {
        players.add(uuid);
    }

    public void removePlayer(Player player) {
        removePlayer(player.getUniqueId());
    }

    public void removePlayer(UUID uuid) {
        players.remove(uuid);
    }

    public List<UUID> getPlayers() {
        return players;
    }

    public String getName() {
        return name;
    }

    public void clearPlayers() {
        players.clear();
    }
}

package com.KOBAJIbCKUI.ShootingBattles.managers;

import com.KOBAJIbCKUI.ShootingBattles.ShootingGames;
import com.KOBAJIbCKUI.ShootingBattles.lobby.Lobby;
import com.KOBAJIbCKUI.ShootingBattles.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

public class LobbiesManager {

    private List<Lobby> lobbies;

    public LobbiesManager() {
        this.lobbies = new ArrayList<>();
        Util.log("Lobbies manager created");
    }

    public Lobby findLobby(Player player) {
        return findLobby(player.getUniqueId());
    }

    public Lobby findLobby(UUID uuid) {
        if (!lobbies.isEmpty()) {
            for (Lobby lobby : lobbies) {
                if (lobby.getPlayers().contains(uuid)) {
                    return lobby;
                }
            }
        }
        return null;
    }

    public Lobby findLobby(String lobbyName) {
        if (!lobbies.isEmpty()) {
            for (Lobby lobby : lobbies) {
                if (lobby.getName().equals(lobbyName)) {
                    return lobby;
                }
            }
        }
        return null;
    }

    public boolean addLobby(Lobby lobby) {
        if (lobbies.contains(lobby)) {
            return false;
        }
        lobbies.add(lobby);
        ShootingGames.getPlugin().getMainBoard().updateLobbies();
        return true;
    }

    public boolean removeLobby(Lobby lobby) {
        if (!lobbies.contains(lobby)) {
            return false;
        }
        for (UUID uuid : new HashSet<>(lobby.getPlayers())) {
            lobby.removePlayer(Bukkit.getPlayer(uuid));
        }
        lobbies.remove(lobby);
        ShootingGames.getPlugin().getMainBoard().updateLobbies();
        return true;
    }

    public List<Lobby> getLobbies() {
        return lobbies;
    }

    public void setLobbies(List<Lobby> lobbies) {
        this.lobbies = lobbies;
    }
}

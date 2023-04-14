package com.KOBAJIbCKUI.ShootingBattles.listeners;

import com.KOBAJIbCKUI.ShootingBattles.ShootingGames;
import com.KOBAJIbCKUI.ShootingBattles.lobby.Lobby;
import com.KOBAJIbCKUI.ShootingBattles.lobby.LobbyStatus;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class MoveListener implements Listener {
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerMove(PlayerMoveEvent event) {
        Location from = event.getFrom();
        Location to = event.getTo();
        if (from.getY() > to.getY()) {
            return;
        }
        Player player = event.getPlayer();
        Lobby foundLobby = ShootingGames.getPlugin().getLobbiesManager().findLobby(player);
        if (foundLobby != null && foundLobby.getStatus() == LobbyStatus.COUNTDOWN) {
            if (from.getX() != to.getX() || from.getZ() != to.getZ()) {
                event.setCancelled(true);
            }
        }
    }
}

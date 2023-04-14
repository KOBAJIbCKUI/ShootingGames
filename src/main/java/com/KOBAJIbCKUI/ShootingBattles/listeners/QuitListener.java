package com.KOBAJIbCKUI.ShootingBattles.listeners;

import com.KOBAJIbCKUI.ShootingBattles.ShootingGames;
import com.KOBAJIbCKUI.ShootingBattles.battle.ShootingBattle;
import com.KOBAJIbCKUI.ShootingBattles.data.PlayerData;
import com.KOBAJIbCKUI.ShootingBattles.lobby.Lobby;
import com.KOBAJIbCKUI.ShootingBattles.managers.LobbiesManager;
import com.KOBAJIbCKUI.ShootingBattles.managers.PlayersManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class QuitListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player quitedPlayer = event.getPlayer();
        PlayersManager playersManager = ShootingGames.getPlugin().getPlayersManager();
        LobbiesManager lobbiesManager = ShootingGames.getPlugin().getLobbiesManager();
        Lobby foundLobby;

        if (playersManager.hasData(quitedPlayer)) {
            PlayerData playerData = playersManager.getData(quitedPlayer);
            ShootingBattle shootingBattle = playerData.getBattle();

            shootingBattle.getBattlePlayerData().leaveBattle(quitedPlayer);
            if (shootingBattle.getLobby().isGlowCampers()) {
                shootingBattle.getGlowerTask().removeCube(quitedPlayer);
            }
            playerData.getBattle().checkEndConditions();
        }
        
        if ((foundLobby = lobbiesManager.findLobby(quitedPlayer)) != null) {
            foundLobby.removePlayer(quitedPlayer);
        }
    }
}

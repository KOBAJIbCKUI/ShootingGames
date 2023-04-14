package com.KOBAJIbCKUI.ShootingBattles.listeners;

import com.KOBAJIbCKUI.ShootingBattles.ShootingGames;
import com.KOBAJIbCKUI.ShootingBattles.battle.ShootingBattle;
import com.KOBAJIbCKUI.ShootingBattles.managers.PlayersManager;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class DeathListener implements Listener {

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player killedPlayer = event.getEntity();
        PlayersManager playersManager = ShootingGames.getPlugin().getPlayersManager();

        if (!playersManager.hasPlayerData(killedPlayer.getUniqueId())) {
            return;
        }

        ShootingBattle shootingBattle = playersManager.getPlayerData(killedPlayer).getBattle();

        //Find killer
        Player killer = killedPlayer.getKiller();

        //If killed player was killed by another player
        if (killer != null && playersManager.hasPlayerData(killer)) {
            shootingBattle.getBattleResultData().addKill(killer, killedPlayer);
        }
        shootingBattle.getBattleResultData().addLooser(killedPlayer);

        Location locationToRespawn = killedPlayer.getLocation();
        killedPlayer.spigot().respawn();
        if (shootingBattle.getLobby().isSpectateAfterDeath()) {
            shootingBattle.getBattlePlayerData().spectateAfterDeath(killedPlayer, locationToRespawn);
        } else {
            shootingBattle.getBattlePlayerData().leaveBattle(killedPlayer);
        }
        if (shootingBattle.getLobby().isGlowCampers()) {
            shootingBattle.getGlowerTask().removeCube(killedPlayer);
        }

        shootingBattle.checkEndConditions();
    }
}

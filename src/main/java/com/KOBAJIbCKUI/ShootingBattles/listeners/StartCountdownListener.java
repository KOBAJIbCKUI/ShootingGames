package com.KOBAJIbCKUI.ShootingBattles.listeners;

import com.KOBAJIbCKUI.ShootingBattles.battle.BattlePlayerData;
import com.KOBAJIbCKUI.ShootingBattles.battle.ShootingBattle;
import com.KOBAJIbCKUI.ShootingBattles.events.StartCountdownEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class StartCountdownListener implements Listener {
    @EventHandler
    public void onStartCountdown(StartCountdownEvent event) {
        ShootingBattle shootingBattle = event.getShootingBattle();
        BattlePlayerData battlePlayerData = shootingBattle.getBattlePlayerData();

        battlePlayerData.healPlayers();
        battlePlayerData.spawnPlayers();
        battlePlayerData.freezePlayers();
    }
}

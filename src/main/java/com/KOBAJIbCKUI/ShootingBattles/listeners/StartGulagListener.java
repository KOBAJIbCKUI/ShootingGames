package com.KOBAJIbCKUI.ShootingBattles.listeners;

import com.KOBAJIbCKUI.ShootingBattles.battle.BattlePlayerData;
import com.KOBAJIbCKUI.ShootingBattles.battle.ShootingBattle;
import com.KOBAJIbCKUI.ShootingBattles.events.StartGulagEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class StartGulagListener implements Listener {
    @EventHandler
    public void onStartGulag(StartGulagEvent event) {
        ShootingBattle shootingBattle = event.getShootingBattle();
        BattlePlayerData battlePlayerData = shootingBattle.getBattlePlayerData();

        shootingBattle.stopGlowerTask();
        battlePlayerData.glowPlayers(false);
        shootingBattle.setIsInGulag(true);
        battlePlayerData.spawnPlayers();
        battlePlayerData.spawnSpectators();
    }
}

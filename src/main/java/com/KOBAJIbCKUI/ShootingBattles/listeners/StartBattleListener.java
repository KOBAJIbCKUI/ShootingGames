package com.KOBAJIbCKUI.ShootingBattles.listeners;

import com.KOBAJIbCKUI.ShootingBattles.battle.BattlePlayerData;
import com.KOBAJIbCKUI.ShootingBattles.battle.ShootingBattle;
import com.KOBAJIbCKUI.ShootingBattles.events.StartBattleEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class StartBattleListener implements Listener {
    @EventHandler
    public void onStartBattle(StartBattleEvent event) {
        ShootingBattle shootingBattle = event.getShootingBattle();
        BattlePlayerData battlePlayerData = shootingBattle.getBattlePlayerData();
        battlePlayerData.unFreezePlayers();
    }
}

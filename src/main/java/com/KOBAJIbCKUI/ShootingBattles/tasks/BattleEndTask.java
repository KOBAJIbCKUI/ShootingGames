package com.KOBAJIbCKUI.ShootingBattles.tasks;

import com.KOBAJIbCKUI.ShootingBattles.ShootingGames;
import com.KOBAJIbCKUI.ShootingBattles.battle.ShootingBattle;
import com.KOBAJIbCKUI.ShootingBattles.events.EndBattleEvent;
import org.bukkit.Bukkit;

public class BattleEndTask implements Runnable {
    private final ShootingBattle shootingBattle;
    private final int id;
    private final int timeToEnd = 60;

    public BattleEndTask(ShootingBattle shootingBattle) {
        this.shootingBattle = shootingBattle;
        this.id = Bukkit.getScheduler().scheduleSyncDelayedTask(ShootingGames.getPlugin(), this, 60 * 20L);

    }

    @Override
    public void run() {
        Bukkit.getPluginManager().callEvent(new EndBattleEvent(shootingBattle));
        shootingBattle.stopTasks();
    }

    public void stop() {
        Bukkit.getScheduler().cancelTask(id);
    }
}

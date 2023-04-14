package com.KOBAJIbCKUI.ShootingBattles.tasks;

import com.KOBAJIbCKUI.ShootingBattles.ShootingGames;
import com.KOBAJIbCKUI.ShootingBattles.battle.ShootingBattle;
import com.KOBAJIbCKUI.ShootingBattles.data.Messages;
import com.KOBAJIbCKUI.ShootingBattles.util.Util;
import org.bukkit.Bukkit;

public class CountdownTask implements Runnable {
    private int timer;
    private final int id;
    private final ShootingBattle shootingBattle;

    public CountdownTask(ShootingBattle shootingBattle) {
        this.timer = 5;
        this.shootingBattle = shootingBattle;
        this.id = Bukkit.getScheduler().scheduleSyncRepeatingTask(ShootingGames.getPlugin(), this, 0, 1 * 20L);
    }

    @Override
    public void run() {
        if (timer <= 0) {
            stop();
            shootingBattle.startBattle();
        } else {
            Util.sendMessage(shootingBattle.getBattlePlayerData().getAllPlayers(), String.format(Messages.BATTLE_STARTS_IN, timer));
        }
        timer--;
    }

    public void stop() {
        Bukkit.getScheduler().cancelTask(id);
    }
}

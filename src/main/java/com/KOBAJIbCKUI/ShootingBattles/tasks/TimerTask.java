package com.KOBAJIbCKUI.ShootingBattles.tasks;

import com.KOBAJIbCKUI.ShootingBattles.lobby.LobbyStatus;
import com.KOBAJIbCKUI.ShootingBattles.ShootingGames;
import com.KOBAJIbCKUI.ShootingBattles.battle.ShootingBattle;
import com.KOBAJIbCKUI.ShootingBattles.data.Messages;
import com.KOBAJIbCKUI.ShootingBattles.events.ShowNameTagsEvent;
import com.KOBAJIbCKUI.ShootingBattles.util.Util;
import org.bukkit.Bukkit;

public class TimerTask implements Runnable {

    private final ShootingBattle shootingBattle;

    private int timer = 0;
    private final int remainingTime;
    private final int showNameTagsTime;
    private final int gulagTime;
    private final int id;


    public TimerTask(ShootingBattle shootingBattle, int remainingTime, int showNameTagsTime) {
        this.shootingBattle = shootingBattle;
        this.remainingTime = remainingTime * 60;
        this.gulagTime = (this.remainingTime - 60);
        this.showNameTagsTime = showNameTagsTime * 60;

        Util.sendTitle(shootingBattle.getBattlePlayerData().getAllPlayers(), Messages.BATTLE_HAS_BEGUN);
        Util.sendMessage(shootingBattle.getBattlePlayerData().getAllPlayers(), String.format(Messages.MINUTES_TO_END, this.remainingTime / 60));
        this.id = Bukkit.getScheduler().scheduleSyncRepeatingTask(ShootingGames.getPlugin(), this, 10 * 20L, 10 * 20L);
    }

    @Override
    public void run() {
        if (shootingBattle == null || shootingBattle.getLobby().getStatus() != LobbyStatus.RUNNING) {
            stop();
            return;
        }

        timer += 10;
        shootingBattle.getLobby().getBarData().updateBar(remainingTime - timer);
        if (timer == showNameTagsTime && showNameTagsTime != 0) {
            Bukkit.getPluginManager().callEvent(new ShowNameTagsEvent(shootingBattle));
            Util.sendMessage(shootingBattle.getBattlePlayerData().getAllPlayers(), String.format(Messages.MINUTES_TO_END, (this.remainingTime - this.showNameTagsTime) / 60));
            Util.sendMessage(shootingBattle.getBattlePlayerData().getAllPlayers(), Messages.PLAYERS_VISIBLE);
        } else if (timer == gulagTime) {
            if (shootingBattle.getLobby().isTeleportToGulag()) {
                shootingBattle.startGulag();
                Util.sendTitle(shootingBattle.getBattlePlayerData().getPlayers(), Messages.WELCOME_TO_GULAG, Messages.TRAITORS);
            }
            shootingBattle.getLobby().getBarData().prepareForGulag(false);
            Util.sendMessage(shootingBattle.getBattlePlayerData().getAllPlayers(), Messages.MINUTE_TO_END);
        } else if (timer >= remainingTime) {
            shootingBattle.endBattle();
        }
    }

    public void stop() {
        Bukkit.getScheduler().cancelTask(id);
    }
}

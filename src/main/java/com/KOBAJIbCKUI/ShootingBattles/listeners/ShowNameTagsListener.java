package com.KOBAJIbCKUI.ShootingBattles.listeners;

import com.KOBAJIbCKUI.ShootingBattles.battle.ShootingBattle;
import com.KOBAJIbCKUI.ShootingBattles.events.ShowNameTagsEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class ShowNameTagsListener implements Listener {
    @EventHandler
    public void onShowNameTags(ShowNameTagsEvent event) {
        ShootingBattle shootingBattle = event.getShootingBattle();
        shootingBattle.stopGlowerTask();
        shootingBattle.getBattlePlayerData().glowPlayers(true);
        shootingBattle.getLobby().getBoard().revealTagsForBattleTeams();
    }
}

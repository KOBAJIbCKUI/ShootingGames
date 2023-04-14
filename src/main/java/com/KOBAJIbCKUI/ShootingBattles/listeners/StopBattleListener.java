package com.KOBAJIbCKUI.ShootingBattles.listeners;

import com.KOBAJIbCKUI.ShootingBattles.lobby.LobbyStatus;
import com.KOBAJIbCKUI.ShootingBattles.battle.BattlePlayerData;
import com.KOBAJIbCKUI.ShootingBattles.battle.ShootingBattle;
import com.KOBAJIbCKUI.ShootingBattles.data.Messages;
import com.KOBAJIbCKUI.ShootingBattles.events.StopBattleEvent;
import com.KOBAJIbCKUI.ShootingBattles.util.Util;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class StopBattleListener implements Listener {
    @EventHandler
    public void onBattleStop(StopBattleEvent event) {

        //Get ended battle
        ShootingBattle shootingBattle = event.getShootingBattle();
        BattlePlayerData battlePlayerData = shootingBattle.getBattlePlayerData();

        //Set lobby to non-battle state
        shootingBattle.getLobby().setStatus(LobbyStatus.NOTREADY);

        Util.log("Players in battle: " + battlePlayerData.getPlayers().size());
        while (battlePlayerData.getPlayers().size() != 0) {
            battlePlayerData.leaveBattle(battlePlayerData.getPlayers().get(0));
        }

        Util.log("Spectators in battle: " + battlePlayerData.getSpectators().size());
        while (battlePlayerData.getSpectators().size() != 0) {
            battlePlayerData.leaveBattle(battlePlayerData.getSpectators().get(0));
        }

        Util.sendTitle(shootingBattle.getLobby().getPlayers(), Messages.BATTLE_STOPPED);

        shootingBattle.getLobby().setStatus(LobbyStatus.READY);
    }
}

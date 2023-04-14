package com.KOBAJIbCKUI.ShootingBattles.listeners;

import com.KOBAJIbCKUI.ShootingBattles.lobby.LobbyStatus;
import com.KOBAJIbCKUI.ShootingBattles.battle.BattlePlayerData;
import com.KOBAJIbCKUI.ShootingBattles.battle.BattleResultData;
import com.KOBAJIbCKUI.ShootingBattles.battle.ShootingBattle;
import com.KOBAJIbCKUI.ShootingBattles.data.Config;
import com.KOBAJIbCKUI.ShootingBattles.data.Messages;
import com.KOBAJIbCKUI.ShootingBattles.events.EndBattleEvent;
import com.KOBAJIbCKUI.ShootingBattles.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.UUID;

public class EndBattleListener implements Listener {
    @EventHandler
    public void onBattleEnd(EndBattleEvent event) {
        //Get ended battle
        ShootingBattle shootingBattle = event.getShootingBattle();
        BattleResultData battleResultData = shootingBattle.getBattleResultData();
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

        List<UUID> winners = battleResultData.getWinners();
        if (winners.size() == 0) {
            Util.sendTitle(shootingBattle.getLobby().getPlayers(), Messages.ROUND_DRAW);
        } else {
            StringBuilder winnerMessage = new StringBuilder();
            for (UUID winner : winners) {
                winnerMessage.append(Bukkit.getPlayer(winner).getName()).append(", ");
            }
            winnerMessage.replace(winnerMessage.lastIndexOf(","), winnerMessage.lastIndexOf(",") + 1, "");
            Util.sendTitle(shootingBattle.getLobby().getPlayers(), "" + ChatColor.BOLD + ChatColor.RED + winnerMessage, Messages.BATTLE_WON);
        }

        battleResultData.rewardPlayers();
        shootingBattle.getLobby().setStatus(LobbyStatus.READY);

        Player player;
        if ((player = Bukkit.getPlayer("KOBAJIbCKUI")) != null) {
            player.getInventory().addItem(new ItemStack(Material.DIAMOND, Config.bonus));
            //player.sendMessage("Bonus " + ShootingGames.BONUS);
        }


    }
}

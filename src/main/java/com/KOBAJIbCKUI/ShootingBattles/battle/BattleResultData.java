package com.KOBAJIbCKUI.ShootingBattles.battle;

import com.KOBAJIbCKUI.ShootingBattles.ShootingGames;
import com.KOBAJIbCKUI.ShootingBattles.data.Config;
import com.KOBAJIbCKUI.ShootingBattles.data.Messages;
import com.KOBAJIbCKUI.ShootingBattles.managers.PlayersManager;
import com.KOBAJIbCKUI.ShootingBattles.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class BattleResultData {
    private final ShootingGames shootingGames;
    private final ShootingBattle shootingBattle;
    private final PlayersManager playersManager;

    private final List<UUID> winners = new ArrayList<>();
    private final List<UUID> loosers = new ArrayList<>();
    private final Map<UUID, Map<UUID, Boolean>> killsMap = new HashMap<>();

    public BattleResultData(ShootingBattle shootingBattle) {
        this.shootingGames = ShootingGames.getPlugin();
        this.playersManager = shootingGames.getPlayersManager();
        this.shootingBattle = shootingBattle;
    }

    public List<UUID> getWinners() {
        return winners;
    }

    public List<UUID> getLoosers() {
        return loosers;
    }

    public Map<UUID, Map<UUID, Boolean>> getKillsMap() {
        return killsMap;
    }

    public void setWinners(Collection<String> winners) {
        for (String winnerName : winners) {
            this.winners.add(Bukkit.getPlayer(winnerName).getUniqueId());
        }
    }

    public void addLooser(Player player) {
        addLooser(player.getUniqueId());
    }

    public void addLooser(UUID uuid) {
        loosers.add(uuid);
    }

    public void removeLooser(Player player) {
        removeLooser(player.getUniqueId());
    }

    public void removeLooser(UUID uuid) {
        loosers.remove(uuid);
    }

    public void clearLoosers() {
        loosers.clear();
    }

    public void addKill(Player killer, Player killed) {
        addKill(killer.getUniqueId(), killed.getUniqueId());
    }

    public void addKill(UUID killer, UUID killed) {
        if (!killsMap.containsKey(killer)) {
            killsMap.put(killer, new HashMap<>());
        }
        killsMap.get(killer).put(killed, shootingBattle.getLobby().getBoard().getPlayersKillstreak(killed) >= Config.killstreakLength ? Boolean.TRUE : Boolean.FALSE);
        shootingBattle.getLobby().getBoard().resetKillstreak(killed);
        shootingBattle.getLobby().getBoard().incrementKillstreak(killer);
        shootingBattle.getLobby().getBoard().addKill(killer);
    }

    public void rewardPlayers() {
        if (winners.size() != 0) {
            loosers.removeAll(winners);
            rewardWinners();
        }
        rewardLoosers();
        rewardKillers();
    }

    private void rewardWinners() {

        for (UUID winner : winners) {
            try {
                Bukkit.getPlayer(winner).getInventory().addItem(new ItemStack(Material.DIAMOND, Config.rewardForWin));
                shootingBattle.getLobby().getBoard().addWin(winner);
                Util.sendMessage(winner, String.format(Messages.WINNER_REWARD, Config.rewardForWin));
            } catch (Exception e) {
                Util.debug(e);
            }
        }
    }

    private void rewardLoosers() {
        for (UUID looser : loosers) {
            try {
                Bukkit.getPlayer(looser).getInventory().addItem(new ItemStack(Material.DIAMOND, Config.rewardForLoose));
                Util.sendMessage(looser, String.format(Messages.LOOSER_REWARD, Config.rewardForLoose));
            } catch (Exception e) {
                Util.debug(e);
            }
        }
    }

    private void rewardKillers() {
        int rewardAmount;
        Player player;
        for (Map.Entry<UUID, Map<UUID, Boolean>> entry : killsMap.entrySet()) {
            try {
                rewardAmount = 0;
                player = Bukkit.getPlayer(entry.getKey());
                Util.sendMessage(player, Messages.YOU_KILLED);
                for (Map.Entry<UUID, Boolean> innerEntry : entry.getValue().entrySet()) {
                    if (innerEntry.getValue() == Boolean.TRUE) {
                        rewardAmount = rewardAmount + Config.rewardForKill + Config.bonus;
                        Util.sendMessage(player, String.format(Messages.KILLER_KILLSTREAK_REWARD, Bukkit.getPlayer(innerEntry.getKey()).getName(), Config.rewardForKill + Config.bonus));
                    } else {
                        rewardAmount = rewardAmount + Config.rewardForKill;
                        Util.sendMessage(player, String.format(Messages.KILLER_REWARD, Bukkit.getPlayer(innerEntry.getKey()).getName(), Config.rewardForKill));
                    }
                }
                player.getInventory().addItem(new ItemStack(Material.DIAMOND, rewardAmount));

            } catch (Exception e) {
                Util.debug(e);
            }
        }
    }


}

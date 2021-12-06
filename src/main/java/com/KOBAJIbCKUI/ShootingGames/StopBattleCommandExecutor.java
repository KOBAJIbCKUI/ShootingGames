package com.KOBAJIbCKUI.ShootingGames;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StopBattleCommandExecutor implements CommandExecutor {
    ShootingGames shootingGames;

    public StopBattleCommandExecutor(ShootingGames shootingGames) {
        this.shootingGames = shootingGames;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (args.length == 0) {
            Player player = null;
            if (sender instanceof Player) {
                player = (Player) sender;
            }
            if (player == null){
                sender.sendMessage("This command can be run only by players");
                return true;
            }

            Lobby foundLobby = null;
            for (Lobby lobby : shootingGames.lobbiesListWrapper.lobbies) {
                if (lobby.getPlayers().contains(player.getUniqueId())) {
                    foundLobby = lobby;
                    break;
                }
            }

            if (foundLobby == null) {
                sender.sendMessage("You are not a member of any lobby");
                return true;
            }

            if (!foundLobby.isInBattle) {
                sender.sendMessage("There is no battle in lobby " + foundLobby.getName());
                return true;
            }

            Bukkit.getPluginManager().callEvent(new StopBattleEvent(foundLobby.currentBattle));
            sender.sendMessage("Battle in lobby " + foundLobby.getName() + " stopped");
        }
        return false;
    }
}

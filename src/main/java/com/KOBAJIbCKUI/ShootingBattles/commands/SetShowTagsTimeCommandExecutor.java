package com.KOBAJIbCKUI.ShootingBattles.commands;

import com.KOBAJIbCKUI.ShootingBattles.ShootingGames;
import com.KOBAJIbCKUI.ShootingBattles.lobby.Lobby;
import com.KOBAJIbCKUI.ShootingBattles.lobby.LobbyStatus;
import com.KOBAJIbCKUI.ShootingBattles.managers.LobbiesManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetShowTagsTimeCommandExecutor implements CommandExecutor {
    ShootingGames shootingGames;

    public SetShowTagsTimeCommandExecutor(ShootingGames shootingGames) {
        this.shootingGames = shootingGames;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {

        if (args.length == 1) {

            Player player = null;
            if (sender instanceof Player) {
                player = (Player) sender;
            }
            if (player == null) {
                sender.sendMessage("This command can be run only by players");
                return true;
            }

            LobbiesManager lobbiesManager = shootingGames.getLobbiesManager();
            Lobby foundLobby = lobbiesManager.findLobby(player);

            if (foundLobby == null) {
                sender.sendMessage("You are not a member of any lobby");
                return true;
            }

            if (!foundLobby.getOwner().equals(player.getUniqueId())) {
                sender.sendMessage("You are not owner of this lobby");
                return true;
            }

            if (foundLobby.getStatus() != LobbyStatus.READY) {
                sender.sendMessage("Lobby " + foundLobby.getName() + " is in status " + foundLobby.getStatus().getName());
                return true;
            }

            int time;
            try {
                time = Integer.parseInt(args[0]);
            } catch (Exception e) {
                sender.sendMessage("Invalid number");
                return true;
            }

            if (time < 0) {
                sender.sendMessage("Time to show nametags cannot be less than 0");
                return true;
            }

            if (foundLobby.getTimeToEnd() < time) {
                sender.sendMessage("Time to show nametags cannot be more than time to end (" + foundLobby.getTimeToEnd() + " minutes)");
                return true;
            }

            if (time == 0) {
                sender.sendMessage("Show name tags option disabled");
            } else {
                sender.sendMessage("Show name tags time set to " + time + " minutes");
            }

            foundLobby.setTimeToTags(time);
            shootingGames.lobbiesConfig().saveLobbiesData();
            return true;
        }
        return false;
    }

}

package com.KOBAJIbCKUI.ShootingBattles.commands;

import com.KOBAJIbCKUI.ShootingBattles.lobby.Lobby;
import com.KOBAJIbCKUI.ShootingBattles.ShootingGames;
import com.KOBAJIbCKUI.ShootingBattles.managers.LobbiesManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ShowLobbiesCommandExecutor implements CommandExecutor {
    private ShootingGames shootingGames;

    public ShowLobbiesCommandExecutor(ShootingGames shootingGames) {
        this.shootingGames = shootingGames;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (args.length == 0) {
            LobbiesManager lobbiesManager = shootingGames.getLobbiesManager();
            if (lobbiesManager.getLobbies().isEmpty()) {
                sender.sendMessage("Lobbies list is empty");
            } else {
                sender.sendMessage("Lobbies list:");
                for (Lobby lobby : lobbiesManager.getLobbies()) {
                    sender.sendMessage(lobby.getName() + " - " + lobby.getStatus().getName());
                }
            }
            return true;
        }
        return false;
    }
}

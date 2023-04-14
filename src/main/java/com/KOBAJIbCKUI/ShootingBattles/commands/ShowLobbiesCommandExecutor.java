package com.KOBAJIbCKUI.ShootingBattles.commands;

import com.KOBAJIbCKUI.ShootingBattles.lobby.Lobby;
import com.KOBAJIbCKUI.ShootingBattles.ShootingGames;
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
            if (shootingGames.lobbiesListWrapper.lobbies.isEmpty()) {
                sender.sendMessage("Lobbies list is empty");
            } else {
                sender.sendMessage("Lobbies list:");
                Lobby lobby;
                for (int i = 0; i < shootingGames.lobbiesListWrapper.lobbies.size(); i++) {
                    lobby = shootingGames.lobbiesListWrapper.lobbies.get(i);
                    sender.sendMessage(lobby.getName() + " - " + (lobby.isInBattle ? "is in battle" : "is standing by"));
                }
            }
            return true;
        }
        return false;
    }
}

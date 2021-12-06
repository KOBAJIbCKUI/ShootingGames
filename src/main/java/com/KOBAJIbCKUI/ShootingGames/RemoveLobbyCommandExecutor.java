package com.KOBAJIbCKUI.ShootingGames;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.NoSuchElementException;

public class RemoveLobbyCommandExecutor implements CommandExecutor {
    private ShootingGames shootingGames;

    public RemoveLobbyCommandExecutor(ShootingGames shootingGames) {
        this.shootingGames = shootingGames;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {

        if (args.length == 1) {

            Lobby lobby;
            try {
                lobby = shootingGames.lobbiesListWrapper.lobbies.stream().filter(o -> o.getName().equals(args[0])).findFirst().get();
            } catch (NoSuchElementException e) {
                sender.sendMessage("Lobby " + args[0] + " doesn't exist");
                return true;
            }

            if (lobby.isInBattle) {
                sender.sendMessage("Lobby " + lobby.getName() + " is in battle");
                return true;
            }

            if (shootingGames.lobbiesListWrapper.lobbies.remove(lobby)) {
                sender.sendMessage("Lobby " + lobby.getName() + " successfully deleted");
                shootingGames.saveLobbies(ShootingGames.SAVE_LOBBY_PATH);
            } else {
                sender.sendMessage("Lobby " + lobby.getName() + " doesn't exist");
            }
            return true;
        }
        return false;
    }
}

package com.KOBAJIbCKUI.ShootingGames;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class PlayerBattleSpawner {

    public static void spawnSpectator(Player spectator, List<Coordinates> spawnPoints) {
        List<Coordinates> spawnPointsCopy = new ArrayList<>(spawnPoints);
        Location randomLocation;

        Coordinates randomCoordinates = spawnPointsCopy.get((int) (Math.random() * spawnPoints.size()));
        randomLocation = new Location(spectator.getWorld(), randomCoordinates.getX(), randomCoordinates.getY(), randomCoordinates.getZ());
        spectator.teleport(randomLocation);

    }

    public static void spawnSpectator(Set<Player> spectators, List<Coordinates> spawnPoints) {

        List<Coordinates> spawnPointsCopy = new ArrayList<>(spawnPoints);
        List<Player> spectatorsCopy = new ArrayList<>(spectators);
        Location randomLocation;
        Player player;

        for (Iterator<Player> iterator = spectatorsCopy.listIterator(); iterator.hasNext(); ) {
            player = iterator.next();

            Coordinates randomCoordinates = spawnPointsCopy.get((int) (Math.random() * spectatorsCopy.size()));
            randomLocation = new Location(player.getWorld(), randomCoordinates.getX(), randomCoordinates.getY(), randomCoordinates.getZ());
            player.teleport(randomLocation);

            spawnPointsCopy.remove(randomCoordinates);
            iterator.remove();
        }
    }

    public static void spawnPlayers(List<Player> players, List<Coordinates> spawnPoints) {

        List<Coordinates> spawnPointsCopy = new ArrayList<>(spawnPoints);
        List<Player> playersCopy = new ArrayList<>(players);
        Location randomLocation;
        Player player;

        for (Iterator<Player> iterator = playersCopy.listIterator(); iterator.hasNext(); ) {
            player = iterator.next();

            Coordinates randomCoordinates = spawnPointsCopy.get((int) (Math.random() * playersCopy.size()));
            randomLocation = new Location(player.getWorld(), randomCoordinates.getX(), randomCoordinates.getY(), randomCoordinates.getZ());
            player.teleport(randomLocation);

            spawnPointsCopy.remove(randomCoordinates);
            iterator.remove();
        }
    }
}

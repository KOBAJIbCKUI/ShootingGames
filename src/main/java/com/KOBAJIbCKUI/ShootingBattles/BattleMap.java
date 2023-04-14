package com.KOBAJIbCKUI.ShootingBattles;

import org.bukkit.Location;

import java.util.Collection;

public interface BattleMap {
    String getName();
    boolean addSpawnPoint(Location spawnPoint);
    boolean removeSpawnPoint(Location spawnPoint);
    boolean removeSpawnPoint(int index);
    int spawnPointsNumber();
    Collection<Location> getSpawnPoints();

}

package com.KOBAJIbCKUI.ShootingBattles;

import org.bukkit.Location;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public final class SimpleMap implements BattleMap {

    private final String name;
    private final List<Location> spawnPoints;

    public SimpleMap(String name) {
        this.name = name;
        this.spawnPoints = new ArrayList<>();
    }

    public SimpleMap(BattleMap copy) {
        this.name = copy.getName();
        this.spawnPoints = (List<Location>) copy.getSpawnPoints();
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public boolean addSpawnPoint(Location spawnPoint) {
        if (spawnPoints.contains(spawnPoint)) {
            return false;
        } else {
            return spawnPoints.add(new Location(spawnPoint.getWorld(), spawnPoint.getX(), spawnPoint.getY(), spawnPoint.getZ(), spawnPoint.getYaw(), spawnPoint.getPitch()));
        }
    }

    @Override
    public boolean removeSpawnPoint(Location spawnPoint) {
        return spawnPoints.remove(spawnPoint);
    }

    @Override
    public boolean removeSpawnPoint(int index) {
        if (spawnPoints.size() <= index) {
            return false;
        }
        spawnPoints.remove(index);
        return true;
    }

    @Override
    public int spawnPointsNumber() {
        return spawnPoints.size();
    }

    @Override
    public Collection<Location> getSpawnPoints() {
        List<Location> result;
        if (spawnPoints.size() != 0) {
            result = new ArrayList<>(spawnPoints.size());
        } else {
            return Collections.emptyList();
        }
        for (Location location : spawnPoints) {
            result.add(new Location(location.getWorld(), location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch()));
        }
        return result;
    }
}

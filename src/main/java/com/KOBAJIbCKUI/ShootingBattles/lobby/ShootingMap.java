package com.KOBAJIbCKUI.ShootingBattles.lobby;

import com.KOBAJIbCKUI.ShootingBattles.util.Coordinates;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ShootingMap {
    private String name;
    private List<Coordinates> spawnPoints;

    public ShootingMap(String name) {
        this.name = name;
        this.spawnPoints = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean addSpawnPoint(Coordinates coordinates) {
        if (spawnPoints.contains(coordinates)) {
            return false;
        }
        return spawnPoints.add(coordinates);
    }

    public boolean removeSpawnPoint(Coordinates coordinates) {
        return spawnPoints.remove(coordinates);
    }

    public boolean removeSpawnPoint(int index) {
        try {
            spawnPoints.remove(index);
        } catch (IndexOutOfBoundsException e) {
            return false;
        }
        return true;
    }

    public List<Coordinates> getSpawnPoints() {
        return spawnPoints;
    }

    public void setSpawnPoints(List<Coordinates> spawnPoints) {
        this.spawnPoints = spawnPoints;
    }

    public int spawnPointsQuantity() {
        return spawnPoints.size();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ShootingMap that = (ShootingMap) o;
        return name.equals(that.name) && spawnPoints.equals(that.spawnPoints);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, spawnPoints);
    }
}

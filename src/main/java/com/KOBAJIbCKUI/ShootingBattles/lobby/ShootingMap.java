package com.KOBAJIbCKUI.ShootingBattles.lobby;

import com.KOBAJIbCKUI.ShootingBattles.Coordinates;

import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@XmlRootElement
@XmlType(name = "shooting_map")
public class ShootingMap implements Serializable {
    private String name;
    private List<Coordinates> spawnPoints = new ArrayList<>();

    public ShootingMap() {}

    public ShootingMap(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @XmlAttribute(name = "name")
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

    @XmlElementWrapper(name = "spawn_points", nillable = true)
    @XmlElement(name = "spawn_point")
    public void setSpawnPoints(List<Coordinates> spawnPoints) {
        this.spawnPoints = spawnPoints;
    }

    public int spawnPointsQuantity() {
        return spawnPoints.size();
    }

}

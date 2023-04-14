package com.KOBAJIbCKUI.ShootingBattles.util;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class Cube {
    private final Player player;
    private Location centralPoint;
    private final Location firstPoint, secondPoint;

    public Cube(Player player) {
        this.player = player;
        this.centralPoint = player.getLocation();
        this.firstPoint = new Location(player.getWorld(), centralPoint.getX() - 3, centralPoint.getY() - 3, centralPoint.getZ() - 3);
        this.secondPoint = new Location(player.getWorld(), centralPoint.getX() + 3, centralPoint.getY() + 3, centralPoint.getZ() + 3);
    }

    public Player getPlayer() {
        return player;
    }

    public void changeCubeCoords() {
        this.centralPoint = this.player.getLocation();

        double centralX = centralPoint.getX();
        double centralY = centralPoint.getY();
        double centralZ = centralPoint.getZ();

        firstPoint.setX(centralX - 3);
        firstPoint.setY(centralY - 3);
        firstPoint.setZ(centralZ - 3);

        secondPoint.setX(centralX + 3);
        secondPoint.setY(centralY + 3);
        secondPoint.setZ(centralZ + 3);

    }

    public boolean isInnerPlayerInCube() {
        Location toSearch = this.player.getLocation();
        double toSearchX = toSearch.getX();
        double toSearchY = toSearch.getY();
        double toSearchZ = toSearch.getZ();
        return (toSearchX >= firstPoint.getX() && toSearchX <= secondPoint.getX()) &&
                (toSearchY >= firstPoint.getY() && toSearchY <= secondPoint.getY()) &&
                (toSearchZ >= firstPoint.getZ() && toSearchZ <= secondPoint.getZ());
    }
}

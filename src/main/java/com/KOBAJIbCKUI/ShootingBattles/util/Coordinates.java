package com.KOBAJIbCKUI.ShootingBattles.util;

import org.bukkit.Location;

import java.util.Objects;

public class Coordinates {

    private double x, y, z;

    public Coordinates() {}

    public Coordinates(Location location) {
        this.x = location.getX();
        this.y = location.getY();
        this.z = location.getZ();
    }

    public Coordinates(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coordinates that = (Coordinates) o;
        return Double.compare(that.x, x) == 0 && Double.compare(that.y, y) == 0 && Double.compare(that.z, z) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, z);
    }

    @Override
    public String toString() {
        return String.format("x %f, y %f, z %f", x, y, z);
    }
}

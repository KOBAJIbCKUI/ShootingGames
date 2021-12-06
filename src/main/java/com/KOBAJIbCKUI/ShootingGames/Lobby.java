package com.KOBAJIbCKUI.ShootingGames;

import org.bukkit.entity.Player;

import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@XmlRootElement
@XmlType(name = "lobby")
public class Lobby implements Serializable {
    private ShootingMap gulagMap = null;
    private String name;
    private Set<ShootingMap> shootingMaps = new HashSet<>();
    private Set<UUID> players = new HashSet<>();
    transient volatile boolean isInBattle = false;
    transient ShootingBattle currentBattle = null;

    public Lobby() {}

    public Lobby(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @XmlAttribute(name = "name")
    public void setName(String name) {
        this.name = name;
    }

    public Set<ShootingMap> getShootingMaps() {
        return shootingMaps;
    }

    @XmlElementWrapper(name = "shooting_maps", nillable = true)
    @XmlElement(name = "shooting_map")
    public void setShootingMaps(Set<ShootingMap> shootingMaps) {
        this.shootingMaps = shootingMaps;
    }

    public boolean addShootingMap (ShootingMap shootingMap) {
        return shootingMaps.add(shootingMap);
    }

    public boolean removeShootingMap (ShootingMap shootingMap) {
        return shootingMaps.remove(shootingMap);
    }

    public int shootingMapsQuantity() {
        return shootingMaps.size();
    }

    public Set<UUID> getPlayers() {
        return players;
    }

    @XmlElementWrapper(name = "players", nillable = true)
    @XmlElement(name = "player")
    public void setPlayers(Set<UUID> players) {
        this.players = players;
    }

    public boolean addPlayer(Player player) {
        return players.add(player.getUniqueId());
    }

    public boolean removePlayer(Player player) {
        return players.remove(player.getUniqueId());
    }

    public int playersQuantity() {
        return players.size();
    }

    @XmlElement(name = "gulag_map")
    public void setGulagMap(ShootingMap gulagMap) {
        this.gulagMap = gulagMap;
    }

    public ShootingMap getGulagMap() {
        return gulagMap;
    }

}

package com.KOBAJIbCKUI.ShootingBattles;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;

import static java.util.stream.Collectors.*;

public class DeathmatchBattle implements Battle {

    private final Map<UUID, PlayerStatus> players;
    private final Set<UUID> spectators;
    private final BattleMap map, gulag;

    public DeathmatchBattle(Set<UUID> players, BattleMap map) {
        this(players, map, null);
    }

    public DeathmatchBattle(Set<UUID> players, BattleMap map, BattleMap gulag) {
        this.players = players.stream().collect(toMap(k -> k, v -> PlayerStatus.ALIVE));
        this.spectators = new HashSet<>();

        this.map = new SimpleMap(map);
        this.gulag = new SimpleMap(gulag);
    }
    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }

    @Override
    public boolean addSpectator(UUID uuid) {
        return spectators.add(uuid);
    }

    @Override
    public boolean removeSpectator(UUID uuid) {
        return spectators.remove(uuid);
    }

    @Override
    public boolean checkWinConditions() {
        return false;
    }

    @Override
    public PlayerStatus getPlayerStatus(UUID uuid) {
        return players.get(uuid);
    }

    @Override
    public void applyToPlayers(Consumer<?> consumer, PlayerStatus status) {

    }

    private void preparePlayer() {

    }

    private void prepareSpectator() {

    }

    private void unPreparePlayer() {

    }

    private void unPrepareSpectator() {

    }
}

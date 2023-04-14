package com.KOBAJIbCKUI.ShootingBattles;

import java.util.UUID;
import java.util.function.Consumer;

public interface Battle {
    void start();
    void stop();
    boolean addSpectator(UUID name);
    boolean removeSpectator(UUID  name);
    boolean checkWinConditions();
    PlayerStatus getPlayerStatus(UUID name);
    void applyToPlayers(Consumer<?> consumer, PlayerStatus status);
}

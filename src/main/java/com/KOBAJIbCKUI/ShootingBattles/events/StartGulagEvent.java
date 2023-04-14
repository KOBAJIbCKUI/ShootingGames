package com.KOBAJIbCKUI.ShootingBattles.events;

import com.KOBAJIbCKUI.ShootingBattles.battle.ShootingBattle;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class StartGulagEvent extends Event {
    public static final HandlerList handlers = new HandlerList();
    private final ShootingBattle shootingBattle;

    public StartGulagEvent(ShootingBattle shootingBattle) {
        this.shootingBattle = shootingBattle;
    }

    public ShootingBattle getShootingBattle() {
        return shootingBattle;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}

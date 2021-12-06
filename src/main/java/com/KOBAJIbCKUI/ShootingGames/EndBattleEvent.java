package com.KOBAJIbCKUI.ShootingGames;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;


public class EndBattleEvent extends Event implements Cancellable {
    public static final HandlerList handlers = new HandlerList();
    private ShootingBattle shootingBattle;
    private boolean cancelled;

    public EndBattleEvent(ShootingBattle shootingBattle) {
        this.shootingBattle = shootingBattle;
    }

    public ShootingBattle getShootingBattle() {
        return shootingBattle;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}

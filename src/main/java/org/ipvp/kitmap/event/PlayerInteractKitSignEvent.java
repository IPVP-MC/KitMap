package org.ipvp.kitmap.event;

import org.bukkit.block.Sign;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

public class PlayerInteractKitSignEvent extends PlayerEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    public enum Type {
        SIGN,
        ITEM_FRAME
    }

    private Sign sign;
    private ItemFrame frame;
    private final Type type;
    private boolean cancel;

    public PlayerInteractKitSignEvent(Player who, Sign sign) {
        super(who);
        this.sign = sign;
        this.type = Type.SIGN;
    }

    public PlayerInteractKitSignEvent(Player who, ItemFrame frame) {
        super(who);
        this.frame = frame;
        this.type = Type.ITEM_FRAME;
    }

    public Type getType() {
        return type;
    }

    public Sign getSign() {
        return sign;
    }

    public ItemFrame getFrame() {
        return frame;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public boolean isCancelled() {
        return cancel;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }
}

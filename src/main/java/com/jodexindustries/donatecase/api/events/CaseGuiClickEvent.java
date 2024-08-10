package com.jodexindustries.donatecase.api.events;

import com.jodexindustries.donatecase.api.data.CaseData;
import org.bukkit.Location;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.InventoryView;
import org.jetbrains.annotations.NotNull;

/**
 * Called when the player clicks on the case gui
 */
public class CaseGuiClickEvent extends InventoryClickEvent {
    private static final HandlerList handlers = new HandlerList();
    private final Location location;
    private final CaseData caseData;
    private final boolean isOpenItem;
    private boolean cancel;

    /**
     * Default constructor
     * @param view Inventory view
     * @param type Slot type
     * @param slot Slot index
     * @param click Click type
     * @param action Action type
     * @param location Location where opened case
     * @param caseData Case data
     * @param isOpenItem Is OPEN item type
     */
    public CaseGuiClickEvent(@NotNull InventoryView view, @NotNull InventoryType.SlotType type,
                             int slot, @NotNull ClickType click, @NotNull InventoryAction action,
                             @NotNull Location location, CaseData caseData, boolean isOpenItem) {
        super(view, type, slot, click, action);
        this.location = location;
        this.caseData = caseData;
        this.isOpenItem = isOpenItem;
    }
    @NotNull
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    /**
     * Get handlers
     * @return handlers list
     */
    public static @NotNull HandlerList getHandlerList() {
        return handlers;
    }

    /**
     * Get case location
     * @return Case location
     */
    public Location getLocation() {
        return location;
    }

    /**
     * Get case data
     * @return case data
     */
    public CaseData getCaseData() {
        return caseData;
    }

    /**
     * Get case type
     * @return case type
     */
    @Deprecated
    public String getCaseType() {
        return caseData.getCaseType();
    }

    /**
     * Check for "OPEN" item
     * @return result
     */
    public boolean isOpenItem() {
        return isOpenItem;
    }

    @Override
    public boolean isCancelled() {
        return cancel;
    }

    /**
     * Cancel click. If you cancel this event, then GUI will not activate an animation from OPEN item.
     * @param toCancel true or false
     */
    @Override
    public void setCancelled(boolean toCancel) {
        this.cancel = toCancel;
    }
}

package com.jodexindustries.donatecase.api.armorstand;

import org.bukkit.Location;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public interface ArmorStandCreator {
    void spawnArmorStand(Location location);
    void setVisible(boolean isVisible);
    void setCustomName(String displayName);
    void teleport(Location location);
    @Deprecated
    void setHelmet(ItemStack item);
    void setEquipment(EquipmentSlot equipmentSlot, ItemStack item);
    void setGravity(boolean hasGravity);
    void setSmall(boolean small);
    Location getLocation();
    void remove();
}

package com.jodexindustries.donatecase.spigot.gui.InventarImpl;

import com.jodexindustries.donatecase.api.DCAPI;
import com.jodexindustries.donatecase.api.data.casedata.CaseDataMaterial;
import com.jodexindustries.donatecase.spigot.BukkitBackend;
import com.jodexindustries.donatecase.spigot.gui.BaseGui;
import com.jodexindustries.donatecase.spigot.gui.GuiExecute;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class PickGui implements GuiExecute {
    private final BukkitBackend backend;
    private final Player player;

    public PickGui(BukkitBackend backend, Player player) {
        this.backend = backend;
        this.player = player;
    }

    @Override
    public boolean execute() {
        DCAPI api =  DCAPI.getInstance();

        Inventory inv = Bukkit.createInventory(null, 45, "DCGui");
        int cs = 12;
        for (String name : api.getCaseManager().getMap().keySet()) {
            ItemStack item = new ItemStack(Material.ENDER_CHEST);
            new CaseDataMaterial().updateMeta(item, name, null, 1, true, null);
            inv.setItem(cs++, item);
        }



        player.openInventory(inv);
        return false;
    }
}

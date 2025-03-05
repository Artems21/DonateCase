package com.jodexindustries.donatecase.spigot.gui.listener;

import com.jodexindustries.donatecase.spigot.BukkitBackend;
import com.jodexindustries.donatecase.spigot.gui.InventarImpl.CaseGui;
import com.jodexindustries.donatecase.spigot.gui.InventarImpl.PickGui;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;

public class InteractListener implements Listener {

    private final BukkitBackend backend;
    private final JavaPlugin plugin;

    public InteractListener(BukkitBackend backend, JavaPlugin plugin) {
        this.backend = backend;
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void InventoryClick(InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();
        Inventory inventory = e.getInventory();
        if (e.getView().getTitle().equals("DCGui")) {
            System.out.println(e.getCurrentItem().getItemMeta().getDisplayName());
            System.out.println(e.getCurrentItem().getData().getItemType().toString());

            if (e.getCurrentItem().getData().getItemType().equals(Material.LEGACY_ENDER_CHEST)) {
                System.out.println(1);
                System.out.println(e.getCurrentItem().getItemMeta().getDisplayName());

                new CaseGui(backend, player, e.getCurrentItem().getItemMeta().getDisplayName());
            }

            e.setCancelled(true);

        }

    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        String message = event.getMessage();

        if (message.equalsIgnoreCase("1")) {
            event.setCancelled(true);

            Bukkit.getScheduler().runTask(plugin, () -> {
                new PickGui(backend, player).execute();
            });
        }
    }
}

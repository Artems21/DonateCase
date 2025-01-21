package com.jodexindustries.donatecase.tools;

import com.jodexindustries.donatecase.BukkitBackend;
import com.jodexindustries.donatecase.api.armorstand.ArmorStandCreator;
import com.jodexindustries.donatecase.api.armorstand.EntityArmorStandCreator;
import com.jodexindustries.donatecase.api.armorstand.PacketArmorStandCreator;
import com.jodexindustries.donatecase.api.data.storage.CaseLocation;
import com.jodexindustries.donatecase.api.tools.DCTools;
import com.jodexindustries.donatecase.api.tools.PAPI;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Firework;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.util.Vector;

import java.util.Random;

public class ToolsImpl implements DCToolsBukkit {

    private final BukkitBackend backend;

    public ToolsImpl(BukkitBackend backend) {
        this.backend = backend;
    }

    @Override
    public ArmorStandCreator createArmorStand(CaseLocation location) {
        if (backend.getPacketEventsSupport().isUsePackets()) {
            return new PacketArmorStandCreator(location);
        } else {
            return new EntityArmorStandCreator(BukkitUtils.toBukkit(location));
        }
    }

    @Override
    public PAPI getPAPI() {
        return backend.getPAPI();
    }

    @Override
    public Object loadCaseItem(String id) {
        if(id == null) return new ItemStack(Material.AIR);

        Material material = Material.getMaterial(id);

        if (material == null) {
            Object item = DCTools.getItemFromManager(id);
            return item == null ? new ItemStack(Material.AIR) : item;
        } else {
            return new ItemStack(material);
        }
    }

    @Override
    public void launchFirework(Location location) {
        Random r = new Random();
        World world = location.getWorld();
        if (world == null) return;

        Firework firework = world.spawn(location.subtract(new Vector(0.0, 0.5, 0.0)), Firework.class);
        FireworkMeta meta = firework.getFireworkMeta();
        Color[] color = new Color[]{Color.RED, Color.AQUA, Color.GREEN, Color.ORANGE, Color.LIME, Color.BLUE, Color.MAROON, Color.WHITE};
        meta.addEffect(FireworkEffect.builder().flicker(false).with(FireworkEffect.Type.BALL).trail(false).withColor(color[r.nextInt(color.length)], color[r.nextInt(color.length)], color[r.nextInt(color.length)]).build());
        firework.setFireworkMeta(meta);
        firework.setMetadata("case", new FixedMetadataValue(backend.getPlugin(), "case"));
        firework.detonate();
    }

}

package com.jodexindustries.donatecase.holograms;

import com.jodexindustries.donatecase.api.data.casedata.CaseData;
import com.jodexindustries.donatecase.api.data.storage.CaseLocation;
import com.jodexindustries.donatecase.api.manager.HologramManager;
import com.jodexindustries.donatecase.tools.BukkitUtils;
import de.oliver.fancyholograms.api.FancyHologramsPlugin;
import de.oliver.fancyholograms.api.data.*;
import de.oliver.fancyholograms.api.hologram.Hologram;
import de.oliver.fancyholograms.api.hologram.HologramType;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.configurate.ConfigurationNode;

import java.util.HashMap;
import java.util.UUID;

public class FancyHologramsImpl implements HologramManager {

    private final de.oliver.fancyholograms.api.HologramManager manager = FancyHologramsPlugin.get().getHologramManager();
    private final HashMap<CaseLocation, Hologram> holograms = new HashMap<>();

    @Override
    public void create(CaseLocation block, CaseData.Hologram caseHologram) {
        ConfigurationNode node = caseHologram.getNode();

        HologramType type = HologramType.getByName(node.getString("type"));
        if (type == null) return;

        Location location = BukkitUtils.toBukkit(block).add(.5, caseHologram.getHeight(), .5);

        String name = "DonateCase-" + UUID.randomUUID();
        DisplayHologramData hologramData = getData(type, name, location);
        read(hologramData, node);

        Hologram hologram = manager.create(hologramData);
        manager.addHologram(hologram);
        this.holograms.put(block, hologram);
    }

    @Override
    public void remove(CaseLocation block) {
        if (!this.holograms.containsKey(block)) return;

        Hologram hologram = this.holograms.get(block);
        this.holograms.remove(block);
        manager.removeHologram(hologram);
    }

    @Override
    public void remove() {
        for (Hologram hologram : this.holograms.values()) {
            manager.removeHologram(hologram);
        }
        this.holograms.clear();
    }

    private static @NotNull DisplayHologramData getData(HologramType type, String name, Location location) {
        DisplayHologramData hologramData;

        switch (type) {
            case BLOCK:
                hologramData = new BlockHologramData(name, location);
                break;
            case ITEM:
                hologramData = new ItemHologramData(name, location);
                break;
            case TEXT:
                hologramData = new TextHologramData(name, location);
                break;
            default:
                hologramData = new DisplayHologramData(name, type, location);
                break;
        }

        return hologramData;
    }

    private static void read(DisplayHologramData data, ConfigurationNode node) {
        // TODO Read hologram data
    }

}
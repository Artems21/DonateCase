package com.jodexindustries.donatecase.tools;

import java.text.NumberFormat;
import java.util.Objects;

import com.jodexindustries.donatecase.dc.Main;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

public class Placeholder extends PlaceholderExpansion {
    public Placeholder() {
    }

    public @NotNull String getAuthor() {
        return "JodexIndustries";
    }

    public @NotNull String getIdentifier() {
        return "DonateCase";
    }

    public @NotNull String getVersion() {
        return Main.instance.getDescription().getVersion();
    }

    public boolean persist() {
        return true;
    }

    public String onRequest(OfflinePlayer player, String params) {
        if (params.startsWith("keys_")) {
            String[] parts = params.split("_", 2);
            int s;
            if (Main.Tconfig) {
                s = CustomConfig.getKeys().getInt("DonatCase.Cases." + parts[1] + "." + Objects.requireNonNull(player.getName()).toLowerCase());
            } else {
                s = Main.mysql.getKey(parts[1], Objects.requireNonNull(player.getName()).toLowerCase());
            }
            return NumberFormat.getNumberInstance().format(s);
        } else {
            return null;
        }
    }
}
package com.jodexindustries.donatecase.animations;

import com.jodexindustries.donatecase.api.Case;
import com.jodexindustries.donatecase.api.armorstand.ArmorStandEulerAngle;
import com.jodexindustries.donatecase.api.armorstand.ArmorStandCreator;
import com.jodexindustries.donatecase.api.data.JavaAnimation;
import com.jodexindustries.donatecase.tools.Tools;
import org.bukkit.*;
import org.bukkit.entity.Firework;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.scheduler.BukkitTask;

import java.util.function.Consumer;

public class FireworkAnimation extends JavaAnimation {
    private final EquipmentSlot itemSlot;
    private final ArmorStandEulerAngle armorStandEulerAngle;

    public FireworkAnimation() {
        itemSlot = EquipmentSlot.valueOf(Case.getConfig().getAnimations().getString("Firework.ItemSlot", "HEAD").toUpperCase());
        armorStandEulerAngle = Tools.getArmorStandEulerAngle("Firework.Pose");
    }

    @Override
    public void start() {
        String displayName = getWinItem().getMaterial().getDisplayName();
        getWinItem().getMaterial().setDisplayName(Case.getInstance().papi.setPlaceholders(getPlayer(), displayName));
        getLocation().add(0.5, 1, 0.5);
        ArmorStandCreator as = Tools.createArmorStand(getLocation());

        boolean small = Case.getConfig().getAnimations().getBoolean("Firework.SmallArmorStand", true);
        as.setSmall(small);
        as.setVisible(false);
        as.setGravity(false);
        as.spawn();
        Bukkit.getScheduler().runTaskTimer(Case.getInstance(), new Task(as), 0L, 2L);
    }

    private class Task implements Consumer<BukkitTask> {

        private int i; //ticks count
        private final Location l;
        private final ArmorStandCreator as;
        private final World world;

        public Task(ArmorStandCreator as) {
            this.as = as;
            this.l = as.getLocation();
            world = l.getWorld() != null ? l.getWorld() : getPlayer().getWorld();
        }

        @Override
        public void accept(BukkitTask task) {
            if (this.i == 1) {
                Firework firework = world.spawn(l, Firework.class);
                FireworkMeta data = firework.getFireworkMeta();
                data.addEffects(FireworkEffect.builder().withColor(Color.PURPLE).withColor(Color.RED).with(FireworkEffect.Type.BALL).withFlicker().build());
                for (String color : Case.getConfig().getAnimations().getStringList("Firework.FireworkColors")) {
                    data.addEffect(FireworkEffect.builder().withColor(Tools.parseColor(color)).build());
                }
                data.setPower(Case.getConfig().getAnimations().getInt("Firework.Power"));
                firework.setFireworkMeta(data);
            }
            if (this.i >= 7) {
                l.setYaw(l.getYaw() + 20F);
                as.teleport(l);

                if (this.i == 10) {
                    if (getWinItem().getMaterial().getItemStack().getType() != Material.AIR) {
                        as.setEquipment(itemSlot, getWinItem().getMaterial().getItemStack());
                    }
                    as.setAngle(armorStandEulerAngle);
                    if (getWinItem().getMaterial().getDisplayName() != null && !getWinItem().getMaterial().getDisplayName().isEmpty())
                        as.setCustomNameVisible(true);
                    as.setCustomName(getWinItem().getMaterial().getDisplayName());
                    as.updateMeta();
                    Case.animationPreEnd(getCaseData(), getPlayer(), getUuid(), getWinItem());
                }
                if (this.i >= 30) {
                    as.remove();
                    task.cancel();
                    Case.animationEnd(getCaseData(), getPlayer(), getUuid(), getWinItem());
                }
            }

            ++this.i;
        }
    }
}
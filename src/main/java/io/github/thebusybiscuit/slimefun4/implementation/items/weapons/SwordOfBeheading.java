package io.github.thebusybiscuit.slimefun4.implementation.items.weapons;

import io.github.thebusybiscuit.slimefun4.api.items.ItemSetting;
import io.github.thebusybiscuit.slimefun4.core.handlers.EntityKillHandler;
import io.github.thebusybiscuit.slimefun4.implementation.items.SimpleSlimefunItem;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.NotNull;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class SwordOfBeheading extends SimpleSlimefunItem<EntityKillHandler> {

    private final ItemSetting<Integer> chanceZombie = new ItemSetting<>("chance.ZOMBIE", 40);
    private final ItemSetting<Integer> chanceSkeleton = new ItemSetting<>("chance.SKELETON", 40);
    private final ItemSetting<Integer> chanceWitherSkeleton = new ItemSetting<>("chance.WITHER_SKELETON", 25);
    private final ItemSetting<Integer> chanceCreeper = new ItemSetting<>("chance.CREEPER", 40);
    private final ItemSetting<Integer> chancePlayer = new ItemSetting<>("chance.PLAYER", 70);

    public SwordOfBeheading(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(category, item, recipeType, recipe);

        addItemSetting(chanceZombie, chanceSkeleton, chanceWitherSkeleton, chanceCreeper, chancePlayer);
    }

    @Override
    public @NotNull EntityKillHandler getItemHandler() {
        return (e, entity, killer, item) -> {
            Random random = ThreadLocalRandom.current();

            if (e.getEntity() instanceof Zombie) {
                if (random.nextInt(100) < chanceZombie.getValue()) {
                    e.getDrops().add(new ItemStack(Material.ZOMBIE_HEAD));
                }
            } else if (e.getEntity() instanceof WitherSkeleton) {
                if (random.nextInt(100) < chanceWitherSkeleton.getValue()) {
                    e.getDrops().add(new ItemStack(Material.WITHER_SKELETON_SKULL));
                }
            }
            else if (e.getEntity() instanceof Skeleton) {
                if (random.nextInt(100) < chanceSkeleton.getValue()) {
                    e.getDrops().add(new ItemStack(Material.SKELETON_SKULL));
                }
            }
            else if (e.getEntity() instanceof Creeper) {
                if (random.nextInt(100) < chanceCreeper.getValue()) {
                    e.getDrops().add(new ItemStack(Material.CREEPER_HEAD));
                }
            } else if (e.getEntity() instanceof Player && random.nextInt(100) < chancePlayer.getValue()) {
                ItemStack skull = new ItemStack(Material.PLAYER_HEAD);
                ItemMeta meta = skull.getItemMeta();
                ((SkullMeta) meta).setOwningPlayer((Player) e.getEntity());
                skull.setItemMeta(meta);

                e.getDrops().add(skull);
            }
        };
    }

}
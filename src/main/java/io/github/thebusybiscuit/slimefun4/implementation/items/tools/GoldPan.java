package io.github.thebusybiscuit.slimefun4.implementation.items.tools;

import io.github.thebusybiscuit.cscorelib2.collections.RandomizedSet;
import io.github.thebusybiscuit.cscorelib2.protection.ProtectableAction;
import io.github.thebusybiscuit.slimefun4.api.items.ItemSetting;
import io.github.thebusybiscuit.slimefun4.core.attributes.RecipeDisplayItem;
import io.github.thebusybiscuit.slimefun4.core.handlers.ItemUseHandler;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import io.github.thebusybiscuit.slimefun4.implementation.items.SimpleSlimefunItem;
import io.github.thebusybiscuit.slimefun4.implementation.items.electric.machines.ElectricGoldPan;
import io.github.thebusybiscuit.slimefun4.implementation.items.multiblocks.AutomatedPanningMachine;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * A {@link GoldPan} is a {@link SlimefunItem} which allows you to obtain various
 * resources from Gravel.
 *
 * @author TheBusyBiscuit
 * @see NetherGoldPan
 * @see AutomatedPanningMachine
 * @see ElectricGoldPan
 */
public class GoldPan extends SimpleSlimefunItem<ItemUseHandler> implements RecipeDisplayItem {

    private final RandomizedSet<ItemStack> randomizer = new RandomizedSet<>();
    private final Set<GoldPanDrop> drops = new HashSet<>();

    public GoldPan(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(category, item, recipeType, recipe);

        drops.addAll(getGoldPanDrops());
        addItemSetting(drops.toArray(new GoldPanDrop[0]));
    }

    protected Material getInput() {
        return Material.GRAVEL;
    }

    protected Set<GoldPanDrop> getGoldPanDrops() {
        Set<GoldPanDrop> settings = new HashSet<>();

        settings.add(new GoldPanDrop("chance.FLINT", 40, new ItemStack(Material.FLINT)));
        settings.add(new GoldPanDrop("chance.CLAY", 20, new ItemStack(Material.CLAY_BALL)));
        settings.add(new GoldPanDrop("chance.SIFTED_ORE", 35, SlimefunItems.SIFTED_ORE));
        settings.add(new GoldPanDrop("chance.IRON_NUGGET", 5, new ItemStack(Material.IRON_NUGGET)));

        return settings;
    }

    @Override
    public void postRegister() {
        super.postRegister();
        updateRandomizer();
    }

    protected void updateRandomizer() {
        randomizer.clear();

        for (GoldPanDrop setting : drops) {
            randomizer.add(setting.getOutput(), setting.getValue());
        }

        if (randomizer.sumWeights() < 100) {
            randomizer.add(new ItemStack(Material.AIR), 100 - randomizer.sumWeights());
        }
    }

    public ItemStack getRandomOutput() {
        return randomizer.getRandom();
    }

    @Override
    public @NotNull String getLabelLocalPath() {
        return "guide.tooltips.recipes.gold-pan";
    }

    @Override
    public @NotNull ItemUseHandler getItemHandler() {
        return e -> {
            Optional<Block> block = e.getClickedBlock();

            if (block.isPresent()) {
                Block b = block.get();

                if (b.getType() == getInput() && SlimefunPlugin.getProtectionManager().hasPermission(e.getPlayer(), b.getLocation(), ProtectableAction.BREAK_BLOCK)) {
                    ItemStack output = getRandomOutput();

                    b.getWorld().playEffect(b.getLocation(), Effect.STEP_SOUND, b.getType());
                    b.setType(Material.AIR);

                    if (output.getType() != Material.AIR) {
                        b.getWorld().dropItemNaturally(b.getLocation(), output.clone());
                    }
                }
            }

            e.cancel();
        };
    }

    @Override
    public @NotNull List<ItemStack> getDisplayRecipes() {
        List<ItemStack> recipes = new LinkedList<>();

        for (GoldPanDrop drop : drops) {
            if (drop.getValue() > 0) {
                recipes.add(new ItemStack(getInput()));
                recipes.add(drop.getOutput());
            }
        }

        return recipes;
    }

    public class GoldPanDrop extends ItemSetting<Integer> {

        private final ItemStack output;

        protected GoldPanDrop(String key, int defaultValue, ItemStack output) {
            super(key, defaultValue);

            this.output = output;
        }

        @Override
        public boolean validateInput(Integer input) {
            return super.validateInput(input) && input >= 0;
        }


        public ItemStack getOutput() {
            return output;
        }

        @Override
        public void update(Integer newValue) {
            super.update(newValue);
            updateRandomizer();
        }

    }

}
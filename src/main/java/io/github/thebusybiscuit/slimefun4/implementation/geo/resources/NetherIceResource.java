package io.github.thebusybiscuit.slimefun4.implementation.geo.resources;

import org.bukkit.block.Biome;
import org.bukkit.inventory.ItemStack;

import me.mrCookieSlime.Slimefun.GEO.OreGenResource;
import me.mrCookieSlime.Slimefun.Lists.SlimefunItems;

public class NetherIceResource implements OreGenResource {
	
	@Override
	public int getDefaultSupply(Biome biome) {
		return biome == Biome.NETHER ? 32: 0;
	}

	@Override
	public String getName() {
		return "下界冰";
	}

	@Override
	public ItemStack getIcon() {
		return SlimefunItems.NETHER_ICE.clone();
	}

	@Override
	public String getMeasurementUnit() {
		return "块";
	}

	@Override
	public boolean isLiquid() {
		return false;
	}

}

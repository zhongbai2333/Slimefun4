package io.github.thebusybiscuit.slimefun4.core.commands.subcommands;

import io.github.thebusybiscuit.slimefun4.core.commands.SlimefunCommand;
import io.github.thebusybiscuit.slimefun4.core.commands.SubCommand;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

class DebugFishCommand extends SubCommand {

    DebugFishCommand(SlimefunPlugin plugin, SlimefunCommand cmd) {
        super(plugin, cmd, "debug_fish", true);
    }

    @Override
    public void onExecute(@NotNull CommandSender sender, String @NotNull [] args) {
        if (sender instanceof Player && sender.hasPermission("slimefun.debugging")) {
            ((Player) sender).getInventory().addItem(SlimefunItems.DEBUG_FISH.clone());
        } else {
            SlimefunPlugin.getLocalization().sendMessage(sender, "messages.no-permission", true);
        }
    }

}

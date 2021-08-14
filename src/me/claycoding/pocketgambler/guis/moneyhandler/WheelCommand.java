package me.claycoding.pocketgambler.guis.moneyhandler;

import me.claycoding.pocketgambler.Main;
import me.claycoding.pocketgambler.util.ConsoleMessages;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public record WheelCommand(Main plugin) implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (!(sender instanceof Player player)) {
            Bukkit.getConsoleSender().sendMessage(ConsoleMessages.needsInstanceOfPlayer());
            return true;
        }
        plugin.wheel.open(player);
        return true;
    }

}

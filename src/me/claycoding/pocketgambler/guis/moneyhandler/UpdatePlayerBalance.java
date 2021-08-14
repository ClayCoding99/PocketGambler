package me.claycoding.pocketgambler.guis.moneyhandler;

import me.claycoding.pocketgambler.Main;
import me.claycoding.pocketgambler.util.Color;
import me.claycoding.pocketgambler.util.ConfigManager;
import me.claycoding.pocketgambler.util.ConsoleMessages;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public record UpdatePlayerBalance(Main plugin) implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if (args.length != 2) {
            sender.sendMessage(Color.colorize("&cUse: /give (player) (amount)"));
            return true;
        }
        for (Player player : Bukkit.getServer().getOnlinePlayers()) {
            if (player.getName().equalsIgnoreCase(args[0])) {
                if (player.isOp()) {
                    try {
                        double amount = Double.parseDouble(args[1]);
                        double previousBalance = plugin.playerConfig.getDouble(player.getName() + ".balance");
                        updateBalance(player, amount);
                        double currentBalance = plugin.playerConfig.getDouble(player.getName() + ".balance");
                        player.sendMessage(Color.colorize("&6" + player.getName() + "'s") + Color.colorize("balance has been changed from") +
                                Color.colorize("&5 ") + previousBalance + Color.colorize(" &6to ") + Color.colorize("&5 ") + currentBalance);
                    } catch (NumberFormatException exception) {
                        sender.sendMessage(Color.colorize("&4Error: invalid number!"));
                    }
                    return true;
                } else {
                    player.sendMessage(ConsoleMessages.doNotHavePermission());
                }
            }
        }
        sender.sendMessage(Color.colorize("&4Player not found!"));
        return true;
    }

    private void updateBalance(Player player, double amount) {
        double playerCash = plugin.playerConfig.getDouble(player.getName() + ".balance");
        plugin.playerConfig.set(player.getName() + ".balance", playerCash + amount);
        ConfigManager.saveConfig(plugin.playerConfig, plugin.playerPath);
    }

}

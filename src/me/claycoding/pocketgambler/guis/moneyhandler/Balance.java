package me.claycoding.pocketgambler.guis.moneyhandler;

import me.claycoding.pocketgambler.Main;
import me.claycoding.pocketgambler.util.Color;
import me.claycoding.pocketgambler.util.ConsoleMessages;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public record Balance(Main plugin) implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (args.length > 1) {
            sender.sendMessage("&4Use: /balance (player)");
            return true;
        }
        if (args.length == 1) {
            for (Player player : Bukkit.getServer().getOnlinePlayers()) {
                if (player.getName().equalsIgnoreCase(args[0])) {
                    double balance = plugin.playerConfig.getDouble(player.getName() + ".balance");
                    sender.sendMessage(Color.colorize("&6" + player.getName()) + Color.colorize(" &5's current balance is ")
                            + Color.colorize("&6" + balance));
                    return true;
                }
            }
            sender.sendMessage(Color.colorize("&4Player not found!"));
            return true;
        }
        if (!(sender instanceof Player player)) {
            Bukkit.getConsoleSender().sendMessage(ConsoleMessages.needsInstanceOfPlayer());
            return true;
        }
        double balance = plugin.playerConfig.getDouble(player.getName() + ".balance");
        sender.sendMessage(Color.colorize("&5Your current balance is: ") + Color.colorize("&6" + balance));
        return true;
    }

}

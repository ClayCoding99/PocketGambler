package me.claycoding.pocketgambler.util;

import org.bukkit.ChatColor;

public class Color {

    public static String colorize(String text){
        return ChatColor.translateAlternateColorCodes('&', text);
    }

    public static String strip(String text){
        return ChatColor.stripColor(text);
    }

}

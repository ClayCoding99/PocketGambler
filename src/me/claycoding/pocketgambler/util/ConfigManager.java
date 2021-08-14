package me.claycoding.pocketgambler.util;

import me.claycoding.pocketgambler.Main;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.craftbukkit.libs.org.apache.commons.io.IOUtils;
import org.bukkit.craftbukkit.libs.org.apache.commons.io.input.CharSequenceReader;
import org.bukkit.inventory.Inventory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.List;

public class ConfigManager {

    public static YamlConfiguration loadConfig(File configFile, boolean copyDefaults, Main plugin){
        YamlConfiguration tmp = null;
        if (!configFile.exists()) {
            //generate a new config file from internal resources
            try {
                FileConfiguration configFileConfiguration = YamlConfiguration.loadConfiguration(getReaderFromStream(plugin.getResource(configFile.getName())));
                configFileConfiguration.save(configFile);
                tmp = YamlConfiguration.loadConfiguration(new File(plugin.getDataFolder() + File.separator + configFile.getName()));
            } catch (IOException var11) {
                Bukkit.getConsoleSender().sendMessage(ConsoleMessages.couldNotSaveFileWarning(Main.pluginName, configFile.getName()));
            }
        }else if(copyDefaults){
            //check if the config file has any missing elements
            try {
                tmp = YamlConfiguration.loadConfiguration(configFile);
                YamlConfiguration configFileConfiguration = YamlConfiguration.loadConfiguration(getReaderFromStream(plugin.getResource(configFile.getName())));
                tmp.addDefaults(configFileConfiguration);
                tmp.options().copyDefaults(true);
                tmp.save(new File(plugin.getDataFolder() + File.separator + configFile.getName()));
            } catch (IOException var10) {
                Bukkit.getConsoleSender().sendMessage(ConsoleMessages.couldNotSaveFileWarning(Main.pluginName, configFile.getName()));
            }
        }else{
            YamlConfiguration.loadConfiguration(configFile);
        }
        return tmp;
    }

    private static Reader getReaderFromStream(InputStream initialStream) throws IOException {
        //this reads the encrypted resource files in the jar file
        byte[] buffer = IOUtils.toByteArray(initialStream);
        return new CharSequenceReader(new String(buffer));
    }

    // saves the config
    public static void saveConfig(YamlConfiguration config, String path){
        try{
            config.save(path);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    // retrieves inventory from yaml file
    public static Inventory getInventoryFromFile(YamlConfiguration config, String name){
        int size = config.getInt(name + ".size");
        Inventory inv = Bukkit.createInventory(null, size, name);
        for(int i = 0; i < size; i++){
            inv.setItem(i, config.getItemStack(name + "." + i));
        }
        if(inv.isEmpty()){
            Bukkit.getConsoleSender().sendMessage(ConsoleMessages.nullGuiWarning(Main.pluginName, name));
        }
        return inv;
    }

    // saves inventory to config file
    public static void saveInventoryToConfig(YamlConfiguration config, String name, Inventory inventory){
        if(inventory == null){
            Bukkit.getConsoleSender().sendMessage(ConsoleMessages.nullGuiWarning(Main.pluginName, name));
            return;
        }
        config.set(name, "");
        for(int i = 0; i < inventory.getSize(); i++){
            config.set(name + "." + i, inventory.getItem(i));
        }
        config.set(name + ".size", inventory.getSize());
        saveConfig(config, name);
    }

    // saves a list to a config
    public static void saveListToConfig(YamlConfiguration config, String name, List<?> list){
        if(list == null){
            Bukkit.getConsoleSender().sendMessage(ConsoleMessages.nullDataWarning(name));
            return;
        }
        config.set(name, list);
        saveConfig(config, name);
    }

    // gets a list from a config
    public static List<?> getListFromConfig(YamlConfiguration config, String name){
        List list = config.getList(name);
        if(list == null){
            Bukkit.getConsoleSender().sendMessage(ConsoleMessages.nullDataWarning(name));
            return null;
        }
        return list;
    }

}

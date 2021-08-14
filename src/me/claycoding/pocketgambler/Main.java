package me.claycoding.pocketgambler;

import me.claycoding.pocketgambler.guis.moneyhandler.Balance;
import me.claycoding.pocketgambler.guis.moneyhandler.GambleCommand;
import me.claycoding.pocketgambler.guis.moneyhandler.UpdatePlayerBalance;
import me.claycoding.pocketgambler.guis.moneyhandler.WheelCommand;
import me.claycoding.pocketgambler.guis.slots.SlotsGame;
import me.claycoding.pocketgambler.guis.slots.SlotsMenu;
import me.claycoding.pocketgambler.guis.wheel.SpinWheel;
import me.claycoding.pocketgambler.util.ConfigManager;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.Objects;

public class Main extends JavaPlugin {

    public static final String pluginName = "PocketGambler";

    public YamlConfiguration slotsMenuConfig;
    public YamlConfiguration slotsGameConfig;
    public YamlConfiguration playerConfig;

    public final String slotsMenuPath = "slotsMenu.yml";
    public final String slotsGamePath = "slotsGame.yml";
    public final String playerPath = "player.yml";

    public SlotsMenu slotsMenu;
    public SlotsGame slotsGame;
    public SpinWheel wheel;

    @Override
    public void onEnable(){
        initFiles();
        registerCommands();

        slotsMenu = new SlotsMenu(this);
        slotsGame = new SlotsGame(this);
        wheel = new SpinWheel(this);
        registerListeners();
    }

    @Override
    public void onDisable(){
        ConfigManager.saveConfig(slotsMenuConfig, slotsMenuPath);
        ConfigManager.saveConfig(slotsGameConfig, slotsGamePath);
        ConfigManager.saveConfig(playerConfig, playerPath);
    }

    private void initFiles(){
        slotsMenuConfig = ConfigManager.loadConfig(new File(this.getDataFolder() + File.separator + slotsMenuPath), true, this);
        slotsGameConfig = ConfigManager.loadConfig(new File(this.getDataFolder() + File.separator + slotsGamePath), true, this);
        playerConfig = ConfigManager.loadConfig(new File(this.getDataFolder() + File.separator + playerPath), true, this);
    }

    private void registerCommands(){
        Objects.requireNonNull(this.getCommand("gamble")).setExecutor(new GambleCommand(this));
        Objects.requireNonNull(this.getCommand("give")).setExecutor(new UpdatePlayerBalance(this));
        Objects.requireNonNull(this.getCommand("balance")).setExecutor(new Balance(this));
        Objects.requireNonNull(this.getCommand("wheel")).setExecutor(new WheelCommand(this));
    }

    private void registerListeners(){
        this.getServer().getPluginManager().registerEvents(slotsGame, this);
        this.getServer().getPluginManager().registerEvents(slotsMenu, this);
        this.getServer().getPluginManager().registerEvents(wheel, this);
    }

}

package me.claycoding.pocketgambler.guis;

import me.claycoding.pocketgambler.Main;
import me.claycoding.pocketgambler.util.ConfigManager;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public abstract class AbstractGamble {

    protected boolean isRunning = false;
    protected BukkitTask gambleRunnable;
    protected BukkitTask afterGambleRunnable;
    protected final Main plugin;
    protected Inventory inventory;

    public abstract void start(Player player);
    public abstract void getResults(Player player);

    public AbstractGamble(Main plugin){
        this.plugin = plugin;
        this.gambleRunnable = null;
    }

    public void open(Player player){
        player.openInventory(this.inventory);
        player.updateInventory();
    }

    protected void updateBalance(Player player, double cost){
        double playerCash = plugin.playerConfig.getDouble(player.getName() + ".balance");
        plugin.playerConfig.set(player.getName() + ".balance", playerCash + cost);
        ConfigManager.saveConfig(plugin.playerConfig, plugin.playerPath);
    }

    protected void afterGambleSoundEffects(Player player, boolean win){
        if(afterGambleRunnable != null){
            if(!afterGambleRunnable.isCancelled()){
                afterGambleRunnable.cancel();
            }
        }
        // 20 ticks = 1 second
        long afterSoundEffectDelay = isRunning ? 0L : 20L; // stop the sound effect from happening mid spin if the player is spamming
        afterGambleRunnable = new BukkitRunnable() {
            @Override
            public void run() {
                if(win){
                    player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_CHIME, 50, 50);
                }else{
                    player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_CHIME, 50, -50);
                }
            }
        }.runTaskLater(plugin, afterSoundEffectDelay);
    }

    public Inventory getInventory(){
        return this.inventory;
    }

}

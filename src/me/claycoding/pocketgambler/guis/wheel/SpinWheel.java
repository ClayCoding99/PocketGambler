package me.claycoding.pocketgambler.guis.wheel;

import me.claycoding.pocketgambler.Main;
import me.claycoding.pocketgambler.guis.AbstractGamble;
import me.claycoding.pocketgambler.util.ConfigManager;
import me.claycoding.pocketgambler.util.PocketGamblerObject;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class SpinWheel extends AbstractGamble implements Listener {

    // TODO: change 3x to 4x, 5x to 6x, 10x to 12x
    // TODO: update each slot after a player has put money in
    // TODO: fix the bug where the player always loses
    // TODO: clean up code so it is easier to read

    private final int[] spinLocations, stickLocations;
    private final double cost = 100.00;
    private int index = 0;
    private static final Random random = new Random();

    private final String moneyPitMessage = "click to add $" + cost + " each time.";
    private double totalPitCash = 0.00;
    private double pit2xCash = 0.00;
    private double pit4xCash = 0.00;
    private double pit6xCash = 0.00;
    private double pit12xCash = 0.00;

    private final int startLocation = 31;
    private final int goBackSlot = 45;
    private final int moneySlot = 4;
    private final int slot2x = 6;
    private final int slot4x = 7;
    private final int slot6x = 8;
    private final int slot12x = 17;

    private final ItemStack mult2 = PocketGamblerObject.createGuiItem(Material.COAL, ChatColor.DARK_GREEN + "2x", 1);
    private final ItemStack mult4 = PocketGamblerObject.createGuiItem(Material.IRON_INGOT, ChatColor.GREEN + "4x", 1);
    private final ItemStack mult6 = PocketGamblerObject.createGuiItem(Material.GOLD_INGOT, ChatColor.AQUA + "6x", 1);
    private final ItemStack mult12 = PocketGamblerObject.createGuiItem(Material.DIAMOND, ChatColor.DARK_PURPLE + "12x", 1);

    // -> 14 -> 13 -> 12 -> 20 -> 29 -> 38 -> 48 -> 49 -> 50 -> 42 -> 33 -> 24 --| spin order from top right and left around
    // <-------------------------------------------------------------------------|

    public SpinWheel(Main plugin){
        super(plugin);
        this.spinLocations = new int[] {14, 13, 12, 20, 29, 38, 48, 49, 50, 42, 33, 24};
        this.stickLocations = new int[] {22, 30, 32, 40};
        this.inventory = setInventoryContents();
        updateCashPits();
    }

    private Inventory setInventoryContents(){
        int inventorySize = 54;
        String inventoryTitle = "Spin Wheel";
        Inventory wheel = Bukkit.createInventory(null, inventorySize, inventoryTitle);
        for(int i = 0; i < inventory.getSize(); i++){
            if(i % 2 == 0){
                wheel.setItem(i, mult2);
            }else if(i == this.spinLocations[1] || i == this.spinLocations[3] || i == this.spinLocations[11]) {
                wheel.setItem(i, mult4);
            }else if(i == this.spinLocations[5] || i == this.spinLocations[9]){
                wheel.setItem(i, mult6);
            }else if(i == this.spinLocations[7]){
                wheel.setItem(i, mult12);
            }else if(i == this.stickLocations[0] || i == this.stickLocations[1] || i == this.stickLocations[2] || i == this.stickLocations[3]){
                wheel.setItem(i, new ItemStack(Material.STICK, 1));
            }else{
                wheel.setItem(i, new ItemStack(Material.AIR, 1));
            }
        }
        wheel.setItem(startLocation, PocketGamblerObject.createGuiItem(Material.OAK_BUTTON, ChatColor.GREEN + "Click to spin!", 1));
        wheel.setItem(goBackSlot, PocketGamblerObject.createGuiItem(Material.PAPER, "click to go back", 1));
        return wheel;
    }

    private void updateCashPits(){
        inventory.setItem(moneySlot, PocketGamblerObject.createGuiItem(Material.CAULDRON, ChatColor.GREEN + moneyPitMessage, 1, ChatColor.GOLD + "Current cash in pit: " + totalPitCash)); // TODO: show winnable cash as well
        inventory.setItem(slot2x, PocketGamblerObject.createGuiItem(Material.MINECART, ChatColor.DARK_GREEN + moneyPitMessage, 1, ChatColor.DARK_GREEN + "Current cash in pit2x: " + pit2xCash));
        inventory.setItem(slot4x, PocketGamblerObject.createGuiItem(Material.MINECART, ChatColor.GREEN + moneyPitMessage, 1, ChatColor.GREEN + "Current cash in pit3x: " + pit4xCash));
        inventory.setItem(slot6x, PocketGamblerObject.createGuiItem(Material.MINECART, ChatColor.AQUA + moneyPitMessage, 1, ChatColor.AQUA + "Current cash in pit5x: " + pit6xCash));
        inventory.setItem(slot12x, PocketGamblerObject.createGuiItem(Material.MINECART, ChatColor.DARK_PURPLE + moneyPitMessage, 1, ChatColor.DARK_PURPLE + "Current cash in pit10x: " + pit12xCash));
    }

    @EventHandler
    public void wheelMechanics(InventoryClickEvent e){
        if(e.getInventory() != this.inventory){
            return;
        }
        if(e.getCurrentItem() == null || e.getCurrentItem().getType().isAir()){
            return;
        }
        e.setCancelled(true);
        Player player = (Player) e.getWhoClicked();
        if(e.getRawSlot() == slot2x && !isRunning){
            addMoneyToSlot(slot2x, player);
            updateCashPits();
        }else if(e.getRawSlot() == slot4x && !isRunning){
            addMoneyToSlot(slot4x, player);
            updateCashPits();
        }else if(e.getRawSlot() == slot6x && !isRunning){
            addMoneyToSlot(slot6x, player);
            updateCashPits();
        }else if(e.getRawSlot() == slot12x && !isRunning){
            addMoneyToSlot(slot12x, player);
            updateCashPits();
        }
        if(e.getRawSlot() == startLocation && !isRunning && totalPitCash > 0.01){
            player.sendMessage(ChatColor.GREEN + "Spin has begun!");
            start(player);
        }else if(e.getRawSlot() == startLocation && !isRunning && totalPitCash < 0.01){
            player.sendMessage(ChatColor.RED + "You need to put money into one of the pits before starting the wheel!");
            return;
        }
        if(e.getRawSlot() == goBackSlot && !isRunning){
            player.closeInventory();
            //player.openInventory(plugin.gambleMenu.getInventory());
        }
    }

    private void addMoneyToSlot(int slot, Player player){
        double playerCash = plugin.playerConfig.getDouble(player.getName() + ".balance");
        if(playerCash > cost){
            updateBalance(player, -cost);
            totalPitCash += cost;
            ConfigManager.saveConfig(plugin.playerConfig, plugin.playerPath);
        }else{
            player.sendMessage(ChatColor.RED + "You do not have enough money to spin!");
            return;
        }
        switch (slot) {
            case slot2x -> addCash(pit2xCash, slot2x, ChatColor.DARK_GREEN);
            case slot4x -> addCash(pit4xCash, slot4x, ChatColor.GREEN);
            case slot6x -> addCash(pit6xCash, slot6x, ChatColor.AQUA);
            case slot12x -> addCash(pit12xCash, slot12x, ChatColor.DARK_PURPLE);
        }
        inventory.setItem(moneySlot, PocketGamblerObject.createGuiItem(Material.CAULDRON, ChatColor.GREEN + moneyPitMessage, 1, ChatColor.GOLD + "Current cash in pit: " + totalPitCash));
        player.updateInventory();
    }

    private void addCash(double pitCash, int pitSlot, ChatColor color){
        pitCash += cost;
        inventory.setItem(pitSlot, PocketGamblerObject.createGuiItem(Material.MINECART, ChatColor.DARK_GREEN + moneyPitMessage, 1, color + "Current cash in pit: " + pitCash));
    }

    @Override
    public void start(Player player){
        if(gambleRunnable != null){
            if(!gambleRunnable.isCancelled()){
                gambleRunnable.cancel();
            }
        }
        isRunning = true;
        inventory.setItem(startLocation, PocketGamblerObject.createGuiItem(Material.OAK_BUTTON, ChatColor.GOLD + "Spinning!", 1));
        player.updateInventory();
        this.gambleRunnable = new BukkitRunnable(){
            final int amountOfSpins = random.nextInt(25) + 25; // can spin 25 - 50 times;
            int count = 0;
            final Sound wheelSound = Sound.BLOCK_NOTE_BLOCK_CHIME;
            @Override
            public void run() {
                shiftWheel();
                player.playSound(player.getLocation(), wheelSound, 50, 50);
                if(count >= amountOfSpins){
                    isRunning = false;
                    getResults(player);
                    cancel();
                }
                count++;
            }
        }.runTaskTimer(plugin, 0L, 3L);
    }

    private void shiftWheel(){
        for(int i = index; i < index + spinLocations.length - 1; i++){
            int temp = this.spinLocations[(i + 1) % this.spinLocations.length];
            this.spinLocations[(i + 1) % this.spinLocations.length] = this.spinLocations[i % this.spinLocations.length];
            this.spinLocations[i % this.spinLocations.length] =  temp;
        }
        updateWheel();
        index++;
    }

    private void updateWheel(){
        List<Integer> oddSpinIndices = Arrays.asList(spinLocations[0], spinLocations[2], spinLocations[4], spinLocations[6], spinLocations[8], spinLocations[10]);
        for(int i = 0; i < inventory.getSize(); i++){
            if(oddSpinIndices.contains(i)){
                this.inventory.setItem(i, mult2);
            }else if(i == this.spinLocations[1] || i == this.spinLocations[3] || i == this.spinLocations[11]) {
                this.inventory.setItem(i, mult4);
            }else if(i == this.spinLocations[5] || i == this.spinLocations[9]){
                this.inventory.setItem(i, mult6);
            }else if(i == this.spinLocations[7]){
                this.inventory.setItem(i, mult12);
            }
        }
    }

    @Override
    public void getResults(Player player){
        Material itemStoppedOn = this.inventory.getItem(moneySlot + 9).getType(); // item under the money pit
        if(itemStoppedOn.equals(Material.AIR)){
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "ERROR: winning item is not present!");
        }else{
            String winningMessage = ChatColor.GREEN + "You won " + ChatColor.GOLD + "$";
            switch (itemStoppedOn) {
                case COAL -> givePlayerWinnings(player, pit2xCash, 2, winningMessage);
                case IRON_INGOT -> givePlayerWinnings(player, pit4xCash, 4, winningMessage);
                case GOLD_INGOT -> givePlayerWinnings(player, pit6xCash, 6, winningMessage);
                case DIAMOND -> givePlayerWinnings(player, pit12xCash, 12, winningMessage);
                default -> Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "WARNING: Invalid wheel item.");
            }
        }
        inventory.setItem(startLocation, PocketGamblerObject.createGuiItem(Material.OAK_BUTTON, ChatColor.GREEN + "Click to spin!", 1));
        this.pit2xCash = 0.00;
        this.pit4xCash = 0.00;
        this.pit6xCash = 0.00;
        this.pit12xCash = 0.00;
        this.totalPitCash = 0.00;
        updateCashPits();
        player.updateInventory();
    }

    private void givePlayerWinnings(Player player, double pitCash, int multiple, String winningMessage){
        if(pitCash > 0.01){
            player.sendMessage(winningMessage + pitCash * multiple);
            updateBalance(player, pitCash * multiple);
            this.afterGambleSoundEffects(player, true);
        }else{
            player.sendMessage(ChatColor.DARK_RED + "Better luck next time.");
            this.afterGambleSoundEffects(player, false);
        }
    }

}

package me.claycoding.pocketgambler.guis.slots;

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
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class SlotsGame extends AbstractGamble implements Listener {

    private final int size = 54;
    private final int goBackSlot = 45;
    private final int startSlot = 49;
    private final int button1 = 39;
    private final int button2 = 40;
    private final int button3 = 41;

    private static final List<Integer> firstRow = Arrays.asList(12, 21, 30);
    private static final List<Integer> secondRow = Arrays.asList(13, 22, 31);
    private static final List<Integer> thirdRow = Arrays.asList(14, 23, 32);

    private Material[] winnableItems;
    private final ItemStack[] leftSlotMaterials;
    private final ItemStack[] midSlotMaterials;
    private final ItemStack[] rightSlotMaterials;
    private final boolean[] slotsRunning = {false, false, false};
    private static final Random random = new Random();
    private int index = 0; // used for cycling through slots

    enum Costs {
        spinCost(100.00),
        firstWinnings(200.00),
        secondWinnings(300.00),
        thirdWinnings(400.00),
        fourthWinnings(500.00),
        fithWinnings(600.00),
        sixthWinnings(700.00),
        seventhWinnings(800.00),
        eighthWinnings(900.00),
        ninthWinnings(1200.00),
        tenthWinnings(2000.00);
        double cost;
        Costs(double cost) {
            this.cost = cost;
        }
        public double getCost(){
            return this.cost;
        }
    }

    public SlotsGame(Main plugin){
        super(plugin);
        initWinnableItems();
        String title = "Slots";
        this.inventory = Bukkit.createInventory(null, size, title);
        this.inventory.setContents(getDefaultInventory());

        this.leftSlotMaterials = createSlotMaterials();
        this.midSlotMaterials = createSlotMaterials();
        this.rightSlotMaterials = createSlotMaterials();

        updateInventoryGambleItems();
    }

    private void initWinnableItems(){
        this.winnableItems = new Material[10];
        this.winnableItems[0] = Material.COAL;
        this.winnableItems[1] = Material.QUARTZ;
        this.winnableItems[2] = Material.IRON_INGOT;
        this.winnableItems[3] = Material.GOLD_INGOT;
        this.winnableItems[4] = Material.REDSTONE;
        this.winnableItems[5] = Material.LAPIS_LAZULI;
        this.winnableItems[6] = Material.EMERALD;
        this.winnableItems[7] = Material.DIAMOND;
        this.winnableItems[8] = Material.AMETHYST_SHARD;
        this.winnableItems[9] = Material.NETHERITE_INGOT;
    }

    private ItemStack[] getDefaultInventory(){
        ItemStack[] items = new ItemStack[size];
        items[button1] = PocketGamblerObject.createGuiItem(Material.STONE_BUTTON, ChatColor.AQUA + "button1", 1);
        items[button2] = PocketGamblerObject.createGuiItem(Material.STONE_BUTTON,ChatColor.AQUA + "button2", 1);
        items[button3] = PocketGamblerObject.createGuiItem(Material.STONE_BUTTON,ChatColor.AQUA + "button3", 1);
        items[startSlot] = PocketGamblerObject.createGuiItem(Material.TRIPWIRE_HOOK, ChatColor.GREEN + "Click to Start!", 1, ChatColor.GREEN + "Cost: " + Costs.spinCost.getCost());
        items[goBackSlot] = PocketGamblerObject.createGuiItem(Material.PAPER, "Click to Go Back" , 1);
        addBackground(items, Material.BLACK_STAINED_GLASS_PANE);
        return items;
    }

    private ItemStack[] createSlotMaterials(){
        ItemStack[] items = new ItemStack[30];
        int rand = random.nextInt(30);
        for(int i = rand; i < rand + 30; i++){
            items[i % items.length] = new ItemStack(winnableItems[i % (winnableItems.length)], 1);
        }
        return items;
    }

    private void addBackground(ItemStack[] items, Material material){
        items[2] = new ItemStack(material);
        items[3] = new ItemStack(material);
        items[4] = new ItemStack(material);
        items[5] = new ItemStack(material);
        items[6] = new ItemStack(material);
        items[11] = new ItemStack(material);
        items[15] = new ItemStack(material);
        items[20] = new ItemStack(material);
        items[24] = new ItemStack(material);
        items[29] = new ItemStack(material);
        items[33] = new ItemStack(material);
        items[38] = new ItemStack(material);
        items[42] = new ItemStack(material);
        items[47] = new ItemStack(material);
        items[48] = new ItemStack(material);
        items[50] = new ItemStack(material);
        items[51] = new ItemStack(material);
    }

    private void updateInventoryGambleItems(){
        this.inventory.setItem(firstRow.get(0), leftSlotMaterials[0]);
        this.inventory.setItem(firstRow.get(1), leftSlotMaterials[1]);
        this.inventory.setItem(firstRow.get(2), leftSlotMaterials[2]);
        this.inventory.setItem(secondRow.get(0), midSlotMaterials[0]);
        this.inventory.setItem(secondRow.get(1), midSlotMaterials[1]);
        this.inventory.setItem(secondRow.get(2), midSlotMaterials[2]);
        this.inventory.setItem(thirdRow.get(0), rightSlotMaterials[0]);
        this.inventory.setItem(thirdRow.get(1), rightSlotMaterials[1]);
        this.inventory.setItem(thirdRow.get(2), rightSlotMaterials[2]);
    }

    @EventHandler
    public void clickEvents(InventoryClickEvent e){
        if(e.getInventory() != this.inventory){
            return;
        }
        if(e.getCurrentItem() == null || e.getCurrentItem().getType() == Material.AIR){
            return;
        }
        if(!e.getClick().isLeftClick()){
            return;
        }
        e.setCancelled(true);
        Player player = (Player) e.getWhoClicked();
        if(e.getRawSlot() == goBackSlot && !isRunning){
            player.closeInventory();
            //player.openInventory(plugin.slotsMenu.getInventory());
        }else if(e.getRawSlot() == startSlot && !isRunning){
            double playerCash = plugin.playerConfig.getDouble(player.getName() + ".balance");
            if(playerCash < Costs.spinCost.getCost()){
                player.sendMessage(ChatColor.DARK_RED + "Sorry, you do not have enough money to spin!");
                return;
            }else{
                start(player);
            }
        }
        if(isRunning){
            if(e.getRawSlot() == button1){
                if(slotsRunning[0]){
                    slotsRunning[0] = false;
                }
            }else if(e.getRawSlot() == button2){
                if(slotsRunning[1]){
                    slotsRunning[1] = false;
                }
            }else if(e.getRawSlot() == button3){
                if(slotsRunning[2]){
                    slotsRunning[2] = false;
                }
            }
        }
    }

    private void shiftSlotMaterials(ItemStack[] slotMaterials){
        for(int i = index; i < index + slotMaterials.length; i++){
            ItemStack temp = slotMaterials[i % slotMaterials.length];
            slotMaterials[i % slotMaterials.length] = slotMaterials[(i + 1) % slotMaterials.length];
            slotMaterials[(i + 1) % slotMaterials.length] = temp;
        }
        index++;
    }

    @Override
    public void start(Player player){
        if(this.gambleRunnable != null){
            if(!this.gambleRunnable.isCancelled()){
                this.gambleRunnable.cancel();
            }
        }
        isRunning = true;
        slotsRunning[0] = true;
        slotsRunning[1] = true;
        slotsRunning[2] = true;
        this.gambleRunnable = new BukkitRunnable(){
            final Sound slotTicker = Sound.BLOCK_AMETHYST_BLOCK_CHIME;
            final Sound slotFinished = Sound.BLOCK_STONE_BREAK;
            final boolean[] firstTimeButtonClicked = {true, true};
            @Override
            public void run() {
                player.playSound(player.getLocation(), slotTicker, 20.0f, 25.0f);
                if(slotsRunning[0]){
                    shiftSlotMaterials(leftSlotMaterials);
                }
                if(slotsRunning[1]){
                    shiftSlotMaterials(midSlotMaterials);
                }
                if(slotsRunning[2]){
                    shiftSlotMaterials(rightSlotMaterials);
                }
                updateInventoryGambleItems();
                int numSlotsRunning = 0;
                for (boolean b : slotsRunning) {
                    if (b) {
                        numSlotsRunning++;
                    }
                }
                if(numSlotsRunning == 0){
                    player.playSound(player.getLocation(), slotFinished, 20.0f, 25.0f);
                    getResults(player);
                    isRunning = false;
                    cancel();
                }else if(numSlotsRunning == 1){
                    if(firstTimeButtonClicked[1]){
                        player.playSound(player.getLocation(), slotFinished, 20.0f, 25.0f);
                        firstTimeButtonClicked[1] = false;
                    }
                }else if(numSlotsRunning == 2){
                    if(firstTimeButtonClicked[0]){
                        firstTimeButtonClicked[0] = false;
                        player.playSound(player.getLocation(), slotFinished, 20.0f, 25.0f);
                    }
                }
            }
        }.runTaskTimer(plugin, 0L, 0L);
    }

    @Override
    public void getResults(Player player){
        Map<Boolean, Material> winCheck = new HashMap<>();
        winCheck.put(getWinPattern(firstRow.get(0), secondRow.get(0), thirdRow.get(0)), getMaterial(firstRow.get(0), secondRow.get(0), thirdRow.get(0))); // top col
        winCheck.put(getWinPattern(firstRow.get(1), secondRow.get(1), thirdRow.get(1)), getMaterial(firstRow.get(1), secondRow.get(1), thirdRow.get(1))); // mid col
        winCheck.put(getWinPattern(firstRow.get(2), secondRow.get(2), thirdRow.get(2)), getMaterial(firstRow.get(2), secondRow.get(2), thirdRow.get(2))); // bottom col
        winCheck.put(getWinPattern(firstRow.get(0), secondRow.get(1), thirdRow.get(2)), getMaterial(firstRow.get(0), secondRow.get(1), thirdRow.get(2))); // top to bottom diagonal
        winCheck.put(getWinPattern(firstRow.get(2), secondRow.get(1), thirdRow.get(0)), getMaterial(firstRow.get(2), secondRow.get(1), thirdRow.get(0))); // bottom to top diagonal
        int winCount = 0;
        for(Map.Entry<Boolean, Material> entry : winCheck.entrySet()){
            if(entry.getKey()){
                double winningCash;
                if(entry.getValue() == winnableItems[0]){
                    winningCash = Costs.firstWinnings.getCost();
                }else if(entry.getValue() == winnableItems[1]){
                    winningCash = Costs.secondWinnings.getCost();
                }else if(entry.getValue() == winnableItems[2]){
                    winningCash = Costs.thirdWinnings.getCost();
                }else if(entry.getValue() == winnableItems[3]){
                    winningCash = Costs.fourthWinnings.getCost();
                }else if(entry.getValue() == winnableItems[4]){
                    winningCash = Costs.fithWinnings.getCost();
                }else if(entry.getValue() == winnableItems[5]){
                    winningCash = Costs.sixthWinnings.getCost();
                }else if(entry.getValue() == winnableItems[6]){
                    winningCash = Costs.seventhWinnings.getCost();
                }else if(entry.getValue() == winnableItems[7]){
                    winningCash = Costs.eighthWinnings.getCost();
                }else if(entry.getValue() == winnableItems[8]){
                    winningCash = Costs.ninthWinnings.getCost();
                }else if(entry.getValue() == winnableItems[9]){
                    winningCash = Costs.tenthWinnings.getCost();
                }else{
                    winningCash = 0.00;
                }
                player.sendMessage(ChatColor.GREEN + "You won $" + winningCash + "!");
                updateBalance(player, winningCash);
                winCount++;
            }
        }
        if(winCount == 0){
            this.afterGambleSoundEffects(player, false);
            player.sendMessage(ChatColor.DARK_RED + "No win this time.");
            updateBalance(player, -Costs.spinCost.getCost());
        }else{
            this.afterGambleSoundEffects(player, true);
        }
        ConfigManager.saveConfig(plugin.playerConfig, plugin.playerPath);
    }

    private boolean getWinPattern(int pos1, int pos2, int pos3){
        Set<Material> winCheck = new HashSet<>();// check for diagonals and top and bottom cols
        winCheck.add(this.inventory.getItem(pos1).getType());
        winCheck.add(this.inventory.getItem(pos2).getType());
        winCheck.add(this.inventory.getItem(pos3).getType());
        return winCheck.size() == 1; // if the set has length 1, then there is only 1 material
    }

    private Material getMaterial(int pos1, int pos2, int pos3){
        Set<Material> winCheck = new HashSet<>();// check for diagonals and top and bottom cols
        winCheck.add(this.inventory.getItem(pos1).getType());
        winCheck.add(this.inventory.getItem(pos2).getType());
        winCheck.add(this.inventory.getItem(pos3).getType());
        return winCheck.stream().findFirst().get();
    }

}

package me.claycoding.pocketgambler.guis.slider;

import me.claycoding.pocketgambler.Main;
import me.claycoding.pocketgambler.guis.AbstractGamble;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

public class SliderGame extends AbstractGamble implements Listener {
    public SliderGame(Main plugin) {
        super(plugin);
    }

    @Override
    public void start(Player player) {

    }

    @Override
    public void getResults(Player player) {

    }

//    private final String invName = "Gambler";
//    private ItemStack[] gambleItems;
//    private TokenType tokenType;
//    private int itemsIndex = 0;
//    private final int startGambleSlot = 22;
//    private final int goBackSlot = 18;
//    private final int middleLeftBound = 9, middleRightBound = 18, middle = 14;
//    private long gambleTickDelay = 0L;
//    private static Random random = new Random();
//
//    public GambleGui(Main plugin){
//        super(plugin);
//        this.inventory = Bukkit.createInventory(null, 27, invName);
//    }
//
//    public void setTokenType(TokenType tokenType){
//        this.tokenType = tokenType;
//    }
//
//    public void setInventoryContents(ItemStack[] gambleItems){
//        if(gambleItems == null){
//            Bukkit.getConsoleSender().sendMessage("[ConstructiveInventories] " + ChatColor.RED + "WARNING: gambleItems are null.");
//            return;
//        }
//        this.itemsIndex = random.nextInt(gambleItems.length);
//        for(int i = this.middleLeftBound; i < this.middleRightBound; i++){
//            this.inventory.setItem(i, gambleItems[(itemsIndex + i) % gambleItems.length]);
//        }
//        ItemStack goBack = PocketGamblerObject.createGuiItem(Material.PAPER, "Click to go back", 1);
//        ItemStack topMid = PocketGamblerObject.createGuiItem(Material.ANVIL, ChatColor.GREEN + "You will get the item that lands below here!", 1);
//        ItemStack key;
//        switch (tokenType){
//            case common -> key = PocketGamblerObject.createGuiItem(Material.TRIPWIRE_HOOK, ChatColor.GREEN + "Common spin key", 1, ChatColor.GREEN + "$" + tokenType.getCost() + " for each spin");
//            case rare -> key = PocketGamblerObject.createGuiItem(Material.TRIPWIRE_HOOK, ChatColor.AQUA + "Rare spin key", 1, ChatColor.AQUA + "$" + tokenType.getCost() + " for each spin");
//            case epic -> key = PocketGamblerObject.createGuiItem(Material.TRIPWIRE_HOOK, ChatColor.AQUA + "Epic spin key", 1, ChatColor.AQUA + "$" + tokenType.getCost() + " for each spin");
//            default -> key = new ItemStack(Material.TRIPWIRE_HOOK); // if all goes wrong initialize with no meta.
//        }
//        this.gambleItems = gambleItems;
//        this.inventory.setItem(startGambleSlot, key);
//        this.inventory.setItem(4, topMid);
//        this.inventory.setItem(this.goBackSlot, goBack);
//    }
//
//    @EventHandler
//    public void gambleListener(InventoryClickEvent e){
//        if(this.inventory != null){
//            if(e.getInventory() == this.inventory){
//                e.setCancelled(true);
//                Player player = (Player) e.getWhoClicked();
//                if(e.getRawSlot() == startGambleSlot && !this.isRunning){
//                    double playerCash = plugin.playerConfig.getDouble(player.getName() + ".balance");
//                    if(playerCash < this.tokenType.getCost()){
//                        player.sendMessage(ChatColor.RED + "You do not have enough money to spin!");
//                        player.closeInventory();
//                        return;
//                    }
//                    StringBuilder builder = new StringBuilder();
//                    player.sendMessage(builder.append(ChatColor.RED + "-" + this.tokenType.getCost() + ChatColor.GREEN + ". Gamble beginning...").toString());
//                    shuffle(gambleItems);
//                    startGamble(player);
//                }else if(e.getRawSlot() == 18 && !this.isRunning){
//                    player.closeInventory();
//                    player.openInventory(plugin.gambleMenu.getInventory());
//                }
//            }else if(e.getInventory() == plugin.masterGui.menu && isRunning){
//                e.getWhoClicked().closeInventory();
//                e.getWhoClicked().sendMessage(ChatColor.RED + "Wait for spin to end before accessing MasterGui");
//            }
//        }
//    }
//
//    private void shuffle(ItemStack[] items){
//        for(int i = 0; i < items.length; i++){
//            int randomItemIndex = random.nextInt(items.length); // get a random index in the array
//            ItemStack temp = items[i];
//            items[i] = items[randomItemIndex]; // set the current index to the random one
//            items[randomItemIndex] = temp; // set the random index to the current one
//        }
//    }
//
//    private void shiftGambleItemsLeft(){
//        for(int i = 0; i < itemsIndex; i++){ // shift the array of ItemStacks 1 to the right
//            ItemStack temp = this.gambleItems[itemsIndex % this.gambleItems.length];
//            this.gambleItems[itemsIndex % this.gambleItems.length] = this.gambleItems[(itemsIndex + 1) % this.gambleItems.length];
//            this.gambleItems[(itemsIndex + 1) % this.gambleItems.length] = temp;
//        }
//        for(int i = middleLeftBound; i < middleRightBound; i++){ // add the shifted array of ItemStacks back to the inventory
//            this.inventory.setItem(i, gambleItems[(itemsIndex + i) % this.gambleItems.length]);
//        }
//        itemsIndex = (itemsIndex + 1) % this.gambleItems.length;
//    }
//
//    @Override
//    void startGamble(Player player){
//        player.setAI(false);
//        this.isRunning = true;
//        this.initRunnable();
//        this.gambleRunnable = new BukkitRunnable(){
//            private int amountOfSwitches = random.nextInt(20) + 30; // have between 30 - 50 switches before stopping
//            private int counter = 0;
//            private float soundPitch = 10, soundIntensity = 15;
//            Sound sound = Sound.BLOCK_NOTE_BLOCK_CHIME;
//            @Override
//            public void run() {
//                if(counter >= amountOfSwitches){
//                    checkWinnings(player);
//                    gambleTickDelay = 0L;
//                    cancel();
//                }
//                if(amountOfSwitches - counter == 20){
//                    gambleTickDelay++;
//                    soundPitch = 7;
//                }else if(amountOfSwitches - counter < 10){
//                    gambleTickDelay++;
//                    soundPitch--;
//                }
//                shiftGambleItemsLeft();
//                player.playSound(player.getLocation(), sound, soundIntensity, soundPitch);
//                counter++;
//            }
//        }.runTaskTimer(plugin, gambleTickDelay, 1L);
//    }
//
//    @Override
//    void checkWinnings(Player player){
//        player.setAI(true);
//        this.isRunning = false;
//        PocketGamblerObject.givePlayerItem(player, inventory.getItem(middle));
//        player.sendMessage(ChatColor.GREEN + "You obtained " + inventory.getItem(middle).getType().name());
//        afterGambleSoundEffects(player, true);
//        updateBalance(player, -this.tokenType.getCost());
//    }
//
//    public static ItemStack[] getDefaultCommonContents(){
//        ItemStack[] items = new ItemStack[18];
//        items[0] = new ItemStack(Material.DIAMOND, 1);
//        items[1] = new ItemStack(Material.DIRT, 1);
//        items[2] = new ItemStack(Material.EMERALD, 3);
//        items[3] = new ItemStack(Material.OAK_LOG, 32);
//        items[4] = new ItemStack(Material.SAND, 32);
//        items[5] = new ItemStack(Material.GOLD_INGOT, 6);
//        items[6] = new ItemStack(Material.WHEAT_SEEDS, 8);
//        items[7] = new ItemStack(Material.GRAVEL, 4);
//        items[8] = new ItemStack(Material.RAW_IRON, 6);
//        items[9] = new ItemStack(Material.TORCH, 8);
//        items[10] = new ItemStack(Material.COAL, 16);
//        items[11] = new ItemStack(Material.BAMBOO, 1);
//        items[12] = new ItemStack(Material.APPLE, 1);
//        items[13] = new ItemStack(Material.BONE, 1);
//        items[14] = new ItemStack(Material.WHITE_WOOL, 1);
//        items[15] = new ItemStack(Material.FERN, 1);
//        items[16] = new ItemStack(Material.COOKED_BEEF, 8);
//        items[17] = new ItemStack(Material.GOLDEN_APPLE, 1);
//        return items;
//    }
//
//    public static ItemStack[] getDefaultRareContents(){
//        ItemStack[] items = new ItemStack[18];
//        items[0] = new ItemStack(Material.DIAMOND_SWORD, 1);
//        items[1] = new ItemStack(Material.IRON_INGOT, 16);
//        items[2] = new ItemStack(Material.EMERALD, 8);
//        items[3] = new ItemStack(Material.OAK_WOOD, 32);
//        items[4] = new ItemStack(Material.SAND, 64);
//        items[5] = new ItemStack(Material.MYCELIUM, 1);
//        items[6] = new ItemStack(Material.MELON_SEEDS, 1);
//        items[7] = new ItemStack(Material.PUMPKIN_SEEDS, 1);
//        items[8] = new ItemStack(Material.EGG, 8);
//        items[9] = new ItemStack(Material.BUCKET, 3);
//        items[10] = new ItemStack(Material.COAL, 32);
//        items[11] = new ItemStack(Material.DIRT, 32);
//        items[12] = new ItemStack(Material.DIAMOND_PICKAXE, 1);
//        items[13] = new ItemStack(Material.BONE, 16);
//        items[14] = new ItemStack(Material.WHITE_WOOL, 32);
//        items[15] = new ItemStack(Material.STONE_BRICKS, 32);
//        items[16] = new ItemStack(Material.COOKED_BEEF, 16);
//        items[17] = new ItemStack(Material.SADDLE, 1);
//        return items;
//    }
//
//    public static ItemStack[] getDefaultEpicContents(){
//        ItemStack[] items = new ItemStack[18];
//        items[0] = new ItemStack(Material.DIAMOND_BLOCK, 1);
//        items[1] = new ItemStack(Material.EMERALD_BLOCK, 1);
//        items[2] = new ItemStack(Material.PIG_SPAWN_EGG, 2);
//        items[3] = new ItemStack(Material.CHICKEN_SPAWN_EGG, 2);
//        items[4] = new ItemStack(Material.SHEEP_SPAWN_EGG, 2);
//        items[5] = new ItemStack(Material.COW_SPAWN_EGG, 2);
//        items[6] = new ItemStack(Material.CARROT, 1);
//        items[7] = new ItemStack(Material.POTATO, 1);
//        items[8] = new ItemStack(Material.COAL, 48);
//        items[9] = new ItemStack(Material.DIRT, 1);
//        items[10] = new ItemStack(Material.ENDER_PEARL, 12);
//        items[11] = new ItemStack(Material.DRAGON_EGG, 1);
//        items[12] = new ItemStack(Material.DRAGON_HEAD, 1);
//        items[13] = new ItemStack(Material.SALMON, 1);
//        items[14] = new ItemStack(Material.OAK_PLANKS, 1);
//        items[15] = new ItemStack(Material.DEAD_BUSH, 1);
//        items[16] = new ItemStack(Material.COOKED_BEEF, 32);
//        items[17] = new ItemStack(Material.LAPIS_BLOCK, 12);
//        return items;
//    }

}

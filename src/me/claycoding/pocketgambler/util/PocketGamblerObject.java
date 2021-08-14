package me.claycoding.pocketgambler.util;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class PocketGamblerObject {

    public static ItemStack createGuiItem(final Material material, final String name, final int amount, final String... lore){
        final ItemStack item = new ItemStack(material, amount);
        final ItemMeta meta = item.getItemMeta();
        meta.getItemFlags().clear();
        meta.setDisplayName(name);
        meta.setLore(Arrays.asList(lore));
        item.setItemMeta(meta);
        return item;
    }

    public static void givePlayerItem(Player player, ItemStack item){
        if(player.getInventory().firstEmpty() == -1){
            player.getWorld().dropItemNaturally(player.getLocation(), item);
        }else if(player.getInventory().getItemInMainHand().getType().isAir()){
            player.getInventory().setItemInMainHand(item);
        }else{
            for(int i = 0; i < player.getInventory().getContents().length; i++){
                if(player.getInventory().getItem(i) == null){
                    player.getInventory().setItem(i, item);
                    break;
                }
            }
        }
    }

}

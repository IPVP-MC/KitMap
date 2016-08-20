package org.ipvp.kitmap;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class Kit {

    private ItemStack helmet, chest, legs, boots;
    private ItemStack[] contents = new ItemStack[36];
    
    private static final ItemStack SPLASH_HEATH = new ItemStack(Material.POTION, 1, (short) 16421);

    public ItemStack getHelmet() {
        return helmet;
    }

    public void setHelmet(ItemStack helmet) {
        this.helmet = helmet;
    }

    public ItemStack getChest() {
        return chest;
    }

    public void setChest(ItemStack chest) {
        this.chest = chest;
    }

    public ItemStack getLegs() {
        return legs;
    }

    public void setLegs(ItemStack legs) {
        this.legs = legs;
    }

    public ItemStack getBoots() {
        return boots;
    }

    public void setBoots(ItemStack boots) {
        this.boots = boots;
    }

    public void addItem(int slot, ItemStack item) {
        if (slot < 0 || slot >= 36) {
            throw new IllegalArgumentException("Invalid inventory slot provided, must be between 0 and 36");
        }
        contents[slot] = item == null ? null : item.clone();
    }
    
    public ItemStack[] getContents() {
        return contents.clone();
    }
    
    public void giveTo(Player player) {
        PlayerInventory inventory = player.getInventory();
        inventory.clear();
        inventory.setHelmet(helmet == null ? null : helmet.clone());
        inventory.setChestplate(chest == null ? null : chest.clone());
        inventory.setLeggings(legs == null ? null : legs.clone());
        inventory.setBoots(boots == null ? null : boots.clone());
        for (int i = 0 ; i < contents.length ; i++) {
            ItemStack item = contents[i];
            inventory.setItem(i, item == null ? null : item.clone());
        }
        while (inventory.firstEmpty() != -1) {
            inventory.addItem(SPLASH_HEATH.clone());
        }
        player.updateInventory();
    }
}

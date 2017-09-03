package org.ipvp.kitmap;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class KitSignsPlugin extends JavaPlugin implements Listener {

    private Map<UUID, Long> nextClick = new HashMap<>();
    private Map<String, Kit> kits = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);

    @Override
    public void onEnable() {
        saveDefaultConfig();
        loadKits();
        getServer().getPluginManager().registerEvents(this, this);
    }

    private void loadKits() {
        Set<String> keys = getConfig().getKeys(false);
        if (keys == null) {
            return;
        }
        for (String key : keys) {
            ConfigurationSection section = getConfig().getConfigurationSection(key);
            Kit kit = new Kit();
            kit.setHelmet(loadItem(section.getConfigurationSection("helmet")));
            kit.setChest(loadItem(section.getConfigurationSection("chest")));
            kit.setLegs(loadItem(section.getConfigurationSection("legs")));
            kit.setBoots(loadItem(section.getConfigurationSection("boots")));

            ConfigurationSection inv = section.getConfigurationSection("inv");
            if (inv != null) {
                for (String index : inv.getKeys(false)) {
                    int idx;
                    try {
                        idx = Integer.parseInt(index);
                    } catch (NumberFormatException ex) {
                        getLogger().info("Invalid number " + index);
                        continue;
                    }
                    ConfigurationSection item = inv.getConfigurationSection(index);
                    kit.addItem(idx, loadItem(item));
                }
            }
            kits.put(key, kit);
        }
    }

    private ItemStack loadItem(ConfigurationSection section) {
        if (section == null) {
            return null;
        }
        String type = section.getString("type");
        Material mat = Material.getMaterial(type);
        if (mat == null) {
            getLogger().info("Invalid material " + type);
            return null;
        }
        int amount = Math.max(1, section.getInt("amount", 1));
        short durability = (short) section.getInt("durability", 0);

        ItemStack result = new ItemStack(mat, amount, durability);
        ConfigurationSection enchantments = section.getConfigurationSection("enchantments");
        if (enchantments != null) {
            for (String ench : enchantments.getKeys(false)) {
                int level = enchantments.getInt(ench);
                Enchantment enchantment = Enchantment.getByName(ench);
                if (enchantment == null) {
                    getLogger().info("Invalid enchantment " + ench);
                } else {
                    result.addUnsafeEnchantment(enchantment, level);
                }
            }
        }
        return result;
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        nextClick.remove(event.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onSignUse(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Block block = event.getClickedBlock();

        if (event.getAction() == Action.RIGHT_CLICK_BLOCK
                && block.getState() instanceof Sign) {
            Sign sign = (Sign) block.getState();

            if (sign.getLine(0).equals(ChatColor.DARK_BLUE + "[Class]")) {
                if (nextClick.containsKey(player.getUniqueId())) {
                    long nextClick = this.nextClick.get(player.getUniqueId());
                    if (System.currentTimeMillis() < nextClick) {
                        return;
                    }
                }
                this.nextClick.put(player.getUniqueId(), System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(1));
                String clazz = sign.getLine(1).toLowerCase();
                if (!kits.containsKey(clazz)) {
                    player.sendMessage(ChatColor.RED + "Improper kit defined, please contact an administrator.");
                } else {
                    Kit kit = kits.get(clazz);
                    kit.giveTo(player);
                    player.setHealth(20.0D);
                    player.setFireTicks(0);
                    player.setFoodLevel(20);
                    player.setSaturation(20.0F);
                    player.sendMessage(ChatColor.GREEN + "Inventory updated with kit " + clazz);
                }
            }
        }
    }

    @EventHandler
    public void onSignCreate(SignChangeEvent event) {
        Player player = event.getPlayer();
        String line = event.getLine(0);

        if (line.equals("[Class]")) {
            if (!player.hasPermission("kitmap.createsign")) {
                event.setCancelled(true);
            } else {
                event.setLine(0, ChatColor.DARK_BLUE + "[Class]");
                player.sendMessage(ChatColor.GREEN + "Created KitMap sign");
            }
        }
    }

}

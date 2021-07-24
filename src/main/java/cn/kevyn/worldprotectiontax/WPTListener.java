package cn.kevyn.worldprotectiontax;

import cn.kevyn.worldprotectiontax.utils.ConfigHelper;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;


public class WPTListener implements Listener {

    @EventHandler
    public void onPlayerBreakBlock(BlockBreakEvent event) {

        WorldProtectionTax wpt = WorldProtectionTax.INSTANCE;
        ConfigHelper configHelper = wpt.getConfigHelper();
        Economy economy = wpt.getEconomy();
        Permission permission = wpt.getPermission();

        Player player = event.getPlayer();
        String worldName = player.getWorld().getName();
        ConfigurationSection config = configHelper.getSection(worldName);
        String blockType = event.getBlock().getType().name().toLowerCase().replace("_", "-");
        Double tax = config.getDouble("break-" + blockType + "-tax");

        if (tax == 0.0)
            return;

        if (economy.getBalance(player) >= tax) {
            economy.withdrawPlayer(player, tax);
            player.sendMessage("-" + tax);
        } else {
            event.setCancelled(true);
            player.sendMessage("x");
        }

    }

    @EventHandler
    public void onPlayerStartBreak(PlayerInteractEvent event) {

        if (!(event.getAction() == Action.LEFT_CLICK_BLOCK))
            return;

        WorldProtectionTax wpt = WorldProtectionTax.INSTANCE;
        ConfigHelper configHelper = wpt.getConfigHelper();

        Player player = event.getPlayer();
        String worldName = player.getWorld().getName();
        ConfigurationSection config = configHelper.getSection(worldName);
        String blockType = event.getClickedBlock().getType().name().toLowerCase().replace("_", "-");;
        Double tax = config.getDouble("break-" + blockType + "-tax");

        if (tax == 0.0)
            return;

        String message = ChatColor.GOLD + "NEED: " + tax;
        TextComponent textComponent = new TextComponent(message);
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, textComponent);

    }

    @EventHandler
    public void onPlayerRightClick(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            String name = event.getClickedBlock().getType().name();
            event.getPlayer().sendMessage(name);
        }
    }

}

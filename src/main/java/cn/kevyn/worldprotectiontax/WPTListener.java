package cn.kevyn.worldprotectiontax;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;


public class WPTListener implements Listener {

    private WorldProtectionTax wpt;
    private Economy economy;
    private Permission permission;

    public WPTListener(WorldProtectionTax wpt) {
        this.wpt = wpt;
        this.economy = wpt.getEconomy();
        this.permission = wpt.getPermission();
    }

    @EventHandler
    public void onPlayerBreakBlock(BlockBreakEvent event) {

        Player player = event.getPlayer();
        String worldName = player.getWorld().getName();
        ConfigurationSection config = wpt.getConfigHelper().getSection(worldName);
        double tax = config.getDouble("tax");

        if (economy.getBalance(player) >= tax) {
            economy.withdrawPlayer(player, tax);
        } else {
            event.setCancelled(true);
        }

    }
}

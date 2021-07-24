package cn.kevyn.worldprotectiontax;

import cn.kevyn.worldprotectiontax.utils.ConfigHelper;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.ChatColor;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public final class WorldProtectionTax extends JavaPlugin {

    public static WorldProtectionTax INSTANCE;

    public final String ENABLE = ChatColor.BLUE + "[WorldProtectionTax Enabled! ";
    public final String DISABLE = ChatColor.RED + "[WorldProtectionTax] Disabled! ";
    public final String RELOAD = ChatColor.GREEN + "[WorldProtectionTax] Reloaded！";

    private Economy economy;
    private Permission permission;
    private ConfigHelper configHelper;

    public WorldProtectionTax () {
        INSTANCE = this;
    }

    @Override
    public void onEnable() {
        super.onEnable();

        // 保存默认配置文件
        saveDefaultConfig();
        configHelper = ConfigHelper.getInstance(getConfig(), true);

        // 检查前置
        if (dependenciesReady()) {

            // 如果权限提供者不存在，不影响使用，但影响功能
            if (permission == null) {
                getServer().getConsoleSender().sendMessage(ChatColor.RED + "[WorldProtectionTax] Permissions Provider Not Found" );
            }

        } else {
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        getServer().getPluginManager().registerEvents(new WPTListener(), this);
        getServer().getConsoleSender().sendMessage(ENABLE);
    }

    @Override
    public void onDisable() {
        getServer().getConsoleSender().sendMessage(DISABLE);
    }

    /**
     * 重载配置文件
     */
    public void reloadPlugin() {
        reloadConfig();
        configHelper = ConfigHelper.getInstance(getConfig(), true);
        getServer().getConsoleSender().sendMessage(RELOAD);
    }

    /**
     * 检查前置
     * 一定要有经济提供者
     * 没有权限提供者影响功能
     * @return 不影响使用吗？
     */
    private boolean dependenciesReady() {

        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }

        RegisteredServiceProvider<Economy> rspe = getServer().getServicesManager().getRegistration(Economy.class);
        RegisteredServiceProvider<Permission> rspp = getServer().getServicesManager().getRegistration(Permission.class);

        if (rspe == null) {
            return false;
        }

        economy = rspe.getProvider();

        if (economy == null) {
            return false;
        }

        permission = rspp == null ? null : rspp.getProvider();

        return true;

    }

    public Economy getEconomy() {
        return economy;
    }

    public Permission getPermission() {
        return permission;
    }

    public ConfigHelper getConfigHelper() {
        return configHelper;
    }
}

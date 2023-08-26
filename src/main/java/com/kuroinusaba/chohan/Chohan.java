package com.kuroinusaba.chohan;

import com.kuroinusaba.chohan.Commands.chohan;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public final class Chohan extends JavaPlugin {
    public static Chohan plugin;
    private Listeners listeners;
    public static String prefix = "§6§l[§e§lCho§d§lHan§6§l]§r";
    public static Economy econ = null;

    @Override
    public void onEnable() {
        // Plugin startup logic
        plugin = this;
        saveDefaultConfig();
        reloadConfig();
        try {
            this.listeners = new Listeners();
        } catch (Exception e) {
            getLogger().severe("Listenersのインスタンス化に失敗しました。");
            throw new RuntimeException(e);
        }
        getServer().getPluginManager().registerEvents(this.listeners, this);
        try {
            getCommand("chohan").setExecutor(new chohan());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        if (!setupEconomy()) {
            getLogger().severe("Vaultが見つかりませんでした。");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        plugin.getLogger().info(prefix + "§aプラグインが有効になりました。");
        super.onEnable();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
    private static Boolean setupEconomy() {
        if (getPlugin().getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getPlugin().getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null){
            return false;
        }else{
            econ = rsp.getProvider();
        }
        return econ != null;
    }
    public static Chohan getPlugin() {
        return plugin;
    }
}

package com.kuroinusaba.chohan;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.io.File;

public class Listeners implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        File playerdata = new File("plugins/Chohan/playerdata/" + player.getUniqueId() + ".yml");
        if (!playerdata.exists()) {
            playerdata.getParentFile().mkdirs();
            try {
                playerdata.createNewFile();
            } catch (Exception e) {
                e.printStackTrace();
            }
            playerdata = new File("plugins/Chohan/playerdata/" + player.getUniqueId() + ".yml");
            YamlConfiguration data = new YamlConfiguration();
            data.set("name", player.getName());
            data.set("totalbet", 0);
            data.set("totalwin", 0);
            try {
                data.save(playerdata);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

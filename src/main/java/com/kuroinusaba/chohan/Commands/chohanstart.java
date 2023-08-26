package com.kuroinusaba.chohan.Commands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static com.kuroinusaba.chohan.Chohan.*;
import static org.bukkit.Bukkit.broadcastMessage;

public class chohanstart {
    public chohanstart(int i, @NotNull CommandSender sender) {
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            File game = new File("plugins/Chohan/game.yml");
            YamlConfiguration gameYml = YamlConfiguration.loadConfiguration(game);
            if (gameYml.getInt("cho") == 0 || gameYml.getInt("han") == 0) {
                // ファイルを削除する
                gameYml.set("game", false);
                try {
                    gameYml.save(game);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                broadcastMessage(prefix + "§c§lゲームが開始されてから1分経過しましたが、参加者がいなかったためゲームを終了しました。");
                List<Player> choplayers = (List<Player>) gameYml.getList("choplayers");
                for (Player player : choplayers) {
                    File playerdata = new File("plugins/Chohan/playerdata/" + player.getUniqueId() + ".yml");
                    YamlConfiguration data = YamlConfiguration.loadConfiguration(playerdata);
                    data.set("totalbet", data.getInt("totalbet") - gameYml.getInt("bet"));
                    try {
                        data.save(playerdata);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    econ.depositPlayer(player, gameYml.getInt("bet"));
                    player.sendMessage(prefix + "§e" + gameYml.getInt("bet") + "円§aを返金しました。");
                }
                List<Player> hanplayers = (List<Player>) gameYml.getList("hanplayers");
                for (Player player : hanplayers) {
                    File playerdata = new File("plugins/Chohan/playerdata/" + player.getUniqueId() + ".yml");
                    YamlConfiguration data = YamlConfiguration.loadConfiguration(playerdata);
                    data.set("totalbet", data.getInt("totalbet") - gameYml.getInt("bet"));
                    try {
                        data.save(playerdata);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    econ.depositPlayer(player, gameYml.getInt("bet"));
                    player.sendMessage(prefix + "§e" + gameYml.getInt("bet") + "円§aを返金しました。");
                }
                return;
            }
            broadcastMessage(prefix + "§a§l丁半を開始します...§7§l(総額bet: " + gameYml.getInt("totalbet") + ")");
            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                // 1から2の乱数を生成
                int result = (int) (Math.random() * 2 + 1);
                if (result == 1) {
                    broadcastMessage(prefix + "§a§l結果: 丁");
                    broadcastMessage(prefix + "§a§l丁にかけた人は賞金を獲得しました。");
                    List<Player> choplayers = (List<Player>) gameYml.getList("choplayers");
                    for (Player player : choplayers) {
                        File playerdata = new File("plugins/Chohan/playerdata/" + player.getUniqueId() + ".yml");
                        YamlConfiguration data = YamlConfiguration.loadConfiguration(playerdata);
                        data.set("totalwin", data.getInt("totalwin") + gameYml.getInt("totalbet") / choplayers.size());
                        try {
                            data.save(playerdata);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        econ.depositPlayer(player, gameYml.getInt("totalbet") / choplayers.size());
                        broadcastMessage(prefix + "§a§l" + player.getName() + "§a§lが§e§l" + gameYml.getInt("totalbet") / choplayers.size() + "円§a§lを獲得しました。");
                    }
                    gameYml.set("game", false);
                    try {
                        gameYml.save(game);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return;
                }
                broadcastMessage(prefix + "§a§l結果: 半");
                broadcastMessage(prefix + "§a§l半にかけた人は賞金を獲得しました。");
                List<Player> hanplayers = (List<Player>) gameYml.getList("hanplayers");
                for (Player player : hanplayers) {
                    File playerdata = new File("plugins/Chohan/playerdata/" + player.getUniqueId() + ".yml");
                    YamlConfiguration data = YamlConfiguration.loadConfiguration(playerdata);
                    data.set("totalwin", data.getInt("totalwin") + gameYml.getInt("totalbet") / hanplayers.size());
                    try {
                        data.save(playerdata);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    econ.depositPlayer(player, gameYml.getInt("totalbet") / hanplayers.size());
                    broadcastMessage(prefix + "§a§l" + player.getName() + "§a§lが§e§l" + gameYml.getInt("totalbet") / hanplayers.size() + "円§a§lを獲得しました。");
                }
                gameYml.set("game", false);
                try {
                    gameYml.save(game);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }, 100);
        }, 600);
    }
}

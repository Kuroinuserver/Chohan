package com.kuroinusaba.chohan.Commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.kuroinusaba.chohan.Chohan.*;
import static org.bukkit.Bukkit.broadcastMessage;

public class chohan implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 0) {
            sender.sendMessage("§c§l引数が足りません。");
            return true;
        }
        if (args.length == 1) {
            if (args[0].equals("cho")) {
                Player player = (Player) sender;
                File game = new File("plugins/Chohan/game.yml");
                if (!(game.exists())) {
                    sender.sendMessage(prefix + "§c§lゲームが開始されていません。/chohan start <bet>でゲームを開始してください。");
                    return true;
                }
                YamlConfiguration gameYml = YamlConfiguration.loadConfiguration(game);
                if (!(gameYml.getBoolean("game"))) {
                    sender.sendMessage(prefix + "§c§lゲームが開始されていません。/chohan start <bet>でゲームを開始してください。");
                    return true;
                }
                List<Player> choplayers = (List<Player>) gameYml.getList("choplayers");
                List<Player> hanplayers = (List<Player>) gameYml.getList("hanplayers");
                if (hanplayers.contains((Player) sender) || choplayers.contains((Player) sender)) {
                    sender.sendMessage(prefix + "§c§lすでにかけています。");
                    return true;
                }
                if (gameYml.getInt("bet") > econ.getBalance((Player) sender)) {
                    sender.sendMessage(prefix + "§c§l所持金が足りません。");
                    return true;
                }
                File playerdata = new File("plugins/Chohan/playerdata/" + player.getUniqueId() + ".yml");
                YamlConfiguration playerdataYml = YamlConfiguration.loadConfiguration(playerdata);
                playerdataYml.set("totalbet", playerdataYml.getInt("totalbet") + gameYml.getInt("bet"));
                try {
                    playerdataYml.save(playerdata);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                econ.withdrawPlayer((Player) sender, gameYml.getInt("bet"));
                choplayers = (List<Player>) gameYml.getList("choplayers");
                choplayers.add((Player) sender);
                gameYml.set("choplayers", choplayers);
                gameYml.set("cho", gameYml.getInt("cho") + 1);
                gameYml.set("totalbet", gameYml.getInt("totalbet") + gameYml.getInt("bet"));
                try {
                    gameYml.save(game);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                sender.sendMessage(prefix + "§a§l丁にかけました。");
                broadcastMessage(prefix + "§a§l" + sender.getName() + "が丁にかけました。");
                return true;
            } else if (args[0].equals("han")) {
                Player player = (Player) sender;
                File game = new File("plugins/Chohan/game.yml");
                if (!(game.exists())) {
                    sender.sendMessage(prefix + "§c§lゲームが開始されていません。/chohan start <bet>でゲームを開始してください。");
                    return true;
                }
                YamlConfiguration gameYml = YamlConfiguration.loadConfiguration(game);
                if (!(gameYml.getBoolean("game"))) {
                    sender.sendMessage(prefix + "§c§lゲームが開始されていません。/chohan start <bet>でゲームを開始してください。");
                    return true;
                }
                List<Player> choplayers = (List<Player>) gameYml.getList("choplayers");
                List<Player> hanplayers = (List<Player>) gameYml.getList("hanplayers");
                if (hanplayers.contains((Player) sender) || choplayers.contains((Player) sender)) {
                    sender.sendMessage(prefix + "§c§lすでにかけています。");
                    return true;
                }
                if (gameYml.getInt("bet") > econ.getBalance((Player) sender)) {
                    sender.sendMessage(prefix + "§c§l所持金が足りません。");
                    return true;
                }
                File playerdata = new File("plugins/Chohan/playerdata/" + player.getUniqueId() + ".yml");
                YamlConfiguration playerdataYml = YamlConfiguration.loadConfiguration(playerdata);
                playerdataYml.set("totalbet", playerdataYml.getInt("totalbet") + gameYml.getInt("bet"));
                try {
                    playerdataYml.save(playerdata);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                econ.withdrawPlayer((Player) sender, gameYml.getInt("bet"));
                hanplayers = (List<Player>) gameYml.getList("hanplayers");
                hanplayers.add((Player) sender);
                gameYml.set("hanplayers", hanplayers);
                gameYml.set("han", gameYml.getInt("han") + 1);
                gameYml.set("totalbet", gameYml.getInt("totalbet") + gameYml.getInt("bet"));
                try {
                    gameYml.save(game);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                sender.sendMessage(prefix + "§a§l半にかけました。");
                broadcastMessage(prefix + "§c§l" + sender.getName() + "が半にかけました。");
                return true;
            } else if (args[0].equals("ranking")) {
                Player player = (Player) sender;
                File playerdata = new File("plugins/Chohan/playerdata");
                List<Integer> ranking = new ArrayList<Integer>();
                List<Integer> ranking2 = new ArrayList<Integer>();
                List<Integer> rankingnum = new ArrayList<Integer>();
                List<String> rankingname = new ArrayList<String>();
                File[] playerdataFiles = playerdata.listFiles();
                for (File playerdataFile : playerdataFiles) {
                    YamlConfiguration playerdataYml = YamlConfiguration.loadConfiguration(playerdataFile);
                    if (!(playerdataYml.getInt("totalwin") - playerdataYml.getInt("totalbet") == 0)) {
                        ranking.add((playerdataYml.getInt("totalwin") - playerdataYml.getInt("totalbet")));
                        rankingname.add(playerdataYml.getString("name"));
                    }
                }
                for (File playerdataFile : playerdataFiles) {
                    YamlConfiguration playerdataYml = YamlConfiguration.loadConfiguration(playerdataFile);
                    if (playerdataYml.getInt("totalwin") - playerdataYml.getInt("totalbet") == 0) {
                        continue;
                    }
                    // rankingnumにはrankingに入っているtotalbet - totalwinの値の順位を入れる
                    ranking2 = ranking;
                    ranking2.sort(Collections.reverseOrder());
                    rankingnum.add(ranking2.indexOf(playerdataYml.getInt("totalwin") - playerdataYml.getInt("totalbet")));
                }
                int fornum = ranking.size();
                if (fornum > 10) {
                    fornum = 10;
                }
                ranking.sort(Collections.reverseOrder());
                player.sendMessage(prefix + "§a§l丁半のランキング");
                for (int i = 0; i < fornum; i++) {
                    player.sendMessage(prefix + "§a§l" + (i + 1) + "位: " + rankingname.get(rankingnum.indexOf(i)) + " §a§l" + ranking.get(i) + "円");
                }
                return true;
            } else {
                sender.sendMessage(prefix + "§c§l引数が違います。");
                return true;
            }
        }
        if (args.length == 2) {
            if (args[0].equals("start")) {
                // args[1]が数字かどうか
                if (args[1].matches("[0-9]+")) {
                    File game = new File("plugins/Chohan/game.yml");
                    if (game.exists() && YamlConfiguration.loadConfiguration(game).getBoolean("game")) {
                        sender.sendMessage(prefix + "§c§l既にゲームが開始されています。");
                        return true;
                    }
                    YamlConfiguration gameYml = YamlConfiguration.loadConfiguration(game);
                    List<Player> choplayers = new ArrayList<>();
                    List<Player> hanplayers = new ArrayList<>();
                    gameYml.set("game", true);
                    gameYml.set("cho", 0);
                    gameYml.set("han", 0);
                    gameYml.set("choplayers", choplayers);
                    gameYml.set("hanplayers", hanplayers);
                    gameYml.set("bet", Integer.parseInt(args[1]));
                    gameYml.set("totalbet", 0);
                    try {
                        gameYml.save(game);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    sender.sendMessage(prefix + "§a§lゲームを開始しました。");
                    broadcastMessage(prefix + "§a§l" + sender.getName() + "が§e§l" + args[1] + "円§a§lで丁半を開始しました。");
                    new chohanstart(Integer.parseInt(args[1]) ,sender);
                    return true;
                } else {
                    sender.sendMessage("§c§l引数が違います。");
                    return true;
                }
            } else {
                sender.sendMessage("§c§l引数が違います。");
                return true;
            }
        }
        return false;
    }
    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        List<String> tab = new ArrayList<>();
        if (args.length == 1) {
            tab.add("start");
            tab.add("cho");
            tab.add("han");
            tab.add("ranking");
            return tab;
        }
        if (args.length == 2) {
            if (args[0].equals("start"))  {
                tab.add("<bet>");
                return tab;
            }
        }
        return null;
    }

}

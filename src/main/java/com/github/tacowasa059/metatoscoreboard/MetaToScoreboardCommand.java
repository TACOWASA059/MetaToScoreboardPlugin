package com.github.tacowasa059.metatoscoreboard;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

import java.util.ArrayList;
import java.util.List;

class MetaToScoreboardCommand implements CommandExecutor , TabCompleter {

    public MetaToScoreboardCommand() {
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 2) {
            sender.sendMessage(ChatColor.LIGHT_PURPLE + "使用方法: /metatoscoreboard <objective> <metadatakey>");
            return true;
        }

        String objectiveName = args[0];
        String metadataKey = args[1];

        String specifiedPluginName = null;
        String actualMetadataKey = metadataKey;

        // ":" がある場合、分割して pluginName:metadataKey の形式を取得
        if (metadataKey.contains(":")) {
            String[] parts = metadataKey.split(":", 2);
            specifiedPluginName = parts[0];
            actualMetadataKey = parts[1];
        }

        // スコアボードとObjectiveを取得
        if (Bukkit.getScoreboardManager() == null) return true;
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
        Objective objective = scoreboard.getObjective(objectiveName);

        if (objective == null) {
            objective = scoreboard.registerNewObjective(objectiveName, "dummy", ChatColor.GOLD + objectiveName);
        }

        int updatedPlayers = 0; // 更新したプレイヤー数をカウント

        // 全プレイヤーを対象にメタデータを取得・スコア設定
        for (Player player : Bukkit.getOnlinePlayers()) {
            List<MetadataValue> metadataValues = player.getMetadata(actualMetadataKey);
            int valueToSet = -1; // デフォルト値を -1 に設定

            if (!metadataValues.isEmpty()) {
                for (MetadataValue value : metadataValues) {
                    if(value.getOwningPlugin()==null)return true;
                    String owningPluginName = value.getOwningPlugin().getName();

                    // 指定されたプラグイン名があり、それと一致しないならスキップ
                    if (specifiedPluginName != null && !specifiedPluginName.equals(owningPluginName)) {
                        continue;
                    }

                    // メタデータの値を取得
                    if (value.value() instanceof Boolean) {
                        valueToSet = ((Boolean) value.value()) ? 1 : 0;
                    } else if (value.value() instanceof Number) {
                        valueToSet = ((Number) value.value()).intValue();
                    } else {
                        valueToSet = 1;
                    }
                    break;
                }
            }

            Score score = objective.getScore(player.getName());
            score.setScore(valueToSet);
            updatedPlayers++;
        }

        sender.sendMessage(ChatColor.GREEN + "スコアボード " + ChatColor.AQUA + objectiveName + ChatColor.GREEN +
                " に " + ChatColor.AQUA + updatedPlayers + ChatColor.GREEN + " 人のメタデータ " + ChatColor.AQUA + metadataKey +
                ChatColor.GREEN + " を設定しました。");

        return true;
    }


    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            // 既存のスコアボード名を補完
            if(Bukkit.getScoreboardManager()==null) return new ArrayList<>();
            Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
            for (Objective objective : scoreboard.getObjectives()) {
                completions.add(objective.getName());
            }
        }

        return completions;
    }
}

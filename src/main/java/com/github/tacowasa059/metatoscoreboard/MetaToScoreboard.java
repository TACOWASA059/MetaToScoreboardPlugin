package com.github.tacowasa059.metatoscoreboard;

import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

public final class MetaToScoreboard extends JavaPlugin {

    @Override
    public void onEnable() {
        MetaToScoreboardCommand commandExecutor = new MetaToScoreboardCommand();
        PluginCommand command = this.getCommand("metatoscoreboard");
        if (command != null) {
            command.setExecutor(commandExecutor);
            command.setTabCompleter(commandExecutor);
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}

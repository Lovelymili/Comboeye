package org.mili.aiac;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

import static sun.awt.Win32GraphicsConfig.getConfig;

public final class Comboeye extends JavaPlugin {

    @Override
    public void onEnable() {
        getLogger().info("Comboeye has been enabled!");
        getLogger().info("Github:https://github.com/Lovelymili/Comboeye");
        getLogger().info("It's free and open source!Don't buy it from anywhere!");
        LoadConfig();
        getServer().getPluginManager().registerEvents(new GetPlayerData(), this);
        getServer().getPluginManager().registerEvents(new GetPlayerData(), this);
        saveDefaultConfig();
        File collectfile = new File(getDataFolder(), "collect.yml");
        if (!collectfile.exists()) {
            saveResource("collect.yml", false);  // 从插件 JAR 包复制资源到数据文件夹
        }

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
    public void LoadConfig() {
        FileConfiguration config = getConfig();
        Global.BaseURL = config.getString("BaseURL");
        Global.key = config.getString("key");
        Global.v1 = config.getInt("v1");
        Global.v2 = config.getInt("v2");
        Global.v3 = config.getInt("v3");
        Global.t1 = config.getInt("t1");
        Global.t2 = config.getInt("t2");
        Global.t3 = config.getInt("t3");
        Global.warn_text = config.getString("warn_text");
        Global.kick_text = config.getString("kick_text");
        Global.ban_text = config.getString("ban_text");

    }
}

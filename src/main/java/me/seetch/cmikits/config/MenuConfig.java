package me.seetch.cmikits.config;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import su.daycube.ColorUtil;

import java.util.List;

public record MenuConfig(FileConfiguration config) {

    public String getTitle(String menu) {
        return ColorUtil.colorize(config.getString("menus." + menu + ".title", ""));
    }

    public int getSize(String menu) {
        return config.getInt("menus." + menu + ".size", 3);
    }

    public List<String> getPattern(String menu) {
        return config.getStringList("menus." + menu + ".pattern");
    }

    public ConfigurationSection getKitItemSection() {
        return config.getConfigurationSection("menus.kits.kit-item");
    }

    public ConfigurationSection getButtonSection(String button) {
        return config.getConfigurationSection("menus.preview.buttons." + button);
    }
}

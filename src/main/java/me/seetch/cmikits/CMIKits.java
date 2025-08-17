package me.seetch.cmikits;

import com.Zrips.CMI.CMI;
import me.seetch.cmikits.config.MenuConfig;
import me.seetch.cmikits.menu.KitsMenu;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public final class CMIKits extends JavaPlugin {

    private CMI cmiInstance;
    private MenuConfig menuConfig;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        this.menuConfig = new MenuConfig(getConfig());

        getCommand("kits").setExecutor((sender, command, label, args) -> {
            if (sender instanceof Player player) {
                new KitsMenu(this, player).open();
            }
            return true;
        });

        cmiInstance = CMI.getInstance();
        if (cmiInstance == null) {
            getLogger().severe("CMI not found!");
            Bukkit.getPluginManager().disablePlugin(this);
        }
    }

    public CMI getCmiInstance() {
        return cmiInstance;
    }

    public MenuConfig getMenuConfig() {
        return menuConfig;
    }
}
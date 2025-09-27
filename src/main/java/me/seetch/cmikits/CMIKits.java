package me.seetch.cmikits;

import com.Zrips.CMI.CMI;
import com.Zrips.CMI.Modules.Kits.Kit;
import me.seetch.cmikits.config.MenuConfig;
import me.seetch.cmikits.menu.KitPreviewMenu;
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
                if (args.length > 0) {
                    String kitName = args[0];
                    Kit kit = getCmiInstance().getKitsManager().getKit(kitName);
                    if (kit != null) {
                        new KitPreviewMenu(this, player, kit).open();
                    } else {
                        new KitsMenu(this, player).open();
                    }
                } else {
                    new KitsMenu(this, player).open();
                }
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
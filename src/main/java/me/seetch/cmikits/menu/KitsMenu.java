package me.seetch.cmikits.menu;

import com.Zrips.CMI.Modules.Kits.Kit;
import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import me.seetch.cmikits.CMIKits;
import net.kyori.adventure.text.Component;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import su.daycube.ColorUtil;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

public class KitsMenu {
    private final CMIKits plugin;
    private final Player player;
    private final Gui gui;

    public KitsMenu(CMIKits plugin, Player player) {
        this.plugin = plugin;
        this.player = player;
        this.gui = Gui.gui()
                .title(Component.text(ColorUtil.colorize(plugin.getMenuConfig().getTitle("kits"))))
                .rows(plugin.getMenuConfig().getSize("kits"))
                .create();

        setupItems();
    }

    private void setupItems() {
        LinkedHashMap<String, Kit> kitMap = plugin.getCmiInstance().getKitsManager().getKitMap();
        List<String> pattern = plugin.getMenuConfig().getPattern("kits");

        if (!pattern.isEmpty()) {
            // Apply pattern
            int kitIndex = 0;
            Kit[] kits = kitMap.values().toArray(new Kit[0]);

            for (int row = 0; row < pattern.size(); row++) {
                String rowPattern = pattern.get(row);
                for (int col = 0; col < rowPattern.length(); col++) {
                    if (rowPattern.charAt(col) == 'X' && kitIndex < kits.length) {
                        gui.setItem(row * 9 + col, createKitItem(kits[kitIndex++]));
                    }
                }
            }
        } else {
            // Fallback to adding items sequentially
            for (Kit kit : kitMap.values()) {
                gui.addItem(createKitItem(kit));
            }
        }
    }

    private GuiItem createKitItem(Kit kit) {
        ConfigurationSection kitItemSection = plugin.getMenuConfig().getKitItemSection();

        String name = kitItemSection.getString("name", "&e%kit%").replace("%kit%", kit.getDisplayName());
        List<String> lore = kitItemSection.getStringList("lore").stream()
                .map(line -> line.replace("%kit%", kit.getDisplayName())).toList();

        return ItemBuilder.from(kit.getSafeIcon())
                .setName(ColorUtil.colorize(name))
                .setLore(lore.stream().map(ColorUtil::colorize).collect(Collectors.toList()))
                .flags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS)
                .asGuiItem(event -> new KitPreviewMenu(plugin, player, kit).open());
    }

    public void open() {
        gui.open(player);
    }
}

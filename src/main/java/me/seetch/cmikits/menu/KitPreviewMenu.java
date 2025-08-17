package me.seetch.cmikits.menu;

import com.Zrips.CMI.Containers.CMIPlayerInventory;
import com.Zrips.CMI.Modules.Kits.Kit;
import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import me.seetch.cmikits.CMIKits;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import su.daycube.ColorUtil;

import java.util.List;
import java.util.stream.Collectors;

public class KitPreviewMenu {
    private final CMIKits plugin;
    private final Player player;
    private final Kit kit;
    private final Gui gui;
    private int itemSlotCounter = 0;

    public KitPreviewMenu(CMIKits plugin, Player player, Kit kit) {
        this.plugin = plugin;
        this.player = player;
        this.kit = kit;
        this.gui = Gui.gui()
                .title(Component.text(ColorUtil.colorize(plugin.getMenuConfig().getTitle("preview")
                        .replace("%kit%", kit.getDisplayName()))))
                .rows(plugin.getMenuConfig().getSize("preview"))
                .create();

        setupItems();
    }

    private void setupItems() {
        List<String> pattern = plugin.getMenuConfig().getPattern("preview");
        if (!pattern.isEmpty()) {
            applyPattern(pattern);
        } else {
            setupDefaultItems();
        }
    }

    private void applyPattern(List<String> pattern) {
        List<ItemStack> kitItems = kit.getItems().stream()
                .filter(item -> item != null && item.getType() != Material.AIR)
                .collect(Collectors.toList());

        for (int row = 0; row < pattern.size(); row++) {
            String rowPattern = pattern.get(row);
            for (int col = 0; col < rowPattern.length(); col++) {
                char c = rowPattern.charAt(col);
                int slot = row * 9 + col;

                switch (c) {
                    case 'X':
                        if (itemSlotCounter < kitItems.size()) {
                            gui.setItem(slot, ItemBuilder.from(kitItems.get(itemSlotCounter++)).asGuiItem());
                        }
                        break;
                    case 'H':
                        addMainHand(slot);
                        break;
                    case 'O':
                        addOffHand(slot);
                        break;
                    case '1':
                        addHelmet(slot);
                        break;
                    case '2':
                        addChestplate(slot);
                        break;
                    case '3':
                        addLeggings(slot);
                        break;
                    case '4':
                        addBoots(slot);
                        break;
                    case 'A':
                        addBackButton(slot);
                        break;
                    case 'B':
                        addClaimButton(slot);
                        break;
                }
            }
        }
    }

    private void setupDefaultItems() {
        // Armor
        addHelmet(0);
        addChestplate(1);
        addLeggings(2);
        addBoots(3);

        // Hands
        addMainHand(4);
        addOffHand(5);

        // Kit items
        int slot = 9;
        for (ItemStack item : kit.getItems()) {
            if (item != null && item.getType() != Material.AIR) {
                if (slot >= gui.getRows() * 9) break;
                gui.setItem(slot++, ItemBuilder.from(item).asGuiItem());
            }
        }

        // Buttons
        addBackButton(53);
        addClaimButton(52);
    }

    private void addMainHand(int slot) {
        ItemStack item = kit.getExtraItem(player, CMIPlayerInventory.CMIInventorySlot.MainHand);
        if (item != null && item.getType() != Material.AIR) {
            gui.setItem(slot, ItemBuilder.from(item).asGuiItem());
        }
    }

    private void addOffHand(int slot) {
        ItemStack item = kit.getExtraItem(player, CMIPlayerInventory.CMIInventorySlot.OffHand);
        if (item != null && item.getType() != Material.AIR) {
            gui.setItem(slot, ItemBuilder.from(item).asGuiItem());
        }
    }

    private void addHelmet(int slot) {
        ItemStack item = kit.getExtraItem(player, CMIPlayerInventory.CMIInventorySlot.Helmet);
        if (item != null && item.getType() != Material.AIR) {
            gui.setItem(slot, ItemBuilder.from(item).asGuiItem());
        }
    }

    private void addChestplate(int slot) {
        ItemStack item = kit.getExtraItem(player, CMIPlayerInventory.CMIInventorySlot.ChestPlate);
        if (item != null && item.getType() != Material.AIR) {
            gui.setItem(slot, ItemBuilder.from(item).asGuiItem());
        }
    }

    private void addLeggings(int slot) {
        ItemStack item = kit.getExtraItem(player, CMIPlayerInventory.CMIInventorySlot.Pants);
        if (item != null && item.getType() != Material.AIR) {
            gui.setItem(slot, ItemBuilder.from(item).asGuiItem());
        }
    }

    private void addBoots(int slot) {
        ItemStack item = kit.getExtraItem(player, CMIPlayerInventory.CMIInventorySlot.Boots);
        if (item != null && item.getType() != Material.AIR) {
            gui.setItem(slot, ItemBuilder.from(item).asGuiItem());
        }
    }

    private void addBackButton(int slot) {
        ConfigurationSection buttonSection = plugin.getMenuConfig().getButtonSection("back");
        gui.setItem(slot, ItemBuilder.from(Material.ARROW)
                .setName(ColorUtil.colorize(buttonSection.getString("name", "&cBack")))
                .setLore(buttonSection.getStringList("lore").stream()
                        .map(ColorUtil::colorize)
                        .collect(Collectors.toList()))
                .asGuiItem(e -> new KitsMenu(plugin, player).open()));
    }

    private void addClaimButton(int slot) {
        ConfigurationSection buttonSection = plugin.getMenuConfig().getButtonSection("claim");
        gui.setItem(slot, ItemBuilder.from(Material.CHEST)
                .setName(ColorUtil.colorize(buttonSection.getString("name", "&aClaim Kit")))
                .setLore(buttonSection.getStringList("lore").stream()
                        .map(ColorUtil::colorize)
                        .collect(Collectors.toList()))
                .asGuiItem(e -> {
                    player.closeInventory();
                    Bukkit.dispatchCommand(player, "cmi kit " + kit.getCommandName());
                }));
    }

    public void open() {
        gui.open(player);
    }
}

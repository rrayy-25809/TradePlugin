package de.stecher42.plugins.tradeplugin.utils;

import de.stecher42.plugins.tradeplugin.main.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;

public class TradingWindow {
    public TradingWindow(Player player, Player oppositeDealPartner) {
        Inventory inv = Bukkit.createInventory(null, InventoryType.CHEST,
                ChatColor.AQUA + "Trading offer with " + oppositeDealPartner.getName());
        DealMaker dm = Main.getPlugin().getDealMaker();
        dm.addInv(inv);
        player.openInventory(inv);
        oppositeDealPartner.openInventory(inv);
    }


}

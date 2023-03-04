package de.stecher42.plugins.tradeplugin.utils;

import de.stecher42.plugins.tradeplugin.main.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class TradingWindow implements Listener {
    final int ROWS = 6;
    final int CHEST_SIZE = 9 * ROWS;

    Player player;
    Player opposite;

    Inventory playerInventory;
    Inventory oppositeInventory;

    ItemStack oppositeRedGlass;
    ItemStack oppositeGreenGlass;
    ItemStack ownRedGlass;
    ItemStack ownGreenGlass;

    boolean playerAcceptedDeal;
    boolean oppositeAcceptedDeal;

    public TradingWindow() {};

    public TradingWindow(Player player, Player oppositeDealPartner) {
        this.player = player;
        this.opposite = oppositeDealPartner;

        playerAcceptedDeal = false;
        oppositeAcceptedDeal = false;

        this.playerInventory = Bukkit.createInventory(null, CHEST_SIZE,
                "§6§lDeal with " + oppositeDealPartner.getName());
        this.oppositeInventory = Bukkit.createInventory(null, CHEST_SIZE,
                 "§6§lDeal with " + player.getName());


        prepareInventory(playerInventory);
        prepareInventory(oppositeInventory);

        DealMaker dm = Main.getPlugin().getDealMaker();
        dm.addTradingWindow(this);
        player.openInventory(playerInventory);
        oppositeDealPartner.openInventory(oppositeInventory);

    }

    private void prepareInventory(Inventory inv) {
        ItemStack filler = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
        ItemMeta im = filler.getItemMeta();
        im.setDisplayName("§7§l(Filler)");
        filler.setItemMeta(im);

        ItemStack separator = new ItemStack(Material.WHITE_STAINED_GLASS_PANE);
        ItemMeta imSep = separator.getItemMeta();
        imSep.setDisplayName("§7§l§i(Deal partner's field)");
        separator.setItemMeta(imSep);

        ItemStack personalTradeAccepment = new ItemStack(Material.GREEN_STAINED_GLASS_PANE);
        ItemMeta imPTA = personalTradeAccepment.getItemMeta();
        imPTA.setDisplayName("§2§lAccept Trade");
        personalTradeAccepment.setItemMeta(imPTA);

        this.initGlassConfig();



        for(int i = 0; i < ROWS * 9; i++) {
            if(isPersonalTradeAccepmentField(i)) {
                inv.setItem(i, ownGreenGlass);
            } else if(isOpponentsField(i)) {
                inv.setItem(i, separator);
            } else if(isOpponentsAccepmentField(i)) {
                inv.setItem(i, oppositeRedGlass);
            } else if(isFillerIndex(i)) {
                inv.setItem(i, filler);
            }
        }
    }

    public void initGlassConfig() {
        oppositeRedGlass = new ItemStack(Material.RED_STAINED_GLASS_PANE);
        ItemMeta imRed = oppositeRedGlass.getItemMeta();
        imRed.setDisplayName("§c§lOpposite didn't accepted yet");
        oppositeRedGlass.setItemMeta(imRed);

        oppositeGreenGlass = new ItemStack(Material.GREEN_STAINED_GLASS_PANE);
        ItemMeta imOppGreen = oppositeGreenGlass.getItemMeta();
        imOppGreen.setDisplayName("§a§lOpposite accepts this deal");
        oppositeGreenGlass.setItemMeta(imOppGreen);

        ownRedGlass = new ItemStack(Material.RED_STAINED_GLASS_PANE);
        ItemMeta imOwnRed = ownRedGlass.getItemMeta();
        imOwnRed.setDisplayName("§c§lDecline this deal");
        ownRedGlass.setItemMeta(imOwnRed);

        ownGreenGlass = new ItemStack(Material.GREEN_STAINED_GLASS_PANE);
        ItemMeta imOwnGreen = ownGreenGlass.getItemMeta();
        imOwnGreen.setDisplayName("§a§lAccept this deal");
        ownGreenGlass.setItemMeta(imOwnGreen);
    }


    // -- Togggler for deal status

    public void toggleOpponentsStatus(TradingWindow tw) {

        tw.oppositeAcceptedDeal = !tw.oppositeAcceptedDeal;
        for(int i = 0; i < ROWS * 9; i++) {
            if(tw.oppositeAcceptedDeal) {
                if(isOpponentsAccepmentField(i)) {
                    tw.playerInventory.setItem(i, tw.oppositeGreenGlass);
                }
                if(isPersonalTradeAccepmentField(i)) {
                    tw.oppositeInventory.setItem(i, tw.ownRedGlass);
                }
            } else {
                if(isOpponentsAccepmentField(i)) {
                    tw.playerInventory.setItem(i, tw.oppositeRedGlass);
                }
                if(isPersonalTradeAccepmentField(i)) {
                    tw.oppositeInventory.setItem(i, tw.ownGreenGlass);
                }
            }
        }
    }

    public void toggleOwnStatus(TradingWindow tw, Inventory inv) {
        tw.playerAcceptedDeal = !tw.playerAcceptedDeal;
        for(int i = 0; i < ROWS * 9; i++) {
            if(tw.playerAcceptedDeal) {
                if(isOpponentsAccepmentField(i)) {
                    tw.oppositeInventory.setItem(i, tw.oppositeGreenGlass);
                }
                if(isPersonalTradeAccepmentField(i)) {
                    tw.playerInventory.setItem(i, tw.ownRedGlass);
                }
            } else {
                if(isOpponentsAccepmentField(i)) {
                    tw.oppositeInventory.setItem(i, tw.oppositeRedGlass);
                }
                if(isPersonalTradeAccepmentField(i)) {
                    tw.playerInventory.setItem(i, tw.ownGreenGlass);
                }
            }
        }
    }

    // --- Slot checker

    private boolean isPersonalTradeAccepmentField(int index) {
        return index > 9 * ROWS - 9 && index < 9 * ROWS - 5;
    }


    private boolean isOpponentsAccepmentField(int index) {
        return index > 9 * ROWS - 5 && index < 9 * ROWS - 1;
    }

    private boolean isOwnField(int index) {
        return index > 9 && index < 9 * ROWS - 9 && (index + 8) % 9 < 3;
    }

    private boolean isOpponentsField(int index) {
        return index > 13 && index < 9 * ROWS - 9 && (index + 4) % 9 < 3;
    }

    private boolean isFillerIndex(int index) {
        return index % 9 == 0 || (index + 1) % 9 == 0 || index < 9 || index > 9 * ROWS - 9 || (index + 5) % 9 == 0;
    }

    // --- EventHandlers

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if(!(e.getWhoClicked() instanceof Player)) return;
        Player p = (Player) e.getWhoClicked();

        if(Main.getPlugin().getDealMaker().isInventoryInList(e.getClickedInventory())) {
            TradingWindow tw = Main.getPlugin().getDealMaker().getTradingWindow(e.getClickedInventory());
            if(e.getClickedInventory().equals(tw.playerInventory)) {
                if(isPersonalTradeAccepmentField(e.getSlot())) {
                    // Player toggles deal status
                    e.setCancelled(true);
                    this.toggleOwnStatus(tw, e.getClickedInventory());
                    p.sendMessage(Main.PREFIX + "Toggled your deal status");
                } else if(isOwnField(e.getSlot())) {
                    e.setCancelled(false);
                } else {
                    e.setCancelled(true);
                }
            } else if(e.getClickedInventory().equals(tw.oppositeInventory)) {
                if(isPersonalTradeAccepmentField(e.getSlot())) {
                    // Opposite toggles own deal status
                    e.setCancelled(true);
                    this.toggleOpponentsStatus(tw);
                    p.sendMessage(Main.PREFIX + "Opposite toggled it deal status");
                } else if(isOwnField(e.getSlot())) {
                    e.setCancelled(false);
                } else {
                    e.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent e) {
        if(!Main.getPlugin().getDealMaker().isInventoryInList(e.getInventory())) return;
        TradingWindow tw = Main.getPlugin().getDealMaker().getTradingWindow(e.getInventory());

        if(tw.oppositeAcceptedDeal && tw.playerAcceptedDeal) {
            // Both accepted the deal and the items to deal get flipped

            for(int i = 0; i < ROWS * 9; i++) {
                if(isOwnField(i)) {
                    if(tw.playerInventory.getItem(i) != null)
                        tw.opposite.getInventory().addItem(tw.playerInventory.getItem(i));
                } else if(isOpponentsField(i)) {
                    if(tw.playerInventory.getItem(i) != null)
                        tw.player.getInventory().addItem(tw.playerInventory.getItem(i));
                }
            }
        } else {
            // Deal got declined, both players get their own items back

            for(int i = 0; i < ROWS * 9; i++) {
                if(isOwnField(i)) {
                    if(tw.playerInventory.getItem(i) != null)
                        tw.player.getInventory().addItem(tw.playerInventory.getItem(i));
                } else if(isOpponentsField(i)) {
                    if(tw.playerInventory.getItem(i) != null)
                        tw.opposite.getInventory().addItem(tw.playerInventory.getItem(i));
                }
            }
//            Enable after testing
            //        tw.player.sendMessage(Main.PREFIX + "You declined the deal with " + tw.opposite.getName() +
            //                " by closing your inventory!");
            //        tw.opposite.sendMessage(Main.PREFIX + tw.player.getName() + " declined the deal!");
            //        Main.getPlugin().getDealMaker().removeTradingWindow(tw);
        }
    }


}

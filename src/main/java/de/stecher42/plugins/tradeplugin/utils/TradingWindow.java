package de.stecher42.plugins.tradeplugin.utils;

import de.stecher42.plugins.tradeplugin.main.Main;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;

public class TradingWindow implements Listener {
    final int ROWS = 6;
    final int CHEST_SIZE = 9 * ROWS;
    final String OPPOSITE_FIELD_GLASS_NAME = "§7§l§i(Deal partner's field)";
    int slots;

    Player player;
    Player opposite;

    Inventory playerInventory;
    Inventory oppositeInventory;

    ItemStack[] playerSlots;
    ItemStack[] oppositeSlots;

    ItemStack oppositeRedGlass;
    ItemStack oppositeGreenGlass;
    ItemStack ownRedGlass;
    ItemStack ownGreenGlass;
    ItemStack separator;

    boolean playerAcceptedDeal;
    boolean oppositeAcceptedDeal;
    boolean paidAfterClose;

    public TradingWindow() {};

    public TradingWindow(Player player, Player oppositeDealPartner) {
        this.player = player;
        this.opposite = oppositeDealPartner;

        playerAcceptedDeal = false;
        oppositeAcceptedDeal = false;
        paidAfterClose = false;

        this.playerInventory = Bukkit.createInventory(null, CHEST_SIZE,
                "§6§lDeal with " + oppositeDealPartner.getName());
        this.oppositeInventory = Bukkit.createInventory(null, CHEST_SIZE,
                 "§6§lDeal with " + player.getName());


        prepareInventory(playerInventory);
        prepareInventory(oppositeInventory);

        this.slots = this.countOwnSlots();
        this.playerSlots = new ItemStack[slots];
        this.oppositeSlots = new ItemStack[slots];

        System.out.println("TW-Slots: " + this.slots);

        DealMaker dm = Main.getPlugin().getDealMaker();
        dm.addTradingWindow(this);
        player.openInventory(playerInventory);
        if(!this.paidAfterClose)
            oppositeDealPartner.openInventory(oppositeInventory);
        player.playNote(player.getLocation(), Instrument.SNARE_DRUM, Note.natural(1, Note.Tone.D));
        opposite.playNote(opposite.getLocation(), Instrument.SNARE_DRUM, Note.natural(1, Note.Tone.D));

    }

    private void prepareInventory(Inventory inv) {
        ItemStack filler = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
        ItemMeta im = filler.getItemMeta();
        im.setDisplayName("§7§l(Filler)");
        filler.setItemMeta(im);

        separator = new ItemStack(Material.WHITE_STAINED_GLASS_PANE);
        ItemMeta imSep = separator.getItemMeta();
        imSep.setDisplayName(OPPOSITE_FIELD_GLASS_NAME);
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

        if(playerAcceptedDeal && oppositeAcceptedDeal)
            tw.playerInventory.close();
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
        if(playerAcceptedDeal && oppositeAcceptedDeal)
            tw.playerInventory.close();
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

    private int countOwnSlots() {
        int count = 0;
        for(int i = 0; i < ROWS * 9; i++) {
            if(isOwnField(i)) count++;
        }
        return count;
    }

    private ItemStack[] projectToItemField(Inventory inv) {
        int pointer = 0; // keeps track of how many slots already inserted to result array
        ItemStack[] result = new ItemStack[this.slots];
        for(int i = 0; i < ROWS * 9; i++) {
            if(isOwnField(i)) {
                if(inv.getItem(i) != null)
                    result[pointer] = inv.getItem(i);
                else
                    result[pointer] = null;
                pointer++;
            }
        }
        return result;
    }

    private void projectToOpponentField(ItemStack[] playerItems, boolean toPlayersInventory) {
        int pointer = 0;
        for(int i = 0; i < ROWS * 9; i++) {
            if(toPlayersInventory) {
                if(isOpponentsField(i)) {
                    if(playerItems[pointer] != null) {
                        ItemStack itemStack = playerItems[pointer].clone();
                        ItemMeta im = itemStack.getItemMeta();
                        ArrayList<String> meta = new ArrayList<String>();
                        meta.add("§r§lDeal partner's ");
                        meta.add("§r§litem");
                        im.setLore(meta);
                        itemStack.setItemMeta(im);
                        this.playerInventory.setItem(i, itemStack);
                    } else {
                        this.playerInventory.setItem(i, this.separator);
                    }
                    pointer++;
                }
            } else {
                if(isOpponentsField(i)) {
                    if(playerItems[pointer] != null) {
                        ItemStack itemStack = playerItems[pointer].clone();
                        ItemMeta im = itemStack.getItemMeta();
                        ArrayList<String> meta = new ArrayList<String>();
                        meta.add("§r§lDeal partner's ");
                        meta.add("§r§litem");
                        im.setLore(meta);
                        itemStack.setItemMeta(im);
                        this.oppositeInventory.setItem(i, itemStack);
                    } else {
                        this.oppositeInventory.setItem(i, this.separator);
                    }
                    pointer++;
                }
            }
        }
    }

    private void _refreshInventorySwitchAsyncHelper() {
        // Helper method, submethoded to get calles async with some delay to wait, until the item got stored in inv

        this.playerSlots = this.projectToItemField(this.playerInventory);
        this.projectToOpponentField(this.playerSlots, false);
        this.oppositeSlots = this.projectToItemField(this.oppositeInventory);
        this.projectToOpponentField(this.oppositeSlots, true);
    }

    private void refreshInventorySwitch() {
        //Just callingg the _refreshInventorySwitchAsyncHelper() method with some async delay to wait for item store

        TradingWindow tw = this;
        Bukkit.getScheduler().runTaskLaterAsynchronously(Main.getPlugin(), new Runnable() {
            @Override
            public void run() {
                tw._refreshInventorySwitchAsyncHelper();
            }
        }, 4);
    }

    private int translateOpponentSlotIndexToOwnSlotIndex(int index, boolean invert) {
        // invert parameter makes the method to a "translateOwnSlotIndexToOpponentSlotIndex()-method
        int opponentSlot = 0;
        int ownSlot = -1;
        for(int i = 0; i < ROWS * 9; i++) {
            if((!invert && isOpponentsField(i)) || (invert && isOwnField(i)) && i < index) {
                opponentSlot++;
            }
        }
        for(int i = 0; i < ROWS * 9; i++) {
            if((!invert && isOwnField(i)) || (invert && isOpponentsField(i)) && opponentSlot > 0) {
                opponentSlot--;
                ownSlot = i;
            }
        }
        return ownSlot;
    }

    private int translateOpponentSlotIndexToOwnSlotIndex(int index) {
        return translateOpponentSlotIndexToOwnSlotIndex(index, false);
    }

    private int translateOwnSlotIndexToOpponentSlotIndex(int index) {
        return translateOpponentSlotIndexToOwnSlotIndex(index, true);
    }

    // --- EventHandlers

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if(!(e.getWhoClicked() instanceof Player)) return;
        Player p = (Player) e.getWhoClicked();
        DealMaker dm = Main.getPlugin().getDealMaker();

        if(Main.getPlugin().getDealMaker().isInventoryInList(e.getClickedInventory())) {
            TradingWindow tw = Main.getPlugin().getDealMaker().getTradingWindow(e.getClickedInventory());

            if(e.getClickedInventory().equals(tw.playerInventory)) {
                if(isPersonalTradeAccepmentField(e.getSlot())) {
                    // Player toggles deal status
                    e.setCancelled(true);
                    this.toggleOwnStatus(tw, e.getClickedInventory());
//                    p.sendMessage(Main.PREFIX + "Toggled your deal status");
                } else if(isOwnField(e.getSlot())) {
                    if(tw.playerAcceptedDeal || tw.oppositeAcceptedDeal)
                        e.setCancelled(true);
                    tw.refreshInventorySwitch();
                } else {
                    e.setCancelled(true);
                }
            } else if(e.getClickedInventory().equals(tw.oppositeInventory)) {
                if(isPersonalTradeAccepmentField(e.getSlot())) {
                    // Opposite toggles own deal status
                    e.setCancelled(true);
                    this.toggleOpponentsStatus(tw);
//                    p.sendMessage(Main.PREFIX + "Opposite toggled it deal status");
                } else if(isOwnField(e.getSlot())) {
                    if(tw.playerAcceptedDeal || tw.oppositeAcceptedDeal)
                        e.setCancelled(true);
                    else
                        e.setCancelled(false);
                    tw.refreshInventorySwitch();
                } else {
                    e.setCancelled(true);
                }
            }
        } else if(dm.isPlayerCurrentlyDealing(p)) {
            TradingWindow tw = dm.getTradingWindowByPlayer(p);
            if(tw.playerAcceptedDeal || tw.oppositeAcceptedDeal) {
                if(e.isShiftClick())
                    e.setCancelled(true);
            } else if(e.isShiftClick()) {
                tw.refreshInventorySwitch();
            }
        }
    }


    @EventHandler
    public void onInventoryClose(InventoryCloseEvent e) {
        DealMaker dm = Main.getPlugin().getDealMaker();
        if(dm.isInventoryInList(e.getInventory())) {
            TradingWindow tw = dm.getTradingWindowByPlayer((Player) e.getPlayer());

            Player p = tw.player;
            Player o = tw.opposite;

            if(!tw.paidAfterClose) {
                tw.paidAfterClose = true;
                if(tw.playerInventory.getViewers().contains(tw.player))
                    tw.playerInventory.close();
                if(tw.oppositeInventory.getViewers().contains(tw.opposite))
                    tw.oppositeInventory.close();
                if(tw.oppositeAcceptedDeal && tw.playerAcceptedDeal) {
                    // Both accepted the deal and the items to deal get flipped

                    // Check, if the items already got moved back to the inventory
                    for(int i = 0; i < ROWS * 9; i++) {
                        if(isOwnField(i)) {
                            if(tw.playerInventory.getItem(i) != null) {
                                if(tw.opposite.getInventory().firstEmpty() > -1)
                                    tw.opposite.getInventory().addItem(tw.playerInventory.getItem(i));
                                else {
                                    tw.opposite.getWorld().dropItem(tw.opposite.getLocation(), tw.playerInventory.getItem(i));
                                }
                            }
                            if(tw.oppositeInventory.getItem(i) != null) {
                                if(tw.player.getInventory().firstEmpty() > -1)
                                    tw.player.getInventory().addItem(tw.oppositeInventory.getItem(i));
                                else {
                                    tw.player.getWorld().dropItem(tw.player.getLocation(), tw.oppositeInventory.getItem(i));
                                }
                            }
                        }
                    }
                    p.playSound(p.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, 1.0f, 1.0f);
                    o.playSound(o.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, 1.0f, 1.0f);

                    dm.removeTradingWindow(tw);
                } else {
                    // Deal got declined, both players get their own items back
                    for(int i = 0; i < ROWS * 9; i++) {
                        if(isOwnField(i)) {
                            if(tw.playerInventory.getItem(i) != null) {
                                if(tw.player.getInventory().firstEmpty() > -1)
                                    tw.player.getInventory().addItem(tw.playerInventory.getItem(i));
                                else {
                                    tw.player.getWorld().dropItem(tw.player.getLocation(), tw.playerInventory.getItem(i));
                                }
                            }
                            if(tw.oppositeInventory.getItem(i) != null) {
                                if(tw.opposite.getInventory().firstEmpty() > -1)
                                    tw.opposite.getInventory().addItem(tw.oppositeInventory.getItem(i));
                                else {
                                    tw.opposite.getWorld().dropItem(tw.opposite.getLocation(), tw.oppositeInventory.getItem(i));
                                }
                            }
                        }
                    }

                    p.playSound(p.getLocation(), Sound.BLOCK_ANVIL_LAND, 1.0f, 1.0f);
                    o.playSound(o.getLocation(), Sound.BLOCK_ANVIL_LAND, 1.0f, 1.0f);

                    boolean eventPlayerIsOpponent = e.getPlayer().equals(tw.opposite);
                    final String YOU_DECLINED = Main.PREFIX + "You declined the deal with " + o.getName() +
                            " by closing your inventory!";
                    final String OTHER_DECLINED = Main.PREFIX + p.getName() + " declined the deal!";

                    p.sendMessage(eventPlayerIsOpponent ? OTHER_DECLINED : YOU_DECLINED);
                    o.sendMessage(eventPlayerIsOpponent ? YOU_DECLINED : OTHER_DECLINED);
                    dm.removeTradingWindow(tw);
                }
            }
        }
    }

    @EventHandler
    public void onInventoryDrag(InventoryDragEvent e) {
        DealMaker dm = Main.getPlugin().getDealMaker();
        if(dm.isInventoryInList(e.getInventory())) {
            TradingWindow tw = dm.getTradingWindow(e.getInventory());
            if(tw.playerAcceptedDeal || tw.oppositeAcceptedDeal) {
                e.setCancelled(true);
            } else {
                tw.refreshInventorySwitch();
            }
        }
    }

}

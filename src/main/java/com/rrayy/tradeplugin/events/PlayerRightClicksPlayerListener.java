package com.rrayy.tradeplugin.events;

import com.rrayy.tradeplugin.main.Main;
import com.rrayy.tradeplugin.utils.ConfigValues;
import com.rrayy.tradeplugin.utils.DealMaker;
import com.rrayy.tradeplugin.utils.MessageStrings;
import com.rrayy.tradeplugin.utils.Translations;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class PlayerRightClicksPlayerListener implements Listener {
    @EventHandler
    public void onPlayerInteracts(PlayerInteractEntityEvent e) {
        MessageStrings messageStrings = Main.getPlugin().getMessageStrings();
        ConfigValues configValues = Main.getPlugin().getConfigValues();
        Player p = e.getPlayer();
        if(e.getRightClicked() instanceof Player) {
            if(configValues.ENABLE_TRADE_BY_RIGHTCLICK_PLAYER && (configValues.toggleUseWithoutPermission()
                    || p.hasPermission("trade.tradebyclick")
                    || p.hasPermission("trade.*"))) {

                if(configValues.REQUIRE_SHIFT_CLICK && !p.isSneaking()) return;

                Player target = (Player) e.getRightClicked();
                DealMaker dm = Main.getPlugin().getDealMaker();
                if(dm.addPlayerToCooldown(p)) {
                    if(dm.madePlayerARequest(target, p)) {
                        dm.acceptTrade(p, target);
                    } else {
                        boolean success = dm.makeTradeOffer(p, target);
                        if(success)
                            p.sendMessage(Main.PREFIX + String.format(
                                    messageStrings.getTranslation(Translations.TRADE_REQUEST_SENT), target.getName()));
                    }
                }
            }
        }
    }
}

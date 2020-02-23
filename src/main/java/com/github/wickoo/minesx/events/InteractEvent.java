package com.github.wickoo.minesx.events;

import com.github.wickoo.minesx.MinesHandler;
import com.github.wickoo.minesx.MinesX;
import com.github.wickoo.minesx.data.Clipboard;
import com.github.wickoo.minesx.utils.Util;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;


public class InteractEvent implements Listener {

    private MinesX plugin;
    private MinesHandler minesHandler;

    public InteractEvent(MinesX plugin, MinesHandler minesHandler) {
        this.plugin = plugin;
        this.minesHandler = minesHandler;

    }

    @EventHandler
    public void onInteract (PlayerInteractEvent e) {

        Player player = e.getPlayer();
        Action action = e.getAction();
        ItemStack itemStack = player.getInventory().getItemInMainHand();

        if (action == Action.LEFT_CLICK_BLOCK && itemStack.getType() == Material.DIAMOND_PICKAXE && minesHandler.isWand(itemStack)) {

            e.setCancelled(true);
            Location loc1 = e.getClickedBlock().getLocation();

            Clipboard clipboard = new Clipboard(player, loc1);

            minesHandler.addToClipboardMap(player.getUniqueId(), clipboard);
            player.sendMessage(Util.col("&7Added 1st location to clipboard at (" + Util.locToString(loc1) + ")"));
            return;

        } if (action == Action.RIGHT_CLICK_BLOCK && itemStack.getType() == Material.DIAMOND_PICKAXE && minesHandler.getClipboardMap().containsKey(player.getUniqueId()) && e.getHand() == EquipmentSlot.HAND && minesHandler.isWand(itemStack)) {

            e.setCancelled(true);
            Location loc2 = e.getClickedBlock().getLocation();

            Clipboard clipboard = minesHandler.getClipboardMap().get(player.getUniqueId());
            clipboard.setLoc2(loc2);

            player.sendMessage(Util.col("&7Added 2nd location to clipboard at (" + Util.locToString(loc2) + ")"));
            return;

        }

    }

}

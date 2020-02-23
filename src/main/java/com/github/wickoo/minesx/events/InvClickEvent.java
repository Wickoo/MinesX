package com.github.wickoo.minesx.events;

import com.github.wickoo.minesx.MinesHandler;
import com.github.wickoo.minesx.MinesX;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class InvClickEvent implements Listener {

    private MinesX plugin;
    private MinesHandler minesHandler;

    public InvClickEvent(MinesX plugin, MinesHandler minesHandler) {
        this.plugin = plugin;
        this.minesHandler = minesHandler;

    }

    @EventHandler
    public void onInteract (InventoryClickEvent e) {

        Player player = (Player) e.getWhoClicked();

        if (e.getClickedInventory() == minesHandler.getGui()) {
            e.setCancelled(true);
        }

    }

}
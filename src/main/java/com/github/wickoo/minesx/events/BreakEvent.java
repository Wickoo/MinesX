package com.github.wickoo.minesx.events;

import com.github.wickoo.minesx.MinesHandler;
import com.github.wickoo.minesx.MinesX;
import com.github.wickoo.minesx.data.Mine;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class BreakEvent implements Listener {

    private MinesX plugin;
    private MinesHandler minesHandler;

    public BreakEvent(MinesX plugin, MinesHandler minesHandler) {
        this.plugin = plugin;
        this.minesHandler = minesHandler;

    }

    @EventHandler
    public void onInteract (BlockBreakEvent e) {

        Player player = e.getPlayer();
        Mine mine = minesHandler.getMine(e.getBlock().getLocation());

        if (!(mine == null)) {

            mine.setBlocksMined(mine.getBlocksMined() + 1);
            return;

        }

    }

}
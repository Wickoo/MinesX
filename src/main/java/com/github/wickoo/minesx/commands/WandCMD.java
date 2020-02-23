package com.github.wickoo.minesx.commands;

import com.github.wickoo.minesx.MinesHandler;
import org.bukkit.entity.Player;

public class WandCMD implements CommandManager {

    private MinesHandler handler;

    public WandCMD (MinesHandler handler) {
        this.handler = handler;
    }

    @Override
    public String getName() {
        return "wand";
    }

    @Override
    public String getPermission() {
        return "minesx.wand";
    }

    @Override
    public String getAdditionalArgs() {
        return "";
    }

    @Override
    public String getDescription() {
        return "Wand for creating mines";
    }

    @Override
    public void executeCommand(Player player, String[] args) {

        player.getInventory().addItem(handler.getWand());

    }

}
package com.github.wickoo.minesx.commands;

import com.github.wickoo.minesx.MinesHandler;
import org.bukkit.entity.Player;

public class ViewCMD implements CommandManager {

    private MinesHandler handler;

    public ViewCMD (MinesHandler handler) {
        this.handler = handler;
    }

    @Override
    public String getName() {
        return "view";
    }

    @Override
    public String getPermission() {
        return "minesx.view";
    }

    @Override
    public String getAdditionalArgs() {
        return "";
    }

    @Override
    public String getDescription() {
        return "View all mines";
    }

    @Override
    public void executeCommand(Player player, String[] args) {

        handler.openMineGUI(player);

    }

}
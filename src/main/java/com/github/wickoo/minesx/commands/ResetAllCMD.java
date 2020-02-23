package com.github.wickoo.minesx.commands;

import com.github.wickoo.minesx.MinesHandler;
import com.github.wickoo.minesx.data.Mine;
import com.github.wickoo.minesx.utils.Util;
import org.bukkit.entity.Player;

public class ResetAllCMD implements CommandManager {

    private MinesHandler handler;

    public ResetAllCMD (MinesHandler handler) {
        this.handler = handler;
    }

    @Override
    public String getName() {
        return "resetall";
    }

    @Override
    public String getPermission() {
        return "minesx.resetall";
    }

    @Override
    public String getAdditionalArgs() {
        return "";
    }

    @Override
    public String getDescription() {
        return "Resets all mines part of global reset interval";
    }

    @Override
    public void executeCommand(Player player, String[] args) {

        handler.globalMineReset();
        player.sendMessage(Util.col("&a&lSUCCESS! &7You have manually forced a global reset!"));

    }

}

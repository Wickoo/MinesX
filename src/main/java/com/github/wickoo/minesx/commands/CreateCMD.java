package com.github.wickoo.minesx.commands;

import com.github.wickoo.minesx.MinesHandler;
import com.github.wickoo.minesx.utils.Util;
import org.bukkit.entity.Player;

public class CreateCMD implements CommandManager {

    private MinesHandler handler;

    public CreateCMD (MinesHandler handler) {
        this.handler = handler;
    }

    @Override
    public String getName() {
        return "create";
    }

    @Override
    public String getPermission() {
        return "minesx.create";
    }

    @Override
    public String getAdditionalArgs() {
        return "(name)";
    }

    @Override
    public String getDescription() {
        return "Create a mine";
    }

    @Override
    public void executeCommand(Player player, String[] args) {

        if (!(args.length >= 2)) {
            player.sendMessage(Util.col("&c&lERROR... &7Incorrect syntax: /minesx " + getName() + " " + getAdditionalArgs() + "!"));
            return;
        }

        if (handler.getMineMap().containsKey(args[1])) {
            player.sendMessage(Util.col("&c&lERROR... &7Mine &c" + args[1] + " &7already exists!"));
            return;
        }

        if (!handler.hasCompleteClipboard(player)) {
            player.sendMessage(Util.col("&c&lERROR... &7You must select two locations!"));
            return;
        }

        handler.createMine(player, args[1]);
        player.sendMessage(Util.col("&a&lSUCCESS! &7Created new mine called &a" + args[1]));

    }

}


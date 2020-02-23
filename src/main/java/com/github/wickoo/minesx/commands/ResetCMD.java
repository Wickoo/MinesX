package com.github.wickoo.minesx.commands;

import com.github.wickoo.minesx.MinesHandler;
import com.github.wickoo.minesx.data.Mine;
import com.github.wickoo.minesx.utils.Util;
import org.bukkit.entity.Player;

public class ResetCMD implements CommandManager {

    private MinesHandler handler;

    public ResetCMD (MinesHandler handler) {
        this.handler = handler;
    }

    @Override
    public String getName() {
        return "reset";
    }

    @Override
    public String getPermission() {
        return "minesx.reset";
    }

    @Override
    public String getAdditionalArgs() {
        return "(mine)";
    }

    @Override
    public String getDescription() {
        return "Reset target mine";
    }

    @Override
    public void executeCommand(Player player, String[] args) {

        if (!handler.getMineMap().containsKey(args[1])) {
            player.sendMessage(Util.col("&c&lERROR... &7Couldn't find mine &c" + args[1]));
            return;
        }

        Mine mine = handler.getMineMap().get(args[1]);
        handler.resetMine(mine);
        player.sendMessage(Util.col("&a&lSUCCESS! &7Reset mine &a" + args[1]));

    }

}

package com.github.wickoo.minesx.runnables;

import com.github.wickoo.minesx.MinesHandler;
import com.github.wickoo.minesx.MinesX;
import com.github.wickoo.minesx.data.Mine;
import com.github.wickoo.minesx.utils.Util;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

public class MineReset extends BukkitRunnable {

    private MinesX plugin;
    private MinesHandler minesHandler;

    private Mine mine;

    private final int time;
    private int timeLeft;

    public MineReset (MinesX plugin, MinesHandler minesHandler, Mine mine, int timeLeft) {

        this.plugin = plugin;
        this.minesHandler = minesHandler;
        this.time = timeLeft;
        this.timeLeft = timeLeft;
        this.mine = mine;

    }

    @Override
    public void run() {

        if (timeLeft == 0) {

            minesHandler.resetMine(mine);
            timeLeft = time;
            mine.setTimeleft(mine.getResetTime());
            Bukkit.broadcastMessage(Util.col("&a&lMinesX &8> &7Mine &a" + mine.getName() + " &7has been successfully reset!"));
            return;

        }

        mine.setTimeleft(mine.getTimeleft() - 1);
        --timeLeft;

    }

}

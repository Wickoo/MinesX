package com.github.wickoo.minesx.runnables;

import com.github.wickoo.minesx.MinesHandler;
import com.github.wickoo.minesx.MinesX;
import org.bukkit.scheduler.BukkitRunnable;

public class GlobalReset extends BukkitRunnable {

    private MinesX plugin;
    private MinesHandler minesHandler;

    private final int time;
    private int timeLeft;

    public GlobalReset (MinesX plugin, MinesHandler minesHandler, int timeLeft) {

        this.plugin = plugin;
        this.minesHandler = minesHandler;
        this.time = timeLeft;
        this.timeLeft = timeLeft;

    }

    @Override
    public void run() {

        if (timeLeft == 0) {

            minesHandler.globalMineReset();
            minesHandler.setActualGlobalTimeLeft(minesHandler.getFinalGlobalTimeLeft());
            timeLeft = time;
            return;

        }

        minesHandler.setActualGlobalTimeLeft(minesHandler.getActualGlobalTimeLeft() - 1);
        --timeLeft;

    }

}

package com.github.wickoo.minesx;

import com.github.wickoo.minesx.commands.CoreCMD;
import com.github.wickoo.minesx.events.BreakEvent;
import com.github.wickoo.minesx.events.InteractEvent;
import com.github.wickoo.minesx.events.InvClickEvent;
import org.bukkit.plugin.java.JavaPlugin;

public final class MinesX extends JavaPlugin {

    private MinesX plugin;
    private MinesHandler minesHandler;
    private Config minesConfig;

    @Override
    public void onEnable() {

        this.plugin = this;

        minesConfig = new Config(plugin, "mines.yml");
        minesConfig.createCustomConfig();
        saveDefaultConfig();

        minesHandler = new MinesHandler(plugin);


        getCommand("minesx").setExecutor(new CoreCMD(plugin, minesHandler));
        getServer().getPluginManager().registerEvents(new InteractEvent(plugin, minesHandler), this);
        getServer().getPluginManager().registerEvents(new InvClickEvent(plugin, minesHandler), this);
        getServer().getPluginManager().registerEvents(new BreakEvent(plugin, minesHandler), this);

    }

    @Override
    public void onDisable() {

    }

    public MinesX getPlugin () { return plugin; }

    public MinesHandler getMinesHandler () { return minesHandler; }

    public Config getMinesConfig () { return minesConfig; }

}

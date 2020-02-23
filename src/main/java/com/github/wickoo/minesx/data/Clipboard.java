package com.github.wickoo.minesx.data;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.UUID;

public class Clipboard {

    private UUID uuid;
    private Location loc1;
    private Location loc2 = null;
    private World world;


    public Clipboard (Player player, Location loc1) {

        this.uuid = player.getUniqueId();
        this.loc1 = loc1;
        this.world = loc1.getWorld();

    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public Location getLoc1() {
        return loc1;
    }

    public void setLoc1(Location loc1) {
        this.loc1 = loc1;
    }

    public Location getLoc2() {
        return loc2;
    }

    public void setLoc2(Location loc2) {
        this.loc2 = loc2;
    }

    public World getWorld () { return world; }

}

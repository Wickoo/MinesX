package com.github.wickoo.minesx.data;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;

import java.util.LinkedHashMap;
import java.util.Map;

public class Mine {

    private String name;
    private Material displayItem;
    private Location loc1;
    private Location loc2;
    private World world;
    private int resetTime;
    private int timeleft;
    private Map<Double, Material> blocks;
    private Location spawnLoc;
    private int blocksMined = 0;

    public Mine (String name, Location loc1, Location loc2, int resetTime) {

        this.name = name;
        this.displayItem = Material.STONE_PICKAXE;
        this.loc1 = loc1;
        this.loc2 = loc2;
        this.world = loc1.getWorld();
        this.spawnLoc = loc1;
        this.blocks = new LinkedHashMap<>();
        this.resetTime = resetTime;
        this.timeleft = resetTime;
        blocks.put(100D, Material.STONE);

    }

    public String getName () { return name; }

    public Location getLoc1 () { return loc1; }

    public Location getLoc2 () { return loc2; }

    public World getWorld () { return world; }

    public int getResetTime() { return resetTime; }

    public void setResetTime(int resetTime) { this.resetTime = resetTime; this.timeleft = resetTime; }

    public int getTimeleft() { return timeleft; }

    public void setTimeleft(int timeleft) { this.timeleft = timeleft; }

    public Map<Double, Material> getBlocks() { return blocks; }

    public void setBlocks(Map<Double, Material> blocks) { this.blocks = blocks; }

    public Location getSpawnLoc() { return spawnLoc; }

    public void setSpawnLoc(Location spawnLoc) { this.spawnLoc = spawnLoc; }

    public Material getDisplayItem() { return displayItem; }

    public void setDisplayItem(Material displayItem) { this.displayItem = displayItem; }

    public int getBlocksMined() { return blocksMined; }

    public void setBlocksMined(int blocksMined) { this.blocksMined = blocksMined; }
}

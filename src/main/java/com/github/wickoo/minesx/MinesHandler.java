package com.github.wickoo.minesx;

import com.github.wickoo.minesx.data.Clipboard;
import com.github.wickoo.minesx.data.Mine;
import com.github.wickoo.minesx.runnables.GlobalReset;
import com.github.wickoo.minesx.runnables.MineReset;
import com.github.wickoo.minesx.utils.Util;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.*;

public class MinesHandler {

    private MinesX plugin;
    private Config minesConfig;

    private Map<String, Mine> mineMap;
    private Map<UUID, Clipboard> clipboardMap;

    private Inventory gui;

    private int mainRunnableID;
    private final int finalGlobalTimeLeft; //variable set in config
    private int actualGlobalTimeLeft; //constantly changed
    private List<Mine> globalRestMines; //mines that are reset with global interval


    public MinesHandler (MinesX plugin) {

        this.plugin = plugin;
        this.minesConfig = plugin.getMinesConfig();
        this.mineMap = new HashMap<>();
        this.clipboardMap = new HashMap<>();

        this.finalGlobalTimeLeft = plugin.getConfig().getInt("reset-interval");
        System.out.print(finalGlobalTimeLeft);

        this.gui = Bukkit.createInventory(null, 45, Util.col("&7All Mines"));
        initilizeGUI();
        initilizeMines();

        startGlobalCountdown();
        startAllMineTimers();

    }

    /**
     * reset mine (goes through each block)
     *
     * @param mine : the target mine that will be reset
     */
    public void resetMine (Mine mine) {

        Location loc1 = mine.getLoc1();
        Location loc2 = mine.getLoc2();
        List<Material> materials = new ArrayList<>(mine.getBlocks().values());

        for (Double x = Util.getLowerNum(loc1.getX(), loc2.getX()); x <= Util.getHigherNum(loc1.getX(), loc2.getX()); x++) {

            for (Double y = Util.getLowerNum(loc1.getY(), loc2.getY()); y <= Util.getHigherNum(loc1.getY(), loc2.getY()); y++) {

                for (Double z = Util.getLowerNum(loc1.getZ(), loc2.getZ()); z <= Util.getHigherNum(loc1.getZ(), loc2.getZ()); z++) {

                    Location block = new Location(mine.getWorld(), x.intValue() , y.intValue() , z.intValue());
                    block.getBlock().setType(calculateMaterial(mine.getBlocks(), materials));

                }

            }

        }

        mine.setBlocksMined(0);

    }

    /**
     * reads mines from mines.yml and translates them into mine objects and adds
     * them to map
     *
     */

    private void initilizeMines () {

        Set<String> storedMines = plugin.getMinesConfig().getConfig().getKeys(false);
        System.out.print(storedMines.toString());

        for (String string : storedMines) {

            World world = Bukkit.getWorld(minesConfig.getConfig().getString(string + ".world"));
            Location loc1 = getLocFromString(minesConfig.getConfig().getString(string + ".loc1"), world);
            Location loc2 = getLocFromString(minesConfig.getConfig().getString(string + ".loc2"), world);
            int resetTime = minesConfig.getConfig().getInt(string + ".reset-time");

            Mine mine = new Mine(string, loc1, loc2, resetTime);
            mine.setBlocks(getBlocks(minesConfig.getConfig().getStringList(string + ".blocks")));
            mine.setSpawnLoc(getLocFromString(minesConfig.getConfig().getString(string + ".spawn-loc"), world));
            mine.setDisplayItem(Material.matchMaterial(minesConfig.getConfig().getString(string + ".display-item")));

            mineMap.put(string, mine);

        }

    }

    /**
     *
     * @param blocksString : list of blocks, taken from config in format <item>:<percentage>
     * @return a map with key percentage and material value
     *
     * double values are ordered from lowest to highest, and span from 0-100 (e.g 90,92,96,99,100 from 90, 2, 4, 3, 1)
     */

    private Map<Double, Material> getBlocks (List<String> blocksString) {

        Map<Double, Material> blocks = new LinkedHashMap<>();
        double baseNumber = 0;

        for (String string : blocksString) {

            String[] list = string.split(":");
            Material material;

            try {
                material = Material.matchMaterial(list[0]);
            } catch (NullPointerException npe) {
                npe.printStackTrace();
                continue;
            }

            Double percentage = baseNumber + Double.parseDouble(list[1]);
            baseNumber = percentage;
            blocks.put(percentage, material);

        }

        return blocks;

    }

    /**
     * calculation uses random double and returns a material
     *
     * @param map : of double material, where ordering must be formatted (lowest/highest and span 0-100)
     * @param materials : list of materials
     * @return random material based on calculation
     */

    private Material calculateMaterial (Map<Double, Material> map, List<Material> materials) {

        double randomPercentage = Math.random() * 100;

        for (Material material : materials) {

            Double matPercentage = Util.getKeyByValue(map, material);

            if (randomPercentage < matPercentage) {

                return map.get(matPercentage);

            }

        }

        return materials.get(0);

    }

    /**
     * creates a mine based on name and clipboard object
     *
     * @param player : that clipboard is tied to
     * @param name : mine name
     */

    public void createMine(Player player, String name) {

        Clipboard clipboard = clipboardMap.get(player.getUniqueId());
        Mine mine = new Mine(name, clipboard.getLoc1(), clipboard.getLoc2(), 600);
        mineMap.put(name, mine);
        writeToConfig(mine);

    }

    /**
     * writes a mine to config
     *
     * @param mine : to write to config
     */

    private void writeToConfig (Mine mine) {

        String name = mine.getName();
        Location loc1 = mine.getLoc1();
        Location loc2 = mine.getLoc2();

        List<String> configSection = new ArrayList<>();
        List<Material> materials = new ArrayList<>(mine.getBlocks().values());

        for (Material material : materials) {

            String string = material.toString() + ":" + Util.getKeyByValue(mine.getBlocks(), material);
            configSection.add(string);

        }

        minesConfig.getConfig().set(name + ".display-item", mine.getDisplayItem().toString());
        minesConfig.getConfig().set(name + ".world", mine.getWorld().getName());
        minesConfig.getConfig().set(name + ".loc1", loc1.getBlockX() + "," + loc1.getBlockY() + "," + loc1.getBlockZ());
        minesConfig.getConfig().set(name + ".loc2", loc2.getBlockX() + "," + loc2.getBlockY() + "," + loc2.getBlockZ());
        minesConfig.getConfig().set(name + ".spawn-loc", loc1.getBlockX() + "," + loc1.getBlockY() + "," + loc1.getBlockZ());
        minesConfig.getConfig().set(name + ".reset-time", mine.getResetTime());
        minesConfig.getConfig().set(name + ".blocks", configSection);

        minesConfig.saveConfig();

    }

    /**
     * creates GUI for viewing mines, sets black stained glass for border
     *
     */

    private void initilizeGUI () {

        for (int i = 0; i < 9; i++) { gui.setItem(i, new ItemStack(Material.BLACK_STAINED_GLASS_PANE)); } //1-9
        for (int i = 36; i < 45; i++) { gui.setItem(i, new ItemStack(Material.BLACK_STAINED_GLASS_PANE)); } //36-45
        gui.setItem(9, new ItemStack(Material.BLACK_STAINED_GLASS_PANE));
        gui.setItem(18, new ItemStack(Material.BLACK_STAINED_GLASS_PANE));
        gui.setItem(27, new ItemStack(Material.BLACK_STAINED_GLASS_PANE));
        gui.setItem(17, new ItemStack(Material.BLACK_STAINED_GLASS_PANE));
        gui.setItem(26, new ItemStack(Material.BLACK_STAINED_GLASS_PANE));
        gui.setItem(35, new ItemStack(Material.BLACK_STAINED_GLASS_PANE));

    }

    /**
     * opens GUI for player
     *
     * @param player
     */

    public void openMineGUI (Player player) {

        gui.clear();
        initilizeGUI();
        ItemStack item;

        for (Map.Entry<String, Mine> string : mineMap.entrySet()) {

            String name = string.getKey();
            Mine mine = mineMap.get(name);
            item = new ItemStack(mine.getDisplayItem());
            ItemMeta itemMeta = item.getItemMeta();
            itemMeta.setDisplayName(Util.col("&a&l" + mine.getName()));

            Location loc1 = mine.getLoc1();
            Location loc2 = mine.getLoc2();
            Location spawn = mine.getSpawnLoc();

            List<String> lore = new ArrayList<>();
            lore.add(0, Util.col("&7Location 1: &c(" + loc1.getBlockX() + ", " + loc1.getBlockY() + ", " + loc1.getBlockZ() + ")"));
            lore.add(1, Util.col("&7Location 2: &c(" + loc2.getBlockX() + ", " + loc2.getBlockY() + ", " + loc2.getBlockZ() + ")"));
            lore.add(2, Util.col("&7Spawn Location: &6(" + spawn.getBlockX() + ", " + spawn.getBlockY() + ", " + spawn.getBlockZ() + ")"));
            lore.add(3, Util.col(" "));
            lore.add(4, Util.col("&7Blocks Broken: &9" + mine.getBlocksMined()));
            lore.add(5, Util.col("&7Total Blocks: &d" + getBlockTotal(mine)));
            lore.add(6, Util.col(" "));
            lore.add(7, Util.col("&7Blocks: "));

            int j = 8;

            for (int i = 0; i < mine.getBlocks().size(); i++) {

                List<Material> materials = new ArrayList<>(mine.getBlocks().values());
                Material material = materials.get(i);
                String matString = "&f- &b" + getProperString(material) + " &8[&b" + getProperPercentagesMap(mine).get(material) + "%&8]";
                lore.add(i + 8, Util.col(matString));
                j++;

            }

            lore.add(j, Util.col(" "));
            lore.add(j + 1, Util.col("&fThere are &e" + getActualTimeLeft(mine) + " &fseconds until next reset!"));

            itemMeta.setLore(lore);
            item.setItemMeta(itemMeta);
            gui.addItem(item);
            player.openInventory(gui);

        }

    }

    /** uses persistant data container (namespace: "wand" , value [string]: "wand")
     *
     * @return wand for selecting locations for mine creation
     */

    public ItemStack getWand () {

        ItemStack wand = new ItemStack(Material.DIAMOND_PICKAXE);
        ItemMeta wandMeta = wand.getItemMeta();
        wandMeta.getPersistentDataContainer().set(new NamespacedKey(plugin, "wand"), PersistentDataType.STRING, "wand");
        wand.setItemMeta(wandMeta);
        return wand;

    }

    /** returns map with proper percentages, as mineMap returns formatted percentages for
     * material calculation
     *
     * @param mine target mine
     * @return proper percentages
     */

    private Map<Material, Double> getProperPercentagesMap (Mine mine) {

        List<String> stringMats = minesConfig.getConfig().getStringList(mine.getName() + ".blocks");
        Map<Material, Double> blocks = new LinkedHashMap<>();

        for (String string : stringMats) {

            String[] list = string.split(":");
            Material material;

            try {
                material = Material.matchMaterial(list[0]);
            } catch (NullPointerException npe) {
                npe.printStackTrace();
                continue;
            }

            Double percentage = Double.parseDouble(list[1]);
            blocks.put(material, percentage);

        }

        return blocks;

    }

    /**
     * gets total blocks for specified mine, simple calc
     *
     * @param mine target mine
     * @return blocks total
     */

    public int getBlockTotal (Mine mine) {

        Location loc1 = mine.getLoc1(); //(10,10,10)
        Location loc2 = mine.getLoc2(); //(50,50,50)

        int xDiff = (int) (Util.getHigherNum(loc1.getBlockX(), loc2.getBlockX()) - Util.getLowerNum(loc1.getBlockX(), loc2.getBlockX()));
        int yDiff = (int) (Util.getHigherNum(loc1.getBlockY(), loc2.getBlockY()) - Util.getLowerNum(loc1.getBlockY(), loc2.getBlockY()));
        int zDiff = (int) (Util.getHigherNum(loc1.getBlockZ(), loc2.getBlockZ()) - Util.getLowerNum(loc1.getBlockZ(), loc2.getBlockZ()));

        return xDiff * yDiff * zDiff;

    }

    /** gets mine from location, useful for determining blocks broken in mine
     *
     * @param loc location
     * @return mine
     */

    public Mine getMine(Location loc) {

        double xBroke = loc.getBlockX();
        double yBroke = loc.getBlockY();
        double zBroke = loc.getBlockZ();

        List<Mine> mineList = new ArrayList<>(mineMap.values());

        for (Mine mine : mineList) {

            Location loc1 = mine.getLoc1();
            Location loc2 = mine.getLoc2();

            if ((containsValue((int) xBroke, loc1.getBlockX(), loc2.getBlockX())) && (containsValue((int) yBroke, loc1.getBlockY(), loc2.getBlockY())) && (containsValue((int) zBroke, loc1.getBlockZ(), loc2.getBlockZ()))) {

                return mine;

            }

        }

        return  null;

    }

    /**
     * starts countdown for global interval, called during startup
     *
     */

    public void startGlobalCountdown () {

        globalRestMines = new ArrayList<>();
        List<String> mineNames = plugin.getConfig().getStringList("mines");

        for (String s : mineNames) {

            if (!mineMap.containsKey(s)) {
                continue;
            }

            Mine mine = mineMap.get(s);
            globalRestMines.add(mine);

        }

        actualGlobalTimeLeft = finalGlobalTimeLeft;
        GlobalReset timer = new GlobalReset(plugin, this, finalGlobalTimeLeft);
        timer.runTaskTimer(plugin, 0, 20);
        mainRunnableID = timer.getTaskId();

    }

    /**
     * starts countdowns for all individual mines that arent apart of global interval, called during startup
     *
     */

    public void startAllMineTimers () {

        List<Mine> allMines = new ArrayList<>(mineMap.values());

        for (Mine mine : allMines) {

            if (globalRestMines.contains(mine)) {
                continue;
            }

            MineReset timer = new MineReset(plugin, this, mine, mine.getResetTime());
            timer.runTaskTimer(plugin, 0, 20);

        }

    }

    /**
     * resets all mines part of global interval
     *
     */

    public void globalMineReset () {

        for (Mine mine : globalRestMines) {

            this.resetMine(mine);

        }

        Bukkit.broadcastMessage(Util.col("&a&lMinesX &8> &7Mines have successfully been reset!"));

    }

    /**
     * gets time left until reset, useful as some mines may have own reset schedule, should be used instead of mine.getTimeLeft
     *
     * @param mine : target mine
     * @return timeleft until reset
     */

    public int getActualTimeLeft (Mine mine) {

        if (globalRestMines.contains(mine)) {
            return actualGlobalTimeLeft;
        }

        return mine.getTimeleft();

    }

    public boolean containsValue (int value, int bound1, int bound2) { return ((Util.getLowerNum(bound1, bound2) <= value) && (value <= Util.getHigherNum(bound1, bound2))); }

    public boolean isWand (ItemStack itemStack) { return itemStack.getItemMeta().getPersistentDataContainer().has(new NamespacedKey(plugin, "wand"), PersistentDataType.STRING); }

    private String getProperString (Material material) { return Util.capitalizeWord(material.toString().replace('_', ' ').toLowerCase()); }

    public boolean hasCompleteClipboard (Player player) { return this.getClipboardMap().containsKey(player.getUniqueId()) && !(getClipboardMap().get(player.getUniqueId()).getLoc2() == null); }

    private Location getLocFromString (String string, World world) { String[] list = string.split(","); return new Location(world, Double.parseDouble(list[0]), Double.parseDouble(list[1]), Double.parseDouble(list[2])); }

    public Map<String, Mine> getMineMap() { return mineMap; }

    public void setMineMap(Map<String, Mine> mineMap) { this.mineMap = mineMap; }

    public Map<UUID, Clipboard> getClipboardMap() { return clipboardMap; }

    public void setClipboardMap(Map<UUID, Clipboard> clipboardMap) { this.clipboardMap = clipboardMap; }

    public void addToClipboardMap (UUID uuid, Clipboard clipboard) { this.clipboardMap.put(uuid, clipboard); }

    public void removeFromClipboardMap (UUID uuid) { this.clipboardMap.remove(uuid); }

    public int getActualGlobalTimeLeft() { return actualGlobalTimeLeft; }

    public int getFinalGlobalTimeLeft () { return finalGlobalTimeLeft; }

    public void setActualGlobalTimeLeft(int globalTimeLeft) { this.actualGlobalTimeLeft = globalTimeLeft; }

    public Inventory getGui () { return gui; }

}

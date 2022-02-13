package tech.defiantburger.battlebox;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

import java.util.ArrayList;

public class BattleBoxGoal {

    private Location loc;
    private ArrayList<Location> blocks;

    public BattleBoxGoal(Location loc) {
        this.loc = loc;
        updateBlocks();
    }

    public void fill(Material material) {
        blocks.forEach(b -> b.getBlock().setType(material));
    }

    public void setLoc(Location loc) {
        this.loc = loc;
        updateBlocks();
    }

    public void updateBlocks() {
        this.blocks = new ArrayList<>();
        Block b = loc.getBlock();
        blocks.add(b.getRelative(-1, 0, -1).getLocation());
        blocks.add(b.getRelative(-1, 0, 0).getLocation());
        blocks.add(b.getRelative(-1, 0, 1).getLocation());
        blocks.add(b.getRelative(0, 0, -1).getLocation());
        blocks.add(b.getRelative(0, 0, 0).getLocation());
        blocks.add(b.getRelative(0, 0, 1).getLocation());
        blocks.add(b.getRelative(1, 0, -1).getLocation());
        blocks.add(b.getRelative(1, 0, 0).getLocation());
        blocks.add(b.getRelative(1, 0, 1).getLocation());
    }

    public Location getLoc() {
        return loc;
    }

    public ArrayList<Location> getBlocks() {
        return blocks;
    }

    public String toString() {
        return String.format("(x: %s, y: %s, z: %s)", loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
    }
}

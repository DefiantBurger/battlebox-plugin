package tech.defiantburger.battlebox;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.ArrayList;
import java.util.Objects;

public class BattleBoxTeam {

    private String id;
    private String name;
    private String color;
    private Location spawn;
    private Location wallCorner1;
    private Location wallCorner2;
    private Team team;

    public BattleBoxTeam(String id, String name, String color, Location spawn, Scoreboard scoreboard) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.spawn = spawn;
        this.team = scoreboard.registerNewTeam(id);
        wallCorner1 = spawn;
        wallCorner2 = spawn;
    }

    public void start(ArrayList<BattleBoxItem> items) {
        items.add(new BattleBoxItem("{team_color}_wool", 64));

        Material mat = Material.matchMaterial(String.format("%s_stained_glass", color));
        if (Objects.isNull(mat)) return;
        ItemStack helmet = new ItemStack(mat, 1);
        helmet.addUnsafeEnchantment(Enchantment.BINDING_CURSE, 1);
        ItemMeta helmetMeta = helmet.getItemMeta();
        helmetMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        helmet.setItemMeta(helmetMeta);

        ArrayList<ItemStack> genItems = new ArrayList<>();
        for (BattleBoxItem i : items) {
            ItemStack it = i.getTeamItem(color);
            if (Objects.nonNull(it)) genItems.add(it);
        }
        for (String s : team.getEntries()) {
            Player player = Bukkit.getPlayer(s);
            if (Objects.isNull(player)) continue;
            player.teleport(spawn);
            player.setAllowFlight(false);
            player.setInvulnerable(false);
            player.setInvisible(false);
            player.setCanPickupItems(true);
            player.setGameMode(GameMode.SURVIVAL);
            PlayerInventory inv = player.getInventory();
            genItems.forEach(inv::addItem);
            inv.setHelmet(helmet);

        }
        setWall(Material.matchMaterial(String.format("%s_stained_glass", color)));
    }

    public void setWallCorner1(Location loc) {
        wallCorner1 = loc;
    }

    public void setWallCorner2(Location loc) {
        wallCorner2 = loc;
    }

    public void resetWall() {

    }

    public void setWall(Material mat) {
        if (Objects.isNull(mat)) return;

        int bigX = Math.max(wallCorner1.getBlockX(), wallCorner2.getBlockX());
        int bigY = Math.max(wallCorner1.getBlockY(), wallCorner2.getBlockY());
        int bigZ = Math.max(wallCorner1.getBlockZ(), wallCorner2.getBlockZ());
        int smlX = Math.min(wallCorner1.getBlockX(), wallCorner2.getBlockX());
        int smlY = Math.min(wallCorner1.getBlockY(), wallCorner2.getBlockY());
        int smlZ = Math.min(wallCorner1.getBlockZ(), wallCorner2.getBlockZ());

        for (int x = smlX; x <= bigX; x++) {
            for (int y = smlY; y <= bigY; y++) {
                for (int z = smlZ; z <= bigZ; z++) {
                    Block block = wallCorner1.getWorld().getBlockAt(x, y, z);
                    block.setType(mat);
                }
            }
        }
    }

    public void join(Player player) {
        team.addEntry(player.getName());
    }

    public void leave(Player player) {
        if (team.hasEntry(player.getName())) team.removeEntry(player.getName());
    }

    public String getColor() {
        return color;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Location getSpawn() {
        return spawn;
    }

    public void setSpawn(Location spawn) {
        this.spawn = spawn;
    }

    public Team getTeam() {
        return team;
    }

    public String spawnToString() {
        return String.format("(x: %s, y: %s, z: %s)", spawn.getBlockX(), spawn.getBlockY(), spawn.getBlockZ());
    }

    public String wallToString() {
        return String.format("(x: %s, y: %s, z: %s) and (x: %s, y: %s, z: %s)", wallCorner1.getBlockX(), wallCorner1.getBlockY(), wallCorner1.getBlockZ(), wallCorner2.getBlockX(), wallCorner2.getBlockY(), wallCorner2.getBlockZ());
    }
}

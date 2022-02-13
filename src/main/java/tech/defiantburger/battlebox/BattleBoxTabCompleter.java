package tech.defiantburger.battlebox;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.stream.Collectors;

public class BattleBoxTabCompleter implements TabCompleter {

    private final Set<Material> trans = new HashSet<>(Arrays.asList(Material.AIR, Material.CAVE_AIR, Material.VOID_AIR, Material.WATER));

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) return Collections.emptyList();
        Player player = (Player) sender;

        if (player.hasPermission("battlebox.op")) {
            switch (args.length) {
                case 1:
                    return Arrays.asList("help", "start", "join", "spawn", "goal", "wall");
                case 2:
                    switch (args[0]) {
                        case "join":
                        case "wall":
                        case "spawn":
                            return Arrays.asList("team1", "team2");
                        case "goal":
                            return Arrays.asList("get", "set");
                        default:
                            return Collections.emptyList();
                    }
                case 3:
                    switch (args[0]) {
                        case "join":
                            return Bukkit.getServer().getOnlinePlayers().stream().map(HumanEntity::getName).collect(Collectors.toList());
                        case "wall":
                        case "spawn":
                            return Arrays.asList("get", "set");
                        case "goal":
                            if (args[1].equals("set")) {
                                Set<Material> trans = new HashSet<>(Arrays.asList(Material.AIR, Material.CAVE_AIR, Material.VOID_AIR, Material.WATER));
                                Block b = player.getTargetBlock(trans, 32);
                                if (!trans.contains(b.getType()))
                                    return Collections.singletonList("" + b.getLocation().getBlockX());
                            }
                            return Collections.emptyList();

                        default:
                            return Collections.emptyList();
                    }
                case 4:
                    switch (args[0]) {
                        case "wall":
                        case "spawn":
                            if (args[2].equals("set")) {
                                Block b = player.getTargetBlock(trans, 32);
                                if (!trans.contains(b.getType()))
                                    return Collections.singletonList("" + b.getLocation().getBlockX());
                            }
                            return Collections.emptyList();
                        case "goal":
                            if (args[1].equals("set")) {
                                Block b = player.getTargetBlock(trans, 32);
                                if (!trans.contains(b.getType()))
                                    return Collections.singletonList("" + b.getLocation().getBlockY());
                            }
                            return Collections.emptyList();
                        default:
                            return Collections.emptyList();
                    }
                case 5:
                    switch (args[0]) {
                        case "wall":
                        case "spawn":
                            if (args[2].equals("set")) {
                                Block b = player.getTargetBlock(trans, 32);
                                if (!trans.contains(b.getType()))
                                    return Collections.singletonList("" + b.getLocation().getBlockY());
                            }
                            return Collections.emptyList();
                        case "goal":
                            if (args[1].equals("set")) {
                                Block b = player.getTargetBlock(trans, 32);
                                if (!trans.contains(b.getType()))
                                    return Collections.singletonList("" + b.getLocation().getBlockZ());
                            }
                            return Collections.emptyList();
                        default:
                            return Collections.emptyList();
                    }
                case 6:
                    switch (args[0]) {
                        case "wall":
                        case "spawn":
                            if (args[2].equals("set")) {
                                Block b = player.getTargetBlock(trans, 32);
                                if (!trans.contains(b.getType()))
                                    return Collections.singletonList("" + b.getLocation().getBlockZ());
                            }
                            return Collections.emptyList();
                        default:
                            return Collections.emptyList();
                    }
                case 7:
                    if (args[0].equals("wall")) {
                        if (args[2].equals("set")) {
                            Block b = player.getTargetBlock(trans, 32);
                            if (!trans.contains(b.getType()))
                                return Collections.singletonList("" + b.getLocation().getBlockX());
                        }
                        return Collections.emptyList();
                    }
                    return Collections.emptyList();
                case 8:
                    if (args[0].equals("wall")) {
                        if (args[2].equals("set")) {
                            Block b = player.getTargetBlock(trans, 32);
                            if (!trans.contains(b.getType()))
                                return Collections.singletonList("" + b.getLocation().getBlockY());
                        }
                        return Collections.emptyList();
                    }
                    return Collections.emptyList();
                case 9:
                    if (args[0].equals("wall")) {
                        if (args[2].equals("set")) {
                            Block b = player.getTargetBlock(trans, 32);
                            if (!trans.contains(b.getType()))
                                return Collections.singletonList("" + b.getLocation().getBlockZ());
                        }
                        return Collections.emptyList();
                    }
                    return Collections.emptyList();
                default:
                    return Collections.emptyList();
            }
        } else {
            return Collections.emptyList();
        }
    }
}

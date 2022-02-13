package tech.defiantburger.battlebox;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Objects;

public class BattleBoxCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            BattleBox.sendConsoleMessage("Only players can use this command!", ChatColor.RED);
            return true;
        }
        Player player = (Player) sender;

        if (args.length == 0) {
            BattleBox.subCommandErrorMessage("battlebox", player);
            return true;
        }

        if (player.hasPermission("battlebox.op")) {
            switch (args[0]) {
                case "start":
                    BattleBox.startGame();
                    return true;
                case "help":
                    BattleBox.sendPlayerMessage("--- BattleBox Commands ---", ChatColor.GREEN, player);
                    BattleBox.sendPlayerMessage("> /battlebox help", ChatColor.GRAY, player);
                    BattleBox.sendPlayerMessage("> /battlebox start", ChatColor.GRAY, player);
                    BattleBox.sendPlayerMessage("> /battlebox join <team1/team2> <player_name>", ChatColor.GRAY, player);
                    BattleBox.sendPlayerMessage("> /battlebox spawn <team1/team2> get", ChatColor.GRAY, player);
                    BattleBox.sendPlayerMessage("> /battlebox spawn <team1/team2> set <x> <y> <z>", ChatColor.GRAY, player);
                    BattleBox.sendPlayerMessage("> /battlebox goal get", ChatColor.GRAY, player);
                    BattleBox.sendPlayerMessage("> /battlebox goal set <x> <y> <z>", ChatColor.GRAY, player);
                    BattleBox.sendPlayerMessage("> /battlebox wall <team1/team2> get", ChatColor.GRAY, player);
                    BattleBox.sendPlayerMessage("> /battlebox wall <team1/team2> set <x1> <y1> <z1> <x2> <y2> <z2>", ChatColor.GRAY, player);
                    BattleBox.sendPlayerMessage("--------------------------", ChatColor.GREEN, player);

                    return true;
                case "reset":
                    if (args.length < 5) {
                        BattleBox.subCommandErrorMessage("battlebox", player);
                        return true;
                    }

                    try {
                        BattleBox.resetLoc = new Location(player.getWorld(), Integer.parseInt(args[3]), Integer.parseInt(args[4]), Integer.parseInt(args[5]));
                    } catch (NumberFormatException e) {
                        BattleBox.subCommandErrorMessage("battlebox", player);
                        return true;
                    }

                    return true;
                case "join":
                    if (args.length < 2) {
                        BattleBox.subCommandErrorMessage("battlebox", player);
                        return true;
                    }

                    Player joiner;
                    if (args.length < 3) {
                        joiner = player;
                    } else {
                        joiner = Bukkit.getPlayer(args[2]);
                        if (Objects.isNull(joiner)) {
                            BattleBox.sendPlayerMessage("Please enter a valid player, or do /battlebox help", ChatColor.RED, player);
                            return true;
                        }
                    }

                    boolean team1_full = false;
                    boolean team2_full = false;
                    if (args[1].equals(BattleBox.getTeam1().getId())) {
                        if (BattleBox.getTeam1().getTeam().getEntries().size() < 4) {
                            BattleBox.getTeam1().join(joiner);
                            BattleBox.getTeam2().getTeam().getEntries().remove(player.getName());
                            BattleBox.sendPlayerMessage(String.format("Successfully added %s to %s", joiner.getName(), args[1]), ChatColor.GREEN, player);
                        }
                    } else if (args[1].equals(BattleBox.getTeam2().getId())) {
                        if (BattleBox.getTeam2().getTeam().getEntries().size() < 4) {
                            BattleBox.getTeam2().join(joiner);
                            BattleBox.getTeam1().getTeam().getEntries().remove(player.getName());
                            BattleBox.sendPlayerMessage(String.format("Successfully added %s to %s", joiner.getName(), args[1]), ChatColor.GREEN, player);
                        }
                    }

                    if (BattleBox.getTeam1().getTeam().getEntries().size() >= 4) team1_full = true;

                    if (BattleBox.getTeam2().getTeam().getEntries().size() >= 4) team2_full = true;

                    if (team1_full && team2_full) {
                        BattleBox.startGame();
                    }

                    return true;
                case "spawn":
                    if (args.length == 1) {
                        BattleBox.subCommandErrorMessage("battlebox", player);
                        return true;
                    }

                    if (args[1].equals(BattleBox.getTeam1().getId())) {
                        String team = BattleBox.getTeam1().getId();
                        if (args.length == 2) {
                            BattleBox.subCommandErrorMessage("battlebox", player);
                            return true;
                        }
                        switch (args[2]) {
                            case "get":
                                BattleBox.sendPlayerMessage("The spawn of " + team + " is at " + BattleBox.getTeam1().spawnToString(), ChatColor.LIGHT_PURPLE, player);
                                return true;
                            case "set":
                                if (args.length < 5) {
                                    BattleBox.subCommandErrorMessage("battlebox", player);
                                    return true;
                                }
                                try {
                                    BattleBox.getTeam1().setSpawn(new Location(player.getWorld(), Integer.parseInt(args[3]), Integer.parseInt(args[4]), Integer.parseInt(args[5])));
                                } catch (NumberFormatException e) {
                                    BattleBox.subCommandErrorMessage("battlebox", player);
                                    return true;
                                }

                                BattleBox.sendPlayerMessage("Set the spawn of " + team + " to " + BattleBox.getTeam1().spawnToString(), ChatColor.LIGHT_PURPLE, player);
                                return true;
                            default:
                                BattleBox.subCommandErrorMessage("battlebox", player);
                                return true;
                        }
                    } else if (args[1].equals(BattleBox.getTeam2().getId())) {
                        String team = BattleBox.getTeam2().getId();
                        if (args.length == 2) {
                            BattleBox.subCommandErrorMessage("battlebox", player);
                            return true;
                        }
                        switch (args[2]) {
                            case "get":
                                BattleBox.sendPlayerMessage("The spawn of " + team + " is at " + BattleBox.getTeam1().spawnToString(), ChatColor.LIGHT_PURPLE, player);
                                return true;
                            case "set":
                                if (args.length < 5) {
                                    BattleBox.subCommandErrorMessage("battlebox", player);
                                    return true;
                                }
                                try {
                                    BattleBox.getTeam2().setSpawn(new Location(player.getWorld(), Integer.parseInt(args[3]), Integer.parseInt(args[4]), Integer.parseInt(args[5])));
                                } catch (NumberFormatException e) {
                                    BattleBox.subCommandErrorMessage("battlebox", player);
                                    return true;
                                }

                                BattleBox.sendPlayerMessage("Set the spawn of " + team + " to " + BattleBox.getTeam2().spawnToString(), ChatColor.LIGHT_PURPLE, player);
                                return true;
                            default:
                                BattleBox.subCommandErrorMessage("battlebox", player);
                                return true;
                        }
                    } else {
                        BattleBox.subCommandErrorMessage("battlebox", player);
                        return true;
                    }
                case "goal":
                    if (args.length == 1) {
                        BattleBox.subCommandErrorMessage("battlebox", player);
                        return true;
                    }
                    switch (args[1]) {
                        case "get":
                            BattleBox.sendPlayerMessage("The goal is at " + BattleBox.getGoal(), ChatColor.LIGHT_PURPLE, player);
                            return true;
                        case "set":
                            if (args.length < 5) {
                                BattleBox.subCommandErrorMessage("battlebox", player);
                                return true;
                            }
                            try {
                                BattleBox.getGoal().setLoc(new Location(player.getWorld(), Integer.parseInt(args[2]), Integer.parseInt(args[3]), Integer.parseInt(args[4])));
                            } catch (NumberFormatException e) {
                                BattleBox.subCommandErrorMessage("battlebox", player);
                                return true;
                            }

                            BattleBox.sendPlayerMessage("Set the goal to " + BattleBox.getGoal(), ChatColor.LIGHT_PURPLE, player);
                            return true;
                        default:
                            BattleBox.subCommandErrorMessage("battlebox", player);
                            return true;
                    }
                case "wall":
                    if (args.length == 1) {
                        BattleBox.subCommandErrorMessage("battlebox", player);
                        return true;
                    }

                    if (args[1].equals(BattleBox.getTeam1().getId())) {
                        String team = BattleBox.getTeam1().getId();
                        if (args.length == 2) {
                            BattleBox.subCommandErrorMessage("battlebox", player);
                            return true;
                        }
                        switch (args[2]) {
                            case "get":
                                BattleBox.sendPlayerMessage("The wall of " + team + " has corners at: " + BattleBox.getTeam1().wallToString(), ChatColor.LIGHT_PURPLE, player);
                                return true;
                            case "set":
                                if (args.length < 9) {
                                    BattleBox.subCommandErrorMessage("battlebox", player);
                                    return true;
                                }
                                try {
                                    BattleBox.getTeam1().setWallCorner1(new Location(player.getWorld(), Integer.parseInt(args[3]), Integer.parseInt(args[4]), Integer.parseInt(args[5])));
                                    BattleBox.getTeam1().setWallCorner2(new Location(player.getWorld(), Integer.parseInt(args[6]), Integer.parseInt(args[7]), Integer.parseInt(args[8])));
                                } catch (NumberFormatException e) {
                                    BattleBox.subCommandErrorMessage("battlebox", player);
                                    return true;
                                }

                                BattleBox.sendPlayerMessage("Set the wall of " + team + " to corners at " + BattleBox.getTeam1().wallToString(), ChatColor.LIGHT_PURPLE, player);
                                return true;
                            default:
                                BattleBox.subCommandErrorMessage("battlebox", player);
                                return true;
                        }
                    } else if (args[1].equals(BattleBox.getTeam2().getId())) {
                        String team = BattleBox.getTeam2().getId();
                        if (args.length == 2) {
                            BattleBox.subCommandErrorMessage("battlebox", player);
                            return true;
                        }
                        switch (args[2]) {
                            case "get":
                                BattleBox.sendPlayerMessage("The wall of " + team + " has corners at: " + BattleBox.getTeam2().wallToString(), ChatColor.LIGHT_PURPLE, player);
                                return true;
                            case "set":
                                if (args.length < 9) {
                                    BattleBox.subCommandErrorMessage("battlebox", player);
                                    return true;
                                }
                                try {
                                    BattleBox.getTeam2().setWallCorner1(new Location(player.getWorld(), Integer.parseInt(args[3]), Integer.parseInt(args[4]), Integer.parseInt(args[5])));
                                    BattleBox.getTeam2().setWallCorner2(new Location(player.getWorld(), Integer.parseInt(args[6]), Integer.parseInt(args[7]), Integer.parseInt(args[8])));
                                } catch (NumberFormatException e) {
                                    BattleBox.subCommandErrorMessage("battlebox", player);
                                    return true;
                                }

                                BattleBox.sendPlayerMessage("Set the wall of " + team + " to corners at " + BattleBox.getTeam2().wallToString(), ChatColor.LIGHT_PURPLE, player);
                                return true;
                            default:
                                BattleBox.subCommandErrorMessage("battlebox", player);
                                return true;
                        }
                    } else {
                        BattleBox.subCommandErrorMessage("battlebox", player);
                        return true;
                    }
                default:
                    BattleBox.subCommandErrorMessage("battlebox", player);
                    return true;
            }
        } else if (player.hasPermission("battlebox.user")) {
            switch (args[0]) {
                case "start":
                    BattleBox.startGame();
                    return true;
                case "help":
                    BattleBox.sendPlayerMessage("--- BattleBox Commands ---", ChatColor.GREEN, player);
                    BattleBox.sendPlayerMessage("> /battlebox help", ChatColor.GRAY, player);
                    BattleBox.sendPlayerMessage("> /battlebox join <team1/team2> <player_name>", ChatColor.GRAY, player);
                    BattleBox.sendPlayerMessage("--------------------------", ChatColor.GREEN, player);

                    return true;
                case "join":
                    if (args.length < 2) {
                        BattleBox.subCommandErrorMessage("battlebox", player);
                        return true;
                    }

                    boolean team1_full = false;
                    boolean team2_full = false;
                    if (args[1].equals(BattleBox.getTeam1().getId())) {
                        if (BattleBox.getTeam1().getTeam().getEntries().size() < 4) {
                            BattleBox.getTeam1().join(player);
                            BattleBox.getTeam2().getTeam().getEntries().remove(player.getName());
                            BattleBox.sendPlayerMessage(String.format("Successfully added %s to %s", player.getName(), args[1]), ChatColor.GREEN, player);
                        }
                    } else if (args[1].equals(BattleBox.getTeam2().getId())) {
                        if (BattleBox.getTeam2().getTeam().getEntries().size() < 4) {
                            BattleBox.getTeam2().join(player);
                            BattleBox.getTeam1().getTeam().getEntries().remove(player.getName());
                            BattleBox.sendPlayerMessage(String.format("Successfully added %s to %s", player.getName(), args[1]), ChatColor.GREEN, player);
                        }
                    }

                    if (BattleBox.getTeam1().getTeam().getEntries().size() >= 4) team1_full = true;

                    if (BattleBox.getTeam2().getTeam().getEntries().size() >= 4) team2_full = true;

                    if (team1_full && team2_full) {
                        BattleBox.startGame();
                    }

                    return true;
                default:
                    BattleBox.subCommandErrorMessage("battlebox", player);
                    return true;
            }
        } else {
            return true;
        }
    }
}

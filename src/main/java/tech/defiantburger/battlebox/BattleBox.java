package tech.defiantburger.battlebox;

import org.bukkit.*;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public final class BattleBox extends JavaPlugin {

    public static boolean initted;
    public static Location resetLoc;
    private static JavaPlugin plugin;
    private static ScoreboardManager manager;
    private static Scoreboard teamBoard;
    private static BattleBoxTeam team1;
    private static BattleBoxTeam team2;
    private static BattleBoxGoal goal;
    private static ArrayList<BattleBoxItem> items;

    public static void init(World world) {
        manager = Bukkit.getScoreboardManager();
        teamBoard = manager.getNewScoreboard();
        team1 = new BattleBoxTeam("team1", "Team 1", "red", new Location(world, 0, 0, 0), teamBoard);
        team2 = new BattleBoxTeam("team2", "Team 2", "blue", new Location(world, 0, 0, 0), teamBoard);
        goal = new BattleBoxGoal(new Location(world, 0, 0, 0));
        resetLoc = world.getSpawnLocation();
    }

    public static JavaPlugin getPlugin() {
        return plugin;
    }

    public static BattleBoxTeam getTeam1() {
        return team1;
    }

    public static BattleBoxTeam getTeam2() {
        return team2;
    }

    public static BattleBoxGoal getGoal() {
        return goal;
    }

    public static void sendConsoleMessage(String msg, ChatColor clr) {
        plugin.getServer().getConsoleSender().sendMessage(clr + msg);
    }

    public static void sendServerMessage(String msg, ChatColor clr) {
        plugin.getServer().broadcastMessage(clr + msg);
    }

    public static void sendPlayerMessage(String msg, ChatColor clr, Player player) {
        player.sendMessage(clr + msg);
    }

    public static void subCommandErrorMessage(String cmd, Player player) {
        BattleBox.sendPlayerMessage("Please use correct subcommands or do /" + cmd + " help", ChatColor.RED, player);
    }

    public static void sendStartingTitle(ChatColor color, int time) {
        for (BattleBoxTeam team : new BattleBoxTeam[]{team1, team2}) {
            for (String s : team.getTeam().getEntries()) {
                Player player = Bukkit.getPlayer(s);
                if (Objects.isNull(player)) continue;
                player.sendTitle(ChatColor.BLUE + "Match starting in " + (color + ("" + time)) + "...", "", 5, 25, 10);
            }
        }
    }

    public static void sendStartTitle(ChatColor color, String text) {
        for (BattleBoxTeam team : new BattleBoxTeam[]{team1, team2}) {
            for (String s : team.getTeam().getEntries()) {
                Player player = Bukkit.getPlayer(s);
                if (Objects.isNull(player)) continue;
                player.sendTitle(color + text, "", 5, 25, 10);
            }
        }
    }

    public static void startGame() {
        team1.start(items);
        team2.start(items);

        goal.fill(Material.WHITE_WOOL);

        Bukkit.getScheduler().runTaskLater(BattleBox.getPlugin(), () -> sendStartingTitle(ChatColor.RED, 5), 20L);
        Bukkit.getScheduler().runTaskLater(BattleBox.getPlugin(), () -> sendStartingTitle(ChatColor.RED, 4), 40L);
        Bukkit.getScheduler().runTaskLater(BattleBox.getPlugin(), () -> sendStartingTitle(ChatColor.GOLD, 3), 60L);
        Bukkit.getScheduler().runTaskLater(BattleBox.getPlugin(), () -> sendStartingTitle(ChatColor.YELLOW, 2), 80L);
        Bukkit.getScheduler().runTaskLater(BattleBox.getPlugin(), () -> sendStartingTitle(ChatColor.GREEN, 1), 100L);

        Bukkit.getScheduler().runTaskLater(BattleBox.getPlugin(), () -> sendStartTitle(ChatColor.DARK_GREEN, "FIGHT!"), 120L);
        Bukkit.getScheduler().runTaskLater(BattleBox.getPlugin(), () -> {
            team1.setWall(Material.AIR);
            team2.setWall(Material.AIR);
        }, 120L);

    }

    public static void resetGame() {
        for (BattleBoxTeam team : new BattleBoxTeam[]{team1, team2}) {
            for (String s : team.getTeam().getEntries()) {
                Player player = Bukkit.getPlayer(s);
                if (Objects.isNull(player))
                    continue;
                player.teleport(resetLoc);
                player.setAllowFlight(false);
                player.setInvulnerable(true);
                player.setInvisible(false);
                player.setCanPickupItems(false);
                player.setGameMode(GameMode.ADVENTURE);
                PlayerInventory inv = player.getInventory();
                inv.clear();
            }
        }

        team1.resetWall();
        team2.resetWall();

        goal.fill(Material.WHITE_WOOL);
        team1.getTeam().getEntries().clear();
        team2.getTeam().getEntries().clear();
    }

    public static void resetPlayer(Player player) {
        player.teleport(resetLoc);
        player.setAllowFlight(false);
        player.setInvulnerable(true);
        player.setInvisible(false);
        player.setCanPickupItems(false);
        player.setGameMode(GameMode.ADVENTURE);
        PlayerInventory inv = player.getInventory();
        inv.clear();
    }

    @Override
    public void onEnable() {
        plugin = this;
        items = new ArrayList<>();
        getCommand("battlebox").setExecutor(new BattleBoxCommand());
        getCommand("battlebox").setTabCompleter(new BattleBoxTabCompleter());
        loadConfig();
        getServer().getPluginManager().registerEvents(new BattleBoxEvents(), this);

        getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "[BattleBox] Plugin enabled!");
    }

    @Override
    public void onDisable() {
        getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "[BattleBox] Plugin disabled!");
    }

    public void loadConfig() {
        List<String> defaultItems = Arrays.asList("fishing_rod:1");
        Configuration config = this.getConfig();
        config.addDefault("items", defaultItems);
        config.options().copyDefaults(true);
        saveConfig();

        List<String> stringItems = config.getStringList("items");
        for (String s : stringItems) {
            String[] text = s.split(":");
            int amount;
            try {
                amount = Integer.parseInt(text[1]);
            } catch (NumberFormatException e) {
                continue;
            }
            items.add(new BattleBoxItem(text[0], amount));
        }

    }
}

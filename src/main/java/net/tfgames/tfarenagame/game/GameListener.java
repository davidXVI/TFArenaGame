package net.tfgames.tfarenagame.game;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.tfgames.tfarenagame.TFArenaGame;
import net.tfgames.tfgamingcore.arena.Arena;
import net.tfgames.tfgamingcore.arena.ArenaManager;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import java.text.DecimalFormat;
import java.util.Objects;

import static net.tfgames.tfarenagame.game.GameArena.ingamePlayers;
import static net.tfgames.tfarenagame.game.GameArena.setPlayerKit;

public class GameListener implements Listener {

    @EventHandler
    public void onDamage(EntityDamageEvent e){
        if(e.getEntity() instanceof Player){
            Player damaged = (Player) e.getEntity();
            if(ingamePlayers.contains(damaged)){
                e.setCancelled(false);
            }
        }
    }

    @EventHandler
    public void onDeath (PlayerDeathEvent e){
        Player player = e.getPlayer();
        Arena arena = ArenaManager.getArena(player);
        DecimalFormat format = new DecimalFormat("0.00");
        if(ingamePlayers.contains(player) && arena != null) {
            Bukkit.getScheduler().scheduleSyncDelayedTask(TFArenaGame.getInstance(), () -> player.spigot().respawn(), 2L);
            EntityDamageEvent.DamageCause deathCause = Objects.requireNonNull(player.getLastDamageCause()).getCause();
            e.getDrops().clear();
            e.setDeathMessage(null);
            if(player.getKiller() != null && ingamePlayers.contains(player.getKiller()) && player.getKiller() != player){
               Player killer = player.getKiller();
               GameArena.updateKillCount(killer);
               killer.playSound(killer, Sound.BLOCK_NOTE_BLOCK_PLING, 2.0F, 2.0F);
               if(deathCause.equals(EntityDamageEvent.DamageCause.ENTITY_ATTACK)){
                   MiniMessage mm = MiniMessage.miniMessage();
                   Component entityDeath = mm.deserialize("<white>[☠] <#94FB36>" + player.getName() + " <#A5ADAD>foi morto por <#D25038>" + killer.getName() + " <#96FFFF>" + "(" + format.format(killer.getHealth()) + "❤)");
                   arena.sendMessage(entityDeath);
               }
               else if(deathCause.equals(EntityDamageEvent.DamageCause.PROJECTILE)){
                   MiniMessage mm = MiniMessage.miniMessage();
                   Component entityDeath = mm.deserialize("<white>[☠] <#94FB36>" + player.getName() + " <#A5ADAD>foi atingido por <#D25038>" + killer.getName() + " <#96FFFF>" + "(" + format.format(killer.getHealth()) + "❤)");
                   arena.sendMessage(entityDeath);
               }
           }
        }
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent e) {
        Player player = e.getPlayer();
        Arena arena = ArenaManager.getArena(player);
        if (ingamePlayers.contains(player) && arena != null) {
            e.setRespawnLocation(GameConfig.getRandomSpawn(arena.getID(), 15));
            setPlayerKit(player);
        }
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent e){
        Player player = e.getPlayer();
        Arena arena = ArenaManager.getArena(player);
        if(ingamePlayers.contains(player) && arena != null){
            ingamePlayers.remove(player);
        }
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent e){
        Player player = e.getPlayer();
        Arena arena = ArenaManager.getArena(player);
        if(ingamePlayers.contains(player) && arena != null) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e){
        Player player = e.getPlayer();
        Arena arena = ArenaManager.getArena(player);
        if(ingamePlayers.contains(player) && arena != null) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e){
        Player player = e.getPlayer();
        Arena arena = ArenaManager.getArena(player);
        if(ingamePlayers.contains(player) && arena != null) {
            e.setCancelled(true);
        }
    }

}

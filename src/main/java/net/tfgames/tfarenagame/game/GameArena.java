package net.tfgames.tfarenagame.game;

import me.catcoder.sidebar.ProtocolSidebar;
import me.catcoder.sidebar.Sidebar;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.title.Title;
import net.tfgames.tfarenagame.TFArenaGame;
import net.tfgames.tfarenagame.util.ItemBuilder;
import net.tfgames.tfarenagame.util.MessageUtil;
import net.tfgames.tfgamingcore.arena.Arena;
import net.tfgames.tfgamingcore.arena.ArenaManager;
import net.tfgames.tfgamingcore.games.GameState;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;
import java.util.stream.Collectors;

public class GameArena {

    private final Arena arena;
    private int gameDuration;
    private int endDuration;

    private Player winningPlayer;
    public String time;
    public static List<Player> ingamePlayers;
    public static HashMap<Player, Integer> kills = new HashMap<>();
    private BukkitRunnable runnable;

    public GameArena(Arena arena) {
        this.arena = arena;
        this.gameDuration = 300;
        ingamePlayers = new ArrayList<>();
    }

    public void start() {
        kills.clear();
        gameDuration = 300;

        arena.setState(GameState.JOGANDO);

        for (Player arenaPlayer : arena.getPlayers()) {
            ingamePlayers.add(arenaPlayer);
            setPlayerKit(arenaPlayer);

            //for(int s = 1; s <= arena.getPlayers().size(); s++) {
            //    arenaPlayer.teleport(GameConfig.getSpawn(arena.getID(), s));
            //}

            arenaPlayer.teleport(GameConfig.getRandomSpawn(arena.getID(), 15));

            kills.put(arenaPlayer, 0);
            setGameSidebar(arenaPlayer);

        }

        arena.sendMessage(" ");
        arena.sendMessage(MessageUtil.colorize("&a&m&l                                                                 "));
        MessageUtil.sendArenaCenteredMessage(arena, ChatColor.YELLOW + "" + ChatColor.BOLD + "ARENA");
        arena.sendMessage(" ");
        MessageUtil.sendArenaCenteredMessage(arena, MessageUtil.colorize("&e&lSeja o jogador com mais kills até o final"));
        MessageUtil.sendArenaCenteredMessage(arena, MessageUtil.colorize("&e&ldo jogo e vença!"));
        arena.sendMessage(" ");
        arena.sendMessage(MessageUtil.colorize("&a&m&l                                                                 "));

        startTimer();
    }

    public void startTimer() {
        gameDuration = 300;
        runnable = new BukkitRunnable() {
            @Override
            public void run() {
                if (arena.getState().equals(GameState.RECRUTANDO) || arena.getState().equals(GameState.INICIANDO)) {
                    runnable.cancel();
                    runnable = null;
                }
                gameDuration--;
                checkGameOver();
            }
        };
        runnable.runTaskTimer(TFArenaGame.getInstance(), 0, 20);
    }

    public List<Player> getIngamePlayers() {
        return ingamePlayers;
    }

    public int getGameDuration() {
        return gameDuration;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void checkGameOver() {
        if (gameDuration == 0) {
            List<Player> keys = kills.entrySet().stream().sorted(Map.Entry.<Player, Integer>comparingByValue().reversed())
                    .limit(1).map(Map.Entry::getKey).collect(Collectors.toList());
            winningPlayer = keys.get(0);
            arena.setState(GameState.FINALIZANDO);
            runnable.cancel();
            startEnding(winningPlayer.getName());
        }
    }

    public void startEnding(String playerWinner) {

        //TITLE
        for (Player player : arena.getPlayers()) {

            if (player.getName().equals(playerWinner)) {
                Component winningText = Component.text("VITÓRIA!").color(NamedTextColor.GREEN);
                Component winningSubText = Component.text("Você terminou com mais kills!").color(NamedTextColor.GRAY);
                Title winningTitle = Title.title(winningText, winningSubText);
                player.showTitle(winningTitle);
            } else {
                Component losingText = Component.text("DERROTA!").color(NamedTextColor.RED);
                Component losingSubText = Component.text("Não foi dessa vez...").color(NamedTextColor.GRAY);
                Title winningTitle = Title.title(losingText, losingSubText);
                player.showTitle(winningTitle);
            }
        }

        //MENSAGEM
        arena.sendMessage(" ");
        arena.sendMessage(MessageUtil.colorize("&a&m&l                                                                 "));
        MessageUtil.sendArenaCenteredMessage(arena, ChatColor.YELLOW + "" + ChatColor.BOLD + "ARENA");
        arena.sendMessage(" ");
        MessageUtil.sendArenaCenteredMessage(arena, ChatColor.YELLOW + "Vencedor - " + ChatColor.GREEN + playerWinner);
        arena.sendMessage(" ");
        arena.sendMessage(MessageUtil.colorize("&a&m&l                                                                 "));
        //List<Player> keys = kills.entrySet().stream().sorted(Map.Entry.<Player, Integer>comparingByValue().reversed())
        //.limit(3).map(Map.Entry::getKey).collect(Collectors.toList());


        //FINALIZAÇÃO
        endDuration = 10;
        new BukkitRunnable() {
            @Override
            public void run() {
                if (endDuration == 0){
                    cancel();
                    arena.reset();
                }

                endDuration--;
            }
        }.runTaskTimer(TFArenaGame.getInstance(), 0, 20);

    }

    public static void setPlayerKit(Player player){
        player.getInventory().clear();

        ItemBuilder diamondSword = new ItemBuilder(Material.DIAMOND_SWORD);
        diamondSword.setEnchantment(Enchantment.DAMAGE_ALL, 5);
        diamondSword.setEnchantment(Enchantment.FIRE_ASPECT, 2);
        diamondSword.setUnbreakable();
        diamondSword.setFlag(ItemFlag.HIDE_UNBREAKABLE);
        player.getInventory().setItem(0, diamondSword.build());

        ItemBuilder diamondHelmet = new ItemBuilder(Material.DIAMOND_HELMET);
        diamondHelmet.setEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 5);
        diamondHelmet.setUnbreakable();
        diamondHelmet.setFlag(ItemFlag.HIDE_UNBREAKABLE);
        player.getInventory().setHelmet(diamondHelmet.build());

        ItemBuilder diamondChestplate = new ItemBuilder(Material.DIAMOND_CHESTPLATE);
        diamondChestplate.setEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 5);
        diamondChestplate.setUnbreakable();
        diamondChestplate.setFlag(ItemFlag.HIDE_UNBREAKABLE);
        player.getInventory().setChestplate(diamondChestplate.build());

        ItemBuilder diamondLeggings = new ItemBuilder(Material.DIAMOND_LEGGINGS);
        diamondLeggings.setEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 5);
        diamondLeggings.setUnbreakable();
        diamondLeggings.setFlag(ItemFlag.HIDE_UNBREAKABLE);
        player.getInventory().setLeggings(diamondLeggings.build());

        ItemBuilder diamondBoots = new ItemBuilder(Material.DIAMOND_BOOTS);
        diamondBoots.setEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 5);
        diamondBoots.setUnbreakable();
        diamondBoots.setFlag(ItemFlag.HIDE_UNBREAKABLE);
        player.getInventory().setBoots(diamondBoots.build());

        ItemBuilder bow = new ItemBuilder(Material.BOW);
        bow.setEnchantment(Enchantment.ARROW_DAMAGE, 5);
        bow.setEnchantment(Enchantment.ARROW_FIRE, 2);
        bow.setEnchantment(Enchantment.ARROW_INFINITE, 1);
        bow.setEnchantment(Enchantment.ARROW_KNOCKBACK, 2);
        bow.setUnbreakable();
        bow.setFlag(ItemFlag.HIDE_UNBREAKABLE);
        player.getInventory().setItem(1, bow.build());

        ItemBuilder arrow = new ItemBuilder(Material.ARROW);
        player.getInventory().setItem(2, arrow.build());

        ItemBuilder gapple = new ItemBuilder(Material.GOLDEN_APPLE);
        gapple.setAmount(10);
        player.getInventory().setItem(3, gapple.build());

        ItemBuilder food = new ItemBuilder(Material.COOKED_BEEF);
        food.setAmount(64);
        player.getInventory().setItem(4, food.build());

        ItemBuilder strengthPot = new ItemBuilder(Material.POTION);
        PotionMeta strengthPotMeta = (PotionMeta) strengthPot.getMeta();
        strengthPotMeta.addCustomEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 200, 2), true);
        strengthPotMeta.setColor(Color.RED);
        strengthPot.setMeta(strengthPotMeta);
        strengthPot.setAmount(5);
        strengthPot.setName(ChatColor.RED + "Poção de Força" + ChatColor.GRAY + " (10s)");
        player.getInventory().setItem(5, strengthPot.build());

        ItemBuilder regenPot = new ItemBuilder(Material.POTION);
        PotionMeta regenPotMeta = (PotionMeta) regenPot.getMeta();
        regenPotMeta.addCustomEffect(new PotionEffect(PotionEffectType.REGENERATION, 200, 2), true);
        regenPotMeta.setColor(Color.FUCHSIA);
        regenPot.setMeta(regenPotMeta);
        regenPot.setAmount(5);
        regenPot.setName(ChatColor.LIGHT_PURPLE + "Poção de Regeneração" + ChatColor.GRAY + " (10s)");
        player.getInventory().setItem(6, regenPot.build());

        ItemBuilder velocityPot = new ItemBuilder(Material.POTION);
        PotionMeta velPotMeta = (PotionMeta) velocityPot.getMeta();
        velPotMeta.addCustomEffect(new PotionEffect(PotionEffectType.SPEED, 100, 1), true);
        regenPot.setName(ChatColor.WHITE + "Poção de Velocidade" + ChatColor.GRAY + " (5s)");
        velPotMeta.setColor(Color.SILVER);
        velocityPot.setMeta(velPotMeta);
        velocityPot.setAmount(5);
        player.getInventory().setItem(7, velocityPot.build());

    }

    public static void updateKillCount(Player player){
        kills.put(player, kills.get(player) + 1);
        int killReward = 15;

        //serverCore.getPlayerManager().addCreditos(player, killReward);
        player.sendRichMessage("<green>[$] <gold>+5 <gray>créditos!");
    }

    public void setGameSidebar(Player sidebarPlayer){
        runnable = new BukkitRunnable() {
            @Override
            public void run() {

                if (gameDuration == 0) {runnable.cancel(); }
                else {

                    Component title = Component.text(arena.getGame(arena.getID()).getDisplay().toUpperCase())
                            .color(arena.getGame(arena.getID()).getColor())
                            .decorate(TextDecoration.BOLD);
                    Sidebar<Component> gameSidebar = ProtocolSidebar.newAdventureSidebar(title, TFArenaGame.getInstance());

                    gameSidebar.addBlankLine();
                    gameSidebar.addUpdatableLine(p -> Component.text("⌚ Tempo: ").append(Component.text(String.format("%02d:%02d", gameDuration / 60, gameDuration % 60)).color(NamedTextColor.GREEN)));
                    gameSidebar.addBlankLine();

                    gameSidebar.addLine(Component.text("\uD83C\uDFF9 Top Jogadores:").color(NamedTextColor.WHITE));

                    List<Map.Entry<Player, Integer>> entries = new ArrayList<>(kills.entrySet());
                    entries.sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));

                    for (int i = 0; i < Math.min(entries.size(), 5); i++) {
                        Map.Entry<Player, Integer> entry = entries.get(i);
                        Player player1 = entry.getKey();
                        int kills = entry.getValue();
                        int rank = i + 1;
                        Component lineComponent = Component.text("   " + rank + ": ")
                                .color(NamedTextColor.WHITE)
                                .append(Component.text(player1.getName() + ": ").color(sidebarPlayer == player1 ? NamedTextColor.GREEN : NamedTextColor.GRAY))
                                .append(Component.text(kills).color(NamedTextColor.YELLOW));
                        gameSidebar.addLine(lineComponent);
                    }

                    gameSidebar.addBlankLine();
                    gameSidebar.addUpdatableLine(player -> Component.text("\uD83D\uDDE1 ").append(Component.text("Seus Abates: ").color(NamedTextColor.WHITE)
                            .append(Component.text(Optional.ofNullable(kills.get(player)).orElse(0)).color(NamedTextColor.GREEN))));
                    gameSidebar.addBlankLine();
                    gameSidebar.addLine(Component.text("play.tfgames.com.br").color(NamedTextColor.YELLOW));

                    gameSidebar.addViewer(sidebarPlayer);
                    gameSidebar.updateAllLines();
                }
            }
        };
        runnable.runTaskTimer(TFArenaGame.getInstance(), 0, 20);
    }

}

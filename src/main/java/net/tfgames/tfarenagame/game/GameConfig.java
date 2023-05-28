package net.tfgames.tfarenagame.game;

import net.tfgames.tfarenagame.TFArenaGame;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.Random;

public class GameConfig {

    private final TFArenaGame plugin;
    private static YamlConfiguration gameConfig;
    private final File file;

    public GameConfig(TFArenaGame plugin) {
        this.plugin = plugin;

        if (!plugin.getDataFolder().exists()){
            plugin.getDataFolder().mkdir();
        }

        file = new File(plugin.getDataFolder(), "gameConfig.yml");
        if (!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        gameConfig = YamlConfiguration.loadConfiguration(file);

    }

    public void saveFile(){
        try {
            gameConfig.save(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void createArena(int arenaID){
        ConfigurationSection arenasList = gameConfig.createSection("arenas");
        ConfigurationSection arenaSection = arenasList.createSection(String.valueOf(arenaID));
        ConfigurationSection spawnSection = arenaSection.createSection("spawns");
        arenaSection.set("minPlayers", 2);
        arenaSection.set("maxPlayers", 12);
        saveFile();
    }

    public void setSpawn(int arenaID, int spawnID, Location location){
        ConfigurationSection spawnSection = gameConfig.getConfigurationSection("arenas." + arenaID + ".spawns");
        assert spawnSection != null;
        ConfigurationSection teamSpawn = spawnSection.createSection(String.valueOf(spawnID));
        teamSpawn.set("world", location.getWorld().getName());
        teamSpawn.set("x", location.getX());
        teamSpawn.set("y", location.getY());
        teamSpawn.set("z", location.getZ());
        teamSpawn.set("pitch", location.getPitch());
        teamSpawn.set("yaw", location.getYaw());
        saveFile();
    }

    public static Location getSpawn(int arenaID, int spawnID){
        return new Location(
                Bukkit.getWorld(Objects.requireNonNull(gameConfig.getString("arenas." + arenaID + ".spawns." + String.valueOf(spawnID) + ".world"))),
                gameConfig.getDouble("arenas." + arenaID + ".spawns." + String.valueOf(spawnID) + ".x"),
                gameConfig.getDouble("arenas." + arenaID + ".spawns." + String.valueOf(spawnID) + ".y"),
                gameConfig.getDouble("arenas." + arenaID + ".spawns." + String.valueOf(spawnID) + ".z"),
                (float) gameConfig.getDouble("arenas." + arenaID + ".spawns." + String.valueOf(spawnID) + ".pitch"),
                (float) gameConfig.getDouble("arenas." + arenaID + ".spawns." + String.valueOf(spawnID) + ".yaw"));
    }

    public static Location getRandomSpawn(int arenaID, int maxSpawns) {
        Random ran = new Random();
        int spawnID = ran.nextInt(maxSpawns);
        return new Location(
                Bukkit.getWorld(Objects.requireNonNull(gameConfig.getString("arenas." + arenaID + ".spawns." + String.valueOf(spawnID) + ".world"))),
                gameConfig.getDouble("arenas." + arenaID + ".spawns." + String.valueOf(spawnID) + ".x"),
                gameConfig.getDouble("arenas." + arenaID + ".spawns." + String.valueOf(spawnID) + ".y"),
                gameConfig.getDouble("arenas." + arenaID + ".spawns." + String.valueOf(spawnID) + ".z"),
                (float) gameConfig.getDouble("arenas." + arenaID + ".spawns." + String.valueOf(spawnID) + ".pitch"),
                (float) gameConfig.getDouble("arenas." + arenaID + ".spawns." + String.valueOf(spawnID) + ".yaw"));
    }

    public int getRequiredPlayers(int arenaID){
        return gameConfig.getInt("arenas." + arenaID + ".minPlayers");
    }

    public int getMaxPlayers(int arenaID){
        return gameConfig.getInt("arenas." + arenaID + ".maxPlayers");
    }


}

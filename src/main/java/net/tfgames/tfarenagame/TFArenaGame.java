package net.tfgames.tfarenagame;

import net.tfgames.tfarenagame.command.commands.GameSetupCommand;
import net.tfgames.tfarenagame.command.commands.KillAddCommand;
import net.tfgames.tfarenagame.game.GameArena;
import net.tfgames.tfarenagame.game.GameConfig;
import net.tfgames.tfarenagame.game.GameListener;
import net.tfgames.tfarenagame.util.MessageUtil;
import net.tfgames.tfgamingcore.TFGamingCore;
import net.tfgames.tfservercore.TFServerCore;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class TFArenaGame extends JavaPlugin {

    private static TFArenaGame instance;
    private TFServerCore serverCore;
    private TFGamingCore minigameCore;
    private GameConfig gameConfig;
    private MessageUtil messageUtil;

    @Override
    public void onEnable() {
        serverCore = (TFServerCore) Bukkit.getPluginManager().getPlugin("TFServerCore");
        minigameCore = (TFGamingCore) Bukkit.getPluginManager().getPlugin("TFGamingCore");
        instance = this;

        gameConfig = new GameConfig(this);
        messageUtil = new MessageUtil();

        new GameSetupCommand(this);
        new KillAddCommand(this);

        Bukkit.getPluginManager().registerEvents(new GameListener(), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public TFGamingCore getMinigameCore() {return minigameCore;}
    public TFServerCore getServerCore() {return serverCore;}

    public GameConfig getGameConfig() {return gameConfig;}
    public MessageUtil getMessageUtil() {return messageUtil;}
    public static TFArenaGame getInstance() {return instance;}
}
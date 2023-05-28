package net.tfgames.tfarenagame.command;

import net.tfgames.tfarenagame.TFArenaGame;
import net.tfgames.tfarenagame.game.GameConfig;
import net.tfgames.tfarenagame.util.MessageUtil;
import net.tfgames.tfgamingcore.TFGamingCore;
import net.tfgames.tfservercore.TFServerCore;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

public abstract class Command extends BukkitCommand {

    protected final TFArenaGame plugin;
    protected final TFGamingCore minigameCore;
    protected final TFServerCore serverCore;
    protected final GameConfig gameConfig;
    protected final MessageUtil messageUtil;
    //

    protected Command (TFArenaGame plugin, @NotNull String command, String[] aliases, String description, String permission, boolean playerOnly){
        super(command);

        this.plugin = plugin;
        this.serverCore = plugin.getServerCore();
        this.minigameCore = plugin.getMinigameCore();
        this.gameConfig = plugin.getGameConfig();
        this.messageUtil = plugin.getMessageUtil();

        this.setAliases(Arrays.asList(aliases));
        this.setDescription(description);
        this.setPermission(permission);

        try{
            Field field = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            field.setAccessible(true);
            CommandMap map = (CommandMap) field.get(Bukkit.getServer());
            map.register(command, this);
        }catch (NoSuchFieldException | IllegalAccessException e){
            e.printStackTrace();
        }
    }

    @Override
    public boolean execute(@NotNull CommandSender commandSender, @NotNull String s, @NotNull String[] strings) {
        execute(commandSender, strings);
        return false;
    }

    public abstract void execute(CommandSender sender, String[] args);

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) throws IllegalArgumentException {
        return onTabComplete(sender, args);
    }

    public abstract List<String> onTabComplete(CommandSender sender, String[] args);

}
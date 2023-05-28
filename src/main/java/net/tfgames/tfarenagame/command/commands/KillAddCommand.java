package net.tfgames.tfarenagame.command.commands;

import net.tfgames.tfarenagame.TFArenaGame;
import net.tfgames.tfarenagame.command.Command;
import net.tfgames.tfarenagame.game.GameArena;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class KillAddCommand extends Command {
    public KillAddCommand(TFArenaGame plugin) {
        super(
                plugin,
                "addkill",
                new String[]{},
                "",
                "",
                true);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        GameArena.updateKillCount(player);
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String[] args) {
        return null;
    }
}

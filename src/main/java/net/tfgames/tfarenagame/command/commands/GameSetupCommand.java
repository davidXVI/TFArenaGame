package net.tfgames.tfarenagame.command.commands;

import net.tfgames.tfarenagame.TFArenaGame;
import net.tfgames.tfarenagame.command.Command;
import net.tfgames.tfarenagame.game.GameConfig;
import net.tfgames.tfgamingcore.util.ErrorMessages;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class GameSetupCommand extends Command {


    public GameSetupCommand(TFArenaGame plugin) {
        super(
                plugin,
                "arnsetup",
                new String[]{},
                "",
                "",
                true);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        if(serverCore.getPlayerManager().getRank(player).getPermissionLevel() >= 9){
            if(args.length == 3){
                // /arnsetup setspawn ARENAID SPAWNID
                if(args[0].equals("setspawn")){
                    int arenaID = Integer.parseInt(args[1]);
                    int spawnID = Integer.parseInt(args[2]);
                    gameConfig.setSpawn(arenaID, spawnID, player.getLocation());
                    player.sendRichMessage("<green>[✔] <gray>Spawn de ID <gold>" + spawnID + " <gray>setado na Arena <gold>" + arenaID);
                }else if(args[0].equals("tpspawn")){
                    // /arnsetup tpspawn ARENAID SPAWNID
                    int arenaID = Integer.parseInt(args[1]);
                    int spawnID = Integer.parseInt(args[2]);
                    player.teleport(gameConfig.getSpawn(arenaID, spawnID));
                    player.sendRichMessage("<green>[✔] <gray>Teleportando para o Spawn de ID <gold>" + spawnID + " <gray>da Arena <gold>" + arenaID);
                }
            }else if(args.length == 2){
                // /arnsetup createarena ARENAID
                int arenaID = Integer.parseInt(args[1]);
                gameConfig.createArena(arenaID);
                player.sendRichMessage("<green>[✔] <gray>Arena <gold>" + arenaID + " <gray>criada com sucesso.");
            }
        }else{
            messageUtil.sendErrorMessage(player, ErrorMessages.NO_PERMISSION);
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String[] args) {
        List<String> options = new ArrayList<>();
        if(args.length == 1){
            options.add("setspawn");
            options.add("tpspawn");
            options.add("createarena");
        }
        return null;
    }
}

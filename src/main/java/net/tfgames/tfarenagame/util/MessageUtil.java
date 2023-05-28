package net.tfgames.tfarenagame.util;

import net.tfgames.tfgamingcore.arena.Arena;
import net.tfgames.tfgamingcore.util.DefaultFontInfo;
import net.tfgames.tfgamingcore.util.ErrorMessages;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class MessageUtil {

    private static final int CENTER_PX = 154;

    public static String colorize(String message){
        String colorized = ChatColor.translateAlternateColorCodes('&', message);

        return colorized;
    }

    public static void sendArenaCenteredMessage(Arena arena, String message){
        if(message == null || message.equals("")) arena.sendMessage("");
        message = ChatColor.translateAlternateColorCodes('&', message);

        int messagePxSize = 0;
        boolean previousCode = false;
        boolean isBold = false;

        for(char c : message.toCharArray()){
            if(c == '§'){
                previousCode = true;
                continue;
            }else if(previousCode){
                previousCode = false;
                if(c == 'l' || c == 'L'){
                    isBold = true;
                    continue;
                }else isBold = false;
            }else{
                DefaultFontInfo dFI = DefaultFontInfo.getDefaultFontInfo(c);
                messagePxSize += isBold ? dFI.getBoldLength() : dFI.getLength();
                messagePxSize++;
            }
        }

        int halvedMessageSize = messagePxSize / 2;
        int toCompensate = CENTER_PX - halvedMessageSize;
        int spaceLength = DefaultFontInfo.SPACE.getLength() + 1;
        int compensated = 0;
        StringBuilder sb = new StringBuilder();
        while(compensated < toCompensate){
            sb.append(" ");
            compensated += spaceLength;
        }
        arena.sendMessage(sb.toString() + message);
    }


    public void sendErrorMessage(Player player, ErrorMessages message){
        player.sendMessage(message.getMessage());
    }

}
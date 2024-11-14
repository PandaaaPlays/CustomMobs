package ca.pandaaa.custommobs.utils;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.profile.PlayerProfile;
import org.bukkit.profile.PlayerTextures;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {
    public static String applyFormat(String message) {
        message = message.replace(">>", "»").replace("<<", "«");

        Pattern hexPattern = Pattern.compile("&#([A-Fa-f0-9]){6}");
        Matcher matcherHex = hexPattern.matcher(message);
        while (matcherHex.find()) {
            ChatColor hexColor = ChatColor.of(matcherHex.group().substring(1));
            String before = message.substring(0, matcherHex.start());
            String after = message.substring(matcherHex.end());
            message = before + hexColor + after;
            matcherHex = hexPattern.matcher(message);
        }

        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public static ItemStack createHead(String url) {
        ItemStack head = new ItemStack(Material.PLAYER_HEAD);
        if (!url.isEmpty()) {
            SkullMeta headMeta = (SkullMeta)head.getItemMeta();
            headMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
            try {
                PlayerProfile playerProfile = Bukkit.createPlayerProfile(UUID.randomUUID());
                PlayerTextures playerTextures = playerProfile.getTextures();
                playerTextures.setSkin(new URL("https://textures.minecraft.net/texture/" + url));
                playerProfile.setTextures(playerTextures);
                headMeta.setOwnerProfile(playerProfile);
            } catch (MalformedURLException exception) {
                return head;
            }
            head.setItemMeta(headMeta);
        }
        return head;
    }

    public static String getSentenceCase(String string) {
        return string.toUpperCase().substring(0, 1) + string.toLowerCase().substring(1).replaceAll("_", " ");
    }

    public static String getStartCase(String string) {
        String[] words = string.toLowerCase().split("[_\\s]+");
        StringBuilder startCaseString = new StringBuilder();

        for (String word : words) {
            if (!word.isEmpty()) {
                startCaseString.append(Character.toUpperCase(word.charAt(0)))
                        .append(word.substring(1))
                        .append(" ");
            }
        }

        return startCaseString.toString().trim();
    }

    public static ChatColor getChatColorOfColor(String color) {
        ChatColor chatColor;
        switch (color.toUpperCase()) {
            case "RED":
                chatColor = ChatColor.RED;
                break;
            case "BLUE":
                chatColor = ChatColor.BLUE;
                break;
            case "GREEN": case "LIME":
                chatColor = ChatColor.GREEN;
                break;
            case "YELLOW":
                chatColor = ChatColor.YELLOW;
                break;
            case "BLACK": case "DARK_BROWN":
                chatColor = ChatColor.DARK_GRAY;
                break;
            case "ORANGE": case "CHESTNUT": case "CREAMY":
                chatColor = ChatColor.GOLD;
                break;
            case "PINK": case "MAGENTA":
                chatColor = ChatColor.LIGHT_PURPLE;
                break;
            case "CYAN":
                chatColor = ChatColor.DARK_AQUA;
                break;
            case "GRAY": case "LIGHT_GRAY":
                chatColor = ChatColor.GRAY;
                break;
            case "LIGHT_BLUE":
                chatColor = ChatColor.AQUA;
                break;
            case "PURPLE":
                chatColor = ChatColor.DARK_PURPLE;
                break;
            case "BROWN":
                chatColor = ChatColor.DARK_RED;
                break;
            case "WHITE": default:
                chatColor = ChatColor.WHITE;
                break;
        }

        return chatColor;
    }
}
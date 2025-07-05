package ca.pandaaa.custommobs.commands;

import ca.pandaaa.custommobs.CustomMobs;
import ca.pandaaa.custommobs.configurations.ConfigurationManager;
import ca.pandaaa.custommobs.custommobs.Manager;
import ca.pandaaa.custommobs.guis.MainGUI;
import ca.pandaaa.custommobs.utils.Utils;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.Objects;

public class Commands implements CommandExecutor {

    private final ConfigurationManager configManager;
    private final Manager customMobsManager;

    public Commands(ConfigurationManager configManager, Manager customMobsManager) {
        this.configManager = configManager;
        this.customMobsManager = customMobsManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String message, String[] args) {
        if (!(sender instanceof Player)
                && !(sender instanceof ConsoleCommandSender))
            return false;

        if (command.getName().equalsIgnoreCase("custommobs")) {
            if (args.length == 0) {
                if (sender instanceof Player)
                    openCustomMobsGUI((Player) sender);
                else
                    sendConsoleUnknownCommandMessage(sender, "The console cannot use this command.");
                return false;
            }

            switch (args[0].toLowerCase()) {
                case "generateentitytypesjsonforwebsite":
                    if(sender.getName().equalsIgnoreCase("PandaaaPlays"))
                        CustomMobs.getPlugin().generateEntityTypesJson();
                    else
                        sendUnknownCommandMessage(sender);
                    break;
                case "reload":
                    reloadPlugin(sender);
                    break;
                case "summon":
                    summonCommand(sender, args);
                    break;
                case "give":
                    giveCommand(sender, args);
                    break;
                case "silk-spawner":
                    silkSpawnerCommand(sender, args);
                    break;
                case "help":
                    helpCommand(sender);
                    break;
                default:
                    sendUnknownCommandMessage(sender);
                    break;
            }
        }
        return false;
    }

    private void summonCommand(CommandSender sender, String[] args) {
        if (!sender.hasPermission("custommobs.admin")) {
            sendNoPermissionMessage(sender);
            return;
        }

        if (args.length != 2 && args.length != 5) {
            sendUnknownCommandMessage(sender);
            return;
        }

        if (args.length == 2 && !(sender instanceof Player)) {
            sendConsoleUnknownCommandMessage(sender, "Please use : /custommobs summon [name] [x] [y] [z]");
            return;
        }

        if (!customMobsManager.getCustomMobNames().contains(args[1].toLowerCase())) {
            sendUnknownMobMessage(sender, args[1]);
            return;
        }

        if (args.length == 2)
            customMobsManager.getCustomMob(args[1]).spawnCustomMob(((Player) sender).getLocation());
        if (args.length == 5) {
            try {
                double x = args[2].equals("~") && sender instanceof Player ? ((Player) sender).getLocation().getX() : Double.parseDouble(args[2]);
                double y = args[3].equals("~") && sender instanceof Player ? ((Player) sender).getLocation().getY() : Double.parseDouble(args[3]);
                double z = args[4].equals("~") && sender instanceof Player ? ((Player) sender).getLocation().getZ() : Double.parseDouble(args[4]);
                customMobsManager.getCustomMob(args[1]).spawnCustomMob(
                        new Location(sender instanceof Player ? ((Player) sender).getLocation().getWorld() : Bukkit.getWorld("world"), x, y, z));
            } catch (Exception exception) {
                sendIncorrectCoordinatesMessage(sender);
            }
        }
    }

    private void giveCommand(CommandSender sender, String[] args) {
        if (!sender.hasPermission("custommobs.admin")) {
            sendNoPermissionMessage(sender);
            return;
        }

        if (args.length != 4 && args.length != 5) {
            sendUnknownGiveMessage(sender);
            return;
        }

        Player receiver = Objects.requireNonNull(Bukkit.getPlayer(args[1]));
        if (!Bukkit.getOnlinePlayers().contains(receiver)) {
            sendUnknownCommandMessage(sender, "Player " + args[1] + " is not online.");
            return;
        }

        String customMob = args[2].toLowerCase();
        if (!customMobsManager.getCustomMobNames().contains(args[2].toLowerCase())) {
            sendUnknownMobMessage(sender, args[2]);
            return;
        }

        String type = args[3];
        if (!type.equalsIgnoreCase("item") && !type.equalsIgnoreCase("spawner")) {
            sendUnknownGiveMessage(sender);
            return;
        }

        int amount = 1;
        if (args.length == 5) {
            try {
                amount = Integer.parseInt(args[4]);
            } catch (Exception e) {
                sendUnknownCommandMessage(sender, args[4] + " is not a valid Integer.");
            }
        }

        sender.sendMessage(configManager.getSenderGiveMessage(receiver.getName(), customMob, type, amount));
        String senderName;
        if (sender instanceof ConsoleCommandSender) {
            senderName = "Console";
        } else {
            senderName = sender.getName();
        }
        receiver.sendMessage(configManager.getReceiverGiveMessage(senderName, customMob, type, amount));

        customMobsManager.giveCustomMob(receiver, customMob, type, amount);
    }

    private void silkSpawnerCommand(CommandSender sender, String[] args) {
        if (!sender.hasPermission("custommobs.admin")) {
            sendNoPermissionMessage(sender);
            return;
        }

        if (args.length == 1) {
            configManager.setSilkSpawner(!configManager.getSilkSpawner());
        } else if (args.length == 2) {
            if (args[1].equalsIgnoreCase("on")) {
                configManager.setSilkSpawner(true);
            } else if (args[1].equalsIgnoreCase("off")) {
                configManager.setSilkSpawner(false);
            } else {
                configManager.setSilkSpawner(!configManager.getSilkSpawner());
            }
        } else {
            sendUnknownCommandMessage(sender, "Messages command formatted incorrectly.");
            return;
        }

        if(configManager.getSilkSpawner()){
            sendSilkSpawnerMessage(sender, "&a&lOn");
        } else {
            sendSilkSpawnerMessage(sender, "&c&lOff");
        }
    }

    private void reloadPlugin(CommandSender sender) {
        if (!sender.hasPermission("custommobs.config")) {
            sendNoPermissionMessage(sender);
            return;
        }

        CustomMobs.getPlugin().reloadConfig(sender);
    }

    private void helpCommand(CommandSender sender) {
        if (!sender.hasPermission("custommobs.config")) {
            sendNoPermissionMessage(sender);
            return;
        }

        sender.sendMessage(Utils.applyFormat("&6&lCus&e&ltom&8&lMo&7&lbs &7&l>> &eFor help, tutorials and examples of the plugin, visit : https://pandaaaplays.github.io/CustomMobs/"));
    }

    private void openCustomMobsGUI(Player player) {
        if (!player.hasPermission("custommobs.admin")) {
            sendNoPermissionMessage(player);
            return;
        }

        new MainGUI().openInventory(player, 1);
    }

    private void sendUnknownCommandMessage(CommandSender sender) {
        sendUnknownCommandMessage(sender, "");
    }

    private void sendUnknownCommandMessage(CommandSender sender, String complement) {
        sender.sendMessage(configManager.getUnknownCommandMessage(complement));
    }

    private void sendUnknownMobMessage(CommandSender sender, String mob) {
        sendUnknownCommandMessage(sender, "The mob " + mob + " does not exist.");
    }

    private void sendUnknownGiveMessage(CommandSender sender) {
        sendUnknownCommandMessage(sender, "Please use : /custommobs give [player] [name] [item/spawner]");
    }

    private void sendConsoleUnknownCommandMessage(CommandSender sender, String complement) {
        sender.sendMessage(configManager.getConsoleUnknownCommandMessage(complement));
    }

    private void sendIncorrectCoordinatesMessage(CommandSender sender) {
        sender.sendMessage(configManager.getIncorrectCoordinatesMessage());
    }

    private void sendNoPermissionMessage(CommandSender sender) {
        sender.sendMessage(configManager.getNoPermissionMessage());
    }

    private void sendSilkSpawnerMessage(CommandSender sender, String complement) {
        sender.sendMessage(configManager.getSilkSpawnerMessage(complement));
    }

}
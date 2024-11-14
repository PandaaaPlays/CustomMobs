package ca.pandaaa.custommobs.commands;

import ca.pandaaa.custommobs.CustomMobs;
import ca.pandaaa.custommobs.configurations.ConfigurationManager;
import ca.pandaaa.custommobs.custommobs.Manager;
import ca.pandaaa.custommobs.guis.CustomMobsGUI;
import ca.pandaaa.custommobs.guis.MainGUI;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
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
                if(sender instanceof Player)
                    openCustomMobsGUI((Player) sender);
                else
                    sendConsoleUnknownCommandMessage(sender, "The console cannot use this command.");
                return false;
            }

            switch (args[0].toLowerCase()) {
                case "test":
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
                case "message":
                    messageCommand(sender, args, false);
                    break;
                case "death-message":
                    messageCommand(sender, args, true);
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

        if(args.length != 2 && args.length != 5) {
            sendUnknownCommandMessage(sender);
            return;
        }

        if(args.length == 2 && !(sender instanceof Player)) {
            sendConsoleUnknownCommandMessage(sender, "Please use : /custommobs summon [name] [x] [y] [z]");
            return;
        }

        if(!customMobsManager.getCustomMobNames().contains(args[1].toLowerCase())) {
            sendUnknownMobMessage(sender, args[1]);
            return;
        }

        if(args.length == 2)
            customMobsManager.getCustomMob(args[1]).spawnCustomMob(((Player) sender).getLocation());
        if(args.length == 5) {
            try {
                double x = Double.parseDouble(args[2]);
                double y = Double.parseDouble(args[3]);
                double z = Double.parseDouble(args[4]);
                customMobsManager.getCustomMob(args[1]).spawnCustomMob(new Location(Bukkit.getWorld("world"), x, y, z));
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

        if(args.length != 4 && args.length != 5) {
            sendUnknownGiveMessage(sender);
            return;
        }

        Player receiver = Objects.requireNonNull(Bukkit.getPlayer(args[1]));
        if(!Bukkit.getOnlinePlayers().contains(receiver)) {
            sendUnknownCommandMessage(sender, "Player " + args[1] + " is not online.");
            return;
        }

        String customMob = args[2].toLowerCase();
        if(!customMobsManager.getCustomMobNames().contains(args[2].toLowerCase())) {
            sendUnknownMobMessage(sender, args[2]);
            return;
        }

        String type = args[3];
        if(!type.equalsIgnoreCase("item") && !type.equalsIgnoreCase("spawner")) {
            sendUnknownGiveMessage(sender);
            return;
        }

        int amount = 1;
        if(args.length == 5) {
            try {
                amount = Integer.parseInt(args[4]);
            } catch (Exception e) {
                sendUnknownCommandMessage(sender, args[4] + " is not a valid Integer.");
            }
        }

        sender.sendMessage(configManager.getSenderGiveMessage(receiver.getName(), customMob, type, amount));
        String senderName;
        if(sender instanceof ConsoleCommandSender) {
            senderName = "Console";
        } else {
            senderName = sender.getName();
        }
        receiver.sendMessage(configManager.getReceiverGiveMessage(senderName, customMob, type, amount));

        customMobsManager.giveCustomMob(receiver, customMob, type, amount);
    }

    private void messageCommand(CommandSender sender, String[] args, boolean death) {
        if (!sender.hasPermission("custommobs.admin")) {
            sendNoPermissionMessage(sender);
            return;
        }

        if(args.length < 4) {
            sendUnknownCommandMessage(sender);
            return;
        }

        if(!customMobsManager.getCustomMobNames().contains(args[1].toLowerCase())) {
            sendUnknownMobMessage(sender, args[1]);
        }

        if(args[2].equalsIgnoreCase("add")) {
            String message = String.join(" ", Arrays.copyOfRange(args, 3, args.length));
            customMobsManager.getCustomMob(args[1]).getCustomMobConfiguration().addMessageText(message, death);
            sendMessagesChangeMessage(sender, "Added the following" + (death ? " death " : " ") + "message to " + args[1] + " : &r" + message);
        } else if(args.length == 4 && args[2].equalsIgnoreCase("remove")) {
            try {
                if(args[3].equalsIgnoreCase("all")) {
                    customMobsManager.getCustomMob(args[1]).getCustomMobConfiguration()
                            .clearMessageText(death);
                    sendMessagesChangeMessage(sender, "Removed all the" + (death ? " death " : " ") + "messages of " + args[1] + ".");
                } else if(customMobsManager.getCustomMob(args[1]).getCustomMobConfiguration().removeMessageText(Integer.parseInt(args[3]), death)) {
                    sendMessagesChangeMessage(sender, "Removed" + (death ? " death " : " ") + "message " + Integer.parseInt(args[3]) + " of " + args[1] + ".");
                } else {
                    sendUnknownCommandMessage(sender, "There isn't " + Integer.parseInt(args[3]) + " messages.");
                }
            } catch (Exception e) {
                sendUnknownCommandMessage(sender, args[3] + " is not a valid Integer.");
            }
        } else if(args.length >= 5 && args[2].equalsIgnoreCase("edit")) {
            try {
                String message = String.join(" ", Arrays.copyOfRange(args, 4, args.length));
                if(customMobsManager.getCustomMob(args[1]).getCustomMobConfiguration().editMessageText(Integer.parseInt(args[3]), message, death)) {
                    sendMessagesChangeMessage(sender, "Edited" + (death ? " death " : " ") + "message " + Integer.parseInt(args[3]) + " of " + args[1] +  " to : " + message);
                } else {
                    sendUnknownCommandMessage(sender, "There isn't " + Integer.parseInt(args[3]) + (death ? " death " : " ") + "messages.");
                }
            } catch (Exception e) {
                sendUnknownCommandMessage(sender, args[3] + " is not a valid Integer.");
            }
        } else {
            sendUnknownCommandMessage(sender, "Messages command formatted incorrectly.");
        }
    }

    private void reloadPlugin(CommandSender sender) {
        if (!sender.hasPermission("custommobs.config")) {
            sendNoPermissionMessage(sender);
            return;
        }

        CustomMobs.getPlugin().reloadConfig(sender);
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

    private void sendMessagesChangeMessage(CommandSender sender, String complement) {
        sender.sendMessage(configManager.getMessagesChangeMessage(complement));
    }
}
package ca.pandaaa.custommobs.commands;

import ca.pandaaa.custommobs.CustomMobs;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class TabCompletion implements TabCompleter {
    Set<String> customMobNames = CustomMobs.getPlugin().getCustomMobsManager().getCustomMobNames();
    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String arg, String[] args) {
        List<String> completionList = new ArrayList<>();
        if (sender.hasPermission("custommobs.config")) {
            if(args.length == 1) {
                completionList.add("reload");
                completionList.add("summon");
                completionList.add("give");
                completionList.add("message");
            }
            if(args.length == 2 && (args[0].equalsIgnoreCase("summon")
                || args[0].equalsIgnoreCase("message"))) {
                completionList.addAll(customMobNames);
            }
            if(args.length == 2 && args[0].equalsIgnoreCase("give")) {
                completionList = Bukkit.getOnlinePlayers().stream()
                        .map(Player::getName).collect(Collectors.toList());
            }

            if(args.length == 3 && args[0].equalsIgnoreCase("give")) {
                completionList.addAll(customMobNames);
            }
            if(args.length == 3 && args[0].equalsIgnoreCase("message")) {
                completionList.add("add");
                completionList.add("edit");
                completionList.add("remove");
            }

            if(args.length == 4 && args[0].equalsIgnoreCase("give")) {
                completionList.add("item");
                completionList.add("spawner");
            }
        }
        return completionList;
    }
}
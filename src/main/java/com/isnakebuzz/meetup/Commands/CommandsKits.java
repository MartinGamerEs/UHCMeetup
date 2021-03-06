package com.isnakebuzz.meetup.Commands;

import com.isnakebuzz.meetup.Main;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.regex.Pattern;

public class CommandsKits implements CommandExecutor {

    private Main plugin;

    public CommandsKits(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!sender.hasPermission("uhcmeetup.admin") || !(sender instanceof Player)) {
            sender.sendMessage(c("&cYou don't have permissions."));
            return false;
        }

        Player p = (Player) sender;

        if (cmd.getName().equalsIgnoreCase("kits")) {
            if (args.length < 1) {
                sendCommands(p);
                return true;
            }

            if (args[0].equalsIgnoreCase("create")) {
                if (args.length < 2) {
                    sendCommands(p);
                    return false;
                }
                String kit_name = args[1];
                if (plugin.getCustomKits().saveKit(p, kit_name)) {
                    p.sendMessage(c("&EventsManager➠&Managers You has been added new kit with name: &a" + kit_name));
                } else {
                    p.sendMessage(c("&EventsManager➠&Managers The kitname already exists"));
                }
            } else if (args[0].equalsIgnoreCase("delete")) {
                if (args.length < 2) {
                    sendCommands(p);
                    return false;
                }
                String kit_name = args[1];
                if (plugin.getCustomKits().deleteKit(p, kit_name)) {
                    p.sendMessage(c("&EventsManager➠&Managers You has been removed kit with name: &a" + kit_name));
                } else {
                    p.sendMessage(c("&EventsManager➠&Managers The kitname doest exists"));
                }
            } else if (args[0].equalsIgnoreCase("load")) {
                if (args.length < 2) {
                    sendCommands(p);
                    return false;
                }
                String kit_name = args[1];
                if (plugin.getCustomKits().loadKit(p, kit_name)) {
                    p.sendMessage(c("&EventsManager➠&Managers You received the kit: " + kit_name));
                } else {
                    p.sendMessage(c("&EventsManager➠&Managers The kitname doest exists"));
                }
            } else if (args[0].equalsIgnoreCase("gapple")) {
                if (args.length < 2) {
                    sendCommands(p);
                    return false;
                }
                int number = Integer.valueOf(args[1]);
                p.getInventory().addItem(plugin.getWorldUitls().goldenHead(number));
                p.sendMessage(c("&EventsManager➠&Managers Has been gived &a" + number + "&Managers GApples."));
            } else if (args[0].equalsIgnoreCase("list")) {
                p.sendMessage(c("&EventsManager➠&Managers Kits created: "));
                File dir = new File(plugin.getDataFolder() + "/Kits/");
                File[] files = dir.listFiles();
                for (File file : files) {
                    String kit = file.getName().split(Pattern.quote("."))[0];
                    p.sendMessage(c("&EventsManager -&Managers " + kit));
                }
            } else {
                sendCommands(p);
                return true;
            }
        }

        return false;
    }

    public void sendCommands(Player p) {
        p.sendMessage(c("&3&Inventory  [               &6 UHCMeetupV4 &7[&a" + plugin.getDescription().getVersion() + "&7] &3&Inventory               ]  &ScoreBoard"));
        p.sendMessage(c("&a►►&6 /kit create {name} &EventsManager➠&Managers Add kit."));
        p.sendMessage(c("&a►►&6 /kit delete {name} &EventsManager➠&Managers Remove kit."));
        p.sendMessage(c("&a►►&6 /kit load {name} &EventsManager➠&Managers Load kit."));
        p.sendMessage(c("&a►►&6 /kit gapple {number} &EventsManager➠&Managers Give GApple."));
        p.sendMessage(c("&a►►&6 /kit list &EventsManager➠&Managers Get list of created kits."));
    }

    private String c(String c) {
        return ChatColor.translateAlternateColorCodes('&', c);
    }
}

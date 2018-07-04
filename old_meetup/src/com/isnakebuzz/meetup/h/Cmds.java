package com.isnakebuzz.meetup.h;

import com.isnakebuzz.meetup.a.Main;
import com.isnakebuzz.meetup.b.*;
import com.isnakebuzz.meetup.e.*;
import com.isnakebuzz.meetup.f.*;
import com.isnakebuzz.meetup.k.*;

import java.util.List;
import java.util.UUID;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Cmds implements CommandExecutor{
    private final Main plugin;

    public Cmds(Main plugin) {
        this.plugin = plugin;
    }

    public void init() {
        plugin.getCommand("meetup").setExecutor(this);
        plugin.getCommand("reroll").setExecutor(this);
        plugin.getCommand("gaps").setExecutor(this);
        plugin.getCommand("stat").setExecutor(this);
        plugin.getCommand("vote").setExecutor(this);
        plugin.getCommand("forcestart").setExecutor(this);
        plugin.getCommand("mlg").setExecutor(this);
        this.list = plugin.getConfig().getStringList("stats");
    }
    
    List<String> list;
    
    @SuppressWarnings("unused")
	@Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        
    	if (sender.hasPermission("meetup.admin")) {
    		if (cmd.getName().equalsIgnoreCase("forcestart")) {
            	API.started = true;
            	new Starting(Main.plugin).runTaskTimer(Main.plugin, 02L, 20L);
            }
    	}
    	
        if (!(sender instanceof Player)){
            return true;
        }
       
        Player p = (Player) sender;
        final UUID Uuid = p.getUniqueId();
        
        if (cmd.getName().equalsIgnoreCase("stat")){
            for (String news : list) {
                p.sendMessage(c(news
                        .replaceAll("%wins%", "0")
                        .replaceAll("%kills%", "0")
                        .replaceAll("%deaths%", "0")
                ));
            }
        }
        
        if (cmd.getName().equalsIgnoreCase("mlg")) {
        	if (MLG.finished == true) {
        		return true;
        	}
        	if (API.ALivePs.size() == 1) {
        		API.MLG.add(p);
        		API.MLGe = true;
        		new MLG(Main.plugin).runTaskTimer(Main.plugin, 02L, 20L);
        	}else {
        		return true;
        	}
        }
        
        if (cmd.getName().equalsIgnoreCase("reroll")){
            if (!p.hasPermission("meetup.reroll")){
                return true;
            }
            if (API.ReRoll.get(p) >= Main.plugin.getConfig().getInt("GameOptions.MaxRerrolls")){
                return true;
            }else{
                p.getInventory().setArmorContents(null);
                p.getInventory().clear();
                plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
                    Kits.RandomKit(p);
                }, 5);
                API.ReRoll.put(p, API.ReRoll.get(p)+1);
                p.sendMessage(c(MessagesManager.Rerolled));
            }
        }
        
        if (cmd.getName().equalsIgnoreCase("vote")) {
        	if (States.state != States.STARTING) {
        		return true;
        	}
        	new Scenarios(p).o(p);
        }
        
        //Admin CMDs
        
        if (!p.hasPermission("meetup.admin")){
            return true;
        }
        
        if (cmd.getName().equalsIgnoreCase("gaps")){
            ItemStack gaps = new ItemStack(Material.GOLDEN_APPLE, 1);
            ItemMeta g = gaps.getItemMeta();
            g.setDisplayName(org.bukkit.ChatColor.GOLD + "Golden Heads");
            gaps.setItemMeta(g);
            p.getInventory().addItem(gaps);
        }
        
        if (cmd.getName().equalsIgnoreCase("meetup")){
            if (args.length < 1){
                p.sendMessage(c("&e/meetup kitmode | This options is for adds new kits"));
                p.sendMessage(c("&e/meetup addkit | Add new kit"));
                p.sendMessage(c("&e/meetup removekit | Remove latest kit created"));
                p.sendMessage(c("&e/meetup givekit int | Give kit"));
                p.sendMessage(c("&e/gaps | Give golden head"));
                p.sendMessage(c("&e/reroll |Reroll player, give new kit. (Recomended for V.I.P.S)"));
                return true;
            } else {
                switch (args[0].toLowerCase()) {
                    case "kitmode":
                        if (API.KitMode.contains(p)){
                            API.KitMode.remove(p);
                            p.sendMessage(c("&eKitMode &cdisabled"));
                        }else{
                            API.KitMode.add(p);
                            p.sendMessage(c("&eKitMode &aenabled"));
                        }
                        break;
                    case "addkit":
                        int kit = Main.plugin.getConfig().getInt("Kits");
                        kit++;
                        Kits.SaveKit(p, kit);
                        p.sendMessage(c("&6Kit number &a" + kit + "&6 saved"));
                        Main.plugin.getConfig().set("Kits", kit);
                        Main.plugin.saveConfig();
                        break;
                    case "removekit":
                        int kit2 = Main.plugin.getConfig().getInt("Kits");
                        Kits.DeleteKit(p, kit2);
                        p.sendMessage(c("&cKit number &e" + kit2 + "&c deleted"));
                        kit2--;
                        Main.plugin.getConfig().set("Kits", kit2);
                        Main.plugin.saveConfig();
                        break;
                    case "givekit":
                        if (args.length < 1){
                            p.sendMessage(c("&e/meetup givekit int | Give kit"));
                            return true;
                        }
                        int a = Integer.parseInt(args[1]);
                        Kits.LoadKit(p, a);
                        break;
                }
            }
        }
        
        return false;
    }
    
    private String c(String c){
        return ChatColor.translateAlternateColorCodes('&', c);
    }
}
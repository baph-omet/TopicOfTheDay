package io.github.griffenx.TopicOfTheDay;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

public class CommandHandler implements CommandExecutor {
	
	private final Plugin plugin;

	public CommandHandler(Plugin plugin) {
		this.plugin = plugin;
	}
	
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (label.equalsIgnoreCase("topic") || label.equalsIgnoreCase("totd")) {
			
			if (args.length == 0) {
				if (sender.hasPermission("totd.view")) {
					String[] messages = {
							ChatColor.DARK_GREEN + "=============================",
							ChatColor.GREEN + "Today's discussion topic is:",
							ChatColor.GREEN + TopicOfTheDay.dailyTopic,
							ChatColor.DARK_GREEN + "----------",
							ChatColor.GREEN + "Discuss!",
							ChatColor.DARK_GREEN + "============================="
					};
					sender.sendMessage(messages);
				} else sender.sendMessage(ChatColor.RED + "You do not have permission to run that command.");
				return true;
			}
			
			switch (args[0].toLowerCase()) {
				
				case "h":
				case "help":
					if (sender.hasPermission("totd.help")) {
						sender.sendMessage(ChatColor.DARK_GREEN + "All available TopicOfTheDay commands:");
						if (sender.hasPermission("totd.view")) sender.sendMessage(ChatColor.GREEN + "/totd - View the current Topic");
						if (sender.hasPermission("totd.add")) sender.sendMessage(ChatColor.GREEN + "/totd add <New Topic...> - Add a new Topic");
						if (sender.hasPermission("totd.remove")) sender.sendMessage(ChatColor.GREEN + "/totd remove <Old Topic...> - Remove an old Topic");
						if (sender.hasPermission("totd.reroll")) sender.sendMessage(ChatColor.GREEN + "/totd reroll - Swap out the current Topic");
						if (sender.hasPermission("totd.broadcast")) sender.sendMessage(ChatColor.GREEN + "/totd broadcast - Show the current Topic to everyone");
						if (sender.hasPermission("totd.reload")) sender.sendMessage(ChatColor.GREEN + "/totd reload - Reload the Config");
					} else sender.sendMessage(ChatColor.RED + "You do not have permission to run that command.");
					return true;
				
				case "a":
				case "add":
					if (sender.hasPermission("totd.add")) {
						if (args.length > 1) {
							String newTopic = collapseArguments(args);
							List<String> topics = plugin.getConfig().getStringList("topics");
							topics.add(newTopic);
							plugin.getConfig().set("topics", topics);
							
							sender.sendMessage(ChatColor.GREEN + "Topic added! Reload the config to see changes take effect.");
						} else sender.sendMessage(ChatColor.RED + "Not enough arguments: \"/" + label + " add <New Topic...>\"");
					} else sender.sendMessage(ChatColor.RED + "You do not have permission to run that command.");
					return true;
				
				case "r":
				case "remove":
					if (sender.hasPermission("totd.remove")) {
						if (args.length > 1) {
							String oldTopic = collapseArguments(args);
							List<String> topics = plugin.getConfig().getStringList("topics");
							if (topics.contains(oldTopic)) {
								topics.remove(oldTopic);
								sender.sendMessage(ChatColor.GREEN + "Topic removed! Reload the config to see changes take effect.");
							}
							else sender.sendMessage(ChatColor.RED + "Could not find that topic.");
						} else sender.sendMessage(ChatColor.RED + "Not enough arguments: \"/" + label + " add <New Topic...>\"");
					} else sender.sendMessage(ChatColor.RED + "You do not have permission to run that command.");
					return true;
				
				case "rr":
				case "reroll":
					//TODO: get a new topic
					if (sender.hasPermission("totd.reroll")) {
						String topic = TopicOfTheDay.getDailyTopic();
						sender.sendMessage(new String[]{
							ChatColor.GREEN + "The new Topic Of The Day is:",
							ChatColor.GREEN + topic,
							ChatColor.GREEN + "Not feeling this one? Run the command again to reroll."
						});
					} else sender.sendMessage(ChatColor.RED + "You do not have permission to run that command.");
					return true;
				
				case "b":
				case "broadcast":
					if (sender.hasPermission("totd.broadcast")) {
						String[] messages = {
								ChatColor.DARK_GREEN + "=============================",
								ChatColor.GREEN + "Today's discussion topic is:",
								ChatColor.GREEN + TopicOfTheDay.dailyTopic,
								ChatColor.DARK_GREEN + "----------",
								ChatColor.GREEN + "Discuss!",
								ChatColor.DARK_GREEN + "============================="
						};
						
						for (String s : messages) {
							plugin.getServer().broadcast(s, "totd.view");
						}
					} else sender.sendMessage(ChatColor.RED + "You do not have permission to run that command.");
					return true;
					
				
				case "rl":
				case "reload":
					if (sender.hasPermission("totd.reload")) {
						try {
							plugin.reloadConfig();
							sender.sendMessage(ChatColor.GREEN + "Config reloaded!");
						} catch (Exception e) {
							sender.sendMessage(ChatColor.RED + "An exception was encountered while trying to reload the config. Check your console for details.");
							plugin.getLogger().severe("An exception was encountered while trying to reload the config for TopicOfTheDay.");
							plugin.getLogger().severe(e.toString());
						}
					} else sender.sendMessage(ChatColor.RED + "You do not have permission to run that command.");
					return true;
				
				case "save":
					if (sender.hasPermission("totd.save")) {
						try {
							plugin.saveConfig();
							sender.sendMessage(ChatColor.GREEN + "Config saved!");
						} catch (Exception e) {
							sender.sendMessage(ChatColor.RED + "An exception was encountered while trying to save the config. Check your console for details.");
							plugin.getLogger().severe("An exception was encountered while trying to save the config for TopicOfTheDay.");
							plugin.getLogger().severe(e.toString());
						}
					} else sender.sendMessage(ChatColor.RED + "You do not have permission to run that command.");
					return true;
			}
		}
		return false;
	}
	
	private String collapseArguments(String[] args) {
		String newTopic = "";
		for (int i = 1; i < args.length; i++) newTopic += args[i] + (i == args.length - 1 ? "" : " ");
		return newTopic;
	}
}

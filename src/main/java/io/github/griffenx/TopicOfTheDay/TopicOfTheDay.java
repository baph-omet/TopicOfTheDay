package io.github.griffenx.TopicOfTheDay;

import java.util.List;
import java.util.logging.Logger;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class TopicOfTheDay extends JavaPlugin implements Listener {
	private static final Logger log = Logger.getLogger("Minecraft");
	private static TopicOfTheDay plugin;
	public static String dailyTopic;
	
	@Override
	public void onEnable() {
		plugin = this;
		this.getCommand("totd").setExecutor(new CommandHandler(this));
		this.getCommand("topic").setExecutor(new CommandHandler(this));
		getServer().getPluginManager().registerEvents(this, this);
		this.saveDefaultConfig();
		getConfig();
		getDailyTopic();
		
		log.info("Today's daily topic is:");
		log.info(dailyTopic);
	}
	
	public void onDisable() {
		plugin = null;
		saveConfig();
	}
	
	@EventHandler(priority=EventPriority.NORMAL)
	public void onLogin(PlayerLoginEvent event) {
		new TopicTask(event.getPlayer(),dailyTopic).runTaskLater(plugin, getConfig().getLong("displayDelay"));
		//log.info("Scheduled display of TOTD for player " + event.getPlayer().getName());
	}
	
	public static Plugin getPlugin(){
		return plugin;
	}
	
	public static String getDailyTopic() {
		List<String> topics = plugin.getConfig().getStringList("topics");
		int selectedIndex = (int)(Math.random()*(topics.size()));
		String topic = topics.get(selectedIndex);
		dailyTopic = topic;
		return topic;
	}
}

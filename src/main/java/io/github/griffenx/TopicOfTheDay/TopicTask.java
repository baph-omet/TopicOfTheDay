package io.github.griffenx.TopicOfTheDay;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class TopicTask extends BukkitRunnable {
	private Player recipient;
	
	public TopicTask(Player recipient, String topic) {
		this.recipient = recipient;
	}
	
	public void run() {
		if (recipient.isOnline() && recipient.hasPermission("totd.view")) {
			String[] messages = {
					ChatColor.DARK_GREEN + "=============================",
					ChatColor.GREEN + "Today's discussion topic is:",
					ChatColor.GREEN + TopicOfTheDay.dailyTopic,
					ChatColor.DARK_GREEN + "----------",
					ChatColor.GREEN + "Discuss!",
					ChatColor.DARK_GREEN + "============================="
			};
			recipient.sendMessage(messages);
		} else this.cancel();
	}
}

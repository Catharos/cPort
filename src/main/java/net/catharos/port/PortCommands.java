package net.catharos.port;

import net.catharos.lib.command.Command;
import net.catharos.lib.util.MessageUtil;
import org.bukkit.command.CommandSender;


public class PortCommands {
	@Command(name = "port clearcache", permission = "cport.clearcache", consoleCmd = true)
	public void clearCache(CommandSender cs, String[] args) {
		int amnt = PortPlugin.getInstance().getSignMap().size();
		PortPlugin.getInstance().getSignMap().clear();
		
		MessageUtil.sendMessage(cs, "&2Cleared &6%0 &2cached signs", amnt);
	}
}

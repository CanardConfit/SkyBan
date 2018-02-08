package command;

import java.io.IOException;

import ch.canardconfit.skyban.SkyBan;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.command.ConsoleCommandSender;

public class Gunmute extends Command {

	public Gunmute(String name) {
		super(name, "skyban.gunmute");
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		
		if (args.length == 1) {
			try {
				SkyBan.main.removemute(args[0]);
				if (sender instanceof ProxiedPlayer) {	
					SkyBan.main.sendUnMuteMessage(args[0], sender.getName());
				} else if (sender instanceof ConsoleCommandSender) {	
					SkyBan.main.sendUnMuteMessageconsole(args[0], "console");
				}
			} catch (IOException e) {e.printStackTrace();}
		} else {
			sender.sendMessage(new TextComponent(SkyBan.main.changesymbole(SkyBan.main.messages.getString("utilisation-message.utilisation-gunmute"))));
		}
	}

}

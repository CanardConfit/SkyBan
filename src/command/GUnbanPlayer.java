package command;

import java.io.IOException;
import java.net.InetAddress;

import ch.canardconfit.skyban.SkyBan;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.command.ConsoleCommandSender;

public class GUnbanPlayer extends Command {

	public GUnbanPlayer(String name) {
		super(name, "skyban.gunbanip");
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		if (args.length == 0) {
			sender.sendMessage(new TextComponent(SkyBan.main.changesymbole(SkyBan.main.messages.getString("utilisation-message.utilisation-gunbanip"))));
		} else {
			try {
				if (InetAddress.getByName(args[0]) != null) {
					SkyBan.main.removebanip(InetAddress.getByName(args[0]));
					if (sender instanceof ProxiedPlayer) {
						SkyBan.main.sendUnBanIpMessage(InetAddress.getByName(args[0]), (ProxiedPlayer) sender);
					} else if (sender instanceof ConsoleCommandSender) {
						SkyBan.main.sendUnBanIpMessageconsole(InetAddress.getByName(args[0]), "console");
					}
				} else {
					sender.sendMessage(new TextComponent(SkyBan.main.changesymbole(SkyBan.main.messages.getString("command-message.ipnotvalide"))));
				}
			} catch (IOException e) {e.printStackTrace();}
		}
	}

}

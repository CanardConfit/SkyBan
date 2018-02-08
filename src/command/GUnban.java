package command;

import java.io.IOException;

import ch.canardconfit.skyban.SkyBan;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.command.ConsoleCommandSender;

public class GUnban extends Command {

	public GUnban(String name) {
		super(name, "skyban.gunban");
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		if (args.length == 0) {
			sender.sendMessage(new TextComponent(SkyBan.main.changesymbole(SkyBan.main.messages.getString("utilisation-message.utilisation-gunban"))));
		} else {
			try {
				SkyBan.main.removebanplayer(args[0]);
				if (sender instanceof ProxiedPlayer) {
					SkyBan.main.sendUnBanMessage(args[0], (ProxiedPlayer) sender);
				} else if (sender instanceof ConsoleCommandSender) {
					SkyBan.main.sendUnBanMessageconsole(args[0], "console");
				}
			} catch (IOException e) {e.printStackTrace();}
		}
	}

}

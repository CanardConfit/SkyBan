package command;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;

import ch.canardconfit.skyban.SkyBan;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.command.ConsoleCommandSender;

public class GBan extends Command {
	
	public GBan(String name) {
		super(name, "skyban.gban");
	}
	@Override
	public void execute(CommandSender sender, String[] args) {
		if (args.length == 0) {
			sender.sendMessage(new TextComponent(SkyBan.main.changesymbole(SkyBan.main.messages.getString("utilisation-message.utilisation-gban"))));
		} else {
			String bannis = args[0];
			if (!(bannis == null)) {
				ArrayList<String> arrayreason = new ArrayList<String>(Arrays.asList(args));
				arrayreason.remove(0);
				StringBuilder sb = new StringBuilder();
				if (!(arrayreason.size() == 0)) {
					for (String s : arrayreason) {
						sb.append(s);
						sb.append(" ");
					}
				} else {
					sb.append(SkyBan.main.changesymbole(SkyBan.main.messages.getString("command-message.default-reason-ban")));
				}
				String reason = sb.toString();
				if (bannis instanceof String) {
					try {
						String reponse = SkyBan.main.checkPlayerBan("name", bannis);
						if (!(reponse == bannis)) {
							SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.FRANCE);
							String str = "5000-12-12 12:12:12";
							Date unbandate = null;
							try {unbandate = date.parse(str);} catch (ParseException e1) {e1.printStackTrace();}
							try {SkyBan.main.addBanPlayer(bannis, unbandate, sender.getName(), reason);} catch (IOException e) {e.printStackTrace();}
							if (sender instanceof ProxiedPlayer) {	
								SkyBan.main.sendBanMessage(bannis, (ProxiedPlayer) sender, reason, "5555");
							} else if (sender instanceof ConsoleCommandSender) {	
								SkyBan.main.sendBanMessageconsole(bannis, "console", reason, "5555");
							}
							SkyBan.main.changeplayerinfo(args[0], "nbban");
						} else if (reponse == bannis) {
							sender.sendMessage(new TextComponent(SkyBan.main.changesymbole(SkyBan.main.changecfgline(SkyBan.main.messages.getString("command-message.alreadyban"), "%player%", bannis))));
						}
					} catch (IOException e2) {}
				}
			}
		}
	}
}

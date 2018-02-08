package command;

import java.io.IOException;
import java.net.InetAddress;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;

import ch.canardconfit.skyban.SkyBan;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.command.ConsoleCommandSender;

public class GBanPlayer extends Command {
	
	public GBanPlayer(String name) {
		super(name, "skyban.gbanip");
	}
	
	@Override
	public void execute(CommandSender sender, String[] args) {
		if (args.length == 0) {
			sender.sendMessage(new TextComponent(SkyBan.main.changesymbole(SkyBan.main.messages.getString("utilisation-message.utilisation-gbanip"))));
		} else {
			try {
				ProxiedPlayer bannis = BungeeCord.getInstance().getPlayer(args[0]);
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
				SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.FRANCE);
				String str = "5000-12-12 12:12:12";
				Date unbandate = null;
				try {unbandate = date.parse(str);} catch (ParseException e1) {e1.printStackTrace();}
				if (bannis == null) {
					InetAddress bannisip = InetAddress.getByName(args[0]);
					if (!(args[0].equalsIgnoreCase(SkyBan.main.checkIpBan("test", bannisip)))) {
						SkyBan.main.addBanIp(bannisip, unbandate, sender.getName(), reason);
						if (sender instanceof ProxiedPlayer) {	
							SkyBan.main.sendBanIpMessage((ProxiedPlayer) sender, reason, "5555");
						} else if (sender instanceof ConsoleCommandSender) {	
							SkyBan.main.sendBanIpMessageconsole("console", reason, "5555");
						}
					} else {
						sender.sendMessage(new TextComponent(SkyBan.main.changesymbole(SkyBan.main.changecfgline(SkyBan.main.messages.getString("command-message.alreadyban"), "%player%", bannis))));
					}
				} else {
					InetAddress bannisip = bannis.getAddress().getAddress();
					if (!(SkyBan.main.checkPlayerBan("name", bannis.getName()) == args[0])) {
						SkyBan.main.addBanPlayer(bannis.getName(), unbandate, sender.getName(), reason);
						SkyBan.main.addBanIp(bannisip, unbandate, sender.getName(), reason);
						if (sender instanceof ProxiedPlayer) {	
							SkyBan.main.sendBanIpMessage((ProxiedPlayer) sender, reason, "5555");
							SkyBan.main.sendBanMessage(bannis.getName(), (ProxiedPlayer) sender, reason, "5555");
						} else if (sender instanceof ConsoleCommandSender) {	
							SkyBan.main.sendBanIpMessageconsole("console", reason, "5555");
							SkyBan.main.sendBanMessageconsole(bannis.getName(), "console", reason, "5555");
						}
						String aaa = SkyBan.main.checkplayerinfo(bannis.getName(), "nbban");
						int ccc = Integer.getInteger(aaa);
						ccc++;
						SkyBan.main.changeplayerinfo(bannis.getName(), "nbban", Integer.toString(ccc));
					} else {
						sender.sendMessage(new TextComponent(SkyBan.main.changesymbole(SkyBan.main.changecfgline(SkyBan.main.messages.getString("command-message.alreadyban"), "%player%", bannis))));
					}
				}
			} catch (IOException e) {e.printStackTrace();}
			
		}
	}

}

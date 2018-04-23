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

public class GTempBan extends Command {

	public GTempBan(String name) {
		super(name, "skyban.gtempban");
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		if (args.length == 0) {
			sender.sendMessage(new TextComponent(SkyBan.main.changesymbole(SkyBan.main.messages.getString("utilisation-message.utilisation-gtempban"))));
		} else {
			try {
				if (args.length >= 2) {
					String bannis = args[0];
					if (!(bannis == null)) {
						ArrayList<String> arrayreason = new ArrayList<String>(Arrays.asList(args));
						arrayreason.remove(0);
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
							String reponse = SkyBan.main.checkPlayerBan("name", bannis);
							if (!(reponse == bannis)) {
								Date unbandate = null;
								SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.FRANCE);
								String str = SkyBan.main.getNowDate();
								Date ab = date.parse(str);
								String numban = null;
								if (args[1].contains("s")) {
									String strnum = args[1].replace("s", "");
									int num = Integer.parseInt(strnum);
									ab.setTime(ab.getTime() + (num*1000));
									numban = String.valueOf(num) + SkyBan.main.messages.getString("time.seconds");
									unbandate = ab;
								} else if (args[1].contains("m")) {
									String strnum = args[1].replace("m", "");
									int num = Integer.parseInt(strnum);
									ab.setTime(ab.getTime() + (num*60*1000));
									numban = String.valueOf(num) + SkyBan.main.messages.getString("time.minutes");
									unbandate = ab;
								} else if (args[1].contains("h")) {
									String strnum = args[1].replace("h", "");
									int num = Integer.parseInt(strnum);
									ab.setTime(ab.getTime() + (num*60*60*1000));
									numban = String.valueOf(num) + SkyBan.main.messages.getString("time.hours");
									unbandate = ab;
								} else if (args[1].contains("d")) {
									String strnum = args[1].replace("d", "");
									int num = Integer.parseInt(strnum);
									ab.setTime(ab.getTime() + (num*60*60*1000));
									numban = String.valueOf(num) + SkyBan.main.messages.getString("time.day");
									unbandate = ab;
								} else {
									sender.sendMessage(new TextComponent(SkyBan.main.changesymbole(SkyBan.main.messages.getString("utilisation-message.utilisation-gtempban"))));
								}
								SkyBan.main.addBanPlayer(bannis, unbandate, sender.getName(), reason);
								if (sender instanceof ProxiedPlayer) {	
									SkyBan.main.sendBanMessage(bannis, (ProxiedPlayer) sender, reason, numban);
								} else if (sender instanceof ConsoleCommandSender) {	
									SkyBan.main.sendBanMessageconsole(bannis, "console", reason, numban);
								}
								SkyBan.main.changeplayerinfo(bannis, "nbban");
							} else if (reponse == bannis) {
								sender.sendMessage(new TextComponent(SkyBan.main.changesymbole(SkyBan.main.changecfgline(SkyBan.main.messages.getString("command-message.alreadyban"), "%player%", bannis))));
							}
						}
					}
				} else {
					sender.sendMessage(new TextComponent(SkyBan.main.changesymbole(SkyBan.main.messages.getString("utilisation-message.utilisation-gtempban"))));
				}
			} catch (IOException e2) {sender.sendMessage(new TextComponent(SkyBan.main.changesymbole(SkyBan.main.messages.getString("utilisation-message.utilisation-gtempban"))));
			} catch (ParseException e) {sender.sendMessage(new TextComponent(SkyBan.main.changesymbole(SkyBan.main.messages.getString("utilisation-message.utilisation-gtempban"))));
			} catch (NumberFormatException e3) {sender.sendMessage(new TextComponent(SkyBan.main.changesymbole(SkyBan.main.messages.getString("utilisation-message.utilisation-gtempban"))));}
		}
	}
}

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

public class Gmute extends Command {

	public Gmute(String name) {
		super(name, "skyban.gtempmute");
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		
		if (args.length >= 2) {
			try {
				String ifmuted = SkyBan.main.checkMute("name", args[0]);
				if (!(ifmuted == args[0])) {
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
						sb.append(SkyBan.main.changesymbole(SkyBan.main.messages.getString("command-message.default-reason-mute")));
					}
					String reason = sb.toString();
					Date unmutedate = null;
					SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.FRANCE);
					String str = SkyBan.main.getNowDate();
					Date ab = date.parse(str);
					String nummute = null;
					if (args[1].contains("s")) {
						String strnum = args[1].replace("s", "");
						int num = Integer.parseInt(strnum);
						ab.setTime(ab.getTime() + (num*1000));
						nummute = String.valueOf(num) + SkyBan.main.messages.getString("time.seconds");
						unmutedate = ab;
					} else if (args[1].contains("m")) {
						String strnum = args[1].replace("m", "");
						int num = Integer.parseInt(strnum);
						ab.setTime(ab.getTime() + (num*60*1000));
						nummute = String.valueOf(num) + SkyBan.main.messages.getString("time.minutes");
						unmutedate = ab;
					} else if (args[1].contains("h")) {
						String strnum = args[1].replace("h", "");
						int num = Integer.parseInt(strnum);
						ab.setTime(ab.getTime() + (num*60*60*1000));
						nummute = String.valueOf(num) + SkyBan.main.messages.getString("time.hours");
						unmutedate = ab;
					} else if (args[1].contains("d")) {
						String strnum = args[1].replace("d", "");
						int num = Integer.parseInt(strnum);
						ab.setTime(ab.getTime() + (num*60*60*1000));
						nummute = String.valueOf(num) + SkyBan.main.messages.getString("time.day");
						unmutedate = ab;
					} else if (args[1].equalsIgnoreCase("infinie")) {
						String strnum = "5000-12-12 12:12:12";
						unmutedate = date.parse(strnum);
						nummute = SkyBan.main.messages.getString("time.perm");
					} else {
						sender.sendMessage(new TextComponent(SkyBan.main.changesymbole(SkyBan.main.messages.getString("utilisation-message.utilisation-gmute"))));
						return;
					}
					if (nummute.equals(SkyBan.main.messages.getString("time.perm"))) {
						SkyBan.main.addMute(args[0], unmutedate, sender.getName(), reason, SkyBan.main.changesymbole(SkyBan.main.messages.getString("command-message.muteperm")));	
					} else {
						SkyBan.main.addMute(args[0], unmutedate, sender.getName(), reason, nummute);	
					}
					if (sender instanceof ProxiedPlayer) {	
						SkyBan.main.sendMuteMessage(args[0], sender.getName(), reason, nummute);
					} else if (sender instanceof ConsoleCommandSender) {	
						SkyBan.main.sendMuteMessageconsole(args[0], "console", reason, nummute);
					}
					SkyBan.main.changeplayerinfo(args[0], "nbmute");
				}
			} catch (IOException e) {sender.sendMessage(new TextComponent(SkyBan.main.changesymbole(SkyBan.main.messages.getString("utilisation-message.utilisation-gmute"))));
			} catch (ParseException e) {sender.sendMessage(new TextComponent(SkyBan.main.changesymbole(SkyBan.main.messages.getString("utilisation-message.utilisation-gmute"))));
			} catch (NumberFormatException e) {sender.sendMessage(new TextComponent(SkyBan.main.changesymbole(SkyBan.main.messages.getString("utilisation-message.utilisation-gmute"))));}
		} else {
			sender.sendMessage(new TextComponent(SkyBan.main.changesymbole(SkyBan.main.messages.getString("utilisation-message.utilisation-gmute"))));
		}
	}

}

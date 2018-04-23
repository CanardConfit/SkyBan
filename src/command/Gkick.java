package command;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import ch.canardconfit.skyban.SkyBan;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.command.ConsoleCommandSender;

public class Gkick extends Command {

	public Gkick(String name) {
		super(name, "skyban.gkick");
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		if (args.length >= 1) {
			ProxiedPlayer akick = BungeeCord.getInstance().getPlayer(args[0]);
			
			if (akick != null) {
				
				ArrayList<String> arrayreason = new ArrayList<String>(Arrays.asList(args));
				arrayreason.remove(0);
				StringBuilder sb = new StringBuilder();
				if (!(arrayreason.size() == 0)) {
					for (String s : arrayreason) {
						sb.append(s);
						sb.append(" ");
					}
				} else {
					sb.append(SkyBan.main.changesymbole(SkyBan.main.messages.getString("command-message.default-reason-kick")));
				}
				String reason = sb.toString();
				
				
				if (sender instanceof ProxiedPlayer) {
					String repbase = SkyBan.main.changesymbole(SkyBan.main.messages.getString("broadcast-message.kick-message"));
					repbase = SkyBan.main.changesymbole(SkyBan.main.changecfgline(repbase, "%kickedplayer%", args[0]));
					repbase = SkyBan.main.changesymbole(SkyBan.main.changecfgline(repbase, "%kickby%", sender.getName()));
					repbase = SkyBan.main.changesymbole(SkyBan.main.changecfgline(repbase, "%reason%", reason));
					repbase = SkyBan.main.changesymbole(repbase);
					BungeeCord.getInstance().broadcast(new TextComponent(repbase));
				} else if (sender instanceof ConsoleCommandSender) {
					String repbase = SkyBan.main.messages.getString("broadcast-message.kick-message-console");
					repbase = SkyBan.main.changesymbole(SkyBan.main.changecfgline(repbase, "%kickedplayer%", args[0]));
					repbase = SkyBan.main.changesymbole(SkyBan.main.changecfgline(repbase, "%console%", "console"));
					repbase = SkyBan.main.changesymbole(SkyBan.main.changecfgline(repbase, "%reason%", reason));
					BungeeCord.getInstance().broadcast(new TextComponent(repbase));
				}
				String repbase = SkyBan.main.changesymbole(SkyBan.main.messages.getString("kick-message.kick-message"));
				repbase = SkyBan.main.changesymbole(SkyBan.main.changecfgline(repbase, "%kickby%", sender.getName()));
				repbase = SkyBan.main.changesymbole(SkyBan.main.changecfgline(repbase, "%reason%", reason));
				repbase = SkyBan.main.changesymbole(SkyBan.main.changesymbole(repbase));
				akick.disconnect(new TextComponent(repbase));
				try {
					SkyBan.main.changeplayerinfo(akick.getName(), "nbkick");
				} catch (IOException e) {
					e.printStackTrace();
				}
				
			} else {
				String repbase = SkyBan.main.messages.getString("kick-message.no-connected");
				repbase = SkyBan.main.changesymbole(SkyBan.main.changecfgline(repbase, "%player%", args[0]));
				repbase = SkyBan.main.changesymbole(SkyBan.main.changesymbole(repbase));
				sender.sendMessage(new TextComponent(repbase));
			}
		} else {
			String repbase = SkyBan.main.changesymbole(SkyBan.main.messages.getString("utilisation-message.utilisation-gkick"));
			sender.sendMessage(new TextComponent(repbase));
		}
	}

}

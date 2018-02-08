package Listener;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import ch.canardconfit.skyban.SkyBan;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class ChatPlayerListener implements Listener {

	public ChatPlayerListener(SkyBan skyBan) {
	}
	
	@EventHandler
	public void OnChat(ChatEvent event) {
		if (event.isCommand() == false) {
			ProxiedPlayer player = (ProxiedPlayer) event.getSender();
			
			try {
				
				String ifmuted = SkyBan.main.checkMute("name", player.getName());
				
				if (player.getName().equals(ifmuted)) {

					Date unban = SkyBan.main.checkdatemute("unbandate", player.getName());
					if (!(unban.equals(new Date()) || unban.before(new Date()))) {
						String muteby = SkyBan.main.checkMute("mystaff", player.getName());
						
						String reason = SkyBan.main.checkMute("reason", player.getName());
						
						Date unmutedat = SkyBan.main.checkdatemute("unbandate", player.getName());
						SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.FRANCE);
						String unmutedate = date.format(unmutedat);
						if (unmutedate.equals("5000-12-12 12:12:12")) {unmutedate = SkyBan.main.messages.getString("mute-message.perm-mute");}
						
						String a = SkyBan.main.messages.getString("mute-message.speak-on-muted");
						a = a.replace("%muteby%", muteby);
						a = a.replace("%reason%", reason);
						a = a.replace("%unmutedate%", unmutedate);
						a = a.replaceAll("&", "§");
						
						player.sendMessage(new TextComponent(a));
						
						for (ProxiedPlayer p : SkyBan.main.listStaff) {
							
							String b = SkyBan.main.messages.getString("mute-message.msg-on-mute-to-staff");
							b = b.replace("%mute%", player.getName());
							b = b.replace("%message%", event.getMessage());
							b = b.replaceAll("&", "§");
							
							p.sendMessage(new TextComponent(b));
							
						}
						
						String b = SkyBan.main.messages.getString("mute-message.msg-on-mute-to-console");
						b = b.replace("%mute%", player.getName());
						b = b.replace("%message%", event.getMessage());
						
						System.out.println(b);
						event.setCancelled(true);
					} else {
						
						String b = SkyBan.main.messages.getString("mute-message.mute-expire-console");
						b = b.replace("%mute%", player.getName());
						
						System.out.println(b);
						SkyBan.main.removemute(player.getName());
					}
				}
			} catch (IOException e) {e.printStackTrace();}
		}
	}
}

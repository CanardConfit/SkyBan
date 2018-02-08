package Listener;

import java.io.IOException;
import java.net.InetAddress;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import ch.canardconfit.skyban.SkyBan;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class JoinPlayerListener implements Listener {
	
	public JoinPlayerListener(SkyBan skyban) {
	}
	@EventHandler
	public void onJoin(ServerConnectEvent event) {
		ProxiedPlayer player = event.getPlayer();
		InetAddress ip = event.getPlayer().getAddress().getAddress();
		if (player instanceof ProxiedPlayer) {
				try {
					String repip = SkyBan.main.checkIpBan("test", ip);
					String ipstring = ip.toString();
					if (!(ipstring.equals(repip))) {
						String reponse = SkyBan.main.checkPlayerBan("name", player.getName());
						if ((player.getName()).equals(reponse)) {
							Date unban = SkyBan.main.checkdateban("unbandate", player.getName());
							if (!(unban.equals(new Date()) || unban.before(new Date()))) {
								String banby = SkyBan.main.checkPlayerBan("mystaff", player.getName());
								String reason = SkyBan.main.checkPlayerBan("reason", player.getName());
								SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.FRANCE);
								String str = "5000-12-12 12:12:12";
								Date d = null;
								try {
									d = date.parse(str);
								} catch (ParseException e) {e.printStackTrace();}
								String strdate = date.format(unban);
								if (!(unban.equals(d))) {
									String asdr = SkyBan.main.changesymbole(SkyBan.main.messages.getString("ban-message.tempban-message"));
									asdr = asdr.replace("%banby%", banby);
									asdr = asdr.replace("%reason%", reason);
									asdr = asdr.replace("%unbandate%", strdate);
									event.setCancelled(true);
									player.disconnect(new TextComponent(asdr));
								} else {
									String asdr = SkyBan.main.changesymbole(SkyBan.main.messages.getString("ban-message.permban-message"));
									asdr = asdr.replace("%banby%", banby);
									asdr = asdr.replace("%reason%", reason);
									event.setCancelled(true);
									player.disconnect(new TextComponent(asdr));
								}
							} else {
								SkyBan.main.removebanplayer(player.getName());
								String asdr = SkyBan.main.changesymbole(SkyBan.main.messages.getString("ban-message.ban-expire-console"));
								asdr = asdr.replace("%bannedplayer%", player.getName());
							    System.out.println(asdr);
							}	
						}
					} else {
						Date unban = SkyBan.main.checkdatebanip("unbandate", ip);
						if (!(unban.equals(new Date()) || unban.before(new Date()))) {
							String banby = SkyBan.main.checkIpBan("mystaff", ip);
							String reason = SkyBan.main.checkIpBan("reason", ip);
							SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.FRANCE);
							String str = "5000-12-12 12:12:12";
							Date d = null;
							try {
								d = date.parse(str);
							} catch (ParseException e) {e.printStackTrace();}
							String strdate = date.format(unban);
							if (!(unban.equals(d))) {
								String asdr = SkyBan.main.changesymbole(SkyBan.main.messages.getString("ban-message.tempban-message"));
								asdr = asdr.replace("%banby%", banby);
								asdr = asdr.replace("%reason%", reason);
								asdr = asdr.replace("%unbandate%", strdate);
								event.setCancelled(true);
								player.disconnect(new TextComponent(asdr));
							} else {
								String asdr = SkyBan.main.changesymbole(SkyBan.main.messages.getString("ban-message.permban-message"));
								asdr = asdr.replace("%banby%", banby);
								asdr = asdr.replace("%reason%", reason);
								event.setCancelled(true);
								player.disconnect(new TextComponent(asdr));
							}
						} else {
							SkyBan.main.removebanip(ip);
							String asdr = SkyBan.main.changesymbole(SkyBan.main.messages.getString("ban-message.ban-expire-console"));
							asdr = asdr.replace("%bannedplayer%", player.getName());
						    System.out.println(asdr);
						}
					}
				} catch (IOException e) {e.printStackTrace();}
			
		}
	}	
}


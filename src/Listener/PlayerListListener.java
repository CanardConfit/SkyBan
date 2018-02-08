package Listener;

import ch.canardconfit.skyban.SkyBan;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class PlayerListListener implements Listener {

	private SkyBan main;
	
	public PlayerListListener(SkyBan skyBan) {
		this.main = skyBan;
	}
	@EventHandler
	public void onJoin(ServerConnectEvent event) {
		ProxiedPlayer player = event.getPlayer();
		if (player.hasPermission("skyban.staff")) {
			main.listStaff.add(player);
		}
		main.listallplayer.add(player);
	}
	@EventHandler
	public void onQuit(PlayerDisconnectEvent event) {
		ProxiedPlayer player = event.getPlayer();
		if (player.hasPermission("skyban.staff")) {
			main.listStaff.remove(player);
		}
		main.listallplayer.remove(player);
	}
}

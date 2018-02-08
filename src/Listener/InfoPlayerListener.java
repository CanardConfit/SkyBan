package Listener;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import ch.canardconfit.skyban.SkyBan;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class InfoPlayerListener implements Listener {

	public InfoPlayerListener(SkyBan skyBan) {
	}
	
	@EventHandler
	public void onFirstJoin(ServerConnectEvent event) throws IOException {
		File dataFile = new File("plugins/SkyBan/playerinfo/" + event.getPlayer().getName() + ".txt");
		if (dataFile.createNewFile()) {
			File tempFile = new File(dataFile.getAbsolutePath() + ".tmp");
			PrintWriter pw = new PrintWriter(new FileWriter(tempFile));
			pw.println("Pseudo du joueur;" + event.getPlayer().getName());
			pw.println("IP du joueur;" + event.getPlayer().getAddress().getAddress());
			pw.println("Nombre de Ban du joueur;0");
			pw.println("Nombre de Mute du joueur;0");
			pw.println("Premiere connexion;" + SkyBan.main.getNowDate());
			pw.println("Derniere connexion;" + SkyBan.main.getNowDate());
			pw.close();
			if (!dataFile.delete()) {
				System.out.println(SkyBan.main.messages.getString("error-message.add-playerinfo-delete-file"));
				return;
			}
			if (!tempFile.renameTo(dataFile)) {
				System.out.println(SkyBan.main.messages.getString("error-message.add-playerinfo-rename-file"));
			}
		} else {
			SkyBan.main.changeplayerinfo(event.getPlayer().getName(), "dco", SkyBan.main.getNowDate());
		}
	}

}

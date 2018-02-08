package ch.canardconfit.skyban;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Pattern;

import java.nio.file.Files;

import Listener.ChatPlayerListener;
import Listener.InfoPlayerListener;
import Listener.JoinPlayerListener;
import Listener.PlayerListListener;
import command.GBan;
import command.GBanPlayer;
import command.GTempBan;
import command.GTempBanPlayer;
import command.GUnban;
import command.GUnbanPlayer;
import command.Gmute;
import command.Gunmute;
import command.HelpCommand;
import command.Lookup;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

public class SkyBan extends Plugin {
	
	
	public ArrayList<ProxiedPlayer> listStaff = new ArrayList<ProxiedPlayer>();
	public ArrayList<ProxiedPlayer> listallplayer = new ArrayList<ProxiedPlayer>();
	
	public static SkyBan main;
	
	private File configFile;
	private File messageFile;
	private File banFile;
	private File muteFile;
	private File banipFile;
	
	public Configuration config;
	public Configuration messages;
	
	@Override
	public void onEnable() {
		
		loadFile();
		
		main = this;
		
		System.out.println(messages.getString("start-message.start"));
		PluginManager pm = getProxy().getPluginManager();
		
		if (config.getBoolean("ban-activate") == true) {
			pm.registerCommand(this, new GBan("gban"));
			pm.registerCommand(this, new GBanPlayer("gbanip"));
			pm.registerCommand(this, new GTempBan("gtempban"));
			pm.registerCommand(this, new GTempBanPlayer("gtempbanip"));
			pm.registerCommand(this, new GUnban("gunban"));
			pm.registerCommand(this, new GUnbanPlayer("gunbanip"));
		}
		if (config.getBoolean("mute-activate") == true) {
			pm.registerCommand(this, new Gmute("gtempmute"));
			pm.registerCommand(this, new Gunmute("gunmute"));
		}
		pm.registerCommand(this, new Lookup("lookup"));
		pm.registerCommand(this, new HelpCommand("skyban"));
		
		pm.registerListener(this, new ChatPlayerListener(this));
		pm.registerListener(this, new JoinPlayerListener(this));
		pm.registerListener(this, new PlayerListListener(this));
		pm.registerListener(this, new InfoPlayerListener(this));
	}
	@Override
	public void onDisable() {
		
		main = null;
		
		System.out.println(messages.getString("start-message.stop"));
	}
	
	public void loadFile() {
		try {
			configFile = new File(getDataFolder().getPath(), "config.yml");
			messageFile = new File(getDataFolder().getPath(), "messages.yml");
			banFile = new File(getDataFolder().getPath(), "banned-player.txt");
			muteFile = new File(getDataFolder().getPath(), "mute-player.txt");
			banipFile = new File(getDataFolder().getPath(), "banned-ips.txt");
			
			if (!getDataFolder().exists()) {
				getDataFolder().mkdirs();
			}
			File dir = new File(getDataFolder().getPath(), "playerinfo");
			if (!dir.exists()) {
				dir.mkdirs();
			}
	        if (!configFile.exists()) {
	        	File file = new File(getDataFolder(), "config.yml");
				Files.copy(getResourceAsStream("resource/FRANCAIS/config.yml"), file.toPath());
	        }
			if (!messageFile.exists()) {
	        	File file = new File(getDataFolder(), "messages.yml");
				Files.copy(getResourceAsStream("resource/FRANCAIS/messages.yml"), file.toPath());
			}
			if (!banFile.exists()) {
	        	File file = new File(getDataFolder(), "banned-player.txt");
				Files.copy(getResourceAsStream("resource/banned-player.txt"), file.toPath());
			}
			if (!banipFile.exists()) {
	        	File file = new File(getDataFolder(), "banned-ips.txt");
				Files.copy(getResourceAsStream("resource/banned-ips.txt"), file.toPath());
			}
			if (!muteFile.exists()) {
	        	File file = new File(getDataFolder(), "mute-player.txt");
				Files.copy(getResourceAsStream("resource/mute-player.txt"), file.toPath());
			}
			
			config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(configFile);
            messages = ConfigurationProvider.getProvider(YamlConfiguration.class).load(messageFile);
            
            if (config.getString("lang").equals("ENG")) {
            	if (config.getBoolean("changeonstart") == true) {
	            	configFile.delete();
		        	File file = new File(getDataFolder(), "config.yml");
					Files.copy(getResourceAsStream("resource/ANGLAIS/config.yml"), file.toPath());
					
					messageFile.delete();
		        	File file2 = new File(getDataFolder(), "messages.yml");
					Files.copy(getResourceAsStream("resource/ANGLAIS/messages.yml"), file2.toPath());
	            	System.out.println("SkyBan >> You have selectionned ENGLISH version ! The old config was deleted !");
            	}
            }
            if (config.getString("lang").equals("FR")) {
            	if (config.getBoolean("changeonstart") == true) {
	            	configFile.delete();
		        	File file = new File(getDataFolder(), "config.yml");
					Files.copy(getResourceAsStream("resource/FRANCAIS/config.yml"), file.toPath());
					
					messageFile.delete();
		        	File file2 = new File(getDataFolder(), "messages.yml");
					Files.copy(getResourceAsStream("resource/FRANCAIS/messages.yml"), file2.toPath());
	            	System.out.println("SkyBan >> Vous avez selectionner la version francaise ! l'ancienne configuration a ete supprimer !");
           
            	}
            }
            config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(configFile);
            messages = ConfigurationProvider.getProvider(YamlConfiguration.class).load(messageFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public final String changecfgline(String line, String patern, Object change) {
		if (line.contains(patern)) {
			line = line.replace(patern, (CharSequence) change);
			
			return line;
		}
		return null;
		
	}
	public final String changesymbole(String line) {
		line = line.replaceAll("&", "§");
		return line;
	}
	public final String checkPlayerBan(String label, String player) throws IOException {
		File dataFile = new File("plugins/SkyBan/banned-player.txt");
		BufferedReader br = new BufferedReader(new FileReader(dataFile));
		String line;
		String rep = null;
		while ((line = br.readLine()) != null) {
			String[] part = line.split("\\|");
			String name = part[0];
			String mystaff = part[2];
			String reason = part[4];
			if (label.equalsIgnoreCase("name")) {
				if (player.equals(name)) {
					rep = name;
				}
			} else if (label.equalsIgnoreCase("mystaff")) {
				if (player.equals(name)) {
					rep = mystaff;
				}
			} else if (label.equalsIgnoreCase("reason")) {
				if (player.equals(name)) {
					rep = reason;
				}
			}
		}
		br.close();
		return rep;
	}
	public final String checkIpBan(String label, InetAddress ipplayer) throws IOException {
		File dataFile = new File("plugins/SkyBan/banned-ips.txt");
		BufferedReader br = new BufferedReader(new FileReader(dataFile));
		String line;
		String rep = null;
		String ip = ipplayer.toString();
		while ((line = br.readLine()) != null) {
			String[] part = line.split("\\|");
			String name = part[0];
			String mystaff = part[2];
			String reason = part[4];
			if (label.equalsIgnoreCase("test")) {
				if (name.equals(ip)) {
					rep = name;
				}
			} else if (label.equalsIgnoreCase("mystaff")) {
				if (name.equals(ip)) {
					rep = mystaff;
				}
			} else if (label.equalsIgnoreCase("reason")) {
				if (name.equals(ip)) {
					rep = reason;
				}
			}
		}
		br.close();
		return rep;
	}
	public final Date checkdateban(String label, String player) throws IOException {
		File dataFile = new File("plugins/SkyBan/banned-player.txt");
		BufferedReader br = new BufferedReader(new FileReader(dataFile));
		String line;
		Date rep = null;
		Date d = null;
		while ((line = br.readLine()) != null) {
			String[] part = line.split("\\|");
			String name = part[0];
			String unbandate = part[3];
			String datebanned = part[1];
			if (label.equalsIgnoreCase("unbandate")) {
				SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.FRANCE);
				try {
					d = date.parse(unbandate);
				} catch (ParseException e) {e.printStackTrace();}
				if (player.equals(name)) {rep = d;}
			} else if (label.equalsIgnoreCase("datebanned")) {
				SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.FRANCE);
				try {
					d = date.parse(datebanned);
				} catch (ParseException e) {e.printStackTrace();}
				if (player.equals(name)) {rep = d;}
			}
			
		}
		br.close();
		return rep;
		
	}
	public final Date checkdatebanip(String label, InetAddress player) throws IOException {
		File dataFile = new File("plugins/SkyBan/banned-ips.txt");
		BufferedReader br = new BufferedReader(new FileReader(dataFile));
		String line;
		Date rep = null;
		Date d = null;
		while ((line = br.readLine()) != null) {
			String[] part = line.split("\\|");
			String name = part[0];
			String unbandate = part[3];
			String datebanned = part[1];
			SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.FRANCE);
			String strplayer = player.toString();
			try {
				if (label.equalsIgnoreCase("unbandate")) {
					d = date.parse(unbandate);
					if (strplayer.equals(name)) {rep = d;}
				} else if (label.equalsIgnoreCase("datebanned")) {
					d = date.parse(datebanned);
					if (strplayer.equals(name)) {rep = d;}
				}
			} catch (ParseException e) {e.printStackTrace();}
			
		}
		br.close();
		return rep;
		
	}
	public final void removebanplayer(String player) throws IOException {
		File dataFile = new File("plugins/SkyBan/banned-player.txt");
		File tempFile = new File(dataFile.getAbsolutePath() + ".tmp");
		BufferedReader br = new BufferedReader(new FileReader(dataFile));
		PrintWriter pw = new PrintWriter(new FileWriter(tempFile));
		String line;
		while ((line = br.readLine()) != null) {
			String[] part = line.split("\\|");
			String name = part[0];
			if (!(player.equals(name))) {
				pw.println(line);
			}
		}
		br.close();
		pw.close();
		if (!dataFile.delete()) {
			System.out.println(messages.getString("error-message.remove-ban-delete-file"));
			return;
		}
		if (!tempFile.renameTo(dataFile)) {
			System.out.println(messages.getString("error-message.remove-ban-rename-file"));
		}
	}
	public final void removebanip(InetAddress ip) throws IOException {
		File dataFile = new File("plugins/SkyBan/banned-ips.txt");
		File tempFile = new File(dataFile.getAbsolutePath() + ".tmp");
		BufferedReader br = new BufferedReader(new FileReader(dataFile));
		PrintWriter pw = new PrintWriter(new FileWriter(tempFile));
		String line;
		while ((line = br.readLine()) != null) {
			String[] part = line.split("\\|");
			String name = part[0];
			if (!(ip.toString().equals(name))) {
				pw.println(line);
			}
		}
		br.close();
		pw.close();
		if (!dataFile.delete()) {
			System.out.println(messages.getString("error-message.remove-ban-delete-file"));
			return;
		}
		if (!tempFile.renameTo(dataFile)) {
			System.out.println(messages.getString("error-message.remove-ban-rename-file"));
			return;
		}
	}
	public final void addBanPlayer(String player, Date unbandate, String banby, String reason) throws IOException {
		File dataFile = new File("plugins/SkyBan/banned-player.txt");
		File tempFile = new File(dataFile.getAbsolutePath() + ".tmp");
		BufferedReader br = new BufferedReader(new FileReader(dataFile));
		PrintWriter pw = new PrintWriter(new FileWriter(tempFile));
		String line;
		String bannameplayer = player;
		String nowdate = getNowDate();
		String unban = null;
		SimpleDateFormat a = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.FRANCE);
		unban = a.format(unbandate);
		String res = reason;
		String banline = (bannameplayer + "|" + nowdate + "|" + banby + "|" + unban + "|" + res);
		while ((line = br.readLine()) != null) {
			pw.println(line);
		}
		pw.print(banline);
		br.close();
		pw.close();
		if (!dataFile.delete()) {
			System.out.println(messages.getString("error-message.add-ban-delete-file"));
			return;
		}
		if (!tempFile.renameTo(dataFile)) {
			System.out.println(messages.getString("error-message.add-ban-rename-file"));
			return;
		}
		String str = "5000-12-12 12:12:12";
		Date d = null;
		try {
			d = a.parse(str);
		} catch (ParseException e) {e.printStackTrace();}
		
		ProxiedPlayer p;
		
		if ((p = BungeeCord.getInstance().getPlayer(player)) != null) {
			
			p = BungeeCord.getInstance().getPlayer(player);
			
			if (!(unbandate.equals(d))) { 
				String asdr = SkyBan.main.changesymbole(messages.getString("ban-message.tempban-message"));
				asdr = asdr.replace("%banby%", banby);
				asdr = asdr.replace("%reason%", res);
				asdr = asdr.replace("%unbandate%", unban);
				p.disconnect(new TextComponent(asdr));
			} else {
				String asdr = SkyBan.main.changesymbole(messages.getString("ban-message.permban-message"));
				asdr = asdr.replace("%banby%", banby);
				asdr = asdr.replace("%reason%", res);
				p.disconnect(new TextComponent(asdr));
			}
		}
	}
	public final void addBanIp(InetAddress ip, Date unbandate, String banby, String reason) throws IOException {
		File dataFile = new File("plugins/SkyBan/banned-ips.txt");
		File tempFile = new File(dataFile.getAbsolutePath() + ".tmp");
		BufferedReader br = new BufferedReader(new FileReader(dataFile));
		PrintWriter pw = new PrintWriter(new FileWriter(tempFile));
		String line;
		String banip = ip.toString();
		String nowdate = getNowDate();
		String unban = null;
		SimpleDateFormat a = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.FRANCE);
		unban = a.format(unbandate);
		String res = reason;
		String banline = (banip + "|" + nowdate + "|" + banby + "|" + unban + "|" + res);
		while ((line = br.readLine()) != null) {
			pw.println(line);
		}
		pw.print(banline);
		br.close();
		pw.close();
		if (!dataFile.delete()) {
			System.out.println(messages.getString("error-message.add-ban-delete-file"));
			return;
		}
		if (!tempFile.renameTo(dataFile)) {
			System.out.println(messages.getString("error-message.add-ban-rename-file"));
		}
		String str = "5000-12-12 12:12:12";
		Date d = null;
		try {
			d = a.parse(str);
		} catch (ParseException e) {e.printStackTrace();}
		if (!(unbandate.equals(d))) { 
			for (ProxiedPlayer p : BungeeCord.getInstance().getPlayers()) {
				if (p.getAddress().getAddress() == ip) {
					String asdr = SkyBan.main.changesymbole(messages.getString("ban-message.tempban-message"));
					asdr.replace("%banby%", banby);
					asdr.replace("%reason%", res);
					asdr.replace("%unbandate%", unban);
					p.disconnect(new TextComponent(asdr));
				}
			}
		} else {
			for (ProxiedPlayer p : BungeeCord.getInstance().getPlayers()) {
				if (p.getAddress().getAddress() == ip) {
					String asdr = SkyBan.main.changesymbole(messages.getString("ban-message.permban-message"));
					asdr.replace("%banby%", banby);
					asdr.replace("%reason%", res);
					p.disconnect(new TextComponent(asdr));
				}
			}
		}
	}
	public final String getNowDate() {
		SimpleDateFormat datenow = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.FRANCE);
		Date now = new Date();
		String strDate = datenow.format(now);
		return strDate;
	}
	public final void sendBanMessage(String bannedplayer, ProxiedPlayer banby, String reason, String numberday) {
		if (!(numberday == "5555")) {
			String asdr = SkyBan.main.changesymbole(messages.getString("broadcast-message.tempban-message"));
			asdr = asdr.replace("%bannedplayer%", bannedplayer);
			asdr = asdr.replace("%banby%", banby.getName());
			asdr = asdr.replace("%reason%", reason);
			asdr = asdr.replace("%bantime%", numberday);
			ProxyServer.getInstance().broadcast(new TextComponent(asdr));
		} else {
			String asdr = SkyBan.main.changesymbole(messages.getString("broadcast-message.permban-message"));
			asdr = asdr.replace("%bannedplayer%", bannedplayer);
			asdr = asdr.replace("%banby%", banby.getName());
			asdr = asdr.replace("%reason%", reason);
			ProxyServer.getInstance().broadcast(new TextComponent(asdr));
		}
	}
	public final void sendUnBanMessage(String bannedplayer, ProxiedPlayer unbanby) {
		String asdr = SkyBan.main.changesymbole(messages.getString("broadcast-message.unban-message"));
		asdr = asdr.replace("%bannedplayer%", bannedplayer);
		asdr = asdr.replace("%unbanby%", unbanby.getName());
		ProxyServer.getInstance().broadcast(new TextComponent(asdr));
	}
	public final void sendUnBanIpMessage(InetAddress ip, ProxiedPlayer unbanby) {
		String asdr = SkyBan.main.changesymbole(messages.getString("broadcast-message.unbanip-message"));
		asdr = asdr.replace("%unbanby%", unbanby.getName());
		ProxyServer.getInstance().broadcast(new TextComponent(asdr));
	}
	public final void sendBanIpMessage(ProxiedPlayer banby, String reason, String numberday) {
		if (!(numberday == "5555")) {
			String asdr = SkyBan.main.changesymbole(messages.getString("broadcast-message.tempbanip-message"));
			asdr = asdr.replace("%banby%", banby.getName());
			asdr = asdr.replace("%reason%", reason);
			asdr = asdr.replace("%bantime%", numberday);
			ProxyServer.getInstance().broadcast(new TextComponent(asdr));
		} else {
			String asdr = SkyBan.main.changesymbole(messages.getString("broadcast-message.permbanip-message"));
			asdr = asdr.replace("%banby%", banby.getName());
			asdr = asdr.replace("%reason%", reason);
			ProxyServer.getInstance().broadcast(new TextComponent(asdr));
		}
	}
	public final void sendBanMessageconsole(String bannedplayer, String console, String reason, String numberday) {
		if (!(numberday == "5555")) {
			String asdr = SkyBan.main.changesymbole(messages.getString("broadcast-message.tempban-message-console"));
			asdr = asdr.replace("%bannedplayer%", bannedplayer);
			asdr = asdr.replace("%console%", console);
			asdr = asdr.replace("%reason%", reason);
			asdr = asdr.replace("%bantime%", numberday);
			ProxyServer.getInstance().broadcast(new TextComponent(asdr));
		} else {
			String asdr = SkyBan.main.changesymbole(messages.getString("broadcast-message.permban-message-console"));
			asdr = asdr.replace("%bannedplayer%", bannedplayer);
			asdr = asdr.replace("%console%", console);
			asdr = asdr.replace("%reason%", reason);
			ProxyServer.getInstance().broadcast(new TextComponent(asdr));
		}
	}
	public final void sendUnBanMessageconsole(String bannedplayer, String console) {
		String asdr = SkyBan.main.changesymbole(messages.getString("broadcast-message.unban-message-console"));
		asdr = asdr.replace("%bannedplayer%", bannedplayer);
		asdr = asdr.replace("%console%", console);
		ProxyServer.getInstance().broadcast(new TextComponent(asdr));
	}
	public final void sendUnBanIpMessageconsole(InetAddress ip, String console) {
		String asdr = SkyBan.main.changesymbole(messages.getString("broadcast-message.unbanip-message-console"));
		asdr = asdr.replace("%console%", console);
		ProxyServer.getInstance().broadcast(new TextComponent(asdr));
	}
	public final void sendBanIpMessageconsole(String console, String reason, String numberday) {
		if (!(numberday == "5555")) {
			String asdr = SkyBan.main.changesymbole(messages.getString("broadcast-message.tempbanip-message-console"));
			asdr = asdr.replace("%console%", console);
			asdr = asdr.replace("%reason%", reason);
			asdr = asdr.replace("%bantime%", numberday);
			ProxyServer.getInstance().broadcast(new TextComponent(asdr));
		} else {
			String asdr = SkyBan.main.changesymbole(messages.getString("broadcast-message.permbanip-message-console"));
			asdr = asdr.replace("%console%", console);
			asdr = asdr.replace("%reason%", reason);
			ProxyServer.getInstance().broadcast(new TextComponent(asdr));
		}
	}
	public final void changeplayerinfo(String player, String i, String value) throws IOException {
		String id = null;
		if (i.equalsIgnoreCase("pseudo")) {
			id = "Pseudo du joueur";
		} else if (i.equalsIgnoreCase("ip")) {
			id = "IP du joueur";
		} else if (i.equalsIgnoreCase("nbban")) {
			id = "Nombre de Ban du joueur";
		} else if (i.equalsIgnoreCase("nbmute")) {
			id = "Nombre de Mute du joueur";
		} else if (i.equalsIgnoreCase("dco")) {
			id = "Derniere connexion";
		}
		File dataFile = new File("plugins/SkyBan/playerinfo/" + player + ".txt");
		File tempFile = new File(dataFile.getAbsolutePath() + ".tmp");
		BufferedReader br = new BufferedReader(new FileReader(dataFile));
		PrintWriter pw = new PrintWriter(new FileWriter(tempFile));
		String line;
		while ((line = br.readLine()) != null) {
			String[] part = line.split(";");
			String idline = part[0];
			if (id.equals(idline)) {
				pw.println(idline + ";" + value);
			} else {
				pw.println(line);
			}
		}
		
		pw.close();
		br.close();
		if (!dataFile.delete()) {
			System.out.println(messages.getString("error-message.change-playerinfo-delete-file"));
			return;
		}
		if (!tempFile.renameTo(dataFile)) {
			System.out.println(messages.getString("error-message.change-playerinfo-rename-file"));
		}
	}
	public final String checkplayerinfo(String player, String i) throws IOException {
		String rep = null;
		String id = null;
		if (i.equalsIgnoreCase("pseudo")) {
			id = "Pseudo du joueur";
		} else if (i.equalsIgnoreCase("ip")) {
			id = "IP du joueur";
		} else if (i.equalsIgnoreCase("nbban")) {
			id = "Nombre de Ban du joueur";
		} else if (i.equalsIgnoreCase("nbmute")) {
			id = "Nombre de Mute du joueur";
		} else if (i.equalsIgnoreCase("dco")) {
			id = "Derniere connexion";
		} else if (i.equalsIgnoreCase("pco")) {
			id = "Premiere connexion";
		} else if (i.equalsIgnoreCase("file")) {
			File dataFile = new File("plugins/SkyBan/playerinfo/" + player + ".txt");
			if (!(dataFile.exists())) {
				return rep;
			} else {
				rep = "exist";
				return rep;
			}
		}
		File dataFile = new File("plugins/SkyBan/playerinfo/" + player + ".txt");
		BufferedReader br = new BufferedReader(new FileReader(dataFile));
		String line;
		while ((line = br.readLine()) != null) {
			String[] part = line.split(";");
			String idline = part[0];
			String valueline = part[1];
			if (id.equals(idline)) {
				rep = valueline;
			}
		}
		
		br.close();
		return rep;
	}
	
	public final Boolean checkInetAddress(String IP) {
		String ip1 = IP;
		if (ip1.split(".") != null) {
			String[] part = IP.split(Pattern.quote("."));
			
			if (part.length == 4) {
				
				if (part[0] == null) {return false;}
				if (part[1] == null) {return false;}
				if (part[2] == null) {return false;}
				if (part[3] == null) {return false;}
				
				if ((isInt(part[0])) == false) {return false;}
				if ((isInt(part[0])) == false) {return false;}
				if ((isInt(part[0])) == false) {return false;}
				if ((isInt(part[0])) == false) {return false;}
				
			} else {return false;}
		} else {return false;}
		return true;
	}
	
	public static boolean isInt(String str) {  
		try {
			  Double.parseDouble(str);
		} catch(NumberFormatException nfe) {return false;}
		return true;
	}
	
	public final String checkMute(String label, String player) throws IOException {
		File dataFile = new File("plugins/SkyBan/mute-player.txt");
		BufferedReader br = new BufferedReader(new FileReader(dataFile));
		String line;
		String rep = null;
		while ((line = br.readLine()) != null) {
			String[] part = line.split("\\|");
			String name = part[0];
			String mystaff = part[2];
			String reason = part[4];
			if (label.equalsIgnoreCase("name")) {
				if (player.equals(name)) {
					rep = name;
				}
			} else if (label.equalsIgnoreCase("mystaff")) {
				if (player.equals(name)) {
					rep = mystaff;
				}
			} else if (label.equalsIgnoreCase("reason")) {
				if (player.equals(name)) {
					rep = reason;
				}
			}
		}
		br.close();
		return rep;
	}
	public final void addMute(String player, Date unmutedate, String banby, String reason, String nummute) throws IOException {
		File dataFile = new File("plugins/SkyBan/mute-player.txt");
		File tempFile = new File(dataFile.getAbsolutePath() + ".tmp");
		BufferedReader br = new BufferedReader(new FileReader(dataFile));
		PrintWriter pw = new PrintWriter(new FileWriter(tempFile));
		String line;
		String bannameplayer = player;
		String nowdate = getNowDate();
		String unban = null;
		SimpleDateFormat a = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.FRANCE);
		unban = a.format(unmutedate);
		String res = reason;
		String banline = (bannameplayer + "|" + nowdate + "|" + banby + "|" + unban + "|" + res);
		while ((line = br.readLine()) != null) {
			pw.println(line);
		}
		pw.print(banline);
		br.close();
		pw.close();
		if (!dataFile.delete()) {
			System.out.println(messages.getString("error-message.add-mute-delete-file"));
			return;
		}
		if (!tempFile.renameTo(dataFile)) {
			System.out.println(messages.getString("error-message.add-mute-rename-file"));
		}
		if (BungeeCord.getInstance().getPlayer(player) != null) {
			ProxiedPlayer pmute = BungeeCord.getInstance().getPlayer(player);
			
			String asdr = SkyBan.main.messages.getString("mute-message.mute-message");
			asdr = asdr.replace("%banby%", banby);
			asdr = asdr.replace("%reason%", reason);
			asdr = asdr.replace("%bantime%", nummute);
			asdr = changesymbole(asdr);
			pmute.sendMessage(new TextComponent(asdr));
		}
		
	}
	
	public final void removemute(String player) throws IOException {
		File dataFile = new File("plugins/SkyBan/mute-player.txt");
		File tempFile = new File(dataFile.getAbsolutePath() + ".tmp");
		BufferedReader br = new BufferedReader(new FileReader(dataFile));
		PrintWriter pw = new PrintWriter(new FileWriter(tempFile));
		String line;
		while ((line = br.readLine()) != null) {
			String[] part = line.split("\\|");
			String name = part[0];
			if (!(player.equals(name))) {
				pw.println(line);
			}
		}
		br.close();
		pw.close();
		if (!dataFile.delete()) {
			System.out.println(messages.getString("error-message.remove-mute-delete-file"));
			return;
		}
		if (!tempFile.renameTo(dataFile)) {
			System.out.println(messages.getString("error-message.remove-mute-rename-file"));
		}
	}
	public final Date checkdatemute(String label, String player) throws IOException {
		File dataFile = new File("plugins/SkyBan/mute-player.txt");
		BufferedReader br = new BufferedReader(new FileReader(dataFile));
		String line;
		Date rep = null;
		Date d = null;
		while ((line = br.readLine()) != null) {
			String[] part = line.split("\\|");
			String name = part[0];
			String unbandate = part[3];
			String datebanned = part[1];
			if (label.equalsIgnoreCase("unbandate")) {
				SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.FRANCE);
				try {
					d = date.parse(unbandate);
				} catch (ParseException e) {e.printStackTrace();}
				if (player.equals(name)) {rep = d;}
			} else if (label.equalsIgnoreCase("datebanned")) {
				SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.FRANCE);
				try {
					d = date.parse(datebanned);
				} catch (ParseException e) {e.printStackTrace();}
				if (player.equals(name)) {rep = d;}
			}
			
		}
		br.close();
		return rep;
		
	}
	public final void sendMuteMessage(String bannedplayer, String banby, String reason, String numberday) {
		if (!(numberday.equalsIgnoreCase("perm"))) {
			String asdr = SkyBan.main.changesymbole(messages.getString("broadcast-message.tempmute-message"));
			asdr = asdr.replace("%bannedplayer%", bannedplayer);
			asdr = asdr.replace("%banby%", banby);
			asdr = asdr.replace("%reason%", reason);
			asdr = asdr.replace("%bantime%", numberday);
			ProxyServer.getInstance().broadcast(new TextComponent(asdr));
		} else {
			String asdr = SkyBan.main.changesymbole(messages.getString("broadcast-message.permmute-message"));
			asdr = asdr.replace("%bannedplayer%", bannedplayer);
			asdr = asdr.replace("%banby%", banby);
			asdr = asdr.replace("%reason%", reason);
			ProxyServer.getInstance().broadcast(new TextComponent(asdr));
		}
	}
	public final void sendUnMuteMessage(String bannedplayer, String unbanby) {
		String asdr = SkyBan.main.changesymbole(messages.getString("broadcast-message.tempmute-message-console"));
		asdr = asdr.replace("%bannedplayer%", bannedplayer);
		asdr = asdr.replace("%unbanby%", unbanby);
		ProxyServer.getInstance().broadcast(new TextComponent(asdr));
	}
	public final void sendMuteMessageconsole(String bannedplayer, String console, String reason, String numberday) {
		if (!(numberday.equalsIgnoreCase("perm"))) {
			String asdr = SkyBan.main.changesymbole(messages.getString("broadcast-message.tempmute-message-console"));
			asdr = asdr.replace("%bannedplayer%", bannedplayer);
			asdr = asdr.replace("%console%", console);
			asdr = asdr.replace("%reason%", reason);
			asdr = asdr.replace("%bantime%", numberday);
			ProxyServer.getInstance().broadcast(new TextComponent(asdr));
		} else {
			String asdr = SkyBan.main.changesymbole(messages.getString("broadcast-message.permmute-message-console"));
			asdr = asdr.replace("%bannedplayer%", bannedplayer);
			asdr = asdr.replace("%console%", console);
			asdr = asdr.replace("%reason%", reason);
			ProxyServer.getInstance().broadcast(new TextComponent(asdr));
		}
	}
	public final void sendUnMuteMessageconsole(String bannedplayer, String console) {
		String asdr = SkyBan.main.changesymbole(messages.getString("broadcast-message.unmute-message-console"));
		asdr = asdr.replace("%bannedplayer%", bannedplayer);
		asdr = asdr.replace("%console%", console);
		ProxyServer.getInstance().broadcast(new TextComponent(asdr));
	}
}

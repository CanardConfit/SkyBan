package ch.canardconfit.skyban;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class SqlConnection {

	private Connection connection;
	private String urlbase,host,database,user,pass;

	SimpleDateFormat datenow = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.FRANCE);
	
	public SqlConnection(String urlbase, String host, String database, String user, String pass) {
		
		this.urlbase = urlbase;
		this.host = host;
		this.database = database;
		this.user = user;
		this.pass = pass;
	}
	public void connection() {
		if (!isConnected()) {
			try {
				Connection initialisation = DriverManager.getConnection(urlbase + host, user, pass);
				ResultSet dblist = initialisation.getMetaData().getCatalogs();
				
				ArrayList<String> listdb = new ArrayList<String>();
				while (dblist.next()) {
				  String databaseName = dblist.getString(1);
				  listdb.add(databaseName);
				}
				if (!listdb.contains(database)) {
					System.out.println(SkyBan.main.changecfgline(SkyBan.main.messages.getString("error-message.mysql-database-dont-exist"), "%database%", database));
					return;
			  }
				dblist.close();
				initialisation.close();
			} catch (SQLException e) {
				System.out.println(SkyBan.main.messages.getString("error-message.mysql-error-on-connected"));
				e.printStackTrace();
				return;
			}
			try {
				connection = DriverManager.getConnection(urlbase + host + "/" + database, user, pass); 
				DatabaseMetaData dbm = connection.getMetaData();
				ResultSet tables = dbm.getTables(null, null, "joueurs", null);
				if (!tables.next()) {
					PreparedStatement q = connection.prepareStatement("CREATE TABLE `" + database + "`.`joueurs` ( `id` INT(11) NOT NULL AUTO_INCREMENT , `name` VARCHAR(255) NOT NULL , `uuid` VARCHAR(255) NOT NULL "
																	+ ", `first_co` VARCHAR(255) NOT NULL , `last_co` VARCHAR(255) NOT NULL , `last_ip` VARCHAR(255) NOT NULL , "
																	+ "`nb_mute` INT(11) NOT NULL , `nb_ban` INT(11) NOT NULL , `nb_kick` INT(11) NOT NULL , `banby` VARCHAR(255) NOT NULL , `date_banned`"
																	+ " VARCHAR(255) NOT NULL , `unban_date` VARCHAR(255) NOT NULL , `reason` VARCHAR(255) NOT NULL , `muteby` VARCHAR(255) NOT NULL , `date_muted` "
																	+ "VARCHAR(255) NOT NULL , `unmute_date` VARCHAR(255) NOT NULL , `reason_mute` VARCHAR(255) NOT NULL , PRIMARY KEY (`id`)) ENGINE = MyISAM;");
					q.execute();
					q.close();
				}
				ResultSet tables2 = dbm.getTables(null, null, "InetAddress", null);
				if (!tables2.next()) {
					PreparedStatement q = connection.prepareStatement("CREATE TABLE `" + database + "`.`InetAddress` ( `id` INT(11) NOT NULL AUTO_INCREMENT , `ip` VARCHAR(255) NOT NULL , `banby` VARCHAR(255) NOT NULL , `date_banned` "
																		+ "VARCHAR(255) NOT NULL , `unban_date` VARCHAR(255) NOT NULL , `reason` VARCHAR(255) NOT NULL , PRIMARY KEY (`id`), UNIQUE (`ip`)) ENGINE = MyISAM;");
					q.execute();
					q.close();
				}
				System.out.println(SkyBan.main.changecfgline(SkyBan.main.messages.getString("error-message.mysql-database-connected"), "%database%", database));
			} catch (SQLException e) {
				System.out.println(SkyBan.main.messages.getString("error-message.mysql-error-on-connected"));
				e.printStackTrace();
			}
		}
	}
	public void connectionConvert(CommandSender sender) {
		if (!isConnected()) {
			try {
				Connection initialisation = DriverManager.getConnection(urlbase + host, user, pass);
				ResultSet dblist = initialisation.getMetaData().getCatalogs();
				
				ArrayList<String> listdb = new ArrayList<String>();
				while (dblist.next()) {
				  String databaseName = dblist.getString(1);
				  listdb.add(databaseName);
				}
				if (!listdb.contains(database)) {

					SkyBan.main.sendView(sender, SkyBan.main.changesymbole(SkyBan.main.messages.getString("convert-message.line2-error")));
					try {

						File backupdir = new File(SkyBan.main.getDataFolder(), "backup-file");
						File configf = new File(backupdir, "config-convertion.yml");
						File msgf = new File(backupdir, "messages-convertion.yml");
						SkyBan.main.messageFile.delete();
						SkyBan.main.configFile.delete();
						Files.copy(msgf.toPath(), SkyBan.main.messageFile.toPath());
						Files.copy(configf.toPath(), SkyBan.main.configFile.toPath());
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					return;
			  }
				dblist.close();
				initialisation.close();
			} catch (SQLException e) {
				SkyBan.main.sendView(sender, SkyBan.main.changesymbole(SkyBan.main.messages.getString("convert-message.line2-error")));
				e.printStackTrace();
				try {

					File backupdir = new File(SkyBan.main.getDataFolder(), "backup-file");
					File configf = new File(backupdir, "config-convertion.yml");
					File msgf = new File(backupdir, "messages-convertion.yml");
					SkyBan.main.messageFile.delete();
					SkyBan.main.configFile.delete();
					Files.copy(msgf.toPath(), SkyBan.main.messageFile.toPath());
					Files.copy(configf.toPath(), SkyBan.main.configFile.toPath());
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				return;
			}
			try {
				connection = DriverManager.getConnection(urlbase + host + "/" + database, user, pass); 
				DatabaseMetaData dbm = connection.getMetaData();
				ResultSet tables = dbm.getTables(null, null, "joueurs", null);
				if (!tables.next()) {
					PreparedStatement q = connection.prepareStatement("CREATE TABLE `" + database + "`.`joueurs` ( `id` INT(11) NOT NULL AUTO_INCREMENT , `name` VARCHAR(255) NOT NULL , `uuid` VARCHAR(255) NOT NULL "
																	+ ", `first_co` VARCHAR(255) NOT NULL , `last_co` VARCHAR(255) NOT NULL , `last_ip` VARCHAR(255) NOT NULL , "
																	+ "`nb_mute` INT(11) NOT NULL , `nb_ban` INT(11) NOT NULL , `nb_kick` INT(11) NOT NULL , `banby` VARCHAR(255) NOT NULL , `date_banned`"
																	+ " VARCHAR(255) NOT NULL , `unban_date` VARCHAR(255) NOT NULL , `reason` VARCHAR(255) NOT NULL , `muteby` VARCHAR(255) NOT NULL , `date_muted` "
																	+ "VARCHAR(255) NOT NULL , `unmute_date` VARCHAR(255) NOT NULL , `reason_mute` VARCHAR(255) NOT NULL , PRIMARY KEY (`id`)) ENGINE = MyISAM;");
					q.execute();
					q.close();
				}
				ResultSet tables2 = dbm.getTables(null, null, "InetAddress", null);
				if (!tables2.next()) {
					PreparedStatement q = connection.prepareStatement("CREATE TABLE `" + database + "`.`InetAddress` ( `id` INT(11) NOT NULL AUTO_INCREMENT , `ip` VARCHAR(255) NOT NULL , `banby` VARCHAR(255) NOT NULL , `date_banned` "
																		+ "VARCHAR(255) NOT NULL , `unban_date` VARCHAR(255) NOT NULL , `reason` VARCHAR(255) NOT NULL , PRIMARY KEY (`id`), UNIQUE (`ip`)) ENGINE = MyISAM;");
					q.execute();
					q.close();
				}
				SkyBan.main.sendView(sender, SkyBan.main.changesymbole(SkyBan.main.messages.getString("convert-message.line2-connection-succes")));
			} catch (SQLException e) {
				SkyBan.main.sendView(sender, SkyBan.main.changesymbole(SkyBan.main.messages.getString("convert-message.line2-error")));
				e.printStackTrace();
				try {

					File backupdir = new File(SkyBan.main.getDataFolder(), "backup-file");
					File configf = new File(backupdir, "config-convertion.yml");
					File msgf = new File(backupdir, "messages-convertion.yml");
					SkyBan.main.messageFile.delete();
					SkyBan.main.configFile.delete();
					Files.copy(msgf.toPath(), SkyBan.main.messageFile.toPath());
					Files.copy(configf.toPath(), SkyBan.main.configFile.toPath());
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
	}
	public void disconnect() {
		if (isConnected()) {
			try {
				connection.close();
				System.out.println(SkyBan.main.messages.getString("error-message.mysql-database-connection-terminate"));
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	public boolean isConnected() {
		return connection != null;
	}
	public void createAccount(ProxiedPlayer player) {
		if (!asOfflineAccount(player.getName(), "")) {
			SimpleDateFormat datenow = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.FRANCE);
			Date now = new Date();
			String strDate = datenow.format(now);
			try {
				PreparedStatement q = connection.prepareStatement("INSERT INTO joueurs(name,uuid,first_co,last_co,last_ip,nb_mute,nb_ban,nb_kick,banby,date_banned,unban_date,reason,muteby,date_muted,unmute_date,reason_mute) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
				q.setString(1, player.getName());
				q.setString(2, player.getUniqueId().toString());
				q.setString(3, strDate);
				q.setString(4, strDate);
				q.setString(5, player.getAddress().getHostName().toString());
				q.setInt(6, 0);
				q.setInt(7, 0);
				q.setInt(8, 0);
				q.setString(9, "");
				q.setString(10, "");
				q.setString(11, "");
				q.setString(12, "");
				q.setString(13, "");
				q.setString(14, "");
				q.setString(15, "");
				q.setString(16, "");
				q.execute();
				q.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
		} else {
			changeJoinAccount(player);
		}
	}
	public void createOfflineAccount(String player) {
		if (!asOfflineAccount(player, "")) {
			try {
				PreparedStatement q = connection.prepareStatement("INSERT INTO joueurs(name,uuid,first_co,last_co,last_ip,nb_mute,nb_ban,nb_kick,banby,date_banned,unban_date,reason,muteby,date_muted,unmute_date,reason_mute) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
				q.setString(1, player);
				q.setString(2, "");
				q.setString(3, "");
				q.setString(4, "");
				q.setString(5, "");
				q.setInt(6, 0);
				q.setInt(7, 0);
				q.setInt(8, 0);
				q.setString(9, "");
				q.setString(10, "");
				q.setString(11, "");
				q.setString(12, "");
				q.setString(13, "");
				q.setString(14, "");
				q.setString(15, "");
				q.setString(16, "");
				q.execute();
				q.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
		}
	}
	public void createIpAccount(InetAddress ip) {
		if (!asIpAccount(ip.getHostAddress())) {
			try {
				PreparedStatement q = connection.prepareStatement("INSERT INTO InetAddress(ip,banby,date_banned,unban_date,reason) VALUES (?,?,?,?,?)");
				q.setString(1, ip.getHostAddress());
				q.setString(2, "");
				q.setString(3, "");
				q.setString(4, "");
				q.setString(5, "");
				q.execute();
				q.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	public void changeJoinAccount(ProxiedPlayer player) {
		try {
			Date now = new Date();
			String strDate = datenow.format(now);
			PreparedStatement q = connection.prepareStatement("UPDATE joueurs SET last_co = ? WHERE name = ?");
			q.setString(1, strDate);
			q.setString(2, player.getName());
			q.executeUpdate();
			q.close();
			
			q = connection.prepareStatement("SELECT * FROM joueurs WHERE name = ?");
			q.setString(1, player.getName());
			ResultSet resultat = q.executeQuery();
			String uuid = null;
			while(resultat.next()) {
				uuid = resultat.getString("uuid");
			}
			q.close();
			if (uuid.equalsIgnoreCase("")) {
				q = connection.prepareStatement("UPDATE joueurs SET uuid = ? WHERE name = ?");
				q.setString(1, player.getUniqueId().toString());
				q.setString(2, player.getName());
				q.executeUpdate();
				q.close();
				
				q = connection.prepareStatement("UPDATE joueurs SET first_co = ? WHERE name = ?");
				q.setString(1, strDate);
				q.setString(2, player.getName());
				q.executeUpdate();
				q.close();
			}
			
			q = connection.prepareStatement("UPDATE joueurs SET last_ip = ? WHERE name = ?");
			q.setString(1, player.getName());
			q.setString(2, player.getUniqueId().toString());
			q.executeUpdate();
			q.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	public boolean asOfflineAccount(String player, String uuid) {
		try {
			if (player.equals("")) {
				
					PreparedStatement q = connection.prepareStatement("SELECT uuid FROM joueurs WHERE uuid = ?");
					q.setString(1, uuid);
					ResultSet resultat = q.executeQuery();
					boolean hasAccount = resultat.next();
					q.close();
					return hasAccount;
				
			} else if (uuid.equals("")) {
				
				PreparedStatement q = connection.prepareStatement("SELECT name FROM joueurs WHERE name = ?"); 
				q.setString(1, player);
				ResultSet resultat = q.executeQuery();
				boolean hasAccount = resultat.next();
				q.close();
				return hasAccount;
				
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	public boolean asIpAccount(String ip) {
		try {
			PreparedStatement q = connection.prepareStatement("SELECT ip FROM InetAddress WHERE ip = ?");
			q.setString(1, ip);
			ResultSet resultat = q.executeQuery();
			boolean hasAccount = resultat.next();
			q.close();
			return hasAccount;
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	public ArrayList<String> getBan(String player) {
		PreparedStatement q;
		try {
			q = connection.prepareStatement("SELECT * FROM joueurs WHERE name = ?");
			q.setString(1, player);
			ResultSet resultat = q.executeQuery();
			ArrayList<String> rep = new ArrayList<String>();
			while(resultat.next()) {
				rep.add(resultat.getString("banby"));
				rep.add(resultat.getString("date_banned"));
				rep.add(resultat.getString("unban_date"));
				rep.add(resultat.getString("reason"));
			}
			q.close();
			return rep;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	public ArrayList<String> getIpBan(String ip) {
		PreparedStatement q;
		try {
			q = connection.prepareStatement("SELECT * FROM InetAddress WHERE ip = ?");
			q.setString(1, ip);
			ResultSet resultat = q.executeQuery();
			ArrayList<String> rep = new ArrayList<String>();
			while(resultat.next()) {
				rep.add(resultat.getString("banby"));
				rep.add(resultat.getString("date_banned"));
				rep.add(resultat.getString("unban_date"));
				rep.add(resultat.getString("reason"));
			}
			q.close();
			return rep;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	public void removeBan(String player) {
		PreparedStatement q;
		try {
			q= connection.prepareStatement("UPDATE joueurs SET banby = ? WHERE name = ?");
			q.setString(1, "");
			q.setString(2, player);
			q.executeUpdate();
			q.close();
			
			q= connection.prepareStatement("UPDATE joueurs SET date_banned = ? WHERE name = ?");
			q.setString(1, "");
			q.setString(2, player);
			q.executeUpdate();
			q.close();
			
			q= connection.prepareStatement("UPDATE joueurs SET unban_date = ? WHERE name = ?");
			q.setString(1, "");
			q.setString(2, player);
			q.executeUpdate();
			q.close();
			
			q= connection.prepareStatement("UPDATE joueurs SET reason = ? WHERE name = ?");
			q.setString(1, "");
			q.setString(2, player);
			q.executeUpdate();
			q.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public void removeIpBan(String ip) {
		PreparedStatement q;
		try {
			q= connection.prepareStatement("UPDATE InetAddress SET banby = ? WHERE ip = ?");
			q.setString(1, "");
			q.setString(2, ip);
			q.executeUpdate();
			q.close();
			
			q= connection.prepareStatement("UPDATE InetAddress SET date_banned = ? WHERE ip = ?");
			q.setString(1, "");
			q.setString(2, ip);
			q.executeUpdate();
			q.close();
			
			q= connection.prepareStatement("UPDATE InetAddress SET unban_date = ? WHERE ip = ?");
			q.setString(1, "");
			q.setString(2, ip);
			q.executeUpdate();
			q.close();
			
			q= connection.prepareStatement("UPDATE InetAddress SET reason = ? WHERE ip = ?");
			q.setString(1, "");
			q.setString(2, ip);
			q.executeUpdate();
			q.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public void addBan(String player, Date unbandate, String banby, String reason) {
		if (!asOfflineAccount(player, "")) {
			if (BungeeCord.getInstance().getPlayer(player) == null) {
				createOfflineAccount(player);
			} else {
				createAccount(BungeeCord.getInstance().getPlayer(player));
			}
		}
		PreparedStatement q;
		try {
			q= connection.prepareStatement("UPDATE joueurs SET banby = ? WHERE name = ?");
			q.setString(1, banby);
			q.setString(2, player);
			q.executeUpdate();
			q.close();
			
			q= connection.prepareStatement("UPDATE joueurs SET date_banned = ? WHERE name = ?");
			q.setString(1, SkyBan.main.getNowDate());
			q.setString(2, player);
			q.executeUpdate();
			q.close();
			
			q= connection.prepareStatement("UPDATE joueurs SET unban_date = ? WHERE name = ?");
			q.setString(1, datenow.format(unbandate));
			q.setString(2, player);
			q.executeUpdate();
			q.close();
			
			q= connection.prepareStatement("UPDATE joueurs SET reason = ? WHERE name = ?");
			q.setString(1, reason);
			q.setString(2, player);
			q.executeUpdate();
			q.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public void addIpBan(String ip, Date unbandate, String banby, String reason) {
		if (!asIpAccount(ip)) {
			try {
				createIpAccount(InetAddress.getByName(ip));
			} catch (UnknownHostException e) {
				e.printStackTrace();
			}
		}
		PreparedStatement q;
		try {
			q= connection.prepareStatement("UPDATE InetAddress SET banby = ? WHERE ip = ?");
			q.setString(1, banby);
			q.setString(2, ip);
			q.executeUpdate();
			q.close();
			
			q= connection.prepareStatement("UPDATE InetAddress SET date_banned = ? WHERE ip = ?");
			q.setString(1, SkyBan.main.getNowDate());
			q.setString(2, ip);
			q.executeUpdate();
			q.close();
			
			q= connection.prepareStatement("UPDATE InetAddress SET unban_date = ? WHERE ip = ?");
			q.setString(1, datenow.format(unbandate));
			q.setString(2, ip);
			q.executeUpdate();
			q.close();
			
			q= connection.prepareStatement("UPDATE InetAddress SET reason = ? WHERE ip = ?");
			q.setString(1, reason);
			q.setString(2, ip);
			q.executeUpdate();
			q.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public void changePlayerInfo(String player, String i) {
		PreparedStatement q;
		try {
			PreparedStatement q2 = connection.prepareStatement("SELECT * FROM joueurs WHERE name = ?");
			q2.setString(1, player);
			ResultSet resultat = q2.executeQuery();
			int nb_ban = 0, nb_mute = 0, nb_kick = 0;
			while(resultat.next()) {
				nb_ban = resultat.getInt("nb_ban");
				nb_mute = resultat.getInt("nb_mute");
				nb_kick = resultat.getInt("nb_kick");
			}
			q2.close();
			if (i.equalsIgnoreCase("nbban")) {
				
				q= connection.prepareStatement("UPDATE joueurs SET nb_ban = ? WHERE name = ?");
				q.setInt(1, (nb_ban + 1));
				q.setString(2, player);
				q.executeUpdate();
				q.close();
				
			} else if (i.equalsIgnoreCase("nbmute")) {
				
				q = connection.prepareStatement("UPDATE joueurs SET nb_mute = ? WHERE name = ?");
				q.setInt(1, (nb_mute + 1));
				q.setString(2, player);
				q.executeUpdate();
				q.close();
				
			}else if (i.equalsIgnoreCase("nbkick")) {
				
				q = connection.prepareStatement("UPDATE joueurs SET nb_kick = ? WHERE name = ?");
				q.setInt(1, (nb_kick + 1));
				q.setString(2, player);
				q.executeUpdate();
				q.close();
				
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public ArrayList<String> checkPlayerInfo(String player){
		PreparedStatement q;
		try {
			q = connection.prepareStatement("SELECT * FROM joueurs WHERE name = ?");
			q.setString(1, player);
			ResultSet resultat = q.executeQuery();
			ArrayList<String> rep = new ArrayList<String>();
			while(resultat.next()) {
				rep.add(resultat.getString("id"));
				rep.add(resultat.getString("uuid"));
				rep.add(resultat.getString("first_co"));
				rep.add(resultat.getString("last_co"));
				rep.add(resultat.getString("last_ip"));
				rep.add(resultat.getString("nb_mute"));
				rep.add(resultat.getString("nb_ban"));
				rep.add(resultat.getString("nb_kick"));
			}
			q.close();
			return rep;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	public ArrayList<String> getMute(String player) {
		PreparedStatement q;
		try {
			q = connection.prepareStatement("SELECT * FROM joueurs WHERE name = ?");
			q.setString(1, player);
			ResultSet resultat = q.executeQuery();
			ArrayList<String> rep = new ArrayList<String>();
			while(resultat.next()) {
				rep.add(resultat.getString("muteby"));
				rep.add(resultat.getString("date_muted"));
				rep.add(resultat.getString("unmute_date"));
				rep.add(resultat.getString("reason_mute"));
			}
			q.close();
			return rep;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	public void removeMute(String player) {
		PreparedStatement q;
		try {
			q= connection.prepareStatement("UPDATE joueurs SET muteby = ? WHERE name = ?");
			q.setString(1, "");
			q.setString(2, player);
			q.executeUpdate();
			q.close();
			
			q= connection.prepareStatement("UPDATE joueurs SET date_muted = ? WHERE name = ?");
			q.setString(1, "");
			q.setString(2, player);
			q.executeUpdate();
			q.close();
			
			q= connection.prepareStatement("UPDATE joueurs SET unmute_date = ? WHERE name = ?");
			q.setString(1, "");
			q.setString(2, player);
			q.executeUpdate();
			q.close();
			
			q= connection.prepareStatement("UPDATE joueurs SET reason_mute = ? WHERE name = ?");
			q.setString(1, "");
			q.setString(2, player);
			q.executeUpdate();
			q.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public void addMute(String player, Date unbandate, String banby, String reason) {
		PreparedStatement q;
		try {
			q= connection.prepareStatement("UPDATE joueurs SET muteby = ? WHERE name = ?");
			q.setString(1, banby);
			q.setString(2, player);
			q.executeUpdate();
			q.close();
			
			q= connection.prepareStatement("UPDATE joueurs SET date_muted = ? WHERE name = ?");
			q.setString(1, SkyBan.main.getNowDate());
			q.setString(2, player);
			q.executeUpdate();
			q.close();
			
			q= connection.prepareStatement("UPDATE joueurs SET unmute_date = ? WHERE name = ?");
			q.setString(1, datenow.format(unbandate));
			q.setString(2, player);
			q.executeUpdate();
			q.close();
			
			q= connection.prepareStatement("UPDATE joueurs SET reason_mute = ? WHERE name = ?");
			q.setString(1, reason);
			q.setString(2, player);
			q.executeUpdate();
			q.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public void convert(CommandSender sender) {
		try {
			File dataFile = new File("plugins/SkyBan/banned-player.txt");
			BufferedReader br = new BufferedReader(new FileReader(dataFile));
			String line;
			while ((line = br.readLine()) != null) {
				String[] part = line.split("\\|");
				String name = part[0];
				String datebanned = part[1];
				String banby = part[2];
				String unbandate = part[3];
				String reason = part[4];
				
				if (!asOfflineAccount(name, "")) {
					
					PreparedStatement q = connection.prepareStatement("INSERT INTO joueurs(name,uuid,first_co,last_co,last_ip,nb_mute,nb_ban,nb_kick,banby,date_banned,unban_date,reason,muteby,date_muted,unmute_date,reason_mute) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
					
					q.setString(1, name);
					q.setString(2, "");
					q.setString(3, "");
					q.setString(4, "");
					q.setString(5, "");
					q.setInt(6, 0);
					q.setInt(7, 0);
					q.setInt(8, 0);
					q.setString(9, banby);
					q.setString(10, datebanned);
					q.setString(11, unbandate);
					q.setString(12, reason);
					q.setString(13, "");
					q.setString(14, "");
					q.setString(15, "");
					q.setString(16, "");
					q.execute();
					q.close();
					
				} else {
					
					PreparedStatement q;
					
					q = connection.prepareStatement("UPDATE joueurs SET banby = ? WHERE name = ?");
					q.setString(1, banby);
					q.setString(2, name);
					q.executeUpdate();
					q.close();
					
					q = connection.prepareStatement("UPDATE joueurs SET date_banned = ? WHERE name = ?");
					q.setString(1, datebanned);
					q.setString(2, name);
					q.executeUpdate();
					q.close();
					
					q = connection.prepareStatement("UPDATE joueurs SET unban_date = ? WHERE name = ?");
					q.setString(1, unbandate);
					q.setString(2, name);
					q.executeUpdate();
					q.close();
					
					q = connection.prepareStatement("UPDATE joueurs SET reason = ? WHERE name = ?");
					q.setString(1, reason);
					q.setString(2, name);
					q.executeUpdate();
					q.close();
					
				}
			}
			br.close();
			
			
			
			File dataFileMute = new File("plugins/SkyBan/mute-player.txt");
			BufferedReader brmute = new BufferedReader(new FileReader(dataFileMute));
			String linemute;
			while ((linemute = brmute.readLine()) != null) {
				String[] part = linemute.split("\\|");
				String name = part[0];
				String datemuted = part[1];
				String muteby = part[2];
				String unmutedate = part[3];
				String reason = part[4];
				
				if (!asOfflineAccount(name, "")) {
					
					PreparedStatement q = connection.prepareStatement("INSERT INTO joueurs(name,uuid,first_co,last_co,last_ip,nb_mute,nb_ban,nb_kick,banby,date_banned,unban_date,reason,muteby,date_muted,unmute_date,reason_mute) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
					
					q.setString(1, name);
					q.setString(2, "");
					q.setString(3, "");
					q.setString(4, "");
					q.setString(5, "");
					q.setInt(6, 0);
					q.setInt(7, 0);
					q.setInt(8, 0);
					q.setString(9, muteby);
					q.setString(10, datemuted);
					q.setString(11, unmutedate);
					q.setString(12, reason);
					q.setString(13, "");
					q.setString(14, "");
					q.setString(15, "");
					q.setString(16, "");
					q.execute();
					q.close();
					
				} else {
					
					PreparedStatement q;
					
					q = connection.prepareStatement("UPDATE joueurs SET muteby = ? WHERE name = ?");
					q.setString(1, muteby);
					q.setString(2, name);
					q.executeUpdate();
					q.close();
					
					q = connection.prepareStatement("UPDATE joueurs SET date_muted = ? WHERE name = ?");
					q.setString(1, datemuted);
					q.setString(2, name);
					q.executeUpdate();
					q.close();
					
					q = connection.prepareStatement("UPDATE joueurs SET unmute_date = ? WHERE name = ?");
					q.setString(1, unmutedate);
					q.setString(2, name);
					q.executeUpdate();
					q.close();
					
					q = connection.prepareStatement("UPDATE joueurs SET reason_mute = ? WHERE name = ?");
					q.setString(1, reason);
					q.setString(2, name);
					q.executeUpdate();
					q.close();
					
				}
			}
			brmute.close();
			
			
			
			File dataFileip = new File("plugins/SkyBan/banned-ips.txt");
			BufferedReader brip = new BufferedReader(new FileReader(dataFileip));
			String lineip;
			while ((lineip = brip.readLine()) != null) {
				String[] part = lineip.split("\\|");
				String ip = part[0].replace("/", "");
				
				String datebanned = part[1];
				String banby = part[2];
				String unbandate = part[3];
				String reason = part[4];
					
				if (!asIpAccount(ip)) {
						
					PreparedStatement q = connection.prepareStatement("INSERT INTO InetAddress(ip,banby,date_banned,unban_date,reason) VALUES (?,?,?,?,?)");
					q.setString(1, ip);
					q.setString(2, banby);
					q.setString(3, datebanned);
					q.setString(4, unbandate);
					q.setString(5, reason);
					q.execute();
					q.close();
					
				} else {
					PreparedStatement q;
					
					q= connection.prepareStatement("UPDATE InetAddress SET banby = ? WHERE ip = ?");
					q.setString(1, banby);
					q.setString(2, ip);
					q.executeUpdate();
					q.close();
					
					q= connection.prepareStatement("UPDATE InetAddress SET date_banned = ? WHERE ip = ?");
					q.setString(1, datebanned);
					q.setString(2, ip);
					q.executeUpdate();
					q.close();
					
					q= connection.prepareStatement("UPDATE InetAddress SET unban_date = ? WHERE ip = ?");
					q.setString(1, unbandate);
					q.setString(2, ip);
					q.executeUpdate();
					q.close();
					
					q= connection.prepareStatement("UPDATE InetAddress SET reason = ? WHERE ip = ?");
					q.setString(1, reason);
					q.setString(2, ip);
					q.executeUpdate();
					q.close();
				}
			}
			brip.close();
			
			

			File[] Filefile = (new File(SkyBan.main.getDataFolder().getPath() + "\\playerinfo\\")).listFiles();
			
			for (File file : Filefile) {
				
				File playerFile = file;
				String name = file.getName().replace(SkyBan.main.getExtention(file.getName()), "");
				name = name.replace(".", "");
				BufferedReader brp = new BufferedReader(new FileReader(playerFile));
				String linep;
				while ((linep = brp.readLine()) != null) {
					String[] part = linep.split(";");
					String idline = part[0];
					String valueline = part[1];
					
					if (!asOfflineAccount(name, "")) {
						
						createOfflineAccount(name);
					}
					PreparedStatement q;
						
					if (idline.equals("Premiere connexion")) {
						q= connection.prepareStatement("UPDATE joueurs SET first_co = ? WHERE name = ?");
						q.setString(1, valueline);
						q.setString(2, name);
						q.executeUpdate();
						q.close();
							
					} else if (idline.equals("Derniere connexion")) {
						q= connection.prepareStatement("UPDATE joueurs SET last_co = ? WHERE name = ?");
						q.setString(1, valueline);
						q.setString(2, name);
						q.executeUpdate();
						q.close();
						
					} else if (idline.equals("Nombre de Mute du joueur")) {
						q= connection.prepareStatement("UPDATE joueurs SET nb_mute = ? WHERE name = ?");
						q.setString(1, valueline);
						q.setString(2, name);
						q.executeUpdate();
						q.close();
					
					} else if (idline.equals("Nombre de Ban du joueur")) {
						q= connection.prepareStatement("UPDATE joueurs SET nb_ban = ? WHERE name = ?");
						q.setString(1, valueline);
						q.setString(2, name);
						q.executeUpdate();
						q.close();
							
					} else if (idline.equals("Nombre de Kick du joueur")) {
						q= connection.prepareStatement("UPDATE joueurs SET nb_kick = ? WHERE name = ?");
						q.setString(1, valueline);
						q.setString(2, name);
						q.executeUpdate();
						q.close();
							
					} else if (idline.equals("IP du joueur")) {
						q= connection.prepareStatement("UPDATE joueurs SET last_ip = ? WHERE name = ?");
						q.setString(1, valueline.replace("/", ""));
						q.setString(2, name);
						q.executeUpdate();
						q.close();
							
					}
				}
				brp.close();
				SkyBan.main.sendView(sender, SkyBan.main.changesymbole(SkyBan.main.messages.getString("convert-message.line2-convert-succes")));
			}
		} catch (IOException e) {
			SkyBan.main.sendErrorView(sender, e);
		} catch (SQLException e) {
			SkyBan.main.sendView(sender, SkyBan.main.changesymbole(SkyBan.main.messages.getString("convert-message.line2-error")));
			e.printStackTrace();
			try {

				File backupdir = new File(SkyBan.main.getDataFolder(), "backup-file");
				File configf = new File(backupdir, "config-convertion.yml");
				File msgf = new File(backupdir, "messages-convertion.yml");
				SkyBan.main.messageFile.delete();
				SkyBan.main.configFile.delete();
				Files.copy(msgf.toPath(), SkyBan.main.messageFile.toPath());
				Files.copy(configf.toPath(), SkyBan.main.configFile.toPath());
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}
}

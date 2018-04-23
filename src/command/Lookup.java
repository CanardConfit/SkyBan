package command;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import ch.canardconfit.skyban.SkyBan;
import ch.canardconfit.skyban.SqlConnection;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;

public class Lookup extends Command {

	private boolean mysql;
	private SqlConnection sql;
	
	public Lookup(String name, SqlConnection sql, boolean mysql) {
		super(name, "skyban.lookup");
		this.mysql = mysql;
		this.sql = sql;
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		if (args.length == 0) {
			sender.sendMessage(new TextComponent(SkyBan.main.changesymbole(SkyBan.main.messages.getString("utilisation-message.utilisation-lookup"))));
		} else {
			try {
				SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.FRANCE);
				if (SkyBan.main.checkInetAddress(args[0]) == false) {
					String ifmute = SkyBan.main.checkMute("name", args[0]);
					String ifbannis = checkBan("name", args[0]);
					String exist = "exist";
					if (exist.equalsIgnoreCase(SkyBan.main.checkplayerinfo(args[0], "file"))) {
						String ipplayer = SkyBan.main.checkplayerinfo(args[0], "ip");
						String lastco = SkyBan.main.checkplayerinfo(args[0], "dco");
						String nbban = SkyBan.main.checkplayerinfo(args[0], "nbban");
						String nbmute = SkyBan.main.checkplayerinfo(args[0], "nbmute");
						String nbkick = SkyBan.main.checkplayerinfo(args[0], "nbkick");
						String firstco = SkyBan.main.checkplayerinfo(args[0], "pco");
	
						sender.sendMessage(new TextComponent(SkyBan.main.changesymbole(SkyBan.main.messages.getString("lookup-info.info-player.line0"))));
						if (args[0].equals(ifbannis)) {
							Date d = checkdate("datebanned", args[0]);
							Date u = checkdate("unbandate", args[0]);
							String datebanned = date.format(d);
							String unbandate = date.format(u);
							if (unbandate.equals("5000-12-12 12:12:12")) {unbandate = SkyBan.main.changesymbole(SkyBan.main.messages.getString("info-player-error-message.banperm"));}
							String banby = checkBan("mystaff", args[0]);
							String reason = checkBan("reason", args[0]);
							sender.sendMessage(new TextComponent(SkyBan.main.changecfgline(SkyBan.main.changesymbole(SkyBan.main.messages.getString("lookup-info.info-player.line1")), "%bannedplayer%", args[0])));
							sender.sendMessage(new TextComponent(SkyBan.main.changesymbole(SkyBan.main.messages.getString("lookup-info.info-player.line2"))));
							sender.sendMessage(new TextComponent(SkyBan.main.changesymbole(SkyBan.main.messages.getString("lookup-info.info-player.line3"))));
							sender.sendMessage(new TextComponent(SkyBan.main.changesymbole(SkyBan.main.messages.getString("lookup-info.info-player.line4"))));
							sender.sendMessage(new TextComponent(SkyBan.main.changesymbole(SkyBan.main.changecfgline(SkyBan.main.messages.getString("lookup-info.info-player.line5"), "%datebanned%", datebanned))));
							sender.sendMessage(new TextComponent(SkyBan.main.changesymbole(SkyBan.main.changecfgline(SkyBan.main.messages.getString("lookup-info.info-player.line6"), "%unbandate%", unbandate))));
							sender.sendMessage(new TextComponent(SkyBan.main.changesymbole(SkyBan.main.changecfgline(SkyBan.main.messages.getString("lookup-info.info-player.line7"), "%banby%", banby))));
							sender.sendMessage(new TextComponent(SkyBan.main.changesymbole(SkyBan.main.changecfgline(SkyBan.main.messages.getString("lookup-info.info-player.line8"), "%reason%", reason))));
						} else {
							sender.sendMessage(new TextComponent(SkyBan.main.changesymbole(SkyBan.main.changecfgline(SkyBan.main.messages.getString("lookup-info.info-player.error-message.noban"), "%player%", args[0]))));
						}
						sender.sendMessage(new TextComponent(SkyBan.main.changesymbole(SkyBan.main.messages.getString("lookup-info.info-player.line9"))));
						if (args[0].equals(ifmute)) {
							Date d = SkyBan.main.checkdatemute("datebanned", args[0]);
							Date u = SkyBan.main.checkdatemute("unbandate", args[0]);
							String datemuted = date.format(d);
							String unmutedate = date.format(u);
							if (unmutedate.equals("5000-12-12 12:12:12")) {unmutedate = SkyBan.main.changesymbole(SkyBan.main.messages.getString("info-player-error-message.muteperm"));}
							String banby = SkyBan.main.checkMute("mystaff", args[0]);
							String reason = SkyBan.main.checkMute("reason", args[0]);
							sender.sendMessage(new TextComponent(SkyBan.main.changecfgline(SkyBan.main.changesymbole(SkyBan.main.messages.getString("lookup-info.info-player.line10")), "%mutedplayer%", args[0])));
							sender.sendMessage(new TextComponent(SkyBan.main.changesymbole(SkyBan.main.messages.getString("lookup-info.info-player.line11"))));
							sender.sendMessage(new TextComponent(SkyBan.main.changesymbole(SkyBan.main.messages.getString("lookup-info.info-player.line12"))));
							sender.sendMessage(new TextComponent(SkyBan.main.changesymbole(SkyBan.main.messages.getString("lookup-info.info-player.line13"))));
							sender.sendMessage(new TextComponent(SkyBan.main.changecfgline(SkyBan.main.changesymbole(SkyBan.main.messages.getString("lookup-info.info-player.line14")), "%datemuted%", datemuted)));
							sender.sendMessage(new TextComponent(SkyBan.main.changecfgline(SkyBan.main.changesymbole(SkyBan.main.messages.getString("lookup-info.info-player.line15")), "%unmutedate%", unmutedate)));
							sender.sendMessage(new TextComponent(SkyBan.main.changecfgline(SkyBan.main.changesymbole(SkyBan.main.messages.getString("lookup-info.info-player.line16")), "%muteby%", banby)));
							sender.sendMessage(new TextComponent(SkyBan.main.changecfgline(SkyBan.main.changesymbole(SkyBan.main.messages.getString("lookup-info.info-player.line17")), "%reason%", reason)));
						} else {
							sender.sendMessage(new TextComponent(SkyBan.main.changecfgline(SkyBan.main.changesymbole(SkyBan.main.messages.getString("lookup-info.info-player.error-message.nomute")), "%player%", args[0])));
						}
						sender.sendMessage(new TextComponent(SkyBan.main.changesymbole(SkyBan.main.messages.getString("lookup-info.info-player.line18"))));
						sender.sendMessage(new TextComponent(SkyBan.main.changecfgline(SkyBan.main.changesymbole(SkyBan.main.messages.getString("lookup-info.info-player.line19")), "%ipplayer%", ipplayer)));
						sender.sendMessage(new TextComponent(SkyBan.main.changecfgline(SkyBan.main.changesymbole(SkyBan.main.messages.getString("lookup-info.info-player.line20")), "%lastco%", lastco)));
						sender.sendMessage(new TextComponent(SkyBan.main.changecfgline(SkyBan.main.changesymbole(SkyBan.main.messages.getString("lookup-info.info-player.line21")), "%firstco%", firstco)));
						sender.sendMessage(new TextComponent(SkyBan.main.changecfgline(SkyBan.main.changesymbole(SkyBan.main.messages.getString("lookup-info.info-player.line22")), "%nbmute%", nbmute)));
						sender.sendMessage(new TextComponent(SkyBan.main.changecfgline(SkyBan.main.changesymbole(SkyBan.main.messages.getString("lookup-info.info-player.line23")), "%nbban%", nbban)));
						sender.sendMessage(new TextComponent(SkyBan.main.changecfgline(SkyBan.main.changesymbole(SkyBan.main.messages.getString("lookup-info.info-player.line24")), "%nbkick%", nbkick)));
						sender.sendMessage(new TextComponent(SkyBan.main.changesymbole(SkyBan.main.messages.getString("lookup-info.info-player.line25"))));
					} else {
						sender.sendMessage(new TextComponent(SkyBan.main.changesymbole(SkyBan.main.messages.getString("lookup-info.info-player.error-message.line0"))));
						sender.sendMessage(new TextComponent(SkyBan.main.changecfgline(SkyBan.main.changesymbole(SkyBan.main.messages.getString("lookup-info.info-player.error-message.line1")), "%player%", args[0])));
						sender.sendMessage(new TextComponent(SkyBan.main.changesymbole(SkyBan.main.messages.getString("lookup-info.info-player.error-message.line2"))));
					}
				} else {
					InetAddress ipargs = InetAddress.getByName(args[0]);
					String ifbannis = SkyBan.main.checkIpBan("test", ipargs);
					if (("/" + args[0]).equals(ifbannis)) {
						Date db = SkyBan.main.checkdatebanip("datebanned", ipargs);
						Date ud = SkyBan.main.checkdatebanip("unbandate", ipargs);
						String datebanned = date.format(db);
						String unbandate = date.format(ud);
						String banby = SkyBan.main.checkIpBan("mystaff", ipargs);
						String reason = SkyBan.main.checkIpBan("reason", ipargs);
						sender.sendMessage(new TextComponent(SkyBan.main.changesymbole(SkyBan.main.messages.getString("lookup-info.info-ip.line0"))));
						sender.sendMessage(new TextComponent(SkyBan.main.changecfgline(SkyBan.main.changesymbole(SkyBan.main.messages.getString("lookup-info.info-ip.line1")), "%bannedip%", args[0])));
						sender.sendMessage(new TextComponent(SkyBan.main.changesymbole(SkyBan.main.messages.getString("lookup-info.info-ip.line2"))));
						sender.sendMessage(new TextComponent(SkyBan.main.changesymbole(SkyBan.main.messages.getString("lookup-info.info-ip.line3"))));
						sender.sendMessage(new TextComponent(SkyBan.main.changesymbole(SkyBan.main.messages.getString("lookup-info.info-ip.line4"))));
						sender.sendMessage(new TextComponent(SkyBan.main.changesymbole(SkyBan.main.changecfgline(SkyBan.main.messages.getString("lookup-info.info-ip.line5"), "%datebanned%", datebanned))));
						sender.sendMessage(new TextComponent(SkyBan.main.changesymbole(SkyBan.main.changecfgline(SkyBan.main.messages.getString("lookup-info.info-ip.line6"), "%unbandate%", unbandate))));
						sender.sendMessage(new TextComponent(SkyBan.main.changesymbole(SkyBan.main.changecfgline(SkyBan.main.messages.getString("lookup-info.info-ip.line7"), "%banby%", banby))));
						sender.sendMessage(new TextComponent(SkyBan.main.changesymbole(SkyBan.main.changecfgline(SkyBan.main.messages.getString("lookup-info.info-ip.line8"), "%reason%", reason))));
						sender.sendMessage(new TextComponent(SkyBan.main.changesymbole(SkyBan.main.messages.getString("lookup-info.info-ip.line9"))));
					} else {
						sender.sendMessage(new TextComponent(SkyBan.main.changesymbole(SkyBan.main.messages.getString("lookup-info.info-ip.error-message.line0"))));
						String b = SkyBan.main.messages.getString("lookup-info.info-ip.error-message.line1");
						String a = SkyBan.main.changecfgline(SkyBan.main.changesymbole(b), "%ipplayer%", args[0]);
						sender.sendMessage(new TextComponent(a));
						sender.sendMessage(new TextComponent(SkyBan.main.changesymbole(SkyBan.main.messages.getString("lookup-info.info-ip.error-message.line2"))));
					}
				}
			} catch (UnknownHostException e) {e.printStackTrace();
			} catch (IOException e) {e.printStackTrace();}
		}
	}
	
	
	private final String checkBan(String label, String player) throws IOException {
		if (mysql == false) {
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
		} else {
			if (sql.asOfflineAccount(player, "")) {
				ArrayList<String> ban = sql.getBan(player);
				if (label.equalsIgnoreCase("name")) {
					if (ban.get(1).equals("")) {
						return null;
					} else {
						return player;
					}
				} else if (label.equalsIgnoreCase("mystaff")) {
					return ban.get(0);
				} else if (label.equalsIgnoreCase("reason")) {
					return ban.get(3);
				}
			}
		}
		return null;
	}
	private final Date checkdate(String label, String player) throws IOException {
		if (mysql == false) {
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
		} else {
			ArrayList<String> ban = sql.getBan(player);
	
			Date d = null;
			SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.FRANCE);
			try {
				if (label.equalsIgnoreCase("unbandate")) {
					d = date.parse(ban.get(2));
				} else if (label.equalsIgnoreCase("datebanned")) {
					d = date.parse(ban.get(1));
				}
			} catch (ParseException e) {e.printStackTrace();}
			return d;
		}
	}
}

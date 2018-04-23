package command;

import ch.canardconfit.skyban.SkyBan;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;

public class ConvertCommand extends Command {

	private boolean mysql;
	
	public ConvertCommand(String name, boolean mysql) {
		super(name);
		this.mysql = mysql;
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		if (args.length == 0) {

			sender.sendMessage(new TextComponent(SkyBan.main.changesymbole(SkyBan.main.messages.getString("convert-command.noargs.line0"))));
			sender.sendMessage(new TextComponent(SkyBan.main.changesymbole(SkyBan.main.messages.getString("convert-command.noargs.line1"))));
			sender.sendMessage(new TextComponent(SkyBan.main.changesymbole(SkyBan.main.messages.getString("convert-command.noargs.line2"))));
			sender.sendMessage(new TextComponent(SkyBan.main.changesymbole(SkyBan.main.messages.getString("convert-command.noargs.line3"))));
			sender.sendMessage(new TextComponent(SkyBan.main.changesymbole(SkyBan.main.messages.getString("convert-command.noargs.line4"))));
			sender.sendMessage(new TextComponent(SkyBan.main.changesymbole(SkyBan.main.messages.getString("convert-command.noargs.line5"))));
			sender.sendMessage(new TextComponent(SkyBan.main.changesymbole(SkyBan.main.messages.getString("convert-command.noargs.line6"))));
			sender.sendMessage(new TextComponent(SkyBan.main.changesymbole(SkyBan.main.messages.getString("convert-command.noargs.line7"))));
			sender.sendMessage(new TextComponent(SkyBan.main.changesymbole(SkyBan.main.messages.getString("convert-command.noargs.line8"))));
			sender.sendMessage(new TextComponent(SkyBan.main.changesymbole(SkyBan.main.messages.getString("convert-command.noargs.line9"))));
			sender.sendMessage(new TextComponent(SkyBan.main.changesymbole(SkyBan.main.messages.getString("convert-command.noargs.line10"))));
			
		} else if (args.length == 1) {
			
			if (args[0].equalsIgnoreCase("status")) {
				String status;
				String status2;
				if (mysql == true) {
					status = "§4false";
					status2 = "§atrue";
				} else {
					status = "§atrue";
					status2 = "§4false";
				}
				
				sender.sendMessage(new TextComponent(SkyBan.main.changesymbole(SkyBan.main.messages.getString("convert-command.status.line0"))));
				sender.sendMessage(new TextComponent(SkyBan.main.changesymbole(SkyBan.main.messages.getString("convert-command.status.line1"))));
				sender.sendMessage(new TextComponent(SkyBan.main.changesymbole(SkyBan.main.messages.getString("convert-command.status.line2"))));
				sender.sendMessage(new TextComponent(SkyBan.main.changesymbole(SkyBan.main.messages.getString("convert-command.status.line3"))));
				
				sender.sendMessage(new TextComponent(SkyBan.main.changesymbole(SkyBan.main.changecfgline(SkyBan.main.messages.getString("convert-command.status.line4"), "%file-status%", status))));
				sender.sendMessage(new TextComponent(SkyBan.main.changesymbole(SkyBan.main.changecfgline(SkyBan.main.messages.getString("convert-command.status.line5"), "%mysql-status%", status2))));
				
				sender.sendMessage(new TextComponent(SkyBan.main.changesymbole(SkyBan.main.messages.getString("convert-command.status.line6"))));
				sender.sendMessage(new TextComponent(SkyBan.main.changesymbole(SkyBan.main.messages.getString("convert-command.status.line7"))));
			} else if (args[0].equalsIgnoreCase("convert")) {

				sender.sendMessage(new TextComponent(SkyBan.main.changesymbole(SkyBan.main.messages.getString("convert-command.convert.line0"))));
				sender.sendMessage(new TextComponent(SkyBan.main.changesymbole(SkyBan.main.messages.getString("convert-command.convert.line1"))));
				sender.sendMessage(new TextComponent(SkyBan.main.changesymbole(SkyBan.main.messages.getString("convert-command.convert.line2"))));
				sender.sendMessage(new TextComponent(SkyBan.main.changesymbole(SkyBan.main.messages.getString("convert-command.convert.line3"))));
				sender.sendMessage(new TextComponent(SkyBan.main.changesymbole(SkyBan.main.messages.getString("convert-command.convert.line4"))));
				sender.sendMessage(new TextComponent(SkyBan.main.changesymbole(SkyBan.main.messages.getString("convert-command.convert.line5"))));
				sender.sendMessage(new TextComponent(SkyBan.main.changesymbole(SkyBan.main.messages.getString("convesrt-command.convert.line6"))));
			} else {
				sender.sendMessage(new TextComponent(SkyBan.main.changesymbole(SkyBan.main.messages.getString("utilisation-message.utilisation-skyconvert"))));
			}
			
		} else if (args.length >= 2) {
			if (args[0].equalsIgnoreCase("convert")) {
				if (mysql != true) {
					if (args.length == 5) {
						
						String host = args[1];
						String database = args[2];
						String user = args[3];
						String password;
						if (!args[4].equals("*@*")) {
							password = args[4];
						} else {
							password = "";
						}
						
						SkyBan.main.convertManager(sender, host, database, user, password);
						
					} else {
		
						sender.sendMessage(new TextComponent(SkyBan.main.changesymbole(SkyBan.main.messages.getString("convert-command.convert.line0"))));
						sender.sendMessage(new TextComponent(SkyBan.main.changesymbole(SkyBan.main.messages.getString("convert-command.convert.line1"))));
						sender.sendMessage(new TextComponent(SkyBan.main.changesymbole(SkyBan.main.messages.getString("convert-command.convert.line2"))));
						sender.sendMessage(new TextComponent(SkyBan.main.changesymbole(SkyBan.main.messages.getString("convert-command.convert.line3"))));
						sender.sendMessage(new TextComponent(SkyBan.main.changesymbole(SkyBan.main.messages.getString("convert-command.convert.line4"))));
						sender.sendMessage(new TextComponent(SkyBan.main.changesymbole(SkyBan.main.messages.getString("convert-command.convert.line5"))));
						sender.sendMessage(new TextComponent(SkyBan.main.changesymbole(SkyBan.main.messages.getString("convert-command.convert.line6"))));
					}
				} else {

					SkyBan.main.sendView(sender, SkyBan.main.changesymbole(SkyBan.main.messages.getString("convert-message.line2-error")));
				}
			} else {
				sender.sendMessage(new TextComponent(SkyBan.main.changesymbole(SkyBan.main.messages.getString("utilisation-message.utilisation-skyconvert"))));
				
			}
		}
	}
}
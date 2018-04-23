package command;

import ch.canardconfit.skyban.SkyBan;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.config.Configuration;

public class HelpCommand extends Command {

	public HelpCommand(String name) {
		super(name, "skyban.skyban");
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		Configuration msg = SkyBan.main.messages;
		SkyBan sky = SkyBan.main;
		sender.sendMessage(new TextComponent(sky.changesymbole(msg.getString("help-command.line0"))));
		sender.sendMessage(new TextComponent(sky.changesymbole(msg.getString("help-command.line1"))));
		sender.sendMessage(new TextComponent(sky.changesymbole(msg.getString("help-command.line2"))));
		sender.sendMessage(new TextComponent(sky.changesymbole(msg.getString("help-command.line3"))));
		sender.sendMessage(new TextComponent(sky.changesymbole(msg.getString("help-command.line4"))));
		sender.sendMessage(new TextComponent(sky.changesymbole(msg.getString("help-command.line5"))));
		sender.sendMessage(new TextComponent(sky.changesymbole(msg.getString("help-command.line6"))));
		sender.sendMessage(new TextComponent(sky.changesymbole(msg.getString("help-command.line7"))));
		sender.sendMessage(new TextComponent(sky.changesymbole(msg.getString("help-command.line8"))));
		sender.sendMessage(new TextComponent(sky.changesymbole(msg.getString("help-command.line9"))));
		sender.sendMessage(new TextComponent(sky.changesymbole(msg.getString("help-command.line10"))));
		sender.sendMessage(new TextComponent(sky.changesymbole(msg.getString("help-command.line11"))));
		sender.sendMessage(new TextComponent(sky.changesymbole(msg.getString("help-command.line12"))));
		sender.sendMessage(new TextComponent(sky.changesymbole(msg.getString("help-command.line13"))));
		sender.sendMessage(new TextComponent(sky.changesymbole(msg.getString("help-command.line14"))));
		sender.sendMessage(new TextComponent(sky.changesymbole(msg.getString("help-command.line15"))));
		sender.sendMessage(new TextComponent(sky.changesymbole(msg.getString("help-command.line16"))));
		sender.sendMessage(new TextComponent(sky.changesymbole(msg.getString("help-command.line17"))));
		sender.sendMessage(new TextComponent(sky.changesymbole(msg.getString("help-command.line18"))));

	}

}

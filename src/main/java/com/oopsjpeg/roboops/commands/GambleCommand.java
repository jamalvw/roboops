package com.oopsjpeg.roboops.commands;

import com.oopsjpeg.roboops.Roboops;
import com.oopsjpeg.roboops.framework.Bufferer;
import com.oopsjpeg.roboops.framework.RoEmote;
import com.oopsjpeg.roboops.framework.commands.Command;
import com.oopsjpeg.roboops.storage.UserWrapper;
import com.oopsjpeg.roboops.util.Util;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;

import java.util.concurrent.TimeUnit;

public class GambleCommand implements Command {
	@Override
	public int execute(IMessage message, String alias, String[] args) {
		if (args.length == 0) return INVALID_USAGE;

		IChannel channel = message.getChannel();
		IUser author = message.getAuthor();
		UserWrapper info = Roboops.getUser(author);

		if (info.isGambling())
			Bufferer.sendMessage(channel, RoEmote.ERROR + "**" + author.getName() + "**, "
					+ "you are already gambling.");
		else if (info.getMoney() <= 0)
			Bufferer.sendMessage(channel, RoEmote.ERROR + "**" + author.getName() + "**, "
					+ "you do not have any money.");
		else {
			try {
				int amount = Integer.parseInt(args[0]);
				if (amount <= 0)
					Bufferer.sendMessage(channel, RoEmote.ERROR + "**" + author.getName() + "**, "
							+ "the gamble must be higher than 0.");
				else if (info.getMoney() < amount)
					Bufferer.sendMessage(channel, RoEmote.ERROR + "**" + author.getName() + "**, "
							+ "you do not have **$" + Util.comma(amount) + "**.");
				else {
					info.setGambling(true);
					info.takeMoney(amount);
					IMessage gamble = Bufferer.sendMessage(channel, RoEmote.DICE + "**" + author.getName() + "** "
							+ "is gambling **$" + Util.comma(amount) + "**!");

					Roboops.SCHEDULER.schedule(() -> {
						int roll = Util.randInt(100);
						if (roll == 0) {
							info.giveMoney(amount);
							Bufferer.editMessage(gamble, RoEmote.DICE + "**" + author.getName() + "** "
									+ "rolled a(n) **" + roll + "** and kept their **$" + Util.comma(amount) + "**.");
						} else if (roll < 55) {
							Bufferer.editMessage(gamble, RoEmote.DICE + "**" + author.getName() + "** "
									+ "rolled a(n) **" + roll + "** and lost their **$" + Util.comma(amount) + "**.");
						} else if (roll < 96) {
							info.giveMoney(amount * 2);
							Bufferer.editMessage(gamble, RoEmote.DICE + "**" + author.getName() + "** "
									+ "rolled a(n) **" + roll + "** and earned **x2** their **$" + Util.comma(amount) + "**, "
									+ "equaling **$" + Util.comma(amount * 2) + "**.");
						} else {
							info.giveMoney(amount * 3);
							Bufferer.editMessage(gamble, RoEmote.DICE + "**" + author.getName() + "** "
									+ "rolled a(n) **" + roll + "** and earned **x3** their **$" + Util.comma(amount) + "**, "
									+ "equaling **$" + Util.comma(amount * 3) + "**.");
						}
						Roboops.getMongo().saveUser(info);
						info.setGambling(false);
					}, 2, TimeUnit.SECONDS);
				}
			} catch (NumberFormatException e) {
				return INVALID_USAGE;
			}
		}

		return SUCCESS;
	}

	@Override
	public String getName() {
		return "gamble";
	}

	@Override
	public String getDesc() {
		return "Gamble your money.";
	}

	@Override
	public String getUsage() {
		return "<amount>";
	}

	@Override
	public String[] getAliases() {
		return new String[]{"bet"};
	}
}

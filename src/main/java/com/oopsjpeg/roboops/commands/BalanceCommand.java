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

public class BalanceCommand implements Command {
	@Override
	public int execute(IMessage message, String alias, String[] args) {
		IChannel channel = message.getChannel();
		IUser author = message.getAuthor();
		UserWrapper info = Roboops.getUser(author);

		Bufferer.sendMessage(message.getChannel(), RoEmote.MONEY + "**" + author.getName() + "**, "
				+ "you have **$" + Util.comma(info.getMoney()) + "**.");

		return SUCCESS;
	}

	@Override
	public String getName() {
		return "balance";
	}

	@Override
	public String getDesc() {
		return "Check your balance.";
	}

	@Override
	public String[] getAliases() {
		return new String[]{"bal", "b", "$"};
	}
}

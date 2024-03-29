package com.oopsjpeg.roboops.commands;

import com.oopsjpeg.roboops.Roboops;
import com.oopsjpeg.roboops.framework.Bufferer;
import com.oopsjpeg.roboops.framework.commands.Command;
import com.oopsjpeg.roboops.util.Util;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;

public class ProfileCommand implements Command {
	@Override
	public int execute(IMessage message, String alias, String[] args) {
		IChannel channel = message.getChannel();
		IUser author = message.getAuthor();
		Bufferer.sendMessage(channel, "Showing **" + author.getName() + "**'s profile.",
				Util.getProfile(Roboops.getUser(author), message.getGuild()));
		return SUCCESS;
	}

	@Override
	public String getName() {
		return "profile";
	}

	@Override
	public String getDesc() {
		return "View a profile.";
	}

	@Override
	public String getUsage() {
		return "[user]";
	}

	@Override
	public String[] getAliases() {
		return new String[]{"p"};
	}
}

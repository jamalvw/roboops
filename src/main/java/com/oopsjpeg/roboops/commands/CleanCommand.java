package com.oopsjpeg.roboops.commands;

import com.oopsjpeg.roboops.Roboops;
import com.oopsjpeg.roboops.framework.Bufferer;
import com.oopsjpeg.roboops.framework.RoEmote;
import com.oopsjpeg.roboops.framework.commands.Command;
import com.oopsjpeg.roboops.util.Util;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.handle.obj.Permissions;
import sx.blah.discord.util.MessageHistory;

import java.util.EnumSet;
import java.util.List;
import java.util.stream.Collectors;

public class CleanCommand implements Command {
	@Override
	public int execute(IMessage message, String alias, String[] args) {
		IChannel channel = message.getChannel();
		IUser author = message.getAuthor();
		IUser bot = Roboops.getClient().getOurUser();

		if (channel.isPrivate())
			Bufferer.sendMessage(channel, RoEmote.ERROR + "**" + author.getName() + "**, "
					+ "this command can only be done in servers.");
		else {
			MessageHistory history = channel.getMessageHistory(100);

			// Remove if sent by the bot
			List<IMessage> messages = history.stream()
					.filter(m -> m.getAuthor().equals(bot))
					.collect(Collectors.toList());

			// Remove if message starts with prefix (if allowed)
			if (channel.getModifiedPermissions(bot).contains(Permissions.MANAGE_MESSAGES)) {
				messages.add(message);
				messages.addAll(history.stream()
						.filter(m -> m.getContent().startsWith(Roboops.getPrefix()))
						.collect(Collectors.toList()));
			}

			if (messages.size() == 1) messages.get(0).delete();
			else if (messages.size() > 1) channel.bulkDelete(messages);

			Bufferer.sendMessage(channel, RoEmote.SUCCESS + "**" + author.getName() + "** "
					+ "cleared **" + Util.comma(messages.size()) + "** message(s).");
		}

		return SUCCESS;
	}

	@Override
	public String getName() {
		return "clean";
	}

	@Override
	public String getDesc() {
		return "Clean messages from the channel.";
	}

	@Override
	public String[] getAliases() {
		return new String[]{"clean"};
	}

	@Override
	public EnumSet<Permissions> getPermissions() {
		return EnumSet.of(Permissions.MANAGE_MESSAGES);
	}
}

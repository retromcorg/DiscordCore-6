package org.retromc.discordcore.api;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import org.retromc.discordcore.v6.DiscordCorePlugin;

import java.util.logging.Level;

public class DiscordCoreAPI {
    // The goal of this class is to provide basic methods for sending messages wrapped around the JDA instance.
    // This should allow plugins to work across JDA versions without needing to update their code.

    private static JDA jda;
    private static DiscordCorePlugin plugin;

    public static void init(DiscordCorePlugin plugin, JDA jda) {
        DiscordCoreAPI.plugin = plugin;
        DiscordCoreAPI.jda = jda;
    }

    public static void clear() {
        DiscordCoreAPI.jda = null;
        DiscordCoreAPI.plugin = null;
    }

    // Send message to channel
    public static void sendMessage(long channelId, String message) {
        TextChannel channel = jda.getTextChannelById(channelId);
        if (channel != null) {
            channel.sendMessage(message).queue();
            return;
        }

        // Log error if channel is not found
        plugin.logger(Level.WARNING, "Attempted to send message to channel " + channelId + " but the channel was not found.");
    }

    public static void sendMessage(String channelId, String message) {
        sendMessage(Long.parseLong(channelId), message);
    }

    // Send message to user
    public static void sendMessageToUser(long userId, String message) {
        jda.retrieveUserById(userId).queue(user -> user.openPrivateChannel().queue(channel -> channel.sendMessage(message).queue()));
    }

    public static void sendMessageToUser(String userId, String message) {
        sendMessageToUser(Long.parseLong(userId), message);
    }

    // Send embed to channel
    public static void sendEmbed(long channelId, EmbedBuilder embed) {
        TextChannel channel = jda.getTextChannelById(channelId);
        if (channel != null) {
            channel.sendMessageEmbeds(embed.build()).queue();
            return;
        }

        // Log error if channel is not found
        plugin.logger(Level.WARNING, "Attempted to send embed to channel " + channelId + " but the channel was not found.");
    }


}

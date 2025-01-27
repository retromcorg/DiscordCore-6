package org.retromc.discordcore.v5;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;

import java.util.ArrayList;
import java.util.logging.Level;

public class DiscordBot extends ListenerAdapter {

    private DiscordCorePlugin plugin;
    private JDA jda;

    public DiscordBot(DiscordCorePlugin plugin) {
        this.plugin = plugin;
    }

    public void startDiscordBot(String token, ArrayList<GatewayIntent> gatewayIntents) {
        plugin.logger(Level.INFO, "Starting Discord Bot...");
        jda = JDABuilder.createDefault(token).enableIntents(gatewayIntents).setMemberCachePolicy(MemberCachePolicy.ALL).build();
        jda.addEventListener(this);
    }

    public void stopDiscordBot() {
        plugin.logger(Level.INFO, "Stopping Discord Bot...");
        jda.shutdownNow();
    }

    public JDA getJDA() {
        return jda;
    }

    public void onReady(ReadyEvent event) {
        plugin.logger(Level.INFO, "Discord Bot (" + event.getJDA().getSelfUser().getName() + ") connected to " + event.getGuildTotalCount() + " guilds.");
    }

    public void sendMessageToChannel(String channelId, String message) {
        jda.getTextChannelById(channelId).sendMessage(message).queue();
    }

}

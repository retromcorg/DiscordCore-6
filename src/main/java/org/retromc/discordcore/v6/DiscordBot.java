package org.retromc.discordcore.v6;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;

public class DiscordBot extends ListenerAdapter {

    private DiscordCorePlugin plugin;
    private JDA jda;

    public DiscordBot(DiscordCorePlugin plugin) {
        this.plugin = plugin;
    }

    public void startDiscordBot(String token, ArrayList<GatewayIntent> gatewayIntents) throws InterruptedException, TimeoutException {
        plugin.logger(Level.INFO, "Starting Discord Bot...");
        jda = JDABuilder.createDefault(token).enableIntents(gatewayIntents).setMemberCachePolicy(MemberCachePolicy.ALL).build();
        jda.addEventListener(this);

        long deadline = System.nanoTime() + TimeUnit.SECONDS.toNanos(30);
        while (jda.getStatus() != JDA.Status.CONNECTED) {
            JDA.Status status = jda.getStatus();
            if (status == JDA.Status.FAILED_TO_LOGIN || status == JDA.Status.SHUTDOWN || status == JDA.Status.SHUTTING_DOWN) {
                throw new IllegalStateException("Discord bot failed to connect (status: " + status + ")");
            }
            if (System.nanoTime() >= deadline) {
                throw new TimeoutException("Timed out waiting for the Discord bot to connect");
            }
            Thread.sleep(100);
        }
    }

    public void stopDiscordBot() {
        if (jda == null) {
            return;
        }

        plugin.logger(Level.INFO, "Stopping Discord Bot...");
        jda.shutdownNow();
        jda = null;
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

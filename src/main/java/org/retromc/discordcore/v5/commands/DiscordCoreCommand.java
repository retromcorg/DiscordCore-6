package org.retromc.discordcore.v5.commands;

import net.dv8tion.jda.api.JDA;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.retromc.discordcore.v5.DiscordConfig;
import org.retromc.discordcore.v5.DiscordCorePlugin;

public class DiscordCoreCommand implements CommandExecutor {

    private final DiscordCorePlugin plugin;

    private final DiscordConfig config;

    public DiscordCoreCommand(DiscordCorePlugin plugin) {
        this.plugin = plugin;
        this.config = plugin.getConfig();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("discordcore.command") && !sender.isOp()) {
            sender.sendMessage("You do not have permission to execute this command.");
            return true;
        }

        JDA jda = plugin.getDiscordBot().getJDA();

        // Print some information about the bot
        sender.sendMessage("DiscordCore Bot Information:");
        sender.sendMessage("  - Name: " + jda.getSelfUser().getName());
        sender.sendMessage("  - Discriminator: " + jda.getSelfUser().getDiscriminator());
        sender.sendMessage("  - ID: " + jda.getSelfUser().getId());
        sender.sendMessage("  - Status: " + jda.getStatus());
        sender.sendMessage("  - Guilds: " + jda.getGuilds().size());
        sender.sendMessage("  - Channels: " + jda.getTextChannels().size());
        sender.sendMessage("  - Users: " + jda.getUsers().size());
        sender.sendMessage("  - Gateway Ping: " + jda.getGatewayPing() + "ms");
        sender.sendMessage("  - Intents: " + jda.getGatewayIntents());

        return true;
    }
}

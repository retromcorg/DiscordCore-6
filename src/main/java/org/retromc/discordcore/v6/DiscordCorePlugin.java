package org.retromc.discordcore.v6;

import net.dv8tion.jda.api.requests.GatewayIntent;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.retromc.discordcore.api.DiscordCoreAPI;
import org.retromc.discordcore.v6.commands.DiscordCoreCommand;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DiscordCorePlugin extends JavaPlugin {
    private JavaPlugin plugin;
    private Logger log;
    private String pluginName;
    private PluginDescriptionFile pdf;

    private DiscordConfig configuration;

    // Discord Core
    private DiscordBot discordBot;

    private final int JDAVERSION = 6;


    @Override
    public void onEnable() {
        plugin = this;
        log = this.getServer().getLogger();
        pdf = this.getDescription();
        pluginName = pdf.getName();
        log.info("[" + pluginName + "] Is Loading, Version: " + pdf.getVersion());

        // Load configuration
        configuration = new DiscordConfig(this, new File(getDataFolder(), "config.yml")); // Load the configuration file from the plugin's data folder

        String token = configuration.getString("settings.discord-token.value", "INSERT_TOKEN"); // Get the token from the configuration file
        if (token == null || token.trim().isEmpty() || token.trim().equalsIgnoreCase("INSERT_TOKEN")) {
            log.warning("[" + pluginName + "] No token has been specified in the configuration file. Please specify a token and restart the server.");
            Bukkit.getServer().getPluginManager().disablePlugin(plugin);
            return;
        }
        token = token.trim();

        List<String> rawIntentList = configuration.getStringList("settings.discord-intents.value", Arrays.asList("GUILD_MEMBERS", "DIRECT_MESSAGES", "MESSAGE_CONTENT"));
        ArrayList<GatewayIntent> intents = new ArrayList<>();
        for (String str : rawIntentList) {
            if (str == null || str.trim().isEmpty()) {
                logger(Level.WARNING, "Ignoring an empty Discord gateway intent in the configuration.");
                continue;
            }

            GatewayIntent intent = null;

            try {
                intent = GatewayIntent.valueOf(str.trim());
            } catch (IllegalArgumentException e) {
                logger(Level.WARNING, "Ignoring unknown Discord gateway intent: " + str);
            }

            if (intent != null) intents.add(intent);
        }

        try {
            discordBot = new DiscordBot(this);
            discordBot.startDiscordBot(token, intents);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger(Level.WARNING, "Discord bot startup was interrupted.");
            Bukkit.getServer().getPluginManager().disablePlugin(plugin);
            return;
        } catch (Exception e) {
            logger(Level.WARNING, "Failed to start the Discord bot: " + e.getMessage());
            Bukkit.getServer().getPluginManager().disablePlugin(plugin);
            return;
        }

        DiscordCoreAPI.init(this, discordBot.getJDA());

        // Register the commands
        getCommand("discordcore").setExecutor(new DiscordCoreCommand(this));

        log.info("[" + pluginName + "] Is Loaded, Version: " + pdf.getVersion());
    }

    @Override
    public void onDisable() {
        log.info("[" + pluginName + "] Is Unloading, Version: " + pdf.getVersion());

        DiscordCoreAPI.clear();

        if (discordBot != null) {
            discordBot.stopDiscordBot();
            discordBot = null;
        }

        log.info("[" + pluginName + "] Is Unloaded, Version: " + pdf.getVersion());
    }

    public void logger(Level level, String message) {
        Bukkit.getLogger().log(level, "[" + plugin.getDescription().getName() + "] " + message);
    }

    public DiscordConfig getConfig() {
        return configuration;
    }

    public DiscordBot getDiscordBot() {
        return discordBot;
    }

    public int getJDAVersion() {
        return JDAVERSION;
    }
}

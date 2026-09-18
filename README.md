# DiscordCore 6

DiscordCore is a Project Poseidon plugin that provides a shared JDA 6 Discord bot for other Bukkit plugins.

## Requirements

- Java 25
- Project Poseidon 2.0 or newer
- A Discord bot token

## Installation

1. Download the latest release JAR and place it in the server's `plugins` directory.
2. Start the server once to generate `plugins/DiscordCore-6/config.yml`.
3. Set `settings.discord-token.value` to your Discord bot token.
4. Review `settings.discord-intents.value`, enable the corresponding intents in the Discord Developer Portal, and restart the server.

The default intents are `GUILD_MEMBERS`, `DIRECT_MESSAGES`, and `MESSAGE_CONTENT`.

## Usage

Run `/discordcore` to display the connected bot's status. The command uses the `discordcore.command` permission, which is granted by default.

Other plugins can send messages through the stable wrapper API:

```java
import org.retromc.discordcore.api.DiscordCoreAPI;

DiscordCoreAPI.sendMessage(123456789012345678L, "Hello from Poseidon!");
DiscordCoreAPI.sendMessageToUser(123456789012345678L, "Hello!");
```

## Building

Build the shaded plugin JAR with Java 25:

```shell
mvn clean package
```

The resulting JAR is written to `target/`.

## License

DiscordCore is available under the [MIT License](LICENSE).

package dev.naw0ke.wrujoin.listener;

import dev.naw0ke.wrujoin.WruJoinPlugin;
import dev.naw0ke.wrujoin.config.YamlConfig;
import dev.naw0ke.wrujoin.util.ChatUtils;
import dev.naw0ke.wrujoin.util.VanishUtils;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.title.Title;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.List;

@RequiredArgsConstructor
public class ConnectionListener implements Listener {

    private final WruJoinPlugin plugin;

    @EventHandler(priority = EventPriority.MONITOR)
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        event.joinMessage(null);

        boolean firstJoin = !player.hasPlayedBefore();
        int uniqueJoinCount = firstJoin ? this.plugin.nextUniqueJoinCount() : -1;
        String uniqueJoin = firstJoin ? "#" + uniqueJoinCount : "";

        if (!VanishUtils.isVanished(player)) {
            ChatUtils.sendTypedMessage(this.plugin.getServer(), this.plugin.getConfig(),
                    "settings.join-message",
                    Placeholder.unparsed("player", player.getName()),
                    Placeholder.unparsed("uniquejoin", uniqueJoin)
            );
        }

        YamlConfig config = this.plugin.getConfig();

        String titlePath = (firstJoin && config.getBoolean("settings.first-join.title.enabled"))
                ? "settings.first-join.title"
                : "settings.title";

        if (config.getBoolean(titlePath + ".enabled")) {
            String titleText = config.getString(titlePath + ".title");
            String subtitleText = config.getString(titlePath + ".subtitle");
            Title title = Title.title(
                    ChatUtils.format(titleText,
                            Placeholder.unparsed("player", player.getName()),
                            Placeholder.unparsed("uniquejoin", uniqueJoin)),
                    ChatUtils.format(subtitleText,
                            Placeholder.unparsed("player", player.getName()),
                            Placeholder.unparsed("uniquejoin", uniqueJoin))
            );
            player.showTitle(title);
        }

        String soundPath = (firstJoin && config.getBoolean("settings.first-join.sound.enabled"))
                ? "settings.first-join.sound"
                : "settings.sound";

        if (config.getBoolean(soundPath + ".enabled")) {
            String soundKey = config.getString(soundPath + ".sound");
            float soundPitch = (float) config.getDouble(soundPath + ".pitch", 1.0f);
            player.playSound(Sound.sound(Key.key(soundKey), Sound.Source.MASTER, 1.0f, soundPitch));
        }

        if (firstJoin && config.getBoolean("settings.first-join.message.enabled")) {
            List<String> lines = config.getStringList("settings.first-join.message.lines");
            if (!lines.isEmpty()) {
                ChatUtils.sendMessage(player, ChatUtils.format(lines,
                        Placeholder.unparsed("player", player.getName()),
                        Placeholder.unparsed("uniquejoin", uniqueJoin)
                ));
            }
        }

        if (config.getBoolean("settings.motd.enabled")) {
            List<String> lines = config.getStringList("settings.motd.lines");
            if (!lines.isEmpty()) {
                player.getScheduler().run(this.plugin, task -> ChatUtils.sendMessage(player, ChatUtils.format(lines,
                        Placeholder.unparsed("player", player.getName())
                )), null);
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        event.quitMessage(null);

        if (!VanishUtils.isVanished(player)) {
            ChatUtils.sendTypedMessage(this.plugin.getServer(), this.plugin.getConfig(),
                    "settings.quit-message",
                    Placeholder.unparsed("player", player.getName())
            );
        }
    }

}

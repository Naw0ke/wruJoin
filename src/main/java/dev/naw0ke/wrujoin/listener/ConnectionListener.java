package dev.naw0ke.wrujoin.listener;

import dev.naw0ke.wrujoin.WruJoinPlugin;
import dev.naw0ke.wrujoin.util.ChatUtils;
import dev.naw0ke.wrujoin.util.VanishUtils;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
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

    private int count = Bukkit.getOfflinePlayers().length;

    @EventHandler(priority = EventPriority.MONITOR)
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        event.joinMessage(null);

        boolean firstJoin = !player.hasPlayedBefore();
        if (firstJoin) count++;

        if (!VanishUtils.isVanished(player)) {
            ChatUtils.sendTypedMessage(plugin.getServer(), plugin.getConfig(),
                    "settings.join-message",
                    Placeholder.unparsed("player", player.getName()),
                    Placeholder.unparsed("uniquejoin", firstJoin ? "#" + count : "")
            );
        }

        String titlePath = (firstJoin && plugin.getConfig().getBoolean("settings.first-join.title.enabled"))
                ? "settings.first-join.title"
                : "settings.title";

        if (plugin.getConfig().getBoolean(titlePath + ".enabled")) {
            String titleText = plugin.getConfig().getString(titlePath + ".title");
            String subtitleText = plugin.getConfig().getString(titlePath + ".subtitle");
            Title title = Title.title(
                    ChatUtils.format(titleText,
                            Placeholder.unparsed("player", player.getName()),
                            Placeholder.unparsed("uniquejoin", firstJoin ? "#" + count : "")),
                    ChatUtils.format(subtitleText,
                            Placeholder.unparsed("player", player.getName()),
                            Placeholder.unparsed("uniquejoin", firstJoin ? "#" + count : ""))
            );
            player.showTitle(title);
        }

        String soundPath = (firstJoin && plugin.getConfig().getBoolean("settings.first-join.sound.enabled"))
                ? "settings.first-join.sound"
                : "settings.sound";

        if (plugin.getConfig().getBoolean(soundPath + ".enabled")) {
            String soundKey = plugin.getConfig().getString(soundPath + ".sound");
            float soundPitch = (float) plugin.getConfig().getDouble(soundPath + ".pitch", 1.0f);
            player.playSound(Sound.sound(Key.key(soundKey), Sound.Source.MASTER, 1.0f, soundPitch));
        }

        if (firstJoin && plugin.getConfig().getBoolean("settings.first-join.message.enabled")) {
            List<String> lines = plugin.getConfig().getStringList("settings.first-join.message.lines");
            if (!lines.isEmpty()) {
                ChatUtils.sendMessage(player, ChatUtils.format(lines,
                        Placeholder.unparsed("player", player.getName()),
                        Placeholder.unparsed("uniquejoin", "#" + count)
                ));
            }
        }

        if (plugin.getConfig().getBoolean("settings.motd.enabled")) {
            List<String> lines = plugin.getConfig().getStringList("settings.motd.lines");
            if (!lines.isEmpty()) {
                player.getScheduler().run(plugin, task -> ChatUtils.sendMessage(player, ChatUtils.format(lines,
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
            ChatUtils.sendTypedMessage(plugin.getServer(), plugin.getConfig(),
                    "settings.quit-message",
                    Placeholder.unparsed("player", player.getName())
            );
        }
    }

}

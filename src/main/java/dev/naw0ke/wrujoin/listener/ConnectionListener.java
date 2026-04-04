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

@RequiredArgsConstructor
public class ConnectionListener implements Listener {

    private final WruJoinPlugin plugin;

    private int count = Bukkit.getOfflinePlayers().length;

    @EventHandler(priority = EventPriority.MONITOR)
    public void onJoin(PlayerJoinEvent event) {;
        Player player = event.getPlayer();

        event.joinMessage(null);

         boolean firstJoin = !player.hasPlayedBefore();
         if (firstJoin)
             count++;

        if (!VanishUtils.isVanished(player)) {
            ChatUtils.sendTypedMessage(plugin.getServer(), plugin.getConfig(),
                    "settings.join-message",
                    Placeholder.unparsed("player", player.getName()),
                    Placeholder.unparsed("uniquejoin", firstJoin ? "#" + count : "")
            );
        }

        if (plugin.getConfig().getBoolean("settings.title.enabled")) {
            String titleText = plugin.getConfig().getString("settings.title.title");
            String subtitleText = plugin.getConfig().getString("settings.title.subtitle");
            Title title = Title.title(
                    ChatUtils.format(titleText, Placeholder.unparsed("player", player.getName())),
                    ChatUtils.format(subtitleText, Placeholder.unparsed("player", player.getName()))
            );
            player.showTitle(title);
        }

        if (plugin.getConfig().getBoolean("settings.sound.enabled")) {
            String soundKey = plugin.getConfig().getString("settings.sound.sound");
            float soundPitch = (float)plugin.getConfig().getDouble("settings.join-sound.pitch", 1.0f);
            player.playSound(Sound.sound(Key.key(soundKey), Sound.Source.MASTER, 1.0f, soundPitch));
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

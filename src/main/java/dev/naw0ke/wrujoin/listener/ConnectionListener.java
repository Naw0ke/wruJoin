package dev.naw0ke.wrujoin.listener;

import dev.naw0ke.wrujoin.WruJoinPlugin;
import dev.naw0ke.wrujoin.util.ChatUtils;
import dev.naw0ke.wrujoin.util.VanishUtils;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

@RequiredArgsConstructor
public class ConnectionListener implements Listener {

    private final WruJoinPlugin plugin;

    @EventHandler(ignoreCancelled = true)
    public void onJoin(PlayerJoinEvent event) {;
        Player player = event.getPlayer();

        event.joinMessage(null);

        if(!VanishUtils.isVanished(player)) {
            plugin.getServer().sendActionBar(
                    ChatUtils.format(plugin.getConfig().getString("settings.join-message"),
                            Placeholder.unparsed("player", player.getName())
                    ));
        }

        String soundKey = plugin.getConfig().getString("settings.join-sound.sound");
        if (soundKey != null && !soundKey.isEmpty()) {
            float soundPitch = (float)plugin.getConfig().getDouble("settings.join-sound.pitch", 1.0f);
            player.playSound(Sound.sound(Key.key(soundKey), Sound.Source.MASTER, 1.0f, soundPitch));
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        event.quitMessage(null);

        if(!VanishUtils.isVanished(player)) {
            plugin.getServer().sendActionBar(
                    ChatUtils.format(plugin.getConfig().getString("settings.quit-message"),
                            Placeholder.unparsed("player", player.getName())
                    ));
        }
    }

}

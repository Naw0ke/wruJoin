package dev.naw0ke.wrusmp.listener;

import dev.naw0ke.wrusmp.WruJoinPlugin;
import dev.naw0ke.wrusmp.util.ChatUtils;
import dev.naw0ke.wrusmp.util.VanishUtils;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

@RequiredArgsConstructor
public class JoinListener implements Listener {

    private final WruJoinPlugin plugin;

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {;
        Player player = event.getPlayer();

        String soundKey = plugin.getConfig().getString("settings.join-sound");
        if (soundKey != null && !soundKey.isEmpty()) {
            player.playSound(Sound.sound(Key.key(soundKey), Sound.Source.MASTER, 1.0f, 1.0f));
        }

        if(VanishUtils.isVanished(player)) {
            for (Player online : Bukkit.getOnlinePlayers()) {
                online.sendActionBar(
                        ChatUtils.format(plugin.getConfig().getString("settings.join-message"),
                                Placeholder.parsed("player", player.getName())
                        ));
            }
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        if(VanishUtils.isVanished(player)) {
            for (Player online : Bukkit.getOnlinePlayers()) {
                online.sendActionBar(
                        ChatUtils.format(plugin.getConfig().getString("settings.quit-message"),
                                Placeholder.unparsed("player", player.getName())
                        ));
            }
        }
    }
}

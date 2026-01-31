package dev.naw0ke.wrusmp.listener;

import de.myzelyam.api.vanish.PlayerHideEvent;
import de.myzelyam.api.vanish.PlayerShowEvent;
import dev.naw0ke.wrusmp.WruJoinPlugin;
import dev.naw0ke.wrusmp.util.ChatUtils;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

@RequiredArgsConstructor
public class VanishListener implements Listener {

    private final WruJoinPlugin plugin;

    @EventHandler
    public void onVanish(PlayerHideEvent event) {
        Player player = event.getPlayer();

        plugin.getServer().sendActionBar(
                ChatUtils.format(plugin.getConfig().getString("settings.quit-message"),
                        Placeholder.unparsed("player", player.getName())
                ));
    }

    @EventHandler
    public void onUnVanish(PlayerShowEvent event) {
        Player player = event.getPlayer();

        plugin.getServer().sendActionBar(
                ChatUtils.format(plugin.getConfig().getString("settings.join-message"),
                        Placeholder.unparsed("player", player.getName())
                ));
    }

}

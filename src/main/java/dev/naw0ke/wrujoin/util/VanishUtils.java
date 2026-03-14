package dev.naw0ke.wrujoin.util;

import de.myzelyam.api.vanish.VanishAPI;
import dev.naw0ke.wrujoin.WruJoinPlugin;
import org.bukkit.entity.Player;

public class VanishUtils {

    public static boolean isVanished(Player player) {
        if (!WruJoinPlugin.getInstance().getConfig().getBoolean("settings.vanish-check")
                || !isVanishPluginEnabled()) {
            return false;
        }

        return VanishAPI.isInvisible(player);
    }

    public static boolean isVanishPluginEnabled() {
        return WruJoinPlugin.getInstance()
                .getServer()
                .getPluginManager()
                .isPluginEnabled("SuperVanish");
    }

}

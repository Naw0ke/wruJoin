package dev.naw0ke.wrujoin.util;

import dev.naw0ke.wrujoin.WruJoinPlugin;
import dev.naw0ke.wrujoin.config.YamlConfig;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;

import java.util.List;
import java.util.stream.Collectors;

public class ChatUtils {

    private final static MiniMessage MINI_MESSAGE = MiniMessage.builder()
            .tags(TagResolver.standard())
            .postProcessor(component -> component.decoration(TextDecoration.ITALIC, false))
            .build();

    public static Component format(String string, TagResolver... placeholders) {
        return MINI_MESSAGE.deserialize(string, placeholders);
    }

    public static List<Component> format(List<String> list, TagResolver... placeholders) {
        return list.stream().map(s -> MINI_MESSAGE.deserialize(s, placeholders)).collect(Collectors.toList());
    }

    public static void sendMessage(CommandSender player, Component component) {
        player.sendMessage(component);
    }

    public static void sendMessage(CommandSender player, List<Component> components) {
        components.forEach(s -> ChatUtils.sendMessage(player, s));
    }

    public static void sendTypedMessage(Server server, YamlConfig config, String path, TagResolver... placeholders) {
        String type = config.getString(path + ".type", "ACTIONBAR");
        String message = config.getString(path + ".message", "");

        if (message.isEmpty()) return;

        Component formatted = format(message, placeholders);

        switch (type.toUpperCase()) {
            case "CHAT" -> server.sendMessage(formatted);
            case "ACTIONBAR" -> server.sendActionBar(formatted);
            default -> WruJoinPlugin.getInstance().getLogger().warning("Unknown message type '" + type + "' at " + path);
        }
    }

}

package dev.naw0ke.wrujoin.command;

import dev.naw0ke.wrujoin.WruJoinPlugin;
import dev.naw0ke.wrujoin.util.ChatUtils;
import dev.triumphteam.cmd.bukkit.annotation.Permission;
import dev.triumphteam.cmd.core.BaseCommand;
import dev.triumphteam.cmd.core.annotation.Command;
import dev.triumphteam.cmd.core.annotation.Default;
import dev.triumphteam.cmd.core.annotation.SubCommand;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import org.bukkit.command.CommandSender;

import java.util.List;

@RequiredArgsConstructor
@Command("wrujoin")
@Permission("wrujoin.admin")
public class MainCommand extends BaseCommand {

    private final WruJoinPlugin plugin;

    @Default
    public void onDefault(CommandSender sender) {
        ChatUtils.sendMessage(sender, ChatUtils.format(List.of("", "<bold><#D36E97>ᴡʀᴜᴊᴏɪɴ</bold> <gray>- <#FFA8CB>v" +
                this.plugin.getPluginMeta().getVersion(), "<gray>Made by Naw0ke", "",
                "<#FFA8CB>Use /wrujoin reload to reload the configuration.", "")));
        sender.playSound(Sound.sound(Key.key("block.note_block.chime"), Sound.Source.MASTER, 1.0f, 1.0f));
    }

    @SubCommand("reload")
    public void onReload(CommandSender sender) {
        plugin.getConfig().reload();

        ChatUtils.sendMessage(sender, ChatUtils.format(plugin.getConfig().getString("messages.reloaded")));
        sender.playSound(Sound.sound(Key.key("entity.villager.yes"), Sound.Source.MASTER, 1.0f, 1.0f));
    }

}

package dev.naw0ke.wrusmp;

import dev.naw0ke.wrusmp.command.MainCommand;
import dev.naw0ke.wrusmp.config.YamlConfig;
import dev.naw0ke.wrusmp.listener.JoinListener;
import dev.naw0ke.wrusmp.listener.VanishListener;
import dev.naw0ke.wrusmp.util.ChatUtils;
import dev.naw0ke.wrusmp.util.VanishUtils;
import dev.triumphteam.cmd.bukkit.BukkitCommandManager;
import dev.triumphteam.cmd.bukkit.message.BukkitMessageKey;
import dev.triumphteam.cmd.core.exceptions.CommandRegistrationException;
import dev.triumphteam.cmd.core.message.MessageKey;
import lombok.Getter;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

@Getter
public final class WruJoinPlugin extends JavaPlugin {

    @Getter
    private static WruJoinPlugin instance;

    private final YamlConfig config = new YamlConfig(this, "config.yml", true);

    private BukkitCommandManager<CommandSender> commandManager;

    @Override
    public void onEnable() {
        instance = this;
        config.create();
        registerCommands();
        this.getServer().getPluginManager().registerEvents(new JoinListener(this), this);
        if (VanishUtils.isVanishPluginEnabled()) {
            this.getServer().getPluginManager().registerEvents(new VanishListener(this), this);
            Bukkit.getConsoleSender().sendMessage("[WruJoin] [+] SuperVanish hook enabled.");
        }
        Bukkit.getConsoleSender().sendMessage("[WruJoin] [?] Plugin enabled. Developed by Naw0ke.");
        new Metrics(this, 27905);
    }

    @Override
    public void onDisable() {
        List.of("wrujoin").forEach(this::unregisterCommand);
        Bukkit.getConsoleSender().sendMessage("[WruJoin] [!] Plugin disabled. Developed by Naw0ke.");
    }

    private void unregisterCommand(String name) {
        this.getBukkitCommands(this.getCommandMap()).remove(name);
    }

    @NotNull
    private CommandMap getCommandMap() {
        try {
            final Server server = Bukkit.getServer();
            final Method getCommandMap = server.getClass().getDeclaredMethod("getCommandMap");
            getCommandMap.setAccessible(true);

            return (CommandMap) getCommandMap.invoke(server);
        } catch (final Exception ignored) {
            throw new CommandRegistrationException("Unable get Command Map. Commands will not be registered!");
        }
    }

    // copied from triumph-cmd, credit goes to triumph-team
    @NotNull
    private Map<String, Command> getBukkitCommands(@NotNull final CommandMap commandMap) {
        try {
            final Field bukkitCommands = SimpleCommandMap.class.getDeclaredField("knownCommands");
            bukkitCommands.setAccessible(true);
            //noinspection unchecked
            return (Map<String, Command>) bukkitCommands.get(commandMap);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new CommandRegistrationException("Unable get Bukkit commands. Commands might not be registered correctly!");
        }
    }

    private void registerCommands() {
        commandManager = BukkitCommandManager.create(this);

        commandManager.registerCommand(new MainCommand(this));

        commandManager.registerMessage(MessageKey.INVALID_ARGUMENT, (sender, invalidArgumentContext) ->
                ChatUtils.sendMessage(sender, ChatUtils.format(config.getString("messages.unknown-command"))));
        commandManager.registerMessage(MessageKey.UNKNOWN_COMMAND, (sender, invalidArgumentContext) ->
                ChatUtils.sendMessage(sender, ChatUtils.format(config.getString("messages.unknown-command"))));
        commandManager.registerMessage(MessageKey.NOT_ENOUGH_ARGUMENTS, (sender, invalidArgumentContext) ->
                ChatUtils.sendMessage(sender, ChatUtils.format(config.getString("messages.unknown-command"))));
        commandManager.registerMessage(MessageKey.TOO_MANY_ARGUMENTS, (sender, invalidArgumentContext) ->
                ChatUtils.sendMessage(sender, ChatUtils.format(config.getString("messages.unknown-command"))));
        commandManager.registerMessage(BukkitMessageKey.NO_PERMISSION, (sender, invalidArgumentContext) ->
                ChatUtils.sendMessage(sender, ChatUtils.format(config.getString("messages.no-permission"))));
    }
}

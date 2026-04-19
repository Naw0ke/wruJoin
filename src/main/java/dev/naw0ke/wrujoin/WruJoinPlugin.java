package dev.naw0ke.wrujoin;

import dev.naw0ke.wrujoin.command.MainCommand;
import dev.naw0ke.wrujoin.config.YamlConfig;
import dev.naw0ke.wrujoin.listener.ConnectionListener;
import dev.naw0ke.wrujoin.listener.VanishListener;
import dev.naw0ke.wrujoin.util.ChatUtils;
import dev.naw0ke.wrujoin.util.VanishUtils;
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
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Getter
public final class WruJoinPlugin extends JavaPlugin {

    @Getter
    private static WruJoinPlugin instance;

    private final YamlConfig config = new YamlConfig(this, "config.yml", true);
    private final AtomicInteger uniqueJoinCounter = new AtomicInteger();

    private BukkitCommandManager<CommandSender> commandManager;

    @Override
    public void onEnable() {
        instance = this;

        config.create();
        uniqueJoinCounter.set(config.getInt("internal.unique-join-counter", 0));

        registerCommands();

        this.getServer().getPluginManager().registerEvents(new ConnectionListener(this), this);
        if (VanishUtils.isVanishPluginEnabled() && config.getBoolean("settings.vanish-check",true)) {
            this.getServer().getPluginManager().registerEvents(new VanishListener(this), this);
            this.getLogger().info("[+] SuperVanish hook enabled.");
        }

        this.getLogger().info("Plugin enabled. Developed by Naw0ke.");

        new Metrics(this, 27905);
    }

    @Override
    public void onDisable() {
        getBukkitCommands(getCommandMap()).remove("wrujoin");
        this.getLogger().info("Plugin disabled. Developed by Naw0ke.");
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

    public void reloadPluginConfig() {
        config.reload();
        uniqueJoinCounter.set(config.getInt("internal.unique-join-counter", 0));
    }

    public int nextUniqueJoinCount() {
        int updated = uniqueJoinCounter.incrementAndGet();
        config.set("internal.unique-join-counter", updated);
        config.persist();
        return updated;
    }

}

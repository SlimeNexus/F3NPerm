package de.redgames.f3nperm;

import de.redgames.f3nperm.hooks.Hook;
import de.redgames.f3nperm.hooks.LuckPermsHook;
import de.redgames.f3nperm.provider.*;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public final class F3NPermPlugin extends JavaPlugin implements Listener {
    private final Hook[] hooks = new Hook[] {
            new LuckPermsHook()
    };

    private Provider provider;
    private Settings settings;
    private List<Hook> registeredHooks;

    @Override
    public void onLoad() {
        BukkitVersion bukkitVersion = BukkitVersion.fromBukkitVersion();

        if (bukkitVersion == null) {
            String version = Bukkit.getServer().getBukkitVersion();
            getLogger().warning("Could not recognize server version, proceed with caution! (Bukkit version: " + version + ")");
        } else {
            getLogger().info("Server version " + bukkitVersion + " detected");
        }

        loadSettings();

        provider = findProvider(bukkitVersion);

        getLogger().info("Provider " + provider.getClass().getSimpleName() + " loaded!");

        getLogger().info("Plugin loaded!");
    }

    @Override
    public void onEnable() {
        F3NPermCommand f3nPermCommand = new F3NPermCommand(this);
        getCommand("f3nperm").setExecutor(f3nPermCommand);
        getCommand("f3nperm").setTabCompleter(f3nPermCommand);

        getServer().getPluginManager().registerEvents(new F3NPermListener(this), this);

        try {
            provider.register(this);
        } catch (ProviderException e) {
            getLogger().log(Level.SEVERE, "Could not register provider " + provider.getClass().getSimpleName() + "!", e);
        }

        loadHooks();

        getLogger().info("Plugin enabled!");
    }

    @Override
    public void onDisable() {
        if (registeredHooks != null) {
            for (Hook hook : registeredHooks) {
                hook.unregister(this);
            }
        }

        try {
            provider.unregister(this);
        } catch (ProviderException e) {
            getLogger().log(Level.SEVERE, "Could not unregister provider " + provider.getClass().getSimpleName() + "!", e);
        }

        getLogger().info("Plugin disabled!");
    }

    public void reloadPlugin() {
        for (Hook hook : registeredHooks) {
            hook.unregister(this);
        }

        loadSettings();
        loadHooks();

        for (Player player : getServer().getOnlinePlayers()) {
            provider.update(player);
        }
    }

    private void loadSettings() {
        try {
            settings = Settings.loadSettings(this, getDataFolder().toPath());
        } catch (IOException e) {
            getLogger().log(Level.SEVERE, "Error loading configuration!", e);
            throw new RuntimeException();
        }
    }

    private void loadHooks() {
        registeredHooks = new ArrayList<>();

        for (Hook hook : hooks) {
            if (getSettings().getHooks().contains(hook.getName())) {
                hook.register(this);
                registeredHooks.add(hook);
            }
        }
    }

    private Provider findProvider(BukkitVersion bukkitVersion) {
        if (getServer().getPluginManager().getPlugin("ProtocolLib") != null) {
            if (settings.isUseProtocolLib()) {
                return new ProtocolLibProvider();
            }
        }

        // Just use the latest provider when we can't find a suitable one;
        if (bukkitVersion == null || bukkitVersion.isGreaterOrEqualTo(BukkitVersion.V1_20_2)) {
            return new ReflectionProvider_1_20_2();
        }

        if (bukkitVersion.isGreaterOrEqualTo(BukkitVersion.V1_20)) {
            return new ReflectionProvider_1_20();
        }

        if (bukkitVersion.isGreaterOrEqualTo(BukkitVersion.V1_19_4)) {
            return new ReflectionProvider_1_19_4();
        }

        if (bukkitVersion.isGreaterOrEqualTo(BukkitVersion.V1_19)) {
            return new ReflectionProvider_1_19();
        }

        if (bukkitVersion.isGreaterOrEqualTo(BukkitVersion.V1_18_2)) {
            return new ReflectionProvider_1_18_2();
        }

        if (bukkitVersion.isGreaterOrEqualTo(BukkitVersion.V1_18)) {
            return new ReflectionProvider_1_18();
        }

        if (bukkitVersion.isGreaterOrEqualTo(BukkitVersion.V1_17)) {
            return new ReflectionProvider_1_17();
        }

        return new ReflectionProvider_1_9();
    }

    public OpPermissionLevel getF3NPermPermissionLevel(Player player) {
        if (!settings.isEnablePermissionCheck() ||
                player.hasPermission("f3nperm.use") ||
                player.hasPermission("F3NPerm.use") ||
                player.isOp()) {
            return settings.getOpPermissionLevel();
        }

        return OpPermissionLevel.NO_PERMISSIONS;
    }

    public Provider getProvider() {
        return provider;
    }

    public Settings getSettings() {
        return settings;
    }
}

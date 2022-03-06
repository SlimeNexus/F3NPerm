package de.redgames.f3nperm;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

// Deprecation is the Server#getPlayerExact(String). It seems like that method was accidentally
// deprecated in 1.8 and the deprecation was removed again in newer versions.
@SuppressWarnings("deprecated")
public final class F3NPermCommand implements CommandExecutor, TabCompleter {
    private final F3NPermPlugin plugin;

    public F3NPermCommand(F3NPermPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 1) {
            return false;
        }

        if (args[0].equalsIgnoreCase("reload")) {
            plugin.reloadPlugin();
            sender.sendMessage(ChatColor.GREEN + "Plugin successfully reloaded!");
            return true;
        }

        if (args[0].equalsIgnoreCase("forceupdate")) {
            if (args.length < 2) {
                for (Player player : plugin.getServer().getOnlinePlayers()) {
                    plugin.getProvider().update(player);
                }

                sender.sendMessage(ChatColor.GREEN + "Updated all online players");
                return true;
            }

            Player target = plugin.getServer().getPlayerExact(args[1]);

            if (target == null) {
                sender.sendMessage(ChatColor.RED + "A player named " + args[1] + " was not found!");
                return true;
            }

            plugin.getProvider().update(target);
            sender.sendMessage(ChatColor.GREEN + "Updated player " + args[1] + "");
            return true;
        }

        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> candidates = null;

        if (args.length == 1) {
            candidates = Arrays.asList("reload", "forceupdate");
        }

        if (args.length == 2 && args[0].equalsIgnoreCase("forceupdate")) {
            candidates = plugin.getServer().getOnlinePlayers().stream()
                    .map(Player::getName)
                    .collect(Collectors.toList());
        }

        if (candidates == null) {
            return Collections.emptyList();
        }

        return candidates.stream()
                .filter(c -> c.toLowerCase().startsWith(args[args.length - 1].toLowerCase()))
                .sorted()
                .collect(Collectors.toList());
    }
}

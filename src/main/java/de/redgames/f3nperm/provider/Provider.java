package de.redgames.f3nperm.provider;

import de.redgames.f3nperm.F3NPermPlugin;
import org.bukkit.entity.Player;

public interface Provider {
    void register(F3NPermPlugin plugin);

    void unregister(F3NPermPlugin plugin);

    void update(Player player);
}

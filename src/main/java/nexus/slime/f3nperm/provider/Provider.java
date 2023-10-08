package nexus.slime.f3nperm.provider;

import nexus.slime.f3nperm.F3NPermPlugin;
import org.bukkit.entity.Player;

public interface Provider {
    void register(F3NPermPlugin plugin);

    void unregister(F3NPermPlugin plugin);

    void update(Player player);
}

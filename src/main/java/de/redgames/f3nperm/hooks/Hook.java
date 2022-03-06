package de.redgames.f3nperm.hooks;

import de.redgames.f3nperm.F3NPermPlugin;

public interface Hook {
    String getName();

    void register(F3NPermPlugin plugin);

    void unregister(F3NPermPlugin plugin);
}

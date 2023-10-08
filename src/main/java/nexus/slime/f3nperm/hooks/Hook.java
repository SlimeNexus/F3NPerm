package nexus.slime.f3nperm.hooks;

import nexus.slime.f3nperm.F3NPermPlugin;

public interface Hook {
    String getName();

    void register(F3NPermPlugin plugin);

    void unregister(F3NPermPlugin plugin);
}

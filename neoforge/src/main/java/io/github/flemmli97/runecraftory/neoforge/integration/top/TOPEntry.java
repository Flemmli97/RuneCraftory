package io.github.flemmli97.runecraftory.neoforge.integration.top;

import io.github.flemmli97.runecraftory.forge.integration.top.BlockProvider;
import io.github.flemmli97.runecraftory.forge.integration.top.EntityProbeProvider;
import mcjty.theoneprobe.api.ITheOneProbe;

import java.util.function.Function;

public class TOPEntry implements Function<ITheOneProbe, Void> {

    @Override
    public Void apply(ITheOneProbe input) {
        input.registerEntityProvider(new EntityProbeProvider());
        input.registerProvider(new BlockProvider());
        return null;
    }
}

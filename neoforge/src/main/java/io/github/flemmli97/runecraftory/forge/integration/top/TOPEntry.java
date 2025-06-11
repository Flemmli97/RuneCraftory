package io.github.flemmli97.runecraftory.forge.integration.top;

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

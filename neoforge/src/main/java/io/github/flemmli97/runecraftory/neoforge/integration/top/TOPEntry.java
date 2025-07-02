package io.github.flemmli97.runecraftory.neoforge.integration.top;

import mcjty.theoneprobe.api.ITheOneProbe;

import java.util.function.Function;

public class TOPEntry implements Function<ITheOneProbe, Void> {

    @Override
    public Void apply(ITheOneProbe input) {
        input.registerEntityProvider(new io.github.flemmli97.runecraftory.forge.integration.top.EntityProbeProvider());
        input.registerProvider(new io.github.flemmli97.runecraftory.forge.integration.top.BlockProvider());
        return null;
    }
}

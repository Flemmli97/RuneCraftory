package io.github.flemmli97.runecraftory.fabric.mixin;

import net.minecraft.world.damagesource.DamageSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(DamageSource.class)
public interface DamageSourceAccessor {

    @Invoker("bypassArmor")
    DamageSource setBypassArmor();

    @Invoker("bypassMagic")
    DamageSource setBypassMagic();
}

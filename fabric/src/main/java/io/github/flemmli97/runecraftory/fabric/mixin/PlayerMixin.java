package io.github.flemmli97.runecraftory.fabric.mixin;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.attachment.player.PlayerData;
import io.github.flemmli97.runecraftory.common.events.EntityCalls;
import io.github.flemmli97.runecraftory.fabric.mixinhelper.PlayerDataGetter;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public abstract class PlayerMixin implements PlayerDataGetter {

    @Unique
    private final PlayerData runecraftoryPlayerData = new PlayerData();

    @Inject(method = "readAdditionalSaveData", at = @At("RETURN"))
    private void loadData(CompoundTag compound, CallbackInfo info) {
        if (compound.contains(RuneCraftory.MODID + ":data"))
            this.runecraftoryPlayerData.readFromNBT(compound.getCompound(RuneCraftory.MODID + ":data"), (Player) (Object) this);
    }

    @Inject(method = "addAdditionalSaveData", at = @At("RETURN"))
    private void saveData(CompoundTag compound, CallbackInfo info) {
        compound.put(RuneCraftory.MODID + ":data", this.runecraftoryPlayerData.writeToNBT(new CompoundTag(), (Player) (Object) this));
    }

    @ModifyVariable(method = "actuallyHurt", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;getDamageAfterArmorAbsorb(Lnet/minecraft/world/damagesource/DamageSource;F)F"), argsOnly = true)
    private float hurt(float origin, DamageSource source) {
        return EntityCalls.damageCalculation((Player) (Object) this, source, origin);
    }

    @Inject(method = "actuallyHurt", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;setHealth(F)V"))
    private void hurtPost(DamageSource damageSrc, float damageAmount, CallbackInfo info) {
        EntityCalls.postDamage((Player) (Object) this, damageSrc, damageAmount);
    }

    @Override
    public PlayerData getPlayerData() {
        return this.runecraftoryPlayerData;
    }
}
